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
public class UserGroupAclPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "acl_code", nullable = false, length = 50)
    private String aclCode;
    @Basic(optional = false)
    @Column(name = "user_group_id", nullable = false)
    private Integer userGroupId;

    public UserGroupAclPK() {
    }

    public UserGroupAclPK(String aclCode, Integer userGroupId) {
        this.aclCode = aclCode;
        this.userGroupId = userGroupId;
    }

    public String getAclCode() {
        return aclCode;
    }

    public void setAclCode(String aclCode) {
        this.aclCode = aclCode;
    }

    public Integer getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Integer userGroupId) {
        this.userGroupId = userGroupId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aclCode != null ? aclCode.hashCode() : 0);
        hash += (int) userGroupId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserGroupAclPK)) {
            return false;
        }
        UserGroupAclPK other = (UserGroupAclPK) object;
        if ((this.aclCode == null && other.aclCode != null) || (this.aclCode != null && !this.aclCode.equals(other.aclCode))) {
            return false;
        }
        if (this.userGroupId != other.userGroupId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.UserGroupAclPK[ aclCode=" + aclCode + ", userGroupId=" + userGroupId + " ]";
    }
    
}
