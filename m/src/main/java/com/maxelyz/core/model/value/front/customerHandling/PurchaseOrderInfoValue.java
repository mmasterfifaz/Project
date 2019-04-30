/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.value.front.customerHandling;

import java.util.Date;

/**
 *
 * @author Manop
 */
public class PurchaseOrderInfoValue {
    private Date contactDate;
    private String referenceNo;
    private String productNo;
    private String productName;
    private Integer quantity;
    private Double amount;
    private String updatedBy;
    private Date updatedDate;
    private String channelType;
    private Boolean approve;
    private Boolean qc;
    private Boolean settlement;

    public PurchaseOrderInfoValue(
            Date contactDate, String referenceNo, String productNo, String productName, Integer quantity,
            Double amount, String updatedBy, Date updatedDate, String channelType, Boolean approve, Boolean qc, Boolean settlement
            ){
        this.contactDate = contactDate;
        this.referenceNo = referenceNo;
        this.productNo = productNo;
        this.productName = productName;
        this.quantity = quantity;
        this.amount = amount;
        this.updatedBy = updatedBy;
        this.updatedDate = updatedDate;
        this.channelType = channelType;
        this.approve = approve;
        this.qc = qc;
        this.settlement = settlement;
    }

    /**
     * @return the contactDate
     */
    public Date getContactDate() {
        return contactDate;
    }

    /**
     * @param contactDate the contactDate to set
     */
    public void setContactDate(Date contactDate) {
        this.contactDate = contactDate;
    }

    /**
     * @return the referenceNo
     */
    public String getReferenceNo() {
        return referenceNo;
    }

    /**
     * @param referenceNo the referenceNo to set
     */
    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    /**
     * @return the productNo
     */
    public String getProductNo() {
        return productNo;
    }

    /**
     * @param productNo the productNo to set
     */
    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return the quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the amount
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * @return the updatedBy
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /**
     * @param updatedBy the updatedBy to set
     */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * @return the updatedDate
     */
    public Date getUpdatedDate() {
        return updatedDate;
    }

    /**
     * @param updatedDate the updatedDate to set
     */
    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    /**
     * @return the approve
     */
    public Boolean getApprove() {
        return approve;
    }

    /**
     * @param approve the approve to set
     */
    public void setApprove(Boolean approve) {
        this.approve = approve;
    }

    /**
     * @return the qc
     */
    public Boolean getQc() {
        return qc;
    }

    /**
     * @param qc the qc to set
     */
    public void setQc(Boolean qc) {
        this.qc = qc;
    }

    /**
     * @return the settlement
     */
    public Boolean getSettlement() {
        return settlement;
    }

    /**
     * @param settlement the settlement to set
     */
    public void setSettlement(Boolean settlement) {
        this.settlement = settlement;
    }

    /**
     * @return the channelType
     */
    public String getChannelType() {
        return channelType;
    }

    /**
     * @param channelType the channelType to set
     */
    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }
}
