package icommand.nxt.comm;

import java.util.Properties;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Vector;

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

class NXTCommBlueCove implements NXTComm, DiscoveryListener  {
	private StreamConnection con;
	private OutputStream os;
	private InputStream is;

	private String NXTBTAddress;

	// BB Added:
	private static Vector devices, nxtInfos;
	private NXTInfo nxtInfo;
	
	// !! Can probably remove this constructor after changes done.
	public NXTCommBlueCove(Properties props) {
		NXTBTAddress = props.getProperty(NXTCommFactory.BT_ADDRESS);
	}
	
	public NXTCommBlueCove() {
	
	}

	/**
	 * Allows the creation of a NXTCommBlueCove object without any parameters, then the search method
	 * can be used, and then the BT Address can be set later with this method.
	 * @param address The Bluetooth Address, with or without colors. e.g. "00:16:53:04:a5:9E"
	 */
	public void setAddress(String address) {
		NXTBTAddress = stripColons(address);
	}
	
	public void open() throws IOException {

		String btResourceString = "btspp://" + stripColons(NXTBTAddress)
				+ ":1;authenticate=false;encrypt=false";

		try {
			con = (StreamConnection) Connector.open(btResourceString);
			os = con.openOutputStream();
			is = con.openInputStream();
		} catch (IOException e) {
			System.err.println("Open of NXT failed: " + e.getMessage());
			throw e;
		}
	}

	public void close() {
		try {
			if (os != null) {
				os.close();
			}
			if (is != null) {
				is.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void sendData(byte[] request) {

		// length of packet (Least and Most significant byte)
		// * NOTE: Bluetooth only. If do USB, doesn't need it.
		int LSB = request.length;
		int MSB = request.length >>> 8;

		try {
			// Send length of packet:
			os.write((byte) LSB);
			os.write((byte) MSB);

			os.write(request);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public byte[] readData() {

		byte[] reply = null;
		int length = -1;

		try {
		do {
			length = is.read(); // First byte specifies length of packet.
		} while (length < 0);

		int lengthMSB = is.read(); // Most Significant Byte value
		length = (0xFF & length) | ((0xFF & lengthMSB) << 8);
		reply = new byte[length];
		is.read(reply);

		} catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		return reply;
	}

	private String stripColons(String s) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			if (c != ':') {
				sb.append(c);
			}
		}

		return sb.toString();
	}
	
public NXTInfo[] search(String name, int protocol) {
		
		devices = new Vector();
        nxtInfos = new Vector();

        if ((protocol | NXTCommFactory.BLUETOOTH) == 0) return new NXTInfo[0];

		synchronized (this) {
			try {
				LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, this);
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} catch (BluetoothStateException e) {
				e.printStackTrace();
			}
		}

		for (Enumeration enum_d = devices.elements(); enum_d.hasMoreElements();) {
			RemoteDevice d = (RemoteDevice) enum_d.nextElement();

			try {	
                nxtInfo = new NXTInfo();

                nxtInfo.name = d.getFriendlyName(false);
                if (nxtInfo.name == null || nxtInfo.name.length() == 0)
                	nxtInfo.name = "Unknown";
				nxtInfo.btDeviceAddress = d.getBluetoothAddress();
                nxtInfo.protocol = NXTCommFactory.BLUETOOTH;

                if (name == null || name.equals(nxtInfo.name)) nxtInfos.addElement(nxtInfo);
				else continue;

                System.out.println("Found: " + nxtInfo.name);
                
      	 		// We want additional attributes, ServiceName (0x100),
    	 		// ServiceDescription (0x101) and ProviderName (0x102).  				

				int[] attributes = {0x100,0x101,0x102};
	
				UUID[] uuids = new UUID[1];
       			uuids[0] = new UUID("1101",true); // Serial Port
    			synchronized (this) {
    				try {
						LocalDevice.getLocalDevice().getDiscoveryAgent().searchServices(attributes,uuids,d,this);
						try {
							wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} catch (BluetoothStateException e) {
					}
				}

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
        NXTInfo[] nxts = new NXTInfo[nxtInfos.size()];
        for(int i=0;i<nxts.length;i++) nxts[i] = (NXTInfo) nxtInfos.elementAt(i);
        return nxts;
	}

	public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
	    //System.out.println(servRecord.length + " service(s) discovered");
	    // Should only be one service on a NXT
	    if (servRecord.length != 1) return;
	    nxtInfo.btResourceString = servRecord[0].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
	    //System.out.println("Setting url to : " + nxtInfo.btResourceString);
	}

	public synchronized void serviceSearchCompleted(int transID, int respCode) {
		//System.out.println("Service search completed: respCode = " + respCode);
		notifyAll();
	}
	
	public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
        //System.out.println("Found Device,  class: " + cod.getMajorDeviceClass() + "/" + cod.getMinorDeviceClass());
		if (cod.getMajorDeviceClass() == 2048 && cod.getMinorDeviceClass() == 4)
			devices.addElement(btDevice);
	}
	
	public synchronized void inquiryCompleted(int discType) {		
        //if (discType == INQUIRY_COMPLETED) System.out.println("Inquiry completed");
        //else System.out.println("Inquiry Failed");
		notifyAll();
	}
}
