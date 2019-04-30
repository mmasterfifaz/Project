/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "contact_history")
@NamedQueries({
    @NamedQuery(name = "ContactHistory.findAll", query = "SELECT c FROM ContactHistory c")})
public class ContactHistory implements Serializable {
    @Column(name = "unique_id", length = 50)
    private String uniqueId;
    @Column(name = "contact_to_name", length = 100)
    private String contactToName;
   
    @JoinTable(name = "contact_history_purchase_order", joinColumns = {
        @JoinColumn(name = "contact_history_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "purchase_order_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<PurchaseOrder> purchaseOrderCollection;
    @JoinTable(name = "contact_history_assignment", joinColumns = {
        @JoinColumn(name = "contact_history_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "assignment_detail_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<AssignmentDetail> assignmentDetailCollection;

    @JoinColumn(name = "create_by_id", referencedColumnName = "id")
    @ManyToOne
    private Users users;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contactHistory")
    private Collection<ContactHistoryProduct> contactHistoryProductCollection;
    @OneToMany(mappedBy = "contactHistory")
    private Collection<ContactCase> contactCaseCollection;
    @OneToMany(mappedBy = "contactHistory")
    private Collection<ContactHistorySaleResult> contactHistorySaleResultCollection;
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "contact_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date contactDate;
    @Column(name = "contact_to")
    private String contactTo;
    @Basic(optional = false)
    @Column(name = "contact_status")
    private String contactStatus;
    @Basic(optional = false)
    @Column(name = "call_success")
    private boolean callSuccess;
    @Basic(optional = false)
    @Column(name = "dmccontact")
    private boolean dmccontact;
    @Basic(optional = false)
    @Column(name = "followupsale")
    private boolean followupsale;
    @Column(name = "telephony_track_id")
    private String telephonyTrackId;
    @Column(name = "remark")
    private String remark;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "create_by")
    private String createBy;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contactHistory")
    private Collection<ContactHistoryKnowledge> contactHistoryKnowledgeCollection;
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @ManyToOne
    private Customer customer;
    @JoinColumn(name = "followupsale_reason_id", referencedColumnName = "id")
    @ManyToOne
    private FollowupsaleReason followupsaleReason;
    @JoinColumn(name = "dmccontact_reason_id", referencedColumnName = "id")
    @ManyToOne
    private DmccontactReason dmccontactReason;
    @JoinColumn(name = "channel_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Channel channel;
    //@Basic(optional = false)
    @JoinColumn(name = "contact_result_id", referencedColumnName = "id")
    @ManyToOne
    private ContactResult contactResult;
    //@Basic(optional = false)
    @Column(name = "contact_close")
    private Boolean contactClose;
    @Column(name = "talk_time")
    private Integer talkTime;
    @Column(name = "station_no")
    private String stationNo;
    @Column(name = "voice_source")
    private String voiceSource;

    public ContactHistory() {
    }

    public ContactHistory(Integer id) {
        this.id = id;
    }

    public ContactHistory(Integer id, Date contactDate, String contactStatus, boolean callSuccess, boolean dmccontact, boolean followupsale) {
        this.id = id;
        this.contactDate = contactDate;
        this.contactStatus = contactStatus;
        this.callSuccess = callSuccess;
        this.dmccontact = dmccontact;
        this.followupsale = followupsale;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getContactDate() {
        return contactDate;
    }

    public void setContactDate(Date contactDate) {
        this.contactDate = contactDate;
    }

    public String getContactStatus() {
        return contactStatus;
    }

    public void setContactStatus(String contactStatus) {
        this.contactStatus = contactStatus;
    }

    public boolean getCallSuccess() {
        return callSuccess;
    }

    public void setCallSuccess(boolean callSuccess) {
        this.callSuccess = callSuccess;
    }

    public boolean getDmccontact() {
        return dmccontact;
    }

    public void setDmccontact(boolean dmccontact) {
        this.dmccontact = dmccontact;
    }

    public boolean getFollowupsale() {
        return followupsale;
    }

    public void setFollowupsale(boolean followupsale) {
        this.followupsale = followupsale;
    }

    public Boolean getContactClose() {
        return contactClose;
    }

    public void setContactClose(Boolean contactClose) {
        this.contactClose = contactClose;
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

    public Collection<ContactHistoryKnowledge> getContactHistoryKnowledgeCollection() {
        return contactHistoryKnowledgeCollection;
    }

    public void setContactHistoryKnowledgeCollection(Collection<ContactHistoryKnowledge> contactHistoryKnowledgeCollection) {
        this.contactHistoryKnowledgeCollection = contactHistoryKnowledgeCollection;
    }

    public FollowupsaleReason getFollowupsaleReason() {
        return followupsaleReason;
    }

    public void setFollowupsaleReason(FollowupsaleReason followupsaleReason) {
        this.followupsaleReason = followupsaleReason;
    }

    public DmccontactReason getDmccontactReason() {
        return dmccontactReason;
    }

    public void setDmccontactReason(DmccontactReason dmccontactReason) {
        this.dmccontactReason = dmccontactReason;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
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
        if (!(object instanceof ContactHistory)) {
            return false;
        }
        ContactHistory other = (ContactHistory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.ContactHistory[id=" + id + "]";
    }

    public Collection<ContactCase> getContactCaseCollection() {
        return contactCaseCollection;
    }

    public void setContactCaseCollection(Collection<ContactCase> contactCaseCollection) {
        this.contactCaseCollection = contactCaseCollection;
    }

    public Collection<ContactHistoryProduct> getContactHistoryProductCollection() {
        return contactHistoryProductCollection;
    }

    public void setContactHistoryProductCollection(Collection<ContactHistoryProduct> contactHistoryProductCollection) {
        this.contactHistoryProductCollection = contactHistoryProductCollection;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public ContactResult getContactResult() {
        return contactResult;
    }

    public void setContactResult(ContactResult contactResult) {
        this.contactResult = contactResult;
    }

    public Collection<PurchaseOrder> getPurchaseOrderCollection() {
        return purchaseOrderCollection;
    }

    public void setPurchaseOrderCollection(Collection<PurchaseOrder> purchaseOrderCollection) {
        this.purchaseOrderCollection = purchaseOrderCollection;
    }

    public Collection<AssignmentDetail> getAssignmentDetailCollection() {
        return assignmentDetailCollection;
    }

    public void setAssignmentDetailCollection(Collection<AssignmentDetail> assignmentDetailCollection) {
        this.assignmentDetailCollection = assignmentDetailCollection;
    }

    public Collection<ContactHistorySaleResult> getContactHistorySaleResultCollection() {
        return contactHistorySaleResultCollection;
    }

    public void setContactHistorySaleResultCollection(Collection<ContactHistorySaleResult> contactHistorySaleResultCollection) {
        this.contactHistorySaleResultCollection = contactHistorySaleResultCollection;
    }

    public String getTelephonyTrackId() {
        return telephonyTrackId;
    }

    public void setTelephonyTrackId(String telephonyTrackId) {
        this.telephonyTrackId = telephonyTrackId;
    }

    
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getContactTo() {
        return contactTo;
    }

    public void setContactTo(String contactTo) {
        this.contactTo = contactTo;
    }

    public Integer getTalkTime() {
        return talkTime;
    }

    public void setTalkTime(Integer talkTime) {
        this.talkTime = talkTime;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getContactToName() {
        return contactToName;
    }

    public void setContactToName(String contactToName) {
        this.contactToName = contactToName;
    }

    public String getStationNo() {
        return stationNo;
    }

    public void setStationNo(String stationNo) {
        this.stationNo = stationNo;
    }

    public String getVoiceSource() {
        return voiceSource;
    }

    public void setVoiceSource(String voiceSource) {
        this.voiceSource = voiceSource;
    }
}
