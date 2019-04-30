/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.service;

import com.maxelyz.core.model.entity.Customer;
import com.maxelyz.core.model.entity.Users;
import org.richfaces.json.JSONObject;

/**
 *
 * @author Administrator
 */
public interface TelephonyService {
    
    // POST METHOD
    public String loginTelephony(String userId, String password, String extension) throws Exception;
    public String logoutTelephony(String userId, String extension) throws Exception;
    public String logoutWithReason(String userId, String extension, String logoutReasonId) throws Exception;
    public String dial(String userId, String extension, String outboundServiceId, String phoneNumber) throws Exception;
    public String hangup(String userId, String extension) throws Exception;
    public String nextcall(String userId, String extension, String nextCallReasonCode) throws Exception;
    public String hold(String userId, String extension) throws Exception;
    public String unhold(String userId, String extension) throws Exception;
    public String ready(String userId, String extension) throws Exception;
    public String notready(String userId, String extension, String notReadyReasonId) throws Exception;
    public String notreadypark(String userId, String extension) throws Exception;
    public String answer(String userId, String extension) throws Exception;
    public String reject(String userId, String extension) throws Exception;    
    public String saveAspectParameter(String userId, String extension, String paramData) throws Exception;
    
    // GET METHOD
    public JSONObject getDisposition(String userId, String extension) throws Exception;
    public JSONObject getNotReadyReason(String userId, String extension) throws Exception;
    public JSONObject getLogOutReason(String userId, String extension) throws Exception;
    public JSONObject getCallInformationData(String userId, String extension) throws Exception;
    public JSONObject getTrackid(String userId, String extension) throws Exception;
    public JSONObject getCurrentstate(String userId, String extension) throws Exception;
    
    //FOR NICE
    // Call when Dial
    public void callNiceUpdateOpenCall(String extension, String refCode, String phoneNo, String custName, String custSurname, String contactID, Users user) throws Exception;
    // Call when Hang up
    public void callNiceUpdateAnyOpenCall(String extension, String refCode, String phoneNo, String custName, String custSurname, String contactID, Users user) throws Exception;
}
