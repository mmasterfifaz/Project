/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.push.model.value;

/**
 *
 * @author oat
 */
public class Message {
     
    private String text;
    private String user;
    private String refId;
 
    public Message() {
    }
 
    public Message(String text) {
        this.text = text;
    }
     
    public String getText() {
        return text;
    }
 
    public Message setText(String text) {
        this.text = text;
        return this;
    }
 
    public String getUser() {
        return user;
    }
 
    public Message setUser(String user) {
        this.user = user;
        return this;
    }
    
    public String getRefId() {
        return refId;
    }
 
    public Message setRefId(String refId) {
        this.refId = refId;
        return this;
    }
 
}