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
 * @author Songwisit
 */
@Embeddable
public class ProductChildRegTypePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "product_id")
    private int productId;
    @Basic(optional = false)
    @Column(name = "child_reg_type_id")
    private int childRegTypeId;

    public ProductChildRegTypePK() {
        
    }
    public ProductChildRegTypePK(int productId, int childRegTypeId) {
        this.productId = productId;
        this.childRegTypeId = childRegTypeId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getChildRegTypeId() {
        return childRegTypeId;
    }

    public void setChildRegTypeId(int childRegTypeId) {
        this.childRegTypeId = childRegTypeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) productId;
        hash += (int) childRegTypeId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductChildRegTypePK)) {
            return false;
        }
        ProductChildRegTypePK other = (ProductChildRegTypePK) object;
        if (this.productId != other.productId) {
            return false;
        }
        if (this.childRegTypeId != other.childRegTypeId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.ProductChildRegTypePK[productId=" + productId + ", childRegTypeId=" + childRegTypeId + "]";
    }

}
