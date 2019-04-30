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
public class RptSalePerformancePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "sale_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date saleDate;
    @Basic(optional = false)
    @Column(name = "user_id")
    private int userId;
    @Basic(optional = false)
    @Column(name = "campaign_id")
    private int campaignId;
    @Basic(optional = false)
    @Column(name = "marketing_id")
    private int marketingId;

    public RptSalePerformancePK() {
    }

    public RptSalePerformancePK(Date saleDate, int userId, int campaignId, int marketingId) {
        this.saleDate = saleDate;
        this.userId = userId;
        this.campaignId = campaignId;
        this.marketingId = marketingId;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public int getMarketingId() {
        return marketingId;
    }

    public void setMarketingId(int marketingId) {
        this.marketingId = marketingId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (saleDate != null ? saleDate.hashCode() : 0);
        hash += (int) userId;
        hash += (int) campaignId;
        hash += (int) marketingId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RptSalePerformancePK)) {
            return false;
        }
        RptSalePerformancePK other = (RptSalePerformancePK) object;
        if ((this.saleDate == null && other.saleDate != null) || (this.saleDate != null && !this.saleDate.equals(other.saleDate))) {
            return false;
        }
        if (this.userId != other.userId) {
            return false;
        }
        if (this.campaignId != other.campaignId) {
            return false;
        }
        if (this.marketingId != other.marketingId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.RptSalePerformancePK[saleDate=" + saleDate + ", userId=" + userId + ", campaignId=" + campaignId + ", marketingId=" + marketingId + "]";
    }

}
