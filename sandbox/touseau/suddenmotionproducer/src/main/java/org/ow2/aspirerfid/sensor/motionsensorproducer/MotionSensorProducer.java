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

package org.ow2.aspirerfid.sensor.motionsensorproducer;

import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.wireadmin.Producer;
import org.osgi.service.wireadmin.Wire;
import org.osgi.service.wireadmin.WireConstants;
import org.osgi.util.measurement.Measurement;

import sms.Unimotion;

/**
 * @author Lionel Touseau
 *
 */
public class MotionSensorProducer implements Producer, Runnable {

	/**
	 * Wires connected to this producer
	 */
	private Wire[] m_wires;
	
	private Class[] m_flavors;
	
	private BundleContext m_bc;
	
	private final String PID = "org.ow2.aspirerfid.sensor.motionsensorproducer.MotionSensorProducer";	
	
	private ServiceRegistration m_sr;
	
	private boolean end;

	/**
	 * Time window in ms between each wire update.
	 */
	private final long UPDATE_DELAY = 500;
	
	public void activate() {
		System.out.println("MotionSensorProducer activated");
		
		m_flavors = new Class[]{Measurement[].class};
		
		// publish the producer service
		Hashtable<String,Object> props = new Hashtable<String, Object>();
		props.put(Constants.SERVICE_PID, this.PID);
		props.put(WireConstants.WIREADMIN_PRODUCER_FLAVORS, m_flavors);
		m_sr = m_bc.registerService(Producer.class.getName(), this, props);
		
		end = false;
		Thread t = new Thread (this,"Accelerometer Producer");
		t.start();
	}

	public void deactivate() {
		end = true;
		m_sr.unregister();
		System.out.println("MotionSensorProducer deactivated");
	}
	
	public MotionSensorProducer(BundleContext bc){
		m_bc = bc;
	}
	
//	/**
//	 * Callback method called by the iPOJO framework at instantiation time
//	 * @param flavors the new flavor properties
//	 */
//	public void setFlavors(String[] flavors){
//		Class[] temp = new Class[flavors.length];
//		for (int i=0; i < flavors.length; i++){
//			try {
//				temp[i] = m_bc.getBundle().loadClass(flavors[i]);
//			} catch (ClassNotFoundException e) {
//				e.printStackTrace(); // TODO msg plus explicite
//			}
//		}
//		m_flavors = temp;
//		
//	}
	
	public void consumersConnected(Wire[] wires) {
		m_wires = wires;
	}

	public Object polled(Wire wire) {
		return Unimotion.getSMSArray();
	}

	public void run() {
		int[] accelerometerValues;
		Measurement[] measurements = new Measurement[3];
		
		while (!end) {
			accelerometerValues = Unimotion.getSMSArray();
			for (int i = 0; i < 3; i++){
				measurements[i] = new Measurement(accelerometerValues[i]);
			}
			if (m_wires != null){
				for (Wire wire : m_wires){
					wire.update(measurements);
				}
			}
			
			// wait for "UPDATE_DELAY" milliseconds
			try {
				Thread.sleep(UPDATE_DELAY);
			} catch (InterruptedException e) {
				System.err.println("Accelerometer Producer has been interrupted");;
			}
			
		}
	}

}
