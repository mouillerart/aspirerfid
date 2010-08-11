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

import fr.touchkey.constantes.Constante;

/**
 * @author Joris Brémond
 * @author Jean-François Knoepfli
 * @author Mathieu Rivoalen
 * @author Raphael Tronc
 */
public class GmapManipulationThread extends Thread {

    private KeyLocation affichage;
    private int fct;

    public GmapManipulationThread(KeyLocation affichage, int fct) {
        affichage.setActionEnCours(true);
        this.affichage = affichage;
        this.fct = fct;
    }

    public void run() {
        switch (fct) {
            case Constante.GETMAP:
                try {
                    GoogleMaps gmap = new GoogleMaps("");
                    gmap.setMapDimensions(affichage.getWidth(), affichage.getHeight());
                    affichage.setGmap(gmap);
                    affichage.setMap(gmap.getMaps(affichage.getAdresse()));
                    affichage.repaint();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            case Constante.ZOOMIN:
                if (affichage.getGmap() != null) {
                    try {
                        affichage.setMap(affichage.getGmap().zoomIn());
                        affichage.repaint();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            case Constante.ZOOMOUT:
                if (affichage.getGmap() != null) {
                    try {
                        affichage.setMap(affichage.getGmap().zoomOut());
                        affichage.repaint();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            case Constante.MOVEDOWN:
                if (affichage.getGmap() != null) {
                    try {
                        affichage.setMap(affichage.getGmap().moveDown());
                        affichage.repaint();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            case Constante.MOVELEFT:
                if (affichage.getGmap() != null) {
                    try {
                        affichage.setMap(affichage.getGmap().moveLeft());
                        affichage.repaint();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            case Constante.MOVERIGHT:
                if (affichage.getGmap() != null) {
                    try {
                        affichage.setMap(affichage.getGmap().moveRight());
                        affichage.repaint();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            case Constante.MOVEUP:
                if (affichage.getGmap() != null) {
                    try {
                        affichage.setMap(affichage.getGmap().moveUp());
                        affichage.repaint();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                break;
        }
        affichage.setActionEnCours(false);
    }
}
