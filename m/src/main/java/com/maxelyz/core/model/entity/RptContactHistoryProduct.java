/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Date;
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
@Table(name = "rpt_contact_history_product")
@NamedQueries({
    @NamedQuery(name = "RptContactHistoryProduct.findAll", query = "SELECT r FROM RptContactHistoryProduct r")})
public class RptContactHistoryProduct implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RptContactHistoryProductPK rptContactHistoryProductPK;
    @Basic(optional = false)
    @Column(name = "total")
    private int total;
    @Basic(optional = false)
    @Column(name = "useful_total")
    private int usefulTotal;
    @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Product product;

    public RptContactHistoryProduct() {
    }

    public RptContactHistoryProduct(RptContactHistoryProductPK rptContactHistoryProductPK) {
        this.rptContactHistoryProductPK = rptContactHistoryProductPK;
    }

    public RptContactHistoryProduct(RptContactHistoryProductPK rptContactHistoryProductPK, int total) {
        this.rptContactHistoryProductPK = rptContactHistoryProductPK;
        this.total = total;
    }

    public RptContactHistoryProduct(Date contactDate, int productId) {
        this.rptContactHistoryProductPK = new RptContactHistoryProductPK(contactDate, productId);
    }

    public RptContactHistoryProductPK getRptContactHistoryProductPK() {
        return rptContactHistoryProductPK;
    }

    public void setRptContactHistoryProductPK(RptContactHistoryProductPK rptContactHistoryProductPK) {
        this.rptContactHistoryProductPK = rptContactHistoryProductPK;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getUsefulTotal() {
        return usefulTotal;
    }

    public void setUsefulTotal(int usefulTotal) {
        this.usefulTotal = usefulTotal;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rptContactHistoryProductPK != null ? rptContactHistoryProductPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RptContactHistoryProduct)) {
            return false;
        }
        RptContactHistoryProduct other = (RptContactHistoryProduct) object;
        if ((this.rptContactHistoryProductPK == null && other.rptContactHistoryProductPK != null) || (this.rptContactHistoryProductPK != null && !this.rptContactHistoryProductPK.equals(other.rptContactHistoryProductPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.RptContactHistoryProduct[rptContactHistoryProductPK=" + rptContactHistoryProductPK + "]";
    }

}
