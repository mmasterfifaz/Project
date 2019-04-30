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
public class KnowledgebaseRelatedPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "knowledgebase_id")
    private int knowledgebaseId;
    @Basic(optional = false)
    @Column(name = "knowledgebase_version")
    private String knowledgebaseVersion;
    @Basic(optional = false)
    @Column(name = "relate_knowledgebase_id")
    private int relateKnowledgebaseId;

    public KnowledgebaseRelatedPK() {
    }

    public KnowledgebaseRelatedPK(int knowledgebaseId, String knowledgebaseVersion, int relateKnowledgebaseId) {
        this.knowledgebaseId = knowledgebaseId;
        this.knowledgebaseVersion = knowledgebaseVersion;
        this.relateKnowledgebaseId = relateKnowledgebaseId;
    }

    public int getKnowledgebaseId() {
        return knowledgebaseId;
    }

    public void setKnowledgebaseId(int knowledgebaseId) {
        this.knowledgebaseId = knowledgebaseId;
    }

    public String getKnowledgebaseVersion() {
        return knowledgebaseVersion;
    }

    public void setKnowledgebaseVersion(String knowledgebaseVersion) {
        this.knowledgebaseVersion = knowledgebaseVersion;
    }

    public int getRelateKnowledgebaseId() {
        return relateKnowledgebaseId;
    }

    public void setRelateKnowledgebaseId(int relateKnowledgebaseId) {
        this.relateKnowledgebaseId = relateKnowledgebaseId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) knowledgebaseId;
        hash += (knowledgebaseVersion != null ? knowledgebaseVersion.hashCode() : 0);
        hash += (int) relateKnowledgebaseId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KnowledgebaseRelatedPK)) {
            return false;
        }
        KnowledgebaseRelatedPK other = (KnowledgebaseRelatedPK) object;
        if (this.knowledgebaseId != other.knowledgebaseId) {
            return false;
        }
        if ((this.knowledgebaseVersion == null && other.knowledgebaseVersion != null) || (this.knowledgebaseVersion != null && !this.knowledgebaseVersion.equals(other.knowledgebaseVersion))) {
            return false;
        }
        if (this.relateKnowledgebaseId != other.relateKnowledgebaseId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.KnowledgebaseRelatedPK[ knowledgebaseId=" + knowledgebaseId + ", knowledgebaseVersion=" + knowledgebaseVersion + ", relateKnowledgebaseId=" + relateKnowledgebaseId + " ]";
    }
    
}
