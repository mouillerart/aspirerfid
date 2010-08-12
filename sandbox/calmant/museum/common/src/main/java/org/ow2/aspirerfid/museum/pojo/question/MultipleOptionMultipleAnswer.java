package org.ow2.aspirerfid.museum.pojo.question;

import java.util.Vector;

import org.ow2.aspirerfid.museum.pojo.InvalidParameterException;

/**
 * This is a question with multiples options and multiples answers.
 * 
 * @author <a href="mailto:angoca@yahoo.com">Andres Gomez</a>
 */
public class MultipleOptionMultipleAnswer extends AbstractQuestion {

    /**
     * XML tag: options.
     */
    public static final String XML_OPTIONS = "options";
    /**
     * Type of question: MultOptMulAns.
     */
    public static final String QUESTION_TYPE = "MultOptMulAns";
    /**
     * Set of options.
     */
    private Vector m_options;

    /**
     * @param id
     *                Question's id.
     * @param content
     *                Question (text.)
     * @throws InvalidParameterException
     *                 If some parameter is null.
     */
    public MultipleOptionMultipleAnswer(final String id, final String content)
	    throws InvalidParameterException {
	super(id, content);
	this.m_options = new Vector();
    }

    /**
     * Adds an option to the set of options.
     * 
     * @param option
     *                Option to add.
     * @throws InvalidParameterException
     *                 If the option is null.
     */
    public final void addOption(final AnswerOption option)
	    throws InvalidParameterException {
	if (option == null) {
	    throw new InvalidParameterException();
	}
	this.m_options.addElement(option);
    }

    /**
     * @return Set of options.
     */
    public final Vector getOptions() {
	return this.m_options;
    }

    /*
     * (non-Javadoc)
     * 
     * @see core.questions.Question#getType()
     */
    public String getType() {
	return MultipleOptionMultipleAnswer.QUESTION_TYPE;
    }

    /**
     * @param options
     *                Set of options.
     * @throws InvalidParameterException
     *                 The options are null.
     */
    public final void setOptions(final Vector options)
	    throws InvalidParameterException {
	if (options == null) {
	    throw new InvalidParameterException();
	}
	this.m_options = options;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.ow2.aspirerfid.museum.pojo.question.AbstractQuestion#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
	boolean equals = false;
	if (super.equals(obj)) {
	    if (obj instanceof MultipleOptionMultipleAnswer) {
		MultipleOptionMultipleAnswer answer = (MultipleOptionMultipleAnswer) obj;
		Vector options = getOptions();
		Vector optionsObj = answer.getOptions();
		if (options.size() == optionsObj.size()) {
		    equals = true;
		    for (int i = 0, length = options.size(); i < length
			    && equals; i++) {
			AnswerOption option = (AnswerOption) options
				.elementAt(i);
			AnswerOption optionObj = (AnswerOption) optionsObj
				.elementAt(i);
			if (option.getName().compareTo(optionObj.getName()) != 0) {
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
     * @see org.ow2.aspirerfid.museum.pojo.question.AbstractQuestion#hashCode()
     */
    public int hashCode() {
	int code = super.hashCode();
	Vector options = getOptions();
	for (int i = 0, length = options.size(); i < length; i++) {
	    AnswerOption option = (AnswerOption) options.elementAt(i);
	    code += option.getName().hashCode();
	}
	return code;
    }
}
