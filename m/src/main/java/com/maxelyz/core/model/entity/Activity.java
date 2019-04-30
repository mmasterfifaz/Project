/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "activity")
@NamedQueries({
    @NamedQuery(name = "Activity.findAll", query = "SELECT a FROM Activity a")})
public class Activity implements Serializable {
    @JoinColumn(name = "contact_case_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ContactCase contactCase;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "activity_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date activityDate;
    @Column(name = "due_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dueDate;
    @Column(name = "accept_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date acceptDate;
    @Lob
    @Column(name = "description")
    private String description;
//    @Column(name = "user_receiver_id")
//    private Integer userReceiverId;
//    @Column(name = "user_sender_id")
//    private Integer userSenderId;
    @Column(name = "remark")
    private String remark;
    @Column(name = "status")
    private String status;
    @Column(name = "receive_status") 
    private String receiveStatus; //accepted, rejected
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "create_by")
    private String createBy;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "update_by")
    private String updateBy;
    @Column(name = "user_receiver_email")
    private String userReceiverEmail;
    @Column(name = "user_receiver_telephone")
    private String userReceiverTelephone;
    @Column(name = "sla_accept_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date slaAcceptDate;
    @JoinColumn(name = "activity_type_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ActivityType activityTypeId;
    @JoinColumn(name = "channel_id", referencedColumnName = "id")
    @ManyToOne
    private Channel channelId;
    @JoinColumn(name = "user_sender_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users userSenderId;
    @JoinColumn(name = "user_receiver_id", referencedColumnName = "id")
    @ManyToOne
    private Users userReceiverId;
    @Column(name = "receiver_name")
    private String receiverName;
    @Column(name = "assign_to")
    private String assignTo;
    @Column(name = "attach_file")
    private Boolean attachFile;

    public Activity() {
    }

    public Activity(Integer id) {
        this.id = id;
    }

    public Activity(Integer id, Date activityDate, Date dueDate) {
        this.id = id;
        this.activityDate = activityDate;
        this.dueDate = dueDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(Date activityDate) {
        this.activityDate = activityDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getAcceptDate() {
        return acceptDate;
    }

    public void setAcceptDate(Date acceptDate) {
        this.acceptDate = acceptDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public Integer getUserReceiverId() {
//        return userReceiverId;
//    }
//
//    public void setUserReceiverId(Integer userReceiverId) {
//        this.userReceiverId = userReceiverId;
//    }
//
//    public Integer getUserSenderId() {
//        return userSenderId;
//    }
//
//    public void setUserSenderId(Integer userSenderId) {
//        this.userSenderId = userSenderId;
//    }
//
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceiveStatus() {
        return receiveStatus;
    }

    public void setReceiveStatus(String receiveStatus) {
        this.receiveStatus = receiveStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public ActivityType getActivityTypeId() {
        return activityTypeId;
    }

    public void setActivityTypeId(ActivityType activityTypeId) {
        this.activityTypeId = activityTypeId;
    }

    public Channel getChannelId() {
        return channelId;
    }

    public void setChannelId(Channel channelId) {
        this.channelId = channelId;
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
        if (!(object instanceof Activity)) {
            return false;
        }
        Activity other = (Activity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.Activity[id=" + id + "]";
    }

    /**
     * @return the userSenderId
     */
    public Users getUserSenderId() {
        return userSenderId;
    }

    /**
     * @param userSenderId the userSenderId to set
     */
    public void setUserSenderId(Users userSenderId) {
        this.userSenderId = userSenderId;
    }

    /**
     * @return the userReceiverId
     */
    public Users getUserReceiverId() {
        return userReceiverId;
    }

    /**
     * @param userReceiverId the userReceiverId to set
     */
    public void setUserReceiverId(Users userReceiverId) {
        this.userReceiverId = userReceiverId;
    }

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


    public ContactCase getContactCase() {
        return contactCase;
    }

    public void setContactCase(ContactCase contactCase) {
        this.contactCase = contactCase;
    }

    public String getUserReceiverEmail() {
        return userReceiverEmail;
    }

    public void setUserReceiverEmail(String userReceiverEmail) {
        this.userReceiverEmail = userReceiverEmail;
    }

    public String getUserReceiverTelephone() {
        return userReceiverTelephone;
    }

    public void setUserReceiverTelephone(String userReceiverTelephone) {
        this.userReceiverTelephone = userReceiverTelephone;
    }

    public Date getSlaAcceptDate() {
        return slaAcceptDate;
    }

    public void setSlaAcceptDate(Date slaAcceptDate) {
        this.slaAcceptDate = slaAcceptDate;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getAssignTo() {
        return assignTo;
    }

    public void setAssignTo(String assignTo) {
        this.assignTo = assignTo;
    }

    public Boolean getAttachFile() {
        return attachFile;
    }

    public void setAttachFile(Boolean attachFile) {
        this.attachFile = attachFile;
    }
}
