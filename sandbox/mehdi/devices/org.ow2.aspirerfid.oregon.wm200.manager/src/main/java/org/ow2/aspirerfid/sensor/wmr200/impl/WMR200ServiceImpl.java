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

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.management.AttributeChangeNotification;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

import org.apache.log4j.Logger;
import org.osgi.service.wireadmin.Producer;
import org.ow2.aspirerfid.sensor.wmr200.service.WMR200Descriptor;
import org.ow2.aspirerfid.sensor.wmr200.service.WMR200Manager;
import org.ow2.aspirerfid.sensor.wmr200.service.WMR200MapKey;
import org.ow2.aspirerfid.sensor.wmr200.service.WMR200Service;

/**
 * The Class WMR200ServiceImpl.
 *  @author Elmehdi Damou
 */
public class WMR200ServiceImpl extends NotificationBroadcasterSupport implements WMR200Service, Runnable {

	/** The translator. */
	private WMR200Translator translator;

	/** The m_all data. */
	private Hashtable<String, Double> m_allData;

	/** Map<IEEEAdress, Time>. */
	private Hashtable<String, Date> m_lastMeasurementTimes;
	
	/** The old_m_all data. */
	Hashtable<String, Double> old_m_allData = m_allData;

	/** The old_m_last measurement times. */
	Hashtable<String, Date> old_m_lastMeasurementTimes = m_lastMeasurementTimes;

	/** The buf. */
	byte[] buf = new byte[WMR200Descriptor.WMR200_CRTL_MSG_SIZE];

	/** The big buffer. */
	List<Byte> bigBuffer = new ArrayList<Byte>();

	/** The wmr200 id. */
	private String wmr200ID; // injected by iPOJO configuration

	/** The wmr200 manager. */
	private WMR200Manager wmr200Manager; // injected by iPOJO configuration

	/** The initial time. */
	private int initialTime = 1000;

	/** The timer. */
	private ScheduledThreadPoolExecutor timer;

	/** The sched future. */
	private ScheduledFuture<?> schedFuture;

	/** The wmr200. */
	private WMR200Descriptor wmr200;

	/** The pooling. */
	private boolean pooling = false;


	/** The logger. */
	private Logger logger = Logger.getLogger(WMR200ServiceImpl.class
			.getName());

	/**
	 * iPOJO validate callback Opens the {@link WMR200Descriptor}.
	 */
	public void start() {
		logger.debug(">>wmr200 " + wmr200ID + " started.");
		translator = new WMR200Translator();
		if (!pooling){
			m_allData = new Hashtable<String, Double>();
			m_lastMeasurementTimes = new Hashtable<String, Date>();
			Pooling(2000, TimeUnit.MILLISECONDS);
		}
		
	}

	/**
	 * iPOJO invalidate callback Closes the {@link WMR200Descriptor}.
	 */
	public void stop() {
		logger.debug(">>wmr200 " + wmr200ID + " stopped");
		schedFuture.cancel(false);
		timer.purge();
		wmr200.close();
		
	}


	/**
	 * Gets the wM r200 id.
	 * 
	 * @return the wM r200 id
	 */
	public String getWMR200ID() {
		return wmr200ID;
	}

	/**
	 * Pooling.
	 * 
	 * @param interval
	 *            the interval
	 * @param unit
	 *            the unit
	 */
	private synchronized void Pooling(long interval, TimeUnit unit) {
		logger.debug(">>> Start Pooling...");
		if (timer == null || timer.isShutdown())
			this.timer = new ScheduledThreadPoolExecutor(1);
		wmr200 = wmr200Manager.getListWMR200().get(wmr200ID);
		if (wmr200 != null) {
			wmr200.open();
			try {
				wmr200.sendInitialisationTrame();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.schedFuture = this.timer.scheduleWithFixedDelay(this,
					initialTime, interval, unit);
			pooling = true;
		}

	}

	

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.sensor.wmr200.service.WMR200Service#reset()
	 */
	public void reset() {
		if (isWMR200Present()) {
			if (wmr200.isOpened()) {
				wmr200.close();
			} else {
				wmr200.open();
				wmr200.close();
			}
		}
	}

	/**
	 * Checks if is wM r200 present.
	 * 
	 * @return true, if is wM r200 present
	 */
	private boolean isWMR200Present() {
		wmr200 = wmr200Manager.getWMR200byID(wmr200ID);
		if (wmr200 != null) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		
		try {
			buf = wmr200.getDataFromWMR200();
			
			int donneeValide = buf[0] & 0xff; // Le premier byte indique le
												// nombre de donnee valide
												// dans la trame

			if (donneeValide > 0 && donneeValide < 8) {
				int indice=1;
				while (indice <= donneeValide) {
					bigBuffer.add((byte) buf[indice++]);
				}
				indice = 1;
			}

			
			if (bigBuffer.size() >= 2) {
				
				 if ((bigBuffer.get(0) & 0xff)<210 || (bigBuffer.get(0) & 0xff)>217 || (bigBuffer.get(1) & 0xff)>50  ){ //keep only value that begin with d2 to d9
					bigBuffer.clear();
				}
				 else if (bigBuffer.size() == (bigBuffer.get(1) & 0xff)) {

					startWorking(bigBuffer);
					bigBuffer.clear();
				} else if (bigBuffer.size() > (bigBuffer.get(1) & 0xff)) {
					startWorking(bigBuffer
							.subList(0, (bigBuffer.get(1) & 0xff)));
					bigBuffer = bigBuffer.subList((bigBuffer.get(1) & 0xff),
							bigBuffer.size());
				}
				
			}

						
		} catch (InterruptedException e) {
			logger.debug(e.getMessage(),e);
		}
	}

	/**
	 * Start working.
	 * 
	 * @param bigBuffer
	 *            the big buffer
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	private void startWorking(List<Byte> bigBuffer) throws InterruptedException {
		String st = null;
		if (bigBuffer.size() > 0 && WMR200Utils.checkSumState(bigBuffer)) {
			st = "Chaine a traiter : \n		";
			for (Byte byte1 : bigBuffer) {
				st = st + (Integer.toHexString(byte1 & 0xff) + " ");
			}

			if (st != null) {
				logger.info(st);
			}

			switch (bigBuffer.get(0)) {

			case ((byte) 0xd1):
				// Message d1
				logger.info("Entre dans d1");
				readUnknownD1();
				break;

			case ((byte) 0xd2):
				// Message d2 : History
				logger.info("Entre dans d2");
				readHistoryFromD2(bigBuffer);
				break;

			case ((byte) 0xd3):
				// Message d3 : Anemometer (Wind)
				logger.info("Entre dans d3");
				readAnemometerD3(bigBuffer);
				break;

			case ((byte) 0xd4):
				// Message d4 : Rain gauge (Rainfall)
				logger.info("Entre dans d4");
				readRainGaugeD4(bigBuffer);
				break;

			case ((byte) 0xd5):
				// Message d5 : UV Meter
				logger.info("Entre dans d5");
				readUVMeterD5(bigBuffer);
				break;

			case ((byte) 0xd6):
				// Message d6 : Barometer
				logger.info("Entre dans d6");
				readBarometerD6(bigBuffer);
				break;

			case ((byte) 0xd7):
				// Message d6 : Thermometer (Temperature)
				logger.info("Entre dans d7");
				readThermometerD7(bigBuffer);
				break;

			case ((byte) 0xd8):
				// Message d8 :
				logger.info("Entre dans d8");
				readUnknownD8();
				break;
			case ((byte) 0xd9):
				// Message d9 : Data Logger Information
				logger.info("Entre dans d9");
				readDataLoggerStateD9();
				break;

			default:
				logger.info("UNKNOWN DATA");
				break;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.sensor.wmr200.host.WMR200Data#getunknownD1()
	 */
	/**
	 * Gets the unknown d1.
	 * 
	 * @return the unknown d1
	 */
	public String readUnknownD1() {
		// TODO Auto-generated method stub
		return "Never Catched!!!";

	}

	/**
	 * Gets the history from d2.
	 * 
	 * @param bigBuffer
	 *            the big buffer
	 * @return the history from d2
	 */
	public void readHistoryFromD2(List<Byte> bigBuffer) {

		System.out.println("find D2");
		//TODO check the cache of WMR200
		// List<Byte> date = bigBuffer.subList(0, 7);
		//
		// List<Byte> temp = bigBuffer.subList(0, 7);
		//
		// // List<Byte> temp = {0x00,0x00};
		//
		// List<Byte> anemometer = bigBuffer.subList(20,25);
		//
		// List<Byte> baromometer = bigBuffer.subList(28,32);
		//
		// List<Byte> thermometer = bigBuffer.subList(33,39);
		//
		// List<Byte> thermometer2 = bigBuffer.subList(40,46);
		//
		//
		// temp.addAll(anemometer);
		//
		// getAnemometerD3(temp);
		//
		// temp = date;
		//
		// temp.addAll(baromometer);
		//
		//
		// temp = date;
		//
		// temp.addAll(thermometer);
		//
		//
		// temp = date;
		//
		// temp.addAll(thermometer2);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.aspirerfid.sensor.wmr200.host.WMR200Data#getAnemometerD3(java
	 * .util.List)
	 */
	/**
	 * Gets the anemometer d3.
	 * 
	 * @param bigBuffer
	 *            the big buffer
	 * @return the anemometer d3
	 */
	public void readAnemometerD3(List<Byte> bigBuffer) {

		Date date = translator.getDate(bigBuffer.subList(2, 7));

		int winDirection = bigBuffer.get(7) & 0xff;

		// int unknownData = bigBuffer.get(8);

		float windSpeed = (float) ((float) (((bigBuffer.get(9) & 0xff) / 10.0)));

		float windAverageSpeed = translator.getWindAverage(bigBuffer.subList(
				10, 12));

		logger.info("wind Direction : "
				+ WMR200MapKey.WIND_DIRECTION_TAB[winDirection]
				+ " | wind Speed   : " + windSpeed + " | Wind Avrg : "
				+ windAverageSpeed + " | Date : " + date);

		old_m_allData = m_allData;
		old_m_lastMeasurementTimes = m_lastMeasurementTimes;
		
		m_lastMeasurementTimes.put(WMR200MapKey.WINDDIRECTION_TYPE, date);
		m_allData.put(WMR200MapKey.WINDDIRECTION_TYPE, new Double(winDirection));
//
//		myNotificationSend("Anemometer Changed");
//
		m_lastMeasurementTimes.put(WMR200MapKey.WINDAVERGAE_TYPE, date);
		m_allData.put(WMR200MapKey.WINDAVERGAE_TYPE, new Double(windAverageSpeed));
		
		m_lastMeasurementTimes.put(WMR200MapKey.WINDSPEED_TYPE, date);
		m_allData.put(WMR200MapKey.WINDSPEED_TYPE, new Double(winDirection));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.aspirerfid.sensor.wmr200.host.WMR200Data#getRainGaugeD4(java.
	 * util.List)
	 */
	/**
	 * Gets the rain gauge d4.
	 * 
	 * @param bigBuffer
	 *            the big buffer
	 * @return the rain gauge d4
	 */
	public void readRainGaugeD4(List<Byte> bigBuffer) {

		Date date = translator.getDate(bigBuffer.subList(2, 7));

		int sensor = translator.getSensor(bigBuffer.get(7));

		int power = translator.getPower(bigBuffer.get(7));

		int rate = bigBuffer.get(8) & 0xff;

		float rainFallH = translator.getRatioRainfall(bigBuffer.subList(9, 11));

		float rainFall24H = translator.getRatioRainfall(bigBuffer.subList(11,
				13));

		float rainfallTotal = translator.getRatioRainfall(bigBuffer.subList(13,
				15));

		Date dateTotal = translator.getDate(bigBuffer.subList(15, 20));

		logger.info("sensor : " + sensor + " |power : " + power
				+ " | Rainfall Rate   : " + rate + " | Rainfall Heure : "
				+ rainFallH + " | Rainfall 24 Heure : " + rainFall24H
				+ " | Rainfall Total : " + rainfallTotal + " | Date Total : "
				+ dateTotal + " | Date : " + date);

		old_m_allData = m_allData;
		old_m_lastMeasurementTimes = m_lastMeasurementTimes;

		m_lastMeasurementTimes.put(WMR200MapKey.RAIN_RATE_TYPE, date);
		m_allData.put(WMR200MapKey.RAIN_RATE_TYPE, new Double(rate));

		myNotificationSend("RAin GAuge Changed");


		m_lastMeasurementTimes.put(WMR200MapKey.RAINFALL_H_TYPE, date);
		m_allData.put(WMR200MapKey.RAINFALL_H_TYPE, new Double(rainFallH));


		m_lastMeasurementTimes.put(WMR200MapKey.RAINFALL_24H_TYPE, date);
		m_allData.put(WMR200MapKey.RAINFALL_24H_TYPE, new Double(rainFall24H));

		

		m_lastMeasurementTimes.put(WMR200MapKey.RAINFALL_TOTAL_TYPE, dateTotal);
		m_allData.put(WMR200MapKey.RAINFALL_TOTAL_TYPE, new Double(
				rainfallTotal));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.aspirerfid.sensor.wmr200.host.WMR200Data#getUVMeterD5(java.util
	 * .List)
	 */
	/**
	 * Gets the uV meter d5.
	 * 
	 * @param bigBuffer
	 *            the big buffer
	 * @return the uV meter d5
	 */
	public void readUVMeterD5(List<Byte> bigBuffer) {
		Date date = translator.getDate(bigBuffer.subList(2, 7));
		int uv = bigBuffer.get(7) & 0xff;

		logger.info("UV : " + uv + " | Date : " + date);

		old_m_allData = m_allData;
		old_m_lastMeasurementTimes = m_lastMeasurementTimes;

		m_lastMeasurementTimes.put(WMR200MapKey.UV_TYPE, date);
		m_allData.put(WMR200MapKey.UV_TYPE, new Double(uv));

//		myNotificationSend("UvMeter Changed");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.aspirerfid.sensor.wmr200.host.WMR200Data#getBarometerD6(java.
	 * util.List)
	 */
	/**
	 * Gets the barometer d6.
	 * 
	 * @param bigBuffer
	 *            the big buffer
	 * @return the barometer d6
	 */
	public void readBarometerD6(List<Byte> bigBuffer) {

		Date date = translator.getDate(bigBuffer.subList(2, 7));

		int pression = translator.getPression(bigBuffer.subList(7, 9));

		int prevision = translator.getPrevison(bigBuffer.get(8));

		int altitudePression = translator.getAltitudePression(bigBuffer
				.subList(9, 11));

		int altitutePrevision = translator.getAltitudePrevison(bigBuffer
				.get(10));

		logger.info("pression : " + pression + " |prevision : "
				+ WMR200MapKey.PREVISION_TEXT[prevision]
				+ " |Altitude pression  : " + altitudePression
				+ " | Altitude prevision : "
				+ WMR200MapKey.PREVISION_TEXT[altitutePrevision] + " |Date : "
				+ date);

		old_m_allData = m_allData;
		old_m_lastMeasurementTimes = m_lastMeasurementTimes;

		m_lastMeasurementTimes.put(WMR200MapKey.PRESSION_TYPE, date);
		m_allData.put(WMR200MapKey.PRESSION_TYPE, new Double(pression));

		myNotificationSend("Barometer Changed");

		

		m_lastMeasurementTimes.put(WMR200MapKey.PREVISION_TYPE, date);
		m_allData.put(WMR200MapKey.PREVISION_TYPE, new Double(prevision));
//
//		
//
		m_lastMeasurementTimes.put(WMR200MapKey.ALTITUDE_PRESSION_TYPE, date);
		m_allData.put(WMR200MapKey.ALTITUDE_PRESSION_TYPE, new Double(
				altitudePression));

		

		m_lastMeasurementTimes.put(WMR200MapKey.ALTITUDE_PREVISION_TYPE, date);
		m_allData.put(WMR200MapKey.ALTITUDE_PREVISION_TYPE, new Double(
				altitutePrevision));

		

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.aspirerfid.sensor.wmr200.host.WMR200Data#getThermometerD7(java
	 * .util.List)
	 */
	/**
	 * Gets the thermometer d7.
	 * 
	 * @param bigBuffer
	 *            the big buffer
	 * @return the thermometer d7
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	public void readThermometerD7(List<Byte> bigBuffer)
			throws InterruptedException {

		Date date = translator.getDate(bigBuffer.subList(2, 7));

		int sensor = translator.getSensor(bigBuffer.get(7));

		int power = translator.getPower(bigBuffer.get(7)); // if powwer =1 out
															// else if 0 in

		float temperature = translator.getTemperature(bigBuffer.subList(8, 10));

		int humidity = bigBuffer.get(10) & 0xff;

		float dewPoint = translator.getDewPoint(bigBuffer.subList(11, 13));

		int unknownValue = bigBuffer.get(13) & 0xff;

		logger.info("SensoTemperature : " + temperature + " |humidity : "
				+ humidity + "% |Dew Point : " + dewPoint + " | Sensor : "
				+ sensor + " | Power :" + power + " (Value On 13): "
				+ unknownValue + " |Date : " + date);

		old_m_allData = m_allData;
		old_m_lastMeasurementTimes = m_lastMeasurementTimes;

		if ((bigBuffer.get(7) & 0xff) != 1) {// indoor
			m_lastMeasurementTimes.put(WMR200MapKey.TEMPERATURE_TYPE, date);
			m_allData.put(WMR200MapKey.TEMPERATURE_TYPE,
					new Double(temperature));
			m_lastMeasurementTimes.put(WMR200MapKey.HUMIDITY_TYPE, date);
			m_allData.put(WMR200MapKey.HUMIDITY_TYPE, new Double(humidity));
			m_lastMeasurementTimes.put(WMR200MapKey.DEWPOINT_TYPE, date);
			m_allData.put(WMR200MapKey.DEWPOINT_TYPE, new Double(dewPoint));
//
//			
//		
//
		} else {// outdoor
			m_lastMeasurementTimes.put(WMR200MapKey.TEMPERATURE_OUT_TYPE, date);
			m_allData.put(WMR200MapKey.TEMPERATURE_OUT_TYPE, new Double(
					temperature));
			m_lastMeasurementTimes.put(WMR200MapKey.HUMIDITY_OUT_TYPE, date);
			m_allData.put(WMR200MapKey.HUMIDITY_OUT_TYPE, new Double(humidity));
			m_lastMeasurementTimes.put(WMR200MapKey.DEWPOINT_OUT_TYPE, date);
			m_allData.put(WMR200MapKey.DEWPOINT_OUT_TYPE, new Double(dewPoint));

		}
//
//		myNotificationSend("Thermometer Changed");

	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.sensor.wmr200.host.WMR200Data#getunknownD8()
	 */
	/**
	 * Gets the unknown d8.
	 * 
	 * @return the unknown d8
	 */
	public String readUnknownD8() {
		// TODO Auto-generated method stub
		return "This data is never Catched";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.aspirerfid.sensor.wmr200.host.WMR200Data#getDataLoggerStateD9()
	 */
	/**
	 * Gets the data logger state d9.
	 * 
	 * @return the data logger state d9
	 */
	public void readDataLoggerStateD9() {
		// TODO Auto-generated method stub

	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.aspirerfid.sensor.wmr200.host.WMR200Service#getallData(java.lang
	 * .String)
	 */
	public Double getAData(String address) {
		return (m_allData.containsKey(address)) ? m_allData.get(address) : 0.0d;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.aspirerfid.sensor.wmr200.host.WMR200DeviceMBean#printAllData()
	 */
	/**
	 * Prints the all data.
	 */
	public void printAllData() {
		String total = "List Of Current Data (" + m_allData.size()
				+ " data): \n	";

		for (String element : m_allData.keySet()) {
			total = total + element + " : " + m_allData.get(element).toString()
					+ " \n	";
		}

		logger.info(total);
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.sensor.wmr200.host.WMR200DeviceMBean#
	 * getLastMeasurementTime(java.lang.String)
	 */
	/**
	 * Gets the last measurement time.
	 *
	 * @param address the address
	 * @return the last measurement time
	 */
	public Date getLastMeasurementTime(String address) {
		return (m_lastMeasurementTimes.containsKey(address)) ? m_lastMeasurementTimes
				.get(address) : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.aspirerfid.sensor.wmr200.host.WMR200DeviceMBean#printMeasurementsTime
	 * ()
	 */
	/**
	 * Prints the measurements time.
	 */
	public void printMeasurementsTime() {
		String total = "List Of Last Measurement of Current Data ("
				+ m_lastMeasurementTimes.size() + " time): \n	";

		for (String element : m_lastMeasurementTimes.keySet()) {
			total = total + element + " : "
					+ m_lastMeasurementTimes.get(element).toString() + " \n	";
		}
		logger.info(total);
	}




	/**
	 * My notification send.
	 * 
	 * @param msg
	 *            the msg
	 */
	private void myNotificationSend(String msg) {
//		Notification n = new AttributeChangeNotification(this, sequence ++,
//				System.currentTimeMillis(), msg, "m_allData", "Hashtable",
//				old_m_allData, m_allData);
//		sendNotification(n);
//
//		n = new AttributeChangeNotification(this, sequence++,
//				System.currentTimeMillis(), msg + " Date",
//				"m_lastMeasurementTimes", "Hashtable",
//				old_m_lastMeasurementTimes, m_lastMeasurementTimes);
//
//		sendNotification(n);
	}
}
