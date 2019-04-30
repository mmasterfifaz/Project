/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "temp_purchase_order")
@NamedQueries({
    @NamedQuery(name = "TempPurchaseOrder.findAll", query = "SELECT t FROM TempPurchaseOrder t")})
public class TempPurchaseOrder implements Serializable {
    @JoinColumn(name = "assignment_detail_id", referencedColumnName = "id")
    @ManyToOne
    private AssignmentDetail assignmentDetail;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "ref_no")
    private String refNo;
    @Basic(optional = false)
    @Column(name = "purchase_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date purchaseDate;
    @Column(name = "total_amount")
    private Double totalAmount;
    @Column(name = "channel_type")
    private String channelType;
    @Basic(optional = false)
    @Column(name = "sale_result")
    private char saleResult;
    @Column(name = "sale_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date saleDate;
    @Column(name = "followupsale_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date followupsaleDate;
    @Column(name = "billing_fullname")
    private String billingFullname;
    @Column(name = "billing_address_line1")
    private String billingAddressLine1;
    @Column(name = "billing_address_line2")
    private String billingAddressLine2;
    @Column(name = "billing_postal_code")
    private String billingPostalCode;
    @Column(name = "billing_phone")
    private String billingPhone;
    @Column(name = "shipping_fullname")
    private String shippingFullname;
    @Column(name = "shipping_address_line1")
    private String shippingAddressLine1;
    @Column(name = "shipping_address_line2")
    private String shippingAddressLine2;
    @Column(name = "shipping_postal_code")
    private String shippingPostalCode;
    @Column(name = "shipping_phone")
    private String shippingPhone;
    @Column(name = "payment_method")
    private String paymentMethod;
    @Column(name = "payment_type")
    private String paymentType;
    @Column(name = "card_type")
    private String cardType;
    @Column(name = "card_holder_name")
    private String cardHolderName;
    @Column(name = "card_expiry_month")
    private Integer cardExpiryMonth;
    @Column(name = "card_expiry_year")
    private Integer cardExpiryYear;
    @Column(name = "payment_amount")
    private Double paymentAmount;
    @Column(name = "trace_no")
    private String traceNo;
    @Column(name = "card_no")
    private String cardNo;
    @Column(name = "approval_status")
    private String approvalStatus;
    @Column(name = "approve_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approveDate;
    @Column(name = "approve_by")
    private String approveBy;
    @Column(name = "qc_status")
    private String qcStatus;
    @Column(name = "qc_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date qcDate;
    @Column(name = "qc_by")
    private String qcBy;
    @Column(name = "settlement")
    private Boolean settlement;
    @Column(name = "settlement_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date settlementDate;
    @Column(name = "settlement_by")
    private String settlementBy;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "create_by")
    private String createBy;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "update_by")
    private String updateBy;
    @Column(name = "fx1")
    private String fx1;
    @Column(name = "fx2")
    private String fx2;
    @Column(name = "fx3")
    private String fx3;
    @Column(name = "fx4")
    private String fx4;
    @Column(name = "fx5")
    private String fx5;
    @Column(name = "fx6")
    private String fx6;
    @Column(name = "fx7")
    private String fx7;
    @Column(name = "fx8")
    private String fx8;
    @Column(name = "fx9")
    private String fx9;
    @Column(name = "fx10")
    private String fx10;
    @Column(name = "fx11")
    private String fx11;
    @Column(name = "fx12")
    private String fx12;
    @Column(name = "fx13")
    private String fx13;
    @Column(name = "fx14")
    private String fx14;
    @Column(name = "fx15")
    private String fx15;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tempPurchaseOrder")
    private Collection<TempPurchaseOrderDetail> tempPurchaseOrderDetailCollection;
    @JoinColumn(name = "approve_by_id", referencedColumnName = "id")
    @ManyToOne
    private Users approveByUser;
    @JoinColumn(name = "update_by_id", referencedColumnName = "id")
    @ManyToOne
    private Users updateByUser;
    @JoinColumn(name = "qc_by_id", referencedColumnName = "id")
    @ManyToOne
    private Users qcByUser;
    @JoinColumn(name = "create_by_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users createByUser;
    @JoinColumn(name = "nosale_reason_id", referencedColumnName = "id")
    @ManyToOne
    private NosaleReason nosaleReason;
    @JoinColumn(name = "folowupsale_reason_id", referencedColumnName = "id")
    @ManyToOne
    private FollowupsaleReason followupsaleReason;
    @JoinColumn(name = "shipping_district_id", referencedColumnName = "id")
    @ManyToOne
    private District shippingDistrict;
    @JoinColumn(name = "billing_district_id", referencedColumnName = "id")
    @ManyToOne
    private District billingDistrict;
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Customer customer;

    public TempPurchaseOrder() {
    }

    public TempPurchaseOrder(Integer id) {
        this.id = id;
    }

    public TempPurchaseOrder(Integer id, String refNo, Date purchaseDate, char saleResult) {
        this.id = id;
        this.refNo = refNo;
        this.purchaseDate = purchaseDate;
        this.saleResult = saleResult;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public char getSaleResult() {
        return saleResult;
    }

    public void setSaleResult(char saleResult) {
        this.saleResult = saleResult;
    }

    public Date getFollowupsaleDate() {
        return followupsaleDate;
    }

    public void setFollowupsaleDate(Date followupsaleDate) {
        this.followupsaleDate = followupsaleDate;
    }

    public String getBillingFullname() {
        return billingFullname;
    }

    public void setBillingFullname(String billingFullname) {
        this.billingFullname = billingFullname;
    }

    public String getBillingAddressLine1() {
        return billingAddressLine1;
    }

    public void setBillingAddressLine1(String billingAddressLine1) {
        this.billingAddressLine1 = billingAddressLine1;
    }

    public String getBillingAddressLine2() {
        return billingAddressLine2;
    }

    public void setBillingAddressLine2(String billingAddressLine2) {
        this.billingAddressLine2 = billingAddressLine2;
    }

    public String getBillingPostalCode() {
        return billingPostalCode;
    }

    public void setBillingPostalCode(String billingPostalCode) {
        this.billingPostalCode = billingPostalCode;
    }

    public String getBillingPhone() {
        return billingPhone;
    }

    public void setBillingPhone(String billingPhone) {
        this.billingPhone = billingPhone;
    }

    public String getShippingFullname() {
        return shippingFullname;
    }

    public void setShippingFullname(String shippingFullname) {
        this.shippingFullname = shippingFullname;
    }

    public String getShippingAddressLine1() {
        return shippingAddressLine1;
    }

    public void setShippingAddressLine1(String shippingAddressLine1) {
        this.shippingAddressLine1 = shippingAddressLine1;
    }

    public String getShippingAddressLine2() {
        return shippingAddressLine2;
    }

    public void setShippingAddressLine2(String shippingAddressLine2) {
        this.shippingAddressLine2 = shippingAddressLine2;
    }

    public String getShippingPostalCode() {
        return shippingPostalCode;
    }

    public void setShippingPostalCode(String shippingPostalCode) {
        this.shippingPostalCode = shippingPostalCode;
    }

    public String getShippingPhone() {
        return shippingPhone;
    }

    public void setShippingPhone(String shippingPhone) {
        this.shippingPhone = shippingPhone;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
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

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public String getApproveBy() {
        return approveBy;
    }

    public void setApproveBy(String approveBy) {
        this.approveBy = approveBy;
    }

    public String getQcStatus() {
        return qcStatus;
    }

    public void setQcStatus(String qcStatus) {
        this.qcStatus = qcStatus;
    }

    public Date getQcDate() {
        return qcDate;
    }

    public void setQcDate(Date qcDate) {
        this.qcDate = qcDate;
    }

    public String getQcBy() {
        return qcBy;
    }

    public void setQcBy(String qcBy) {
        this.qcBy = qcBy;
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

    public Collection<TempPurchaseOrderDetail> getTempPurchaseOrderDetailCollection() {
        return tempPurchaseOrderDetailCollection;
    }

    public void setTempPurchaseOrderDetailCollection(Collection<TempPurchaseOrderDetail> tempPurchaseOrderDetailCollection) {
        this.tempPurchaseOrderDetailCollection = tempPurchaseOrderDetailCollection;
    }

    public Users getApproveByUser() {
        return approveByUser;
    }

    public void setApproveByUser(Users approveByUser) {
        this.approveByUser = approveByUser;
    }

    public Users getCreateByUser() {
        return createByUser;
    }

    public void setCreateByUser(Users createByUser) {
        this.createByUser = createByUser;
    }

    public Users getQcByUser() {
        return qcByUser;
    }

    public void setQcByUser(Users qcByUser) {
        this.qcByUser = qcByUser;
    }

    public Users getUpdateByUser() {
        return updateByUser;
    }

    public void setUpdateByUser(Users updateByUser) {
        this.updateByUser = updateByUser;
    }

    public NosaleReason getNosaleReason() {
        return nosaleReason;
    }

    public void setNosaleReason(NosaleReason nosaleReason) {
        this.nosaleReason = nosaleReason;
    }

    public FollowupsaleReason getFollowupsaleReason() {
        return followupsaleReason;
    }

    public void setFollowupsaleReason(FollowupsaleReason followupsaleReason) {
        this.followupsaleReason = followupsaleReason;
    }

    public District getBillingDistrict() {
        return billingDistrict;
    }

    public void setBillingDistrict(District billingDistrict) {
        this.billingDistrict = billingDistrict;
    }

    public District getShippingDistrict() {
        return shippingDistrict;
    }

    public void setShippingDistrict(District shippingDistrict) {
        this.shippingDistrict = shippingDistrict;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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
        if (!(object instanceof TempPurchaseOrder)) {
            return false;
        }
        TempPurchaseOrder other = (TempPurchaseOrder) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.TempPurchaseOrder[id=" + id + "]";
    }

    public AssignmentDetail getAssignmentDetail() {
        return assignmentDetail;
    }

    public void setAssignmentDetail(AssignmentDetail assignmentDetail) {
        this.assignmentDetail = assignmentDetail;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

}
