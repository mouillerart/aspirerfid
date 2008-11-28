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

package org.ow2.aspirerfid.device.thermometerdongle.impl;

import gnu.io.SerialPort;

import org.ow2.aspirerfid.device.serialcommunicator.SerialCommunicator;
import org.ow2.aspirerfid.device.serialcommunicator.SerialParameters;
import org.ow2.aspirerfid.device.thermometerdongle.ThermometerService;

/**
 * Implementation of the @{ThermometerService}.
 * This object access to the thermometer device through the serial protocol.
 * The code of this driver was extracted and converted from the TEMPer6.1 driver.
 * @author Francois Fornaciari
 */
public class ThermometerImpl implements ThermometerService {

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
    public ThermometerImpl(SerialCommunicator serialCommunicator) {
        this.serialCommunicator = serialCommunicator;
    }

    public synchronized double getTemperature(final String portId) {
        SerialParameters sp = new SerialParameters();
        sp.setPortName(portId);
        try {
            serialPort = serialCommunicator.connect(sp, "thermometer", 1000);
            
            SDout((byte) 1);
            SDin();
            SDout((byte) 0);
            SDin();
            Start_IIC();
            SDout((byte) 1);
            HiLowSCLK();
            SDout((byte) 0);
            HiLowSCLK();
            SDout((byte) 0);
            HiLowSCLK();
            SDout((byte) 1);
            HiLowSCLK();
            SDout((byte) 1);
            HiLowSCLK();
            SDout((byte) 1);
            HiLowSCLK();
            SDout((byte) 1);
            HiLowSCLK();
            SDout((byte) 1);
            HiLowSCLK();
            Sclk((byte) 1);
            HiLowSCLK();

            StringBuffer data = new StringBuffer(16);
            for (int i = 0; i < 8; i++) {
                data.append(Byte.toString(SDin()));
                HiLowSCLK();
            }

            for (int i = 0; i < 8; i++) {
                HiLowSCLK();
                data.append(Byte.toString(SDin()));
            }

            Sclk((byte) 0);

            double num1 = Integer.parseInt(data.substring(0, 4), 2);
            double num2 = Integer.parseInt(data.substring(4, 12), 2);
            double num3 = (num1 * 256.0) + num2;

            return (num3 * 0.0625);

        } catch (Exception e) {
            return 0;
        } finally {
            serialCommunicator.disconnect(this.serialPort);
        }

    }

    private void Sclk(byte ad01) {
        if (ad01 == 0) {
            serialPort.setDTR(false);
        }
        if (ad01 == 1) {
            serialPort.setDTR(true);
        }
    }

    private void Start_IIC() {
        SDout((byte) 1);
        Sclk((byte) 1);
        SDout((byte) 0);
        Sclk((byte) 0);
    }

    private void HiLowSCLK() {
        Sclk((byte) 1);
        Sclk((byte) 0);
    }

    private void SDout(byte ad01) {
        if (ad01 == 0) {
            serialPort.setRTS(false);
        }
        if (ad01 == 1) {
            serialPort.setRTS(true);
        }
    }

    private byte SDin() {
        SDout((byte) 1);
        if (!serialPort.isCTS()) {
            return 0;
        }
        return 1;
    }
}
