
/*
__BANNER__
*/
// this file was generated at 21-January-2011 12:56 AM by ${author}
package org.ow2.aspire.upnp.rfidreader.impl;

import org.apache.felix.upnp.devicegen.holder.*;
import org.osgi.service.upnp.UPnPException;
import org.ow2.aspire.upnp.rfidreader.model.*;

	
public class TemperatureSensorModelImpl implements TemperatureSensorModel {		
	
	/**
	 * This method is "add description here"	
 * currentApplication out  parameter


	 */
	public void getApplication(
		StringHolder currentApplication
	) throws UPnPException {
		// TODO
		throw new UPnPException(UPnPException.DEVICE_INTERNAL_ERROR,"Not implemented");
	}


	/**
	 * This method is "add description here"	
 * newApplication in  parameter


	 */
	public void setApplication(
		java.lang.String newApplication
	) throws UPnPException {
		// TODO
		throw new UPnPException(UPnPException.DEVICE_INTERNAL_ERROR,"Not implemented");
	}


	/**
	 * This method is "add description here"	
 * currentTemp out  parameter


	 */
	public void getCurrentTemperature(
		IntegerHolder currentTemp
	) throws UPnPException {
		// TODO
		throw new UPnPException(UPnPException.DEVICE_INTERNAL_ERROR,"Not implemented");
	}


	/**
	 * This method is "add description here"	
 * currentName out  parameter


	 */
	public void getName(
		StringHolder currentName
	) throws UPnPException {
		// TODO
		throw new UPnPException(UPnPException.DEVICE_INTERNAL_ERROR,"Not implemented");
	}


	/**
	 * This method is "add description here"	
 * newName in  parameter


	 */
	public void setName(
		java.lang.String newName
	) throws UPnPException {
		// TODO
		throw new UPnPException(UPnPException.DEVICE_INTERNAL_ERROR,"Not implemented");
	}

	

	// Those getters are used for the first notification just after a subscription
	
		public java.lang.Integer getCurrentTemperatureStateVariableValue(){
			// TODO
			return null;
		}		
		
		public java.lang.String getApplicationStateVariableValue(){
			// TODO
			return null;
		}		
		
		public java.lang.String getNameStateVariableValue(){
			// TODO
			return null;
		}		
			
}
