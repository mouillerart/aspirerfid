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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Image;

/**
 * @author Joris Brémond
 * @author Jean-François Knoepfli
 * @author Mathieu Rivoalen
 * @author Raphael Tronc
 * 
 * @author Thomas Calmant - Added map dimensions, removed complex scrolling
 */
public class GoogleMaps {

    private static final String URL_UNRESERVED =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "abcdefghijklmnopqrstuvwxyz" +
            "0123456789-_.~";

    private String apiKey = null;
    private String adresse = null;
    private int actualZoom = 16;
    private double[] actualLanLng;
    private static final double DELTA = 0.00012;
    
    private int mapHeight = 320;
    private int mapWidth = 240;

    public GoogleMaps(String key) {
        apiKey = key;
    }
    
    public void setMapDimensions(int width, int height) {
    	mapWidth = width;
    	mapHeight = height;
    }

    public double[] geocodeAddress(String address) throws Exception {
        byte[] res = loadHttpFile(getGeocodeUrl(address));
        String[] data = split(new String(res), ',');

        if (!data[0].equals("200")) {
            int errorCode = Integer.parseInt(data[0]);
            throw new Exception("Google Maps Exception: " + getGeocodeError(errorCode));
        }
        
        try {
        	// Address is a real address
        	return new double[]{
                    Double.parseDouble(data[2]), Double.parseDouble(data[3])
                };
        } catch(NumberFormatException ex) {
        	// Address may be GPS coordinates
        	data = split(address, ',');
        	return new double [] {
        			Double.parseDouble(data[0]), Double.parseDouble(data[1])
        	};
        }
    }
    
    public String reverseGeocodeAddress(double latitude, double longitude) throws Exception {
    	byte[] res = loadHttpFile(getReverseGeocodeUrl(latitude, longitude));
    	String str_res = new String(res);
    	
    	if(!str_res.startsWith("200")) {
            int errorCode = Integer.parseInt(str_res.substring(0,3));
            throw new Exception("Google Maps Exception: " + getGeocodeError(errorCode));
        }
    	
    	String data[] = split(str_res, '"');
    	return data[1];
    }
    
    public String findPOIinJSON(String category, double latitude, double longitude) throws Exception {
    	byte[] res = loadHttpFile(queryPOIUrl(category, latitude, longitude));
    	// Don't test the validity of the answer, let the user do it
    	return new String(res);
    }

    public Image retrieveStaticImage(int width, int height, double lat, double lng, int zoom,
            String format) throws IOException {
        byte[] imageData = loadHttpFile(getMapUrl(width, height, lng, lat, zoom, format));

        return Image.createImage(imageData, 0, imageData.length);
    }

    private static String getGeocodeError(int errorCode) {
        switch (errorCode) {
            case 400:
                return "Bad request";
            case 500:
                return "Server error";
            case 601:
                return "Missing query";
            case 602:
                return "Unknown address";
            case 603:
                return "Unavailable address";
            case 604:
                return "Unknown directions";
            case 610:
                return "Bad API key";
            case 620:
                return "Too many queries";
            default:
                return "Generic error";
        }
    }

    private String getGeocodeUrl(String address) {
        return "http://maps.google.com/maps/geo?q=" + urlEncode(address) + "&output=csv&key=" + apiKey;
    }
    
    private String getReverseGeocodeUrl(double latitude, double longitude) {
    	return "http://maps.google.com/maps/geo?ll=" + Double.toString(latitude)
    		+ "," + Double.toString(longitude) + "&output=csv&key=" + apiKey;
    }
    
    private String queryPOIUrl(String what, double latitude, double longitude) {
    	return "http://ajax.googleapis.com/ajax/services/search/local?q=" + urlEncode(what)
    		+ "&sll=" + Double.toString(latitude) + "," + Double.toString(longitude) + "&v=1.0";
    }

    private String getMapUrl(int width, int height, double lng, double lat, int zoom, String format) {
        return "http://maps.google.com/maps/api/staticmap?center=" + lat + "," + lng + "&format=" + format + "&zoom=" + zoom + "&size=" + width + "x" + height + "&markers="+urlEncode(adresse)+ "&key=" + apiKey+ "&sensor=false";
    }

    private static String urlEncode(String str) {
        StringBuffer buf = new StringBuffer();
        byte[] bytes = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);
            dos.writeUTF(str);
            bytes = bos.toByteArray();
        } catch (IOException e) {
            // ignore
        }
        for (int i = 2; i < bytes.length; i++) {
            byte b = bytes[i];
            if (URL_UNRESERVED.indexOf(b) >= 0) {
                buf.append((char) b);
            } else {
                //buf.append('%').append(Character.forDigit((b >> 4) & 0x0f, 16)).append(Character.forDigit(b & 0x0f, 16));
                buf.append("%20");
            }
        }
        return buf.toString();
    }

    private static byte[] loadHttpFile(String url) throws IOException {
        byte[] byteBuffer;
        
        HttpConnection hc = (HttpConnection) Connector.open(url);
        try {
            hc.setRequestMethod(HttpConnection.GET);
            InputStream is = hc.openInputStream();
            try {
                int len = (int) hc.getLength();
                if (len > 0) {
                    byteBuffer = new byte[len];
                    int done = 0;
                    while (done < len) {
                        done += is.read(byteBuffer, done, len - done);
                    }
                } else {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[512];
                    int count;
                    while ((count = is.read(buffer)) >= 0) {
                        bos.write(buffer, 0, count);
                    }
                    byteBuffer = bos.toByteArray();
                }
            } finally {
                is.close();
            }
        } finally {
            hc.close();
        }

        return byteBuffer;
    }

    private static String[] split(String s, int chr) {
        Vector res = new Vector();

        int curr;
        int prev = 0;

        while ((curr = s.indexOf(chr, prev)) >= 0) {
            res.addElement(s.substring(prev, curr));
            prev = curr + 1;
        }
        res.addElement(s.substring(prev));

        String[] splitted = new String[res.size()];
        for (int i = 0; i < res.size(); i++) {
            splitted[i] = (String) res.elementAt(i);
        }
        return splitted;
    }

    public Image adjust(int deltaY, int deltaX) throws IOException {
    	actualLanLng[0] += deltaY * DELTA * (22 - actualZoom);
    	actualLanLng[1] += deltaX * DELTA * (22 - actualZoom);
        return retrieveStaticImage(mapWidth, mapHeight, actualLanLng[0], actualLanLng[1], actualZoom, "png32");
    }

    //------- Methodes graphiques--------------------
    public Image getMaps(String adresse) throws Exception {
        this.adresse = adresse;
        double[] lanLng = geocodeAddress(adresse);
        this.actualLanLng = lanLng;
        return retrieveStaticImage(mapWidth, mapHeight, lanLng[0], lanLng[1], actualZoom, "png32");
    }

    public Image zoomIn() throws Exception {
        if (adresse != null && actualLanLng != null && actualZoom < 22) {
            this.actualZoom++;
            return retrieveStaticImage(mapWidth, mapHeight, actualLanLng[0], actualLanLng[1], actualZoom, "png32");
        } else {
            return null;
        }
    }

    public Image zoomOut() throws Exception {
        if (adresse != null && actualLanLng != null && actualZoom > 1) {
            this.actualZoom--;
            return retrieveStaticImage(mapWidth, mapHeight, actualLanLng[0], actualLanLng[1], actualZoom, "png32");
        } else {
            return null;
        }
    }

    public Image moveRight() throws IOException {
        return adjust(0, 1);
    }

    public Image moveLeft() throws IOException {
        return adjust(0, -1);
    }

    public Image moveUp() throws IOException {
        return adjust(1, 0);
    }

    public Image moveDown() throws IOException {
        return adjust(-1, 0);
    }
}
