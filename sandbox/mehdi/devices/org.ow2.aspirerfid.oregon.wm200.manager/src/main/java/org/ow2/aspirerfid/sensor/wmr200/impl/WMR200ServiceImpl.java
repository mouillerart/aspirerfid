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
import org.ow2.aspirerfid.sensor.wmr200.service.THERMOMETER_INDOOR;
import org.ow2.aspirerfid.sensor.wmr200.service.THERMOMETER_OUTDOOR;
import org.ow2.aspirerfid.sensor.wmr200.service.UVMETER;
import org.ow2.aspirerfid.sensor.wmr200.service.Utils;
import org.ow2.aspirerfid.sensor.wmr200.service.WMR200Descriptor;
import org.ow2.aspirerfid.sensor.wmr200.service.WMR200Manager;
import org.ow2.aspirerfid.sensor.wmr200.service.WMR200Service;

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
    private Hashtable<Enum, Double> m_allData;

    /** Map<IEEEAdress, Time>. */
    private Hashtable<Enum, Date> m_lastMeasurementTimes;

    /** The old_m_all data. */
    Hashtable<Enum, Double> old_m_allData = this.m_allData;

    /** The old_m_last measurement times. */
    Hashtable<Enum, Date> old_m_lastMeasurementTimes = this.m_lastMeasurementTimes;

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

    /** The bad frames. */
    private int badFrames = 0;

    /** The frames. */
    private Map<Byte, List<Byte>> frames;

    /**
     * iPOJO validate callback Opens the {@link WMR200Descriptor}.
     */
    public void start() {
        this.logger.debug(">>wmr200 " + this.wmr200ID + " started.");
        this.translator = new WMR200Translator();
        if (!this.pooling) {
            this.m_allData = new Hashtable<Enum, Double>();
            this.m_lastMeasurementTimes = new Hashtable<Enum, Date>();
            Pooling(2500, TimeUnit.MILLISECONDS);

        }

    }

    /**
     * iPOJO invalidate callback Closes the {@link WMR200Descriptor}.
     */
    public void stop() {
        this.logger.debug(">>wmr200 " + this.wmr200ID + " stopped");
        this.schedFuture.cancel(false);
        this.timer.purge();
        this.wmr200.close();

    }

    /**
     * Gets the wM r200 id.
     * 
     * @return the wM r200 id
     */
    public String getWMR200ID() {
        return this.wmr200ID;
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
        this.logger.debug(">>> Start Pooling...");
        if ((this.timer == null) || this.timer.isShutdown()) {
            this.timer = new ScheduledThreadPoolExecutor(1);
        }
        this.wmr200 = this.wmr200Manager.getListWMR200().get(this.wmr200ID);
        if (this.wmr200 != null) {
            this.wmr200.open();
            try {
                this.wmr200.sendInitialisationTrame();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.schedFuture = this.timer.scheduleWithFixedDelay(this,
                    this.initialTime, interval, unit);
            // this.timer.sc
            this.pooling = true;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.ow2.aspirerfid.sensor.wmr200.service.WMR200Service#reset()
     */
    public void reset() {
        if (isWMR200Present()) {
            if (this.wmr200.isOpened()) {
                this.wmr200.close();
            } else {
                this.wmr200.open();
                this.wmr200.close();
            }
        }
    }

    /**
     * Checks if is wM r200 present.
     * 
     * @return true, if is wM r200 present
     */
    private boolean isWMR200Present() {
        this.wmr200 = this.wmr200Manager.getWMR200byID(this.wmr200ID);
        if (this.wmr200 != null) {
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
            this.buf = this.wmr200
                    .sendCommandAndReceiveDataFromWMR200(WMR200Descriptor.WMR200_REQUEST_NEXT_DATA);

            while (this.buf != null) {
                int donneeValide = this.buf[0] & 0xff; // Le premier byte
                                                       // indique le
                // nombre de donnee valide
                // dans la trame
                this.logger.debug("valide data : " + donneeValide);
                for (int i = 1; i <= donneeValide; i++) {
                    if (bigBuffer.size() == 0) {
                        if ((this.buf[i] >= (byte) 0xD1)
                                & (this.buf[i] <= (byte) 0xD9)) {
                            bigBuffer.add(this.buf[i]);
                        }
                    } else {
                        bigBuffer.add(this.buf[i]);
                    }

                }
                this.logger.debug("current bigbuffer "
                        + this.wmr200.printByteArray(bigBuffer));
                Thread.currentThread();
                Thread.sleep(2000);
                this.buf = this.wmr200
                        .sendCommandAndReceiveDataFromWMR200(WMR200Descriptor.WMR200_REQUEST_NEXT_DATA);
            }

            while (bigBuffer.size() > 0) {
                if ((bigBuffer.get(0) < (byte) 0xD1)
                        || (bigBuffer.get(0) > (byte) 0xD9)) {
                    // All frames must start with 0xD1 - 0xD9. If the first
                    // byte is not within this range, we don't have a proper
                    // frame start. Discard all octets and restart with the next
                    // packet.
                    this.logger.debug("Bad frame: "
                            + this.wmr200.printByteArray(bigBuffer));
                    this.badFrames += 1;
                    break;
                }

                if ((bigBuffer.get(0) == (byte) 0xD1)
                        && (bigBuffer.size() == 1)) {
                    // 0xD1 frames have only 1 octet.
                    this.logger.debug("D1 frame:"
                            + this.wmr200.printByteArray(bigBuffer));
                    startWorking(bigBuffer.subList(1, bigBuffer.size()));
                    bigBuffer = bigBuffer.subList(1, bigBuffer.size());

                } else if ((bigBuffer.size() < 2)
                        || (bigBuffer.size() < bigBuffer.get(1))) {
                    // 0xD2 - 0xD9 frames use the 2nd octet to specifiy the
                    // length of the frame. The length includes the type and
                    // length octet.
                    this.logger.debug("Short frame:"
                            + this.wmr200.printByteArray(bigBuffer));
                    this.badFrames += 1;
                    break;
                } else {
                    // This is for all frames with length byte and checksum.
                    this.logger.debug("DX frame:"
                            + this.wmr200.printByteArray(bigBuffer));
                    if (bigBuffer.get(0) == (byte) 0xD1) {
                        bigBuffer = bigBuffer.subList(1, bigBuffer.size());
                    }

                    while ((bigBuffer.size() != 0) && (bigBuffer.get(1) != 0)) {
                        this.logger.debug("DX frame:"
                                + this.wmr200.printByteArray(bigBuffer));
                        startWorking(new ArrayList<Byte>(bigBuffer.subList(0,
                                bigBuffer.get(1))));
                        bigBuffer = bigBuffer.subList(bigBuffer.get(1),
                                bigBuffer.size());
                        this.logger.debug("After DX frame:"
                                + this.wmr200.printByteArray(bigBuffer));
                        if (bigBuffer.size() < bigBuffer.get(1)) {
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
            this.logger.debug(e.getMessage(), e);
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
        if ((frame.size() > 0) && WMR200Utils.checkSumState(frame)) {
            st = "Chaine a traiter : \n		";
            for (Byte byte1 : frame) {
                st = st + (Integer.toHexString(byte1 & 0xff) + " ");
            }

            if (st != null) {
                this.logger.info(st);
            }

            switch (frame.get(0)) {

            case ((byte) 0xd1):
                // Message d1
                this.logger.info("Entre dans d1");
                readUnknownD1();
                this.logger.info("Sort de d1");
                break;

            case ((byte) 0xd2):
                // Message d2 : History
                this.logger.info("Entre dans d2");
                readHistoryFromD2(frame);
                this.logger.info("Sort de d2");
                break;

            case ((byte) 0xd3):
                // Message d3 : Anemometer (Wind)
                this.logger.info("Entre dans d3");
                readAnemometerD3(frame);
                this.logger.info("Sort de d3");
                break;

            case ((byte) 0xd4):
                // Message d4 : Rain gauge (Rainfall)
                this.logger.info("Entre dans d4");
                readRainGaugeD4(frame);
                this.logger.info("Sort de d4");
                break;

            case ((byte) 0xd5):
                // Message d5 : UV Meter
                this.logger.info("Entre dans d5");
                readUVMeterD5(frame);
                this.logger.info("Sort de d5");
                break;

            case ((byte) 0xd6):
                // Message d6 : Barometer
                this.logger.info("Entre dans d6");
                readBarometerD6(frame);
                this.logger.info("Sort de d6");
                break;

            case ((byte) 0xd7):
                // Message d6 : Thermometer (Temperature)
                this.logger.info("Entre dans d7");
                readThermometerD7(frame);
                this.logger.info("Sort de d7");
                break;

            case ((byte) 0xd8):
                // Message d8 :
                this.logger.info("Entre dans d8");
                readUnknownD8();
                this.logger.info("Sort de d8");
                break;
            case ((byte) 0xd9):
                // Message d9 : Data Logger Information
                this.logger.info("Entre dans d9");
                readDataLoggerStateD9();
                this.logger.info("Sort de d9");
                break;

            default:
                this.logger.info("UNKNOWN DATA");
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

        Date date = this.translator.getDate(bigBuffer.subList(2, 7));

        int winDirection = bigBuffer.get(7) & 0xff;

        // int unknownData = bigBuffer.get(8);

        float windSpeed = ((float) (((bigBuffer.get(9) & 0xff) / 10.0)));

        float windAverageSpeed = this.translator.getWindAverage(bigBuffer
                .subList(10, 12));

        this.logger.info("wind Direction : "
                + Utils.WIND_DIRECTION_TAB[winDirection] + " | wind Speed   : "
                + windSpeed + " | Wind Avrg : " + windAverageSpeed
                + " | Date : " + date);

        this.old_m_allData = this.m_allData;
        this.old_m_lastMeasurementTimes = this.m_lastMeasurementTimes;

        this.m_lastMeasurementTimes.put(ANEMOMETER.WIND_DIRECTION, date);
        this.m_allData.put(ANEMOMETER.WIND_DIRECTION, new Double(winDirection));

        this.m_lastMeasurementTimes.put(ANEMOMETER.WIND_AVERAGE, date);
        this.m_allData.put(ANEMOMETER.WIND_AVERAGE,
                new Double(windAverageSpeed));

        this.m_lastMeasurementTimes.put(ANEMOMETER.WIND_SPEED, date);
        this.m_allData.put(ANEMOMETER.WIND_SPEED, new Double(windSpeed));
        sendNotification(ANEMOMETER.class.getSimpleName());
        notifyMetricValues(ANEMOMETER.values());
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
        this.eventAdmin.postEvent(new Event(getTopic(), props));

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

        Date date = this.translator.getDate(bigBuffer.subList(2, 7));

        int sensor = this.translator.getSensor(bigBuffer.get(7));

        int power = this.translator.getPower(bigBuffer.get(7));

        int rate = bigBuffer.get(8) & 0xff;

        float rainFallH = this.translator.getRatioRainfall(bigBuffer.subList(9,
                11));

        float rainFall24H = this.translator.getRatioRainfall(bigBuffer.subList(
                11, 13));

        float rainfallTotal = this.translator.getRatioRainfall(bigBuffer
                .subList(13, 15));

        Date dateTotal = this.translator.getDate(bigBuffer.subList(15, 20));

        this.logger.info("sensor : " + sensor + " |power : " + power
                + " | Rainfall Rate   : " + rate + " | Rainfall Heure : "
                + rainFallH + " | Rainfall 24 Heure : " + rainFall24H
                + " | Rainfall Total : " + rainfallTotal + " | Date Total : "
                + dateTotal + " | Date : " + date);

        this.old_m_allData = this.m_allData;
        this.old_m_lastMeasurementTimes = this.m_lastMeasurementTimes;

        this.m_lastMeasurementTimes.put(PLUVIOMETER.RAIN_RATE, date);
        this.m_allData.put(PLUVIOMETER.RAIN_RATE, new Double(rate));

        this.m_lastMeasurementTimes.put(PLUVIOMETER.RAINFALL_H, date);
        this.m_allData.put(PLUVIOMETER.RAINFALL_H, new Double(rainFallH));

        this.m_lastMeasurementTimes.put(PLUVIOMETER.RAINFALL_24H, date);
        this.m_allData.put(PLUVIOMETER.RAINFALL_24H, new Double(rainFall24H));

        this.m_lastMeasurementTimes.put(PLUVIOMETER.RAINFALL_TOTAL, dateTotal);
        this.m_allData.put(PLUVIOMETER.RAINFALL_TOTAL,
                new Double(rainfallTotal));

        sendNotification(PLUVIOMETER.class.getSimpleName());
        notifyMetricValues(PLUVIOMETER.values());
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
        Date date = this.translator.getDate(bigBuffer.subList(2, 7));
        int uv = bigBuffer.get(7) & 0xff;

        this.logger.info("UV : " + uv + " | Date : " + date);

        this.old_m_allData = this.m_allData;
        this.old_m_lastMeasurementTimes = this.m_lastMeasurementTimes;

        this.m_lastMeasurementTimes.put(UVMETER.UV, date);
        this.m_allData.put(UVMETER.UV, new Double(uv));

        sendNotification(UVMETER.class.getSimpleName());
        notifyMetricValues(UVMETER.values());
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

        Date date = this.translator.getDate(bigBuffer.subList(2, 7));

        int pression = this.translator.getPression(bigBuffer.subList(7, 9));

        int prevision = this.translator.getPrevison(bigBuffer.get(8));

        int altitudePression = this.translator.getAltitudePression(bigBuffer
                .subList(9, 11));

        int altitutePrevision = this.translator.getAltitudePrevison(bigBuffer
                .get(10));

        this.logger.info("pression : " + pression + " |prevision : "
                + Utils.PREVISION_TEXT[prevision] + " |Altitude pression  : "
                + altitudePression + " | Altitude prevision : "
                + Utils.PREVISION_TEXT[altitutePrevision] + " |Date : " + date);

        this.old_m_allData = this.m_allData;
        this.old_m_lastMeasurementTimes = this.m_lastMeasurementTimes;

        this.m_lastMeasurementTimes.put(BAROMETER.PRESSURE, date);
        this.m_allData.put(BAROMETER.PRESSURE, new Double(pression));

        this.m_lastMeasurementTimes.put(BAROMETER.PREVISION, date);
        this.m_allData.put(BAROMETER.PREVISION, new Double(prevision));

        this.m_lastMeasurementTimes.put(BAROMETER.ALTITUDE_PRESSURE, date);
        this.m_allData.put(BAROMETER.ALTITUDE_PRESSURE, new Double(
                altitudePression));

        this.m_lastMeasurementTimes.put(BAROMETER.ALTITUDE_PREVISION, date);
        this.m_allData.put(BAROMETER.ALTITUDE_PREVISION, new Double(
                altitutePrevision));

        sendNotification(BAROMETER.class.getSimpleName());
        notifyMetricValues(BAROMETER.values());
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

        Date date = this.translator.getDate(bigBuffer.subList(2, 7));

        int sensor = this.translator.getSensor(bigBuffer.get(7));

        int power = this.translator.getPower(bigBuffer.get(7)); // if powwer =1
                                                                // out
        // else if 0 in

        float temperature = this.translator.getTemperature(bigBuffer.subList(8,
                10));

        int humidity = bigBuffer.get(10) & 0xff;

        float dewPoint = this.translator.getDewPoint(bigBuffer.subList(11, 13));

        int unknownValue = bigBuffer.get(13) & 0xff;

        this.logger.info("SensoTemperature : " + temperature + " |humidity : "
                + humidity + "% |Dew Point : " + dewPoint + " | Sensor : "
                + sensor + " | Power :" + power + " (Value On 13): "
                + unknownValue + " |Date : " + date);

        this.old_m_allData = this.m_allData;
        this.old_m_lastMeasurementTimes = this.m_lastMeasurementTimes;

        if ((bigBuffer.get(7) & 0xff) != 1) {// indoor
            this.m_lastMeasurementTimes.put(THERMOMETER_INDOOR.TEMPERATURE,
                    date);
            this.m_allData.put(THERMOMETER_INDOOR.TEMPERATURE, new Double(
                    temperature));
            this.m_lastMeasurementTimes.put(THERMOMETER_INDOOR.HUMIDITY, date);
            this.m_allData.put(THERMOMETER_INDOOR.HUMIDITY,
                    new Double(humidity));
            this.m_lastMeasurementTimes.put(THERMOMETER_INDOOR.DEW_POINT, date);
            this.m_allData.put(THERMOMETER_INDOOR.DEW_POINT, new Double(
                    dewPoint));

            sendNotification(THERMOMETER_INDOOR.class.getSimpleName());
            notifyMetricValues(THERMOMETER_INDOOR.values());

        } else {// outdoor
            this.m_lastMeasurementTimes.put(THERMOMETER_OUTDOOR.TEMPERATURE,
                    date);
            this.m_allData.put(THERMOMETER_OUTDOOR.TEMPERATURE, new Double(
                    temperature));
            this.m_lastMeasurementTimes.put(THERMOMETER_OUTDOOR.HUMIDITY, date);
            this.m_allData.put(THERMOMETER_OUTDOOR.HUMIDITY, new Double(
                    humidity));
            this.m_lastMeasurementTimes
                    .put(THERMOMETER_OUTDOOR.DEW_POINT, date);
            this.m_allData.put(THERMOMETER_OUTDOOR.DEW_POINT, new Double(
                    dewPoint));

            sendNotification(THERMOMETER_OUTDOOR.class.getSimpleName());
            notifyMetricValues(THERMOMETER_OUTDOOR.values());
        }
    }

    /**
     * Notify metric values.
     * 
     * @param values
     *            the values
     */
    private void notifyMetricValues(Object[] values) {
        for (Object object : values) {
            sendNotification(((Enum) object).name());
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
    /**
     * Gets the a value.
     * 
     * @param address
     *            the address
     * @return the a value
     */
    public Double getAValue(Enum address) {
        return (this.m_allData.containsKey(address)) ? this.m_allData
                .get(address) : 0.0d;
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
        String total = "List Of Current Data (" + this.m_allData.size()
                + " data): \n	";

        for (Enum element : this.m_allData.keySet()) {
            total = total + element + " : "
                    + this.m_allData.get(element).toString() + " \n	";
        }

        this.logger.info(total);

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
    public Date getLastMeasurementTime(Enum address) {
        return (this.m_lastMeasurementTimes.containsKey(address)) ? this.m_lastMeasurementTimes
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
                + this.m_lastMeasurementTimes.size() + " time): \n	";

        for (Enum element : this.m_lastMeasurementTimes.keySet()) {
            total = total + element + " : "
                    + this.m_lastMeasurementTimes.get(element).toString()
                    + " \n	";
        }
        this.logger.info(total);
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
    public Map<Enum, Double> getAllData() {
        return this.m_allData;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.ow2.aspirerfid.sensor.wmr200.service.WMR200Service#getAllMesurementTime
     * ()
     */
    public Map<Enum, Date> getAllMeasurementTimes() {
        return this.m_lastMeasurementTimes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.ow2.aspirerfid.sensor.wmr200.service.WMR200Service#getTopic()
     */
    public String getTopic() {
        return this.topic;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.ow2.aspirerfid.sensor.wmr200.service.WMR200Service#setTopic(java.
     * lang.String)
     */
    public void setTopic(String topicName) {
        this.topic = topicName;

    }

}
