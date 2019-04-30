/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author oat
 */
@Entity
@Table(name = "campaign_customer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CampaignCustomer.findAll", query = "SELECT c FROM CampaignCustomer c")})
public class CampaignCustomer implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CampaignCustomerPK campaignCustomerPK;
    @JoinColumn(name = "assignment_detail_id", referencedColumnName = "id")
    @ManyToOne
    private AssignmentDetail assignmentDetail;
    @Column(name = "list_used")
    private Boolean listUsed;
    @Column(name = "list_reused")
    private Boolean listReused;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "list_used_date")
    private Date listUsedDate;
    @JoinColumn(name = "customer_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Customer customer;
    @JoinColumn(name = "campaign_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Campaign campaign;

    public CampaignCustomer() {
    }

    public CampaignCustomer(CampaignCustomerPK campaignCustomerPK) {
        this.campaignCustomerPK = campaignCustomerPK;
    }

    public CampaignCustomer(int campaignId, int customerId) {
        this.campaignCustomerPK = new CampaignCustomerPK(campaignId, customerId);
    }

    public CampaignCustomerPK getCampaignCustomerPK() {
        return campaignCustomerPK;
    }

    public void setCampaignCustomerPK(CampaignCustomerPK campaignCustomerPK) {
        this.campaignCustomerPK = campaignCustomerPK;
    }

    public AssignmentDetail getAssignmentDetail() {
        return assignmentDetail;
    }

    public void setAssignmentDetail(AssignmentDetail assignmentDetail) {
        this.assignmentDetail = assignmentDetail;
    }

    public Boolean getListUsed() {
        return listUsed;
    }

    public void setListUsed(Boolean listUsed) {
        this.listUsed = listUsed;
    }

    public Boolean getListReused() {
        return listReused;
    }

    public void setListReused(Boolean listReused) {
        this.listReused = listReused;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (campaignCustomerPK != null ? campaignCustomerPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CampaignCustomer)) {
            return false;
        }
        CampaignCustomer other = (CampaignCustomer) object;
        if ((this.campaignCustomerPK == null && other.campaignCustomerPK != null) || (this.campaignCustomerPK != null && !this.campaignCustomerPK.equals(other.campaignCustomerPK))) {
            return false;
        }
        return true;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getListUsedDate() {
        return listUsedDate;
    }

    public void setListUsedDate(Date listUsedDate) {
        this.listUsedDate = listUsedDate;
    }
    
    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.CampaignCustomer[ campaignCustomerPK=" + campaignCustomerPK + " ]";
    }
    
}
