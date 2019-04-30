/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author User
 */
@Entity
@Table(name = "declaration_form")
@NamedQueries({
    @NamedQuery(name = "DeclarationForm.findAll", query = "SELECT d FROM DeclarationForm d")})
public class DeclarationForm implements Serializable  {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Lob
    @Column(name = "description")
    private String description;
    @Column(name = "enable")
    private Boolean enable;
    @Column(name = "merge_file")
    private String merge_file;
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
    @Column(name = "view_name")
    private String viewName;
//    @OneToMany(mappedBy = "declarationForm")
//    private Collection<Product> productCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "declarationForm")
    private Collection<DeclarationField> declarationFieldCollection;
    
    public DeclarationForm() {
    }

    public DeclarationForm(Integer id) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getMerge_file() {
        return merge_file;
    }

    public void setMerge_file(String merge_file) {
        this.merge_file = merge_file;
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

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }
    
    

//    public Collection<Product> getProductCollection() {
//        return productCollection;
//    }
//
//    public void setProductCollection(Collection<Product> productCollection) {
//        this.productCollection = productCollection;
//    }
//
    public Collection<DeclarationField> getDeclarationFieldCollection() {
        return declarationFieldCollection;
    }

    public void setDeclarationFieldCollection(Collection<DeclarationField> declarationFieldCollection) {
        this.declarationFieldCollection = declarationFieldCollection;
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
        if (!(object instanceof DeclarationForm)) {
            return false;
        }
        DeclarationForm other = (DeclarationForm) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.DeclarationForm[id=" + id + "]";
    }
    
    
}
