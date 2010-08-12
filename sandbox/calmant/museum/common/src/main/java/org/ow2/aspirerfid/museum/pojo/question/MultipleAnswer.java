/**
 * 
 */
package org.ow2.aspirerfid.museum.pojo.question;

import java.util.Vector;

import org.ow2.aspirerfid.museum.pojo.InvalidParameterException;

/**
 * this class represents a answer of multiple options.
 * 
 * @author <a href="mailto:angoca@yahoo.com">Andres Gomez</a>
 */
public class MultipleAnswer extends Answer {

    /**
     * Set of answers.
     */
    private Vector m_answers;

    /**
     * @param question
     *                Related question.
     * @throws InvalidParameterException
     *                 If a parameter is invalid.
     */
    public MultipleAnswer(final MultipleOptionMultipleAnswer question)
	    throws InvalidParameterException {
	super(question, "");
	this.m_answers = new Vector();
    }

    /**
     * @return Set of answers.
     */
    public final Vector getAnswers() {
	return this.m_answers;
    }

    /**
     * @param answers
     *                Set of answers.
     */
    public final void setAnswers(final Vector answers) {
	this.m_answers = answers;
    }

    /**
     * @param answer
     *                Answer to add to the set of answers.
     */
    public final void addAnswer(final String answer) {
	this.m_answers.addElement(answer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.ow2.aspirerfid.museum.pojo.question.Answer#equals(java.lang.Object)
     */
    public final boolean equals(final Object obj) {
	boolean equals = false;
	if (obj instanceof MultipleAnswer) {
	    MultipleAnswer multAnswer = (MultipleAnswer) obj;
	    if (multAnswer.getAnswers().size() == getAnswers().size()) {
		equals = true;
		for (int i = 0, length = multAnswer.getAnswers().size(); i < length
			&& equals; i++) {
		    String answerObj = (String) multAnswer.getAnswers()
			    .elementAt(i);
		    String answer = (String) getAnswers().elementAt(i);
		    if (answer.compareTo(answerObj) != 0) {
			equals = false;
		    }
		}
	    }
	}
	return equals;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.ow2.aspirerfid.museum.pojo.question.Answer#hashCode()
     */
    public final int hashCode() {
	int code = getAnswers().size();
	for (int i = 0, length = getAnswers().size(); i < length; i++) {
	    String answer = (String) getAnswers().elementAt(i);
	    code += answer.hashCode();
	}
	return code;
    }
}
