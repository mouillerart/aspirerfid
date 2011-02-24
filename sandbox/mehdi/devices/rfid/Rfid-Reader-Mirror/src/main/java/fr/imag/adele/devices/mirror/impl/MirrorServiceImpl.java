package fr.imag.adele.devices.mirror.impl;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.sembysem.sam.demo.rfid.service.RfidReader;
import org.sembysem.sam.demo.rfid.service.RfidTag;

import fr.imag.adele.devices.mirror.service.MirrorDescriptor;
import fr.imag.adele.devices.mirror.service.MirrorManager;
import fr.imag.adele.devices.mirror.service.MirrorService;

public class MirrorServiceImpl implements MirrorService,RfidReader,Runnable{

	private String mirrorID; // injected by iPOJO configuration
	private MirrorManager mirrorManager; // injected by iPOJO configuration
	private int initialTime = 1000;

	private ScheduledThreadPoolExecutor timer;
	private ScheduledFuture<?> schedFuture;
	private MirrorDescriptor mirror;
	private long interval= 1000 ;
	
	
	private static Logger logger = Logger.getLogger(MirrorServiceImpl.class
			.getName());
	
	/**
	 * iPOJO validate callback Opens the {@link TikitagDescriptor}
	 */
	public void start() {
		logger.debug(">>Mirror"+ mirrorID + " started.");
		mirror = mirrorManager.getListMirror().get(mirrorID);
		if (System.getProperty("os.name").equals("Linux")){
			mirror.open();
		}else {
//			this.reset();
			mirror.open();
			this.timer = new ScheduledThreadPoolExecutor(1);
			this.schedFuture = this.timer.scheduleWithFixedDelay(new Runnable() {
				public void run() {
					mirror.updateState();
				}
			}, initialTime, interval , TimeUnit.MILLISECONDS);
						
		}

//		tags = new HashMap<String, Long>();
	}

	/**
	 * iPOJO invalidate callback Closes the {@link TikitagDescriptor}
	 */
	public void stop() {
		if (System.getProperty("os.name").equals("Linux")){
			mirror.close();
		}else {
			schedFuture.cancel(false);
			timer.purge();
			mirror.close();	
		}
		logger.debug(">>Mirror " + mirrorID + " stopped");
	}
	
	public String getRfidReaderID() {
		return mirror.getId();
	}

	public RfidTag getRemovedTag() {
		return mirror.getRemovedTag();
	}

	public RfidTag getTag() {
		return mirror.getTag();

	}

	public boolean isStarted() {
		return mirror.isStarted();
	}

	public boolean isUpsideDown() {
		return mirror.isUpsideDown();
	}

	public void reset() {
		if (isMirrorPresent()) {
			if (mirror.isOpened()) {
				mirror.close();
			} else {
				mirror.open();
				mirror.close();
			}
		}
	}
	
	private boolean isMirrorPresent() {
		mirror = mirrorManager.getMirrorbyID(mirrorID);
		if (mirror != null) {
			return true;
		} else {
			return false;
		}
	}

	public void run() {
		mirror.updateState();
	}
	
	
	
}
