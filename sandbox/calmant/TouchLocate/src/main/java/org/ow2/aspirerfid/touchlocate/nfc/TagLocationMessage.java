/**
 * 
 */
package org.ow2.aspirerfid.touchlocate.nfc;

import org.ow2.aspirerfid.nfc.midlet.generic.RequestMessage;
import org.ow2.aspirerfid.nfc.midlet.reader.TagDetector;

/**
 * @author Thomas Calmant
 * 
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

	/** Latitude */
	private float m_latitude;

	/** Longitude */
	private float m_longitude;

	/** Altitude */
	private float m_altitude;

	/** Bearing */
	private float m_bearing;
	
	public String info = "";

	/**
	 * @param midlet
	 */
	public TagLocationMessage(TagDetector midlet) {
		super(midlet);

		m_latitude = Float.NaN;
		m_longitude = Float.NaN;
		m_altitude = Float.NaN;
		m_bearing = Float.NaN;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.nfc.midlet.generic.RequestMessage#getAllMessage()
	 */
	public String getAllMessage() {
		return "TODO getAllMessage";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.nfc.midlet.generic.Message#getTagUID()
	 */
	public String getTagUID() {
		return m_uid;
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

	/**
	 * Payload format (all are 4-bytes arrays): latitude longitude altitude
	 * bearing
	 * 
	 * @param id
	 *            NDEFRecord ID
	 * @param payload
	 *            NDEFRecord payload
	 * @return True if at least latitude and longitude have been read
	 */
	public boolean setLocation(String id, byte[] payload) {

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

		return m_latitude != Float.NaN && m_longitude != Float.NaN;
	}

	/**
	 * Reads the representation of a float from a big endian 4-byte array
	 * 
	 * @link 
	 *       http://dbaktiar.wordpress.com/2009/06/26/float-and-byte-array-conversion
	 */
	public float convertByteArrayToFloat(byte[] array) {
		/*
		int bits = 0;
		int i = 0;
		for (int shifter = 3; shifter >= 0; shifter--) {
			bits |= ((int) array[i] & 0xff) << ((shifter-i) * 8);
			i++;
		}

		return Float.intBitsToFloat(bits);
		*/
		
		/*
		int bits = 0;
		for(int i=3; i>=0; i--) {
			bits |= (array[i] & 0xff) << (3-i) * 8;
		}
		*/
		
		String bits = "";
		for(int i=0; i < array.length; i++)
			bits += Integer.toString(array[i] & 0xff) + ",";
		
		info += "Data : " + bits + "\n";
		
		// return Float.intBitsToFloat(bits);
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Latitude: " + m_latitude + "\nLongitude : " + m_longitude
				+ "\nAltitude : " + m_altitude + "\nBearing : " + m_bearing
				+ "\nInfo : " + info;
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
}
