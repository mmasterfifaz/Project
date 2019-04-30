/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author oat
 */
@Entity
@Table(name = "assignment_transfer_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AssignmentTransferDetail.findAll", query = "SELECT a FROM AssignmentTransferDetail a")})
public class AssignmentTransferDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    /*
    @OneToMany(mappedBy = "assignmentTransferDetail")
    private Collection<AssignmentDetail> assignmentDetailCollection;*/
    @JoinColumn(name = "assignment_transfer_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private AssignmentTransfer assignmentTransfer;
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @ManyToOne
    private Customer customer;
    

    public AssignmentTransferDetail() {
    }

    public AssignmentTransferDetail(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /*
    @XmlTransient
    public Collection<AssignmentDetail> getAssignmentDetailCollection() {
        return assignmentDetailCollection;
    }

    public void setAssignmentDetailCollection(Collection<AssignmentDetail> assignmentDetailCollection) {
        this.assignmentDetailCollection = assignmentDetailCollection;
    }*/

    public AssignmentTransfer getAssignmentTransfer() {
        return assignmentTransfer;
    }

    public void setAssignmentTransfer(AssignmentTransfer assignmentTransfer) {
        this.assignmentTransfer = assignmentTransfer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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
        if (!(object instanceof AssignmentTransferDetail)) {
            return false;
        }
        AssignmentTransferDetail other = (AssignmentTransferDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.AssignmentTransferDetail[ id=" + id + " ]";
    }
    
}
