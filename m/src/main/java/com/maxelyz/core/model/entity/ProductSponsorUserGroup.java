/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Nuttakarn
 */
@Entity
@Table(name = "product_sponsor_user_group")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductSponsorUserGroup.findAll", query = "SELECT p FROM ProductSponsorUserGroup p"),
    @NamedQuery(name = "ProductSponsorUserGroup.findByProductSponsorId", query = "SELECT p FROM ProductSponsorUserGroup p WHERE p.productSponsorUserGroupPK.productSponsorId = :productSponsorId"),
    @NamedQuery(name = "ProductSponsorUserGroup.findByUserGroupId", query = "SELECT p FROM ProductSponsorUserGroup p WHERE p.productSponsorUserGroupPK.userGroupId = :userGroupId")})
public class ProductSponsorUserGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProductSponsorUserGroupPK productSponsorUserGroupPK;

    public ProductSponsorUserGroup() {
    }

    public ProductSponsorUserGroup(ProductSponsorUserGroupPK productSponsorUserGroupPK) {
        this.productSponsorUserGroupPK = productSponsorUserGroupPK;
    }

    public ProductSponsorUserGroup(int productSponsorId, int userGroupId) {
        this.productSponsorUserGroupPK = new ProductSponsorUserGroupPK(productSponsorId, userGroupId);
    }

    public ProductSponsorUserGroupPK getProductSponsorUserGroupPK() {
        return productSponsorUserGroupPK;
    }

    public void setProductSponsorUserGroupPK(ProductSponsorUserGroupPK productSponsorUserGroupPK) {
        this.productSponsorUserGroupPK = productSponsorUserGroupPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productSponsorUserGroupPK != null ? productSponsorUserGroupPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductSponsorUserGroup)) {
            return false;
        }
        ProductSponsorUserGroup other = (ProductSponsorUserGroup) object;
        if ((this.productSponsorUserGroupPK == null && other.productSponsorUserGroupPK != null) || (this.productSponsorUserGroupPK != null && !this.productSponsorUserGroupPK.equals(other.productSponsorUserGroupPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.ProductSponsorUserGroup[ productSponsorUserGroupPK=" + productSponsorUserGroupPK + " ]";
    }
    
}
