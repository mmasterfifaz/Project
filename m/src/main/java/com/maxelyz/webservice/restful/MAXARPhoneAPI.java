/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.webservice.restful;

import com.maxelyz.push.service.PushServiceImpl;
import com.maxelyz.webservice.model.JSONResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedProperty;
import org.apache.log4j.Logger;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/v1/cti/")
public class MAXARPhoneAPI {

    //LOG
    private static Logger log = Logger.getLogger(MAXARPhoneAPI.class);

    @RequestMapping(value = "/stagechanged/{user}/{state}", method = RequestMethod.POST , produces = "application/json")
    public @ResponseBody ResponseEntity<JSONResponse> stateChanged(@PathVariable String user, @PathVariable String state) {
        try {
            EventBus eventBus = EventBusFactory.getDefault().eventBus();
            eventBus.publish("/cti/"+user, user+PushServiceImpl.SEPERATOR+"stagechanged|"+state);
        } catch(Exception e) {
            JSONResponse response = new JSONResponse(e.getMessage(),false);
            return new ResponseEntity<JSONResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        JSONResponse response = new JSONResponse("stagechanged|"+user+"|"+state,true);
        return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
    }
   
    
     @RequestMapping(value = "/screenpop/{user}/{param}", method = RequestMethod.POST , produces = "application/json")
    public @ResponseBody ResponseEntity<JSONResponse> ringing(@PathVariable String user, @PathVariable String param) {
        try {
            EventBus eventBus = EventBusFactory.getDefault().eventBus();
            eventBus.publish("/cti/"+user, user+PushServiceImpl.SEPERATOR+"screenpop|"+param);
        } catch(Exception e) {
            JSONResponse response = new JSONResponse(e.getMessage(),false);
            return new ResponseEntity<JSONResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        JSONResponse response = new JSONResponse("screenpop|"+user+"|"+param,true);
        return new ResponseEntity<JSONResponse>(response, HttpStatus.OK);
    }
}