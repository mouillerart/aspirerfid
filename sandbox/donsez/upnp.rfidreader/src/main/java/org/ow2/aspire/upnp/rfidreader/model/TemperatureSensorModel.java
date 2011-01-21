
/*
__BANNER__
*/
// this file was generated at 21-January-2011 12:56 AM by ${author}
package org.ow2.aspire.upnp.rfidreader.model;

import org.apache.felix.upnp.devicegen.holder.*;
import org.osgi.service.upnp.UPnPException;

	
public interface TemperatureSensorModel {		
	
	/**
	 * This method is "add description here"	
 * currentApplication out  parameter


	 */
	public void getApplication(
		StringHolder currentApplication
	) throws UPnPException;
	// TODO return the type of the return argument when specified


	/**
	 * This method is "add description here"	
 * newApplication in  parameter


	 */
	public void setApplication(
		java.lang.String newApplication
	) throws UPnPException;
	// TODO return the type of the return argument when specified


	/**
	 * This method is "add description here"	
 * currentTemp out  parameter


	 */
	public void getCurrentTemperature(
		IntegerHolder currentTemp
	) throws UPnPException;
	// TODO return the type of the return argument when specified


	/**
	 * This method is "add description here"	
 * currentName out  parameter


	 */
	public void getName(
		StringHolder currentName
	) throws UPnPException;
	// TODO return the type of the return argument when specified


	/**
	 * This method is "add description here"	
 * newName in  parameter


	 */
	public void setName(
		java.lang.String newName
	) throws UPnPException;
	// TODO return the type of the return argument when specified

	

	// Those getters are used for the first notification just after a subscription
	
		public java.lang.Integer getCurrentTemperatureStateVariableValue();		
		
		public java.lang.String getApplicationStateVariableValue();		
		
		public java.lang.String getNameStateVariableValue();		
			
}
