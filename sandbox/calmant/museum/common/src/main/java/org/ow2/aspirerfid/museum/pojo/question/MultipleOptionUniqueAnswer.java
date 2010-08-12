package org.ow2.aspirerfid.museum.pojo.question;

import org.ow2.aspirerfid.museum.pojo.InvalidParameterException;

/**
 * This is a question with multiples options and just one answer.
 * 
 * @author <a href="mailto:angoca@yahoo.com">Andres Gomez</a>
 */
public class MultipleOptionUniqueAnswer extends MultipleOptionMultipleAnswer {

    /**
     * Type of question: MultOptUniAns.
     */
    public static final String QUESTION_TYPE_UNI_ANS = "MultOptUniAns";

    /**
     * @param id
     *                question's id.
     * @param content
     *                Question (text.)
     * @throws InvalidParameterException
     *                 if some parameter is null.
     */
    public MultipleOptionUniqueAnswer(final String id, final String content)
	    throws InvalidParameterException {
	super(id, content);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.ow2.aspirerfid.museum.pojo.question.MultipleOptionMultipleAnswer#getType()
     */
    public final String getType() {
	return MultipleOptionUniqueAnswer.QUESTION_TYPE_UNI_ANS;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.ow2.aspirerfid.museum.pojo.question.MultipleOptionMultipleAnswer#equals(java.lang.Object)
     */
    public final boolean equals(final Object obj) {
	boolean equals = false;
	if (super.equals(obj)) {
	    if (obj instanceof MultipleOptionUniqueAnswer) {
		MultipleOptionUniqueAnswer answer = (MultipleOptionUniqueAnswer) obj;
		if (answer.getContent().compareTo(getContent()) == 0) {
		    equals = true;
		}
	    }
	}
	return equals;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.ow2.aspirerfid.museum.pojo.question.MultipleOptionMultipleAnswer#hashCode()
     */
    public final int hashCode() {
	int code = super.hashCode() - getContent().hashCode();
	return code;
    }
}
