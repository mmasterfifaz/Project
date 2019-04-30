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
 * @author Oat
 */
@Embeddable
public class FileTemplateMappingPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "file_template_id")
    private int fileTemplateId;
    @Basic(optional = false)
    @Column(name = "file_column_no")
    private int fileColumnNo;

    public FileTemplateMappingPK() {
        
    }
    public FileTemplateMappingPK(int fileTemplateId, int fileColumnNo) {
        this.fileTemplateId = fileTemplateId;
        this.fileColumnNo = fileColumnNo;
    }

    public int getFileTemplateId() {
        return fileTemplateId;
    }

    public void setFileTemplateId(int fileTemplateId) {
        this.fileTemplateId = fileTemplateId;
    }

    public int getCustomerField() {
        return fileColumnNo;
    }

    public void setCustomerField(int fileColumnNo) {
        this.fileColumnNo = fileColumnNo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) fileTemplateId;
        hash += (int) fileColumnNo;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FileTemplateMappingPK)) {
            return false;
        }
        FileTemplateMappingPK other = (FileTemplateMappingPK) object;
        if (this.fileTemplateId != other.fileTemplateId) {
            return false;
        }
        if (this.fileColumnNo != other.fileColumnNo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.FileTemplateMappingPK[fileTemplateId=" + fileTemplateId + ", fileColumnNo=" + fileColumnNo + "]";
    }

 




}
