package fr.imag.adele.devices.mirror.service;

import org.sembysem.sam.demo.rfid.service.RfidTag;

public interface MirrorService {
		
	public RfidTag getRemovedTag();

	public boolean isStarted();
	
	public boolean isUpsideDown();
	
}
