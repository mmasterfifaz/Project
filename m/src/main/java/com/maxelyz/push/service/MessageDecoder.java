/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.push.service;

import com.maxelyz.push.model.value.Message;
import org.primefaces.push.Decoder;

/**
 *
 * @author oat
 */

public class MessageDecoder implements Decoder<String,Message> {
 
    @Override
    public Message decode(String s) {
        String[] messages = s.split(PushServiceImpl.SEPERATOR);
        switch (messages.length) {
            case 1:
                return new Message(messages[0]);
            case 2:
                return new Message().setUser(messages[0]).setText(messages[1]);
            default:
                return new Message().setUser(messages[0]).setRefId(messages[1]).setText(messages[2]);
        }
    }
}