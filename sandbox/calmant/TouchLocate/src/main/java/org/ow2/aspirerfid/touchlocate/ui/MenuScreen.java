/*
 *  Copyright (C) Aspire
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.ow2.aspirerfid.touchlocate.ui;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import org.ow2.aspirerfid.nfc.midlet.generic.GenericMidlet;
import org.ow2.aspirerfid.nfc.midlet.generic.ui.Screen;
import org.ow2.aspirerfid.touchlocate.nfc.TagLocationMessage;

import fr.touchkey.gui.GoogleMaps;

/**
 * Proposes actions to the user. Actions success depends on location tag data.
 *
 * @author Thomas Calmant
 */
public class MenuScreen extends Screen {

    /** Exit command. */
    private static final Command s_backCmd = new Command("Back", Command.BACK,
            0);

    /** Select command */
    private static final Command s_selectCmd = new Command("Select",
            Command.ITEM, 0);

    /** Show on map option */
    private static final String s_showOnMap = "Show on Google Maps";

    /** Reverse geocode address */
    private static final String s_geocode = "Reverse geocode";

    /** Near points of interest */
    private static final String s_poi = "Points of interest";

    /** Show position information */
    private static final String s_infos = "Show tag informations";

    /** Menu options list */
    private static final String[] s_optionsNames = { s_showOnMap, s_geocode,
            s_poi, s_infos };

    /** Previous screen */
    private Screen m_previousScreen;

    /** Information screen */
    private InformationScreen m_infosScreen;

    /** POI search screen */
    private POIScreen m_poiScreen;

    /** Map screen */
    private MapScreen m_mapScreen;

    /** Menu list */
    private List m_optionsList;

    /** Working location message */
    private TagLocationMessage m_locationMsg;

    /**
     * @param midlet
     *            Parent MIDlet
     * @param previousScreen
     *            Screen shown on back command
     */
    public MenuScreen(GenericMidlet midlet, Screen previousScreen) {
        super(midlet);
        m_previousScreen = previousScreen;

        m_infosScreen = new InformationScreen(midlet, this);
        m_poiScreen = new POIScreen(midlet, this);
        m_mapScreen = new MapScreen(midlet, this);
        m_locationMsg = null;

        m_optionsList = new List("Menu", Choice.IMPLICIT, s_optionsNames, null);
        m_optionsList.setSelectCommand(s_selectCmd);
        m_optionsList.addCommand(s_backCmd);
        m_optionsList.setCommandListener(this);

        setDiplayable(m_optionsList);
    }

    /**
     * Sets the working location message
     *
     * @param message
     *            Message read from a location tag
     */
    public void setLocationMessage(TagLocationMessage message) {
        m_locationMsg = message;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.microedition.lcdui.CommandListener#commandAction(javax.microedition
     * .lcdui.Command, javax.microedition.lcdui.Displayable)
     */
    public void commandAction(Command command, Displayable displayable) {
        if (command == s_backCmd) {
            getMidlet().setActiveScreen(m_previousScreen);
        } else if (command == s_selectCmd) {
            String option = s_optionsNames[m_optionsList.getSelectedIndex()];

            if (option.equals(s_showOnMap)) {
                // Show Google Maps, centered on the tag location
                m_mapScreen.setAddress(m_locationMsg.getLatitude(),
                        m_locationMsg.getLongitude());

                getMidlet().setActiveScreen(m_mapScreen);

            } else if (option.equals(s_geocode)) {
                // Retrieve address from GPS coordinates
                GoogleMaps gmaps = new GoogleMaps("");
                try {
                    m_infosScreen.setText(gmaps.reverseGeocodeAddress(
                            m_locationMsg.getLatitude(),
                            m_locationMsg.getLongitude()));
                } catch (Exception e) {
                    m_infosScreen.setText("Error during reverse geocoding :\n"
                            + e.toString());
                }
                getMidlet().setActiveScreen(m_infosScreen);

            } else if (option.equals(s_poi)) {
                // Show POI search screen
                m_poiScreen.setLocation(m_locationMsg.getLatitude(),
                        m_locationMsg.getLongitude());
                getMidlet().setActiveScreen(m_poiScreen);

            } else if (option.equals(s_infos)) {
                // Show tag informations
                m_infosScreen.setText(m_locationMsg.toString());
                getMidlet().setActiveScreen(m_infosScreen);
            }
        }
    }
}
