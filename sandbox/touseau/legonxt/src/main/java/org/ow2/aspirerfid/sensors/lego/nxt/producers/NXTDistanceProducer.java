package org.ow2.aspirerfid.sensors.lego.nxt.producers;

import icommand.nxt.SensorPort;
import icommand.nxt.UltrasonicSensor;
import icommand.nxt.comm.NXTCommand;

import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.service.wireadmin.Producer;
import org.osgi.service.wireadmin.Wire;
import org.osgi.service.wireadmin.WireConstants;


/**
 * 
 * This class is a wireadmin producer for the Lego NXT ultrasonic sensor.
 * It produces [distance objects] in xxx unit.
 * 
 * @author lionel
 *
 */
public class NXTDistanceProducer implements Producer, Runnable {

	final Class[] m_flavors = new Class[] {java.lang.Integer.class};
	
	private boolean m_end;
	
	private Wire[] m_wires;

	private final long POLL_DELAY = 100;
	
	private int m_distance;
	
	public NXTDistanceProducer(){
		m_wires = null;
		m_distance = 255;
	}
	
	
	public void startPolling(){
		// create a new thread
        Thread thread = new Thread(this);
        m_end = false;
        
        // open the BT connection
		NXTCommand.open();        
        
        // start the producer        
        thread.start();
	}


	public void run() {
		
		// connect to the NXT brick...
		
		//SAMPLE code
//		NXTCommand.setVerify(true);
		UltrasonicSensor us = new UltrasonicSensor(SensorPort.S4);
		System.out.println("Version: " + us.getVersion());
		System.out.println("Product ID: " + us.getProductID());
		System.out.println("Port Type: " + us.getSensorType());
		System.out.println("Measurement Interval: "
				+ us.getMeasurementInterval());
		
		// loop and constantly update the wires with value from the NXT ultrasonic sensor		
		
		while (!m_end){
			
			// get the NXT ultrasonic sensor value
			m_distance = us.getDistance();
			
			// update the wire values
			if (m_wires != null){
				for (Wire wire : m_wires){
					wire.update(new Integer(m_distance));
				}
			} else //System.out.println("no consumer connected\n"+"distance: "+m_distance);
			// let some time to the NXT between each BT communication			
			try {
				Thread.sleep(POLL_DELAY);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}		

		// close the BT connection
		NXTCommand.close();			
		
		
//		System.out.println("Factory scale factor: "
//				+ us.getFactoryScaleFactor());
//		System.out.println("Factory scale divisor: "
//				+ us.getFactoryScaleDivisor());
//		byte[] distances = us.getDistances();
//		for (int i = 0; i < distances.length; i++)
//			System.out.println("Distance " + i + " = " + distances[i]);
	
	}

	public void stopPolling(){
		// ends the created thread
		m_end = true;			
	}	
	
	public void consumersConnected(Wire[] wires) {
		// consumers that are interested by the NXT values
		System.out.println("consumer detected");
		m_wires = wires;
	}

	/* 
	 * When called this method will poll the NXT to get the distance value given by the ultrasonic sensor
	 * (non-Javadoc)
	 * @see org.osgi.service.wireadmin.Producer#polled(org.osgi.service.wireadmin.Wire)
	 */
	public Object polled(Wire wire) {

		// get the NXT device
		// TODO with icommand
		// TODO sleep 50ms to wait for NXT to end its current task??
		// get the distance value
		// TODO with icommand
		// return the distance to the consumer
		return m_distance;
	}	
	
}
