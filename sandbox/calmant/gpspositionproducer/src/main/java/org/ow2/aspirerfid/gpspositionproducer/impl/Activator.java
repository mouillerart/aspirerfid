/*
 * Simple GPS Position Provider
 * read NMEA0183 sentences from a serial GPS receiver
 * and keep the last position
 * 
 * Copyright (C) 2003 Didier Donsez
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Library General Public License for more details.
 * 
 * You should have received a copy of the GNU Library General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * Contact: Didier Donsez (Didier.Donsez@ieee.org)
 * Contributor(s):
 */
package org.ow2.aspirerfid.gpspositionproducer.impl;

import java.io.InputStream;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.wireadmin.Producer;
import org.osgi.util.position.Position;

/**
 * This activator read configuration properties from a file
 * and initialize a GPSReader service with these properties
 * and register it
 * 
 * @version 1.00 22/03/2003
 * @author Didier Donsez (Didier.Donsez@ieee.org)
 */

public class Activator implements BundleActivator {

	private static final String CONFIGFILE = "/CONFIG-INF/config.properties";

	private BundleContext context = null;

	private GPSToolProducer producer;

	private ServiceRegistration servreg;

	/**
	 * Load the configuration properties and register e new service
	 */
	private void initialize() throws Exception {

		if (context == null)
			return;

		// Get the Config-Location value from the manifest

		String configLocation = null;
		// Properties props = null;
		Dictionary dict = context.getBundle().getHeaders();
		Enumeration enumeration = dict.keys();
		while (enumeration.hasMoreElements()) {
			Object nextKey = enumeration.nextElement();
			Object nextElem = dict.get(nextKey);
			if (nextKey.equals("Config-Location")) {
				configLocation = nextElem.toString();
				break;
			}
		}
		if (configLocation == null) {
			configLocation = CONFIGFILE;
		}
		// System.out.println(this.getClass().getName()+" starting");

		// Load properties from configLocation file
		// and create and register a service
		InputStream sourcestream = getClass().getResourceAsStream(
				configLocation);

		producer = new GPSToolProducer(sourcestream);
		// sourcestream.close(sourcestream);
		try {
			Hashtable attr = new Hashtable();
			attr.put(
					org.osgi.service.wireadmin.WireConstants.WIREADMIN_PRODUCER_FLAVORS,
					new Class[] { Position.class, String.class });
			attr.put(org.osgi.framework.Constants.SERVICE_PID,
					"fr.imag.adele.bundle.gpspositionproducer");
			attr.put(org.osgi.framework.Constants.SERVICE_DESCRIPTION,
					"A service that polls positions from a GPS receiver");
			attr.put("PositioningMethod", "GPS");

			servreg = context.registerService(Producer.class.getName(),
					producer, attr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println(this.getClass().getName()+" started");
	}

	/**
	 * Called upon starting of the bundle. This method invokes initialize()
	 * which
	 * load the configuration file, creates the dependency managers and
	 * registers the
	 * eventual services.
	 * 
	 * @param context
	 *            The bundle context passed by the framework
	 * @exception Exception
	 */
	public void start(final BundleContext context) throws Exception {
		this.context = context;
		initialize();
	}

	public void stop(final BundleContext context) throws Exception {
		// System.out.println(this.getClass().getName()+" stopped");
		if (servreg != null)
			servreg.unregister();
		if (producer != null)
			producer.close();
		producer = null;
	}
}
