/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "file_template_mapping")
@NamedQueries({@NamedQuery(name = "FileTemplateMapping.findAll", query = "SELECT f FROM FileTemplateMapping f")})
public class FileTemplateMapping implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FileTemplateMappingPK fileTemplateMappingPK;
    @Basic(optional = false)
    @Column(name = "file_column_no", insertable = false, updatable = false)
    private int fileColumnNo;
    @Basic(optional = false)
    @Column(name = "file_column_name")
    private String fileColumnName;
    @JoinColumn(name = "customer_field", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CustomerField customerField;
    @JoinColumn(name = "file_template_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private FileTemplate fileTemplate;
    @Column(name = "field_type")
    private String fieldType;
    @Column(name = "pattern")
    private String pattern;
    @Column(name = "default_value")
    private String defaultValue;
    @Column(name = "confidential")
    private Boolean confidential;

    public FileTemplateMapping() {
    }

    public FileTemplateMapping(FileTemplateMappingPK fileTemplateMappingPK) {
        this.fileTemplateMappingPK = fileTemplateMappingPK;
    }

    public FileTemplateMapping(FileTemplateMappingPK fileTemplateMappingPK, CustomerField customerField) {
        this.fileTemplateMappingPK = fileTemplateMappingPK;
        this.customerField = customerField;
    }

    public FileTemplateMapping(int fileTemplateId, int fileColumnNo) {
        this.fileTemplateMappingPK = new FileTemplateMappingPK(fileTemplateId, fileColumnNo);
    }

    public FileTemplateMappingPK getFileTemplateMappingPK() {
        return fileTemplateMappingPK;
    }

    public void setFileTemplateMappingPK(FileTemplateMappingPK fileTemplateMappingPK) {
        this.fileTemplateMappingPK = fileTemplateMappingPK;
    }

    public int getFileColumnNo() {
        return fileColumnNo;
    }

    public void setFileColumnNo(int fileColumnNo) {
        this.fileColumnNo = fileColumnNo;
    }

    public CustomerField getCustomerField() {
        return customerField;
    }

    public void setCustomerField(CustomerField customerField) {
        this.customerField = customerField;
    }

    public FileTemplate getFileTemplate() {
        return fileTemplate;
    }

    public void setFileTemplate(FileTemplate fileTemplate) {
        this.fileTemplate = fileTemplate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fileTemplateMappingPK != null ? fileTemplateMappingPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FileTemplateMapping)) {
            return false;
        }
        FileTemplateMapping other = (FileTemplateMapping) object;
        if ((this.fileTemplateMappingPK == null && other.fileTemplateMappingPK != null) || (this.fileTemplateMappingPK != null && !this.fileTemplateMappingPK.equals(other.fileTemplateMappingPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.FileTemplateMapping[fileTemplateMappingPK=" + fileTemplateMappingPK + "]";
    }

    public String getFileColumnName() {
        return fileColumnName;
    }

    public void setFileColumnName(String fileColumnName) {
        this.fileColumnName = fileColumnName;
    }

    public String getFieldType() {
        return fieldType;
}

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Boolean getConfidential() {
        return confidential;
    }

    public void setConfidential(Boolean confidential) {
        this.confidential = confidential;
    }
}
