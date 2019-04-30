/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "case_detail")
@NamedQueries({
    @NamedQuery(name = "CaseDetail.findAll", query = "SELECT c FROM CaseDetail c")})
public class CaseDetail implements Serializable {
    @Column(name = "sla_closecase_low")
    private Double slaClosecaseLow;
    @Column(name = "sla_closecase_medium")
    private Double slaClosecaseMedium;
    @Column(name = "sla_closecase_high")
    private Double slaClosecaseHigh;
    @Column(name = "sla_closecase_immediate")
    private Double slaClosecaseImmediate;
    @Column(name = "sla_acceptcase_low")
    private Double slaAcceptcaseLow;
    @Column(name = "sla_acceptcase_medium")
    private Double slaAcceptcaseMedium;
    @Column(name = "sla_acceptcase_high")
    private Double slaAcceptcaseHigh;
    @Column(name = "sla_acceptcase_immediate")
    private Double slaAcceptcaseImmediate;
    @Column(name = "default_priority")
    private String defaultPriority;
    @Column(name = "to_user_name")
    private String toUserName;
    @Column(name = "to_user_email")
    private String toUserEmail;
    @Column(name = "to_user_telephone")
    private String toUserTelephone;
    @JoinColumn(name = "to_user_id", referencedColumnName = "id")
    @ManyToOne
    private Users toUsers;
 /*
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "caseDetail")
    private Collection<RptContactChannel> rptContactChannelCollection;

  */
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "code")
    private String code;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "enable")
    private Boolean enable;
    @Column(name = "status")
    private Boolean status;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "create_by")
    private String createBy;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "update_by")
    private String updateBy;
    /*
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "caseDetailId")
    private Collection<ContactCase> contactCaseCollection;
    */
     @JoinColumn(name = "case_topic_id", referencedColumnName = "id")
    @ManyToOne
    private CaseTopic caseTopicId;
    @OneToMany(mappedBy = "caseDetailId")
    private Collection<CaseRequest> caseRequestCollection;

    public CaseDetail() {
    }

    public CaseDetail(Integer id) {
        this.id = id;
    }

    public CaseDetail(Integer id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
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
/*
    public Collection<ContactCase> getContactCaseCollection() {
        return contactCaseCollection;
    }

    public void setContactCaseCollection(Collection<ContactCase> contactCaseCollection) {
        this.contactCaseCollection = contactCaseCollection;
    }
*/
    public CaseTopic getCaseTopicId() {
        return caseTopicId;
    }

    public void setCaseTopicId(CaseTopic caseTopicId) {
        this.caseTopicId = caseTopicId;
    }

    public Collection<CaseRequest> getCaseRequestCollection() {
        return caseRequestCollection;
    }

    public void setCaseRequestCollection(Collection<CaseRequest> caseRequestCollection) {
        this.caseRequestCollection = caseRequestCollection;
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
        if (!(object instanceof CaseDetail)) {
            return false;
        }
        CaseDetail other = (CaseDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.CaseDetail[id=" + id + "]";
    }

/*
    public Collection<RptContactChannel> getRptContactChannelCollection() {
        return rptContactChannelCollection;
    }

    public void setRptContactChannelCollection(Collection<RptContactChannel> rptContactChannelCollection) {
        this.rptContactChannelCollection = rptContactChannelCollection;
    }
*/
    public Double getSlaClosecaseLow() {
        return slaClosecaseLow;
    }

    public void setSlaClosecaseLow(Double slaClosecaseLow) {
        this.slaClosecaseLow = slaClosecaseLow;
    }

    public Double getSlaClosecaseMedium() {
        return slaClosecaseMedium;
    }

    public void setSlaClosecaseMedium(Double slaClosecaseMedium) {
        this.slaClosecaseMedium = slaClosecaseMedium;
    }

    public Double getSlaClosecaseHigh() {
        return slaClosecaseHigh;
    }

    public void setSlaClosecaseHigh(Double slaClosecaseHigh) {
        this.slaClosecaseHigh = slaClosecaseHigh;
    }

    public Double getSlaAcceptcaseLow() {
        return slaAcceptcaseLow;
    }

    public void setSlaAcceptcaseLow(Double slaAcceptcaseLow) {
        this.slaAcceptcaseLow = slaAcceptcaseLow;
    }

    public Double getSlaAcceptcaseMedium() {
        return slaAcceptcaseMedium;
    }

    public void setSlaAcceptcaseMedium(Double slaAcceptcaseMedium) {
        this.slaAcceptcaseMedium = slaAcceptcaseMedium;
    }

    public Double getSlaAcceptcaseHigh() {
        return slaAcceptcaseHigh;
    }

    public void setSlaAcceptcaseHigh(Double slaAcceptcaseHigh) {
        this.slaAcceptcaseHigh = slaAcceptcaseHigh;
    }

    public String getDefaultPriority() {
        return defaultPriority;
    }

    public void setDefaultPriority(String defaultPriority) {
        this.defaultPriority = defaultPriority;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getToUserEmail() {
        return toUserEmail;
    }

    public void setToUserEmail(String toUserEmail) {
        this.toUserEmail = toUserEmail;
    }

    public String getToUserTelephone() {
        return toUserTelephone;
    }

    public void setToUserTelephone(String toUserTelephone) {
        this.toUserTelephone = toUserTelephone;
    }

    public Users getToUsers() {
        return toUsers;
    }

    public void setToUsers(Users toUsers) {
        this.toUsers = toUsers;
    }

    public Double getSlaAcceptcaseImmediate() {
        return slaAcceptcaseImmediate;
    }

    public void setSlaAcceptcaseImmediate(Double slaAcceptcaseImmediate) {
        this.slaAcceptcaseImmediate = slaAcceptcaseImmediate;
    }

    public Double getSlaClosecaseImmediate() {
        return slaClosecaseImmediate;
    }

    public void setSlaClosecaseImmediate(Double slaClosecaseImmediate) {
        this.slaClosecaseImmediate = slaClosecaseImmediate;
    }

}
