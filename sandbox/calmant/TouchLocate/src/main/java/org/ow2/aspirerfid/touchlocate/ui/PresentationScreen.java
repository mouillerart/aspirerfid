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

import java.io.InputStream;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.StringItem;

import org.ow2.aspirerfid.nfc.midlet.generic.GenericMidlet;
import org.ow2.aspirerfid.nfc.midlet.generic.ui.AlertScreen;
import org.ow2.aspirerfid.nfc.midlet.generic.ui.Screen;
import org.ow2.aspirerfid.nfc.midlet.generic.ui.UITools;

/**
 * Presentation screen (title + logo)
 * 
 * @author Thomas Calmant
 */
public class PresentationScreen extends Screen {

	/** Next screen command */
	private static final Command m_enter = new Command("Enter", Command.OK, 0);

	/** Next screen */
	private Screen m_nextScreen;

	public PresentationScreen(GenericMidlet midlet, Screen nextScreen) {
		super(midlet);
		m_nextScreen = nextScreen;

		Form form = new Form("Touch'n Locate Demo");
		form.addCommand(m_enter);

		// Loads an image and adds it to the current form.
		try {
			InputStream inputStream = this.getClass().getResourceAsStream(
					"/aspire.jpg");

			form.append(new ImageItem(null, UITools
					.getImageInParam(inputStream), ImageItem.LAYOUT_CENTER,
					null));

		} catch (NullPointerException e) {
			getMidlet().setActiveScreen(
					new AlertScreen(getMidlet(), "Error: Logo not found."));
		}

		// Application's description.
		StringItem description = new StringItem("",
				"LIG - Adele\nThomas Calmant\n2010\nIncludes code from\nTeam TouchKey");
		description.setLayout(Item.LAYOUT_CENTER);
		form.append(description);

		// Establishes the listener to this screen.
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
		// Skip command.
		if (command == m_enter) {
			getMidlet().setActiveScreen(m_nextScreen);
		}
	}

}
