package org.ow2.aspirerfid.museum.midlet.ui;

import java.io.InputStream;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.Ticker;

import org.ow2.aspirerfid.museum.midlet.MuseumMidlet;
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
	/**
	 * Form that has all the elements to show.
	 */
	private Form m_form = null;
	/**
	 * Go back to menu command.
	 */
	private Command m_menuCmd = null;
	/**
	 * Screen activate when selected the selectCmd command.
	 */
	private Screen m_nextScreen = null;
	/**
	 * Screen activate when selected the secondSelectCmd command.
	 */
	private Screen m_secondNextScreen = null;
	/**
	 * Command added dynamically. This is used to activates another screen.
	 */
	private Command m_selectCmd = null;
	/**
	 * Command added dynamically. This is used to activates another screen.
	 */
	private Command m_secondSelectCmd = null;

	/**
	 * Constructor that associates the caller midlet and creates all the objects
	 * to show.
	 * 
	 * @param midlet
	 *            Caller midlet.
	 */
	public ShowScreen(GenericMidlet midlet) {
		super(midlet);

		try {
			this.m_form = new Form("Information");

			// Menu command.
			this.m_menuCmd = new Command("Menu", Command.STOP, 1);
			this.m_form.addCommand(this.m_menuCmd);

			// Establishes the listener to this screen.
			this.m_form.setCommandListener(this);

			this.setDiplayable(this.m_form);
		} catch (Exception e) {
			throw new RuntimeException("Error MusuemUI1 constructor: "
					+ e.getMessage());
		}
	}

	/**
	 * Sets a command to the screen. This change only one command in the screen.
	 * If this method is called, it does not add severals commands.
	 * 
	 * @param name
	 *            Name of the command.
	 * @param nextScreen
	 *            Screen to activate when the command is selected.
	 */
	public void addCommand(String name, Screen nextScreen) {
		try {
			this.removeCommand();

			// Next screen
			this.m_nextScreen = nextScreen;
			// Select command.
			this.m_selectCmd = new Command(name, Command.ITEM, 1);
			this.m_form.addCommand(this.m_selectCmd);

			// Establishes the listener to this screen.
			this.m_form.setCommandListener(this);

			this.setDiplayable(this.m_form);
		} catch (Exception e) {
			throw new RuntimeException("Error MusuemUI2 adding command: "
					+ e.getMessage());
		}
	}

	/**
	 * Sets a second command to the screen. This change only the second command
	 * in the screen. If this method is called, it does not add severals
	 * commands.
	 * 
	 * @param name
	 *            Name of the second command.
	 * @param nextScreen
	 *            Screen to activate when the second command is selected.
	 */
	public void addSecondCommand(String name, Screen nextScreen) {
		try {
			this.removeSecondCommand();

			// Next screen
			this.m_secondNextScreen = nextScreen;
			// Select command.
			this.m_secondSelectCmd = new Command(name, Command.ITEM, 2);
			this.m_form.addCommand(this.m_secondSelectCmd);

			// Establishes the listener to this screen.
			this.m_form.setCommandListener(this);

			this.setDiplayable(this.m_form);
		} catch (Exception e) {
			throw new RuntimeException(
					"Error MusuemUI3 adding second command: " + e.getMessage());
		}
	}

	/**
	 * Adds an image to the information.
	 * 
	 * @param stream
	 *            Stream where the image can be obtained.
	 */
	public void addImage(InputStream stream) {
		try {
			Image image = UITools.getImageInParam(stream);
			this.m_form.append(image);
		} catch (Exception e) {
			throw new RuntimeException("Error MusuemUI4 adding image: "
					+ e.getMessage());
		}

	}

	/**
	 * Adds a structured text.
	 * 
	 * @param stream
	 *            Stream where the text can be read.
	 * @param title
	 *            Title of the text.
	 */
	public void addStringItem(String title, InputStream stream) {
		try {
			StringItem string = UITools.getTextInParam(stream, title);
			this.m_form.append(string);
		} catch (Exception e) {
			throw new RuntimeException("Error MusuemUI5 adding sound: "
					+ e.getMessage());
		}
	}

	/**
	 * Adds a message with a title.
	 * 
	 * @param title
	 *            Title of the message.
	 * @param content
	 *            Content of the message.
	 */
	public void addStringItem(String title, String content) {
		try {
			this.m_form.append(new StringItem(title, content));
		} catch (Exception e) {
			throw new RuntimeException("Error MusuemUI6 adding string: "
					+ e.getMessage());
		}

	}

	/**
	 * Adds a text message.
	 * 
	 * @param message
	 *            Message to show.
	 */
	public void addText(String message) {
		try {
			this.m_form.append(message + "\n");
		} catch (Exception e) {
			throw new RuntimeException("Error MusuemUI7 adding text: "
					+ e.getMessage());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command,
	 *      javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command command, Displayable displayable) {
		try {
			if (displayable != this.m_form) {
				// Invalid displayable.
				throw new RuntimeException("Error: Invalid 'show' displayable");
			}

			// OK Command.
			if (command == this.m_selectCmd) {
				if (this.m_nextScreen != null) {
					// TODO when activating, stop nfc detection.
					this.getMidlet().setActiveScreen(this.m_nextScreen);
				} else {
					throw new RuntimeException(
							"Trying to activate a null screen.");
				}
			} else if (command == this.m_secondSelectCmd) {
				if (this.m_secondNextScreen != null) {
					// TODO when activating, stop nfc detection.
					this.getMidlet().setActiveScreen(this.m_secondNextScreen);
				} else {
					throw new RuntimeException(
							"Trying to activate a null screen.");
				}

			} else if (command == this.m_menuCmd) {
				((MuseumMidlet) this.getMidlet()).stopDetecting();
			} else {
				// Unknown command.
				throw new RuntimeException("Error: Invalid command in 'show'");
			}
		} catch (Exception e) {
			throw new RuntimeException(
					"Error MusuemUI8 command action problem: " + e.getMessage());
		}
	}

	/**
	 * Activates the gauge in the top of the information to indicate that the
	 * informations is being received.
	 * 
	 * @param active
	 *            if the gauge has to be shown.
	 */
	public void gaugeActive(boolean active) {
		try {
			if (active) {
				Gauge progress = new Gauge("", false, Gauge.INDEFINITE,
						Gauge.CONTINUOUS_RUNNING);
				progress.setLayout(Item.LAYOUT_CENTER);
				progress.setLayout(Item.LAYOUT_VCENTER);
				this.m_form.append(progress);
			} else {
				this.m_form.delete(0);
			}
		} catch (Exception e) {
			throw new RuntimeException("Error MusuemUI9 activating gauge: "
					+ e.getMessage());
		}
	}

	/**
	 * Plays a sound that receives with the given stream.
	 * 
	 * @param stream
	 *            Stream where the sound can be obtained.
	 */
	public void playSound(InputStream stream) {
		try {
			UITools.playSound(stream);
		} catch (Exception e) {
			throw new RuntimeException("Error MusuemUI10 playing sound: "
					+ e.getMessage());
		}

	}

	/**
	 * Removes the set command.
	 */
	private void removeCommand() {
		try {
			this.m_nextScreen = null;
			// Removes the current command.
			this.m_form.removeCommand(this.m_selectCmd);
		} catch (Exception e) {
			throw new RuntimeException("Error MusuemUI11 removing command: "
					+ e.getMessage());
		}

	}

	/**
	 * Removes the second command.
	 */
	private void removeSecondCommand() {
		try {
			this.m_secondNextScreen = null;
			// Removes the current command.
			this.m_form.removeCommand(this.m_secondSelectCmd);
		} catch (Exception e) {
			throw new RuntimeException(
					"Error MusuemUI12 removing second command: "
							+ e.getMessage());
		}

	}

	/**
	 * Sets the title in a ticker way,
	 * 
	 * @param message
	 *            Title of the information..
	 */
	public void setTicker(String message) {
		try {
			Ticker ticker = new Ticker(message);
			this.m_form.setTicker(ticker);
		} catch (Exception e) {
			throw new RuntimeException("Error MusuemUI13 setting ticker: "
					+ e.getMessage());
		}
	}

	/**
	 * Vibrates the telephone.
	 */
	public void vibrate() {
		try {
			Display.getDisplay(this.getMidlet()).vibrate(5000);
		} catch (Exception e) {
			throw new RuntimeException("Error MusuemUI14 vibrating: "
					+ e.getMessage());
		}

	}
}
