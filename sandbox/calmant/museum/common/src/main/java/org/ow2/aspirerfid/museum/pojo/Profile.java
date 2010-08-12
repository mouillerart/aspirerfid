/**
 * 
 */
package org.ow2.aspirerfid.museum.pojo;

/**
 * This class represents a POJO of a profile.
 * 
 * @author <a href="mailto:angoca@yahoo.com">Andres Gomez</a>
 */
public class Profile {

    /**
     * Language.
     */
    private String m_language;
    /**
     * The user is deaf.
     */
    private boolean m_deaf;
    /**
     * The user is blind.
     */
    private boolean m_blind;
    /**
     * The range of age of the user (1:<10, 2:11-17, 3:18-25, 4:26-40, 5:40-60,
     * 6:<61).
     */
    private int m_rangeAge;
    /**
     * The user's privacy is sent.
     */
    private boolean m_privacy;

    /**
     * @param language
     *                Language.
     * @param deaf
     *                The user is deaf.
     * @param blind
     *                The user is blind.
     * @param rangeAge
     *                The range of age of the user (1:<10, 2:11-17, 3:18-25,
     *                4:26-40, 5:40-60, 6:<61)
     * @param privacy
     *                The user's privacy is sent.
     */
    public Profile(final String language, final boolean deaf,
	    final boolean blind, final int rangeAge, final boolean privacy) {
	this.m_language = language;
	this.m_deaf = deaf;
	this.m_blind = blind;
	this.m_rangeAge = rangeAge;
	this.m_privacy = privacy;
    }

    /**
     * @return Language.
     */
    public final String getLanguage() {
	return this.m_language;
    }

    /**
     * @param language
     *                Language.
     */
    public final void setLanguage(final String language) {
	this.m_language = language;
    }

    /**
     * @return The user is deaf.
     */
    public final boolean isDeaf() {
	return this.m_deaf;
    }

    /**
     * @param deaf
     *                The user is deaf.
     */
    public final void setDeaf(final boolean deaf) {
	this.m_deaf = deaf;
    }

    /**
     * @return The user is blind.
     */
    public final boolean isBlind() {
	return this.m_blind;
    }

    /**
     * @param blind
     *                The user is blind.
     */
    public final void setBlind(final boolean blind) {
	this.m_blind = blind;
    }

    /**
     * @return The range of age of the user (1:<10, 2:11-17, 3:18-25, 4:26-40,
     *         5:40-60, 6:<61)
     */
    public final int getRangeAge() {
	return this.m_rangeAge;
    }

    /**
     * @param rangeAge
     *                The range of age of the user (1:<10, 2:11-17, 3:18-25,
     *                4:26-40, 5:40-60, 6:<61)
     */
    public final void setRangeAge(final int rangeAge) {
	this.m_rangeAge = rangeAge;
    }

    /**
     * @return The user's privacy is sent.
     */
    public final boolean isPrivacy() {
	return this.m_privacy;
    }

    /**
     * @param privacy
     *                The user's privacy is sent.
     */
    public final void setPrivacy(final boolean privacy) {
	this.m_privacy = privacy;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public final boolean equals(final Object obj) {
	boolean equals = false;
	if (obj instanceof Profile) {
	    Profile profile = (Profile) obj;
	    if (profile.getLanguage().compareTo(getLanguage()) == 0
		    && profile.getRangeAge() == getRangeAge()
		    && profile.isBlind() == isBlind()
		    && profile.isDeaf() == isDeaf()
		    && profile.isPrivacy() == isPrivacy()) {
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
    public final int hashCode() {
	int code = getLanguage().hashCode() - getRangeAge();
	if (isBlind()) {
	    code *= 2;
	}
	if (isDeaf()) {
	    code -= 3;
	}
	if (isPrivacy()) {
	    code += code;
	}
	return code;
    }
}
