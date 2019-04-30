/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.notification.dto;

import com.maxelyz.core.notification.Constant;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author ukritj
 */
public class FollowupDTO {

    private String type;
    private Integer id;
    private Integer userId;
    private String userName;
    private String customerName;
    private String followupDate;
    private String detail;
    private String refNo;
    
    public FollowupDTO(String type, Integer id, Integer userId, String customerName, Date followupDate, String detail){
        this.type = type;
        this.id = id;
        this.userId = userId;
        this.customerName = customerName;
        if(followupDate != null){
            this.followupDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(followupDate);
        }
        this.detail = detail;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getFollowupDate() {
        return followupDate;
    }

    public void setFollowupDate(String followupDate) {
        this.followupDate = followupDate;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
}
