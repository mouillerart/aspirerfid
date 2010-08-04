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
import org.ow2.aspirerfid.touchlocate.TouchLocate;

/**
 * @author Thomas Calmant
 *
 */
public class WaitingScreen extends Screen {
	/** Exit command */
	private static final Command s_exitCmd = new Command("Exit", Command.EXIT, 0);
	
	/** Status information */
	private StringItem m_status;

	/**
	 * @param midlet Parent MIDlet
	 */
	public WaitingScreen(GenericMidlet midlet) {
		super(midlet);
		
		// Start detecting tags
		((TouchLocate)midlet).startDetecting(this);
		
		m_status = new StringItem("Status", "Waiting for a tag...");
		
		Form form = new Form("Touch'n Locate");
		form.append(m_status);
		form.addCommand(s_exitCmd);
		form.setCommandListener(this);
		
		setDiplayable(form);
	}

	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command command, Displayable displayable) {
		if(command == s_exitCmd) {
			getMidlet().stopApplication(true, true);
		}
	}
	
	public void setText(String text) {
		m_status.setText(text);
	}
}
