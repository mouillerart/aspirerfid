/*
 __BANNER__
 */
// this file was generated at 11-July-2010 04:42 PM by ${author}
package org.ow2.aspire.upnp.rfidreader.impl;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.apache.felix.upnp.devicegen.holder.IntegerHolder;
import org.apache.felix.upnp.devicegen.holder.StringHolder;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.upnp.UPnPException;
import org.ow2.aspire.upnp.rfidreader.device.RFIDReader;
import org.ow2.aspire.upnp.rfidreader.model.LowPowerDeviceModel;
import org.ow2.aspire.upnp.rfidreader.service.LowPowerDevice.PowerStateStateVariable;
import org.ow2.aspire.upnp.rfidreader.service.LowPowerDevice.SleepPeriodStateVariable;

public class LowPowerDeviceModelImpl implements LowPowerDeviceModel, BundleActivator {

	private boolean isWindowsPlatform = false;
	private boolean isLinuxPlatform = false;
	
	public LowPowerDeviceModelImpl(RFIDReader reader) {
		setPlatform();
	}

	/**
	 * This method is "add description here" wakeupMethod out parameter
	 * 
	 * powerSupplyStatus out parameter
	 */
	public void getPowerManagementInfo(
			StringHolder wakeupMethod,
			StringHolder powerSupplyStatus) throws UPnPException {
		throw new UPnPException(UPnPException.DEVICE_INTERNAL_ERROR,
				"Not implemented");
	}

	/**
	 * This method is "add description here" recommendedSleepPeriod in parameter
	 * 
	 * recommendedPowerState in parameter
	 * 
	 * sleepPeriod out parameter
	 * 
	 * powerState out parameter
	 */
	public void goToSleep(int recommendedSleepPeriod,java.lang.String recommendedPowerState,IntegerHolder sleepPeriod,StringHolder powerState) throws UPnPException {
		
		
		int _recommendedPowerState;
		try {
			_recommendedPowerState=Integer.parseInt(recommendedPowerState);			
		} catch (NumberFormatException e) {
			throw new UPnPException(UPnPException.INVALID_ARGS, "Invalid recommendedPowerState");
		}
		switch(_recommendedPowerState) {
			case PowerStateStateVariable.POWERSTATE_ACTIVE: break;
			case PowerStateStateVariable.POWERSTATE_TRANSPARENT: break;
			case PowerStateStateVariable.POWERSTATE_DEEPSLEEP: break;
			case PowerStateStateVariable.POWERSTATE_OFFLINE: break;
			default: throw new UPnPException(UPnPException.INVALID_ARGS, "Invalid recommendedPowerState");
		}
		
		if(_recommendedPowerState==PowerStateStateVariable.POWERSTATE_DEEPSLEEP && isWindowsPlatform) {
			sleepPeriod.setValue(SleepPeriodStateVariable.SLEEPPERIOD_INFINITE);
			powerState.setValue(Integer.toString(PowerStateStateVariable.POWERSTATE_DEEPSLEEP));
			Thread t=new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
					}
					hibernate();
				}
			});
			t.setName("LowPower GoToSleep");
			t.start();
			return;
		} else {
			throw new UPnPException(LowPowerDeviceModel.ERROR_GOTOSLEEP_POWERSTATE_NOT_IMPLEMENTED, "Not implemented");
		}		
	}
	
	/*
	 * os.name  can be AIX Digital Unix FreeBSD HP UX Irix Linux Mac OS Mac OS X MPE/iX
	 * Netware 4.11 OS/2 Solaris Windows 2000 Windows 7 Windows 95 Windows 98
	 * Windows NT Windows Vista Windows XP
	 */

	private void setPlatform() {
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.startsWith("windows"))
			isWindowsPlatform = true;
		if (osName.startsWith("linux"))
			isLinuxPlatform = true;
	}

	public void hibernate() {
		if (isWindowsPlatform) {
			exec("C:\\WINDOWS\\system32", new String[] { "rundll32.exe",
					"powrprof.dll,SetSuspendState", "Hibernate" }, null);
		} else if (isLinuxPlatform) {
			// TODO
		}
	}
	
	private void exec(String dir, String[] args, Map/*<String, String>*/ environment) {
		ProcessBuilder pb = new ProcessBuilder(args);
		if (environment != null) {
			Map/*<String, String>*/ env = pb.environment();
			env.clear();
			Iterator iter=environment.entrySet().iterator();
			while(iter.hasNext()) {
				Map.Entry/*<String, String>*/ e= (Map.Entry) iter.next();
				env.put(e.getKey(), e.getValue());
			}
		}
		pb.directory(new File(dir));
		try {
			Process p = pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	/**
	 * This method is "add description here"
	 */
	public void wakeup(

	) throws UPnPException {
		// TODO
		throw new UPnPException(UPnPException.DEVICE_INTERNAL_ERROR,
				"Not implemented");
	}

	// Those getters are used for the first notification just after a
	// subscription

	public java.lang.Integer getExternalPowerSupplySourceStateVariableValue() {
		// TODO
		return null;
	}

	public java.lang.Boolean getBatteryLowStateVariableValue() {
		// TODO
		return null;
	}

	public void start(BundleContext arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void stop(BundleContext arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
