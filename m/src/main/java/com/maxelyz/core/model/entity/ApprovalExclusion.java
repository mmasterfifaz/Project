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
@Table(name = "approval_exclusion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ApprovalExclusion.findAll", query = "SELECT a FROM ApprovalExclusion a")})
public class ApprovalExclusion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "code", nullable = false, length = 100)
    private String code;
    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Basic(optional = false)
    @Column(name = "status", nullable = false)
    private boolean status;
    @Basic(optional = false)
    @Column(name = "enable", nullable = false)
    private boolean enable;
    @Column(name = "create_by", length = 100)
    private String createBy;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "update_by", length = 100)
    private String updateBy;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @ManyToMany(mappedBy = "approvalExclusionCollection")
    private Collection<PurchaseOrderApprovalLog> purchaseOrderApprovalLogCollection;

    public ApprovalExclusion() {
    }

    public ApprovalExclusion(Integer id) {
        this.id = id;
    }

    public ApprovalExclusion(Integer id, String code, String name, boolean status, boolean enable) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.status = status;
        this.enable = enable;
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

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
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

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Collection<PurchaseOrderApprovalLog> getPurchaseOrderApprovalLogCollection() {
        return purchaseOrderApprovalLogCollection;
    }

    public void setPurchaseOrderApprovalLogCollection(Collection<PurchaseOrderApprovalLog> purchaseOrderApprovalLogCollection) {
        this.purchaseOrderApprovalLogCollection = purchaseOrderApprovalLogCollection;
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
        if (!(object instanceof ApprovalExclusion)) {
            return false;
        }
        ApprovalExclusion other = (ApprovalExclusion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.ApprovalExclusion[ id=" + id + " ]";
    }
    
}
