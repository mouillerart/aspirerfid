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

package org.ow2.aspirerfid.bluetooth;

import java.io.IOException;
import java.io.InputStream;

/**
 * Interface of a Bluetooth server, to be used for server control To be
 * complete, a server must implement the CommunicationListener interface, to be
 * able to dialog with BluetoothCommunication.
 * 
 * @author Thomas Calmant
 */
public interface BluetoothServerService extends Runnable {
	/**
	 * Subscribe a new listener for devices events.
	 * 
	 * @param comm
	 *            The new listener
	 */
	public void addCommunicationListener(CommunicationListener comm);

	/**
	 * Retrieve a communication object from its logical name
	 * 
	 * @param logicalName
	 *            The communication identifier (logical name)
	 * @return The communication object, null if non existent.
	 */
	public BluetoothCommunication getCommunication(String logicalName);

	/**
	 * Returns the current server running state
	 * 
	 * @return True if the server thread is currently listening
	 */
	public boolean isRunning();

	/**
	 * Configure the server (UUID, etc) with default configuration file or
	 * default internal configuration
	 */
	public void prepareServer();

	/**
	 * Configure the server (UUID, etc) with the specified input stream or
	 * default internal configuration
	 */
	public void prepareServer(InputStream configurationStream)
			throws IOException;

	/**
	 * Remove a listener from the devices events update list.
	 * 
	 * @param comm
	 *            The listener to be removed
	 */
	public void removeCommunicationListener(CommunicationListener comm);

	/**
	 * Stop the server. Do not accept any new connection. Close all active
	 * connections.
	 */
	public void stop();
}