
/*
__BANNER__
*/
// this file was generated at 21-January-2011 12:56 AM by ${author}
package org.ow2.aspire.upnp.rfidreader.controlpoint;

import org.apache.felix.upnp.devicegen.holder.*;
import org.ow2.aspire.upnp.rfidreader.model.*;

import java.util.Dictionary;
import java.util.Hashtable;
import org.osgi.service.upnp.UPnPAction;
import org.osgi.service.upnp.UPnPException;
import org.osgi.service.upnp.UPnPService;

	
public class LowPowerDeviceProxyImpl implements LowPowerDeviceModel {		

	private UPnPService upnpService;
	LowPowerDeviceProxyImpl(UPnPService upnpService){
		super();
		this.upnpService=upnpService;
	}

	
	/**
	 * This method is "add description here"	
 * wakeupMethod out  parameter

 * powerSupplyStatus out  parameter


	 */
	public void getPowerManagementInfo(
		StringHolder wakeupMethod,

StringHolder powerSupplyStatus
	) throws UPnPException {
		
		UPnPAction upnpAction = upnpService.getAction("getPowerManagementInfo");

		Dictionary _parameters = new Hashtable();
		
		// TODO _parameters.put("InParam", inparam);

		Dictionary _result;
		try {
				_result = upnpAction.invoke(_parameters);
		} catch (UPnPException e) {
			throw e;
		} catch (Exception e) {
			throw new UPnPException(UPnPException.DEVICE_INTERNAL_ERROR,e.getMessage());
		}

		// TODO outparam.setType((Type) _result.get("OutParam"));
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
		
		UPnPAction upnpAction = upnpService.getAction("goToSleep");

		Dictionary _parameters = new Hashtable();
		
		// TODO _parameters.put("InParam", inparam);

		Dictionary _result;
		try {
				_result = upnpAction.invoke(_parameters);
		} catch (UPnPException e) {
			throw e;
		} catch (Exception e) {
			throw new UPnPException(UPnPException.DEVICE_INTERNAL_ERROR,e.getMessage());
		}

		// TODO outparam.setType((Type) _result.get("OutParam"));
	}


	/**
	 * This method is "add description here"	

	 */
	public void wakeup(
		
	) throws UPnPException {
		
		UPnPAction upnpAction = upnpService.getAction("wakeup");

		Dictionary _parameters = new Hashtable();
		
		// TODO _parameters.put("InParam", inparam);

		Dictionary _result;
		try {
				_result = upnpAction.invoke(_parameters);
		} catch (UPnPException e) {
			throw e;
		} catch (Exception e) {
			throw new UPnPException(UPnPException.DEVICE_INTERNAL_ERROR,e.getMessage());
		}

		// TODO outparam.setType((Type) _result.get("OutParam"));
	}

	

	// Those listeners are used for the subscription to state variable change
	
		// TODO current listeners list
		
		public void addListenerOnExternalPowerSupplySourceStateVariableValue(/*TODO Listener listener*/){
			// TODO java.lang.Integer
			return;
		}		
		public void removeListenerOnExternalPowerSupplySourceStateVariableValue(/*TODO Listener listener*/){
			// TODO
			return;
		}		
		
		// TODO current listeners list
		
		public void addListenerOnBatteryLowStateVariableValue(/*TODO Listener listener*/){
			// TODO java.lang.Boolean
			return;
		}		
		public void removeListenerOnBatteryLowStateVariableValue(/*TODO Listener listener*/){
			// TODO
			return;
		}		
			
	
	
	// Those getters are used for ??? may be not useful !!!
	
		public java.lang.Integer getExternalPowerSupplySourceStateVariableValue(){
			// TODO
			return null;
		}		
		
		public java.lang.Boolean getBatteryLowStateVariableValue(){
			// TODO
			return null;
		}		
			
	
	
}
