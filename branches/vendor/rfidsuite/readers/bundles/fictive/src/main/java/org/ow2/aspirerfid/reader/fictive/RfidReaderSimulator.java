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
package org.ow2.aspirerfid.reader.fictive;

import java.io.InputStream;
import java.util.Dictionary;
import java.util.Properties;

import org.apache.felix.ipojo.handlers.event.publisher.Publisher;
import org.osgi.service.event.EventConstants;
import org.osgi.service.log.LogService;
import org.ow2.aspirerfid.reader.util.config.Configuration;
import org.ow2.aspirerfid.util.RFIDConstants;

/**
 * This class implements a fictive RFID reader. On each step, it choose a random
 * number of RFID Guid in a list of tags defined in a properties file.
 * Configurable parameters are: <br/>- period : the step period in milliseconds
 * <br/>- logicalName : the fictive reader name. <br/>- readerGuid : the fictive
 * reader identifier. The maximum of read tags, and the tag list in which the
 * fictive reader will randomly chose the read one are defined in a properties
 * configFile. The number of read tags is randomly chosen between 0 and the
 * maximum defined in the configuration file.
 * 
 * @author Anne Robert
 * @author Guillaume Surrel
 * @author Perisanidi Maroula
 * @author Lionel Touseau
 * @version 3.0.0 01/2009
 */
public class RfidReaderSimulator implements Runnable {

	/**
	 * Time between two polls in milliseconds.
	 */
	private int period = 3000;

	/**
	 * Logical name associated with the reader.
	 */
	private String logicalName = "fictive";

	/**
	 * Guid of the reader periodical task in cron service.
	 */
	private String readerGUId = "fictiveguid";

	/**
	 * Object to build the random sequences.
	 */
	private RfidSimulator reader;

	/**
	 * State of the reader.
	 */
	private boolean running = false;
	
	/**
	 * Default name of the config File where Maximum, and RFID tags list are
	 * read.
	 */
	private String configFile = "/STATE-INF/initialstate.properties";

	/**
	 * List of tags read from the properties file.
	 */
	private String[] tl;

	/**
	 * Logger used to record errors, warnings, information and debug messages.
	 * LogService optional reference injected by iPOJO
	 */
	private LogService logger;

	/**
	 * iPOJO EventAdmin Handler publisher.
	 */
	private Publisher m_publisher;		
	
	/**
	 * Constructor called by iPOJO
	 */
	private RfidReaderSimulator() {
		logger.log(LogService.LOG_INFO, "{RFIDREADERSIMULATOR}");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public synchronized void run() {		

		while (running /*bundleIsActive()*/) {

			if (tl!=null) {
		        Dictionary e = new Properties();
		        // Fill out the event
		        e.put(RFIDConstants.TAGGUID_KEY, getTag());
		        e.put(RFIDConstants.READERGUID_KEY, getReaderGUId());
		        e.put(RFIDConstants.READERNAME_KEY, getLogicalName());
		        e.put(EventConstants.TIMESTAMP, Long.toString(System.currentTimeMillis()));

		        // Send event via iPOJO eventAdmin handler
		        m_publisher.send(e);
			}
			try {				
				wait(period);
			} catch (InterruptedException ie) {
				/* will recheck quit */
			}
		}
	}

	/**
	 * This method replace the getMeasurement that works only for RFIDTagRead structure
	 * @return a random Tag
	 */
	private String getTag(){
		int num = randomNumber(1, tl.length);
		return tl[num - 1];
	}

	/**
	 * Returns a random integer value from the interval [min,max].
	 * 
	 * @param min
	 *            minimum value
	 * @param max
	 *            maximum value
	 * @return
	 */
	private int randomNumber(int min, int max) {
		return min + (int) (Math.random() * (max - min));
	}

	// __________________________________________________
	// MBean Interface : getter and setter on attributes
	// __________________________________________________

	// implementation of MBean interface
	// setter of max attribute
	/**
	 * setter for the maximum of simultaneous read tag the reader will read a
	 * random number between 0 and the max
	 * 
	 * @param max
	 *            the maximum tag read in a command
	 */
	public void setMaxTagRead(int max) {
		reader.setMaxTagRead(max);

	}

	// implementation of MBean interface
	// getter of max attribute
	/**
	 * getter for the maximum of simultaneous read tag the reader will read a
	 * random number between 0 and the max
	 * 
	 * @return the max number of tag read in a command
	 */
	public int getMaxTagRead() {
		return reader.getMaxTagRead();
	}

	// implementation of MBean interface
	// setter of tagList attribute
	/**
	 * setter for the tag list used to randomly choose read tags.
	 * 
	 * @param tagList
	 *            a string array containing the Rfid guid
	 */
	public void setTagList(String[] tagList) {
		reader.setTagList(tagList);
	}

	// implementation of RFID Reader MBean interface
	/**
	 * getter for the tag list used to randomly choose read tags.
	 * 
	 * @return the list of tag fictitiously read
	 */
	public String[] getTagList() {
		return reader.getTagList();
	}

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
	 * @see org.ow2.aspirerfid.reader.fictive.RfidReaderSimulatorMBean#setReaderGUId(java.lang.String)
	 */
	public void setReaderGUId(String readerGuid) {
		if (running) {
			// Stop the cron, because it is referenced by the Guid
			stopReader();
			this.readerGUId = readerGuid;
			restart();
		} else {
			this.readerGUId = readerGuid;
		}
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
		// if cron is activated, stop it and reactivate it
		if (running) {
			stopReader();
			restart();
		}
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
	 * initialize the reader. Attributes may be defined by the properties
	 * parameter.
	 * 
	 * @param properties
	 */
	public void init(Properties properties) {
		
		// initialize attributes
		logger.log(LogService.LOG_INFO, "create the rfid simulator");
		reader = new RfidSimulator(properties);
		logger.log(LogService.LOG_DEBUG, "Max tag read : "
				+ reader.getMaxTagRead());
		tl = reader.getTagList();
		logger.log(LogService.LOG_DEBUG, "Tags : ");
		for (int i = 0; i < tl.length; i++) {
			logger.log(LogService.LOG_DEBUG, "\t - " + tl[i]);
		}

		if (properties != null)
			try {
				// read initial value of attributes in properties
				logger.log(LogService.LOG_INFO, "read properties");
				String prop;
				prop = properties.getProperty("logicalName");
				if (prop != null)
					logicalName = prop;

				prop = properties.getProperty("period");
				if ((prop != null) && (prop.length() > 0)) {
					period = new Integer(prop).intValue();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	/**
	 * Start, or restart the reader
	 */
	private void restart() {
		logger.log(LogService.LOG_INFO, "fictive reader restart");
		if (!running) {
			running = true;
			Thread readerThread = new Thread(this,"FictiveReader");
			readerThread.start();
		}
	}

	/**
	 * Start the fictive reader. It initializes parameters with the properties
	 * parameter, and starts the read task.
	 * 
	 * @param properties
	 */
	public void startReader(Properties properties) {
		// First read its parameters in properties
		init(properties);
		// Then start the periodically read
		restart();
	}

	/**
	 * Start the fictive reader. It initializes parameters with the
	 * configuration file, and start the periodically read task
	 * 
	 * @see org.ow2.aspirerfid.reader.RfidReaderMBean#startReader()
	 */
	public void startReader() {
		Properties properties;
		// Load properties from configLocation file
		InputStream is = getClass().getResourceAsStream(configFile);
		logger.log(LogService.LOG_INFO, configFile);
		// If the file does not exist, no properties are defined
		if (is == null)
			properties = null;
		// analyze the config file to build properties
		else
			properties = Configuration.loadProperties(is);
		
		startReader(properties);
		
	}

	/**
	 * Stop the reader, and remove the periodical task from the cron service
	 * 
	 * @see org.ow2.aspirerfid.reader.RfidReaderMBean#stopReader()
	 */
	public void stopReader() {
		running = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.RfidReaderMBean#dispose()
	 */
	public void dispose() {
		System.err
				.println("The fictive reader has no driver, it cannot be disposed.");
	}
	
}
