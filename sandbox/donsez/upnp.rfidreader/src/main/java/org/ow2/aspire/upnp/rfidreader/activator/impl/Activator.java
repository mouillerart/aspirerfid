/*
 */
package org.ow2.aspire.upnp.rfidreader.activator.impl;

import org.ow2.aspire.upnp.rfidreader.device.RFIDReader;

import org.osgi.framework.*;
/**
 * @author Didier Donsez
 */
public class Activator implements BundleActivator {

	private RFIDReader rfidReader;
	
	public void start(BundleContext bundleContext) throws Exception {	
		rfidReader=new RFIDReader(bundleContext, null);
	}
	
	public void stop(BundleContext bundleContext) throws Exception {
		rfidReader.stop(bundleContext);
	}
}
