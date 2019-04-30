/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Nuttakarn
 */
@Entity
@Table(name = "media_plan_import_log")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MediaPlanImportLog.findAll", query = "SELECT m FROM MediaPlanImportLog m")})
public class MediaPlanImportLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "int")
    private Integer int1;
    @Size(max = 100)
    @Column(name = "file_name")
    private String fileName;
    @Size(max = 50)
    @Column(name = "import_by")
    private String importBy;
    @Column(name = "import_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date importDate;
    @Size(max = 50)
    @Column(name = "status")
    private String status;
    @Column(name = "total_record")
    private Integer totalRecord;
    @Column(name = "success_record")
    private Integer successRecord;
    @Column(name = "reject_record")
    private Integer rejectRecord;

    public MediaPlanImportLog() {
    }

    public MediaPlanImportLog(Integer int1) {
        this.int1 = int1;
    }

    public Integer getInt1() {
        return int1;
    }

    public void setInt1(Integer int1) {
        this.int1 = int1;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getImportBy() {
        return importBy;
    }

    public void setImportBy(String importBy) {
        this.importBy = importBy;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(Integer totalRecord) {
        this.totalRecord = totalRecord;
    }

    public Integer getSuccessRecord() {
        return successRecord;
    }

    public void setSuccessRecord(Integer successRecord) {
        this.successRecord = successRecord;
    }

    public Integer getRejectRecord() {
        return rejectRecord;
    }

    public void setRejectRecord(Integer rejectRecord) {
        this.rejectRecord = rejectRecord;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int1 != null ? int1.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MediaPlanImportLog)) {
            return false;
        }
        MediaPlanImportLog other = (MediaPlanImportLog) object;
        if ((this.int1 == null && other.int1 != null) || (this.int1 != null && !this.int1.equals(other.int1))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.MediaPlanImportLog[ int1=" + int1 + " ]";
    }
    
}
