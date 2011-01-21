
/*
__BANNER__
*/
// this file was generated at 21-January-2011 12:56 AM by ${author}
package org.ow2.aspire.upnp.rfidreader.impl;

import org.apache.felix.upnp.devicegen.holder.*;
import org.osgi.service.upnp.UPnPException;
import org.ow2.aspire.upnp.rfidreader.model.*;

	
public class LowPowerDeviceModelImpl implements LowPowerDeviceModel {		
	
	/**
	 * This method is "add description here"	
 * wakeupMethod out  parameter

 * powerSupplyStatus out  parameter


	 */
	public void getPowerManagementInfo(
		StringHolder wakeupMethod,

StringHolder powerSupplyStatus
	) throws UPnPException {
		// TODO
		throw new UPnPException(UPnPException.DEVICE_INTERNAL_ERROR,"Not implemented");
	}


	/**
	 * This method is "add description here"	
 * recommendedSleepPeriod in  parameter

 * recommendedPowerState in  parameter

 * sleepPeriod out  parameter

 * powerState out  parameter


	 */
	public void goToSleep(
		int recommendedSleepPeriod,

java.lang.String recommendedPowerState,

IntegerHolder sleepPeriod,

StringHolder powerState
	) throws UPnPException {
		// TODO
		throw new UPnPException(UPnPException.DEVICE_INTERNAL_ERROR,"Not implemented");
	}


	/**
	 * This method is "add description here"	

	 */
	public void wakeup(
		
	) throws UPnPException {
		// TODO
		throw new UPnPException(UPnPException.DEVICE_INTERNAL_ERROR,"Not implemented");
	}

	

	// Those getters are used for the first notification just after a subscription
	
		public java.lang.Integer getExternalPowerSupplySourceStateVariableValue(){
			// TODO
			return null;
		}		
		
		public java.lang.Boolean getBatteryLowStateVariableValue(){
			// TODO
			return null;
		}		
			
}
