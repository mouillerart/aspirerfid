/*
 __BANNER__
 */
// this file was generated at 11-July-2010 04:42 PM by ${author}
package org.ow2.aspire.upnp.rfidreader.model;

import org.apache.felix.upnp.devicegen.holder.*;
import org.osgi.service.upnp.UPnPException;

public interface LowPowerDeviceModel {

	// Error codes for GoToSleep section 2.5.3.5

	public final static int ERROR_GOTOSLEEP_REJECTPOWERSTATE = 705;
	public final static int ERROR_GOTOSLEEP_POWERSTATE_NOT_IMPLEMENTED = 706;

	/**
	 * This method is "add description here" wakeupMethod out parameter
	 * 
	 * powerSupplyStatus out parameter
	 */
	public void getPowerManagementInfo(StringHolder wakeupMethod,

	StringHolder powerSupplyStatus) throws UPnPException;

	// TODO return the type of the return argument when specified

	/**
	 * This method is "add description here" recommendedSleepPeriod in parameter
	 * 
	 * recommendedPowerState in parameter
	 * 
	 * sleepPeriod out parameter
	 * 
	 * powerState out parameter
	 */
	public void goToSleep(int recommendedSleepPeriod,
			String recommendedPowerState, IntegerHolder sleepPeriod,
			StringHolder powerState) throws UPnPException;

	/**
	 * This method is "add description here"
	 */
	public void wakeup(

	) throws UPnPException;

	// TODO return the type of the return argument when specified

	// Those getters are used for the first notification just after a
	// subscription

	public java.lang.Integer getExternalPowerSupplySourceStateVariableValue();

	public java.lang.Boolean getBatteryLowStateVariableValue();

}
