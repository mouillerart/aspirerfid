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

import java.util.List;

import org.apache.log4j.Logger;
import org.ow2.aspirerfid.sensor.wmr200.impl.WMR200Utils;
import org.ow2.chameleon.usb.descriptor.UsbDevice;
import org.ow2.chameleon.usb.service.USBBridge;

// TODO: Auto-generated Javadoc
/**
 * The Class WMR200.
 * 
 * @author Elmehdi Damou
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
	public static byte[] WMR200_REQUEST_NEXT_DATA = { 0x01, (byte) 0xd0, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

	/** The WMr200_ check_ station_ is_ ready. */
	public static byte[] WMR200_CHECK_STATION_IS_READY = { 0x01, (byte) 0xda,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

	/** The WMr200_ clear_ historic_ data. */
	public static byte[] WMR200_CLEAR_HISTORIC_DATA = { 0x01, (byte) 0xdb,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

	/** The WMr200_ end_ of_ transfer. */
	public static byte[] WMR200_END_OF_TRANSFER = { 0x01, (byte) 0xdf, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

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
	public static int WMR200_CRTL_MSG_TIMEOUT = 2000;

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

	private int totalPackets = 0;

	private int badPackets = 0;

	private int packets = 0;

	/**
	 * Instantiates a new wM r200.
	 * 
	 * @param id2
	 *            the id2
	 * @param usbDev
	 *            the usb dev
	 * @param usbBridge
	 *            the usb bridge
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
				sendCommandAndReceiveDataFromWMR200(WMR200_END_OF_TRANSFER);
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

	public String printByteArray(byte[] buf) {
		String st = "";
	    for (int i = 0; i < buf.length; i++) {
	    		st = st + (Integer.toHexString(buf[i] & 0xff) + " ");
		}    
	    return st;
	}

	/**
	 * Send initialisation trame.
	 * 
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	public void sendInitialisationTrame() throws InterruptedException {
		// Class Interface 20 00 08 01 00 00 00 00
		byte[] buf;

		int ret = usbBridge.controlMsg(usbDevice, USBBridge.REQ_TYPE_TYPE_CLASS
				+ USBBridge.REQ_TYPE_RECIP_INTERFACE, WMR200_CRTL_MSG_REQUEST,
				WMR200_CRTL_MSG_VALUE, WMR200_CRTL_MSG_INDEX,
				WMR200_DATA_INITIALIZATION, WMR200_CRTL_MSG_SIZE,
				WMR200_CRTL_MSG_TIMEOUT);

		//
		// # The WMR200 is known to support the following commands:
		//
		// def sendCommand(self, command):
		// # All commands are only 1 octect long.
		// self.sendPacket([0x01, command, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00])

		// This command is supposed to stop the communication between PC and the
		// station.

		sendCommandAndReceiveDataFromWMR200(WMR200_END_OF_TRANSFER);
		// Ignore any response packets the commands might have generated.
		Thread.currentThread().sleep(1000);
		//
		// # This command is a 'hello' command. The station respons with a 0x01
		// 0xD1 packet.
		buf = sendCommandAndReceiveDataFromWMR200(WMR200_END_OF_TRANSFER);
		if (buf == null) {
			logger.error("Station did not respond properly to WMR200 ping");
			throw new InterruptedException();
		} else if ((buf[0] == 0x01) && (buf[1] == 0xD1)) {
			logger.info("Station identified as WMR200");
		} else {
			logger.debug("Ping answer doesn't match :" + printByteArray(buf));
			// throw new InterruptedException();
		}

		logger.info("Envoi de message d'initialisation de la WMR200 : "
				+ ((ret < 0) ? " ECHOUE :  " + ret : "REUSSIE (" + ret + ")"));

		logger.info("USB connection established");

		Thread.sleep(100);

	}

	/**
	 * Test device.
	 * 
	 * @param ret
	 *            the ret
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	public void testDevice(int ret) throws InterruptedException {
		String retString;
		byte[] buf = new byte[WMR200_CRTL_MSG_SIZE];

		// assert (ret >= 0);

		retString = usbBridge.getDescriptor(usbDevice, WMR200_DESCRIPTOR_TYPE1,
				WMR200_DESCRIPTOR_INDEX, WMR200_DESCRIPTOR_SIZE1);

		logger.info(" {TEST} Get descritor (TYPE : 0x0000001) : " + retString);

		retString = usbBridge.getDescriptor(usbDevice, WMR200_DESCRIPTOR_TYPE1,
				WMR200_DESCRIPTOR_INDEX, WMR200_DESCRIPTOR_SIZE2);

		logger.info(" {TEST} Get descritor (TYPE : 0x0000002 , SIZE : 18) : "
				+ retString);

		retString = usbBridge.getDescriptor(usbDevice, WMR200_DESCRIPTOR_TYPE1,
				WMR200_DESCRIPTOR_INDEX, WMR200_DESCRIPTOR_SIZE1);

		logger.info(" {TEST} Get descritor (TYPE : 0x0000002 , SIZE : 9) : "
				+ retString);

		// Thread.currentThread();
		// Thread.sleep(100);

		ret = usbBridge
				.releaseInterface(usbDevice, WMR200_USB_bInterfaceNumber);
		logger.info("{TEST} Liberation de l'interface "
				+ ((ret < 0) ? " ECHOUE :  " + ret : "REUSSIE (" + ret + ")"));
		if (System.getProperty("os.name").startsWith("Windows")) {
			ret = usbBridge.setConfiguration(usbDevice,
					WMR200_USB_bConfigurationValue);
			logger.info("{TEST} Configuration"
					+ ((ret < 0) ? " ECHOUE :  " + ret : "REUSSIE (" + ret
							+ ")"));
		}
		ret = usbBridge.claimInterface(usbDevice, WMR200_USB_bInterfaceNumber);
		logger.info("{TEST} Demande de l'interface : "
				+ ((ret < 0) ? " ECHOUE :  " + ret : "REUSSIE (" + ret + ")"));

		ret = usbBridge
				.setAltinterface(usbDevice, WMR200_USB_bAlternateSetting);
		logger.info("{TEST} Configuration de l'interface alternative: "
				+ ((ret < 0) ? " ECHOUE :  " + ret : "REUSSIE (" + ret + ")"));

		// Thread.currentThread();
		// Thread.sleep(1000);

		ret = usbBridge.controlMsg(usbDevice, USBBridge.REQ_TYPE_TYPE_CLASS
				+ USBBridge.REQ_TYPE_RECIP_INTERFACE, 10, 0, 0, buf, 0, 1000);
		logger.info(" {TEST} Control MSG 1 Reponse: " + retString);

		Thread.currentThread();
		Thread.sleep(1000);

		retString = usbBridge.getDescriptor(usbDevice, (byte) 0x0000022,
				(byte) 0x0000000, 98);
		// Thread.currentThread();
		// Thread.sleep(5000);
		logger.info(" {TEST} Control MSG 2 Reponse: " + retString);

		ret = usbBridge.interruptRead(usbDevice, WMR200_USB_EP, buf,
				WMR200_CRTL_MSG_SIZE, WMR200_CRTL_MSG_TIMEOUT);
		logger.info(" {TEST} Lecture a partir du EndPoint Reponse: "
				+ ((ret < 0) ? " ECHOUE :  " + ret : "REUSSIE (" + ret + ")"));

		System.out.println("Type, bytes: " + ret);
		WMR200Utils.logData(buf);
		ret = usbBridge.interruptRead(usbDevice, 129, buf, 8, 1000);
		if (ret == 8) {
			System.out.println("Type, bytes: " + ret);
			WMR200Utils.logData(buf);
		}
		// Thread.currentThread();
		// Thread.sleep(2500);

	}

	/**
	 * Gets the data from wm r200.
	 * 
	 * @param command
	 *            the command
	 * @return the data from wm r200
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	@SuppressWarnings("unused")
	public byte[] sendCommandAndReceiveDataFromWMR200(byte[] command)
			throws InterruptedException {

		int ret;

		byte[] packet = new byte[WMR200_CRTL_MSG_SIZE];

		ret = usbBridge.controlMsg(usbDevice, USBBridge.REQ_TYPE_TYPE_CLASS
				+ USBBridge.REQ_TYPE_RECIP_INTERFACE, WMR200_CRTL_MSG_REQUEST,
				WMR200_CRTL_MSG_VALUE, WMR200_CRTL_MSG_INDEX, command,
				WMR200_CRTL_MSG_SIZE, WMR200_CRTL_MSG_TIMEOUT);
		   
		if (ret < 0) {
			System.out.println("Control message ne passe pas! " + ret);
		}
//		Thread.currentThread().sleep(00);
		packet = new byte[WMR200_CRTL_MSG_SIZE];
		
		ret = usbBridge.interruptRead(usbDevice, 0x81, packet,
				8, WMR200_CRTL_MSG_TIMEOUT);

//		packet = self.devh.interruptRead(USB. + 1, 8,
//                int(self.pollDelay * 1000))
                
		this.totalPackets += 1;

		// # Provide some statistics on the USB connection every 1000
		// # packets.
		if ((this.totalPackets > 0) && (this.totalPackets % 1000 == 0)) {
			logStats();
		}

		if (ret < 0 || packet.length != 8) {

			// # Valid packets must always have 8 octets.
			this.badPackets += 1;
			logger.debug("Wrong packet size: " + printByteArray(packet));
			return null;
		} else if (packet[0] > 7) {
			// # The first octet is always the number of valid octets in the
			// # packet. Since a packet can only have 8 bytes, ignore all
			// packets
			// # with a larger size value. It must be corrupted.
			this.badPackets += 1;
			logger.debug("Bad packet: " + printByteArray(packet));
			return null;
		} else {
			// # We've received a new packet.
			this.packets += 1;
			logger.debug("Packet: " + printByteArray(packet));
		}

		logger.debug("Good packets : "+printByteArray(packet));
		return packet;
	}

	private void logStats() {
		// long now = new Date().getTime();
		// uptime = now - self.start
		// self.logger.info("Uptime: %s" % self.durationToStr(uptime))
		// if self.totalPackets > 0:
		// self.logger.info("Total packets: %d" % self.totalPackets)
		// self.logger.info("Good packets: %d (%.1f%%)" %
		// (self.packets,
		// self.packets * 100.0 / self.totalPackets))
		// self.logger.info("Bad packets: %d (%.1f%%)" %
		// (self.badPackets,
		// self.badPackets * 100.0 / self.totalPackets))
		// if self.frames > 0:
		// self.logger.info("Frames: %d" % self.frames)
		// self.logger.info("Bad frames: %d (%.1f%%)" %
		// (self.badFrames,
		// self.badFrames * 100.0 / self.frames))
		// self.logger.info("Checksum errors: %d (%.1f%%)" %
		// (self.checkSumErrors,
		// self.checkSumErrors * 100.0 / self.frames))
		// self.logger.info("Requests: %d" % self.requests)
		// self.logger.info("Clock delta: %d" % self.clockDelta)
		// # Generate a warning if PC and station clocks are more than 2
		// # minutes out of sync.
		// if abs(self.clockDelta) > 2:
		// self.logger.warning("PC and station clocks are out of sync")
		//
		// self.logger.info("Polling delay: %.1f" % self.pollDelay)
		// self.logger.info("USB resyncs: %d" % self.resyncs)
		//
		// loggedTime = self.loggedTime
		// resyncTime = self.resyncTime
		// if not self.syncing:
		// loggedTime += now - self.lastResync
		// else:
		// resyncTime += now - self.lastResync
		// self.logger.info("Logged time: %s (%.1f%%)" %
		// (self.durationToStr(loggedTime),
		// loggedTime * 100.0 / uptime))
		// self.logger.info("Resync time: %s (%.1f%%)" %
		// (self.durationToStr(resyncTime),
		// resyncTime * 100.0 / uptime))
		// for i in xrange(9):
		// self.logger.info("0x%X records: %8d (%2d%%)" %
		// (0xD1 + i, self.recordCounters[i],
		// self.recordCounters[i] * 100.0 / self.frames))

	}

	public String printByteArray(List<Byte> array) {
		String st = "";
		for (Byte byte1 : array) {
			st = st + (Integer.toHexString(byte1 & 0xff) + " ");
		}    
	    return st;
	}

}
