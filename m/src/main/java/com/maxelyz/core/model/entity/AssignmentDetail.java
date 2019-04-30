/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "assignment_detail")
@NamedQueries({@NamedQuery(name = "AssignmentDetail.findAll", query = "SELECT a FROM AssignmentDetail a")})
public class AssignmentDetail implements Serializable {
    @OneToMany(mappedBy = "assignmentDetail")
    private Collection<CampaignCustomer> campaignCustomerCollection;
    @OneToMany(mappedBy = "assignmentDetail")
    private Collection<PurchaseOrder> purchaseOrderCollection;
    @OneToMany(mappedBy = "assignmentDetail")
    private Collection<TempPurchaseOrder> tempPurchaseOrderCollection;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users users;
    @JoinColumn(name = "followupsale_reason_id", referencedColumnName = "id")
    @ManyToOne
    private FollowupsaleReason followupsaleReason;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "assignmentDetail")
    private Collection<PurchaseOrderDetail> purchaseOrderDetailCollection;
    @JoinColumn(name = "unassignment_id", referencedColumnName = "id")
    @ManyToOne
    private Unassignment unassignment;
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "called")
    private boolean called;
    @Basic(optional = false)
    @Column(name = "status")
    private String status;
    @Column(name = "call_attempt")
    private Integer callAttempt;
    @Column(name = "dmc")
    private boolean dmc;
    @Column(name = "call_attempt2")
    private Integer callAttempt2;
    @Column(name = "sale_attempt")
    private Integer saleAttempt;
    @Column(name = "customer_name")
    private String customerName;
    @Column(name = "customer_type")
    private String customerType;
    @Column(name = "dob")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dob;
    @Column(name = "followupsale_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date followupsaleDate;
    @Column(name = "assign_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date assignDate;
    @Column(name = "marketing_code")
    private String marketingCode;
    @Column(name = "max_call")
    private Integer maxCall;
    @Column(name = "max_call2")
    private Integer maxCall2;
    @Column(name = "campaign_name")
    private String campaignName;
    @Column(name = "gender")
    private String gender;
    @Column(name = "transfer")
    private Boolean transfer;
    @Column(name = "transfer_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transferDate;
    @Column(name = "transfer_type")
    private Boolean transferType;
    @JoinColumn(name = "transfer_detail_id", referencedColumnName = "id")
    @ManyToOne
    private AssignmentTransferDetail assignmentTransferDetail;
    @JoinTable(name = "contact_history_assignment", joinColumns = {@JoinColumn(name = "assignment_detail_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "contact_history_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<ContactHistory> contactHistoryCollection;
    @JoinColumn(name = "assignment_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Assignment assignmentId;
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Customer customerId;
    @Column(name = "unassign")
    private Boolean unassign;
    @Column(name = "unassign_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date unassignDate;
    @Column(name = "sale_result")
    private String saleResult;
    @Column(name = "contact_result")
    private String contactResult;
    @JoinColumn(name = "contact_result_id", referencedColumnName = "id")
    @ManyToOne
    private ContactResult contactResultId;
    @Column(name = "contact_remark")
    private String contactRemark;
    @Column(name = "contact_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date contactDate;
    @Column(name = "new_list")
    private Boolean newList;
    @Column(name = "contact_status")
    private String contactStatus;
    @Column(name = "latest_contact_status")
    private String latestContactStatus;
    @Column(name = "auto_assign")
    private Boolean autoAssign;
    @Column(name = "remark")
    private String remark;

    public AssignmentDetail() {
    }

    public AssignmentDetail(Integer id) {
        this.id = id;
    }

    public AssignmentDetail(Integer id, boolean called, String status) {
        this.id = id;
        this.called = called;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getCallAttempt() {
        return callAttempt;
    }

    public void setCallAttempt(Integer callAttempt) {
        this.callAttempt = callAttempt;
    }

    public boolean isDmc() {
        return dmc;
    }

    public void setDmc(boolean dmc) {
        this.dmc = dmc;
    }

    public Integer getCallAttempt2() {
        return callAttempt2;
    }

    public void setCallAttempt2(Integer callAttempt2) {
        this.callAttempt2 = callAttempt2;
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

    public Date getFollowupsaleDate() {
        return followupsaleDate;
    }

    public void setFollowupsaleDate(Date followupsaleDate) {
        this.followupsaleDate = followupsaleDate;
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

    public Integer getMaxCall2() {
        return maxCall2;
    }

    public void setMaxCall2(Integer maxCall2) {
        this.maxCall2 = maxCall2;
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

    public Collection<ContactHistory> getContactHistoryCollection() {
        return contactHistoryCollection;
    }

    public void setContactHistoryCollection(Collection<ContactHistory> contactHistoryCollection) {
        this.contactHistoryCollection = contactHistoryCollection;
    }

    public Assignment getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Assignment assignmentId) {
        this.assignmentId = assignmentId;
    }

    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
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
        if (!(object instanceof AssignmentDetail)) {
            return false;
        }
        AssignmentDetail other = (AssignmentDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.AssignmentDetail[id=" + id + "]";
    }

    public FollowupsaleReason getFollowupsaleReason() {
        return followupsaleReason;
    }

    public void setFollowupsaleReason(FollowupsaleReason followupsaleReason) {
        this.followupsaleReason = followupsaleReason;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }


    public Collection<PurchaseOrderDetail> getPurchaseOrderDetailCollection() {
        return purchaseOrderDetailCollection;
    }

    public void setPurchaseOrderDetailCollection(Collection<PurchaseOrderDetail> purchaseOrderDetailCollection) {
        this.purchaseOrderDetailCollection = purchaseOrderDetailCollection;
    }

    public Boolean getUnassign() {
        return unassign;
    }

    public void setUnassign(Boolean unassign) {
        this.unassign = unassign;
    }

    public Date getUnassignDate() {
        return unassignDate;
    }

    public void setUnassignDate(Date unassignDate) {
        this.unassignDate = unassignDate;
    }

    public Collection<PurchaseOrder> getPurchaseOrderCollection() {
        return purchaseOrderCollection;
    }

    public void setPurchaseOrderCollection(Collection<PurchaseOrder> purchaseOrderCollection) {
        this.purchaseOrderCollection = purchaseOrderCollection;
    }

    public Collection<TempPurchaseOrder> getTempPurchaseOrderCollection() {
        return tempPurchaseOrderCollection;
    }

    public void setTempPurchaseOrderCollection(Collection<TempPurchaseOrder> tempPurchaseOrderCollection) {
        this.tempPurchaseOrderCollection = tempPurchaseOrderCollection;
    }

    public String getSaleResult() {
        return saleResult;
    }

    public void setSaleResult(String saleResult) {
        this.saleResult = saleResult;
    }

    public Unassignment getUnassignment() {
        return unassignment;
    }

    public void setUnassignment(Unassignment unassignment) {
        this.unassignment = unassignment;
    }

    public String getContactRemark() {
        return contactRemark;
    }

    public void setContactRemark(String contactRemark) {
        this.contactRemark = contactRemark;
    }

    public String getContactResult() {
        return contactResult;
    }

    public void setContactResult(String contactResult) {
        this.contactResult = contactResult;
    }

    public Date getContactDate() {
        return contactDate;
    }

    public void setContactDate(Date contactDate) {
        this.contactDate = contactDate;
    }

    
    public AssignmentTransferDetail getAssignmentTransferDetail() {
        return assignmentTransferDetail;
    }

    public void setAssignmentTransferDetail(AssignmentTransferDetail assignmentTransferDetail) {
        this.assignmentTransferDetail = assignmentTransferDetail;
    }

    public Boolean getTransfer() {
        return transfer;
    }

    public void setTransfer(Boolean transfer) {
        this.transfer = transfer;
    }

    public Date getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }

    public Boolean getTransferType() {
        return transferType;
    }

    public void setTransferType(Boolean transferType) {
        this.transferType = transferType;
    }

    public Boolean getNewList() {
        return newList;
    }

    public void setNewList(Boolean newList) {
        this.newList = newList;
    }

    public Collection<CampaignCustomer> getCampaignCustomerCollection() {
        return campaignCustomerCollection;
    }

    public void setCampaignCustomerCollection(Collection<CampaignCustomer> campaignCustomerCollection) {
        this.campaignCustomerCollection = campaignCustomerCollection;
    }

    public String getContactStatus() {
        return contactStatus;
    }

    public void setContactStatus(String contactStatus) {
        this.contactStatus = contactStatus;
    }

    public String getLatestContactStatus() {
        return latestContactStatus;
    }

    public void setLatestContactStatus(String latestContactStatus) {
        this.latestContactStatus = latestContactStatus;
    }

    public ContactResult getContactResultId() {
        return contactResultId;
    }

    public void setContactResultId(ContactResult contactResultId) {
        this.contactResultId = contactResultId;
    }
    
    public Boolean getAutoAssign() {
        return autoAssign;
    }

    public void setAutoAssign(Boolean autoAssign) {
        this.autoAssign = autoAssign;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    
}
