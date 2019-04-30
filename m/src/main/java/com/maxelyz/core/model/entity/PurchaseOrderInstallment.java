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
@Table(name = "purchase_order_installment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PurchaseOrderInstallment.findAll", query = "SELECT p FROM PurchaseOrderInstallment p")})
public class PurchaseOrderInstallment implements Serializable {
    @Column(name = "due_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dueDate;
    @Column(name = "paid_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date paidDate;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "settlement")
    private Boolean settlement;
    @Column(name = "settlement_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date settlementDate;
    @Column(name = "settlement_by", length = 50)
    private String settlementBy;
    @Column(name = "installment_ref_no", length = 50)
    private String installmentRefNo;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "installment_no", nullable = false)
    private int installmentNo;
    @JoinColumn(name = "payment_method_id", referencedColumnName = "id")
    @ManyToOne
    private PaymentMethod paymentMethod;
    @Column(name = "payment_type", length = 50)
    private String paymentType;
    @Column(name = "card_type", length = 50)
    private String cardType;
    @Column(name = "card_holder_name", length = 100)
    private String cardHolderName;
    @Column(name = "card_expiry_month")
    private Integer cardExpiryMonth;
    @Column(name = "card_expiry_year")
    private Integer cardExpiryYear;
    @Column(name = "card_no", length = 50)
    private String cardNo;
    @Column(name = "trace_no", length = 50)
    private String traceNo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "installment_amount", precision = 6, scale = 2)
    private BigDecimal installmentAmount;
    @Basic(optional = false)
    @Column(name = "payment_status", nullable = false, length = 50)
    private String paymentStatus;
    @Column(name = "remark", length = 500)
    private String remark;
    @Column(name = "ref_no", length = 50)
    private String refNo;
    @Column(name = "receive_from", length = 50)
    private String receiveFrom;
    @JoinColumn(name = "purchase_order_id", referencedColumnName = "id")
    @ManyToOne
    private PurchaseOrder purchaseOrder;
    @Column(name = "update_by", length = 50)
    private String updateBy;
    @Column(name = "update_by_id")
    private Integer updateById;
    @Column(name = "card_issuer_name")
    private String cardIssuerName;
    @Column(name = "card_issuer_country")
    private String cardIssuerCountry;
    @Column(name = "send_sms")
    private Boolean sendSms;
    @Column(name = "sms_no")
    private String smsNo;
    @Column(name = "send_email")
    private Boolean sendEmail;
    @Column(name = "email_address")
    private String emailAddress;
    @Column(name = "auth_id")
    private String authId;
    

    public PurchaseOrderInstallment() {
    }

    public PurchaseOrderInstallment(Integer id) {
        this.id = id;
    }

    public PurchaseOrderInstallment(Integer id, int installmentNo, String paymentStatus) {
        this.id = id;
        this.installmentNo = installmentNo;
        this.paymentStatus = paymentStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getInstallmentNo() {
        return installmentNo;
    }

    public void setInstallmentNo(int installmentNo) {
        this.installmentNo = installmentNo;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
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

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(BigDecimal installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public Date getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(Date paidDate) {
        this.paidDate = paidDate;
    }

    public String getReceiveFrom() {
        return receiveFrom;
    }

    public void setReceiveFrom(String receiveFrom) {
        this.receiveFrom = receiveFrom;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Integer getUpdateById() {
        return updateById;
    }

    public void setUpdateById(Integer updateById) {
        this.updateById = updateById;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
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
        if (!(object instanceof PurchaseOrderInstallment)) {
            return false;
        }
        PurchaseOrderInstallment other = (PurchaseOrderInstallment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.PurchaseOrderInstallment[ id=" + id + " ]";
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public Boolean getSettlement() {
        return settlement;
    }

    public void setSettlement(Boolean settlement) {
        this.settlement = settlement;
    }

    public Date getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(Date settlementDate) {
        this.settlementDate = settlementDate;
    }

    public String getSettlementBy() {
        return settlementBy;
    }

    public void setSettlementBy(String settlementBy) {
        this.settlementBy = settlementBy;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCardIssuerCountry() {
        return cardIssuerCountry;
    }

    public void setCardIssuerCountry(String cardIssuerCountry) {
        this.cardIssuerCountry = cardIssuerCountry;
    }

    public String getCardIssuerName() {
        return cardIssuerName;
    }

    public void setCardIssuerName(String cardIssuerName) {
        this.cardIssuerName = cardIssuerName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Boolean getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(Boolean sendEmail) {
        this.sendEmail = sendEmail;
    }

    public Boolean getSendSms() {
        return sendSms;
    }

    public void setSendSms(Boolean sendSms) {
        this.sendSms = sendSms;
    }

    public String getSmsNo() {
        return smsNo;
    }

    public void setSmsNo(String smsNo) {
        this.smsNo = smsNo;
    }

    public String getInstallmentRefNo() {
        return installmentRefNo;
    }

    public void setInstallmentRefNo(String installmentRefNo) {
        this.installmentRefNo = installmentRefNo;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }
    
}
