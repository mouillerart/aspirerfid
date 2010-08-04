/**
 * 
 */
package org.ow2.aspirerfid.touchlocate.ui;

import java.io.InputStream;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.StringItem;

import org.ow2.aspirerfid.nfc.midlet.generic.GenericMidlet;
import org.ow2.aspirerfid.nfc.midlet.generic.ui.AlertScreen;
import org.ow2.aspirerfid.nfc.midlet.generic.ui.Screen;
import org.ow2.aspirerfid.nfc.midlet.generic.ui.UITools;

/**
 * Presentation screen (title + logo) Based on Andres Gomez code
 * 
 * @author Thomas Calmant
 * 
 */
public class PresentationScreen extends Screen {
	/** UI container */
	private Form m_form;

	/** Next screen command */
	private Command m_enter;

	/** Next screen */
	private Screen m_nextScreen;

	public PresentationScreen(GenericMidlet midlet, Screen nextScreen) {
		super(midlet);
		m_nextScreen = nextScreen;

		m_form = new Form("Touch'n Locate Demo");

		// Command to pass to the main menu.
		m_enter = new Command("Enter", Command.OK, 1);
		m_form.addCommand(m_enter);

		// Loads an image and adds it to the current form.
		try {
			InputStream inputStream = this.getClass().getResourceAsStream(
					"/aspire.jpg");

			m_form.append(new ImageItem(null, UITools
					.getImageInParam(inputStream), ImageItem.LAYOUT_CENTER,
					null));

		} catch (NullPointerException e) {
			getMidlet().setActiveScreen(
					new AlertScreen(getMidlet(), "Error: Logo not found."));
		}

		// Application's description.
		StringItem description = new StringItem("",
				"LIG - Adele\nThomas Calmant\n2010\nIncludes code from Team TouchKey");
		description.setLayout(Item.LAYOUT_CENTER);
		m_form.append(description);

		// Establishes the listener to this screen.
		m_form.setCommandListener(this);

		setDiplayable(m_form);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.microedition.lcdui.CommandListener#commandAction(javax.microedition
	 * .lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command command, Displayable displayable) {
		if (displayable != m_form) {
			// Invalid displayable.
			throw new RuntimeException(
					"Error: Invalid 'presentation' displayable");
		}

		// Skip command.
		if (command == m_enter) {
			getMidlet().setActiveScreen(m_nextScreen);
		} else {
			// Unknown command.
			throw new RuntimeException(
					"Error: Invalid command in 'presentation'");
		}
	}

}
