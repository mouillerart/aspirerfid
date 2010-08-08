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

import org.ow2.aspirerfid.nfc.midlet.generic.GenericMidlet;
import org.ow2.aspirerfid.nfc.midlet.generic.ui.Screen;
import org.ow2.aspirerfid.nfc.midlet.sendersReceivers.bluetooth.BluetoothController;
import org.ow2.aspirerfid.patrolman.Patrolman;
import org.ow2.aspirerfid.patrolman.ecspec.LightECSpec;
import org.ow2.aspirerfid.patrolman.ecspec.LightECSpecParser;

/**
 * Screen shown when connected to a BlueTooth server, and waiting for an ECSpec
 * XML file.
 * 
 * @author Thomas Calmant
 */
public class WaitingECSpec extends Screen {
	/** BlueTooth controller */
	private BluetoothController m_btController;

	/** State information */
	private StringItem m_infos;

	/** Next screen */
	private Screen m_menuScreen;

	/** Name of the Disconnect option. */
	private static final String m_disconnectName = "Disconnect";

	/** Disconnect command. */
	private static final Command m_disconnectCmd = new Command(
			m_disconnectName, Command.EXIT, 2);

	/** Name of the Back option */
	private static final String m_backName = "Back";

	/** Back command */
	private static final Command m_backCmd = new Command(m_backName,
			Command.BACK, 1);

	/** Name of the Refresh option */
	private static final String m_refreshName = "Refresh";

	/** Refresh command */
	private static final Command m_refreshCmd = new Command(m_refreshName,
			Command.ITEM, 0);

	/** Refresh request */
	private static final String REFRESH_COMMAND = "refresh";

	/** Name of the exit option. */
	private static final String m_exitName = "Exit";

	/** Exit command. */
	private static final Command m_exitCmd = new Command(m_exitName,
			Command.EXIT, 3);

	/**
	 * Prepares the screen UI
	 * 
	 * @param midlet
	 *            Parent midlet
	 * @param btController
	 *            A BlueTooth controller (connected state)
	 * @param menuScreen
	 *            Screen to be shown when using the Back command
	 */
	public WaitingECSpec(GenericMidlet midlet,
			BluetoothController btController, Screen menuScreen) {
		super(midlet);
		m_btController = btController;
		m_menuScreen = menuScreen;

		m_infos = new StringItem("State: ", "Press the refresh button");

		Form form = new Form("Waiting...");
		form.append(m_infos);
		form.addCommand(m_disconnectCmd);
		form.addCommand(m_exitCmd);
		form.addCommand(m_backCmd);
		form.addCommand(m_refreshCmd);
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
		if (command == m_disconnectCmd) {
			// Stop command and go to menu screen
			closeConnection();
			getMidlet().setActiveScreen(m_menuScreen);
		} else if (command == m_backCmd) {
			// Go to the menu screen
			getMidlet().setActiveScreen(m_menuScreen);
		} else if (command == m_refreshCmd) {
			// Ask for a refresh
			refresh();
		} else if (command == m_exitCmd) {
			getMidlet().stopApplication(true, true);
		}
	}

	/**
	 * Asks the server to send an ECSpec
	 */
	private void refresh() {
		m_btController.sendMessage(REFRESH_COMMAND);

		m_infos.setText("Waiting for an answer...");
		String data = m_btController.receiveMessage();
		m_infos.setText("Analizing data...");

		try {
			LightECSpecParser parser = new LightECSpecParser(
					(Patrolman) getMidlet());
			LightECSpec ecSpec = parser.parseString(data);

			((Patrolman) getMidlet()).addECSpec(ecSpec);

			m_infos.setText("ECSpec received");
		} catch (Exception parseEx) {
			m_infos.setText("Error analizing data :\n" + parseEx + "\n"
					+ parseEx.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.nfc.midlet.generic.ui.Screen#setActive()
	 */
	public void setActive() {
		super.setActive();
		refresh();
	}

	/**
	 * Disconnects from the server
	 */
	private void closeConnection() {
		if (m_btController.isBluetoothConnected())
			m_btController.disconnectBluetooth(this, m_menuScreen);
	}
}
