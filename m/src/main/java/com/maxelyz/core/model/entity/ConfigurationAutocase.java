/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "configuration_autocase")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConfigurationAutocase.findAll", query = "SELECT c FROM ConfigurationAutocase c")})
public class ConfigurationAutocase implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "case_request_id", nullable = false)
    private int caseRequestId;
    @Basic(optional = false)
    @Column(name = "servicetype_id", nullable = false)
    private int servicetypeId;
    @Basic(optional = false)
    @Column(name = "location_id", nullable = false)
    private int locationId;

    public ConfigurationAutocase() {
    }

    public ConfigurationAutocase(Integer id) {
        this.id = id;
    }

    public ConfigurationAutocase(Integer id, int caseRequestId, int locationId) {
        this.id = id;
        this.caseRequestId = caseRequestId;
        this.locationId = locationId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCaseRequestId() {
        return caseRequestId;
    }

    public void setCaseRequestId(int caseRequestId) {
        this.caseRequestId = caseRequestId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getServicetypeId() {
        return servicetypeId;
    }

    public void setServicetypeId(int servicetypeId) {
        this.servicetypeId = servicetypeId;
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
        if (!(object instanceof ConfigurationAutocase)) {
            return false;
        }
        ConfigurationAutocase other = (ConfigurationAutocase) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.ConfigurationAutocase[ id=" + id + " ]";
    }
    
}
