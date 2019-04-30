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
 * @author oat
 */
@Embeddable
public class CampaignCustomerPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "campaign_id", nullable = false)
    private int campaignId;
    @Basic(optional = false)
    @Column(name = "customer_id", nullable = false)
    private int customerId;

    public CampaignCustomerPK() {
    }

    public CampaignCustomerPK(int campaignId, int customerId) {
        this.campaignId = campaignId;
        this.customerId = customerId;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
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
        hash += (int) campaignId;
        hash += (int) customerId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CampaignCustomerPK)) {
            return false;
        }
        CampaignCustomerPK other = (CampaignCustomerPK) object;
        if (this.campaignId != other.campaignId) {
            return false;
        }
        if (this.customerId != other.customerId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.CampaignCustomerPK[ campaignId=" + campaignId + ", customerId=" + customerId + " ]";
    }
    
}
