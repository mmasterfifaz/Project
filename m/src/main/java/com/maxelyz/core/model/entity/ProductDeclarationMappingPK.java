/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author User
 */
@Embeddable
public class ProductDeclarationMappingPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "product_id", nullable = false, length = 100)
    private Integer productId;
    @Basic(optional = false)
    @Column(name = "declaration_form_id", nullable = false, length = 100)
    private Integer declarationFormId;

    public ProductDeclarationMappingPK() {
    }

    public ProductDeclarationMappingPK(Integer productId, Integer declarationFormId) {
        this.productId = productId;
        this.declarationFormId = declarationFormId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getDeclarationFormId() {
        return declarationFormId;
    }

    public void setDeclarationFormId(Integer declarationFormId) {
        this.declarationFormId = declarationFormId;
    }
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productId != null ? productId.hashCode() : 0);
        hash += (declarationFormId != null ? declarationFormId.hashCode() : 0);
        return hash;
    }
    
        @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductDeclarationMappingPK)) {
            return false;
        }
        ProductDeclarationMappingPK other = (ProductDeclarationMappingPK) object;
        if ((this.productId == null && other.productId != null) || (this.productId != null && !this.productId.equals(other.productId))) {
            return false;
        }
        if ((this.declarationFormId == null && other.declarationFormId != null) || (this.declarationFormId != null && !this.declarationFormId.equals(other.declarationFormId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.ProductDeclarationMappingPK[ productId=" + productId + ", declarationFormId=" + declarationFormId + " ]";
    }
    
    
}
