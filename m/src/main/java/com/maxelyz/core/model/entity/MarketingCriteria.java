/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "marketing_criteria")
@NamedQueries({
    @NamedQuery(name = "MarketingCriteria.findAll", query = "SELECT m FROM MarketingCriteria m")})
public class MarketingCriteria implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Column(name = "select_dob")
    private Boolean selectDob;
    @Column(name = "dob_from")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dobFrom;
    @Column(name = "dob_to")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dobTo;
    @Column(name = "select_gender")
    private Boolean selectGender;
    @Lob
    @Column(name = "gender")
    private String gender;
    @Column(name = "select_province")
    private Boolean selectProvince;
    @Lob
    @Column(name = "province")
    private String province;
    @Column(name = "select_occupation")
    private Boolean selectOccupation;
    @Lob
    @Column(name = "occupation")
    private String occupation;
    @Column(name = "select_salary")
    private Boolean selectSalary;
    @Column(name = "salary_from")
    private Integer salaryFrom;
    @Column(name = "salary_to")
    private Integer salaryTo;
    @Column(name = "select_application_date")
    private Boolean selectApplicationDate;
    @Column(name = "application_date_from")
    @Temporal(TemporalType.TIMESTAMP)
    private Date applicationDateFrom;
    @Column(name = "application_date_to")
    @Temporal(TemporalType.TIMESTAMP)
    private Date applicationDateTo;
    @Column(name = "select_telephone_prefix")
    private Boolean selectTelephonePrefix;
    @Lob
    @Column(name = "telephone_prefix")
    private String telephonePrefix;
    @Column(name = "select_product")
    private Boolean selectProduct;
    @Lob
    @Column(name = "product_plan_category")
    private String productPlanCategory;
    @Lob
    @Column(name = "product_category")
    private String productCategory;
    @Lob
    @Column(name = "product_sponsor")
    private String productSponsor;
    @Column(name = "model_type_id")
    private Integer modelTypeId;
    @Column(name = "model_id")
    private Integer modelId;
    @Column(name = "model_fromyear")
    private Integer modelFromyear;
    @Column(name = "model_toyear")
    private Integer modelToyear;
    @Lob
    @Column(name = "product")
    private String product;
    @Column(name = "select_contact_date")
    private Boolean selectContactDate;
    @Column(name = "contact_date_from")
    @Temporal(TemporalType.TIMESTAMP)
    private Date contactDateFrom;
    @Column(name = "contact_date_to")
    @Temporal(TemporalType.TIMESTAMP)
    private Date contactDateTo;
    @Column(name = "select_sale_date")
    private Boolean selectSaleDate;
    @Column(name = "sale_date_from")
    @Temporal(TemporalType.TIMESTAMP)
    private Date saleDateFrom;
    @Column(name = "sale_date_to")
    @Temporal(TemporalType.TIMESTAMP)
    private Date saleDateTo;
    @Column(name = "select_effective_date")
    private Boolean selectEffectiveDate;
    @Column(name = "effective_date_from")
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectiveDateFrom;
    @Column(name = "effective_date_to")
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectiveDateTo;
    @Column(name = "select_cancel_date")
    private Boolean selectCancelDate;
    @Column(name = "cancel_date_from")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cancelDateFrom;
    @Column(name = "cancel_date_to")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cancelDateTo;
    @Column(name = "select_opout")
    private Boolean selectOpout;
    @Lob
    @Column(name = "opout")
    private String opout;
    @Column(name = "select_nosale_reason")
    private Boolean selectNosaleReason;
    @Lob
    @Column(name = "nosale_reason")
    private String nosaleReason;
    @Column(name = "select_qc_reject")
    private Boolean selectQcReject;
    @Lob
    @Column(name = "qc_reject")
    private String qcReject;
    @Column(name = "select_uw_reject")
    private Boolean selectUwReject;
    @Lob
    @Column(name = "uw_reject")
    private String uwReject;
    @Column(name = "select_payment_reject")
    private Boolean selectPaymentReject;
    @Lob
    @Column(name = "payment_reject")
    private String paymentReject;
    @JoinColumn(name = "marketing_id", referencedColumnName = "id")
    @ManyToOne
    private Marketing marketing;
    @Column(name = "product_category_type")
    private String productCategoryType;
    
    public MarketingCriteria() {
    }

    public MarketingCriteria(Integer id) {
        this.id = id;
    }

    public MarketingCriteria(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSelectDob() {
        return selectDob;
    }

    public void setSelectDob(Boolean selectDob) {
        this.selectDob = selectDob;
    }

    public Date getDobFrom() {
        return dobFrom;
    }

    public void setDobFrom(Date dobFrom) {
        this.dobFrom = dobFrom;
    }

    public Date getDobTo() {
        return dobTo;
    }

    public void setDobTo(Date dobTo) {
        this.dobTo = dobTo;
    }

    public Boolean getSelectGender() {
        return selectGender;
    }

    public void setSelectGender(Boolean selectGender) {
        this.selectGender = selectGender;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Boolean getSelectProvince() {
        return selectProvince;
    }

    public void setSelectProvince(Boolean selectProvince) {
        this.selectProvince = selectProvince;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Boolean getSelectOccupation() {
        return selectOccupation;
    }

    public void setSelectOccupation(Boolean selectOccupation) {
        this.selectOccupation = selectOccupation;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public Boolean getSelectSalary() {
        return selectSalary;
    }

    public void setSelectSalary(Boolean selectSalary) {
        this.selectSalary = selectSalary;
    }

    public Integer getSalaryFrom() {
        return salaryFrom;
    }

    public void setSalaryFrom(Integer salaryFrom) {
        this.salaryFrom = salaryFrom;
    }

    public Integer getSalaryTo() {
        return salaryTo;
    }

    public void setSalaryTo(Integer salaryTo) {
        this.salaryTo = salaryTo;
    }

    public Boolean getSelectApplicationDate() {
        return selectApplicationDate;
    }

    public void setSelectApplicationDate(Boolean selectApplicationDate) {
        this.selectApplicationDate = selectApplicationDate;
    }

    public Date getApplicationDateFrom() {
        return applicationDateFrom;
    }

    public void setApplicationDateFrom(Date applicationDateFrom) {
        this.applicationDateFrom = applicationDateFrom;
    }

    public Date getApplicationDateTo() {
        return applicationDateTo;
    }

    public void setApplicationDateTo(Date applicationDateTo) {
        this.applicationDateTo = applicationDateTo;
    }

    public Boolean getSelectTelephonePrefix() {
        return selectTelephonePrefix;
    }

    public void setSelectTelephonePrefix(Boolean selectTelephonePrefix) {
        this.selectTelephonePrefix = selectTelephonePrefix;
    }

    public String getTelephonePrefix() {
        return telephonePrefix;
    }

    public void setTelephonePrefix(String telephonePrefix) {
        this.telephonePrefix = telephonePrefix;
    }

    public Boolean getSelectProduct() {
        return selectProduct;
    }

    public void setSelectProduct(Boolean selectProduct) {
        this.selectProduct = selectProduct;
    }

    public String getProductPlanCategory() {
        return productPlanCategory;
    }

    public void setProductPlanCategory(String productPlanCategory) {
        this.productPlanCategory = productPlanCategory;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductSponsor() {
        return productSponsor;
    }

    public void setProductSponsor(String productSponsor) {
        this.productSponsor = productSponsor;
    }

    public Integer getModelTypeId() {
        return modelTypeId;
    }

    public void setModelTypeId(Integer modelTypeId) {
        this.modelTypeId = modelTypeId;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public Integer getModelFromyear() {
        return modelFromyear;
    }

    public void setModelFromyear(Integer modelFromyear) {
        this.modelFromyear = modelFromyear;
    }

    public Integer getModelToyear() {
        return modelToyear;
    }

    public void setModelToyear(Integer modelToyear) {
        this.modelToyear = modelToyear;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Boolean getSelectContactDate() {
        return selectContactDate;
    }

    public void setSelectContactDate(Boolean selectContactDate) {
        this.selectContactDate = selectContactDate;
    }

    public Date getContactDateFrom() {
        return contactDateFrom;
    }

    public void setContactDateFrom(Date contactDateFrom) {
        this.contactDateFrom = contactDateFrom;
    }

    public Date getContactDateTo() {
        return contactDateTo;
    }

    public void setContactDateTo(Date contactDateTo) {
        this.contactDateTo = contactDateTo;
    }

    public Boolean getSelectSaleDate() {
        return selectSaleDate;
    }

    public void setSelectSaleDate(Boolean selectSaleDate) {
        this.selectSaleDate = selectSaleDate;
    }

    public Date getSaleDateFrom() {
        return saleDateFrom;
    }

    public void setSaleDateFrom(Date saleDateFrom) {
        this.saleDateFrom = saleDateFrom;
    }

    public Date getSaleDateTo() {
        return saleDateTo;
    }

    public void setSaleDateTo(Date saleDateTo) {
        this.saleDateTo = saleDateTo;
    }

    public Boolean getSelectEffectiveDate() {
        return selectEffectiveDate;
    }

    public void setSelectEffectiveDate(Boolean selectEffectiveDate) {
        this.selectEffectiveDate = selectEffectiveDate;
    }

    public Date getEffectiveDateFrom() {
        return effectiveDateFrom;
    }

    public void setEffectiveDateFrom(Date effectiveDateFrom) {
        this.effectiveDateFrom = effectiveDateFrom;
    }

    public Date getEffectiveDateTo() {
        return effectiveDateTo;
    }

    public void setEffectiveDateTo(Date effectiveDateTo) {
        this.effectiveDateTo = effectiveDateTo;
    }

    public Boolean getSelectOpout() {
        return selectOpout;
    }

    public void setSelectOpout(Boolean selectOpout) {
        this.selectOpout = selectOpout;
    }

    public String getOpout() {
        return opout;
    }

    public void setOpout(String opout) {
        this.opout = opout;
    }

    public Boolean getSelectNosaleReason() {
        return selectNosaleReason;
    }

    public void setSelectNosaleReason(Boolean selectNosaleReason) {
        this.selectNosaleReason = selectNosaleReason;
    }

    public String getNosaleReason() {
        return nosaleReason;
    }

    public void setNosaleReason(String nosaleReason) {
        this.nosaleReason = nosaleReason;
    }

    public Boolean getSelectQcReject() {
        return selectQcReject;
    }

    public void setSelectQcReject(Boolean selectQcReject) {
        this.selectQcReject = selectQcReject;
    }

    public String getQcReject() {
        return qcReject;
    }

    public void setQcReject(String qcReject) {
        this.qcReject = qcReject;
    }

    public Boolean getSelectUwReject() {
        return selectUwReject;
    }

    public void setSelectUwReject(Boolean selectUwReject) {
        this.selectUwReject = selectUwReject;
    }

    public String getUwReject() {
        return uwReject;
    }

    public void setUwReject(String uwReject) {
        this.uwReject = uwReject;
    }

    public Boolean getSelectPaymentReject() {
        return selectPaymentReject;
    }

    public void setSelectPaymentReject(Boolean selectPaymentReject) {
        this.selectPaymentReject = selectPaymentReject;
    }

    public String getPaymentReject() {
        return paymentReject;
    }

    public void setPaymentReject(String paymentReject) {
        this.paymentReject = paymentReject;
    }

    public Marketing getMarketing() {
        return marketing;
    }

    public void setMarketing(Marketing marketing) {
        this.marketing = marketing;
    }

    public Date getCancelDateFrom() {
        return cancelDateFrom;
    }

    public void setCancelDateFrom(Date cancelDateFrom) {
        this.cancelDateFrom = cancelDateFrom;
    }

    public Date getCancelDateTo() {
        return cancelDateTo;
    }

    public void setCancelDateTo(Date cancelDateTo) {
        this.cancelDateTo = cancelDateTo;
    }

    public Boolean getSelectCancelDate() {
        return selectCancelDate;
    }

    public void setSelectCancelDate(Boolean selectCancelDate) {
        this.selectCancelDate = selectCancelDate;
    }

    public String getProductCategoryType() {
        return productCategoryType;
    }

    public void setProductCategoryType(String productCategoryType) {
        this.productCategoryType = productCategoryType;
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
        if (!(object instanceof MarketingCriteria)) {
            return false;
        }
        MarketingCriteria other = (MarketingCriteria) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.MarketingCriteria[ id=" + id + " ]";
    }
    
}
