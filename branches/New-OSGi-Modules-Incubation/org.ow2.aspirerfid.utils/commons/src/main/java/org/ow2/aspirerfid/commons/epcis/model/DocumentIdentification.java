
package org.ow2.aspirerfid.commons.epcis.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for DocumentIdentification complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DocumentIdentification">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Standard" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TypeVersion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="InstanceIdentifier" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MultipleType" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="CreationDateAndTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentIdentification", namespace = "http://www.unece.org/cefact/namespaces/StandardBusinessDocumentHeader", propOrder = {
    "standard",
    "typeVersion",
    "instanceIdentifier",
    "type",
    "multipleType",
    "creationDateAndTime"
})
public class DocumentIdentification {

    @XmlElement(name = "Standard", namespace = "http://www.unece.org/cefact/namespaces/StandardBusinessDocumentHeader", required = true)
    protected String standard;
    @XmlElement(name = "TypeVersion", namespace = "http://www.unece.org/cefact/namespaces/StandardBusinessDocumentHeader", required = true)
    protected String typeVersion;
    @XmlElement(name = "InstanceIdentifier", namespace = "http://www.unece.org/cefact/namespaces/StandardBusinessDocumentHeader", required = true)
    protected String instanceIdentifier;
    @XmlElement(name = "Type", namespace = "http://www.unece.org/cefact/namespaces/StandardBusinessDocumentHeader", required = true)
    protected String type;
    @XmlElement(name = "MultipleType", namespace = "http://www.unece.org/cefact/namespaces/StandardBusinessDocumentHeader")
    protected Boolean multipleType;
    @XmlElement(name = "CreationDateAndTime", namespace = "http://www.unece.org/cefact/namespaces/StandardBusinessDocumentHeader", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar creationDateAndTime;

    /**
     * Gets the value of the standard property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStandard() {
        return standard;
    }

    /**
     * Sets the value of the standard property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStandard(String value) {
        this.standard = value;
    }

    /**
     * Gets the value of the typeVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypeVersion() {
        return typeVersion;
    }

    /**
     * Sets the value of the typeVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypeVersion(String value) {
        this.typeVersion = value;
    }

    /**
     * Gets the value of the instanceIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstanceIdentifier() {
        return instanceIdentifier;
    }

    /**
     * Sets the value of the instanceIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstanceIdentifier(String value) {
        this.instanceIdentifier = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the multipleType property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isMultipleType() {
        return multipleType;
    }

    /**
     * Sets the value of the multipleType property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMultipleType(Boolean value) {
        this.multipleType = value;
    }

    /**
     * Gets the value of the creationDateAndTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreationDateAndTime() {
        return creationDateAndTime;
    }

    /**
     * Sets the value of the creationDateAndTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreationDateAndTime(XMLGregorianCalendar value) {
        this.creationDateAndTime = value;
    }

}
