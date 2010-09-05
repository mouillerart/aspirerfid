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
import javax.microedition.lcdui.TextBox;
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
public class POIScreen extends Screen {

	/** Back command */
	private static final Command s_backCmd = new Command("Back", Command.BACK,
			0);

	/** Find command */
	private static final Command s_findCmd = new Command("Find", Command.ITEM,
			0);

	/** Previous screen */
	private Screen m_previousScreen;

	/** Search text field */
	private TextBox m_searchField;

	/** PoI Result Screen */
	private POISearchResult m_resultScreen;

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
		m_resultScreen = new POISearchResult(midlet, this);

		m_searchField = new TextBox("Search", "", 256, TextField.ANY);
		m_searchField.addCommand(s_findCmd);
		m_searchField.addCommand(s_backCmd);
		m_searchField.setCommandListener(this);

		/*
		 * Form form = new Form("POI search"); form.append(m_searchField);
		 * 
		 * form.addCommand(s_findCmd); form.addCommand(s_backCmd);
		 * form.setCommandListener(this);
		 */

		setDiplayable(m_searchField);
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
		if (command == s_findCmd) {
			m_resultScreen.reset();

			GoogleMaps maps = new GoogleMaps("");
			try {
				showMessage("Retrieving data...");
				String result = maps.findPOIinJSON(m_searchField.getString(),
						m_latitude, m_longitude);

				// parse JSON data and show it correctly
				parseJSONList(result);

				// Show the result screen
				getMidlet().setActiveScreen(m_resultScreen);

			} catch (Exception ex) {
				showMessage("Error :\n" + ex);
			}
		} else if (command == s_backCmd) {
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
				m_resultScreen.addResult(poi.toString(), poi);
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
}
