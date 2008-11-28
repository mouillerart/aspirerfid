/**
 * Copyright 2008, Aspire
 * 
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation (the "LGPL"); either version 2.1 of the 
 * License, or (at your option) any later version. If you do not alter this 
 * notice, a recipient may use your version of this file under either the 
 * LGPL version 2.1, or (at his option) any later version.
 * 
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this library; if not, write to the Free Software Foundation, 
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY 
 * KIND, either express or implied. See the GNU Lesser General Public 
 * License for the specific language governing rights and limitations.
 *
 * --------------------------------------------------------------------------
 * $Id$
 * --------------------------------------------------------------------------
 */

package org.ow2.aspirerfid.device.ledscreen.impl;

import gnu.io.SerialPort;

import org.ow2.aspirerfid.device.ledscreen.LedScreenMessage;
import org.ow2.aspirerfid.device.ledscreen.LedScreenService;
import org.ow2.aspirerfid.device.serialcommunicator.SerialCommunicator;
import org.ow2.aspirerfid.device.serialcommunicator.SerialParameters;

/**
 * Implementation of the @{LedScreenService}. 
 * This object allows to configure the led screen device through the serial protocol.
 * @author Francois Fornaciari
 */
public class LedScreenImpl implements LedScreenService {

	/**
	 * Reference to the @{SerialCommunicator} service.
	 */
	private SerialCommunicator serialCommunicator = null;

	/**
	 * Current serial port instance.
	 */
	private SerialPort serialPort = null;

	/**
	 * Constructor.
	 * @param serialCommunicator
	 */
	public LedScreenImpl(final SerialCommunicator serialCommunicator) {
		this.serialCommunicator = serialCommunicator;
	}

	public void configure(final String portId, final LedScreenMessage message) {
		SerialParameters sp = new SerialParameters();
		sp.setPortName(portId);
		sp.setBaudRate(1200);
		try {
			serialPort = serialCommunicator.connect(sp, "jonas", 1000);
			
			serialCommunicator.writeSerialPort(serialPort, message.hello());
			serialCommunicator.writeSerialPort(serialPort, message.configuration());
			
			for (int i = 0; i < 8; i++) {
				serialCommunicator.writeSerialPort(serialPort, message.text(i));
			}
		} catch (Exception e) {
			System.out.println("Led screen device not connected");
		} finally {
		    serialCommunicator.disconnect(serialPort);
		}
	}

	
}
