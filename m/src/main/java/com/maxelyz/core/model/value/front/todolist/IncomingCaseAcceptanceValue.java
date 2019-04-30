/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.value.front.todolist;

import java.util.Date;

/**
 *
 * @author Niranriths
 */
public class IncomingCaseAcceptanceValue {

    private int contactCaseId;
    private int activityId;

    private String serviceType;
    private String caseID;//contact_case.code
    private String caseType;//case_type.name
    private String caseTopic;//case_topic.name
    private String caseDetail;//case_detail.name
    private String reasonOfRequest;
    private String description;//contact_case.description
    private String status;//contact_case.status
    private Date contactDate;//contact_case.contact_date
    private String contactChanel;//chanel.name
    private Date createdDate;//contact_case.create_date
    private String createdBy;//users.name + ' ' + users.surname
    private Date lastUpdate;//contact_case.update_date
    private String updatedBy;//users.name + ' ' + users.surname
    private String attachment;//case_attachment.file_name
    private String remark;

    public IncomingCaseAcceptanceValue() {
    }

    public IncomingCaseAcceptanceValue(
            int contactCaseId,
            int activityId,
            String serviceType,
            String caseID,
            String caseType,
            String caseTopic,
            String caseDetail,
            String reasonOfRequest,
            String description,
            String status,
            Date contactDate,
            String contactChanel,
            Date createdDate,
            String createdBy,
            Date lastUpdate,
            String updatedBy,
            String attachment,
            String remark) {
        this.contactCaseId = contactCaseId;
        this.activityId = activityId;
        this.serviceType = serviceType;
        this.caseID = caseID;
        this.caseType = caseType;
        this.caseTopic = caseTopic;
        this.caseDetail = caseDetail;
        this.reasonOfRequest = reasonOfRequest;
        this.description = description;
        this.status = status;
        this.contactDate = contactDate;
        this.contactChanel = contactChanel;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.updatedBy = updatedBy;
        this.attachment = attachment;
        this.remark = remark;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getCaseDetail() {
        return caseDetail;
    }

    public void setCaseDetail(String caseDetail) {
        this.caseDetail = caseDetail;
    }

    public String getCaseID() {
        return caseID;
    }

    public void setCaseID(String caseID) {
        this.caseID = caseID;
    }

    public String getCaseTopic() {
        return caseTopic;
    }

    public void setCaseTopic(String caseTopic) {
        this.caseTopic = caseTopic;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getContactChanel() {
        return contactChanel;
    }

    public void setContactChanel(String contactChanel) {
        this.contactChanel = contactChanel;
    }

    public Date getContactDate() {
        return contactDate;
    }

    public void setContactDate(Date contactDate) {
        this.contactDate = contactDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getReasonOfRequest() {
        return reasonOfRequest;
    }

    public void setReasonOfRequest(String reasonOfRequest) {
        this.reasonOfRequest = reasonOfRequest;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getContactCaseId() {
        return contactCaseId;
    }

    public void setContactCaseId(int contactCaseId) {
        this.contactCaseId = contactCaseId;
    }
}
