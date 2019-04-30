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
import javax.validation.constraints.NotNull;

/**
 *
 * @author Nuttakarn
 */
@Embeddable
public class ProductSponsorUserGroupPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "product_sponsor_id")
    private int productSponsorId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_group_id")
    private int userGroupId;

    public ProductSponsorUserGroupPK() {
    }

    public ProductSponsorUserGroupPK(int productSponsorId, int userGroupId) {
        this.productSponsorId = productSponsorId;
        this.userGroupId = userGroupId;
    }

    public int getProductSponsorId() {
        return productSponsorId;
    }

    public void setProductSponsorId(int productSponsorId) {
        this.productSponsorId = productSponsorId;
    }

    public int getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(int userGroupId) {
        this.userGroupId = userGroupId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) productSponsorId;
        hash += (int) userGroupId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductSponsorUserGroupPK)) {
            return false;
        }
        ProductSponsorUserGroupPK other = (ProductSponsorUserGroupPK) object;
        if (this.productSponsorId != other.productSponsorId) {
            return false;
        }
        if (this.userGroupId != other.userGroupId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.ProductSponsorUserGroupPK[ productSponsorId=" + productSponsorId + ", userGroupId=" + userGroupId + " ]";
    }
    
}
