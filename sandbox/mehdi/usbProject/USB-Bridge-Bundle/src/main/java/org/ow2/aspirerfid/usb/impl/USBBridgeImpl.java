/**
 * Copyright (c) 2008-2010, AspireRFID
 *
 * This library is free software; you can redistribute it and/or
 * modify it either under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation
 * (the "LGPL"). If you do not alter this
 * notice, a recipient may use your version of this file under the LGPL.
 *
 * You should have received a copy of the LGPL along with this library
 * in the file COPYING-LGPL-2.1; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTY
 * OF ANY KIND, either express or implied. See the LGPL  for
 * the specific language governing rights and limitations.
 *
 * Contact: AspireRFID mailto:aspirerfid@ow2.org
 */

package org.ow2.aspirerfid.usb.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.ow2.aspirerfid.usb.USBBridge;
import org.ow2.aspirerfid.usb.descriptor.UsbDevice;

import ch.ntb.usb.LibusbJava;
import ch.ntb.usb.Usb_Bus;
import ch.ntb.usb.Usb_Device;
import ch.ntb.usb.Usb_Device_Descriptor;

public class USBBridgeImpl implements USBBridge, Runnable {

	public static ConcurrentMap<String, UsbDevice> allUsbDevice = null;

	public static Usb_Bus busUSB;

	private ScheduledThreadPoolExecutor timer;

	private ScheduledFuture<?> schedFuture;

	private int libusbJava_debug;

	/**
	 * TODO : replace it with LogService
	 */
	private static Logger logger = Logger.getLogger(USBBridgeImpl.class	.getName());

	public void start() {
		// if you don't use the ch.ntb.usb.Device class you must initialise
		// Libusb before use Device device ;
		initialzing();
		allUsbDevice = new ConcurrentHashMap<String, UsbDevice>();
		updateListDevice();
		logger.info(" - USB Bridge Started (" + allUsbDevice.size()+" devices)");
		this.timer = new ScheduledThreadPoolExecutor(1);
		this.schedFuture = this.timer.scheduleWithFixedDelay(this, 2, 2,TimeUnit.SECONDS);
	}

	private void initialzing() {
		LibusbJava.usb_init();
		// pour le debogage
		LibusbJava.usb_set_debug(libusbJava_debug); 
		LibusbJava.usb_find_busses();
		LibusbJava.usb_find_devices();
		busUSB = LibusbJava.usb_get_busses();
	}

	private synchronized void updateListDevice() {
		LibusbJava.usb_find_devices();
		busUSB = LibusbJava.usb_get_busses();

		Usb_Device libUsbDevicetemp = null;
		Usb_Bus busTemp = busUSB;
		UsbDevice deviceDescriptor = null;
		Set<String> currentDevice = new HashSet<String>();
		while (busTemp != null) {
			libUsbDevicetemp = busTemp.getDevices();
			while (libUsbDevicetemp != null) {
				Usb_Device_Descriptor descriptor = libUsbDevicetemp.getDescriptor();
				currentDevice.add(libUsbDevicetemp.getFilename());
				deviceDescriptor = new UsbDevice(descriptor.getIdVendor(),	descriptor.getIdProduct());
				deviceDescriptor.setNum(libUsbDevicetemp.getDevnum());
				deviceDescriptor.setFilename(libUsbDevicetemp.getFilename());
				deviceDescriptor.setLibUsbDevice(libUsbDevicetemp);
				deviceDescriptor.setLibUsbDeviceDescriptor(descriptor);

				if (!allUsbDevice.containsValue(deviceDescriptor)) {
					// if the device is unknown, add it to the list
					deviceDescriptor.setOnUse(false);
					deviceDescriptor.setPresent(true);
					allUsbDevice.put(deviceDescriptor.getFilename(),deviceDescriptor);

				}


				//TODO else { // looking for the device in the list
				
				libUsbDevicetemp = libUsbDevicetemp.getNext();
			}
			busTemp = busTemp.getNext();
		}

		checkDisapearDevice(currentDevice);
		logger.debug(" : "+allUsbDevice.size() + " devices found." );
	}

	private void checkDisapearDevice(Set<String> currentDevice) {

		for (String filename : allUsbDevice.keySet()) {
			if (!currentDevice.contains(filename) ) {
				if (allUsbDevice.get(filename).isPresent()){
					allUsbDevice.get(filename).setPresent(false);
					allUsbDevice.keySet().remove(filename);
					allUsbDevice.remove(filename);
				}
			}
			else {//TODO			
			}
			
		}
	}

	public UsbDevice getDevice(String id) {
		return allUsbDevice.get(id);
	}

	/**
	 * 
	 */
	public Map<String, UsbDevice> findDevices(short vendorID, short productID) {
		Map<String, UsbDevice> devicesTemp = new HashMap<String, UsbDevice>();
		for (String idUsbDevice : allUsbDevice.keySet()) {
			long tempVdId = allUsbDevice.get(idUsbDevice).getVendorId();
			long tempPrdId = allUsbDevice.get(idUsbDevice).getProductId();
			if ((tempVdId == vendorID) && (tempPrdId == productID)) {
				devicesTemp.put(idUsbDevice, allUsbDevice.get(idUsbDevice));
			}
		}
		return devicesTemp;
	}

	public void stop() {
		schedFuture.cancel(false);
		timer.purge();
		logger.info("USB Bridge stop.");
	}

	private synchronized Usb_Device findUsbDevice(short vendorID, short productID,String filename) {
		Usb_Device temp = null;
		Usb_Device device = null;
		Usb_Device_Descriptor descriptor = null;
		boolean trouve = false;
		Usb_Bus tempbus = busUSB;
		while (!trouve && (tempbus != null)) {
			temp = tempbus.getDevices();
			while (!trouve && (temp != null)) {
				descriptor = temp.getDescriptor();
				logger.debug("USB Device : VendorID: "	+ Integer.toHexString(descriptor.getIdVendor() & 0xFFFF)
								      + ", ProductID: "	+ Integer.toHexString(descriptor.getIdProduct() & 0xFFFF));

				if (((short) descriptor.getIdVendor() == (short) vendorID)	&& ((short) descriptor.getIdProduct() == (short) productID) && (temp.getFilename().equals(filename))) {
					trouve = true;
					device = temp;
				}
				temp = temp.getNext();
			}
			tempbus = tempbus.getNext();
		}
		return device;
	}

	/**
	 * @see org.ow2.aspirerfid.usb.USBBridge#bulk_read(UsbDevice, int, byte[],
	 *      int, int)
	 */
	public int bulk_read(UsbDevice usbDevice, int ep, byte[] bytes, int size,
			int timeout) {
		Long dev_handle = usbDevice.getHandle();
		return LibusbJava.usb_bulk_read(dev_handle, ep, bytes, size, timeout);
	}

	/**
	 * @see org.ow2.aspirerfid.usb.USBBridge#bulk_write(UsbDevice, int, byte[],
	 *      int, int)
	 */
	public int bulk_write(UsbDevice usbDevice, int ep, byte[] bytes, int size,
			int timeout) {
		Long dev_handle = usbDevice.getHandle();
		return LibusbJava.usb_bulk_write(dev_handle, ep, bytes, size, timeout);
	}

	/**
	 * @see org.ow2.aspirerfid.usb.USBBridge#claim_interface(UsbDevice, int)
	 */
	public int claim_interface(UsbDevice usbDevice, int interface_) {
		Long dev_handle = usbDevice.getHandle();
		return LibusbJava.usb_claim_interface(dev_handle, interface_);
	}

	/**
	 * @see org.ow2.aspirerfid.usb.USBBridge#clear_halt(UsbDevice, int)
	 */
	public int clear_halt(UsbDevice usbDevice, int ep) {
		Long dev_handle = usbDevice.getHandle();
		return LibusbJava.usb_clear_halt(dev_handle, ep);
	}

	/**
	 * @see org.ow2.aspirerfid.usb.USBBridge#close(UsbDevice)
	 */
	public int close(UsbDevice usbDevice) {
		Long dev_handle = usbDevice.getHandle();
		return LibusbJava.usb_close(dev_handle);
	}

	/**
	 * @see org.ow2.aspirerfid.usb.USBBridge#control_msg(UsbDevice, int, int, int,
	 *      int, byte[], int, int)
	 */
	public int control_msg(UsbDevice usbDevice, int requesttype, int request,
			int value, int index, byte[] bytes, int size, int timeout) {
		Long dev_handle = usbDevice.getHandle();
		return LibusbJava.usb_control_msg(dev_handle, requesttype, request,
				value, index, bytes, size, timeout);
	}

	/**
	 * @see org.ow2.aspirerfid.usb.USBBridge#get_descriptor_by_endpoint(UsbDevice,
	 *      int, byte, byte, int)
	 */
	public String get_descriptor_by_endpoint(UsbDevice usbDevice, int ep,
			byte type, byte index, int size) {
		Long dev_handle = usbDevice.getHandle();
		return LibusbJava.usb_get_descriptor_by_endpoint(dev_handle, ep, type,
				index, size);
	}

	/**
	 * @see org.ow2.aspirerfid.usb.USBBridge#getDevice(Long)
	 */
	public String get_descriptor(UsbDevice usbDevice, byte type, byte index,
			int size) {
		Long dev_handle = usbDevice.getHandle();
		return LibusbJava.usb_get_descriptor(dev_handle, type, index, size);
	}

	/**
	 * @see org.ow2.aspirerfid.usb.USBBridge#get_string_simple(UsbDevice, int)
	 */
	public String get_string_simple(UsbDevice usbDevice, int index) {
		Long dev_handle = usbDevice.getHandle();
		return LibusbJava.usb_get_string_simple(dev_handle, index);
	}

	/**
	 * @see org.ow2.aspirerfid.usb.USBBridge#get_string(UsbDevice, int, int)
	 */
	public String get_string(UsbDevice usbDevice, int index, int langid) {
		Long dev_handle = usbDevice.getHandle();
		return LibusbJava.usb_get_string(dev_handle, index, langid);
	}

	/**
	 * @see org.ow2.aspirerfid.usb.USBBridge#interrupt_read(UsbDevice, int,
	 *      byte[], int, int)
	 */
	public int interrupt_read(UsbDevice usbDevice, int ep, byte[] bytes,
			int size, int timeout) {
		Long dev_handle = usbDevice.getHandle();
		return LibusbJava.usb_interrupt_read(dev_handle, ep, bytes, size,
				timeout);
	}

	/**
	 * @see org.ow2.aspirerfid.usb.USBBridge#interrupt_write(UsbDevice, int,
	 *      byte[], int, int)
	 */
	public int interrupt_write(UsbDevice usbDevice, int ep, byte[] bytes,
			int size, int timeout) {
		Long dev_handle = usbDevice.getHandle();
		return LibusbJava.usb_interrupt_write(dev_handle, ep, bytes, size,
				timeout);
	}

	/**
	 * @see org.ow2.aspirerfid.usb.USBBridge#open(UsbDevice)
	 */
	public long open(UsbDevice usbDevice) {
		
		Usb_Device dev = findUsbDevice(usbDevice.getVendorId(), usbDevice.getProductId(), usbDevice.getFilename());
		long handle = LibusbJava.usb_open((Usb_Device) dev);
		usbDevice.setHandle(handle);
		return usbDevice.getHandle();
	}

	/**
	 * @see org.ow2.aspirerfid.usb.USBBridge#release_interface(UsbDevice, int)
	 */
	public int release_interface(UsbDevice usbDevice, int interface_) {
		Long dev_handle = usbDevice.getHandle();
		return LibusbJava.usb_release_interface(dev_handle, interface_);
	}

	/**
	 * @see org.ow2.aspirerfid.usb.USBBridge#usb_reset(UsbDevice)
	 */
	public int usb_reset(UsbDevice usbDevice) {
		Long dev_handle = usbDevice.getHandle();
		return LibusbJava.usb_reset(dev_handle);
	}

	/**
	 * @see org.ow2.aspirerfid.usb.USBBridge#set_altinterface(UsbDevice, int)
	 */
	public int set_altinterface(UsbDevice usbDevice, int alternate) {
		Long dev_handle = usbDevice.getHandle();
		return LibusbJava.usb_set_altinterface(dev_handle, alternate);
	}

	/**
	 * @see org.ow2.aspirerfid.usb.USBBridge#set_configuration(UsbDevice, int)
	 */
	public int set_configuration(UsbDevice usbDevice, int configuration) {
		Long dev_handle = usbDevice.getHandle();
		return LibusbJava.usb_set_configuration(dev_handle, configuration);
	}

	/**
	 * @see org.ow2.aspirerfid.usb.USBBridge#set_debug(int)
	 */
	public void set_debug(int level) {
		LibusbJava.usb_set_debug(level);
	}

	/**
	 * @see org.ow2.aspirerfid.usb.USBBridge#strerror()
	 */
	public String strerror() {
		return LibusbJava.usb_strerror();
	}

	public void run() {
		updateListDevice();

	}

	

}
