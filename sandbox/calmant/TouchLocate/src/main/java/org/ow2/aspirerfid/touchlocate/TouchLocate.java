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
package org.ow2.aspirerfid.touchlocate;

import javax.microedition.contactless.TargetProperties;
import javax.microedition.midlet.MIDletStateChangeException;

import org.ow2.aspirerfid.nfc.midlet.generic.GenericMidlet;
import org.ow2.aspirerfid.nfc.midlet.generic.NFCMidletException;
import org.ow2.aspirerfid.nfc.midlet.generic.ReaderThread;
import org.ow2.aspirerfid.nfc.midlet.generic.RequestMessage;
import org.ow2.aspirerfid.nfc.midlet.generic.ui.AlertScreen;
import org.ow2.aspirerfid.nfc.midlet.generic.ui.Screen;
import org.ow2.aspirerfid.nfc.midlet.reader.TagDetector;
import org.ow2.aspirerfid.nfc.midlet.reader.rfid.RFIDDetector;
import org.ow2.aspirerfid.touchlocate.nfc.TagLocationMessage;
import org.ow2.aspirerfid.touchlocate.nfc.TagReaderThread;
import org.ow2.aspirerfid.touchlocate.ui.MenuScreen;
import org.ow2.aspirerfid.touchlocate.ui.PresentationScreen;
import org.ow2.aspirerfid.touchlocate.ui.WaitingScreen;

/**
 * Touch'n Locate mobile demonstration Reads an NFC position tag then let the
 * user choose which action to do : launch Google Maps, retrieve current
 * address...
 * 
 * @author Thomas Calmant
 * 
 */
public class TouchLocate extends GenericMidlet implements TagDetector {

	/** Tag waiting screen */
	private WaitingScreen m_waitScreen;

	/** Tag handling screen */
	private MenuScreen m_menuScreen;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	protected void startApp() throws MIDletStateChangeException {
		m_waitScreen = new WaitingScreen(this);
		m_menuScreen = new MenuScreen(this, m_waitScreen);

		setActiveScreen(new PresentationScreen(this, m_waitScreen));
	}

	/**
	 * Starts detecting RFID tags.
	 * 
	 * @param previousScreen
	 *            Screen to return if connection fail.
	 */
	public void startDetecting(Screen previousScreen) {
		// Start RFID detector
		try {
			startDetector(new RFIDDetector(this));
		} catch (NFCMidletException e) {
			setActiveScreen(new AlertScreen(this,
					"There was a problem stablishing the tag listener.", true));
		}
	}

	/**
	 * Stops the RFID detection and show the main screen.
	 */
	public void stopDetecting() {
		stopDetector();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.aspirerfid.nfc.midlet.reader.TagDetector#startReaderThread(javax
	 * .microedition.contactless.TargetProperties[])
	 */
	public ReaderThread startReaderThread(TargetProperties[] props) {
		return new TagReaderThread(props, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.aspirerfid.nfc.midlet.reader.TagDetector#tagRead(org.ow2.aspirerfid
	 * .nfc.midlet.generic.RequestMessage)
	 */
	public void tagRead(RequestMessage msg) {

		if (!(msg instanceof TagLocationMessage)) {
			m_waitScreen.setText("Not a location tag");
			return;
		}

		TagLocationMessage location = (TagLocationMessage) msg;
		if (!location.isValid()) {
			m_waitScreen.setText("Invalid location tag");
			return;
		}

		// // Useful if you debug the project on the emulator TagLocationMessage
		// location = new TagLocationMessage(this);
		// location.setLocation("U",
		// new String("_loc/45.187778/5.726945").getBytes());

		m_menuScreen.setLocationMessage(location);

		setActiveScreen(m_menuScreen);
		m_waitScreen.setText("Waiting for a tag...");
	}
}
