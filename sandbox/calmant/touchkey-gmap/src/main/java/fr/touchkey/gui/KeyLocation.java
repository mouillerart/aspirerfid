/*
 *  Copyright (C) Team TouchKey
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
package fr.touchkey.gui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import fr.touchkey.constantes.Constante;

/**
 * @author Joris Brémond
 * @author Jean-François Knoepfli
 * @author Mathieu Rivoalen
 * @author Raphael Tronc
 */
public abstract class KeyLocation extends Canvas implements CommandListener {

    private Command zoomIn, zoomOut;
    private Image map;
    private GoogleMaps gmap;
    private boolean actionEnCours = false;

    public KeyLocation() {
        zoomIn = new Command("Zoom+", Command.OK, 1);
        zoomOut = new Command("Zoom-", Command.OK, 0);
        this.addCommand(zoomIn);
        this.addCommand(zoomOut);

        this.setCommandListener(this);
    }
    
    public abstract String getAdresse();

    public GoogleMaps getGmap() {
        return gmap;
    }

    public Image getMap() {
        return map;
    }

    public void setMap(Image map) {
        this.map = map;
    }

    public void commandAction(Command c, Displayable d) {
        if (d == this && map != null) {
            if (c == zoomIn) {
                if (!actionEnCours) {
                    Thread t = new GmapManipulationThread(this, Constante.ZOOMIN);
                    t.start();
                }
            }
            if (c == zoomOut) {
                if (!actionEnCours) {
                    Thread t = new GmapManipulationThread(this, Constante.ZOOMOUT);
                    t.start();
                }
            }
        }
    }

    protected void paint(Graphics g) {
        if (map != null) {
            g.drawImage(map, 0, 0, 0);
        } else {
            g.drawString("Please wait a while...", 0, 0, 0);
        }
    }

    public void keyPressed(int keyCode) {
    	if(map == null)
    		return;
    	
        switch (keyCode) {
        	case UP:
            case -1://UP
                if (!actionEnCours) {
                    Thread t = new GmapManipulationThread(this, Constante.MOVEUP);
                    t.start();
                }
                break;
            case DOWN:
            case -2://Down
                if (!actionEnCours) {
                    Thread t = new GmapManipulationThread(this, Constante.MOVEDOWN);
                    t.start();
                }
                break;
            case LEFT:
            case -3://Left
                if (!actionEnCours) {
                    Thread t = new GmapManipulationThread(this, Constante.MOVELEFT);
                    t.start();
                }
                break;
            case RIGHT:
            case -4://Right
                if (!actionEnCours) {
                    Thread t = new GmapManipulationThread(this, Constante.MOVERIGHT);
                    t.start();
                }
                break;
        }
    }

    public void setActionEnCours(boolean actionEnCours) {
        this.actionEnCours = actionEnCours;
    }

    public void setGmap(GoogleMaps gmap) {
        this.gmap = gmap;
    }
}
