/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.value.admin;

import com.maxelyz.core.model.entity.Users;
import java.util.Date;

/**
 *
 * @author oat
 */
public class SessionMonitorValue {
    private Users user;
    private Date loginTime;
    private String remoteAddr;
    private String sessionId;
    private String sessionKey;
    private String agentStatus="Not Ready";

    public SessionMonitorValue(Users user, Date loginTime, String remoteAddr) {
        this.user = user;
        this.loginTime = loginTime;
        this.remoteAddr = remoteAddr;
    }
    
    public SessionMonitorValue(Users user, Date loginTime, String remoteAddr, String sessionId) {
        this.user = user;
        this.loginTime = loginTime;
        this.remoteAddr = remoteAddr;
        this.sessionId = sessionId;
    }
    
    public SessionMonitorValue(Users user, Date loginTime, String remoteAddr, String sessionId, String agentStatus) {
        this.user = user;
        this.loginTime = loginTime;
        this.remoteAddr = remoteAddr;
        this.sessionId = sessionId;
        this.agentStatus = agentStatus;
    }
    
    public SessionMonitorValue(Users user, Date loginTime, String remoteAddr, String sessionId, String agentStatus, String sessionKey) {
        this.user = user;
        this.loginTime = loginTime;
        this.remoteAddr = remoteAddr;
        this.sessionId = sessionId;
        this.agentStatus = agentStatus;
        this.sessionKey = sessionKey;
    }
        
    public SessionMonitorValue() {
    }

    
    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getAgentStatus() {
        return agentStatus;
    }

    public void setAgentStatus(String agentStatus) {
        this.agentStatus = agentStatus;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    
}
