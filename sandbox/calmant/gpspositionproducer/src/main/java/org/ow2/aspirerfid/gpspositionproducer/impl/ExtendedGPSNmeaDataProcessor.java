package org.ow2.aspirerfid.gpspositionproducer.impl;

import java.util.Iterator;
import java.util.List;

import org.dinopolis.gpstool.gpsinput.nmea.NMEA0183Sentence;
import org.dinopolis.gpstool.gpsinput.sirf.GPSSirfDataProcessor;
import org.dinopolis.util.Debug;

// ----------------------------------------------------------------------
/**
 * This class extends org.dinopolis.gpstool.gpsinput.GPSNmeaDataProcessor
 * to add interpretations of VTG and HDT NMEA sentences
 * 
 * @author Didier Donsez (didier.donsez@imag.fr)
 * @version $Revision: 1.0 $
 * @deprecated since CHRISTOF INTEGRATE IT IN THE GPSNmeaDataProcessor
 */

public class ExtendedGPSNmeaDataProcessor extends GPSSirfDataProcessor { // GPSNmeaDataProcessor
																			// {

	private static void dumpSentence(final NMEA0183Sentence sentence) {
		Iterator enumeration = sentence.getDataFields().listIterator();
		while (enumeration.hasNext())
			System.out.print(enumeration.next() + ";");
		System.out.println();
	}

	// ----------------------------------------------------------------------
	/**
	 * Default constructor.
	 */
	public ExtendedGPSNmeaDataProcessor() {
		super();
	}

	// ----------------------------------------------------------------------
	/**
	 * Default constructor.
	 * 
	 * @param delay_time
	 *            the time between two NMEA messages are read (may
	 *            be used for reading NMEA files slower) in milliseconds.
	 */
	public ExtendedGPSNmeaDataProcessor(final int delay_time) {
		// super(delay_time);
		super();
	}

	// ----------------------------------------------------------------------
	/**
	 * Processes a HDT nmea sentences and fires the specific events about
	 * the information contained in this sentence (property name
	 * GPSDataProcessor.HEADING).
	 * 
	 * @param sentence
	 *            a NMEA sentence.
	 * 
	 * @link http://home.mira.net/~gnb/gps/nmea.html#gphdt
	 */
	protected void processHDT(final NMEA0183Sentence sentence) {
		if (Debug.DEBUG)
			Debug.println("gpstool_nmea", "HDT detected: " + sentence);
		String heading_str = (String) sentence.getDataFields().get(0);
		Float heading = null;
		try {
			heading = new Float(heading_str);
			changeGPSData(HEADING, heading);
		} catch (NumberFormatException nfe) {
			if (Debug.DEBUG)
				Debug.println("gpstool_nmea",
						"bad number format in VTG sentence: " + sentence);
		}
	}

	// ----------------------------------------------------------------------
	/**
	 * Processes the different nmea sentences.
	 * 
	 * @param sentence
	 *            a NMEA sentence.
	 */
	protected void processNmeaSentence(final NMEA0183Sentence sentence) {
		String id = sentence.getSentenceId().toUpperCase();

		dumpSentence(sentence);

		System.out.println(sentence);

		if (id.equals("VTG")) {
			processVTG(sentence);
			return;
		}

		if (id.equals("HDT")) {
			processHDT(sentence);
			return;
		}

		super.processNmeaSentence(sentence);
	}

	// ----------------------------------------------------------------------
	/**
	 * Processes a VTG nmea sentences and fires the specific events about
	 * the information contained in this sentence (property name
	 * GPSDataProcessor.SPEED).
	 * 
	 * @param sentence
	 *            a NMEA sentence.
	 * 
	 * @link http://home.mira.net/~gnb/gps/nmea.html#gpvtg
	 */

	protected void processVTG(final NMEA0183Sentence sentence) {
		if (Debug.DEBUG)
			Debug.println("gpstool_nmea", "VTG detected: " + sentence);
		List data_fields = sentence.getDataFields();
		String trueCourse = (String) data_fields.get(0); // True course
		// made good
		// over ground,
		// degrees
		// String magneticCourse = (String)data_fields.elementAt(2); // Magnetic
		// course made good over ground, degrees
		// String groundSpeedKnots = (String)data_fields.elementAt(4); // Ground
		// speed, N=Knots
		String groundSpeedKmH = (String) data_fields.get(6); // Ground
		// speed,
		// K=Kilometers
		// per hour

		Float heading = null;
		try {
			heading = new Float(trueCourse);
			changeGPSData(HEADING, heading);
		} catch (NumberFormatException nfe) {
			if (Debug.DEBUG)
				Debug.println("gpstool_nmea",
						"bad number format in VTG sentence: " + sentence);
		}

		try {
			float speed = Float.parseFloat(groundSpeedKmH);
			// speed = speed / KM2NAUTIC;
			changeGPSData(SPEED, new Float(speed));
		} catch (NumberFormatException nfe) {
			if (Debug.DEBUG)
				Debug.println("gpstool_nmea",
						"bad number format in VTG sentence: " + sentence);
		}
	}
}
