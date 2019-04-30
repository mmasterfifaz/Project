/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
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
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Cascade;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "product_plan")
@NamedQueries({@NamedQuery(name = "ProductPlan.findAll", query = "SELECT p FROM ProductPlan p")})
public class ProductPlan implements Serializable {
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "sum_insured", precision = 18, scale = 2)
    private Double sumInsured;
    @Column(name = "claim_type")
    private String claimType;
    @Column(name = "life_loss_person")
    private Integer lifeLossPerson;
    @Column(name = "life_loss_time")
    private Integer lifeLossTime;
    @Column(name = "asset_loss_time")
    private Integer assetLossTime;
    @Column(name = "asset_deduct")
    private Integer assetDeduct;
    @Column(name = "motor_loss")
    private Integer motorLoss;
    @Column(name = "motor_deduct")
    private Integer motorDeduct;
    @Column(name = "motor_damage")
    private Integer motorDamage;
    @Column(name = "no_lost_driver")
    private Integer noLostDriver;
    @Column(name = "cost_lost_driver")
    private Integer costLostDriver;
    @Column(name = "no_lost_passenger")
    private Integer noLostPassenger;
    @Column(name = "cost_lost_passenger")
    private Integer costLostPassenger;
    @Column(name = "no_disable_driver")
    private Integer noDisableDriver;
    @Column(name = "cost_disable_driver")
    private Integer costDisableDriver;
    @Column(name = "no_disable_passenger")
    private Integer noDisablePassenger;
    @Column(name = "cost_disable_passenger")
    private Integer costDisablePassenger;
    @Column(name = "medical_expense")
    private Integer medicalExpense;
    @Column(name = "bail")
    private Integer bail;
    @Column(name = "net_premium")
    private Double netPremium;
    @Column(name = "act")
    private Double act;
    @OneToMany(mappedBy = "productPlan")
    private Collection<TempPurchaseOrderDetail> tempPurchaseOrderDetailCollection;
    @OneToMany(mappedBy = "productPlan")
    private Collection<PurchaseOrderDetail> purchaseOrderDetailCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "no")
    private Integer no;
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "payment_mode")
    private String paymentMode;
    @Lob
    @Column(name = "description")
    private String description;
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @ManyToOne
    private Product product;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "productPlan")
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
        org.hibernate.annotations.CascadeType.DELETE,
        org.hibernate.annotations.CascadeType.MERGE,
        org.hibernate.annotations.CascadeType.PERSIST,
        org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private Collection<ProductPlanDetail> productPlanDetailCollection;
    @Column(name = "enable")
    private Boolean enable;
    @JoinColumn(name = "product_plan_category_id", referencedColumnName = "id")
    @ManyToOne
    private ProductPlanCategory productPlanCategory;
    @Column(name = "master_plan")
    private Boolean masterPlan;
    
    @Transient
    private String status;
    @Transient
    private String reason;
    @Transient
    private String sumInsuredTemp;
    @Transient
    private String masterPlanTemp;
    @Transient
    private String planNo;
    
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
    
    @JoinTable(name = "product_plan_relation", joinColumns = {
        @JoinColumn(name = "master_product_plan_id", referencedColumnName = "id")}, 
        inverseJoinColumns = {
        @JoinColumn(name = "child_product_plan_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<ProductPlan> productPlanMasterCollection;
    
    @JoinTable(name = "product_plan_relation", joinColumns = {
        @JoinColumn(name = "child_product_plan_id", referencedColumnName = "id")}, 
        inverseJoinColumns = {
        @JoinColumn(name = "master_product_plan_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<ProductPlan> productPlanSpouseCollection;

    public ProductPlan() {
    }

    public ProductPlan(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Collection<ProductPlanDetail> getProductPlanDetailCollection() {
        return productPlanDetailCollection;
    }

    public void setProductPlanDetailCollection(Collection<ProductPlanDetail> productPlanDetailCollection) {
        this.productPlanDetailCollection = productPlanDetailCollection;
    }

    public Double getSumInsured() {
        return sumInsured;
    }

    public void setSumInsured(Double sumInsured) {
        this.sumInsured = sumInsured;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Boolean getMasterPlan() {
        return masterPlan;
    }

    public void setMasterPlan(Boolean masterPlan) {
        this.masterPlan = masterPlan;
    }

    public String getPlanNo() {
        return planNo;
    }

    public void setPlanNo(String planNo) {
        this.planNo = planNo;
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
        if (!(object instanceof ProductPlan)) {
            return false;
        }
        ProductPlan other = (ProductPlan) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public String getPaymentModeLabel() {
        String result="";
        if ("1".equals(paymentMode)) {
            result="Monthly";
        } else if ("2".equals(paymentMode)) {
            result="Quarterly";
        } else if ("3".equals(paymentMode)) {
            result="Half Year";
        } else if ("4".equals(paymentMode)) {
            result="Yearly";
        } 
        return result;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    
    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.ProductPlan[id=" + id + "]";
    }

    public Collection<TempPurchaseOrderDetail> getTempPurchaseOrderDetailCollection() {
        return tempPurchaseOrderDetailCollection;
    }

    public void setTempPurchaseOrderDetailCollection(Collection<TempPurchaseOrderDetail> tempPurchaseOrderDetailCollection) {
        this.tempPurchaseOrderDetailCollection = tempPurchaseOrderDetailCollection;
    }

    public Collection<PurchaseOrderDetail> getPurchaseOrderDetailCollection() {
        return purchaseOrderDetailCollection;
    }

    public void setPurchaseOrderDetailCollection(Collection<PurchaseOrderDetail> purchaseOrderDetailCollection) {
        this.purchaseOrderDetailCollection = purchaseOrderDetailCollection;
    }

    public ProductPlanCategory getProductPlanCategory() {
        return productPlanCategory;
    }

    public void setProductPlanCategory(ProductPlanCategory productPlanCategory) {
        this.productPlanCategory = productPlanCategory;
    }

    public Integer getLifeLossPerson() {
        return lifeLossPerson;
    }

    public void setLifeLossPerson(Integer lifeLossPerson) {
        this.lifeLossPerson = lifeLossPerson;
    }

    public Integer getLifeLossTime() {
        return lifeLossTime;
    }

    public void setLifeLossTime(Integer lifeLossTime) {
        this.lifeLossTime = lifeLossTime;
    }

    public Integer getAssetLossTime() {
        return assetLossTime;
    }

    public void setAssetLossTime(Integer assetLossTime) {
        this.assetLossTime = assetLossTime;
    }

    public Integer getAssetDeduct() {
        return assetDeduct;
    }

    public void setAssetDeduct(Integer assetDeduct) {
        this.assetDeduct = assetDeduct;
    }

    public Integer getMotorLoss() {
        return motorLoss;
    }

    public void setMotorLoss(Integer motorLoss) {
        this.motorLoss = motorLoss;
    }

    public Integer getMotorDeduct() {
        return motorDeduct;
    }

    public void setMotorDeduct(Integer motorDeduct) {
        this.motorDeduct = motorDeduct;
    }

    public Integer getMotorDamage() {
        return motorDamage;
    }

    public void setMotorDamage(Integer motorDamage) {
        this.motorDamage = motorDamage;
    }

    public Integer getNoLostDriver() {
        return noLostDriver;
    }

    public void setNoLostDriver(Integer noLostDriver) {
        this.noLostDriver = noLostDriver;
    }

    public Integer getCostLostDriver() {
        return costLostDriver;
    }

    public void setCostLostDriver(Integer costLostDriver) {
        this.costLostDriver = costLostDriver;
    }

    public Integer getNoLostPassenger() {
        return noLostPassenger;
    }

    public void setNoLostPassenger(Integer noLostPassenger) {
        this.noLostPassenger = noLostPassenger;
    }

    public Integer getCostLostPassenger() {
        return costLostPassenger;
    }

    public void setCostLostPassenger(Integer costLostPassenger) {
        this.costLostPassenger = costLostPassenger;
    }

    public Integer getNoDisableDriver() {
        return noDisableDriver;
    }

    public void setNoDisableDriver(Integer noDisableDriver) {
        this.noDisableDriver = noDisableDriver;
    }

    public Integer getCostDisableDriver() {
        return costDisableDriver;
    }

    public void setCostDisableDriver(Integer costDisableDriver) {
        this.costDisableDriver = costDisableDriver;
    }

    public Integer getNoDisablePassenger() {
        return noDisablePassenger;
    }

    public void setNoDisablePassenger(Integer noDisablePassenger) {
        this.noDisablePassenger = noDisablePassenger;
    }

    public Integer getCostDisablePassenger() {
        return costDisablePassenger;
    }

    public void setCostDisablePassenger(Integer costDisablePassenger) {
        this.costDisablePassenger = costDisablePassenger;
    }

    public Integer getMedicalExpense() {
        return medicalExpense;
    }

    public void setMedicalExpense(Integer medicalExpense) {
        this.medicalExpense = medicalExpense;
    }

    public Integer getBail() {
        return bail;
    }

    public void setBail(Integer bail) {
        this.bail = bail;
    }

    public Double getAct() {
        return act;
    }

    public void setAct(Double act) {
        this.act = act;
    }

    public Double getNetPremium() {
        return netPremium;
    }

    public void setNetPremium(Double netPremium) {
        this.netPremium = netPremium;
    }

    public String getClaimType() {
        return claimType;
    }

    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSumInsuredTemp() {
        return sumInsuredTemp;
    }

    public void setSumInsuredTemp(String sumInsuredTemp) {
        this.sumInsuredTemp = sumInsuredTemp;
    }

    public String getMasterPlanTemp() {
        return masterPlanTemp;
    }

    public void setMasterPlanTemp(String masterPlanTemp) {
        this.masterPlanTemp = masterPlanTemp;
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

    public Collection<ProductPlan> getProductPlanMasterCollection() {
        return productPlanMasterCollection;
    }

    public void setProductPlanMasterCollection(Collection<ProductPlan> productPlanMasterCollection) {
        this.productPlanMasterCollection = productPlanMasterCollection;
    }

    public Collection<ProductPlan> getProductPlanSpouseCollection() {
        return productPlanSpouseCollection;
    }

    public void setProductPlanSpouseCollection(Collection<ProductPlan> productPlanSpouseCollection) {
        this.productPlanSpouseCollection = productPlanSpouseCollection;
    }

}
