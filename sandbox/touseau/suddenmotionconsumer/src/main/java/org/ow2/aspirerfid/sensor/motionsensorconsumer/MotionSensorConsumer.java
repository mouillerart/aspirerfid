package org.ow2.aspirerfid.sensor.motionsensorconsumer;

import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.wireadmin.Consumer;
import org.osgi.service.wireadmin.Wire;
import org.osgi.service.wireadmin.WireConstants;
import org.osgi.util.measurement.Measurement;

/**
 * @author Lionel Touseau
 *
 */
public class MotionSensorConsumer implements Consumer {

	private Wire[] m_wires;
	
	private Class[] m_flavors;
	
	private final String PID = "org.ow2.aspirerfid.sensor.motionsensorconsumer.MotionSensorConsumer";
	
	private BundleContext m_bc;
	
	private ServiceRegistration m_sr;
	
	private Measurement[] lastAccelerometerValues;

	// iPOJO callbacks	
	
	public MotionSensorConsumer(BundleContext bc){
		m_bc = bc;
	}
		
	public void activate (){
		System.out.println("MotionSensorConsumer activated");
		
		m_flavors = new Class[]{Measurement[].class};
		
		// publish the consumer service
		Hashtable<String,Object> props = new Hashtable<String, Object>();
		props.put(Constants.SERVICE_PID, this.PID);
		props.put(WireConstants.WIREADMIN_CONSUMER_FLAVORS, m_flavors);
		m_sr = m_bc.registerService(Consumer.class.getName(), this, props);

	}
	
	public void deactivate (){
		m_sr.unregister();
	}
	
	public void producersConnected(Wire[] wires) {
		m_wires = wires;
	}

	public void updated(Wire wire, Object obj) {
		lastAccelerometerValues = (Measurement[]) obj;
		System.out.println("X axis: "+lastAccelerometerValues[0].getValue()
						+"\t Y axis: "+lastAccelerometerValues[1].getValue()
						+"\t Z axis: "+lastAccelerometerValues[2].getValue());
	}


}
