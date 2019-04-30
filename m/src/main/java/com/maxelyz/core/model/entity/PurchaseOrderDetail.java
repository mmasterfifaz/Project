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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Cascade;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "purchase_order_detail")
@NamedQueries({
    @NamedQuery(name = "PurchaseOrderDetail.findAll", query = "SELECT p FROM PurchaseOrderDetail p")})
public class PurchaseOrderDetail implements Serializable {
    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "purchaseOrderDetail")    
    @OneToMany(orphanRemoval=true, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "purchaseOrderDetail", fetch = FetchType.EAGER)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
        org.hibernate.annotations.CascadeType.DELETE,
        org.hibernate.annotations.CascadeType.MERGE,
        org.hibernate.annotations.CascadeType.PERSIST,
        org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private Collection<PurchaseOrderRegister> purchaseOrderRegisterCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "purchaseOrderDetail")
    private Collection<PurchaseOrderChildRegister> purchaseOrderChildRegisterCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "purchaseOrderDetail")
    private Collection<PurchaseOrderDeclaration> purchaseOrderDeclarationCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "purchaseOrderDetail")
    private Collection<PurchaseOrderQuestionaire> purchaseOrderQuestionaireCollection;
    @Column(name = "payment_mode")
    private String paymentMode;
    @Column(name = "delivery_method")
    private String deliveryMethod;
    @JoinColumn(name = "product_plan_id", referencedColumnName = "id")
    @ManyToOne
    private ProductPlan productPlan;
    @JoinColumn(name = "product_plan_detail_id", referencedColumnName = "id")
    @ManyToOne
    private ProductPlanDetail productPlanDetail;
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Product product;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "unit_price")
    private Double unitPrice;
    @Basic(optional = false)
    @Column(name = "quantity")
    private int quantity;
    @Basic(optional = false)
    @Column(name = "amount")
    private Double amount;
    @Column(name = "monthly_first_payment")
    private Integer monthlyFirstPayment;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @JoinColumn(name = "purchase_order_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private PurchaseOrder purchaseOrder;
    @JoinColumn(name = "assignment_detail_id", referencedColumnName = "id")
    @ManyToOne
    private AssignmentDetail assignmentDetail;

    public PurchaseOrderDetail() {
    }

    public PurchaseOrderDetail(Integer id) {
        this.id = id;
    }

    public PurchaseOrderDetail(Integer id, Double unitPrice, int quantity, Double amount) {
        this.id = id;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public AssignmentDetail getAssignmentDetail() {
        return assignmentDetail;
    }

    public void setAssignmentDetail(AssignmentDetail assignmentDetail) {
        this.assignmentDetail = assignmentDetail;
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
        if (!(object instanceof PurchaseOrderDetail)) {
            return false;
        }
        PurchaseOrderDetail other = (PurchaseOrderDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.PurchaseOrderDetail[id=" + id + "]";
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Collection<PurchaseOrderRegister> getPurchaseOrderRegisterCollection() {
        return purchaseOrderRegisterCollection;
    }

    public void setPurchaseOrderRegisterCollection(Collection<PurchaseOrderRegister> purchaseOrderRegisterCollection) {
        this.purchaseOrderRegisterCollection = purchaseOrderRegisterCollection;
    }
    
    public Collection<PurchaseOrderChildRegister> getPurchaseOrderChildRegisterCollection() {
        return purchaseOrderChildRegisterCollection;
    }
    
    public void setPurchaseOrderChildRegisterCollection(Collection<PurchaseOrderChildRegister> purchaseOrderChildRegisterCollection) {
        this.purchaseOrderChildRegisterCollection = purchaseOrderChildRegisterCollection;
    }
    
    public Collection<PurchaseOrderDeclaration> getPurchaseOrderDeclarationCollection() {
        return purchaseOrderDeclarationCollection;
    }

    public void setPurchaseOrderDeclarationCollection(Collection<PurchaseOrderDeclaration> PurchaseOrderDeclarationCollection) {
        this.purchaseOrderDeclarationCollection = PurchaseOrderDeclarationCollection;
    }

    public ProductPlan getProductPlan() {
        return productPlan;
    }

    public void setProductPlan(ProductPlan productPlan) {
        this.productPlan = productPlan;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public Collection<PurchaseOrderQuestionaire> getPurchaseOrderQuestionaireCollection() {
        return purchaseOrderQuestionaireCollection;
    }

    public void setPurchaseOrderQuestionaireCollection(Collection<PurchaseOrderQuestionaire> purchaseOrderQuestionaireCollection) {
        this.purchaseOrderQuestionaireCollection = purchaseOrderQuestionaireCollection;
    }

    public ProductPlanDetail getProductPlanDetail() {
        return productPlanDetail;
    }

    public void setProductPlanDetail(ProductPlanDetail productPlanDetail) {
        this.productPlanDetail = productPlanDetail;
}

    public Integer getMonthlyFirstPayment() {
        return monthlyFirstPayment;
    }

    public void setMonthlyFirstPayment(Integer monthlyFirstPayment) {
        this.monthlyFirstPayment = monthlyFirstPayment;
    }
}
