package org.ow2.aspirerfid.museum.pojo.question;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.ow2.aspirerfid.museum.pojo.InvalidParameterException;

/**
 * This class represents a questionnaire.
 * 
 * @author <a href="mailto:angoca@yahoo.com">Andres Gomez</a>
 */
public class Questionnaire {

	/**
	 * XML tag: questionnaire.
	 */
	public static final String XML_QUESTIONNAIRE = "questionnaire";
	/**
	 * XML tag: id.
	 */
	public static final String XML_QUESTIONNAIRE_ID = "questionnaire.id";
	/**
	 * XML tag: title.
	 */
	public static final String XML_QUESTIONNAIRE_TITLE = "title";
	/**
	 * XML tag: instructions.
	 */
	public static final String XML_QUESTIONNAIRE_INSTRUCTIONS = "instructions";
	/**
	 * XML tag: type.
	 */
	public static final String XML_QUESTIONNAIRE_TYPE = "type";
	/**
	 * XML tag: questions.
	 */
	public static final String XML_QUESTIONS = "questions";
	/**
	 * XML tag: profile.request.
	 */
	public static final String XML_PROFILE_REQUEST = "profile.request";
	/**
	 * XML tag: survey.request.
	 */
	public static final String XML_SURVEY_REQUEST = "survey.request";
	/**
	 * XML tag: rating.request.
	 */
	public static final String XML_RATING_REQUEST = "rating.request";
	/**
	 * The type of questionnaire is profile.
	 */
	public static final String TYPE_PROFILE_REQUEST = "profile.request";
	/**
	 * The type of questionnaire is survey.
	 */
	public static final String TYPE_SURVEY_QUESTION = "survey.request";
	/**
	 * The type of questionnaire is rate an artwork.
	 */
	public static final String TYPE_ARTWORK_RATING = "rating.request";
	/**
	 * Questionnaire's Id.
	 */
	private String m_id;
	/**
	 * Set of questions.
	 */
	private Vector m_questions;
	/**
	 * Type of questionnaire.
	 */
	private String m_questionnaireType;
	/**
	 * Sets of ID to ensure that all the elements has different ID. The
	 * hashtable has as value the same key.
	 */
	private Hashtable m_ids;
	/**
	 * Questionnaire's title.
	 */
	private String m_title;
	/**
	 * Instructions about the questionnaire.
	 */
	private String m_instructions;

	/**
	 * @param id
	 *            Questionnaire's Id.
	 * @param title
	 *            The title.
	 * @param instructions
	 *            Instructions to know how to fill the questionnaire, or the
	 *            objective. null is valid.
	 * @param questionnaireType
	 *            Type of questionnaire (XML identifier.)
	 * @throws InvalidParameterException
	 *             If some parameter is invalid.
	 */
	public Questionnaire(final String id, final String title,
			final String instructions, final String questionnaireType)
			throws InvalidParameterException {
		if (id == null || id.compareTo("") == 0 || title == null
				|| title.compareTo("") == 0 || questionnaireType == null
				|| questionnaireType.compareTo("") == 0) {
			throw new InvalidParameterException();
		} else if (!(questionnaireType
				.compareTo(Questionnaire.TYPE_ARTWORK_RATING) == 0)
				&& !(questionnaireType
						.compareTo(Questionnaire.TYPE_PROFILE_REQUEST) == 0)
				&& !(questionnaireType
						.compareTo(Questionnaire.TYPE_SURVEY_QUESTION) == 0)) {
			throw new InvalidParameterException();
		}
		this.m_id = id;
		this.m_title = title;
		this.m_instructions = instructions;
		this.m_questionnaireType = questionnaireType;
		this.m_questions = new Vector();
		this.m_ids = new Hashtable();
	}

	/**
	 * Adds the given question to the set of questions.
	 * 
	 * @param question
	 *            Question to add.
	 * @throws ExistantQuestionIdException
	 *             If the question's id is already in the questionnaire.
	 * @throws InvalidParameterException
	 *             The questions param is null.
	 */
	public final void addQuestion(final AbstractQuestion question)
			throws ExistantQuestionIdException, InvalidParameterException {
		if (question == null) {
			throw new InvalidParameterException();
		}
		String id = question.getId();
		if (this.m_ids.contains(id)) {
			throw new ExistantQuestionIdException();
		}
		this.m_ids.put(id, id);
		this.m_questions.addElement(question);
	}

	/**
	 * @return Questionnaire's Id.
	 */
	public final String getId() {
		return this.m_id;
	}

	/**
	 * @return Set of questions. This is a clone, not the original.
	 */
	public final Vector getQuestions() {
		return this.m_questions;
	}

	/**
	 * @param id
	 *            Questionnaire's Id.
	 * @throws InvalidParameterException
	 *             the id is invalid.
	 */
	public final void setId(final String id) throws InvalidParameterException {
		if (id == null || id.compareTo("") == 0) {
			throw new InvalidParameterException();
		}
		this.m_id = id;
	}

	/**
	 * @param questions
	 *            Set of questions.
	 * @throws ExistantQuestionIdException
	 *             If one of the question has an id that already exist.
	 * @throws InvalidParameterException
	 *             if the questions param is invalid.
	 */
	public final void setQuestions(final Vector questions)
			throws ExistantQuestionIdException, InvalidParameterException {
		if (questions == null) {
			throw new InvalidParameterException();
		}
		for (int i = 0, length = questions.size(); i < length; i++) {
			AbstractQuestion question = (AbstractQuestion) questions
					.elementAt(i);
			String id = question.getId();
			if (this.m_ids.contains(id)) {
				throw new ExistantQuestionIdException();
			}
			this.m_ids.put(id, null);
			this.m_questions.addElement(question);
		}
	}

	/**
	 * @return Type of questionnaire.
	 */
	public final String getQuestionnaireType() {
		return this.m_questionnaireType;
	}

	/**
	 * @param questionnaireType
	 *            Type of questionnaire.
	 * @throws InvalidParameterException
	 *             The type of questionnaire is invalid.
	 */
	public final void setQuestionnaireType(final String questionnaireType)
			throws InvalidParameterException {
		if (!(questionnaireType.compareTo(Questionnaire.TYPE_ARTWORK_RATING) == 0)
				&& !(questionnaireType
						.compareTo(Questionnaire.TYPE_PROFILE_REQUEST) == 0)
				&& !(questionnaireType
						.compareTo(Questionnaire.TYPE_SURVEY_QUESTION) == 0)) {
			throw new InvalidParameterException();
		}
		this.m_questionnaireType = questionnaireType;
	}

	/**
	 * @return Questionnaire's title
	 */
	public final String getTitle() {
		return this.m_title;
	}

	/**
	 * @param title
	 *            Questionnaire's title
	 * @throws InvalidParameterException
	 *             If the title is null.
	 */
	public final void setTitle(final String title)
			throws InvalidParameterException {
		if (title == null) {
			throw new InvalidParameterException();
		}
		this.m_title = title;
	}

	/**
	 * @return Instructions about the questionnaire.
	 */
	public final String getInstructions() {
		return this.m_instructions;
	}

	/**
	 * @param instructions
	 *            Instructions about the questionnaire.
	 */
	public final void setInstructions(final String instructions) {
		this.m_instructions = instructions;
	}

	/**
	 * @param id
	 *            Id of the question to find.
	 * @return question with the corresponding id. Null if there is no question
	 *         with the given id.
	 */
	public final AbstractQuestion getQuestionById(final String id) {
		AbstractQuestion foundQuestion = null;
		Enumeration elemts = this.m_questions.elements();
		while (elemts.hasMoreElements()) {
			AbstractQuestion question = (AbstractQuestion) elemts.nextElement();
			if (question.getId().compareTo(id) == 0) {
				foundQuestion = question;
			}
		}
		return foundQuestion;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public final boolean equals(final Object obj) {
		boolean equals = false;
		if (obj instanceof Questionnaire) {
			Questionnaire questionnaire = (Questionnaire) obj;
			if (questionnaire.getId().compareTo(getId()) == 0
					&& questionnaire.getInstructions().compareTo(
							getInstructions()) == 0
					&& questionnaire.getTitle().compareTo(getTitle()) == 0
					&& questionnaire.getQuestionnaireType().compareTo(
							getQuestionnaireType()) == 0) {
				Vector questions = getQuestions();
				Vector questionsObj = questionnaire.getQuestions();
				if (questions.size() == questionsObj.size()) {
					equals = true;
					for (int i = 0, length = questions.size(); i < length
							&& equals; i++) {
						AbstractQuestion question = (AbstractQuestion) questions
								.elementAt(i);
						AbstractQuestion questionObj = (AbstractQuestion) questionsObj
								.elementAt(i);
						if (question.hashCode() != questionObj.hashCode()) {
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
		int code = 0;
		// FIXME
		return code;
	}
}
