package org.ow2.aspirerfid.museum.midlet.ui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.StringItem;

import org.ow2.aspirerfid.nfc.midlet.generic.GenericMidlet;
import org.ow2.aspirerfid.nfc.midlet.generic.ui.Screen;

/**
 * This class has the help to understand how the application works.
 * 
 * @author <a href="mailto:angoca@yahoo.com">Andres Gomez</a>
 */
public class HelpScreen extends Screen {

	/**
	 * Command that goes back to the main menu.
	 */
	private Command m_back = null;
	/**
	 * Form where the help is presented.
	 */
	private Form m_help = null;

	/**
	 * Constructor of the instruction interfaces that associates the caller
	 * midlet.
	 * 
	 * @param midlet
	 *            Caller midlet.
	 */
	public HelpScreen(GenericMidlet midlet) {
		super(midlet);

		this.m_help = new Form("Instructions");

		// Application's description.
		StringItem description = new StringItem(
				"",
				"This application permits the interaction between an artwork "
						+ "and its history.\nEach artwork of the museum has an "
						+ "RFID tag associated and when it is touched the "
						+ "application connects to the local servers to obtain "
						+ "more information");
		description.setLayout(Item.LAYOUT_LEFT);
		this.m_help.append(description);

		// Commands
		StringItem commandsDescription = new StringItem(
				"Use",
				"This is the instruction command.\n"
						+ "Start the application, and select read. The "
						+ "application will find the near Bluetooth servers, "
						+ "and it will try to connect to one of them. Once it "
						+ "is connected, you can touch a tag with the "
						+ "telephone (rear upper side) and wait for the "
						+ "information.\nWhen beginning, you will be asked for "
						+ "a profile. For each artwork you can tell us you "
						+ "opinion. And when leaving the musuem you will be "
						+ "free to give us some comments.");
		commandsDescription.setLayout(Item.LAYOUT_LEFT);
		this.m_help.append(commandsDescription);

		// Command to go back to the main menu.
		this.m_back = new Command("Back", Command.BACK, 1);
		this.m_help.addCommand(this.m_back);

		// Establishes the listener to this screen.
		this.m_help.setCommandListener(this);

		this.setDiplayable(this.m_help);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command,
	 *      javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command command, Displayable displayable) {
		if (displayable != this.m_help) {
			// Invalid displayable.
			throw new RuntimeException(
					"Error: Invalid 'instructions' displayable");
		}

		// Go back command.
		if (command == this.m_back) {
			this.getMidlet().setActiveScreen(new MenuScreen(this.getMidlet()));
		} else {
			// Unknown command.
			throw new RuntimeException(
					"Error: Invalid command in 'instructions'");
		}
	}
}
