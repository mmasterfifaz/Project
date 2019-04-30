/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "workflow_rule_detail")
@NamedQueries({
    @NamedQuery(name = "WorkflowRuleDetail.findAll", query = "SELECT w FROM WorkflowRuleDetail w")})
public class WorkflowRuleDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "seq_no")
    private int seqNo;
    @Basic(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users user;
    @Basic(optional = false)
    @Column(name = "allow_close_case")
    private boolean allowCloseCase;
    @Basic(optional = false)
    @Column(name = "sent_email")
    private boolean sentEmail;
    @Column(name = "email_to")
    private String emailTo;
    @Column(name = "email_cc")
    private String emailCc;
    @Column(name = "email_bcc")
    private String emailBcc;
    @Transient
    private String message;
    @JoinColumn(name = "workflow_rule_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private WorkflowRule workflowRule;

    public WorkflowRuleDetail() {
    }

    public WorkflowRuleDetail(Integer id) {
        this.id = id;
    }

    public WorkflowRuleDetail(Integer id, int no, Users user, boolean allowCloseCase, boolean sentEmail) {
        this.id = id;
        this.seqNo = no;
        this.user = user;
        this.allowCloseCase = allowCloseCase;
        this.sentEmail = sentEmail;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public boolean getAllowCloseCase() {
        return allowCloseCase;
    }

    public void setAllowCloseCase(boolean allowCloseCase) {
        this.allowCloseCase = allowCloseCase;
    }

    public boolean getSentEmail() {
        return sentEmail;
    }

    public void setSentEmail(boolean sentEmail) {
        this.sentEmail = sentEmail;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    public String getEmailCc() {
        return emailCc;
    }

    public void setEmailCc(String emailCc) {
        this.emailCc = emailCc;
    }

    public String getEmailBcc() {
        return emailBcc;
    }

    public void setEmailBcc(String emailBcc) {
        this.emailBcc = emailBcc;
    }

    public WorkflowRule getWorkflowRule() {
        return workflowRule;
    }

    public void setWorkflowRule(WorkflowRule workflowRule) {
        this.workflowRule = workflowRule;
    }

    @Transient
    public String getMessage() {
        return message;
    }

    @Transient
    public void setMessage(String message) {
        this.message = message;
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
        if (!(object instanceof WorkflowRuleDetail)) {
            return false;
        }
        WorkflowRuleDetail other = (WorkflowRuleDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.WorkflowRuleDetail[ id=" + id + " ]";
    }
    
}
