/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "assignment_product")
@NamedQueries({@NamedQuery(name = "AssignmentProduct.findAll", query = "SELECT a FROM AssignmentProduct a")})
public class AssignmentProduct implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AssignmentProductPK assignmentProductPK;
    @Basic(optional = false)
    @Column(name = "product_code")
    private String productCode;
    @Basic(optional = false)
    @Column(name = "product_name")
    private String productName;
    @JoinColumn(name = "assignment_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Assignment assignment;
    @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Product product;

    public AssignmentProduct() {
    }

    public AssignmentProduct(AssignmentProductPK assignmentProductPK) {
        this.assignmentProductPK = assignmentProductPK;
    }

    public AssignmentProduct(AssignmentProductPK assignmentProductPK, String productCode, String productName) {
        this.assignmentProductPK = assignmentProductPK;
        this.productCode = productCode;
        this.productName = productName;
    }

    public AssignmentProduct(int assignmentId, int productId) {
        this.assignmentProductPK = new AssignmentProductPK(assignmentId, productId);
    }

    public AssignmentProductPK getAssignmentProductPK() {
        return assignmentProductPK;
    }

    public void setAssignmentProductPK(AssignmentProductPK assignmentProductPK) {
        this.assignmentProductPK = assignmentProductPK;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (assignmentProductPK != null ? assignmentProductPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AssignmentProduct)) {
            return false;
        }
        AssignmentProduct other = (AssignmentProduct) object;
        if ((this.assignmentProductPK == null && other.assignmentProductPK != null) || (this.assignmentProductPK != null && !this.assignmentProductPK.equals(other.assignmentProductPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.AssignmentProduct[assignmentProductPK=" + assignmentProductPK + "]";
    }

}
