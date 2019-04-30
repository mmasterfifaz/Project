/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "cti_adapter_export_file")
@NamedQueries({
    @NamedQuery(name = "CtiAdapterExportFile.findAll", query = "SELECT c FROM CtiAdapterExportFile c")})
public class CtiAdapterExportFile implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "cti_adapter_id", referencedColumnName = "id")
    @ManyToOne
    private CtiAdapter ctiAdapter;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "status")
    private String status;

    public CtiAdapterExportFile() {
    }

    public CtiAdapterExportFile(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CtiAdapter getCtiAdapter() {
        return ctiAdapter;
    }

    public void setCtiAdapter(CtiAdapter ctiAdapter) {
        this.ctiAdapter = ctiAdapter;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
        if (!(object instanceof CtiAdapterExportFile)) {
            return false;
        }
        CtiAdapterExportFile other = (CtiAdapterExportFile) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.CtiAdapterExportFile[ id=" + id + " ]";
    }
    
}
