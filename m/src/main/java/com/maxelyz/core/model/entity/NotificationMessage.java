/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author songwisit
 */
@Entity
@Table(name = "notification_message")
public class NotificationMessage implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "uuid")
    private String uuid; 
    @Column(name = "token")
    private String token;
    @Column(name = "title")
    private String title;
    @Column(name = "display_value")
    private String displayValue;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "function_name")
    private String functionName;
    @Column(name = "ref_id")
    private Integer refId;
    @Column(name = "request_channel")
    private String requestChannel;
    @Column(name = "is_manual_accept_call")
    private Boolean isManualAcceptCall;
    @Column(name = "is_phone_state_active")
    private Boolean isPhoneStateActive;
    @Column(name = "is_cti_event")
    private Boolean isCTIEvent;
    @Column(name = "status")
    private Integer status;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "login_name")
    private String loginName;
    @Column(name = "type")
    private String type;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "notify_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date notifyDate;
    @Column(name = "priority")
    private Integer priority;
    @Column(name = "service_id")
    private Integer serviceId;
    @Column(name = "service_name")
    private String serviceName;
    @Column(name = "call_category")
    private String callCategory;
    @Column(name = "call_type")
    private String callType;
    @Column(name = "dial_mode")
    private String dialMode;
    
    @Column(name = "param1")
    private String param1;
    @Column(name = "param2")
    private String param2;
    @Column(name = "param3")
    private String param3;
    @Column(name = "param4")
    private String param4;
    @Column(name = "param5")
    private String param5;
    @Column(name = "param6")
    private String param6;
    @Column(name = "param7")
    private String param7;
    @Column(name = "param8")
    private String param8;
    @Column(name = "param9")
    private String param9;
    @Column(name = "param10")
    private String param10;
    @Column(name = "param11")
    private String param11;
    @Column(name = "param12")
    private String param12;
    @Column(name = "param13")
    private String param13;
    @Column(name = "param14")
    private String param14;
    @Column(name = "param15")
    private String param15;
    @Column(name = "param16")
    private String param16;
    @Column(name = "param17")
    private String param17;
    @Column(name = "param18")
    private String param18;
    @Column(name = "param19")
    private String param19;
    @Column(name = "param20")
    private String param20;
    
    public NotificationMessage() {
        
    }

    public NotificationMessage(Integer id, String uuid, String token, String title, String displayValue, String phoneNumber, String functionName, Integer refId, String requestChannel, Boolean isManualAcceptCall, Boolean isPhoneStateActive, Boolean isCTIEvent, Integer status, Integer userId, String loginName, String type, Date createDate, Date notifyDate, Integer priority, Integer serviceId, String serviceName, String callCategory, String callType, String dialMode, String param1, String param2, String param3, String param4, String param5, String param6, String param7, String param8, String param9, String param10, String param11, String param12, String param13, String param14, String param15, String param16, String param17, String param18, String param19, String param20) {
        this.id = id;
        this.uuid = uuid;
        this.token = token;
        this.title = title;
        this.displayValue = displayValue;
        this.phoneNumber = phoneNumber;
        this.functionName = functionName;
        this.refId = refId;
        this.requestChannel = requestChannel;
        this.isManualAcceptCall = isManualAcceptCall;
        this.isPhoneStateActive = isPhoneStateActive;
        this.isCTIEvent = isCTIEvent;
        this.status = status;
        this.userId = userId;
        this.loginName = loginName;
        this.type = type;
        this.createDate = createDate;
        this.notifyDate = notifyDate;
        this.priority = priority;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.callCategory = callCategory;
        this.callType = callType;
        this.dialMode = dialMode;
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
        this.param4 = param4;
        this.param5 = param5;
        this.param6 = param6;
        this.param7 = param7;
        this.param8 = param8;
        this.param9 = param9;
        this.param10 = param10;
        this.param11 = param11;
        this.param12 = param12;
        this.param13 = param13;
        this.param14 = param14;
        this.param15 = param15;
        this.param16 = param16;
        this.param17 = param17;
        this.param18 = param18;
        this.param19 = param19;
        this.param20 = param20;
    }  
        
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public Integer getRefId() {
        return refId;
    }

    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    public String getRequestChannel() {
        return requestChannel;
    }

    public void setRequestChannel(String requestChannel) {
        this.requestChannel = requestChannel;
    }

    public Boolean getIsManualAcceptCall() {
        return isManualAcceptCall;
    }

    public void setIsManualAcceptCall(Boolean isManualAcceptCall) {
        this.isManualAcceptCall = isManualAcceptCall;
    }

    public Boolean getIsPhoneStateActive() {
        return isPhoneStateActive;
    }

    public void setIsPhoneStateActive(Boolean isPhoneStateActive) {
        this.isPhoneStateActive = isPhoneStateActive;
    }

    public Boolean getIsCTIEvent() {
        return isCTIEvent;
    }

    public void setIsCTIEvent(Boolean isCTIEvent) {
        this.isCTIEvent = isCTIEvent;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getNotifyDate() {
        return notifyDate;
    }

    public void setNotifyDate(Date notifyDate) {
        this.notifyDate = notifyDate;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getCallCategory() {
        return callCategory;
    }

    public void setCallCategory(String callCategory) {
        this.callCategory = callCategory;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getDialMode() {
        return dialMode;
    }

    public void setDialMode(String dialMode) {
        this.dialMode = dialMode;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public String getParam3() {
        return param3;
    }

    public void setParam3(String param3) {
        this.param3 = param3;
    }

    public String getParam4() {
        return param4;
    }

    public void setParam4(String param4) {
        this.param4 = param4;
    }

    public String getParam5() {
        return param5;
    }

    public void setParam5(String param5) {
        this.param5 = param5;
    }

    public String getParam6() {
        return param6;
    }

    public void setParam6(String param6) {
        this.param6 = param6;
    }

    public String getParam7() {
        return param7;
    }

    public void setParam7(String param7) {
        this.param7 = param7;
    }

    public String getParam8() {
        return param8;
    }

    public void setParam8(String param8) {
        this.param8 = param8;
    }

    public String getParam9() {
        return param9;
    }

    public void setParam9(String param9) {
        this.param9 = param9;
    }

    public String getParam10() {
        return param10;
    }

    public void setParam10(String param10) {
        this.param10 = param10;
    }

    public String getParam11() {
        return param11;
    }

    public void setParam11(String param11) {
        this.param11 = param11;
    }

    public String getParam12() {
        return param12;
    }

    public void setParam12(String param12) {
        this.param12 = param12;
    }

    public String getParam13() {
        return param13;
    }

    public void setParam13(String param13) {
        this.param13 = param13;
    }

    public String getParam14() {
        return param14;
    }

    public void setParam14(String param14) {
        this.param14 = param14;
    }

    public String getParam15() {
        return param15;
    }

    public void setParam15(String param15) {
        this.param15 = param15;
    }

    public String getParam16() {
        return param16;
    }

    public void setParam16(String param16) {
        this.param16 = param16;
    }

    public String getParam17() {
        return param17;
    }

    public void setParam17(String param17) {
        this.param17 = param17;
    }

    public String getParam18() {
        return param18;
    }

    public void setParam18(String param18) {
        this.param18 = param18;
    }

    public String getParam19() {
        return param19;
    }

    public void setParam19(String param19) {
        this.param19 = param19;
    }

    public String getParam20() {
        return param20;
    }

    public void setParam20(String param20) {
        this.param20 = param20;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotificationMessage)) {
            return false;
        }
        NotificationMessage other = (NotificationMessage) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.NotificationMessage[ id=" + id + " ]";
    }
    
}
