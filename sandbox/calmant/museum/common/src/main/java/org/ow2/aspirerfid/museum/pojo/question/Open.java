package org.ow2.aspirerfid.museum.pojo.question;

import org.ow2.aspirerfid.museum.pojo.InvalidParameterException;

/**
 * Represents an open question where the user can write a text.
 * 
 * @author <a href="mailto:angoca@yahoo.com">Andres Gomez</a>
 */
public class Open extends AbstractQuestion {

	/**
	 * Type of question: open.
	 */
	public static final String QUESTION_TYPE = "open";

	/**
	 * @param id
	 *            Question's id.
	 * @param content
	 *            Question (text.)
	 * @throws InvalidParameterException
	 *             If some parameter is null.
	 */
	public Open(final String id, final String content) throws InvalidParameterException {
		super(id, content);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see core.questions.Question#getType()
	 */
	public final String getType() {
		return Open.QUESTION_TYPE;
	}
}
