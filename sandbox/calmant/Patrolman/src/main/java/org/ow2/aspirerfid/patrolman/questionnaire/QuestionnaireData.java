/**
 * 
 */
package org.ow2.aspirerfid.patrolman.questionnaire;

import java.util.Vector;

/**
 * @author Thomas Calmant
 */
public class QuestionnaireData {
	/** Tag UID */
	private String m_tagUID;
	
	/** Pattern used for this data */
	private String m_usedPattern;
	
	/** Questionnaire data */
	private Vector m_values;
	
	public QuestionnaireData(String tagUID, String usedPattern) {
		m_values = new Vector();
		m_tagUID = tagUID;
		m_usedPattern = usedPattern;
	}
	
	/**
	 * @return the UID of the tag associated to the data
	 */
	public String getTagUID() {
		return m_tagUID;
	}
	
	/**
	 * @return the pattern corresponding the tag UID
	 */
	public String getUsedPattern() {
		return m_usedPattern;
	}
	
	public void addValue(Object value) {
		m_values.addElement(value);
	}
	
	public void clearValues() {
		m_values.removeAllElements();
	}
	
	public Vector getValues() {
		return m_values;
	}
}
