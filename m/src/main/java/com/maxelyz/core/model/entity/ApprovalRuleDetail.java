/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author oat
 */
@Entity
@Table(name = "approval_rule_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ApprovalRuleDetail.findAll", query = "SELECT a FROM ApprovalRuleDetail a")})
public class ApprovalRuleDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ApprovalRuleDetailPK approvalRuleDetailPK;
    @Basic(optional = false)
    @Column(name = "field_name", nullable = false, length = 100)
    private String fieldName;
    @Basic(optional = false)
    @Column(name = "operation", nullable = false, length = 10)
    private String operation;
    @Basic(optional = false)
    @Column(name = "value", nullable = false, length = 100)
    private String value;
    @JoinColumn(name = "approval_rule_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ApprovalRule approvalRule;

    public ApprovalRuleDetail() {
    }

    public ApprovalRuleDetail(ApprovalRuleDetailPK approvalRuleDetailPK) {
        this.approvalRuleDetailPK = approvalRuleDetailPK;
    }

    public ApprovalRuleDetail(ApprovalRuleDetailPK approvalRuleDetailPK, String fieldName, String operation, String value) {
        this.approvalRuleDetailPK = approvalRuleDetailPK;
        this.fieldName = fieldName;
        this.operation = operation;
        this.value = value;
    }

    public ApprovalRuleDetail(int approvalRuleId, String type, int seqNo) {
        this.approvalRuleDetailPK = new ApprovalRuleDetailPK(approvalRuleId, type, seqNo);
    }

    public ApprovalRuleDetailPK getApprovalRuleDetailPK() {
        return approvalRuleDetailPK;
    }

    public void setApprovalRuleDetailPK(ApprovalRuleDetailPK approvalRuleDetailPK) {
        this.approvalRuleDetailPK = approvalRuleDetailPK;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ApprovalRule getApprovalRule() {
        return approvalRule;
    }

    public void setApprovalRule(ApprovalRule approvalRule) {
        this.approvalRule = approvalRule;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (approvalRuleDetailPK != null ? approvalRuleDetailPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ApprovalRuleDetail)) {
            return false;
        }
        ApprovalRuleDetail other = (ApprovalRuleDetail) object;
        if ((this.approvalRuleDetailPK == null && other.approvalRuleDetailPK != null) || (this.approvalRuleDetailPK != null && !this.approvalRuleDetailPK.equals(other.approvalRuleDetailPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.ApprovalRuleDetail[ approvalRuleDetailPK=" + approvalRuleDetailPK + " ]";
    }
    
}
