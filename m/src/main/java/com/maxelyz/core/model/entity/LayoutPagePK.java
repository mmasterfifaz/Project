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
public class LayoutPagePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "page_name", nullable = false, length = 100)
    private String pageName;
    @Basic(optional = false)
    @Column(name = "field_name", nullable = false, length = 100)
    private String fieldName;

    public LayoutPagePK() {
    }

    public LayoutPagePK(String pageName, String fieldName) {
        this.pageName = pageName;
        this.fieldName = fieldName;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pageName != null ? pageName.hashCode() : 0);
        hash += (fieldName != null ? fieldName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LayoutPagePK)) {
            return false;
        }
        LayoutPagePK other = (LayoutPagePK) object;
        if ((this.pageName == null && other.pageName != null) || (this.pageName != null && !this.pageName.equals(other.pageName))) {
            return false;
        }
        if ((this.fieldName == null && other.fieldName != null) || (this.fieldName != null && !this.fieldName.equals(other.fieldName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.LayoutPagePK[ pageName=" + pageName + ", fieldName=" + fieldName + " ]";
    }
    
}
