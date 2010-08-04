/**
 * 
 */
package org.ow2.aspirerfid.touchlocate.ui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;

import org.ow2.aspirerfid.nfc.midlet.generic.GenericMidlet;
import org.ow2.aspirerfid.nfc.midlet.generic.ui.Screen;

/**
 * @author Thomas Calmant
 * 
 */
public class InformationScreen extends Screen {

	/** Back command */
	private static final Command s_backCmd = new Command("Back", Command.BACK,
			0);

	/** Previous screen */
	private Screen m_previousScreen;

	/** Information item */
	private StringItem m_infos;

	/**
	 * @param midlet
	 *            Parent MIDlet
	 * @param previousScreen
	 *            Screen shown with Back command
	 */
	public InformationScreen(GenericMidlet midlet, Screen previousScreen) {
		super(midlet);
		m_previousScreen = previousScreen;

		m_infos = new StringItem(null, null);

		Form form = new Form("Information");
		form.append(m_infos);
		form.addCommand(s_backCmd);
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
		if (command == s_backCmd) {
			getMidlet().setActiveScreen(m_previousScreen);
			m_infos.setText("");
		}
	}

	public void setText(String text) {
		m_infos.setText(text);
	}
}
