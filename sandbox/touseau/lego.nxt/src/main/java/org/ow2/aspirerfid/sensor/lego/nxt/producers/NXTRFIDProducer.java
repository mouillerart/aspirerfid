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

import icommand.nxt.RFIDSensor;
import icommand.nxt.SensorPort;

import org.osgi.service.wireadmin.Wire;


/**
 * 
 * This class is a wireadmin producer for the Lego NXT ultrasonic sensor.
 * It produces [distance objects] in xxx unit.
 * 
 * @author lionel
 *
 */
public class NXTRFIDProducer implements NXTProducer {

	private final String PID = "org.ow2.aspirerfid.sensors.lego.nxt.rfid.producer";	
	
	/**
	 * The object types produced by this producer. Flavors are published as a WireAdmin property.
	 */
	private final Class[] m_flavors = new Class[] {java.lang.String.class};
	
	private boolean m_end;
	
	private Wire[] m_wires;

	private final long POLL_DELAY = 1000;
	
	private String m_tagID;
	
	private RFIDSensor m_rs; 
	
	public NXTRFIDProducer(SensorPort port){
		m_wires = null;
	}
	
	
	public void startSensor(){
		// create a new thread
        Thread thread = new Thread(this, PID);
        m_end = false;
        // start the producer        
        thread.start();
	}


	public void run() {
		
		m_rs = new RFIDSensor(SensorPort.S4);
		
		m_rs.stop();
		m_rs.bootStart();
		// wake up the sensor with a dummy command
		m_rs.dummyRFID();
		
		// set the reading mode
		m_rs.setReadMode(RFIDSensor.CONTINUOUS_READ);
	
	
		while (!m_end){
			// get the rfid tag
			m_tagID = m_rs.readTag();
			// update the wire values
			if (m_wires != null && !m_tagID.equals("00000")){
				for (Wire wire : m_wires){
					wire.update(m_tagID);
				}
			} else System.out.println("no consumer connected\n"+"tag ID: "+m_tagID);				
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
		// sends a STOP command to the sensor
		m_rs.stop();		
	}	
	
	public void consumersConnected(Wire[] wires) {
		// consumers that are interested by the NXT values
		m_wires = wires;
	}

	/* 
	 * When called this method will poll the NXT to get the distance value given by the ultrasonic sensor
	 * (non-Javadoc)
	 * @see org.osgi.service.wireadmin.Producer#polled(org.osgi.service.wireadmin.Wire)
	 */
	public Object polled(Wire wire) {
		// return the last read tag ID
		return m_tagID;
	}


	/**
	 * @return the flavors for this producer
	 */
	public Class[] getFlavors() {
		return m_flavors;
	}


	public String getPID() {
		return PID;
	}	
	
}
