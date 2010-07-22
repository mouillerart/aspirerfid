/**
 * 
 */
package org.ow2.aspirerfid.patrolman.nfc;

import java.util.Enumeration;
import java.util.Vector;

import org.ow2.aspirerfid.nfc.midlet.generic.RequestMessage;
import org.ow2.aspirerfid.nfc.midlet.reader.TagDetector;

/**
 * @author Thomas Calmant
 * 
 */
public class TagReaderMessage extends RequestMessage {

	/** Exception thrown during reading */
	private Exception m_exception;

	/** Tag records types */
	private Vector m_recordsTypes;
	
	/** Tag UID */
	private String m_uid;

	/**
	 * @param midlet
	 */
	public TagReaderMessage(TagDetector midlet) {
		super(midlet);
		m_recordsTypes = new Vector();
	}

	/**
	 * Adds a tag record type
	 * 
	 * @param recordType
	 *            Tag record type
	 */
	public void addRecordType(String recordType) {
		m_recordsTypes.addElement(recordType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.nfc.midlet.generic.RequestMessage#getAllMessage()
	 */
	public String getAllMessage() {
		return toString();
	}

	/**
	 * @return The tag records types, or null if unknown
	 */
	public Vector getRecordsType() {
		return m_recordsTypes;
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
	 * @return The exception thrown during reading process, or null
	 */
	public Exception getThrownException() {
		return m_exception;
	}

	/**
	 * Tests if an exception has been raised during reading process
	 * @return True if an exception occurred, else False
	 */
	public boolean raisedException() {
		return m_exception != null;
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
	 * Stores the exception thrown during the reading process 
	 * @param exception Exception thrown during the reading process
	 */
	public void setThrownException(Exception exception) {
		m_exception = exception;
	}
	
	/**
	 * Returns a simple description of the message
	 */
	public String toString() {
		String description = "UID: " + m_uid + "\nRecords Types:\n";

		Enumeration types = m_recordsTypes.elements();
		while (types.hasMoreElements()) {
			description += '\t' + types.nextElement().toString() + '\n';
		}

		return description;
	}

}
