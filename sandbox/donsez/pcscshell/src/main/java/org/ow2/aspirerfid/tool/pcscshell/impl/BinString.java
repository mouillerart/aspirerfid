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


/**
 * A utility class to print and to parse binary-formatted strings
 * @author  Didier DONSEZ <X.Y@imag.fr> where X=Didier and Y=Donsez
 **/

public class BinString {

	/**
	 * Parse a binary-formatted string
	 * @param binString a a binary-formatted string
	 * @throws NumberFormatException
	 */
	public static byte[] parse(String binString)
		throws java.lang.NumberFormatException {
		int len=binString.length();
		if(len%8!=0) throw new NumberFormatException("Incorrect length");
		byte[] res=new byte[len/8];
		int j=0;
		byte b=0;
		for(int i=0;i<len;i++) {
			b=(byte)(b<<1);
			char c=binString.charAt(i);
			if(c=='1') {
				b+=1;
			} else if(c!='0') {
				throw new NumberFormatException("Not a binary symbol");	
			}			
			if(i%8==7) {
				res[j++]=b;
				b=0;
			}
		}
		return res;
	}
	
	/**
	 * Binify a byte
	 * @param sb a string buffer
	 * @param b a byte
	 */
	public static void binify(StringBuffer sb, byte b) {
		for(int i=0; i<8; i++) {
			sb.append((b<0) ? '1' : '0');
			b <<= 1;
		}
	}	
	
	/**
	 * Binify a byte array
	 * @param sb a string buffer
	 * @param bs a byte array
	 */
	public static void binify(StringBuffer sb, byte[] bs) {
		for(int i=0; i<bs.length; i++) {
			binify(sb, bs[i]);
		}
	}	

}