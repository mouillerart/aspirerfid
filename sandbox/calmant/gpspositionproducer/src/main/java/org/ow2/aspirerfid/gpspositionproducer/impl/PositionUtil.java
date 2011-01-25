/*
 * Position Utility
 *
 * Copyright (C) 2003  Didier Donsez
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * Contact: Didier Donsez (Didier.Donsez@ieee.org)
 * Contributor(s):
 *
 **/
package org.ow2.aspirerfid.gpspositionproducer.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.osgi.util.measurement.Measurement;
import org.osgi.util.position.Position;

/**
 * this class provides methods to format position, coordinates and map server
 * links for following formats: (Plain)Text, Html, Xml, JavaScript <br>
 * TODO: <br>
 * 
 * 
 * @author Didier Donsez (Didier.Donsez@ieee.org)
 * @version 0.1, 29 march 2003
 */
public class PositionUtil {

	/**
	 * formats the position in PlainText
	 * 
	 * @param sb
	 *            a String Buffer to append (could be null)
	 * @param position
	 *            the position
	 * @return sb (created if sb is null)
	 */
	public static StringBuffer toText(StringBuffer sb, Position position) {
		if (sb == null) {
			sb = new StringBuffer();
		}
		sb.append("Position[");
		if (position == null) {
			sb.append((String) null);
		} else {
			sb.append("latitude:").append(position.getLatitude()).append(',');
			sb.append("longitude:").append(position.getLongitude()).append(',');
			sb.append("altitude:").append(position.getAltitude()).append(',');
			sb.append("speed:").append(position.getSpeed()).append(',');
			sb.append("track:").append(position.getTrack()).append(',');
		}
		sb.append(']');
		return sb;
	}

	/**
	 * formats the position in HTML
	 * 
	 * @param sb
	 *            a String Buffer to append (could be null)
	 * @param position
	 *            the position
	 * @return sb (created if sb is null)
	 */
	public static StringBuffer toHtml(StringBuffer sb, Position position) {
		if (sb == null) {
			sb = new StringBuffer();
		}
		sb.append("Position:");
		if (position == null) {
			sb.append((String) null);
		} else {
			sb.append("<ul>");
			sb.append("<li>latitude:").append(position.getLatitude()).append(
					"</li>");
			sb.append("<li>longitude:").append(position.getLongitude()).append(
					"</li>");
			sb.append("<li>altitude:").append(position.getAltitude()).append(
					"</li>");
			sb.append("<li>speed:").append(position.getSpeed()).append("</li>");
			sb.append("<li>track:").append(position.getTrack()).append("</li>");
			sb.append("</ul>");
		}
		return sb;
	}

	/**
	 * formats the measurement in XML (textual)
	 * 
	 * @param sb
	 *            a String Buffer to append (could be null)
	 * @param measurementName
	 *            the measurement name
	 * @param measurement
	 *            the measurement
	 * @return sb (created if sb is null)
	 */

	public static StringBuffer toXml(StringBuffer sb, String measurementName,
			Measurement measurement) {
		if (sb == null) {
			sb = new StringBuffer();
		}
		if (measurement == null) {
			sb.append("<" + measurementName + "/>");

		} else {
			sb.append("<" + measurementName);

			if (measurement.getUnit() != null) {
				sb.append(" unit=\"" + measurement.getUnit().toString() + "\"");
			}

			if (measurement.getError() != 0.0d) {
				sb.append(" error=\"" + measurement.getError() + "\"");
			}

			if (measurement.getTime() != 0) {
				sb
						.append(" time=\"" + formatDate(measurement.getTime())
								+ "\"");
			}

			sb.append(">").append(measurement.getValue());

			sb.append("</" + measurementName + ">");
		}
		return sb;
	}

	/**
	 * formats the position in XML (textual)
	 * 
	 * @param sb
	 *            a String Buffer to append (could be null)
	 * @param position
	 *            the position
	 * @return sb (created if sb is null)
	 */
	public static StringBuffer toXml(StringBuffer sb, Position position) {
		if (sb == null) {
			sb = new StringBuffer();
		}
		if (position == null) {
			sb.append("<position/>");
		} else {

			sb.append("<position>");
			toXml(sb, "latitude", position.getLatitude());
			toXml(sb, "longitude", position.getLongitude());
			toXml(sb, "altitude", position.getAltitude());
			toXml(sb, "speed", position.getSpeed());
			toXml(sb, "track", position.getTrack());
			sb.append("</position>");
		}
		return sb;
	}

	/**
	 * formats the measurement in XML (DOM)
	 * 
	 * @param doc
	 *            the document
	 * @param measurementName
	 *            the measurement name
	 * @param measurement
	 *            the measurement
	 * @return the element created
	 */
	public static org.w3c.dom.Element toElement(org.w3c.dom.Document doc,
			String measurementName, Measurement measurement) {
		org.w3c.dom.Element e_measurement;
		org.w3c.dom.Element elem;
		org.w3c.dom.Attr attr;

		e_measurement = doc.createElement(measurementName);

		if (measurement != null) {
			e_measurement.appendChild(doc.createTextNode(Double.toString(measurement
					.getValue())));

			if (measurement.getUnit() != null) {
				attr = doc.createAttribute("unit");
				attr.setValue(measurement.getUnit().toString());
				e_measurement.appendChild(attr);
			}

			if (measurement.getError() != 0.0d) {
				attr = doc.createAttribute("error");
				attr.setValue(Double.toString(measurement.getError()));
				e_measurement.appendChild(attr);
			}

			if (measurement.getTime() != 0) {
				attr = doc.createAttribute("time");
				attr.setValue(formatDate(measurement.getTime()));
				e_measurement.appendChild(attr);
			}
		}
		return e_measurement;
	}

	/**
	 * formats the position in XML (DOM)
	 * 
	 * @param doc
	 *            the document
	 * @param position
	 *            the position
	 * @return the element created
	 */
	public static org.w3c.dom.Element toElement(org.w3c.dom.Document doc,
			Position position) {
		org.w3c.dom.Element e_position;
		org.w3c.dom.Element elem;

		e_position = doc.createElement("position");

		if (position != null) {
			e_position.appendChild(toElement(doc, "latitude", position
					.getLatitude()));
			e_position.appendChild(toElement(doc, "longitude", position
					.getLongitude()));
			e_position.appendChild(toElement(doc, "altitude", position
					.getAltitude()));
			e_position
					.appendChild(toElement(doc, "speed", position.getSpeed()));
			e_position
					.appendChild(toElement(doc, "track", position.getTrack()));
		}

		return e_position;
	}

	/**
	 * formats the position in JavaScript
	 * 
	 * @param sb
	 *            a String Buffer to append (could be null)
	 * @param position
	 *            the position
	 * @return sb (created if sb is null)
	 */
	public static StringBuffer toJavaScript(StringBuffer sb, Position position) {
		if (sb == null) {
			sb = new StringBuffer();
		}
		if (position == null) {
			sb.append("position=null;");
		} else {
			sb.append("position.latitude=").append(position.getLatitude())
					.append(";\n");
			sb.append("position.longitude=").append(position.getLongitude())
					.append(";\n");
			sb.append("position.altitude=").append(position.getAltitude())
					.append(";\n");
			sb.append("position.speed=").append(position.getSpeed()).append(
					";\n");
			sb.append("position.track=").append(position.getTrack()).append(
					";\n");
		}
		return sb;
	}

	private static DateFormat dateFormat = new SimpleDateFormat(
			"EEE, d MMM yyyy HH:mm:ss Z");

	public static String formatDate(long date) {
		return dateFormat.format(new Date(date));
	}

}

