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
package org.ow2.aspirerfid.smsreceiver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utility class providing methods dedicated to ASCII string reading.
 * 
 * @author Sebastien Jean, IUT Valence (sebastien.jean@iut-valence.fr)
 * 
 */
public class CommUtils
{
	/**
	 * Constant for ASCII character 'CR'
	 */
	public final static byte CR = (byte) 0x0D;

	/**
	 * Constant for ASCII character 'SP'
	 */
	public final static byte SP = (byte) 0x20;

	/**
	 * Constant for ASCII character 'LF'
	 */
	public final static byte LF = (byte) 0x0A;

	/**
	 * Constant for escape sequence 'Ctrl-Z'
	 */
	public final static byte CTRL_Z = (byte) 0x1A;

	/**
	 * Constant for ASCII string "OK\r\n".
	 */
	public final static byte[] OK_CR_LF = { (byte) 0x4F, (byte) 0x4B, CR, LF };

	/**
	 * Test if two byte arrays are equals.
	 * 
	 * @param b1 the first byte array.
	 * @param b2 the second byte array.
	 * @return <tt>true</tt> if <tt>b1</tt> and <tt>b2</tt> are equals,
	 *         <tt>false</tt>else.
	 */
	public static boolean areSameArrays(byte[] b1, byte[] b2)
	{
		if (b1.length != b2.length)
			return false;
		for (int i = 0; i < b1.length; i++)
		{
			if (b1[i] != b2[i])
				return false;
		}
		return true;
	}

	/**
	 * Waiting (i.e. blocking) for an ASCII text line (ended by CR/LF) contains
	 * a specified substring.
	 * 
	 * @param is the input stream in which reading ASCII chars.
	 * @param sub the substring to search for.
	 * @return the read ASCII text line containing <tt>sub</tt> or
	 *         <tt>null</tt> if reading failed.
	 */
	public static String waitForLineEndedByCRLFOccurringSubstring(
			InputStream is, String sub)
	{
		while (true)
		{
			System.out.println("waiting for " + sub);
			try
			{
				byte[] line = CommUtils.readLineEndedByAFlag(is, CommUtils.LF);
				if (line == null)
					return null;
				String sline = new String(line, "US-ASCII");
				if (sline.indexOf(sub) != -1)
				{
					System.out.println(sub + " received");
					return sline;
				}
			}
			catch (Exception e)
			{
				System.out.println("Timeout, retrying...");
				continue;
			}

		}
	}

	/**
	 * Waiting (i.e. blocking) for an ASCII string ended by a specified
	 * character.
	 * 
	 * @param is the input stream in which reading ASCII chars.
	 * @param flag the character to search for.
	 * @return the read ASCII string (as a byte array) ended by <tt>flag</tt>
	 *         or <tt>null</tt> if reading failed.
	 */
	public static byte[] readLineEndedByAFlag(InputStream is, byte flag)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		while (true)
		{
			int read = 0;
			try
			{
				read = is.read();
				// System.out.println(read);
			}
			catch (IOException e)
			{
				return null;
			}

			if (read == -1)
				return null;

			bos.write(read);
			if ((byte) read == flag)
				return bos.toByteArray();
		}
	}

	/**
	 * Test if the next read ASCII text line (ended by CR/LF) is "OK\r\n".
	 * @param is the input stream in which reading ASCII chars.
	 * @return <tt>true</tt> if the read ASCII text line is "OK\r\n", <tt>false</tt> else.
	 */
	public static boolean answerIsOKCRLF(InputStream is)
	{
		byte[] buffer = new byte[4];

		for (int i = 0; i < 4; i++)
		{
			try
			{
				buffer[i] = (byte) (is.read());
			}
			catch (IOException e)
			{
				return false;
			}
		}
		return areSameArrays(buffer, OK_CR_LF);
	}
}
