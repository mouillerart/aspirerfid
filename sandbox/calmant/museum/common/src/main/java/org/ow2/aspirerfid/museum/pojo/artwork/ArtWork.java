/*
 *  Copyright (C) Aspire
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.ow2.aspirerfid.museum.pojo.artwork;

import java.util.Vector;

import org.ow2.aspirerfid.museum.pojo.InvalidParameterException;

/**
 * This POJO represents the information about a tag. A tag is usually related to
 * an artwork, but it can be related to a position, such as the entry point.
 * 
 * @author <a href="mailto:angoca@yahoo.com">Andres Gomez</a>
 * @author Thomas Calmant
 */
public class ArtWork {

	/**
	 * XML tag: artwork.
	 */
	public static final String XML_ARTWORK = "artwork";
	/**
	 * XML tag: description.
	 */
	public static final String XML_ARTWORK_DESCRIPTIONS = "descriptions";
	/**
	 * XML tag: id (artwork's id).
	 */
	public static final String XML_ARTWORK_ID = "artwork.id";
	/**
	 * XML tag: uid (tag's id).
	 */
	public static final String XML_ARTWORK_UID = "uid";
	/**
	 * This is the undefined id, when the artwork couldn't be found.
	 */
	public static final String UNDEFINED_ID = "undefined";

	/** Id of the associated questionnaire */
	public static final String XML_ARTWORK_QUESTIONNAIRE = "questionnaire.id";

	/**
	 * Set of descriptions about the artwork.
	 */
	private Vector m_descriptions;
	/**
	 * Artwork's ID.
	 */
	private String m_id;

	/**
	 * Tag's UID.
	 */
	private String m_tid;

	/** Questionnaire ID */
	private String m_questionnaireId;

	/**
	 * @param id
	 *            Artwork's ID.
	 * @param uid
	 *            Tag's UID.
	 * @throws InvalidParameterException
	 *             If the parameter is not valid.
	 */
	public ArtWork(final String id, final String uid)
			throws InvalidParameterException {
		if (id == null || id.compareTo("") == 0) {
			throw new InvalidParameterException();
		}
		this.m_id = id;
		this.m_tid = uid;
		this.m_descriptions = new Vector();
		this.m_questionnaireId = null;
	}

	/**
	 * Adds a description to the set of descriptions of the artwork.
	 * 
	 * @param description
	 *            Description to add.
	 * @throws InvalidParameterException
	 *             if the description is null.
	 */
	public final void addDescription(
			final AbstractArtWorkDescription description)
			throws InvalidParameterException {
		if (description == null) {
			throw new InvalidParameterException();
		}
		this.m_descriptions.addElement(description);
	}

	/**
	 * @return Set of descriptions about the artwork.
	 */
	public final Vector getDescriptions() {
		return this.m_descriptions;
	}

	/**
	 * @return Artworks's Id.
	 */
	public final String getId() {
		return this.m_id;
	}

	/**
	 * @return Tag's UID.
	 */
	public final String getUid() {
		return this.m_tid;
	}

	/**
	 * @param descriptions
	 *            Set of descriptions about the artwork.
	 * @throws InvalidParameterException
	 *             If the set of descriptions is null.
	 */
	public final void setDescriptions(final Vector descriptions)
			throws InvalidParameterException {
		if (descriptions == null) {
			throw new InvalidParameterException();
		}
		this.m_descriptions = descriptions;
	}

	/**
	 * @param id
	 *            Artwork's Id.
	 * @throws InvalidParameterException
	 *             If the id is invalid.
	 */
	public final void setId(final String id) throws InvalidParameterException {
		if (id == null || id.compareTo("") == 0) {
			throw new InvalidParameterException();
		}
		this.m_id = id;
	}

	/**
	 * @param tid
	 *            Tag's UID. This parameter can be empty "".
	 * @throws InvalidParameterException
	 *             If the TID of the artwork is null.
	 */
	public final void setTid(final String tid) throws InvalidParameterException {
		if (tid == null) {
			throw new InvalidParameterException();
		}
		this.m_tid = tid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public final boolean equals(final Object obj) {
		boolean equals = false;
		if (obj instanceof ArtWork) {
			ArtWork artwork = (ArtWork) obj;
			if (artwork.getId().compareTo(getId()) == 0) {
				if (artwork.getUid() == null && getUid() == null) {
					equals = true;
				} else if (artwork.getUid() != null && getUid() != null
						&& artwork.getUid().compareTo(getUid()) == 0) {
					equals = true;
				}
				if (equals) {
					Vector descriptionsObj = artwork.getDescriptions();
					Vector descriptions = getDescriptions();
					if (descriptionsObj.size() == descriptions.size()) {
						equals = true;
						for (int i = 0, length = descriptions.size(); i < length
								&& equals; i++) {
							AbstractArtWorkDescription descriptionObj = (AbstractArtWorkDescription) descriptionsObj
									.elementAt(i);
							AbstractArtWorkDescription description = (AbstractArtWorkDescription) descriptions
									.elementAt(i);
							if (description instanceof ArtWorkDescriptionText
									&& descriptionObj instanceof ArtWorkDescriptionText) {
								ArtWorkDescriptionText text = (ArtWorkDescriptionText) description;
								ArtWorkDescriptionText textObj = (ArtWorkDescriptionText) descriptionObj;
								equals = text.equals(textObj);
							} else if (description instanceof ArtWorkDescriptionBinaire
									&& descriptionObj instanceof ArtWorkDescriptionBinaire) {
								ArtWorkDescriptionBinaire bin = (ArtWorkDescriptionBinaire) description;
								ArtWorkDescriptionBinaire textObj = (ArtWorkDescriptionBinaire) descriptionObj;
								equals = bin.equals(textObj);
							} else {
								equals = false;
							}
						}
					} else {
						equals = false;
					}
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
		if (getUid() != null) {
			code /= getUid().hashCode();
		}
		for (int i = 0, length = getDescriptions().size(); i < length; i++) {
			AbstractArtWorkDescription description = (AbstractArtWorkDescription) getDescriptions()
					.elementAt(i);
			if (description instanceof ArtWorkDescriptionText) {
				ArtWorkDescriptionText text = (ArtWorkDescriptionText) description;
				code += text.hashCode();
			} else if (description instanceof ArtWorkDescriptionBinaire) {
				ArtWorkDescriptionBinaire binaire = (ArtWorkDescriptionBinaire) description;
				code -= binaire.hashCode();
			}
		}
		return code;
	}

	/**
	 * Retrieves the associated questionnaire ID (can be null)
	 * 
	 * @return the associated questionnaire ID
	 */
	public String getQuestionnaireId() {
		return m_questionnaireId;
	}

	/**
	 * Sets the associated questionnaire ID.
	 * 
	 * @param questionnaireId
	 *            Id of the associated questionnaire
	 */
	public void setQuestionnaire(String questionnaireId) {
		this.m_questionnaireId = questionnaireId;
	}
}
