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
 * @author Administrator
 */
@Embeddable
public class UserMemberPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "member_user_id")
    private int memberUserId;
    @Basic(optional = false)
    @Column(name = "user_id")
    private int userId;

    public UserMemberPK() {
    }

    public UserMemberPK(int memberUserId, int userId) {
        this.memberUserId = memberUserId;
        this.userId = userId;
    }

    public int getMemberUserId() {
        return memberUserId;
    }

    public void setMemberUserId(int memberUserId) {
        this.memberUserId = memberUserId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) memberUserId;
        hash += (int) userId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserMemberPK)) {
            return false;
        }
        UserMemberPK other = (UserMemberPK) object;
        if (this.memberUserId != other.memberUserId) {
            return false;
        }
        if (this.userId != other.userId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.UserMemberPK[ memberUserId=" + memberUserId + ", userId=" + userId + " ]";
    }
    
}
