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
@Table(name = "followupsale_reason")
@NamedQueries({
    @NamedQuery(name = "FollowupsaleReason.findAll", query = "SELECT f FROM FollowupsaleReason f")})
public class FollowupsaleReason implements Serializable {
    @OneToMany(mappedBy = "followupsaleReason")
    private Collection<AssignmentDetail> assignmentDetailCollection;
    @OneToMany(mappedBy = "followupsaleReason")
    private Collection<PurchaseOrder> purchaseOrderCollection;
    @OneToMany(mappedBy = "followupsaleReason")
    private Collection<ContactHistory> contactHistoryCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "code")
    private String code;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "enable")
    private Boolean enable;
    @Column(name = "status")
    private Boolean status;
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
    @OneToMany(mappedBy = "followupsaleReason")
    private Collection<TempPurchaseOrder> tempPurchaseOrderCollection;
    @Column(name = "status_id")
    private String statusId;
    @Column(name = "export")
    private Boolean export;

    public FollowupsaleReason() {
    }

    public FollowupsaleReason(Integer id) {
        this.id = id;
    }

    public FollowupsaleReason(Integer id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FollowupsaleReason)) {
            return false;
        }
        FollowupsaleReason other = (FollowupsaleReason) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.FollowupsaleReason[id=" + id + "]";
    }

    public Collection<AssignmentDetail> getAssignmentDetailCollection() {
        return assignmentDetailCollection;
    }

    public void setAssignmentDetailCollection(Collection<AssignmentDetail> assignmentDetailCollection) {
        this.assignmentDetailCollection = assignmentDetailCollection;
    }

    public Collection<ContactHistory> getContactHistoryCollection() {
        return contactHistoryCollection;
    }

    public void setContactHistoryCollection(Collection<ContactHistory> contactHistoryCollection) {
        this.contactHistoryCollection = contactHistoryCollection;
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

    public String getStatusId() {
        return statusId;
}

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public Boolean getExport() {
        return export;
    }

    public void setExport(Boolean export) {
        this.export = export;
    }

}
