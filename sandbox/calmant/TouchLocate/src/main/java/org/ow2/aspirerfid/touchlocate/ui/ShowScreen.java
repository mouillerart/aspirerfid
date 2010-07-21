package org.ow2.aspirerfid.touchlocate.ui;

import java.io.InputStream;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;

import org.ow2.aspirerfid.nfc.midlet.generic.GenericMidlet;
import org.ow2.aspirerfid.nfc.midlet.generic.ui.Screen;
import org.ow2.aspirerfid.nfc.midlet.generic.ui.UITools;

/**
 * This screen shows the received content from the server. The content can be in
 * the form of text, image or sound.
 * 
 * @author <a href="mailto:angoca@yahoo.com">Andres Gomez</a>
 */
public class ShowScreen extends Screen {
	/** Name of the exit option. */
	private static final String m_exitName = "Exit";
	
	/** Exit command. */
	private static final Command m_exitCmd = new Command(m_exitName, Command.EXIT, 1);
	
	/** Name of the back option */
	private static final String m_backName = "Back";
	
	/** Back option */
	private static final Command m_backCmd = new Command(m_backName, Command.ITEM, 1);
	
	/** Form that has all the elements to show. */
	private Form m_form;
	
	/** Informational text */
	private StringItem m_infos;

	/**
	 * Command added dynamically. This is used to activates another screen.
	 */
	private Command m_selectCmd;
	
	private Screen m_previousScreen;

	/**
	 * Constructor that associates the caller midlet and creates all the objects
	 * to show.
	 * 
	 * @param midlet
	 *            Caller midlet.
	 */
	public ShowScreen(GenericMidlet midlet, Screen previousScreen) {
		super(midlet);
		m_previousScreen = previousScreen;
		
		m_form = new Form("Information");
		
		m_infos = new StringItem("Data :", "");
		m_form.append(m_infos);
		
		// Exit command
		m_form.addCommand(m_exitCmd);

		// Menu command.
		m_form.addCommand(m_backCmd);

		// Establishes the listener to this screen.
		m_form.setCommandListener(this);

		setDiplayable(m_form);
	}

	/**
	 * Sets the information message.
	 * 
	 * @param message
	 *            Message to show.
	 */
	public void setText(String message) {
		m_infos.setText(message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command,
	 *      javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command command, Displayable displayable) {
		try {
			/*
			if (displayable != m_form) {
				// Invalid displayable.
				throw new RuntimeException("Error: Invalid 'show' displayable");
			}
			*/

			// OK Command.
			if(command == m_exitCmd) {
				getMidlet().stopApplication(true, true);
			}
			else if(command == m_backCmd) {
				if(m_previousScreen != null) {
					getMidlet().setActiveScreen(m_previousScreen);
				}
			}
			else if (command == m_selectCmd) {
				// TODO
			} else {
				// Unknown command.
				throw new RuntimeException("Error: Invalid command in 'show'");
			}
		} catch (Exception e) {
			throw new RuntimeException(
					"Error MusuemUI8 command action problem: " + e.getMessage());
		}
	}
}
