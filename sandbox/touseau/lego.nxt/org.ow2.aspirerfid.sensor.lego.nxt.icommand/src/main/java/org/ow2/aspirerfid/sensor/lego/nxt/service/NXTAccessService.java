package org.ow2.aspirerfid.sensor.lego.nxt.service;

import icommand.nxt.SensorPort;

/**
 * @author lionel
 * Service available if a Lego NXT brick is reachable
 * 
 * Open/Close pas utile si fait automatiquement
 * 
 */
public interface NXTAccessService {

	
	/**
	 * Opens a connection with an NXT brick if not already done
	 * TODO return something?
	 */
//	public void openConnection();
	
	/**
	 * Close the connection with an NXT brick if it is no more used
	 */
//	public void closeConnection();
	
	public SensorPort getSensorPort(String portType);
	
//	/**
//	 * Configures the NXT brick using configuration properties
//	 * @param configurationProperties
//	 * @throws Exception
//	 */
//	public void configureNXT(Dictionary configurationProperties) throws Exception;
	
	// attention à override de configuration => à faire ici ou plus globalement?
	
	// TODO synchronized access control
	
}
