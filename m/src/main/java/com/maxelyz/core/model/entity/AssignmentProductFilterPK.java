/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author ukritj
 */
@Embeddable
public class AssignmentProductFilterPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "assignment_id")
    private int assignmentId;
    @Basic(optional = false)
    @Column(name = "product_id")
    private int productId;

    public AssignmentProductFilterPK() {
    
    }
    
    public AssignmentProductFilterPK(int assignmentId, int productId) {
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
    
}
