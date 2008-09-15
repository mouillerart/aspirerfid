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
import icommand.nxt.TouchSensor;

import org.osgi.service.event.EventAdmin;

/**
 * @author lionel
 * TODO => EventAdmin (not plugged yet) or MonitorAdmin
 * TODO polling communications => high cost
 */
public class NXTTouchPublisher implements Runnable {

	private final static String PID = "org.ow2.aspirerfid.sensors.lego.nxt.touch.producer";

	private EventAdmin m_evtAdmin; // to be injected
	
	private TouchSensor m_ts;
	
	private boolean m_end;
	
	private boolean m_buttonPressed;
	
	private final String pressedTopic = "org/ow2/aspirerfid/sensors/lego/nxt/touch/pressed/*";
	
	private final String releasedTopic = "org/ow2/aspirerfid/sensors/lego/nxt/touch/released/*";
	
	private final long POLL_DELAY = 250;
	
	public NXTTouchPublisher (SensorPort port){
		m_ts = new TouchSensor(port);
	}
	
	public String getPID(){
		return PID;
	}
	
	public void startSensor(){
		m_end = false;
		Thread t = new Thread(this, PID);
		t.start();
	}

	public void run() {
		while (!m_end){
			boolean oldState = m_buttonPressed;
			m_buttonPressed = m_ts.isPressed();
			if (!oldState && m_buttonPressed){ // the touch sensor was not pressed, but now it is.
//				Event evt = new Event (pressedTopic, null);
//				m_evtAdmin.postEvent(evt);
				System.out.println("button pressed");
			} else if (oldState && !m_buttonPressed){ // the touch sensor has just been released
//				Event evt = new Event (releasedTopic, null);
//				m_evtAdmin.postEvent(evt);
				System.out.println("button released");
			} else { // either the button was pressed and it is still, or it is still released
				// do nothing,	
			}
			try {
				Thread.sleep(POLL_DELAY);
			} catch (InterruptedException it) {
				it.printStackTrace(); // TODO handle exception
			}
		}
	}
	
	public void stopSensor(){
		m_end = true;
	}
}
