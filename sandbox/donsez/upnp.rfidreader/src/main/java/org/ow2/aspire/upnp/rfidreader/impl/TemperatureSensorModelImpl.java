/*
 __BANNER__
 */
// this file was generated at 21-January-2011 12:56 AM by ${author}
package org.ow2.aspire.upnp.rfidreader.impl;

import java.beans.PropertyChangeEvent;

import org.apache.felix.upnp.devicegen.holder.IntegerHolder;
import org.apache.felix.upnp.devicegen.holder.StringHolder;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.upnp.UPnPException;
import org.ow2.aspire.upnp.rfidreader.device.RFIDReader;
import org.ow2.aspire.upnp.rfidreader.model.TemperatureSensorModel;
import org.ow2.aspire.upnp.rfidreader.service.TemperatureSensor.CurrentTemperatureStateVariable;

public class TemperatureSensorModelImpl implements TemperatureSensorModel, Runnable, BundleActivator {

	
    private String name = "Fridge";
    private String application = "INDOOR";

    private static final int MAXTEMP = 1100;
	private static final int MINTEMP = 400;
	private static final int INCR = 50;
	private int currentTemperature=MINTEMP;
	private int increment=INCR;

	private RFIDReader reader;
    private Thread thread;
    private volatile boolean end;
        
    public TemperatureSensorModelImpl(RFIDReader reader) {
    	this.reader=reader;
    }

	public void run() {
		while(!end) {
			try {
				Thread.sleep(5000);
				if(end) return;

				int previousTemperature=currentTemperature;
				if(increment>0) {
					if(currentTemperature+increment>=MAXTEMP) {
						increment*=-1;
					}
				} else {
					if(currentTemperature+increment<=MINTEMP) {
						increment*=-1;
					}					
				}
				synchronized (this){
					currentTemperature+=increment;
				}
				PropertyChangeEvent event=new PropertyChangeEvent(this,CurrentTemperatureStateVariable.NAME,Integer.toString(previousTemperature),Integer.toString(currentTemperature));
				reader.propertyChange(event);				
			} catch (InterruptedException e) {
				return;
			}
		}
	}

	public void start(BundleContext bundleContext) throws Exception {
		thread=new Thread(this);
		thread.setName(this.getClass().getName());
		thread.start();        
	}

	public void stop(BundleContext bundleContext) throws Exception {
		end=true;
		Thread.interrupted();		
	}

	/**
	 * This method is "add description here" * currentApplication out parameter
	 */
	public void getApplication(StringHolder currentApplication)
			throws UPnPException {
		currentApplication.setValue(application);
	}

	/**
	 * This method is "add description here" newApplication in parameter
	 */
	public void setApplication(java.lang.String newApplication)
			throws UPnPException {
		application=newApplication;
	}

	/**
	 * This method is "add description here" currentTemp out parameter
	 */
	public void getCurrentTemperature(IntegerHolder currentTemp)
			throws UPnPException {
		synchronized (this){
			currentTemp.setValue(currentTemperature);
		}
	}

	/**
	 * This method is "add description here" currentName out parameter
	 */
	public void getName(StringHolder currentName) throws UPnPException {
		currentName.setValue(name);
	}

	/**
	 * This method is "add description here" newName in parameter
	 */
	public void setName(java.lang.String newName) throws UPnPException {
		name=newName;
	}

	// Those getters are used for the first notification just after a
	// subscription

	public java.lang.Integer getCurrentTemperatureStateVariableValue() {
		synchronized (this){
			return new Integer(currentTemperature);
		}
	}

	public java.lang.String getApplicationStateVariableValue() {
		return application;
	}

	public java.lang.String getNameStateVariableValue() {
		return name;
	}
}
