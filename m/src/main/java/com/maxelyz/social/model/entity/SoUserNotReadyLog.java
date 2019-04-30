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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import com.maxelyz.core.model.entity.Users;
import javax.persistence.ManyToOne;
/**
 *
 * @author Administrator
 */

@Entity
@Table(name = "so_user_notready_log")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SoUserNotReadyLog.findAll", query = "SELECT l FROM SoUserNotReadyLog l")})
public class SoUserNotReadyLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users users;
    @Column(name = "notready_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date notReadyTime;
    @Size(min = 1, max = 255)
    @Column(name = "reason", nullable = false, length = 255)
    private String reason;
    
    public SoUserNotReadyLog() {
    }

    public SoUserNotReadyLog(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Date getNotReadyTime() {
        return notReadyTime;
    }

    public void setNotReadyTime(Date notReadyTime) {
        this.notReadyTime = notReadyTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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
        if (!(object instanceof SoUserNotReadyLog)) {
            return false;
        }
        SoUserNotReadyLog other = (SoUserNotReadyLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.social.model.entity.SoUserNotReadyLog[ id=" + id + " ]";
    }
    
}