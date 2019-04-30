
package com.maxelyz.webservicevoice;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ExportVoiceResult" type="{http://schemas.datacontract.org/2004/07/WCFMaxarConvertVoice}ArrayOfExportVoiceAspect.ExportResult" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "exportVoiceResult"
})
@XmlRootElement(name = "ExportVoiceResponse", namespace = "http://tempuri.org/")
public class ExportVoiceResponse {

    @XmlElementRef(name = "ExportVoiceResult", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfExportVoiceAspectExportResult> exportVoiceResult;

    /**
     * Gets the value of the exportVoiceResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfExportVoiceAspectExportResult }{@code >}
     *     
     */
    public JAXBElement<ArrayOfExportVoiceAspectExportResult> getExportVoiceResult() {
        return exportVoiceResult;
    }

    /**
     * Sets the value of the exportVoiceResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfExportVoiceAspectExportResult }{@code >}
     *     
     */
    public void setExportVoiceResult(JAXBElement<ArrayOfExportVoiceAspectExportResult> value) {
        this.exportVoiceResult = value;
    }

}
