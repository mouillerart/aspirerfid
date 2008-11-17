package org.ow2.aspirerfid.sensor.sunspot.producers;

import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.wireadmin.Producer;
import org.osgi.service.wireadmin.Wire;
import org.osgi.service.wireadmin.WireConstants;
import org.osgi.util.measurement.Measurement;
import org.osgi.util.measurement.Unit;
import org.ow2.aspirerfid.sensor.sunspot.host.HostServer;

public class AccelerationProducer implements Producer, Runnable {
	
	private boolean m_end;
	
	private Wire[] m_wires;
	
	private BundleContext m_bc;
	
	private ServiceRegistration m_sr;
	
	private final String PID="org.ow2.aspirerfid.sensor.sunspot.producers.acceleration";
	
	private long UPDATE_DELAY = 200;
	
	private HostServer m_host;
	
	public AccelerationProducer(BundleContext bc){
		m_bc = bc;
	}
	
	
	// iPOJO life-cycle callbacks
	
	public void start(){
		System.out.println("SunSPOT acceleration producer activated");
		
		Class[] flavors = new Class[]{Measurement[].class};
		
		// publish the producer service
		Hashtable<String,Object> props = new Hashtable<String, Object>();
		props.put(Constants.SERVICE_PID, PID);
		props.put(WireConstants.WIREADMIN_PRODUCER_FLAVORS, flavors);
		props.put(PropertyConstants.DATA_TYPE, PropertyConstants.ACCELERATION_TYPE);		
		m_sr = m_bc.registerService(Producer.class.getName(), this, props);
		
		m_host = HostServer.getSingleton();
		
		m_end = false;
		Thread t = new Thread (this,"SunSPOT-acceleration-producer");
		t.start();		
	}
	
	public void stop(){
		m_end = true;
		m_sr.unregister();
		System.out.println("SunSPOT acceleration producer deactivated");		
	}
	
	public void consumersConnected(Wire[] wires) {
		m_wires = wires;
	}

	public Object polled(Wire wire) {
		Measurement[] accelMeasurements = new Measurement[3];
		double[] acceleration = m_host.getAcceleration();
		for (int i = 0; i < 2; i++){
			accelMeasurements[i] = new Measurement(acceleration[i],Unit.m_s2);
		}
		return accelMeasurements;
	}

	public void run() {
		Measurement[] accelMeasurements = new Measurement[3];
		while (!m_end){
			double[] acceleration = m_host.getAcceleration();
//			if (acceleration != null)System.out.println("Acceleration updated : "
//					+"aX="+acceleration[0]
//					+"\taY="+acceleration[1]
//					+"\taZ="+acceleration[2]);			
			if (m_wires != null && acceleration != null){
				for (int i = 0; i < acceleration.length; i++){
					accelMeasurements[i] = new Measurement(acceleration[i],Unit.m_s2);
				}
				for (Wire wire : m_wires){
					wire.update(accelMeasurements);
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
