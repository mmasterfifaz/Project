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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author oat
 */
@Entity
@Table(name = "assignment_pooling_daily")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AssignmentPoolingDaily.findAll", query = "SELECT a FROM AssignmentPoolingDaily a")})
public class AssignmentPoolingDaily implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AssignmentPoolingDailyPK assignmentPoolingDailyPK;
    @Basic(optional = false)
    @Column(name = "no_used", nullable = false)
    private int noUsed;
    @JoinColumn(name = "assignment_pooling_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private AssignmentPooling assignmentPooling;

    public AssignmentPoolingDaily() {
    }

    public AssignmentPoolingDaily(AssignmentPoolingDailyPK assignmentPoolingDailyPK) {
        this.assignmentPoolingDailyPK = assignmentPoolingDailyPK;
    }

    public AssignmentPoolingDaily(AssignmentPoolingDailyPK assignmentPoolingDailyPK, int noUsed) {
        this.assignmentPoolingDailyPK = assignmentPoolingDailyPK;
        this.noUsed = noUsed;
    }

    public AssignmentPoolingDaily(Date transDate, int assignmentPoolingId) {
        this.assignmentPoolingDailyPK = new AssignmentPoolingDailyPK(transDate, assignmentPoolingId);
    }

    public AssignmentPoolingDailyPK getAssignmentPoolingDailyPK() {
        return assignmentPoolingDailyPK;
    }

    public void setAssignmentPoolingDailyPK(AssignmentPoolingDailyPK assignmentPoolingDailyPK) {
        this.assignmentPoolingDailyPK = assignmentPoolingDailyPK;
    }

    public int getNoUsed() {
        return noUsed;
    }

    public void setNoUsed(int noUsed) {
        this.noUsed = noUsed;
    }

    public AssignmentPooling getAssignmentPooling() {
        return assignmentPooling;
    }

    public void setAssignmentPooling(AssignmentPooling assignmentPooling) {
        this.assignmentPooling = assignmentPooling;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (assignmentPoolingDailyPK != null ? assignmentPoolingDailyPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AssignmentPoolingDaily)) {
            return false;
        }
        AssignmentPoolingDaily other = (AssignmentPoolingDaily) object;
        if ((this.assignmentPoolingDailyPK == null && other.assignmentPoolingDailyPK != null) || (this.assignmentPoolingDailyPK != null && !this.assignmentPoolingDailyPK.equals(other.assignmentPoolingDailyPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.AssignmentPoolingDaily[ assignmentPoolingDailyPK=" + assignmentPoolingDailyPK + " ]";
    }
    
}
