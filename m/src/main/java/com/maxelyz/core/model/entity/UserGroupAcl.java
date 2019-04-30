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
@Table(name = "user_group_acl")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserGroupAcl.findAll", query = "SELECT u FROM UserGroupAcl u")})
public class UserGroupAcl implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UserGroupAclPK userGroupAclPK;
    @Column(name = "remark", length = 1000)
    private String remark;
    @JoinColumn(name = "user_group_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UserGroup userGroup;
    @JoinColumn(name = "acl_code", referencedColumnName = "code", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Acl acl;

    public UserGroupAcl() {
    }

    public UserGroupAcl(UserGroupAclPK userGroupAclPK) {
        this.userGroupAclPK = userGroupAclPK;
    }

    public UserGroupAcl(String aclCode, Integer userGroupId) {
        this.userGroupAclPK = new UserGroupAclPK(aclCode, userGroupId);
    }

    public UserGroupAclPK getUserGroupAclPK() {
        return userGroupAclPK;
    }

    public void setUserGroupAclPK(UserGroupAclPK userGroupAclPK) {
        this.userGroupAclPK = userGroupAclPK;
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

    public Acl getAcl() {
        return acl;
    }

    public void setAcl(Acl acl) {
        this.acl = acl;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userGroupAclPK != null ? userGroupAclPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserGroupAcl)) {
            return false;
        }
        UserGroupAcl other = (UserGroupAcl) object;
        if ((this.userGroupAclPK == null && other.userGroupAclPK != null) || (this.userGroupAclPK != null && !this.userGroupAclPK.equals(other.userGroupAclPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.UserGroupAcl[ userGroupAclPK=" + userGroupAclPK + " ]";
    }
    
}
