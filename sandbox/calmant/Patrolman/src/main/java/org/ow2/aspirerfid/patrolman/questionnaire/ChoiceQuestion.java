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
			boolean[] selected = new boolean[size()];
			int nb_selected = getSelectedFlags(selected);
			int nb_tested = 0;

			// We already have tested if the correct answers list is empty
			if (nb_selected == 0)
				return false;

			for (int i = 0; i < selected.length && nb_tested < nb_selected; i++) {
				if (selected[i]) {
					// Tests if selected box is in the correct answers list
					boolean correct = false;
					for (int j = 0; j < m_correctAnswer.length; j++) {
						if (i == m_correctAnswer[j]) {
							correct = true;
							break;
						}
					}

					// If the selected has not been found, it's incorrect
					if (!correct)
						return false;

					nb_tested++;
				}
			}
			
			// At this point, all possibilities has been tested
			return true;
		}
		
		// We may never reach this point
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.patrolman.Question#toXML()
	 */
	public String toXML() {
		String result = "<choiceList id=\"" + m_id + "\">";

		switch (m_type) {
		case UNIQUE:
			result += "<choice>" + getSelectedIndex() + "</choice>";
			break;

		case MULTIPLE:
			int nb_choices = size();
			boolean[] state = new boolean[nb_choices];
			int nb_checked = getSelectedFlags(state);
			int nb_found = 0;

			if (nb_checked <= 0)
				break;

			for (int i = 0; i < nb_choices; i++) {
				if (state[i]) {
					nb_found++;
					result += "<choice>" + getString(i) + "</choice>";

					if (nb_found >= nb_checked)
						break;
				}
			}
			break;
		}

		result += "</choiceList>";
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.patrolman.questionnaire.Question#clear()
	 */
	public void clear() {
		// On initialization, booleans are false
		setSelectedFlags(new boolean[size()]);
	}
}
