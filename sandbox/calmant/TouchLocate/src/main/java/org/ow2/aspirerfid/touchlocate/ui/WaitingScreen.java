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

import org.ow2.aspirerfid.nfc.midlet.generic.ui.Screen;
import org.ow2.aspirerfid.touchlocate.TouchLocate;

/**
 * Shows a screen indicating the tag detection status
 * 
 * @author Thomas Calmant
 */
public class WaitingScreen extends Screen {
	
	/** Exit command */
	private static final Command s_exitCmd = new Command("Exit", Command.EXIT,
			0);

	/** Status information */
	private StringItem m_status;

	/**
	 * @param midlet
	 *            Parent MIDlet
	 */
	public WaitingScreen(TouchLocate midlet) {
		super(midlet);

		// Start detecting tags
		midlet.startDetecting(this);

		m_status = new StringItem("Status", "Waiting for a tag...");

		Form form = new Form("Touch'n Locate");
		form.append(m_status);
		form.addCommand(s_exitCmd);
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
		if (command == s_exitCmd) {
			getMidlet().stopApplication(true, true);
		}
	}

	/**
	 * Sets the status message
	 * 
	 * @param text
	 *            Tag detection status
	 */
	public void setText(String text) {
		m_status.setText(text);
	}
}
