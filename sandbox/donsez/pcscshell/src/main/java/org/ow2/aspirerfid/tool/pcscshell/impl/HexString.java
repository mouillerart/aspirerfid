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
import java.util.StringTokenizer;

/**
 * A utility class to print and to parse hexadecimal strings
 * @author  Didier DONSEZ <X.Y@imag.fr> where X=Didier and Y=Donsez
 **/

public class HexString {

	/**
	 * Parse a Hex string
	 * @param hexString a a hexadecimal-formatted string
	 * @note : java.lang.Integer.parseInt(String s, int radix) do not verify if symbol is correct !!
	 */
	public static byte[] parse(String hexString, String separator)
		throws java.lang.NumberFormatException {
		if(separator==null){
			separator="\n\t ";
		}
		int byteCounter=0;
		StringTokenizer st = new StringTokenizer(hexString,separator);
		byte[][] tok = new byte[st.countTokens()][];
		for(int i=0; st.hasMoreTokens(); i++) {
			tok[i]=parse(st.nextToken());
			byteCounter+=tok[i].length;
		}
		byte[] result = new byte[byteCounter];
		int k=0;
		for(int i=0; i<tok.length; i++) {
			for(int j=0; j<tok[i].length; j++) {
				result[k++]=tok[i][j];
			}
		}		
		return result;
	}

	
	/**
	 * Parse a Hex string without separator
	 * @param hexString a a hexadecimal-formatted string
	 * @note : java.lang.Integer.parseInt(String s, int radix) do not verify if symbol is correct !!
	 */
	public static byte[] parse(String hexString)
		throws java.lang.NumberFormatException {
		int len=hexString.length();
		if(len%2!=0) throw new java.lang.NumberFormatException();
		byte[] result = new byte[len/2];
		char[] ca=hexString.toCharArray();
		for(int i=0; i<len; i+=2) {
			result[i/2]=(byte)(ca[i]*16+parse(ca[i+1]));
		}
		return result;
	}
	
	/**
	 * Parse a radix 16 symbol
	 * @param c a symbol
	 * @note : java.lang.Integer.parseInt(String s, int radix) do not verify if symbol is correct !!
	 */
	public static byte parse(char c)
		throws java.lang.NumberFormatException {
		if((c>='0' && c<='9') || (c>='a' && c<='f') || (c>='A' && c<='F'))
			return (byte)(Character.digit(c,16));
		throw new java.lang.NumberFormatException();
	}

	/**
	 * Hexify a byte
	 * @param b a byte
	 */
	public static String hexify(byte b) {
		StringBuffer sb = new StringBuffer();
		String bs = Integer.toHexString(b & 0xFF);
		if (bs.length() == 1) {
			sb.append(0);
		}
		sb.append(bs);
		return sb.toString();
	}


	/**
	 * Hexify a byte array
	 * @param ba a byte array
	 */
	public static String hexify(byte[] ba, String separator, int bytePerLine) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ba.length; i++) {
			if(i!=0 && separator!=null) {
				sb.append(separator);
			}
			String bs = Integer.toHexString(ba[i] & 0xFF);
			if (bs.length() == 1) {
				sb.append(0);
			}
			sb.append(bs);
			if((i+1)%bytePerLine==0) {
				sb.append('\n');
			}
		}
		return sb.toString();
	}

	
	/**
	 * Hexify a byte array
	 * @param ba a byte array
	 */
	public static String hexify(byte[] ba, String separator) {
		return hexify(ba,separator,Integer.MAX_VALUE);
	}
}