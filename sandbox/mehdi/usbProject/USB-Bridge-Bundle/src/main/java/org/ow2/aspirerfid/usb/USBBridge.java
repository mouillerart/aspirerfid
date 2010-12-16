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
package org.ow2.aspirerfid.usb;



import java.util.Map;

import org.ow2.aspirerfid.usb.descriptor.UsbDevice;



/**

*/
public interface USBBridge {


	/**
	 * 
	 * @param usbDevice
	 * @param ep
	 * @param bytes
	 * @param size
	 * @param timeout
	 * @return
	 */
	public int bulk_read(UsbDevice usbDevice, int ep, byte[] bytes, int size,
			int timeout);

	/**
	 * 
	 * @param usbDevice
	 * @param ep
	 * @param bytes
	 * @param size
	 * @param timeout
	 * @return
	 */
	public int bulk_write(UsbDevice usbDevice, int ep, byte[] bytes, int size,
			int timeout);

	/**
	 * 
	 * @param usbDevice
	 * @param interface_
	 * @return
	 */
	public int claim_interface(UsbDevice usbDevice, int interface_);

	/**
	 * 
	 * @param usbDevice
	 * @param ep
	 * @return
	 */
	public int clear_halt(UsbDevice usbDevice, int ep);

	/**
	 * 
	 * @param usbDevice
	 * @return
	 */
	public int close(UsbDevice usbDevice);

	/**
	 * 
	 * @param usbDevice
	 * @param requesttype
	 * @param request
	 * @param value
	 * @param index
	 * @param bytes
	 * @param size
	 * @param timeout
	 * @return
	 */
	public int control_msg(UsbDevice usbDevice, int requesttype, int request,
			int value, int index, byte[] bytes, int size, int timeout);

	/**
	 * 
	 * @param usbDevice
	 * @param ep
	 * @param type
	 * @param index
	 * @param size
	 * @return
	 */
	public String get_descriptor_by_endpoint(UsbDevice usbDevice, int ep,
			byte type, byte index, int size);

	/**
	 * 
	 * @param usbDevice
	 * @param type
	 * @param index
	 * @param size
	 * @return
	 */
	public String get_descriptor(UsbDevice usbDevice, byte type, byte index,
			int size);

	/**
	 * 
	 * @param usbDevice
	 * @param index
	 * @return
	 */
	public String get_string_simple(UsbDevice usbDevice, int index);

	/**
	 * 
	 * @param usbDevice
	 * @param index
	 * @param langid
	 * @return
	 */
	public String get_string(UsbDevice usbDevice, int index, int langid);

	/**
	 * 
	 * @param usbDevice
	 * @param ep
	 * @param bytes
	 * @param size
	 * @param timeout
	 * @return
	 */
	public int interrupt_read(UsbDevice usbDevice, int ep, byte[] bytes, int size,
			int timeout);

	/**
	 * 
	 * @param usbDevice
	 * @param ep
	 * @param bytes
	 * @param size
	 * @param timeout
	 * @return
	 */
	public int interrupt_write(UsbDevice usbDevice, int ep, byte[] bytes, int size,
			int timeout);

	/**
	 * 
	 * @param usbDevice
	 * @return
	 */
	public long open(UsbDevice usbDevice);

	/**
	 * 
	 * @param usbDevice
	 * @param interface_
	 * @return
	 */
	public int release_interface(UsbDevice usbDevice, int interface_);

	/**
	 * 
	 * @param usbDevice
	 * @return
	 */
	public int usb_reset(UsbDevice usbDevice);

	/**
	 * 
	 * @param usbDevice
	 * @param alternate
	 * @return
	 */
	public int set_altinterface(UsbDevice usbDevice, int alternate);

	/**
	 * 
	 * @param usbDevice
	 * @param configuration
	 * @return
	 */
	public int set_configuration(UsbDevice usbDevice, int configuration);

	/**
	 * 
	 * @param level
	 */
	public void set_debug(int level);

	/**
	 * 
	 * @return
	 */
	public String strerror();
	
	/**
	 * 
	 * @param vendorID
	 * @param productID
	 * @return
	 */
	public Map<String, UsbDevice> findDevices(short vendorID, short productID) ;
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public UsbDevice getDevice(String id);
}
