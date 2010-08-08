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
package org.ow2.aspirerfid.patrolman;

import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

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
import org.ow2.aspirerfid.nfc.midlet.sendersReceivers.bluetooth.BluetoothControlerUser;
import org.ow2.aspirerfid.nfc.midlet.sendersReceivers.bluetooth.BluetoothController;
import org.ow2.aspirerfid.patrolman.ecspec.LightECReportSpec;
import org.ow2.aspirerfid.patrolman.ecspec.LightECSpec;
import org.ow2.aspirerfid.patrolman.nfc.TagReaderMessage;
import org.ow2.aspirerfid.patrolman.nfc.TagReaderThread;
import org.ow2.aspirerfid.patrolman.questionnaire.Questionnaire;
import org.ow2.aspirerfid.patrolman.ui.MenuScreen;
import org.ow2.aspirerfid.patrolman.ui.PresentationScreen;
import org.ow2.aspirerfid.patrolman.ui.SubmitScreen;
import org.ow2.aspirerfid.patrolman.ui.WaitingECSpec;

/**
 * Patrolman MIDlet entry point
 * 
 * @author Thomas Calmant
 */
public class Patrolman extends GenericMidlet implements BluetoothControlerUser,
		TagDetector {

	/** Bluetooth connection controller */
	private BluetoothController m_btController;

	/** Menu screen */
	private MenuScreen m_menuScreen;

	/** Questionnaire submission screen */
	private SubmitScreen m_submitScreen;

	/** ECSpecs */
	private Vector m_ecSpecs;

	/** ECSpec waiting screen */
	private WaitingECSpec m_waitingScreen;

	/**
	 * Adds a read ECSpec to the vector
	 * 
	 * @param ecspec
	 *            The ECSpec to be added
	 */
	public void addECSpec(LightECSpec ecspec) {
		m_ecSpecs.addElement(ecspec);
	}

	/**
	 * Sends an ECReport XML file from the given ECSpec over BlueTooth.The XML
	 * file will contain all reports with all their associated questionnaires
	 * 
	 * @param ecSpec
	 *            The base ECSpec
	 */
	private void sendECReportXMLFile(LightECSpec ecSpec) {
		String date = new Date(System.currentTimeMillis()).toString();

		// Beginning of XML File.
		// Flush it
		m_btController
				.sendMessage("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
						+ "<ale:ECReports xmlns:ale=\"urn:epcglobal:ale:xsd:1\"\n"
						+ "xmlns:epcglobal=\"urn:epcglobal:xsd:1\"\n"
						+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
						+ "schemaVersion=\"1.0\"\n" + "specName=\""
						+ ecSpec.getName() + "\"\ndate=\"" + date
						+ "\"\nALEID=\"Patrolman\"\n"
						+ "totalMilliseconds=\"0\">\n<reports>\n");

		// Add ECReports
		Enumeration ecReports = ecSpec.getReportSpecs();
		while (ecReports.hasMoreElements()) {
			LightECReportSpec ecReport = (LightECReportSpec) ecReports
					.nextElement();
			ecReport.sendXML(m_btController);
		}

		// End of the file
		m_btController.sendMessage("</reports>\n</ale:ECReports>\n");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.nfc.midlet.sendersReceivers.bluetooth.
	 * BluetoothControlerUser#informConnected()
	 */
	public void informConnected() {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.nfc.midlet.sendersReceivers.bluetooth.
	 * BluetoothControlerUser#informDisonnected()
	 */
	public void informDisonnected() {
		// do nothing
	}

	/**
	 * Activates the menu screen
	 */
	public void showMenuScreen() {
		setActiveScreen(m_menuScreen);
	}

	/**
	 * Pops up an AlertScreen
	 * 
	 * @param message
	 *            Message to be shown
	 */
	private void showMessage(String message) {
		AlertScreen as = new AlertScreen(this, message);
		as.setActive();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	protected void startApp() throws MIDletStateChangeException {
		m_ecSpecs = new Vector();
		m_btController = new BluetoothController(this);
		m_menuScreen = new MenuScreen(this);
		m_submitScreen = new SubmitScreen(this, m_menuScreen);
		m_waitingScreen = new WaitingECSpec(this, m_btController, m_menuScreen);

		setActiveScreen(new PresentationScreen(this));
	}

	/**
	 * Searches for bluetooth servers and ask the user to connect one of them
	 */
	public void startBluetoothDetection(Screen previousScreen) {
		if (m_btController.isBluetoothConnected()) {
			setActiveScreen(m_waitingScreen);
		} else {
			m_btController.connectBluetooth(previousScreen, m_waitingScreen);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.aspirerfid.nfc.midlet.reader.TagDetector#startReaderThread(javax
	 * .microedition.contactless.TargetProperties[])
	 */
	public ReaderThread startReaderThread(TargetProperties[] properties) {
		return new TagReaderThread(properties, this);
	}

	/**
	 * Starts the RFID detection
	 */
	public boolean startTagDetection() {
		try {
			startDetector(new RFIDDetector(this));
			return true;
		} catch (NFCMidletException e) {
			setActiveScreen(new AlertScreen(this,
					"There was a problem stablishing the tag listener.", true));
		}

		return false;
	}

	/**
	 * Stops the RFID detection and return to the menu screen.
	 */
	public void stopTagDetection() {
		stopDetector();
		showMenuScreen();
	}

	/**
	 * Shows the questionnaires submission screen, which automatically submits
	 * all questionnaires.
	 * 
	 * If the phone is not connected to a server, a connection screen is shown
	 * before the submission screen.
	 */
	public void connectSubmitAllQuestionnaires() {
		if (!m_btController.isBluetoothConnected()) {
			m_btController.connectBluetooth(m_menuScreen, m_submitScreen);
		} else {
			setActiveScreen(m_submitScreen);
		}
	}

	/**
	 * Submits all questionnaires to the server. The phone must be connected to
	 * a server.
	 */
	public void submitAllQuestionnaires() {
		// Loop through ECSpecs
		Enumeration spec_enum = m_ecSpecs.elements();
		while (spec_enum.hasMoreElements()) {
			sendECReportXMLFile((LightECSpec) spec_enum.nextElement());
		}
	}

	/**
	 * Called when a tag has been read.
	 * 
	 * @param rawMessage
	 *            Data read from the tag (can be incomplete) Must be a
	 *            {@link TagReaderMessage} object
	 * 
	 * @see org.ow2.aspirerfid.nfc.midlet.reader.TagDetector#tagRead(org.ow2.aspirerfid
	 *      .nfc.midlet.generic.RequestMessage)
	 */
	public void tagRead(RequestMessage rawMessage) {
		// Wait for our special type of message
		if (!(rawMessage instanceof TagReaderMessage)) {
			showMessage("Error: not a valid Tag message");
			return;
		}

		TagReaderMessage message = (TagReaderMessage) rawMessage;
		Enumeration elems = m_ecSpecs.elements();

		while (elems.hasMoreElements()) {
			LightECSpec spec = (LightECSpec) elems.nextElement();
			Questionnaire qst = spec.findAssociatedQuestionnaire(message
					.getTagUID());

			if (qst == null) {
				qst = spec
						.findAssociatedQuestionnaire(message.getRecordsType());
			}

			// Questionnaire found, activate it
			if (qst != null) {
				qst.loadQuestionnaire(message.getTagUID());
				setActiveScreen(qst);

				// Pops up an alert message if the tag couldn't be completely
				// read
				// (not an "important" error, since tag's UID has been read)
				if (message.raisedException()) {
					showMessage("Incomplete reading :"
							+ message.getThrownException());
				}

				// Stop searching for a valid questionnaire
				return;
			}
		}

		// No questionnaire found
		showMessage("No questionnaire found for this tag ("
				+ message.getTagUID() + ")");
	}
	
	/**
	 * Disconnect from BlueTooth server
	 */
	public void disconnectBluetooth() {
		if(m_btController.isBluetoothConnected()) {
			m_btController.disconnectBluetooth(m_menuScreen, m_menuScreen);
		} else {
			showMessage("Not connected");
		}
	}
}
