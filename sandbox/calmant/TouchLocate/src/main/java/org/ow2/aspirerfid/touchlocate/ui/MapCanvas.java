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

import fr.touchkey.constantes.Constante;
import fr.touchkey.gui.GmapManipulationThread;
import fr.touchkey.gui.KeyLocation;

/**
 * Draws a Google Map static image. Handles moving and zooming.
 * 
 * @author Thomas Calmant
 * 
 *         TODO cache images in memory (~60 kb/image) to speed up UI (10 seconds
 *         freeze per image loading)
 */
public class MapCanvas extends KeyLocation {

	/** Back command */
	private static final Command s_backCmd = new Command("Back", Command.BACK,
			0);

	/** Parent {@link MapScreen} */
	private MapScreen m_parentScreen;

	/** Visible address */
	private String m_address;

	/**
	 * @param parentScreen
	 *            Parent {@link MapScreen}
	 */
	public MapCanvas(MapScreen parentScreen) {
		super();

		m_parentScreen = parentScreen;
		m_address = null;

		addCommand(s_backCmd);
		setCommandListener(this);
	}

	/**
	 * Overrides KeyLocation address
	 * 
	 * @return Location of map center
	 */
	public String getAdresse() {
		return m_address;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.touchkey.gui.KeyLocation#commandAction(javax.microedition.lcdui.Command
	 * , javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command command, Displayable displayable) {
		if (command == s_backCmd) {
			m_parentScreen.goBack();
			return;
		}

		super.commandAction(command, displayable);
	}

	/**
	 * Load a map centered on the address
	 * 
	 * @param address
	 *            Center of loaded map
	 */
	public void loadMap(String address) {
		// Reset display
		setMap(null);
		repaint();

		// Load the new map
		m_address = address;
		new GmapManipulationThread(this, Constante.GETMAP).start();
	}
}
