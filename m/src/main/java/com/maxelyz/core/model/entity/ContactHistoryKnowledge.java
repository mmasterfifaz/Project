/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "contact_history_knowledge")
@NamedQueries({
    @NamedQuery(name = "ContactHistoryKnowledge.findAll", query = "SELECT c FROM ContactHistoryKnowledge c"),
    @NamedQuery(name = "ContactHistoryKnowledge.findByContactHistoryId", query = "SELECT c FROM ContactHistoryKnowledge c WHERE c.contactHistoryKnowledgePK.contactHistoryId = :contactHistoryId"),
    @NamedQuery(name = "ContactHistoryKnowledge.findByKnowledgebaseId", query = "SELECT c FROM ContactHistoryKnowledge c WHERE c.contactHistoryKnowledgePK.knowledgebaseId = :knowledgebaseId")})
public class ContactHistoryKnowledge implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ContactHistoryKnowledgePK contactHistoryKnowledgePK;
    @Basic(optional = false)
    @Column(name = "useful")
    private boolean useful;
    @Column(name = "remark")
    private String remark;
    @JoinColumn(name = "knowledgebase_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Knowledgebase knowledgebase;
    @JoinColumn(name = "contact_history_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ContactHistory contactHistory;

    public ContactHistoryKnowledge() {
    }

    public ContactHistoryKnowledge(ContactHistoryKnowledgePK contactHistoryKnowledgePK) {
        this.contactHistoryKnowledgePK = contactHistoryKnowledgePK;
    }

    public ContactHistoryKnowledge(ContactHistoryKnowledgePK contactHistoryKnowledgePK, boolean useful) {
        this.contactHistoryKnowledgePK = contactHistoryKnowledgePK;
        this.useful = useful;
    }

    public ContactHistoryKnowledge(int contactHistoryId, int knowledgebaseId) {
        this.contactHistoryKnowledgePK = new ContactHistoryKnowledgePK(contactHistoryId, knowledgebaseId);
    }

    public ContactHistoryKnowledgePK getContactHistoryKnowledgePK() {
        return contactHistoryKnowledgePK;
    }

    public void setContactHistoryKnowledgePK(ContactHistoryKnowledgePK contactHistoryKnowledgePK) {
        this.contactHistoryKnowledgePK = contactHistoryKnowledgePK;
    }

    public boolean getUseful() {
        return useful;
    }

    public void setUseful(boolean useful) {
        this.useful = useful;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Knowledgebase getKnowledgebase() {
        return knowledgebase;
    }

    public void setKnowledgebase(Knowledgebase knowledgebase) {
        this.knowledgebase = knowledgebase;
    }

    public ContactHistory getContactHistory() {
        return contactHistory;
    }

    public void setContactHistory(ContactHistory contactHistory) {
        this.contactHistory = contactHistory;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (contactHistoryKnowledgePK != null ? contactHistoryKnowledgePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContactHistoryKnowledge)) {
            return false;
        }
        ContactHistoryKnowledge other = (ContactHistoryKnowledge) object;
        if ((this.contactHistoryKnowledgePK == null && other.contactHistoryKnowledgePK != null) || (this.contactHistoryKnowledgePK != null && !this.contactHistoryKnowledgePK.equals(other.contactHistoryKnowledgePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.ContactHistoryKnowledge[contactHistoryKnowledgePK=" + contactHistoryKnowledgePK + "]";
    }

}
