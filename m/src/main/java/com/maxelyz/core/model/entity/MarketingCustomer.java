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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "marketing_customer")
@NamedQueries({@NamedQuery(name = "MarketingCustomer.findAll", query = "SELECT m FROM MarketingCustomer m")})
public class MarketingCustomer implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MarketingCustomerPK marketingCustomerPK;
    @Column(name = "assign")
    private Boolean assign;
    @Column(name = "new_list")
    private Boolean newList;
    @Basic(optional = false)
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "first_contact_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date firstContactDate;
    @JoinColumn(name = "customer_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Customer customer;
    @JoinColumn(name = "marketing_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Marketing marketing;

    public MarketingCustomer() {
    }

    public MarketingCustomer(MarketingCustomerPK marketingCustomerPK) {
        this.marketingCustomerPK = marketingCustomerPK;
    }

    public MarketingCustomer(MarketingCustomerPK marketingCustomerPK, Date createDate) {
        this.marketingCustomerPK = marketingCustomerPK;
        this.createDate = createDate;
    }

    public MarketingCustomer(int id, int customerId) {
        this.marketingCustomerPK = new MarketingCustomerPK(id, customerId);
    }

    public MarketingCustomerPK getMarketingCustomerPK() {
        return marketingCustomerPK;
    }

    public void setMarketingCustomerPK(MarketingCustomerPK marketingCustomerPK) {
        this.marketingCustomerPK = marketingCustomerPK;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Marketing getMarketing() {
        return marketing;
    }

    public void setMarketing(Marketing marketing) {
        this.marketing = marketing;
    }

    public Boolean getAssign() {
        return assign;
    }

    public void setAssign(Boolean assign) {
        this.assign = assign;
    }
    
    public Boolean getNewList() {
        return newList;
    }

    public void setNewList(Boolean newList) {
        this.newList = newList;
    }
    
    public Date getFirstContactDate() {
        return firstContactDate;
    }

    public void setFirstContactDate(Date firstContactDate) {
        this.firstContactDate = firstContactDate;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (marketingCustomerPK != null ? marketingCustomerPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MarketingCustomer)) {
            return false;
        }
        MarketingCustomer other = (MarketingCustomer) object;
        if ((this.marketingCustomerPK == null && other.marketingCustomerPK != null) || (this.marketingCustomerPK != null && !this.marketingCustomerPK.equals(other.marketingCustomerPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.MarketingCustomer[marketingCustomerPK=" + marketingCustomerPK + "]";
    }

}
