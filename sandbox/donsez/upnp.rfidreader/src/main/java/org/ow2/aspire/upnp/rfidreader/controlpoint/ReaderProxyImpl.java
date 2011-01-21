
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

	
public class ReaderProxyImpl implements ReaderModel {		

	private UPnPService upnpService;
	ReaderProxyImpl(UPnPService upnpService){
		super();
		this.upnpService=upnpService;
	}

	
	/**
	 * This method is "add description here"	
 * newDuration in  parameter


	 */
	public void setDuration(
		long newDuration
	) throws UPnPException {
		
		UPnPAction upnpAction = upnpService.getAction("setDuration");

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
 * currentDuration out  parameter


	 */
	public void getDuration(
		LongHolder currentDuration
	) throws UPnPException {
		
		UPnPAction upnpAction = upnpService.getAction("getDuration");

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
 * currentProperties out  parameter


	 */
	public void getProperties(
		StringHolder currentProperties
	) throws UPnPException {
		
		UPnPAction upnpAction = upnpService.getAction("getProperties");

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
		
		public void addListenerOnDurationStateVariableValue(/*TODO Listener listener*/){
			// TODO java.lang.Long
			return;
		}		
		public void removeListenerOnDurationStateVariableValue(/*TODO Listener listener*/){
			// TODO
			return;
		}		
		
		// TODO current listeners list
		
		public void addListenerOnReportMembersStateVariableValue(/*TODO Listener listener*/){
			// TODO java.lang.String
			return;
		}		
		public void removeListenerOnReportMembersStateVariableValue(/*TODO Listener listener*/){
			// TODO
			return;
		}		
			
	
	
	// Those getters are used for ??? may be not useful !!!
	
		public java.lang.Long getDurationStateVariableValue(){
			// TODO
			return null;
		}		
		
		public java.lang.String getReportMembersStateVariableValue(){
			// TODO
			return null;
		}		
			
	
	
}
