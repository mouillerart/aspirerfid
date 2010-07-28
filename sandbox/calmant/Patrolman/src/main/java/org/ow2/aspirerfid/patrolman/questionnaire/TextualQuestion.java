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

import javax.microedition.lcdui.TextField;

/**
 * Textual question to be added to a {@link Questionnaire} Has a fixed answer
 * length limit : {@link #MAX_LENGHT} : {@value #MAX_LENGHT}
 * 
 * @author Thomas Calmant
 */
public class TextualQuestion extends TextField implements Question {
	/** Max answer length */
	public static final int MAX_LENGHT = 256;

	/** Question's id */
	private String m_id;

	/** Default value */
	private String m_defaultValue;

	/** Correct answer */
	private String m_correctAnswer;

	/**
	 * Creates a textual question
	 * 
	 * @param id
	 *            Question ID
	 * @param label
	 *            Question label
	 * @param text
	 *            Default answer (can be null)
	 * @param correctAnswer
	 *            Correct value (can be null)
	 */
	public TextualQuestion(String id, String label, String text,
			String correctAnswer) {
		super(label, text, MAX_LENGHT, TextField.ANY);
		m_id = id;
		m_defaultValue = text;
		m_correctAnswer = correctAnswer;

		if (m_defaultValue == null)
			m_defaultValue = "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.patrolman.questionnaire.Question#clear()
	 */
	public void clear() {
		setString(m_defaultValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.patrolman.questionnaire.Question#getData()
	 */
	public Object getData() {
		return getString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.patrolman.Question#getId()
	 */
	public String getId() {
		return m_id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.patrolman.Question#isCorrect()
	 */
	public boolean isCorrect() {
		if (m_correctAnswer != null && m_correctAnswer.length() != 0)
			return getString().equals(m_correctAnswer);

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.aspirerfid.patrolman.questionnaire.Question#setData(java.lang
	 * .Object)
	 */
	public void setData(Object value) {
		setString(value.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.patrolman.Question#toXML()
	 */
	public String toXML() {
		return toXML(getString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.aspirerfid.patrolman.questionnaire.Question#toXML(java.lang.Object
	 * )
	 */
	public String toXML(Object data) {
		StringBuffer result = new StringBuffer();
		result.append("<textual id=\"").append(m_id).append("\">\n<answer>");
		result.append(getString()).append("</answer>\n</textual>\n");

		return result.toString();
	}
}
