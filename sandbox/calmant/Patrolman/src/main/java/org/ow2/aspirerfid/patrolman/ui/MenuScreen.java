/**
 * 
 */
package org.ow2.aspirerfid.patrolman.ui;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import org.ow2.aspirerfid.nfc.midlet.generic.GenericMidlet;
import org.ow2.aspirerfid.nfc.midlet.generic.ui.Screen;
import org.ow2.aspirerfid.patrolman.Patrolman;

/**
 * @author Thomas Calmant
 * 
 */
public class MenuScreen extends Screen {

	/** Select command reportName */
	private static final String m_selectName = "Select";

	/** Select command */
	private static final Command m_selectCmd = new Command(m_selectName,
			Command.ITEM, 1);

	/** Name of the exit option. */
	private static final String m_exitName = "Exit";

	/** Exit command. */
	private static final Command m_exitCmd = new Command(m_exitName,
			Command.EXIT, 1);

	/** Connect Bluetooth device option */
	private final String m_connectName = "Connect BT device";

	/** Listen for tags */
	private final String m_startTagsListeningName = "Wait for tags";

	/** Stop listening for tags */
	private final String m_stopTagsListeningName = "Stop waiting for tags";

	/** Tag listening state */
	private boolean m_tagListening;

	/** Options UI list */
	private List m_optionsList;

	/** Options names */
	private final String[] m_optionsNames = { m_connectName,
			m_startTagsListeningName, m_exitName };

	/**
	 * @param midlet
	 */
	public MenuScreen(GenericMidlet midlet) {
		super(midlet);
		m_tagListening = false;

		m_optionsList = new List("Main menu", Choice.IMPLICIT, m_optionsNames,
				null);

		// Select command
		m_optionsList.setSelectCommand(m_selectCmd);

		// Exit command
		m_optionsList.addCommand(m_exitCmd);

		// Listener
		m_optionsList.setCommandListener(this);

		setDiplayable(m_optionsList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.microedition.lcdui.CommandListener#commandAction(javax.microedition
	 * .lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command command, Displayable displayable) {
		if (displayable != m_optionsList)
			throw new RuntimeException("Invalid 'main menu' displayable");

		// Exit command
		if (command == m_exitCmd) {
			getMidlet().stopApplication(true, true);
		} else if (command == m_selectCmd) {
			String option = m_optionsNames[m_optionsList.getSelectedIndex()];

			// Exit menu command
			if (option.compareTo(m_exitName) == 0) {
				getMidlet().stopApplication(true, true);
			}
			// Connect menu command
			else if (option.compareTo(m_connectName) == 0) {
				((Patrolman) getMidlet()).startBluetoothDetection(this);
			}
			// Toggle listening for tags
			else if (option.compareTo(m_startTagsListeningName) == 0) {
				if (m_tagListening) {
					// Stop listening
					((Patrolman) getMidlet()).stopTagDetection();
					m_tagListening = false;
					m_optionsList.set(m_optionsList.getSelectedIndex(),
							m_startTagsListeningName, null);
				} else {
					// Start listening
					if (((Patrolman) getMidlet()).startTagDetection()) {
						m_tagListening = true;
						m_optionsList.set(m_optionsList.getSelectedIndex(),
								m_stopTagsListeningName, null);
					}
				}
			}
			// Unknown command
			else
				throw new RuntimeException("Invalid option : " + option);
		}

	}

}
