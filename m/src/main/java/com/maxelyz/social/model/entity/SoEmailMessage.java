/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.social.model.entity;

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
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author ukritj
 */
@Entity
@Table(name = "so_email_message")
@NamedQueries({
    @NamedQuery(name = "SoEmailMessage.findAll", query = "SELECT s FROM SoEmailMessage s")})
public class SoEmailMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "email_from")
    private String emailFrom;
    @Size(max = 255)
    @Column(name = "email_to")
    private String emailTo;
    @Size(max = 255)
    @Column(name = "email_reply_to")
    private String emailReplyTo;
    @Size(max = 255)
    @Column(name = "email_cc")
    private String emailCc;
    @Size(max = 255)
    @Column(name = "email_bcc")
    private String emailBcc;
    @Lob
    @Column(name = "body")
    private String body;
    @Size(max = 255)
    @Column(name = "subject")
    private String subject;
    @Lob
    @Column(name = "uid")
    private String uid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "direction")
    private String direction;
    @Size(max = 2)
    @Column(name = "send_status")
    private String sendStatus;
    @Column(name = "retry_cnt")
    private Integer retryCnt;
    @JoinColumn(name = "so_message_id", referencedColumnName = "id")
    @OneToOne(optional = false)
    private SoMessage soMessage;
    @Column(name = "received_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receivedDate;
    @Column(name = "sent_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sentDate;
    @Column(name = "attachment")
    private String attachment;
    @Column(name = "attachment_map")
    private String attachmentMap;

    public SoEmailMessage() {
    }

    public SoEmailMessage(Integer id) {
        this.id = id;
    }

    public SoEmailMessage(Integer id, String direction) {
        this.id = id;
        this.direction = direction;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    public Integer getRetryCnt() {
        return retryCnt;
    }

    public void setRetryCnt(Integer retryCnt) {
        this.retryCnt = retryCnt;
    }

    public SoMessage getSoMessage() {
        return soMessage;
    }

    public void setSoMessage(SoMessage soMessage) {
        this.soMessage = soMessage;
    }

    public String getEmailReplyTo() {
        return emailReplyTo;
    }

    public void setEmailReplyTo(String emailReplyTo) {
        this.emailReplyTo = emailReplyTo;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getAttachmentMap() {
        return attachmentMap;
    }

    public void setAttachmentMap(String attachmentMap) {
        this.attachmentMap = attachmentMap;
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
        if (!(object instanceof SoEmailMessage)) {
            return false;
        }
        SoEmailMessage other = (SoEmailMessage) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.social.model.entity.SoEmailMessage[ id=" + id + " ]";
    }
    
}
