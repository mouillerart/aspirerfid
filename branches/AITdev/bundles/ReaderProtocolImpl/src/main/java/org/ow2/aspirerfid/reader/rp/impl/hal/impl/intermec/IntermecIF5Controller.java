/*
 * Copyright © 2008-2010, Aspire 
 * 
 * Aspire is free software; you can redistribute it and/or 
 * modify it under  the terms of the GNU Lesser General Public 
 * License version 2.1 as published by the Free Software Foundation (the 
 * "LGPL"). 
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library in the file COPYING-LGPL-2.1; if not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301 USA. 
 * 
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTY 
 * OF ANY KIND, either express or implied. See the GNU Lesser General Public 
 * License for the specific language governing rights and limitations.
 *  
 */
package org.ow2.aspirerfid.reader.rp.impl.hal.impl.intermec;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.ow2.aspirerfid.reader.rp.impl.hal.AsynchronousIdentifyListener;
import org.ow2.aspirerfid.reader.rp.impl.hal.ControllerProperties;
import org.ow2.aspirerfid.reader.rp.impl.hal.HardwareAbstraction;
import org.ow2.aspirerfid.reader.rp.impl.hal.HardwareException;
import org.ow2.aspirerfid.reader.rp.impl.hal.MemoryBankDescriptor;
import org.ow2.aspirerfid.reader.rp.impl.hal.MemoryDescriptor;
import org.ow2.aspirerfid.reader.rp.impl.hal.Observation;
import org.ow2.aspirerfid.reader.rp.impl.hal.ReadPointNotFoundException;
import org.ow2.aspirerfid.reader.rp.impl.hal.TagDescriptor;
import org.ow2.aspirerfid.reader.rp.impl.hal.Trigger;
import org.ow2.aspirerfid.reader.rp.impl.hal.UnsignedByteArray;
import org.ow2.aspirerfid.reader.rp.impl.hal.UnsupportedOperationException;
import org.ow2.aspirerfid.reader.rp.impl.hal.transponder.EPCTransponderModel;
import org.ow2.aspirerfid.reader.rp.impl.hal.transponder.IDType;
import org.ow2.aspirerfid.reader.rp.impl.hal.transponder.InventoryItem;
import org.ow2.aspirerfid.reader.rp.impl.hal.util.ByteBlock;
import org.ow2.aspirerfid.reader.rp.impl.hal.impl.intermec.Tag;
import org.ow2.aspirerfid.reader.rp.impl.core.util.ResourceLocator;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.*;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * Intermec IF5 HAL controller
 * 
 * @author Kefalakis Nikos (nkef@ait.edu.gr), Nektarios Leontiadis (nele@ait.edu.gr)
 */

public class IntermecIF5Controller implements HardwareAbstraction {

	/**
	 * The logger.
	 */
	private static Logger log = Logger.getLogger(IntermecIF5Controller.class);

	// ------ Controller fields --------

	/**
	 * The name of the HAL.
	 */
	private String halName;
	// =============================================================
	/**
	 * The properties file.
	 */
	private String defaultPropFile = "/props/IntermecIF5Controller_default.xml";
	private String propFile;

	/**
	 * Logical read points
	 */
	private HashMap<String, Integer> readPoints;
	private HashMap<Integer, String> antennaNames;

	/**
	 * Number of read points
	 */
	private int nOfReadPoints = 0;
	// ===========================================================
	/**
	 * The names of all available read points connected to this reader.
	 */
	private String[] readPointNames;

	/**
	 * Contains the names of all read points that are ready.
	 */
	private Vector<String> readyReadPoints;

	//TODO:add the curent inventory feature so as to select the right tags properties.
//	/**
//	 * The current inventory (all seen ids but only at one antenna)
//	 */
//	private HashMap<String, InventoryItem> currentInventory = new HashMap<String, InventoryItem>();

	/**
	 * The port number the reader is listening
	 */
	private int port;

	
	/**
	 * The address of the reader
	 */
	private String address;
	// ===================================
	private String readerInfo[];
	private String eol = "/n";
	private BufferedReader in;
	private DataOutputStream out;
	private static final String LOCK_CODE = String.valueOf(0xBB);
	private static Socket socket;
	// ===================================

	// ----- Properties ----------------

	/**
	 * The configuration
	 */
	private XMLConfiguration config = null;

	/**
	 * Properties file for transponder models.
	 */
	private String epcTransponderModelsConfig;

	/**
	 * This <code>HashMap</code> has an entry for each read point (antenna).
	 * Entry: Key: readPoint (String) Value: readpoint/antenna content (HashSet)
	 */
	private HashMap reader;

	// /**
	// * Contains the names of all read points that are ready.
	// */
	// private Vector<String> readyReadPoints;

	/**
	 * Contains the names of all read points whose next identify-operation will
	 * fail.
	 */
	public Vector<String> identifyError = new Vector<String>();

	/**
	 * Contains the names of all read points whose next read-operation will
	 * fail.
	 */
	public Vector<String> readError = new Vector<String>();

	/**
	 * Contains the names of all read points whose next write-operation will
	 * fail.
	 */
	public Vector<String> writeError = new Vector<String>();

	/**
	 * Contains the names of all read points whose next kill-operation will
	 * fail.
	 */
	public Vector<String> killError = new Vector<String>();

	/**
	 * Contains the names of all read points whose identify-operations will
	 * always fail.
	 */
	public Vector<String> continuousIdentifyErrors = new Vector<String>();

	/**
	 * Contains the names of all read points whose read-operations will always
	 * fail.
	 */
	public Vector<String> continuousReadErrors = new Vector<String>();

	/**
	 * Contains the names of all read points whose write-operations will always
	 * fail.
	 */
	public Vector<String> continuousWriteErrors = new Vector<String>();

	/**
	 * Contains the names of all read points whose kill-operations will always
	 * fail.
	 */
	public Vector<String> continuousKillErrors = new Vector<String>();

	public IntermecIF5Controller(String halName, String propFile) {
		this.halName = halName;
		this.propFile = propFile;
		this.initialize();

		log.debug("Hal Name: " + halName);
		reader = new HashMap();

		for (int i = 0; i < nOfReadPoints; i++) {

			reader.put(readPointNames[i], new HashSet());

		}
	}

	/**
	 * Initialize a reader.
	 */
	public void initialize() {

		// read parameters from configuration file
		config = new XMLConfiguration();
		config.setListDelimiter(',');
		URL fileurl = ResourceLocator.getURL(propFile, defaultPropFile);

		// sets the parameters according to the properties file
		try {
			config.load(fileurl);
			address = config.getString("address");
			log.debug(halName+" IP Address:"+address);
			port = config.getInt("port");
			log.debug(halName+" Connection Port:"+port);
			// ==================================================================

			readPoints = new HashMap<String, Integer>();
			antennaNames = new HashMap<Integer, String>();
	    	nOfReadPoints = config.getMaxIndex("readpoints.readpoint") + 1;	
			if (nOfReadPoints > 4) {
				nOfReadPoints = 4;
			}

			readPointNames = new String[nOfReadPoints];
			readyReadPoints = new Vector<String>();

			for (int i = 0; i < nOfReadPoints; i++) {
				// key to current read point
				String key = "readpoint(" + i + ")";
				// read point name
				String readpointName = config.getString("readpoints."+key + ".name");
				
				
				log.debug("Property found: " + key + ".name = " + readpointName);
				// read point connector
				//TODO:fix this bug
				int readpointConnector = config.getInt("readpoints."+key + ".connector");
				log.debug("Property found: " + key + ".connector = " + readpointConnector);
				if ((readpointConnector < 1) || (readpointConnector > 4)) {
					log.error("Readpoint connector value out of range, readpoint '" + readpointName + "' not added to read points.");
				} else {
					readPoints.put(readpointName, readpointConnector);
					antennaNames.put(readpointConnector, readpointName);
					readyReadPoints.add(readpointName);
					readPointNames[i] = readpointName;
				}
				// readPointNames = (String[]) readPoints.keySet().toArray();
			}
			// ==============================================================================

			epcTransponderModelsConfig = config.getString("epcTransponderModelsConfig");
			eol = config.getString("endOfLineChars");
			if (eol == null) {
				eol = "/n";
			}
			// nOfReadPoints =
			// Integer.parseInt(config.getString("numberOfReadPoints"));

			// for (int i = 0; i < nOfReadPoints; i++) {
			// readPointNames[i] = this.config.getString("readPoint_" + (i +
			// 1));
			// readyReadPoints.add(readPointNames[i]);
			// }

			connect();
			// TODO:Fill the reader Info
//			 if (readerInfo == null) {
//			 readerInfo = new String[5];
//			 synchronized (out) {
//			 out.writeBytes("HWID" + eol);
//			 readerInfo[0] = "Hardware Id:" + in.readLine();
//			 out.writeBytes("HWPROD" + eol);
//			 readerInfo[1] = "Product name:" + in.readLine();
//			 out.writeBytes("HWREGION" + eol);
//			 readerInfo[2] = "Hardware region:" + in.readLine();
//			 out.writeBytes("HWVER" + eol);
//			 readerInfo[3] = "Board version level:" + in.readLine();
//			 out.writeBytes("SWVER" + eol);
//			 readerInfo[4] = "Firmware version:" + in.readLine();
//			 }
//			 }

		} catch (Exception e) {
			String message = "Couldn't initialize the reader:" + e.getMessage();
			log.fatal(message);
			e.printStackTrace();
		}
	}

	private final void connect() throws UnknownHostException, IOException, HardwareException {
		if (in == null || out == null || socket == null || !socket.isConnected()) {
			socket = new Socket(address, port);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new DataOutputStream(socket.getOutputStream());
			for (int i = 0; i < 8; i++)
				//log.debug("Connect Error:"+in.readLine());
				log.debug(106 + in.readLine());
		}
	}

	private final void disconnect() {
		try {
			if (socket != null && socket.isConnected())
				socket.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private ArrayList<String> read(String prefix, String antennas) throws HardwareException {
		ArrayList<String> tags = new ArrayList<String>();
		try {
			String charsRead;
			connect();
			synchronized (out) {
				if (antennas != null) {
					// antennas = antennas.trim().replace(' ', ',');
					out.writeBytes("ATTRIBUTE ANTS=" + antennas + "\n");
				} else {
					out.writeBytes("ATTRIBUTE ANTS=" + "1,2,3,4" + eol);
				}
				//log.debug("Read Error:"+in.readLine());
				log.debug(133 + in.readLine());

				out.writeBytes("READ ANT ");
				if (prefix != null && !prefix.trim().equals("")) {
					out.writeBytes(" WHERE EPCID=" + prefix);
					//log.debug("Read Error:"+in.readLine());
					log.debug(" WHERE EPCID=" + prefix);
				}
				out.writeBytes("\n");

				while (true) {
					charsRead = in.readLine();
					if (charsRead.equalsIgnoreCase("ERR")) {
						throw new Exception("ERR: Error while reading");
					}
					if (charsRead.compareToIgnoreCase("OK>") == 0)
						break;
					if (charsRead.startsWith("H")) {
						charsRead = charsRead.substring(1);
					}
					if (!(charsRead.compareToIgnoreCase("NOTAG") == 0))
					tags.add(charsRead);
				}
			}// synchronized
			return tags;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error(ex);
			throw new HardwareException(ex.getMessage());
		}
	}

	// //-------------------- SimulatorEngine access methods
	// ----------------------------
	// /**
	// *
	// * To move a certain tag to a certain antenna.
	// *
	// * @param readPointName The name of the read point to which the tag should
	// be added.
	// * @param tag The tag object to be added to the antenna / read point.
	// *
	// * @return Returns true if tag has been added. False if tag with same
	// TagID already is in this antenna.
	// */
	// public boolean add(String readPointName, Tag tag){
	// log.info("'" + tag.getTagID() + "' ENTER the range of antenna '" +
	// readPointName + "'");
	// return ((Set)reader.get(readPointName)).add(tag);
	// }
	//		
	// /**
	// * To move a certain tag to a certain antenna.
	// *
	// * @param readPointName The name of the read point to which the tag should
	// be added.
	// * @param tagId The ID of a tag to be added to the antenna / read point. A
	// new (empty) tag object with ID tagId will be generated.
	// *
	// * @return Returns true if tag has been added. False if tag with same
	// TagID already is in this antenna.
	// */
	// public boolean add(String readPointName, String tagId){
	// Tag t = new Tag(tagId);
	// return add(readPointName, t);
	// }
	//		
	// /**
	// * To remove a certain tag from a certain antenna.
	// *
	// * @param readPointName The name of the read point from which the tag
	// should be removed.
	// * @param tag The tag object to be removed from the antenna / read point.
	// *
	// * @return Returns true if tag has been removed. False if no such tag
	// existed in this antenna.
	// */
	// public boolean remove(String readPointName, Tag tag){
	// log.info("'" + tag.getTagID() + "' EXIT the range of antenna '" +
	// readPointName + "'");
	// return ((Set)reader.get(readPointName)).remove(tag);
	// }
	//		
	// /**
	// * To remove a certain tag from a certain antenna.
	// *
	// * @param readPointName The name of the read point from which the tag
	// should be removed.
	// * @param tagId The ID of a tag to be removed from the antenna / read
	// point.
	// *
	// * @return Returns true if tag has been removed. False if no such tag
	// existed in this antenna.
	// */
	// public boolean remove(String readPointName, String tagId){
	// Tag t = new Tag(tagId);
	// return remove(readPointName, t);
	// }
	//		
	//	
	// /**
	// *
	// * To find out whether a certain tag is situated on a certain antenna.
	// *
	// * @param readPointName The name of the read point
	// * @param tag The tag.
	// *
	// * @return Returns true if such a tag is on this antenna. False if no such
	// tag existed in this antenna.
	// */
	// public boolean contains(String readPointName, Tag tag){
	// return ((Set)reader.get(readPointName)).contains(tag);
	// }
	//			
	// /**
	// * To find out whether a certain tag is situated on a certain antenna.
	// *
	// * @param readPointName The name of the read point
	// * @param tagId The ID of a tag.
	// *
	// * @return Returns true if a tag with ID tagId is on this antenna. False
	// if no such tag existed in this antenna.
	// */
	// public boolean contains(String readPointName, String tagId){
	// Tag t = new Tag(tagId);
	// return contains(readPointName, t);
	// }
	// /**
	// * To get a set of all tags situated on a certain antenna.
	// *
	// * @param readPointName The name of the read point
	// * @return Returns a Set containing a copy of all tags currently on this
	// read point.
	// */
	// public Set getTagsFromReadPoint(String readPointName){
	// Iterator it = ((Set)reader.get(readPointName)).iterator();
	// Set setCopy = new HashSet();
	//			
	// while (it.hasNext()){
	// setCopy.add(it.next());
	// }
	// return setCopy;
	// }

	// -------------------- HAL interface
	// methods------------------------------------

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.rp.impl.hal.HardwareAbstraction#getHalName()
	 */
	public String getHALName() {
		return halName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.rp.impl.hal.HardwareAbstraction#identify(java.lang.String[])
	 */
	public Observation[] identify(String[] readPointNames) throws HardwareException {
		// Each read point gets its own Observation
		Observation[] observations = new Observation[readPointNames.length];
		
//		currentInventory.clear();

		// ==============Start===============
		ArrayList<String> tags;
		// Vector[] tagIds = new Vector[readPointNames.length];
		String[] line;
		// final char SPACE = ' ';
		// ===============End==============

		for (int i = 0; i < readPointNames.length; i++) {

			observations[i] = new Observation();
			observations[i].setHalName(this.halName);
			observations[i].setReadPointName(readPointNames[i]);
			// Get the tag IDs on this antenna
			// Vector<String> v = new Vector<String>();
			Vector<String> tagIds = new Vector<String>();
			// Iterator it = ((HashSet)
			// reader.get(readPointNames[i])).iterator();

			// ==============Start==========
			// tagIds[i] = new Vector();
			tags = read(null, readPoints.get(readPointNames[i]).toString());
			for (String reading : tags) {
				line = reading.split(" ");
				// line[1]: antenna id
				// line[0]: epcid
				tagIds.add(line[0]);
			}
			// ==========End======================

			// while (it.hasNext()) {
			// Tag t = (Tag) it.next();
			// v.add(t.getTagID());
			// }

			int len = tagIds.size();
			String v_arr[] = new String[len];
			v_arr = tagIds.toArray(v_arr);
			TagDescriptor[] td_arr = new TagDescriptor[v_arr.length];
			String idTConf;
			try {
				idTConf = config.getString("idTypesConfig");
			} catch (Exception e) {
				idTConf = null;
			}
			
			
			for (int j = 0; j < td_arr.length; j++) {
				IDType idType = IDType.getIdType("EPC", idTConf);
				byte[] accada_sim_tid_bytes = ByteBlock.hexStringToByteArray("E2656656");
				EPCTransponderModel tagModel = EPCTransponderModel.getEpcTrasponderModel(accada_sim_tid_bytes, epcTransponderModelsConfig);
				MemoryBankDescriptor[] memoryBankDescriptors = new MemoryBankDescriptor[4];
				memoryBankDescriptors[0] = new MemoryBankDescriptor(tagModel.getReservedSize(), tagModel.getReservedReadable(), tagModel.getReservedWriteable());
				memoryBankDescriptors[1] = new MemoryBankDescriptor(tagModel.getEpcSize(), tagModel.getEpcReadable(), tagModel.getEpcWriteable());
				memoryBankDescriptors[2] = new MemoryBankDescriptor(tagModel.getTidSize(), tagModel.getTidReadable(), tagModel.getTidWriteable());
				memoryBankDescriptors[3] = new MemoryBankDescriptor(tagModel.getUserSize(), tagModel.getUserReadable(), tagModel.getUserWriteable());
				MemoryDescriptor memoryDescriptor = new MemoryDescriptor(memoryBankDescriptors);
				td_arr[j] = new TagDescriptor(idType, memoryDescriptor);
			}
			observations[i].setTagDescriptors(td_arr);
			observations[i].setIds(v_arr);
			observations[i].setTimestamp(System.currentTimeMillis());

			if (!readPoints.containsKey(readPointNames[i])) {
				log.error("Read point \"" + readPointNames[i] + "\" is not ready.");
				observations[i].setIds(new String[0]);
				observations[i].setTagDescriptors(new TagDescriptor[0]);
				observations[i].successful = false;
			} else if (continuousIdentifyErrors.contains(readPointNames[i])) {
				observations[i].successful = false;
			} else if (identifyError.contains(readPointNames[i])) {
				observations[i].successful = false;
				identifyError.remove(readPointNames[i]);
			} else {
				observations[i].successful = true;
			}

			log.debug(observations[i].toString());
		}
		return observations;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.rp.impl.hal.HardwareAbstraction#startAsynchronousIdentify(java.lang.String[],
	 *      org.ow2.aspirerfid.reader.rp.impl.hal.AsynchronousIdentifyListener, java.lang.String,
	 *      java.lang.String)
	 */
	public void startAsynchronousIdentify(String[] readPointNames, Trigger trigger) throws HardwareException, UnsupportedOperationException {
		log.debug("This Function Is Not Implemented Yet");
		throw new HardwareException("HAL not ready.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.rp.impl.hal.HardwareAbstraction#stopAsynchronousIdentify(org.ow2.aspirerfid.reader.rp.impl.hal.AsynchronousIdentifyListener)
	 */
	public void stopAsynchronousIdentify() throws HardwareException, UnsupportedOperationException {
		log.debug("This Function Is Not Implemented Yet");
		throw new HardwareException("HAL not ready.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.rp.impl.hal.HardwareAbstraction#isAsynchronousIdentifyRunning(org.ow2.aspirerfid.reader.rp.impl.hal.AsynchronousIdentifyListener)
	 */
	public boolean isAsynchronousIdentifyRunning() throws HardwareException, UnsupportedOperationException {
		log.debug("This Function Is Not Implemented Yet");
		throw new HardwareException("HAL not ready.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.rp.impl.hal.HardwareAbstraction#addAsynchronousIdentifyListener(org.ow2.aspirerfid.reader.rp.impl.hal.AsynchronousIdentifyListener)
	 */
	public void addAsynchronousIdentifyListener(AsynchronousIdentifyListener listener) throws HardwareException, UnsupportedOperationException {
		log.debug("This Function Is Not Implemented Yet");
		throw new HardwareException("HAL not ready.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.rp.impl.hal.HardwareAbstraction#removeAsynchronousIdentifyListener(org.ow2.aspirerfid.reader.rp.impl.hal.AsynchronousIdentifyListener)
	 */
	public void removeAsynchronousIdentifyListener(AsynchronousIdentifyListener listener) throws HardwareException, UnsupportedOperationException {
		log.debug("This Function Is Not Implemented Yet");
		throw new HardwareException("HAL not ready.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.rp.impl.hal.HardwareAbstraction#supportsAsynchronousIdentify()
	 */
	public boolean supportsAsynchronousIdentify() {
		log.debug("This Function Is Not Implemented Yet");
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.rp.impl.hal.HardwareAbstraction#readBytes(java.lang.String,
	 *      java.lang.String, int, int, int, java.lang.String[])
	 */
	public UnsignedByteArray readBytes(String readPointName, String id, int memoryBank, int offset, int length, String[] passwords) throws HardwareException,
			UnsupportedOperationException {
		// =============Start==================
		String read;
		UnsignedByteArray byteData;
		try {
			connect();
			synchronized (out) {
				out.writeBytes("ATTRIBUTE ANTS=" + readPointName + eol);
				out.writeBytes("READ STRING(" + offset + "," + length + ") WHERE TAGID=" + id + eol);
				read = in.readLine();
			}
			byteData = new UnsignedByteArray(read.getBytes());
			return byteData;

		} catch (Exception ex) {
			ex.printStackTrace();
			log.error(ex);
			throw new HardwareException(ex.getMessage());
		}
		// ==============End==================

		//		
		//		
		//		
		// if (readyReadPoints.contains(readPointName)) {
		//
		// if (continuousReadErrors.contains(readPointName)) {
		// throw new HardwareException();
		// } else if (readError.contains(readPointName)) {
		// readError.remove(readPointName);
		// throw new HardwareException();
		// }
		//
		// UnsignedByteArray byteData;
		// Tag tag = null;
		// if (contains(readPointName, id)) {
		// Iterator it = ((HashSet) reader.get(readPointName)).iterator();
		// while (it.hasNext()) {
		// Tag curTag = (Tag) it.next();
		// if (curTag.getTagID().equalsIgnoreCase(id)) {
		// tag = curTag;
		// break;
		// }
		// }
		// }
		//
		// if (tag != null) {
		// byteData = new UnsignedByteArray(tag.readData(memoryBank, offset,
		// length));
		// } else {
		// String message = "Specified tag not in range";
		// throw new HardwareException(message);
		// }
		//
		// return byteData;
		// } else {
		// throw new HardwareException("Read point is not ready.");
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.rp.impl.hal.HardwareAbstraction#writeBytes(java.lang.String,
	 *      java.lang.String, int, int, byte[], java.lang.String[])
	 */
	public void writeBytes(String readPointName, String id, int memoryBank, int offset, UnsignedByteArray data, String[] passwords) throws HardwareException,
			UnsupportedOperationException {

		// =============Start===From The old Code===================
		// String read;
		// String antennas;
		// StringBuilder command;
		//
		// // Build the write command
		// command = new StringBuilder("WRITE ");
		// // The format is WRITE INT(addr,byte), INT(addr,byte), ...
		// // StringBuilder is very fast in concatenations
		// for (int i = 0; i < data.length; i++) {
		// command.append("INT(");
		// command.append(from + i);
		// command.append(",");
		// command.append(data[i]);
		// command.append(")");
		// // if this is the last byte don't add a comma
		// if (i + 1 == data.length)
		// break;
		// command.append(", ");
		// }
		// command.append(" WHERE EPCID=");
		// command.append(id);
		//
		// if (sourceIds != null) {
		// antennas = sourceIds[0];
		// for (int i = 1; i < sourceIds.length; i++) {
		// antennas += "," + sourceIds[i];
		// }
		// } else {
		// antennas = activeAntennasString;
		// }
		//
		// try {
		// connect();
		// synchronized (out) {
		// out.writeBytes("ATTRIBUTE ANTS=" + antennas + eol);
		// out.writeBytes(command + eol);
		// read = in.readLine();
		// }
		//
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// log.error(ex);
		// throw new HardwareException(ex.getMessage());
		// }
		// if (read.contains("WRERR"))
		// throw new HardwareException("Write incomplete");
		// ==============End=====================

		// if (readPoints.containsKey(readPointName)) {
		//
		// if (continuousWriteErrors.contains(readPointName)) {
		// throw new HardwareException();
		// } else if (writeError.contains(readPointName)) {
		// writeError.remove(readPointName);
		// throw new HardwareException();
		// }
		//
		// Tag tag = null;
		// if (contains(readPointName, id)) {
		// Iterator it = ((HashSet) reader.get(readPointName)).iterator();
		// while (it.hasNext()) {
		// Tag curTag = (Tag) it.next();
		// if (curTag.getTagID().equalsIgnoreCase(id)) {
		// tag = curTag;
		// break;
		// }
		// }
		// }
		//
		// if (tag != null) {
		// tag.writeData(memoryBank, offset, data.toByteArray());
		// } else {
		// String message = "Specified tag not in range";
		// throw new HardwareException(message);
		// }
		// } else {
		// throw new HardwareException("Read point is not ready.");
		// }
		log.debug("This Function Is Not Implemented Yet");
		throw new HardwareException("HAL not ready.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.rp.impl.hal.HardwareAbstraction#writeId(java.lang.String,
	 *      java.lang.String, java.lang.String[])
	 */
	public void writeId(String readPointName, String id, String[] passwords) throws ReadPointNotFoundException, HardwareException,
			UnsupportedOperationException {
		log.debug("This Function Is Not Implemented Yet");
		throw new HardwareException("HAL not ready.");
		// TODO: implement
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.rp.impl.hal.HardwareAbstraction#supportsWriteId()
	 */
	public boolean supportsWriteId() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.rp.impl.hal.HardwareAbstraction#getReadPointNames()
	 */
	public String[] getReadPointNames() {
		return readPointNames;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.rp.impl.hal.HardwareAbstraction#setParameter(java.lang.String,
	 *      java.lang.String)
	 */
	public void setParameter(String param, String value) throws HardwareException, UnsupportedOperationException {
		log.debug("This Function Is Not Implemented Yet");
		throw new HardwareException("HAL not ready.");
		// TODO: implement

		// try {
		// String[] paramNames = getAllParameterNames();
		// for (int i = 0; i < paramNames.length; i++) {
		// if (paramNames[i].equalsIgnoreCase(param)) {
		// props.setParameter(param, value);
		// disconnect();
		// initialize();
		// break;
		// }
		// }
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// log.error(ex);
		// throw new HardwareException(ex.getMessage());
		// }

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.rp.impl.hal.HardwareAbstraction#getAllParameterNames()
	 */
	public String[] getAllParameterNames() throws HardwareException, UnsupportedOperationException {
		// try {
		// return this.props.getParameterNames();
		// } catch (Exception e) {
		// throw new HardwareException(e.getMessage());
		// }
		// String[] allParameterNames = null;
		// allParameterNames[0]= "Address:" + address;
		// allParameterNames[1]= "Port:" + port;
		// allParameterNames[2]= "Number Of ReadPoints:" + nOfReadPoints;
		// for(int i = 0; i < readPointNames.length; i++)
		// allParameterNames[3+i]= "readPointName " +(i+1)
		// +":"+readPointNames[i];
		// return allParameterNames;
		try {
			Iterator it = config.getKeys();
			Vector<String> names = new Vector<String>();
			Object item;
			while (it.hasNext()) {
				item = it.next();
				if (String.class.isInstance(item)) {
					names.add((String) item);
				}
			}
			String[] namesarray = new String[names.size()];
			namesarray = names.toArray(namesarray);
			return namesarray;
		} catch (Exception e) {
			log.error("getAllParameterNames: Error gettings parameter names", e);
			throw new HardwareException("Error getting parameter names", e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.rp.impl.hal.HardwareAbstraction#getParameter(java.lang.String)
	 */
	public String getParameter(String param) throws HardwareException, UnsupportedOperationException {
		try {
			return this.config.getString(param);
		} catch (Exception e) {
			e.printStackTrace();
			throw new HardwareException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.rp.impl.hal.HardwareAbstraction#supportsParameters()
	 */
	public boolean supportsParameters() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.rp.impl.hal.HardwareAbstraction#programId(java.lang.String,
	 *      java.lang.String[])
	 */
	public void programId(String id, String[] passwords) throws HardwareException, UnsupportedOperationException {
		log.debug("This Function Is Not Implemented Yet");
		throw new HardwareException("HAL not ready.");
		// TODO: implement
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.rp.impl.hal.HardwareAbstraction#reset()
	 */
	public void reset() throws HardwareException {
		disconnect();
		initialize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.rp.impl.hal.HardwareAbstraction#kill(java.lang.String,
	 *      java.lang.String[])
	 */
	public void kill(String readPointName, String id, String[] passwords) throws HardwareException, UnsupportedOperationException {
		log.debug("This Function Is Not Implemented Yet");
		throw new HardwareException("HAL not ready.");
		// TODO: implement
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.rp.impl.hal.HardwareAbstraction#supportsReadBytes()
	 */
	public boolean supportsReadBytes() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.rp.impl.hal.HardwareAbstraction#supportsWriteBytes()
	 */
	public boolean supportsWriteBytes() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.rp.impl.hal.HardwareAbstraction#supportsProgramId()
	 */
	public boolean supportsProgramId() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.rp.impl.hal.HardwareAbstraction#supportsKill()
	 */
	public boolean supportsKill() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.rp.impl.hal.HardwareAbstraction#supportsReset()
	 */
	public boolean supportsReset() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.rp.impl.hal.HardwareAbstraction#getReadPointPowerLevel(java.lang.String,
	 *      boolean)
	 */
	public int getReadPointPowerLevel(String readPointName, boolean normalize) {
		log.debug("This Function Is Not Implemented Yet");
		// TODO: implement
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.rp.impl.hal.HardwareAbstraction#supportsGetReadPointPowerLevel()
	 */
	public boolean supportsGetReadPointPowerLevel() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.rp.impl.hal.HardwareAbstraction#getReadPointNoiseLevel(java.lang.String,
	 *      boolean)
	 */
	public int getReadPointNoiseLevel(String readPointName, boolean normalize) {
		log.debug("This Function Is Not Implemented Yet");
		// TODO: implement
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.rp.impl.hal.HardwareAbstraction#supportsGetReadPointNoiseLevel()
	 */
	public boolean supportsGetReadPointNoiseLevel() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.rp.impl.hal.HardwareAbstraction#startUpReadPoint(java.lang.String)
	 */
	public void startUpReadPoint(String readPointName) {
		// TODO: implement
		if (!readyReadPoints.contains(readPointName)) {// readPoints.containsKey(readPointNames[i])
			readyReadPoints.add(readPointName);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.rp.impl.hal.HardwareAbstraction#supportsStartUpReadPoint()
	 */
	public boolean supportsStartUpReadPoint() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.rp.impl.hal.HardwareAbstraction#shutDownReadPoint(java.lang.String)
	 */
	public void shutDownReadPoint(String readPointName) {
		// TODO: implement
		if (readyReadPoints.contains(readPointName)) {// readPoints.containsKey(readPointNames[i])
			readyReadPoints.remove(readPointName);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.rp.impl.hal.HardwareAbstraction#supportsShutDownReadPoint()
	 */
	public boolean supportsShutDownReadPoint() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.rp.impl.hal.HardwareAbstraction#isReadPointReady(java.lang.String)
	 */
	public boolean isReadPointReady(String readPointName) {
		return readPoints.containsKey(readPointName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.reader.rp.impl.hal.HardwareAbstraction#supportsIsReadPointReady()
	 */
	public boolean supportsIsReadPointReady() {
		return true;
	}

	/**
	 * For debugging.
	 * 
	 * @param args
	 *            No arguments
	 */
	/*
	public static void main(String[] args) {
		DOMConfigurator.configure("bin/props/log4j.xml"); // DOMConfigurator.configure("./props/log4j.xml");
		IntermecIF5Controller intermecIF5Controller = new IntermecIF5Controller("IntermecIF5Controller", "bin/props/IntermecIF5Controller_default.xml");

		Observation[] observ;
		String[] readPointNames = new String[1];
		readPointNames[0] = "1";
		//readPointNames[1] = "2";
		//readPointNames[1] = "3";
		try {
			observ = intermecIF5Controller.identify(readPointNames);
			//System.out.println("I did it"+observ.toString());
		} catch (Exception e) {
			String message = "" + e.getMessage();
			log.debug(message);
			e.printStackTrace();

		}
	}
	*/
}