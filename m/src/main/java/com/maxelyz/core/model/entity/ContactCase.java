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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "contact_case")
@NamedQueries({
@NamedQuery(name = "ContactCase.findAll", query = "SELECT c FROM ContactCase c")})
public class ContactCase implements Serializable {
   
    @JoinColumn(name = "service_type_id", referencedColumnName = "id")
    @ManyToOne
    private ServiceType serviceType;
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    @ManyToOne
    private Location location;
    @Column(name = "location_name")
    private String locationName;
    @Column(name = "closed_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date closedDate;
    @JoinColumn(name = "closed_by_id", referencedColumnName = "id")
    @ManyToOne
    private Users usersClosed;
    @JoinColumn(name = "update_by_id", referencedColumnName = "id")
    @ManyToOne
    private Users usersUpdate;
    @JoinColumn(name = "create_by_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users usersCreate;

    @JoinColumn(name = "contact_history_id", referencedColumnName = "id")
    @ManyToOne
    private ContactHistory contactHistory;
    @OneToMany(mappedBy = "contactCase")
    private Collection<ContactCase> contactCaseCollection;
    @JoinColumn(name = "ref_id", referencedColumnName = "id")
    @ManyToOne
    private ContactCase contactCase;
    @JoinColumn(name = "latest_activity_id", referencedColumnName = "id")
    @ManyToOne
    private Activity activity;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contactCase")
    private Collection<Activity> activityCollection;
    @JoinColumn(name = "latest_taskdelegate_id", referencedColumnName = "id")
    @ManyToOne
    private Activity activityDelegate;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contactCase")
    private Collection<ContactCaseWorkflowLog> contactCaseWorkflowLogCollection;
    
    @JoinColumn(name = "business_unit_id", referencedColumnName = "id")
    @ManyToOne
    private BusinessUnit businessUnit;
  
    @Column(name = "workflow_seq_no")
    private Integer workflowSeqNo;
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "code")
    private String code;
    @Column(name = "contact_person")
    private String contactPerson;
    @Column(name = "relationship_id")
    private Integer relationshipId;
    @Basic(optional = false)
    @Column(name = "contact_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date contactDate;
    @Lob
    @Column(name = "description")
    private String description;
    @Lob
    @Column(name = "remark")
    private String remark;
    @Basic(optional = false)
    @Column(name = "priority")
    private String priority;
    @Basic(optional = false)
    @Column(name = "status")
    private String status;
    @Column(name = "schedule_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date scheduleDate;
    @Lob
    @Column(name = "schedule_description")
    private String scheduleDescription;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "create_by")
    private String createBy;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "update_by")
    private String updateBy;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "caseId")
    private Collection<CaseAttachment> caseAttachmentCollection;
    @JoinColumn(name = "case_detail_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CaseDetail caseDetailId;
    @JoinColumn(name = "case_request_id", referencedColumnName = "id")
    @ManyToOne
    private CaseRequest caseRequestId;
    @JoinColumn(name = "channel_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Channel channelId;
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Customer customerId;
    @Column(name = "sla_close")
    private Double slaClose;
    @Column(name = "sla_close_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date slaCloseDate;
    @Column(name = "sla_accept")
    private Double slaAccept;
    @Column(name = "telephony_track_id")
    private String telephonyTrackId;
    @Column(name = "contact_to")
    private String contactTo;
    @Column(name = "workflow")
    private Boolean workflow;
    @Column(name = "attach_file")
    private Boolean attachFile;
    @Column(name = "related_sale")
    private String relatedSale;
    
    public ContactCase() {
    }

    public ContactCase(Integer id) {
        this.id = id;
    }

    public ContactCase(Integer id, String code, Date contactDate, String priority, String status) {
        this.id = id;
        this.code = code;
        this.contactDate = contactDate;
        this.priority = priority;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public Integer getRelationshipId() {
        return relationshipId;
    }

    public void setRelationshipId(Integer relationshipId) {
        this.relationshipId = relationshipId;
    }

    public Date getContactDate() {
        return contactDate;
    }

    public void setContactDate(Date contactDate) {
        this.contactDate = contactDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getScheduleDescription() {
        return scheduleDescription;
    }

    public void setScheduleDescription(String scheduleDescription) {
        this.scheduleDescription = scheduleDescription;
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

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Collection<CaseAttachment> getCaseAttachmentCollection() {
        return caseAttachmentCollection;
    }

    public void setCaseAttachmentCollection(Collection<CaseAttachment> caseAttachmentCollection) {
        this.caseAttachmentCollection = caseAttachmentCollection;
    }

    public CaseDetail getCaseDetailId() {
        return caseDetailId;
    }

    public void setCaseDetailId(CaseDetail caseDetailId) {
        this.caseDetailId = caseDetailId;
    }

    public CaseRequest getCaseRequestId() {
        return caseRequestId;
    }

    public void setCaseRequestId(CaseRequest caseRequestId) {
        this.caseRequestId = caseRequestId;
    }

    public Channel getChannelId() {
        return channelId;
    }

    public void setChannelId(Channel channelId) {
        this.channelId = channelId;
    }

    public Collection<ContactCase> getContactCaseCollection() {
        return contactCaseCollection;
    }

    public void setContactCaseCollection(Collection<ContactCase> contactCaseCollection) {
        this.contactCaseCollection = contactCaseCollection;
    }


    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }

    public Collection<Activity> getActivityCollection() {
        return activityCollection;
    }

    public void setActivityCollection(Collection<Activity> activityCollection) {
        this.activityCollection = activityCollection;
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
        if (!(object instanceof ContactCase)) {
            return false;
        }
        ContactCase other = (ContactCase) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.ContactCase[id=" + id + "]";
    }

    public ContactCase getContactCase() {
        return contactCase;
    }

    public void setContactCase(ContactCase contactCase) {
        this.contactCase = contactCase;
    }


    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public ContactHistory getContactHistory() {
        return contactHistory;
    }

    public void setContactHistory(ContactHistory contactHistory) {
        this.contactHistory = contactHistory;
    }


    public Date getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(Date closedDate) {
        this.closedDate = closedDate;
    }

    public Users getUsersClosed() {
        return usersClosed;
    }

    public void setUsersClosed(Users usersClosed) {
        this.usersClosed = usersClosed;
    }

    public Users getUsersCreate() {
        return usersCreate;
    }

    public void setUsersCreate(Users usersCreate) {
        this.usersCreate = usersCreate;
    }

    public Users getUsersUpdate() {
        return usersUpdate;
    }

    public void setUsersUpdate(Users usersUpdate) {
        this.usersUpdate = usersUpdate;
    }

    public Double getSlaAccept() {
        return slaAccept;
    }

    public void setSlaAccept(Double slaAccept) {
        this.slaAccept = slaAccept;
    }

    public Double getSlaClose() {
        return slaClose;
    }

    public void setSlaClose(Double slaClose) {
        this.slaClose = slaClose;
    }

    public Date getSlaCloseDate() {
        return slaCloseDate;
    }

    public void setSlaCloseDate(Date slaCloseDate) {
        this.slaCloseDate = slaCloseDate;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public String getContactTo() {
        return contactTo;
    }

    public void setContactTo(String contactTo) {
        this.contactTo = contactTo;
    }

    public String getTelephonyTrackId() {
        return telephonyTrackId;
    }

    public void setTelephonyTrackId(String telephonyTrackId) {
        this.telephonyTrackId = telephonyTrackId;
    }

    public BusinessUnit getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
    } 

    public Collection<ContactCaseWorkflowLog> getContactCaseWorkflowLogCollection() {
        return contactCaseWorkflowLogCollection;
    }

    public void setContactCaseWorkflowLogCollection(Collection<ContactCaseWorkflowLog> contactCaseWorkflowLogCollection) {
        this.contactCaseWorkflowLogCollection = contactCaseWorkflowLogCollection;
    }

    public Boolean getWorkflow() {
        return workflow;
    }

    public void setWorkflow(Boolean workflow) {
        this.workflow = workflow;
    }

    public Integer getWorkflowSeqNo() {
        return workflowSeqNo;
    }

    public void setWorkflowSeqNo(Integer workflowSeqNo) {
        this.workflowSeqNo = workflowSeqNo;
    }
    
    public Boolean getAttachFile() {
        return attachFile;
    }

    public void setAttachFile(Boolean attachFile) {
        this.attachFile = attachFile;
    }

    public String getRelatedSale() {
        return relatedSale;
    }

    public void setRelatedSale(String relatedSale) {
        this.relatedSale = relatedSale;
    }

    public Activity getActivityDelegate() {
        return activityDelegate;
    }

    public void setActivityDelegate(Activity activityDelegate) {
        this.activityDelegate = activityDelegate;
    }
    
}
