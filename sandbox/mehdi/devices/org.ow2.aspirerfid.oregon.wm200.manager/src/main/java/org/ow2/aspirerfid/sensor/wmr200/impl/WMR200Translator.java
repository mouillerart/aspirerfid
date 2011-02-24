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

import java.util.Date;
import java.util.List;

/**
 * The Class WMR200Translator.
 * @author Elmehdi Damou
 */
public class WMR200Translator {
	
	

	
	
	/**
	 * Gets the ratio rainfall.
	 *
	 * @param rainfallList the rainfall list
	 * @return the ratio rainfall
	 */
	public float getRatioRainfall(List<Byte> rainfallList) {
		int rainfall_low = rainfallList.get(0);
		int rainfall_high = rainfallList.get(1);
		float rainfall = (float) ((float)(((rainfall_high*255) + rainfall_low) * 25.4) /100);
		return rainfall;
	}
	
	
	/**
	 * Gets the dew point.
	 *
	 * @param dewList the dew list
	 * @return the dew point
	 */
	public float getDewPoint(List<Byte> dewList) {
		
		int dewPointLow = dewList.get(0) & 0xff;
	
		int dewPointHigh = (dewList.get(1) & 0x0f);
		
		float dewPoint = (float)((float)((dewPointHigh*255) + dewPointLow ) / 10);
		
		int signe = (dewList.get(1) >> 4 );
		
		if (signe==0x08){
			dewPoint *= -1;
		}
		
		return dewPoint;
	}
	
	/**
	 * Gets the power.
	 *
	 * @param byte1 the byte1
	 * @return the power
	 */
	public int getPower(Byte byte1) {
		return (byte1 & 0xff);
	}
	
	/**
	 * Gets the sensor.
	 *
	 * @param byte1 the byte1
	 * @return the sensor
	 */
	public int getSensor(Byte byte1) {
		return (byte1 >> 4);
	}
	
	/**
	 * Gets the temperature.
	 *
	 * @param list the list
	 * @return the temperature
	 */
	public float getTemperature(List<Byte> list) {
		
		int tempLow = list.get(0) & 0xff ;
		
		int tempHight =((byte)list.get(1)) & (byte)(0x0f);
		
		float temperature = (float) ((float)((tempHight*255) + tempLow) /10);
				
		
		int signe = (list.get(1) >> 4) & 0xff;
		
		if (signe==0x08){
			temperature *= -1;
		}
		return  ((float) (temperature)) ;
		
	}
	
	/**
	 * Gets the date.
	 *
	 * @param byteDate the byte date
	 * @return the date
	 */
	@SuppressWarnings("deprecation")
	public Date getDate(List<Byte> byteDate) {
	
		int min = byteDate.get(0) & 0xff;
		int hrs = byteDate.get(1)& 0xff;
		int day = byteDate.get(2)& 0xff;
		int month = (byteDate.get(3)& 0xff) - 1 ;
		int year = (byteDate.get(4)& 0xff ) + 100; 
		
		Date date = new Date(year, month,day,hrs, min);
		
		return date;
	}
	
	/**
	 * Gets the altitude previson.
	 *
	 * @param byte1 the byte1
	 * @return the altitude previson
	 */
	public int getAltitudePrevison(Byte byte1) {	
		return (byte1 >> 4) ;
	}
	
	/**
	 * Gets the altitude pression.
	 *
	 * @param altitudeList the altitude list
	 * @return the altitude pression
	 */
	public int getAltitudePression(List<Byte> altitudeList) {
		int altitudeLow = altitudeList.get(0)& 0xff;
		int altitudeHigh = ((altitudeList.get(1)) & (byte)(0x0f)) << 8;
		return altitudeHigh + altitudeLow;
	}
	
	/**
	 * Gets the previson.
	 *
	 * @param byte1 the byte1
	 * @return the previson
	 */
	public int getPrevison(Byte byte1) {
		return (byte1 >> 4) & 0xff;
	}
	
	/**
	 * Gets the pression.
	 *
	 * @param pressionList the pression list
	 * @return the pression
	 */
	public int getPression(List<Byte> pressionList) {
		int pressionLow = pressionList.get(0) & 0xff;
		int pressionHigh = ((pressionList.get(1)) & (byte)(0x0f)) << 8;
		return pressionHigh + pressionLow;
	}
	
	/**
	 * Gets the wind average.
	 *
	 * @param avrgList the avrg list
	 * @return the wind average
	 */
	public float getWindAverage(List<Byte> avrgList) {
		int avrgLow = (avrgList.get(0) >> 4) & 0x0f ;
		int avrgHigh = (avrgList.get(1)<< 4) & 0xff ;
		float avrgSpeed = (float) ((float)(((float)avrgHigh+avrgLow) /10));
		return avrgSpeed;
	}
	

	
}

