/**
 * 
 */
package org.ow2.aspirerfid.museum.util;

import java.util.Iterator;
import java.util.Vector;

import junit.framework.TestCase;

import org.ow2.aspirerfid.museum.pojo.InvalidParameterException;
import org.ow2.aspirerfid.museum.pojo.Profile;
import org.ow2.aspirerfid.museum.pojo.Visitor;
import org.ow2.aspirerfid.museum.pojo.artwork.ArtWork;
import org.ow2.aspirerfid.museum.pojo.artwork.ArtWorkDescriptionText;
import org.ow2.aspirerfid.museum.pojo.question.AbstractQuestion;
import org.ow2.aspirerfid.museum.pojo.question.Answer;
import org.ow2.aspirerfid.museum.pojo.question.AnswerOption;
import org.ow2.aspirerfid.museum.pojo.question.ExistantQuestionIdException;
import org.ow2.aspirerfid.museum.pojo.question.MultipleAnswer;
import org.ow2.aspirerfid.museum.pojo.question.MultipleOptionMultipleAnswer;
import org.ow2.aspirerfid.museum.pojo.question.MultipleOptionUniqueAnswer;
import org.ow2.aspirerfid.museum.pojo.question.Open;
import org.ow2.aspirerfid.museum.pojo.question.Questionnaire;
import org.ow2.aspirerfid.museum.pojo.question.Rating;
import org.ow2.aspirerfid.museum.pojo.question.UniqueAnswerOption;
import org.ow2.aspirerfid.museum.pojo.question.VisitorsAnswers;
import org.ow2.aspirerfid.museum.util.ObjectsProcessor;

/**
 * @author <a href="mailto:angoca@yahoo.com">Andres Gomez</a>
 */
public class ObjectsProcessorTest extends TestCase {
	/**
	 * Message received from the server.
	 */
	private static String ARTWORK_MESS = "<museum>"
			+ "<visitor.id>1234567890</visitor.id>"
			+ "<artwork><artwork.id>2</artwork.id><uid>04C50A29C90284</uid>"
			+ "<descriptions><description><type>text/plain</type><content>"
			+ "<title>Title</title><message>Mona Lisa</message></content></description>"
			+ "<description><type>text/plain</type><content><title>Author</title>"
			+ "<message>Leonardo Da Vinci</message></content></description>"
			+ "<description><type>text/plain</type><content><title>Date</title>"
			+ "<message>1503 - 1519</message></content></description>"
			+ "<description><type>text/plain</type><content><title>Description</title><message>"
			+ "Mona Lisa (also known as La Gioconda) is a 16th century portrait "
			+ " painted in oil on a poplar panel by Leonardo Da Vinci during "
			+ "the Italian Renaissance. The work is owned by the French "
			+ "government and hangs in the Mus�e du Louvre in Paris, France with "
			+ "the title Portrait of Lisa Gherardini, wife of Francesco del Giocondo."
			+ "</message></content></description></descriptions></artwork>"
			+ "<rating.request><questionnaire><type>rating.request</type>"
			+ "<questionnaire.id>2</questionnaire.id><title>Rating</title>"
			+ "<instructions>Give us your opinion about this artwork</instructions>"
			+ "<questions><question><id>21</id><content>:)</content>"
			+ "<type>rate</type><min>1</min><max>5</max><average>3.5</average>"
			+ "</question></questions></questionnaire><answers><question><id>11</id>"
			+ "<answer><option>18-25</option></answer></question><question><id>12</id>"
			+ "<answer><option>English</option></answer></question><question>"
			+ "<id>13</id></question><question><id>14</id><answer><option>Yes</option>"
			+ "</answer></question></answers></rating.request>"
			+ "<profile.request>"
			+ "<questionnaire><type>profile.request</type>"
			+ "<questionnaire.id>1</questionnaire.id><title>Profile</title>"
			+ "<instructions>Tell us about you</instructions><questions><question>"
			+ "<id>11</id><content>Range of age</content><type>MultOptUniAns</type>"
			+ "<options>"
			+ "<option><name>under 10</name><percentage>5.0</percentage></option>"
			+ "<option><name>11-17</name><percentage>10.0</percentage></option>"
			+ "<option><name>18-25</name><percentage>20.0</percentage></option>"
			+ "<option><name>26-40</name><percentage>25.0</percentage></option>"
			+ "<option><name>41-60</name><percentage>25.0</percentage></option>"
			+ "<option><name>over 60</name><percentage>15.0</percentage></option>"
			+ "</options></question><question><id>12</id>"
			+ "<content>Language</content><type>MultOptUniAns</type>"
			+ "<options>"
			+ "<option><name>English</name><percentage>50.0</percentage></option>"
			+ "<option><name>French</name><percentage>40.0</percentage></option>"
			+ "<option><name>Spanish</name><percentage>10.0</percentage></option>"
			+ "</options></question><question><id>13</id><content>Handicaps</content>"
			+ "<type>MultOptMulAns</type><options><option><name>Blind</name></option>"
			+ "<option><name>Deaf</name></option></options></question>"
			+ "<question><id>14</id><content>Send personal information</content>"
			+ "<type>MultOptUniAns</type><options>"
			+ "<option><name>Yes</name><percentage>60.0</percentage></option>"
			+ "<option><name>No</name><percentage>40.0</percentage></option>"
			+ "</options></question></questions></questionnaire>"
			+ "<answers><question><id>11</id><answer>"
			+ "<option>18-25</option></answer></question>"
			+ "<question><id>12</id><answer>"
			+ "<option>English</option></answer></question>"
			+ "<question><id>13</id></question>"
			+ "<question><id>14</id><answer>"
			+ "<option>Yes</option></answer></question></answers>"
			+ "</profile.request>" + "</museum>";

	/**
	 * Test method for
	 * {@link org.ow2.aspirerfid.museum.midlet.ObjectsProcessor#getQuestionnaireFromMessage(java.lang.String, java.lang.String, org.ow2.aspirerfid.museum.pojo.artwork.ArtWork)}.
	 */
	public void testGetSurveyQuestionnaireFromMessage() {
		try {
			// FIXME ArtWork artwork = new ArtWork("2", null);
			Questionnaire expected = new Questionnaire("3", "Survey",
					"Tell us how was you visit",
					Questionnaire.TYPE_SURVEY_QUESTION);
			// Qualify the museum
			Rating museumRating = new Rating("31", "Quality the museum", 1, 5,
					3.5);
			expected.addQuestion(museumRating);

			// Comments
			Open comments = new Open("32", "Comments");
			expected.addQuestion(comments);
			/* FIXME
			Questionnaire actual = ObjectsProcessor
					.getQuestionnaireFromMessage(ARTWORK_MESS,
							Questionnaire.XML_SURVEY_REQUEST, artwork);
			*/
			// FIXME (old) assertEquals(expected, actual);
		} catch (InvalidParameterException e) {
			fail("Failed because of a parameter");
		} catch (ExistantQuestionIdException e) {
			fail("Failed because of a parameter");
		}
	}

	/**
	 * Test method for
	 * {@link org.ow2.aspirerfid.museum.midlet.ObjectsProcessor#getQuestionnaireFromMessage(java.lang.String, java.lang.String, org.ow2.aspirerfid.museum.pojo.artwork.ArtWork)}.
	 */
	public void testGetRatingQuestionnaireFromMessage() {
		try {
			ArtWork artwork = new ArtWork("2", null);
			Questionnaire expected = new Questionnaire("2", "Rating",
					"Give us your opinion about this artwork",
					Questionnaire.TYPE_ARTWORK_RATING);
			Rating rating = new Rating("21", ":)", 1, 5, 3.5);
			expected.addQuestion(rating);

			Questionnaire actual = ObjectsProcessor
					.getQuestionnaireFromMessage(ARTWORK_MESS,
							Questionnaire.XML_RATING_REQUEST);
			assertEquals(expected, actual);
		} catch (Exception e) {
			fail("Failed because of a parameter");
		}
	}

	/**
	 * Test method for
	 * {@link org.ow2.aspirerfid.museum.midlet.ObjectsProcessor#getQuestionnaireFromMessage(java.lang.String, java.lang.String, org.ow2.aspirerfid.museum.pojo.artwork.ArtWork)}.
	 */
	public void testGetProfileQuestionnaireFromMessage() {
		try {
			ArtWork artwork = new ArtWork("2", null);
			Questionnaire expected = new Questionnaire("1", "Profile",
					"Tell us about you", Questionnaire.TYPE_PROFILE_REQUEST);

			// Range of age
			MultipleOptionUniqueAnswer age = new MultipleOptionUniqueAnswer(
					"11", "Range of age");
			age.addOption(new UniqueAnswerOption("under 10", 5));
			age.addOption(new UniqueAnswerOption("11-17", 10));
			age.addOption(new UniqueAnswerOption("18-25", 20));
			age.addOption(new UniqueAnswerOption("26-40", 25));
			age.addOption(new UniqueAnswerOption("41-60", 25));
			age.addOption(new UniqueAnswerOption("over 60", 15));
			expected.addQuestion(age);

			// Language
			MultipleOptionUniqueAnswer language = new MultipleOptionUniqueAnswer(
					"12", "Language");
			language.addOption(new UniqueAnswerOption("English", 50));
			language.addOption(new UniqueAnswerOption("French", 40));
			language.addOption(new UniqueAnswerOption("Spanish", 10));
			expected.addQuestion(language);

			// Handicaps
			MultipleOptionMultipleAnswer handicaps = new MultipleOptionMultipleAnswer(
					"13", "Handicaps");
			handicaps.addOption(new AnswerOption("Blind"));
			handicaps.addOption(new AnswerOption("Deaf"));
			expected.addQuestion(handicaps);

			// Privacy
			MultipleOptionUniqueAnswer privacy = new MultipleOptionUniqueAnswer(
					"14", "Send personal information");
			privacy.addOption(new UniqueAnswerOption("Yes", 60));
			privacy.addOption(new UniqueAnswerOption("No", 40));
			expected.addQuestion(privacy);

			Questionnaire actual = ObjectsProcessor
					.getQuestionnaireFromMessage(ARTWORK_MESS,
							Questionnaire.XML_PROFILE_REQUEST);

			assertEquals(expected, actual);
		} catch (InvalidParameterException e) {
			fail("Failed because of a parameter");
		} catch (ExistantQuestionIdException e) {
			fail("Failed because of a question id");
		}
	}

	/**
	 * Test method for
	 * {@link org.ow2.aspirerfid.museum.midlet.ObjectsProcessor#getArtworkFromMessage(java.lang.String)}.
	 */
	public void testGetArtworkFromMessage() {
		ArtWork actual = ObjectsProcessor.getArtworkFromMessage(ARTWORK_MESS);
		ArtWork expected;
		try {
			expected = new ArtWork("2", null);
			expected.addDescription(new ArtWorkDescriptionText(
					ArtWorkDescriptionText.TYPE_TEXT, "Title", "Mona Lisa"));
			expected.addDescription(new ArtWorkDescriptionText(
					ArtWorkDescriptionText.TYPE_TEXT, "Author",
					"Leonardo Da Vinci"));
			expected.addDescription(new ArtWorkDescriptionText(
					ArtWorkDescriptionText.TYPE_TEXT, "Date", "1503 - 1519"));
			expected
					.addDescription(new ArtWorkDescriptionText(
							ArtWorkDescriptionText.TYPE_TEXT,
							"Description",
							"Mona Lisa (also known as La Gioconda) is a 16th century portrait  painted in oil on a poplar panel by Leonardo Da Vinci during the Italian Renaissance. The work is owned by the French government and hangs in the Mus�e du Louvre in Paris, France with the title Portrait of Lisa Gherardini, wife of Francesco del Giocondo."));
			assertEquals(expected, actual);
		} catch (InvalidParameterException e) {
			fail("Diferent objects");
		}
	}

	/**
	 * Test method for
	 * {@link org.ow2.aspirerfid.museum.midlet.ObjectsProcessor#buildResponseQuestionnaire(org.ow2.aspirerfid.museum.pojo.question.VisitorsAnswers, org.ow2.aspirerfid.museum.pojo.artwork.ArtWork, java.lang.String)}.
	 */
	public void testBuildResponseQuestionnaire() {
		// fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link org.ow2.aspirerfid.museum.midlet.ObjectsProcessor#getUserFromMessage(java.lang.String)}.
	 */
	public void testGetUserFromMessage() {

		Visitor actual = ObjectsProcessor.getUserFromMessage(ARTWORK_MESS);
		Visitor expected;
		try {
			expected = new Visitor("1234567890", null);
			assertEquals(expected, actual);
		} catch (InvalidParameterException e) {
			fail("Problem creating the expected visitor");
		}
	}

	/**
	 * Test method for
	 * {@link org.ow2.aspirerfid.museum.midlet.ObjectsProcessor#getQuestionnaireAnswersFromMessage(java.lang.String, org.ow2.aspirerfid.museum.pojo.question.Questionnaire, org.ow2.aspirerfid.museum.pojo.Visitor, java.lang.String)}.
	 */
	public void testGetProfileQuestionnaireAnswersFromMessage() {
		try {
			// FIXME ArtWork artwork = new ArtWork("2", null);
			Questionnaire questionnaire = new Questionnaire("1", "Profile",
					"Tell us about you", Questionnaire.TYPE_PROFILE_REQUEST);

			// Range of age
			MultipleOptionUniqueAnswer age = new MultipleOptionUniqueAnswer(
					"11", "Range of age");
			age.addOption(new UniqueAnswerOption("under 10", 5));
			age.addOption(new UniqueAnswerOption("11-17", 10));
			age.addOption(new UniqueAnswerOption("18-25", 20));
			age.addOption(new UniqueAnswerOption("26-40", 25));
			age.addOption(new UniqueAnswerOption("41-60", 25));
			age.addOption(new UniqueAnswerOption("over 60", 15));
			questionnaire.addQuestion(age);

			// Language
			MultipleOptionUniqueAnswer languageOpt = new MultipleOptionUniqueAnswer(
					"12", "Language");
			languageOpt.addOption(new UniqueAnswerOption("English", 50));
			languageOpt.addOption(new UniqueAnswerOption("French", 40));
			languageOpt.addOption(new UniqueAnswerOption("Spanish", 10));
			questionnaire.addQuestion(languageOpt);

			// Handicaps
			MultipleOptionMultipleAnswer handicaps = new MultipleOptionMultipleAnswer(
					"13", "Handicaps");
			handicaps.addOption(new AnswerOption("Blind"));
			handicaps.addOption(new AnswerOption("Deaf"));
			questionnaire.addQuestion(handicaps);

			// Privacy
			MultipleOptionUniqueAnswer privacy = new MultipleOptionUniqueAnswer(
					"14", "Send personal information");
			privacy.addOption(new UniqueAnswerOption("Yes", 60));
			privacy.addOption(new UniqueAnswerOption("No", 40));
			questionnaire.addQuestion(privacy);

			Visitor visitor = new Visitor("1234567890", null);
			Profile profile = new Profile("English", false, false, 3, true);
			visitor.setProfile(profile);

			VisitorsAnswers expected = new VisitorsAnswers(questionnaire,
					visitor);

			// TODO Walter, use the profile to present the profile (in different
			// languages)

			int range = visitor.getProfile().getRangeAge();
			String rangeStr = null;
			rangeStr = assignRange(range);
			String id = "11";
			AbstractQuestion question = getQuestionById(questionnaire, id);
			Answer answer = new Answer(question, rangeStr);
			expected.addAnswer(answer);

			String language = visitor.getProfile().getLanguage();
			id = "12";
			question = getQuestionById(questionnaire, id);
			answer = new Answer(question, language);
			expected.addAnswer(answer);

			id = "13";
			question = getQuestionById(questionnaire, id);
			MultipleAnswer mulAnswer = new MultipleAnswer(
					(MultipleOptionMultipleAnswer) question);
			if (visitor.getProfile().isBlind()) {
				mulAnswer.addAnswer("Blind");
				expected.addAnswer(mulAnswer);
			}
			if (visitor.getProfile().isDeaf()) {
				mulAnswer.addAnswer("Deaf");
				expected.addAnswer(mulAnswer);
			}

			id = "14";
			question = getQuestionById(questionnaire, id);
			if (visitor.getProfile().isPrivacy()) {
				answer = new Answer(question, "Yes");
			} else {
				answer = new Answer(question, "No");
			}
			expected.addAnswer(answer);

			VisitorsAnswers actual = ObjectsProcessor
					.getQuestionnaireAnswersFromMessage(ARTWORK_MESS,
							questionnaire, visitor,
							Questionnaire.XML_PROFILE_REQUEST);

			assertEquals(expected, actual);
		} catch (InvalidParameterException e) {
			fail("Exception");
		} catch (ExistantQuestionIdException e) {
			fail("Exception");
		}
	}

	/**
	 * @param range
	 *            Range to analyze.
	 * @return String of the range.
	 */
	private String assignRange(final int range) {
		String rangeStr;
		switch (range) {
		case 1:
			rangeStr = "under 10";
			break;
		case 2:
			rangeStr = "11-17";
			break;
		case 3:
			rangeStr = "18-25";
			break;
		case 4:
			rangeStr = "26-40";
			break;
		case 5:
			rangeStr = "41-60";
			break;
		case 6:
			rangeStr = "over 60";
			break;
		default:
			rangeStr = "undefined";
			break;
		}
		return rangeStr;
	}

	/**
	 * @param questionnaire
	 *            Questionnaire that has all the questions.
	 * @param id
	 *            Question id.
	 * @return question that matches with the given id.
	 */
	private AbstractQuestion getQuestionById(final Questionnaire questionnaire,
			final String id) {
		Vector questions = questionnaire.getQuestions();
		boolean found = false;
		AbstractQuestion retQuestion = null;
		for (Iterator iterator = questions.iterator(); iterator.hasNext()
				&& !found;) {
			AbstractQuestion question = (AbstractQuestion) iterator.next();
			if (question.getId().compareTo(id) == 0) {
				found = true;
				retQuestion = question;
			}
		}
		return retQuestion;
	}
}
