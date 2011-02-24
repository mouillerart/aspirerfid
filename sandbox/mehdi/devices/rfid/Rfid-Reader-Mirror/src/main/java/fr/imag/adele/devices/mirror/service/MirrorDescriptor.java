package fr.imag.adele.devices.mirror.service;

import java.io.FileNotFoundException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.ow2.chameleon.usb.descriptor.UsbDevice;
import org.ow2.chameleon.usb.service.USBBridge;
import org.sembysem.sam.demo.rfid.service.RfidTag;

public class MirrorDescriptor {

	public static final short MIRROR_ENDPOINT_IN_IRP = 0x81;
	public static final short PACKET_LENGTH = 0x40;
	public static final short READ_TIMEOUT = 1000;
	private String id;
	private UsbDevice usb_Device;
	private USBBridge usbManager;
	private boolean opened = false;
	private long handle;

	private Logger logger = Logger.getLogger(MirrorDescriptor.class);

	private RfidTag currentTag = null;
	private RfidTag removedTag = null;
	private boolean started = false;
	private boolean upsidedown = false;
	private MirrorLinux mirrorLinux;
	private Thread t;
	
	public MirrorDescriptor(String id2, UsbDevice usbDev, USBBridge usblib) {
		this.id = id2;
		this.usb_Device = usbDev;
		this.usbManager = usblib;
	}

	public synchronized void open() {
		if ((opened == false) && (usb_Device != null)) {
			handle = usbManager.open(usb_Device);
			logger.info("open -> usb_open : " + handle);

			int lDebug;
			if (System.getProperty("os.name").equals("Linux")) {
				logger.info("os -> Running on Linux");
				try {
					mirrorLinux = new MirrorLinux(this);
					t = new Thread(mirrorLinux);
					t.start();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				logger
						.info("os -> Running on "
								+ System.getProperty("os.name"));
				lDebug = usbManager.setConfiguration(usb_Device, 1);
				logger.info("open -> usb_set_configuration : " + lDebug);
				lDebug = usbManager.claimInterface(usb_Device, 0);

				// byte[] buf = new byte[64];

				lDebug = usbManager.setAltinterface(usb_Device, 0);
				logger.info("open -> usb_set_altinterface : " + lDebug);

			}

			opened = true;
		}

	}

	public synchronized void updateState() {
		if (opened == true) {
			long lDebug;

			byte[] b = new byte[64];
			lDebug = usbManager.interruptRead(usb_Device,
					MIRROR_ENDPOINT_IN_IRP, b, PACKET_LENGTH, READ_TIMEOUT);
			if (lDebug == 64) {
				// started = false;
				// upsidedown = false;
				// currentTag =null;
				// removedTag=null;

				if (b[0] != 0) {
					// logger.info("Message !! ");
					/*
					 * 14 start 15 stop (upsideDown) 21 tag detected 22 tag
					 * removed
					 */

					if (b[0] == 1) {
						if (b[1] == 4) {
							// Mirror (re)started
							started = true;
							upsidedown=false;
							logger.info("Mirror (re)started");
						} else if (b[1] == 5) {
							// Mirror stopped (Mirror is upside down)
							upsidedown = true;
							started=false;
							logger
									.info("Mirror stopped : Mirror is upside down");
						}
					} else if (b[0] == 2) {

						/*
						 * The Mirror device seems to miss the 3 last bytes of
						 * the tag id, the value is truncated and appears as
						 * XXXX000
						 */

						if (b[1] == 1) {
							// Tag detected
							currentTag = new RfidTag(String.format(
									"%02X%02X%02X%02X%02X%02X%02X", b[5], b[6],
									b[7], b[8], b[9], b[10], b[11]), new Date());
							logger.info("Mirror Add Tag : "
									+ currentTag.getTagValue() + " at "
									+ currentTag.getTime());
						} else if (b[1] == 2) {
							// Tag removed
							removedTag = new RfidTag(String.format(
									"%02X%02X%02X%02X%02X%02X%02X", b[5], b[6],
									b[7], b[8], b[9], b[10], b[11]), new Date());
							logger.info("Mirror Remove Tag : "
									+ removedTag.getTagValue() + " at "
									+ removedTag.getTime());
							currentTag = null;
						}
					}
				}
			}
		}
	}

	public void close() {
		try {
			if (System.getProperty("os.name").equals("Linux")) {
				mirrorLinux.setStop(true);
				t.interrupt();

			} else {
				if (opened == true) {
					// usbManager.release_interface(usb_Device, 0);
					Thread.sleep(500);
					usbManager.usbReset(usb_Device);
					usbManager.close(usb_Device);
				}
				// usblib.close(handle);
				opened = false;

			}

			logger.debug(">>> " + opened);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the opened
	 */
	public boolean isOpened() {
		return opened;
	}

	public RfidTag getTag() {
		return currentTag;
	}

	public RfidTag getRemovedTag() {
		return removedTag;
	}

	public boolean isStarted() {
		return started;
	}

	public boolean isUpsideDown() {
		return upsidedown;
	}


	public void setIsStarted(boolean b) {
		started = b;
	}

	public void setIsUpSideDown(boolean b) {
		upsidedown = b;	
	}

	public void setCurrentTag(RfidTag rfidTag) {
		currentTag=rfidTag;
	}

	public void setRemovedTag(RfidTag rfidTag) {
		removedTag=rfidTag;
	}
}
