/**
 * 
 */
package org.ow2.aspirerfid.patrolman;

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
import org.ow2.aspirerfid.patrolman.nfc.TagReaderThread;
import org.ow2.aspirerfid.patrolman.questionnaire.Questionnaire;
import org.ow2.aspirerfid.patrolman.ui.MenuScreen;
import org.ow2.aspirerfid.patrolman.ui.PresentationScreen;
import org.ow2.aspirerfid.patrolman.ui.WaitingECSpec;

/**
 * @author Thomas Calmant
 * 
 */
public class Patrolman extends GenericMidlet implements BluetoothControlerUser,
		TagDetector {

	/** Bluetooth connection controller */
	private BluetoothController m_btController;

	/** Menu screen */
	private MenuScreen m_menuScreen;
	
	/** ECSpecs */
	private Vector m_ecSpecs;

	/** ECSpec waiting screen */
	private WaitingECSpec m_waitingScreen;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	protected void startApp() throws MIDletStateChangeException {
		m_ecSpecs = new Vector();
		m_btController = new BluetoothController(this);
		m_menuScreen = new MenuScreen(this);
		m_waitingScreen = new WaitingECSpec(this, m_btController, m_menuScreen);
		
		setActiveScreen(new PresentationScreen(this));
	}

	/**
	 * Activates the menu screen
	 */
	public void showMenuScreen() {
		m_menuScreen.setActive();
	}

	/**
	 * Starts RFID detection
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
	 * Stops the RFID detection and show the main screen.
	 */
	public void stopTagDetection() {
		stopDetector();
		setActiveScreen(m_menuScreen);
	}

	/**
	 * Searches for bluetooth server
	 */
	public void startBluetoothDetection(Screen previousScreen) {
		if(m_btController.isBluetoothConnected())
			m_waitingScreen.setActive();
		else {
			m_btController.connectBluetooth(previousScreen, m_waitingScreen);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.nfc.midlet.sendersReceivers.bluetooth.
	 * BluetoothControlerUser#informConnected()
	 */
	public void informConnected() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.nfc.midlet.sendersReceivers.bluetooth.
	 * BluetoothControlerUser#informDisonnected()
	 */
	public void informDisonnected() {
	}
	
	/**
	 * Adds a read ECSpec to the vector
	 * @param ecspec The ECSpec to be added
	 */
	public void addECSpec(LightECSpec ecspec) {
		m_ecSpecs.addElement(ecspec);
	}

	/**
	 * Prepares an ECReport and sends it over BlueTooth
	 * 
	 * @param ecSpec
	 *            ECReport specifications
	 */
	public void sendECReport(LightECReportSpec ecSpec) {
		Questionnaire qst = ecSpec.getQuestionnaire();

		// TODO: generate a real ECReport
		String correctedXML = qst.toXML();
		try {
			m_btController.sendMessage(correctedXML);

			AlertScreen as = new AlertScreen(this, "Data sent");
			as.setActive();
		} catch (Exception e) {
			AlertScreen as = new AlertScreen(this, "Error sending message : "
					+ e.getMessage());
			as.setActive();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.aspirerfid.nfc.midlet.reader.TagDetector#tagRead(org.ow2.aspirerfid
	 * .nfc.midlet.generic.RequestMessage)
	 */
	public void tagRead(RequestMessage message) {
		Enumeration elems = m_ecSpecs.elements();
		
		while(elems.hasMoreElements()) {
			LightECSpec spec = (LightECSpec) elems.nextElement();
			Questionnaire qst = spec.findAssociatedQuestionnaire(message.getTagUID());
			if(qst != null) {
				qst.setActive();
				return;
			}
		}
	}
}
