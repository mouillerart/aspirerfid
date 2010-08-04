/**
 * 
 */
package org.ow2.aspirerfid.touchlocate.ui;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import org.ow2.aspirerfid.nfc.midlet.generic.GenericMidlet;
import org.ow2.aspirerfid.nfc.midlet.generic.ui.Screen;
import org.ow2.aspirerfid.touchlocate.nfc.TagLocationMessage;

/**
 * @author Thomas Calmant
 * 
 */
public class MenuScreen extends Screen {

	/** Exit command. */
	private static final Command s_backCmd = new Command("Back", Command.BACK,
			0);

	/** Select command */
	private static final Command s_selectCmd = new Command("Select",
			Command.ITEM, 0);

	/** Show on map option */
	private static final String s_showOnMap = "Show on Google Maps";

	/** Reverse geocode address */
	private static final String s_geocode = "Reverse geocode";

	/** Near points of interest */
	private static final String s_poi = "Points of interest";

	/** Show position information */
	private static final String s_infos = "Show tag informations";

	/** Menu options list */
	private static final String[] s_optionsNames = { s_showOnMap, s_geocode,
			s_poi, s_infos };

	/** Previous screen */
	private Screen m_previousScreen;

	/** Information screen */
	private InformationScreen m_infosScreen;

	/** Menu list */
	private List m_optionsList;

	/** Working location message */
	private TagLocationMessage m_locationMsg;

	/**
	 * @param midlet Parent MIDlet
	 * @param previousScreen Screen shown on back command 
	 */
	public MenuScreen(GenericMidlet midlet, Screen previousScreen) {
		super(midlet);
		m_previousScreen = previousScreen;

		m_infosScreen = new InformationScreen(midlet, this);
		m_locationMsg = null;

		m_optionsList = new List("Menu", Choice.IMPLICIT, s_optionsNames, null);
		m_optionsList.setSelectCommand(s_selectCmd);
		m_optionsList.addCommand(s_backCmd);
		m_optionsList.setCommandListener(this);

		setDiplayable(m_optionsList);
	}

	/**
	 * Sets the working location message
	 * 
	 * @param message
	 *            Message read from a location tag
	 */
	public void setLocationMessage(TagLocationMessage message) {
		m_locationMsg = message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.microedition.lcdui.CommandListener#commandAction(javax.microedition
	 * .lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command command, Displayable displayable) {
		if (command == s_backCmd) {
			getMidlet().setActiveScreen(m_previousScreen);
		} else if (command == s_selectCmd) {
			String option = s_optionsNames[m_optionsList.getSelectedIndex()];

			if (option.equals(s_showOnMap)) {
				// TODO: show Google Map
			} else if (option.equals(s_geocode)) {
				// TODO: show address informations
			} else if (option.equals(s_infos)) {
				m_infosScreen.setText(m_locationMsg.toString());
				getMidlet().setActiveScreen(m_infosScreen);
			}
		}
	}

}
