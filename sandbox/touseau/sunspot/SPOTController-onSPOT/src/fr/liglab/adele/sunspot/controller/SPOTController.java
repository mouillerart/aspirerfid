/*
 * SPOTController.java
 *
 * Copyright (c) 2008 Sun Microsystems, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */


package fr.liglab.adele.sunspot.controller;

import com.sun.spot.sensorboard.EDemoBoard;
import com.sun.spot.sensorboard.io.IScalarInput;
import com.sun.spot.io.j2me.radiogram.*;
import com.sun.spot.sensorboard.peripheral.ITriColorLED;
import com.sun.spot.util.BootloaderListener;
import com.sun.spot.util.Utils;
import java.io.IOException;
import javax.microedition.io.*;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * This application is the 'on SPOT' portion of the SendDataDemo. It
 * periodically samples a sensor value on the SPOT and transmits it to
 * a desktop application (the 'on Desktop' portion of the SendDataDemo)
 * where the values are displayed.
 *
 * @author: Vipul Gupta
 * modified: Ron Goldman
 */
public class SPOTController extends MIDlet {

    private static final int HOST_PORT = 50;
    private static final int REPLY_PORT = 51;
    private static final int SAMPLE_PERIOD = 10 * 1000;  // in milliseconds
    
    private static final long HEARTBEAT_PERIOD = 2000;
    
    private RadiogramConnection dgConnection = null;
    
    private boolean running;
    
    protected void startApp() throws MIDletStateChangeException {
        
        new BootloaderListener().start();
            // ALGO
            // Broadcast pour trouver Adresse Host.
            // Broadcast heartbeat toutes les 3 secondes ?
            // Ouvrir connection vers host (2 : R & W )
            // Sur cette connexion on enverra les pressions SW et les valeurs de l'accel.
            // Demarrer un accel monitor qui nous rappelle à chaque dépassement de seuil
            // Enregistrer les listeners de SW1&2
            // Format message : byte pour type commande (SWITCH_PRESSED, ACCEL, SWITCH_RELEASE, )
            // byte pour type SW (1 ou 2) ou double pour valeurs accel
        
        // Open a broadcast connection
        
        System.out.println("MIDLet starting");
        
        try {
            dgConnection = (RadiogramConnection) Connector.open("radiogram://broadcast:"+HOST_PORT);
        } catch (IOException e) {
                    System.out.println("Could not open radiogram connection");
                    e.printStackTrace();
                    return;
        }
        
        running = true;
        startHeartbeat();
        
        startDispatcher();
        
        System.out.println("hb & dispatch started");
        
        while (running) {
            Utils.sleep(5000);
        }
        
    }

    private void startDispatcher() {
        new Thread("ListenningLoop") {
             public void run() {
                
                RadiogramConnection radCon =  null;
                Datagram dg = null;
                
                try {
                    // open a new connection to listen for replies
                    radCon = (RadiogramConnection) Connector.open("radiogram://:"+REPLY_PORT);
                    // Then, we ask for a datagram with a small size
                    dg = radCon.newDatagram(PacketTypes.DEFAULT_DATAGRAM_SIZE);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                
                String spotAddress;
                byte cmdType;
                
                while(running){
                    try {
                        dg.reset();
                        radCon.receive(dg);
                        spotAddress = dg.getAddress();
                        cmdType = dg.readByte();
                        int comPort;
                        
                        switch (cmdType) {
                            case PacketTypes.ADMIN_SVC_CONNECTION :
                                comPort = dg.readInt();
                                SPOTControlHandler ch = new SPOTControlHandler(spotAddress, comPort);
                                new Thread(ch).start();
                                break;
                            case PacketTypes.TEMP_PROD_CONNECTION :
                                comPort = dg.readInt();
                                long sampleDelay = dg.readLong();
                                TemperatureProducer tp = new TemperatureProducer(spotAddress, comPort, sampleDelay);
                                new Thread(tp).start();
                                break;
                            default: break;
                        }
                        
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }.start();
    }
    
    private void startHeartbeat(){
        new Thread("HeartBeat") {
             public void run() {
                
                Datagram dg = null;
                try {
                    // Then, we ask for a datagram with a small size
                    dg = dgConnection.newDatagram(PacketTypes.DEFAULT_DATAGRAM_SIZE);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                while(running){
                    try {
                        dg.reset();
                        dg.writeByte(PacketTypes.HEARTBEAT);
                        dg.writeInt(REPLY_PORT);
                        dgConnection.send(dg);
                        
                        Utils.sleep(HEARTBEAT_PERIOD);
                        
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }.start();
    }
    
    protected void pauseApp() {
        // This will never be called by the Squawk VM
    }
    
    protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
        // Only called if startApp throws any exception other than MIDletStateChangeException
        running = false;
    }
}
