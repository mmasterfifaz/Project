/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "news_user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NewsUser.findAll", query = "SELECT n FROM NewsUser n")})
public class NewsUser implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected NewsUserPK newsUserPK;
    @Column(name = "view_status")
    private Boolean viewStatus;
    @Column(name = "view_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date viewDate;
    @JoinColumn(name = "users_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Users users;
    @JoinColumn(name = "news_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private News news;

    public NewsUser() {
    }

    public NewsUser(NewsUserPK newsUserPK) {
        this.newsUserPK = newsUserPK;
    }

    public NewsUser(int newsId, int usersId) {
        this.newsUserPK = new NewsUserPK(newsId, usersId);
    }

    public NewsUserPK getNewsUserPK() {
        return newsUserPK;
    }

    public void setNewsUserPK(NewsUserPK newsUserPK) {
        this.newsUserPK = newsUserPK;
    }

    public Boolean getViewStatus() {
        return viewStatus;
    }

    public void setViewStatus(Boolean viewStatus) {
        this.viewStatus = viewStatus;
    }

    public Date getViewDate() {
        return viewDate;
    }

    public void setViewDate(Date viewDate) {
        this.viewDate = viewDate;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (newsUserPK != null ? newsUserPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NewsUser)) {
            return false;
        }
        NewsUser other = (NewsUser) object;
        if ((this.newsUserPK == null && other.newsUserPK != null) || (this.newsUserPK != null && !this.newsUserPK.equals(other.newsUserPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.NewsUser[ newsUserPK=" + newsUserPK + " ]";
    }
    
}
