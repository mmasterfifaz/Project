/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "file_template")
@NamedQueries({@NamedQuery(name = "FileTemplate.findAll", query = "SELECT f FROM FileTemplate f")})
public class FileTemplate implements Serializable {
    @OneToMany(mappedBy = "fileTemplate")
    private Collection<Marketing> marketingCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Lob
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "field_enclosed")
    private String fieldEnclosed;
    @Column(name = "field_enclosed_custom")
    private String fieldEnclosedCustom;
    @Basic(optional = false)
    @Column(name = "field_delimiter")
    private String fieldDelimiter;
    @Column(name = "field_delimiter_custom")
    private String fieldDelimiterCustom;
    @Column(name = "row_delimiter")
    private String rowDelimiter;
    @Column(name = "row_delimiter_custom")
    private String rowDelimiterCustom;
    @Column(name = "has_column_name")
    private Boolean hasColumnName;
    @Column(name = "unicode")
    private Boolean unicode;
    @Column(name = "enable")
    private Boolean enable;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "create_by")
    private String createBy;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "update_by")
    private String updateBy;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fileTemplate")
    private Collection<FileTemplateMapping> fileTemplateMappingCollection;
    @JoinColumn(name = "customer_layout_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private CustomerLayout customerLayout;
    
    public FileTemplate() {
    }

    public FileTemplate(Integer id) {
        this.id = id;
    }

    public FileTemplate(Integer id, String name, String fieldEnclosed, String fieldDelimiter, String rowDelimiter) {
        this.id = id;
        this.name = name;
        this.fieldEnclosed = fieldEnclosed;
        this.fieldDelimiter = fieldDelimiter;
        this.rowDelimiter = rowDelimiter;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFieldEnclosed() {
        return fieldEnclosed;
    }

    public void setFieldEnclosed(String fieldEnclosed) {
        this.fieldEnclosed = fieldEnclosed;
    }

    public String getFieldEnclosedCustom() {
        return fieldEnclosedCustom;
    }

    public void setFieldEnclosedCustom(String fieldEnclosedCustom) {
        this.fieldEnclosedCustom = fieldEnclosedCustom;
    }

    public String getFieldDelimiter() {
        return fieldDelimiter;
    }

    public void setFieldDelimiter(String fieldDelimiter) {
        this.fieldDelimiter = fieldDelimiter;
    }

    public String getFieldDelimiterCustom() {
        return fieldDelimiterCustom;
    }

    public void setFieldDelimiterCustom(String fieldDelimiterCustom) {
        this.fieldDelimiterCustom = fieldDelimiterCustom;
    }

    public String getRowDelimiter() {
        return rowDelimiter;
    }

    public void setRowDelimiter(String rowDelimiter) {
        this.rowDelimiter = rowDelimiter;
    }

    public String getRowDelimiterCustom() {
        return rowDelimiterCustom;
    }

    public void setRowDelimiterCustom(String rowDelimiterCustom) {
        this.rowDelimiterCustom = rowDelimiterCustom;
    }

    public Boolean getHasColumnName() {
        return hasColumnName;
    }

    public void setHasColumnName(Boolean hasColumnName) {
        this.hasColumnName = hasColumnName;
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

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Collection<FileTemplateMapping> getFileTemplateMappingCollection() {
        return fileTemplateMappingCollection;
    }

    public void setFileTemplateMappingCollection(Collection<FileTemplateMapping> fileTemplateMappingCollection) {
        this.fileTemplateMappingCollection = fileTemplateMappingCollection;
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
        if (!(object instanceof FileTemplate)) {
            return false;
        }
        FileTemplate other = (FileTemplate) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.FileTemplate[id=" + id + "]";
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Boolean getUnicode() {
        return unicode;
    }

    public void setUnicode(Boolean unicode) {
        this.unicode = unicode;
    }

    public Collection<Marketing> getMarketingCollection() {
        return marketingCollection;
    }

    public void setMarketingCollection(Collection<Marketing> marketingCollection) {
        this.marketingCollection = marketingCollection;
    }

    public CustomerLayout getCustomerLayout() {
        return customerLayout;
    }

    public void setCustomerLayout(CustomerLayout customerLayout) {
        this.customerLayout = customerLayout;
    }


}
