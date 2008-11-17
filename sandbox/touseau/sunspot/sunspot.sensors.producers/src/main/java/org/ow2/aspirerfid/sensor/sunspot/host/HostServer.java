package org.ow2.aspirerfid.sensor.sunspot.host;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Map.Entry;

import javax.microedition.io.Connector;
import javax.microedition.io.Datagram;

import org.osgi.framework.BundleContext;
import org.osgi.service.wireadmin.Producer;
import org.ow2.aspirerfid.sensor.sunspot.host.config.Configuration;
import org.ow2.aspirerfid.sensor.sunspot.producers.LuminosityProducer;
import org.ow2.aspirerfid.sensor.sunspot.producers.TemperatureProducer;

import com.sun.spot.io.j2me.radiogram.RadiogramConnection;

/**
 * @author Lionel Touseau
 * 
 * Listens to Sun SPOTS connections.
 * Reads luminosity and temperature value from a SPOT and accelerometer values from another SPOT.
 * 
 * TODO detect end of communication with a SPOT to stop the corresponding producer
 * 
 */
public class HostServer implements Runnable {
	
	private static HostServer singleton;
	
    // Broadcast port on which we listen for sensor samples
//    private int HOST_PORT = 67;
    private int m_hostPort;
    
//	private String STATION_SERIAL_PORT = "COM5";
    private String m_serialPort;
	
	private AccelerometerListener m_listener;
	
    private RadiogramConnection m_rCon;	
	
    private boolean m_end;
    
	private double[] m_acceleration;
	
//	private double m_temperature;
	
	/**
	 * Map<IEEEAdress, Temperature>
	 */
	private Hashtable<String, Double> m_temperatures;
	
	/**
	 * Map<IEEEAdress, Luminosity>
	 */
	private Hashtable<String, Integer> m_luminosities;
	/**
	 * Map<IEEEAdress, Time>
	 */
	private Hashtable<String, Long> m_lastMeasurementTimes;
		
//	private int m_luminosity;
	
	private Hashtable<String, Producer[]> m_producers;
	
	private BundleContext m_bundleContext;
	
	/**
	 * Default configuration file location
	 */
	private static final String m_CONFIGFILE = "/config.properties";	
	
	private HostServer(BundleContext bc){
//		if (singleton == null){
			singleton = this;
			m_bundleContext = bc;
			configurePorts();
			System.setProperty("SERIAL_PORT", m_serialPort /*STATION_SERIAL_PORT*/);
			m_listener = new AccelerometerListener();
		
			m_producers = new Hashtable<String, Producer[]>();
			m_temperatures = new Hashtable<String, Double>();
			m_luminosities = new Hashtable<String, Integer>();
			m_lastMeasurementTimes = new Hashtable<String, Long>();
//		}
		
	}
	
	private void configurePorts() {

		String configLocation = m_CONFIGFILE;
		
		// Load properties from configLocation file
		InputStream is = getClass().getResourceAsStream(configLocation);
		Properties configurationProperties = Configuration.loadProperties(is);

		String hostPortProperty = (String)configurationProperties.get(Configuration.HOST_PORT);
		m_hostPort = 67; // default host port
		if (hostPortProperty != null) {
			m_hostPort = Integer.parseInt(hostPortProperty);
		}
		
		String serialPortProperty = (String)configurationProperties.get(Configuration.STATION_SERIAL_PORT);
		m_serialPort = "COM5"; // default
		if (serialPortProperty != null) {
			m_serialPort = serialPortProperty;
		}
		
	}	
	
	// iPOJO callbacks
	
	public void start(){
		// starts the accelerometer listener.		
		m_listener.start();
		m_end = false;
		Thread t = new Thread(this);
		t.start();
	}
	
	public void stop(){
		// stops receiving data from the SunSPOT
		m_end = true;
		try {
			m_rCon.close();
			System.out.println("Connection closed on port "+m_hostPort);	
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		// switch off the accelerometer listener
		m_listener.doBlink();
		m_listener.doSendData(false);
		m_listener.doQuit();
		
		// stop all producers
		for (Entry<String, Producer[]> entries : m_producers.entrySet()){
			Producer[] prods = entries.getValue();
			((TemperatureProducer) prods[0]).stop();
			((LuminosityProducer) prods[1]).stop();
		}
		m_producers.clear();
	}
	

	public void run() {
		
        m_rCon = null;
        Datagram dg = null;
        DateFormat fmt = DateFormat.getTimeInstance();
         
        try {
            // Open up a server-side broadcast radiogram connection
            // to listen for sensor readings being sent by different SPOTs
        	m_rCon = (RadiogramConnection) Connector.open("radiogram://:" + m_hostPort);
            dg = m_rCon.newDatagram(m_rCon.getMaximumLength());
        } catch (Exception e) {
             System.err.println("setUp caught " + e.getMessage());
//             throw e;
        }


        // Main data collection loop
        while (!m_end) {
            try {
                // Read sensor sample received over the radio
            	if (m_rCon==null || dg == null){
            		m_end = true;
            		break;
            	}
            	else {
	            	m_rCon.receive(dg);
	                String addr = dg.getAddress();  // read sender's Id
	                
	                if (!m_producers.containsKey(addr)){ // if the address is unknown, create new producers
	                	TemperatureProducer temp = new TemperatureProducer(m_bundleContext, addr);
	                	LuminosityProducer lum = new LuminosityProducer(m_bundleContext, addr);
	                	m_producers.put(addr, new Producer[]{temp, lum});
	                	temp.start();
	                	lum.start();
	                }
	                
	                long time = dg.readLong();      // read time of the reading
	                int lumVal = dg.readInt();         // read the luminosity sensor value
	                double tempVal = dg.readDouble();	// read the temperature sensor value
	                m_lastMeasurementTimes.put(addr, time); // mark the timestamp
	                updateLuminosity(lumVal, addr);
	                updateTemperature(tempVal, addr);
	//                System.out.println(fmt.format(new Date(time)) + "  from: "+ addr
	//                		+ "   luminosity value = " + lumVal
	//                		+ "   temperature value = " + tempVal);
            	}
            	
            } catch (Exception e) {
                System.err.println("Caught " + e +  " while reading sensor samples.");
                
            }
        }	
	}	
	
	public static HostServer getSingleton(){
		return singleton;
	}
	
	/**
	 * Callback method called by the AccelerationListener
	 * @param accel
	 */
	public void updateAcceleration(double[] accel){
		m_acceleration = accel;
	}
	
	public void updateLuminosity(int luminosity, String address){
		m_luminosities.put(address, luminosity);
	}

	public void updateTemperature(double temperature, String address){
		m_temperatures.put(address, temperature);
	}
	
	/**
	 * @return the accel
	 */
	public double[] getAcceleration() {
		return m_acceleration;
	}
	
	/**
	 * @param address, the IEEE address of the SPOT
	 * @return the luminosity for the corresponding SPOT sensor
	 */
	public int getLuminosity(String address) {
		return m_luminosities.get(address);
	}
	
	/**
	 * @param address, the IEEE address of the SPOT
	 * @return the temperature for the corresponding SPOT sensor
	 */
	public double getTemperature(String address) {
		return (m_temperatures.containsKey(address)) ? m_temperatures.get(address) : 0.0d;
	}

	/**
	 * @param address, the IEEE address of the SPOT
	 * @return the last time the corresponding SPOT sensor has sent a value
	 */	
	public long getLastMeasurementTime(String address) {
		return (m_lastMeasurementTimes.containsKey(address)) ? m_lastMeasurementTimes.get(address) : 0;
	}
	
	public void producerRemoved (String address){
		m_producers.remove(address);
	}
}
