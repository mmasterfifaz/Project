/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author ukritj
 */
@Entity
@Table(name = "assignment_product_filter")
public class AssignmentProductFilter implements Serializable {
    
    @EmbeddedId
    protected AssignmentProductFilterPK assignmentProductFilterPK;
    @JoinColumn(name = "assignment_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Assignment assignment;
    @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Product product;
    @Basic(optional = false)
    @Column(name = "date_from")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFrom;
    @Basic(optional = false)
    @Column(name = "date_to")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTo;

    public AssignmentProductFilter() {
    }

    public AssignmentProductFilter(AssignmentProductFilterPK assignmentProductFilterPK) {
        this.assignmentProductFilterPK = assignmentProductFilterPK;
    }

    public AssignmentProductFilter(AssignmentProductFilterPK assignmentProductFilterPK, Date dateFrom, Date dateTo) {
        this.assignmentProductFilterPK = assignmentProductFilterPK;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public AssignmentProductFilterPK getAssignmentProductFilterPK() {
        return assignmentProductFilterPK;
    }

    public void setAssignmentProductFilterPK(AssignmentProductFilterPK assignmentProductFilterPK) {
        this.assignmentProductFilterPK = assignmentProductFilterPK;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }
    
    
}
