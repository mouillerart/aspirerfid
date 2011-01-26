/*
 * Copyright 2005-2008, Aspire
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
 */
package org.ow2.aspirerfid.sensor.wmr200.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.felix.ipojo.ComponentInstance;
import org.apache.felix.ipojo.Factory;
import org.apache.log4j.Logger;
import org.ow2.aspirerfid.sensor.wmr200.service.WMR200Descriptor;
import org.ow2.aspirerfid.sensor.wmr200.service.WMR200Manager;
import org.ow2.chameleon.usb.descriptor.UsbDevice;
import org.ow2.chameleon.usb.service.USBBridge;



/**
 * WMR200 Manager.
 *
 * @author ElMehdi Damou
 */

public class WMR200ManagerImpl implements WMR200Manager, Runnable {

	/** The wmr200 factory. */
	private Factory wmr200Factory;

	/** The logger. */
	private static Logger logger = Logger.getLogger(WMR200ManagerImpl.class
			.getName());

	/** The Constant WMR200_VENDOR_ID. */
	public static final short WMR200_VENDOR_ID = 0x0FDE;
	
	/** The Constant WMR200_PRODUCT_ID. */
	public static final short WMR200_PRODUCT_ID = (short) 0xCA01;

	/** The timer. */
	private ScheduledThreadPoolExecutor timer;
	
	/** The sched future. */
	private ScheduledFuture<?> schedFuture;

	/** The usb bridge. */
	private USBBridge usbBridge;
	
	/** The wmr200 components. */
	private ConcurrentMap<String, ComponentInstance> wmr200Components;
	
	/** The configuration. */
	private Hashtable<String, Object> configuration;
	
	/** The wmr200 descriptor map. */
	private ConcurrentMap<String, WMR200Descriptor> wmr200DescriptorMap = null;

	/** The Constant wmr200_ID_PROPERTY. */
	private static final String wmr200_ID_PROPERTY = "wmr200.id";

	/**
	 * Start.
	 */
	public void start() {
		logger.debug(">>> wmr200 Management Starting...");
		wmr200Components = new ConcurrentHashMap<String, ComponentInstance>();
		configuration = new Hashtable<String, Object>();
		wmr200DescriptorMap = new ConcurrentHashMap<String, WMR200Descriptor>();

		this.timer = new ScheduledThreadPoolExecutor(1);
		this.schedFuture = this.timer.scheduleWithFixedDelay(this, 2, 2,
				TimeUnit.SECONDS);

	}

	/**
	 * Stop.
	 */
	public void stop() {
		logger.debug(">>> wmr200 Management Stopping...");
		schedFuture.cancel(false);
		timer.purge();
	}

	/* (non-Javadoc)
	 * @see org.ow2.aspirerfid.sensor.wmr200.service.WMR200Manager#getListWMR200()
	 */
	public ConcurrentMap<String, WMR200Descriptor> getListWMR200() {

		Map<String,UsbDevice> wmr200DeviceMap = usbBridge.findDevices(WMR200_VENDOR_ID, WMR200_PRODUCT_ID);

		if (wmr200DeviceMap != null && !wmr200DeviceMap.isEmpty()) {
			Set<String> devNumbers = new HashSet<String>();

			// check if new devices have been added
			for (String id : wmr200DeviceMap.keySet()) {

				devNumbers.add(id);
				if (!wmr200DescriptorMap.containsKey(id)) {
					UsbDevice usbDev = wmr200DeviceMap.get(id);
					wmr200DescriptorMap.put(id, new WMR200Descriptor(id,
							usbDev, usbBridge));
				}
			}

			// check if devices have been removed
			Set<String> wmr200Ids = new HashSet<String>(wmr200DescriptorMap
					.keySet());
			for (String wmr200Id : wmr200Ids) {
				if (!devNumbers.contains(wmr200Id)) {
					wmr200DescriptorMap.remove(wmr200Id);
				}

			}

		} else {
			wmr200DescriptorMap.clear();
		}
		return wmr200DescriptorMap;
	}

	/* (non-Javadoc)
	 * @see org.ow2.aspirerfid.sensor.wmr200.service.WMR200Manager#getWMR200byID(java.lang.String)
	 */
	public WMR200Descriptor getWMR200byID(String i) {
		return getListWMR200().get(i);
	}

	/**
	 * Update wm r200components.
	 */
	private void updateWMR200components() {
		ConcurrentMap<String, WMR200Descriptor> wmr200List = getListWMR200();
		if (wmr200List != null && wmr200List.size() > 0) {

			// First, detect removed wmr200s
			Set<String> wmr200s = new HashSet<String>();
			wmr200s.addAll(wmr200Components.keySet());
			for (String wmr200ID : wmr200s/* wmr200Components.keySet() */) {
				// compare to updated list
				if (!wmr200List.containsKey(wmr200ID)) {
					// this wmr200 has been removed, dispose the instance
					ComponentInstance inst = wmr200Components.remove(wmr200ID);
					inst.dispose();
				}
			}

			// Second, detect new wmr200s

			for (String wmr200ID : wmr200List.keySet()) {
				// compare to local list
				if (!wmr200Components.containsKey(wmr200ID)) {
					// this wmr200 is unknown, create an instance
					// create a wmr200 component instance

					configuration.clear();
					configuration.put("instance.name", "wmr200-inst-"
							+ wmr200ID);
					configuration.put("service.pid", wmr200Factory.getName()
							+ "@{DEVICE_NUMBER:" + wmr200ID + "}");
					configuration.put(wmr200_ID_PROPERTY, wmr200ID);
					try {
						ComponentInstance instance = wmr200Factory
								.createComponentInstance(configuration);
						wmr200Components.put(wmr200ID, instance);
					} catch (Exception e) {
						// System.err.println("Cannot create the instance : " +
						// e.getMessage());
						e.printStackTrace();
					}
				}
			}

		} else {
			// the updated list is empty => there is no more wmr200s
			if (wmr200Components.size() > 0) {
				// dispose all
				Collection<ComponentInstance> instances = wmr200Components
						.values();
				for (ComponentInstance inst : instances) {
					inst.dispose();
				}
				// and clear the entries
				wmr200Components.clear();
			} else {
				// null received

			}

		}

	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		updateWMR200components();
	}



}
