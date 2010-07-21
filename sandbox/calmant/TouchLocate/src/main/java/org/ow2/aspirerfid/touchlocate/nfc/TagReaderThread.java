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

	/**
	 * Instance the class.
	 * 
	 * @param properties
	 *            Set of properties of the tag.
	 * @param midlet
	 *            Midlet that calls the RFID Detector.
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
		String descr = "";

		if (targetProp == null) {
			descr = "No Props...";
			return reader_msg;
		}

		// UID
		reader_msg.setTagUID(targetProp.getUid());

		if (targetProp.hasTargetType(TargetType.NDEF_TAG)) {
			descr = "NDEF TAG\n";
			NDEFTagConnection conn = null;
			try {
				conn = (NDEFTagConnection) Connector.open(targetProp.getUrl());
				NDEFMessage msg = conn.readNDEF();

				if (msg != null) {
					NDEFRecord[] records = msg.getRecords();
					int nb_records = records.length;

					for (int i = 0; i < nb_records; i++) {
						NDEFRecord rec = records[i];
						if (rec == null) {
							descr += "NullRecord: " + i + "\n";
						} else {
							String id = byte2string(rec.getId());
							String payload = byte2string(rec.getPayload());

							descr += "RecordType: "
									+ rec.getRecordType() + "\n";
							descr += "Id: " + id + "\nPayload: "
									+ payload + "\n";
						}
					}
				} else {
					descr += "No msg\n";
				}
			} catch (IOException e) {
				e.printStackTrace();
				descr += "IOEx - " + e.getMessage();
			} catch (ContactlessException e) {
				e.printStackTrace();
				descr += "ClEx - " + e.getMessage();
			} catch (Exception e) {
				descr += "Ex - " + e.getMessage();
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
		
		reader_msg.setDescription(descr);
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
