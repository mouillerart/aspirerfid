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

package org.ow2.aspirerfid.device.serialcommunicator.impl;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.ow2.aspirerfid.device.serialcommunicator.SerialCommunicator;
import org.ow2.aspirerfid.device.serialcommunicator.SerialParameters;

/**
 * This class implements the {@SerialCommunicator} interface.
 * @author Francois Fornaciari
 */
public class SerialCommunicatorImpl implements SerialCommunicator {
    
    /**
     * @see com.my.serial.communicator.SerialCommunicator#connect(com.my.serial.communicator.SerialParameters, java.lang.String, int)
     */
    public SerialPort connect(final SerialParameters serialParameters, final String owner, final int timout)
            throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException {

        CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(serialParameters.getPortName());
        SerialPort serialPort = (SerialPort) portId.open(owner, timout);

        serialPort.setSerialPortParams(serialParameters.getBaudRate(), serialParameters.getDatabits(), serialParameters
                .getStopbits(), serialParameters.getParity());

        return serialPort;
    }

    /**
     * @see com.my.serial.communicator.SerialCommunicator#disconnect(javax.comm.SerialPort)
     */
    public void disconnect(final SerialPort serialPort) {
        serialPort.close();
    }

    /**
     * @see com.my.serial.communicator.SerialCommunicator#getSerialPortNames()
     */
    public List<String> getSerialPortNames() {
        List<String> serialPortNames = new ArrayList<String>();
        Enumeration<?> portIds = CommPortIdentifier.getPortIdentifiers();

        while (portIds.hasMoreElements()) {
            CommPortIdentifier portId = (CommPortIdentifier) portIds.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                serialPortNames.add(portId.getName());
            }
        }
        return serialPortNames;
    }

    public String readSerialPort(SerialPort serialPort) throws IOException {
        InputStream inputStream = serialPort.getInputStream();
        StringBuffer result = new StringBuffer();
        
        int read;
        do {
            read = inputStream.read();
            if (read != 13 && read != 10 && read != -1) {
                result.append((char) read);
            }
        } while (read != -1);


        return result.toString().trim();
    }

    public void writeSerialPort(SerialPort serialPort, byte[] data) throws IOException {
        OutputStream outputStream = serialPort.getOutputStream();
        outputStream.write(data);
        outputStream.flush();
    }  

}
