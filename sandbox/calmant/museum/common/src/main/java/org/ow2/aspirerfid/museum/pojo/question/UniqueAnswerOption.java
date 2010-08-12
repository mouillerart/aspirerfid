package org.ow2.aspirerfid.museum.pojo.question;

import org.ow2.aspirerfid.museum.pojo.InvalidParameterException;

/**
 * This represents an option of multiple options in a question.
 * 
 * @author <a href="mailto:angoca@yahoo.com">Andres Gomez</a>
 */
public class UniqueAnswerOption extends AnswerOption {
    /**
     * XML tag: percentage.
     */
    public static final String XML_PERCENTAGE = "percentage";
    /**
     * Percentage of this option chosen by the users.
     */
    private double m_percentage = -1;

    /**
     * @param name
     *                Possible answer.
     * @param percentage
     *                Percentage of this option chosen by the users.
     * @throws InvalidParameterException
     *                 Some parameter is invalid, less than 0 or greater than
     *                 100.
     */
    public UniqueAnswerOption(final String name, final double percentage)
	    throws InvalidParameterException {
	super(name);
	if (percentage < 0 || percentage > 100) {
	    throw new InvalidParameterException();
	}
	this.m_percentage = percentage;
    }

    /**
     * @return Percentage of this option chosen by the users.
     */
    public final double getPercentage() {
	return this.m_percentage;
    }

    /**
     * @param percentage
     *                Percentage of this option chosen by the users.
     * @throws InvalidParameterException
     *                 Some parameter is invalid, less than 0 or greater than
     *                 100.
     */
    public final void setPercentage(final double percentage)
	    throws InvalidParameterException {
	if (percentage < 0 || percentage > 100) {
	    throw new InvalidParameterException();
	}
	this.m_percentage = percentage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.ow2.aspirerfid.museum.pojo.question.AnswerOption#equals(java.lang.Object)
     */
    public final boolean equals(final Object obj) {
	boolean equals = false;
	if (super.equals(obj)) {
	    if (obj instanceof UniqueAnswerOption) {
		UniqueAnswerOption answer = (UniqueAnswerOption) obj;
		if (answer.getPercentage() == getPercentage()) {
		    equals = true;
		}
	    }
	}
	return equals;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.ow2.aspirerfid.museum.pojo.question.AnswerOption#hashCode()
     */
    public final int hashCode() {
	int code = super.hashCode() + new Double(getPercentage()).intValue();
	return code;
    }
}
