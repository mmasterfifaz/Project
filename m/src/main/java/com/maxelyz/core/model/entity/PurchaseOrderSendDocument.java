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
@Table(name = "purchase_order_send_document")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PurchaseOrderSendDocument.findAll", query = "SELECT p FROM PurchaseOrderSendDocument p")})
public class PurchaseOrderSendDocument implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "send_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date sendDate;
    @Basic(optional = false)
    @Column(name = "item_no", nullable = false, length = 50)
    private String itemNo;
    @Basic(optional = false)
    @Column(name = "document_type", nullable = false, length = 50)
    private String documentType;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "amount")
    private Double amount;
    @Column(name = "remark", length = 500)
    private String remark;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "create_by", length = 100)
    private String createBy;
    @JoinColumn(name = "create_by_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users createByUser;
    @JoinColumn(name = "purchase_order_id", referencedColumnName = "id")
    @ManyToOne
    private PurchaseOrder purchaseOrder;

    public PurchaseOrderSendDocument() {
    }

    public PurchaseOrderSendDocument(Integer id) {
        this.id = id;
    }

    public PurchaseOrderSendDocument(Integer id, Date sendDate, String itemNo, String documentType) {
        this.id = id;
        this.sendDate = sendDate;
        this.itemNo = itemNo;
        this.documentType = documentType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public Users getCreateByUser() {
        return createByUser;
    }

    public void setCreateByUser(Users createByUser) {
        this.createByUser = createByUser;
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
        if (!(object instanceof PurchaseOrderSendDocument)) {
            return false;
        }
        PurchaseOrderSendDocument other = (PurchaseOrderSendDocument) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.PurchaseOrderSendDocument[ id=" + id + " ]";
    }
    
}
