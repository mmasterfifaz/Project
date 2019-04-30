/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author oat
 */
@Entity
@Table(name = "fx_mapping")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FxMapping.findAll", query = "SELECT f FROM FxMapping f")})
public class FxMapping implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FxMappingPK fxMappingPK;
    @Column(name = "custom_name", length = 100)
    private String customName;

    public FxMapping() {
    }

    public FxMapping(FxMappingPK fxMappingPK) {
        this.fxMappingPK = fxMappingPK;
    }

    public FxMapping(String tableName, String fxName) {
        this.fxMappingPK = new FxMappingPK(tableName, fxName);
    }

    public FxMappingPK getFxMappingPK() {
        return fxMappingPK;
    }

    public void setFxMappingPK(FxMappingPK fxMappingPK) {
        this.fxMappingPK = fxMappingPK;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fxMappingPK != null ? fxMappingPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FxMapping)) {
            return false;
        }
        FxMapping other = (FxMapping) object;
        if ((this.fxMappingPK == null && other.fxMappingPK != null) || (this.fxMappingPK != null && !this.fxMappingPK.equals(other.fxMappingPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.FxMapping[ fxMappingPK=" + fxMappingPK + " ]";
    }
    
}
