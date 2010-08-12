package org.ow2.aspirerfid.museum.pojo.question;

import org.ow2.aspirerfid.museum.pojo.InvalidParameterException;

/**
 * This class represents an abstract question.
 * 
 * @author <a href="mailto:angoca@yahoo.com">Andres Gomez</a>
 */
public abstract class AbstractQuestion {

    /**
     * XML tag: question.
     */
    public static final String XML_QUESTION = "question";
    /**
     * XML tag: content.
     */
    public static final String XML_QUESTION_CONTENT = "content";
    /**
     * XML tag: id (question).
     */
    public static final String XML_QUESTION_ID = "id";
    /**
     * XML tag: type.
     */
    public static final String XML_QUESTION_TYPE = "type";
    /**
     * Question (Text).
     */
    private String m_content;
    /**
     * Question's id. This has to be unique.
     */
    private String m_id;

    /**
     * @param id
     *                Question's id. This has to be unique.
     * @param content
     *                Question (Text.)
     * @throws InvalidParameterException
     *                 if some of the parameters are null.
     */
    protected AbstractQuestion(final String id, final String content)
	    throws InvalidParameterException {
	if (id == null || content == null) {
	    throw new InvalidParameterException();
	}
	this.m_id = id;
	this.m_content = content;
    }

    /**
     * @return Question (Text.)
     */
    public final String getContent() {
	return this.m_content;
    }

    /**
     * @return Question's id.
     */
    public final String getId() {
	return this.m_id;
    }

    /**
     * @return Type of the question.
     */
    public abstract String getType();

    /**
     * @param content
     *                Question (Text.)
     * @throws InvalidParameterException
     *                 If the content is null.
     */
    public final void setContent(final String content)
	    throws InvalidParameterException {
	if (content == null) {
	    throw new InvalidParameterException();
	}
	this.m_content = content;
    }

    /**
     * @param id
     *                Question's id. This has to be unique.
     * @throws InvalidParameterException
     *                 If the id is null.
     */
    public final void setId(final String id) throws InvalidParameterException {
	if (id == null) {
	    throw new InvalidParameterException();
	}
	this.m_id = id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final Object obj) {
	boolean equals = false;
	if (obj instanceof AbstractQuestion) {
	    AbstractQuestion question = (AbstractQuestion) obj;
	    if (question.getContent().compareTo(getContent()) == 0
		    && question.getId().compareTo(getId()) == 0) {
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
	int code = getContent().hashCode() + getId().hashCode();
	return code;
    }
}
