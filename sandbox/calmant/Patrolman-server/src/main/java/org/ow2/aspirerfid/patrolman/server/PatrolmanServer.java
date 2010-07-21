package org.ow2.aspirerfid.patrolman.server;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.ow2.aspirerfid.sandbox.calmant.bluetooth.BluetoothCommunication;
import org.ow2.aspirerfid.sandbox.calmant.bluetooth.BluetoothServerService;
import org.ow2.aspirerfid.sandbox.calmant.bluetooth.CommunicationListener;

public class PatrolmanServer implements CommunicationListener {
	/** ECSpec file */
	public static final String ECSPEC_FILE = "/res/ECSpec.xml";

	/** Server configuration file */
	public static final String BT_SETTINGS = "/res/bluetoothConfig.xml";

	/** BlueTooth server service */
	private BluetoothServerService m_server;

	/** Current reading state */
	private HashMap<String, String> m_readingClients;

	/**
	 * Bundle starting point
	 */
	public void start() {
		m_readingClients = new HashMap<String, String>();

		if (!m_server.isRunning()) {
			try {
				m_server.prepareServer(PatrolmanServer.class
						.getResourceAsStream(BT_SETTINGS));
			} catch (IOException e) {
				System.err
						.println("[Patrolman] Error reading configuration file");
				e.printStackTrace(System.err);
			}
			new Thread(m_server).start();
			System.out.println("Server started.");
		}

		m_server.addCommunicationListener(this);
		System.out.println("Waiting for clients");
	}

	/**
	 * Bundle closing point
	 */
	public void stop() {
		m_readingClients.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.aspirerfid.sandbox.calmant.bluetooth.CommunicationListener#commBegin
	 * (java.lang.String)
	 */
	public void commBegin(String logicalName) {
		System.out.println("[Patrolman] Communication started with "
				+ logicalName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.aspirerfid.sandbox.calmant.bluetooth.CommunicationListener#commEnd
	 * (java.lang.String)
	 */
	public void commEnd(String logicalName) {
		System.out.println("[Patrolman] Communication ended with "
				+ logicalName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.aspirerfid.sandbox.calmant.bluetooth.CommunicationListener#commRead
	 * (java.lang.String, java.lang.String)
	 */
	public void commRead(String logicalName, String data) {

		if (m_readingClients.containsKey(logicalName)) {
			// Reading data from this client
			String xmlData = m_readingClients.get(logicalName) + data;

			// End of XML transmission
			if (data.contains("</ale:ECReports>")) {
				m_readingClients.remove(logicalName);

				System.out.println("ECReport from " + logicalName + " :");
				System.out.println(xmlData);
			} else {
				// Data...
				m_readingClients.put(logicalName, xmlData);
			}
		} else if (data.equals("refresh")) {
			// Ask for ECSpec
			refresh(m_server.getCommunication(logicalName));
		} else if (data.startsWith("<?xml")) {
			// Beginning of a XML file transfer
			m_readingClients.put(logicalName, "");
		}
	}

	/**
	 * Sends an ECSpec XML file to the client
	 * 
	 * @param comm
	 *            The communication object to the client
	 */
	private void refresh(BluetoothCommunication comm) {
		if (comm == null || comm.getDataOutputStream() == null)
			System.err.println("[Patrolman] No valid communication");
		
		System.out.println("[Patrolman] Sending ECSpec to "
				+ comm.getLogicalName());

		// Read the test XML file
		StringBuffer xmlData = new StringBuffer();
		InputStream is = PatrolmanServer.class.getResourceAsStream(ECSPEC_FILE);
		DataInputStream dis = new DataInputStream(is);
		if (dis == null || is == null) {
			System.err.println("[Patrolman] Test file not found.");
			return;
		}

		try {
			while (true) {
				xmlData.append((char) dis.readByte());
			}
		} catch (EOFException e) {
			// do nothing
		} catch (IOException e) {
			System.err.println("[Patrolman] IOError : " + e.getMessage());
			return;
		}

		// Send it to the client
		try {
			comm.getDataOutputStream().writeUTF(xmlData.toString());
			System.out.println("[Patrolman] ECSpec sent");
		} catch (IOException e) {
			System.err.println("[Patrolman] Error sending data to client :");
			e.printStackTrace(System.err);
		}
	}
}
