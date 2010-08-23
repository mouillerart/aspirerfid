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

import java.util.Vector;

import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.TextField;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.ow2.aspirerfid.nfc.midlet.generic.GenericMidlet;
import org.ow2.aspirerfid.nfc.midlet.generic.ui.AlertScreen;
import org.ow2.aspirerfid.nfc.midlet.generic.ui.Screen;

import fr.touchkey.gui.GoogleMaps;

/**
 * Points of interest search screen.
 * 
 * @author Thomas Calmant
 */
public class POIScreen extends Screen implements ItemCommandListener {

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

	/** Results list UI */
	private ChoiceGroup m_resultsListUI;

	/** PoI Screen */
	private POIInfoScreen m_poiScreen;

	/** Results list */
	private Vector m_results;

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
		m_results = new Vector();

		m_poiScreen = new POIInfoScreen(midlet, this);

		m_searchField = new TextField("Search", "", 64, TextField.ANY);
		m_resultsListUI = new ChoiceGroup("Result", ChoiceGroup.IMPLICIT);
		m_resultsListUI.setItemCommandListener(this);

		Form form = new Form("POI search");
		form.append(m_searchField);
		form.append(m_resultsListUI);

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
				showMessage("Retrieving data...");
				String result = maps.findPOIinJSON(m_searchField.getString(),
						m_latitude, m_longitude);

				// parse JSON data and show it correctly
				parseJSONList(result);
			} catch (Exception ex) {
				showMessage("Error :\n" + ex);
			}
		} else if (command == m_backCmd) {
			getMidlet().setActiveScreen(m_previousScreen);
		}
	}

	/**
	 * Parses Google Ajax API result
	 * 
	 * @param json
	 *            JSON data received from Google
	 * @throws JSONException
	 *             Error parsing the JSON data
	 */
	private void parseJSONList(String json) throws JSONException {
		JSONObject input = new JSONObject(json);

		if (!input.getString("responseStatus").equals("200")) {
			return;
		}

		JSONArray dico = input.getJSONObject("responseData").getJSONArray(
				"results");
		int nb_results = dico.length();

		for (int i = 0; i < nb_results; i++) {
			JSONObject element = dico.getJSONObject(i);
			PointOfInterest poi = new PointOfInterest();

			poi.title = element.optString("titleNoFormatting");
			if (poi.title == null) {
				poi.title = element.optString("title");
			}

			poi.latitude = element.optString("lat");
			poi.longitude = element.optString("lng");

			poi.address = element.optString("streetAddress");
			poi.city = element.optString("city");

			JSONObject phoneObject = element.optJSONObject("phoneNumbers");
			if (phoneObject != null) {
				poi.phone = phoneObject.optString("number");
			}

			if (poi.isValid()) {
				m_results.addElement(poi);
				m_resultsListUI.append(poi.toString(), null);
			}
		}
	}

	/**
	 * Shows a message on the UI
	 * 
	 * @param text
	 *            Text to show
	 */
	private void showMessage(String text) {
		AlertScreen as = new AlertScreen(getMidlet(), text);
		getMidlet().setActiveScreen(as);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.microedition.lcdui.ItemCommandListener#commandAction(javax.microedition
	 * .lcdui.Command, javax.microedition.lcdui.Item)
	 */
	public void commandAction(Command command, Item item) {
		if (item != m_resultsListUI)
			return;

		// Retrieve selected element
		String title = m_resultsListUI.getString(m_resultsListUI
				.getSelectedIndex());

		// Get its informations
		int index = m_results.indexOf(title);
		if (index == -1)
			return;

		PointOfInterest poi = (PointOfInterest) m_results.elementAt(index);
		if (poi.isValid()) {
			m_poiScreen.setPoI(poi);
			getMidlet().setActiveScreen(m_poiScreen);
		}
	}
}
