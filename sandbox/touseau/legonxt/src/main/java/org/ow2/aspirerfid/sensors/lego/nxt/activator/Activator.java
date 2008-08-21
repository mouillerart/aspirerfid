package org.ow2.aspirerfid.sensors.lego.nxt.activator;


import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.wireadmin.Producer;
import org.osgi.service.wireadmin.WireConstants;

public class Activator implements BundleActivator {

//	private NXTDistanceProducer producer;
	private ServiceRegistration serviceReg;
	private final String PRODUCER_PID = "lego.nxt.ultrasonic.distance.producer";
	
	public void start(BundleContext bundleContext) throws Exception {
		
		// Get the configuration properties
		// TODO cf code Didier 
		// Bind to the Mindstorm NXT
		
		// Create all producers with the right sensor ports
		
		// Publish their services along with properties (sensorType, sensorPort, Product ID, version, ...)
		// distanceProducer = new NXTDistanceProducer();
		
		System.out.println("Creation of NXT Producers");
		Hashtable<String,Object> props = new Hashtable<String,Object>();
		props.put(Constants.SERVICE_PID, PRODUCER_PID ); // TODO ajouter BTaddress du NXT?
		props.put(WireConstants.WIREADMIN_PRODUCER_FLAVORS,
					new Class[] { int.class } );
		
		// service registration (one per producer)
		serviceReg = bundleContext.registerService(Producer.class.getName(),
		distanceProducer, props );
		// start the producer activities
		distanceProducer.startPolling();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		//unregister
		serviceReg.unregister();
		producer.stopPolling();
	}

}
