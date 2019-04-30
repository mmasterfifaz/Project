/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.value.front.search;

import java.util.Date;

/**
 *
 * @author Administrator
 */
public class SearchMessageValue {
    private String source;
    private String sourceSub;
    private String accountID;
    private Integer msgID;
    private String emailFrom;
    private String emailTo;
    private String lastReceiveByName;
    private Date contactDate;
    private String status;
    private String subject;
    private String customerName;
    private Integer parentId;
    private String ticketId;
    
    public SearchMessageValue() {
        
    }
    
    public SearchMessageValue(String source, String accountID, Integer msgID, String emailFrom, String emailTo, Date contactDate, String status, String subject, String customerName, String lastReceiveByName) {
        this.source = source;
        this.accountID = accountID;
        this.msgID = msgID;
        this.emailFrom = emailFrom;
        this.emailTo = emailTo;
        this.contactDate = contactDate;
        this.status = status;
        this.subject = subject;
        this.customerName = customerName;
        this.lastReceiveByName = lastReceiveByName;
    }
    
    public SearchMessageValue(String source, String accountID, Integer msgID, String emailFrom, String emailTo, Date contactDate, String status, String subject, String customerName, String lastReceiveByName, String sourceSub) {
        this.source = source;
        this.accountID = accountID;
        this.msgID = msgID;
        this.emailFrom = emailFrom;
        this.emailTo = emailTo;
        this.contactDate = contactDate;
        this.status = status;
        this.subject = subject;
        this.customerName = customerName;
        this.lastReceiveByName = lastReceiveByName;
        this.sourceSub = sourceSub;
    }
    
    public SearchMessageValue(String source, String accountID, Integer msgID, String emailFrom, String emailTo, Date contactDate, String status, String subject, String customerName, String lastReceiveByName, String sourceSub, Integer parentId, String ticketId) {
        this.source = source;
        this.accountID = accountID;
        this.msgID = msgID;
        this.emailFrom = emailFrom;
        this.emailTo = emailTo;
        this.contactDate = contactDate;
        this.status = status;
        this.subject = subject;
        this.customerName = customerName;
        this.lastReceiveByName = lastReceiveByName;
        this.sourceSub = sourceSub;
        this.parentId = parentId;
        this.ticketId = ticketId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public Integer getMsgID() {
        return msgID;
    }

    public void setMsgID(Integer msgID) {
        this.msgID = msgID;
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    public Date getContactDate() {
        return contactDate;
    }

    public void setContactDate(Date contactDate) {
        this.contactDate = contactDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getLastReceiveByName() {
        return lastReceiveByName;
    }

    public void setLastReceiveByName(String lastReceiveByName) {
        this.lastReceiveByName = lastReceiveByName;
    }

    public String getSourceSub() {
        return sourceSub;
    }

    public void setSourceSub(String sourceSub) {
        this.sourceSub = sourceSub;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    
}
