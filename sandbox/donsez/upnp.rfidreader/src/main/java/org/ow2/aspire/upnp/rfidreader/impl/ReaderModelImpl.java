
/*
__BANNER__
*/
// this file was generated at 21-January-2011 12:56 AM by ${author}
package org.ow2.aspire.upnp.rfidreader.impl;

import org.apache.felix.upnp.devicegen.holder.*;
import org.osgi.service.upnp.UPnPException;
import org.ow2.aspire.upnp.rfidreader.model.*;

	
public class ReaderModelImpl implements ReaderModel {		
	
	/**
	 * This method is "add description here"	
 * newDuration in  parameter


	 */
	public void setDuration(
		long newDuration
	) throws UPnPException {
		// TODO
		throw new UPnPException(UPnPException.DEVICE_INTERNAL_ERROR,"Not implemented");
	}


	/**
	 * This method is "add description here"	
 * currentDuration out  parameter


	 */
	public void getDuration(
		LongHolder currentDuration
	) throws UPnPException {
		// TODO
		throw new UPnPException(UPnPException.DEVICE_INTERNAL_ERROR,"Not implemented");
	}


	/**
	 * This method is "add description here"	
 * currentProperties out  parameter


	 */
	public void getProperties(
		StringHolder currentProperties
	) throws UPnPException {
		// TODO
		throw new UPnPException(UPnPException.DEVICE_INTERNAL_ERROR,"Not implemented");
	}

	

	// Those getters are used for the first notification just after a subscription
	
		public java.lang.Long getDurationStateVariableValue(){
			// TODO
			return null;
		}		
		
		public java.lang.String getReportMembersStateVariableValue(){
			// TODO
			return null;
		}		
			
}
