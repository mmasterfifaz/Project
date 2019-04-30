
package com.maxelyz.webservicevoice;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ExportVoiceAspect.ExportResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ExportVoiceAspect.ExportResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="convertResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="trackId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExportVoiceAspect.ExportResult", propOrder = {
    "convertResult",
    "trackId"
})
public class ExportVoiceAspectExportResult {

    @XmlElementRef(name = "convertResult", namespace = "http://schemas.datacontract.org/2004/07/WCFMaxarConvertVoice", type = JAXBElement.class, required = false)
    protected JAXBElement<String> convertResult;
    @XmlElementRef(name = "trackId", namespace = "http://schemas.datacontract.org/2004/07/WCFMaxarConvertVoice", type = JAXBElement.class, required = false)
    protected JAXBElement<String> trackId;

    /**
     * Gets the value of the convertResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getConvertResult() {
        return convertResult;
    }

    /**
     * Sets the value of the convertResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setConvertResult(JAXBElement<String> value) {
        this.convertResult = value;
    }

    /**
     * Gets the value of the trackId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTrackId() {
        return trackId;
    }

    /**
     * Sets the value of the trackId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTrackId(JAXBElement<String> value) {
        this.trackId = value;
    }

}
