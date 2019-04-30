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
import javax.persistence.Lob;
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
@Table(name = "so_activity_log")
@NamedQueries({
    @NamedQuery(name = "SoActivityLog.findAll", query = "SELECT s FROM SoActivityLog s")})
public class SoActivityLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Column(name = "log_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date logDate;
    @Size(max = 50)
    @Column(name = "log_type")
    private String logType;
    @Lob
    @Column(name = "remark")
    private String remark;
    @Lob
    @Column(name = "reason")
    private String reason;
    @JoinColumn(name = "create_by", referencedColumnName = "id")
    @ManyToOne
    private Users createBy;
    @JoinColumn(name = "so_message_id", referencedColumnName = "id")
    @ManyToOne
    private SoMessage soMessage;
    @JoinColumn(name = "log_type_id", referencedColumnName = "id")
    @ManyToOne
    private SoActivityType soActivityType;

    public SoActivityLog() {
    }

    public SoActivityLog(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Users getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Users createBy) {
        this.createBy = createBy;
    }

    public SoMessage getSoMessage() {
        return soMessage;
    }

    public void setSoMessage(SoMessage soMessage) {
        this.soMessage = soMessage;
    }

    public SoActivityType getSoActivityType() {
        return soActivityType;
    }

    public void setSoActivityType(SoActivityType soActivityType) {
        this.soActivityType = soActivityType;
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
        if (!(object instanceof SoActivityLog)) {
            return false;
        }
        SoActivityLog other = (SoActivityLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.social.model.entity.SoActivityLog[ id=" + id + " ]";
    }
}
