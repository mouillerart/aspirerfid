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

public class LuminosityProducer implements Runnable, Producer {
	
	private boolean m_end;
	
	private Wire[] m_wires;
	
	private BundleContext m_bc;
	
	private ServiceRegistration m_sr;
	
	private HostServer m_host;	
	
	/**
	 * IEEE Address of the corresponding SPOT sensor
	 */
	private String m_address;	
	
	private String PID="org.ow2.aspirerfid.sensor.sunspot.producers.luminosity";
	
	private long UPDATE_DELAY = 10*1000;
	
	private long m_spotUpdateDelay = 10*1000;
	private long m_lastMeasurementTime = 0;	
	
	public LuminosityProducer(BundleContext bc, String address){
		m_bc = bc;
		m_address = address;
	}
	
	
	// iPOJO life-cycle callbacks
	
	public void start(){
		System.out.println("SunSPOT luminosity producer activated");
		
		Class[] flavors = new Class[]{Measurement.class};
		
		// publish the producer service
		Hashtable<String,Object> props = new Hashtable<String, Object>();
		String specificPID = PID+"@{spot:"+m_address+"}";
		props.put(Constants.SERVICE_PID, specificPID);
		props.put(WireConstants.WIREADMIN_PRODUCER_FLAVORS, flavors);
		props.put(PropertyConstants.DATA_TYPE, PropertyConstants.LUMINOSITY_TYPE);		
		m_sr = m_bc.registerService(Producer.class.getName(), this, props);
		
		m_host = HostServer.getSingleton();		
		
		m_end = false;
		Thread t = new Thread (this,"SunSPOT-luminosity-producer");
		t.start();		
	}
	
	public void stop(){
		m_end = true;
		m_host.producerRemoved(m_address);	
		m_sr.unregister();
		System.out.println("SunSPOT luminosity producer deactivated");		
	}	
	
	public void run() {
		Measurement lumMeasurement;
		while (!m_end){
			// Verify if the luminosity value has been updated
			long time = m_host.getLastMeasurementTime(m_address);
			
			if (time >= m_lastMeasurementTime + m_spotUpdateDelay) { // everything is fine
				m_lastMeasurementTime = time;
			} else { // no new sensor value has been received from the SPOT
				// producer must be stopped
				m_host.producerRemoved(m_address);
				this.stop();
			}			
			
			int luminosity = m_host.getLuminosity(m_address);
			if (luminosity != 0)
				System.out.println("Luminosity updated : "+luminosity);			
			if (m_wires != null && luminosity != 0){
				lumMeasurement = new Measurement(luminosity,Unit.unity);
				for (Wire wire : m_wires){
					wire.update(lumMeasurement);
				}
			}
			
			// wait for "UPDATE_DELAY" milliseconds
			try {
				Thread.sleep(UPDATE_DELAY);
			} catch (InterruptedException e) {
				System.err.println("Luminosity Producer has been interrupted");;
			}
		}
	}

	public void consumersConnected(Wire[] wires) {
		m_wires = wires;
	}

	public Object polled(Wire wire) {
		int luminosity = m_host.getLuminosity(m_address);
		Measurement lumMeasurement = new Measurement(luminosity,Unit.unity);
		return lumMeasurement;
	}

}
