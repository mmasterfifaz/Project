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
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "assignment")
@NamedQueries({@NamedQuery(name = "Assignment.findAll", query = "SELECT a FROM Assignment a")})
public class Assignment implements Serializable {
    @Basic(optional = false)
    @Column(name = "assign_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date assignDate;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "pooling_from_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date poolingFromDate;
    @Column(name = "pooling_to_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date poolingToDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "assignment")
    private Collection<AssignmentPooling> assignmentPoolingCollection;
    @Column(name = "parent_assignment_supervisor_id")
    private Integer parentAssignmentSupervisorId;
    @Column(name = "assignment_mode", length = 50)
    private String assignmentMode;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "assignmentId")
    private Collection<AssignmentSupervisor> assignmentSupervisorCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "assignment_type")
    private String assignmentType;
    @Column(name = "criteria")
    private String criteria;
    @Column(name = "new_list")
    private Boolean newList;
    @Column(name = "no_customer")
    private Integer noCustomer;
    @Column(name = "no_user")
    private Integer noUser;
    @Column(name = "create_by")
    private String createBy;
    @JoinColumn(name = "create_by_id", referencedColumnName = "id")
    @ManyToOne
    private Users createByUser;
    @JoinColumn(name = "marketing_id", referencedColumnName = "id")
    @ManyToOne
    private Marketing marketing;
    @JoinColumn(name = "customer_type_id", referencedColumnName = "id")
    @ManyToOne
    private CustomerType customerTypeId;
    @JoinColumn(name = "campaign_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Campaign campaign;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "assignmentId")
    private Collection<AssignmentDetail> assignmentDetailCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "assignment")
    private Collection<AssignmentProduct> assignmentProductCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "assignment")
    private Collection<AssignmentProductFilter> assignmentProductFilterCollection;

    @Column(name = "reset_new_list")
    private Boolean resetNewList;
            
    public Assignment() {
    }

    public Assignment(Integer id) {
        this.id = id;
    }

    public Assignment(Integer id, Date assignDate) {
        this.id = id;
        this.assignDate = assignDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getAssignDate() {
        return assignDate;
    }

    public void setAssignDate(Date assignDate) {
        this.assignDate = assignDate;
    }

    public String getAssignmentType() {
        return assignmentType;
    }

    public void setAssignmentType(String assignmentType) {
        this.assignmentType = assignmentType;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public Boolean getNewList() {
        return newList;
    }

    public void setNewList(Boolean newList) {
        this.newList = newList;
    }

    public Integer getNoCustomer() {
        return noCustomer;
    }

    public void setNoCustomer(Integer noCustomer) {
        this.noCustomer = noCustomer;
    }

    public Integer getNoUser() {
        return noUser;
    }

    public void setNoUser(Integer noUser) {
        this.noUser = noUser;
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

    public Marketing getMarketing() {
        return marketing;
    }

    public void setMarketing(Marketing marketing) {
        this.marketing = marketing;
    }

    public CustomerType getCustomerTypeId() {
        return customerTypeId;
    }

    public void setCustomerTypeId(CustomerType customerTypeId) {
        this.customerTypeId = customerTypeId;
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
        if (!(object instanceof Assignment)) {
            return false;
        }
        Assignment other = (Assignment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.Assignment[id=" + id + "]";
    }


    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public Collection<AssignmentProduct> getAssignmentProductCollection() {
        return assignmentProductCollection;
    }

    public void setAssignmentProductCollection(Collection<AssignmentProduct> assignmentProductCollection) {
        this.assignmentProductCollection = assignmentProductCollection;
    }

    public Collection<AssignmentDetail> getAssignmentDetailCollection() {
        return assignmentDetailCollection;
    }

    public void setAssignmentDetailCollection(Collection<AssignmentDetail> assignmentDetailCollection) {
        this.assignmentDetailCollection = assignmentDetailCollection;
    }

    public Integer getParentAssignmentSupervisorId() {
        return parentAssignmentSupervisorId;
    }

    public void setParentAssignmentSupervisorId(Integer parentAssignmentSupervisorId) {
        this.parentAssignmentSupervisorId = parentAssignmentSupervisorId;
    }

    public String getAssignmentMode() {
        return assignmentMode;
    }

    public void setAssignmentMode(String assignmentMode) {
        this.assignmentMode = assignmentMode;
    }

    public Users getCreateByUser() {
        return createByUser;
    }

    public void setCreateByUser(Users createByUser) {
        this.createByUser = createByUser;
    }

    public Date getPoolingFromDate() {
        return poolingFromDate;
    }

    public void setPoolingFromDate(Date poolingFromDate) {
        this.poolingFromDate = poolingFromDate;
    }

    public Date getPoolingToDate() {
        return poolingToDate;
    }

    public void setPoolingToDate(Date poolingToDate) {
        this.poolingToDate = poolingToDate;
    }

    @XmlTransient
    public Collection<AssignmentSupervisor> getAssignmentSupervisorCollection() {
        return assignmentSupervisorCollection;
    }

    public void setAssignmentSupervisorCollection(Collection<AssignmentSupervisor> assignmentSupervisorCollection) {
        this.assignmentSupervisorCollection = assignmentSupervisorCollection;
    }

    @XmlTransient
    public Collection<AssignmentPooling> getAssignmentPoolingCollection() {
        return assignmentPoolingCollection;
    }

    public void setAssignmentPoolingCollection(Collection<AssignmentPooling> assignmentPoolingCollection) {
        this.assignmentPoolingCollection = assignmentPoolingCollection;
    }

    public Collection<AssignmentProductFilter> getAssignmentProductFilterCollection() {
        return assignmentProductFilterCollection;
    }

    public void setAssignmentProductFilterCollection(Collection<AssignmentProductFilter> assignmentProductFilterCollection) {
        this.assignmentProductFilterCollection = assignmentProductFilterCollection;
    }

    public Boolean getResetNewList() {
        return resetNewList;
    }

    public void setResetNewList(Boolean resetNewList) {
        this.resetNewList = resetNewList;
    }
    
    
}
