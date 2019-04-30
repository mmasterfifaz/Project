/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "contact_history_sale_result")
@NamedQueries({
    @NamedQuery(name = "ContactHistorySaleResult.findAll", query = "SELECT c FROM ContactHistorySaleResult c")})
public class ContactHistorySaleResult implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "sale_result")
    private char saleResult;
    @Column(name = "nosale_reason_id")
    private Integer nosaleReasonId;
    @Column(name = "followupsale_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date followupsaleDate;
    @Column(name = "followupsale_reason_id")
    private Integer followupsaleReasonId;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "create_by")
    private String createBy;
    @JoinColumn(name = "create_by_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users createById;
    @JoinColumn(name = "purchase_order_id", referencedColumnName = "id")
    @ManyToOne
    private PurchaseOrder purchaseOrderId;
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Product product;
    @JoinColumn(name = "contact_history_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ContactHistory contactHistory;

    public ContactHistorySaleResult() {
    }

    public ContactHistorySaleResult(Integer id) {
        this.id = id;
    }
        
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public char getSaleResult() {
        return saleResult;
    }

    public void setSaleResult(char saleResult) {
        this.saleResult = saleResult;
    }

    public Integer getNosaleReasonId() {
        return nosaleReasonId;
    }

    public void setNosaleReasonId(Integer nosaleReasonId) {
        this.nosaleReasonId = nosaleReasonId;
    }

    public Date getFollowupsaleDate() {
        return followupsaleDate;
    }

    public void setFollowupsaleDate(Date followupsaleDate) {
        this.followupsaleDate = followupsaleDate;
    }

    public Integer getFollowupsaleReasonId() {
        return followupsaleReasonId;
    }

    public void setFollowupsaleReasonId(Integer followupsaleReasonId) {
        this.followupsaleReasonId = followupsaleReasonId;
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

    public Users getCreateById() {
        return createById;
    }

    public void setCreateById(Users createById) {
        this.createById = createById;
    }

    public PurchaseOrder getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(PurchaseOrder purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ContactHistory getContactHistory() {
        return contactHistory;
    }

    public void setContactHistory(ContactHistory contactHistory) {
        this.contactHistory = contactHistory;
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
        if (!(object instanceof ContactHistorySaleResult)) {
            return false;
        }
        ContactHistorySaleResult other = (ContactHistorySaleResult) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.ContactHistorySaleResult[ contactHistorySaleResultPK=" + id + " ]";
    }
    
}
