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

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.osgi.service.event.EventConstants;
import org.osgi.util.measurement.Unit;
import org.ow2.aspirerfid.util.RFIDConstants;
import org.ow2.aspirerfid.wires.TemperatureRead;

/**
 * Holds the logic for adapting HTTP GET parameters to RFID Suite properties
 * 
 * @author kiev
 * 
 */
public class RfidPropertyAdapter {
	private static final String ID = "id";
	private static final String TIMESTAMP = "timestamp";
	private static final String READER_ID = "readerid";
	private static final String LATITUDE = "latitude";
	private static final String LONGITUDE = "longitude";
	static final String[] MANDATORY_FIELDS = new String[] { ID, TIMESTAMP,
			READER_ID };
	private boolean valid;
	private HashMap<String, MeasurementAdapter> measurements = new HashMap<String, MeasurementAdapter>();
	private TagAdapter tagAdapter = new TagAdapter();

	private Pattern pattern = Pattern.compile("(\\w+)(\\.("
			+ MeasurementAdapter.ERROR + "|" + MeasurementAdapter.UNIT + "))?");
	// private RFIDTagRead tagProp;

	// Added to use event instead of RFIDTagRead WireAdmin
	private String id, timestamp, readerId, gpsCoordinates;

	public RfidPropertyAdapter(Map values) {
		if (validate(values)) {
			setTagInfo(values);
		}
	}

	private boolean validate(Map valuesToAdapt) {
		this.valid = true;
		for (String mandatoryField : RfidPropertyAdapter.MANDATORY_FIELDS) {
			String param = (String) valuesToAdapt.get(mandatoryField);
			if (param == null || param.trim().equals("")) {
				this.valid = false;
				break;
			}
		}
		return valid;
	}

	boolean isValid() {
		return valid;
	}

	/**
	 * @return a dictionary containing a tagId, a timestamp and a readerId
	 */
	public Dictionary getTagInfo() {
		Properties tagInfo = new Properties();
		tagInfo.put(RFIDConstants.TAGGUID_KEY, tagAdapter.adaptIDToEPC(id));
		tagInfo.put(EventConstants.TIMESTAMP, timestamp);
		tagInfo.put(RFIDConstants.READERGUID_KEY, readerId);
		if (gpsCoordinates != null) {
			tagInfo.put(RFIDConstants.COORDINATES_KEY, gpsCoordinates);
		}

		if (measurements.size() > 0) {
			// Temperature read is semantically incorrect...
			ArrayList<TemperatureRead> list = new ArrayList<TemperatureRead>();
			for (MeasurementAdapter measure : measurements.values()) {
				TemperatureRead tempMeasurement = new TemperatureRead(measure.getValue(),
						measure.getUnit(), 0, measure.getMeasurementName(), "httpreader",Long.parseLong(timestamp));
				list.add(tempMeasurement);
			}
			tagInfo.put(RFIDConstants.MEASUREMENT_KEY, list);
			
		}

		return tagInfo;
	}

	/**
	 * @param values,
	 *            a tagID, a timestamp and the readerID
	 */
	public void setTagInfo(Map values) {
		HashMap map = new HashMap(values);
		String latitude = (String) map.remove(LATITUDE);
		String longitude = (String) map.remove(LONGITUDE);
		;
		this.id = (String) map.remove(ID);
		this.timestamp = (String) map.remove(TIMESTAMP);
		this.readerId = (String) map.remove(READER_ID);

		if (latitude != null && longitude != null) {
			this.gpsCoordinates = latitude + "," + longitude;
		}

		for (Object key : map.keySet()) {
			String param = key.toString();
			processMeasurement(param, (String) map.get(param));
		}
	}

	/**
	 * Considering the rule that a measurement must be provided like:
	 * mymeasurement=123&mymeasurement.unit=K&mymeasurement.error=1 <br>
	 * This method would extract only the "mymeasurement" key.
	 * 
	 * @param s
	 * @return
	 */
	private void processMeasurement(String key, String value) {
		Matcher matcher = pattern.matcher(key);
		String property, subproperty;
		MeasurementAdapter adapter = new MeasurementAdapter(key);
		// must be a string optionally ending with .error or .unit
		if (!matcher.matches()) {
			throw new RuntimeException("Unaccepted key: " + key);
		}

		property = matcher.group(1);
		subproperty = matcher.group(3);
		adapter = this.getFromMap(property);

		if (subproperty == null) {
			adapter.setValue(Double.parseDouble(value));
		} else if (MeasurementAdapter.ERROR.equals(subproperty)) {
			adapter.setError(Double.parseDouble(value));
		} else if (MeasurementAdapter.UNIT.equals(subproperty)) {
			Unit unit = UnitParser.fromString(value);
			if (unit == null) {
				throw new RuntimeException("Unknown unit " + value
						+ " on entry " + key);
			}
			adapter.setUnit(unit);
		}
	}

	// Retrieves measurement from map. Creates one map entry if it does not
	// exist.
	private MeasurementAdapter getFromMap(String measurementName) {
		MeasurementAdapter adapter = this.measurements.get(measurementName);
		if (adapter == null) {
			adapter = new MeasurementAdapter(measurementName);
			this.measurements.put(measurementName, adapter);
		}
		return adapter;
	}
}

// Temp solution for adapting non EPC tag ids (e.g. NFC) to a format compliant
// with rfidsuite's tag factory
// Current workaround forces a GID prefix
// FIXME Migrate this to a TDT compliant solution (if any for NFC tags...)
class TagAdapter {
	private static final int TAG_LENGTH = 24;
	private static final String GID_TAG_PREFIX = "35";

	public String adaptIDToEPC(String tagID) {
		StringBuffer sb = new StringBuffer(tagID);
		sb.insert(0, GID_TAG_PREFIX);
		while (sb.length() < TAG_LENGTH) {
			sb.insert(2, "0");
		}
		return sb.toString();
	}
}