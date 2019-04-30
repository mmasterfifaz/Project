/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Oat
 */
@Embeddable
public class RptContactChannelPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "contact_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date contactDate;
    @Basic(optional = false)
    @Column(name = "user_id")
    private int userId;
    @Basic(optional = false)
    @Column(name = "channel_id")
    private int channelId;
    @Basic(optional = false)
    @Column(name = "case_detail_id")
    private int caseDetailId;
    @Basic(optional = false)
    @Column(name = "case_request_id")
    private int caseRequestId;
    @Basic(optional = false)
    @Column(name = "service_type_id")
    private int serviceTypeId;
    @Basic(optional = false)
    @Column(name = "location_id")
    private int locationId;

    public RptContactChannelPK() {
    }

    public RptContactChannelPK(Date contactDate, int userId, int channelId, int caseDetailId, int caseRequestId, int serviceTypeId, int locationId) {
        this.contactDate = contactDate;
        this.userId = userId;
        this.channelId = channelId;
        this.caseDetailId = caseDetailId;
        this.caseRequestId = caseRequestId;
        this.serviceTypeId = serviceTypeId;
        this.locationId = locationId;
    }

    public Date getContactDate() {
        return contactDate;
    }

    public void setContactDate(Date contactDate) {
        this.contactDate = contactDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getCaseDetailId() {
        return caseDetailId;
    }

    public void setCaseDetailId(int caseDetailId) {
        this.caseDetailId = caseDetailId;
    }

    public int getCaseRequestId() {
        return caseRequestId;
    }

    public void setCaseRequestId(int caseRequestId) {
        this.caseRequestId = caseRequestId;
    }

    public int getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(int serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (contactDate != null ? contactDate.hashCode() : 0);
        hash += (int) userId;
        hash += (int) channelId;
        hash += (int) caseDetailId;
        hash += (int) caseRequestId;
        hash += (int) serviceTypeId;
        hash += (int) locationId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RptContactChannelPK)) {
            return false;
        }
        RptContactChannelPK other = (RptContactChannelPK) object;
        if ((this.contactDate == null && other.contactDate != null) || (this.contactDate != null && !this.contactDate.equals(other.contactDate))) {
            return false;
        }
        if (this.userId != other.userId) {
            return false;
        }
        if (this.channelId != other.channelId) {
            return false;
        }
        if (this.caseDetailId != other.caseDetailId) {
            return false;
        }
        if (this.caseRequestId != other.caseRequestId) {
            return false;
        }
        if (this.serviceTypeId != other.serviceTypeId) {
            return false;
        }
        if (this.locationId != other.locationId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.RptContactChannelPK[contactDate=" + contactDate + ", userId=" + userId + ", channelId=" + channelId + ", caseDetailId=" + caseDetailId + ", caseRequestId=" + caseRequestId + ", serviceTypeId=" + serviceTypeId + ", locationId=" + locationId + "]";
    }

}
