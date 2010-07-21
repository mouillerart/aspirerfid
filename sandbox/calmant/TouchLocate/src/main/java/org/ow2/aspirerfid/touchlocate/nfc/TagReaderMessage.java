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
public class TagReaderMessage extends RequestMessage {

	/** Tag UID */
	private String m_uid;

	/** Tag description */
	private String m_description;

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
		// TODO Auto-generated method stub
		return m_description;
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

	public void addRecord(String id, String payload) {
		// TODO
	}
	
	public void setDescription(String descr) {
		m_description = descr;
	}
	
	public String toString() {
		return "UID: " + m_uid + "\n" + m_description;
	}

}
