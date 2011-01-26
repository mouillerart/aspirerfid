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
package org.ow2.aspirerfid.sensor.wmr200.service;

import org.apache.log4j.Logger;
import org.ow2.aspirerfid.sensor.wmr200.impl.WMR200Utils;
import org.ow2.chameleon.usb.descriptor.UsbDevice;
import org.ow2.chameleon.usb.service.USBBridge;

/**
 * The Class WMR200.
 *  @author Elmehdi Damou
 */
public class WMR200Descriptor {

	/** The WM r200_ descripto r_ typ e1. */
	public static byte WMR200_DESCRIPTOR_TYPE1 = (byte) 0x0000001;

	/** The WM r200_ descripto r_ typ e2. */
	public static byte WMR200_DESCRIPTOR_TYPE2 = (byte) 0x0000002;

	/** The WM r200_ descripto r_ index. */
	public static byte WMR200_DESCRIPTOR_INDEX = (byte) 0x0000000;

	/** The WM r200_ descripto r_ siz e1. */
	public static int WMR200_DESCRIPTOR_SIZE1 = 18; // 0x0000012

	/** The WM r200_ descripto r_ siz e2. */
	public static int WMR200_DESCRIPTOR_SIZE2 = 9; // 0x0000009;

	/** WMR200 Initialization (once). */
	public static byte[] WMR200_DATA_INITIALIZATION = { 0x20, 0x00, 0x08, 0x01,
			0x00, 0x00, 0x00, 0x00, 0x00 };

	/** PC Ready to receive. */
	public static byte[] WMR200_DATA_READY_TO_RECEIVE = { 0x01, (byte) 0xd0,
			0x08, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00 };

	// Control Message Fields
	/** The WM r200_ crt l_ ms g_ request. */
	public static int WMR200_CRTL_MSG_REQUEST = 9;

	/** The WM r200_ crt l_ ms g_ value. */
	public static int WMR200_CRTL_MSG_VALUE = 512;

	/** The WM r200_ crt l_ ms g_ index. */
	public static int WMR200_CRTL_MSG_INDEX = 0;

	/** The WM r200_ crt l_ ms g_ size. */
	public static int WMR200_CRTL_MSG_SIZE = 8; // 8

	/** The WM r200_ crt l_ ms g_ timeout. */
	public static int WMR200_CRTL_MSG_TIMEOUT = 1000;

	/** The WM r200_ us b_b interface number. */
	public static int WMR200_USB_bInterfaceNumber = 0;

	/** The WM r200_ us b_b configuration value. */
	public static int WMR200_USB_bConfigurationValue = 1;

	/** The WM r200_ us b_b alternate setting. */
	public static int WMR200_USB_bAlternateSetting = 0;

	/** The WM r200_ us b_ ep. */
	public static int WMR200_USB_EP = 129;

	/** The debug. */
	private boolean debug = false;

	/** The opened. */
	private boolean opened = false;

	/** The id. */
	private String id;

	/** The handle. */
	private long handle;

	/** The usb_ device. */
	private UsbDevice usbDevice;

	/** The usb bridge. */
	private USBBridge usbBridge;

	/** The logger. */
	private Logger logger = Logger.getLogger(WMR200Descriptor.class);

	/**
	 * Instantiates a new wM r200.
	 *
	 * @param id2 the id2
	 * @param usbDev the usb dev
	 * @param usbBridge the usb bridge
	 */
	public WMR200Descriptor(String id2, UsbDevice usbDev, USBBridge usbBridge) {
		this.id = id2;
		this.usbDevice = usbDev;
		this.usbBridge = usbBridge;
	}

	/**
	 * Open.
	 */
	public synchronized void open() {
		if ((opened == false) && (usbDevice != null)) {
			handle = usbBridge.open(usbDevice);
			printDebug("DEBUG_WMR200 open -> usb_open : " + handle);
			logger.info("Etat USB :  "
					+ ((handle == 0) ? "Impossible d'ouvrir le port usb"
							: "REUSSIE ,Port Usb ouvert =" + handle));
			int lDebug;

			if (System.getProperty("os.name").startsWith("Windows")) {

				lDebug = usbBridge.setConfiguration(usbDevice,
						WMR200_USB_bConfigurationValue);
				logger.info("Configuration :  "
						+ ((lDebug < 0) ? " ECHOUE :  " + lDebug : "REUSSIE ("
								+ lDebug + ")"));

			}
			lDebug = usbBridge.claimInterface(usbDevice,
					WMR200_USB_bInterfaceNumber);
			logger.info("Demande de l'interface USB :  "
					+ ((lDebug < 0) ? " ECHOUE :  " + lDebug : "REUSSIE ("
							+ lDebug + ")"));

			lDebug = usbBridge.setAltinterface(usbDevice,
					WMR200_USB_bAlternateSetting);
			logger.info("Configuration de l'interface alternative: "
					+ ((lDebug < 0) ? " ECHOUE :  " + lDebug : "REUSSIE ("
							+ lDebug + ")"));

			opened = true;
		}

	}



	/**
	 * Sets the debug.
	 * 
	 * @param pState
	 *            the new debug
	 */
	public void setDebug(boolean pState) {
		debug = pState;
	}

	/**
	 * Close.
	 */
	public void close() {
		try {
			if (opened == true) {
				usbBridge.releaseInterface(usbDevice, 0);
				Thread.sleep(500);
				usbBridge.close(usbDevice);
			}
			opened = false;

			logger.debug(">>> " + opened);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Prints the debug.
	 * 
	 * @param pStr
	 *            the str
	 */
	public void printDebug(String pStr) {
		if (debug == true)
			System.out.println("DEBUG_JTikitag " + pStr);
	}

	/**
	 * Checks if is opened.
	 * 
	 * @return the opened
	 */
	public boolean isOpened() {
		return opened;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Send initialisation trame.
	 *
	 * @throws InterruptedException the interrupted exception
	 */
	public void sendInitialisationTrame()
			throws InterruptedException {
		// Class Interface 20 00 08 01 00 00 00 00
	
		int ret = usbBridge.controlMsg(usbDevice, USBBridge.REQ_TYPE_TYPE_CLASS
				+ USBBridge.REQ_TYPE_RECIP_INTERFACE,
				WMR200_CRTL_MSG_REQUEST,
				WMR200_CRTL_MSG_VALUE,
				WMR200_CRTL_MSG_INDEX,
				WMR200_DATA_INITIALIZATION, 
				WMR200_CRTL_MSG_SIZE,
				WMR200_CRTL_MSG_TIMEOUT);
	
		logger.info("Envoi de message d'initialisation de la WMR200 : "
				+ ((ret < 0) ? " ECHOUE :  " + ret : "REUSSIE (" + ret + ")"));
	
		Thread.currentThread();
		Thread.sleep(100);
	
	}
	
	/**
	 * Test device.
	 *
	 * @param ret the ret
	 * @throws InterruptedException the interrupted exception
	 */
	public void testDevice(int ret) throws InterruptedException {
		String retString;
		byte[] buf = new byte[WMR200_CRTL_MSG_SIZE];
	
//		assert (ret >= 0);
	
		retString = usbBridge.getDescriptor(usbDevice,
				WMR200_DESCRIPTOR_TYPE1,
				WMR200_DESCRIPTOR_INDEX,
				WMR200_DESCRIPTOR_SIZE1);
	
		logger.info(" {TEST} Get descritor (TYPE : 0x0000001) : " + retString);
	
		retString = usbBridge.getDescriptor(usbDevice,
				WMR200_DESCRIPTOR_TYPE1,
				WMR200_DESCRIPTOR_INDEX,
				WMR200_DESCRIPTOR_SIZE2);
	
		logger.info(" {TEST} Get descritor (TYPE : 0x0000002 , SIZE : 18) : "
				+ retString);
	
		retString = usbBridge.getDescriptor(usbDevice,
				WMR200_DESCRIPTOR_TYPE1,
				WMR200_DESCRIPTOR_INDEX,
				WMR200_DESCRIPTOR_SIZE1);
	
		logger.info(" {TEST} Get descritor (TYPE : 0x0000002 , SIZE : 9) : "
				+ retString);
	
		Thread.currentThread();
		Thread.sleep(100);
	
		ret = usbBridge.releaseInterface(usbDevice,
				WMR200_USB_bInterfaceNumber);
		logger.info("{TEST} Liberation de l'interface "
				+ ((ret < 0) ? " ECHOUE :  " + ret : "REUSSIE (" + ret + ")"));
		if (System.getProperty("os.name").startsWith("Windows")){
		ret = usbBridge.setConfiguration(usbDevice,
				WMR200_USB_bConfigurationValue);
		logger.info("{TEST} Configuration"
				+ ((ret < 0) ? " ECHOUE :  " + ret : "REUSSIE (" + ret + ")"));
		}
		ret = usbBridge.claimInterface(usbDevice,
				WMR200_USB_bInterfaceNumber);
		logger.info("{TEST} Demande de l'interface : "
				+ ((ret < 0) ? " ECHOUE :  " + ret : "REUSSIE (" + ret + ")"));
	
		ret = usbBridge.setAltinterface(usbDevice,
				WMR200_USB_bAlternateSetting);
		logger.info("{TEST} Configuration de l'interface alternative: "
				+ ((ret < 0) ? " ECHOUE :  " + ret : "REUSSIE (" + ret + ")"));
	
		Thread.currentThread();
		Thread.sleep(1000);
	
		ret = usbBridge.controlMsg(usbDevice, USBBridge.REQ_TYPE_TYPE_CLASS
				+ USBBridge.REQ_TYPE_RECIP_INTERFACE, 10, 0, 0, buf, 0, 1000);
		logger.info(" {TEST} Control MSG 1 Reponse: " + retString);
	
		Thread.currentThread();
		Thread.sleep(1000);
	
		retString = usbBridge.getDescriptor(usbDevice, (byte) 0x0000022,
				(byte) 0x0000000, 98);
		Thread.currentThread();
		Thread.sleep(5000);
		logger.info(" {TEST} Control MSG 2 Reponse: " + retString);
	
		ret = usbBridge.interruptRead(usbDevice, WMR200_USB_EP,
				buf, WMR200_CRTL_MSG_SIZE,
				WMR200_CRTL_MSG_TIMEOUT);
		logger.info(" {TEST} Lecture a partir du EndPoint Reponse: "
				+ ((ret < 0) ? " ECHOUE :  " + ret : "REUSSIE (" + ret + ")"));
	
		System.out.println("Type, bytes: " + ret);
		WMR200Utils.logData(buf);
		ret = usbBridge.interruptRead(usbDevice, 129, buf, 8, 1000);
		if (ret == 8) {
			System.out.println("Type, bytes: " + ret);
			WMR200Utils.logData(buf);
		}
		Thread.currentThread();
		Thread.sleep(2500);
	
	}

	/**
	 * Gets the data from wm r200.
	 *
	 * @return the data from wm r200
	 * @throws InterruptedException the interrupted exception
	 */
	public byte[] getDataFromWMR200() throws InterruptedException {
		
		int ret ;
	
		byte[] buf = new byte[WMR200_CRTL_MSG_SIZE];
	
		ret = usbBridge.controlMsg( usbDevice,USBBridge.REQ_TYPE_TYPE_CLASS
				+ USBBridge.REQ_TYPE_RECIP_INTERFACE,
				WMR200_CRTL_MSG_REQUEST,
				WMR200_CRTL_MSG_VALUE,
				WMR200_CRTL_MSG_INDEX,
				WMR200_DATA_READY_TO_RECEIVE, 
				WMR200_CRTL_MSG_SIZE,
				WMR200_CRTL_MSG_TIMEOUT);
	
		Thread.currentThread();
		Thread.sleep(100);
	
		ret = usbBridge.interruptRead(usbDevice, WMR200_USB_EP,
				buf, WMR200_CRTL_MSG_SIZE,
				WMR200_CRTL_MSG_TIMEOUT);
	
		return buf;
	}
	
}
