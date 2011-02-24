package fr.imag.adele.devices.tikitag.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.sembysem.sam.demo.rfid.service.RfidTag;

public interface TikitagService{
	
	
	/**
	 * executes a periodic reading and buffer data values with delay
	 * @param interval interval between two reads
	 * @param unit TimeUnit for both interval 
	 */
	public void doPool(long interval, TimeUnit unit);
	
	/**
	 * Stop pooling
	 */
	public void interuptPool() ;


	/**
	 * get buffered data by the pooling
	 * 
	 * @return List of RfidTag Object 
	 */
	public List<RfidTag> getTags();

	/**
	 * Resets the tikitag
	 */
	public void reset();	

}
