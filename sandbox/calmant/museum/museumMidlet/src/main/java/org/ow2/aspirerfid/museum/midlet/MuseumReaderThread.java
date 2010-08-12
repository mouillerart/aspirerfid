package org.ow2.aspirerfid.museum.midlet;

import javax.microedition.contactless.TargetProperties;
import javax.microedition.contactless.TargetType;

import org.ow2.aspirerfid.nfc.midlet.generic.ReaderThread;
import org.ow2.aspirerfid.nfc.midlet.generic.RequestMessage;
import org.ow2.aspirerfid.nfc.midlet.reader.TagDetector;

/**
 * Process the information tag and creates a message with all the information.
 * TODO To read the content is probably necessary to use NDEFMessage interface.
 * 
 * @author Andres Gomez
 */
public class MuseumReaderThread extends ReaderThread {

	/**
	 * Instance the class.
	 * 
	 * @param properties
	 *            Set of properties of the tag.
	 * @param midlet
	 *            Midlet that calls the RFID Detector.
	 */
	public MuseumReaderThread(TargetProperties[] properties, TagDetector midlet) {
		super(properties, midlet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see core.RFIDReaderThread#buildMessage(javax.microedition.contactless.TargetProperties)
	 */
	protected RequestMessage buildMessage(TargetProperties properties) {
		MuseumRequestMessage message = new MuseumRequestMessage(this.m_midlet);

		// UID
		String uid = properties.getUid();
		message.setTagUID(uid);

		if (properties.hasTargetType(TargetType.ISO14443_CARD)) {
			// ISO 14443 Tag type.
			message.setTagType("ISO 14443 Tag");
		}
		if (properties.hasTargetType(TargetType.RFID_TAG)) {
			// RFID Tag type.
			message.setTagType("RFID Tag");
		}
		if (properties.hasTargetType(TargetType.NDEF_TAG)) {
			// NDEF Tag type.
			message.setTagType("NDEF Tag");
		}
		// Not implemented in the Nokia 6131
		if (properties.hasTargetType(TargetType.VISUAL_TAG)) {
			// VISUAL Tag type.
			message.setTagType("VISUAL Tag");
		}
		// TODO Read the content: tag range and value

		return message;
	}
}
