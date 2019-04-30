/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "user_member")
@NamedQueries({
    @NamedQuery(name = "UserMember.findAll", query = "SELECT u FROM UserMember u")})
public class UserMember implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UserMemberPK userMemberPK;
    
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Users users;
    @JoinColumn(name = "member_user_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Users usersMember;
    
    public UserMember() {
    }

    public UserMember(UserMemberPK userMemberPK) {
        this.userMemberPK = userMemberPK;
    }

    public UserMember(int memberUserId, int userId) {
        this.userMemberPK = new UserMemberPK(memberUserId, userId);
    }

    public UserMemberPK getUserMemberPK() {
        return userMemberPK;
    }

    public void setUserMemberPK(UserMemberPK userMemberPK) {
        this.userMemberPK = userMemberPK;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Users getUsersMember() {
        return usersMember;
    }

    public void setUsersMember(Users usersMember) {
        this.usersMember = usersMember;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userMemberPK != null ? userMemberPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserMember)) {
            return false;
        }
        UserMember other = (UserMember) object;
        if ((this.userMemberPK == null && other.userMemberPK != null) || (this.userMemberPK != null && !this.userMemberPK.equals(other.userMemberPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.UserMember[ userMemberPK=" + userMemberPK + " ]";
    }
    
}

