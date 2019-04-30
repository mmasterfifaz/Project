/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.notification.dto;

import com.maxelyz.core.notification.Constant;

/**
 *
 * @author ukritj
 */
public class NotificationDTO {

    private final String type = Constant.NOTIFICATION;
    private String id;
    private Integer userId;
    private String userName;
    private Integer count;

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    
}
