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
@Table(name = "rpt_sale_performance1")
@NamedQueries({
    @NamedQuery(name = "RptSalePerformance1.findAll", query = "SELECT r FROM RptSalePerformance1 r")})
public class RptSalePerformance1 implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RptSalePerformancePK rptSalePerformancePK;
    @Basic(optional = false)
    @Column(name = "list_assigned")
    private int listAssigned;
    @Column(name = "list_used")
    private Integer listUsed;
    @Column(name = "list_new")
    private Integer listNew;
    @Column(name = "list_old")
    private Integer listOld;
    @Column(name = "list_opened")
    private Integer listOpened;
    @Column(name = "list_followup")
    private Integer listFollowup;
    @Column(name = "list_uncontactable")
    private Integer listUncontactable;
    @Column(name = "list_unreachable")
    private Integer listUnreachable;
    @Column(name = "list_closed_contactable")
    private Integer listClosedContactable;
    @Column(name = "list_closed_uncontactable")
    private Integer listClosedUncontactable;
    @Column(name = "list_closed_firstend")
    private Integer listClosedFirstend;
    @Column(name = "list_closed_unreachable")
    private Integer listClosedUnreachable;
    @Column(name = "call_attempt")
    private int callAttempt;
    @Column(name = "call_success")
    private int callSuccess;
    @Column(name = "call_fail")
    private Integer callFail;
    @Column(name = "sale_attempt")
    private int saleAttempt;
    @Column(name = "list_yes_sale")
    private int listYesSale;
    @Column(name = "list_no_sale")
    private int listNoSale;
    @Column(name = "list_followup_sale")
    private int listFollowupSale;
    @Column(name = "product_yes_sale")
    private Integer productYesSale;
    @Column(name = "product_yes_sale_today")
    private Integer productYesSaleToday;
    @Column(name = "product_yes_pending_sale")
    private Integer productYesPendingSale;
    @Column(name = "product_no_sale")
    private int productNoSale;
    @Column(name = "product_followup_sale")
    private int productFollowupSale;
    @Column(name = "total_amount")
    private Double totalAmount;
    @Column(name = "total_amount_today")
    private Double totalAmountToday;
    @Column(name = "total_pending_amount")
    private Double totalPendingAmount;
    @Column(name = "list_new_contact")
    private Integer listNewContact;
    @Column(name = "total_new_contact")
    private Double totalNewContact;
    @Column(name = "list_old_contact")
    private Integer listOldContact;
    @Column(name = "total_old_contact")
    private Double totalOldContact;

    @Column(name = "list_new_net_premium_credit")
    private Double listNewNetPremiumCredit;
    @Column(name = "list_new_net_premium_debit")
    private Double listNewNetPremiumDebit;
    @Column(name = "list_new_gross_premium_credit")
    private Double listNewGrossPremiumCredit;
    @Column(name = "list_new_gross_premium_debit")
    private Double listNewGrossPremiumDebit;

    @Column(name = "list_old_net_premium_credit")
    private Double listOldNetPremiumCredit;
    @Column(name = "list_old_net_premium_debit")
    private Double listOldNetPremiumDebit;
    @Column(name = "list_old_gross_premium_credit")
    private Double listOldGrossPremiumCredit;
    @Column(name = "list_old_gross_premium_debit")
    private Double listOldGrossPremiumDebit;

    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Users users;
    @JoinColumn(name = "marketing_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Marketing marketing;
    @JoinColumn(name = "campaign_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Campaign campaign;

    public RptSalePerformance1() {
        this.listAssigned = 0;
        this.listUsed = 0;
        this.listOpened = 0;
        this.listFollowup = 0;
        this.listUncontactable = 0;
        this.listUnreachable = 0;
        this.listClosedContactable = 0;
        this.listClosedUncontactable = 0;
        this.listClosedFirstend = 0;
        this.listClosedUnreachable = 0;
        this.callAttempt = 0;
        this.callSuccess = 0;
        this.callFail = 0;
        this.saleAttempt = 0;
        this.listYesSale = 0;
        this.listNoSale = 0;
        this.listFollowupSale = 0;
        this.productYesSale = 0;
        this.productNoSale = 0;
        this.productFollowupSale = 0;
        this.totalAmount = 0.0;
        this.listNewContact = 0;
        this.listOldContact = 0;
        this.totalNewContact = 0.0;
        this.totalOldContact = 0.0;

        this.listNewNetPremiumCredit = 0.0;
        this.listNewNetPremiumDebit = 0.0;
        this.listNewGrossPremiumCredit = 0.0;
        this.listNewGrossPremiumDebit = 0.0;
        this.listOldNetPremiumCredit = 0.0;
        this.listOldNetPremiumDebit = 0.0;
        this.listOldGrossPremiumCredit = 0.0;
        this.listOldGrossPremiumDebit = 0.0;

    }

    public RptSalePerformance1(RptSalePerformancePK rptSalePerformancePK) {
        this.rptSalePerformancePK = rptSalePerformancePK;
    }

    public RptSalePerformance1(RptSalePerformancePK rptSalePerformancePK, int listAssigned, int callAttempt, int callSuccess, int saleAttempt, int listYesSale, int listNoSale, int productNoSale, double totalAmount) {
        this.rptSalePerformancePK = rptSalePerformancePK;
        this.listAssigned = listAssigned;
        this.callAttempt = callAttempt;
        this.callSuccess = callSuccess;
        this.saleAttempt = saleAttempt;
        this.listYesSale = listYesSale;
        this.listNoSale = listNoSale;
        this.productNoSale = productNoSale;
        this.totalAmount = totalAmount;
    }
    
   
    public RptSalePerformance1(Date saleDate, int userId, int campaignId, int marketingId) {
        this.rptSalePerformancePK = new RptSalePerformancePK(saleDate, userId, campaignId, marketingId);
    }

    public RptSalePerformancePK getRptSalePerformancePK() {
        return rptSalePerformancePK;
    }

    public void setRptSalePerformancePK(RptSalePerformancePK rptSalePerformancePK) {
        this.rptSalePerformancePK = rptSalePerformancePK;
    }

    public int getListAssigned() {
        return listAssigned;
    }

    public void setListAssigned(int listAssigned) {
        this.listAssigned = listAssigned;
    }

    public Integer getListUsed() {
        return listUsed;
    }

    public void setListUsed(Integer listUsed) {
        this.listUsed = listUsed;
    }

    public Integer getListOpened() {
        return listOpened;
    }

    public void setListOpened(Integer listOpened) {
        this.listOpened = listOpened;
    }

    public Integer getListFollowup() {
        return listFollowup;
    }

    public void setListFollowup(Integer listFollowup) {
        this.listFollowup = listFollowup;
    }

    public Integer getListUncontactable() {
        return listUncontactable;
    }

    public void setListUncontactable(Integer listUncontactable) {
        this.listUncontactable = listUncontactable;
    }

    public Integer getListUnreachable() {
        return listUnreachable;
    }

    public void setListUnreachable(Integer listUnreachable) {
        this.listUnreachable = listUnreachable;
    }

    public Integer getListClosedContactable() {
        return listClosedContactable;
    }

    public void setListClosedContactable(Integer listClosedContactable) {
        this.listClosedContactable = listClosedContactable;
    }

    public Integer getListClosedUncontactable() {
        return listClosedUncontactable;
    }

    public void setListClosedUncontactable(Integer listClosedUncontactable) {
        this.listClosedUncontactable = listClosedUncontactable;
    }

    public Integer getListClosedFirstend() {
        return listClosedFirstend;
    }

    public void setListClosedFirstend(Integer listClosedFirstend) {
        this.listClosedFirstend = listClosedFirstend;
    }

    public Integer getListClosedUnreachable() {
        return listClosedUnreachable;
    }

    public void setListClosedUnreachable(Integer listClosedUnreachable) {
        this.listClosedUnreachable = listClosedUnreachable;
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

    public Integer getCallFail() {
        return callFail;
    }

    public void setCallFail(Integer callFail) {
        this.callFail = callFail;
    }

    public int getSaleAttempt() {
        return saleAttempt;
    }

    public void setSaleAttempt(int saleAttempt) {
        this.saleAttempt = saleAttempt;
    }

    public int getListYesSale() {
        return listYesSale;
    }

    public void setListYesSale(int listYesSale) {
        this.listYesSale = listYesSale;
    }

    public int getListNoSale() {
        return listNoSale;
    }

    public void setListNoSale(int listNoSale) {
        this.listNoSale = listNoSale;
    }

    public Integer getProductYesSale() {
        return productYesSale;
    }

    public void setProductYesSale(Integer productYesSale) {
        this.productYesSale = productYesSale;
    }

    public int getProductNoSale() {
        return productNoSale;
    }

    public void setProductNoSale(int productNoSale) {
        this.productNoSale = productNoSale;
    }

    public int getListFollowupSale() {
        return listFollowupSale;
    }

    public void setListFollowupSale(int listFollowupSale) {
        this.listFollowupSale = listFollowupSale;
    }

    public int getProductFollowupSale() {
        return productFollowupSale;
    }

    public void setProductFollowupSale(int productFollowupSale) {
        this.productFollowupSale = productFollowupSale;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getProductYesPendingSale() {
        return productYesPendingSale;
    }

    public void setProductYesPendingSale(Integer productYesPendingSale) {
        this.productYesPendingSale = productYesPendingSale;
    }

    public Double getTotalPendingAmount() {
        return totalPendingAmount;
    }

    public void setTotalPendingAmount(Double totalPendingAmount) {
        this.totalPendingAmount = totalPendingAmount;
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

    public Integer getListNew() {
        return listNew;
    }

    public void setListNew(Integer listNew) {
        this.listNew = listNew;
    }

    public Integer getListOld() {
        return listOld;
    }

    public void setListOld(Integer listOld) {
        this.listOld = listOld;
    }

    public Integer getProductYesSaleToday() {
        return productYesSaleToday;
    }

    public void setProductYesSaleToday(Integer productYesSaleToday) {
        this.productYesSaleToday = productYesSaleToday;
    }

    public Double getTotalAmountToday() {
        return totalAmountToday;
    }

    public void setTotalAmountToday(Double totalAmountToday) {
        this.totalAmountToday = totalAmountToday;
    }

    public Integer getListNewContact() {
        return listNewContact;
    }

    public void setListNewContact(Integer listNewContact) {
        this.listNewContact = listNewContact;
    }

    public Integer getListOldContact() {
        return listOldContact;
    }

    public void setListOldContact(Integer listOldContact) {
        this.listOldContact = listOldContact;
    }

    public Double getTotalNewContact() {
        return totalNewContact;
    }

    public void setTotalNewContact(Double totalNewContact) {
        this.totalNewContact = totalNewContact;
    }

    public Double getTotalOldContact() {
        return totalOldContact;
    }

    public void setTotalOldContact(Double totalOldContact) {
        this.totalOldContact = totalOldContact;
    }

    public Double getListNewNetPremiumCredit() {
        return listNewNetPremiumCredit;
    }

    public void setListNewNetPremiumCredit(Double listNewNetPremiumCredit) {
        this.listNewNetPremiumCredit = listNewNetPremiumCredit;
    }

    public Double getListNewNetPremiumDebit() {
        return listNewNetPremiumDebit;
    }

    public void setListNewNetPremiumDebit(Double listNewNetPremiumDebit) {
        this.listNewNetPremiumDebit = listNewNetPremiumDebit;
    }

    public Double getListNewGrossPremiumCredit() {
        return listNewGrossPremiumCredit;
    }

    public void setListNewGrossPremiumCredit(Double listNewGrossPremiumCredit) {
        this.listNewGrossPremiumCredit = listNewGrossPremiumCredit;
    }

    public Double getListNewGrossPremiumDebit() {
        return listNewGrossPremiumDebit;
    }

    public void setListNewGrossPremiumDebit(Double listNewGrossPremiumDebit) {
        this.listNewGrossPremiumDebit = listNewGrossPremiumDebit;
    }

    public Double getListOldNetPremiumCredit() {
        return listOldNetPremiumCredit;
    }

    public void setListOldNetPremiumCredit(Double listOldNetPremiumCredit) {
        this.listOldNetPremiumCredit = listOldNetPremiumCredit;
    }

    public Double getListOldNetPremiumDebit() {
        return listOldNetPremiumDebit;
    }

    public void setListOldNetPremiumDebit(Double listOldNetPremiumDebit) {
        this.listOldNetPremiumDebit = listOldNetPremiumDebit;
    }

    public Double getListOldGrossPremiumCredit() {
        return listOldGrossPremiumCredit;
    }

    public void setListOldGrossPremiumCredit(Double listOldGrossPremiumCredit) {
        this.listOldGrossPremiumCredit = listOldGrossPremiumCredit;
    }

    public Double getListOldGrossPremiumDebit() {
        return listOldGrossPremiumDebit;
    }

    public void setListOldGrossPremiumDebit(Double listOldGrossPremiumDebit) {
        this.listOldGrossPremiumDebit = listOldGrossPremiumDebit;
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
