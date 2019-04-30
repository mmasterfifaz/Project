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
@Table(name = "knowledgebase_division")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KnowledgebaseDivision.findAll", query = "SELECT k FROM KnowledgebaseDivision k") })
public class KnowledgebaseDivision implements Serializable {
    @Column(name = "startdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startdate;
    @Column(name = "enddate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date enddate;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "approval")
    private Boolean approval;
    @Column(name = "approval_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approvalDate;
    @Column(name = "approval_by")
    private String approvalBy;
    @Column(name = "faq")
    private Boolean faq;
    @Column(name = "contentown")
    private Integer contentown;
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected KnowledgebaseDivisionPK knowledgebaseDivisionPK;
    @Column(name = "topic")
    private String topic;
    @Lob
    @Column(name = "description")
    private String description;
    @Column(name = "topic_level")
    private Boolean topicLevel;
    @Column(name = "ref_id")
    private Integer refId;
    @Column(name = "enable")
    private Boolean enable;
    @Column(name = "schedule")
    private Boolean schedule;
    @Column(name = "viewcount")
    private Integer viewcount;
    @Column(name = "create_by")
    private String createBy;
    @Column(name = "update_by")
    private String updateBy;
    

    public KnowledgebaseDivision() {
    }

    public KnowledgebaseDivision(KnowledgebaseDivisionPK knowledgebaseDivisionPK) {
        this.knowledgebaseDivisionPK = knowledgebaseDivisionPK;
    }

    public KnowledgebaseDivision(int knowledgebaseId, String version) {
        this.knowledgebaseDivisionPK = new KnowledgebaseDivisionPK(knowledgebaseId, version);
    }

    public KnowledgebaseDivisionPK getKnowledgebaseDivisionPK() {
        return knowledgebaseDivisionPK;
    }

    public void setKnowledgebaseDivisionPK(KnowledgebaseDivisionPK knowledgebaseDivisionPK) {
        this.knowledgebaseDivisionPK = knowledgebaseDivisionPK;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getTopicLevel() {
        return topicLevel;
    }

    public void setTopicLevel(Boolean topicLevel) {
        this.topicLevel = topicLevel;
    }

    public Integer getRefId() {
        return refId;
    }

    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Boolean getSchedule() {
        return schedule;
    }

    public void setSchedule(Boolean schedule) {
        this.schedule = schedule;
    }

    public Integer getViewcount() {
        return viewcount;
    }

    public void setViewcount(Integer viewcount) {
        this.viewcount = viewcount;
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

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (knowledgebaseDivisionPK != null ? knowledgebaseDivisionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KnowledgebaseDivision)) {
            return false;
        }
        KnowledgebaseDivision other = (KnowledgebaseDivision) object;
        if ((this.knowledgebaseDivisionPK == null && other.knowledgebaseDivisionPK != null) || (this.knowledgebaseDivisionPK != null && !this.knowledgebaseDivisionPK.equals(other.knowledgebaseDivisionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.KnowledgebaseDivision[ knowledgebaseDivisionPK=" + knowledgebaseDivisionPK + " ]";
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }
 

    public Boolean getApproval() {
        return approval;
    }

    public void setApproval(Boolean approval) {
        this.approval = approval;
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getApprovalBy() {
        return approvalBy;
    }

    public void setApprovalBy(String approvalBy) {
        this.approvalBy = approvalBy;
    }

    public Boolean getFaq() {
        return faq;
    }

    public void setFaq(Boolean faq) {
        this.faq = faq;
    }

    public Integer getContentown() {
        return contentown;
    }

    public void setContentown(Integer contentown) {
        this.contentown = contentown;
    }
    
}
