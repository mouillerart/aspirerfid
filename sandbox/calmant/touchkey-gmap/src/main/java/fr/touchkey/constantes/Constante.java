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
package fr.touchkey.constantes;

/**
 * @author Joris Brémond
 * @author Jean-François Knoepfli
 * @author Mathieu Rivoalen
 * @author Raphael Tronc
 */
public class Constante {

    public static final boolean debug = false;

    //-------------------------------------------WEB SERVICES---------------------------------------

    public static final String USERWEBSERICE = "https://www.touchkey.fr/WUserRequestsService";
    public static final String NAMESPACE = "http://ws.touchkey.nfc.com/";

    //-------------------------------------------GMAP Constantes---------------------------------------

    public static final int ZOOMIN = 0;
    public static final int ZOOMOUT = 1;
    public static final int MOVERIGHT = 2;
    public static final int MOVELEFT = 3;
    public static final int MOVEUP = 4;
    public static final int MOVEDOWN = 5;
    public static final int GETMAP = 6;


     //-------------------------------------------Timer Constantes---------------------------------------

    public static int TIMEOUT_ALERT = 2000;
}
