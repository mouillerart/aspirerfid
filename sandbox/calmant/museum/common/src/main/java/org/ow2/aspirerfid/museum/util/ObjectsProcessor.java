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
package org.ow2.aspirerfid.museum.util;

import java.util.Enumeration;

import org.ow2.aspirerfid.museum.pojo.InvalidParameterException;
import org.ow2.aspirerfid.museum.pojo.Visitor;
import org.ow2.aspirerfid.museum.pojo.artwork.AbstractArtWorkDescription;
import org.ow2.aspirerfid.museum.pojo.artwork.ArtWork;
import org.ow2.aspirerfid.museum.pojo.artwork.ArtWorkDescriptionBinaire;
import org.ow2.aspirerfid.museum.pojo.artwork.ArtWorkDescriptionText;
import org.ow2.aspirerfid.museum.pojo.question.AbstractQuestion;
import org.ow2.aspirerfid.museum.pojo.question.Answer;
import org.ow2.aspirerfid.museum.pojo.question.AnswerOption;
import org.ow2.aspirerfid.museum.pojo.question.MultipleAnswer;
import org.ow2.aspirerfid.museum.pojo.question.MultipleOptionMultipleAnswer;
import org.ow2.aspirerfid.museum.pojo.question.MultipleOptionUniqueAnswer;
import org.ow2.aspirerfid.museum.pojo.question.Open;
import org.ow2.aspirerfid.museum.pojo.question.Questionnaire;
import org.ow2.aspirerfid.museum.pojo.question.Rating;
import org.ow2.aspirerfid.museum.pojo.question.UniqueAnswerOption;
import org.ow2.aspirerfid.museum.pojo.question.VisitorsAnswers;

import com.exploringxml.xml.Node;
import com.exploringxml.xml.Xparse;

/**
 * This objects process the received XML message, and creates the objects that
 * are described in the String.
 * 
 * @author <a href="mailto:angoca@yahoo.com">Andres Gomez</a>
 * @author Thomas Calmant
 */
public final class ObjectsProcessor {
	/**
	 * Default constructor.
	 */
	private ObjectsProcessor() {
		// Nothing.
	}

	public static Questionnaire getQuestionnaireFromMessage(
			final String respMessage, final String title) {
		return getQuestionnaireFromMessage(respMessage, title, 1);
	}

	public static Questionnaire getQuestionnaireFromMessage(
			final String respMessage, final int occurence) {
		return getQuestionnaireFromMessage(respMessage, null, occurence);
	}

	/**
	 * Returns a questionnaire from the message.
	 * 
	 * @param respMessage
	 *            XML to analyze.
	 * @param title
	 *            questionnaire's title.
	 * @param artwork
	 *            Related artwork.
	 * @return Questionnaire
	 */
	private static Questionnaire getQuestionnaireFromMessage(
			final String respMessage, final String title, final int occurence) {
		
		Questionnaire questionnaire = null;
		try {
			int[] occur = { 1, 1, 1, 1, 1, 1 };

			// Nodes of the XML
			Node root = new Xparse().parse(respMessage);

			// Read the XML.
			Node questionnaireNode = null;

			if (title != null) {
				questionnaireNode = root.find(
						PhoneMessagesXmlTags.XML_MUSEUM_MESSAGE + "/" + title,
						new int[] { 1, 1 });
			} else if (occurence > 0) {
				questionnaireNode = root.find(
						PhoneMessagesXmlTags.XML_MUSEUM_MESSAGE + "/"
								+ Questionnaire.XML_QUESTIONNAIRE, new int[] {
								1, occurence });
			}

			if (questionnaireNode != null) {
				// Questionnaire id
				Node questionnaireId = questionnaireNode.find(
						Questionnaire.XML_QUESTIONNAIRE + "/"
								+ Questionnaire.XML_QUESTIONNAIRE_ID, occur);
				Node questionnaireType = questionnaireNode.find(
						Questionnaire.XML_QUESTIONNAIRE + "/"
								+ Questionnaire.XML_QUESTIONNAIRE_TYPE, occur);
				Node questionnaireTitle = questionnaireNode.find(
						Questionnaire.XML_QUESTIONNAIRE + "/"
								+ Questionnaire.XML_QUESTIONNAIRE_TITLE, occur);
				Node questionnaireInstructions = questionnaireNode.find(
						Questionnaire.XML_QUESTIONNAIRE + "/"
								+ Questionnaire.XML_QUESTIONNAIRE_INSTRUCTIONS,
						occur);
				questionnaire = new Questionnaire(
						questionnaireId.getCharacters(),
						questionnaireTitle.getCharacters(),
						questionnaireInstructions.getCharacters(),
						questionnaireType.getCharacters());

				int j = 1;
				boolean cont = true;
				// Searches all the questions.
				while (cont) {
					occur[2] = j;
					j++;
					// Analyze one question.
					if (questionnaireNode.find(Questionnaire.XML_QUESTIONNAIRE
							+ "/" + Questionnaire.XML_QUESTIONS + "/"
							+ AbstractQuestion.XML_QUESTION, occur) != null) {
						Node questionId = questionnaireNode.find(
								Questionnaire.XML_QUESTIONNAIRE + "/"
										+ Questionnaire.XML_QUESTIONS + "/"
										+ AbstractQuestion.XML_QUESTION + "/"
										+ AbstractQuestion.XML_QUESTION_ID,
								occur);
						Node content = questionnaireNode
								.find(Questionnaire.XML_QUESTIONNAIRE + "/"
										+ Questionnaire.XML_QUESTIONS + "/"
										+ AbstractQuestion.XML_QUESTION + "/"
										+ AbstractQuestion.XML_QUESTION_CONTENT,
										occur);
						Node questionType = questionnaireNode.find(
								Questionnaire.XML_QUESTIONNAIRE + "/"
										+ Questionnaire.XML_QUESTIONS + "/"
										+ AbstractQuestion.XML_QUESTION + "/"
										+ AbstractQuestion.XML_QUESTION_TYPE,
								occur);
						String quesType = questionType.getCharacters();
						processQuestion(questionnaire, occur,
								questionnaireNode, questionId, content,
								quesType);
					} else {
						cont = false;
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(
					"Error ObjProcc1 getting questionnaire from message: "
							+ e.getMessage());
		}
		return questionnaire;
	}

	/**
	 * @param questionnaire
	 *            questionnaire to return.
	 * @param occur
	 *            occur for the xML message.
	 * @param questionnaireNode
	 *            questionnaire node.
	 * @param questionId
	 *            Question's Id.
	 * @param content
	 *            Content of the question.
	 * @param quesType
	 *            Type of question.
	 */
	private static void processQuestion(final Questionnaire questionnaire,
			final int[] occur, final Node questionnaireNode,
			final Node questionId, final Node content, final String quesType) {
		try {
			// This is an open question.
			if (quesType.compareTo(Open.QUESTION_TYPE) == 0) {
				questionnaire.addQuestion(new Open(questionId.getCharacters(),
						content.getCharacters()));
			} else if (quesType.compareTo(Rating.QUESTION_TYPE) == 0) {
				// This is a rating question.
				processRatingQuestion(questionnaire, occur, questionnaireNode,
						questionId, content);
			} else if (quesType
					.compareTo(MultipleOptionMultipleAnswer.QUESTION_TYPE) == 0) {
				MultipleOptionMultipleAnswer multQuestion = new MultipleOptionMultipleAnswer(
						questionId.getCharacters(), content.getCharacters());
				processMomaQuestion(occur, questionnaireNode, multQuestion);
				// This is a multiple option multiple answer
				// question.
				questionnaire.addQuestion(multQuestion);
			} else if (quesType
					.compareTo(MultipleOptionUniqueAnswer.QUESTION_TYPE_UNI_ANS) == 0) {
				MultipleOptionUniqueAnswer multQuestion = new MultipleOptionUniqueAnswer(
						questionId.getCharacters(), content.getCharacters());
				processMouaQuestion(occur, questionnaireNode, multQuestion);
				// This is a multiple option unique answer question.
				questionnaire.addQuestion(multQuestion);
			}
		} catch (Exception e) {
			throw new RuntimeException("Error ObjProcc2 processing question: "
					+ e.getMessage());
		}

	}

	/**
	 * @param occur
	 *            Occur for the XML message.
	 * @param questionnaireNode
	 *            Node that has the question
	 * @param multQuestion
	 *            Question to analyze.
	 */
	private static void processMouaQuestion(int[] occur,
			Node questionnaireNode, MultipleOptionUniqueAnswer multQuestion) {
		try {
			int k = 1;
			boolean cont2 = true;
			// Searches all the questions.
			while (cont2) {
				occur[4] = k;
				k++;
				// Analyze one option
				if (questionnaireNode.find(Questionnaire.XML_QUESTIONNAIRE
						+ "/" + Questionnaire.XML_QUESTIONS + "/"
						+ AbstractQuestion.XML_QUESTION + "/"
						+ MultipleOptionMultipleAnswer.XML_OPTIONS + "/"
						+ AnswerOption.XML_OPTION, occur) != null) {
					Node name = questionnaireNode.find(
							Questionnaire.XML_QUESTIONNAIRE + "/"
									+ Questionnaire.XML_QUESTIONS + "/"
									+ AbstractQuestion.XML_QUESTION + "/"
									+ MultipleOptionMultipleAnswer.XML_OPTIONS
									+ "/" + AnswerOption.XML_OPTION + "/"
									+ AnswerOption.XML_NAME, occur);
					Node percentage = questionnaireNode.find(
							Questionnaire.XML_QUESTIONNAIRE + "/"
									+ Questionnaire.XML_QUESTIONS + "/"
									+ AbstractQuestion.XML_QUESTION + "/"
									+ MultipleOptionMultipleAnswer.XML_OPTIONS
									+ "/" + AnswerOption.XML_OPTION + "/"
									+ UniqueAnswerOption.XML_PERCENTAGE, occur);
					multQuestion.addOption(new UniqueAnswerOption(name
							.getCharacters(), Double.parseDouble(percentage
							.getCharacters())));
				} else {
					cont2 = false;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(
					"Error ObjProcc3 proccessing moua question from message: "
							+ e.getMessage());
		}

	}

	/**
	 * @param occur
	 *            Occur for the XML message.
	 * @param questionnaireNode
	 *            Node that has the question.
	 * @param multQuestion
	 *            Question to analyze.
	 */
	private static void processMomaQuestion(int[] occur,
			Node questionnaireNode, MultipleOptionMultipleAnswer multQuestion) {
		try {
			int k = 1;
			boolean cont2 = true;
			// Searches all the questions.
			while (cont2) {
				occur[4] = k;
				k++;
				// Analyze one option
				if (questionnaireNode.find(Questionnaire.XML_QUESTIONNAIRE
						+ "/" + Questionnaire.XML_QUESTIONS + "/"
						+ AbstractQuestion.XML_QUESTION + "/"
						+ MultipleOptionMultipleAnswer.XML_OPTIONS + "/"
						+ AnswerOption.XML_OPTION, occur) != null) {
					Node name = questionnaireNode.find(
							Questionnaire.XML_QUESTIONNAIRE + "/"
									+ Questionnaire.XML_QUESTIONS + "/"
									+ AbstractQuestion.XML_QUESTION + "/"
									+ MultipleOptionMultipleAnswer.XML_OPTIONS
									+ "/" + AnswerOption.XML_OPTION + "/"
									+ AnswerOption.XML_NAME, occur);
					multQuestion.addOption(new AnswerOption(name
							.getCharacters()));
				} else {
					cont2 = false;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(
					"Error ObjProcc4 processing moma question from message: "
							+ e.getMessage());
		}
	}

	/**
	 * @param questionnaire
	 *            Questionnaire to fill.
	 * @param occur
	 *            Occur for the XML message.
	 * @param questionnaireNode
	 *            Node that has the questionnaire.
	 * @param questionId
	 *            Question's Id.
	 * @param content
	 *            Content of the question.
	 */
	private static void processRatingQuestion(Questionnaire questionnaire,
			int[] occur, Node questionnaireNode, Node questionId, Node content) {
		try {
			Node min = questionnaireNode.find(Questionnaire.XML_QUESTIONNAIRE
					+ "/" + Questionnaire.XML_QUESTIONS + "/"
					+ AbstractQuestion.XML_QUESTION + "/"
					+ Rating.XML_QUESTION_MIN, occur);
			Node max = questionnaireNode.find(Questionnaire.XML_QUESTIONNAIRE
					+ "/" + Questionnaire.XML_QUESTIONS + "/"
					+ AbstractQuestion.XML_QUESTION + "/"
					+ Rating.XML_QUESTION_MAX, occur);
			Node average = questionnaireNode.find(
					Questionnaire.XML_QUESTIONNAIRE + "/"
							+ Questionnaire.XML_QUESTIONS + "/"
							+ AbstractQuestion.XML_QUESTION + "/"
							+ Rating.XML_QUESTION_AVERAGE, occur);
			questionnaire.addQuestion(new Rating(questionId.getCharacters(),
					content.getCharacters(), Integer.parseInt(min
							.getCharacters()), Integer.parseInt(max
							.getCharacters()), Double.parseDouble(average
							.getCharacters())));
		} catch (Exception e) {
			throw new RuntimeException(
					"Error ObjProcc5 processing rating question from message: "
							+ e.getMessage());
		}

	}

	public static ArtWork getArtworkFromMessage(final String respMessage) {
		return getArtworkFromMessage(respMessage, 1);
	}

	/**
	 * Returns an artwork from the message.
	 * 
	 * @param respMessage
	 *            XML to analyze.
	 * @return Artwork.
	 */
	public static ArtWork getArtworkFromMessage(final String respMessage,
			int occurence) {
		ArtWork artwork = null;

		try {
			int[] occur = { 1, 1, 1, 1 };

			// Nodes of the XML
			Node root = new Xparse().parse(respMessage);

			// Read the xml.
			Node artworkNode = root.find(
					PhoneMessagesXmlTags.XML_MUSEUM_MESSAGE + "/"
							+ ArtWork.XML_ARTWORK, new int[] { 1, occurence });
			try {
				if (artworkNode != null) {
					// Artwork id
					Node artworkId = artworkNode.find(ArtWork.XML_ARTWORK_ID,
							occur);

					artwork = new ArtWork(artworkId.getCharacters(), null);

					// Searches for associated questionnaire
					Node questionnaireNode = artworkNode.find(
							ArtWork.XML_ARTWORK_QUESTIONNAIRE, new int[] { 1 });
					if (questionnaireNode != null) {
						artwork.setQuestionnaire(questionnaireNode
								.getCharacters());
					}

					// TODO Andres validar si el artwork no tiene descriptciones
					int j = 1;
					boolean cont = true;
					// Searches all the descriptions.
					while (cont) {
						try {
							occur[1] = j;
							// Analyze one description.
							if (artworkNode
									.find(ArtWork.XML_ARTWORK_DESCRIPTIONS
											+ "/"
											+ AbstractArtWorkDescription.XML_ARWORK_DESCRIPTION,
											occur) != null) {
								try {
									Node typeDescription = artworkNode
											.find(ArtWork.XML_ARTWORK_DESCRIPTIONS
													+ "/"
													+ AbstractArtWorkDescription.XML_ARWORK_DESCRIPTION
													+ "/"
													+ AbstractArtWorkDescription.XML_ARTWORK_INF_TYPE,
													occur);
									// The description is a text.
									if (typeDescription
											.getCharacters()
											.compareTo(
													ArtWorkDescriptionText.TYPE_TEXT) == 0) {
										try {
											Node titleDescription = artworkNode
													.find(ArtWork.XML_ARTWORK_DESCRIPTIONS
															+ "/"
															+ AbstractArtWorkDescription.XML_ARWORK_DESCRIPTION
															+ "/"
															+ AbstractArtWorkDescription.XML_ARTWORK_INF_CONTENT
															+ "/"
															+ ArtWorkDescriptionText.XML_TEXT_TITLE,
															occur);
											Node contentDescription = artworkNode
													.find(ArtWork.XML_ARTWORK_DESCRIPTIONS
															+ "/"
															+ AbstractArtWorkDescription.XML_ARWORK_DESCRIPTION
															+ "/"
															+ AbstractArtWorkDescription.XML_ARTWORK_INF_CONTENT
															+ "/"
															+ ArtWorkDescriptionText.XML_MESSAGE,
															occur);
											artwork.addDescription(new ArtWorkDescriptionText(
													typeDescription
															.getCharacters(),
													titleDescription
															.getCharacters(),
													contentDescription
															.getCharacters()));
										} catch (Exception e) {
											throw new RuntimeException("flag4");
										}
									} else if ((typeDescription
											.getCharacters()
											.compareTo(
													ArtWorkDescriptionBinaire.TYPE_AUDIO) == 0)
											|| (typeDescription
													.getCharacters()
													.compareTo(
															ArtWorkDescriptionBinaire.TYPE_IMAGE_BMP) == 0)
											|| (typeDescription
													.getCharacters()
													.compareTo(
															ArtWorkDescriptionBinaire.TYPE_IMAGE_JPEG) == 0)) {
										try {
											// The description is an image or
											// video.
											Node contentDescription = artworkNode
													.find(ArtWork.XML_ARTWORK_DESCRIPTIONS
															+ "/"
															+ AbstractArtWorkDescription.XML_ARWORK_DESCRIPTION
															+ "/"
															+ AbstractArtWorkDescription.XML_ARTWORK_INF_CONTENT
															+ "/"
															+ ArtWorkDescriptionBinaire.XML_FILE,
															occur);
											ArtWorkDescriptionBinaire bin = new ArtWorkDescriptionBinaire(
													typeDescription
															.getCharacters());
											bin.setFileName(contentDescription
													.getCharacters());
											artwork.addDescription(bin);
										} catch (Exception e) {
											throw new RuntimeException("flag4"
													+ e.getMessage());
										}
									}
								} catch (Exception e) {
									throw new RuntimeException("flag3"
											+ e.getMessage());
								}
							} else {
								cont = false;
							}
						} catch (Exception e) {
							throw new RuntimeException("flag2" + e.getMessage());
						}
						j++;
					}
				}
			} catch (Exception e) {
				throw new RuntimeException("flag1" + e.getMessage());
			}
		} catch (Exception e) {
			throw new RuntimeException(
					"Error ObjProcc6 getting artwork from message: "
							+ e.getMessage());
		}
		return artwork;
	}

	/**
	 * @param answers
	 *            Questionnaire answers.
	 * @param artwork
	 *            Artwork currently presented.
	 * @param phoneId
	 *            Message that can identify the phone.
	 * @return String that represents the answers.
	 */
	public static String buildResponseQuestionnaire(
			final VisitorsAnswers answers, final ArtWork artwork,
			final String phoneId) {
		String message = "";
		try {
			if (answers != null) {
				String type = answers.getQuestionnaire().getQuestionnaireType();
				if (type.compareTo(Questionnaire.TYPE_ARTWORK_RATING) == 0) {
					type = VisitorsAnswers.XML_RATING_RESPONSE;
				} else if (type.compareTo(Questionnaire.TYPE_PROFILE_REQUEST) == 0) {
					type = VisitorsAnswers.XML_PROFILE_RESPONSE;
				} else if (type.compareTo(Questionnaire.TYPE_SURVEY_QUESTION) == 0) {
					type = VisitorsAnswers.XML_SURVEY_RESPONSE;
				}

				message += "<" + PhoneMessagesXmlTags.XML_MUSEUM_MESSAGE + ">";
				message += "<" + ArtWork.XML_ARTWORK_ID + ">" + artwork.getId()
						+ "</" + ArtWork.XML_ARTWORK_ID + ">";
				// Phone's ID
				message += phoneId;

				message += "<" + type + "><" + VisitorsAnswers.XML_ANSWERS
						+ ">";
				Enumeration elems = answers.getAnswers().elements();
				while (elems.hasMoreElements()) {
					Answer answer = (Answer) elems.nextElement();
					message += "<" + AbstractQuestion.XML_QUESTION + ">";
					message += "<" + AbstractQuestion.XML_QUESTION_ID + ">"
							+ answer.getQuestion().getId() + "</"
							+ AbstractQuestion.XML_QUESTION_ID + ">";
					if (answer instanceof MultipleAnswer) {
						MultipleAnswer multAnswer = (MultipleAnswer) answer;
						if (multAnswer.getAnswers().size() > 0) {
							message += "<" + Answer.XML_ANSWER + ">";
							Enumeration elems2 = multAnswer.getAnswers()
									.elements();
							while (elems2.hasMoreElements()) {
								message += "<" + Answer.XML_OPTION + ">"
										+ (String) elems2.nextElement() + "</"
										+ Answer.XML_OPTION + ">";
							}
							message += "</" + Answer.XML_ANSWER + ">";
						}
					} else {
						message += "<" + Answer.XML_ANSWER + ">";
						message += "<" + Answer.XML_OPTION + ">"
								+ answer.getAnswer() + "</" + Answer.XML_OPTION
								+ ">";
						message += "</" + Answer.XML_ANSWER + ">";
					}
					message += "</" + AbstractQuestion.XML_QUESTION + ">";
				}
				message += "</" + VisitorsAnswers.XML_ANSWERS + "></" + type
						+ "></" + PhoneMessagesXmlTags.XML_MUSEUM_MESSAGE + ">";
			}
		} catch (Exception e) {
			throw new RuntimeException(
					"Error ObjProcc7 setting answers in message: "
							+ e.getMessage());
		}
		return message;
	}

	/**
	 * @param respMessage
	 *            Message to analyze
	 * @return Visitor
	 */
	public static Visitor getUserFromMessage(final String respMessage) {
		Visitor visitor = null;
		// TODO Andres validar si el mensaje es null
		try {
			int[] occur = { 1, 1 };

			// Node of the XML
			Node root = new Xparse().parse(respMessage);

			// Read the xml.
			Node visitorIdNode = root.find(
					PhoneMessagesXmlTags.XML_MUSEUM_MESSAGE + "/"
							+ Visitor.XML_VISITOR_ID, occur);
			if (visitorIdNode != null) {
				visitor = new Visitor(visitorIdNode.getCharacters(), null);
			}
		} catch (Exception e) {
			throw new RuntimeException(
					"Error ObjProcc8 getting visitor from message: "
							+ e.getMessage());
		}
		return visitor;
	}

	/**
	 * TODO create test suite.
	 * 
	 * @param args
	 */
	public static void main(final String[] args) {
		String artworkMess = "<museum>"
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
				+ "<option><name>&lt;10</name><percentage>5.0</percentage></option>"
				+ "<option><name>11-17</name><percentage>10.0</percentage></option>"
				+ "<option><name>18-25</name><percentage>20.0</percentage></option>"
				+ "<option><name>26-40</name><percentage>25.0</percentage></option>"
				+ "<option><name>41-60</name><percentage>25.0</percentage></option>"
				+ "<option><name>61&lt;</name><percentage>15.0</percentage></option>"
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
		Visitor visitor = getUserFromMessage(artworkMess);
		ArtWork artwork = getArtworkFromMessage(artworkMess);
		Questionnaire profile = getQuestionnaireFromMessage(artworkMess,
				Questionnaire.XML_PROFILE_REQUEST);
		getQuestionnaireFromMessage(artworkMess,
				Questionnaire.XML_RATING_REQUEST);
		try {
			VisitorsAnswers answers = getQuestionnaireAnswersFromMessage(
					artworkMess, profile, visitor,
					Questionnaire.XML_PROFILE_REQUEST);
			System.out
					.println(buildResponseQuestionnaire(answers, artwork, ""));
		} catch (InvalidParameterException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param respMessage
	 *            Message where are the answers.
	 * @param profileRequest
	 *            Questionnaire.
	 * @param visitor
	 *            visitor to filled the questionnaire.
	 * @param title
	 *            Title of the questionnaire.
	 * @return the answers
	 * @throws InvalidParameterException
	 *             If some parameter is invalid.
	 */
	public static VisitorsAnswers getQuestionnaireAnswersFromMessage(
			final String respMessage, final Questionnaire profileRequest,
			final Visitor visitor, final String title)
			throws InvalidParameterException {
		VisitorsAnswers answers = null;
		try {
			answers = new VisitorsAnswers(profileRequest, visitor);
			Node rootNode = new Xparse().parse(respMessage);
			Node answersNode = rootNode.find(
					PhoneMessagesXmlTags.XML_MUSEUM_MESSAGE + "/" + title,
					new int[] { 1, 1 });

			int[] occur = { 1, 1, 1, 1 };

			int j = 1;
			boolean cont = true;
			// Searches all the questions.
			while (cont) {
				occur[1] = j;
				if (answersNode.find(VisitorsAnswers.XML_ANSWERS + "/"
						+ AbstractQuestion.XML_QUESTION, occur) != null) {

					// question id.
					Node idNode = answersNode.find(VisitorsAnswers.XML_ANSWERS
							+ "/" + AbstractQuestion.XML_QUESTION + "/"
							+ AbstractQuestion.XML_QUESTION_ID, occur);
					String id = idNode.getCharacters();

					// Retrieve the question.
					AbstractQuestion question = profileRequest
							.getQuestionById(id);
					if (question == null) {
						throw new RuntimeException(
								"Error in the received message, the question does not exists");
					}

					// the question has multiples answers.
					if (!(question instanceof MultipleOptionMultipleAnswer)
							|| question instanceof MultipleOptionUniqueAnswer) {
						processMouaAnswer(answers, answersNode, occur, question);
					} else {
						processMomaAnswer(answersNode, occur, question);
					}
				} else {
					cont = false;
				}
				j++;
			}
		} catch (InvalidParameterException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(
					"Error ObjProcc9 getting profile responses "
							+ e.getMessage());
		}
		return answers;
	}

	/**
	 * @param answers
	 *            Set of answers.
	 * @param answersNode
	 *            Node of the answer.
	 * @param occur
	 *            Occur for the XML message.
	 * @param question
	 *            Question to analyze
	 * @throws InvalidParameterException
	 *             If some parameter is invalid.
	 */
	private static void processMouaAnswer(final VisitorsAnswers answers,
			final Node answersNode, final int[] occur,
			final AbstractQuestion question) throws InvalidParameterException {
		try {
			// the question has just one answer.
			Node answerNode = answersNode.find(VisitorsAnswers.XML_ANSWERS
					+ "/" + AbstractQuestion.XML_QUESTION + "/"
					+ Answer.XML_ANSWER + "/" + Answer.XML_OPTION, occur);
			Answer answer = new Answer(question, answerNode.getCharacters());
			answers.addAnswer(answer);
		} catch (Exception e) {
			throw new RuntimeException(
					"Error ObjProcc10 processing moua answer: "
							+ e.getMessage());
		}

	}

	/**
	 * @param answersNode
	 *            Node of the answer.
	 * @param occur
	 *            Occur for the XML message.
	 * @param question
	 *            Question to analyze
	 * @throws InvalidParameterException
	 *             If some parameter is invalid.
	 */
	private static void processMomaAnswer(final Node answersNode,
			final int[] occur, final AbstractQuestion question)
			throws InvalidParameterException {
		try {
			if (answersNode.find(VisitorsAnswers.XML_ANSWERS + "/"
					+ AbstractQuestion.XML_QUESTION + "/" + Answer.XML_ANSWER,
					occur) != null) {
				MultipleAnswer multAnswer = new MultipleAnswer(
						(MultipleOptionMultipleAnswer) question);
				int k = 1;
				boolean cont2 = true;
				// Searches all the options.
				while (cont2) {
					occur[3] = k;
					Node answerNode = answersNode.find(
							VisitorsAnswers.XML_ANSWERS + "/"
									+ AbstractQuestion.XML_QUESTION + "/"
									+ Answer.XML_ANSWER + "/"
									+ Answer.XML_OPTION, occur);
					if (answerNode != null) {
						multAnswer.addAnswer(answerNode.getCharacters());
					} else {
						cont2 = false;
					}
					k++;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(
					"Error ObjProcc11 processing moma answer: "
							+ e.getMessage());
		}

	}
}
