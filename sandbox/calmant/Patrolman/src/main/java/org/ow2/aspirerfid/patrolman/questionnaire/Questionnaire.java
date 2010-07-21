package org.ow2.aspirerfid.patrolman.questionnaire;

import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.StringItem;

import org.ow2.aspirerfid.nfc.midlet.generic.ui.AlertScreen;
import org.ow2.aspirerfid.nfc.midlet.generic.ui.Screen;
import org.ow2.aspirerfid.nfc.midlet.sendersReceivers.bluetooth.BluetoothController;
import org.ow2.aspirerfid.patrolman.Patrolman;
import org.ow2.aspirerfid.patrolman.ecspec.LightECReportSpec;

/**
 * Questionnaire UI and XML generation
 * 
 * @author Thomas Calmant
 * 
 */
public class Questionnaire extends Screen implements ItemCommandListener {
	/** UI Container */
	private Form m_form;

	/** GroupSpec patterns */
	private Vector m_patterns;

	/** Questionnaire id */
	private String m_id;

	/** Questions */
	private Vector m_questions;

	/** Submit button */
	private StringItem m_saveBtn;

	/** Submit command */
	private Command m_saveCmd;

	/** Reset button */
	private StringItem m_clearBtn;

	/** Reset command */
	private Command m_resetCmd;

	/** Back button */
	private StringItem m_backBtn;

	/** Back command */
	private Command m_backCmd;

	/** Associated ECReportSpec */
	private LightECReportSpec m_reportSpec;

	/** Parent MIDlet */
	private Patrolman m_patrolman;

	/** Questionnaire data */
	private Vector m_questionnaireData;

	/** UID of the tag which activated this questionnaire */
	private StringItem m_tagUIDItem;

	/**
	 * Prepares the form
	 * 
	 * @param title
	 *            Form title
	 * @param id
	 *            Questionnaire Id
	 */
	public Questionnaire(Patrolman midlet, String title, String id) {
		super(midlet);
		m_patrolman = (Patrolman) midlet;
		m_questions = new Vector();
		m_questionnaireData = new Vector();

		if (id == null)
			m_id = title;
		else
			m_id = id;

		m_tagUIDItem = new StringItem("Tag: ", "NoID");

		m_form = new Form(title);
		m_form.append(m_tagUIDItem);
		m_form.setCommandListener(this);
		setDiplayable(m_form);
	}

	/**
	 * Adds a multiple choice question to the form
	 * 
	 * @param id
	 *            Question Id (mandatory)
	 * @param label
	 *            Label associated to the choice group (mandatory)
	 * @param choices
	 *            Available choices (mandatory)
	 * @param default_answer
	 *            Default selected answer (can be null)
	 * @param correct_answers
	 *            Valid answers table (can be null)
	 * @return True if the question has been added
	 */
	public boolean addMultipleChoice(String id, String label, String[] choices,
			int default_answer, int[] correct_answers) {
		if (label == null || label.length() == 0 || id == null
				|| id.length() == 0 || choices == null || choices.length == 0)
			return false;

		ChoiceQuestion qst = new ChoiceQuestion(id, label,
				ChoiceQuestion.MULTIPLE, choices, default_answer,
				correct_answers);
		appendQuestion(qst);
		return true;
	}

	/**
	 * Adds a UID pattern of handled tags.
	 * 
	 * @param pattern
	 *            Pattern of handled tags
	 */
	public void addPattern(String pattern) {
		if (m_patterns == null)
			m_patterns = new Vector();

		m_patterns.addElement(pattern);
	}
	
	/**
	 * Adds a textual question to the form
	 * 
	 * @param id
	 *            Question id (mandatory)
	 * @param label
	 *            Label associated to the text box (mandatory)
	 * @param default_answer
	 *            (can be null)
	 * @param correct_answer
	 *            (can be null)
	 * @return True if the question has been added
	 */
	public boolean addTextualQuestion(String id, String label,
			String default_answer, String correct_answer) {
		if (label == null || label.length() == 0 || id == null
				|| id.length() == 0)
			return false;

		TextualQuestion qst = new TextualQuestion(id, label, default_answer,
				correct_answer);
		appendQuestion(qst);
		return true;
	}

	/**
	 * Adds a single choice question to the form
	 * 
	 * @param id
	 *            Question Id (mandatory)
	 * @param label
	 *            Label associated to the choice group (mandatory)
	 * @param choices
	 *            Available choices (mandatory)
	 * @param default_answer
	 *            Default selected answer (can be null)
	 * @param correct_answer
	 *            Valid answer (can be null)
	 * @return True if the question has been added
	 */
	public boolean addUniqueChoice(String id, String label, String[] choices,
			int default_answer, int correct_answer) {
		if (label == null || label.length() == 0 || id == null
				|| id.length() == 0 || choices == null || choices.length == 0)
			return false;

		ChoiceQuestion qst = new ChoiceQuestion(id, label,
				ChoiceQuestion.UNIQUE, choices, default_answer,
				new int[] { correct_answer });
		appendQuestion(qst);
		return true;
	}

	/**
	 * Append a question to the UI
	 * @param item Question to be added
	 * @return The appending result
	 */
	private int appendQuestion(Item item) {
		if (item instanceof Question) {
			m_questions.addElement(item);
		}

		return m_form.append(item);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.microedition.lcdui.CommandListener#commandAction(javax.microedition
	 * .lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command command, Displayable displayable) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.microedition.lcdui.ItemCommandListener#commandAction(javax.microedition
	 * .lcdui.Command, javax.microedition.lcdui.Item)
	 */
	public void commandAction(Command command, Item item) {
		if (command == m_saveCmd) {
			String tagUID = m_tagUIDItem.getText();
			QuestionnaireData data = new QuestionnaireData(tagUID,
					getHandlingPattern(tagUID));

			// Save questionnaire data
			Enumeration questions = m_questions.elements();
			while (questions.hasMoreElements()) {
				data.addValue(((Question) questions.nextElement()).getData());
			}

			m_questionnaireData.addElement(data);

			AlertScreen as = new AlertScreen(m_patrolman, "Data saved");
			as.setActive();
		} else if (command == m_resetCmd) {
			reset();
		} else if (command == m_backCmd) {
			m_patrolman.showMenuScreen();
		}
	}

	/**
	 * Tests if the first argument corresponds to the given pattern. Handles
	 * multiple jokers in the string
	 * 
	 * @param str
	 *            String to be tested
	 * @param pattern
	 *            Pattern to validate the string
	 * @return True if the first parameter is validated by the pattern
	 */
	private boolean correspond(String str, String pattern) {
		int str_pos = 0;
		int old_joker_pos = 0;
		int joker_pos = pattern.indexOf('*');

		// Explicit pattern, just compare string and pattern
		if (joker_pos == -1)
			return str.compareTo(pattern) == 0;

		int pattern_length = pattern.length();

		do {
			// Recognize parts between jokers
			String sub_part = pattern.substring(old_joker_pos, joker_pos);

			// string must correspond all along the pattern
			if (!str.substring(str_pos).startsWith(sub_part))
				return false;

			// Get further
			str_pos += sub_part.length();

			old_joker_pos = joker_pos + 1;
			joker_pos = pattern.indexOf('*');
		} while (joker_pos != -1 || old_joker_pos > pattern_length);

		if (str.substring(str_pos).startsWith(pattern.substring(old_joker_pos)))
			return true;

		return false;
	}

	/**
	 * Adds submit, reset and back buttons
	 */
	public void finalizeUI() {
		// Submit button
		m_saveBtn = new StringItem("", "Save", StringItem.BUTTON);
		m_saveCmd = new Command("Save", Command.ITEM, 0);
		m_saveBtn.setDefaultCommand(m_saveCmd);
		m_saveBtn.setItemCommandListener(this);
		
		// Back button
		m_backBtn = new StringItem("", "Back", StringItem.BUTTON);
		m_backCmd = new Command("Back", Command.ITEM, 1);
		m_backBtn.setDefaultCommand(m_backCmd);
		m_backBtn.setItemCommandListener(this);

		// Reset button
		m_clearBtn = new StringItem("", "Clear", StringItem.BUTTON);
		m_resetCmd = new Command("Clear", Command.ITEM, 2);
		m_clearBtn.setDefaultCommand(m_resetCmd);
		m_clearBtn.setItemCommandListener(this);

		m_form.append(m_saveBtn);
		m_form.append(m_clearBtn);
		m_form.append(m_backBtn);
	}
	
	/**
	 * Returns the pattern corresponding to the given tag UID or null if not
	 * found
	 * 
	 * @param tagUID
	 *            Tag's UID
	 * @return The corresponding pattern or null.
	 */
	private String getHandlingPattern(String tagUID) {
		// No patterns, everything is handled
		if (m_patterns == null || m_patterns.size() == 0)
			return null;

		Enumeration en = m_patterns.elements();
		while (en.hasMoreElements()) {
			String pattern = (String) en.nextElement();

			if (correspond(tagUID, pattern))
				return pattern;
		}

		return null;
	}

	/**
	 * Retrieves questionnaire's identifier
	 * 
	 * @return Questionnaire's identifier
	 */
	public String getId() {
		return m_id;
	}

	/**
	 * Returns the patterns list
	 */
	public Vector getPatterns() {
		return m_patterns;
	}

	/**
	 * @return All known data
	 */
	public Vector getQuestionnaireData() {
		return m_questionnaireData;
	}

	/**
	 * Retrieves the data associated to the given tag UID
	 * @param tagUID Tag UID
	 * @return The associated data, or null
	 */
	public QuestionnaireData getQuestionnaireData(String tagUID) {
		Enumeration savedData = m_questionnaireData.elements();
		while (savedData.hasMoreElements()) {
			QuestionnaireData data = (QuestionnaireData) savedData
					.nextElement();
			if (data.getTagUID().equals(tagUID))
				return data;
		}

		return null;
	}

	/**
	 * @return the report specification
	 */
	public LightECReportSpec getReportSpec() {
		return m_reportSpec;
	}

	/**
	 * Tests if the questionnaire corresponds to the given UID
	 * 
	 * @param tagUID
	 *            Tag's UID
	 * @return True if this questionnaire handles the given UID
	 */
	public boolean handlesTag(String tagUID) {
		return getHandlingPattern(tagUID) != null;
	}

	public void loadQuestionnaire(String tagUID) {
		m_tagUIDItem.setText(tagUID);
		QuestionnaireData data = getQuestionnaireData(tagUID);

		if (data != null) {
			// Load questionnaire data
			Enumeration questions = m_questions.elements();
			Enumeration qst_data = data.getValues().elements();

			while (questions.hasMoreElements() && qst_data.hasMoreElements()) {
				((Question) questions.nextElement()).setData(qst_data
						.nextElement());
			}
		} else {
			// Show a blank questionnaire
			reset();
		}
	}

	/**
	 * Reset questionnaire shown data
	 */
	private void reset() {
		// Clear all questions content
		Enumeration questions = m_questions.elements();
		while (questions.hasMoreElements()) {
			((Question) questions.nextElement()).clear();
		}
	}

	/**
	 * Sets UID patterns of handled tags. Erases existing patterns list. The
	 * Vector may contain String objects
	 * 
	 * @param patterns
	 *            The new UID patterns list
	 */
	public void setPatterns(Vector patterns) {
		m_patterns = patterns;
	}

	/**
	 * @param m_reportSpec
	 *            the report specification to set
	 */
	public void setReportSpec(LightECReportSpec reportSpec) {
		m_reportSpec = reportSpec;
	}

	/**
	 * Returns the XML representation of the current questionnaire state.
	 * (WARNING: heavy memory consumption)
	 * 
	 * @return An XML representation of the current state
	 */
	/*
	public String toXML() {
		StringBuffer result = new StringBuffer("<questionnaire id=\"" + m_id
				+ "\">\n");

		Enumeration e = m_questions.elements();
		while (e.hasMoreElements()) {
			result.append(((Question) e.nextElement()).toXML());
		}

		result.append("</questionnaire>\n");
		return result.toString();
	}
	*/
	
	/**
	 * Returns the XML representation of the given questionnaire state.
	 * (WARNING: heavy memory consumption)
	 * 
	 * @param data Questionnaire state to be sent
	 * @return An XML representation of the given state
	 */
	/*
	public String toXML(QuestionnaireData data) {
		if(data == null)
			return toXML();
		
		if(m_questions.size() != data.getValues().size())
			throw new RuntimeException("Invalid QuestionnaireData size");
		
		StringBuffer result = new StringBuffer("<questionnaire id=\"" + m_id
				+ "\">\n");
		
		Enumeration questions = m_questions.elements();
		Enumeration qst_data = data.getValues().elements();
		
		while (questions.hasMoreElements() && qst_data.hasMoreElements()) {
			result.append(((Question) questions.nextElement()).toXML(qst_data.nextElement()));
		}

		result.append("</questionnaire>\n");
		return result.toString();
	}
	*/

	/**
	 * Sends the XML representation of the given questionnaire state.
	 * 
	 * @param btController A connected BlueTooth controller
	 * @param data Questionnaire state to be sent
	 */
	public void sendXML(BluetoothController btController, QuestionnaireData data) {
		if(data == null || m_questions.size() != data.getValues().size())
			throw new RuntimeException("Invalid QuestionnaireData size");
		
		btController.sendMessage("<questionnaire id=\"" + m_id + "\">\n");
		
		Enumeration questions = m_questions.elements();
		Enumeration qst_data = data.getValues().elements();
		
		while (questions.hasMoreElements() && qst_data.hasMoreElements()) {
			btController.sendMessage(((Question) questions.nextElement()).toXML(qst_data.nextElement()));
		}

		btController.sendMessage("</questionnaire>\n");
	}
}
