package org.ow2.aspirerfid.sensor.lego.nxt.service.impl;

import icommand.nxt.SensorPort;
import icommand.nxt.comm.DeviceInfo;
import icommand.nxt.comm.NXTCommand;

import java.io.InputStream;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.ow2.aspirerfid.sensor.lego.nxt.service.NXTAccessService;
import org.ow2.aspirerfid.sensor.lego.nxt.service.NXTConstants;
import org.ow2.aspirerfid.sensor.lego.nxt.service.config.Configuration;

public class NXTAccessServiceImpl implements NXTAccessService, Runnable {

	/**
	 * Bundle context injected by the iPOJO framework
	 */
	private BundleContext m_bundleContext;
	
	/**
	 * Service Registration object
	 */
	private ServiceRegistration m_serviceReg;
	
	/**
	 * If true, will quit the NXT connection routine
	 */
	private boolean m_end;
	
	/**
	 * True if the NXT brick connection succeeds
	 */
	private boolean m_connected;
	
	/**
	 * 
	 */
	private long CONNECTION_RETRY_PERIOD = 30000;
	
	// Sensor ports
	private SensorPort m_lightPort;
	private SensorPort m_soundPort;
	private SensorPort m_sonarPort;
	private SensorPort m_rfidPort;
	private SensorPort m_touchPort;
	
	/**
	 * Default configuration file location
	 */
	private static final String CONFIGFILE = "/config.properties";
	
	// iPOJO Callbacks
	
	public NXTAccessServiceImpl(BundleContext bc){
		m_bundleContext = bc;
	}
	
	public void start(){
		m_end = false;
		m_connected = false;
		// Try to open a connection in a new Thread not to block other bundle activators
		Thread startThread = new Thread(this, "NXT start thread");
		startThread.start();
	}
	
	public void stop(){
		m_end = true;
		m_serviceReg.unregister();
		closeConnection();
	}	
	
	// start thread
	public void run() {
		
		// Configure all sensor ports
		try {
			configure();
		} catch (Exception e) {
			System.err.println("Configuration Exception");
			e.printStackTrace();
		}
		
		// loop until a Mindstorm NXT is connected
		while (!m_connected || !m_end){
			// Bind to the Mindstorm NXT
			m_connected = tryOpenningNXTConnection(); // might take some time
			if (!m_connected){
				try {
					Thread.sleep(CONNECTION_RETRY_PERIOD);
				} catch (InterruptedException e) {
					// do nothing TODO ??
				}
			}
		}
		if (m_end)
			return;
		// the NXT brick is connected, the service can be published
		publishService();
		
	}
	
	// Service methods

	public SensorPort getSensorPort(String sensorType) {
		if (sensorType.equals(NXTConstants.LIGHT_TYPE)
				&& m_lightPort != null)
			return m_lightPort;
		if (sensorType.equals(NXTConstants.SOUND_TYPE)
				&& m_soundPort != null)
			return m_soundPort;
		if (sensorType.equals(NXTConstants.ULTRASONIC_TYPE)
				&& m_sonarPort != null)
			return m_sonarPort;
		if (sensorType.equals(NXTConstants.TOUCH_TYPE)
				&& m_touchPort != null)
			return m_touchPort;
		if (sensorType.equals(NXTConstants.RFID_TYPE)
				&& m_rfidPort != null)
			return m_rfidPort;
		
		// else
		return null;
	}

	// private methods
	
	private boolean tryOpenningNXTConnection() {
		// Bind to the Mindstorm NXT
		try {
			NXTCommand.open(); // might take some time
			return true;
		} catch (RuntimeException re) {
			System.err.println("Could not connect to the Lego NXT brick");
			return false;
		}
	}

	private void closeConnection() {
		NXTCommand.close();
	}
	
	private void publishService(){
		DeviceInfo info = NXTCommand.getSingleton().getDeviceInfo();
		String servicePID = info.NXTname+"-"+info.bluetoothAddress;
		
		Hashtable<String,Object> props = new Hashtable<String,Object>();
		props.put("service.pid", servicePID);
		
		m_serviceReg = m_bundleContext.registerService("org.ow2.aspirerfid.sensor.lego.nxt.service.NXTAccessService", this, props);
	}
	
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
	
}
