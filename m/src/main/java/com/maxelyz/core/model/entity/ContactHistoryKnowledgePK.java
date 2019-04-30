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
 * @author Oat
 */
@Embeddable
public class ContactHistoryKnowledgePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "contact_history_id")
    private int contactHistoryId;
    @Basic(optional = false)
    @Column(name = "knowledgebase_id")
    private int knowledgebaseId;

    public ContactHistoryKnowledgePK() {
    }

    public ContactHistoryKnowledgePK(int contactHistoryId, int knowledgebaseId) {
        this.contactHistoryId = contactHistoryId;
        this.knowledgebaseId = knowledgebaseId;
    }

    public int getContactHistoryId() {
        return contactHistoryId;
    }

    public void setContactHistoryId(int contactHistoryId) {
        this.contactHistoryId = contactHistoryId;
    }

    public int getKnowledgebaseId() {
        return knowledgebaseId;
    }

    public void setKnowledgebaseId(int knowledgebaseId) {
        this.knowledgebaseId = knowledgebaseId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) contactHistoryId;
        hash += (int) knowledgebaseId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContactHistoryKnowledgePK)) {
            return false;
        }
        ContactHistoryKnowledgePK other = (ContactHistoryKnowledgePK) object;
        if (this.contactHistoryId != other.contactHistoryId) {
            return false;
        }
        if (this.knowledgebaseId != other.knowledgebaseId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.ContactHistoryKnowledgePK[contactHistoryId=" + contactHistoryId + ", knowledgebaseId=" + knowledgebaseId + "]";
    }

}
