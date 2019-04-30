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
public class MarketingUserGroupPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "marketing_id")
    private int marketingId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_group_id")
    private int userGroupId;

    public MarketingUserGroupPK() {
    }

    public MarketingUserGroupPK(int marketingId, int userGroupId) {
        this.marketingId = marketingId;
        this.userGroupId = userGroupId;
    }

    public int getMarketingId() {
        return marketingId;
    }

    public void setMarketingId(int marketingId) {
        this.marketingId = marketingId;
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
        hash += (int) marketingId;
        hash += (int) userGroupId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MarketingUserGroupPK)) {
            return false;
        }
        MarketingUserGroupPK other = (MarketingUserGroupPK) object;
        if (this.marketingId != other.marketingId) {
            return false;
        }
        if (this.userGroupId != other.userGroupId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.MarketingUserGroupPK[ marketingId=" + marketingId + ", userGroupId=" + userGroupId + " ]";
    }
    
}
