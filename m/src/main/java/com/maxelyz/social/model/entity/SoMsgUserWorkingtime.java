/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.social.model.entity;

import com.maxelyz.core.model.entity.Users;
import com.maxelyz.core.model.entity.UsersTimeAttendance;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ukritj
 */
@Entity
@Table(name = "so_msg_user_workingtime")
@NamedQueries({
    @NamedQuery(name = "SoMsgUserWorkingtime.findAll", query = "SELECT s FROM SoMsgUserWorkingtime s")})
public class SoMsgUserWorkingtime implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Column(name = "working_start")
    @Temporal(TemporalType.TIMESTAMP)
    private Date workingStart;
    @Column(name = "working_end")
    @Temporal(TemporalType.TIMESTAMP)
    private Date workingEnd;
    @Column(name = "duration_milis")
    private Integer durationMilis;
    @JoinColumn(name = "so_message_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SoMessage soMessage;
    @JoinColumn(name = "user_login_id", referencedColumnName = "id")
    @ManyToOne
    private UsersTimeAttendance usersTimeAttendance;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users users;
    @Column(name = "session_id")
    private String sessionId;

    public SoMsgUserWorkingtime() {
    }

    public SoMsgUserWorkingtime(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getWorkingStart() {
        return workingStart;
    }

    public void setWorkingStart(Date workingStart) {
        this.workingStart = workingStart;
    }

    public Date getWorkingEnd() {
        return workingEnd;
    }

    public void setWorkingEnd(Date workingEnd) {
        this.workingEnd = workingEnd;
    }

    public Integer getDurationMilis() {
        return durationMilis;
    }

    public void setDurationMilis(Integer durationMilis) {
        this.durationMilis = durationMilis;
    }

    public SoMessage getSoMessage() {
        return soMessage;
    }

    public void setSoMessage(SoMessage soMessage) {
        this.soMessage = soMessage;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public UsersTimeAttendance getUsersTimeAttendance() {
        return usersTimeAttendance;
    }

    public void setUsersTimeAttendance(UsersTimeAttendance usersTimeAttendance) {
        this.usersTimeAttendance = usersTimeAttendance;
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
        if (!(object instanceof SoMsgUserWorkingtime)) {
            return false;
        }
        SoMsgUserWorkingtime other = (SoMsgUserWorkingtime) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.social.model.entity.SoMsgUserWorkingtime[ id=" + id + " ]";
    }
    
}
