package org.sembysem.sam.demo.rfid.service;

public interface RfidReader {

	/**
	 * Get the Rfid Reader ID
	 * @return the Id of the Device RfidReader 
	 */
	public String getRfidReaderID();
	
	/**
	 * Read a tag only if the RfidReader is not buffering, otherwise return null
	 * @return The tag value 
	 */
	public RfidTag getTag();
	

}
