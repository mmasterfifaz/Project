/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author apichatt
 */
@Entity
@Table(name = "so_rpt_post")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SoRptPost.findAll", query = "SELECT s FROM SoRptPost s"),
    @NamedQuery(name = "SoRptPost.findById", query = "SELECT s FROM SoRptPost s WHERE s.id = :id"),
    @NamedQuery(name = "SoRptPost.findByTransDate", query = "SELECT s FROM SoRptPost s WHERE s.transDate = :transDate"),
    @NamedQuery(name = "SoRptPost.findByUserId", query = "SELECT s FROM SoRptPost s WHERE s.userId = :userId"),
    @NamedQuery(name = "SoRptPost.findByUserGroupId", query = "SELECT s FROM SoRptPost s WHERE s.userGroupId = :userGroupId"),
    @NamedQuery(name = "SoRptPost.findByChannelId", query = "SELECT s FROM SoRptPost s WHERE s.channelId = :channelId"),
    @NamedQuery(name = "SoRptPost.findByChannelName", query = "SELECT s FROM SoRptPost s WHERE s.channelName = :channelName"),
    @NamedQuery(name = "SoRptPost.findBySoAccountName", query = "SELECT s FROM SoRptPost s WHERE s.soAccountName = :soAccountName"),
    @NamedQuery(name = "SoRptPost.findBySoCasetypeId", query = "SELECT s FROM SoRptPost s WHERE s.soCasetypeId = :soCasetypeId"),
    @NamedQuery(name = "SoRptPost.findBySoCasetypeName", query = "SELECT s FROM SoRptPost s WHERE s.soCasetypeName = :soCasetypeName"),
    @NamedQuery(name = "SoRptPost.findByReply", query = "SELECT s FROM SoRptPost s WHERE s.reply = :reply"),
    @NamedQuery(name = "SoRptPost.findByIgnore", query = "SELECT s FROM SoRptPost s WHERE s.ignore = :ignore"),
    @NamedQuery(name = "SoRptPost.findByPendingPost", query = "SELECT s FROM SoRptPost s WHERE s.pendingPost = :pendingPost"),
    @NamedQuery(name = "SoRptPost.findByClosedPost", query = "SELECT s FROM SoRptPost s WHERE s.closedPost = :closedPost"),
    @NamedQuery(name = "SoRptPost.findByTotalPost", query = "SELECT s FROM SoRptPost s WHERE s.totalPost = :totalPost"),
    @NamedQuery(name = "SoRptPost.findByNegative", query = "SELECT s FROM SoRptPost s WHERE s.negative = :negative"),
    @NamedQuery(name = "SoRptPost.findByNeutral", query = "SELECT s FROM SoRptPost s WHERE s.neutral = :neutral"),
    @NamedQuery(name = "SoRptPost.findByPositive", query = "SELECT s FROM SoRptPost s WHERE s.positive = :positive")})
public class SoRptPost implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "trans_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transDate;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "user_group_id")
    private Integer userGroupId;
    @Basic(optional = false)
    @Column(name = "channel_id")
    private int channelId;
    @Column(name = "channel_name")
    private String channelName;
    @Basic(optional = false)
    @Column(name = "so_account_id")
    private int soAccountId;
    @Column(name = "so_account_name")
    private String soAccountName;
    @Column(name = "so_casetype_id")
    private Integer soCasetypeId;
    @Column(name = "so_casetype_name")
    private String soCasetypeName;
    @Column(name = "reply")
    private Integer reply;
    @Column(name = "ignore")
    private Integer ignore;
    @Column(name = "pending_post")
    private Integer pendingPost;
    @Column(name = "closed_post")
    private Integer closedPost;
    @Column(name = "total_post")
    private Integer totalPost;
    @Column(name = "negative")
    private Integer negative;
    @Column(name = "neutral")
    private Integer neutral;
    @Column(name = "positive")
    private Integer positive;

    public SoRptPost() {
    }

    public SoRptPost(Integer id) {
        this.id = id;
    }

    public SoRptPost(Integer id, Date transDate, int channelId) {
        this.id = id;
        this.transDate = transDate;
        this.channelId = channelId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getTransDate() {
        return transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Integer userGroupId) {
        this.userGroupId = userGroupId;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public int getSoAccountId() {
        return soAccountId;
    }

    public void setSoAccountId(int soAccountId) {
        this.soAccountId = soAccountId;
    }

    public String getSoAccountName() {
        return soAccountName;
    }

    public void setSoAccountName(String soAccountName) {
        this.soAccountName = soAccountName;
    }

    public Integer getSoCasetypeId() {
        return soCasetypeId;
    }

    public void setSoCasetypeId(Integer soCasetypeId) {
        this.soCasetypeId = soCasetypeId;
    }

    public String getSoCasetypeName() {
        return soCasetypeName;
    }

    public void setSoCasetypeName(String soCasetypeName) {
        this.soCasetypeName = soCasetypeName;
    }

    public Integer getReply() {
        return reply;
    }

    public void setReply(Integer reply) {
        this.reply = reply;
    }

    public Integer getIgnore() {
        return ignore;
    }

    public void setIgnore(Integer ignore) {
        this.ignore = ignore;
    }

    public Integer getPendingPost() {
        return pendingPost;
    }

    public void setPendingPost(Integer pendingPost) {
        this.pendingPost = pendingPost;
    }

    public Integer getClosedPost() {
        return closedPost;
    }

    public void setClosedPost(Integer closedPost) {
        this.closedPost = closedPost;
    }

    public Integer getTotalPost() {
        return totalPost;
    }

    public void setTotalPost(Integer totalPost) {
        this.totalPost = totalPost;
    }

    public Integer getNegative() {
        return negative;
    }

    public void setNegative(Integer negative) {
        this.negative = negative;
    }

    public Integer getNeutral() {
        return neutral;
    }

    public void setNeutral(Integer neutral) {
        this.neutral = neutral;
    }

    public Integer getPositive() {
        return positive;
    }

    public void setPositive(Integer positive) {
        this.positive = positive;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SoRptPost)) {
            return false;
        }
        SoRptPost other = (SoRptPost) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.SoRptPost[ id=" + id + " ]";
    }
    
}
