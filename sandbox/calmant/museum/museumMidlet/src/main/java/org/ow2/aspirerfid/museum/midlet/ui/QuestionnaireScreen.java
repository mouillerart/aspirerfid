package org.ow2.aspirerfid.museum.midlet.ui;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;

import org.ow2.aspirerfid.museum.midlet.MuseumMidlet;
import org.ow2.aspirerfid.nfc.midlet.generic.GenericMidlet;
import org.ow2.aspirerfid.nfc.midlet.generic.ui.Screen;

/**
 * This is the questionnaire to show to the user to know his profile.
 * 
 * @author <a href="mailto:angoca@yahoo.com">Andres Gomez</a>
 */
public class QuestionnaireScreen extends Screen {
	/**
	 * Size of the text for an open question.
	 */
	private static final int TEXT_SIZE = 50;
	/**
	 * Cancel command.
	 */
	private Command m_cancelCmd = null;
	/**
	 * Form that have all the elements to show.
	 */
	private Form m_form = null;
	/**
	 * Set of displayed questions.
	 */
	private Hashtable m_questions = null;
	/**
	 * Select command.
	 */
	private Command m_sendCmd = null;
	/**
	 * Screen to return after filled the questionnaire.
	 */
	private ShowScreen m_showScreen = null;
	/**
	 * Title of the questionnaire.
	 */
	private String m_title;
	/**
	 * Type of questionnaire.
	 */
	private String m_type;

	/**
	 * Creates a questionnaire screen.
	 * 
	 * @param midlet
	 *            Caller midlet.
	 * @param type
	 *            Type of questionnaire.
	 * @param title
	 *            Title of the questionnaire.
	 * @param instructions
	 *            Instructions of the questionnaire.
	 * @param show
	 *            Screen to return if canceled.
	 */
	public QuestionnaireScreen(GenericMidlet midlet, String type, String title,
			String instructions, ShowScreen show) {
		super(midlet);

		try {
			this.m_showScreen = show;
			this.m_type = type;
			this.m_title = title;

			this.m_questions = new Hashtable();

			this.m_form = new Form("Questionary");
			StringItem string = new StringItem(title, instructions);
			this.m_form.append(string);

			// Select command.
			this.m_sendCmd = new Command("Save", Command.CANCEL, 1);
			this.m_form.addCommand(this.m_sendCmd);
			// Exit command.
			this.m_cancelCmd = new Command("Back", Command.ITEM, 1);
			this.m_form.addCommand(this.m_cancelCmd);

			// Establishes the listener to this screen.
			this.m_form.setCommandListener(this);

			this.setDiplayable(this.m_form);
		} catch (Exception e) {
			throw new RuntimeException("Error MusuemUI15 constructor quest: "
					+ e.getMessage());
		}
	}

	/**
	 * Adds a question with multiple options and multiple answers.
	 * 
	 * @param id
	 *            Question's ID.
	 * @param title
	 *            Question.
	 * @param options
	 *            Multiple options of the question.
	 * @param selected
	 *            Selected options.
	 */
	public void addMultOptMulAns(String id, String title, String[] options,
			int[] selected) {
		try {
			ChoiceGroup choice = new ChoiceGroup(title, Choice.MULTIPLE,
					options, null);
			for (int i = 0; i < selected.length; i++) {
				choice.setSelectedIndex(selected[i], true);
			}
			this.m_form.append(choice);

			this.m_questions.put(id, choice);
		} catch (Exception e) {
			throw new RuntimeException(
					"Error MusuemUI16 adding addMultOptMulAns question: "
							+ e.getMessage());
		}
	}

	/**
	 * Adds a question with multiple options and just one possible answer.
	 * 
	 * @param id
	 *            Question's ID.
	 * @param title
	 *            Question.
	 * @param options
	 *            Multiple options of the question.
	 * @param selected
	 *            Selected option par default.
	 */
	public void addMultOptUniAns(String id, String title, String[] options,
			int selected) {
		try {
			ChoiceGroup choice = new ChoiceGroup(title, Choice.EXCLUSIVE,
					options, null);
			choice.setSelectedIndex(selected, true);
			this.m_form.append(choice);

			this.m_questions.put(id, choice);
		} catch (Exception e) {
			throw new RuntimeException(
					"Error MuseumUI17 adding addMultOptUniAns question: "
							+ e.getMessage());
		}
	}

	/**
	 * Adds an open question, where the user can write the answer.
	 * 
	 * @param id
	 *            Question's ID.
	 * @param title
	 *            Question.
	 * @param defaultText
	 *            text to show.
	 */
	public void addOpenQuestion(String id, String title, String defaultText) {
		try {
			TextField text = new TextField(title, "", TEXT_SIZE, TextField.ANY);
			text.setString(defaultText);
			this.m_form.append(text);

			this.m_questions.put(id, text);
		} catch (Exception e) {
			throw new RuntimeException(
					"Error MusuemUI18 adding open question: " + e.getMessage());
		}
	}

	/**
	 * Adds a rating question. The range of options are integer numbers [min
	 * max]
	 * 
	 * @param id
	 *            Question's Id.
	 * @param title
	 *            Question.
	 * @param min
	 *            Minimal value for the rating.
	 * @param max
	 *            Maximal value for the rating.
	 * @param selected
	 *            Selected value.
	 */
	public void addRatingQuestion(String id, String title, int min, int max,
			int selected) {
		try {
			int qty = max - min + 1;
			String[] options = new String[qty];
			for (int i = 0; i < qty; i++) {
				options[i] = "" + (min + i);
			}

			ChoiceGroup choice = new ChoiceGroup(title, Choice.EXCLUSIVE,
					options, null);
			choice.setSelectedIndex(selected, true);
			this.m_form.append(choice);

			this.m_questions.put(id, choice);
		} catch (Exception e) {
			throw new RuntimeException(
					"Error MusuemUI19 adding rating question: "
							+ e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command,
	 *      javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command command, Displayable displayable) {
		try {
			if (displayable != this.m_form) {
				// Invalid displayable.
				throw new RuntimeException(
						"Error: Invalid 'questionary' displayable");
			}
			if (command == this.m_sendCmd) {
				Hashtable answers = this.getAnswers();
				((MuseumMidlet) this.getMidlet()).filledQuestionnaire(
						this.m_type, answers);
			} else if (command == this.m_cancelCmd) {
				// Cancel command.
				this.getMidlet().setActiveScreen(this.m_showScreen);
			} else {
				// Unknown command.
				throw new RuntimeException(
						"Error: Invalid command in 'questionary'");
			}
		} catch (Exception e) {
			throw new RuntimeException("Error MusuemUI20 command action: "
					+ e.getMessage());
		}

	}

	/**
	 * Returns the answers of the displayed questions.
	 * 
	 * @return Hashtable that has the answers (id, answer.)
	 */
	private Hashtable getAnswers() {
		Hashtable answers = new Hashtable();
		try {
			Enumeration ids = this.m_questions.keys();
			Enumeration questions = this.m_questions.elements();
			while (ids.hasMoreElements()) {
				String id = (String) ids.nextElement();
				Item question = (Item) questions.nextElement();
				if (question instanceof TextField) {
					TextField textQuestion = (TextField) question;
					char[] text = new char[TEXT_SIZE];
					textQuestion.getChars(text);
					answers.put(id, String.valueOf(text));
				} else if (question instanceof ChoiceGroup) {
					ChoiceGroup multQuestion = (ChoiceGroup) question;
					int size = multQuestion.size();
					// Gets the answer from the choiceGroup
					boolean[] selected = new boolean[size];
					multQuestion.getSelectedFlags(selected);
					Vector selected2 = new Vector();
					// Analyzes the selected, and takes just the true values.
					for (int i = 0; i < selected.length; i++) {
						if (selected[i]) {
							selected2.addElement(new Integer(i));
						}
					}
					answers.put(id, selected2);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Error MuseumUI21 retrieving answers: "
			/* + e.getMessage() */);
		}
		return answers;
	}
}
