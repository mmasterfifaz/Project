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
public class MarketingCustomerPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "marketing_id")
    private int marketingId;
    @Basic(optional = false)
    @Column(name = "customer_id")
    private int customerId;


    public MarketingCustomerPK() {
        
    }
    public MarketingCustomerPK(int marketingId, int customerId) {
        this.marketingId = marketingId;
        this.customerId = customerId;
    }

    public int getMarketingId() {
        return marketingId;
    }

    public void setMarketingId(int marketingId) {
        this.marketingId = marketingId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) marketingId;
        hash += (int) customerId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MarketingCustomerPK)) {
            return false;
        }
        MarketingCustomerPK other = (MarketingCustomerPK) object;
        if (this.marketingId != other.marketingId) {
            return false;
        }
        if (this.customerId != other.customerId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.MarketingCustomerPK[marketingId=" + marketingId + ", customerId=" + customerId + "]";
    }

}
