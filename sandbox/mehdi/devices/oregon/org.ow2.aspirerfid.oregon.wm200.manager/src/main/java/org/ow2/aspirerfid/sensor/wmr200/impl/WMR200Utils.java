/*
 * Copyright 2005-2008, Aspire
 * 
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation (the "LGPL"); either version 2.1 of the 
 * License, or (at your option) any later version. If you do not alter this 
 * notice, a recipient may use your version of this file under either the 
 * LGPL version 2.1, or (at his option) any later version.
 * 
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this library; if not, write to the Free Software Foundation, 
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY 
 * KIND, either express or implied. See the GNU Lesser General Public 
 * License for the specific language governing rights and limitations.
 */
package org.ow2.aspirerfid.sensor.wmr200.impl;

import java.util.List;

/**
 * The Class WMR200Utils.
 *  @author Elmehdi Damou
 */
public class WMR200Utils {

	
	/**
	 * Removes the content data.
	 *
	 * @param data the data
	 */
	public static void removeContentData(byte[] data) {

		for (int i = 0; i < data.length; i++) {
			data[i] = 0x00;
		}
	}
	
	
	/**
	 * Log data.
	 *
	 * @param data the data
	 */
	public static void logData(byte[] data) {

		System.out.print("Data: 0x");
		for (int i = 0; i < data.length; i++) {
			System.out.print(Integer.toHexString(data[i] & 0xff) + " ");
		}
		System.out.println();
	}
	
	/**
	 * Check sum state.
	 *
	 * @param data the data
	 * @return true, if successful
	 */
	public static boolean checkSumState(List<Byte> data) {
		if (data.size()>2){
			int checkSumHigh = data.get(data.size() - 2) & 0xff;
			int checkSumLow = data.get(data.size() - 1) & 0xff;

			int temp = data.size() - 2;

			int somme = 0;
			for (int i = 0; i < temp; i++) {
				somme += (data.get(i) & 0xff);
			}

			return (somme == (checkSumHigh + checkSumLow * 256));
		}else {
			return false;
		}
	}
	
}
