/*
 * Copyright (C) 2007 ETH Zurich
 *
 * This file is part of Accada (www.accada.org).
 *
 * Accada is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1, as published by the Free Software Foundation.
 *
 * Accada is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Accada; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA  02110-1301  USA
 */

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// 	See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 	Any modifications to this file will be lost upon recompilation of the source schema. 
// 	Generated on: 2006.04.12 um 10:18:48 EDT 
//


package org.ow2.aspirerfid.reader.rp.impl.core.msg.reply;


/**
 * Java content class for anonymous complex type.
 * 	<p>The following schema fragment specifies the expected 	content contained within this java content object. 	(defined at file:/C:/Data/workspace/RFIDReader/RpReply.xsd line 28)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="resultCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;choice>
 *           &lt;element name="error" type="{urn:epcglobal:rp:xsd:1}ErrorType"/>
 *           &lt;element name="readerDevice" type="{urn:epcglobal:rp:xsd:1}ReaderDeviceReply"/>
 *           &lt;element name="source" type="{urn:epcglobal:rp:xsd:1}SourceReply"/>
 *           &lt;element name="readPoint" type="{urn:epcglobal:rp:xsd:1}ReadPointReply"/>
 *           &lt;element name="trigger" type="{urn:epcglobal:rp:xsd:1}TriggerReply"/>
 *           &lt;element name="tagSelector" type="{urn:epcglobal:rp:xsd:1}TagSelectorReply"/>
 *           &lt;element name="notificationChannel" type="{urn:epcglobal:rp:xsd:1}NotificationChannelReply"/>
 *           &lt;element name="dataSelector" type="{urn:epcglobal:rp:xsd:1}DataSelectorReply"/>
 *           &lt;element name="eventType" type="{urn:epcglobal:rp:xsd:1}EventTypeReply"/>
 *           &lt;element name="triggerType" type="{urn:epcglobal:rp:xsd:1}TriggerTypeReply"/>
 *           &lt;element name="fieldName" type="{urn:epcglobal:rp:xsd:1}FieldNameReply"/>
 *           &lt;element name="tagField" type="{urn:epcglobal:rp:xsd:1}TagFieldReply"/>
 *           &lt;any/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface ReplyType {


    /**
     * Gets the value of the any property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.Object}
     */
    java.lang.Object getAny();

    /**
     * Sets the value of the any property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.Object}
     */
    void setAny(java.lang.Object value);

    /**
     * A Read/Notify Trigger
     * 
     * @return
     *     possible object is
     *     {@link org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.TriggerReply}
     */
    org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.TriggerReply getTrigger();

    /**
     * A Read/Notify Trigger
     * 
     * @param value
     *     allowed object is
     *     {@link org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.TriggerReply}
     */
    void setTrigger(org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.TriggerReply value);

    /**
     * Which data is reported by (a
     * notification channel/read command) depends on the settings
     * of the associated DataSelector.
     * 
     * @return
     *     possible object is
     *     {@link org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.DataSelectorReply}
     */
    org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.DataSelectorReply getDataSelector();

    /**
     * Which data is reported by (a
     * notification channel/read command) depends on the settings
     * of the associated DataSelector.
     * 
     * @param value
     *     allowed object is
     *     {@link org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.DataSelectorReply}
     */
    void setDataSelector(org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.DataSelectorReply value);

    /**
     * Name of the fields in a read report
     * 
     * @return
     *     possible object is
     *     {@link org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.FieldNameReply}
     */
    org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.FieldNameReply getFieldName();

    /**
     * Name of the fields in a read report
     * 
     * @param value
     *     allowed object is
     *     {@link org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.FieldNameReply}
     */
    void setFieldName(org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.FieldNameReply value);

    /**
     * A read point
     * 
     * @return
     *     possible object is
     *     {@link org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.ReadPointReply}
     */
    org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.ReadPointReply getReadPoint();

    /**
     * A read point
     * 
     * @param value
     *     allowed object is
     *     {@link org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.ReadPointReply}
     */
    void setReadPoint(org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.ReadPointReply value);

    /**
     * The data fields on a tag that can be read or written
     * 
     * @return
     *     possible object is
     *     {@link org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.TagFieldReply}
     */
    org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.TagFieldReply getTagField();

    /**
     * The data fields on a tag that can be read or written
     * 
     * @param value
     *     allowed object is
     *     {@link org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.TagFieldReply}
     */
    void setTagField(org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.TagFieldReply value);

    /**
     * the error code for the command - 0
     * for success.
     * 
     */
    int getResultCode();

    /**
     * the error code for the command - 0
     * for success.
     * 
     */
    void setResultCode(int value);

    /**
     * A Read Source
     * 
     * @return
     *     possible object is
     *     {@link org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.SourceReply}
     */
    org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.SourceReply getSource();

    /**
     * A Read Source
     * 
     * @param value
     *     allowed object is
     *     {@link org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.SourceReply}
     */
    void setSource(org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.SourceReply value);

    /**
     * A tag selector
     * 
     * @return
     *     possible object is
     *     {@link org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.TagSelectorReply}
     */
    org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.TagSelectorReply getTagSelector();

    /**
     * A tag selector
     * 
     * @param value
     *     allowed object is
     *     {@link org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.TagSelectorReply}
     */
    void setTagSelector(org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.TagSelectorReply value);

    /**
     * An enumeration object - Type of
     * the Read/Notify Trigger e.g. Continuous
     * 
     * @return
     *     possible object is
     *     {@link org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.TriggerTypeReply}
     */
    org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.TriggerTypeReply getTriggerType();

    /**
     * An enumeration object - Type of
     * the Read/Notify Trigger e.g. Continuous
     * 
     * @param value
     *     allowed object is
     *     {@link org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.TriggerTypeReply}
     */
    void setTriggerType(org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.TriggerTypeReply value);

    /**
     * An enumeration object - Type of
     * the Tag Event e.g. evGlimpsed
     * 
     * @return
     *     possible object is
     *     {@link org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.EventTypeReply}
     */
    org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.EventTypeReply getEventType();

    /**
     * An enumeration object - Type of
     * the Tag Event e.g. evGlimpsed
     * 
     * @param value
     *     allowed object is
     *     {@link org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.EventTypeReply}
     */
    void setEventType(org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.EventTypeReply value);

    /**
     * Information about the error.
     * 
     * @return
     *     possible object is
     *     {@link org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.ErrorType}
     */
    org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.ErrorType getError();

    /**
     * Information about the error.
     * 
     * @param value
     *     allowed object is
     *     {@link org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.ErrorType}
     */
    void setError(org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.ErrorType value);

    /**
     * A Reader
     * 
     * @return
     *     possible object is
     *     {@link org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.ReaderDeviceReply}
     */
    org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.ReaderDeviceReply getReaderDevice();

    /**
     * A Reader
     * 
     * @param value
     *     allowed object is
     *     {@link org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.ReaderDeviceReply}
     */
    void setReaderDevice(org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.ReaderDeviceReply value);

    /**
     * The notification channel
     * carries messages issued asynchronously by the Reader to the Host.
     * 
     * @return
     *     possible object is
     *     {@link org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.NotificationChannelReply}
     */
    org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.NotificationChannelReply getNotificationChannel();

    /**
     * The notification channel
     * carries messages issued asynchronously by the Reader to the Host.
     * 
     * @param value
     *     allowed object is
     *     {@link org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.NotificationChannelReply}
     */
    void setNotificationChannel(org.ow2.aspirerfid.reader.rp.impl.core.msg.reply.NotificationChannelReply value);

    /**
     * The id of the command for which
     * this is the reply.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getId();

    /**
     * The id of the command for which
     * this is the reply.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setId(java.lang.String value);

}