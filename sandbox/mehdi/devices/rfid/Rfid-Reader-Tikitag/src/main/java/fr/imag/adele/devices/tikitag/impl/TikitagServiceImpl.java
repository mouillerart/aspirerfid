package fr.imag.adele.devices.tikitag.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.sembysem.sam.demo.rfid.service.RfidReader;
import org.sembysem.sam.demo.rfid.service.RfidTag;

import fr.imag.adele.devices.tikitag.service.TikitagDescriptor;
import fr.imag.adele.devices.tikitag.service.TikitagManager;
import fr.imag.adele.devices.tikitag.service.TikitagService;

public class TikitagServiceImpl implements TikitagService,RfidReader {

	private String tikitagID; // injected by iPOJO configuration
	private TikitagManager tikitagManager; // injected by iPOJO configuration
	private int initialTime = 1000;

	private ScheduledThreadPoolExecutor timer;
	private ScheduledFuture<?> schedFuture;
	private TikitagDescriptor tikitag;

	private List<RfidTag> tags;
	private static Logger logger = Logger.getLogger(TikitagServiceImpl.class
			.getName());

	/**
	 * iPOJO validate callback Opens the {@link TikitagDescriptor}
	 */
	public void start() {
		logger.debug(">>Tikitag " + tikitagID + " started.");
		this.reset();
		tags = new ArrayList<RfidTag>();
	}

	/**
	 * iPOJO invalidate callback Closes the {@link TikitagDescriptor}
	 */
	public void stop() {
		logger.debug(">>Tikitag " + tikitagID + " stopped");
	}

	public RfidTag getTag() {
		RfidTag tag=null;

		if (timer == null || timer.isShutdown()) {

			tikitag = tikitagManager.getListTikitag().get(tikitagID);
			if (tikitag!=null){
				tikitag.open();
				tag= tikitag.getTag();
				tikitag.close();
			}
			
			return tag;
		}
		logger.debug(">>Tikitag " + tikitagID + " is pooling .");
		return null;

	}

	public String getRfidReaderID() {
		return tikitagID;
	}

	public synchronized void doPool(long interval, TimeUnit unit) {
		logger.debug(">>> Start Pooling...");
		if (timer == null || timer.isShutdown())
			this.timer = new ScheduledThreadPoolExecutor(1);
		tikitag = tikitagManager.getListTikitag().get(tikitagID);
		tikitag.open();
		this.schedFuture = this.timer.scheduleWithFixedDelay(new Runnable() {
			public void run() {
				RfidTag value = tikitag.getTag();
				if (value != null) {
					tags.add( value);
				}
			}
		}, initialTime, interval, unit);
	}

	public synchronized void interuptPool() {

		schedFuture.cancel(false);
		timer.purge();
		tikitag.close();
	}

	public synchronized List<RfidTag> getTags() {
		if (timer == null || timer.isShutdown()) {
			logger.debug(">>Tikitag " + tikitagID + " is not pooling!");
			return tags;
		}
		List<RfidTag> result = new ArrayList<RfidTag>();
		result.addAll(tags);
		tags.clear();
		return result;
	}

	public void reset() {
		if (isTikitagPresent()) {
			if (tikitag.isOpened()) {
				tikitag.close();
			} else {
				tikitag.open();
				tikitag.close();
			}
		}
	}

	private boolean isTikitagPresent() {
		tikitag = tikitagManager.getTikitagbyID(tikitagID);
		if (tikitag != null) {
			return true;
		} else {
			return false;
		}
	}

}
