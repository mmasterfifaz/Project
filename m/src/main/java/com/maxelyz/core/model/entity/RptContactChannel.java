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
@Table(name = "rpt_contact_channel")
@NamedQueries({
    @NamedQuery(name = "RptContactChannel.findAll", query = "SELECT r FROM RptContactChannel r")})
public class RptContactChannel implements Serializable {


    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RptContactChannelPK rptContactChannelPK;
    @Basic(optional = false)
    @Column(name = "total")
    private int total;
    @Basic(optional = false)
    @Column(name = "pending")
    private int pending;
    @Basic(optional = false)
    @Column(name = "closed")
    private int closed;
    @Basic(optional = false)
    @Column(name = "firstcall_resolution")
    private int firstcallResolution;
    @Basic(optional = false)
    @Column(name = "sla_closed1")
    private int slaClosed1;
    @Basic(optional = false)
    @Column(name = "sla_closed2")
    private int slaClosed2;
    @Basic(optional = false)
    @Column(name = "sla_closed3")
    private int slaClosed3;
    @Basic(optional = false)
    @Column(name = "sla_closed4")
    private int slaClosed4;
    @Basic(optional = false)
    @Column(name = "sla_closed5")
    private int slaClosed5;
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Users users;
    @JoinColumn(name = "channel_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Channel channel;
    @JoinColumn(name = "case_detail_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CaseDetail caseDetail;
    @JoinColumn(name = "case_request_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CaseRequest caseRequest;
    @JoinColumn(name = "service_type_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ServiceType serviceType;
    @JoinColumn(name = "location_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Location location;

    public RptContactChannel() {
    }

    public RptContactChannel(RptContactChannelPK rptContactChannelPK) {
        this.rptContactChannelPK = rptContactChannelPK;
    }

    public RptContactChannel(RptContactChannelPK rptContactChannelPK, int total) {
        this.rptContactChannelPK = rptContactChannelPK;
        this.total = total;
    }

    public RptContactChannel(Date contactDate, int userId, int channelId, int caseDetailId, int caseRequestId, int serviceTypeId, int locationId) {
        this.rptContactChannelPK = new RptContactChannelPK(contactDate, userId, channelId, caseDetailId, caseRequestId, serviceTypeId, locationId);
    }

    public RptContactChannelPK getRptContactChannelPK() {
        return rptContactChannelPK;
    }

    public void setRptContactChannelPK(RptContactChannelPK rptContactChannelPK) {
        this.rptContactChannelPK = rptContactChannelPK;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public CaseDetail getCaseDetail() {
        return caseDetail;
    }

    public void setCaseDetail(CaseDetail caseDetail) {
        this.caseDetail = caseDetail;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rptContactChannelPK != null ? rptContactChannelPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RptContactChannel)) {
            return false;
        }
        RptContactChannel other = (RptContactChannel) object;
        if ((this.rptContactChannelPK == null && other.rptContactChannelPK != null) || (this.rptContactChannelPK != null && !this.rptContactChannelPK.equals(other.rptContactChannelPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.RptContactChannel[rptContactChannelPK=" + rptContactChannelPK + "]";
    }

    public int getPending() {
        return pending;
    }

    public void setPending(int pending) {
        this.pending = pending;
    }

    public int getClosed() {
        return closed;
    }

    public void setClosed(int closed) {
        this.closed = closed;
    }

    public int getFirstcallResolution() {
        return firstcallResolution;
    }

    public void setFirstcallResolution(int firstcallResolution) {
        this.firstcallResolution = firstcallResolution;
    }

    public int getSlaClosed1() {
        return slaClosed1;
    }

    public void setSlaClosed1(int slaClosed1) {
        this.slaClosed1 = slaClosed1;
    }

    public int getSlaClosed2() {
        return slaClosed2;
    }

    public void setSlaClosed2(int slaClosed2) {
        this.slaClosed2 = slaClosed2;
    }

    public int getSlaClosed3() {
        return slaClosed3;
    }

    public void setSlaClosed3(int slaClosed3) {
        this.slaClosed3 = slaClosed3;
    }

    public int getSlaClosed4() {
        return slaClosed4;
    }

    public void setSlaClosed4(int slaClosed4) {
        this.slaClosed4 = slaClosed4;
    }

    public int getSlaClosed5() {
        return slaClosed5;
    }

    public void setSlaClosed5(int slaClosed5) {
        this.slaClosed5 = slaClosed5;
    }

}
