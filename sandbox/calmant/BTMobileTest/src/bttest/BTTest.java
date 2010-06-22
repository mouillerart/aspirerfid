package bttest;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class BTTest extends MIDlet implements DiscoveryListener {
	private static final UUID RFCOMM_UUID = new UUID(
			"9ee2b296da2448399658cbc0a7ee69e2", false);

	private TextBox str;

	private LocalDevice m_localDevice;
	private DiscoveryAgent m_discoveryAgent;
	private boolean m_serverFound;
	private String m_url;
	private StreamConnection m_conn;
	
	private static Object lock = new Object();

	public BTTest() {
		str = new TextBox("Info", "", 2048, TextField.ANY);
		m_serverFound = false;
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		if (m_conn != null)
			try {
				m_conn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	protected void pauseApp() {
	}

	protected void startApp() throws MIDletStateChangeException {
		Display.getDisplay(this).setCurrent(str);
		log("Started.");

		searchDevices();
		
		synchronized (lock) {
			try {
				lock.wait();
			} catch (InterruptedException e) {
				log("InterruptedException");
			}
		}
		
		trySend(m_url);
	}

	public void searchDevices() {
		try {
			m_localDevice = LocalDevice.getLocalDevice();
			m_discoveryAgent = m_localDevice.getDiscoveryAgent();

			m_discoveryAgent.startInquiry(DiscoveryAgent.GIAC, this);
			log("Searching...");
		} catch (BluetoothStateException error) {
			System.err.println("Device search error");
			error.printStackTrace();
			log("Device search error");

			Alert alert = new Alert("Error", "Device search error", null,
					AlertType.ERROR);
			alert.setTimeout(2000);
		}
	}

	public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
		try {
			// Device information
			/*
			 * System.out.println("Major Device Class and information : " +
			 * cod.getMajorDeviceClass() + " Minor Device Class: " +
			 * cod.getMinorDeviceClass());
			 * System.out.println("Bluetooth Address of the device: " +
			 * btDevice.getBluetoothAddress());
			 * System.out.println("Friendly Name: " +
			 * btDevice.getFriendlyName(false));
			 */
			log("Device found : ");
			log(btDevice.getFriendlyName(false));
			log(btDevice.getBluetoothAddress() + "\n");

			// Now its our responsibility to search its services
			/*
			 * UUID uuidSet[] = new UUID[1]; uuidSet[0] = RFCOMM_UUID;
			 * m_discoveryAgent.searchServices(null, uuidSet, btDevice, this);
			 */

			final RemoteDevice remoteDev = btDevice;
			new Thread(new Runnable() {

				public void run() {
					try {
						UUID uuidSet[] = new UUID[1];
						uuidSet[0] = RFCOMM_UUID;
						m_discoveryAgent.searchServices(null, uuidSet,
								remoteDev, BTTest.this);
					} catch (BluetoothStateException e) {
						log("SelectService error : " + e);
					}
				}
			}).start();
			log("Service search started...");

		} catch (Exception e) {
			System.out.println("Device Discovered Error: " + e);
			log("Device Discovered Error: " + e);
		}

	}

	public void inquiryCompleted(int discType) {
		if (!m_serverFound) {
			log("Inquiry finished. No server found yet.");
			return;
		}
	}

	public void serviceSearchCompleted(int transID, int respCode) {
		if (!m_serverFound) {
			log("Server not found (Service)");
			return;
		}

		synchronized (lock) {
			lock.notify();
		}
	}

	public void trySend(String url) {
		try {
			m_conn = (StreamConnection) Connector.open(url);
			log("Connection done");
		} catch (IOException e) {
			e.printStackTrace();
			log("Connection error : " + e);
		} catch (Exception e) {
			log("Greater Error 1 : " + e);
		}

		try {
			String test_msg = "Test !\nAvec saut de ligne...\n";
			DataOutputStream dos = m_conn.openDataOutputStream();
			Thread.sleep(1500);
			dos.write(test_msg.getBytes());
			dos.flush();
			dos.writeUTF("UTF: " + test_msg);
			dos.flush();
			log("Write done");
			
			dos.close();
		} catch (IOException e) {
			e.printStackTrace();
			log("Send error : " + e);
		} catch (Exception e) {
			log("Greater Error 2 : " + e);
		}
	}

	public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
		for (int i = 0; i < servRecord.length; i++) {
			String strUrl = servRecord[i].getConnectionURL(
					ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);

			log("Last url : " + strUrl);
			System.out.println(strUrl);
			// we have found our service protocol
			if (strUrl.startsWith("btspp")) {
				m_serverFound = true;
				m_url = strUrl;
				str.setString("Server found");
				break;
			}
		}
	}

	public void log(String msg) {
		String data = str.getString();
		data += "\n> ";
		data += msg;

		str.setString(data);
	}

}
