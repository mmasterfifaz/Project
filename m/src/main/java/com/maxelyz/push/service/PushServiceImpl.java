/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.push.service;

import com.maxelyz.push.model.value.Message;
import org.apache.log4j.Logger;
import org.primefaces.push.EventBus;
import org.primefaces.push.RemoteEndpoint;
import org.primefaces.push.annotation.OnClose;
import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.OnOpen;
import org.primefaces.push.annotation.PathParam;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.annotation.Singleton;

/**
 *
 * @author oat
 */
@PushEndpoint("/cti/{user}")
@Singleton
public class PushServiceImpl implements PushService {
    private static Logger log = Logger.getLogger(PushServiceImpl.class);
    public static final String SEPERATOR = "#-#";
    
    @PathParam("user")
    private String username;
    
    @OnOpen
    @Override
    public void onOpen(RemoteEndpoint r, EventBus eventBus) {
        log.info("OnOpen {} "+r);
    }
    
    @OnClose
    @Override
    public void onClose(RemoteEndpoint r, EventBus eventBus) {
        log.info("OnClose {} "+r);      
    }
    
    @OnMessage(decoders = {MessageDecoder.class}, encoders = {MessageEncoder.class})
    @Override
    public Message onMessage(Message message) {
        return message;
    }
}