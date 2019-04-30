/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author oat
 */
@Entity
@Table(name = "motor_protection_class")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MotorProtectionClass.findAll", query = "SELECT m FROM MotorProtectionClass m")})
public class MotorProtectionClass implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "code", length = 100)
    private String code;
    @Column(name = "name", length = 100)
    private String name;
    @Column(name = "payment_mode", length = 100)
    private String paymentMode;
    @Lob
    @Column(name = "description")
    private String description;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "sum_insured", precision = 18, scale = 2)
    private BigDecimal sumInsured;
    @Column(name = "claim_type", length = 100)
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
    @Column(name = "act", precision = 18, scale = 2)
    private BigDecimal act;
    @Column(name = "net_premium", precision = 18, scale = 2)
    private BigDecimal netPremium;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "create_by", length = 100)
    private String createBy;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "update_by", length = 100)
    private String updateBy;

    public MotorProtectionClass() {
    }

    public MotorProtectionClass(Integer id) {
        this.id = id;
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

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getSumInsured() {
        return sumInsured;
    }

    public void setSumInsured(BigDecimal sumInsured) {
        this.sumInsured = sumInsured;
    }

    public String getClaimType() {
        return claimType;
    }

    public void setClaimType(String claimType) {
        this.claimType = claimType;
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

    public BigDecimal getAct() {
        return act;
    }

    public void setAct(BigDecimal act) {
        this.act = act;
    }

    public BigDecimal getNetPremium() {
        return netPremium;
    }

    public void setNetPremium(BigDecimal netPremium) {
        this.netPremium = netPremium;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MotorProtectionClass)) {
            return false;
        }
        MotorProtectionClass other = (MotorProtectionClass) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.MotorProtectionClass[ id=" + id + " ]";
    }
    
}
