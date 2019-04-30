/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.social.model.entity;

import com.maxelyz.core.model.entity.Users;
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
import javax.validation.constraints.Size;

/**
 *
 * @author ukritj
 */
@Entity
@Table(name = "so_msg_user_assigntime")
@NamedQueries({
    @NamedQuery(name = "SoMsgUserAssigntime.findAll", query = "SELECT s FROM SoMsgUserAssigntime s")})
public class SoMsgUserAssigntime implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Column(name = "so_message_id")
    private Integer soMessageId;
    @Column(name = "assignee_id")
    private Integer assigneeId;
    @Size(max = 50)
    @Column(name = "type")
    private String type;
    @Column(name = "logdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date logdate;
    @Column(name = "create_by")
    private Integer createBy;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "update_by")
    private Integer updateBy;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "turnaround_start")
    @Temporal(TemporalType.TIMESTAMP)
    private Date turnaroundStart;
    @Column(name = "turnaround_end")
    @Temporal(TemporalType.TIMESTAMP)
    private Date turnaroundEnd;
    @Column(name = "duration_milis")
    private Integer durationMilis;
    @JoinColumn(name = "prev_assignee_id", referencedColumnName = "id")
    @ManyToOne
    private Users previousUsers;

    public SoMsgUserAssigntime() {
    }

    public SoMsgUserAssigntime(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSoMessageId() {
        return soMessageId;
    }

    public void setSoMessageId(Integer soMessageId) {
        this.soMessageId = soMessageId;
    }

    public Integer getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Integer assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getLogdate() {
        return logdate;
    }

    public void setLogdate(Date logdate) {
        this.logdate = logdate;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getTurnaroundStart() {
        return turnaroundStart;
    }

    public void setTurnaroundStart(Date turnaroundStart) {
        this.turnaroundStart = turnaroundStart;
    }

    public Date getTurnaroundEnd() {
        return turnaroundEnd;
    }

    public void setTurnaroundEnd(Date turnaroundEnd) {
        this.turnaroundEnd = turnaroundEnd;
    }

    public Integer getDurationMilis() {
        return durationMilis;
    }

    public void setDurationMilis(Integer durationMilis) {
        this.durationMilis = durationMilis;
    }

    public Users getPreviousUsers() {
        return previousUsers;
    }

    public void setPreviousUsers(Users previousUsers) {
        this.previousUsers = previousUsers;
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
        if (!(object instanceof SoMsgUserAssigntime)) {
            return false;
        }
        SoMsgUserAssigntime other = (SoMsgUserAssigntime) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.social.model.entity.SoMsgUserAssigntime[ id=" + id + " ]";
    }
    
}
