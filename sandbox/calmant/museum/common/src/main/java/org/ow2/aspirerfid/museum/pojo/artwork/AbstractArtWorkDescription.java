package org.ow2.aspirerfid.museum.pojo.artwork;

import org.ow2.aspirerfid.museum.pojo.InvalidParameterException;

/**
 * Represents a description about the artwork.
 * 
 * @author <a href="mailto:angoca@yahoo.com">Andres Gomez</a>
 */
public abstract class AbstractArtWorkDescription {

    /**
     * XML tag: content.
     */
    public static final String XML_ARTWORK_INF_CONTENT = "content";
    /**
     * XML tag: type.
     */
    public static final String XML_ARTWORK_INF_TYPE = "type";
    /**
     * XML tag: description.
     */
    public static final String XML_ARWORK_DESCRIPTION = "description";
    /**
     * Type of description.
     */
    private String m_type;

    /**
     * @param type
     *                Type of description.
     * @throws InvalidParameterException
     *                 If the type is null or empty.
     */
    public AbstractArtWorkDescription(final String type)
	    throws InvalidParameterException {
	if (type == null || type.compareTo("") == 0) {
	    throw new InvalidParameterException();
	}
	this.m_type = type;
    }

    /**
     * @return Type of description.
     */
    public final String getType() {
	return this.m_type;
    }

    /**
     * @param type
     *                Type of description.
     * @throws InvalidParameterException
     *                 If the type is invalid.
     */
    public final void setType(final String type)
	    throws InvalidParameterException {
	if (type == null || type.compareTo("") == 0) {
	    throw new InvalidParameterException();
	}
	this.m_type = type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final Object obj) {
	boolean equals = false;
	if (obj instanceof AbstractArtWorkDescription) {
	    AbstractArtWorkDescription description = (AbstractArtWorkDescription) obj;
	    if (description.getType().compareTo(getType()) == 0) {
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
	int code = getType().hashCode();
	return code;
    }
}
