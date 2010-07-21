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
	
	/** Default value */
	private String m_defaultValue;

	/** Correct answer */
	private String m_correctAnswer;

	public TextualQuestion(String id, String label, String text,
			String correctAnswer) {
		super(label, text, MAX_LENGHT, TextField.ANY);
		m_id = id;
		m_defaultValue = text;
		m_correctAnswer = correctAnswer;
		
		if(m_defaultValue == null)
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

	/* (non-Javadoc)
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

	/* (non-Javadoc)
	 * @see org.ow2.aspirerfid.patrolman.questionnaire.Question#setData(java.lang.Object)
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

	/* (non-Javadoc)
	 * @see org.ow2.aspirerfid.patrolman.questionnaire.Question#toXML(java.lang.Object)
	 */
	public String toXML(Object data) {
		StringBuffer result = new StringBuffer();
		result.append("<textual id=\"").append(m_id).append("\">\n<answer>");
		result.append(getString()).append("</answer>\n</textual>\n");
		
		return result.toString();
	}
}
