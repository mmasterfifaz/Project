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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "auto_assignment_config")
@NamedQueries({@NamedQuery(name = "AutoAssignmentConfig.findAll", query = "SELECT n FROM AutoAssignmentConfig n")})
public class AutoAssignmentConfig implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "user_quota")
    private Integer userQuota;
    @Basic(optional = false)
    @Column(name = "check_duplicate")
    private Boolean checkDuplicate;
    @Basic(optional = false)
    @Column(name = "duplicate_period")
    private Integer duplicatePeriod;
    @Column(name = "update_by", length = 50)
    private String updateBy;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    public AutoAssignmentConfig() {
    }

    public AutoAssignmentConfig(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserQuota() {
        return userQuota;
    }

    public void setUserQuota(Integer userQuota) {
        this.userQuota = userQuota;
    }

    public Boolean getCheckDuplicate() {
        return checkDuplicate;
    }

    public void setCheckDuplicate(Boolean checkDuplicate) {
        this.checkDuplicate = checkDuplicate;
    }

    public Integer getDuplicatePeriod() {
        return duplicatePeriod;
    }

    public void setDuplicatePeriod(Integer duplicatePeriod) {
        this.duplicatePeriod = duplicatePeriod;
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

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AutoAssignmentConfig)) {
            return false;
        }
        AutoAssignmentConfig other = (AutoAssignmentConfig) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.AutoAssignmentConfig[id=" + id + "]";
    }

}
