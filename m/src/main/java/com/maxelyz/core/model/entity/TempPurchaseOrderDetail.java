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
@Table(name = "temp_purchase_order_detail")
@NamedQueries({
    @NamedQuery(name = "TempPurchaseOrderDetail.findAll", query = "SELECT t FROM TempPurchaseOrderDetail t")})
public class TempPurchaseOrderDetail implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tempPurchaseOrderDetail")
    private Collection<TempPurchaseOrderQuestionaire> tempPurchaseOrderQuestionaireCollection;
    @Column(name = "payment_mode")
    private String paymentMode;
    @Column(name = "delivery_method")
    private String deliveryMethod;
    @JoinColumn(name = "product_plan_id", referencedColumnName = "id")
    @ManyToOne
    private ProductPlan productPlan;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "assignment_detail_id")
    private Integer assignmentDetailId;
    @Basic(optional = false)
    @Column(name = "unit_price")
    private Double unitPrice;
    @Basic(optional = false)
    @Column(name = "quantity")
    private int quantity;
    @Basic(optional = false)
    @Column(name = "amount")
    private Double amount;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @JoinColumn(name = "purchase_order_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TempPurchaseOrder tempPurchaseOrder;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tempPurchaseOrderDetail")
    private Collection<TempPurchaseOrderRegister> tempPurchaseOrderRegisterCollection;
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Product product;

    public TempPurchaseOrderDetail() {
    }

    public TempPurchaseOrderDetail(Integer id) {
        this.id = id;
    }

    public TempPurchaseOrderDetail(Integer id, int assignmentDetailId, Product product, Double unitPrice, int quantity, Double amount) {
        this.id = id;
        this.assignmentDetailId = assignmentDetailId;
        this.product = product;
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

    public Integer getAssignmentDetailId() {
        return assignmentDetailId;
    }

    public void setAssignmentDetailId(Integer assignmentDetailId) {
        this.assignmentDetailId = assignmentDetailId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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

    public TempPurchaseOrder getTempPurchaseOrder() {
        return tempPurchaseOrder;
    }

    public void setTempPurchaseOrder(TempPurchaseOrder tempPurchaseOrder) {
        this.tempPurchaseOrder = tempPurchaseOrder;
    }

    public Collection<TempPurchaseOrderRegister> getTempPurchaseOrderRegisterCollection() {
        return tempPurchaseOrderRegisterCollection;
    }

    public void setTempPurchaseOrderRegisterCollection(Collection<TempPurchaseOrderRegister> tempPurchaseOrderRegisterCollection) {
        this.tempPurchaseOrderRegisterCollection = tempPurchaseOrderRegisterCollection;
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
        if (!(object instanceof TempPurchaseOrderDetail)) {
            return false;
        }
        TempPurchaseOrderDetail other = (TempPurchaseOrderDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.TempPurchaseOrderDetail[id=" + id + "]";
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

    public Collection<TempPurchaseOrderQuestionaire> getTempPurchaseOrderQuestionaireCollection() {
        return tempPurchaseOrderQuestionaireCollection;
    }

    public void setTempPurchaseOrderQuestionaireCollection(Collection<TempPurchaseOrderQuestionaire> tempPurchaseOrderQuestionaireCollection) {
        this.tempPurchaseOrderQuestionaireCollection = tempPurchaseOrderQuestionaireCollection;
    }

}
