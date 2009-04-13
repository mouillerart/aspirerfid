/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.liglab.adele.sunspot.controller;

/**
 *
 * @author lionel
 */
public interface PacketTypes {

    public static final int DEFAULT_DATAGRAM_SIZE = 20;
    
    // Command & reply codes for data packets
    
    public static final byte NEW_CONNECTION_REQ = 1;
    
    public static final byte ADMIN_SVC_CONNECTION = 2;
    
    public static final byte TEMP_PROD_CONNECTION = 3;
    
    public static final byte HEARTBEAT = 10;
    
    public static final byte TEMPERATURE = 11;
    
    public static final int SET_ACCEL_SAMPLE_DELAY = 3;
   
    
    // temporary
    public static final byte BLINK_LEDS_REQ = 10;
    
    public static final byte SWITCH = 2;
    
    public static final byte ACCEL = 3;
    
}
