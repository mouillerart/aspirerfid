/**
 * 
 */
package org.ow2.aspirerfid.touchlocate.ui;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import org.ow2.aspirerfid.nfc.midlet.generic.GenericMidlet;
import org.ow2.aspirerfid.nfc.midlet.generic.ui.AlertScreen;
import org.ow2.aspirerfid.nfc.midlet.generic.ui.Screen;
import org.ow2.aspirerfid.touchlocate.GPSLocation;

/**
 * Action selection screen. Shown when a position tag has been successfully read
 * 
 * @author Thomas Calmant
 * 
 */
public class ActionScreen extends Screen {
	/** Name of the exit option. */
	private static final String m_exitName = "Exit";

	/** Exit command. */
	private static final Command m_exitCmd = new Command(m_exitName,
			Command.EXIT, 1);

	/** Name of the back option */
	private static final String m_backName = "Back";

	/** Back option */
	private static final Command m_backCmd = new Command(m_backName,
			Command.ITEM, 1);

	/** Name of the select option */
	private static final String m_selectName = "Select";

	/** Select option */
	private static final Command m_selectCmd = new Command(m_selectName,
			Command.ITEM, 1);

	/** Menu option : show on google maps */
	private static final String m_mapOption = "Show on Google Maps";

	/** Menu option : geo reversing */
	private static final String m_reverseOption = "Geo reversing";

	/** Menu option : points of interest */
	private static final String m_poiOption = "Points of interest";

	/** Menu options */
	private static final String[] m_optionNames = { m_mapOption,
			m_reverseOption, m_poiOption, m_exitName };

	/** Menu list */
	private List m_menuList;

	/** GPS location associated to the screen */
	private GPSLocation m_location;

	/**
	 * @param midlet
	 */
	public ActionScreen(GenericMidlet midlet) {
		super(midlet);

		m_menuList = new List("Tag read", Choice.IMPLICIT, m_optionNames, null);
		m_menuList.addCommand(m_backCmd);
		m_menuList.addCommand(m_exitCmd);
		m_menuList.setSelectCommand(m_selectCmd);
		m_menuList.setCommandListener(this);
		setDiplayable(m_menuList);
	}

	public void setLocation(GPSLocation location) {
		m_location = location;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.microedition.lcdui.CommandListener#commandAction(javax.microedition
	 * .lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command command, Displayable displayable) {
		// Exit command
		if (command == m_exitCmd) {
			getMidlet().stopApplication(true, true);
		}
		// Select command
		else if (command == m_selectCmd) {
			String option = m_optionNames[m_menuList.getSelectedIndex()];

			// Exit command
			if (option == m_exitName) {
				getMidlet().stopApplication(true, true);
			}
			// Open google maps command
			else if (option == m_mapOption) {
				String url = "http://www.google.com/maps/?ll=";

				// Center the map
				url += m_location.latitudeString() + ","
						+ m_location.longitudeString();

				// Add a marker
				url += "&q=TagInformation%40" + m_location.latitudeString()
						+ "," + m_location.longitudeString();

				try {
					getMidlet().platformRequest(url);
					
					/*
					 * FIXME On S40 phones, it seems we can't have our MIDlet
					 * and a browser. So we have to exit our application in
					 * order to launch the browser (Source:
					 * http://java-embedded.
					 * info/Addison.Wesley-Developing.Scalable
					 * .Series.40.Applications
					 * -A.Guide.for.Java/0321268636/ch03lev1sec1.html)
					 */
					getMidlet().notifyDestroyed();
				} catch (ConnectionNotFoundException e) {
					showAlert("Error opening URL : " + e.getMessage());
				}
			}
		}
	}

	public void showAlert(String msg) {
		AlertScreen as = new AlertScreen(getMidlet(), msg);
		as.setActive();
	}

}
