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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author oat
 */
@Entity
@Table(name = "yes_log")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "YesLog.findAll", query = "SELECT y FROM YesLog y")})
public class YesLog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "yes_filename", length = 255)
    private String yesFilename;
    @Column(name = "voice_filename", length = 255)
    private String voiceFilename;
    @Column(name = "app_filename", length = 255)
    private String appFilename;

    @Column(name = "yes_filepath", length = 255)
    private String yesFilepath;
    @Column(name = "voice_filepath", length = 255)
    private String voiceFilepath;
    @Column(name = "app_filepath", length = 255)
    private String appFilepath;

    @Column(name = "result", length = 4000)
    private String result;
    @Basic(optional = false)
    @Column(name = "create_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Basic(optional = false)
    @Column(name = "create_by", nullable = false, length = 100)
    private String createBy;
    @Column(name = "file_type")
    private Integer fileType;

    public YesLog() {
    }

    public YesLog(Integer id) {
        this.id = id;
    }

    public YesLog(Integer id, Date createDate, String createBy) {
        this.id = id;
        this.createDate = createDate;
        this.createBy = createBy;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getYesFilename() {
        return yesFilename;
    }

    public void setYesFilename(String yesFilename) {
        this.yesFilename = yesFilename;
    }

    public String getVoiceFilename() {
        return voiceFilename;
    }

    public void setVoiceFilename(String voiceFilename) {
        this.voiceFilename = voiceFilename;
    }

    public String getAppFilename() {
        return appFilename;
    }

    public void setAppFilename(String appFilename) {
        this.appFilename = appFilename;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getYesFilepath() {
        return yesFilepath;
    }

    public void setYesFilepath(String yesFilepath) {
        this.yesFilepath = yesFilepath;
    }

    public String getVoiceFilepath() {
        return voiceFilepath;
    }

    public void setVoiceFilepath(String voiceFilepath) {
        this.voiceFilepath = voiceFilepath;
    }

    public String getAppFilepath() {
        return appFilepath;
    }

    public void setAppFilepath(String appFilepath) {
        this.appFilepath = appFilepath;
    }

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
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
        if (!(object instanceof YesLog)) {
            return false;
        }
        YesLog other = (YesLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.YesLog[ id=" + id + " ]";
    }
}
