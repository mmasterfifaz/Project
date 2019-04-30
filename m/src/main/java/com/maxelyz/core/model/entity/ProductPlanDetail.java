/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "product_plan_detail")
@NamedQueries({@NamedQuery(name = "ProductPlanDetail.findAll", query = "SELECT p FROM ProductPlanDetail p")})
public class ProductPlanDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "seq_no")
    private Integer seqNo;
    @Column(name = "from_val")
    private Integer fromVal;
    @Column(name = "to_val")
    private Integer toVal;
    @Column(name = "gender")
    private String gender;
    @Column(name = "payment_mode")
    private String paymentMode;
    @Column(name = "term")
    private String term;
    @Column(name = "payment_term")
    private String paymentTerm;
    @Column(name = "price")
    private Double price;
    @Column(name = "stamp_duty")
    private Double stampDuty;
    @Column(name = "vat")
    private Double vat;
    @Column(name = "net_premium")
    private Double netPremium;
    @Column(name = "annual_net_premium")
    private Double annualNetPremium;
    @Column(name = "annual_price")
    private Double annualPrice;
    @JoinColumn(name = "product_plan_id", referencedColumnName = "id")
    @ManyToOne
    private ProductPlan productPlan;
    
    @Transient
    private Integer planNo;
    @Transient
    private String status;
    @Transient
    private String reason;
    @Transient
    private String fromValTemp;
    @Transient
    private String toValTemp;
    @Transient
    private String priceTemp;
    @Transient
    private String stampDutyTemp;
    @Transient
    private String vatTemp;
    @Transient
    private String netPremiumTemp;
    @Transient
    private String annualNetPremiumTemp;
    @Transient
    private String annualPriceTemp;
    @Transient
    private String planNoTemp;
    @Transient
    private boolean doInsert;
    
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
    @Column(name = "code")
    private String code;

    public ProductPlanDetail() {
    }

    public ProductPlanDetail(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public Integer getFromVal() {
        return fromVal;
    }

    public void setFromVal(Integer fromVal) {
        this.fromVal = fromVal;
    }

    public Integer getToVal() {
        return toVal;
    }

    public void setToVal(Integer toVal) {
        this.toVal = toVal;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getStampDuty() {
        return stampDuty;
    }

    public void setStampDuty(Double stampDuty) {
        this.stampDuty = stampDuty;
    }

    public Double getVat() {
        return vat;
    }

    public void setVat(Double vat) {
        this.vat = vat;
    }

    public Double getNetPremium() {
        return netPremium;
    }

    public void setNetPremium(Double netPremium) {
        this.netPremium = netPremium;
    }

    public Double getAnnualNetPremium() {
        return annualNetPremium;
    }

    public void setAnnualNetPremium(Double annualNetPremium) {
        this.annualNetPremium = annualNetPremium;
    }

    public Double getAnnualPrice() {
        return annualPrice;
    }

    public void setAnnualPrice(Double annualPrice) {
        this.annualPrice = annualPrice;
    }

    public ProductPlan getProductPlan() {
        return productPlan;
    }

    public void setProductPlan(ProductPlan productPlan) {
        this.productPlan = productPlan;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getPaymentTerm() {
        return paymentTerm;
    }

    public void setPaymentTerm(String paymentTerm) {
        this.paymentTerm = paymentTerm;
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

    public Integer getPlanNo() {
        return planNo;
    }

    public void setPlanNo(Integer planNo) {
        this.planNo = planNo;
    }

    public String getFromValTemp() {
        return fromValTemp;
    }

    public void setFromValTemp(String fromValTemp) {
        this.fromValTemp = fromValTemp;
    }

    public String getToValTemp() {
        return toValTemp;
    }

    public void setToValTemp(String toValTemp) {
        this.toValTemp = toValTemp;
    }

    public String getPriceTemp() {
        return priceTemp;
    }

    public void setPriceTemp(String priceTemp) {
        this.priceTemp = priceTemp;
    }

    public String getStampDutyTemp() {
        return stampDutyTemp;
    }

    public void setStampDutyTemp(String stampDutyTemp) {
        this.stampDutyTemp = stampDutyTemp;
    }

    public String getVatTemp() {
        return vatTemp;
    }

    public void setVatTemp(String vatTemp) {
        this.vatTemp = vatTemp;
    }

    public String getNetPremiumTemp() {
        return netPremiumTemp;
    }

    public void setNetPremiumTemp(String netPremiumTemp) {
        this.netPremiumTemp = netPremiumTemp;
    }

    public String getAnnualNetPremiumTemp() {
        return annualNetPremiumTemp;
    }

    public void setAnnualNetPremiumTemp(String annualNetPremiumTemp) {
        this.annualNetPremiumTemp = annualNetPremiumTemp;
    }

    public String getAnnualPriceTemp() {
        return annualPriceTemp;
    }

    public void setAnnualPriceTemp(String annualPriceTemp) {
        this.annualPriceTemp = annualPriceTemp;
    }

    public String getPlanNoTemp() {
        return planNoTemp;
    }

    public void setPlanNoTemp(String planNoTemp) {
        this.planNoTemp = planNoTemp;
    }

    public boolean getDoInsert() {
        return doInsert;
    }

    public void setDoInsert(boolean doInsert) {
        this.doInsert = doInsert;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
        if (!(object instanceof ProductPlanDetail)) {
            return false;
        }
        ProductPlanDetail other = (ProductPlanDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.ProductPlanDetail[id=" + id + "]";
    }

}
