package org.ow2.aspirerfid.museum.util;

/**
 * This is an interface that has all the XML tag to send messages about a
 * detected tag and to identify the phone.
 * 
 * @author <a href="mailto:angoca@yahoo.com">Andres Gomez</a>
 */
public final class PhoneIdXmlTags {
	/**
	 * Default constructor.
	 */
	private PhoneIdXmlTags() {
		// Nothing
	}

	/**
	 * Bluetooth address.
	 */
	public static final String XML_HANDSET_BLUETOOTH_ADDRESS = "handset.bluetoothaddress";
	/**
	 * This is the size of the screen (resolution).
	 */
	public static final String XML_HANDSET_DISPLAY = "handset.display";
	/**
	 * This is the identifier of the phone.
	 */
	public static final String XML_HANDSET_IMEI = "handset.imei";
	/**
	 * This is the name of the caller midlet (Class name).
	 */
	public static final String XML_MIDLET_SYMBOLIC_NAME = "midlet.symbolicname";
	/**
	 * This is the uuid of the jad, that was generated at deployment time.
	 */
	public static final String XML_MIDLET_UUDI = "midlet.uuid";
	/**
	 * This is the version of the jad.
	 */
	public static final String XML_MIDLET_VERSION = "midlet.version";
	/**
	 * This is the information that is in the SIM.
	 */
	public static final String XML_SUBSCRIBER_ID = "subscriber.id";
	/**
	 * Composite Capability/Preference Profiles (CC/PP).
	 */
	public static final String XML_USER_PROFILE = "user.profile";
	/**
	 * The MIDP system property "microedition.locale" tells the current language
	 * used in the device.
	 */
	public static final String XML_USER_PROFILE_LANGUAGE = "user.profile.languages";
}
