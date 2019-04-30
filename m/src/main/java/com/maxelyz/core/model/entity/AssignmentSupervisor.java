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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author oat
 */
@Entity
@Table(name = "assignment_supervisor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AssignmentSupervisor.findAll", query = "SELECT a FROM AssignmentSupervisor a")})
public class AssignmentSupervisor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "no_customer")
    private Integer noCustomer;
    @Column(name = "no_used")
    private Integer noUsed;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "create_by", length = 100)
    private String createBy;
    @Column(name = "create_by_id")
    private Integer createById;
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Users userId;
    @JoinColumn(name = "assignment_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Assignment assignmentId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "assignmentSupervisorId")
    private Collection<AssignmentSupervisorDetail> assignmentSupervisorDetailCollection;
    @Column(name = "reset_new_list")
    private Boolean resetNewList;

    public AssignmentSupervisor() {
    }

    public AssignmentSupervisor(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNoCustomer() {
        return noCustomer;
    }

    public void setNoCustomer(Integer noCustomer) {
        this.noCustomer = noCustomer;
    }

    public Integer getNoUsed() {
        return noUsed;
    }

    public void setNoUsed(Integer noUsed) {
        this.noUsed = noUsed;
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

    public Integer getCreateById() {
        return createById;
    }

    public void setCreateById(Integer createById) {
        this.createById = createById;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    public Assignment getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Assignment assignmentId) {
        this.assignmentId = assignmentId;
    }

    @XmlTransient
    public Collection<AssignmentSupervisorDetail> getAssignmentSupervisorDetailCollection() {
        return assignmentSupervisorDetailCollection;
    }

    public void setAssignmentSupervisorDetailCollection(Collection<AssignmentSupervisorDetail> assignmentSupervisorDetailCollection) {
        this.assignmentSupervisorDetailCollection = assignmentSupervisorDetailCollection;
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
        if (!(object instanceof AssignmentSupervisor)) {
            return false;
        }
        AssignmentSupervisor other = (AssignmentSupervisor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.AssignmentSupervisor[ id=" + id + " ]";
    }
    
}
