/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.value.front.search;

import com.maxelyz.utils.SecurityUtil;
import java.util.Date;

/**
 *
 * @author Manop
 */
public class SearchCaseValue {
    private int caseID;             // contact_case.id
    private String code;            // contact_case.code
    private Date contactDate;       // contact_case.contact_date
    private String createdBy;       // contact_case.create_by
    private String customerName;    // customer.initial + customer.name + ' ' + customer.surname
    private String caseType;        // case_type.name
    private String caseTopic;       // case_topic.name
    private String caseDetail;      // case_detail.name
    private String caseStatus;      // contact_case.status
    private String channel;         // channel.name
    private String channelType;     // channel.type
    private String serviceType;     // contact_case.serviceType.name
    private Integer serviceTypeId;
    private Integer businessUnitId;
    private Integer locationId;
    private boolean viewLocation;

    public SearchCaseValue(){
    }

    public SearchCaseValue(
            int caseID,
            String code,
            Date contactDate,
            String createdBy,
            String customerName,
            String caseType,
            String caseTopic,
            String caseDetail,
            String caseStatus,
            String channel,
            String channelType,
            String serviceType,
            Integer serviceTypeId,
            Integer businessUnitId,
            Integer locationId            
            ){
        this.caseID = caseID;
        this.code = code;
        this.contactDate = contactDate;
        this.createdBy = createdBy;
        this.customerName = customerName;
        this.caseType = caseType;
        this.caseTopic = caseTopic;
        this.caseDetail = caseDetail;
        this.caseStatus = caseStatus;
        this.channel = channel;
        this.channelType = channelType;
        this.serviceType = serviceType;
        this.serviceTypeId = serviceTypeId;
        this.businessUnitId = businessUnitId;
        this.locationId = locationId;
        this.viewLocation = SecurityUtil.isViewLocation(serviceTypeId, businessUnitId, locationId);
    }

    /**
     * @return the cacseID
     */
    public int getCaseID() {
        return caseID;
    }

    /**
     * @param cacseID the cacseID to set
     */
    public void setCaseID(int caseID) {
        this.caseID = caseID;
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
     * @return the caseStatus
     */
    public String getCaseStatus() {
        return caseStatus;
    }

    /**
     * @param caseStatus the caseStatus to set
     */
    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
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
     * @return the channelType
     */
    public String getChannelType() {
        return channelType;
    }

    /**
     * @param channelType the channelType to set
     */
    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public boolean isViewLocation() {
        return viewLocation;
    }

    public void setViewLocation(boolean viewLocation) {
        this.viewLocation = viewLocation;
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

}
