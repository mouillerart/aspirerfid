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

import java.util.StringTokenizer;

import org.ow2.aspirerfid.smsreceiver.data.MalformedMessageException;
import org.ow2.aspirerfid.smsreceiver.data.Message;
import org.ow2.aspirerfid.smsreceiver.data.MessageProcessor;

public class SMSBalloonMessageProcessor extends MessageProcessor {
	
	public Message parse(String message) throws MalformedMessageException {
		
		// SMS is like T=... A=... P=... AltP=... Lat=... Lgt=... AltGPS=...
		// field separator is <sp>
		StringTokenizer sTokenizer = new StringTokenizer(message," ");
		if (sTokenizer.countTokens() != 5) throw new MalformedMessageException();
		
		// Temperature
		String temperature = sTokenizer.nextToken();
		if (!(temperature.startsWith("T="))) throw new MalformedMessageException();
		temperature = temperature.substring(2);
		
		// Pressure
		String pressure = sTokenizer.nextToken();
		if (!(pressure.startsWith("P="))) throw new MalformedMessageException();
		pressure = pressure.substring(2);
		
		// AltP
		String altP = sTokenizer.nextToken();
		if (!(altP.startsWith("A="))) throw new MalformedMessageException();
		altP = altP.substring(2);
		
		// Latitude
		String latitude = sTokenizer.nextToken();
		if (!(latitude.startsWith("Lat="))) throw new MalformedMessageException();
		latitude = latitude.substring(4);
		
		// Longitude
		String longitude = sTokenizer.nextToken();
		if (!(longitude.startsWith("Lgt="))) throw new MalformedMessageException();
		longitude = longitude.substring(4);
		
		// AltGPS
		String altGPS = sTokenizer.nextToken();
		if (!(altGPS.startsWith("AltGPS="))) throw new MalformedMessageException();
		altGPS = altGPS.substring(6);
		
		// Latitude conversion 
		if (latitude.indexOf('-')==-1) throw new MalformedMessageException();
		int latInt = Integer.parseInt(latitude.substring(0,latitude.indexOf('-')));
		
		float latConverted = latInt + Float.parseFloat(latitude.substring(latitude.indexOf('-') + 1, latitude.length()-2))/60;
		if (latitude.charAt(latitude.length()-1)=='S') latConverted = -latConverted;
		latitude = ""+latConverted;
		
		// Longitude conversion 
		if (longitude.indexOf('-')==-1) throw new MalformedMessageException();
		int lgtInt = Integer.parseInt(longitude.substring(0,longitude.indexOf('-')));
		
		float lgtConverted = lgtInt + Float.parseFloat(longitude.substring(longitude.indexOf('-') + 1, longitude.length()-2))/60;
		if (longitude.charAt(longitude.length()-1)=='W') lgtConverted = -lgtConverted;
		longitude = ""+lgtConverted;
		
		return new SMSBalloonMessage(latitude,longitude,altP,altGPS,pressure,temperature);
	}

	public Message parse(String[] messages) {
		return null;
	}
}
