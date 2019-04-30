package com.maxelyz.core.model.value.admin;

import java.util.Date;

public class CreditCardUsageValue {
    private String customerName;
    private String insureName;
    private Date saleDate;
    private String saleBy;
    
    public CreditCardUsageValue(){}
    
    public CreditCardUsageValue(String customerName , String insureName, Date saleDate, String saleBy){
        this.customerName = customerName;
        this.insureName = insureName;
        this.saleDate = saleDate;
        this.saleBy = saleBy;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getInsureName() {
        return insureName;
    }

    public void setInsureName(String insureName) {
        this.insureName = insureName;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public String getSaleBy() {
        return saleBy;
    }

    public void setSaleBy(String saleBy) {
        this.saleBy = saleBy;
    }
    
    
    
}
