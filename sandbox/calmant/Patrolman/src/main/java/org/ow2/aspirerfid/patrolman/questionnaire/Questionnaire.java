package org.ow2.aspirerfid.patrolman.questionnaire;

import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.StringItem;

import org.ow2.aspirerfid.nfc.midlet.generic.ui.Screen;
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

	/** GroupSpec pattern */
	public String pattern;

	/** Questionnaire id */
	private String m_id;

	/** Questions */
	private Vector m_questions;

	/** Submit button */
	private StringItem m_submitBtn;

	/** Submit command */
	private Command m_submitCmd;
	
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

		if (id == null)
			m_id = title;
		else
			m_id = id;

		m_form = new Form(title);
		m_form.setCommandListener(this);
		setDiplayable(m_form);
	}

	/**
	 * Retrieve questionnaire's identifier
	 * 
	 * @return Questionnaire's identifier
	 */
	public String getId() {
		return m_id;
	}

	/**
	 * Add a textual question to the form
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
	 * Add a single choice question to the form
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
	 * Add a multiple choice question to the form
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

	public int appendQuestion(Item item) {
		if (item instanceof Question) {
			m_questions.addElement(item);
		}

		return m_form.append(item);
	}

	/**
	 * Returns the XML representation of the current questionnaire state.
	 * 
	 * @return An XML representation of the current state
	 */
	public String toXML() {
		String result = "<questionnaire id=\"" + m_id + "\">";

		Enumeration e = m_questions.elements();
		while (e.hasMoreElements()) {
			Object obj = e.nextElement();
			if (obj instanceof Question) {
				result += ((Question) obj).toXML();
			}
		}

		result += "</questionnaire>";
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.microedition.lcdui.CommandListener#commandAction(javax.microedition
	 * .lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command command, Displayable displayable) {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.microedition.lcdui.ItemCommandListener#commandAction(javax.microedition
	 * .lcdui.Command, javax.microedition.lcdui.Item)
	 */
	public void commandAction(Command command, Item item) {
		if (command == m_submitCmd) {
			// Ask the MIDlet to send the report
			m_patrolman.sendECReport(m_reportSpec);
		} else if(command == m_resetCmd) {
			// TODO: set all questions to their default value
		} else if(command == m_backCmd) {
			/*
			 * We may destroy the current displayable object
			 * but it seems to be impossible
			 */
			m_patrolman.showMenuScreen();
		}
	}

	/**
	 * @param m_reportSpec
	 *            the report specification to set
	 */
	public void setReportSpec(LightECReportSpec reportSpec) {
		m_reportSpec = reportSpec;
	}

	/**
	 * @return the report specification
	 */
	public LightECReportSpec getReportSpec() {
		return m_reportSpec;
	}

	/**
	 * Adds submit, reset and back buttons
	 */
	public void finalizeUI() {
		// Submit button
		m_submitBtn = new StringItem("", "Submit", StringItem.BUTTON);
		m_submitCmd = new Command("Submit", Command.ITEM, 0);
		m_submitBtn.setDefaultCommand(m_submitCmd);
		m_submitBtn.setItemCommandListener(this);
		
		// Reset button
		m_clearBtn = new StringItem("", "Clear", StringItem.BUTTON);
		m_resetCmd = new Command("Clear", Command.ITEM, 0);
		m_clearBtn.setDefaultCommand(m_resetCmd);
		m_clearBtn.setItemCommandListener(this);
		
		// Back button
		m_backBtn = new StringItem("", "Back", StringItem.BUTTON);
		m_backCmd = new Command("Back", Command.ITEM, 0);
		m_backBtn.setDefaultCommand(m_backCmd);
		m_backBtn.setItemCommandListener(this);
		
		m_form.append(m_submitBtn);
		m_form.append(m_clearBtn);
		m_form.append(m_backBtn);
	}
}
