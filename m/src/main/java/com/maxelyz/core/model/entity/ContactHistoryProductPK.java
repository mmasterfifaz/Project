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
public class ContactHistoryProductPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "contact_history_id")
    private int contactHistoryId;
    @Basic(optional = false)
    @Column(name = "product_id")
    private int productId;

    public ContactHistoryProductPK() {
    }

    public ContactHistoryProductPK(int contactHistoryId, int productId) {
        this.contactHistoryId = contactHistoryId;
        this.productId = productId;
    }

    public int getContactHistoryId() {
        return contactHistoryId;
    }

    public void setContactHistoryId(int contactHistoryId) {
        this.contactHistoryId = contactHistoryId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) contactHistoryId;
        hash += (int) productId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContactHistoryProductPK)) {
            return false;
        }
        ContactHistoryProductPK other = (ContactHistoryProductPK) object;
        if (this.contactHistoryId != other.contactHistoryId) {
            return false;
        }
        if (this.productId != other.productId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.ContactHistoryProductPK[contactHistoryId=" + contactHistoryId + ", productId=" + productId + "]";
    }

}
