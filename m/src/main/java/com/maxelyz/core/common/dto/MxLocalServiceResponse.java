/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.common.dto;

import java.io.Serializable;

/**
 *
 * @author DevTeam
 */
public class MxLocalServiceResponse implements Serializable{

    public enum MX_LOCAL_SERVICE_STATUS{NA,OK,ERROR};
    /**************************** PROPERTY *****************************/
    private Object result = null;
    private MX_LOCAL_SERVICE_STATUS serviceStatus = null;
    private String serviceErrorMessage = null;

    /**************************** METHOD *****************************/
    public MxLocalServiceResponse() {
        this.serviceStatus = MX_LOCAL_SERVICE_STATUS.NA;
    }
    
    public MxLocalServiceResponse(MX_LOCAL_SERVICE_STATUS serviceStatus,String serviceErrorMessage, Object result ){
        this.serviceStatus=serviceStatus;
        this.serviceErrorMessage=serviceErrorMessage;
        this.result=result;
    }
    
    public void setOK(Object result){
        this.serviceStatus = MX_LOCAL_SERVICE_STATUS.OK;
        this.serviceErrorMessage = null;
        this.result=result;
    }
    public void setError(String serviceErrorMessage){
        this.serviceStatus = MX_LOCAL_SERVICE_STATUS.ERROR;
        this.serviceErrorMessage = serviceErrorMessage;
        this.result = null;
    }
    
    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
    
    
    /**
     * @return the serviceStatus
     */
    public MX_LOCAL_SERVICE_STATUS getServiceStatus() {
        return serviceStatus;
    }

    /**
     * @param serviceStatus the serviceStatus to set
     */
    public void setServiceStatus(MX_LOCAL_SERVICE_STATUS serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    /**
     * @return the serviceErrorMessage
     */
    public String getServiceErrorMessage() {
        return serviceErrorMessage;
    }

    /**
     * @param serviceErrorMessage the serviceErrorMessage to set
     */
    public void setServiceErrorMessage(String serviceErrorMessage) {
        this.serviceErrorMessage = serviceErrorMessage;
    }

    
    @Override
    public String toString() {
        return this.getClass().getName()+" : [ serviceStatus="+serviceStatus+", serviceErrorMessage="+serviceErrorMessage+", result="+result+"] ";
    }
    
    
    
    
}
