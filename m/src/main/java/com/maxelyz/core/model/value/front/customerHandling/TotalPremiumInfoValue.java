/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.value.front.customerHandling;

import java.util.Date;

/**
 *
 * @author Administrator
 */
public class TotalPremiumInfoValue {
    private Integer poId;
    private String refNo;
    private String insurePerson;
    private String gender;
    private Integer age;
    private String relationMainInsure;
    private String saleResult;
    private Date purchaseDate;
    private String approvalStatus;
    private String qcStatus;
    private String paymentStatus;
    private Double netPremium;

    public TotalPremiumInfoValue(Integer poId, String refNo, String insurePerson, String gender, Integer age, String relationMainInsure, Character saleResult,
                                 Date purchaseDate, String approvalStatus, String qcStatus, String paymentStatus, Double netPremium){
        this.poId = poId;
        this.refNo = refNo;
        this.insurePerson = insurePerson;
        this.gender = gender;
        this.age = age;
        
        if(relationMainInsure != null) {
            if(relationMainInsure.equals("S")) {
                this.relationMainInsure = "Spouse";
            } else if(relationMainInsure.equals("K")) {
                this.relationMainInsure = "Child";
            } else if(relationMainInsure.equals("P")) {
                this.relationMainInsure = "Parent";
            }
        } else {
            this.relationMainInsure = "Main Insure";
        }
        
        if(saleResult != null) {
            String status = String.valueOf(saleResult);        
            if(status.equals("Y")) {
                this.saleResult = "Yes";
            } else if(status.equals("F")) {
                this.saleResult = "Follow Up";
            } else {
                this.saleResult = "No";
            }
        }
        
        this.purchaseDate = purchaseDate;
        this.approvalStatus = approvalStatus;
        this.qcStatus = qcStatus; 
        this.paymentStatus = paymentStatus; 
        this.netPremium = netPremium; 
    }
     
    public Integer getPoId() {
        return poId;
    }

    public void setPoId(Integer poId) {
        this.poId = poId;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getInsurePerson() {
        return insurePerson;
    }

    public void setInsurePerson(String insurePerson) {
        this.insurePerson = insurePerson;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getRelationMainInsure() {
        return relationMainInsure;
    }

    public void setRelationMainInsure(String relationMainInsure) {
        this.relationMainInsure = relationMainInsure;
    }

    public String getSaleResult() {
        return saleResult;
    }

    public void setSaleResult(String saleResult) {
        this.saleResult = saleResult;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getQcStatus() {
        return qcStatus;
    }

    public void setQcStatus(String qcStatus) {
        this.qcStatus = qcStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Double getNetPremium() {
        return netPremium;
    }

    public void setNetPremium(Double netPremium) {
        this.netPremium = netPremium;
    }
    
    
}
