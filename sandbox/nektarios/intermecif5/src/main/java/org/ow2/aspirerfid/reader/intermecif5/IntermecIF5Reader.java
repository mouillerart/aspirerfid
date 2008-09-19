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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.ListIterator;
import java.util.Vector;
import java.util.logging.Logger;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.event.EventConstants;

import org.ow2.aspirerfid.util.RFIDConstants;
import org.ow2.aspirerfid.common.cron.CronService;
import org.ow2.aspirerfid.common.cron.TimedObject;
import org.ow2.aspirerfid.reader.RfidReaderMBean;

import org.ow2.aspirerfid.common.tdt.TDT;

/**
 * @author Nektarios Leontiadis
 *
 */
public class IntermecIF5Reader implements RfidReaderMBean, TimedObject {

	private String detectionTopic = RFIDConstants.TOPIC_DETECTION;
	private EventAdmin ea;
	private String readerGUId;
	private int period, port;
	/**
	 * reader logical name.
	 */
	private String logicalName, cronName, address;
	/**
	 * save the last event to not duplicate it on successive read.
	 */
	Event2 lastEvent;
	/**
	 * reader coordinates. For this reader, the geographical coordinates are fix
	 * by configuration
	 */
	private String coordinates;
	/**
	 * cron service to periodically send a read command. Automatically binded by
	 * iPOJO
	 */
	private CronService cronService;
	/**
	 * true between call of startReader and stopReader functions.
	 */
	private boolean active = false;
	private ServiceRegistration serviceRegistration;
	private Socket socket;
	private BufferedReader in;
	private DataOutputStream out;
	private TagPoller poller;
	protected String encodingType;
	private TDT tdt;

	public IntermecIF5Reader(BundleContext context, int readerNumber) {
		readerGUId = "Intermec " + readerNumber;
		cronName = "Intermec";
		active = false;
		Dictionary props = new Hashtable();
		props.put("jmxagent.objectname",
				"rfid:type=reader,SymbolicName=IntermecIF5Reader"
						+ readerNumber);

		serviceRegistration = context.registerService(
				"org.ow2.aspirerfid.rfid.intermecif5.IntermecIF5Reader", this,
				props);
		
		ServiceReference ref = context.getServiceReference(TDT.class.getName());
		if(ref != null)
		{
			tdt = (TDT)context.getService(ref);
		}
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setPort(int port) {
		this.port = port;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.RfidReaderMBean#dispose()
	 */
	public void dispose() {
		stopReader();
		serviceRegistration.unregister();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.RfidReaderMBean#getGpsCoordinates()
	 */
	public String getGpsCoordinates() {
		return coordinates;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.RfidReaderMBean#getLogicalName()
	 */
	public String getLogicalName() {
		return logicalName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.RfidReaderMBean#getRFIDEventTopic()
	 */
	public String getRFIDEventTopic() {
		return detectionTopic;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.RfidReaderMBean#getReaderGUId()
	 */
	public String getReaderGUId() {
		return readerGUId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.RfidReaderMBean#getRepeatPeriod()
	 */
	public int getRepeatPeriod() {
		return period;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.RfidReaderMBean#setGpsCoordinates(java.lang.String)
	 */
	public void setGpsCoordinates(String coord) {
		coordinates = coord;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.RfidReaderMBean#setLogicalName(java.lang.String)
	 */
	public void setLogicalName(String logName) {
		this.logicalName = logName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.RfidReaderMBean#setRFIDEventTopic(java.lang.String)
	 */
	public void setRFIDEventTopic(String topic) {
		detectionTopic = topic;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.RfidReaderMBean#setRepeatPeriod(int)
	 */
	public void setRepeatPeriod(int period) {
		this.period = period;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.RfidReaderMBean#startReader()
	 */
	public synchronized void startReader() {
		if (!active) {
			active = true;
			try {
				socket = new Socket(address, port);
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new DataOutputStream(socket.getOutputStream());
				for (int i = 0; i < 8; i++)
					// log.debug("Connect Error:"+in.readLine());
					System.err.println(106 + in.readLine());

				System.err.println("Start reading");
				poller = new TagPoller(period);
				poller.start();
				System.err.println("Started");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.RfidReaderMBean#stopReader()
	 */
	public void stopReader() {
		if (active) {
			poller.stopPolling();
			active = false;
			try {
				if (socket != null && socket.isConnected())
					socket.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public void doReact(Serializable serializable) {
		process(readerGUId, poller.read());
	}

	/**
	 * compute the command response to publish an event
	 * 
	 * @param reader
	 * @param tagid
	 */
	public void process(String reader, ArrayList tags) {
		Event2 event;
		String tagid;
		ListIterator iterator = tags.listIterator();
		while (iterator.hasNext()) {
			String tag=(String) iterator.next();
			System.err.println("INITIAL TAG "+tag);
			tagid = tag;
			System.err.println("Tagid "+tagid);
			// create the sensor event
			event = new Event2("uuid:" + reader, // "uuid:tagit:" +
					tagid, Event2.DETECTION, System.currentTimeMillis());
			// check if the new event is not redundant with the last send event
			if (lastEvent != null
					&& event.getReaderGUId().equals(lastEvent.getReaderGUId())
					&& event.getTagGUId().equals(lastEvent.getTagGUId())) {
				return;
			}

			// store the new event as the last one
			lastEvent = event;

			// create a dictionary for the event properties
			Hashtable props = new Hashtable();
			// props.put(EventConstants.BUNDLE_SYMBOLICNAME,
			// context.getBundle().getHeaders());
			// add the event information in this dictionary
			props.put(RFIDConstants.READERGUID_KEY, event.getReaderGUId());
			if (logicalName != null)
				props.put(RFIDConstants.READERNAME_KEY, logicalName);
			if (coordinates != null)
				props.put(RFIDConstants.COORDINATES_KEY, coordinates);
			props.put(RFIDConstants.TAGGUID_KEY, event.getTagGUId());
			props.put(EventConstants.TIMESTAMP, new Long(event.getTimestamp())
					.toString());

			// find the publish topic depending on the sensor event type
			String topic;
			switch (event.getType()) {
			case Event2.DETECTION:
				// new tag detection
				topic = detectionTopic;
				break;
			default:
				topic = null; // should throw a exception
				break;
			}

			// build the event
			Event e = new Event(topic, props);
			// use the publish object is not null (replace eventadmin service)
			if (ea != null)
				ea.postEvent(e);
		}
	}

	/**
	 * bind the cronService service. Used only in non iPOJO version
	 * 
	 * @param cron2
	 *            the reference to cronService service
	 */
	public void bindCronService(CronService cron2) {
		cronService = cron2;
		if (active) {
			// and add the periodical task
			cronService.add(this, cronName, cronService.cronString(period));
		}
	}

	/**
	 * unbind the cronService service: dereference the service. Used only in non
	 * iPOJO version
	 */
	public void unbindCronService() {
		cronService.remove(this, cronName);
		cronService = null;
	}

	/**
	 * bind the eventadmin service. Used only in non iPOJO version
	 * 
	 * @param ea
	 *            the reference to event admin service
	 */
	public void bindEventAdminService(EventAdmin ea) {
		this.ea = ea;
	}

	/**
	 * unbind the event admin service: dereference the service. Used only in non
	 * iPOJO version
	 */
	public void unbindEventAdminService() {
		this.ea = null;
	}
	
	public String toURI(String tagID) {
        String uri;

            //need to have 24 hex digits
            if (tagID.length() != 24) {
                int missingDigits = 24 - tagID.length();
                for (int j = 0; j < missingDigits; j++) {
                    tagID = "0".concat(tagID);
                }
            }
            //place a valid header (GID-96)
            tagID = "35".concat(tagID.substring(2));

            //if (encodingType.equalsIgnoreCase("pure")) {
                uri = convert(hexEPC2binEPC(tagID), "PURE IDENTITY");
            //} else if (encodingType.equalsIgnoreCase("tag")) {
            //    uri = convert(hexEPC2binEPC(tagID), LevelTypeList.TAG_ENCODING);
            //}
        
        return uri;
    }
    
    
//    public Vector toURI(Vector tagIDs) {
//        Vector uris = new Vector();
//        for (int i = 0; i < tagIDs.size(); i++) {
//            String tagID = (String) tagIDs.elementAt(i);
//            //need to have 24 hex digits
//            if (tagID.length() != 24) {
//                int missingDigits = 24 - tagID.length();
//                for (int j = 0; j < missingDigits; j++) {
//                    tagID = "0".concat(tagID);
//                }
//            }
//
//            //place a valid header (GID-96)
//            tagID = "35".concat(tagID.substring(2));
//
//            String uri = "";
//            if (encodingType.equalsIgnoreCase("pure")) {
//                uri = convert(hexEPC2binEPC(tagID), "PURE_IDENTITY");
//            } else if (encodingType.equalsIgnoreCase("tag")) {
//                uri = convert(hexEPC2binEPC(tagID), "TAG_ENCODING");
//            }
//            uris.add(uri);
//        }
//        return uris;
//    }

    private String convert(String tagID, String levelTypeList) {
        HashMap extraparams = new HashMap();
        extraparams.put("taglength", "96");
        extraparams.put("filter", "1");
        extraparams.put("companyprefixlength", "7");

        return tdt.convert(tagID, extraparams, levelTypeList);
    }

    private String hexEPC2binEPC(String hexEPC) {

        String binEPC = "";
        for (int i = 0; i < hexEPC.length(); i++) {
            String hexEPCDigit = "";
            if (i == (hexEPC.length() - 1)) {
                hexEPCDigit = hexEPC.substring(i);
            } else {
                hexEPCDigit = hexEPC.substring(i, i + 1);
            }
            binEPC += hex2bin(hexEPCDigit);
        }
        return binEPC;
    }
    
    private String hex2bin(String hex) {
        int dec = Integer.parseInt(hex, 16);
        String bin = Integer.toBinaryString(dec);
        if (bin.length() < 4) {
            //zero padding until 4
            int zeros = 4 - bin.length();
            for (int i = 0; i < zeros; i++) {
                bin = "0".concat(bin);
            }
        }
        return bin;
    }

	class TagPoller extends Thread {

		private boolean running;
		private long interval;

		public TagPoller(int pollInterval) {
			interval = pollInterval * 1000;
		}

		public void run() {
			if (running)
				return;

			running = true;
			System.err.println("Poll interval: "+interval);
			while (running) {
				try {
					try {
					process(readerGUId, read());
					}catch(Exception e)
					{
						System.err.println(e.getMessage());
					}
					System.err.println("sleeping");
					Thread.sleep(5000);
				} catch (Exception e) {
				}
			}
		}

		public void stopPolling() {
			System.err.println("Stopping");
			running = false;
		}

		synchronized ArrayList read() {
			ArrayList tags = new ArrayList();

			
			if (active) {
				try {
					String charsRead;
					
						// log.debug("Read Error:"+in.readLine());

						out.writeBytes("READ \n");

						while (true) {
							charsRead = in.readLine();
							
							if (charsRead.equalsIgnoreCase("ERR")) {
								throw new Exception("ERR: Error while reading");
							}
							if (charsRead.compareToIgnoreCase("OK>") == 0)
								break;
							tags.add(charsRead.replaceFirst("H", ""));
						}
						System.err.println(charsRead);
					System.err.println("Tags read:"+tags.size());
				} catch (Exception ex) {
					System.err.println(ex.getMessage());
					ex.printStackTrace();
				}
			}
			return tags;
		}
	}
}
