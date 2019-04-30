/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "rpt_sale_performance")
@NamedQueries({
    @NamedQuery(name = "RptSalePerformance.findAll", query = "SELECT r FROM RptSalePerformance r")})
public class RptSalePerformance implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RptSalePerformancePK rptSalePerformancePK;
    @Basic(optional = false)
    @Column(name = "list_used_new")
    private int listUsedNew;
    @Basic(optional = false)
    @Column(name = "list_used_old")
    private int listUsedOld;
    @Basic(optional = false)
    @Column(name = "call_attempt")
    private int callAttempt;
    @Basic(optional = false)
    @Column(name = "call_success")
    private int callSuccess;
    @Basic(optional = false)
    @Column(name = "sale_attempt")
    private int saleAttempt;
    @Basic(optional = false)
    @Column(name = "sale_offering")
    private int saleOffering;
    @Basic(optional = false)
    @Column(name = "yes_sale")
    private int yesSale;
    @Basic(optional = false)
    @Column(name = "no_sale")
    private int noSale;
    @Basic(optional = false)
    @Column(name = "contactable")
    private int contactable;
    @Basic(optional = false)
    @Column(name = "follow_up")
    private int followUp;
    @Basic(optional = false)
    @Column(name = "uncontactable")
    private int uncontactable;
    @Basic(optional = false)
    @Column(name = "total_amount")
    private BigDecimal totalAmount;
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Users users;
    @JoinColumn(name = "marketing_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Marketing marketing;
    @JoinColumn(name = "campaign_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Campaign campaign;

    public RptSalePerformance() {
    }

    public RptSalePerformance(RptSalePerformancePK rptSalePerformancePK) {
        this.rptSalePerformancePK = rptSalePerformancePK;
    }

    public RptSalePerformance(RptSalePerformancePK rptSalePerformancePK, int listUsedNew, int listUsedOld, int callAttempt, int saleOffering, int yesSale, int noSale, int contactable, int followUp, int uncontactable, BigDecimal totalAmount) {
        this.rptSalePerformancePK = rptSalePerformancePK;
        this.listUsedNew = listUsedNew;
        this.listUsedOld = listUsedOld;
        this.callAttempt = callAttempt;
        this.saleOffering = saleOffering;
        this.yesSale = yesSale;
        this.noSale = noSale;
        this.contactable = contactable;
        this.followUp = followUp;
        this.uncontactable = uncontactable;
        this.totalAmount = totalAmount;
    }

    public RptSalePerformance(Date saleDate, int userId, int campaignId, int marketingId) {
        this.rptSalePerformancePK = new RptSalePerformancePK(saleDate, userId, campaignId, marketingId);
    }

    public RptSalePerformancePK getRptSalePerformancePK() {
        return rptSalePerformancePK;
    }

    public void setRptSalePerformancePK(RptSalePerformancePK rptSalePerformancePK) {
        this.rptSalePerformancePK = rptSalePerformancePK;
    }

    public int getListUsedNew() {
        return listUsedNew;
    }

    public void setListUsedNew(int listUsedNew) {
        this.listUsedNew = listUsedNew;
    }

    public int getListUsedOld() {
        return listUsedOld;
    }

    public void setListUsedOld(int listUsedOld) {
        this.listUsedOld = listUsedOld;
    }

    public int getCallAttempt() {
        return callAttempt;
    }

    public void setCallAttempt(int callAttempt) {
        this.callAttempt = callAttempt;
    }

    public int getCallSuccess() {
        return callSuccess;
    }

    public void setCallSuccess(int callSuccess) {
        this.callSuccess = callSuccess;
    }

    public int getSaleAttempt() {
        return saleAttempt;
    }

    public void setSaleAttempt(int saleAttempt) {
        this.saleAttempt = saleAttempt;
    }

    public int getSaleOffering() {
        return saleOffering;
    }

    public void setSaleOffering(int saleOffering) {
        this.saleOffering = saleOffering;
    }

    public int getYesSale() {
        return yesSale;
    }

    public void setYesSale(int yesSale) {
        this.yesSale = yesSale;
    }

    public int getNoSale() {
        return noSale;
    }

    public void setNoSale(int noSale) {
        this.noSale = noSale;
    }

    public int getContactable() {
        return contactable;
    }

    public void setContactable(int contactable) {
        this.contactable = contactable;
    }

    public int getFollowUp() {
        return followUp;
    }

    public void setFollowUp(int followUp) {
        this.followUp = followUp;
    }

    public int getUncontactable() {
        return uncontactable;
    }

    public void setUncontactable(int uncontactable) {
        this.uncontactable = uncontactable;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Marketing getMarketing() {
        return marketing;
    }

    public void setMarketing(Marketing marketing) {
        this.marketing = marketing;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rptSalePerformancePK != null ? rptSalePerformancePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RptSalePerformance)) {
            return false;
        }
        RptSalePerformance other = (RptSalePerformance) object;
        if ((this.rptSalePerformancePK == null && other.rptSalePerformancePK != null) || (this.rptSalePerformancePK != null && !this.rptSalePerformancePK.equals(other.rptSalePerformancePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.RptSalePerformance[rptSalePerformancePK=" + rptSalePerformancePK + "]";
    }

}
