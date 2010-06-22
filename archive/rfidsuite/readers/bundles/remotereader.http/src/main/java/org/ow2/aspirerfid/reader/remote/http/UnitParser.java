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

import java.util.HashMap;

import org.osgi.util.measurement.Unit;

/**
 * Utility class for parsing textual units to OSGi's units for measurements
 * 
 * @author kiev
 *
 */
public abstract class UnitParser {
	private static HashMap unitsMap;
	
	static {
		Unit[] units = { Unit.A, Unit.C, Unit.cd, Unit.F, Unit.Gy, Unit.Hz,
				Unit.J, Unit.K, Unit.kat, Unit.kg, Unit.lx, Unit.m, Unit.m_s,
				Unit.m_s2, Unit.m2, Unit.m3, Unit.mol, Unit.N, Unit.Ohm, Unit.Pa,
				Unit.rad, Unit.s, Unit.S, Unit.T, Unit.unity, Unit.V, Unit.W,
				Unit.Wb };
		unitsMap = new HashMap();
		for (Unit unit : units) {
			unitsMap.put(unit.toString(), unit);
		}
	}
	
	private UnitParser() { }
	/**
	 * parses a unit string into a valid unit used by OSGi
	 * @param str
	 * @return
	 */
	public static Unit fromString(String str) {
		return (Unit)unitsMap.get(str);
	}
}
