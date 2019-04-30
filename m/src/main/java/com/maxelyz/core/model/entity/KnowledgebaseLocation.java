/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author oat
 */
@Entity
@Table(name = "knowledgebase_location")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KnowledgebaseLocation.findAll", query = "SELECT k FROM KnowledgebaseLocation k")})
public class KnowledgebaseLocation implements Serializable {
    @JoinColumn(name = "service_type_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ServiceType serviceType;
    @JoinColumn(name = "business_unit_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private BusinessUnit businessUnit;
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected KnowledgebaseLocationPK knowledgebaseLocationPK;
    @Column(name = "remark", length = 1000)
    private String remark;
    @JoinColumn(name = "location_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Location location;
    @JoinColumn(name = "knowledgebase_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Knowledgebase knowledgebase;
    

    public KnowledgebaseLocation() {
    }

    public KnowledgebaseLocation(KnowledgebaseLocationPK knowledgebaseLocationPK) {
        this.knowledgebaseLocationPK = knowledgebaseLocationPK;
    }

    public KnowledgebaseLocation(int knowledgebaseId, int serviceTypeId, int businessUnitId, int locationId) {
        this.knowledgebaseLocationPK = new KnowledgebaseLocationPK(knowledgebaseId, serviceTypeId, businessUnitId, locationId);
    }

    public KnowledgebaseLocationPK getKnowledgebaseLocationPK() {
        return knowledgebaseLocationPK;
    }

    public void setKnowledgebaseLocationPK(KnowledgebaseLocationPK knowledgebaseLocationPK) {
        this.knowledgebaseLocationPK = knowledgebaseLocationPK;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Knowledgebase getKnowledgebase() {
        return knowledgebase;
    }

    public void setKnowledgebase(Knowledgebase knowledgebase) {
        this.knowledgebase = knowledgebase;
    }

    public BusinessUnit getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (knowledgebaseLocationPK != null ? knowledgebaseLocationPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KnowledgebaseLocation)) {
            return false;
        }
        KnowledgebaseLocation other = (KnowledgebaseLocation) object;
        if ((this.knowledgebaseLocationPK == null && other.knowledgebaseLocationPK != null) || (this.knowledgebaseLocationPK != null && !this.knowledgebaseLocationPK.equals(other.knowledgebaseLocationPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.KnowledgebaseLocation[ knowledgebaseLocationPK=" + knowledgebaseLocationPK + " ]";
    }
    
}
