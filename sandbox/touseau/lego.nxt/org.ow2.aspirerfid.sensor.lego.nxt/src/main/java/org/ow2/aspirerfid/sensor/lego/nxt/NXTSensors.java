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
package org.ow2.aspirerfid.sensor.lego.nxt;


import icommand.nxt.SensorPort;
import icommand.nxt.comm.NXTCommand;

import java.io.InputStream;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Map.Entry;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.wireadmin.Producer;
import org.osgi.service.wireadmin.WireConstants;
import org.ow2.aspirerfid.sensor.lego.nxt.config.Configuration;
import org.ow2.aspirerfid.sensor.lego.nxt.producers.NXTDistanceProducer;
import org.ow2.aspirerfid.sensor.lego.nxt.producers.NXTLightProducer;
import org.ow2.aspirerfid.sensor.lego.nxt.producers.NXTRFIDReader;
import org.ow2.aspirerfid.sensor.lego.nxt.producers.NXTSoundProducer;
import org.ow2.aspirerfid.sensor.lego.nxt.producers.NXTTouchPublisher;
import org.ow2.aspirerfid.sensor.lego.nxt.service.NXTConstants;

/**
 * @author lionel
 * TODO vecotirser les NXTProducers?
 * TODO synchronization issue when dealing with multiple sensors (more than 2 at a time). See iCommand library.
 */
public class NXTSensors implements Runnable {

	/**
	 * <Sensor Type, registration object>
	 */
	private HashMap<String, ServiceRegistration> serviceRegs;

	private NXTTouchPublisher m_touchPublisher;
	
	private NXTDistanceProducer m_distanceProducer;

	private NXTLightProducer m_lightProducer;

	private NXTSoundProducer m_soundProducer;

	private NXTRFIDReader m_rfidProducer;
	
	private SensorPort m_lightPort;
	private SensorPort m_soundPort;
	private SensorPort m_sonarPort;
	private SensorPort m_rfidPort;
	private SensorPort m_touchPort;
	
	private BundleContext m_bundleContext;
	
	/**
	 * Default configuration file location
	 */
	private static final String CONFIGFILE = "/config.properties";
	
	public NXTSensors (BundleContext bundleContext) {
		m_bundleContext = bundleContext;
	}
	
	/**
	 * Load the configuration properties from the default location
	 */
	private void configure() throws Exception {

		// Get the Config-Location value from the manifest

		String configLocation = null;
		Dictionary dict = m_bundleContext.getBundle().getHeaders();
		Enumeration enumeration = dict.keys();
		while (enumeration.hasMoreElements()) {
			Object nextKey = enumeration.nextElement();
			Object nextElem = dict.get(nextKey);
			if (nextKey.equals("Config-Location")) {
				configLocation = nextElem.toString();
				break;
			}
		}
		if (configLocation == null) {
			configLocation = CONFIGFILE;
		}
		
		// Load properties from configLocation file
		InputStream is = getClass().getResourceAsStream(configLocation);
		Properties configurationProperties = Configuration.loadProperties(is);
		configureNXT(configurationProperties);
	}

	
	private void configureNXT(Dictionary configurationProperties) throws Exception {

		int sensorPort;
		// Light sensor
		String lightSensorPortProperty = (String)configurationProperties.get("Light.sensor.port");
		sensorPort = 1; // default light port 1
		if (lightSensorPortProperty != null) {
			sensorPort = Integer.parseInt(lightSensorPortProperty);
		}
		else {
			sensorPort = 0; // 0 = undefined
		}
		switch (sensorPort) {
			case 1: m_lightPort = SensorPort.S1; 
					break;
			case 2: m_lightPort = SensorPort.S2; 
					break;
			case 3: m_lightPort = SensorPort.S3; 
					break;
			case 4: m_lightPort = SensorPort.S4; 
					break;
			default: break; // the light sensor is not plugged-in
		}
		
		// Sound sensor
		String soundSensorPortProperty = (String)configurationProperties.get("Sound.sensor.port");
		sensorPort = 2; // default sound port 2
		if (soundSensorPortProperty != null) {
			sensorPort = Integer.parseInt(soundSensorPortProperty);
		}
		else {
			sensorPort = 0; // 0 = undefined
		}
		switch (sensorPort) {
			case 1: m_soundPort = SensorPort.S1; 
					break;
			case 2: m_soundPort = SensorPort.S2; 
					break;
			case 3: m_soundPort = SensorPort.S3; 
					break;
			case 4: m_soundPort = SensorPort.S4; 
					break;
			default: break; // the sound sensor is not plugged-in
		}
		
		// Ultrasonic sensor
		String sonarSensorPortProperty = (String)configurationProperties.get("Ultrasonic.sensor.port");
		sensorPort = 3; // default sonar port = 3
		if (sonarSensorPortProperty != null) {
			sensorPort = Integer.parseInt(sonarSensorPortProperty);
		}
		else {
			sensorPort = 0; // 0 = undefined
		}
		switch (sensorPort) {
			case 1: m_sonarPort = SensorPort.S1; 
					break;
			case 2: m_sonarPort = SensorPort.S2; 
					break;
			case 3: m_sonarPort = SensorPort.S3; 
					break;
			case 4: m_sonarPort = SensorPort.S4; 
					break;
			default: break; // the ultra-sonic sensor is not plugged-in
		}
		
		// RFID reader
		String rfidSensorPortProperty = (String)configurationProperties.get("RFID.sensor.port");
		sensorPort = 4; // default sonar port = 4
		if (rfidSensorPortProperty != null) {
			sensorPort = Integer.parseInt(rfidSensorPortProperty);
		}
		else {
			sensorPort = 0; // 0 = undefined
		}
		switch (sensorPort) {
			case 1: m_rfidPort = SensorPort.S1; 
					break;
			case 2: m_rfidPort = SensorPort.S2; 
					break;
			case 3: m_rfidPort = SensorPort.S3; 
					break;
			case 4: m_rfidPort = SensorPort.S4; 
					break;
			default: break; // the RFID reader is not plugged-in
		}
		
		// Touch sensor
		String touchSensorPortProperty = (String)configurationProperties.get("Touch.sensor.port");
		sensorPort = 1; // default touch port 1
		if (touchSensorPortProperty != null) {
			sensorPort = Integer.parseInt(touchSensorPortProperty);
		}
		else {
			sensorPort = 0; // 0 = undefined
		}
		switch (sensorPort) {
			case 1: m_touchPort = SensorPort.S1; 
					break;
			case 2: m_touchPort = SensorPort.S2; 
					break;
			case 3: m_touchPort = SensorPort.S3; 
					break;
			case 4: m_touchPort = SensorPort.S4; 
					break;
			default: break; // the touch sensor is not plugged-in
		}		
		
	}
	
	public void start() throws Exception {
		// start a new Thread not to block other bundle activators
		Thread startThread = new Thread(this, "NXT start thread");
		startThread.start();
	}

	/** (non-Javadoc)
	 * Long lasting activator start task.
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		
		// Configure all sensor ports
		try {
			configure();
		} catch (Exception e) {
			System.err.println("Configuration Exception");
			e.printStackTrace();
		}
		
		// Bind to the Mindstorm NXT
		NXTCommand.open(); // might take some time
		
		// Create all producers with the right sensor ports
		System.out.println("Creation of NXT Producers");		
		
		
		// Publish their services along with properties (sensorType, sensorPort, Product ID, version, ...)
		serviceRegs = new HashMap<String, ServiceRegistration>();
		Hashtable<String,Object> props = new Hashtable<String,Object>();
		ServiceRegistration serviceReg;
		
		// Touch sensor
		if (m_touchPort != null){
			m_touchPublisher = new NXTTouchPublisher(m_touchPort);
			
			// start the producer activity				
			m_touchPublisher.startSensor();			
		}
		
		// Light producer
		if (m_lightPort != null){
			m_lightProducer = new NXTLightProducer(m_lightPort);
			
			// Publish the corresponding service along with some properties (sensorType, sensorPort, Product ID, version, ...)
			props.put(Constants.SERVICE_PID, m_lightProducer.getPID()); // TODO ajouter BTaddress du NXT?
			props.put(WireConstants.WIREADMIN_PRODUCER_FLAVORS,
						m_lightProducer.getFlavors());	
			// service registration
			serviceReg = m_bundleContext.registerService(Producer.class.getName(),
						m_lightProducer, props );
			serviceRegs.put(NXTConstants.LIGHT_TYPE, serviceReg);
			
			// start the producer activity				
			m_lightProducer.startSensor();			
		}
		
		// Sound producer
		if (m_soundPort != null){
			m_soundProducer = new NXTSoundProducer(m_soundPort);
			
			// Publish the corresponding service along with some properties (sensorType, sensorPort, Product ID, version, ...)
			props.put(Constants.SERVICE_PID, m_soundProducer.getPID()); // TODO ajouter BTaddress du NXT?
			props.put(WireConstants.WIREADMIN_PRODUCER_FLAVORS,
						m_soundProducer.getFlavors());
			// service registration
			serviceReg = m_bundleContext.registerService(Producer.class.getName(),
						m_soundProducer, props );
			serviceRegs.put(NXTConstants.SOUND_TYPE, serviceReg);
			
			// start the producer activity			
			m_soundProducer.startSensor();
			
		}
		
		// Distance producer
		if (m_sonarPort != null){
			m_distanceProducer = new NXTDistanceProducer(m_sonarPort);
			
			// Publish the corresponding service along with some properties (sensorType, sensorPort, Product ID, version, ...)
			props.put(Constants.SERVICE_PID, m_distanceProducer.getPID()); // TODO ajouter BTaddress du NXT?
			props.put(WireConstants.WIREADMIN_PRODUCER_FLAVORS,
						m_distanceProducer.getFlavors());
			// service registration
			serviceReg = m_bundleContext.registerService(Producer.class.getName(),
						m_distanceProducer, props );
			serviceRegs.put(NXTConstants.ULTRASONIC_TYPE, serviceReg);	

			// start the producer activity			
			m_distanceProducer.startSensor();
		}
		
		// RFID producer
		if (m_rfidPort != null){
			m_rfidProducer = new NXTRFIDReader(m_rfidPort);
			
			// Publish the corresponding service along with some properties (sensorType, sensorPort, Product ID, version, ...)
			props.put(Constants.SERVICE_PID, m_rfidProducer.getPID() ); // TODO ajouter BTaddress du NXT?
			props.put(WireConstants.WIREADMIN_PRODUCER_FLAVORS,
						m_rfidProducer.getFlavors());
			// TODO Add some other properties
			// service registration
			serviceReg = m_bundleContext.registerService(Producer.class.getName(),
							m_rfidProducer, props );
			serviceRegs.put(NXTConstants.RFID_TYPE, serviceReg);
			
			// start the producer activity
			m_rfidProducer.startSensor();
		}
	}

	public void stop() throws Exception {
		//unregister
		if (serviceRegs != null){
			for (Entry<String, ServiceRegistration> entry : serviceRegs.entrySet()){
				entry.getValue().unregister();
			}
			
			// Stop the activity of running sensors
			if (m_touchPort != null){
				m_touchPublisher.stopSensor();				
			}
			if (m_lightPort != null){
				m_lightProducer.stopSensor();				
			}
			if (m_soundPort != null){
				m_soundProducer.stopSensor();				
			}
			if (m_sonarPort != null){
				m_distanceProducer.stopSensor();				
			}
			if (m_rfidPort != null){
				m_rfidProducer.stopSensor();				
			}
		}
		try{
			NXTCommand.close();
		} catch (Exception e){
			System.err.println("Stream closed. Read Exception.");
		}
	}
	
}
