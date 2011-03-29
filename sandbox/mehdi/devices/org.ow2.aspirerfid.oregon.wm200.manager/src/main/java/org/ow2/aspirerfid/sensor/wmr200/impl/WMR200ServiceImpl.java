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
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.management.NotificationBroadcasterSupport;

import org.apache.log4j.Logger;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.ow2.aspirerfid.sensor.wmr200.service.ANEMOMETER;
import org.ow2.aspirerfid.sensor.wmr200.service.BAROMETER;
import org.ow2.aspirerfid.sensor.wmr200.service.PLUVIOMETER;
import org.ow2.aspirerfid.sensor.wmr200.service.THERMOMETER;
import org.ow2.aspirerfid.sensor.wmr200.service.UVMETER;
import org.ow2.aspirerfid.sensor.wmr200.service.WMR200Descriptor;
import org.ow2.aspirerfid.sensor.wmr200.service.WMR200Manager;
import org.ow2.aspirerfid.sensor.wmr200.service.WMR200Service;

import com.sun.java.swing.plaf.windows.WindowsTreeUI.CollapsedIcon;

// TODO: Auto-generated Javadoc
/**
 * The Class WMR200ServiceImpl.
 * 
 * @author Elmehdi Damou
 */
public class WMR200ServiceImpl extends NotificationBroadcasterSupport implements
		WMR200Service, Runnable {

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

	/** The event admin. */
	private EventAdmin eventAdmin;

	/** The topics. */
	private String topic;

	/** The logger. */
	private Logger logger = Logger.getLogger(WMR200ServiceImpl.class.getName());

	private int badFrames = 0;

	private Map<Byte, List<Byte>> frames;

	/**
	 * iPOJO validate callback Opens the {@link WMR200Descriptor}.
	 */
	public void start() {
		logger.debug(">>wmr200 " + wmr200ID + " started.");
		translator = new WMR200Translator();
		if (!pooling) {
			m_allData = new Hashtable<String, Double>();
			m_lastMeasurementTimes = new Hashtable<String, Date>();
			Pooling(2500, TimeUnit.MILLISECONDS);

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
			// this.timer.sc
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
			List<Byte> bigBuffer = new ArrayList<Byte>();
			buf = wmr200
					.sendCommandAndReceiveDataFromWMR200(WMR200Descriptor.WMR200_REQUEST_NEXT_DATA);

			while (buf != null) {
				int donneeValide = buf[0] & 0xff; // Le premier byte indique le
				// nombre de donnee valide
				// dans la trame
				logger.debug("valide data : " + donneeValide);
				for (int i = 1; i <= donneeValide; i++) {
					if (bigBuffer.size()==0){
						if (buf[i]>= (byte)0xD1 & buf[i]<=(byte)0xD9){
							bigBuffer.add((byte) buf[i]);
						}
					}else {
						bigBuffer.add((byte) buf[i]);
					}
					
				}
				logger.debug("current bigbuffer " + wmr200.printByteArray(bigBuffer));
				Thread.currentThread().sleep(2000);
				buf = wmr200
						.sendCommandAndReceiveDataFromWMR200(WMR200Descriptor.WMR200_REQUEST_NEXT_DATA);
			}

			while (bigBuffer.size() > 0) {
				if (((byte)bigBuffer.get(0) < (byte)0xD1) || ((byte)bigBuffer.get(0) > (byte)0xD9)) {
					// All frames must start with 0xD1 - 0xD9. If the first
					// byte is not within this range, we don't have a proper
					// frame start. Discard all octets and restart with the next
					// packet.
					logger.debug("Bad frame: " + wmr200.printByteArray(bigBuffer));
					this.badFrames += 1;
					break;
				}

				if (((byte)bigBuffer.get(0) == (byte)0xD1) && bigBuffer.size() == 1) {
					// 0xD1 frames have only 1 octet.
					logger.debug("D1 frame:" + wmr200.printByteArray(bigBuffer));
					startWorking(bigBuffer.subList(1, bigBuffer.size()));
					bigBuffer = bigBuffer.subList(1, bigBuffer.size());
					
				} else if ((bigBuffer.size() < 2)
						|| (bigBuffer.size() < bigBuffer.get(1))) {
					// 0xD2 - 0xD9 frames use the 2nd octet to specifiy the
					// length of the frame. The length includes the type and length octet.
					logger.debug("Short frame:" + wmr200.printByteArray(bigBuffer));
					badFrames += 1;
					break;
				} else {
					// This is for all frames with length byte and checksum.
					logger.debug("DX frame:" + wmr200.printByteArray(bigBuffer));
					if ((byte)bigBuffer.get(0) == (byte)0xD1){
						bigBuffer = bigBuffer.subList(1, bigBuffer.size());
					}
					
					while (bigBuffer.size()!=0 && bigBuffer.get(1)!=0){
						logger.debug("DX frame:" + wmr200.printByteArray(bigBuffer));
						startWorking(new ArrayList<Byte>(bigBuffer.subList(0, bigBuffer.get(1))));
						bigBuffer = bigBuffer.subList(bigBuffer.get(1),	bigBuffer.size());
						logger.debug("After DX frame:" + wmr200.printByteArray(bigBuffer));
						if (bigBuffer.size() < bigBuffer.get(1)){
							bigBuffer.clear();
						}
					}
				}

				// if (bigBuffer.size() >= 2) {
				//
				// if ((bigBuffer.get(0) & 0xff)<210 || (bigBuffer.get(0) &
				// 0xff)>217 || (bigBuffer.get(1) & 0xff)>50 ){ //keep only
				// value
				// that begin with d2 to d9
				// bigBuffer.clear();
				// }
				// else if (bigBuffer.size() == (bigBuffer.get(1) & 0xff)) {
				//
				// startWorking(bigBuffer);
				// bigBuffer.clear();
				// } else if (bigBuffer.size() > (bigBuffer.get(1) & 0xff)) {
				// startWorking(bigBuffer
				// .subList(0, (bigBuffer.get(1) & 0xff)));
				// bigBuffer = bigBuffer.subList((bigBuffer.get(1) & 0xff),
				// bigBuffer.size());
				// }
				//
				// }
			}
		} catch (Exception e) {
			logger.debug(e.getMessage(), e);
		}
	}

	/**
	 * Start working.
	 * 
	 * @param frame
	 *            the big buffer
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	private void startWorking(List<Byte> frame) throws InterruptedException {
		String st = null;
		if (frame.size() > 0 && WMR200Utils.checkSumState(frame)) {
			st = "Chaine a traiter : \n		";
			for (Byte byte1 : frame) {
				st = st + (Integer.toHexString(byte1 & 0xff) + " ");
			}

			if (st != null) {
				logger.info(st);
			}

			switch (frame.get(0)) {

			case ((byte) 0xd1):
				// Message d1
				logger.info("Entre dans d1");
				readUnknownD1();
				logger.info("Sort de d1");
				break;

			case ((byte) 0xd2):
				// Message d2 : History
				logger.info("Entre dans d2");
				readHistoryFromD2(frame);
				logger.info("Sort de d2");
				break;

			case ((byte) 0xd3):
				// Message d3 : Anemometer (Wind)
				logger.info("Entre dans d3");
				readAnemometerD3(frame);
				logger.info("Sort de d3");
				break;

			case ((byte) 0xd4):
				// Message d4 : Rain gauge (Rainfall)
				logger.info("Entre dans d4");
				readRainGaugeD4(frame);
				logger.info("Sort de d4");
				break;

			case ((byte) 0xd5):
				// Message d5 : UV Meter
				logger.info("Entre dans d5");
				readUVMeterD5(frame);
				logger.info("Sort de d5");
				break;

			case ((byte) 0xd6):
				// Message d6 : Barometer
				logger.info("Entre dans d6");
				readBarometerD6(frame);
				logger.info("Sort de d6");
				break;

			case ((byte) 0xd7):
				// Message d6 : Thermometer (Temperature)
				logger.info("Entre dans d7");
				readThermometerD7(frame);
				logger.info("Sort de d7");
				break;

			case ((byte) 0xd8):
				// Message d8 :
				logger.info("Entre dans d8");
				readUnknownD8();
				logger.info("Sort de d8");
				break;
			case ((byte) 0xd9):
				// Message d9 : Data Logger Information
				logger.info("Entre dans d9");
				readDataLoggerStateD9();
				logger.info("Sort de d9");
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
		// TODO check the cache of WMR200
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
				+ ANEMOMETER.WIND_DIRECTION_TAB[winDirection]
				+ " | wind Speed   : " + windSpeed + " | Wind Avrg : "
				+ windAverageSpeed + " | Date : " + date);

		old_m_allData = m_allData;
		old_m_lastMeasurementTimes = m_lastMeasurementTimes;

		m_lastMeasurementTimes.put(ANEMOMETER.WIND_DIRECTION, date);
		m_allData.put(ANEMOMETER.WIND_DIRECTION, new Double(winDirection));

		m_lastMeasurementTimes.put(ANEMOMETER.WIND_AVERAGE, date);
		m_allData.put(ANEMOMETER.WIND_AVERAGE, new Double(windAverageSpeed));

		m_lastMeasurementTimes.put(ANEMOMETER.WIND_SPEED, date);
		m_allData.put(ANEMOMETER.WIND_SPEED, new Double(windSpeed));
		sendNotification(ANEMOMETER.NAME);

	}

	/**
	 * Send notification.
	 * 
	 * @param sensorName
	 *            the sensor name
	 */
	private void sendNotification(String sensorName) {
		Dictionary<String, String> props = new Hashtable<String, String>();
		props.put(WMR200Service.SENSOR, sensorName);
		eventAdmin.postEvent(new Event(getTopic(), props));

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

		m_lastMeasurementTimes.put(PLUVIOMETER.RAIN_RATE, date);
		m_allData.put(PLUVIOMETER.RAIN_RATE, new Double(rate));

		m_lastMeasurementTimes.put(PLUVIOMETER.RAINFALL_H, date);
		m_allData.put(PLUVIOMETER.RAINFALL_H, new Double(rainFallH));

		m_lastMeasurementTimes.put(PLUVIOMETER.RAINFALL_24H, date);
		m_allData.put(PLUVIOMETER.RAINFALL_24H, new Double(rainFall24H));

		m_lastMeasurementTimes.put(PLUVIOMETER.RAINFALL_TOTAL, dateTotal);
		m_allData.put(PLUVIOMETER.RAINFALL_TOTAL, new Double(rainfallTotal));

		sendNotification(PLUVIOMETER.NAME);
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

		m_lastMeasurementTimes.put(UVMETER.UV, date);
		m_allData.put(UVMETER.UV, new Double(uv));

		sendNotification(UVMETER.NAME);

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
				+ BAROMETER.PREVISION_TEXT[prevision]
				+ " |Altitude pression  : " + altitudePression
				+ " | Altitude prevision : "
				+ BAROMETER.PREVISION_TEXT[altitutePrevision] + " |Date : "
				+ date);

		old_m_allData = m_allData;
		old_m_lastMeasurementTimes = m_lastMeasurementTimes;

		m_lastMeasurementTimes.put(BAROMETER.PRESSURE, date);
		m_allData.put(BAROMETER.PRESSURE, new Double(pression));

		m_lastMeasurementTimes.put(BAROMETER.PREVISION, date);
		m_allData.put(BAROMETER.PREVISION, new Double(prevision));

		m_lastMeasurementTimes.put(BAROMETER.ALTITUDE_PRESSURE, date);
		m_allData
				.put(BAROMETER.ALTITUDE_PRESSURE, new Double(altitudePression));

		m_lastMeasurementTimes.put(BAROMETER.ALTITUDE_PREVISION, date);
		m_allData.put(BAROMETER.ALTITUDE_PREVISION, new Double(
				altitutePrevision));

		sendNotification(BAROMETER.NAME);

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
			m_lastMeasurementTimes.put(THERMOMETER.INDOOR_TEMPERATURE, date);
			m_allData.put(THERMOMETER.INDOOR_TEMPERATURE, new Double(
					temperature));
			m_lastMeasurementTimes.put(THERMOMETER.INDOOR_HUMIDITY, date);
			m_allData.put(THERMOMETER.INDOOR_HUMIDITY, new Double(humidity));
			m_lastMeasurementTimes.put(THERMOMETER.INDOOR_DEW_POINT, date);
			m_allData.put(THERMOMETER.INDOOR_DEW_POINT, new Double(dewPoint));
			sendNotification(THERMOMETER.INDOOR_NAME);

		} else {// outdoor
			m_lastMeasurementTimes.put(THERMOMETER.OUTDOOR_TEMPERATURE, date);
			m_allData.put(THERMOMETER.OUTDOOR_TEMPERATURE, new Double(
					temperature));
			m_lastMeasurementTimes.put(THERMOMETER.OUTDOOR_HUMIDITY, date);
			m_allData.put(THERMOMETER.OUTDOOR_HUMIDITY, new Double(humidity));
			m_lastMeasurementTimes.put(THERMOMETER.OUTDOOR_DEW_POINT, date);
			m_allData.put(THERMOMETER.OUTDOOR_DEW_POINT, new Double(dewPoint));
			sendNotification(THERMOMETER.OUTDOOR_NAME);
		}
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
	public Double getAValue(String address) {
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
	 * @param address
	 *            the address
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
		// Notification n = new AttributeChangeNotification(this, sequence ++,
		// System.currentTimeMillis(), msg, "m_allData", "Hashtable",
		// old_m_allData, m_allData);
		// sendNotification(n);
		//
		// n = new AttributeChangeNotification(this, sequence++,
		// System.currentTimeMillis(), msg + " Date",
		// "m_lastMeasurementTimes", "Hashtable",
		// old_m_lastMeasurementTimes, m_lastMeasurementTimes);
		//
		// sendNotification(n);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.sensor.wmr200.service.WMR200Service#getAllData()
	 */
	public Map<String, Double> getAllData() {
		return m_allData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.aspirerfid.sensor.wmr200.service.WMR200Service#getAllMesurementTime
	 * ()
	 */
	public Map<String, Date> getAllMeasurementTimes() {
		return m_lastMeasurementTimes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ow2.aspirerfid.sensor.wmr200.service.WMR200Service#getTopic()
	 */
	public String getTopic() {
		return topic;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.ow2.aspirerfid.sensor.wmr200.service.WMR200Service#setTopic(java.
	 * lang.String)
	 */
	public void setTopic(String topicName) {
		topic = topicName;

	}

}
