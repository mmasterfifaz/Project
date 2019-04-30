/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "purchase_order")
@NamedQueries({
    @NamedQuery(name = "PurchaseOrder.findAll", query = "SELECT p FROM PurchaseOrder p")})
public class PurchaseOrder implements Serializable {
    @Column(name = "purchase_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date purchaseDate;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total_amount", precision = 10, scale = 2)
    private Double totalAmount;
    @Column(name = "sale_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date saleDate;
    @Column(name = "followupsale_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date followupsaleDate;
    @Column(name = "payment_amount", precision = 10, scale = 2)
    private Double paymentAmount;
    @Column(name = "approve_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approveDate;
    @Column(name = "payment_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate;
    @Column(name = "qc_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date qcDate;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "due_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dueDate;
    @Column(name = "amount1", precision = 10, scale = 2)
    private Double amount1;
    @Column(name = "amount2", precision = 10, scale = 2)
    private Double amount2;
    @Column(name = "discount", precision = 10, scale = 2)
    private Double discount;
    @Column(name = "discount_percent", precision = 4, scale = 2)
    private Double discountPercent;
    @Column(name = "discount_type")
    private String discountType;
    @Column(name = "vat", precision = 10, scale = 2)
    private Double vat;
    @Column(name = "amount_bf_vat", precision = 10, scale = 2)
    private Double amountBfVat;
    @JoinColumn(name = "qc_qa_trans_id", referencedColumnName = "id")
    @ManyToOne
    private QaTrans qaTransQc;
    @JoinColumn(name = "payment_qa_trans_id", referencedColumnName = "id")
    @ManyToOne
    private QaTrans qaTransPayment;
    @JoinColumn(name = "approval_qa_trans_id", referencedColumnName = "id")
    @ManyToOne
    private QaTrans qaTransApproval;
    @JoinColumn(name = "assignment_detail_id", referencedColumnName = "id")
    @ManyToOne
    private AssignmentDetail assignmentDetail;
    @JoinColumn(name = "approval_reason_id", referencedColumnName = "id")
    @ManyToOne
    private ApprovalReason approvalReason;
    @JoinColumn(name = "payment_reason_id", referencedColumnName = "id")
    @ManyToOne
    private ApprovalReason paymentReason;
    @JoinColumn(name = "qc_reason_id", referencedColumnName = "id")
    @ManyToOne
    private ApprovalReason qcReason;
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "ref_no")
    private String refNo;
    @Column(name = "ref_no2")
    private String refNo2;
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Customer customer;
    @Column(name = "channel_type")
    private String channelType;
    @Basic(optional = false)
    @Column(name = "sale_result")
    private char saleResult;
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
    @Column(name = "payment_method_name")
    private String paymentMethodName;   
    @Column(name = "payment_type")
    private String paymentType;
    @Column(name = "card_type")
    private String cardType;
    @Column(name = "card_issuer")
    private String cardIssuer;
    @Column(name = "card_holder_name")
    private String cardHolderName;
    @Column(name = "card_expiry_month")
    private Integer cardExpiryMonth;
    @Column(name = "card_expiry_year")
    private Integer cardExpiryYear;
    @Column(name = "trace_no")
    private String traceNo;
    @Column(name = "card_no")
    private String cardNo;
    @Column(name = "approval_status")
    private String approvalStatus;
    @Column(name = "approve_by")
    private String approveBy;
    
    @Column(name = "payment_status")
    private String paymentStatus;
    @Column(name = "payment_by")
    private String paymentBy;
    
    @Column(name = "qc_status")
    private String qcStatus;
    @Column(name = "qc_by")
    private String qcBy;
    
    @Column(name = "approval_sup_status")
    private String approvalSupStatus;
    @Column(name = "approve_sup_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approveSupDate;
    @Column(name = "approve_sup_by")
    private String approveSupBy;
    @JoinColumn(name = "approve_sup_by_id", referencedColumnName = "id")
    @ManyToOne
    private Users approveSupByUser;
    
    @Column(name = "policy_no")
    private String policyNo;
    @Column(name = "issue_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date issueDate;
    @Column(name = "reason")
    private String reason;
    @Column(name = "reason_code")
    private String reasonCode;
    
    @Column(name = "settlement")
    private Boolean settlement;
    @Column(name = "settlement_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date settlementDate;
    @Column(name = "settlement_by")
    private String settlementBy;
    @Column(name = "create_by")
    private String createBy;
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
    
    @Column(name = "sum_insured")
    private Double sumInsured;
    @Column(name = "price")
    private Double price;
    @Column(name = "stamp_duty")
    private Double stampDuty;
    @Column(name = "net_premium")
    private Double netPremium;
    @Column(name = "annual_net_premium")
    private Double annualNetPremium;
    @Column(name = "annual_price")
    private Double annualPrice;
  
    @Column(name = "other_exclusion_code")
    private String otherExclusionCode;
    @Column(name = "other_exclusion_name")
    private String otherExclusionName;
    
    @Column(name = "no_installment")
    private Integer noInstallment; 
    @Column(name = "model_year")
    private Integer modelYear; 
    @Column(name = "latest_reason")
    private String latestReason;
    @Column(name = "remark")
    private String remark;
    @Column(name = "remark2")
    private String remark2;
    
    @Column(name = "effective_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date effctiveDate;
    
    @Column(name = "delivery_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveryDate;
    @JoinColumn(name = "delivery_method_id", referencedColumnName = "id")
    @ManyToOne
    private DeliveryMethod deliveryMethod;
    @Column(name = "delivery_description")
    private String deliveryDescription;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "purchaseOrder")
    private Collection<PurchaseOrderDetail> purchaseOrderDetailCollection;
    
    @JoinColumn(name = "update_by_id", referencedColumnName = "id")
    @ManyToOne
    private Users updateByUser;
    @JoinColumn(name = "qc_by_id", referencedColumnName = "id")
    @ManyToOne
    private Users qcByUser;
    @JoinColumn(name = "create_by_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users createByUser;
    @JoinColumn(name = "approve_by_id", referencedColumnName = "id")
    @ManyToOne
    private Users approveByUser;
    @JoinColumn(name = "payment_by_id", referencedColumnName = "id")
    @ManyToOne
    private Users paymentByUser;
    @JoinColumn(name = "nosale_reason_id", referencedColumnName = "id")
    @ManyToOne
    private NosaleReason nosaleReason;
    @JoinColumn(name = "followupsale_reason_id", referencedColumnName = "id")
    @ManyToOne
    private FollowupsaleReason followupsaleReason;
    @JoinColumn(name = "shipping_district_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne
    private District shippingDistrict;
    @JoinColumn(name = "billing_district_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne
    private District billingDistrict;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "purchaseOrder")
    private Collection<PurchaseOrderApprovalLog> purchaseOrderApprovalLogCollection;
    @ManyToMany(mappedBy = "purchaseOrderCollection")
    private Collection<ContactHistory> contactHistoryCollection;
    
    @Column(name = "first_damage", precision = 10, scale = 2)
    private Double firstDamage;
    @Column(name = "discount_group", precision = 10, scale = 2)
    private Double discountGroup;
    @Column(name = "discount_good_record", precision = 10, scale = 2)
    private Double discountGoodRecord;
    
    @Column(name = "insure_person")
    private String insurePerson;
    @Column(name = "idno")
    private String idno;
    @Column(name = "latest_status")
    private String latestStatus;
    @Column(name = "latest_delegate_to_role")
    private String latestDelegateToRole;
    @Column(name = "latest_delegate_from_role")
    private String latestDelegateFromRole;
    @Column(name = "latest_status_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date latestStatusDate;
    @Column(name = "latest_status_by")
    private String latestStatusBy;
    @JoinColumn(name = "latest_status_by_id", referencedColumnName = "id")
    @ManyToOne
    private Users latestStatusByUser;
    @Column(name = "yes_flag")
    private Boolean yesFlag;
    @Column(name = "yes_gen_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date yesGenDate;
    @Column(name = "yes_gen_by")
    private String yesGenBy;
    
    @Column(name = "payer_same_as_insured")
    private Boolean payerSameAsInsured;
    @Column(name = "payer_initial")
    private String payerInitial;
    @Column(name = "payer_initial_text")
    private String payerInitialText;
    @Column(name = "payer_name")
    private String payerName;
    @Column(name = "payer_surname")
    private String payerSurname;
    @Column(name = "payer_idcard")
    private String payerIdcard;
    @Column(name = "payer_idcard_expiry_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date payerIdcardExpiryDate;
    @Column(name = "payer_gender")
    private String payerGender;
    @Column(name = "payer_marital_status")
    private String payerMaritalStatus;
    @Column(name = "payer_dob")
    @Temporal(TemporalType.TIMESTAMP)
    private Date payerDob;
    @Column(name = "payer_relation")
    private String payerRelation;

    @JoinColumn(name = "bank_id", referencedColumnName = "id")
    @ManyToOne
    private Bank bank;
    
    @Column(name = "payer_home_phone")
    private String payerHomePhone;
    @Column(name = "payer_mobile_phone")
    private String payerMobilePhone;
    @Column(name = "payer_office_phone")
    private String payerOfficePhone;
    @Column(name = "payer_email")
    private String payerEmail;
    @Column(name = "paid_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date paidDate;
    
    @Column(name = "payer_idtype")
    private String payerIdtype;
    @Column(name = "payer_address1")
    private String payerAddress1;
    @Column(name = "payer_address2")
    private String payerAddress2;
    @Column(name = "payer_address3")
    private String payerAddress3;
    @JoinColumn(name = "payer_subdistrict_id", referencedColumnName = "id")
    @ManyToOne
    private Subdistrict payerSubDistrict;
    @JoinColumn(name = "payer_district_id", referencedColumnName = "id")
    @ManyToOne
    private District payerDistrict;
    @Column(name = "payer_postal_code")
    private String payerPostalCode;
    @Column(name = "payer_occupation_code")
    private String payerOccupationCode;
    @Column(name = "payer_type")
    private Integer payerType;
    @JoinColumn(name = "payer_occupation_id", referencedColumnName = "id")
    @ManyToOne
    private Occupation payerOccupation;
    @Column(name = "payer_occupation_other")
    private String payerOccupationOther;
    @Column(name = "payer_occupation_position")
    private String payerOccupationPosition;

    @Column(name = "confirm_flag")
    private Boolean confirmFlag;
    @Column(name = "confirm_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date confirmDate;
    @Column(name = "confirm_by")
    private String confirmBy;
    @JoinColumn(name = "confirm_by_id", referencedColumnName = "id")
    @ManyToOne
    private Users confirmByUser;
    @Column(name = "product_plan_code")
    private String productPlanCode;
    @Column(name = "product_plan_name")
    private String productPlanName;
    @JoinColumn(name = "parent_purchase_order_id", referencedColumnName = "id")
    @ManyToOne
    private PurchaseOrder parentPurchaseOrder;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentPurchaseOrder", fetch = FetchType.EAGER)
    private Collection<PurchaseOrder> purchaseOrderCollection;
    @Column(name = "bank_account_no")
    private String bankAccountNo;
    
    @Column(name = "payment_account_no")
    private String paymentAccountNo;
    @Column(name = "payment_expected_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentExpectedDate;
    @Column(name = "payment_remarks")
    private String paymentRemarks;
    
    @Column(name = "product_plan_detail_info")
    private String productPlanDetailInfo;
    
    @Column(name = "main_po_id")
    private Integer mainPoId;

    @Column(name = "preapproval_status")
    private String preapprovalStatus;

    @Column(name = "preapproval_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date preapprovalDate;

    @Column(name = "preapproval_by")
    private String preapprovalBy;

    @Column(name = "payment_gateway_submit_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentGatewaySubmitDate;

    @Column(name = "payment_gateway_result")
    private String paymentGatewayResult;

    @Column(name = "payment_gateway_url")
    private String paymentGatewayUrl;

    @Column(name = "sms_submit_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date smsSubmitDate;

    @Column(name = "sms_result")
    private String smsResult;

    @Column(name = "payment_submit_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentSubmitDate;

    @Column(name = "payment_reference_no1")
    private String paymentReferenceNo1;

    @Column(name = "channel_cash_type")
    private String channelCashType;

    @Column(name = "channel_cash_code")
    private String channelCashCode;

    @Column(name = "agent_cash_code")
    private String agentCashCode;

    @Column(name = "payment_count_no")
    private String paymentCountNo;


    public PurchaseOrder() {
    }

    public PurchaseOrder(Integer id) {
        this.id = id;
    }

    public PurchaseOrder(Integer id, String refNo, Date purchaseDate, char saleResult) {
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

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
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
    
    public String getPaymentAccountNo() {
        return paymentAccountNo;
    }

    public void setPaymentAccountNo(String paymentAccountNo) {
        this.paymentAccountNo = paymentAccountNo;
    }
    
    public Date getPaymentExpectedDate() {
        return paymentExpectedDate;
    }

    public void setPaymentExpectedDate(Date paymentExpectedDate) {
        this.paymentExpectedDate = paymentExpectedDate;
    }
    
    public String getPaymentRemarks() {
        return paymentRemarks;
    }

    public void setPaymentRemarks(String paymentRemarks) {
        this.paymentRemarks = paymentRemarks;
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

    public Users getPaymentByUser() {
        return paymentByUser;
    }

    public void setPaymentByUser(Users paymentByUser) {
        this.paymentByUser = paymentByUser;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentBy() {
        return paymentBy;
    }

    public void setPaymentBy(String paymentBy) {
        this.paymentBy = paymentBy;
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

    public Collection<PurchaseOrderDetail> getPurchaseOrderDetailCollection() {
        return purchaseOrderDetailCollection;
    }

    public void setPurchaseOrderDetailCollection(Collection<PurchaseOrderDetail> purchaseOrderDetailCollection) {
        this.purchaseOrderDetailCollection = purchaseOrderDetailCollection;
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

    public String getCardIssuer() {
        return cardIssuer;
    }

    public void setCardIssuer(String cardIssuer) {
        this.cardIssuer = cardIssuer;
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
        if (!(object instanceof PurchaseOrder)) {
            return false;
        }
        PurchaseOrder other = (PurchaseOrder) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.PurchaseOrder[id=" + id + "]";
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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

    public Collection<PurchaseOrderApprovalLog> getPurchaseOrderApprovalLogCollection() {
        return purchaseOrderApprovalLogCollection;
    }

    public void setPurchaseOrderApprovalLogCollection(Collection<PurchaseOrderApprovalLog> purchaseOrderApprovalLogCollection) {
        this.purchaseOrderApprovalLogCollection = purchaseOrderApprovalLogCollection;
    }

    public Collection<ContactHistory> getContactHistoryCollection() {
        return contactHistoryCollection;
    }

    public void setContactHistoryCollection(Collection<ContactHistory> contactHistoryCollection) {
        this.contactHistoryCollection = contactHistoryCollection;
    }

    public AssignmentDetail getAssignmentDetail() {
        return assignmentDetail;
    }

    public void setAssignmentDetail(AssignmentDetail assignmentDetail) {
        this.assignmentDetail = assignmentDetail;
    }

    public Double getAmountBfVat() {
        return amountBfVat;
    }

    public void setAmountBfVat(Double amountBfVat) {
        this.amountBfVat = amountBfVat;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getNoInstallment() {
        return noInstallment;
    }

    public void setNoInstallment(Integer noInstallment) {
        this.noInstallment = noInstallment;
    }


    public Double getAmount1() {
        return amount1;
    }

    public void setAmount1(Double amount1) {
        this.amount1 = amount1;
    }

    public Double getAmount2() {
        return amount2;
    }

    public void setAmount2(Double amount2) {
        this.amount2 = amount2;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getVat() {
        return vat;
    }

    public void setVat(Double vat) {
        this.vat = vat;
    }

    public QaTrans getQaTransQc() {
        return qaTransQc;
    }

    public void setQaTransQc(QaTrans qaTransQc) {
        this.qaTransQc = qaTransQc;
    }

    public QaTrans getQaTransApproval() {
        return qaTransApproval;
    }

    public void setQaTransApproval(QaTrans qaTransApproval) {
        this.qaTransApproval = qaTransApproval;
    }

    public QaTrans getQaTransPayment() {
        return qaTransPayment;
    }

    public void setQaTransPayment(QaTrans qaTransPayment) {
        this.qaTransPayment = qaTransPayment;
    }

    public Integer getModelYear() {
        return modelYear;
    }

    public void setModelYear(Integer modelYear) {
        this.modelYear = modelYear;
    }

    public String getLatestReason() {
        return latestReason;
    }

    public void setLatestReason(String latestReason) {
        this.latestReason = latestReason;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRefNo2() {
        return refNo2;
    }

    public void setRefNo2(String refNo2) {
        this.refNo2 = refNo2;
    }

    public ApprovalReason getApprovalReason() {
        return approvalReason;
    }

    public void setApprovalReason(ApprovalReason approvalReason) {
        this.approvalReason = approvalReason;
    }

    public ApprovalReason getPaymentReason() {
        return paymentReason;
    }

    public void setPaymentReason(ApprovalReason paymentReason) {
        this.paymentReason = paymentReason;
    }

    public ApprovalReason getQcReason() {
        return qcReason;
    }

    public void setQcReason(ApprovalReason qcReason) {
        this.qcReason = qcReason;
    }

    public String getDeliveryDescription() {
        return deliveryDescription;
    }

    public void setDeliveryDescription(String deliveryDescription) {
        this.deliveryDescription = deliveryDescription;
    }

    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Date getEffctiveDate() {
        return effctiveDate;
    }

    public void setEffctiveDate(Date effctiveDate) {
        this.effctiveDate = effctiveDate;
    }

    public String getOtherExclusionCode() {
        return otherExclusionCode;
    }

    public void setOtherExclusionCode(String otherExclusionCode) {
        this.otherExclusionCode = otherExclusionCode;
    }

    public String getOtherExclusionName() {
        return otherExclusionName;
    }

    public void setOtherExclusionName(String otherExclusionName) {
        this.otherExclusionName = otherExclusionName;
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

    public Double getNetPremium() {
        return netPremium;
    }

    public void setNetPremium(Double netPremium) {
        this.netPremium = netPremium;
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

    public Double getSumInsured() {
        return sumInsured;
    }

    public void setSumInsured(Double sumInsured) {
        this.sumInsured = sumInsured;
    }

    public Double getFirstDamage() {
        return firstDamage;
    }

    public void setFirstDamage(Double firstDamage) {
        this.firstDamage = firstDamage;
    }

    public Double getDiscountGroup() {
        return discountGroup;
    }

    public void setDiscountGroup(Double discountGroup) {
        this.discountGroup = discountGroup;
    }

    public Double getDiscountGoodRecord() {
        return discountGoodRecord;
    }

    public void setDiscountGoodRecord(Double discountGoodRecord) {
        this.discountGoodRecord = discountGoodRecord;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public String getInsurePerson() {
        return insurePerson;
    }

    public void setInsurePerson(String insurePerson) {
        this.insurePerson = insurePerson;
    }
    
    public String getApprovalSupStatus() {
        return approvalSupStatus;
}

    public void setApprovalSupStatus(String approvalSupStatus) {
        this.approvalSupStatus = approvalSupStatus;
    }

    public Date getApproveSupDate() {
        return approveSupDate;
    }

    public void setApproveSupDate(Date approveSupDate) {
        this.approveSupDate = approveSupDate;
    }

    public String getApproveSupBy() {
        return approveSupBy;
    }

    public void setApproveSupBy(String approveSupBy) {
        this.approveSupBy = approveSupBy;
    }

    public Users getApproveSupByUser() {
        return approveSupByUser;
    }

    public void setApproveSupByUser(Users approveSupByUser) {
        this.approveSupByUser = approveSupByUser;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getLatestStatus() {
        return latestStatus;
    }

    public void setLatestStatus(String latestStatus) {
        this.latestStatus = latestStatus;
    }

    public String getLatestDelegateToRole() {
        return latestDelegateToRole;
    }

    public void setLatestDelegateToRole(String latestDelegateToRole) {
        this.latestDelegateToRole = latestDelegateToRole;
    }

    public Date getLatestStatusDate() {
        return latestStatusDate;
    }

    public void setLatestStatusDate(Date latestStatusDate) {
        this.latestStatusDate = latestStatusDate;
    }

    public String getLatestStatusBy() {
        return latestStatusBy;
    }

    public void setLatestStatusBy(String latestStatusBy) {
        this.latestStatusBy = latestStatusBy;
    }

    public Users getLatestStatusByUser() {
        return latestStatusByUser;
    }

    public void setLatestStatusByUser(Users latestStatusByUser) {
        this.latestStatusByUser = latestStatusByUser;
    }

    public String getLatestDelegateFromRole() {
        return latestDelegateFromRole;
    }

    public void setLatestDelegateFromRole(String latestDelegateFromRole) {
        this.latestDelegateFromRole = latestDelegateFromRole;
    }

    public Boolean getYesFlag() {
        return yesFlag;
    }

    public void setYesFlag(Boolean yesFlag) {
        this.yesFlag = yesFlag;
    }

    public Date getYesGenDate() {
        return yesGenDate;
    }

    public void setYesGenDate(Date yesGenDate) {
        this.yesGenDate = yesGenDate;
    }

    public String getYesGenBy() {
        return yesGenBy;
    }

    public void setYesGenBy(String yesGenBy) {
        this.yesGenBy = yesGenBy;
    }

    public Boolean getPayerSameAsInsured() {
        return payerSameAsInsured;
    }

    public void setPayerSameAsInsured(Boolean payerSameAsInsured) {
        this.payerSameAsInsured = payerSameAsInsured;
    }

    public String getPayerInitial() {
        return payerInitial;
    }

    public void setPayerInitial(String payerInitial) {
        this.payerInitial = payerInitial;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPayerSurname() {
        return payerSurname;
    }

    public void setPayerSurname(String payerSurname) {
        this.payerSurname = payerSurname;
    }

    public String getPayerGender() {
        return payerGender;
    }

    public void setPayerGender(String payerGender) {
        this.payerGender = payerGender;
    }

    public String getPayerMaritalStatus() {
        return payerMaritalStatus;
    }

    public void setPayerMaritalStatus(String payerMaritalStatus) {
        this.payerMaritalStatus = payerMaritalStatus;
    }

    public Date getPayerDob() {
        return payerDob;
    }

    public void setPayerDob(Date payerDob) {
        this.payerDob = payerDob;
    }

    public String getPayerRelation() {
        return payerRelation;
    }

    public void setPayerRelation(String payerRelation) {
        this.payerRelation = payerRelation;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public String getPayerHomePhone() {
        return payerHomePhone;
    }

    public void setPayerHomePhone(String payerHomePhone) {
        this.payerHomePhone = payerHomePhone;
    }

    public String getPayerMobilePhone() {
        return payerMobilePhone;
    }

    public void setPayerMobilePhone(String payerMobilePhone) {
        this.payerMobilePhone = payerMobilePhone;
    }

    public Date getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(Date paidDate) {
        this.paidDate = paidDate;
    }

    public Boolean getConfirmFlag() {
        return confirmFlag;
    }

    public void setConfirmFlag(Boolean confirmFlag) {
        this.confirmFlag = confirmFlag;
    }

    public Date getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(Date confirmDate) {
        this.confirmDate = confirmDate;
    }

    public String getConfirmBy() {
        return confirmBy;
    }

    public void setConfirmBy(String confirmBy) {
        this.confirmBy = confirmBy;
    }

    public Users getConfirmByUser() {
        return confirmByUser;
    }

    public void setConfirmByUser(Users confirmByUser) {
        this.confirmByUser = confirmByUser;
    }

    public String getPayerIdcard() {
        return payerIdcard;
    }

    public void setPayerIdcard(String payerIdcard) {
        this.payerIdcard = payerIdcard;
    }

    public String getProductPlanCode() {
        return productPlanCode;
    }

    public void setProductPlanCode(String productPlanCode) {
        this.productPlanCode = productPlanCode;
    }

    public String getProductPlanName() {
        return productPlanName;
    }

    public void setProductPlanName(String productPlanName) {
        this.productPlanName = productPlanName;
    }

    public PurchaseOrder getParentPurchaseOrder() {
        return parentPurchaseOrder;
    }

    public void setParentPurchaseOrder(PurchaseOrder parentPurchaseOrder) {
        this.parentPurchaseOrder = parentPurchaseOrder;
    }

    public Collection<PurchaseOrder> getPurchaseOrderCollection() {
        return purchaseOrderCollection;
    }

    public void setPurchaseOrderCollection(Collection<PurchaseOrder> purchaseOrderCollection) {
        this.purchaseOrderCollection = purchaseOrderCollection;
    }

    public String getPayerOfficePhone() {
        return payerOfficePhone;
    }

    public void setPayerOfficePhone(String payerOfficePhone) {
        this.payerOfficePhone = payerOfficePhone;
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public void setPayerEmail(String payerEmail) {
        this.payerEmail = payerEmail;
    }

    public String getPayerIdtype() {
        return payerIdtype;
    }

    public void setPayerIdtype(String payerIdtype) {
        this.payerIdtype = payerIdtype;
    }

    public String getPayerAddress1() {
        return payerAddress1;
    }

    public void setPayerAddress1(String payerAddress1) {
        this.payerAddress1 = payerAddress1;
    }

    public String getPayerAddress2() {
        return payerAddress2;
    }

    public void setPayerAddress2(String payerAddress2) {
        this.payerAddress2 = payerAddress2;
    }

    public String getPayerAddress3() {
        return payerAddress3;
    }

    public void setPayerAddress3(String payerAddress3) {
        this.payerAddress3 = payerAddress3;
    }

    public Subdistrict getPayerSubDistrict() {
        return payerSubDistrict;
    }

    public void setPayerSubDistrict(Subdistrict payerSubDistrict) {
        this.payerSubDistrict = payerSubDistrict;
    }

    public District getPayerDistrict() {
        return payerDistrict;
    }

    public void setPayerDistrict(District payerDistrict) {
        this.payerDistrict = payerDistrict;
    }

    public String getPayerPostalCode() {
        return payerPostalCode;
    }

    public void setPayerPostalCode(String payerPostalCode) {
        this.payerPostalCode = payerPostalCode;
    }

    public String getPayerOccupationCode() {
        return payerOccupationCode;
    }

    public void setPayerOccupationCode(String payerOccupationCode) {
        this.payerOccupationCode = payerOccupationCode;
    }

    public Integer getPayerType() {
        return payerType;
    }

    public void setPayerType(Integer payerType) {
        this.payerType = payerType;
    }

    public Occupation getPayerOccupation() {
        return payerOccupation;
    }

    public void setPayerOccupation(Occupation payerOccupation) {
        this.payerOccupation = payerOccupation;
    }

    public String getPayerInitialText() {
        return payerInitialText;
    }

    public void setPayerInitialText(String payerInitialText) {
        this.payerInitialText = payerInitialText;
    }

    public String getPayerOccupationOther() {
        return payerOccupationOther;
    }

    public void setPayerOccupationOther(String payerOccupationOther) {
        this.payerOccupationOther = payerOccupationOther;
    }

    public String getPayerOccupationPosition() {
        return payerOccupationPosition;
    }

    public void setPayerOccupationPosition(String payerOccupationPosition) {
        this.payerOccupationPosition = payerOccupationPosition;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public Date getPayerIdcardExpiryDate() {
        return payerIdcardExpiryDate;
    }

    public void setPayerIdcardExpiryDate(Date payerIdcardExpiryDate) {
        this.payerIdcardExpiryDate = payerIdcardExpiryDate;
    }

    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }

    public String getPaymentMethodName() {
        return paymentMethodName;
    }

    public void setPaymentMethodName(String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }

    public String getProductPlanDetailInfo() {
        return productPlanDetailInfo;
    }

    public void setProductPlanDetailInfo(String productPlanDetailInfo) {
        this.productPlanDetailInfo = productPlanDetailInfo;
    }

    public Integer getMainPoId() {
        return mainPoId;
    }

    public void setMainPoId(Integer mainPoId) {
        this.mainPoId = mainPoId;
    }

    public Double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public Date getPaymentGatewaySubmitDate() {
        return paymentGatewaySubmitDate;
    }

    public void setPaymentGatewaySubmitDate(Date paymentGatewaySubmitDate) {
        this.paymentGatewaySubmitDate = paymentGatewaySubmitDate;
    }

    public String getPaymentGatewayResult() {
        return paymentGatewayResult;
    }

    public void setPaymentGatewayResult(String paymentGatewayResult) {
        this.paymentGatewayResult = paymentGatewayResult;
    }

    public String getPaymentGatewayUrl() {
        return paymentGatewayUrl;
    }

    public void setPaymentGatewayUrl(String paymentGatewayUrl) {
        this.paymentGatewayUrl = paymentGatewayUrl;
    }

    public Date getSmsSubmitDate() {
        return smsSubmitDate;
    }

    public void setSmsSubmitDate(Date smsSubmitDate) {
        this.smsSubmitDate = smsSubmitDate;
    }

    public String getSmsResult() {
        return smsResult;
    }

    public void setSmsResult(String smsResult) {
        this.smsResult = smsResult;
    }

    public Date getPaymentSubmitDate() {
        return paymentSubmitDate;
    }

    public void setPaymentSubmitDate(Date paymentSubmitDate) {
        this.paymentSubmitDate = paymentSubmitDate;
    }

    public String getPreapprovalStatus() {
        return preapprovalStatus;
    }

    public void setPreapprovalStatus(String preapprovalStatus) {
        this.preapprovalStatus = preapprovalStatus;
    }

    public Date getPreapprovalDate() {
        return preapprovalDate;
    }

    public void setPreapprovalDate(Date preapprovalDate) {
        this.preapprovalDate = preapprovalDate;
    }

    public String getPreapprovalBy() {
        return preapprovalBy;
    }

    public void setPreapprovalBy(String preapprovalBy) {
        this.preapprovalBy = preapprovalBy;
    }

    public String getPaymentReferenceNo1() {
        return paymentReferenceNo1;
    }

    public void setPaymentReferenceNo1(String paymentReferenceNo1) {
        this.paymentReferenceNo1 = paymentReferenceNo1;
    }


    public String getAgentCashCode() {
        return agentCashCode;
    }

    public String getChannelCashType() {
        return channelCashType;
    }

    public void setChannelCashType(String channelCashType) {
        this.channelCashType = channelCashType;
    }

    public String getChannelCashCode() {
        return channelCashCode;
    }

    public void setChannelCashCode(String channelCashCode) {
        this.channelCashCode = channelCashCode;
    }

    public void setAgentCashCode(String agentCashCode) {
        this.agentCashCode = agentCashCode;
    }

    public String getPaymentCountNo() {
        return paymentCountNo;
    }

    public void setPaymentCountNo(String paymentCountNo) {
        this.paymentCountNo = paymentCountNo;
    }
}
