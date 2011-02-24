package fr.imag.adele.devices.tikitag.impl;

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
import org.ow2.chameleon.usb.descriptor.UsbDevice;
import org.ow2.chameleon.usb.service.USBBridge;

import fr.imag.adele.devices.tikitag.service.TikitagDescriptor;
import fr.imag.adele.devices.tikitag.service.TikitagManager;

/**
 * Tikitag
 * 
 * @author Daniel Lovera and Clement Deschamps and Mehdi Damou
 **/

public class TikitagManagerImpl implements TikitagManager, Runnable {

	private Factory tikitagFactory;

	private static Logger logger = Logger.getLogger(TikitagManagerImpl.class
			.getName());

	public static final short TIKITAG_VENDOR_ID = 0x072F;
	public static final short TIKITAG_PRODUCT_ID = (short) 0x90CC;

	private ScheduledThreadPoolExecutor timer;
	private ScheduledFuture<?> schedFuture;

	private USBBridge usbManager;
	private ConcurrentMap<String, ComponentInstance> tikitagComponents;
	private Hashtable<String, Object> configuration;
	private ConcurrentMap<String, TikitagDescriptor> tikitagDescriptorMap = null;

	private static final String TIKITAG_ID_PROPERTY = "tikitag.id";

	public void start() {
		logger.debug(">>> Tikitag Management Starting...");
		tikitagComponents = new ConcurrentHashMap<String, ComponentInstance>();
		configuration = new Hashtable<String, Object>();
		tikitagDescriptorMap = new ConcurrentHashMap<String, TikitagDescriptor>();

		this.timer = new ScheduledThreadPoolExecutor(1);
		this.schedFuture = this.timer.scheduleWithFixedDelay(this, 2, 2,
				TimeUnit.SECONDS);

	}

	public void stop() {
		logger.debug(">>> Tikitag Management Stopping...");
		schedFuture.cancel(false);
		timer.purge();
	}

	public ConcurrentMap<String, TikitagDescriptor> getListTikitag() {

		Map<String, UsbDevice> tikitagDeviceMap = usbManager.findDevices(
				TIKITAG_VENDOR_ID, TIKITAG_PRODUCT_ID);

		if (tikitagDeviceMap != null && !tikitagDeviceMap.isEmpty()) {
			Set<String> devNumbers = new HashSet<String>();

			// check if new devices have been added
			for (String id : tikitagDeviceMap.keySet()) {

				devNumbers.add(id);
				if (!tikitagDescriptorMap.containsKey(id)) {
					UsbDevice usbDev = tikitagDeviceMap.get(id);
					tikitagDescriptorMap.put(id, new TikitagDescriptor(id,
							usbDev, usbManager));
				}
			}

			// check if devices have been removed
			Set<String> tikitagIds = new HashSet<String>(tikitagDescriptorMap
					.keySet());
			for (String tikitagId : tikitagIds) {
				if (!devNumbers.contains(tikitagId)) {
					tikitagDescriptorMap.remove(tikitagId);
				}

			}

		} else {
			tikitagDescriptorMap.clear();
		}
		return tikitagDescriptorMap;
	}

	public TikitagDescriptor getTikitagbyID(String i) {
		return getListTikitag().get(i);
	}

	private void updateTikitagcomponents() {
		ConcurrentMap<String, TikitagDescriptor> tikitagList = getListTikitag();
		if (tikitagList != null && tikitagList.size() > 0) {

			// First, detect removed Tikitags
			Set<String> tikitags = new HashSet<String>();
			tikitags.addAll(tikitagComponents.keySet());
			for (String tikitagID : tikitags/* tikitagComponents.keySet() */) {
				// compare to updated list
				if (!tikitagList.containsKey(tikitagID)) {
					// this tikitag has been removed, dispose the instance
					ComponentInstance inst = tikitagComponents.remove(tikitagID);
					inst.dispose();
				}
			}

			// Second, detect new tikitags

			for (String tikitagID : tikitagList.keySet()) {
				// compare to local list
				if (!tikitagComponents.containsKey(tikitagID)) {
					// this tikitag is unknown, create an instance
					// create a tikitag component instance

					configuration.clear();
					configuration.put("instance.name", "tikitag-inst-"
							+ tikitagID);
					configuration.put("service.pid", tikitagFactory.getName()
							+ "@{DEVICE_NUMBER:" + tikitagID + "}");
					configuration.put(TIKITAG_ID_PROPERTY, tikitagID);
					try {
						ComponentInstance instance = tikitagFactory
								.createComponentInstance(configuration);
						tikitagComponents.put(tikitagID, instance);
					} catch (Exception e) {
						// System.err.println("Cannot create the instance : " +
						// e.getMessage());
						e.printStackTrace();
					}
				}
			}

		} else {
			// the updated list is empty => there is no more tikitags
			if (tikitagComponents.size() > 0) {
				// dispose all
				Collection<ComponentInstance> instances = tikitagComponents
						.values();
				for (ComponentInstance inst : instances) {
					inst.dispose();
				}
				// and clear the entries
				tikitagComponents.clear();
			} else {
				// null received

			}

		}

	}

	public void run() {
		updateTikitagcomponents();
	}

}
