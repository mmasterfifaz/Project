/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Oat
 */
@Embeddable
public class RptContactHistoryProductPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "contact_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date contactDate;
    @Basic(optional = false)
    @Column(name = "product_id")
    private int productId;

    public RptContactHistoryProductPK() {
    }

    public RptContactHistoryProductPK(Date contactDate, int productId) {
        this.contactDate = contactDate;
        this.productId = productId;
    }

    public Date getContactDate() {
        return contactDate;
    }

    public void setContactDate(Date contactDate) {
        this.contactDate = contactDate;
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
        hash += (contactDate != null ? contactDate.hashCode() : 0);
        hash += (int) productId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RptContactHistoryProductPK)) {
            return false;
        }
        RptContactHistoryProductPK other = (RptContactHistoryProductPK) object;
        if ((this.contactDate == null && other.contactDate != null) || (this.contactDate != null && !this.contactDate.equals(other.contactDate))) {
            return false;
        }
        if (this.productId != other.productId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.RptContactHistoryProductPK[contactDate=" + contactDate + ", productId=" + productId + "]";
    }

}
