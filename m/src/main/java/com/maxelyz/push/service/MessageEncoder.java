/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.push.service;

import org.primefaces.json.JSONObject;
import com.maxelyz.push.model.value.Message;
import org.primefaces.push.Encoder;

/**
 *
 * @author oat
 */
public final class MessageEncoder implements Encoder<Message, String> {
 
    @Override
    public String encode(Message message) {
        return new JSONObject(message).toString();
    }
}