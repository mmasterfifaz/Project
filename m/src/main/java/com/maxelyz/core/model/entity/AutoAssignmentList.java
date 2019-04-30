/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "auto_assignment_list")
@NamedQueries({@NamedQuery(name = "AutoAssignmentList.findAll", query = "SELECT n FROM AutoAssignmentList n")})
public class AutoAssignmentList implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;    
    @Column(name = "reference_id")
    private String referenceId;
    @Column(name = "transaction_date")
    private String transactionDate;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email")
    private String email;
    @Column(name = "telephone")
    private String telephone;
    @Column(name = "language")
    private String language;
    @Column(name = "source")
    private String source;
    @Column(name = "keyword")
    private String keyword;
    @Column(name = "marketing_list_name")
    private String marketingListName;
    @Column(name = "assign_status")
    private String assignStatus;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;    
    @Column(name = "message")
    private String message;
    @Column(name = "car_plate_number")
    private String carPlateNumber;
    @Column(name = "car_year")
    private String carYear;
    @Column(name = "car_make")
    private String carMake;
    @Column(name = "car_model")
    private String carModel;
    @Column(name = "car_description")
    private String carDescription;
    @Column(name = "dob")
    private String dob;
    @Column(name = "gender")
    private String gender;
    @Column(name = "marital_status")
    private String maritalStatus;
    @Column(name = "main_driver_drive")
    private String mainDriverDrive;
    @Column(name = "main_driver_use")
    private String mainDriverUse;
    @Column(name = "insurance_type")
    private String insuranceType;
    @Column(name = "premium")
    private String premium;
    @Column(name = "campaign")
    private String campaign;
    @Column(name = "renewal_month")
    private String renewalMonth;
    @Column(name = "national_id")
    private String nationalID;
    @Column(name = "mt_number")
    private String mtNumber;
    @Column(name = "policy_start_date")
    private String policyStartDate;    
    @Column(name = "other1")
    private String other1;
    @Column(name = "other2")
    private String other2;
    @Column(name = "other3")
    private String other3;
    @Column(name = "other4")
    private String other4;
    @Column(name = "other5")
    private String other5;
    @Column(name = "other6")
    private String other6;
    @Column(name = "other7")
    private String other7;
    @Column(name = "other8")
    private String other8;
    @Column(name = "other9")
    private String other9;
    @Column(name = "other10")
    private String other10;
    @Column(name = "other11")
    private String other11;
    @Column(name = "other12")
    private String other12;
    
    public AutoAssignmentList() {
    }

    public AutoAssignmentList(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getMarketingListName() {
        return marketingListName;
    }

    public void setMarketingListName(String marketingListName) {
        this.marketingListName = marketingListName;
    }

    public String getAssignStatus() {
        return assignStatus;
    }

    public void setAssignStatus(String assignStatus) {
        this.assignStatus = assignStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCarPlateNumber() {
        return carPlateNumber;
    }

    public void setCarPlateNumber(String carPlateNumber) {
        this.carPlateNumber = carPlateNumber;
    }

    public String getCarYear() {
        return carYear;
    }

    public void setCarYear(String carYear) {
        this.carYear = carYear;
    }

    public String getCarMake() {
        return carMake;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarDescription() {
        return carDescription;
    }

    public void setCarDescription(String carDescription) {
        this.carDescription = carDescription;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getMainDriverDrive() {
        return mainDriverDrive;
    }

    public void setMainDriverDrive(String mainDriverDrive) {
        this.mainDriverDrive = mainDriverDrive;
    }

    public String getMainDriverUse() {
        return mainDriverUse;
    }

    public void setMainDriverUse(String mainDriverUse) {
        this.mainDriverUse = mainDriverUse;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }

    public String getPremium() {
        return premium;
    }

    public void setPremium(String premium) {
        this.premium = premium;
    }

    public String getCampaign() {
        return campaign;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    public String getRenewalMonth() {
        return renewalMonth;
    }

    public void setRenewalMonth(String renewalMonth) {
        this.renewalMonth = renewalMonth;
    }

    public String getNationalID() {
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
    }
    
    public String getMtNumber() {
        return mtNumber;
    }

    public void setMtNumber(String mtNumber) {
        this.mtNumber = mtNumber;
    }

    public String getPolicyStartDate() {
        return policyStartDate;
    }

    public void setPolicyStartDate(String policyStartDate) {
        this.policyStartDate = policyStartDate;
    }

    public String getOther1() {
        return other1;
    }

    public void setOther1(String other1) {
        this.other1 = other1;
    }

    public String getOther2() {
        return other2;
    }

    public void setOther2(String other2) {
        this.other2 = other2;
    }

    public String getOther3() {
        return other3;
    }

    public void setOther3(String other3) {
        this.other3 = other3;
    }

    public String getOther4() {
        return other4;
    }

    public void setOther4(String other4) {
        this.other4 = other4;
    }

    public String getOther5() {
        return other5;
    }

    public void setOther5(String other5) {
        this.other5 = other5;
    }

    public String getOther6() {
        return other6;
    }

    public void setOther6(String other6) {
        this.other6 = other6;
    }

    public String getOther7() {
        return other7;
    }

    public void setOther7(String other7) {
        this.other7 = other7;
    }

    public String getOther8() {
        return other8;
    }

    public void setOther8(String other8) {
        this.other8 = other8;
    }

    public String getOther9() {
        return other9;
    }

    public void setOther9(String other9) {
        this.other9 = other9;
    }

    public String getOther10() {
        return other10;
    }

    public void setOther10(String other10) {
        this.other10 = other10;
    }

    public String getOther11() {
        return other11;
    }

    public void setOther11(String other11) {
        this.other11 = other11;
    }

    public String getOther12() {
        return other12;
    }

    public void setOther12(String other12) {
        this.other12 = other12;
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
        if (!(object instanceof AutoAssignmentList)) {
            return false;
        }
        AutoAssignmentList other = (AutoAssignmentList) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.AutoAssignmentList[id=" + id + "]";
    }

}
