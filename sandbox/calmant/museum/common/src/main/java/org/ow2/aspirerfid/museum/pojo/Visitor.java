package org.ow2.aspirerfid.museum.pojo;

/**
 * Visitor that touches the tags, sees the information about the works of art
 * and answer the questionnaires.
 * 
 * @author <a href="mailto:angoca@yahoo.com">Andres Gomez</a>
 */
public class Visitor {

    /**
     * XML tag: visitor.id.
     */
    public static final String XML_VISITOR_ID = "visitor.id";
    /**
     * This is the identifier for a non valid visitor.
     */
    public static final String NOT_DEFINED = "-1";
    /**
     * Visitor's id.
     */
    private String m_id;
    /**
     * Visitor's profile.
     */
    private Profile m_profile;

    /**
     * @param id
     *                Visitor's id.
     * @param profile
     *                visitor's profile.
     * @throws InvalidParameterException
     *                 If the parameter is not valid.
     */
    public Visitor(final String id, final Profile profile)
	    throws InvalidParameterException {
	if (id == null || id.compareTo("") == 0) {
	    throw new InvalidParameterException();
	}
	this.m_id = id;
	this.m_profile = profile;
    }

    /**
     * @return Visitor's id.
     */
    public final String getId() {
	return this.m_id;
    }

    /**
     * @param id
     *                Visitor's id.
     * @throws InvalidParameterException
     *                 If the parameter is not valid.
     */
    public final void setId(final String id) throws InvalidParameterException {
	if (id == null || id.compareTo("") == 0) {
	    throw new InvalidParameterException();
	}
	this.m_id = id;
    }

    /**
     * @param profile
     *                Visitor's profile.
     */
    public final void setProfile(final Profile profile) {
	this.m_profile = profile;
    }

    /**
     * @return Visitor's profile.
     */
    public final Profile getProfile() {
	return this.m_profile;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public final boolean equals(final Object obj) {
	boolean equals = false;
	if (obj instanceof Visitor) {
	    Visitor visitor = (Visitor) obj;
	    if (visitor.getId().compareTo(getId()) == 0) {
		if (visitor.getProfile() == null && getProfile() == null) {
		    equals = true;
		} else if (visitor.getProfile().equals(getProfile())) {
		    equals = true;
		}
	    }
	}
	return equals;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    public final int hashCode() {
	int code = getId().hashCode();
	if (getProfile() != null) {
	    code += code - getProfile().hashCode();
	}
	return code;
    }
}
