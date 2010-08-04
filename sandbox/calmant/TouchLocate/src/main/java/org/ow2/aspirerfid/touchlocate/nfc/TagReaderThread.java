/**
 * 
 */
package org.ow2.aspirerfid.touchlocate.nfc;

import java.io.IOException;

import javax.microedition.contactless.ContactlessException;
import javax.microedition.contactless.TargetProperties;
import javax.microedition.contactless.TargetType;
import javax.microedition.contactless.ndef.NDEFMessage;
import javax.microedition.contactless.ndef.NDEFRecord;
import javax.microedition.contactless.ndef.NDEFTagConnection;
import javax.microedition.io.Connector;

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
	/** Location record type */
	public static final String LOCATION_TYPE = "urn:nfc:wkt:L";

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
	 * 
	 * TODO: remove/improve debug informations
	 */
	protected RequestMessage buildMessage(TargetProperties targetProp) {
		TagLocationMessage reader_msg = new TagLocationMessage(m_midlet);

		if (targetProp == null) {
			return null;
		}

		// UID
		reader_msg.setTagUID(targetProp.getUid());

		if (targetProp.hasTargetType(TargetType.NDEF_TAG)) {
			NDEFTagConnection conn = null;
			try {
				conn = (NDEFTagConnection) Connector.open(targetProp.getUrl());
				NDEFMessage msg = conn.readNDEF();

				if (msg != null) {
					NDEFRecord[] records = msg.getRecords();
					int nb_records = records.length;

					for (int i = 0; i < nb_records; i++) {
						NDEFRecord rec = records[i];
						
						if(rec != null) { // && rec.getRecordType().getName().equals(LOCATION_TYPE)) {
							// reader_msg.info += rec.getRecordType().toString() + " - " + rec.getRecordType().getName() + "\n";
							String id = byte2string(rec.getId());
							if(reader_msg.setLocation(id, rec.getPayload())) {
								return reader_msg;
							}
						}
					}
				}
				else
					reader_msg.info += "No message\n";
			} catch (IOException e) {
				reader_msg.info += "IOEx - " + e.getMessage() + "\n";
			} catch (ContactlessException e) {
				reader_msg.info += "ClEx - " + e.getMessage() + "\n";
			} catch (Exception e) {
				reader_msg.info += "Ex - " + e.getMessage() + "\n";
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (IOException e) {
						// do nothing
					}
				}
			}
		}
		
		return reader_msg;
	}

	private String byte2string(byte[] bytes) {
		String ret = "";
		if (bytes == null)
			return "null";

		for (int i = 0; i < bytes.length; i++) {
			/*
			 * Worst behavior I've ever seen on a Nokia : A direct conversion
			 * from byte to char throws a NullPointerException An arithmetic
			 * conversion (0 + byte value) is OK.
			 */
			char conv = 0;
			byte c = bytes[i];

			conv += c;
			ret += conv;
		}

		return ret;
	}
}
