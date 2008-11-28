/**
 * JOnAS: Java(TM) Open Application Server
 * Copyright (C) 2008 Bull S.A.S.
 * Contact: jonas-team@ow2.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
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
