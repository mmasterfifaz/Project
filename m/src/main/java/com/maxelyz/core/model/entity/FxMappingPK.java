/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author oat
 */
@Embeddable
public class FxMappingPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "table_name", nullable = false, length = 100)
    private String tableName;
    @Basic(optional = false)
    @Column(name = "fx_name", nullable = false, length = 100)
    private String fxName;

    public FxMappingPK() {
    }

    public FxMappingPK(String tableName, String fxName) {
        this.tableName = tableName;
        this.fxName = fxName;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tableName != null ? tableName.hashCode() : 0);
        hash += (fxName != null ? fxName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FxMappingPK)) {
            return false;
        }
        FxMappingPK other = (FxMappingPK) object;
        if ((this.tableName == null && other.tableName != null) || (this.tableName != null && !this.tableName.equals(other.tableName))) {
            return false;
        }
        if ((this.fxName == null && other.fxName != null) || (this.fxName != null && !this.fxName.equals(other.fxName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.FxMappingPK[ tableName=" + tableName + ", fxName=" + fxName + " ]";
    }
    
}
