/*
   Copyright 2005-2008, OW2 Aspire RFID project 
   
   This library is free software; you can redistribute it and/or modify it 
   under the terms of the GNU Lesser General Public License as published by 
   the Free Software Foundation (the "LGPL"); either version 2.1 of the 
   License, or (at your option) any later version. If you do not alter this 
   notice, a recipient may use your version of this file under either the 
   LGPL version 2.1, or (at his option) any later version.
   
   You should have received a copy of the GNU Lesser General Public License 
   along with this library; if not, write to the Free Software Foundation, 
   Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
   
   This software is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY 
   KIND, either express or implied. See the GNU Lesser General Public 
   License for the specific language governing rights and limitations.

   Contact: OW2 Aspire RFID project <X AT Y DOT org> (with X=aspirerfid and Y=ow2)

   LGPL version 2.1 full text http://www.gnu.org/licenses/lgpl-2.1.txt    
*/

package org.ow2.aspirerfid.smsreceiver.data.ballon;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.ow2.aspirerfid.smsreceiver.data.Message;

/**
 * This class contains ballon information
 * @author S�bastien Jean, Didier Donsez
 */

public class SMSBalloonMessage implements Message {
	
	public SMSBalloonMessage(String latitude, String longitude, String altP, String altGPS, String pressure, String temperature) {
		this.latitude=latitude;
		this.longitude=longitude;
		this.altP=altP;
		this.altGPS=altGPS;
		this.pressure=pressure;
		this.temperature=temperature;
	}
		
	/**
	 * Temperature.
	 */
	private String temperature;
	
	/**
	 * Pressure. 
	 */
	private String pressure;
	
	/**
	 *  Altitude (calculated from pressure).
	 */
	private String altP;
	
	/**
	 * Latitude.
	 */
	private String latitude;
	
	/**
	 * Longitude.
	 */
	private String longitude;
	
	/**
	 * Altitude (given by GPS). 
	 */
	private String altGPS;

	
	/**
	 * Getting temperature.
	 * @return the temperature.
	 */
	public String getTemperature()
	{
		return this.temperature;
	}
	
	/**
	 * Getting the pressure.
	 * @return the pressure
	 */
	public String getPressure()
	{
		return this.pressure;
	}
	
	/**
	 * Getting the altitude (calculated from pressure).
	 * @return the altitude (calculated from pressure).
	 */
	public String getAltP()
	{
		return this.altP;
	}
	
	/**
	 * Getting the latitude.
	 * @return the latitude.
	 */
	public String getLatitude()
	{
		return this.latitude;
	}
	
	/**
	 * Getting the longitude.
	 * @return the longitude.
	 */
	public String getLongitude()
	{
		return this.longitude;
	}
	
	/**
	 * Getting the altitude (given by GPS).
	 * @return the altitude (given by GPS).
	 */
	public String getAltGPS()
	{
		return this.altGPS;
	}
	
	public Map toMap(){
		Map map=new HashMap();
		
		map.put("latitude",latitude);
		map.put("longitude",longitude);
		map.put("altP",altP);
		map.put("altGPS",altGPS);
		map.put("pressure",pressure);
		map.put("temperature",temperature);

		return map;
	}

	public Dictionary toDictonary(){
		Dictionary dict=new Hashtable();
		
		dict.put("latitude",latitude);
		dict.put("longitude",longitude);
		dict.put("altP",altP);
		dict.put("altGPS",altGPS);
		dict.put("pressure",pressure);
		dict.put("temperature",temperature);

		return dict;
	}

	
	public String toString(){
		StringBuffer sb=new StringBuffer();
		
		sb.append("SMSBalloonMessage[");
		sb.append("latitude").append(latitude).append(';');
		sb.append("longitude").append(longitude).append(';');
		sb.append("altP").append(altP).append(';');
		sb.append("altGPS").append(altGPS).append(';');
		sb.append("pressure").append(pressure).append(';');
		sb.append("temperature").append(temperature);
		sb.append(']');
		return sb.toString();
	}
	
	
	public String toHTML(){
		
		StringBuffer sb=new StringBuffer();
		
		sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"");
		sb.append("\n\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
		sb.append("\n<html xmlns=\"http://www.w3.org/1999/xhtml\">");
		sb.append("\n<head>");
		sb.append("\n<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"/>");
		sb.append("\n<title>Géolocalisation d'un ballon sonde</title>");
		sb.append("\n<script src=\"http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAtt-OR7NSRlv8oz0dTEW-yRS-A2bT2PuC0K-4WTjBjPHEk9KmaxTkK25odgbEtOmSoqpeshaD1w50fQ\"");
		sb.append("\ntype=\"text/javascript\"></script>");
		sb.append("\n<script type=\"text/javascript\">");
		sb.append("\n//<![CDATA[");
		sb.append("\nfunction load() {");
		sb.append("\nif (GBrowserIsCompatible()) {");
		sb.append("\nvar map = new GMap2(document.getElementById(\"map\"));");
		sb.append("\nmap.setCenter(new GLatLng(" + this.getLatitude() + ", "
				+ this.getLongitude() + "), 13);");
		sb.append("\nvar point = new GLatLng(" + this.getLatitude() + ", "
				+ this.getLongitude() + ");");
		sb.append("\nvar marker =  new GMarker(point);");
		sb.append("\nGEvent.addListener(marker, \"click\", function() {");
		sb.append("\nmarker.openInfoWindowHtml(");
		sb.append("\n\"<b>Capteurs</b><br/>");
		sb.append("\n<ul><li>Température : " + this.getTemperature()
				+ " °C </li>");
		sb.append("\n<li>Pression : " + this.getPressure()
				+ " hPa </li><br/>");
		sb.append("\n<li>Altitude (déduite de la pression) : "
				+ this.getAltP() + " m </li></ul><br/>");
		sb.append("\n<b>GPS</b><br/>");
		sb.append("\n<ul><li>Latitude : " + this.getLatitude() + "</li>");
		sb.append("\n<li>Longitude : " + this.getLongitude() + "</li>");
		sb.append("\n<li>Altitude : " + this.getAltGPS()
				+ " m </li></ul><br/>\"");
		sb.append("\n});");
		sb.append("\nmap.addOverlay(marker);");
		sb.append("\n}");
		sb.append("\n}");
		sb.append("\n//]]>");
		sb.append("\n</script>");
		sb.append("\n</head>");
		sb.append("\n<body onload=\"load()\" onunload=\"GUnload()\">");
		sb.append("\n<div id=\"map\" style=\"width: 500px; height: 300px\"></div>");
		sb.append("\n</body>");
		sb.append("\n</html>");
		return sb.toString();
	}
	
	
	/**
	 * 
	 * @see http://code.google.com/intl/fr/apis/kml/documentation/
	 */
	public String toKML(){
		// TODO
		StringBuffer sb=new StringBuffer();
		return sb.toString();
		
	}	
}
