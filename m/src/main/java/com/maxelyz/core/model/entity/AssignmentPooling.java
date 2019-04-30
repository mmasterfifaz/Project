/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Collection;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author oat
 */
@Entity
@Table(name = "assignment_pooling")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AssignmentPooling.findAll", query = "SELECT a FROM AssignmentPooling a")})
public class AssignmentPooling implements Serializable {
    @Basic(optional = false)
    @Column(name = "enable", nullable = false)
    private boolean enable;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "assignmentPooling")
    private Collection<AssignmentPoolingDaily> assignmentPoolingDailyCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "no_customer", nullable = false)
    private int noCustomer;
    @Basic(optional = false)
    @Column(name = "no_used", nullable = false)
    private int noUsed;
    @Column(name = "remark")
    private String remark;
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Users user;
    @JoinColumn(name = "assignment_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Assignment assignment;

    public AssignmentPooling() {
    }

    public AssignmentPooling(Integer id) {
        this.id = id;
    }

    public AssignmentPooling(Integer id, int noCustomer, int noUsed) {
        this.id = id;
        this.noCustomer = noCustomer;
        this.noUsed = noUsed;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getNoCustomer() {
        return noCustomer;
    }

    public void setNoCustomer(int noCustomer) {
        this.noCustomer = noCustomer;
    }

    public int getNoUsed() {
        return noUsed;
    }

    public void setNoUsed(int noUsed) {
        this.noUsed = noUsed;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        if (!(object instanceof AssignmentPooling)) {
            return false;
        }
        AssignmentPooling other = (AssignmentPooling) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.AssignmentPooling[ id=" + id + " ]";
    }

    public boolean getEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @XmlTransient
    public Collection<AssignmentPoolingDaily> getAssignmentPoolingDailyCollection() {
        return assignmentPoolingDailyCollection;
    }

    public void setAssignmentPoolingDailyCollection(Collection<AssignmentPoolingDaily> assignmentPoolingDailyCollection) {
        this.assignmentPoolingDailyCollection = assignmentPoolingDailyCollection;
    }
    
}
