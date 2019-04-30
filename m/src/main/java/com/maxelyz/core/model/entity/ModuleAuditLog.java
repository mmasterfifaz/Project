/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Nuttakarn
 */
@Entity
@Table(name = "module_audit_log")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ModuleAuditLog.findAll", query = "SELECT m FROM ModuleAuditLog m")})
public class ModuleAuditLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "module_name")
    private String moduleName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ref_no")
    private int refNo;
    @Size(max = 50)
    @Column(name = "action_type")
    private String actionType;
    @Column(name = "action_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actionDate;
    @Size(max = 50)
    @Column(name = "action_by")
    private String actionBy;
    @Column(name = "action_detail")
    private String actionDetail;

    public ModuleAuditLog() {
    }

    public ModuleAuditLog(Integer id) {
        this.id = id;
    }

    public ModuleAuditLog(Integer id, String moduleName, int refNo) {
        this.id = id;
        this.moduleName = moduleName;
        this.refNo = refNo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public int getRefNo() {
        return refNo;
    }

    public void setRefNo(int refNo) {
        this.refNo = refNo;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public String getActionBy() {
        return actionBy;
    }

    public void setActionBy(String actionBy) {
        this.actionBy = actionBy;
    }

    public String getActionDetail() {
        return actionDetail;
    }

    public void setActionDetail(String actionDetail) {
        this.actionDetail = actionDetail;
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
        if (!(object instanceof ModuleAuditLog)) {
            return false;
        }
        ModuleAuditLog other = (ModuleAuditLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.ModuleAuditLog[ id=" + id + " ]";
    }
    
}
