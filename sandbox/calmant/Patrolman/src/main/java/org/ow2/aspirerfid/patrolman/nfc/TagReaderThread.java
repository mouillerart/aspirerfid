/**
 * 
 */
package org.ow2.aspirerfid.patrolman.nfc;

import javax.microedition.contactless.TargetProperties;

import org.ow2.aspirerfid.nfc.midlet.generic.ReaderThread;
import org.ow2.aspirerfid.nfc.midlet.generic.RequestMessage;
import org.ow2.aspirerfid.nfc.midlet.reader.TagDetector;

/**
 * Process the information tag and creates a message with all the information.
 * TODO To read the content is probably necessary to use NDEFMessage interface.
 * 
 * @author Andres Gomez
 * @author Thomas Calmant
 */
public class TagReaderThread extends ReaderThread {

	/**
	 * Instance the class.
	 * 
	 * @param properties
	 *            Set of properties of the tag.
	 * @param midlet
	 *            MIDlet that calls the RFID Detector.
	 */
	public TagReaderThread(TargetProperties[] properties, TagDetector midlet) {
		super(properties, midlet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.aspirerfid.nfc.midlet.generic.ReaderThread#buildMessage(javax
	 * .microedition.contactless.TargetProperties)
	 */
	protected RequestMessage buildMessage(TargetProperties targetProp) {
		TagReaderMessage reader_msg = new TagReaderMessage(m_midlet);

		if (targetProp == null)
			return reader_msg;

		// UID
		reader_msg.setTagUID(targetProp.getUid());
		return reader_msg;
	}
}
