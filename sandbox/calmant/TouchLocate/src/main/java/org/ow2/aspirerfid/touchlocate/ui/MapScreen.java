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
import org.ow2.aspirerfid.nfc.midlet.generic.GenericMidlet;
import org.ow2.aspirerfid.nfc.midlet.generic.ui.Screen;

/**
 * Displays a Google Map canvas
 * 
 * @author Thomas Calmant
 */
public class MapScreen extends Screen {

	/** Previous screen */
	private Screen m_previousScreen;

	/** GMap canvas */
	private MapCanvas m_mapCanvas;

	/**
	 * @param midlet
	 *            Parent MIDlet
	 */
	public MapScreen(GenericMidlet midlet, Screen previousScreen) {
		super(midlet);
		m_previousScreen = previousScreen;

		m_mapCanvas = new MapCanvas(this);
		setDiplayable(m_mapCanvas);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.microedition.lcdui.CommandListener#commandAction(javax.microedition
	 * .lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command command, Displayable displayable) {
		// No action at this level
	}

	/**
	 * Changes the map center address
	 * 
	 * @param address
	 *            The new address / GPS coordinates
	 */
	public void setAddress(String address) {
		m_mapCanvas.loadMap(address);
	}

	/**
	 * Changes the map center address
	 * 
	 * @param latitude
	 *            Location latitude
	 * @param longitude
	 *            Location longitude
	 */
	public void setAddress(double latitude, double longitude) {
		m_mapCanvas.loadMap(Double.toString(latitude) + ','
				+ Double.toString(longitude));
	}

	/**
	 * Activates the previous menu
	 */
	protected void goBack() {
		getMidlet().setActiveScreen(m_previousScreen);
	}
}
