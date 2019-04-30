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
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "marketing")
@NamedQueries({@NamedQuery(name = "Marketing.findAll", query = "SELECT m FROM Marketing m")})
public class Marketing implements Serializable {
    /*
    @Column(name = "prospectlist_sponsor_id")
    private Integer prospectlistSponsorId;
    @Column(name = "file_template_id")
    private Integer fileTemplateId;
    @Column(name = "dup_within")
    private Boolean dupWithin;
    @Column(name = "dup_yes")
    private Boolean dupYes;
    @Column(name = "dup_customer")
    private Boolean dupCustomer;
    @Column(name = "dup_opout")
    private Boolean dupOpout;
    @Size(max = 200)
    @Column(name = "blacklist_criteria", length = 200)
    private String blacklistCriteria;
    @Size(max = 10)
    @Column(name = "auto_gen_period", length = 10)
    private String autoGenPeriod;
    @Column(name = "marketing_age")
    private Integer marketingAge;
    @Column(name = "customer_layout_id")
    private Integer customerLayoutId;
    @Column(name = "flexfield_template_id")
    private Integer flexfieldTemplateId;
    @Column(name = "sms_alert_template_id")
    private Integer smsAlertTemplateId;
    */
    @JoinTable(name = "marketing_user_group", joinColumns = {
        @JoinColumn(name = "marketing_id", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "user_group_id", referencedColumnName = "id", nullable = false)})
    @ManyToMany
    private Collection<UserGroup> userGroupCollection;
    @JoinTable(name = "marketing_product", joinColumns = {
        @JoinColumn(name = "marketing_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "product_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Product> productCollection;
    /*
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "marketing1")
    private Marketing marketing;
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false)
    @OneToOne(optional = false)
    private Marketing marketing1;
    */
    @JoinColumn(name = "prospectlist_sponsor_id", referencedColumnName = "id")
    @ManyToOne
    private ProspectlistSponsor prospectlistSponsor;
    @JoinColumn(name = "file_template_id", referencedColumnName = "id")
    @ManyToOne
    private FileTemplate fileTemplate;
    @JoinColumn(name = "campaign_id", referencedColumnName = "id")
    @ManyToOne
    private Campaign campaign;
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "marketing")
    private Collection<MarketingCustomer> marketingCustomerCollection;
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
    @Basic(optional = false)
    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Basic(optional = false)
    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Lob
    @Column(name = "description")
    private String description;
    @Column(name = "filename")
    private String filename;
    @Column(name = "total_temp_record")
    private int totalTempRecord;
    @Basic(optional = false)
    @Column(name = "total_record")
    private int totalRecord;
    @Column(name = "total_assigned")
    private Integer totalAssigned;
    @Column(name = "assigned_no")
    private Integer assignedNo;
    @Column(name = "assigned_latest_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date assignedLatestDate;
    @Column(name = "datasource")
    private String datasource;
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
    @OneToMany(mappedBy = "marketing")
    private Collection<Assignment> assignmentCollection;
    @OneToMany(mappedBy = "marketing")
    private Collection<MarketingCriteria> marketingCriteriaCollection;
    @Column(name = "period_customer")
    private Integer periodCustomer;
    @Column(name = "period_blacklist")
    private Integer periodBlacklist;
    @Column(name = "period_yessale")
    private Integer periodYessale;
    @Column(name = "check_format_tel_no")
    private Boolean checkFormatTelNo;
    //@Column(name = "inbound")
    //private Boolean inbound;
    @Column(name = "auto_dial")
    private Boolean autoDial;
    @Column(name = "dup_type")
    private String dupType;
    @Column(name = "auto_gen_period")
    private String autoGeneratePeriod;
    @Column(name = "fx1")
    private String fx1;
    @Column(name = "fx2")
    private String fx2;
    @Column(name = "fx3")
    private String fx3;
    @Column(name = "fx4")
    private String fx4;
    @Column(name = "fx5")
    private String fx5;
    @Column(name = "fx6")
    private String fx6;
    @Column(name = "fx7")
    private String fx7;
    @Column(name = "fx8")
    private String fx8;
    @Column(name = "fx9")
    private String fx9;
    @Column(name = "fx10")
    private String fx10;
    @Column(name = "fx11")
    private String fx11;
    @Column(name = "fx12")
    private String fx12;
    @Column(name = "fx13")
    private String fx13;
    @Column(name = "fx14")
    private String fx14;
    @Column(name = "fx15")
    private String fx15;
    @Column(name = "fx16")
    private String fx16;
    @Column(name = "fx17")
    private String fx17;
    @Column(name = "fx18")
    private String fx18;
    @Column(name = "fx19")
    private String fx19;
    @Column(name = "fx20")
    private String fx20;
    @Column(name = "is_link_product")
    private Boolean isLinkProduct;

    public Marketing() {
    }

    public Marketing(Integer id) {
        this.id = id;
    }

    public Marketing(Integer id, String code, String name, Date startDate, Date endDate, int totalRecord) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalRecord = totalRecord;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public Date getAssignedLatestDate() {
        return assignedLatestDate;
    }

    public void setAssignedLatestDate(Date assignedLatestDate) {
        this.assignedLatestDate = assignedLatestDate;
    }

    public Integer getAssignedNo() {
        return assignedNo;
    }

    public void setAssignedNo(Integer assignedNo) {
        this.assignedNo = assignedNo;
    }

    public Integer getTotalAssigned() {
        return totalAssigned;
    }

    public void setTotalAssigned(Integer totalAssigned) {
        this.totalAssigned = totalAssigned;
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

    public Collection<MarketingCustomer> getMarketingCustomerCollection() {
        return marketingCustomerCollection;
    }

    public void setMarketingCustomerCollection(Collection<MarketingCustomer> marketingCustomerCollection) {
        this.marketingCustomerCollection = marketingCustomerCollection;
    }

    public Collection<Assignment> getAssignmentCollection() {
        return assignmentCollection;
    }

    public void setAssignmentCollection(Collection<Assignment> assignmentCollection) {
        this.assignmentCollection = assignmentCollection;
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
        if (!(object instanceof Marketing)) {
            return false;
        }
        Marketing other = (Marketing) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.Marketing[id=" + id + "]";
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public ProspectlistSponsor getProspectlistSponsor() {
        return prospectlistSponsor;
    }

    public void setProspectlistSponsor(ProspectlistSponsor prospectlistSponsor) {
        this.prospectlistSponsor = prospectlistSponsor;
    }

    public FileTemplate getFileTemplate() {
        return fileTemplate;
    }

    public void setFileTemplate(FileTemplate fileTemplate) {
        this.fileTemplate = fileTemplate;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Collection<MarketingCriteria> getMarketingCriteriaCollection() {
        return marketingCriteriaCollection;
    }

    public void setMarketingCriteriaCollection(Collection<MarketingCriteria> marketingCriteriaCollection) {
        this.marketingCriteriaCollection = marketingCriteriaCollection;
    }

    public int getTotalTempRecord() {
        return totalTempRecord;
    }

    public void setTotalTempRecord(int totalTempRecord) {
        this.totalTempRecord = totalTempRecord;
    }

    public Integer getPeriodCustomer() {
        return periodCustomer;
}

    public void setPeriodCustomer(Integer periodCustomer) {
        this.periodCustomer = periodCustomer;
    }

    public Integer getPeriodBlacklist() {
        return periodBlacklist;
    }

    public void setPeriodBlacklist(Integer periodBlacklist) {
        this.periodBlacklist = periodBlacklist;
    }

    public Boolean getCheckFormatTelNo() {
        return checkFormatTelNo;
    }

    public void setCheckFormatTelNo(Boolean checkFormatTelNo) {
        this.checkFormatTelNo = checkFormatTelNo;
    }

//    public Boolean getInbound() {
//        return inbound;
//    }
//
//    public void setInbound(Boolean inbound) {
//        this.inbound = inbound;
//    }

    public Boolean getAutoDial() {
        return autoDial;
    }

    public void setAutoDial(Boolean autoDial) {
        this.autoDial = autoDial;
    }

    public String getDupType() {
        return dupType;
    }

    public void setDupType(String dupType) {
        this.dupType = dupType;
    }
/*
    public Integer getProspectlistSponsorId() {
        return prospectlistSponsorId;
    }

    public void setProspectlistSponsorId(Integer prospectlistSponsorId) {
        this.prospectlistSponsorId = prospectlistSponsorId;
    }

    public Integer getFileTemplateId() {
        return fileTemplateId;
    }

    public void setFileTemplateId(Integer fileTemplateId) {
        this.fileTemplateId = fileTemplateId;
    }

    public Boolean getDupWithin() {
        return dupWithin;
    }

    public void setDupWithin(Boolean dupWithin) {
        this.dupWithin = dupWithin;
    }

    public Boolean getDupYes() {
        return dupYes;
    }

    public void setDupYes(Boolean dupYes) {
        this.dupYes = dupYes;
    }

    public Boolean getDupCustomer() {
        return dupCustomer;
    }

    public void setDupCustomer(Boolean dupCustomer) {
        this.dupCustomer = dupCustomer;
    }

    public Boolean getDupOpout() {
        return dupOpout;
    }

    public void setDupOpout(Boolean dupOpout) {
        this.dupOpout = dupOpout;
    }

    public String getBlacklistCriteria() {
        return blacklistCriteria;
    }

    public void setBlacklistCriteria(String blacklistCriteria) {
        this.blacklistCriteria = blacklistCriteria;
    }

    public String getAutoGenPeriod() {
        return autoGenPeriod;
    }

    public void setAutoGenPeriod(String autoGenPeriod) {
        this.autoGenPeriod = autoGenPeriod;
    }

    public Integer getMarketingAge() {
        return marketingAge;
    }

    public void setMarketingAge(Integer marketingAge) {
        this.marketingAge = marketingAge;
    }

    public Integer getCustomerLayoutId() {
        return customerLayoutId;
    }

    public void setCustomerLayoutId(Integer customerLayoutId) {
        this.customerLayoutId = customerLayoutId;
    }

    public Integer getFlexfieldTemplateId() {
        return flexfieldTemplateId;
    }

    public void setFlexfieldTemplateId(Integer flexfieldTemplateId) {
        this.flexfieldTemplateId = flexfieldTemplateId;
    }

    public Integer getSmsAlertTemplateId() {
        return smsAlertTemplateId;
    }

    public void setSmsAlertTemplateId(Integer smsAlertTemplateId) {
        this.smsAlertTemplateId = smsAlertTemplateId;
    }
*/
    public Collection<UserGroup> getUserGroupCollection() {
        return userGroupCollection;
    }

    public void setUserGroupCollection(Collection<UserGroup> userGroupCollection) {
        this.userGroupCollection = userGroupCollection;
    }
    
    public Collection<Product> getProductCollection() {
        return productCollection;
    }

    public void setProductCollection(Collection<Product> productCollection) {
        this.productCollection = productCollection;
    }
/*
    public Marketing getMarketing() {
        return marketing;
    }

    public void setMarketing(Marketing marketing) {
        this.marketing = marketing;
    }

    public Marketing getMarketing1() {
        return marketing1;
    }

    public void setMarketing1(Marketing marketing1) {
        this.marketing1 = marketing1;
    }
 */
    public String getAutoGeneratePeriod() {
        return autoGeneratePeriod;
    }

    public void setAutoGeneratePeriod(String autoGeneratePeriod) {   
        this.autoGeneratePeriod = autoGeneratePeriod;
    }
    
    public String getFx1() {
        return fx1;
    }

    public void setFx1(String fx1) {
        this.fx1 = fx1;
    }

    public String getFx2() {
        return fx2;
    }

    public void setFx2(String fx2) {
        this.fx2 = fx2;
    }

    public String getFx3() {
        return fx3;
    }

    public void setFx3(String fx3) {
        this.fx3 = fx3;
    }

    public String getFx4() {
        return fx4;
    }

    public void setFx4(String fx4) {
        this.fx4 = fx4;
    }

    public String getFx5() {
        return fx5;
    }

    public void setFx5(String fx5) {
        this.fx5 = fx5;
    }

    public String getFx6() {
        return fx6;
    }

    public void setFx6(String fx6) {
        this.fx6 = fx6;
    }

    public String getFx7() {
        return fx7;
    }

    public void setFx7(String fx7) {
        this.fx7 = fx7;
    }

    public String getFx8() {
        return fx8;
    }

    public void setFx8(String fx8) {
        this.fx8 = fx8;
    }

    public String getFx9() {
        return fx9;
    }

    public void setFx9(String fx9) {
        this.fx9 = fx9;
    }

    public String getFx10() {
        return fx10;
    }

    public void setFx10(String fx10) {
        this.fx10 = fx10;
    }

    public String getFx11() {
        return fx11;
    }

    public void setFx11(String fx11) {
        this.fx11 = fx11;
    }

    public String getFx12() {
        return fx12;
    }

    public void setFx12(String fx12) {
        this.fx12 = fx12;
    }

    public String getFx13() {
        return fx13;
    }

    public void setFx13(String fx13) {
        this.fx13 = fx13;
    }

    public String getFx14() {
        return fx14;
    }

    public void setFx14(String fx14) {
        this.fx14 = fx14;
    }

    public String getFx15() {
        return fx15;
    }

    public void setFx15(String fx15) {
        this.fx15 = fx15;
    }

    public String getFx16() {
        return fx16;
    }

    public void setFx16(String fx16) {
        this.fx16 = fx16;
    }

    public String getFx17() {
        return fx17;
    }

    public void setFx17(String fx17) {
        this.fx17 = fx17;
    }

    public String getFx18() {
        return fx18;
    }

    public void setFx18(String fx18) {
        this.fx18 = fx18;
    }

    public String getFx19() {
        return fx19;
    }

    public void setFx19(String fx19) {
        this.fx19 = fx19;
    }

    public String getFx20() {
        return fx20;
    }

    public void setFx20(String fx20) {
        this.fx20 = fx20;
    }

    public Integer getPeriodYessale() {
        return periodYessale;
    }

    public void setPeriodYessale(Integer periodYessale) {
        this.periodYessale = periodYessale;
    }
    
    public Boolean getIsLinkProduct() {
        return isLinkProduct;
    }

    public void setIsLinkProduct(Boolean isLinkProduct) {
        this.isLinkProduct = isLinkProduct;
    }
    
}
