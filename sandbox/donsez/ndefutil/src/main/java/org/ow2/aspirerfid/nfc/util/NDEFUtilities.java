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
package org.ow2.aspirerfid.nfc.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.microedition.contactless.ndef.NDEFMessage;
import javax.microedition.contactless.ndef.NDEFRecord;
import javax.microedition.contactless.ndef.NDEFRecordType;

/**
 * This class provides utility methods to generate NDEF records and messages
 * @author Didier Donsez
 */
public class NDEFUtilities {

	/**
	 * Generate a NDEF Record containing an image
	 * @param is
	 * @return the NDEF record
	 */
	public static NDEFRecord generateImageRecord(InputStream is, String mimetype) throws IOException{
		
		//Read image from phone to ByteArrayOutputStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int c;
        while ((c = is.read()) != -1) {
            baos.write(c);
        }
        is.close();
        
        // Create NDEF Record to be added to NDEF Message
        NDEFRecord record = new NDEFRecord(
        			new NDEFRecordType(NDEFRecordType.MIME, mimetype),
        			null,
        			baos.toByteArray()
        );

		return record; 
	}
	

	/**
	 * Generate a NDEF Record containing a URI
	 * @param urlstr
	 * @return the NDEF record
	 */
	public static NDEFRecord generateRecordWithURL(String urlstr){
		
        // Headerbyte: open Bookmark in Browser
        byte[] prefixCode = {(byte)(NDEFURIConstants.getURLPrefixCode(urlstr))};

        // Payload Text: URL as a byte-Array
        byte[] urlBytes = NDEFURIConstants.getURLSuffix(urlstr).getBytes();

        // Create NDEF Record
        NDEFRecord record = new NDEFRecord(new NDEFRecordType(NDEFRecordType.NFC_FORUM_RTD, "urn:nfc:wkt:U"), null, null);

        // Append Payload Manually
        record.appendPayload(prefixCode);
        record.appendPayload(urlBytes);

		return record; 
	}
	
	
	/**
	 * Generate a NDEF Record containing a text
	 * @param lang ISO/IANA language code. Examples: "fi", "en-US", "fr-CA", "jp".
	 * @param text  The encoding in US-ASCII
	 * @return the NDEF record
	 */
	public static NDEFRecord generateRecordWithText(String lang, String text) {
		

        // Create NDEF Record
        NDEFRecord record = new NDEFRecord(new NDEFRecordType(NDEFRecordType.NFC_FORUM_RTD, "urn:nfc:wkt:T"), null, null);

        // Append Payload Manually
        record.appendPayload(new byte[]{(byte)(lang.length())});
        record.appendPayload(lang.getBytes());
        record.appendPayload(text.getBytes());

		return record; 
	}
	

	/**
	 * Generate a NDEF message for a smart posters
	 * @urlstr
	 * @recommendedAction
	 * @param langs ISO/IANA language code. Examples: "fi", "en-US", "fr-CA", "jp".
	 * @param texts  The encoding in US-ASCII
	 * @return the NDEF record
	 */
	public static NDEFMessage generateSmartPosterMessage(String urlstr, /*icon,recommendedAction*/String[] langs, String[] texts) {
	
	    // Create NDEF Message and add created record in it.
	    NDEFMessage message = new NDEFMessage();
	   
	    /* TODO Recommended Action Record
	    0 : Do the action (send the SMS, launch the browser, make the telephone call)
	    1 : Save for later (store the SMS in INBOX, put the URI in a bookmark, save the telephone number in contacts)
	    2 : Open for editing (open an SMS in the SMS editor, open the URI in an URI editor, open the telephone number for editing).
	    */
	    
	    // TODO Icon record
	    
	    message.appendRecord(generateRecordWithURL(urlstr));
	    for (int i = 0; i < texts.length; i++) {
	        message.appendRecord(generateRecordWithText(langs[i], texts[i]));		
		}
		return message; 
	}
	
	
	public static void main(String args[]){
		
	    NDEFMessage message = new NDEFMessage();
	    message.appendRecord(generateRecordWithURL(args[0]));
		
		System.out.println(HexUtility.toHexString(message.toByteArray()," "));		
	}
}
