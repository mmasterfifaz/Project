
package com.maxelyz.webservicevoice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b14002
 * Generated source version: 2.2
 * 
 */
@WebService(name = "IExportVoiceAspect", targetNamespace = "http://tempuri.org/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface IExportVoiceAspect {


    /**
     * 
     * @param trackIds
     * @return
     *     returns com.maxelyz.webservicevoice.ArrayOfExportVoiceAspectExportResult
     */
    @WebMethod(operationName = "ExportVoice", action = "http://tempuri.org/IExportVoiceAspect/ExportVoice")
    @WebResult(name = "ExportVoiceResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "ExportVoice", targetNamespace = "http://tempuri.org/", className = "com.maxelyz.webservicevoice.ExportVoice")
    @ResponseWrapper(localName = "ExportVoiceResponse", targetNamespace = "http://tempuri.org/", className = "com.maxelyz.webservicevoice.ExportVoiceResponse")
    public ArrayOfExportVoiceAspectExportResult exportVoice(
        @WebParam(name = "trackIds", targetNamespace = "http://tempuri.org/")
        ArrayOfstring trackIds);

}