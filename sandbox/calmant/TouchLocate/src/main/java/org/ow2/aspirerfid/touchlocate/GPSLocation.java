/**
 * 
 */
package org.ow2.aspirerfid.touchlocate;

/**
 * @author Thomas Calmant
 *
 */
public class GPSLocation {
	public double latitude;
	public double longitude;
	
	public String latitudeString() {
		return Double.toString(latitude);
	}
	
	public String longitudeString() {
		return Double.toString(longitude);
	}
}
