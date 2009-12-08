/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ow2.aspirerfid.sensor.sunspot.controller;

import com.sun.spot.sensorboard.EDemoBoard;
import com.sun.spot.sensorboard.peripheral.ITriColorLED;
import com.sun.spot.sensorboard.peripheral.LEDColor;

/**
 *
 * @author lionel
 */
public class SPOTServiceImpl implements SPOTService {

    private ITriColorLED leds[] = EDemoBoard.getInstance ().getLEDs();
    
    public void setLED(int ledNumber, LEDColor color) {
        if (ledNumber < leds.length && ledNumber >= 0) {
                leds[ledNumber].setColor(color);
        }
        
    }

    public void setLEDs(int[] ledsNumber, LEDColor color) {
        for (int i = 0; i < ledsNumber.length; i++) {
            if (ledsNumber[i] < leds.length && ledsNumber[i] >= 0) {
                leds[ledsNumber[i]].setColor(color);
            }   
        }
        
    }

    public void turnLEDoff(int ledNumber) {
        if (ledNumber < leds.length && ledNumber >= 0 && leds[ledNumber].isOn()) {
            leds[ledNumber].setOff();
        }
    }

    public void turnLEDsoff() {
        for (int j = 0; j < 8; j++) {
            leds[j].setOff();
        }
    }

    public void getBatteryLevel() {
    }
    
}
