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
@Table(name = "scl_post_schedule")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SclPostSchedule.findAll", query = "SELECT s FROM SclPostSchedule s"),
    @NamedQuery(name = "SclPostSchedule.findById", query = "SELECT s FROM SclPostSchedule s WHERE s.id = :id"),
    @NamedQuery(name = "SclPostSchedule.findByMessage", query = "SELECT s FROM SclPostSchedule s WHERE s.message = :message"),
    @NamedQuery(name = "SclPostSchedule.findByChannel", query = "SELECT s FROM SclPostSchedule s WHERE s.channel = :channel"),
    @NamedQuery(name = "SclPostSchedule.findByScheduleDate", query = "SELECT s FROM SclPostSchedule s WHERE s.scheduleDate = :scheduleDate"),
    @NamedQuery(name = "SclPostSchedule.findByScheduleTime", query = "SELECT s FROM SclPostSchedule s WHERE s.scheduleTime = :scheduleTime"),
    @NamedQuery(name = "SclPostSchedule.findByCreateDate", query = "SELECT s FROM SclPostSchedule s WHERE s.createDate = :createDate"),
    @NamedQuery(name = "SclPostSchedule.findByCreateBy", query = "SELECT s FROM SclPostSchedule s WHERE s.createBy = :createBy"),
    @NamedQuery(name = "SclPostSchedule.findByUpdateDate", query = "SELECT s FROM SclPostSchedule s WHERE s.updateDate = :updateDate"),
    @NamedQuery(name = "SclPostSchedule.findByUpdateBy", query = "SELECT s FROM SclPostSchedule s WHERE s.updateBy = :updateBy")})
public class SclPostSchedule implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "message")
    private String message;
    @Column(name = "channel")
    private String channel;
    @Column(name = "schedule_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date scheduleDate;
    @Column(name = "schedule_time")
    private String scheduleTime;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "create_by")
    private Integer createBy;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "update_by")
    private Integer updateBy;

    public SclPostSchedule() {
    }

    public SclPostSchedule(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
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
        if (!(object instanceof SclPostSchedule)) {
            return false;
        }
        SclPostSchedule other = (SclPostSchedule) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.SclPostSchedule[ id=" + id + " ]";
    }
    
}
