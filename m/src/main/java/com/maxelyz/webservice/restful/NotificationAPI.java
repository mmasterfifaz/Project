/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.webservice.restful;

import com.maxelyz.push.service.NotificationService;
import com.maxelyz.webservice.model.*;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.core.model.dao.NotificationMessageDAO;
import com.maxelyz.core.model.dao.UsersDAO;
import com.maxelyz.core.model.dao.AssignmentDetailDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import java.text.SimpleDateFormat;

@Controller
@RequestMapping("/v1/notifications")
public class NotificationAPI {

    //LOG
    private static Logger log = Logger.getLogger(NotificationAPI.class);
 
    @Autowired
    private NotificationMessageDAO notificationMessageDAO;
    @Autowired
    private UsersDAO usersDAO;
    @Autowired
    private AssignmentDetailDAO assignmentDetailDAO;

    @RequestMapping(value = "/notify/{user}", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<JSONResponse> notify(@PathVariable String user, @RequestHeader Map<String, String> headers, @RequestBody String jsonMessage, HttpServletRequest request) {
        List<Users> users;
        AssignmentDetail assignmentDetail;
        
        JsonObject jsonObject;
        String responseMessage = "";
        boolean responseSuccess = false;
        NotificationMessage notificationMessage = new NotificationMessage();       
        
        try {
            jsonObject = new JsonParser().parse(jsonMessage).getAsJsonObject();

            if (jsonObject.get("uuid") != null) notificationMessage.setUuid(jsonObject.get("uuid").getAsString());
            if (jsonObject.get("token") != null) notificationMessage.setToken(jsonObject.get("token").getAsString());
            if (jsonObject.get("title") != null) notificationMessage.setTitle(jsonObject.get("title").getAsString());
            if (jsonObject.get("displayvalue") != null) notificationMessage.setDisplayValue((jsonObject.get("displayvalue").isJsonArray()) ? jsonObject.get("displayvalue").getAsJsonArray().toString() : jsonObject.get("displayvalue").getAsString());
            if (jsonObject.get("phonenumber") != null) notificationMessage.setPhoneNumber(jsonObject.get("phonenumber").getAsString());
            if (jsonObject.get("functionname") != null) notificationMessage.setFunctionName(jsonObject.get("functionname").getAsString());
            if (jsonObject.get("refid") != null) notificationMessage.setRefId(Integer.parseInt(jsonObject.get("refid").getAsString()));
            if (jsonObject.get("channel") != null) notificationMessage.setRequestChannel(jsonObject.get("channel").getAsString());
            if (jsonObject.get("ismanualacceptcall") != null) notificationMessage.setIsManualAcceptCall(Boolean.parseBoolean(jsonObject.get("ismanualacceptcall").getAsString()));
            if (jsonObject.get("isphonestateactive") != null) notificationMessage.setIsPhoneStateActive(Boolean.parseBoolean(jsonObject.get("isphonestateactive").getAsString()));
            if (jsonObject.get("isctievent") != null) notificationMessage.setIsCTIEvent(Boolean.parseBoolean(jsonObject.get("isctievent").getAsString()));
            if (jsonObject.get("userid") != null) notificationMessage.setUserId(Integer.parseInt(jsonObject.get("userid").getAsString()));
            if (jsonObject.get("loginname") != null) {
                notificationMessage.setLoginName(jsonObject.get("loginname").getAsString());
            }else{
                notificationMessage.setLoginName("");
            }
            if (jsonObject.get("type") != null)  notificationMessage.setType(jsonObject.get("type").getAsString());
            if (jsonObject.get("priority") != null) notificationMessage.setPriority(Integer.parseInt(jsonObject.get("priority").getAsString()));
            if (jsonObject.get("serviceid") != null) notificationMessage.setServiceId(Integer.parseInt(jsonObject.get("serviceid").getAsString()));
            if (jsonObject.get("servicename") != null) notificationMessage.setServiceName(jsonObject.get("servicename").getAsString());
            if (jsonObject.get("callcategory") != null) notificationMessage.setCallCategory(jsonObject.get("callcategory").getAsString());
            if (jsonObject.get("calltype") != null) notificationMessage.setCallType(jsonObject.get("calltype").getAsString());
            if (jsonObject.get("dialmode") != null)  notificationMessage.setDialMode(jsonObject.get("dialmode").getAsString());
            if (jsonObject.get("param1") != null) notificationMessage.setParam1(jsonObject.get("param1").getAsString());
            if (jsonObject.get("param2") != null) notificationMessage.setParam2(jsonObject.get("param2").getAsString());
            if (jsonObject.get("param3") != null) notificationMessage.setParam3(jsonObject.get("param3").getAsString());
            if (jsonObject.get("param4") != null) notificationMessage.setParam4(jsonObject.get("param4").getAsString());
            if (jsonObject.get("param5") != null) notificationMessage.setParam5(jsonObject.get("param5").getAsString());
            if (jsonObject.get("param6") != null) notificationMessage.setParam6(jsonObject.get("param6").getAsString());
            if (jsonObject.get("param7") != null) notificationMessage.setParam7(jsonObject.get("param7").getAsString());
            if (jsonObject.get("param8") != null) notificationMessage.setParam8(jsonObject.get("param8").getAsString());
            if (jsonObject.get("param9") != null) notificationMessage.setParam9(jsonObject.get("param9").getAsString());
            if (jsonObject.get("param10") != null) notificationMessage.setParam10(jsonObject.get("param10").getAsString());
            if (jsonObject.get("param11") != null) notificationMessage.setParam11(jsonObject.get("param11").getAsString());
            if (jsonObject.get("param12") != null) notificationMessage.setParam12(jsonObject.get("param12").getAsString());
            if (jsonObject.get("param13") != null) notificationMessage.setParam13(jsonObject.get("param13").getAsString());
            if (jsonObject.get("param14") != null) notificationMessage.setParam14(jsonObject.get("param14").getAsString());
            if (jsonObject.get("param15") != null) notificationMessage.setParam15(jsonObject.get("param15").getAsString());
            if (jsonObject.get("param16") != null) notificationMessage.setParam16(jsonObject.get("param16").getAsString());
            if (jsonObject.get("param17") != null) notificationMessage.setParam17(jsonObject.get("param17").getAsString());
            if (jsonObject.get("param18") != null) notificationMessage.setParam18(jsonObject.get("param18").getAsString());
            if (jsonObject.get("param19") != null) notificationMessage.setParam19(jsonObject.get("param19").getAsString());
            if (jsonObject.get("param20") != null) notificationMessage.setParam20(jsonObject.get("param20").getAsString());

            notificationMessage.setStatus(1);
            notificationMessage.setCreateDate(new Date());
            notificationMessage.setNotifyDate(new Date());
            
            users = usersDAO.findUsersByTelephonyLoginName(notificationMessage.getLoginName());
            assignmentDetail = assignmentDetailDAO.findAssignmentDetail(notificationMessage.getRefId());
            if (users == null) {
                responseMessage = "User <"+ notificationMessage.getLoginName() +"> does not exists";
            }else if (users.isEmpty()) {
                responseMessage = "User <"+ notificationMessage.getLoginName() +"> does not exists";
            }else if (assignmentDetail == null) {
                responseMessage = "Assignment Detail Id <"+ notificationMessage.getRefId() +"> does not exists";
            }else{
                assignmentDetail.setUsers(users.get(0));
                notificationMessage.setUserId(users.get(0).getId());
                responseSuccess = true;
                responseMessage = "Notify "+ notificationMessage.getLoginName() + " Success";
            }
            
            if (assignmentDetail != null) {
                assignmentDetail.setStatus("viewed");
                assignmentDetailDAO.edit(assignmentDetail);
            }
            
            notificationMessageDAO.create(notificationMessage);
        } catch(Exception e) {
            //System.out.println("Exception Messsage : " + e.getMessage());
            responseMessage = e.getMessage();
            return new ResponseEntity<JSONResponse>(new JSONResponse(responseMessage, false), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            notificationMessageDAO.createRequestLog(request.getRequestURI(), headers.toString(), jsonMessage, "{\"message\":\""+responseMessage+"\",\"success\":"+responseSuccess+"}");
        }
        
        if (responseSuccess) {
            //EventBus eventBus = EventBusFactory.getDefault().eventBus();
            
            //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            //System.out.println("Start send message on Event Bus : " + "/notifications/" + users.get(0).getId() + " : " + dateFormat.format(new Date()));
            //eventBus.publish("/notifications/"+users.get(0).getId(), user+NotificationService.SEPERATOR+jsonObject.get("refid").getAsString() + NotificationService.SEPERATOR + jsonMessage);
            //System.out.println("End send message on Event Bus : " + "/notifications/" + users.get(0).getId());

            return new ResponseEntity<JSONResponse>(new JSONResponse(responseMessage, responseSuccess), HttpStatus.OK);
        }else{
            return new ResponseEntity<JSONResponse>(new JSONResponse(responseMessage, responseSuccess), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}