/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "knowledgebase")
@NamedQueries({@NamedQuery(name = "Knowledgebase.findAll", query = "SELECT k FROM Knowledgebase k")})
public class Knowledgebase implements Serializable {
    
    @Column(name =     "startdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startdate;
    @Column(name =     "enddate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date enddate;
    @Column(name =     "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name =     "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name =     "approval_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approvalDate;
    @Column(name = "contentown")
    private Integer contentown;
    @Column(name = "faq")
    private Boolean faq;
    @Column(name = "approval_by")
    private String approvalBy;
    @Column(name = "approval")
    private Boolean approval;
    @Column(name =     "version")
    private String version;
    @Column(name =     "schedule")
    private Boolean schedule;
    @Column(name = "viewcount")
    private Integer viewcount;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "knowledgebase")
    private Collection<KnowledgebaseLocation> knowledgebaseLocationCollection;
    @OneToMany(mappedBy = "knowledgebase")
    private Collection<RptContactHistoryKnowledge> rptContactHistoryKnowledgeCollection;
    @OneToMany(mappedBy = "knowledgebase")
    private Collection<ContactHistoryKnowledge> contactHistoryKnowledgeCollection;
    @OneToMany(mappedBy = "knowledgebase")
    private Collection<Knowledgebase> knowledgebaseCollection;
    @JoinColumn(name = "ref_id", referencedColumnName = "id")
    @ManyToOne
    private Knowledgebase knowledgebase;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "topic")
    private String topic;
    @Lob
    @Column(name = "description")
    private String description;
    @Column(name = "topic_level")
    private Boolean topicLevel;
    @Column(name = "enable")
    private Boolean enable;
    @Column(name = "create_by")
    private String createBy;
    @Column(name = "update_by")
    private String updateBy;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "knowledgebase")
    private Collection<KnowledgebaseAttfile> knowledgebaseAttfile; 
    
    
    public Knowledgebase() {
    }

    public Knowledgebase(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
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

    public Collection<Knowledgebase> getKnowledgebaseCollection() {
        return knowledgebaseCollection;
    }

    public void setKnowledgebaseCollection(Collection<Knowledgebase> knowledgebaseCollection) {
        this.knowledgebaseCollection = knowledgebaseCollection;
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
        if (!(object instanceof Knowledgebase)) {
            return false;
        }
        Knowledgebase other = (Knowledgebase) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.Knowledgebase[id=" + id + "]";
    }

    public Collection<ContactHistoryKnowledge> getContactHistoryKnowledgeCollection() {
        return contactHistoryKnowledgeCollection;
    }

    public void setContactHistoryKnowledgeCollection(Collection<ContactHistoryKnowledge> contactHistoryKnowledgeCollection) {
        this.contactHistoryKnowledgeCollection = contactHistoryKnowledgeCollection;
    }


    public Knowledgebase getKnowledgebase() {
        return knowledgebase;
    }

    public void setKnowledgebase(Knowledgebase knowledgebase) {
        this.knowledgebase = knowledgebase;
    }

    public Collection<RptContactHistoryKnowledge> getRptContactHistoryKnowledgeCollection() {
        return rptContactHistoryKnowledgeCollection;
    }

    public void setRptContactHistoryKnowledgeCollection(Collection<RptContactHistoryKnowledge> rptContactHistoryKnowledgeCollection) {
        this.rptContactHistoryKnowledgeCollection = rptContactHistoryKnowledgeCollection;
    }


    @XmlTransient
    public Collection<KnowledgebaseLocation> getKnowledgebaseLocationCollection() {
        return knowledgebaseLocationCollection;
    }

    public void setKnowledgebaseLocationCollection(Collection<KnowledgebaseLocation> knowledgebaseLocationCollection) {
        this.knowledgebaseLocationCollection = knowledgebaseLocationCollection;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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


    public Integer getContentown() {
        return contentown;
    }

    public void setContentown(Integer contentown) {
        this.contentown = contentown;
    }

    public Collection<KnowledgebaseAttfile> getKnowledgebaseAttfile() {
        return knowledgebaseAttfile;
    }

    public void setKnowledgebaseAttfile(Collection<KnowledgebaseAttfile> knowledgebaseAttfile) {
        this.knowledgebaseAttfile = knowledgebaseAttfile;
    }

   

}
