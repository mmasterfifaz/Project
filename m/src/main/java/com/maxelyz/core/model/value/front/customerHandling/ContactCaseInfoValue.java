/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.value.front.customerHandling;

import com.maxelyz.core.model.entity.BusinessUnit;
import com.maxelyz.core.model.entity.Location;
import com.maxelyz.core.model.entity.UserGroupLocation;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Manop
 */
public class ContactCaseInfoValue {
    private Integer id;
    private String code;
    private Date contactDate;
    private String contactPerson;
    private String createdBy;
    private Date createdDate;
    private String description;
    private String priority;
    private Integer relationshipId;
//    private String relationship;
    private String remark;
    private Date scheduleDate;
    private String scheduleDescription;
    private String status;
    private String updatedBy;
    private Date updatedDate;
    private Integer caseDetailId;
    private String caseDetail;
    private Integer caseRequestId;
    private String caseRequest;
    private Integer caseTopicId;
    private String caseTopic;
    private Integer caseTypeId;
    private String caseType;
    private Integer channelId;
    private String channel;
    private String channelType;
    private Integer customerId;
    private String customerName;
    private Integer refContactCaseId;
    private String refContactCaseCode;
    private String serviceTypeName;
    
    //Activity
    private String activityTypeName;
    private String activityDescription;
    private String activityStatus;
    private String channelName;
    private Integer serviceTypeId;
    private Integer businessUnitId;
    private Integer locationId;
    private boolean viewLocation = false;
    private String assignTo;
    
    //ContactHistory
    private Date chContactDate;
    private String chChannel;
    private String chChannelType;
    private String chResult;
    private String chCsrName;
    private String chTelNo;
    private Boolean attachFile;
    private boolean bCounterEdit = SecurityUtil.isPermitted("case:edit:counteredit");
    private List<UserGroupLocation> userGroupLocationList = JSFUtil.getUserSession().getUserGroupLocationList();

    public ContactCaseInfoValue(){}
    
    //For search case controller and customer detail
    public ContactCaseInfoValue(
            Integer id, String channel, String channelType, String caseType, Date contactDate, String status, String code, String caseDetail,
            String activityTypeName, String activityDescription, String activityStatus, String channelName, 
            Integer serviceTypeId, Integer businessUnitId, Integer locationId,
            Date chContactDate, String chChannel, String chChannelType, String chResult, String chCsrName, String chTelNo,
            String customerName, String serviceTypeName, String assignTo, String priority, Boolean attachFile, Integer customerId
            ){
        this.id = id;
        this.channel = channel;
        this.channelType = channelType;
        this.caseType = caseType;
        this.contactDate = contactDate;
        this.status = status;
        this.code = code;
        this.caseDetail = caseDetail;
        this.priority = priority;
        this.attachFile = attachFile;

        this.activityTypeName = activityTypeName;
        this.activityDescription = activityDescription;
        this.activityStatus = activityStatus;
        this.channelName = channelName;
        this.serviceTypeId = serviceTypeId;
        this.businessUnitId = businessUnitId;
        this.locationId = locationId;
        checkCounterEdit();
        
        //Contact History
        this.chContactDate = chContactDate;
        this.chChannel = chChannel;
        this.chChannelType = chChannelType;
        this.chResult = chResult;
        this.chCsrName = chCsrName;
        this.chTelNo = chTelNo;
        
        this.customerName = customerName;
        this.serviceTypeName = serviceTypeName;
        this.assignTo = assignTo;
        this.customerId = customerId;
    }

    public ContactCaseInfoValue(
            Integer id, String channel, String channelType, String caseType, Date contactDate, String status, String code, String caseDetail,
            String activityTypeName, String activityDescription, String activityStatus, String channelName, 
            Integer serviceTypeId, Integer businessUnitId, Integer locationId,
            Date chContactDate, String chChannel, String chChannelType, String chResult, String chCsrName, String chTelNo,
            String customerName, String serviceTypeName, String assignTo
            ){
        this.id = id;
        this.channel = channel;
        this.channelType = channelType;
        this.caseType = caseType;
        this.contactDate = contactDate;
        this.status = status;
        this.code = code;
        this.caseDetail = caseDetail;

        this.activityTypeName = activityTypeName;
        this.activityDescription = activityDescription;
        this.activityStatus = activityStatus;
        this.channelName = channelName;
        this.serviceTypeId = serviceTypeId;
        this.businessUnitId = businessUnitId;
        this.locationId = locationId;
        checkCounterEdit();
        
        //Contact History
        this.chContactDate = chContactDate;
        this.chChannel = chChannel;
        this.chChannelType = chChannelType;
        this.chResult = chResult;
        this.chCsrName = chCsrName;
        this.chTelNo = chTelNo;
        
        this.customerName = customerName;
        this.serviceTypeName = serviceTypeName;
        this.assignTo = assignTo;
    }

    public ContactCaseInfoValue(
            Integer id, String code, Date contactDate, String contactPerson, String createdBy, Date createdDate, String description,
            String priority, Integer relationshipId, /*String relationship,*/ String remark, Date scheduleDate, String scheduleDescription,
            String status, String updatedBy, Date updatedDate, Integer caseDetailId, String caseDetail, Integer caseRequestId,
            String caseRequest, Integer caseTopicId, String caseTopic, Integer caseTypeId, String caseType, Integer channelId, String channel,
            String channelType, Integer customerId, String customerName, Integer refContactCaseId, String refContactCaseCode) {
        this.id = id;
        this.code = code;
        this.contactDate = contactDate;
        this.contactPerson = contactPerson;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.description = description;
        this.priority = priority;
        this.relationshipId = relationshipId;
//            this.relationship = relationship;
        this.remark = remark;
        this.scheduleDate = scheduleDate;
        this.scheduleDescription = scheduleDescription;
        this.status = status;
        this.updatedBy = updatedBy;
        this.updatedDate = updatedDate;
        this.caseDetailId = caseDetailId;
        this.caseDetail = caseDetail;
        this.caseRequestId = caseRequestId;
        this.caseRequest = caseRequest;
        this.caseTopicId = caseTopicId;
        this.caseTopic = caseTopic;
        this.caseTypeId = caseTypeId;
        this.caseType = caseType;
        this.channelId = channelId;
        this.channel = channel;
        this.channelType = channelType;
        this.customerId = customerId;
        this.customerName = customerName;
        this.refContactCaseId = refContactCaseId;
        this.refContactCaseCode = refContactCaseCode;
        
        checkCounterEdit();

    }
    
    //vee
    public ContactCaseInfoValue(
            Integer id, String channel, String channelType, String caseType, Date contactDate, String status, String code, String caseDetail,
            String activityTypeName, String description, String activityStatus, String channelName, 
            Integer serviceTypeId, Integer businessUnitId, Integer locationId,
            Date chContactDate, String chChannel, String chChannelType, String chResult, String chCsrName, String chTelNo,
            String customerName, String serviceTypeName, String assignTo, String contactPerson, String caseTopic
            ){
        this.id = id;
        this.channel = channel;
        this.channelType = channelType;
        this.caseType = caseType;
        this.contactDate = contactDate;
        this.status = status;
        this.code = code;
        this.caseDetail = caseDetail;

        this.activityTypeName = activityTypeName;
        this.description = description;
        this.activityStatus = activityStatus;
        this.channelName = channelName;
        this.serviceTypeId = serviceTypeId;
        this.businessUnitId = businessUnitId;
        this.locationId = locationId;
        checkCounterEdit();
        
        //Contact History
        this.chContactDate = chContactDate;
        this.chChannel = chChannel;
        this.chChannelType = chChannelType;
        this.chResult = chResult;
        this.chCsrName = chCsrName;
        this.chTelNo = chTelNo;
        
        this.customerName = customerName;
        this.serviceTypeName = serviceTypeName;
        this.assignTo = assignTo;
        this.contactPerson = contactPerson;
        this.caseTopic = caseTopic;
    }
    
    private void checkCounterEdit(){    
        try{
            if(bCounterEdit){
                for(UserGroupLocation userGroupLocation : userGroupLocationList){
                    BusinessUnit bu = userGroupLocation.getBusinessUnit();
                    if(bu.getId().intValue() == businessUnitId.intValue()){
                        this.viewLocation = true;
                        break;
                    }else{
                        this.viewLocation = false;
                    }
                }
            }else{
                this.viewLocation = SecurityUtil.isViewLocation(serviceTypeId, businessUnitId, locationId);
            }
        }catch(Exception e){
            this.viewLocation = SecurityUtil.isViewLocation(serviceTypeId, businessUnitId, locationId);
        }
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
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the contactDate
     */
    public Date getContactDate() {
        return contactDate;
    }

    /**
     * @param contactDate the contactDate to set
     */
    public void setContactDate(Date contactDate) {
        this.contactDate = contactDate;
    }

    /**
     * @return the contactPerson
     */
    public String getContactPerson() {
        return contactPerson;
    }

    /**
     * @param contactPerson the contactPerson to set
     */
    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
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
     * @return the priority
     */
    public String getPriority() {
        return priority;
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * @return the relationship
     */
    public Integer getRelationshipId() {
        return relationshipId;
    }

    /**
     * @param relationship the relationship to set
     */
    public void setRelationshipId(Integer relationshipId) {
        this.relationshipId = relationshipId;
    }

    /**
     * @return the relationship
     */
//    public String getRelationship() {
//        return relationship;
//    }

    /**
     * @param relationship the relationship to set
     */
//    public void setRelationship(String relationship) {
//        this.relationship = relationship;
//    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return the scheduleDate
     */
    public Date getScheduleDate() {
        return scheduleDate;
    }

    /**
     * @param scheduleDate the scheduleDate to set
     */
    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    /**
     * @return the scheduleDescription
     */
    public String getScheduleDescription() {
        return scheduleDescription;
    }

    /**
     * @param scheduleDescription the scheduleDescription to set
     */
    public void setScheduleDescription(String scheduleDescription) {
        this.scheduleDescription = scheduleDescription;
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
     * @return the caseDetailId
     */
    public Integer getCaseDetailId() {
        return caseDetailId;
    }

    /**
     * @param caseDetailId the caseDetailId to set
     */
    public void setCaseDetailId(Integer caseDetailId) {
        this.caseDetailId = caseDetailId;
    }

    /**
     * @return the caseDetail
     */
    public String getCaseDetail() {
        return caseDetail;
    }

    /**
     * @param caseDetail the caseDetail to set
     */
    public void setCaseDetail(String caseDetail) {
        this.caseDetail = caseDetail;
    }

    /**
     * @return the caseRequestId
     */
    public Integer getCaseRequestId() {
        return caseRequestId;
    }

    /**
     * @param caseRequestId the caseRequestId to set
     */
    public void setCaseRequestId(Integer caseRequestId) {
        this.caseRequestId = caseRequestId;
    }

    /**
     * @return the caseRequest
     */
    public String getCaseRequest() {
        return caseRequest;
    }

    /**
     * @param caseRequest the caseRequest to set
     */
    public void setCaseRequest(String caseRequest) {
        this.caseRequest = caseRequest;
    }

    /**
     * @return the caseTopicId
     */
    public Integer getCaseTopicId() {
        return caseTopicId;
    }

    /**
     * @param caseTopicId the caseTopicId to set
     */
    public void setCaseTopicId(Integer caseTopicId) {
        this.caseTopicId = caseTopicId;
    }

    /**
     * @return the caseTopic
     */
    public String getCaseTopic() {
        return caseTopic;
    }

    /**
     * @param caseTopic the caseTopic to set
     */
    public void setCaseTopic(String caseTopic) {
        this.caseTopic = caseTopic;
    }

    /**
     * @return the caseTypeId
     */
    public Integer getCaseTypeId() {
        return caseTypeId;
    }

    /**
     * @param caseTypeId the caseTypeId to set
     */
    public void setCaseTypeId(Integer caseTypeId) {
        this.caseTypeId = caseTypeId;
    }

    /**
     * @return the caseType
     */
    public String getCaseType() {
        return caseType;
    }

    /**
     * @param caseType the caseType to set
     */
    public void setCaseType(String caseType) {
        this.caseType = caseType;
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
     * @return the customerId
     */
    public Integer getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName the customerName to set
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * @return the refContactCaseId
     */
    public Integer getRefContactCaseId() {
        return refContactCaseId;
    }

    /**
     * @param refContactCaseId the refContactCaseId to set
     */
    public void setRefContactCaseId(Integer refContactCaseId) {
        this.refContactCaseId = refContactCaseId;
    }

    /**
     * @return the refContactCaseCode
     */
    public String getRefContactCaseCode() {
        return refContactCaseCode;
    }

    /**
     * @param refContactCaseCode the refContactCaseCode to set
     */
    public void setRefContactCaseCode(String refContactCaseCode) {
        this.refContactCaseCode = refContactCaseCode;
    }

    public String getActivityTypeName() {
        return activityTypeName;
    }

    public void setActivityTypeName(String activityTypeName) {
        this.activityTypeName = activityTypeName;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public String getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(String activityStatus) {
        this.activityStatus = activityStatus;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Integer getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(Integer businessUnitId) {
        this.businessUnitId = businessUnitId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Integer serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public boolean isViewLocation() {
        return viewLocation;
    }

    public void setViewLocation(boolean viewLocation) {
        this.viewLocation = viewLocation;
    }

    public String getChChannel() {
        return chChannel;
    }

    public void setChChannel(String chChannel) {
        this.chChannel = chChannel;
    }

    public Date getChContactDate() {
        return chContactDate;
    }

    public void setChContactDate(Date chContactDate) {
        this.chContactDate = chContactDate;
    }

    public String getChResult() {
        return chResult;
    }

    public void setChResult(String chResult) {
        this.chResult = chResult;
    }

    public String getChCsrName() {
        return chCsrName;
    }

    public void setChCsrName(String chCsrName) {
        this.chCsrName = chCsrName;
    }

    public String getChTelNo() {
        return chTelNo;
    }

    public void setChTelNo(String chTelNo) {
        this.chTelNo = chTelNo;
    }

    public String getChChannelType() {
        return chChannelType;
    }

    public void setChChannelType(String chChannelType) {
        this.chChannelType = chChannelType;
    }

    public String getAssignTo() {
        return assignTo;
    }

    public void setAssignTo(String assignTo) {
        this.assignTo = assignTo;
    }

    public String getServiceTypeName() {
        return serviceTypeName;
    }

    public void setServiceTypeName(String serviceTypeName) {
        this.serviceTypeName = serviceTypeName;
    }

    public Boolean getAttachFile() {
        return attachFile;
    }

    public void setAttachFile(Boolean attachFile) {
        this.attachFile = attachFile;
    }
    
}
