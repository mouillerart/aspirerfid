/**
 * 
 */
package org.ow2.aspirerfid.touchlocate.ui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;

import fr.touchkey.constantes.Constante;
import fr.touchkey.gui.GmapManipulationThread;
import fr.touchkey.gui.KeyLocation;

/**
 * @author Thomas Calmant
 * 
 */
public class MapCanvas extends KeyLocation {

	/** Parent {@link MapScreen} */
	private MapScreen m_parentScreen;

	/** Visible address */
	private String m_address;

	/**
	 * @param parentScreen
	 *            Parent {@link MapScreen}
	 */
	public MapCanvas(MapScreen parentScreen) {
		super();

		m_parentScreen = parentScreen;
		m_address = null;
	}

	/**
	 * Overrides KeyLocation address
	 * 
	 * @return Address to center map
	 */
	public String getAdresse() {
		return m_address;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.touchkey.gui.KeyLocation#commandAction(javax.microedition.lcdui.Command
	 * , javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command command, Displayable displayable) {
		if (command.getCommandType() == Command.BACK) {
			m_parentScreen.goBack();
			return;
		}

		super.commandAction(command, displayable);
	}

	/**
	 * Load a map centered on the address
	 * 
	 * @param address
	 *            Center of loaded map
	 */
	public void loadMap(String address) {
		// Reset display
		setMap(null);
		repaint();
		
		// Load the new map
		m_address = address;
		new GmapManipulationThread(this, Constante.GETMAP).start();
	}
}
