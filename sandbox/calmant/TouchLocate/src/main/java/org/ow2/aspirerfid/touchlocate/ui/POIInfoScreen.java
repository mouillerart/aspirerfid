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

import org.ow2.aspirerfid.nfc.midlet.generic.GenericMidlet;
import org.ow2.aspirerfid.nfc.midlet.generic.ui.Screen;

/**
 * Shows PoI informations
 * 
 * @author Thomas Calmant
 */
public class POIInfoScreen extends Screen {

	/** Back command */
	private static final Command s_backCmd = new Command("Back", Command.BACK,
			0);

	/** Previous screen */
	private Screen m_previousScreen;

	/** Information field */
	private StringItem m_infos;

	/**
	 * @param midlet
	 *            Parent MIDlet
	 * @param previousScreen
	 *            Back screen
	 */
	public POIInfoScreen(GenericMidlet midlet, Screen previousScreen) {
		super(midlet);
		m_previousScreen = previousScreen;

		m_infos = new StringItem(null, null);

		Form form = new Form("Point of Interest");
		form.append(m_infos);
		form.addCommand(s_backCmd);
		form.setCommandListener(this);

		setDiplayable(form);
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
		}
	}

	/**
	 * Sets PoI informations to be shown
	 * 
	 * @param poi
	 *            Point of Interest
	 */
	public void setPoI(PointOfInterest poi) {

		String info = "";

		if (poi.title != null) {
			info += "Title: " + poi.title + "\n";
		}

		if (poi.address != null) {
			info += "Address: " + poi.address + "\n";
		}

		if (poi.city != null) {
			info += "City: " + poi.city + "\n";
		}

		if (poi.phone != null) {
			info += "Phone: " + poi.phone + "\n";
		}

		if (poi.latitude != null && poi.longitude != null) {
			info += "Location :\n" + poi.latitude + ", " + poi.longitude + "\n";
		}

		m_infos.setText(info);
	}
}
