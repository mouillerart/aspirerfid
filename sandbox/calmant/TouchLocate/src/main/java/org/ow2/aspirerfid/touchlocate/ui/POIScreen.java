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
package org.ow2.aspirerfid.touchlocate.ui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;

import org.ow2.aspirerfid.nfc.midlet.generic.GenericMidlet;
import org.ow2.aspirerfid.nfc.midlet.generic.ui.Screen;

import fr.touchkey.gui.GoogleMaps;

/**
 * Points of interest search screen.
 * 
 * @author Thomas Calmant
 * 
 *         TODO parse JSON POI data and show it nicely
 */
public class POIScreen extends Screen {

	/** Back command */
	private static final Command m_backCmd = new Command("Back", Command.BACK,
			0);

	/** Find command */
	private static final Command m_findCmd = new Command("Find",
			Command.SCREEN, 0);

	/** Previous screen */
	private Screen m_previousScreen;

	/** Search text field */
	private TextField m_searchField;

	/** Result string item */
	private StringItem m_resultUI;

	/** Center latitude */
	private double m_latitude;

	/** Center longitude */
	private double m_longitude;

	/**
	 * @param midlet
	 *            Parent MIDlet
	 * @param previousScreen
	 *            Screen shown on back command
	 */
	public POIScreen(GenericMidlet midlet, Screen previousScreen) {
		super(midlet);
		m_previousScreen = previousScreen;

		m_searchField = new TextField("Search", "", 64, TextField.ANY);
		m_resultUI = new StringItem("Result :", null);

		Form form = new Form("POI search");
		form.append(m_searchField);
		form.append(m_resultUI);

		form.addCommand(m_findCmd);
		form.addCommand(m_backCmd);
		form.setCommandListener(this);

		setDiplayable(form);
	}

	/**
	 * Update the center of the map
	 * 
	 * @param latitude
	 *            Latitude center of map
	 * @param longitude
	 *            Longitude center of map
	 */
	public void setLocation(double latitude, double longitude) {
		m_latitude = latitude;
		m_longitude = longitude;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.microedition.lcdui.CommandListener#commandAction(javax.microedition
	 * .lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command command, Displayable displayable) {
		if (command == m_findCmd) {
			GoogleMaps maps = new GoogleMaps("");
			try {
				m_resultUI.setText("Retrieving data...");
				String result = maps.findPOIinJSON(m_searchField.getString(),
						m_latitude, m_longitude);

				// TODO parse JSON data and show it correctly
				// TODO find a way to limit the search area
				m_resultUI.setText(result);
			} catch (Exception ex) {
				m_resultUI.setText("Error :\n" + ex);
			}
		} else if (command == m_backCmd) {
			getMidlet().setActiveScreen(m_previousScreen);
		}
	}

}
