/*
 *  Copyright (C) Aspire
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.ow2.aspirerfid.patrolman.questionnaire;

import java.util.Vector;

/**
 * Stores data corresponding to a Questionnaire instance (linked with a tag UID)
 * 
 * @author Thomas Calmant
 */
public class QuestionnaireData {
	/** Tag UID */
	private String m_tagUID;

	/** Pattern used for this data */
	private String m_usedPattern;

	/** Questionnaire data */
	private Vector m_values;

	/**
	 * Prepares data storage
	 * 
	 * @param tagUID
	 *            Tag UID which shown the questionnaire
	 * @param usedPattern
	 *            Questionnaire pattern corresponding to the tag UID
	 */
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

	/**
	 * Stores a value
	 * 
	 * @param value
	 *            A {@link Question#getData()} value
	 */
	public void addValue(Object value) {
		m_values.addElement(value);
	}

	/**
	 * Resets the data storage
	 */
	public void clearValues() {
		m_values.removeAllElements();
	}

	/**
	 * @return The data storage content
	 */
	public Vector getValues() {
		return m_values;
	}
}
