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

/**
 * @author Didier Donsez, Lionel Touseau
 */ 
public interface PacketType {
    public static final int BROADCAST_PORT              = 42;
    /**
     * Type for the configuration message (host --> spot)
     */
    public static final int RADIO_CONFIG_PACKET            = 0xEA;
    /**
     * Type for the sensor data message (spot --> spot/host)
     */
    public static final int RADIO_SENSORDATA_PACKET        = 0xDD;
    
    /**
     * Type for the history of sensor data message (spot --> spot/host)
     */
    public static final int RADIO_GETHISTORYSENSORDATA_PACKET        = 0xEF;
    
    /**
     * Type for the history of sensor data message (spot --> spot/host)
     */
    public static final int RADIO_HISTORYSENSORDATA_PACKET        = 0xDF;
    
        /**
     * Type for the accumulated sensor data message (spot --> spot/host)
     */
    public static final int RADIO_GETACCUMULATEDSENSORDATA_PACKET        = 0xEB;
    
    /**
     * Type for the accumulated sensor data message (spot --> spot/host)
     */
    public static final int RADIO_ACCUMULATEDSENSORDATA_PACKET        = 0xDB;

}