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

/**
 * Interface that must be implemented by all classes accessing data through the
 * BlueTooth server.
 * 
 * @author Thomas Calmant
 * 
 */
public interface CommunicationListener {
	/**
	 * A connection has been done, the communication can begin.
	 * 
	 * @param logicalName
	 *            The logical name of the new device.
	 */
	public void commBegin(String logicalName);

	/**
	 * Some data has been read from a device, identified by its logical name
	 * 
	 * @param logicalName
	 *            The data source device
	 * @param data
	 *            Data read from the device
	 */
	public void commRead(String logicalName, String data);

	/**
	 * A connection has been closed (mainly via a connection error).
	 * 
	 * @param logicalName
	 *            Logical name of the lost device
	 */
	public void commEnd(String logicalName);
}
