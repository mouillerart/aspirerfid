package org.ow2.aspirerfid.museum.midlet.ui;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import org.ow2.aspirerfid.museum.midlet.MuseumMidlet;
import org.ow2.aspirerfid.nfc.midlet.generic.GenericMidlet;
import org.ow2.aspirerfid.nfc.midlet.generic.ui.Screen;

/**
 * This is the screen of the main menu that show the possible options of the
 * application.
 * 
 * @author <a href="mailto:angoca@yahoo.com">Andres Gomez</a>
 */
public class MenuScreen extends Screen {

	/**
	 * Exit command.
	 */
	private Command m_exitCmd = null;
	/**
	 * Name of the exit option.
	 */
	private final String m_exitName = "Exit";
	/**
	 * Name of the help option.
	 */
	private final String m_helpName = "Help";
	/**
	 * List of options of the menu.
	 */
	private List m_menuOptions = null;
	/**
	 * List of options
	 */
	private final String[] m_nameOptions = { this.m_readTagName,
			this.m_helpName, this.m_exitName };
	/**
	 * Name of the read tag option.
	 */
	private final String m_readTagName = "Read";
	/**
	 * Select option command.
	 */
	private Command m_selectCmd = null;

	/**
	 * Constructor that established the association with the midlet and creates
	 * all the elements of to show.
	 * 
	 * @param midlet
	 *            Midlet that calls the screen.
	 */
	public MenuScreen(GenericMidlet midlet) {
		super(midlet);

		this.m_menuOptions = new List("Main menu", Choice.IMPLICIT,
				this.m_nameOptions, null);
		// Select command.
		this.m_selectCmd = new Command("Select", Command.ITEM, 1);
		this.m_menuOptions.setSelectCommand(this.m_selectCmd);
		// Exit command.
		this.m_exitCmd = new Command("Exit", Command.EXIT, 1);
		this.m_menuOptions.addCommand(this.m_exitCmd);

		// Establishes the listener to this screen.
		this.m_menuOptions.setCommandListener(this);

		this.setDiplayable(this.m_menuOptions);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command,
	 *      javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command command, Displayable displayable) {
		if (displayable != this.m_menuOptions) {
			// Invalid displayable.
			throw new RuntimeException("Error: Invalid 'main menu' displayable");
		}

		if (command == this.m_selectCmd) {
			String option = this.m_nameOptions[this.m_menuOptions
					.getSelectedIndex()];
			if (option.compareTo(this.m_helpName) == 0) {
				// Help command.
				this.getMidlet().setActiveScreen(
						new HelpScreen(this.getMidlet()));
			} else if (option.compareTo(this.m_readTagName) == 0) {
				// Read tag command.
				((MuseumMidlet) this.getMidlet()).startDetecting(this);
			} else if (option.compareTo(this.m_exitName) == 0) {
				// Exit command.
				this.getMidlet().stopApplication(true, true);
			} else {
				throw new RuntimeException("Error: Invalid selected option");
			}
		} else if (command == this.m_exitCmd) {
			// Exit command.
			this.getMidlet().stopApplication(true, true);
		} else {
			// Unknown command.
			throw new RuntimeException("Error: Invalid command in 'main menu'");
		}
	}
}
