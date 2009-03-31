/*
 * Copyright 2005-2008, Aspire
 * 
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation (the "LGPL"); either version 2.1 of the 
 * License, or (at your option) any later version. If you do not alter this 
 * notice, a recipient may use your version of this file under either the 
 * LGPL version 2.1, or (at his option) any later version.
 * 
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this library; if not, write to the Free Software Foundation, 
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY 
 * KIND, either express or implied. See the GNU Lesser General Public 
 * License for the specific language governing rights and limitations.
 */
package org.ow2.aspirerfid.reader.remote.http;

import java.util.Dictionary;
import java.util.Properties;

import org.apache.felix.ipojo.handlers.event.publisher.Publisher;
import org.osgi.service.event.EventConstants;
import org.osgi.service.log.LogService;
import org.ow2.aspirerfid.util.Logger;
import org.ow2.aspirerfid.util.RFIDConstants;
import org.ow2.aspirerfid.wires.RFIDTagRead;

/**
 * Propagates tag reading events
 * 
 * @author Kiev Gama
 * @author Lionel Touseau 2009
 *
 * TODO the "period" field and methods are not useful for this reader
 * TODO redo the comments and methods that are not suited for this HttpReader
 */
public class RfidHttpReader {
	/**
	 * Time between two polls in milliseconds.
	 */
	private int period = 3000;

	/**
	 * Logical name associated with the reader.
	 */
	private String logicalName = "http";

	/**
	 * Guid of the reader periodical task in cron service.
	 */
	private String readerGUId = "httpguid";

	/**
	 * Logger used to record errors, warnings, information and debug messages.
	 */
	private Logger logger;

	private String gpsCoordinates;
	
	private static RfidHttpReader instance;
	
	/**
	 * iPOJO eventAdmin handler publisher
	 */
	private Publisher httpPublisher;

	/**
	 * @param bc
	 *            BundleContext used for service registrations.
	 */
	RfidHttpReader() {
		logger = new Logger("HTTPReader", Logger.INFO);
		instance = this;
	}
	
	static RfidHttpReader getInstance() {
		return instance;
	}
	
	public String getGpsCoordinates() {
		return gpsCoordinates;
	}
	
	public void setGpsCoordinates(String coordinates) {
		this.gpsCoordinates = coordinates;
	}
	
	
	void sendEvent(Dictionary tagInfo) {
		// add complementary information to the tag reading
		tagInfo.put(RFIDConstants.READERNAME_KEY, this.getLogicalName());
		if (this.gpsCoordinates != null) {
			tagInfo.put(RFIDConstants.COORDINATES_KEY,this.gpsCoordinates);
		}
		// send the Event using iPOJO eventAdmin handler
		httpPublisher.send(tagInfo);
	}
	
	// __________________________________________________
	// MBean Interface : getter and setter on attributes
	// __________________________________________________

		/**
	 * implementation of RFID Reader MBean interface setter of logicalName
	 * attribute
	 * 
	 * @see org.ow2.aspirerfid.reader.RfidReaderMBean#setLogicalName(java.lang.String)
	 */
	public void setLogicalName(String logName) {
		this.logicalName = logName;
	}

	/**
	 * implementation of RFID Reader MBean interface getter of logicalName
	 * attribute
	 * 
	 * @see org.ow2.aspirerfid.reader.RfidReaderMBean#getLogicalName()
	 */
	public String getLogicalName() {
		return logicalName;
	}

	/**
	 * implementation of RFID Reader MBean interface setter of logicalName
	 * attribute
	 * 
	 * @see org.ow2.aspirerfid.reader.remote.http.RfidHttpReaderMBean#setReaderGUId(java.lang.String)
	 */
	public void setReaderGUId(String readerGuid) {
		this.readerGUId = readerGuid;
	}

	/*
	 * implementation of RFID Reader MBean interface getter of logicalName
	 * attribute
	 * 
	 * @see org.ow2.aspirerfid.reader.RfidReaderMBean#getReaderGUId()
	 */
	public String getReaderGUId() {
		return readerGUId;
	}

	/**
	 * implementation of RFID Reader MBean interface setter of period attribute
	 * 
	 * @see org.ow2.aspirerfid.reader.RfidReaderMBean#setRepeatPeriod(int)
	 */
	public void setRepeatPeriod(int period) {
		this.period = period;
		stopReader();
		restart();
	}

	/**
	 * implementation of RFID Reader MBean interface getter of period attribute
	 * 
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.RfidReaderMBean#getRepeatPeriod()
	 */
	public int getRepeatPeriod() {
		return period;
	}


	/**
	 * Start, or restart the reader
	 */
	private void restart() {
		logger.log(LogService.LOG_INFO, "HTTP reader restart");
	}

	/**
	 * Start the HTTP reader. It initializes parameters with the properties
	 * parameter, and starts the read task.
	 * 
	 * @param properties
	 */
	public void startReader(Properties properties) {
		// First read its parameters in properties
		restart();
	}

	/**
	 * Start the HTTP reader. It initializes parameters with the
	 * configuration file, and start the periodically read task
	 * 
	 * @see org.ow2.aspirerfid.reader.RfidReaderMBean#startReader()
	 */
	public void startReader() {
		restart();
	}

	/**
	 * Stop the reader, and remove the periodical task from the cron service
	 * 
	 * @see org.ow2.aspirerfid.reader.RfidReaderMBean#stopReader()
	 */
	public void stopReader() {
	}

	/*
	 * @see org.ow2.aspirerfid.reader.RfidReaderMBean#dispose()
	 */
	public void dispose() {
		logger.log(Level.WARNING,"The HTTP reader has no driver, it can't be disposed.");
	}

	/*
	 * In the props we add only the information related to the tag. The
	 * temperature and position values are given by separate producers.
	 * 
	 */
	public RFIDTagRead readThisTag(String tag) {
		RFIDTagRead tagProp = new RFIDTagRead();
		String elmts = getReaderGUId();
		if (elmts != null)
			tagProp.put(RFIDConstants.READERGUID_KEY, elmts);
		elmts = getLogicalName();
		if (elmts != null)
			tagProp.put(RFIDConstants.READERNAME_KEY, elmts);
		elmts = Long.toString(System.currentTimeMillis());
		if (elmts != null)
			tagProp.put(EventConstants.TIMESTAMP, elmts);
		tagProp.put(RFIDConstants.TAGGUID_KEY, tag);

		return tagProp;
	}
}
