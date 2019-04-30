/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author test01
 */
@Entity
@Table(name = "knowledgebase_related")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KnowledgebaseRelated.findAll", query = "SELECT k FROM KnowledgebaseRelated k")})
public class KnowledgebaseRelated implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected KnowledgebaseRelatedPK knowledgebaseRelatedPK;

    public KnowledgebaseRelated() {
    }

    public KnowledgebaseRelated(KnowledgebaseRelatedPK knowledgebaseRelatedPK) {
        this.knowledgebaseRelatedPK = knowledgebaseRelatedPK;
    }

    public KnowledgebaseRelated(int knowledgebaseId, String knowledgebaseVersion, int relateKnowledgebaseId) {
        this.knowledgebaseRelatedPK = new KnowledgebaseRelatedPK(knowledgebaseId, knowledgebaseVersion, relateKnowledgebaseId);
    }

    public KnowledgebaseRelatedPK getKnowledgebaseRelatedPK() {
        return knowledgebaseRelatedPK;
    }

    public void setKnowledgebaseRelatedPK(KnowledgebaseRelatedPK knowledgebaseRelatedPK) {
        this.knowledgebaseRelatedPK = knowledgebaseRelatedPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (knowledgebaseRelatedPK != null ? knowledgebaseRelatedPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KnowledgebaseRelated)) {
            return false;
        }
        KnowledgebaseRelated other = (KnowledgebaseRelated) object;
        if ((this.knowledgebaseRelatedPK == null && other.knowledgebaseRelatedPK != null) || (this.knowledgebaseRelatedPK != null && !this.knowledgebaseRelatedPK.equals(other.knowledgebaseRelatedPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.KnowledgebaseRelated[ knowledgebaseRelatedPK=" + knowledgebaseRelatedPK + " ]";
    }
    
}
