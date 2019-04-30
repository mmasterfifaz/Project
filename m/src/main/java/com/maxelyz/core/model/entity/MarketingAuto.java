/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
 * @author support
 */
@Entity
@Table(name = "marketing_auto")
@NamedQueries({
    @NamedQuery(name = "MarketingAuto.findAll", query = "SELECT m FROM MarketingAuto m")})
public class MarketingAuto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
    @Column(name = "prefix_code")
    private String prefixCode;

    @JoinColumn(name = "prospectlist_sponsor_id", referencedColumnName = "id")
    @ManyToOne
    private ProspectlistSponsor prospectlistSponsorId;

    @JoinColumn(name = "file_template_id", referencedColumnName = "id")
    @ManyToOne
    private FileTemplate fileTemplateId;

    @Column(name = "description")
    private String description;
    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "enable")
    private boolean enable;
    @Column(name = "period_customer")
    private Integer periodCustomer;
    @Column(name = "period_blacklist")
    private Integer periodBlacklist;
    @Column(name = "check_format_tel_no")
    private boolean checkFormatTelNo;

    @Column(name = "dup_within")
    private boolean dupWithin;
    @Column(name = "dup_yes")
    private boolean dupYes;
    @Column(name = "dup_customer")
    private boolean dupCustomer;
    @Column(name = "dup_opout")
    private boolean dupOpout;
    @Column(name = "auto_dial")
    private boolean autoDial;

    @Column(name = "dup_type")
    private String dupType;

    @Column(name = "blacklist_criteria")
    private String blacklistCriteria;

    @Column(name = "renew_period")
    private String renewPeriod;

    @Column(name = "marketing_age")
    private Integer marketingAge;
    @Column(name = "current_marketing_id")
    private Integer currentMarketingId;
    @Column(name = "current_marketing_code")
    private String currentMarketingCode;

    @Column(name = "user_groups")
    private String userGroups;

//    @JoinTable(name = "marketing_user_group", joinColumns = {
//        @JoinColumn(name = "marketing_id", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
//        @JoinColumn(name = "user_group_id", referencedColumnName = "id", nullable = false)})
//    @ManyToMany
//    private Collection<UserGroup> userGroupCollection;

    @Column(name = "period_yessale")
    private Integer periodYessale;

    @Column(name = "products")
    private String products;

//    @JoinTable(name = "marketing_product", joinColumns = {
//        @JoinColumn(name = "marketing_id", referencedColumnName = "id")}, inverseJoinColumns = {
//        @JoinColumn(name = "product_id", referencedColumnName = "id")})
//    @ManyToMany
//    private Collection<Product> productCollection;

    @Column(name = "type")
    private Integer type;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrefixCode() {
        return prefixCode;
    }

    public void setPrefixCode(String prefixCode) {
        this.prefixCode = prefixCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRenewPeriod() {
        return renewPeriod;
    }

    public void setRenewPeriod(String renewPeriod) {
        this.renewPeriod = renewPeriod;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public ProspectlistSponsor getProspectlistSponsorId() {
        return prospectlistSponsorId;
    }

    public void setProspectlistSponsorId(ProspectlistSponsor prospectlistSponsorId) {
        this.prospectlistSponsorId = prospectlistSponsorId;
    }

    public Integer getPeriodCustomer() {
        return periodCustomer;
    }

    public void setPeriodCustomer(Integer periodCustomer) {
        this.periodCustomer = periodCustomer;
    }

    public String getDupType() {
        return dupType;
    }

    public void setDupType(String dupType) {
        this.dupType = dupType;
    }

    public Integer getPeriodBlacklist() {
        return periodBlacklist;
    }

    public void setPeriodBlacklist(Integer periodBlacklist) {
        this.periodBlacklist = periodBlacklist;
    }

    public Integer getPeriodYessale() {
        return periodYessale;
    }

    public void setPeriodYessale(Integer periodYessale) {
        this.periodYessale = periodYessale;
    }

    public FileTemplate getFileTemplateId() {
        return fileTemplateId;
    }

    public void setFileTemplateId(FileTemplate fileTemplateId) {
        this.fileTemplateId = fileTemplateId;
    }

    public boolean isCheckFormatTelNo() {
        return checkFormatTelNo;
    }

    public void setCheckFormatTelNo(boolean checkFormatTelNo) {
        this.checkFormatTelNo = checkFormatTelNo;
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

    public boolean isAutoDial() {
        return autoDial;
    }

    public void setAutoDial(boolean autoDial) {
        this.autoDial = autoDial;
    }

    public boolean isDupYes() {
        return dupYes;
    }

    public void setDupYes(boolean dupYes) {
        this.dupYes = dupYes;
    }

    public boolean isDupCustomer() {
        return dupCustomer;
    }

    public void setDupCustomer(boolean dupCustomer) {
        this.dupCustomer = dupCustomer;
    }

    public boolean isDupOpout() {
        return dupOpout;
    }

    public void setDupOpout(boolean dupOpout) {
        this.dupOpout = dupOpout;
    }

    public Integer getMarketingAge() {
        return marketingAge;
    }

    public void setMarketingAge(Integer marketingAge) {
        this.marketingAge = marketingAge;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getBlacklistCriteria() {
        return blacklistCriteria;
    }

    public void setBlacklistCriteria(String blacklistCriteria) {
        this.blacklistCriteria = blacklistCriteria;
    }

    public Integer getCurrentMarketingId() {
        return currentMarketingId;
    }

    public void setCurrentMarketingId(Integer currentMarketingId) {
        this.currentMarketingId = currentMarketingId;
    }

    public String getCurrentMarketingCode() {
        return currentMarketingCode;
    }

    public void setCurrentMarketingCode(String currentMarketingCode) {
        this.currentMarketingCode = currentMarketingCode;
    }

//    public Collection<UserGroup> getUserGroupCollection() {
//        return userGroupCollection;
//    }
//
//    public void setUserGroupCollection(Collection<UserGroup> userGroupCollection) {
//        this.userGroupCollection = userGroupCollection;
//    }
//
//    public Collection<Product> getProductCollection() {
//        return productCollection;
//    }
//
//    public void setProductCollection(Collection<Product> productCollection) {
//        this.productCollection = productCollection;
//    }

    public boolean isDupWithin() {
        return dupWithin;
    }

    public void setDupWithin(boolean dupWithin) {
        this.dupWithin = dupWithin;
    }

    public String getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(String userGroups) {
        this.userGroups = userGroups;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
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
        if (!(object instanceof MarketingAuto)) {
            return false;
        }
        MarketingAuto other = (MarketingAuto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.MarketingAuto[ id=" + id + " ]";
    }

}
