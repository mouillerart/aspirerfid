/**
 * 
 */
package org.ow2.aspirerfid.touchlocate.ui;

import fr.touchkey.gui.KeyLocation;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;

import org.ow2.aspirerfid.nfc.midlet.generic.GenericMidlet;
import org.ow2.aspirerfid.nfc.midlet.generic.ui.Screen;

/**
 * @author Thomas Calmant
 *
 */
public class MapScreen extends Screen {
	private static final Command s_exitCmd = new Command("Exit", Command.EXIT, 0);

	/**
	 * @param midlet
	 */
	public MapScreen(GenericMidlet midlet) {
		super(midlet);
		
		Form f = new Form("Test GMap"); 
		f.addCommand(s_exitCmd);
		f.setCommandListener(this);
		
		// setDiplayable(f);
		
		// TODO: use parametric address
		KeyLocation map = new KeyLocation(getMidlet(), null, "47.32167,5.04139");
		setDiplayable(map);
	}

	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command command, Displayable displayable) {
		if(command == s_exitCmd) {
			getMidlet().stopApplication(true, true);
		}
	}
}
