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
import icommand.nxt.UltrasonicSensor;

import org.osgi.service.wireadmin.Wire;
import org.osgi.util.measurement.Measurement;
import org.osgi.util.measurement.Unit;
import org.ow2.aspirerfid.sensor.lego.nxt.service.NXTAccessService;


/**
 * 
 * This class is a wireadmin producer for the Lego NXT ultrasonic sensor.
 * It produces [distance objects] in xxx unit.
 * 
 * @author lionel
 *
 */
public class NXTDistanceProducer implements NXTProducer {

	private final String PID = "org.ow2.aspirerfid.sensors.lego.nxt.distance.producer";
	
	/**
	 * Serive allowing this producer to connect to the NXT brick and access sensors
	 */
	private NXTAccessService m_nxtAccessService;
	
	private final Class[] m_flavors = new Class[] {Measurement.class};
	
	private double sensorError = 3;
	
	private boolean m_end;
	
	private Wire[] m_wires;

	private UltrasonicSensor m_us;
	
	private final long POLL_DELAY = 1000;
	
	private Measurement m_lastDistance; // reprensents the last measurement returned by the sonar
	private int m_lastDistanceCM; // distance in centimeter
	
	public NXTDistanceProducer(SensorPort port){
		m_wires = null;
		m_lastDistanceCM = 255;
		m_us = new UltrasonicSensor(port);
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
			m_lastDistanceCM = m_us.getDistance(); // distance in centimeters
			
			m_lastDistance = new Measurement(((double)m_lastDistanceCM / 100), sensorError, Unit.m, System.currentTimeMillis()); //distance in meters
			
			// update the wire values
			if (m_wires != null){
				for (Wire wire : m_wires){
					wire.update(m_lastDistance);
				}
			} else System.out.println("no consumer connected\n"+"distance: "+m_lastDistanceCM);
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
		// return the distance measurement to the consumer
		return m_lastDistance;
	}


	public Class[] getFlavors() {
		return m_flavors;
	}


	public String getPID() {
		return PID;
	}	
	
}
