/*
 *  Copyright (C) Aspire
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.ow2.aspirerfid.sandbox.calmant.bluetooth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

/**
 * The bluetooth server itself.
 * 
 * Sample :
 * 
 * <pre>
 * server = new BluetoothServer();
 * // Configure the server
 * server.prepareServer()
 * // Wait for clients
 * new Thread(server).start();
 * </pre>
 * 
 * @author Thomas Calmant
 */
public class BluetoothServer implements BluetoothServerService,
		CommunicationListener {
	/** Server configuration */
	private final BluetoothSettings m_properties;

	/** Current 'valid' communications */
	private Map<String, BluetoothCommunication> m_clients;

	/** Set it to true to stop the server 'gracefully' */
	private boolean m_stop;

	/**
	 * The communications thread pool, avoiding to have a large amount of client
	 * threads
	 */
	private ExecutorService m_threadPool;

	/** The Bluetooth client acceptor */
	private StreamConnectionNotifier m_connectionNotifier;

	/** Bluetooth server URL */
	private String m_url;

	/** The communication events subscribers */
	private Vector<CommunicationListener> m_commListeners;

	/**
	 * Prepares the thread pool
	 */
	public BluetoothServer() {
		m_clients = new HashMap<String, BluetoothCommunication>();
		m_commListeners = new Vector<CommunicationListener>();

		m_properties = new BluetoothSettings();

		int nbThreads;
		try {
			nbThreads = Integer.parseInt(m_properties
					.getProperty(BluetoothSettings.MAX_THREADS));
		} catch (NumberFormatException e) {
			nbThreads = 2;
			System.err
					.println("Error reading max thread count. Using default value : "
							+ nbThreads);
		}

		/*
		 * The working threads are using blocking calls (DataInputStream),
		 * so when all available threads are working and waiting for data,
		 * the thread pool let new client threads in queue, until a working one stops.
		 * 
		 * Using a cached thread pool bypass this problem, by creating one thread per client,
		 * but don't allow to control how many threads are running.
		 * 
		 * It may be sufficient for a little amount of clients (maybe 5-10), but could cause
		 * problems if it grows significantly. In this case, we should rewrite the worker thread,
		 * to be stopped when waiting for data, and reactivated when data is available
		 * (select() like behavior).
		 */
		// m_threadPool = Executors.newFixedThreadPool(nbThreads);
		m_threadPool = Executors.newCachedThreadPool();
	}

	/**
	 * Client acceptance loop. May be used in a thread
	 */
	public void run() {
		try {
			// Publish the service and waits for connections.
			System.out.println("Start advertising service...");
			// L2CAP returns L2CAPConnectionNotifier, RFCOMM returns
			// StreamConnectionNotifier and OBEX returns ClientSession.
			m_connectionNotifier = (StreamConnectionNotifier) Connector
					.open(m_url);
			
			while (!m_stop) {	
				System.out.println("Waiting for incoming connection...");

				// Inserts the service record into the SDDB and wait for
				// incoming
				// connections.
				StreamConnection client_connection = m_connectionNotifier
						.acceptAndOpen();
				// Errors opening the communication streams are not critical
				try {
					BluetoothCommunication client_communication = new BluetoothCommunication(
							this, client_connection);

					m_clients.put(client_communication.getLogicalName(),
							client_communication);

					System.out.println("New client connected");

					// Add the communication in the pool
					m_threadPool.execute(client_communication);
					
					// Advise all listeners
					commBegin(client_communication.getLogicalName());
				} catch (IOException error) {
					System.err
							.println("Error creating the communication stream.");
					error.printStackTrace();
				}
			}
		} catch (IOException error) {
			error.printStackTrace();
		} finally {
			stop();
		}
	}

	/**
	 * Starts the bluetooth server and set it discoverable. May need root rights
	 * under Linux (or set the device discoverable in the system configuration)
	 */
	public void prepareServer() {
		int inquiry_mode;
		try {
			inquiry_mode = Integer.parseInt(m_properties
					.getProperty(BluetoothSettings.INQUIRY_MODE));
		} catch (NumberFormatException e) {
			inquiry_mode = DiscoveryAgent.GIAC;
			System.err.println("Error reading inquiry mode, using default : "
					+ inquiry_mode);
		}

		try {
			LocalDevice localDevice = LocalDevice.getLocalDevice();
			System.out.println("Bluetooth address "
					+ localDevice.getBluetoothAddress());

			/*
			 * Sets the local device visible to others devices. The mode can
			 * be GIAC that is undefined in time. In the other hand, the LIAC
			 * mode is limited in time, usually one minute.
			 * 
			 * Under Linux, this method needs root rights.
			 * This restriction can be bypassed by setting the bluetooth device
			 * discoverable in the system settings. 
			 */
			System.out.println("Setting device to be discoverable...");
			localDevice.setDiscoverable(inquiry_mode);

			m_stop = false;
		} catch (BluetoothStateException e) {
			throw new RuntimeException("Invalid bluetooth state: "
					+ e.getMessage());
		}

		// URL preparation
		String uuidString = m_properties.getProperty(BluetoothSettings.UUID);
		String name = m_properties
				.getProperty(BluetoothSettings.SERVICE_NAME);
		String authenticate = m_properties
				.getProperty(BluetoothSettings.AUTHENTICATE);
		String encrypt = m_properties.getProperty(BluetoothSettings.ENCRYPT);

		// UUID
		UUID uuid = new UUID(uuidString, false);

		// Connection's parameters
		String params = ";name=" + name + ";authenticate=" + authenticate
				+ ";encrypt=" + encrypt + ";";

		// The service url. The protocol used is RFCOMM (btspp://). Other
		// protocols are L2CAP (btl2cap://) and OBEX
		// (btgoep://localhost:uuid)
		m_url = "btspp://localhost:" + uuid + params;
	}

	/**
	 * Stops the server.
	 */
	public void stop() {
		// End the loop
		m_stop = true;

		// Stop all communications
		for (BluetoothCommunication comm : m_clients.values())
			comm.stop();

		// Kill clients threads
		m_threadPool.shutdownNow();

		// Gracefully stop the Bluetooth parts
		try {
			m_connectionNotifier.close();
		} catch (IOException e) {
			// Don't worry about that...
		}
	}

	/*
	 * Communication events dispatching stuff...
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.ow2.aspirerfid.nfc.server.bluetooth.v2.BluetoothServerService#
	 * addCommunicationListener
	 * (org.ow2.aspirerfid.nfc.server.bluetooth.v2.CommunicationListener)
	 */
	public void addCommunicationListener(CommunicationListener comm) {
		// We must avoid infinite loop by calling communication events on the
		// event dispatcher
		if (comm != null && !(comm instanceof BluetoothServer))
			m_commListeners.add(comm);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.ow2.aspirerfid.nfc.server.bluetooth.v2.BluetoothServerService#
	 * getCommunication(java.lang.String)
	 */
	public BluetoothCommunication getCommunication(String logicalName) {
		if (logicalName == null)
			return null;

		return m_clients.get(logicalName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.ow2.aspirerfid.nfc.server.bluetooth.v2.BluetoothServerService#
	 * removeCommunicationListener
	 * (org.ow2.aspirerfid.nfc.server.bluetooth.v2.CommunicationListener)
	 */
	public void removeCommunicationListener(CommunicationListener comm) {
		m_commListeners.remove(comm);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.aspirerfid.nfc.server.bluetooth.v2.CommunicationListener#commBegin
	 * (java.lang.String)
	 */
	public void commBegin(String logicalName) {
		if (logicalName == null)
			return;

		for (CommunicationListener listener : m_commListeners)
			listener.commBegin(logicalName);

		System.out.println("[BEGC] Communication ready : " + logicalName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.aspirerfid.nfc.server.bluetooth.v2.CommunicationListener#commEnd
	 * (java.lang.String)
	 */
	public void commEnd(String logicalName) {
		if (logicalName == null)
			return;

		for (CommunicationListener listener : m_commListeners)
			listener.commEnd(logicalName);

		m_clients.remove(logicalName);
		System.out.println("[ENDC] Communication closed : " + logicalName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.aspirerfid.nfc.server.bluetooth.v2.CommunicationListener#commRead
	 * (java.lang.String, java.lang.String)
	 */
	public void commRead(String logicalName, String data) {
		if (logicalName == null || data == null)
			return;

		for (CommunicationListener listener : m_commListeners)
			listener.commRead(logicalName, data);

		System.out.println("[DATA] Data received from : " + logicalName + "("
				+ data.length() + "bytes)");
	}
}
