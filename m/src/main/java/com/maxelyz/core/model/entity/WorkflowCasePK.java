/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Oat
 */
@Embeddable
public class WorkflowCasePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "user_group_id", nullable = false)
    private int userGroupId;
    @Basic(optional = false)
    @Column(name = "case_detail_id", nullable = false)
    private int caseDetailId;
    @Basic(optional = false)
    @Column(name = "case_request_id", nullable = false)
    private int caseRequestId;
    @Basic(optional = false)
    @Column(name = "service_type_id", nullable = false)
    private int serviceTypeId;
    @Basic(optional = false)
    @Column(name = "business_unit_id", nullable = false)
    private int businessUnitId;
    @Basic(optional = false)
    @Column(name = "location_id", nullable = false)
    private int locationId;
            

    public WorkflowCasePK() {
    }

    public WorkflowCasePK(int userGroupId, int caseDetailId, int caseRequestId, int serviceTypeId, int businessUnitId, int locationId) {
        this.userGroupId = userGroupId;
        this.caseDetailId = caseDetailId;
        this.caseRequestId = caseRequestId;
        this.serviceTypeId = serviceTypeId;
        this.businessUnitId = businessUnitId;
        this.locationId = locationId;
    }

    public int getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(int userGroupId) {
        this.userGroupId = userGroupId;
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

    public int getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(int businessUnitId) {
        this.businessUnitId = businessUnitId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(int serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) userGroupId;
        hash += (int) caseDetailId;
        hash += (int) caseRequestId;
        hash += (int) serviceTypeId;
        hash += (int) businessUnitId;
        hash += (int) locationId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WorkflowCasePK)) {
            return false;
        }
        WorkflowCasePK other = (WorkflowCasePK) object;
        if (this.userGroupId != other.userGroupId) {
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
        if (this.businessUnitId != other.businessUnitId) {
            return false;
        }
        if (this.locationId != other.locationId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.WorkflowCasePK[ userGroupId=" + userGroupId + ", caseDetailId=" + caseDetailId + ", caseRequestId=" + caseRequestId + ", serviceTypeId=" + serviceTypeId + ", businessUnitId=" + businessUnitId +", locationId=" + locationId + " ]";
    }
    
}
