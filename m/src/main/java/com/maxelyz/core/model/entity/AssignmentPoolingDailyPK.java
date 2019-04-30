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
 * @author oat
 */
@Embeddable
public class AssignmentPoolingDailyPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "trans_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date transDate;
    @Basic(optional = false)
    @Column(name = "assignment_pooling_id", nullable = false)
    private int assignmentPoolingId;

    public AssignmentPoolingDailyPK() {
    }

    public AssignmentPoolingDailyPK(Date transDate, int assignmentPoolingId) {
        this.transDate = transDate;
        this.assignmentPoolingId = assignmentPoolingId;
    }

    public Date getTransDate() {
        return transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    public int getAssignmentPoolingId() {
        return assignmentPoolingId;
    }

    public void setAssignmentPoolingId(int assignmentPoolingId) {
        this.assignmentPoolingId = assignmentPoolingId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transDate != null ? transDate.hashCode() : 0);
        hash += (int) assignmentPoolingId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AssignmentPoolingDailyPK)) {
            return false;
        }
        AssignmentPoolingDailyPK other = (AssignmentPoolingDailyPK) object;
        if ((this.transDate == null && other.transDate != null) || (this.transDate != null && !this.transDate.equals(other.transDate))) {
            return false;
        }
        if (this.assignmentPoolingId != other.assignmentPoolingId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.AssignmentPoolingDailyPK[ transDate=" + transDate + ", assignmentPoolingId=" + assignmentPoolingId + " ]";
    }
    
}
