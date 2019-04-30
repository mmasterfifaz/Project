/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.value.admin;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author admin
 */
public class SendDocumentValue {
    private Integer purchaseOrderSendDocumentId;
    private Date saleDate;
    private String refNo;
    private String customerName;
    private String itemNo;
    private String documentType;
    private Double amount;
    private String remark;
    
    public SendDocumentValue(Integer purchaseOrderSendDocumentId, Date saleDate, String refNo, String name, String surname, String itemNo, String documentType, Double amount, String remark){
        this.purchaseOrderSendDocumentId = purchaseOrderSendDocumentId;
        this.saleDate = saleDate;
        this.refNo = refNo;
        this.customerName = name + (!surname.equals("") ? (" " + surname) : "");
        this.itemNo = itemNo;
        this.documentType = documentType;
        this.amount = amount;
        this.remark = remark;
    
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public Integer getPurchaseOrderSendDocumentId() {
        return purchaseOrderSendDocumentId;
    }

    public void setPurchaseOrderSendDocumentId(Integer purchaseOrderSendDocumentId) {
        this.purchaseOrderSendDocumentId = purchaseOrderSendDocumentId;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }
    
}
