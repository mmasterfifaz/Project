/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.value.admin;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Administrator
 */
public class ExportPaymentValue {
    private String poRefNo;
    private BigDecimal installmentAmount;
    private Date purchaseDate;
    private String idNo;
    private String initialLabel;
    private String name;
    private String surname;
    private String cardNo;
    private String cardHolderName;
    private Integer cardExpiryMonth;
    private Integer cardExpiryYear;
    private String cardIssuerName;
    private String cardType;
    
    public ExportPaymentValue(String poRefNo, BigDecimal installmentAmount, Date purchaseDate, String idNo, String initialLabel, String name, String surname,
                 String cardNo, String cardHolderName, Integer cardExpiryMonth, Integer cardExpiryYear, String cardIssuerName, String cardType) {
        this.poRefNo = poRefNo;
        this.installmentAmount = installmentAmount;
        this.purchaseDate = purchaseDate;
        this.idNo = idNo;
        this.initialLabel = initialLabel;
        this.name = name;
        this.surname = surname;
        this.cardNo = cardNo;
        this.cardHolderName = cardHolderName;
        this.cardExpiryMonth = cardExpiryMonth;
        this.cardExpiryYear = cardExpiryYear;
        this.cardIssuerName = cardIssuerName;
        this.cardType = cardType;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getInitialLabel() {
        return initialLabel;
    }

    public void setInitialLabel(String initialLabel) {
        this.initialLabel = initialLabel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getPoRefNo() {
        return poRefNo;
    }

    public void setPoRefNo(String poRefNo) {
        this.poRefNo = poRefNo;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public BigDecimal getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(BigDecimal installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public Integer getCardExpiryMonth() {
        return cardExpiryMonth;
    }

    public void setCardExpiryMonth(Integer cardExpiryMonth) {
        this.cardExpiryMonth = cardExpiryMonth;
    }

    public Integer getCardExpiryYear() {
        return cardExpiryYear;
    }

    public void setCardExpiryYear(Integer cardExpiryYear) {
        this.cardExpiryYear = cardExpiryYear;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCardIssuerName() {
        return cardIssuerName;
    }

    public void setCardIssuerName(String cardIssuerName) {
        this.cardIssuerName = cardIssuerName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
    
    
}
