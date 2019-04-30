/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "product")
@NamedQueries({
    @NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p")})
public class Product implements Serializable {
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @JoinColumn(name = "approval_rule_id", referencedColumnName = "id")
    @ManyToOne
    private ApprovalRule approvalRule;
    @JoinColumn(name = "sequence_no_id", referencedColumnName = "id")
    @ManyToOne
    private SequenceNo sequenceNo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "price", precision = 19, scale = 2)
    private Double price;
    @Column(name = "promotion_price", precision = 19, scale = 2)
    private Double promotionPrice;
    @Column(name = "model_year", length = 4)
    private String modelYear;
    @Column(name = "model_fromyear")
    private Integer modelFromYear;
    @Column(name = "model_toyear")
    private Integer modelToYear;
    @Column(name = "model_seat")
    private Integer modelSeat;
    @Column(name = "model_cc")
    private Integer modelCc;
    @Column(name = "model_weight")
    private Integer modelWeight;
    @Column(name = "fx1", length = 100)
    private String fx1;
    @Column(name = "fx2", length = 100)
    private String fx2;
    @Column(name = "fx3", length = 100)
    private String fx3;
    @Column(name = "fx4", length = 100)
    private String fx4;
    @Column(name = "fx5", length = 100)
    private String fx5;
    @Column(name = "fx6", length = 100)
    private String fx6;
    @Column(name = "fx7", length = 100)
    private String fx7;
    @Column(name = "fx8", length = 100)
    private String fx8;
    @Column(name = "fx9", length = 100)
    private String fx9;
    @Column(name = "fx10", length = 100)
    private String fx10;
    @Column(name = "fx11", length = 100)
    private String fx11;
    @Column(name = "fx12", length = 100)
    private String fx12;
    @Column(name = "fx13", length = 100)
    private String fx13;
    @Column(name = "fx14", length = 100)
    private String fx14;
    @Column(name = "fx15", length = 100)
    private String fx15;
    @Column(name = "fx16", length = 100)
    private String fx16;
    @Column(name = "fx17", length = 100)
    private String fx17;
    @Column(name = "fx18", length = 100)
    private String fx18;
    @Column(name = "fx19", length = 100)
    private String fx19;
    @Column(name = "fx20", length = 100)
    private String fx20;
    @JoinColumn(name = "model_type_id", referencedColumnName = "id")
    @ManyToOne
    private ModelType modelType;
    @JoinColumn(name = "model_id", referencedColumnName = "id")
    @ManyToOne
    private Model model;
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
    @Lob
    @Column(name = "highlight")
    private String highlight;
    @Lob
    @Column(name = "description")
    private String description;
    @Lob
    @Column(name = "confirmation_script")
    private String confirmationScript;
    @Column(name = "remark")
    private String remark;
    @Column(name = "product_thumbnail")
    private String productThumbnail;
    @Column(name = "payment_mode")
    private String paymentMode;
    @Column(name = "payment_method")
    private String paymentMethod;
    @Column(name = "card_type")
    private String cardType;
    @Column(name = "card_bank")
    private String cardBank;
    @Column(name = "card_type_debit")
    private String cardTypeDebit;
    @Column(name = "card_bank_debit")
    private String cardBankDebit;
    @Column(name = "delivery_method")
    private String deliveryMethod;
    @Column(name = "enable")
    private Boolean enable;
    @Column(name = "create_by")
    private String createBy;
    @Column(name = "update_by")
    private String updateBy;
    @Column(name = "monthly_first_payment")
    private Integer monthlyFirstPayment;
    @Column(name = "quarterly_first_payment")
    private Integer quarterlyFirstPayment;
    @Column(name = "age_cal_method")
    private String ageCalMethod;
    @Column(name = "age_cal_month")
    private Integer ageCalMonth;
    @Column(name = "age_cal_day")
    private Integer ageCalDay;
    @Column(name = "ref_no_generate_event")
    private String refNoGenerateEvent;
    @Column(name = "gender")
    private Boolean gender;
    @Column(name = "allow_copy")
    private Boolean allowCopy;
    @Column(name = "beneficiary_no")
    private Integer beneficiaryNo;
    @Column(name = "percent_beneficiary_fxfieldno")
    private Integer percentBeneficiaryFxFieldNo;
    @Column(name = "family_product")
    private Boolean familyProduct;    
    @Column(name = "family_product_type")
    private Integer familyProductType;
    @Column(name = "family_product_logic")
    private String familyProductLogic;
    @Column(name = "product_plan_coverage")
    private Integer productPlanCoverage;
    
    @JoinColumn(name = "contact_result_plan_id", referencedColumnName = "id")
    @ManyToOne
    private ContactResultPlan contactResultPlan;
    
    @JoinColumn(name = "registration_form_id", referencedColumnName = "id")
    @ManyToOne
    private RegistrationForm registrationForm;

    @JoinColumn(name = "product_sponsor_id", referencedColumnName = "id")
    @ManyToOne
    private ProductSponsor productSponsor;

    @JoinColumn(name = "product_category_id", referencedColumnName = "id")
    @ManyToOne
    private ProductCategory productCategory;

    @OneToMany(mappedBy = "product")
    private Collection<ProductPlan> productPlanCollection;

    @ManyToMany(mappedBy = "productCollection")
    private Collection<Campaign> campaignCollection;

    @JoinColumn(name = "motor_protection_class_id", referencedColumnName = "id")
    @ManyToOne
    private MotorProtectionClass motorProtectionClass;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private Collection<ProductWorkflow> productWorkflowCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private Collection<ProductChildRegType> productChildRegTypeCollection;

    public Product() {
    }

    public Product(Integer id) {
        this.id = id;
    }

    public Product(Integer id, String code, String name) {
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

    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConfirmationScript() {
        return confirmationScript;
    }

    public void setConfirmationScript(String confirmationScript) {
        this.confirmationScript = confirmationScript;
    }
    
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getProductThumbnail() {
        return productThumbnail;
    }

    public void setProductThumbnail(String productThumbnail) {
        this.productThumbnail = productThumbnail;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public Double getPromotionPrice() {
        return promotionPrice;
    }

    public void setPromotionPrice(Double promotionPrice) {
        this.promotionPrice = promotionPrice;
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

    public Collection<Campaign> getCampaignCollection() {
        return campaignCollection;
    }

    public void setCampaignCollection(Collection<Campaign> campaignCollection) {
        this.campaignCollection = campaignCollection;
    }

    public RegistrationForm getRegistrationForm() {
        return registrationForm;
    }

    public void setRegistrationForm(RegistrationForm registrationForm) {
        this.registrationForm = registrationForm;
    }

    public ProductSponsor getProductSponsor() {
        return productSponsor;
    }

    public void setProductSponsor(ProductSponsor productSponsor) {
        this.productSponsor = productSponsor;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public Collection<ProductPlan> getProductPlanCollection() {
        return productPlanCollection;
    }

    public void setProductPlanCollection(Collection<ProductPlan> productPlanCollection) {
        this.productPlanCollection = productPlanCollection;
    }

    public Integer getMonthlyFirstPayment() {
        return monthlyFirstPayment;
    }

    public void setMonthlyFirstPayment(Integer monthlyFirstPayment) {
        this.monthlyFirstPayment = monthlyFirstPayment;
    }

    public Integer getQuarterlyFirstPayment() {
        return quarterlyFirstPayment;
    }

    public void setQuarterlyFirstPayment(Integer quarterlyFirstPayment) {
        this.quarterlyFirstPayment = quarterlyFirstPayment;
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
        if (!(object instanceof Product)) {
            return false;
        }
        Product other = (Product) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.Product[id=" + id + "]";
    }

    public String getModelYear() {
        return modelYear;
    }

    public void setModelYear(String modelYear) {
        this.modelYear = modelYear;
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

    public ModelType getModelType() {
        return modelType;
    }

    public void setModelType(ModelType modelType) {
        this.modelType = modelType;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public ApprovalRule getApprovalRule() {
        return approvalRule;
    }

    public void setApprovalRule(ApprovalRule approvalRule) {
        this.approvalRule = approvalRule;
    }

    public Integer getModelFromYear() {
        return modelFromYear;
    }

    public void setModelFromYear(Integer modelFromYear) {
        this.modelFromYear = modelFromYear;
    }

    public Integer getModelToYear() {
        return modelToYear;
    }

    public void setModelToYear(Integer modelToYear) {
        this.modelToYear = modelToYear;
    }

    public MotorProtectionClass getMotorProtectionClass() {
        return motorProtectionClass;
    }

    public void setMotorProtectionClass(MotorProtectionClass motorProtectionClass) {
        this.motorProtectionClass = motorProtectionClass;
    }

    public Integer getModelCc() {
        return modelCc;
    }

    public void setModelCc(Integer modelCc) {
        this.modelCc = modelCc;
    }

    public Integer getModelSeat() {
        return modelSeat;
    }

    public void setModelSeat(Integer modelSeat) {
        this.modelSeat = modelSeat;
    }

    public Integer getModelWeight() {
        return modelWeight;
    }

    public void setModelWeight(Integer modelWeight) {
        this.modelWeight = modelWeight;
    }

    public String getAgeCalMethod() {
        return ageCalMethod;
    }

    public void setAgeCalMethod(String ageCalMethod) {
        this.ageCalMethod = ageCalMethod;
    }

    public Integer getAgeCalMonth() {
        return ageCalMonth;
    }

    public void setAgeCalMonth(Integer ageCalMonth) {
        this.ageCalMonth = ageCalMonth;
    }

    public SequenceNo getSequenceNo() {
        return sequenceNo;
}

    public void setSequenceNo(SequenceNo sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getRefNoGenerateEvent() {
        return refNoGenerateEvent;
    }

    public void setRefNoGenerateEvent(String refNoGenerateEvent) {
        this.refNoGenerateEvent = refNoGenerateEvent;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public Boolean getAllowCopy() {
        return allowCopy;
    }

    public void setAllowCopy(Boolean allowCopy) {
        this.allowCopy = allowCopy;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardBank() {
        return cardBank;
    }

    public void setCardBank(String cardBank) {
        this.cardBank = cardBank;
    }

    public Collection<ProductWorkflow> getProductWorkflowCollection() {
        return productWorkflowCollection;
    }

    public void setProductWorkflowCollection(Collection<ProductWorkflow> productWorkflowCollection) {
        this.productWorkflowCollection = productWorkflowCollection;
    }

    public Integer getBeneficiaryNo() {
        return beneficiaryNo;
    }

    public void setBeneficiaryNo(Integer beneficiaryNo) {
        this.beneficiaryNo = beneficiaryNo;
    }

    public Integer getPercentBeneficiaryFxFieldNo() {
        return percentBeneficiaryFxFieldNo;
    }

    public void setPercentBeneficiaryFxFieldNo(Integer percentBeneficiaryFxFieldNo) {
        this.percentBeneficiaryFxFieldNo = percentBeneficiaryFxFieldNo;
    }

    public Boolean getFamilyProduct() {
        return familyProduct;
    }

    public void setFamilyProduct(Boolean familyProduct) {
        this.familyProduct = familyProduct;
    }

    public Integer getAgeCalDay() {
        return ageCalDay;
    }

    public void setAgeCalDay(Integer ageCalDay) {
        this.ageCalDay = ageCalDay;
    }

    public String getCardTypeDebit() {
        return cardTypeDebit;
    }

    public void setCardTypeDebit(String cardTypeDebit) {
        this.cardTypeDebit = cardTypeDebit;
    }

    public String getCardBankDebit() {
        return cardBankDebit;
    }

    public void setCardBankDebit(String cardBankDebit) {
        this.cardBankDebit = cardBankDebit;
    }

    public ContactResultPlan getContactResultPlan() {
        return contactResultPlan;
    }

    public void setContactResultPlan(ContactResultPlan contactResultPlan) {
        this.contactResultPlan = contactResultPlan;
    }

    public Integer getFamilyProductType() {
        return familyProductType;
    }

    public void setFamilyProductType(Integer familyProductType) {
        this.familyProductType = familyProductType;
    }

    public String getFamilyProductLogic() {
        return familyProductLogic;
    }

    public void setFamilyProductLogic(String familyProductLogic) {
        this.familyProductLogic = familyProductLogic;
    }

    public Integer getProductPlanCoverage() {
        return productPlanCoverage;
    }

    public void setProductPlanCoverage(Integer productPlanCoverage) {
        this.productPlanCoverage = productPlanCoverage;
    }
}
