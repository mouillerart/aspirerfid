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
import org.ow2.aspirerfid.touchlocate.nfc.TagReaderThread;
import org.ow2.aspirerfid.touchlocate.ui.ActionScreen;
import org.ow2.aspirerfid.touchlocate.ui.MenuScreen;
import org.ow2.aspirerfid.touchlocate.ui.PresentationScreen;
import org.ow2.aspirerfid.touchlocate.ui.ShowScreen;

/**
 * Touch'n Locate mobile demonstration Reads an NFC position tag then let the
 * user choose which action to do : launch Google Maps, retrieve current
 * address...
 * 
 * @author Thomas Calmant
 * 
 */
public class TouchLocate extends GenericMidlet implements TagDetector {

	/** Result screen */
	private ShowScreen m_showScreen;
	
	/** Menu screen */
	private MenuScreen m_menuScreen;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	protected void startApp() throws MIDletStateChangeException {
		m_menuScreen = new MenuScreen(this);
		m_showScreen = new ShowScreen(this, m_menuScreen);
		
		// setActiveScreen(new PresentationScreen(this, m_menuScreen));
		GPSLocation loc = new GPSLocation();
		loc.latitude = 12.5;
		loc.longitude = 45.12;
		
		ActionScreen as = new ActionScreen(this);
		as.setLocation(loc);
		as.setActive();
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
		setActiveScreen(m_menuScreen);
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
		m_showScreen.setText(msg.toString());
		setActiveScreen(m_showScreen);
	}

}
