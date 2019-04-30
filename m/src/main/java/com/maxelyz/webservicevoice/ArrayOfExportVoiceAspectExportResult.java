
package com.maxelyz.webservicevoice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfExportVoiceAspect.ExportResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfExportVoiceAspect.ExportResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ExportVoiceAspect.ExportResult" type="{http://schemas.datacontract.org/2004/07/WCFMaxarConvertVoice}ExportVoiceAspect.ExportResult" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfExportVoiceAspect.ExportResult", propOrder = {
    "exportVoiceAspectExportResult"
})
public class ArrayOfExportVoiceAspectExportResult {

    @XmlElement(name = "ExportVoiceAspect.ExportResult", nillable = true)
    protected List<ExportVoiceAspectExportResult> exportVoiceAspectExportResult;

    /**
     * Gets the value of the exportVoiceAspectExportResult property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the exportVoiceAspectExportResult property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExportVoiceAspectExportResult().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ExportVoiceAspectExportResult }
     * 
     * 
     */
    public List<ExportVoiceAspectExportResult> getExportVoiceAspectExportResult() {
        if (exportVoiceAspectExportResult == null) {
            exportVoiceAspectExportResult = new ArrayList<ExportVoiceAspectExportResult>();
        }
        return this.exportVoiceAspectExportResult;
    }

}
