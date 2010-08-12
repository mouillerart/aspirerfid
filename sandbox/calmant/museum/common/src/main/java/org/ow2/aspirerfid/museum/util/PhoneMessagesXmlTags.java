/**
 * 
 */
package org.ow2.aspirerfid.museum.util;

/**
 * These are the types of messages.
 * 
 * @author <a href="mailto:angoca@yahoo.com">Andres Gomez</a>
 */
public final class PhoneMessagesXmlTags {
	/**
	 * Default constructor.
	 */
	private PhoneMessagesXmlTags() {
		// Nothing
	}

	/**
	 * This is the XML tag for all types of messages from the telephone.
	 */
	public static final String XML_MUSEUM_MESSAGE = "museum";
	/**
	 * The main XML tag of a phone id.
	 */
	public static final String XML_PHONE_ID = "phoneid";
	/**
	 * This is the data contained in the bar code.
	 */
	public static final String XML_BARCODE_DATA = "barcode.data";
	/**
	 * This is the type of bar code.
	 */
	public static final String XML_BARCODE_TYPE = "barcode.type";
	/**
	 * This is the timestamp of the phone.
	 */
	public static final String XML_HANDSET_TIMESTAMP = "handset.timestamp";
	/**
	 * This is the name of the place where the tag was read.
	 */
	public static final String XML_LOCATION_SYMBOLICNAME = "location.symbolicname";
	/**
	 * These are the coordinates of the position of the read tag (Location API).
	 */
	public static final String XML_LOCATION_WGS84COORDINATES = "location.wgs84coordinates";
	/**
	 * This is the range of read octects from the tag.
	 */
	public static final String XML_TAG_DATA_RANGE = "tag.data.range";
	/**
	 * This is the value found in the read octects from the tag.
	 */
	public static final String XML_TAG_DATA_VALUE = "tag.data.value";
	/**
	 * This is the type of read tag.
	 */
	public static final String XML_TAG_TYPE = "tag.type";
	/**
	 * This is the uid of the tag.
	 */
	public static final String XML_TAG_UID = "tag.uid";
}
