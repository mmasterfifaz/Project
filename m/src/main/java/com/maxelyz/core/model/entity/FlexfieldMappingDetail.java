/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "flexfield_mapping_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FlexfieldMappingDetail.findAll", query = "SELECT f FROM FlexfieldMappingDetail f"),
    @NamedQuery(name = "FlexfieldMappingDetail.findById", query = "SELECT f FROM FlexfieldMappingDetail f WHERE f.id = :id"),
    @NamedQuery(name = "FlexfieldMappingDetail.findByTableName", query = "SELECT f FROM FlexfieldMappingDetail f WHERE f.tableName = :tableName"),
    @NamedQuery(name = "FlexfieldMappingDetail.findByFxName", query = "SELECT f FROM FlexfieldMappingDetail f WHERE f.fxName = :fxName"),
    @NamedQuery(name = "FlexfieldMappingDetail.findByCustomName", query = "SELECT f FROM FlexfieldMappingDetail f WHERE f.customName = :customName"),
    @NamedQuery(name = "FlexfieldMappingDetail.findByFlexfieldMappingId", query = "SELECT f FROM FlexfieldMappingDetail f WHERE f.flexfieldMappingId = :flexfieldMappingId")})
public class FlexfieldMappingDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "table_name")
    private String tableName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "fx_name")
    private String fxName;
    @Size(max = 100)
    @Column(name = "custom_name")
    private String customName;
    @Column(name = "flexfield_mapping_id")
    private Integer flexfieldMappingId;

    public FlexfieldMappingDetail() {
    }

    public FlexfieldMappingDetail(Integer id) {
        this.id = id;
    }

    public FlexfieldMappingDetail(Integer id, String tableName, String fxName) {
        this.id = id;
        this.tableName = tableName;
        this.fxName = fxName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getFxName() {
        return fxName;
    }

    public void setFxName(String fxName) {
        this.fxName = fxName;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public Integer getFlexfieldMappingId() {
        return flexfieldMappingId;
    }

    public void setFlexfieldMappingId(Integer flexfieldMappingId) {
        this.flexfieldMappingId = flexfieldMappingId;
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
        if (!(object instanceof FlexfieldMappingDetail)) {
            return false;
        }
        FlexfieldMappingDetail other = (FlexfieldMappingDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.FlexfieldMappingDetail[ id=" + id + " ]";
    }
    
}
