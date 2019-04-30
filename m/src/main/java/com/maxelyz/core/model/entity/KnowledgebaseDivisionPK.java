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
 * @author test01
 */
@Embeddable
public class KnowledgebaseDivisionPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "knowledgebase_id")
    private int knowledgebaseId;
    @Basic(optional = false)
    @Column(name = "version")
    private String version;

    public KnowledgebaseDivisionPK() {
    }

    public KnowledgebaseDivisionPK(int knowledgebaseId, String version) {
        this.knowledgebaseId = knowledgebaseId;
        this.version = version;
    }

    public int getKnowledgebaseId() {
        return knowledgebaseId;
    }

    public void setKnowledgebaseId(int knowledgebaseId) {
        this.knowledgebaseId = knowledgebaseId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) knowledgebaseId;
        hash += (version != null ? version.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KnowledgebaseDivisionPK)) {
            return false;
        }
        KnowledgebaseDivisionPK other = (KnowledgebaseDivisionPK) object;
        if (this.knowledgebaseId != other.knowledgebaseId) {
            return false;
        }
        if ((this.version == null && other.version != null) || (this.version != null && !this.version.equals(other.version))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.KnowledgebaseDivisionPK[ knowledgebaseId=" + knowledgebaseId + ", version=" + version + " ]";
    }
    
}
