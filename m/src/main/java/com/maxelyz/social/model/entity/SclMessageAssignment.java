/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author apichatt
 */
@Entity
@Table(name = "scl_message_assignment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SclMessageAssignment.findAll", query = "SELECT s FROM SclMessageAssignment s"),
    @NamedQuery(name = "SclMessageAssignment.findById", query = "SELECT s FROM SclMessageAssignment s WHERE s.id = :id"),
//    @NamedQuery(name = "SclMessageAssignment.findByMessageId", query = "SELECT s FROM SclMessageAssignment s WHERE s.messageId = :messageId"),
    @NamedQuery(name = "SclMessageAssignment.findByMessage", query = "SELECT s FROM SclMessageAssignment s WHERE s.message = :message"),
    @NamedQuery(name = "SclMessageAssignment.findByAssignBy", query = "SELECT s FROM SclMessageAssignment s WHERE s.assignBy = :assignBy"),
    @NamedQuery(name = "SclMessageAssignment.findByAssignDate", query = "SELECT s FROM SclMessageAssignment s WHERE s.assignDate = :assignDate"),
    @NamedQuery(name = "SclMessageAssignment.findByReceivedBy", query = "SELECT s FROM SclMessageAssignment s WHERE s.receivedBy = :receivedBy"),
    @NamedQuery(name = "SclMessageAssignment.findByReceivedDate", query = "SELECT s FROM SclMessageAssignment s WHERE s.receivedDate = :receivedDate"),
    @NamedQuery(name = "SclMessageAssignment.findByStatusId", query = "SELECT s FROM SclMessageAssignment s WHERE s.statusId = :statusId"),
    @NamedQuery(name = "SclMessageAssignment.findByActionId", query = "SELECT s FROM SclMessageAssignment s WHERE s.actionId = :actionId"),
    @NamedQuery(name = "SclMessageAssignment.findByActionDate", query = "SELECT s FROM SclMessageAssignment s WHERE s.actionDate = :actionDate"),
    @NamedQuery(name = "SclMessageAssignment.findByRemark", query = "SELECT s FROM SclMessageAssignment s WHERE s.remark = :remark"),
    @NamedQuery(name = "SclMessageAssignment.findByCustFeelingId", query = "SELECT s FROM SclMessageAssignment s WHERE s.custFeelingId = :custFeelingId"),
    @NamedQuery(name = "SclMessageAssignment.findByCampaignId", query = "SELECT s FROM SclMessageAssignment s WHERE s.campaignId = :campaignId"),
    @NamedQuery(name = "SclMessageAssignment.findByProductId", query = "SELECT s FROM SclMessageAssignment s WHERE s.productId = :productId"),
    @NamedQuery(name = "SclMessageAssignment.findByPostTypeId", query = "SELECT s FROM SclMessageAssignment s WHERE s.postTypeId = :postTypeId"),
    @NamedQuery(name = "SclMessageAssignment.findByCreateDate", query = "SELECT s FROM SclMessageAssignment s WHERE s.createDate = :createDate"),
    @NamedQuery(name = "SclMessageAssignment.findByCreateBy", query = "SELECT s FROM SclMessageAssignment s WHERE s.createBy = :createBy"),
    @NamedQuery(name = "SclMessageAssignment.findByUpdateDate", query = "SELECT s FROM SclMessageAssignment s WHERE s.updateDate = :updateDate"),
    @NamedQuery(name = "SclMessageAssignment.findByUpdateBy", query = "SELECT s FROM SclMessageAssignment s WHERE s.updateBy = :updateBy")})
public class SclMessageAssignment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

//    @Column(name = "message_id")
//    private Integer messageId;
    @JoinColumn(name = "message_id", referencedColumnName = "id")
    @ManyToOne
    private SclMessage message;

    @Column(name = "assign_by")
    private Integer assignBy;
    @Column(name = "assign_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date assignDate;
    @Column(name = "received_by")
    private Integer receivedBy;
    @Column(name = "received_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receivedDate;
    @Column(name = "status_id")
    private Integer statusId;
    @Column(name = "action_id")
    private Integer actionId;
    @Column(name = "action_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actionDate;
    @Column(name = "remark")
    private String remark;
    @Column(name = "cust_feeling_id")
    private Integer custFeelingId;
    @Column(name = "campaign_id")
    private Integer campaignId;
    @Column(name = "product_id")
    private Integer productId;
    @Column(name = "post_type_id")
    private Integer postTypeId;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "create_by")
    private Integer createBy;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "update_by")
    private Integer updateBy;
    @Column(name = "priority")
    private Integer priority;

    public SclMessageAssignment() {
    }

    public SclMessageAssignment(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    //    public Integer getMessageId() {
    //        return messageId;
    //    }
    //
    //    public void setMessageId(Integer messageId) {
    //        this.messageId = messageId;
    //    }
    public SclMessage getMessage() {
        return message;
    }

    public void setMessage(SclMessage message) {
        this.message = message;
    }

    public Integer getAssignBy() {
        return assignBy;
    }

    public void setAssignBy(Integer assignBy) {
        this.assignBy = assignBy;
    }

    public Date getAssignDate() {
        return assignDate;
    }

    public void setAssignDate(Date assignDate) {
        this.assignDate = assignDate;
    }

    public Integer getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(Integer receivedBy) {
        this.receivedBy = receivedBy;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getActionId() {
        return actionId;
    }

    public void setActionId(Integer actionId) {
        this.actionId = actionId;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getCustFeelingId() {
        return custFeelingId;
    }

    public void setCustFeelingId(Integer custFeelingId) {
        this.custFeelingId = custFeelingId;
    }

    public Integer getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getPostTypeId() {
        return postTypeId;
    }

    public void setPostTypeId(Integer postTypeId) {
        this.postTypeId = postTypeId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
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
        if (!(object instanceof SclMessageAssignment)) {
            return false;
        }
        SclMessageAssignment other = (SclMessageAssignment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.SclMessageAssignment[ id=" + id + " ]";
    }
    
}
