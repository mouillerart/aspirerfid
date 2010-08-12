package org.ow2.aspirerfid.museum.pojo.question;

/**
 * The given id already exists in the ser of questions.
 * 
 * @author <a href="mailto:angoca@yahoo.com">Andres Gomez</a>
 */
public class ExistantQuestionIdException extends Exception {

	/**
	 * Exception's ID.
	 */
	private static final long serialVersionUID = -8795612642579631634L;

	/**
	 * Default constructor.
	 */
	ExistantQuestionIdException() {
		// Nothing
	}
}
