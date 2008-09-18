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

import org.osgi.service.component.ComponentContext;
import org.osgi.service.wireadmin.Producer;
import org.osgi.service.wireadmin.Wire;

import sms.Unimotion;

/**
 * @author Lionel Touseau
 *
 */
public class MotionSensorProducer implements Producer, Runnable {

	/**
	 * Wires connected to this producer
	 */
	private Wire[] wires;
	
	private final Class[] flavors = {int.class};
	
	private boolean end;

	/**
	 * Time window in ms between each wire update.
	 */
	private final long UPDATE_DELAY = 500;
	
	public void activate(ComponentContext componentContext) {
		System.out.println("MotionSensorProducer activated");		
		end = false;
		Thread t = new Thread (this,"Accelerometer Producer");
		t.start();
	}

	public void deactivate(ComponentContext componentContext) {
		end = true;
		System.out.println("MotionSensorProducer deactivated");
	}
	
	public void consumersConnected(Wire[] wires) {
		this.wires = wires;
	}

	public Object polled(Wire wire) {
		return Unimotion.getSMSArray();
	}

	public void run() {
		end = false;
		int[] accelerometerValues;
		
		while (!end){
			accelerometerValues = Unimotion.getSMSArray();
			if (wires != null){
				for (Wire wire : wires){
					wire.update(accelerometerValues);
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
