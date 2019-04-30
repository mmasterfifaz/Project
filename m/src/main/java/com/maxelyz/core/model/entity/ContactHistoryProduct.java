/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "contact_history_product")
@NamedQueries({
    @NamedQuery(name = "ContactHistoryProduct.findAll", query = "SELECT c FROM ContactHistoryProduct c")})
public class ContactHistoryProduct implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ContactHistoryProductPK contactHistoryProductPK;
    @Basic(optional = false)
    @Column(name = "useful")
    private boolean useful;
    @Column(name = "remark")
    private String remark;
    @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Product product;
    @JoinColumn(name = "contact_history_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ContactHistory contactHistory;

    public ContactHistoryProduct() {
    }

    public ContactHistoryProduct(ContactHistoryProductPK contactHistoryProductPK) {
        this.contactHistoryProductPK = contactHistoryProductPK;
    }

    public ContactHistoryProduct(ContactHistoryProductPK contactHistoryProductPK, boolean useful) {
        this.contactHistoryProductPK = contactHistoryProductPK;
        this.useful = useful;
    }

    public ContactHistoryProduct(int contactHistoryId, int productId) {
        this.contactHistoryProductPK = new ContactHistoryProductPK(contactHistoryId, productId);
    }

    public ContactHistoryProductPK getContactHistoryProductPK() {
        return contactHistoryProductPK;
    }

    public void setContactHistoryProductPK(ContactHistoryProductPK contactHistoryProductPK) {
        this.contactHistoryProductPK = contactHistoryProductPK;
    }

    public boolean getUseful() {
        return useful;
    }

    public void setUseful(boolean useful) {
        this.useful = useful;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        hash += (contactHistoryProductPK != null ? contactHistoryProductPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContactHistoryProduct)) {
            return false;
        }
        ContactHistoryProduct other = (ContactHistoryProduct) object;
        if ((this.contactHistoryProductPK == null && other.contactHistoryProductPK != null) || (this.contactHistoryProductPK != null && !this.contactHistoryProductPK.equals(other.contactHistoryProductPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.ContactHistoryProduct[contactHistoryProductPK=" + contactHistoryProductPK + "]";
    }

}
