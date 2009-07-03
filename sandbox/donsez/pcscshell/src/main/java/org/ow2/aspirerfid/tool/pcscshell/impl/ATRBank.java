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
package org.ow2.aspirerfid.tool.pcscshell.impl;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * This class parses the SC list http://ludovic.rousseau.free.fr/softwares/pcsc-tools/smartcard_list.txt
 * The format is:
 * ATR in regular expression form
 * \t descriptive text
 * \t descriptive text
 * \t descriptive text
 *  empty line
 * @author Didier DONSEZ <X.Y@imag.fr> where X=Didier and Y=Donsez
 * TODO regular expression
 */
public class ATRBank {

	private Map<byte[], String> atrs=new HashMap<byte[], String>();
		
	public void load(Reader reader) throws IOException{
		LineNumberReader r=new LineNumberReader(reader);
		String line=null;
		byte[] atr=null;
		StringBuffer atrDescription=null;
		while((line=r.readLine())!=null){
			// comment
			if(line.indexOf("#")==0) continue;
			if(line.indexOf("\t")==0){
				if(atrDescription!=null){
					atrDescription.append(line);
				}
				continue;
			}
			if(line.length()==0){
				if(atr!=null) {
					atrs.put(atr,atrDescription.toString());
					atr=null;
					atrDescription=null;
				}
			} else {
				try {
					atr=HexString.parse(line," \t");
					atrDescription=new StringBuffer();
				} catch (java.lang.NumberFormatException e) {
					System.err.println("skip ATR "+line);
				}
			}
		}
		r.close();
	}
	
	public String getDescription(byte atr[]){
		return atrs.get(atr);
	}
}
