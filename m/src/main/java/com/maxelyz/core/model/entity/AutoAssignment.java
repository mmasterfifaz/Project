/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "auto_assignment")
@NamedQueries({@NamedQuery(name = "AutoAssignment.findAll", query = "SELECT n FROM AutoAssignment n")})
public class AutoAssignment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "keyword")
    private String keyword;
    @Column(name = "priority")
    private Integer priority;
    @JoinColumn(name = "token_user_id", referencedColumnName = "id")
    @ManyToOne
    private Users tokenUser;
    @JoinColumn(name = "campaign_id", referencedColumnName = "id")
    @ManyToOne
    private Campaign campaign;
    @JoinColumn(name = "predefined_marketing_id", referencedColumnName = "id")
    @ManyToOne
    private Marketing predefinedMarketing;
    @Column(name = "auto_create")
    private Boolean autoCreate;
    @Column(name = "auto_create_type")
    private String autoCreateType;
    @Column(name = "auto_marketing_prefix")
    private String autoMarketingPrefix;
    @Column(name = "auto_marketing_name")
    private String autoMarketingName;
    @JoinColumn(name = "auto_marketing_id", referencedColumnName = "id")
    @ManyToOne
    private Marketing autoMarketing;
    @Column(name = "auto_effective_period")
    private Integer autoEffectivePeriod;
    @JoinColumn(name = "auto_prospectlist_sponsor_id", referencedColumnName = "id")
    @ManyToOne
    private ProspectlistSponsor autoProspectlistSponsor;
    @Column(name = "enable")
    private Boolean enable;
    @Column(name = "status")
    private Boolean status;
    @Column(name = "create_by")
    private String createBy;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "update_by")
    private String updateBy;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @JoinTable(name = "auto_assignment_detail", joinColumns = {@JoinColumn(name = "auto_assignment_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Users> agentsCollection;
    
    public AutoAssignment() {
    }

    public AutoAssignment(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public Boolean getAutoCreate() {
        return autoCreate;
    }

    public void setAutoCreate(Boolean autoCreate) {
        this.autoCreate = autoCreate;
    }

    public String getAutoCreateType() {
        return autoCreateType;
    }

    public void setAutoCreateType(String autoCreateType) {
        this.autoCreateType = autoCreateType;
    }

    public String getAutoMarketingName() {
        return autoMarketingName;
    }

    public void setAutoMarketingName(String autoMarketingName) {
        this.autoMarketingName = autoMarketingName;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getAutoMarketingPrefix() {
        return autoMarketingPrefix;
    }

    public void setAutoMarketingPrefix(String autoMarketingPrefix) {
        this.autoMarketingPrefix = autoMarketingPrefix;
    }

    public Marketing getPredefinedMarketing() {
        return predefinedMarketing;
    }

    public void setPredefinedMarketing(Marketing predefinedMarketing) {
        this.predefinedMarketing = predefinedMarketing;
    }

    public Marketing getAutoMarketing() {
        return autoMarketing;
    }

    public void setAutoMarketing(Marketing autoMarketing) {
        this.autoMarketing = autoMarketing;
    }

    public Integer getAutoEffectivePeriod() {
        return autoEffectivePeriod;
    }

    public void setAutoEffectivePeriod(Integer autoEffectivePeriod) {
        this.autoEffectivePeriod = autoEffectivePeriod;
    }

    public ProspectlistSponsor getAutoProspectlistSponsor() {
        return autoProspectlistSponsor;
    }

    public void setAutoProspectlistSponsor(ProspectlistSponsor autoProspectlistSponsor) {
        this.autoProspectlistSponsor = autoProspectlistSponsor;
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

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Collection<Users> getAgentsCollection() {
        return agentsCollection;
    }

    public void setAgentsCollection(Collection<Users> agentsCollection) {
        this.agentsCollection = agentsCollection;
    }

    public Users getTokenUser() {
        return tokenUser;
    }

    public void setTokenUser(Users tokenUser) {
        this.tokenUser = tokenUser;
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
        if (!(object instanceof AutoAssignment)) {
            return false;
        }
        AutoAssignment other = (AutoAssignment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.AutoAssignment[id=" + id + "]";
    }

}
