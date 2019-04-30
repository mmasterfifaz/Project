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
@Table(name = "marketing_user_group")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MarketingUserGroup.findAll", query = "SELECT m FROM MarketingUserGroup m"),
    @NamedQuery(name = "MarketingUserGroup.findByMarketingId", query = "SELECT m FROM MarketingUserGroup m WHERE m.marketingUserGroupPK.marketingId = :marketingId"),
    @NamedQuery(name = "MarketingUserGroup.findByUserGroupId", query = "SELECT m FROM MarketingUserGroup m WHERE m.marketingUserGroupPK.userGroupId = :userGroupId")})
public class MarketingUserGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MarketingUserGroupPK marketingUserGroupPK;

    public MarketingUserGroup() {
    }

    public MarketingUserGroup(MarketingUserGroupPK marketingUserGroupPK) {
        this.marketingUserGroupPK = marketingUserGroupPK;
    }

    public MarketingUserGroup(int marketingId, int userGroupId) {
        this.marketingUserGroupPK = new MarketingUserGroupPK(marketingId, userGroupId);
    }

    public MarketingUserGroupPK getMarketingUserGroupPK() {
        return marketingUserGroupPK;
    }

    public void setMarketingUserGroupPK(MarketingUserGroupPK marketingUserGroupPK) {
        this.marketingUserGroupPK = marketingUserGroupPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (marketingUserGroupPK != null ? marketingUserGroupPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MarketingUserGroup)) {
            return false;
        }
        MarketingUserGroup other = (MarketingUserGroup) object;
        if ((this.marketingUserGroupPK == null && other.marketingUserGroupPK != null) || (this.marketingUserGroupPK != null && !this.marketingUserGroupPK.equals(other.marketingUserGroupPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.MarketingUserGroup[ marketingUserGroupPK=" + marketingUserGroupPK + " ]";
    }
    
}
