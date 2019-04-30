/*
 * To change this template, choose Tools | Templates
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "unassignment_detail")
@NamedQueries({
    @NamedQuery(name = "UnassignmentDetail.findAll", query = "SELECT u FROM UnassignmentDetail u")})
public class UnassignmentDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "user_id")
    private int userId;
    @Column(name = "assignment_id")
    private Integer assignmentId;
    @Basic(optional = false)
    @Column(name = "customer_id")
    private int customerId;
    @Basic(optional = false)
    @Column(name = "called")
    private boolean called;
    @Basic(optional = false)
    @Column(name = "status")
    private String status;
    @Column(name = "followupsale_reason_id")
    private Integer followupsaleReasonId;
    @Column(name = "call_attempt")
    private Integer callAttempt;
    @Column(name = "sale_attempt")
    private Integer saleAttempt;
    @Column(name = "customer_name")
    private String customerName;
    @Column(name = "customer_type")
    private String customerType;
    @Column(name = "dob")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dob;
    @Column(name = "assign_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date assignDate;
    @Column(name = "marketing_code")
    private String marketingCode;
    @Column(name = "max_call")
    private Integer maxCall;
    @Column(name = "campaign_name")
    private String campaignName;
    @Column(name = "gender")
    private String gender;
    @JoinColumn(name = "unassignment_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Unassignment unassignment;

    public UnassignmentDetail() {
    }

    public UnassignmentDetail(Integer id) {
        this.id = id;
    }

    public UnassignmentDetail(Integer id, int userId, int customerId, boolean called, String status) {
        this.id = id;
        this.userId = userId;
        this.customerId = customerId;
        this.called = called;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Integer getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Integer assignmentId) {
        this.assignmentId = assignmentId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public boolean getCalled() {
        return called;
    }

    public void setCalled(boolean called) {
        this.called = called;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getFollowupsaleReasonId() {
        return followupsaleReasonId;
    }

    public void setFollowupsaleReasonId(Integer followupsaleReasonId) {
        this.followupsaleReasonId = followupsaleReasonId;
    }

    public Integer getCallAttempt() {
        return callAttempt;
    }

    public void setCallAttempt(Integer callAttempt) {
        this.callAttempt = callAttempt;
    }

    public Integer getSaleAttempt() {
        return saleAttempt;
    }

    public void setSaleAttempt(Integer saleAttempt) {
        this.saleAttempt = saleAttempt;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Date getAssignDate() {
        return assignDate;
    }

    public void setAssignDate(Date assignDate) {
        this.assignDate = assignDate;
    }

    public String getMarketingCode() {
        return marketingCode;
    }

    public void setMarketingCode(String marketingCode) {
        this.marketingCode = marketingCode;
    }

    public Integer getMaxCall() {
        return maxCall;
    }

    public void setMaxCall(Integer maxCall) {
        this.maxCall = maxCall;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Unassignment getUnassignment() {
        return unassignment;
    }

    public void setUnassignment(Unassignment unassignment) {
        this.unassignment = unassignment;
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
        if (!(object instanceof UnassignmentDetail)) {
            return false;
        }
        UnassignmentDetail other = (UnassignmentDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.UnassignmentDetail[id=" + id + "]";
    }

}
