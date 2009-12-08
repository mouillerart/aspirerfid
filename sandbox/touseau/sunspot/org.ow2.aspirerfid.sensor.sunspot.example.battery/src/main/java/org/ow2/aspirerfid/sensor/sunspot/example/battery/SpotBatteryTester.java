package org.ow2.aspirerfid.sensor.sunspot.example.battery;

import org.osgi.service.event.Event;
import org.ow2.aspirerfid.sensor.sunspot.service.SunSpotService;

public class SpotBatteryTester {
	
	private SunSpotService[] spotServices;
	
	public void start(){
		
	}

	public void stop(){
		
	}
	
	public void buttonStateChanged(Event evt){
		String topic = evt.getTopic();
		int index = topic.lastIndexOf("sw");
		String cutTopic = topic.substring(index);
		String button = cutTopic.substring(0,3);
		String action = cutTopic.substring(4);
		String spotAddress = (String) evt.getProperty("spot.address");
		
		if (button.equals("sw1") && action.equals("pressed")) {
			// show battery level
			displayBatteryLevel(spotAddress);
		}
		if (button.equals("sw2") && action.equals("pressed")){
			turnOffLEDs(spotAddress);
		}
		
	}

	private void turnOffLEDs(String spotAddress) {
		for (SunSpotService spot : spotServices) {
			if (spot.getAddress().equals(spotAddress)) {
				spot.ledsOff();
			}
		}
	}

	private void displayBatteryLevel(String spotAddress) {
		for (SunSpotService spot : spotServices) {
			if (spot.getAddress().equals(spotAddress)) {
				int batteryLevel = spot.getBatteryLevel();
				if (batteryLevel < 50) {
					spot.blinkLEDs(1);
				} else {
					spot.blinkLEDs(2);
				}
				System.out.println("BATTERY LEVEL of SPOT "+spot.getAddress()+" : "+batteryLevel);
			}
		}
	}
	
}
