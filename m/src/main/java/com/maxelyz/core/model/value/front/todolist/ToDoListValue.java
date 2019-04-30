/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.value.front.todolist;

import com.maxelyz.core.model.entity.ContactCase;
import com.maxelyz.utils.JSFUtil;
import java.util.Date;

/**
 *
 * @author Niranriths
 */
public class ToDoListValue {
    private ContactCase contactCase;
    private Integer contactCaseId;
    private Integer activityId;

    private String caseId;  //contact_case.code
    private Date dueDate;   // activity.due_date
    private Date activityDate;  // activity.activity_date
    private Date sendDate;  //activity.create_date
    private String sender;  //users.login_name
    private String receiver;  //users.login_name
    private String customer;    //customer.name + ' ' + customer.surname
    private String caseType;    //case_type.name
    private String caseTopic;   //case_topic.name
    private String caseDetail;  //case_detail.name
    private String iconFlag;    // 'CC' - activity.due_date - current_date > 0
                                // 'CA' - current_date * 24 - activity.activity_date * 24 > configuration.value
    private boolean  rejectFlag;
    private Date slaCloseDate;
    private Date slaAcceptDate;
    private boolean overClose = false;
    private boolean overAccept = false;
    private String description;
    private Date createDate;
    private String priority;
    private Boolean attachFile;
    
    private String receiver_user;
    private String receiver_status;

    public ToDoListValue() {
    }

    public ToDoListValue(ContactCase cc) {
        this.contactCase = cc;
        this.slaCloseDate = cc.getSlaCloseDate();
        this.rejectFlag = false;
        try {
            //if (cc.getActivity().getReceiveStatus().equals("reject")) //lastest activity
            if(cc.getActivityDelegate().getReceiveStatus().equals("reject"))  {//lastest task delegate activity; 
                this.rejectFlag = true;
            }
        } catch(Exception e) {
            this.rejectFlag = false;    
        }
        long current = new Date().getTime();
        
        if(this.slaCloseDate != null){
            long closeDate = this.slaCloseDate.getTime();
            if (current > closeDate){
                this.overClose = true;
            }
        }
        if(cc.getActivityDelegate() != null) {
            this.receiver_user = cc.getActivityDelegate().getAssignTo();
            this.receiver_status = cc.getActivityDelegate().getReceiveStatus()==null?"-":cc.getActivityDelegate().getReceiveStatus().equals("reject")?"Reject":cc.getActivityDelegate().getReceiveStatus().equals("accepted")?"Accepted":"-";
            this.sender = JSFUtil.getUserSession().getUserName();
        } else {
            this.receiver_user = "-";
            this.receiver_status = "-";
            this.sender = "";
        }
    }

    public ToDoListValue(
            int contactCaseId,
            String caseId,
            String customer,
            Date createDate,
            String caseType,
            String caseTopic,
            String caseDetail,
            Date slaCloseDate,
            String description) {
        this.contactCaseId = contactCaseId;
        this.caseId = caseId;
        this.customer = customer;
        this.createDate = createDate;
        this.caseType = caseType;
        this.caseTopic = caseTopic;
        this.caseDetail = caseDetail;
        this.slaCloseDate = slaCloseDate;
        this.description = description;

        long current = new Date().getTime();

        // Determine what icon need to display
        if(this.slaCloseDate != null){
            long closeDate = this.slaCloseDate.getTime();
            if (current > closeDate){
                this.overClose = true;
            }
        }
    }

    public ToDoListValue(
            int contactCaseId,
            int activityId,
            Date dueDate,
            Date activityDate,
            String caseId,
            Date sendDate,
            String sender,
            String customer,
            String caseType,
            String caseTopic,
            String caseDetail,
            Date slaAcceptDate,
            Date slaCloseDate,
            String receiver,
            String description) {
        this.contactCaseId = contactCaseId;
        this.activityId = activityId;
        this.dueDate = dueDate;
        this.activityDate = activityDate;
        this.caseId = caseId;
        this.sendDate = sendDate;
        this.sender = sender;
        this.customer = customer;
        this.caseType = caseType;
        this.caseTopic = caseTopic;
        this.caseDetail = caseDetail;
        this.slaAcceptDate = slaAcceptDate;
        this.slaCloseDate = slaCloseDate;
        this.receiver = receiver;
        this.description = description;

        long current = new Date().getTime();

        // Determine what icon need to display
        if(this.slaAcceptDate != null){
            long acceptDate = this.slaAcceptDate.getTime();
            if (current > acceptDate){
                this.overAccept = true;
            }
        }

        if(this.slaCloseDate != null){
            long closeDate = this.slaCloseDate.getTime();
            if (current > closeDate){
                this.overClose = true;
            }
        }

        this.rejectFlag = false;
    }
    
    // for incoming list
    public ToDoListValue(
            int contactCaseId,
            int activityId,
            Date dueDate,
            Date activityDate,
            String caseId,
            Date sendDate,
            String sender,
            String customer,
            String caseType,
            String caseTopic,
            String caseDetail,
            Date slaAcceptDate,
            Date slaCloseDate,
            String receiver,
            String description,
            String priority,
            Boolean attachFile) {
        this.contactCaseId = contactCaseId;
        this.activityId = activityId;
        this.dueDate = dueDate;
        this.activityDate = activityDate;
        this.caseId = caseId;
        this.sendDate = sendDate;
        this.sender = sender;
        this.customer = customer;
        this.caseType = caseType;
        this.caseTopic = caseTopic;
        this.caseDetail = caseDetail;
        this.slaAcceptDate = slaAcceptDate;
        this.slaCloseDate = slaCloseDate;
        this.receiver = receiver;
        this.description = description;
        this.priority = priority;
        this.attachFile = attachFile;        
        long current = new Date().getTime();

        // Determine what icon need to display
        if(this.slaAcceptDate != null){
            long acceptDate = this.slaAcceptDate.getTime();
            if (current > acceptDate){
                this.overAccept = true;
            }
        }

        if(this.slaCloseDate != null){
            long closeDate = this.slaCloseDate.getTime();
            if (current > closeDate){
                this.overClose = true;
            }
        }

        this.rejectFlag = false;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getCaseDetail() {
        return caseDetail;
    }

    public void setCaseDetail(String caseDetail) {
        this.caseDetail = caseDetail;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
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

    public Integer getContactCaseId() {
        return contactCaseId;
    }

    public void setContactCaseId(Integer contactCaseId) {
        this.contactCaseId = contactCaseId;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
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
     * @return the iconFlag
     */
    public String getIconFlag() {
        return iconFlag;
    }

    /**
     * @param iconFlag the iconFlag to set
     */
    public void setIconFlag(String iconFlag) {
        this.iconFlag = iconFlag;
    }

    /**
     * @return the rejectFlag
     */
    public boolean getRejectFlag() {
        return rejectFlag;
    }

    /**
     * @param rejectFlag the rejectFlag to set
     */
    public void setRejectFlag(boolean rejectFlag) {
        this.rejectFlag = rejectFlag;
    }

    public Date getSlaAcceptDate() {
        return slaAcceptDate;
    }

    public void setSlaAcceptDate(Date slaAcceptDate) {
        this.slaAcceptDate = slaAcceptDate;
    }

    public Date getSlaCloseDate() {
        return slaCloseDate;
    }

    public void setSlaCloseDate(Date slaCloseDate) {
        this.slaCloseDate = slaCloseDate;
    }

    public boolean isOverAccept() {
        return overAccept;
    }

    public void setOverAccept(boolean overAccept) {
        this.overAccept = overAccept;
    }

    public boolean isOverClose() {
        return overClose;
    }

    public void setOverClose(boolean overClose) {
        this.overClose = overClose;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public ContactCase getContactCase() {
        return contactCase;
    }

    public void setContactCase(ContactCase contactCase) {
        this.contactCase = contactCase;
    }

    public Boolean getAttachFile() {
        return attachFile;
    }

    public void setAttachFile(Boolean attachFile) {
        this.attachFile = attachFile;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getReceiver_status() {
        return receiver_status;
    }

    public void setReceiver_status(String receiver_status) {
        this.receiver_status = receiver_status;
    }

    public String getReceiver_user() {
        return receiver_user;
    }

    public void setReceiver_user(String receiver_user) {
        this.receiver_user = receiver_user;
    }

}
