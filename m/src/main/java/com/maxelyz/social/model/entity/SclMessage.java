/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author apichatt
 */
@Entity
@Table(name = "scl_message")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SclMessage.findAll", query = "SELECT s FROM SclMessage s"),
    @NamedQuery(name = "SclMessage.findById", query = "SELECT s FROM SclMessage s WHERE s.id = :id"),
    @NamedQuery(name = "SclMessage.findByRelatedId", query = "SELECT s FROM SclMessage s WHERE s.relatedId = :relatedId"),
//    @NamedQuery(name = "SclMessage.findByUserId", query = "SELECT s FROM SclMessage s WHERE s.userId = :userId"),
    @NamedQuery(name = "SclMessage.findByUserName", query = "SELECT s FROM SclMessage s WHERE s.userName = :userName"),
    @NamedQuery(name = "SclMessage.findByContents", query = "SELECT s FROM SclMessage s WHERE s.contents = :contents"),
    @NamedQuery(name = "SclMessage.findBySource", query = "SELECT s FROM SclMessage s WHERE s.source = :source"),
    @NamedQuery(name = "SclMessage.findBySourceName", query = "SELECT s FROM SclMessage s WHERE s.sourceName = :sourceName"),
    @NamedQuery(name = "SclMessage.findBySourceType", query = "SELECT s FROM SclMessage s WHERE s.sourceType = :sourceType"),
    @NamedQuery(name = "SclMessage.findByActivityTime", query = "SELECT s FROM SclMessage s WHERE s.activityTime = :activityTime"),
    @NamedQuery(name = "SclMessage.findByCollectTime", query = "SELECT s FROM SclMessage s WHERE s.collectTime = :collectTime"),
    @NamedQuery(name = "SclMessage.findByActivityTimeFormat", query = "SELECT s FROM SclMessage s WHERE s.activityTimeFormat = :activityTimeFormat"),
    @NamedQuery(name = "SclMessage.findByCollectTimeFormat", query = "SELECT s FROM SclMessage s WHERE s.collectTimeFormat = :collectTimeFormat"),
    @NamedQuery(name = "SclMessage.findByTransId", query = "SELECT s FROM SclMessage s WHERE s.transId = :transId")})
public class SclMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "related_id")
    private BigInteger relatedId;
//    @Column(name = "user_id")
//    private BigInteger userId;

    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne
    private SclUsers sclUsers;

    @Column(name = "user_name")
    private String userName;
    @Column(name = "contents")
    private String contents;
    @Column(name = "source")
    private String source;
    @Column(name = "source_name")
    private String sourceName;
    @Column(name = "source_type")
    private String sourceType;
    @Column(name = "activity_time")
    private Integer activityTime;
    @Column(name = "collect_time")
    private Integer collectTime;
    @Column(name = "activity_time_format")
    @Temporal(TemporalType.TIMESTAMP)
    private Date activityTimeFormat;
    @Column(name = "collect_time_format")
    @Temporal(TemporalType.TIMESTAMP)
    private Date collectTimeFormat;
    @Column(name = "trans_id")
    private String transId;

    public SclMessage() {
    }

    public SclMessage(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigInteger getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(BigInteger relatedId) {
        this.relatedId = relatedId;
    }

//    public BigInteger getUserId() {
//        return userId;
//    }
//
//    public void setUserId(BigInteger userId) {
//        this.userId = userId;
//    }

    public SclUsers getSclUsers() {
        return sclUsers;
    }

    public void setSclUsers(SclUsers sclUsers) {
        this.sclUsers = sclUsers;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Integer getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(Integer activityTime) {
        this.activityTime = activityTime;
    }

    public Integer getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Integer collectTime) {
        this.collectTime = collectTime;
    }

    public Date getActivityTimeFormat() {
        return activityTimeFormat;
    }

    public void setActivityTimeFormat(Date activityTimeFormat) {
        this.activityTimeFormat = activityTimeFormat;
    }

    public Date getCollectTimeFormat() {
        return collectTimeFormat;
    }

    public void setCollectTimeFormat(Date collectTimeFormat) {
        this.collectTimeFormat = collectTimeFormat;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
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
        if (!(object instanceof SclMessage)) {
            return false;
        }
        SclMessage other = (SclMessage) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.SclMessage[ id=" + id + " ]";
    }
    
}
