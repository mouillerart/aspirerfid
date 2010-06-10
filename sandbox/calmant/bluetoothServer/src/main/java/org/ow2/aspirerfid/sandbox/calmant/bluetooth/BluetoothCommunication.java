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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.UUID;

import javax.microedition.io.StreamConnection;

public class BluetoothCommunication implements Runnable {
	/** Stream to read messages */
	private DataInputStream m_inputStream;
	/** Stream to send messages */
	private DataOutputStream m_outputStream;

	/** Parent BlueTooth server */
	private CommunicationListener m_parentListener;

	/** Loop controller (true to stop reading) */
	private boolean m_stop;

	/** Client logical name (ID) */
	private String m_logicalName;

	/**
	 * Prepares a thread handling a client connection.
	 * 
	 * @param server
	 *            The server which has accepted the current client.
	 * @param connection
	 *            A connected BlueTooth client (RFCOMM)
	 * @throws IOException
	 *             An error occurred during the opening of data streams.
	 * 
	 * @see StreamConnection#openDataInputStream()
	 * @see StreamConnection#openDataOutputStream()
	 */
	public BluetoothCommunication(CommunicationListener server,
			StreamConnection connection) throws IOException {
		m_parentListener = server;
		m_inputStream = connection.openDataInputStream();
		m_outputStream = connection.openDataOutputStream();

		// Generate the logical name
		m_logicalName = UUID.randomUUID().toString();
	}
	
	/**
	 * Retrieves the auto-generated logical name of this communication
	 * @return The logical name associated to the communication
	 */
	public String getLogicalName()
	{
		return m_logicalName;
	}

	/**
	 * Reads the client data. Advise the server when correct data has been read,
	 * or when an exception occurred
	 */
	public void run() {
		m_stop = false;

		try {
			/*
			 * Reading data with readByte allow non-java, non-UTF programs to
			 * communicate with the server.
			 * 
			 * A better java-only way would be to use readUTF, because it allows
			 * us to
			 * 
			 * The use of a BufferedReader, and the readLine() method may be the
			 * best way to work, in the UTF world.
			 */
			while (!m_stop) {
				// String data = m_inputStream.readUTF();

				// Read data line by line
				String data = "";
				byte read;
				while ((read = m_inputStream.readByte()) != '\n')
					data += (char) read;

				m_parentListener.commRead(m_logicalName, data);
			}
		} catch (EOFException error) {
			/*
			 * The client has ended the connection, it's not a real error, so we
			 * show nothing special about the error. Let the finally block
			 * handle the event.
			 */
		} catch (InterruptedIOException error) {
			/*
			 * Interruption due to the server death (by a call to stop()) Show
			 * nothing, it's not a real error. Let the finally block handle the
			 * event.
			 */
		} catch (IOException error) {
			/*
			 * Prints the error only if the server is running. Avoid the
			 * exception raised by closing the input stream in stop().
			 */
			if (!m_stop) {
				System.err.println("Error reading client data from '"
						+ m_logicalName + "'");
				error.printStackTrace(System.err);
			}
		} finally {
			stop();
			m_parentListener.commEnd(m_logicalName);
		}
	}

	/**
	 * Stops this communication. Closes streams.
	 */
	public void stop() {
		m_stop = true;

		try {
			m_inputStream.close();
			m_outputStream.close();
		} catch (IOException error) {
			/*
			 * Errors at this point can be ignored, the communication is
			 * finished.
			 */
		}
	}
}
