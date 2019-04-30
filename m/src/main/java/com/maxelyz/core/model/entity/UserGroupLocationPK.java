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
public class UserGroupLocationPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "user_group_id", nullable = false)
    private int userGroupId;
    @Basic(optional = false)
    @Column(name = "service_type_id", nullable = false)
    private int serviceTypeId;
    @Basic(optional = false)
    @Column(name = "business_unit_id", nullable = false)
    private int businessUnitId;
    @Basic(optional = false)
    @Column(name = "location_id", nullable = false)
    private int locationId;

    public UserGroupLocationPK() {
    }

    public UserGroupLocationPK(int userGroupId, int serviceTypeId, int businessUnitId, int locationId) {
        this.userGroupId = userGroupId;
        this.serviceTypeId = serviceTypeId;
        this.businessUnitId = businessUnitId;
        this.locationId = locationId;
    }

    public int getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(int userGroupId) {
        this.userGroupId = userGroupId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(int businessUnitId) {
        this.businessUnitId = businessUnitId;
    }

    public int getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(int serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) userGroupId;
        hash += (int) locationId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserGroupLocationPK)) {
            return false;
        }
        UserGroupLocationPK other = (UserGroupLocationPK) object;
        if (this.userGroupId != other.userGroupId) {
            return false;
        }
        if (this.locationId != other.locationId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.UserGroupLocationPK[ userGroupId=" + userGroupId + ", locationId=" + locationId + " ]";
    }
    
}
