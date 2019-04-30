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
 * @author Oat
 */
@Embeddable
public class NewsUserPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "news_id", nullable = false)
    private int newsId;
    @Basic(optional = false)
    @Column(name = "users_id", nullable = false)
    private int usersId;

    public NewsUserPK() {
    }

    public NewsUserPK(int newsId, int usersId) {
        this.newsId = newsId;
        this.usersId = usersId;
    }

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public int getUsersId() {
        return usersId;
    }

    public void setUsersId(int usersId) {
        this.usersId = usersId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) newsId;
        hash += (int) usersId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NewsUserPK)) {
            return false;
        }
        NewsUserPK other = (NewsUserPK) object;
        if (this.newsId != other.newsId) {
            return false;
        }
        if (this.usersId != other.usersId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.NewsUserPK[ newsId=" + newsId + ", usersId=" + usersId + " ]";
    }
    
}
