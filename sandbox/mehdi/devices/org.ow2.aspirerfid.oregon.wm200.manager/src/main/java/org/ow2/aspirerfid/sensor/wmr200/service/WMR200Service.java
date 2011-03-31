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
package org.ow2.aspirerfid.sensor.wmr200.service;

import java.util.Date;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Interface WMR200Service.
 * 
 * @author Elmehdi Damou
 */
public interface WMR200Service {

    /** The Constant SENSOR. */
    public static final String SENSOR = "sensor";

    /**
     * Start.
     */
    public void start();

    /**
     * Stop.
     */
    public void stop();

    /**
     * Gets the a data.
     * 
     * @param metricName
     *            the address
     * @return the a data
     */
    public Double getAValue(Enum metricName);

    /**
     * Gets the last measurement time.
     * 
     * @param metricName
     *            the metric name
     * @return the last measurement time
     */
    public Date getLastMeasurementTime(Enum metricName);

    /**
     * return the map containing all data.
     * 
     * @return the all data
     */
    public Map<Enum, Double> getAllData();

    /**
     * Gets the all mesurement time.
     * 
     * @return the all mesurement time
     */
    public Map<Enum, Date> getAllMeasurementTimes();

    /**
     * Resets the wmr200.
     */
    public void reset();

    /**
     * Gets the topic.
     * 
     * @return the topic
     */
    public String getTopic();

    /**
     * Sets the topic.
     * 
     * @param topicName
     *            the new topic
     */
    public void setTopic(String topicName);

}
