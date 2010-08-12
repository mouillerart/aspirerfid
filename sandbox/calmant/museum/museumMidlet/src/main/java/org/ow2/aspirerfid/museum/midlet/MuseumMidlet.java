package org.ow2.aspirerfid.museum.midlet;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.contactless.TargetProperties;
import javax.microedition.midlet.MIDletStateChangeException;

import org.ow2.aspirerfid.museum.midlet.ui.MenuScreen;
import org.ow2.aspirerfid.museum.midlet.ui.PresentationScreen;
import org.ow2.aspirerfid.museum.midlet.ui.QuestionnaireScreen;
import org.ow2.aspirerfid.museum.midlet.ui.ShowScreen;
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
import org.ow2.aspirerfid.museum.util.ObjectsProcessor;
import org.ow2.aspirerfid.nfc.midlet.generic.GenericMidlet;
import org.ow2.aspirerfid.nfc.midlet.generic.NFCMidletException;
import org.ow2.aspirerfid.nfc.midlet.generic.ReaderThread;
import org.ow2.aspirerfid.nfc.midlet.generic.RequestMessage;
import org.ow2.aspirerfid.nfc.midlet.generic.ui.AlertScreen;
import org.ow2.aspirerfid.nfc.midlet.generic.ui.Screen;
import org.ow2.aspirerfid.nfc.midlet.reader.TagDetector;
import org.ow2.aspirerfid.nfc.midlet.reader.rfid.RFIDDetector;
import org.ow2.aspirerfid.nfc.midlet.sendersReceivers.bluetooth.BluetoothControlerUser;
import org.ow2.aspirerfid.nfc.midlet.sendersReceivers.bluetooth.BluetoothController;
import org.ow2.aspirerfid.nfc.midlet.sendersReceivers.bluetooth.ui.UIToBluetoothInterface;

/**
 * This midlet is the controller of the Museum application. This midlet uses the
 * BluetoothController to manage the communication. Also, it uses the RFID
 * reader to detect and read the tags, to create a message to send to the
 * server. The message is build in XML format thanks to a given specialized
 * message builder.
 * TODO use push registry to activates the application when
 * detecting any type of tag.
 * 
 * @author <a href="mailto:angoca@yahoo.com">Andres Gomez</a>
 */
public class MuseumMidlet extends GenericMidlet implements TagDetector,
		BluetoothControlerUser {
	/**
	 * Bluetooth controller.
	 */
	private BluetoothController m_bluecontroller = null;
	/**
	 * Privacy activated.
	 */
	private boolean m_privacyActive = true;
	/**
	 * History of touched tags screen.
	 */
	private ShowScreen m_showScreen = null;
	/**
	 * Visitor.
	 */
	private Visitor m_visitor;
	/**
	 * Questionnaire: rating or survey.
	 */
	private Questionnaire m_questionnaire;
	/**
	 * Artwork currently presented.
	 */
	private ArtWork m_artwork;
	/**
	 * Profile request.
	 */
	private Questionnaire m_profileRequest;
	/**
	 * Message received from the detector (RFID).
	 */
	private MuseumRequestMessage m_messageReceived;

	/**
	 * Activates the method selectService.
	 */
	public synchronized void continueSelectService() {
		// Activates the selectService method to permit it to return the
		// selected id.
		this.notify();
	}

	/**
	 * The UI sends the user answer's and this method creates a messages this
	 * the response to finally send them to the server.
	 * 
	 * @param type
	 *            Type of questionnaire: profile or the other (survey or rating)
	 * @param hashtable
	 *            Set of answers (question id, responses).
	 */
	public void filledQuestionnaire(String type, Hashtable hashtable) {
		try {
			VisitorsAnswers answers;
			// TODO modify the privacy setting set by the user.
			Questionnaire questionnaire = this.m_profileRequest;

			if (this.m_questionnaire != null
					&& type.compareTo(this.m_questionnaire
							.getQuestionnaireType()) == 0) {
				questionnaire = this.m_questionnaire;
			}
			answers = new VisitorsAnswers(questionnaire, this.m_visitor);

			Enumeration ids = hashtable.keys();
			Enumeration answersHash = hashtable.elements();
			while (ids.hasMoreElements()) {
				String id = (String) ids.nextElement();
				Object answerHash = answersHash.nextElement();
				AbstractQuestion question = questionnaire.getQuestionById(id);
				Answer answer = null;
				// The question is open.
				if (question instanceof Open) {
					Open openQuestion = (Open) question;
					answer = new Answer(openQuestion, (String) answerHash);
				} else if (question instanceof MultipleOptionUniqueAnswer) {
					// The question is multiple option
					// unique answer
					MultipleOptionUniqueAnswer moua = (MultipleOptionUniqueAnswer) question;
					int option = ((Integer) ((Vector) answerHash).elementAt(0))
							.intValue();
					String name = ((AnswerOption) moua.getOptions().elementAt(
							option)).getName();
					answer = new Answer(moua, name);
				} else if (question instanceof MultipleOptionMultipleAnswer) {
					// the question is multiple option
					// multiple answer
					MultipleOptionMultipleAnswer moma = (MultipleOptionMultipleAnswer) question;
					answer = new MultipleAnswer(moma);
					Vector positions = (Vector) answerHash;
					for (int i = 0, length = positions.size(); i < length; i++) {
						int option = ((Integer) positions.elementAt(i))
								.intValue();
						String name = ((AnswerOption) moma.getOptions()
								.elementAt(option)).getName();
						((MultipleAnswer) answer).addAnswer(name);
					}
				} else if (question instanceof Rating) {
					// the question is a rating.
					Rating rating = (Rating) question;
					int option = ((Integer) ((Vector) answerHash).elementAt(0))
							.intValue();
					String value = "" + (rating.getMin() + option);
					answer = new Answer(rating, value);
				}
				answers.addAnswer(answer);
			}
			String messageToSend = ObjectsProcessor.buildResponseQuestionnaire(
					answers, this.m_artwork, this.m_messageReceived
							.getPhoneIdMessage());
			this.m_bluecontroller.sendMessage(messageToSend);

			// Receives the message with the artwork information.
			String respMessage = this.m_bluecontroller.receiveMessage();

			if (this.m_questionnaire != null) {
				this.m_showScreen = new ShowScreen(this);
				this.setActiveScreen(this.m_showScreen);

				processReceivedMessage(respMessage);
			}
		} catch (InvalidParameterException e) {
			// Do nothing
		} catch (Exception e) {
			throw new RuntimeException(
					"Error MuseumMidlet1 filled questionnaire: "
							+ e.getMessage());
		} finally {
			// TODO show the averages if available.
			setActiveScreen(this.m_showScreen);
		}
	}

	/**
	 * Returns the bluetooth controller.
	 * 
	 * @return Bluetooth controller.
	 */
	public UIToBluetoothInterface getBluetoothController() {
		return this.m_bluecontroller;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see appl.museum.BluetoothControlerUser#informConnected()
	 */
	public void informConnected() {
		// Starts the detector.
		try {
			this.startDetector(new RFIDDetector(this));
		} catch (NFCMidletException e) {
			this.setActiveScreen(new AlertScreen(this,
					"There was a problem stablishing the tag listener.", true));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	protected void startApp() throws MIDletStateChangeException {
		try {
			// Shows the presentation screen.
			this.setActiveScreen(new PresentationScreen(this));

			// Starts the bluetooth controller.
			this.m_bluecontroller = new BluetoothController(this);

			String privacyString = this
					.getAppProperty("transmitted-parameters");
			if ((privacyString != null)
					&& (privacyString.compareTo("true") == 0)) {
				this.m_privacyActive = true;
			}

			// TODO To use these parameters.
			String tagIdMask = this.getAppProperty("filter.tagid.mask");
			String tagIdValue = this.getAppProperty("filter.tagid.value");
			String dataRange = this.getAppProperty("filter.data.range");
			String dataValue = this.getAppProperty("filter.data.value");

		} catch (Exception e) {
			this.setActiveScreen(new AlertScreen(this, "General problem: "
					+ e.getMessage(), true));
		}
	}

	/**
	 * Starts detecting RFID tags.
	 * 
	 * @param previousScreen
	 *            Screen to return if connection fail.
	 */
	public void startDetecting(Screen previousScreen) {
		// Read tag screen.
		this.m_showScreen = new ShowScreen(this);
		this.m_showScreen.setTicker("Touch a tag");

		// Previous screen.

		if (!this.m_bluecontroller.isBluetoothConnected()) {
			this.m_bluecontroller.connectBluetooth(previousScreen,
					this.m_showScreen);
		} else {
			this.setActiveScreen(this.m_showScreen);
			try {
				this.startDetector(new RFIDDetector(this));
			} catch (NFCMidletException e) {
				this.setActiveScreen(new AlertScreen(this,
						"There was a problem stablishing the tag listener.",
						true));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see reader.TagDetector#startReaderThread(javax.microedition.contactless.TargetProperties[])
	 */
	public ReaderThread startReaderThread(TargetProperties[] properties) {
		ReaderThread readerThread = new MuseumReaderThread(properties, this);
		return readerThread;
	}

	/**
	 * Stops the RFID detection and show the main screen.
	 */
	public void stopDetecting() {
		this.stopDetector();
		this.setActiveScreen(new MenuScreen(this));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see core.GenericMidlet#putMessageToSend(core.RequestMessage)
	 */
	public void tagRead(RequestMessage message) {
		try {
			this.m_messageReceived = (MuseumRequestMessage) message;

			// Show the next screen with a waiting image.
			this.m_showScreen = new ShowScreen(this);
			this.setActiveScreen(this.m_showScreen);
			this.m_showScreen.gaugeActive(true);

			// Sends a message with the phone id, the user information and the
			// tag id.
			this.m_messageReceived.setPrivacy(this.m_privacyActive);
			if (this.m_visitor != null) {
				this.m_messageReceived.setUid(this.m_visitor.getId());
			}
			this.m_bluecontroller.sendMessage(this.m_messageReceived
					.getAllMessage());

			// Receives the message with the artwork information.
			String respMessage = this.m_bluecontroller.receiveMessage();

			processReceivedMessage(respMessage);

		} catch (Exception e) {
			throw new RuntimeException(
					"Error showing artwork and questionnaire: "
							+ e.getMessage());
		}
	}

	/**
	 * Process an XML message and show its content (Artwork and questionnaires)
	 * 
	 * @param respMessage
	 *            XML message that describes the artwork.
	 * @throws InvalidParameterException
	 *             If some parameter is invalid.
	 */
	private void processReceivedMessage(String respMessage)
			throws InvalidParameterException {
		// Gets the user's id from the message.
		this.m_visitor = ObjectsProcessor.getUserFromMessage(respMessage);

		// Gets the artwork from the message.
		this.m_artwork = ObjectsProcessor.getArtworkFromMessage(respMessage);

		// Shows the artwork properties in the screen.
		showArtwork(this.m_artwork);

		// Gets the questionnaire from the message.
		this.m_questionnaire = ObjectsProcessor.getQuestionnaireFromMessage(
				respMessage, Questionnaire.XML_RATING_REQUEST);
		if (this.m_questionnaire == null) {
			this.m_questionnaire = ObjectsProcessor
					.getQuestionnaireFromMessage(respMessage,
							Questionnaire.XML_SURVEY_REQUEST);
		}
		// Shows the questionnaire in the screen.
		if (this.m_questionnaire != null) {
			QuestionnaireScreen questionnaireScreen = showQuestionnaire(
					this.m_questionnaire.getQuestionnaireType(),
					this.m_questionnaire.getTitle(), this.m_questionnaire
							.getInstructions(), this.m_questionnaire, null);
			this.m_showScreen.addCommand(this.m_questionnaire.getTitle(),
					questionnaireScreen);
		}

		// Gets the profile from the message and its answers.
		this.m_profileRequest = ObjectsProcessor.getQuestionnaireFromMessage(
				respMessage, Questionnaire.XML_PROFILE_REQUEST);
		VisitorsAnswers answers = ObjectsProcessor
				.getQuestionnaireAnswersFromMessage(respMessage,
						this.m_profileRequest, this.m_visitor,
						Questionnaire.XML_PROFILE_REQUEST);

		// Show the profile in the screen
		QuestionnaireScreen questionnaireScreen = showQuestionnaire(
				this.m_profileRequest.getQuestionnaireType(),
				this.m_profileRequest.getTitle(), this.m_profileRequest
						.getInstructions(), this.m_profileRequest, null// FIXME
		// answers
		);
		this.m_showScreen.addSecondCommand(this.m_profileRequest.getTitle(),
				questionnaireScreen);
	}

	/**
	 * @param type
	 *            Type of questionnaire.
	 * @param title
	 *            Title of the questionnaire.
	 * @param instructions
	 *            Instructions of the questionnaire.
	 * @param questionnaire
	 *            questionnaire to show.
	 * @param answers
	 *            questionnaire answers.
	 * @return screen where the questionnaire is shown.
	 */
	private QuestionnaireScreen showQuestionnaire(String type, String title,
			String instructions, Questionnaire questionnaire,
			VisitorsAnswers answers) {
		QuestionnaireScreen questionnaireScreen = new QuestionnaireScreen(this,
				type, title, instructions, this.m_showScreen);
		Vector questions = questionnaire.getQuestions();
		for (int i = 0, length = questions.size(); i < length; i++) {
			AbstractQuestion question = (AbstractQuestion) questions
					.elementAt(i);
			if (question instanceof Rating) {

				// Rating question
				int selected = 0;
				Rating rating = (Rating) question;
				if (answers != null) {
					Answer answer = answers.getAnswerByQuestionId(rating
							.getId());
					selected = Integer.parseInt(answer.getAnswer());
				}
				questionnaireScreen.addRatingQuestion(rating.getId(), rating
						.getContent(), rating.getMin(), rating.getMax(),
						selected);
			} else if (question instanceof Open) {

				// Open question.
				String text = "";
				Open open = (Open) question;
				if (answers != null) {
					Answer answer = answers.getAnswerByQuestionId(open.getId());
					text = answer.getAnswer();
				}
				questionnaireScreen.addOpenQuestion(open.getId(), open
						.getContent(), text);
			} else if (question instanceof MultipleOptionUniqueAnswer) {

				// Multiple option unique answer question.
				int selected = 0;
				MultipleOptionUniqueAnswer mulOptUniAns = (MultipleOptionUniqueAnswer) question;
				Vector optionsVec = mulOptUniAns.getOptions();
				String[] options = new String[optionsVec.size()];
				for (int j = 0, length2 = optionsVec.size(); j < length2; j++) {
					options[j] = ((UniqueAnswerOption) optionsVec.elementAt(j))
							.getName();
				}
				if (answers != null) {
					Answer answer = answers.getAnswerByQuestionId(mulOptUniAns
							.getId());
					selected = Integer.parseInt(answer.getAnswer());
				}
				questionnaireScreen.addMultOptUniAns(mulOptUniAns.getId(),
						mulOptUniAns.getContent(), options, selected);
			} else if (question instanceof MultipleOptionMultipleAnswer) {

				// Multiple option multiple answer question.
				int[] selected = new int[0];
				MultipleOptionMultipleAnswer mulOptMulAns = (MultipleOptionMultipleAnswer) question;
				Vector optionsVec = mulOptMulAns.getOptions();
				String[] options = new String[optionsVec.size()];
				for (int j = 0, length2 = optionsVec.size(); j < length2; j++) {
					options[j] = ((AnswerOption) optionsVec.elementAt(j))
							.getName();
				}
				if (answers != null) {
					MultipleAnswer answer = (MultipleAnswer) answers
							.getAnswerByQuestionId(mulOptMulAns.getId());
					Vector multAnswers = answer.getAnswers();
					// TODO take the multiples answers.
				}
				questionnaireScreen.addMultOptMulAns(mulOptMulAns.getId(),
						mulOptMulAns.getContent(), options, selected);
			} else {
				// Invalid type of question.
				throw new RuntimeException("Unknown type of question.");
			}
		}
		return questionnaireScreen;
	}

	/**
	 * Shows the artwork properties in the screen.
	 * 
	 * @param artWork
	 *            Artwork to show.
	 */
	private void showArtwork(ArtWork artWork) {
		Vector descriptions = artWork.getDescriptions();
		for (int i = 0, length = descriptions.size(); i < length; i++) {
			AbstractArtWorkDescription description = (AbstractArtWorkDescription) descriptions
					.elementAt(i);
			if (description instanceof ArtWorkDescriptionText) {
				ArtWorkDescriptionText text = (ArtWorkDescriptionText) description;
				String title = text.getTitle();
				String content = text.getMessage();
				if (title.compareTo(ArtWorkDescriptionText.XML_TEXT_TITLE) == 0) {
					// Sets the title of the information
					this.m_showScreen.setTicker(content);
				} else {
					this.m_showScreen.addStringItem(title, content);
				}
			} else if (description instanceof ArtWorkDescriptionBinaire) {
				String type = description.getType();
				ArtWorkDescriptionBinaire binDesc = (ArtWorkDescriptionBinaire) description;
				String fileName = binDesc.getFileName();
				// TODO revisar si no encuentra el recurso, lanzar una
				// exception.
				InputStream stream = getClass().getResourceAsStream(fileName);
				if ((type.compareTo(ArtWorkDescriptionBinaire.TYPE_IMAGE_JPEG) == 0)
						|| (type
								.compareTo(ArtWorkDescriptionBinaire.TYPE_IMAGE_BMP) == 0)) {
					this.m_showScreen.addImage(stream);
				} else if (type.compareTo(ArtWorkDescriptionBinaire.TYPE_AUDIO) == 0) {
					this.m_showScreen.playSound(stream);
				} else {
					this.m_showScreen.addStringItem("Error",
							"Media type not supported :( " + type);
				}
			}
		}
		this.m_showScreen.gaugeActive(false);
		this.m_showScreen.vibrate();
	}

	public void informDisonnected() {
		// TODO Auto-generated method stub
	}
}
