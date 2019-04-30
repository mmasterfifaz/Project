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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "customer_audit_log")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CustomerAuditLog.findAll", query = "SELECT c FROM CustomerAuditLog c")})
public class CustomerAuditLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "field_name", nullable = false, length = 100)
    private String fieldName;
    @Basic(optional = false)
    @Column(name = "old_value", nullable = false, length = 2000)
    private String oldValue;
    @Basic(optional = false)
    @Column(name = "new_value", nullable = false, length = 2000)
    private String newValue;
    @Basic(optional = false)
    @Column(name = "create_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Basic(optional = false)
    @Column(name = "create_by", nullable = false, length = 100)
    private String createBy;
    @Basic(optional = false)
    @Column(name = "customer_id", nullable = false)
    private int customerId;

    public CustomerAuditLog() {
    }

    public CustomerAuditLog(Integer id) {
        this.id = id;
    }

    public CustomerAuditLog(Integer id, String fieldName, String oldValue, String newValue, Date createDate, String createBy, int customerId) {
        this.id = id;
        this.fieldName = fieldName;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.createDate = createDate;
        this.createBy = createBy;
        this.customerId = customerId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
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

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
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
        if (!(object instanceof CustomerAuditLog)) {
            return false;
        }
        CustomerAuditLog other = (CustomerAuditLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.CustomerAuditLog[ id=" + id + " ]";
    }
    
}
