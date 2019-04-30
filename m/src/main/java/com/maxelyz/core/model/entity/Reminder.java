/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "reminder")
@NamedQueries({
    @NamedQuery(name = "Reminder.findAll", query = "SELECT r FROM Reminder r")})
public class Reminder implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "reminder_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reminderDate;
    @Basic(optional = false)
    @Column(name = "all_day")
    private boolean allDay;
    @Basic(optional = false)
    @Column(name = "module_name")
    private String moduleName;
    @Basic(optional = false)
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "dismiss")
    private boolean dismiss;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users userId;

    public Reminder() {
    }

    public Reminder(Integer id) {
        this.id = id;
    }

    public Reminder(Integer id, Date reminderDate, boolean allDay, String moduleName, String title, boolean dismiss) {
        this.id = id;
        this.reminderDate = reminderDate;
        this.allDay = allDay;
        this.moduleName = moduleName;
        this.title = title;
        this.dismiss = dismiss;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(Date reminderDate) {
        this.reminderDate = reminderDate;
    }

    public boolean getAllDay() {
        return allDay;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getDismiss() {
        return dismiss;
    }

    public void setDismiss(boolean dismiss) {
        this.dismiss = dismiss;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
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
        if (!(object instanceof Reminder)) {
            return false;
        }
        Reminder other = (Reminder) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.Reminder[ id=" + id + " ]";
    }
    
}
