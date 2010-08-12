package org.ow2.aspirerfid.museum.pojo.question;

import java.util.Vector;

import org.ow2.aspirerfid.museum.pojo.InvalidParameterException;
import org.ow2.aspirerfid.museum.pojo.Visitor;

/**
 * This the filled questionnaire by a visitor.
 * 
 * @author <a href="mailto:angoca@yahoo.com">Andres Gomez</a>
 */
public class VisitorsAnswers {

	/**
	 * XML tag: answers.
	 */
	public static final String XML_ANSWERS = "answers";
	/**
	 * The main XML tag of a survey response.
	 */
	public static final String XML_SURVEY_RESPONSE = "survey.response";
	/**
	 * The main XML tag of a rating response.
	 */
	public static final String XML_RATING_RESPONSE = "rating.response";
	/**
	 * The main XML tag of a profile response.
	 */
	public static final String XML_PROFILE_RESPONSE = "profile.response";
	/**
	 * Set of answer of the filled questionnaire.
	 */
	private Vector m_answers;
	/**
	 * Filled questionnaire.
	 */
	private Questionnaire m_questionnaire;
	/**
	 * Visitor that fills the questionnaire.
	 */
	private Visitor m_visitor;

	/**
	 * Default constructor that initializes the list of answers.
	 * 
	 * @param questionnaire
	 *            Filled questionnaire.
	 * @param visitor
	 *            Visitor that fills the questionnaire.
	 * @throws InvalidParameterException
	 *             if some parameter is null.
	 */
	public VisitorsAnswers(final Questionnaire questionnaire,
			final Visitor visitor) throws InvalidParameterException {
		if (questionnaire == null || visitor == null) {
			throw new InvalidParameterException();
		}
		this.m_questionnaire = questionnaire;
		this.m_visitor = visitor;
		this.m_answers = new Vector();
	}

	/**
	 * Adds an answer to the set of answers.
	 * 
	 * @param answer
	 *            Answer to add.
	 * @throws InvalidParameterException
	 *             if the answer is null.
	 */
	public final void addAnswer(final Answer answer)
			throws InvalidParameterException {
		if (answer == null) {
			throw new InvalidParameterException();
		}
		this.m_answers.addElement(answer);
	}

	/**
	 * @return Set of answer of the filled questionnaire.
	 */
	public final Vector getAnswers() {
		return this.m_answers;
	}

	/**
	 * @return Filled questionnaire.
	 */
	public final Questionnaire getQuestionnaire() {
		return this.m_questionnaire;
	}

	/**
	 * @return Visitor that fills the questionnaire.
	 */
	public final Visitor getVisitor() {
		return this.m_visitor;
	}

	/**
	 * @param answers
	 *            Set of answers of the filled questionnaire.
	 * @throws InvalidParameterException
	 *             if the answer is null.
	 */
	public final void setAnswers(final Vector answers)
			throws InvalidParameterException {
		if (answers == null) {
			throw new InvalidParameterException();
		}
		this.m_answers = answers;
	}

	/**
	 * @param questionnaire
	 *            Filled questionnaire.
	 * @throws InvalidParameterException
	 *             the questionnaire is null.
	 */
	public final void setQuestionnaire(final Questionnaire questionnaire)
			throws InvalidParameterException {
		if (questionnaire == null) {
			throw new InvalidParameterException();
		}
		this.m_questionnaire = questionnaire;
	}

	/**
	 * @param visitor
	 *            Visitor that fills the questionnaire.
	 * @throws InvalidParameterException
	 *             the visitor is null.
	 */
	public final void setVisitor(final Visitor visitor)
			throws InvalidParameterException {
		if (visitor == null) {
			throw new InvalidParameterException();
		}
		this.m_visitor = visitor;
	}

	/**
	 * Searches an answer that it question has the given id.
	 * 
	 * @param id
	 *            question id.
	 * @return answer that correspond to the given id. null if there is not a
	 *         question with this id.
	 */
	public final Answer getAnswerByQuestionId(final String id) {
		Answer foundAnswer = null;
		boolean found = false;
		for (int i = 0, length = this.m_answers.size(); i < length && !found; i++) {
			Answer answer = (Answer) this.m_answers.elementAt(i);
			if (answer.getQuestion().getId().compareTo(id) == 0) {
				found = true;
				foundAnswer = answer;
			}
		}
		return foundAnswer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public final boolean equals(final Object obj) {
		boolean equals = false;
		if (obj instanceof VisitorsAnswers) {
			VisitorsAnswers questionnaire = (VisitorsAnswers) obj;
			if (questionnaire.getQuestionnaire().hashCode() == getQuestionnaire()
					.hashCode()
					&& questionnaire.getVisitor().hashCode() == getVisitor()
							.hashCode()) {
				Vector answers = getAnswers();
				Vector answersObj = questionnaire.getAnswers();
				if (answers.size() == answersObj.size()) {
					equals = true;
					for (int i = 0, length = answers.size(); i < length
							&& equals; i++) {
						if (answers.elementAt(i).hashCode() != answersObj
								.elementAt(i).hashCode()) {
							equals = false;
						}
					}
				}
			}
		}
		return equals;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public final int hashCode() {
		int code = getQuestionnaire().hashCode() + getAnswers().size()
				- getVisitor().hashCode();
		return code;
	}
}
