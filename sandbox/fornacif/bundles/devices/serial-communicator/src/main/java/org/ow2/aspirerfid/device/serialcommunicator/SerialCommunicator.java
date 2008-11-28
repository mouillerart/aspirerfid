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

package org.ow2.aspirerfid.device.serialcommunicator;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.util.List;

/**
 * This interface provides basic commands for serial port communications.
 * @author Francois Fornaciari
 */
public interface SerialCommunicator {
    
    /**
     * Connect to a serial device.
     * @param serialParameters Parameters used for the connection
     * @param owner Owner that request the connection
     * @param timout Connection timeout
     * @return The serial port instance representing the connected device
     * @throws NoSuchPortException If the port is not found
     * @throws PortInUseException If the port is already in use
     * @throws UnsupportedCommOperationException If parameters cannot be set
     */
    SerialPort connect(final SerialParameters serialParameters, final String owner, final int timout) throws NoSuchPortException,
            PortInUseException, UnsupportedCommOperationException;

    /**
     * Disconnect from a given serial port.
     * @param serialPort the serial port instance
     */
    void disconnect(final SerialPort serialPort);

    /**
     * Return the list of serial port names.
     * @return The list of serial port names
     */
    List<String> getSerialPortNames();
    
    /**
     * Read the command result of a given serial port.
     * @param serialPort The serial port instance representing the connected device
     * @return The command result
     * @throws IOException If the reading fails
     */
    String readSerialPort(final SerialPort serialPort) throws IOException;
    
    /**
     * Send a byte array to a given serial port.
     * @param serialPort The serial port instance representing the connected device
     * @param data The byte array to send
     * @throws IOException If the writing fails
     */
    void writeSerialPort(final SerialPort serialPort, final byte[] data) throws IOException;
}
