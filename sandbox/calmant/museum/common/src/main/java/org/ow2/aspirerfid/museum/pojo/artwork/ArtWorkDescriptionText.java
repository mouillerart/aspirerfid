package org.ow2.aspirerfid.museum.pojo.artwork;

import org.ow2.aspirerfid.museum.pojo.InvalidParameterException;

/**
 * The description about the artwork is plain text.
 * 
 * @author <a href="mailto:angoca@yahoo.com">Andres Gomez</a>
 */
public class ArtWorkDescriptionText extends AbstractArtWorkDescription {

    /**
     * Type of text.
     */
    public static final String TYPE_TEXT = "text/plain";
    /**
     * XML tag: message.
     */
    public static final String XML_MESSAGE = "message";
    /**
     * XML tag: title.
     */
    public static final String XML_TEXT_TITLE = "title";
    /**
     * Text of the description.
     */
    private String m_message;
    /**
     * Title of the description.
     */
    private String m_title;

    /**
     * @param type
     *                Type of description.
     * @param title
     *                Title of the description.
     * @param message
     *                Text of the description.
     * @throws InvalidParameterException
     *                 If some parameter is null or invalid.
     */
    public ArtWorkDescriptionText(final String type, final String title,
	    final String message) throws InvalidParameterException {
	super(type);
	if (type == null || type.compareTo("") == 0 || title == null
		|| message == null) {
	    throw new InvalidParameterException();
	} else if (type.compareTo(ArtWorkDescriptionText.TYPE_TEXT) != 0) {
	    throw new InvalidParameterException("Invalid type of text file.");
	}
	this.m_title = title;
	this.m_message = message;
    }

    /**
     * @return Text of the description.
     */
    public final String getMessage() {
	return this.m_message;
    }

    /**
     * @return Title of the description.
     */
    public final String getTitle() {
	return this.m_title;
    }

    /**
     * @param message
     *                Text of the description.
     */
    public final void setMessage(final String message) {
	this.m_message = message;
    }

    /**
     * @param title
     *                Title of the description.
     */
    public final void setTitle(final String title) {
	this.m_title = title;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public final boolean equals(final Object obj) {
	boolean equals = false;
	if (super.equals(obj)) {
	    if (obj instanceof ArtWorkDescriptionText) {
		ArtWorkDescriptionText text = (ArtWorkDescriptionText) obj;
		if (text.getMessage().compareTo(getMessage()) == 0
			&& text.getTitle().compareTo(getTitle()) == 0
			&& text.getType().compareTo(getType()) == 0) {
		    equals = true;
		}
	    }
	}
	return equals;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.ow2.aspirerfid.museum.pojo.artwork.AbstractArtWorkDescription#hashCode()
     */
    public final int hashCode() {
	int code = super.hashCode() + getMessage().hashCode()
		- getTitle().hashCode() - getType().hashCode();
	return code;
    }
}
