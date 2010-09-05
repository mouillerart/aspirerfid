/**
 * 
 */
package org.ow2.aspirerfid.touchlocate.ui;

import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import org.ow2.aspirerfid.nfc.midlet.generic.GenericMidlet;
import org.ow2.aspirerfid.nfc.midlet.generic.ui.AlertScreen;
import org.ow2.aspirerfid.nfc.midlet.generic.ui.Screen;

/**
 * @author Thomas Calmant
 * 
 */
public class POISearchResult extends Screen {

	/** Back command */
	private static final Command s_backCmd = new Command("Back", Command.BACK,
			0);

	/** Select command */
	private static final Command s_selectCmd = new Command("Select",
			Command.ITEM, 0);

	/** Parent search screen */
	private Screen m_previousScreen;

	/** PoI information screen */
	private POIInfoScreen m_infoScreen;

	/** Results list */
	private List m_resultUI;

	/** Results data */
	private Vector m_results;

	/**
	 * @param midlet
	 *            Parent MIDlet
	 */
	public POISearchResult(GenericMidlet midlet, Screen previousScreen) {
		super(midlet);
		m_infoScreen = new POIInfoScreen(midlet, this);
		m_previousScreen = previousScreen;

		m_results = new Vector();
		m_resultUI = new List("Results", List.IMPLICIT);
		m_resultUI.setSelectCommand(s_selectCmd);
		m_resultUI.addCommand(s_backCmd);
		m_resultUI.setCommandListener(this);

		setDiplayable(m_resultUI);
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
			// Back to search screen
			getMidlet().setActiveScreen(m_previousScreen);

		} else if (command == s_selectCmd) {
			// Retrieve PoI informations
			try {
				PointOfInterest poi = (PointOfInterest) m_results
						.elementAt(m_resultUI.getSelectedIndex());

				m_infoScreen.setPoI(poi);
				getMidlet().setActiveScreen(m_infoScreen);

			} catch (ArrayIndexOutOfBoundsException ex) {
				getMidlet().setActiveScreen(
						new AlertScreen(getMidlet(), "No more data found"));
			}

		}
	}

	/**
	 * Adds a search result to the list
	 * 
	 * @param title
	 *            Item text
	 * @param data
	 *            Associated data
	 */
	public void addResult(String title, PointOfInterest data) {
		m_results.addElement(data);
		m_resultUI.append(title, null);
	}

	/**
	 * Erase search results
	 */
	public void reset() {
		m_results.removeAllElements();
		m_resultUI.deleteAll();
	}
}
