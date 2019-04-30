/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author test01
 */
@Entity
@Table(name = "knowledgebase_vote")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KnowledgebaseVote.findAll", query = "SELECT k FROM KnowledgebaseVote k"),
    @NamedQuery(name = "KnowledgebaseVote.findByKnowledgebaseId", query = "SELECT k FROM KnowledgebaseVote k WHERE k.knowledgebaseVotePK.knowledgebaseId = :knowledgebaseId"),
    @NamedQuery(name = "KnowledgebaseVote.findByKnowledgebaseVersion", query = "SELECT k FROM KnowledgebaseVote k WHERE k.knowledgebaseVotePK.knowledgebaseVersion = :knowledgebaseVersion"),
    @NamedQuery(name = "KnowledgebaseVote.findByVote", query = "SELECT k FROM KnowledgebaseVote k WHERE k.vote = :vote"),
    @NamedQuery(name = "KnowledgebaseVote.findByUserId", query = "SELECT k FROM KnowledgebaseVote k WHERE k.knowledgebaseVotePK.userId = :userId"),
    @NamedQuery(name = "KnowledgebaseVote.findByCreateDate", query = "SELECT k FROM KnowledgebaseVote k WHERE k.createDate = :createDate"),
    @NamedQuery(name = "KnowledgebaseVote.findByCreateBy", query = "SELECT k FROM KnowledgebaseVote k WHERE k.createBy = :createBy")})
public class KnowledgebaseVote implements Serializable {
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "vote")
    private Long vote;
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected KnowledgebaseVotePK knowledgebaseVotePK;
    @Column(name = "create_by")
    private String createBy;

    public KnowledgebaseVote() {
    }

    public KnowledgebaseVote(KnowledgebaseVotePK knowledgebaseVotePK) {
        this.knowledgebaseVotePK = knowledgebaseVotePK;
    }

    public KnowledgebaseVote(int knowledgebaseId, String knowledgebaseVersion, int userId) {
        this.knowledgebaseVotePK = new KnowledgebaseVotePK(knowledgebaseId, knowledgebaseVersion, userId);
    }

    public KnowledgebaseVotePK getKnowledgebaseVotePK() {
        return knowledgebaseVotePK;
    }

    public void setKnowledgebaseVotePK(KnowledgebaseVotePK knowledgebaseVotePK) {
        this.knowledgebaseVotePK = knowledgebaseVotePK;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (knowledgebaseVotePK != null ? knowledgebaseVotePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KnowledgebaseVote)) {
            return false;
        }
        KnowledgebaseVote other = (KnowledgebaseVote) object;
        if ((this.knowledgebaseVotePK == null && other.knowledgebaseVotePK != null) || (this.knowledgebaseVotePK != null && !this.knowledgebaseVotePK.equals(other.knowledgebaseVotePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.KnowledgebaseVote[ knowledgebaseVotePK=" + knowledgebaseVotePK + " ]";
    }

    public Long getVote() {
        return vote;
    }

    public void setVote(Long vote) {
        this.vote = vote;
    }
 
}
