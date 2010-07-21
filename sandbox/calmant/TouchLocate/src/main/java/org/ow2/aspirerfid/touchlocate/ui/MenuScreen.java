/**
 * 
 */
package org.ow2.aspirerfid.touchlocate.ui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;

import org.ow2.aspirerfid.nfc.midlet.generic.GenericMidlet;
import org.ow2.aspirerfid.nfc.midlet.generic.ui.Screen;
import org.ow2.aspirerfid.touchlocate.TouchLocate;

/**
 * @author Thomas Calmant
 * 
 */
public class MenuScreen extends Screen {
	/** Name of the exit option. */
	private static final String m_exitName = "Exit";
	
	/** Exit command. */
	private static final Command m_exitCmd = new Command(m_exitName, Command.EXIT, 1);
	
	/** UI Container */
	private Form m_form;

	/**
	 * @param midlet
	 */
	public MenuScreen(GenericMidlet midlet) {
		super(midlet);
		
		m_form = new Form("Main menu");
		m_form.append("Waiting for a tag...");
		
		// Exit command
		m_form.addCommand(m_exitCmd);
		
		m_form.setCommandListener(this);
		setDiplayable(m_form);
		
		((TouchLocate)midlet).startDetecting(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.microedition.lcdui.CommandListener#commandAction(javax.microedition
	 * .lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command command, Displayable displayable) {
		if(command == m_exitCmd) {
			getMidlet().stopApplication(true, true);
		}
	}

}
