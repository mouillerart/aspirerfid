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
package org.ow2.aspirerfid.bluetooth.test;

import java.io.IOException;

import org.ow2.aspirerfid.bluetooth.BluetoothServerService;
import org.ow2.aspirerfid.bluetooth.CommunicationListener;

/**
 * Bluetooth server service demonstration
 * 
 * @author Thomas Calmant
 */
public class BTSActivator implements CommunicationListener {

	/** Bluetooth server service instance */
	private BluetoothServerService m_server;

	/**
	 * iPOJO entry point. Starts the server
	 */
	public void start() {
		// Set the default values for
		try {
			m_server.prepareServer(getClass().getResourceAsStream(
					"/bluetooth.xml"));
		} catch (IOException e) {
			System.err.println("Error reading the configuration file.");
			e.printStackTrace();

			m_server.prepareServer();
		}

		// Subscribe this bundle
		m_server.addCommunicationListener(this);
		System.out.println("Server prepared.");

		// Starts the server
		m_server.startServer();
		System.out.println("Server started.");
	}

	/**
	 * iPOJO exit point. Server is already stopped at this point
	 */
	public void stop() {
		System.out.println("Server stopped.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.aspirerfid.bluetooth.CommunicationListener#commBegin(java.lang
	 * .String)
	 */
	public void commBegin(String logicalName) {
		System.out
				.println("[BTTest] Communication started with " + logicalName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.aspirerfid.bluetooth.CommunicationListener#commEnd(java.lang.
	 * String)
	 */
	public void commEnd(String logicalName) {
		System.out.println("[BTTest] Communication ended with " + logicalName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.aspirerfid.bluetooth.CommunicationListener#commRead(java.lang
	 * .String, java.lang.String)
	 */
	public void commRead(String logicalName, String data) {
		System.out.println("[BTTest] " + logicalName + " -> " + data);
	}
}
