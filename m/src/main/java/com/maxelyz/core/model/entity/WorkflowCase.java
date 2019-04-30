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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "workflow_case")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WorkflowCase.findAll", query = "SELECT w FROM WorkflowCase w")})
public class WorkflowCase implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected WorkflowCasePK workflowCasePK;
    @Column(name = "create_by", length = 100)
    private String createBy;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "update_by", length = 100)
    private String updateBy;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    
    @ManyToOne
    @JoinColumn(name = "immediately_priority_workflow_rule_id", referencedColumnName = "id")
    private WorkflowRule immediatelyPriorityWorkflowRuleId;
    @ManyToOne
    @JoinColumn(name = "high_priority_workflow_rule_id", referencedColumnName = "id")
    private WorkflowRule highPriorityWorkflowRuleId;
    @ManyToOne
    @JoinColumn(name = "medium_priority_workflow_rule_id", referencedColumnName = "id")
    private WorkflowRule mediumPriorityWorkflowRuleId;
    @ManyToOne
    @JoinColumn(name = "low_priority_workflow_rule_id", referencedColumnName = "id")
    private WorkflowRule lowPriorityWorkflowRuleId;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_group_id", referencedColumnName = "id", insertable = false, updatable = false)
    private UserGroup userGroup;
    @ManyToOne(optional = false) 
    @JoinColumn(name = "service_type_id", referencedColumnName = "id", insertable = false, updatable = false)
    private ServiceType serviceType;
    @ManyToOne(optional = false) 
    @JoinColumn(name = "business_unit_id", referencedColumnName = "id", insertable = false, updatable = false)
    private BusinessUnit businessUnit;
    @ManyToOne(optional = false) 
    @JoinColumn(name = "location_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Location location;
    @ManyToOne(optional = false)
    @JoinColumn(name = "case_request_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CaseRequest caseRequest;
    @ManyToOne(optional = false)
    @JoinColumn(name = "case_detail_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CaseDetail caseDetail;

    public WorkflowCase() {
    }

    public WorkflowCase(WorkflowCasePK workflowCasePK) {
        this.workflowCasePK = workflowCasePK;
    } 

    public WorkflowCase(int userGroupId, int caseDetailId, int caseRequestId, int serviceTypeId, int businessUnitId, int locationId) {
        this.workflowCasePK = new WorkflowCasePK(userGroupId, caseDetailId, caseRequestId, serviceTypeId, businessUnitId, locationId);
    }

    public WorkflowCasePK getWorkflowCasePK() {
        return workflowCasePK;
    }

    public void setWorkflowCasePK(WorkflowCasePK workflowCasePK) {
        this.workflowCasePK = workflowCasePK;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public WorkflowRule getHighPriorityWorkflowRuleId() {
        return highPriorityWorkflowRuleId;
    }

    public void setHighPriorityWorkflowRuleId(WorkflowRule highPriorityWorkflowRuleId) {
        this.highPriorityWorkflowRuleId = highPriorityWorkflowRuleId;
    }

    public WorkflowRule getImmediatelyPriorityWorkflowRuleId() {
        return immediatelyPriorityWorkflowRuleId;
    }

    public void setImmediatelyPriorityWorkflowRuleId(WorkflowRule immediatelyPriorityWorkflowRuleId) {
        this.immediatelyPriorityWorkflowRuleId = immediatelyPriorityWorkflowRuleId;
    }

    public WorkflowRule getLowPriorityWorkflowRuleId() {
        return lowPriorityWorkflowRuleId;
    }

    public void setLowPriorityWorkflowRuleId(WorkflowRule lowPriorityWorkflowRuleId) {
        this.lowPriorityWorkflowRuleId = lowPriorityWorkflowRuleId;
    }

    public WorkflowRule getMediumPriorityWorkflowRuleId() {
        return mediumPriorityWorkflowRuleId;
    }

    public void setMediumPriorityWorkflowRuleId(WorkflowRule mediumPriorityWorkflowRuleId) {
        this.mediumPriorityWorkflowRuleId = mediumPriorityWorkflowRuleId;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public BusinessUnit getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public CaseRequest getCaseRequest() {
        return caseRequest;
    }

    public void setCaseRequest(CaseRequest caseRequest) {
        this.caseRequest = caseRequest;
    }

    public CaseDetail getCaseDetail() {
        return caseDetail;
    }

    public void setCaseDetail(CaseDetail caseDetail) {
        this.caseDetail = caseDetail;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (workflowCasePK != null ? workflowCasePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WorkflowCase)) {
            return false;
        }
        WorkflowCase other = (WorkflowCase) object;
        if ((this.workflowCasePK == null && other.workflowCasePK != null) || (this.workflowCasePK != null && !this.workflowCasePK.equals(other.workflowCasePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.WorkflowCase[ workflowCasePK=" + workflowCasePK + " ]";
    }
    
}
