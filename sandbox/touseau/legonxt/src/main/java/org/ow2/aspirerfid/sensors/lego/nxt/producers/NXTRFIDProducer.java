package org.ow2.aspirerfid.sensors.lego.nxt.producers;

import fr.liglab.adele.lego.nxt.rfid.sensor.RFIDSensor;
import icommand.nxt.SensorPort;
import icommand.nxt.comm.NXTCommand;

import org.osgi.service.wireadmin.Producer;
import org.osgi.service.wireadmin.Wire;


/**
 * 
 * This class is a wireadmin producer for the Lego NXT ultrasonic sensor.
 * It produces [distance objects] in xxx unit.
 * 
 * @author lionel
 *
 */
public class NXTRFIDProducer implements Producer, Runnable {

	final Class[] m_flavors = new Class[] {java.lang.Integer.class};
	
	private boolean m_end;
	
	private Wire[] m_wires;

	private final long POLL_DELAY = 100;
	
	private int m_distance;
	
	public NXTRFIDProducer(){
		m_wires = null;
		m_distance = 255;
	}
	
	
	public void startSensor(){
		// create a new thread
        Thread thread = new Thread(this);
        m_end = false;
        
        // open the BT connection and connect to the NXT brick...
		NXTCommand.open();        
        
        // start the producer        
        thread.start();
	}


	public void run() {
		
		//SAMPLE code
//		NXTCommand.setVerify(true);
		RFIDSensor rs = new RFIDSensor(SensorPort.S1);
		try {
			System.out.println("Version: " + rs.getVersion());
			Thread.sleep(100);
			System.out.println("Product ID: " + rs.getProductID());
			Thread.sleep(100);
			System.out.println("Port Type: " + rs.getSensorType());
			Thread.sleep(100);
			System.out.println("Version: " + rs.getVersion());
			Thread.sleep(100);
			System.out.println("Product ID: " + rs.getProductID());
			Thread.sleep(100);
			System.out.println("Port Type: " + rs.getSensorType());
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// loop and constantly update the wires with value from the NXT ultrasonic sensor		
		
//		while (!m_end){
//			
//			// get the NXT ultrasonic sensor value
//			m_distance = us.getDistance();
//			
//			// update the wire values
//			if (m_wires != null){
//				for (Wire wire : m_wires){
//					wire.update(new Integer(m_distance));
//				}
//			} else //System.out.println("no consumer connected\n"+"distance: "+m_distance);
//			// let some time to the NXT between each BT communication			
//			try {
//				Thread.sleep(POLL_DELAY);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}			
//		}		

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

	public void stopSensor(){
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
