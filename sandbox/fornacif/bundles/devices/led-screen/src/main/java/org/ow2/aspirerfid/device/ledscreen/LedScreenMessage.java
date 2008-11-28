/**
 * Copyright 2008, Aspire
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
 *
 * --------------------------------------------------------------------------
 * $Id$
 * --------------------------------------------------------------------------
 */

package org.ow2.aspirerfid.device.ledscreen;

/**
 * @author Francois Fornaciari
 */
public class LedScreenMessage {
	private static final String HELLO = "Hello";

	private LedScreenLine[] lines = new LedScreenLine[8];
	
	public LedScreenMessage() {
		for (int i = 0; i < 8; i++) {
			lines[i] = new LedScreenLine();
		}
	}

	public byte[] hello() {
		byte[] data = new byte[HELLO.length()];

		for (int i = 0; i < data.length; i++) {
			data[i] = ((byte) HELLO.charAt(i));
		}
		return data;
	}

	public byte[] configuration() {
		byte[] data = new byte[16];
		data[0] = ((byte) 01);
		data[9] = ((byte) 03);

		for (int i = 1; i <= 8; i++) {
			data[i] = (byte) lines[i - 1].configuration();
		}

		return data;
	}

	public byte[] text(final int lineNumber) {
		byte[] data = new byte[256];
		String text = lines[lineNumber].text();

		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == ' ') {
				data[i] = (byte) 0;
			} else {
				data[i] = (byte) text.charAt(i);
			}
		}

		return data;
	}
	
	public void setLine(final int lineNumber, final LedScreenLine line) {
		lines[lineNumber] = line;
	}
	
}
