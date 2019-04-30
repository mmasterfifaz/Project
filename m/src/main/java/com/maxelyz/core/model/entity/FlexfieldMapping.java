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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "flexfield_mapping")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FlexfieldMapping.findAll", query = "SELECT f FROM FlexfieldMapping f"),
    @NamedQuery(name = "FlexfieldMapping.findById", query = "SELECT f FROM FlexfieldMapping f WHERE f.id = :id"),
    @NamedQuery(name = "FlexfieldMapping.findByName", query = "SELECT f FROM FlexfieldMapping f WHERE f.name = :name"),
    @NamedQuery(name = "FlexfieldMapping.findByEnable", query = "SELECT f FROM FlexfieldMapping f WHERE f.enable = :enable"),
    @NamedQuery(name = "FlexfieldMapping.findByCreateBy", query = "SELECT f FROM FlexfieldMapping f WHERE f.createBy = :createBy"),
    @NamedQuery(name = "FlexfieldMapping.findByCreateDate", query = "SELECT f FROM FlexfieldMapping f WHERE f.createDate = :createDate"),
    @NamedQuery(name = "FlexfieldMapping.findByModifyBy", query = "SELECT f FROM FlexfieldMapping f WHERE f.modifyBy = :modifyBy"),
    @NamedQuery(name = "FlexfieldMapping.findByModifyDate", query = "SELECT f FROM FlexfieldMapping f WHERE f.modifyDate = :modifyDate")})
public class FlexfieldMapping implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 100)
    @Column(name = "name")
    private String name;
    @Column(name = "enable")
    private Boolean enable;
    @Size(max = 50)
    @Column(name = "create_by")
    private String createBy;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Size(max = 50)
    @Column(name = "modify_by")
    private String modifyBy;
    @Column(name = "modify_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyDate;

    public FlexfieldMapping() {
    }

    public FlexfieldMapping(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
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
        if (!(object instanceof FlexfieldMapping)) {
            return false;
        }
        FlexfieldMapping other = (FlexfieldMapping) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.FlexfieldMapping[ id=" + id + " ]";
    }
    
}
