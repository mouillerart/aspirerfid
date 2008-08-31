/*
 * Copyright 2005-2008, Aspire
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
 */

package org.ow2.aspirerfid.sensor.sunspot;

import com.sun.spot.peripheral.radio.IProprietaryRadio;
import com.sun.spot.peripheral.radio.IRadioPolicyManager;
import com.sun.spot.sensorboard.EDemoBoard;
import com.sun.spot.sensorboard.io.IIOPin;
import com.sun.spot.sensorboard.io.IOutputPin;
import com.sun.spot.sensorboard.peripheral.IAccelerometer3DThresholdListener;
import com.sun.spot.sensorboard.peripheral.ILightSensorThresholdListener;
import com.sun.spot.sensorboard.peripheral.ITemperatureInput;
import com.sun.spot.sensorboard.peripheral.IAccelerometer3D;
import com.sun.spot.sensorboard.peripheral.ITemperatureInputThresholdListener;
import com.sun.spot.sensorboard.peripheral.ITriColorLED;
import com.sun.spot.sensorboard.peripheral.ISwitch;
import com.sun.spot.sensorboard.peripheral.ILightSensor; 
import com.sun.spot.sensorboard.peripheral.LEDColor;
import com.sun.spot.util.Utils;
import com.sun.spot.util.BootloaderListener;

import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.Datagram;
import javax.microedition.io.DatagramConnection;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * @author Didier Donsez, Lionel Touseau
 */ 
public class SunSpotSensorsProducerOnSpot extends MIDlet
        implements IAccelerometer3DThresholdListener, ITemperatureInputThresholdListener, ILightSensorThresholdListener, PacketType {

    
    private static final String VERSION = "1.0";
    private static final int INITIAL_CHANNEL_NUMBER = IProprietaryRadio.DEFAULT_CHANNEL;
    private static final short PAN_ID               = IRadioPolicyManager.DEFAULT_PAN_ID;
    private static final short MAX_BROADCAST_HOPS=1;      // =1 means "don't want packets being rebroadcasted""
    
    // indicators
    private ITriColorLED leds[]          = EDemoBoard.getInstance().getLEDs();

    // outputs
    //private IOutputPin outputPins        = EDemoBoard.getInstance().getOutputPins();

    // sensors
    private ISwitch sw1                  = EDemoBoard.getInstance().getSwitches()[0];  
    private ISwitch sw2                  = EDemoBoard.getInstance().getSwitches()[1];  
    private ILightSensor lightSensor     = EDemoBoard.getInstance().getLightSensor();  
    private ITemperatureInput tempSensor = EDemoBoard.getInstance().getADCTemperature();
    private IAccelerometer3D accelSensor = EDemoBoard.getInstance().getAccelerometer();
    //private IIOPin ioPins                = EDemoBoard.getInstance().getIOPins();
    
    // configuration parameters
    private boolean indicatorOnFlag = true;
    private boolean resetCountersAfterEmission = true;
    private long emissionPeriod = 1000;
    private double accelerationXThreshold = 20.0d;
    private double accelerationYThreshold = 20.0d;
    private double accelerationZThreshold = 20.0d;
    private double lightMinThreshold = 20.0d;
    private double lightMaxThreshold = 20.0d;
    private double temperatureMinThreshold = 273.0d;
    private double temperatureMaxThreshold = 293.0d;
    private boolean emissionOnClosedSW1 = true;
    private boolean emissionOnOpenedSW1 = true;
    private boolean emissionOnClosedSW2 = true;
    private boolean emissionOnOpenedSW2 = true;
    private boolean alertThresholdsOnLED = true;
    // private double batteryMinThreshold = 20.0d;

    // accumulators and counters
    private long startingTimestamp = 0;
    private int accelerationXThresholdCounter = 0;
    private int accelerationYThresholdCounter = 0;
    private int accelerationZThresholdCounter = 0;
    private int lightMinThresholdCounter = 0;
    private int lightMaxThresholdCounter = 0;
    private int temperatureMinThresholdCounter = 0;
    private int temperatureMaxThresholdCounter = 0;
    private int closedSW1Counter = 0;
    private int openedSW1Counter = 0;
    private int closedSW2Counter = 0;
    private int openedSW2Counter = 0;
    
    
    
    
    
    private void initIndicator(){
        for(int i = 0; i < leds.length; i++){
            leds[i].setOn();                    // Enable this LED
            leds[i].setRGB(255,255,255);              // Set it to black
        }
        resetIndicator();
    }

    private void resetIndicator(){
        for(int i = 0; i < leds.length; i++){
            leds[i].setOn();                    // Enable this LED
            leds[i].setRGB(0,0,0);              // Set it to black
        }
        leds[0].setColor(LEDColor.TURQUOISE);   // See LEDColor for more predefined colors.
    }
    
    private void accelIndicator(double accelerationValue){
        leds[0].setRGB(0, (int) (accelerationValue * accelerationValue * 60.0), 0);  // [0 : 240]
    }
   
    private void lightIndicator(){
        // TODO
    }
    
    private void heatIndicator() throws IOException {
         // heatIndication is scaled so that reaching 20 degrees away from 72 gives the maximum LED brightness (255)
            int heatIndication = (int) ((tempSensor.getCelsius() - 25.0) * 255.0 / 20);
            if(heatIndication > 0){
                leds[0].setRGB(heatIndication, 0, 0);                  //above 25 degrees C (room temp) in red
            } else {
                leds[0].setRGB(0, 0, - heatIndication);                //below 25 degrees C (room temp) in blue
            }
            
            int lightIndication = lightSensor.getValue();              //ranges from 0 - 740
            leds[1].setRGB(0, lightIndication / 3, 0);                 //Set LED green, will range from 0 - 246
    }

    private void radioConnectionIndicator(){
        // TODO
    }

    private void batteryIndicator(){
        // TODO
    }
    
    private void sendPacketIndicator(boolean ok){
        
    }
    
    /**
     * Receive commands to configure the SunSpot
     */    
    public void startMessageReceiver() {
        new Thread() {
            public void run() {                                
                while(true){
                    try {
                        dg.reset();
                        dgConnection.receive(dg);
                        
                        // TODO test the message header and switch to the method
                        byte packetType = dg.readByte();
                        if (packetType == RADIO_CONFIG_PACKET) {
                            processConfigurationMessage();
                            sendPacketIndicator(true);
                        } else {
                            System.out.println("Unknown message type");                    
                            sendPacketIndicator(false);
                        }                   
                    } catch (IOException e) {
                        System.out.println("Nothing received");
                        sendPacketIndicator(false);
                    }
                }
            }
        }.start();
    }

    public void processConfigurationMessage() throws IOException {
        // read the header (contains the message type)

        // configure the emission period
        long periodTmp = dg.readLong();
        if(periodTmp>0) {
            emissionPeriod=periodTmp;
        } else {
            System.out.println("Received a illegal value " + periodTmp + " from " + dg.getAddress());                            
        }

        // TODO light thredhold
        // TODO accelSensor thredhold                                               
    }
    
    private void startSensorThresholdListeners(){
        // TODO
    }
    
    /**
     * Sends measurement message periodically
     */
    synchronized public void startPeriodicalSender() {
        // TODO should be interupted when the emissionPeriod is changed
        new Thread() {
            public void run() {
                sendSensorDataMessage();
                Utils.sleep(emissionPeriod);
            }
        }.start();
    }
    
    private DatagramConnection dgConnection = null;
    private Datagram dg = null;

    synchronized private void openDatagramConnection() {
        // create a DatagramConnection
        try {
            // The Connection is a broadcast so we specify it in the creation string
            dgConnection = (DatagramConnection) Connector.open("radiogram://broadcast:"+BROADCAST_PORT);
            // TODO dgConnection.setMaxBroadcastHops(MAX_BROADCAST_HOPS);
            // Then, we ask for a datagram with the maximum size allowed
            dg = dgConnection.newDatagram(dgConnection.getMaximumLength());
        } catch (IOException ex) {
            System.out.println("Could not open radiogram broadcast connection");
            ex.printStackTrace();
            return;
        }
    }
        
    /**
     * Sends measurement message periodically
     */
    synchronized private void sendSensorDataMessage() {
        try {
            // Fill up the message
            dg.reset();

            // the message type
            dg.writeByte(RADIO_SENSORDATA_PACKET);
            
            // instantaneous acceleration
            dg.writeDouble(accelSensor.getAccelX());
            dg.writeDouble(accelSensor.getAccelY());
            dg.writeDouble(accelSensor.getAccelZ());

            // instantaneous temperature
            dg.writeDouble(tempSensor.getCelsius()+273.0);

            // instantaneous light
            dg.writeInt(lightSensor.getValue());

            // instantaneous switch status
            dg.writeBoolean(sw1.isOpen());
            dg.writeBoolean(sw2.isOpen());

            dgConnection.send(dg);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        // TODO blink leds
        
    }

    protected void startApp() throws MIDletStateChangeException { 
        new BootloaderListener().start();       // Listen for downloads/commands over USB connection
        try {
            initIndicator();
            openDatagramConnection();
            startSensorThresholdListeners();
            startPeriodicalSender();
            startMessageReceiver();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    protected void pauseApp() { 
    }

    protected void destroyApp(boolean arg0) throws MIDletStateChangeException { 
        // TODO close connections, stop threads ...
        endIndicator();            
        stopSensorThresholdListeners();
        stopPeriodicalSender();
    }
    
    private void endIndicator(){
        // TODO        
    }
    
    private void stopSensorThresholdListeners(){
        // TODO
    }

    private void stopPeriodicalSender(){
        // TODO
    }

    public void thresholdExceeded(IAccelerometer3D accel, int axis, double val, boolean relative) {
        // TODO
    }

    public void thresholdChanged(IAccelerometer3D accel, int axis, double low, double high, boolean relative) {
        // TODO
    }

    public void thresholdExceeded(ITemperatureInput temp, double val, boolean inCelsius) {
        // TODO
    }

    public void thresholdChanged(ITemperatureInput temp, double low, double high, boolean inCelsius) {
        // TODO
    }

    public void thresholdExceeded(ILightSensor light, int val) {
        // TODO
    }

    public void thresholdChanged(ILightSensor light, int low, int high) {
        // TODO
    }
}
