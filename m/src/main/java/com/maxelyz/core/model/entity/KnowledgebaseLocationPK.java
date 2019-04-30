/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author oat
 */
@Embeddable
public class KnowledgebaseLocationPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "knowledgebase_id", nullable = false)
    private int knowledgebaseId;
    @Basic(optional = false)
    @Column(name = "service_type_id", nullable = false)
    private int serviceTypeId;
    @Basic(optional = false)
    @Column(name = "business_unit_id", nullable = false)
    private int businessUnitId;
    @Basic(optional = false)
    @Column(name = "location_id", nullable = false)
    private int locationId;

    public KnowledgebaseLocationPK() {
    }

    public KnowledgebaseLocationPK(int knowledgebaseId, int serviceTypeId, int businessUnitId, int locationId) {
        this.knowledgebaseId = knowledgebaseId;
        this.serviceTypeId = serviceTypeId;
        this.businessUnitId = businessUnitId;
        this.locationId = locationId;
    }

    public int getKnowledgebaseId() {
        return knowledgebaseId;
    }

    public void setKnowledgebaseId(int knowledgebaseId) {
        this.knowledgebaseId = knowledgebaseId;
    }

    public int getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(int serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public int getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(int businessUnitId) {
        this.businessUnitId = businessUnitId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) knowledgebaseId;
        hash += (int) locationId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KnowledgebaseLocationPK)) {
            return false;
        }
        KnowledgebaseLocationPK other = (KnowledgebaseLocationPK) object;
        if (this.knowledgebaseId != other.knowledgebaseId) {
            return false;
        }
        if (this.locationId != other.locationId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.KnowledgebaseLocationPK[ knowledgebaseId=" + knowledgebaseId + ", locationId=" + locationId + " ]";
    }
    
}
