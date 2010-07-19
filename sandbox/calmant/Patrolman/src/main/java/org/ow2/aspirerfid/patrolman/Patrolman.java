/**
 * 
 */
package org.ow2.aspirerfid.patrolman;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import javax.microedition.midlet.MIDletStateChangeException;

import org.ow2.aspirerfid.nfc.midlet.generic.GenericMidlet;
import org.ow2.aspirerfid.nfc.midlet.generic.ui.AlertScreen;
import org.ow2.aspirerfid.nfc.midlet.generic.ui.Screen;
import org.ow2.aspirerfid.nfc.midlet.sendersReceivers.bluetooth.BluetoothControlerUser;
import org.ow2.aspirerfid.nfc.midlet.sendersReceivers.bluetooth.BluetoothController;
import org.ow2.aspirerfid.patrolman.ecspec.LightECReportSpec;
import org.ow2.aspirerfid.patrolman.ecspec.LightECSpec;
import org.ow2.aspirerfid.patrolman.ecspec.LightECSpecParser;
import org.ow2.aspirerfid.patrolman.questionnaire.Questionnaire;
import org.ow2.aspirerfid.patrolman.ui.MenuScreen;
import org.ow2.aspirerfid.patrolman.ui.PresentationScreen;

/**
 * @author Thomas Calmant
 * 
 */
public class Patrolman extends GenericMidlet implements BluetoothControlerUser {

	/** Bluetooth connection controller */
	private BluetoothController m_btController;
	
	/** Menu screen */
	private MenuScreen m_menuScreen; 

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	protected void startApp() throws MIDletStateChangeException {
		m_btController = new BluetoothController(this);
		
		m_menuScreen = new MenuScreen(this);
		setActiveScreen(new PresentationScreen(this));
	}
	
	/**
	 * Activates the menu screen
	 */
	public void showMenuScreen() {
		m_menuScreen.setActive();
	}

	/**
	 * Searches for bluetooth server
	 */
	public void startBluetoothDetection(Screen previousScreen) {
		// TODO: next screen (waiting for data)
		m_btController.connectBluetooth(previousScreen, m_menuScreen);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.nfc.midlet.sendersReceivers.bluetooth.
	 * BluetoothControlerUser#informConnected()
	 */
	public void informConnected() {
		// TODO: show a 'waiting for spec' screen

		// Parser / Questionnaire test
		StringBuffer data = new StringBuffer();
		InputStream is = Patrolman.class.getResourceAsStream("/text.xml");
		DataInputStream dis = new DataInputStream(is);
		if (dis == null || is == null) {
			AlertScreen as = new AlertScreen(this, "File not found...");
			as.setActive();
			return;
		}new MenuScreen(this)

		try {
			while (true) {
				data.append((char) dis.readByte());
			}
		} catch (EOFException e) {
			// do nothing
		} catch (IOException e) {
			AlertScreen as = new AlertScreen(this, "File not found...");
			as.setActive();
			return;
		}

		LightECSpecParser parser = new LightECSpecParser(this);
		try {
			LightECSpec spec = parser.parseString(data.toString());
			LightECReportSpec report = spec.getReportSpec("ReportTest");

			setActiveScreen(report.getQuestionnaire());
		} catch (Exception e) {
			System.out.println("Exception : " + e);
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.nfc.midlet.sendersReceivers.bluetooth.
	 * BluetoothControlerUser#informDisonnected()
	 */
	public void informDisonnected() {
		// TODO: show info on UI
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
		String correctedXML = qst.toXML(); //.replace('\n', ' ');
		try {
			m_btController.sendMessage(correctedXML);

			// TODO: show error/success on UI
			AlertScreen as = new AlertScreen(this, "Data sent");
			as.setActive();
		} catch (Exception e) {
			AlertScreen as = new AlertScreen(this, "Error sending message : "
					+ e.getMessage());
			as.setActive();
		}
	}
}
