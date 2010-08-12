package org.ow2.aspirerfid.museum.midlet.ui;

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
 * This is the screen that presents the application.
 * 
 * @author <a href="mailto:angoca@yahoo.com">Andres Gomez</a>
 */
public class PresentationScreen extends Screen {

	/**
	 * Command that permits to access the main menu.
	 */
	private Command m_enter = null;

	/**
	 * Form where the application's presentation is showed.
	 */
	private Form m_presentation = null;

	/**
	 * Constructor that associates the caller midlet.
	 * 
	 * @param midlet
	 *            Midlet that calls the application.
	 */
	public PresentationScreen(GenericMidlet midlet) {
		super(midlet);

		// Form's title.
		this.m_presentation = new Form("Museum Application");

		// Command to pass to the main menu.
		this.m_enter = new Command("Enter", Command.OK, 1);
		this.m_presentation.addCommand(this.m_enter);

		// Loads an image and adds it to the current form.
		try {
			InputStream inputStream = this.getClass().getResourceAsStream(
					"/presentation.jpg");
			this.m_presentation.append(new ImageItem(null, UITools
					.getImageInParam(inputStream), ImageItem.LAYOUT_CENTER,
					null));
			InputStream stream = this.getClass().getResourceAsStream(
					"0447CB19E90280.wav");
			UITools.playSound(stream);
		} catch (NullPointerException e) {
			this
					.getMidlet()
					.setActiveScreen(
							new AlertScreen(this.getMidlet(),
									"Error: The image is not available for the presentation."));
		}

		// Application's description.
		StringItem description = new StringItem("",
				"LIG - Adele\nAndr�s G�mez\nMaroula Perisanidi\n2008");
		description.setLayout(Item.LAYOUT_CENTER);
		this.m_presentation.append(description);

		// Establishes the listener to this screen.
		this.m_presentation.setCommandListener(this);

		this.setDiplayable(this.m_presentation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command,
	 *      javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command command, Displayable displayable) {
		if (displayable != this.m_presentation) {
			// Invalid displayable.
			throw new RuntimeException(
					"Error: Invalid 'presentation' displayable");
		}

		// Skip command.
		if (command == this.m_enter) {
			this.getMidlet().setActiveScreen(new MenuScreen(this.getMidlet()));
		} else {
			// Unknown command.
			throw new RuntimeException(
					"Error: Invalid command in 'presentation'");
		}
	}

}
