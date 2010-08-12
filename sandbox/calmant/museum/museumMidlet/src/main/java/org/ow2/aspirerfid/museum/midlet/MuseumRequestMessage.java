package org.ow2.aspirerfid.museum.midlet;

import org.ow2.aspirerfid.museum.pojo.Visitor;
import org.ow2.aspirerfid.museum.util.PhoneIdXmlTags;
import org.ow2.aspirerfid.museum.util.PhoneMessagesXmlTags;
import org.ow2.aspirerfid.nfc.midlet.generic.DataProfile;
import org.ow2.aspirerfid.nfc.midlet.generic.GenericMidlet;
import org.ow2.aspirerfid.nfc.midlet.generic.RequestMessage;
import org.ow2.aspirerfid.nfc.midlet.reader.TagDetector;
import org.ow2.aspirerfid.nfc.midlet.sendersReceivers.bluetooth.BluetoothException;
import org.ow2.aspirerfid.nfc.midlet.sendersReceivers.bluetooth.BluetoothSenderReceiver;
import org.ow2.aspirerfid.nfc.midlet.sendersReceivers.bluetooth.BluetoothToBusinessInterface;
import org.ow2.aspirerfid.nfc.midlet.sendersReceivers.bluetooth.BusinessToBluetoothCommInterface;

/**
 * This is a message that sends all the information about the user.
 * 
 * @author <a href="mailto:angoca@yahoo.com">Andres Gomez</a>
 */
public class MuseumRequestMessage extends RequestMessage {

    /**
     * Read data from a barcode.
     */
    private String m_barcodeData = null;
    /**
     * Type of the read data from a barcode.
     */
    private String m_barcodeType = null;
    /**
     * Data range of the read tag.
     */
    private String m_tagDataRange = null;
    /**
     * Data value of the read tag.
     */
    private String m_tagDataValue = null;
    /**
     * Type of read tag.
     */
    private String m_tagType = null;
    /**
     * Send privacy data.
     */
    private boolean m_privacy;
    /**
     * user's id.
     */
    private String m_uid = null;

    /**
     * Default constructor with the midlet.
     * 
     * @param midlet
     *                Midlet that calls the message.
     */
    MuseumRequestMessage(TagDetector midlet) {
	super(midlet);
	if (!(midlet instanceof MuseumMidlet)) {
	    throw new RuntimeException(
		    "The midlet does not implement the tag detector interface");
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see core.RequestMessage#getAllMessage(boolean)
     */
    public String getAllMessage() {
	String message = "";
	try {
	    message += "<" + PhoneMessagesXmlTags.XML_MUSEUM_MESSAGE + ">";
	    message += getPhoneIdMessage();
	    message += "</" + PhoneMessagesXmlTags.XML_MUSEUM_MESSAGE + ">";
	} catch (Exception e) {
	    throw new RuntimeException("Error MuseumReqMess1 get all message: "
		    + e.getMessage());
	}

	return message;
    }

    /**
     * @return Returns the message that describes the telephone.
     */
    String getPhoneIdMessage() {
	String message = "";
	try {
	    if (this.m_uid != null) {
		message += "<" + Visitor.XML_VISITOR_ID + ">" + this.m_uid
			+ "</" + Visitor.XML_VISITOR_ID + ">";
	    }
	    message += "<" + PhoneMessagesXmlTags.XML_PHONE_ID + ">";
	    if (!this.m_privacy) {
		message += this.getHandsetTimestamp();
	    }
	    message += this.getMidletSymbolicName() + this.getMidletVersion()
		    + this.getMidletUUID() + this.getTagType() + this.getUID()
		    + this.getLocationSymbolicName()
		    + this.getLocationWGS4Coordinates();
	    if (!this.m_privacy) {
		message += this.getHandsetBluetoothAddress()
			+ this.getHandsetImei() + this.getHandsetDisplay()
			+ this.getSubscriberId() + this.getUserProfile()
			+ this.getUserProfileLanguage();
	    }
	    message += this.getBarcodeType() + this.getBarcodeData()
		    + this.getTagDataRange() + this.getTagDataValue();
	    message += "</" + PhoneMessagesXmlTags.XML_PHONE_ID + ">";
	} catch (Exception e) {
	    throw new RuntimeException("Error MuseumReqMess2 getphoneid: "
		    + e.getMessage());
	}
	return message;
    }

    /**
     * Returns an XML that describes the barcode data.
     * 
     * @return Barcode data tag in XML format.
     */
    private String getBarcodeData() {
	String value = "";
	try {
	    // The value has been previously defined.
	    if (this.m_barcodeData != null) {
		value = "<" + PhoneMessagesXmlTags.XML_BARCODE_DATA + ">"
			+ this.m_barcodeData + "</"
			+ PhoneMessagesXmlTags.XML_BARCODE_DATA + ">";
	    } else {
		// The data is undefined.
		value = "<" + PhoneMessagesXmlTags.XML_BARCODE_DATA + "/>";
	    }
	} catch (Exception e) {
	    throw new RuntimeException("Error MuseumReqMess3 barcode data: "
		    + e.getMessage());
	}
	return value;
    }

    /**
     * Returns an XML that describes the type of the read barcode.
     * 
     * @return Barcode type in XML format.
     */
    private String getBarcodeType() {
	String value = "";
	try {
	    // The barcode type has been defined previously.
	    if (this.m_barcodeType != null) {
		value = "<" + PhoneMessagesXmlTags.XML_BARCODE_TYPE + ">"
			+ this.m_barcodeType + "</"
			+ PhoneMessagesXmlTags.XML_BARCODE_TYPE + ">";
	    } else {
		// The barcode hasn't been defined.
		value = "<" + PhoneMessagesXmlTags.XML_BARCODE_TYPE + "/>";
	    }
	} catch (Exception e) {
	    throw new RuntimeException("Error MuseumReqMess2 barcode type: "
		    + e.getMessage());
	}

	return value;
    }

    /**
     * Returns the bluetooth address of the phone. The message is in XML format.
     * 
     * @return bluetooth address.
     */
    private String getHandsetBluetoothAddress() {
	String value = "";
	try {
	    BluetoothToBusinessInterface bluetooth = BluetoothSenderReceiver
		    .getInstance((BusinessToBluetoothCommInterface) ((MuseumMidlet) this.m_midlet)
			    .getBluetoothController());
	    String address = null;
	    try {
		address = bluetooth.getLocalAddress();
	    } catch (BluetoothException e) {
		// Do nothing.
	    }

	    if (address != null) {
		value = "<" + PhoneIdXmlTags.XML_HANDSET_BLUETOOTH_ADDRESS
			+ ">" + address + "</"
			+ PhoneIdXmlTags.XML_HANDSET_BLUETOOTH_ADDRESS + ">";
	    } else {
		value = "<" + PhoneIdXmlTags.XML_HANDSET_BLUETOOTH_ADDRESS
			+ "/>";
	    }
	} catch (Exception e) {
	    throw new RuntimeException(
		    "Error MuseumReqMess4 bluetooth address: " + e.getMessage());
	}
	return value;
    }

    /**
     * Returns an XML that has the size of the phone's screen.
     * 
     * @return Phone's screen size in XML format.
     */
    private String getHandsetDisplay() {
	String value = "";
	try {
	    String size = DataProfile
		    .getScreenSize((GenericMidlet) this.m_midlet);

	    // The handset screen size could be gotten.
	    if (size != null) {
		value = "<" + PhoneIdXmlTags.XML_HANDSET_DISPLAY + ">" + size
			+ "</" + PhoneIdXmlTags.XML_HANDSET_DISPLAY + ">";
	    } else {
		// The screen's size is unknown.
		value = "<" + PhoneIdXmlTags.XML_HANDSET_DISPLAY + "/>";
	    }
	} catch (Exception e) {
	    throw new RuntimeException("Error MuseumReqMess5 display: "
		    + e.getMessage());
	}
	return value;
    }

    /**
     * Returns an XML that has the bluetooth address of the phone and other
     * properties that describe the phone.
     * 
     * @return Identifier of the phone in XML format. NA means that the address
     *         couldn't be gotten.
     */
    private String getHandsetImei() {
	String value = "";
	try {
	    String imei = DataProfile.getImei();

	    value = "<" + PhoneIdXmlTags.XML_HANDSET_IMEI + ">" + imei + "</"
		    + PhoneIdXmlTags.XML_HANDSET_IMEI + ">";
	} catch (Exception e) {
	    throw new RuntimeException("Error MuseumReqMess6 imei: "
		    + e.getMessage());
	}
	return value;
    }

    /**
     * Returns an XML that has the current date of the phone.
     * 
     * @return Current date in XML format.
     */
    private String getHandsetTimestamp() {
	String value = "";
	try {
	    String timestamp = DataProfile.getTimestamp();

	    value = "<" + PhoneMessagesXmlTags.XML_HANDSET_TIMESTAMP + ">";
	    value += timestamp;
	    value += "</" + PhoneMessagesXmlTags.XML_HANDSET_TIMESTAMP + ">";
	} catch (Exception e) {
	    throw new RuntimeException("Error MuseumReqMess7 timestamp: "
		    + e.getMessage());
	}
	return value;
    }

    /**
     * Returns an XML that has the symbolic name of the current location.
     * 
     * @return the location in XML format.
     */
    private String getLocationSymbolicName() {
	String value = "";
	try {
	    BluetoothToBusinessInterface bluetooth = BluetoothSenderReceiver
		    .getInstance((BusinessToBluetoothCommInterface) ((MuseumMidlet) this.m_midlet)
			    .getBluetoothController());
	    String location = bluetooth.getRemoteName();

	    if (location != null) {
		value = "<" + PhoneMessagesXmlTags.XML_LOCATION_SYMBOLICNAME
			+ ">" + location + "</"
			+ PhoneMessagesXmlTags.XML_LOCATION_SYMBOLICNAME + ">";
	    } else {
		value = "<" + PhoneMessagesXmlTags.XML_LOCATION_SYMBOLICNAME
			+ "/>";
	    }
	} catch (Exception e) {
	    throw new RuntimeException("Error MuseumReqMess8 symbolic name: "
		    + e.getMessage());
	}
	return value;
    }

    /**
     * Returns an XML that has the coordinates of the current location in WGS 84
     * format.
     * 
     * @return The coordinates of the location in XML format.
     */
    private String getLocationWGS4Coordinates() {
	String value = "";
	try {
	    String coordinates = DataProfile.getWGS84Coordinates();

	    if (coordinates != null) {
		value = "<"
			+ PhoneMessagesXmlTags.XML_LOCATION_WGS84COORDINATES
			+ ">" + coordinates + "</"
			+ PhoneMessagesXmlTags.XML_LOCATION_WGS84COORDINATES
			+ ">";
	    } else {
		value = "<"
			+ PhoneMessagesXmlTags.XML_LOCATION_WGS84COORDINATES
			+ "/>";
	    }
	} catch (Exception e) {
	    throw new RuntimeException("Error MuseumReqMess9 wgs4: "
		    + e.getMessage());
	}
	return value;
    }

    /**
     * This method has to returns the midlet caller, this means that the message
     * has to have access to this object. The description is in XML format.
     * 
     * @return Midlet that is being executed.
     */
    private String getMidletSymbolicName() {
	String value = "";
	try {
	    String className = DataProfile
		    .getSymbolicName((GenericMidlet) this.m_midlet);

	    value = "<" + PhoneIdXmlTags.XML_MIDLET_SYMBOLIC_NAME + ">"
		    + className + "</"
		    + PhoneIdXmlTags.XML_MIDLET_SYMBOLIC_NAME + ">";
	} catch (Exception e) {
	    throw new RuntimeException("Error MuseumReqMess10 symbolic name: "
		    + e.getMessage());
	}

	return value;
    }

    /**
     * Returns the unique identifier of the midlet. This number is generated
     * automatically when the midlet is deployed. The description is in XML
     * format.
     * 
     * @return uuid of the midlet.
     */
    private String getMidletUUID() {
	String value = "";
	try {
	    String uuid = DataProfile.getUUID((GenericMidlet) this.m_midlet);

	    if ((uuid != null) && (uuid.compareTo("") != 0)) {
		value = "<" + PhoneIdXmlTags.XML_MIDLET_UUDI + ">" + uuid
			+ "</" + PhoneIdXmlTags.XML_MIDLET_UUDI + ">";
	    } else {
		value = "<" + PhoneIdXmlTags.XML_MIDLET_UUDI + "/>";
	    }
	} catch (Exception e) {
	    throw new RuntimeException("Error MuseumReqMess11 midlet uuid: "
		    + e.getMessage());
	}
	return value;
    }

    /**
     * Returns the version of the midlet. The format is XML.
     * 
     * @return Version of the midlet.
     */
    private String getMidletVersion() {
	String value = "";
	try {
	    String version = DataProfile
		    .getMidletVersion((GenericMidlet) this.m_midlet);

	    if ((version != null) && (version.compareTo("") != 0)) {
		value = "<" + PhoneIdXmlTags.XML_MIDLET_VERSION + ">" + version
			+ "</" + PhoneIdXmlTags.XML_MIDLET_VERSION + ">";
	    } else {
		value = "<" + PhoneIdXmlTags.XML_MIDLET_VERSION + "/>";
	    }
	} catch (Exception e) {
	    throw new RuntimeException("Error MuseumReqMess12 midlet version: "
		    + e.getMessage());
	}

	return value;
    }

    /**
     * Returns the description of the SIM. The format is XML.
     * 
     * @return Description of the SIM card.
     */
    private String getSubscriberId() {
	String value = "";
	try {
	    String sim = DataProfile.getSIMInformation();

	    if (sim != null) {
		value = "<" + PhoneIdXmlTags.XML_SUBSCRIBER_ID + ">" + sim
			+ "</" + PhoneIdXmlTags.XML_SUBSCRIBER_ID + ">";
	    } else {
		value = "<" + PhoneIdXmlTags.XML_SUBSCRIBER_ID + "/>";
	    }
	} catch (Exception e) {
	    throw new RuntimeException("Error MuseumReqMess13 subscriber: "
		    + e.getMessage());
	}

	return value;
    }

    /**
     * Returns the range of data read from the tag. The format ix XML.
     * 
     * @return data range read from the tag.
     */
    private String getTagDataRange() {
	String value = "";
	try {
	    if (this.m_tagDataRange != null) {
		value = "<" + PhoneMessagesXmlTags.XML_TAG_DATA_RANGE + ">"
			+ this.m_tagDataRange + "</"
			+ PhoneMessagesXmlTags.XML_TAG_DATA_RANGE + ">";
	    } else {
		value = "<" + PhoneMessagesXmlTags.XML_TAG_DATA_RANGE + "/>";
	    }
	} catch (Exception e) {
	    throw new RuntimeException("Error MuseumReqMess14 tag data range: "
		    + e.getMessage());
	}

	return value;
    }

    /**
     * Returns the data values read from the tag. The format is XML.
     * 
     * @return Data read from the tag.
     */
    private String getTagDataValue() {
	String value = "";
	try {
	    if (this.m_tagDataValue != null) {
		value = "<" + PhoneMessagesXmlTags.XML_TAG_DATA_VALUE + ">"
			+ this.m_tagDataValue + "</"
			+ PhoneMessagesXmlTags.XML_TAG_DATA_VALUE + ">";
	    } else {
		value = "<" + PhoneMessagesXmlTags.XML_TAG_DATA_VALUE + "/>";
	    }
	} catch (Exception e) {
	    throw new RuntimeException("Error MuseumReqMess15 tag data value: "
		    + e.getMessage());
	}

	return value;
    }

    /**
     * Return the tag's type. The format is XML.
     * 
     * @return Tag's type.
     */
    private String getTagType() {
	String value = "";
	try {
	    if (this.m_tagType != null) {
		value = "<" + PhoneMessagesXmlTags.XML_TAG_TYPE + ">"
			+ this.m_tagType + "</"
			+ PhoneMessagesXmlTags.XML_TAG_TYPE + ">";
	    } else {
		value = "<" + PhoneMessagesXmlTags.XML_TAG_TYPE + "/>";
	    }
	} catch (Exception e) {
	    throw new RuntimeException("Error MuseumReqMess16 tag type: "
		    + e.getMessage());
	}

	return value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see core.Message#getTagUID()
     */
    public String getTagUID() {
	return this.m_tagUID;
    }

    /**
     * Returns the ID of the read tag. The format is XML.
     * 
     * @return Tag's Id.
     */
    private String getUID() {
	String value = "";
	try {
	    if (this.m_tagUID != null) {
		value = "<" + PhoneMessagesXmlTags.XML_TAG_UID + ">"
			+ this.m_tagUID + "</"
			+ PhoneMessagesXmlTags.XML_TAG_UID + ">";
	    } else {
		value = "<" + PhoneMessagesXmlTags.XML_TAG_UID + "/>";
	    }
	} catch (Exception e) {
	    throw new RuntimeException("Error MuseumReqMess17 uid: "
		    + e.getMessage());
	}

	return value;
    }

    /**
     * Profile of the user. The format is XML.
     * 
     * @return User's profile
     */
    private String getUserProfile() {
	String value = "";
	try {
	    String profile = DataProfile
		    .getProfile((GenericMidlet) this.m_midlet);

	    if (profile != null) {
		value = "<" + PhoneIdXmlTags.XML_USER_PROFILE + ">" + profile
			+ "</" + PhoneIdXmlTags.XML_USER_PROFILE + ">";
	    } else {
		value = "<" + PhoneIdXmlTags.XML_USER_PROFILE + "/>";
	    }
	} catch (Exception e) {
	    throw new RuntimeException("Error MuseumReqMess18 user profile: "
		    + e.getMessage());
	}

	return value;

    }

    /**
     * Laguage of the user. The format is XML.
     * 
     * @return User's language.
     */
    private String getUserProfileLanguage() {
	String value = "";
	try {
	    String language = DataProfile
		    .getLanguage((GenericMidlet) this.m_midlet);

	    if (language != null) {
		value = "<" + PhoneIdXmlTags.XML_USER_PROFILE_LANGUAGE + ">"
			+ language + "</"
			+ PhoneIdXmlTags.XML_USER_PROFILE_LANGUAGE + ">";
	    } else {
		value = "<" + PhoneIdXmlTags.XML_USER_PROFILE_LANGUAGE + "/>";
	    }
	} catch (Exception e) {
	    throw new RuntimeException("Error MuseumReqMess19 language: "
		    + e.getMessage());
	}

	return value;

    }

    /**
     * Sets the value of the barcode data.
     * 
     * @param data
     *                barcode data.
     */
    void setBarcodeData(String data) {
	if (data == null) {
	    throw new IllegalArgumentException(
		    "Error #: The data param is null");
	}
	this.m_barcodeData = data;
    }

    /**
     * Sets the value of the barcode type.
     * 
     * @param type
     *                barcode data.
     */
    void setBarcodeType(String type) {
	if (type == null) {
	    throw new IllegalArgumentException(
		    "Error #: The type param is null");
	}
	this.m_barcodeType = type;
    }

    /**
     * Sets the range of read data from the tag.
     * 
     * @param dataRange
     *                the m_tagDataRange to set
     */
    void setTagDataRange(String dataRange) {
	if (dataRange == null) {
	    throw new IllegalArgumentException(
		    "Error #: The dataRange param is null");
	}
	this.m_tagDataRange = dataRange;
    }

    /**
     * Sets the read value from the tag.
     * 
     * @param dataValue
     *                the m_tagDataValue to set
     */
    void setTagDataValue(String dataValue) {
	if (dataValue == null) {
	    throw new IllegalArgumentException(
		    "Error #: The dataValue param is null");
	}
	this.m_tagDataValue = dataValue;
    }

    /**
     * Sets the type of read tag. If the tag has several types, and one of them
     * has previously defined, it adds the new type.
     * 
     * @param type
     *                Type of tag.
     */
    void setTagType(String type) {
	if (type == null) {
	    throw new IllegalArgumentException(
		    "Error #: The type param is null");
	}
	if (this.m_tagType == null) {
	    this.m_tagType += type;
	} else {
	    this.m_tagType += "," + type;
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see core.Message#setTagUID(java.lang.String)
     */
    public void setTagUID(String uid) {
	if (uid == null) {
	    throw new IllegalArgumentException("Error #: The uid param is null");
	}
	this.m_tagUID = uid;
    }

    /**
     * @param privacy
     *                send private information.
     */
    void setPrivacy(boolean privacy) {
	this.m_privacy = privacy;
    }

    /**
     * @param uid
     *                user's id.
     */
    void setUid(String uid) {
	this.m_uid = uid;
    }
}
