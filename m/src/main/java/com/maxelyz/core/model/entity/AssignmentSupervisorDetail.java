/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
 * @author oat
 */
@Entity
@Table(name = "assignment_supervisor_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AssignmentSupervisorDetail.findAll", query = "SELECT a FROM AssignmentSupervisorDetail a")})
public class AssignmentSupervisorDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "create_by", length = 100)
    private String createBy;
    @Column(name = "assign")
    private Boolean assign;
    @Column(name = "assign_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date assignDate;
    @Column(name = "unassign")
    private Boolean unassign;
    @Column(name = "unassign_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date unassignDate;
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Customer customerId;
    @JoinColumn(name = "assignment_supervisor_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private AssignmentSupervisor assignmentSupervisorId;
    @JoinColumn(name = "unassignment_id", referencedColumnName = "id")
    @ManyToOne
    private Unassignment unassignment;
    @Column(name = "new_list")
    private Boolean newList;
    @Column(name = "reset_new_list")
    private Boolean resetNewList;

    public AssignmentSupervisorDetail() {
    }

    public AssignmentSupervisorDetail(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Boolean getUnassign() {
        return unassign;
    }

    public void setUnassign(Boolean unassign) {
        this.unassign = unassign;
    }

    public Date getUnassignDate() {
        return unassignDate;
    }

    public void setUnassignDate(Date unassignDate) {
        this.unassignDate = unassignDate;
    }

    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }

    public AssignmentSupervisor getAssignmentSupervisorId() {
        return assignmentSupervisorId;
    }

    public void setAssignmentSupervisorId(AssignmentSupervisor assignmentSupervisorId) {
        this.assignmentSupervisorId = assignmentSupervisorId;
    }

    public Unassignment getUnassignment() {
        return unassignment;
    }

    public void setUnassignment(Unassignment unassignment) {
        this.unassignment = unassignment;
    }

    public Boolean getAssign() {
        return assign;
    }

    public void setAssign(Boolean assign) {
        this.assign = assign;
    }

    public Date getAssignDate() {
        return assignDate;
    }

    public void setAssignDate(Date assignDate) {
        this.assignDate = assignDate;
    }

    public Boolean getNewList() {
        return newList;
    }

    public void setNewList(Boolean newList) {
        this.newList = newList;
    }

    public Boolean getResetNewList() {
        return resetNewList;
    }

    public void setResetNewList(Boolean resetNewList) {
        this.resetNewList = resetNewList;
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
        if (!(object instanceof AssignmentSupervisorDetail)) {
            return false;
        }
        AssignmentSupervisorDetail other = (AssignmentSupervisorDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.AssignmentSupervisorDetail[ id=" + id + " ]";
    }
    
}
