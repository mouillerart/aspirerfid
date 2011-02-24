package fr.imag.adele.devices.mirror.impl;

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

import fr.imag.adele.devices.mirror.service.MirrorDescriptor;
import fr.imag.adele.devices.mirror.service.MirrorManager;

/**
 * Mirror
 * 
 * @author Mehdi
 *
 */
public class MirrorManagerImpl implements MirrorManager, Runnable {

	private Factory mirrorFactory;
	
	public static final short MIRROR_VENDOR_ID = 0x1DA8;
	public static final short MIRROR_PRODUCT_ID = 0x1301;
	
	private ScheduledThreadPoolExecutor timer;
	private ScheduledFuture<?> schedFuture;

	private USBBridge usbManager;
	private ConcurrentMap<String, ComponentInstance> mirrorComponents;
	private Hashtable<String, Object> configuration;
	private ConcurrentMap<String, MirrorDescriptor> mirrorDescriptorMap = null;
	
	private static final String MIRROR_ID_PROPERTY = "mirror.id";

	
	private static Logger logger = Logger.getLogger(MirrorManagerImpl.class
			.getName());
	

	
	public void start() {
		logger.debug(">>> Mirror Management Starting...");
		mirrorComponents = new ConcurrentHashMap<String, ComponentInstance>();
		configuration = new Hashtable<String, Object>();
		mirrorDescriptorMap = new ConcurrentHashMap<String, MirrorDescriptor>();

		this.timer = new ScheduledThreadPoolExecutor(1);
		this.schedFuture = this.timer.scheduleWithFixedDelay(this, 2, 2,
				TimeUnit.SECONDS);

	}

	public void stop() {
		logger.debug(">>> Mirror Management Stopping...");
		schedFuture.cancel(false);
		timer.purge();
	}

	
	
	public ConcurrentMap<String, MirrorDescriptor> getListMirror() {
		Map<String, UsbDevice> mirrorDeviceMap = usbManager.findDevices(
				MIRROR_VENDOR_ID, MIRROR_PRODUCT_ID);

		if (mirrorDeviceMap != null && !mirrorDeviceMap.isEmpty()) {
			Set<String> devNumbers = new HashSet<String>();

			// check if new devices have been added
			for (String id : mirrorDeviceMap.keySet()) {

				devNumbers.add(id);
				if (!mirrorDescriptorMap.containsKey(id)) {
					UsbDevice usbDev = mirrorDeviceMap.get(id);
					mirrorDescriptorMap.put(id, new MirrorDescriptor(id,
							usbDev, usbManager));
				}
			}

			// check if devices have been removed
			Set<String> mirrorIds = new HashSet<String>(mirrorDescriptorMap
					.keySet());
			for (String mirrorId : mirrorIds) {
				if (!devNumbers.contains(mirrorId)) {
					mirrorDescriptorMap.remove(mirrorId);
				}

			}

		} else {
			mirrorDescriptorMap.clear();
		}
		return mirrorDescriptorMap;
	}

	public MirrorDescriptor getMirrorbyID(String i) {
		return getListMirror().get(i);
	}
	

	private void updateMirrorComponents() {
		ConcurrentMap<String, MirrorDescriptor> mirrorList = getListMirror();
		if (mirrorList != null && mirrorList.size() > 0) {

			// First, detect removed Mirros
			Set<String> mirrors = new HashSet<String>();
			mirrors.addAll(mirrorComponents.keySet());
			for (String mirrorID : mirrors/* mirrorComponents.keySet() */) {
				// compare to updated list
				if (!mirrorList.containsKey(mirrorID)) {
					// this mirror has been removed, dispose the instance
					ComponentInstance inst = mirrorComponents.remove(mirrorID);
					inst.dispose();
				}
			}

			// Second, detect new mirrors

			for (String mirrorID : mirrorList.keySet()) {
				// compare to local list
				if (!mirrorComponents.containsKey(mirrorID)) {
					// this mirror is unknown, create an instance
					// create a mirror component instance

					configuration.clear();
					configuration.put("instance.name", "mirror-inst-"
							+ mirrorID);
					configuration.put("service.pid", mirrorFactory.getName()
							+ "@{DEVICE_NUMBER:" + mirrorID + "}");
					configuration.put(MIRROR_ID_PROPERTY, mirrorID);
					try {
						ComponentInstance instance = mirrorFactory
								.createComponentInstance(configuration);
						mirrorComponents.put(mirrorID, instance);
					} catch (Exception e) {
						// System.err.println("Cannot create the instance : " +
						// e.getMessage());
						e.printStackTrace();
					}
				}
			}

		} else {
			// the updated list is empty => there is no more mirrors
			if (mirrorComponents.size() > 0) {
				// dispose all
				Collection<ComponentInstance> instances = mirrorComponents
						.values();
				for (ComponentInstance inst : instances) {
					inst.dispose();
				}
				// and clear the entries
				mirrorComponents.clear();
			} else {
				// null received

			}

		}

	}

	public void run() {
		updateMirrorComponents();
	}

}
