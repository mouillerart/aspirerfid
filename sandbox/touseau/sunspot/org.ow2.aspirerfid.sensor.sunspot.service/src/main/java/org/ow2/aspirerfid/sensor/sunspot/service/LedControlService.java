package org.ow2.aspirerfid.sensor.sunspot.service;

public interface LedControlService {

	public void blinkLEDs(int times);
	
	public void ledsOn();
	
	public void ledsOff();
	
}
