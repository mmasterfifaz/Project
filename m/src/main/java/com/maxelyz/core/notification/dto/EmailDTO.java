/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.notification.dto;

/**
 *
 * @author ukritj
 */
public class EmailDTO {
    private String type;
    private String id;
    private Integer userId;
    
    public EmailDTO(String type, String id, Integer userId){
        this.type = type;
        this.id = id;
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    
    
}
