/**
 * 
 */
package org.ow2.aspirerfid.patrolman.nfc;

import org.ow2.aspirerfid.nfc.midlet.generic.RequestMessage;
import org.ow2.aspirerfid.nfc.midlet.reader.TagDetector;

/**
 * @author Thomas Calmant
 * 
 */
public class TagReaderMessage extends RequestMessage {

	/** Tag UID */
	private String m_uid;

	/**
	 * @param midlet
	 */
	public TagReaderMessage(TagDetector midlet) {
		super(midlet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.nfc.midlet.generic.RequestMessage#getAllMessage()
	 */
	public String getAllMessage() {
		return toString();
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
	
	public String toString() {
		return "UID: " + m_uid;
	}

}
