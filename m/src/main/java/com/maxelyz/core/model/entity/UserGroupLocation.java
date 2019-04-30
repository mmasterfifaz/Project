/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author oat
 */
@Entity
@Table(name = "user_group_location")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserGroupLocation.findAll", query = "SELECT u FROM UserGroupLocation u")})
public class UserGroupLocation implements Serializable {
    @JoinColumn(name = "service_type_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ServiceType serviceType;
    @JoinColumn(name = "business_unit_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private BusinessUnit businessUnit;
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UserGroupLocationPK userGroupLocationPK;
    @Column(name = "remark", length = 1000)
    private String remark;
    @JoinColumn(name = "user_group_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UserGroup userGroup;
    @JoinColumn(name = "location_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Location location;

    public UserGroupLocation() {
    }

    public UserGroupLocation(UserGroupLocationPK userGroupLocationPK) {
        this.userGroupLocationPK = userGroupLocationPK;
    }

    public UserGroupLocation(int userGroupId, int serviceTypeId, int businessUnitId, int locationId) {
        this.userGroupLocationPK = new UserGroupLocationPK(userGroupId, serviceTypeId, businessUnitId, locationId);
    }

    public UserGroupLocationPK getUserGroupLocationPK() {
        return userGroupLocationPK;
    }

    public void setUserGroupLocationPK(UserGroupLocationPK userGroupLocationPK) {
        this.userGroupLocationPK = userGroupLocationPK;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userGroupLocationPK != null ? userGroupLocationPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserGroupLocation)) {
            return false;
        }
        UserGroupLocation other = (UserGroupLocation) object;
        if ((this.userGroupLocationPK == null && other.userGroupLocationPK != null) || (this.userGroupLocationPK != null && !this.userGroupLocationPK.equals(other.userGroupLocationPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.UserGroupLocation[ userGroupLocationPK=" + userGroupLocationPK + " ]";
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public BusinessUnit getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
    }
    
}
