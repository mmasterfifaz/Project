/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.webservice.model;

/**
 *
 * @author songwisit
 */
public class JSONRequest {
    private String token = "";
    private String title = "";
    //private String displayValue;
    //private String phoneNumber;
    //private String functionName;
    //private Integer refId;
    //private String requestChannel;
    
    public JSONRequest()
    {
        token = "";
        title = "";
    }
   
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    /*
    public String getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }
    
    public Integer getRefId() {
        return refId;
    }

    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    public String getRequestChannel() {
        return requestChannel;
    }

    public void setRequestChannel(String requestChannel) {
        this.requestChannel = requestChannel;
    }
    */
}