/*
 * Copyright © 2008-2010, Aspire 
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
package org.ow2.aspirerfid.reader.intermecif5;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.service.event.EventAdmin;

import org.ow2.aspirerfid.util.RFIDConstants;
import org.ow2.aspirerfid.common.cron.CronService;


/**
 * @author Nektarios Leontiadis
 *
 */
public class IntermecIF5Driver implements IntermecIF5DriverMBean {

	private String eventTopic, coordinates, logicalName;
	
	private BundleContext context;

	private CronService cronService;

	private EventAdmin eventAdmin;
	
	private int readerNumber = 1;
	
	private int period, port;
	
	private List readers;

	private String readerGUId, address;
	/**
	 * 
	 */
	public IntermecIF5Driver(BundleContext context) {
		readers = new ArrayList();
		this.context = context;
		initConfig();
	}

	private void initConfig() {
		eventTopic = RFIDConstants.TOPIC_DETECTION;
		coordinates = "37.943514,23.870048";
		logicalName = "intermec";
		port = 2189;
		period = 5;
		address = "192.168.212.233";
		readerGUId = "167256187";
		
	}
	
	private void createReader() {
		IntermecIF5Reader reader = new IntermecIF5Reader(context, readerNumber);
		readerNumber++;
		
		reader.setLogicalName(logicalName);
		reader.setGpsCoordinates(coordinates);
		reader.setRFIDEventTopic(eventTopic);
		reader.setAddress(address);
		reader.setPort(port);

		reader.setRepeatPeriod(new Integer(period).intValue());
		reader.setLogicalName(logicalName);
		reader.setGpsCoordinates(coordinates);
		reader.setRFIDEventTopic(eventTopic);
		reader.bindCronService(cronService);
		reader.bindEventAdminService(eventAdmin);
		
		readers.add(reader);
		reader.startReader();
	}

	/**
	 * stop the driver and all the generated readers
	 */
	public void stop() {
		Iterator iter = readers.iterator();
		while (iter.hasNext()) {
			IntermecIF5Reader reader =  (IntermecIF5Reader)iter.next();
			reader.dispose();
		}
	}
	public String getEventTopic() {
		return eventTopic;
	}

	public String getGpsCoordinates() {
		return coordinates;
	}

	public String getLogicalName() {
		return logicalName;
	}

	public String getReaderGUId() {
		return readerGUId;
	}

	public int getRepeatPeriod() {
		return period;
	}

	public void newReader() {
		createReader();
	}

	public void setEventTopic(String eventTopic) {
		this.eventTopic = eventTopic;
	}

	public void setGpsCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}

	public void setLogicalName(String logicalName) {
		this.logicalName = logicalName;
	}

	public void setReaderGUId(String readerId) {
		this.readerGUId = readerId;
	}

	public void setRepeatPeriod(int period) {
		this.period = period;
	}
	
	public void removeCron(CronService ref) {
		Iterator iter = readers.iterator();
		while (iter.hasNext()) {
			IntermecIF5Reader reader = (IntermecIF5Reader)iter.next();
			reader.unbindCronService();
		}
		cronService = null;
	}

	public void addCron(CronService ref) {
		Iterator iter = readers.iterator();
		while (iter.hasNext()) {
			IntermecIF5Reader reader =  (IntermecIF5Reader)iter.next();
			reader.bindCronService(ref);
		}
		cronService = ref;
	}

	public void removeEventAdmin() {
		Iterator iter = readers.iterator();
		while (iter.hasNext()) {
			IntermecIF5Reader reader = (IntermecIF5Reader)iter.next();
			reader.unbindEventAdminService();
		}
		eventAdmin = null;
	}

	public void addEventAdmin(EventAdmin ea) {
		Iterator iter = readers.iterator();
		while (iter.hasNext()) {
			IntermecIF5Reader reader =  (IntermecIF5Reader)iter.next();
			reader.bindEventAdminService(ea);
		}
		eventAdmin = ea;
	}

	public String getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public void setAddress(String ipAddress) {
		address = ipAddress;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
