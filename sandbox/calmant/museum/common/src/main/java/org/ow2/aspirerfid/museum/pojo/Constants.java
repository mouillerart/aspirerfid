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
package org.ow2.aspirerfid.museum.pojo;

/**
 * Various constants shared around the Museum elements
 * 
 * @author Thomas Calmant
 */
public interface Constants {
	/** Communication default answer */
	public static final String COMMUNICATION_ACK = "<ok />";
	
	/** Answer constants */
	public static final String ANSWER_YES = "Yes";
	public static final String ANSWER_NO = "No";
	public static final String ANSWER_BLIND = "Blind";
	public static final String ANSWER_DEAF = "Deaf";
	
	/** Constants of default Profile questionnaire */
	public static final String QUESTIONNAIRE_PROFILE_ID = "default.profile"; // Old 1
	public static final String PROFILE_AGE_ID = "age"; // Old 11
	public static final String PROFILE_LANGUAGE_ID = "lang"; // Old 12
	public static final String PROFILE_HANDICAP_ID = "handicap"; // Old 13
	public static final String PROFILE_PRIVACY_ID = "privacy"; // Old 14
	
	/** Constant of default Rating questionnaire */
	public static final String QUESTIONNAIRE_RATING_ID = "default.rating"; // Old 2
	public static final String RATING_RATING_ID = "rating"; // Old 21

	/** Constant of default Survey questionnaire */
	public static final String QUESTIONNAIRE_SURVEY_ID = "default.survey"; // Old 3
	public static final String SURVEY_RATING_ID = "rating"; // Old 31
	public static final String SURVEY_COMMENTS_ID = "comments"; // Old 32
}
