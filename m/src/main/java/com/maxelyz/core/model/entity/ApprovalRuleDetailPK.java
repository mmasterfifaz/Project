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
 * @author oat
 */
@Embeddable
public class ApprovalRuleDetailPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "approval_rule_id", nullable = false)
    private int approvalRuleId;
    @Basic(optional = false)
    @Column(name = "type", nullable = false, length = 50)
    private String type;
    @Basic(optional = false)
    @Column(name = "seq_no", nullable = false)
    private int seqNo;

    public ApprovalRuleDetailPK() {
    }

    public ApprovalRuleDetailPK(int approvalRuleId, String type, int seqNo) {
        this.approvalRuleId = approvalRuleId;
        this.type = type;
        this.seqNo = seqNo;
    }

    public int getApprovalRuleId() {
        return approvalRuleId;
    }

    public void setApprovalRuleId(int approvalRuleId) {
        this.approvalRuleId = approvalRuleId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) approvalRuleId;
        hash += (type != null ? type.hashCode() : 0);
        hash += (int) seqNo;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ApprovalRuleDetailPK)) {
            return false;
        }
        ApprovalRuleDetailPK other = (ApprovalRuleDetailPK) object;
        if (this.approvalRuleId != other.approvalRuleId) {
            return false;
        }
        if ((this.type == null && other.type != null) || (this.type != null && !this.type.equals(other.type))) {
            return false;
        }
        if (this.seqNo != other.seqNo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.ApprovalRuleDetailPK[ approvalRuleId=" + approvalRuleId + ", type=" + type + ", seqNo=" + seqNo + " ]";
    }
    
}
