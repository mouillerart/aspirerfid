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


package org.ow2.aspirerfid.usb.descriptor;


/**
 * TODO comment the methods and the fields
 * TODO set private the fields
 * @author El Mehdi DAMOU and Lionel Touseau
 */
public class UsbDevice {
	
	private short vendorId;
	
	private short productId;
	
	private Long handle;
	
	private Boolean onUse;
	
	private Boolean present;
	
	private Object libUsbDevice;
	
	private Object libUsbDeviceDescriptor; 
	
	private String filename;

	private byte devnum;
	
	public UsbDevice(short vendor, short product){
		this.vendorId = vendor;
		this.productId = product;
	}


	/**
	 * @return the vendorId
	 */
	public short getVendorId() {
		return vendorId;
	}

	/**
	 * @param vendorId the vendorId to set
	 */
	public void setVendorId(short vendorId) {
		this.vendorId = vendorId;
	}

	/**
	 * @return the productId
	 */
	public short getProductId() {
		return productId;
	}

	/**
	 * @param productId the productId to set
	 */
	public void setProductId(short productId) {
		this.productId = productId;
	}


	/**
	 * @return the handles
	 */
	public Long getHandle() {
		return handle;
	}


	/**
	 * @param handles the handles to set
	 */
	public void setHandle(Long handle) {
		this.handle = handle;
	}


	/**
	 * @return the onUse
	 */
	public Boolean isOnUse() {
		return onUse;
	}


	/**
	 * @param onUse the onUse to set
	 */
	public void setOnUse(Boolean onUse) {
		this.onUse = onUse;
	}

	/**
	 * @return the present
	 */
	public Boolean isPresent() {
		return present;
	}


	/**
	 * @param present the present to set
	 */
	public void setPresent(Boolean present) {
		this.present = present;
	}


	/**
	 * @return the libUsbDevice
	 */
	public Object getLibUsbDevice() {
		return libUsbDevice;
	}


	/**
	 * @param libUsbDevice the libUsbDevice to set
	 */
	public void setLibUsbDevice(Object libUsbDevice) {
		this.libUsbDevice = libUsbDevice;
	}


	/**
	 * @return the libUsbDeviceDescriptor
	 */
	public Object getLibUsbDeviceDescriptor() {
		return libUsbDeviceDescriptor;
	}


	/**
	 * @param libUsbDeviceDescriptor the libUsbDeviceDescriptor to set
	 */
	public void setLibUsbDeviceDescriptor(Object libUsbDeviceDescriptor) {
		this.libUsbDeviceDescriptor = libUsbDeviceDescriptor;
	}


	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}


	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}


	public void setNum(byte devnum) {
		this.devnum = devnum;
		
	}

	public byte getNum() {
		return devnum;
		
	}
	
	


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + devnum;
		result = prime * result
				+ ((filename == null) ? 0 : filename.hashCode());
		result = prime * result + productId;
		result = prime * result + vendorId;
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof UsbDevice))
			return false;
		UsbDevice other = (UsbDevice) obj;
		if (devnum != other.devnum)
			return false;
		if (filename == null) {
			if (other.filename != null)
				return false;
		} else if (!filename.equals(other.filename))
			return false;
		if (productId != other.productId)
			return false;
		if (vendorId != other.vendorId)
			return false;
		return true;
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "{USBDevice : num:" + devnum + " productID: " + productId + "vendorID:"+vendorId + " filename:"+filename + "}";
	}

}
