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

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "cti_adapter")
@NamedQueries({
    @NamedQuery(name = "CtiAdapter.findAll", query = "SELECT c FROM CtiAdapter c")})
public class CtiAdapter implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "cti_server_url")
    private String ctiServerUrl;
    @Column(name = "web_toolbar_url")
    private String webToolbarUrl;
    @Column(name = "cti_vendor_code")
    private String ctiVendorCode;
    @Column(name = "recorder_vendor_code")
    private String recorderVendorCode;
    @Column(name = "recorder_thread_sleep")
    private Integer recorderThreadSleep;
    @Column(name = "recorder_param")
    private String recorderParam;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "create_by")
    private String createBy;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "update_by")
    private String updateBy;
    @Column(name = "enable")
    private Boolean enable;
    @Column(name = "status")
    private Boolean status;

    public CtiAdapter() {
    }

    public CtiAdapter(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCtiServerUrl() {
        return ctiServerUrl;
    }

    public void setCtiServerUrl(String ctiServerUrl) {
        this.ctiServerUrl = ctiServerUrl;
    }

    public String getWebToolbarUrl() {
        return webToolbarUrl;
    }

    public void setWebToolbarUrl(String webToolbarUrl) {
        this.webToolbarUrl = webToolbarUrl;
    }

    public String getCtiVendorCode() {
        return ctiVendorCode;
    }

    public void setCtiVendorCode(String ctiVendorCode) {
        this.ctiVendorCode = ctiVendorCode;
    }

    public String getRecorderVendorCode() {
        return recorderVendorCode;
    }

    public void setRecorderVendorCode(String recorderVendorCode) {
        this.recorderVendorCode = recorderVendorCode;
    }

    public Integer getRecorderThreadSleep() {
        return recorderThreadSleep;
    }

    public void setRecorderThreadSleep(Integer recorderThreadSleep) {
        this.recorderThreadSleep = recorderThreadSleep;
    }

    public String getRecorderParam() {
        return recorderParam;
    }

    public void setRecorderParam(String recorderParam) {
        this.recorderParam = recorderParam;
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

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
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
        if (!(object instanceof CtiAdapter)) {
            return false;
        }
        CtiAdapter other = (CtiAdapter) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.CtiAdapter[ id=" + id + " ]";
    }
    
}
