/**
 * 
 */
package org.ow2.aspirerfid.patrolman.questionnaire;

import javax.microedition.lcdui.ChoiceGroup;

/**
 * @author Thomas Calmant
 * 
 */
public class ChoiceQuestion extends ChoiceGroup implements Question {

	/** Unique choice */
	public static final int UNIQUE = ChoiceGroup.EXCLUSIVE;

	/** Multiple choices */
	public static final int MULTIPLE = ChoiceGroup.MULTIPLE;

	/** Question's id */
	private String m_id;

	/** Correct answer */
	private int[] m_correctAnswer;

	/** Choice type */
	private int m_type;

	public ChoiceQuestion(String id, String label, int type, String[] choices,
			int default_value, int[] correctAnswers) {
		super(label, type);

		m_id = id;
		m_correctAnswer = correctAnswers;
		m_type = type;

		for (int i = 0; i < choices.length; i++)
			append(choices[i], null);

		if (default_value >= 0 && default_value < choices.length) {
			boolean[] checks = new boolean[choices.length];
			checks[default_value] = true;

			setSelectedFlags(checks);
		}
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
		if (m_correctAnswer == null || m_correctAnswer.length == 0)
			return true;

		switch (m_type) {
		case UNIQUE:
			int answer = getSelectedIndex();
			for (int i = 0; i < m_correctAnswer.length; i++) {
				if (answer == m_correctAnswer[i])
					return true;
			}
			return false;

		case MULTIPLE:
			// TODO: not implemented
			break;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.ow2.aspirerfid.patrolman.Question#toXML()
	 */
	public String toXML() {
		String result = "<choiceList id=\"" + m_id + "\">";

		switch(m_type) {
		case UNIQUE:
			result += "<choice>" + getSelectedIndex() + "</choice>";
			break;
			
		case MULTIPLE:
			int nb_choices = size();
			boolean[] state = new boolean[nb_choices];
			int nb_checked = getSelectedFlags(state);
			int nb_found = 0;
			
			if(nb_checked <= 0)
				break;
			
			for(int i=0; i < nb_choices; i++) {
				if(state[i]) {
					nb_found++;
					result += "<choice>" + getString(i) + "</choice>";
					
					if(nb_found >= nb_checked)
						break;
				}
			}
			break;
		}
		
		result += "</choiceList>";
		return result;
	}
}
