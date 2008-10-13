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
package org.ow2.aspirerfid.sensor.lego.nxt.producers;

import icommand.nxt.SensorPort;
import icommand.nxt.SoundSensor;

import org.osgi.service.wireadmin.Wire;
import org.ow2.aspirerfid.sensor.lego.nxt.service.NXTAccessService;

public class NXTSoundProducer implements NXTProducer {

	private final String PID = "org.ow2.aspirerfid.sensors.lego.nxt.sound.producer";
	
	private final Class[] m_flavors = new Class[] {int.class};
	
	/**
	 * Serive allowing this producer to connect to the NXT brick and access sensors
	 */
	private NXTAccessService m_nxtAccessService;	
	
	/**
	 * In decibels (dB).
	 * e.g. Whispering = 20 dB<br>
	 * Vacuum cleaner = 80 dB<br>
	 * Jet engine = 150 dB
	 */
	private int m_lastSoundValue;
	
	private boolean m_end;
	
	private Wire[] m_wires;

	private SoundSensor m_ss;
	
	private final long POLL_DELAY = 1000;
	
	public NXTSoundProducer(SensorPort port) {
		m_wires = null;
		m_lastSoundValue = 0;
		m_ss = new SoundSensor(port);
	}

	public void consumersConnected(Wire[] wires) {
		m_wires = wires;
	}

	public Object polled(Wire wire) {
		return m_lastSoundValue;
	}

	public Class[] getFlavors() {
		return m_flavors;
	}

	public String getPID() {
		return PID;
	}

	public void startSensor(){
		// create a new thread
        Thread thread = new Thread(this, PID);
        m_end = false;        
        // start the producer        
        thread.start();
	}	
	
	public void run() {
		// loop and constantly update the wires with value from the NXT ultrasonic sensor		
		while (!m_end){
			
			// get the NXT ultrasonic sensor value
			m_lastSoundValue = m_ss.getdB();

			// update the wire values
			if (m_wires != null){
				for (Wire wire : m_wires){
					wire.update(m_lastSoundValue);
				}
			} else System.out.println("no consumer connected\n"+"sound: "+m_lastSoundValue);
			// let some time to the NXT between each BT communication			
			try {
				Thread.sleep(POLL_DELAY);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}		
	}
	
	public void stopSensor(){
		// ends the created thread
		m_end = true;
	}
	
}
