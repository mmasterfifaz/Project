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
public class AssignmentProductPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "assignment_id")
    private int assignmentId;
    @Basic(optional = false)
    @Column(name = "product_id")
    private int productId;

    public AssignmentProductPK() {
    
    }
    public AssignmentProductPK(int assignmentId, int productId) {
        this.assignmentId = assignmentId;
        this.productId = productId;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) assignmentId;
        hash += (int) productId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AssignmentProductPK)) {
            return false;
        }
        AssignmentProductPK other = (AssignmentProductPK) object;
        if (this.assignmentId != other.assignmentId) {
            return false;
        }
        if (this.productId != other.productId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.AssignmentProductPK[assignmentId=" + assignmentId + ", productId=" + productId + "]";
    }

}
