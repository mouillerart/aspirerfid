package org.ow2.aspirerfid.museum.pojo.question;

import org.ow2.aspirerfid.museum.pojo.InvalidParameterException;

/**
 * This is the representation of a rating question, where the minimal and
 * maximal values are defined. The average answer is also in the class.
 * 
 * @author <a href="mailto:angoca@yahoo.com">Andres Gomez</a>
 */
public class Rating extends AbstractQuestion {
    /**
     * Type of question: rate.
     */
    public static final String QUESTION_TYPE = "rate";
    /**
     * XML tag: average.
     */
    public static final String XML_QUESTION_AVERAGE = "average";
    /**
     * XML tag: max.
     */
    public static final String XML_QUESTION_MAX = "max";
    /**
     * XML tag: min.
     */
    public static final String XML_QUESTION_MIN = "min";
    /**
     * Default value.
     */
    private static final int DEFAULT_VALUE = -1;
    /**
     * Average of the answer.
     */
    private double m_average = DEFAULT_VALUE;
    /**
     * Maximal value for rating.
     */
    private int m_max = DEFAULT_VALUE;
    /**
     * Minimal value for rating.
     */
    private int m_min = DEFAULT_VALUE;

    /**
     * @param id
     *                Question's id.
     * @param content
     *                Question (text.)
     * @param min
     *                Minimal value for rating.
     * @param max
     *                Maximal value for rating.
     * @param average
     *                Average of the answer.
     * @throws InvalidParameterException
     *                 If the id or content is invalid, or the range are
     *                 invalid.
     */
    public Rating(final String id, final String content, final int min,
	    final int max, final double average)
	    throws InvalidParameterException {
	super(id, content);
	if (min >= max || average < min || average > max) {
	    throw new InvalidParameterException();
	}
	this.m_min = min;
	this.m_max = max;
	this.m_average = average;
    }

    /**
     * @return Average of the answer.
     */
    public final double getAverage() {
	return this.m_average;
    }

    /**
     * @return Maximal value for rating.
     */
    public final int getMax() {
	return this.m_max;
    }

    /**
     * @return Minimal value for rating.
     */
    public final int getMin() {
	return this.m_min;
    }

    /*
     * (non-Javadoc)
     * 
     * @see core.questions.Question#getType()
     */
    public final String getType() {
	return Rating.QUESTION_TYPE;
    }

    /**
     * @param average
     *                Average of the answer.
     * @throws InvalidParameterException
     *                 the average value is the default and it's invalid.
     */
    public final void setAverage(final double average)
	    throws InvalidParameterException {
	if (average == DEFAULT_VALUE) {
	    throw new InvalidParameterException();
	}
	this.m_average = average;
    }

    /**
     * @param max
     *                Maximal value for rating.
     * @throws InvalidParameterException
     *                 the maximum value is the default and it's invalid.
     */
    public final void setMax(final int max) throws InvalidParameterException {
	if (max == DEFAULT_VALUE) {
	    throw new InvalidParameterException();
	}
	this.m_max = max;
    }

    /**
     * @param min
     *                Minimal value for rating.
     * @throws InvalidParameterException
     *                 the minimum value is the default and it's invalid.
     */
    public final void setMin(final int min) throws InvalidParameterException {
	if (min == DEFAULT_VALUE) {
	    throw new InvalidParameterException();
	}
	this.m_min = min;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.ow2.aspirerfid.museum.pojo.question.AbstractQuestion#equals(java.lang.Object)
     */
    public final boolean equals(final Object obj) {
	boolean equals = false;
	if (super.equals(obj)) {
	    if (obj instanceof Rating) {
		Rating question = (Rating) obj;
		if (question.getAverage() == getAverage()
			&& question.getMin() == getMin()
			&& question.getMax() == getMax()) {
		    equals = true;
		}
	    }
	}
	return equals;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.ow2.aspirerfid.museum.pojo.question.AbstractQuestion#hashCode()
     */
    public final int hashCode() {
	int code = super.hashCode() * getMin() - getMax()
		- new Double(getAverage()).intValue();
	return code;
    }
}
