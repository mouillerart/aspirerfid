package org.ow2.aspirerfid.patrolman.questionnaire;

import javax.microedition.lcdui.TextField;

/**
 * Textual question form item
 * 
 * @author Thomas Calmant
 */
public class TextualQuestion extends TextField implements Question {
	/** Max answer length */
	public static final int MAX_LENGHT = 256;

	/** Question's id */
	private String m_id;

	/** Correct answer */
	private String m_correctAnswer;

	public TextualQuestion(String id, String label, String text,
			String correctAnswer) {
		super(label, text, MAX_LENGHT, TextField.ANY);
		m_id = id;
		m_correctAnswer = correctAnswer;
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
	 * @see org.ow2.aspirerfid.patrolman.Question#toXML()
	 */
	public String toXML() {
		String result = "<textual id=\"" + m_id + "\">";
		result += "<answer>" + getString() + "</answer>";
		result += "</textual>";
		return result;
	}
}
