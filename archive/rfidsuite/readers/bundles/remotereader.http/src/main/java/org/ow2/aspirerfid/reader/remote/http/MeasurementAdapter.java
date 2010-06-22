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
package org.ow2.aspirerfid.reader.remote.http;

import org.osgi.util.measurement.Measurement;
import org.osgi.util.measurement.Unit;

/**
 * Adapts the measurements read via HTTP
 * 
 * @author kiev
 *
 */
public class MeasurementAdapter {
	public static final String UNIT = "unit";
	public static final String ERROR = "error";
	private String measurementName; //eg. latitude, speed, etc...
	private double value;
	//default unit
	private Unit unit = Unit.unity;
	private double error;
	
	public MeasurementAdapter(String measurementName) {
		this.measurementName = measurementName;
	}
	
	public String getMeasurementName() {
		return measurementName;
	}
	
	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public double getError() {
		return error;
	}

	public void setError(double error) {
		this.error = error;
	}

	/**
	 * gets a representation of the values as an OSGi Measurement object
	 * @return
	 */
	public Measurement adaptValue() {
		return null;
	}
}