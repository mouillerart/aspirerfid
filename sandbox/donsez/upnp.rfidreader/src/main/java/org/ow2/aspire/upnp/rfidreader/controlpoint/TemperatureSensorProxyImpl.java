
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

	
public class TemperatureSensorProxyImpl implements TemperatureSensorModel {		

	private UPnPService upnpService;
	TemperatureSensorProxyImpl(UPnPService upnpService){
		super();
		this.upnpService=upnpService;
	}

	
	/**
	 * This method is "add description here"	
 * currentApplication out  parameter


	 */
	public void getApplication(
		StringHolder currentApplication
	) throws UPnPException {
		
		UPnPAction upnpAction = upnpService.getAction("getApplication");

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
 * newApplication in  parameter


	 */
	public void setApplication(
		java.lang.String newApplication
	) throws UPnPException {
		
		UPnPAction upnpAction = upnpService.getAction("setApplication");

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
 * currentTemp out  parameter


	 */
	public void getCurrentTemperature(
		IntegerHolder currentTemp
	) throws UPnPException {
		
		UPnPAction upnpAction = upnpService.getAction("getCurrentTemperature");

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
 * currentName out  parameter


	 */
	public void getName(
		StringHolder currentName
	) throws UPnPException {
		
		UPnPAction upnpAction = upnpService.getAction("getName");

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
 * newName in  parameter


	 */
	public void setName(
		java.lang.String newName
	) throws UPnPException {
		
		UPnPAction upnpAction = upnpService.getAction("setName");

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
		
		public void addListenerOnCurrentTemperatureStateVariableValue(/*TODO Listener listener*/){
			// TODO java.lang.Integer
			return;
		}		
		public void removeListenerOnCurrentTemperatureStateVariableValue(/*TODO Listener listener*/){
			// TODO
			return;
		}		
		
		// TODO current listeners list
		
		public void addListenerOnApplicationStateVariableValue(/*TODO Listener listener*/){
			// TODO java.lang.String
			return;
		}		
		public void removeListenerOnApplicationStateVariableValue(/*TODO Listener listener*/){
			// TODO
			return;
		}		
		
		// TODO current listeners list
		
		public void addListenerOnNameStateVariableValue(/*TODO Listener listener*/){
			// TODO java.lang.String
			return;
		}		
		public void removeListenerOnNameStateVariableValue(/*TODO Listener listener*/){
			// TODO
			return;
		}		
			
	
	
	// Those getters are used for ??? may be not useful !!!
	
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
