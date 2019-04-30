/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "contact_case_workflow_log")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ContactCaseWorkflowLog.findAll", query = "SELECT c FROM ContactCaseWorkflowLog c")})
public class ContactCaseWorkflowLog implements Serializable {
    @JoinColumn(name = "contact_case_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ContactCase contactCase;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "seq_no", nullable = false)
    private int seqNo;
    @Basic(optional = false)
    @ManyToOne
    @JoinColumn(name = "workflow_rule_id", referencedColumnName = "id")
    private WorkflowRule workflowRule;
    @Basic(optional = false)
    @Column(name = "user_name", nullable = false, length = 100)
    private String userName;
    @Column(name = "user_surname", length = 100)
    private String userSurname;
    @Column(name = "receive_user_name", length = 100)
    private String receiveUserName;
    @Column(name = "receive_user_surname", length = 100)
    private String receiveUserSurname;
    @Basic(optional = false)
    @Column(name = "allow_close_case", nullable = false)
    private boolean allowCloseCase;
    @Basic(optional = false)
    @Column(name = "sent_email", nullable = false)
    private boolean sentEmail;
    @Column(name = "email_to", length = 1000)
    private String emailTo;
    @Column(name = "email_cc", length = 1000)
    private String emailCc;
    @Column(name = "email_bcc", length = 1000)
    private String emailBcc;
    @Column(name = "due_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dueDate;
    @Column(name = "accept_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date acceptDate;
    @Column(name = "accept_status", length = 50)
    private String acceptStatus;
    @Column(name = "remark", length = 1000)
    private String remark;
    @JoinColumn(name = "receive_user_id", referencedColumnName = "id")
    @ManyToOne
    private Users receiveUsers;
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Users users;

    public ContactCaseWorkflowLog() {
    }

    public ContactCaseWorkflowLog(Integer id) {
        this.id = id;
    }

    public ContactCaseWorkflowLog(Integer id, int seqNo, WorkflowRule workflowRule, String userName, boolean allowCloseCase, boolean sentEmail) {
        this.id = id;
        this.seqNo = seqNo;
        this.workflowRule = workflowRule;
        this.userName = userName;
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

    public WorkflowRule getWorkflowRule() {
        return workflowRule;
    }

    public void setWorkflowRule(WorkflowRule workflowRule) {
        this.workflowRule = workflowRule;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }

    public String getReceiveUserName() {
        return receiveUserName;
    }

    public void setReceiveUserName(String receiveUserName) {
        this.receiveUserName = receiveUserName;
    }

    public String getReceiveUserSurname() {
        return receiveUserSurname;
    }

    public void setReceiveUserSurname(String receiveUserSurname) {
        this.receiveUserSurname = receiveUserSurname;
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

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getAcceptDate() {
        return acceptDate;
    }

    public void setAcceptDate(Date acceptDate) {
        this.acceptDate = acceptDate;
    }

    public String getAcceptStatus() {
        return acceptStatus;
    }

    public void setAcceptStatus(String acceptStatus) {
        this.acceptStatus = acceptStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Users getReceiveUsers() {
        return receiveUsers;
    }

    public void setReceiveUsers(Users receiveUsers) {
        this.receiveUsers = receiveUsers;
    }

    public ContactCase getContactCase() {
        return contactCase;
    }

    public void setContactCase(ContactCase contactCase) {
        this.contactCase = contactCase;
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
        if (!(object instanceof ContactCaseWorkflowLog)) {
            return false;
        }
        ContactCaseWorkflowLog other = (ContactCaseWorkflowLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.ContactCaseWorkflowLog[ id=" + id + " ]";
    }
    
}
