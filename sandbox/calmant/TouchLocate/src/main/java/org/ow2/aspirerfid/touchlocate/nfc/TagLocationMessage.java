/*
 *  Copyright (C) Aspire
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.ow2.aspirerfid.touchlocate.nfc;

import org.ow2.aspirerfid.nfc.midlet.generic.RequestMessage;
import org.ow2.aspirerfid.nfc.midlet.reader.TagDetector;

/**
 * Parses and stores location tag data  
 * 
 * @author Thomas Calmant
 */
public class TagLocationMessage extends RequestMessage {
	
	/** Latitude field number in record */
	private static final int LATITUDE_FIELD = 0;
	/** Longitude field number in record */
	private static final int LONGITUDE_FIELD = 1;
	/** Altitude field number in record */
	private static final int ALTITUDE_FIELD = 2;
	/** Bearing field number in record */
	private static final int BEARING_FIELD = 3;

	/** Tag UID */
	private String m_uid;

	/** Record type name */
	private String m_recordTypeName;

	/** Latitude */
	public double m_latitude;

	/** Longitude */
	public double m_longitude;

	/** Altitude */
	private double m_altitude;

	/** Bearing */
	private double m_bearing;

	/** Tag message informations (exceptions during reading...) */
	private String m_informations;

	/**
	 * @param detector
	 *            Parent TagDetector
	 */
	public TagLocationMessage(TagDetector detector) {
		super(detector);

		m_latitude = Double.NaN;
		m_longitude = Double.NaN;
		m_altitude = Double.NaN;
		m_bearing = Double.NaN;

		m_informations = "";
	}

	/**
	 * Appends a line to the record informations (\n automatically added)
	 * 
	 * @param information
	 *            Message to add to record informations
	 */
	public void appendInformation(String information) {
		m_informations += information + "\n";
	}

	/**
	 * Reads the representation of a float from a big endian 4-byte array
	 * 
	 * @link 
	 *       http://dbaktiar.wordpress.com/2009/06/26/float-and-byte-array-conversion
	 */
	public float convertByteArrayToFloat(byte[] array) {
		/*
		 * int bits = 0; int i = 0; for (int shifter = 3; shifter >= 0;
		 * shifter--) { bits |= ((int) array[i] & 0xff) << ((shifter-i) * 8);
		 * i++; }
		 * 
		 * return Float.intBitsToFloat(bits);
		 */

		int bits = 0;
		for (int i = 3; i >= 0; i--) {
			bits |= (array[i] & 0xff) << (3 - i) * 8;
		}
		return Float.intBitsToFloat(bits);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.nfc.midlet.generic.RequestMessage#getAllMessage()
	 */
	public String getAllMessage() {
		return "TODO getAllMessage";
	}

	/**
	 * @return Tag data : Altitude
	 */
	public double getAltitude() {
		return m_altitude;
	}

	/**
	 * @return Tag data : Bearing
	 */
	public double getBearing() {
		return m_bearing;
	}

	/**
	 * @return the tag record informations
	 */
	public String getInformations() {
		return m_informations;
	}

	/**
	 * @return Tag data : latitude
	 */
	public double getLatitude() {
		return m_latitude;
	}

	/**
	 * @return Tag data : longitude
	 */
	public double getLongitude() {
		return m_longitude;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.nfc.midlet.generic.Message#getTagUID()
	 */
	public String getTagUID() {
		return m_uid;
	}

	/**
	 * @return
	 */
	public boolean isValid() {
		return m_latitude != Double.NaN && m_longitude != Double.NaN;
	}

	/**
	 * Adds the hexadecimal version of tag data to the informations
	 * 
	 * @param payload
	 */
	private void logTagData(byte[] payload) {
		if (payload == null || payload.length == 0) {
			m_informations += "No payload";
			return;
		}

		StringBuffer bits = new StringBuffer();
		for (int i = 0; i < payload.length - 1; i++)
			bits.append(Integer.toString(payload[i] & 0xff, 16)).append(',');

		bits.append(Integer.toString(payload[payload.length - 1] & 0xff));

		m_informations += "Tag Data :\n" + bits + "\n";
	}

	/**
	 * Parses a Location record
	 * 
	 * @param payload
	 *            Record data
	 * @return True if at least a latitude and a longitude have been found
	 * 
	 *         <p>
	 *         Payload format (all are 4-bytes arrays): latitude longitude
	 *         altitude bearing
	 *         </p>
	 */
	private boolean parseRTLocation(byte[] payload) {
		byte[] temp = new byte[4];
		int current_field = 0;

		for (int i = 0; i < payload.length; i += 4) {

			for (int j = 0; j < temp.length && (i + j) < payload.length; j++) {
				temp[j] = payload[i + j];
			}

			float value = convertByteArrayToFloat(temp);

			switch (current_field) {
			case LATITUDE_FIELD:
				m_latitude = value;
				break;

			case LONGITUDE_FIELD:
				m_longitude = value;
				break;

			case ALTITUDE_FIELD:
				m_altitude = value;
				break;

			case BEARING_FIELD:
				m_bearing = value;
				break;

			default:
				break;
			}

			current_field++;
		}

		return m_latitude != Double.NaN && m_longitude != Double.NaN;
	}

	/**
	 * Parses a URI location record
	 * 
	 * @param payload
	 *            Record data
	 * @return True if at least a latitude and a longitude have been found
	 * 
	 * @see http://www.maintag.fr/fichiers/pdf-fr/nfcforum-ts-rtd-uri-1-0.pdf
	 * 
	 *      <p>
	 *      URI location record format : loc/<i>latitude</i>/<i>longitude</i>
	 *      </br> Example : loc/45.187778/5.726945
	 *      </p>
	 */
	private boolean parseRTUri(byte[] payload) {
		StringBuffer uriBuffer = new StringBuffer(payload.length - 1);

		for (int i = 1; i < payload.length; i++) {
			/*
			 * Worst behavior I've ever seen on a Nokia : A direct conversion
			 * from byte to char throws a NullPointerException An arithmetic
			 * conversion (0 + byte value) is OK.
			 */
			char character = 0;
			character += payload[i];
			uriBuffer.append(character);
		}

		String uri = uriBuffer.toString();
		if (!uri.startsWith("loc/"))
			return false;

		m_informations += "URI : " + uri + "\n";

		int index;
		int old_index = uri.indexOf('/') + 1;
		int current_field = 0;
		String valueStr = "";

		try {
			while ((index = uri.indexOf('/', old_index)) != -1) {
				valueStr = uri.substring(old_index, index);
				old_index = index + 1;

				saveFieldValue(current_field, Double.parseDouble(valueStr));
				current_field++;
			}

			// Last field, if needed
			if (old_index < uri.length()) {
				valueStr = uri.substring(old_index, uri.length());
				saveFieldValue(current_field, Double.parseDouble(valueStr));
			}

		} catch (NumberFormatException ex) {
			// Just stop the loop...
			appendInformation("Numberformat exception : " + valueStr);
		}

		return m_latitude != Double.NaN && m_longitude != Double.NaN;
	}

	/**
	 * Saves value in the corresponding field
	 * 
	 * @param field
	 *            Field identifier ({@link #LATITUDE_FIELD},
	 *            {@link #LONGITUDE_FIELD}, {@link #ALTITUDE_FIELD},
	 *            {@link #BEARING_FIELD})
	 * @param value
	 *            Value to be saved
	 */
	private void saveFieldValue(int field, double value) {
		switch (field) {
		case LATITUDE_FIELD:
			m_latitude = value;
			break;

		case LONGITUDE_FIELD:
			m_longitude = value;
			break;

		case ALTITUDE_FIELD:
			m_altitude = value;
			break;

		case BEARING_FIELD:
			m_bearing = value;
			break;

		default:
			break;
		}
	}

	/**
	 * 
	 * 
	 * @param recordTypeName
	 *            NDEFRecord Record Type Name (U, L)
	 * @param payload
	 *            NDEFRecord payload
	 * 
	 * @return True if at least latitude and longitude have been read
	 */
	public boolean setLocation(String recordTypeName, byte[] payload) {
		boolean valid = false;

		// RAW Location record
		if (recordTypeName.equals("L")) {
			valid = parseRTLocation(payload);
		}
		// URI encoded location record
		else if (recordTypeName.equals("U")) {
			valid = parseRTUri(payload);
		}

		// Add informations about the validated tag
		if (valid) {
			m_recordTypeName = recordTypeName;
			logTagData(payload);
		}

		return valid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.aspirerfid.nfc.midlet.generic.Message#setTagUID(java.lang.String)
	 */
	public void setTagUID(String uid) {
		m_uid = uid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Tag UID: " + m_uid + "\nRecord Type: " + m_recordTypeName
				+ "\nLatitude: " + m_latitude + "\nLongitude : " + m_longitude
				+ "\nAltitude : " + m_altitude + "\nBearing : " + m_bearing
				+ "\nDebug : " + m_informations;
	}
}
