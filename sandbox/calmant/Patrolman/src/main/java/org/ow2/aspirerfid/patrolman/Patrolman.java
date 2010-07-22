/**
 * 
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
	 * Generates an ECReport XML file from the given ECSpec The XML file will
	 * contain all reports with all their associated questionnaires
	 * 
	 * @param ecSpec
	 *            The base ECSpec
	 * @return An XML file content
	 */
	private void sendECReportXMLFile(LightECSpec ecSpec) {
		String date = new Date(System.currentTimeMillis()).toString();

		// Beginning of XML File
		/*
		 * xmlFile.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
		 * .append("<ale:ECReports xmlns:ale=\"urn:epcglobal:ale:xsd:1\"\n")
		 * .append("xmlns:epcglobal=\"urn:epcglobal:xsd:1\"\n")
		 * .append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n")
		 * .append("schemaVersion=\"1.0\"\n").append("specName=\"")
		 * .append(ecSpec.getName()).append("\"\ndate=\"").append(date)
		 * .append("\"\nALEID=\"Patrolman\"\n")
		 * .append("totalMilliseconds=\"0\">\n<reports>\n");
		 */

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
		// Do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.nfc.midlet.sendersReceivers.bluetooth.
	 * BluetoothControlerUser#informDisonnected()
	 */
	public void informDisonnected() {
		// Do nothing
	}

	/**
	 * Prepares an ECReport and sends it over BlueTooth (!! WARNING: heavy
	 * memory consumption !!)
	 * 
	 * @param questionnaire
	 *            Questionnaire to be sent in the ECReport
	 */
	/*
	 * public void sendECReport(Questionnaire questionnaire) { String
	 * ecReportData = questionnaire.getReportSpec() .toXML(questionnaire);
	 * 
	 * try { m_btController.sendMessage(ecReportData.replace('\n', ' '));
	 * 
	 * AlertScreen as = new AlertScreen(this, "Data sent"); as.setActive(); }
	 * catch (Exception e) { AlertScreen as = new AlertScreen(this,
	 * "Error sending message : " + e.getMessage()); as.setActive(); } }
	 */

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
		m_waitingScreen = new WaitingECSpec(this, m_btController, m_menuScreen);

		setActiveScreen(new PresentationScreen(this));
	}

	/**
	 * Searches for bluetooth server
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
	 * Submits all questionnaires to the server. If the phone is not connected
	 * to a server, connects to a user-selected one
	 */
	public void submitAllQuestionnaires() {
		if (!m_btController.isBluetoothConnected()) {
			m_btController.connectBluetooth(m_menuScreen, m_menuScreen);
		}

		// Loop through ECSpecs
		Enumeration spec_enum = m_ecSpecs.elements();
		while (spec_enum.hasMoreElements()) {
			sendECReportXMLFile((LightECSpec) spec_enum.nextElement());
		}

		showMessage("All reports sent");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.aspirerfid.nfc.midlet.reader.TagDetector#tagRead(org.ow2.aspirerfid
	 * .nfc.midlet.generic.RequestMessage)
	 */
	public void tagRead(RequestMessage rawMessage) {
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

			if (qst != null) {
				qst.loadQuestionnaire(message.getTagUID());
				setActiveScreen(qst);
			}

			if (message.raisedException()) {
				showMessage("Incomplete reading :"
						+ message.getThrownException());
			}

			if (qst != null)
				return;
		}
	}
}
