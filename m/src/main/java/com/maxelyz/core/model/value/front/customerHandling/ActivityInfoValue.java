/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.value.front.customerHandling;

import java.util.Date;

/**
 *
 * @author Manop
 */
public class ActivityInfoValue {
    private Integer id;
    private Date activityDate;
    private String createdBy;
    private Date createdDate;
    private String description;
    private Date dueDate;
    private String receivedStatus;
    private String status;
    private String updatedBy;
    private Date updatedDate;
    private Integer userReceiverId;
    private String userReceiverName;
    private Integer userSenderId;
    private String userSenderName;
    private Integer activityTypeId;
    private String activityType;
    private Integer channelId;
    private String channel;
    private String channelType;
    private Integer contactCaseId;
    private String contactCaseCode;
    private String remark;
    private String userAssignTo;
    private Boolean attachFile;
    
    public ActivityInfoValue(){}

    /**
     *
     * @param id
     * @param activityDate
     * @param activityType
     * @param channel
     * @param channelType
     * @param dueDate
     * @param createdBy
     * @param assignedTo
     *
     */
    public ActivityInfoValue(Integer id, Date activityDate, String createdBy, Date createdDate, String description, Date dueDate,
            String receivedStatus, String status, String updatedBy, Date updatedDate, Integer userReceiverId, String userReceiverName,
            Integer userSenderId, String userSenderName, Integer activityTypeId, String activityType, Integer channelId, String channel,
            String channelType, Integer contactCaseId, String contactCaseCode
            ){
        this.id = id;
        this.activityDate = activityDate;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.description = description;
        this.dueDate = dueDate;
        this.receivedStatus = receivedStatus;
        this.status = status;
        this.updatedBy = updatedBy;
        this.updatedDate = updatedDate;
        this.userReceiverId = userReceiverId;
        this.userReceiverName = userReceiverName;
        this.userSenderId = userSenderId;
        this.userSenderName = userSenderName;
        this.activityTypeId = activityTypeId;
        this.activityType = activityType;
        this.channelId = channelId;
        this.channel = channel;
        this.channelType = channelType;
        this.contactCaseId = contactCaseId;
        this.contactCaseCode = contactCaseCode;
    }

    //findActivity
    public ActivityInfoValue(Integer id, Date activityDate, String activityType, String description, String receiveStatus, String remark, String status, String channel,
            String channelType, Date dueDate, String createdBy, String assignedTo, Integer contactCaseId, String contactCaseCode, Boolean attachFile, String userAssignTo){
        this.id = id;
        this.activityDate = activityDate;
        this.activityType = activityType;
        this.description = description;
        this.receivedStatus = receiveStatus;
        this.remark = remark;
        this.status = status;
        this.channel = channel;
        this.channelType = channelType;
        this.dueDate = dueDate;
        this.createdBy = createdBy;
        this.userReceiverName = assignedTo;
        this.contactCaseId = contactCaseId;
        this.contactCaseCode = contactCaseCode;
        this.attachFile = attachFile;
        this.userAssignTo = userAssignTo;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the activityDate
     */
    public Date getActivityDate() {
        return activityDate;
    }

    /**
     * @param activityDate the activityDate to set
     */
    public void setActivityDate(Date activityDate) {
        this.activityDate = activityDate;
    }

    /**
     * @return the createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return the createdDate
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * @param createdDate the createdDate to set
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the dueDate
     */
    public Date getDueDate() {
        return dueDate;
    }

    /**
     * @param dueDate the dueDate to set
     */
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * @return the receivedStatus
     */
    public String getReceivedStatus() {
        return receivedStatus;
    }

    /**
     * @param receivedStatus the receivedStatus to set
     */
    public void setReceivedStatus(String receivedStatus) {
        this.receivedStatus = receivedStatus;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the updatedBy
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /**
     * @param updatedBy the updatedBy to set
     */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * @return the updatedDate
     */
    public Date getUpdatedDate() {
        return updatedDate;
    }

    /**
     * @param updatedDate the updatedDate to set
     */
    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    /**
     * @return the userReceiverId
     */
    public Integer getUserReceiverId() {
        return userReceiverId;
    }

    /**
     * @param userReceiverId the userReceiverId to set
     */
    public void setUserReceiverId(Integer userReceiverId) {
        this.userReceiverId = userReceiverId;
    }

    /**
     * @return the userReceiverName
     */
    public String getUserReceiverName() {
        return userReceiverName;
    }

    /**
     * @param userReceiverName the userReceiverName to set
     */
    public void setUserReceiverName(String userReceiverName) {
        this.userReceiverName = userReceiverName;
    }

    /**
     * @return the userSenderId
     */
    public Integer getUserSenderId() {
        return userSenderId;
    }

    /**
     * @param userSenderId the userSenderId to set
     */
    public void setUserSenderId(Integer userSenderId) {
        this.userSenderId = userSenderId;
    }

    /**
     * @return the userSenderName
     */
    public String getUserSenderName() {
        return userSenderName;
    }

    /**
     * @param userSenderName the userSenderName to set
     */
    public void setUserSenderName(String userSenderName) {
        this.userSenderName = userSenderName;
    }

    /**
     * @return the activityTypeId
     */
    public Integer getActivityTypeId() {
        return activityTypeId;
    }

    /**
     * @param activityTypeId the activityTypeId to set
     */
    public void setActivityTypeId(Integer activityTypeId) {
        this.activityTypeId = activityTypeId;
    }

    /**
     * @return the activityType
     */
    public String getActivityType() {
        return activityType;
    }

    /**
     * @param activityType the activityType to set
     */
    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    /**
     * @return the channelId
     */
    public Integer getChannelId() {
        return channelId;
    }

    /**
     * @param channelId the channelId to set
     */
    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    /**
     * @return the channel
     */
    public String getChannel() {
        return channel;
    }

    /**
     * @param channel the channel to set
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     * @return the channel
     */
    public String getChannelType() {
        return channelType;
    }

    /**
     * @param channel the channel to set
     */
    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    /**
     * @return the contactCaseId
     */
    public Integer getContactCaseId() {
        return contactCaseId;
    }

    /**
     * @param contactCaseId the contactCaseId to set
     */
    public void setContactCaseId(Integer contactCaseId) {
        this.contactCaseId = contactCaseId;
    }

    /**
     * @return the contactCaseCode
     */
    public String getContactCaseCode() {
        return contactCaseCode;
    }

    /**
     * @param contactCaseCode the contactCaseCode to set
     */
    public void setContactCaseCode(String contactCaseCode) {
        this.contactCaseCode = contactCaseCode;
    }
    
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getAttachFile() {
        return attachFile;
    }

    public void setAttachFile(Boolean attachFile) {
        this.attachFile = attachFile;
    }

    public String getUserAssignTo() {
        return userAssignTo;
    }

    public void setUserAssignTo(String userAssignTo) {
        this.userAssignTo = userAssignTo;
    }
    
}
