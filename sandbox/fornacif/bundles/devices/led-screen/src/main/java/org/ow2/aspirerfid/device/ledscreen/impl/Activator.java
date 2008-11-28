/**
 * Copyright 2008, Aspire
 * 
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation (the "LGPL"); either version 2.1 of the 
 * License, or (at your option) any later version. If you do not alter this 
 * notice, a recipient may use your version of this file under either the 
 * LGPL version 2.1, or (at his option) any later version.
 * 
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this library; if not, write to the Free Software Foundation, 
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY 
 * KIND, either express or implied. See the GNU Lesser General Public 
 * License for the specific language governing rights and limitations.
 *
 * --------------------------------------------------------------------------
 * $Id$
 * --------------------------------------------------------------------------
 */

package org.ow2.aspirerfid.device.ledscreen.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.ow2.aspirerfid.device.ledscreen.LedScreenService;
import org.ow2.aspirerfid.device.serialcommunicator.SerialCommunicator;

public class Activator implements BundleActivator, ServiceListener {

	/**
	 * ServiceRegistration for the LedScreenService.
	 */
	private ServiceRegistration serviceRegistration = null;

	/**
	 * OSGi BundleContext.
	 */
	private BundleContext bundleContext;

	public void start(BundleContext bundleContext) throws Exception {
		this.bundleContext = bundleContext;
		// Try to register the LedScreenService service
		registerService();
		// Add service listener to be notified when the SerialCommunicator
		// service is registered or is unregistering
		bundleContext.addServiceListener(this, "(objectClass=" + SerialCommunicator.class.getName() + ")");
	}

	public void stop(BundleContext bundleContext) throws Exception {
	    unregisterService();
	}

	/**
	 * @see org.osgi.framework.ServiceListener#serviceChanged(org.osgi.framework.ServiceEvent)
	 */
	public void serviceChanged(ServiceEvent serviceEvent) {
		// The SerialCommunicator service is registered
		if (serviceEvent.getType() == ServiceEvent.REGISTERED) {
			try {
				registerService();
			} catch (Exception e) {
				System.err.println("Cannot register the " + LedScreenService.class.getName() + " service");
			}
		} else if (serviceEvent.getType() == ServiceEvent.UNREGISTERING) {
			// The SerialCommunicator is unregistering, so unregister the ThermometerService
			unregisterService();
		}

	}

	/**
	 * Register the LedScreenService service.
	 * @throws Exception If the service registration fails
	 */
	private void registerService() throws Exception {
		ServiceReference serviceReference = bundleContext.getServiceReference(SerialCommunicator.class.getName());
		if (serviceReference != null) {
			SerialCommunicator serialCommunicator = (SerialCommunicator) bundleContext.getService(serviceReference);
			serviceRegistration = bundleContext.registerService(LedScreenService.class.getName(), new LedScreenImpl(serialCommunicator),
					null);
		}
	}

	/**
	 * Unregister the {@link LedScreenService} service
	 */
	private void unregisterService() {
		if (serviceRegistration != null) {
			serviceRegistration.unregister();
			serviceRegistration = null;
		}
	}

}
