/*
 *  Copyright (C) Aspire
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.ow2.aspirerfid.touchlocate.ui;

/**
 * Stores points of interest data
 * 
 * @author Thomas Calmant
 */
public class PointOfInterest {

	/** POI title */
	public String title;

	/** Phone number */
	public String phone;

	/** Street address */
	public String address;

	/** City (not included in address */
	public String city;

	/** Location - Latitude */
	public String latitude;

	/** Location - Longitude */
	public String longitude;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof PointOfInterest))
			return false;

		return obj.toString().equals(toString());
	}

	/**
	 * Tests information validity
	 * 
	 * @return True if the object has at least a title or an address
	 */
	public boolean isValid() {
		if (title != null && title.length() > 0)
			return true;

		if (address != null && address.length() > 0)
			return true;

		return false;
	}

	/**
	 * Tries to set an identifier for this PoI
	 */
	public String toString() {
		if (title != null && title.length() > 0)
			return title;

		if (address != null && address.length() > 0)
			return address;

		if (latitude != null && longitude != null)
			return latitude + "," + longitude;

		return "Invalid PoI";
	}
}