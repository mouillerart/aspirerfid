package org.ow2.aspirerfid.museum.pojo.artwork;

import org.ow2.aspirerfid.museum.pojo.InvalidParameterException;

/**
 * This represents a description about an artwork in binary format.
 * 
 * @author <a href="mailto:angoca@yahoo.com">Andres Gomez</a>
 */
public class ArtWorkDescriptionBinaire extends AbstractArtWorkDescription {

    /**
     * Type of valid audio: wav.
     */
    public static final String TYPE_AUDIO = "audio/x-wav";
    /**
     * Type of valid image: bmp.
     */
    public static final String TYPE_IMAGE_BMP = "image/bmp";
    /**
     * Type of valid image: jpeg.
     */
    public static final String TYPE_IMAGE_JPEG = "image/jpeg";
    /**
     * XML tag: filename.
     */
    public static final String XML_FILE = "filename";
    /**
     * File name where the binary content is.
     */
    private String m_fileName;

    /**
     * @param type
     *                Type of description.
     * @param fileName
     *                File name where the binary content is.
     * @throws InvalidParameterException
     *                 If the type or the filename is null or invalid.
     */
    public ArtWorkDescriptionBinaire(final String type, final String fileName)
	    throws InvalidParameterException {
	super(type);
	if (fileName == null || fileName.compareTo("") == 0) {
	    throw new InvalidParameterException();
	}
	this.m_fileName = fileName;
	if (type == null
		|| (type.compareTo(ArtWorkDescriptionBinaire.TYPE_IMAGE_BMP) != 0
			&& type
				.compareTo(ArtWorkDescriptionBinaire.TYPE_IMAGE_JPEG) != 0 && type
			.compareTo(ArtWorkDescriptionBinaire.TYPE_AUDIO) != 0)) {
	    throw new RuntimeException("Invalid type of binary file.");
	}
    }

    /**
     * @param type
     *                Type of description.
     * @throws InvalidParameterException
     *                 If the type is invalid.
     */
    public ArtWorkDescriptionBinaire(final String type)
	    throws InvalidParameterException {
	super(type);
    }

    /**
     * @return File name where the binary content is.
     */
    public final String getFileName() {
	return this.m_fileName;
    }

    /**
     * @param fileName
     *                File name where the binary content is.
     * @throws InvalidParameterException
     *                 If the filename is invalid or null.
     */
    public final void setFileName(final String fileName)
	    throws InvalidParameterException {
	if (fileName == null || fileName.compareTo("") == 0) {
	    throw new InvalidParameterException();
	}
	this.m_fileName = fileName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.ow2.aspirerfid.museum.pojo.artwork.AbstractArtWorkDescription#equals(java.lang.Object)
     */
    public final boolean equals(final Object obj) {
	boolean equals = false;
	if (super.equals(obj)) {
	    if (obj instanceof ArtWorkDescriptionBinaire) {
		ArtWorkDescriptionBinaire binaire = (ArtWorkDescriptionBinaire) obj;
		if (binaire.getFileName().compareTo(getFileName()) == 0) {
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
	int code = super.hashCode() + getFileName().hashCode();
	return code;
    }
}
