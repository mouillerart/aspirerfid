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
package org.ow2.aspirerfid.patrolman.ui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;

import org.ow2.aspirerfid.nfc.midlet.generic.ui.Screen;
import org.ow2.aspirerfid.patrolman.Patrolman;

/**
 * @author Thomas Calmant
 * 
 */
public class SubmitScreen extends Screen {
	/** Back command */
	private static final Command m_backCmd = new Command("Back", Command.BACK,
			0);
	
	/** Parent Patrolman instance */
	private Patrolman m_parent;

	/** Previous screen */
	private Screen m_previousScreen;

	/** Status information */
	private StringItem m_status;

	/**
	 * @param parent
	 *            Parent Patrolman MIDlet
	 * @param previousScreen
	 *            Screen to show on back command.
	 */
	public SubmitScreen(Patrolman parent, Screen previousScreen) {
		super(parent);
		m_parent = parent;
		m_previousScreen = previousScreen;

		// UI preparation
		m_status = new StringItem("Status :", null);
		Form form = new Form("Submission");
		form.append(m_status);
		form.addCommand(m_backCmd);
		form.setCommandListener(this);

		setDiplayable(form);
	}
	
	/* (non-Javadoc)
	 * @see org.ow2.aspirerfid.nfc.midlet.generic.ui.Screen#setActive()
	 */
	public void setActive() {
		super.setActive();
		
		// Submission
		m_status.setText("Sending data...");
		try {
			m_parent.submitAllQuestionnaires();
			m_status.setText("Done");
		} catch (Exception ex) {
			m_status.setText("Error sending data :\n" + ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.microedition.lcdui.CommandListener#commandAction(javax.microedition
	 * .lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command command, Displayable displayable) {
		if (command == m_backCmd) {
			getMidlet().setActiveScreen(m_previousScreen);
		}
	}

}
