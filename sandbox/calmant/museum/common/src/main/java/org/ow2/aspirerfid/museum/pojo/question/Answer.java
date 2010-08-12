package org.ow2.aspirerfid.museum.pojo.question;

import org.ow2.aspirerfid.museum.pojo.InvalidParameterException;

/**
 * This represents the visitor's answer of a question.
 * 
 * @author <a href="mailto:angoca@yahoo.com">Andres Gomez</a>
 */
public class Answer {

    /**
     * XML tag: answer.
     */
    public static final String XML_ANSWER = "answer";
    /**
     * XML tag: option.
     */
    public static final String XML_OPTION = "option";
    /**
     * Answer of the question.
     */
    private String m_answer;
    /**
     * Related question.
     */
    private AbstractQuestion m_question;

    /**
     * @param question
     *                Related question.
     * @param answer
     *                Answer of the question.
     * @throws InvalidParameterException
     *                 if some parameter is null.
     */
    public Answer(final AbstractQuestion question, final String answer)
	    throws InvalidParameterException {
	if (question == null || answer == null) {
	    throw new InvalidParameterException();
	}
	this.m_question = question;
	this.m_answer = answer;
    }

    /**
     * @return Related question.
     */
    public final String getAnswer() {
	return this.m_answer;
    }

    /**
     * @return Related question.
     */
    public final AbstractQuestion getQuestion() {
	return this.m_question;
    }

    /**
     * @param answer
     *                Related question.
     * @throws InvalidParameterException
     *                 If the answer is null.
     */
    public final void setAnswer(final String answer)
	    throws InvalidParameterException {
	if (answer == null) {
	    throw new InvalidParameterException();
	}
	this.m_answer = answer;
    }

    /**
     * @param question
     *                Related question.
     * @throws InvalidParameterException
     *                 If the question is null.
     */
    public final void setQuestion(final AbstractQuestion question)
	    throws InvalidParameterException {
	if (question == null) {
	    throw new InvalidParameterException();
	}
	this.m_question = question;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final Object obj) {
	boolean equals = false;
	if (obj instanceof Answer) {
	    Answer answer = (Answer) obj;
	    if (answer.getAnswer().compareTo(getAnswer()) == 0) {
		AbstractQuestion question = answer.getQuestion();
		if (question.equals(getQuestion())) {
		    equals = true;
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
    public int hashCode() {
	int code = getAnswer().hashCode() + getQuestion().hashCode();
	return code;
    }
}
