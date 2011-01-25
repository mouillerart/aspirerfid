/*
 * Simple GPS Position Provider
 * read NMEA0183 sentences from a serial GPS receiver
 * and keep the last position
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;

import org.dinopolis.gpstool.gpsinput.GPSDataProcessor;
import org.dinopolis.gpstool.gpsinput.GPSDevice;
import org.dinopolis.gpstool.gpsinput.GPSException;
import org.dinopolis.gpstool.gpsinput.GPSPosition;
import org.dinopolis.gpstool.gpsinput.SatelliteInfo;
import org.osgi.service.wireadmin.Producer;
import org.osgi.service.wireadmin.Wire;
import org.osgi.util.measurement.Measurement;
import org.osgi.util.measurement.Unit;
import org.osgi.util.position.Position;
import org.ow2.aspirerfid.util.config.Configuration;


public class GPSToolProducer implements Producer, PropertyChangeListener{

	GPSDataProcessor gps_data_processor;
	GPSDevice gps_device;

	private void _GPSToolProducer(Properties props) {
		try {

			Class clazz;

			clazz= Class.forName(props.getProperty("gpstool.gpsdataprocessor"));
			gps_data_processor = (GPSDataProcessor)clazz.newInstance();

			clazz= Class.forName(props.getProperty("gpstool.gpsdevice"));
			gps_device = (GPSDevice)clazz.newInstance();

			Hashtable environment = Configuration.getHashtable(props,"gpstool.initproperties");

			gps_device.init(environment);
			gps_data_processor.setGPSDevice(gps_device);

			gps_data_processor.open();

			gps_data_processor.addGPSDataChangeListener(GPSDataProcessor.LOCATION,this);
			gps_data_processor.addGPSDataChangeListener(GPSDataProcessor.HEADING,this);
			gps_data_processor.addGPSDataChangeListener(GPSDataProcessor.ALTITUDE,this);
			gps_data_processor.addGPSDataChangeListener(GPSDataProcessor.SPEED,this);
			gps_data_processor.addGPSDataChangeListener(GPSDataProcessor.NUMBER_SATELLITES,this);
			gps_data_processor.addGPSDataChangeListener(GPSDataProcessor.SATELLITE_INFO,this);

		} catch (Exception ex){
			ex.printStackTrace();
			return;
		}
	}

	public GPSToolProducer(Properties props) {
		_GPSToolProducer(props);
	}

	public GPSToolProducer(String filename) {
		Properties props=Configuration.loadProperties(filename);
		_GPSToolProducer(props);
	}

	public GPSToolProducer(InputStream in) {
		Properties props=Configuration.loadProperties(in);
		_GPSToolProducer(props);
	}

	public void close()
	{
		if(gps_data_processor != null)
		{
			try {
				gps_data_processor.close();
			} catch(GPSException ex) {
				ex.printStackTrace();
			}
			gps_data_processor = null;
		}

		if(gps_device != null)
		{
			try {
				gps_device.close();
			} catch(GPSException ex) {
				ex.printStackTrace();
			}
			gps_device = null;
		}
	}

	protected void finalize() {
		close();
	}

	
	// -------------------------------------------------------------------------
	// Procucer methods
	Wire wires[];

	private void updateWires() {
		Position lastPosition=getLatestKnownPosition();
		for( int i=0; wires!=null && i<wires.length; i++ )
			wires[i].update( lastPosition );
	}
	
	public synchronized void consumersConnected(Wire wires[]){
		this.wires = wires;
	}

	public Object polled(Wire wire) {
		if(wire==null)
			return getLatestKnownPosition();

		Class clazzes[] = wire.getFlavors();
		for ( int i=0; i<clazzes.length; i++ ) {
			Class clazz = clazzes[i];
			if ( clazz.isAssignableFrom( Position.class ) ){
				return getLatestKnownPosition();
			}
			if ( clazz.isAssignableFrom( String.class) ){
				return PositionUtil.toText(null,getLatestKnownPosition()).toString();
			}
		}
		return null;
	}
	
	
	/**
	* The last GPSPosition
	*/
	Position latestKnownPosition;
	
	// -------------------------------------------------------------------------
	/**
	 *  Returns the latest known position
	 *
	 *@return    The latestKnownPosition value
	 *@see org.osgi.util.position.Position
	 */
	public Position getLatestKnownPosition(){
		return new Position(
			(lastLatitude==Double.NaN)?null:new Measurement(			// latitude in radian
				(lastLatitude*Math.PI)/180,	// lastLatitude in degree
				Double.NaN,			// error is not know
				Unit.rad,
				lastLatLonTimestamp
			),
			(lastLongitude==Double.NaN)?null:new Measurement(			// longitude in radian
				(lastLongitude*Math.PI)/180,	// lastLongitude in degree
				Double.NaN,			// error is not know
				Unit.rad,
				lastLatLonTimestamp
			),
			(lastAltitude==Double.NaN)?null:new Measurement(			// altitude in meter
				lastAltitude,		
				Double.NaN,			// error is not know
				Unit.m,
				lastAltitudeTimestamp
			),
			(lastSpeed==Double.NaN)?null:new Measurement(			// speed in meter per second
				lastSpeed,
				Double.NaN,			// error is not know
				Unit.m_s,
				lastSpeedTimestamp
			),
			(lastHeading==Double.NaN)?null:new Measurement(			// track in radian
				(lastHeading*Math.PI)/180, 	// getCourse() returns degree
				0.0,
				Unit.rad,
				lastHeadingTimestamp
			)		
		);
	}

	// -------------------------------------------------------------------------
	// last position fields values
	
	double	lastLatitude=Double.NaN;
	double	lastLongitude=Double.NaN;
	long	lastLatLonTimestamp=0;
	
	double	lastAltitude=Double.NaN;
	long	lastAltitudeTimestamp=0;
	
	double	lastHeading=Double.NaN;
	long	lastHeadingTimestamp=0;
	
	double	lastSpeed=Double.NaN;
	long	lastSpeedTimestamp=0;
	
	// -------------------------------------------------------------------------

	public void propertyChange(PropertyChangeEvent event)
	{
		Object value = event.getNewValue();
		String name = event.getPropertyName();
		long currentTimestamp=System.currentTimeMillis();

		
		if(name.equals(GPSDataProcessor.SATELLITE_INFO)) {
			SatelliteInfo[] infos = (SatelliteInfo[])value;
			SatelliteInfo info;
			for(int count=0; count < infos.length; count++)
			{
				info = infos[count];
				trace("sat "+info.getPRN()+": elev="+info.getElevation()
				      + " azim="+info.getAzimuth()+" dB="+info.getSNR());
			}
		} else 	if(name.equals(GPSDataProcessor.LOCATION)) {
			// set current GPS Position
			//if(gpsposition==null) gpsposition=(GPSPosition)event.getNewValue();
			GPSPosition gpsposition=(GPSPosition)value;

			double latitude=gpsposition.getLatitude();
			if(latitude!=Double.NaN) lastLatitude=latitude;

			double longitude=gpsposition.getLongitude();
			if(longitude!=Double.NaN) lastLongitude=longitude;

			double altitude=gpsposition.getAltitude();
			if(altitude!=Double.NaN) {
				lastAltitude=altitude;
				lastAltitudeTimestamp=currentTimestamp;
			}

			lastLatLonTimestamp=currentTimestamp;

		} else if(name.equals(GPSDataProcessor.ALTITUDE)) {
			float altitude=((Float)value).floatValue();
			if(altitude!=Float.NaN){
				lastAltitude=altitude;
				lastAltitudeTimestamp=currentTimestamp;
			}
		} else if(name.equals(GPSDataProcessor.HEADING)) {
			// set current Course
			float heading=((Float)value).floatValue();
			if(heading==-1.0){
				lastHeading=Float.NaN;
			} else {
				lastHeading=heading;
				lastHeadingTimestamp=currentTimestamp;
			}
			
		} else if(name.equals(GPSDataProcessor.SPEED)) {
			// set current Speed
			float speed=((Float)value).floatValue();
			if(speed!=Float.NaN){
				lastSpeed=speed;
				lastSpeedTimestamp=currentTimestamp;
			}
		}
		
		updateWires();

		// add extra info
		// location.addExtraInfo("application/X-java-location-nmea", ???);

		trace(event.getPropertyName()+": "+event.getNewValue());
		
		trace(PositionUtil.toText(null,getLatestKnownPosition()).toString());

	}

	//--------------
	private boolean debug=true;
	final protected void trace(String msg){
		if(debug){ System.err.println(getClass().getName()+":"+msg); }
	}

}



