/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.liglab.adele.sunspot.controller;

import com.sun.spot.io.j2me.radiogram.Radiogram;
import com.sun.spot.io.j2me.radiogram.RadiogramConnection;
import com.sun.spot.sensorboard.EDemoBoard;
import com.sun.spot.sensorboard.peripheral.ITriColorLED;
import com.sun.spot.sensorboard.peripheral.LEDColor;
import com.sun.spot.util.Utils;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.Datagram;



/**
 *
 * @author lionel
 */
public class SPOTControlHandler implements Runnable {

    private String targetAddress;
    
    private int communicationPort;
    private RadiogramConnection rCon;
    
    private boolean end;
    
    private ITriColorLED leds[] = EDemoBoard.getInstance ().getLEDs();
    
    public SPOTControlHandler(String address, int port) {
        targetAddress = address;
        communicationPort = port;
        try {
            // open a connection to communicate with the remote base station
            rCon = (RadiogramConnection) Connector.open("radiogram://"+targetAddress+":"+communicationPort);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void stop(){
        try {
            end = true;
            rCon.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void run() {
        end = false;
        Datagram dg = null;
        try {
            dg = rCon.newDatagram(PacketTypes.DEFAULT_DATAGRAM_SIZE);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        while (!end) {
            try {
                // listen for incomming commands
                rCon.receive(dg);
                byte cmd = dg.readByte();
                
                switch (cmd) {
                    case PacketTypes.BLINK_LEDS_REQ :
                        blinkLEDs();
                        break;
                }
                
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            
        }

    }
    
    // handle Blink LEDs command
    private void blinkLEDs() {
        for (int i = 0; i < 10; i++) {          // blink LEDs 10 times = 10 seconds
            for (int j = 0; j < 8; j++) {
                leds[j].setColor(LEDColor.MAGENTA);
                leds[j].setOn();
            }
            Utils.sleep(250);
            for (int j = 0; j < 8; j++) {
                leds[j].setOff();
            }
            Utils.sleep(750);
        }
    }

}
