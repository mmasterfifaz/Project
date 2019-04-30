/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author oat
 */
@Entity
@Table(name = "purchase_order_approval_log")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PurchaseOrderApprovalLog.findAll", query = "SELECT p FROM PurchaseOrderApprovalLog p")})
public class PurchaseOrderApprovalLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "approval_status", nullable = true, length = 50)
    private String approvalStatus;
    //@Basic(optional = false)
    @JoinColumn(name = "approval_reason_id", referencedColumnName = "id")
    @ManyToOne
    private ApprovalReason approvalReason;
    @Basic(optional = false)
    @JoinColumn(name = "purchase_order_id", referencedColumnName = "id")
    @ManyToOne
    private PurchaseOrder purchaseOrder;
    //@Basic(optional = false)
    @Column(name = "to_role", nullable = true, length = 50)
    private String toRole;
    @JoinColumn(name = "to_user_id", referencedColumnName = "id")
    @ManyToOne
    private Users toUser;
    @Basic(optional = false)
    @Column(name = "create_by", nullable = false, length = 100)
    private String createBy;
    @Basic(optional = false)
    @Column(name = "create_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "create_by_role", length = 100)
    private String createByRole;
    @Basic(optional = false)
    @JoinColumn(name = "create_by_id", referencedColumnName = "id")
    @ManyToOne
    private Users users;
    @Column(name = "message")
    private String message;
    
    @Column(name = "other_exclusion_code")
    private String otherExclusionCode;
    @Column(name = "other_exclusion_name")
    private String otherExclusionName;
    
    @JoinTable(name = "purchase_order_approval_exclusion", joinColumns = {
        @JoinColumn(name = "purchase_order_approval_log_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "approval_exclusion_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<ApprovalExclusion> approvalExclusionCollection;

    public PurchaseOrderApprovalLog() {
    }

    public PurchaseOrderApprovalLog(Integer id) {
        this.id = id;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getToRole() {
        return toRole;
    }

    public void setToRole(String toRole) {
        this.toRole = toRole;
    }

    public Users getToUser() {
        return toUser;
    }

    public void setToUser(Users toUser) {
        this.toUser = toUser;
    }


    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateByRole() {
        return createByRole;
    }

    public void setCreateByRole(String createByRole) {
        this.createByRole = createByRole;
    }

    public Collection<ApprovalExclusion> getApprovalExclusionCollection() {
        return approvalExclusionCollection;
    }

    public void setApprovalExclusionCollection(Collection<ApprovalExclusion> approvalExclusionCollection) {
        this.approvalExclusionCollection = approvalExclusionCollection;
    }

    public ApprovalReason getApprovalReason() {
        return approvalReason;
    }

    public void setApprovalReason(ApprovalReason approvalReason) {
        this.approvalReason = approvalReason;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
        if (!(object instanceof PurchaseOrderApprovalLog)) {
            return false;
        }
        PurchaseOrderApprovalLog other = (PurchaseOrderApprovalLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.PurchaseOrderApprovalLog[ id=" + id + " ]";
    }

    public String getOtherExclusionCode() {
        return otherExclusionCode;
    }

    public void setOtherExclusionCode(String otherExclusionCode) {
        this.otherExclusionCode = otherExclusionCode;
    }

    public String getOtherExclusionName() {
        return otherExclusionName;
    }

    public void setOtherExclusionName(String otherExclusionName) {
        this.otherExclusionName = otherExclusionName;
    }
    
    
    
}
