package org.ow2.aspirerfid.museum.pojo;

/**
 * Some parameter is not valid. It can be null or an empty string.
 * 
 * @author <a href="mailto:angoca@yahoo.com">Andres Gomez</a>
 */
public class InvalidParameterException extends Exception {

	/**
	 * Exception's Id.
	 */
	private static final long serialVersionUID = 4343823639679828196L;

	/**
	 * @param message
	 *            Message of the exception.
	 */
	public InvalidParameterException(final String message) {
		super(message);
	}

	/**
	 * Default constructor.
	 */
	public InvalidParameterException() {
		super("Invalid parameter");
	}

}
