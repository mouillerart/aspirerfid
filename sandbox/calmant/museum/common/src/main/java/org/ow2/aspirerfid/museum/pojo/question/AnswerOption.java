package org.ow2.aspirerfid.museum.pojo.question;

import org.ow2.aspirerfid.museum.pojo.InvalidParameterException;

/**
 * Represents a option of a question with multiples options.
 * 
 * @author <a href="mailto:angoca@yahoo.com">Andres Gomez</a>
 */
public class AnswerOption {

    /**
     * XML tag: option.
     */
    public static final String XML_OPTION = "option";
    /**
     * XML tag: name.
     */
    public static final String XML_NAME = "name";
    /**
     * Possible answer.
     */
    private String m_name = null;

    /**
     * @param name
     *                Possible answer.
     * @throws InvalidParameterException
     *                 If the answer is null.
     */
    public AnswerOption(final String name) throws InvalidParameterException {
	if (name == null) {
	    throw new InvalidParameterException();
	}
	this.m_name = name;
    }

    /**
     * @return Possible answer.
     */
    public final String getName() {
	return this.m_name;
    }

    /**
     * @param name
     *                Possible answer.
     * @throws InvalidParameterException
     *                 if the name is null.
     */
    public final void setName(final String name)
	    throws InvalidParameterException {
	if (name == null) {
	    throw new InvalidParameterException();
	}
	this.m_name = name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final Object obj) {
	boolean equals = false;
	if (obj instanceof AnswerOption) {
	    AnswerOption option = (AnswerOption) obj;
	    if (option.getName().compareTo(getName()) == 0) {
		equals = true;
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
	int code = getName().hashCode();
	return code;
    }
}
