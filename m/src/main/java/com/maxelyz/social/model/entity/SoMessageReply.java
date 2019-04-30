/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author apichatt
 */
@Entity
@Table(name = "so_message_reply")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SoMessageReply.findAll", query = "SELECT s FROM SoMessageReply s"),
    @NamedQuery(name = "SoMessageReply.findById", query = "SELECT s FROM SoMessageReply s WHERE s.id = :id"),
    @NamedQuery(name = "SoMessageReply.findByAssignDate", query = "SELECT s FROM SoMessageReply s WHERE s.assignDate = :assignDate"),
    @NamedQuery(name = "SoMessageReply.findByReceiveById", query = "SELECT s FROM SoMessageReply s WHERE s.receiveById = :receiveById"),
    @NamedQuery(name = "SoMessageReply.findByReceiveByName", query = "SELECT s FROM SoMessageReply s WHERE s.receiveByName = :receiveByName"),
    @NamedQuery(name = "SoMessageReply.findByReceiveDate", query = "SELECT s FROM SoMessageReply s WHERE s.receiveDate = :receiveDate"),
    @NamedQuery(name = "SoMessageReply.findByMessageStatus", query = "SELECT s FROM SoMessageReply s WHERE s.messageStatus = :messageStatus")})
public class SoMessageReply implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "assign_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date assignDate;
    @Column(name = "receive_by_id")
    private Integer receiveById;
    @Column(name = "receive_by_name")
    private String receiveByName;
    @Column(name = "receive_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receiveDate;
    @Basic(optional = false)
    @Column(name = "message_status")
    private String messageStatus;

    public SoMessageReply() {
    }

    public SoMessageReply(Long id) {
        this.id = id;
    }

    public SoMessageReply(Long id, Date assignDate, String messageStatus) {
        this.id = id;
        this.assignDate = assignDate;
        this.messageStatus = messageStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getAssignDate() {
        return assignDate;
    }

    public void setAssignDate(Date assignDate) {
        this.assignDate = assignDate;
    }

    public Integer getReceiveById() {
        return receiveById;
    }

    public void setReceiveById(Integer receiveById) {
        this.receiveById = receiveById;
    }

    public String getReceiveByName() {
        return receiveByName;
    }

    public void setReceiveByName(String receiveByName) {
        this.receiveByName = receiveByName;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
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
        if (!(object instanceof SoMessageReply)) {
            return false;
        }
        SoMessageReply other = (SoMessageReply) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.SoMessageReply[ id=" + id + " ]";
    }
    
}
