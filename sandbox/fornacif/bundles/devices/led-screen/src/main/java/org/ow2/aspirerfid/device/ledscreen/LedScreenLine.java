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
public class LedScreenLine {

	public static final int SPEED_1 = 0;
	public static final int SPEED_2 = 1;
	public static final int SPEED_3 = 2;
	public static final int SPEED_4 = 3;
	public static final int SPEED_5 = 4;
	public static final int SPEED_6 = 5;
	public static final int SPEED_7 = 6;
	public static final int SPEED_8 = 7;

	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int UP = 2;
	public static final int DOWN = 3;
	public static final int FREEZE = 4;

	private int speed;
	private int direction;
	private boolean flash;
	private String text = "";

	public LedScreenLine() {
		this.speed = SPEED_1;
		this.direction = LEFT;
		this.flash = false;
	}

	public LedScreenLine(final int speed, final int direction, final boolean flash, final String text) {
		this.speed = speed;
		this.direction = direction;
		this.flash = flash;
		this.text = text;
	}

	public int configuration() {
		if (flash) {
			return (speed * 16) + direction + 8;
		} else {
			return (speed * 16) + direction;
		}
		
	}

	public String text() {
		return this.text;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public void setText(String text) {
		this.text = text;
	}

}
