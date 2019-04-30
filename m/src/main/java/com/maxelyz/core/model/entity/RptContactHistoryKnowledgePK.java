/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Oat
 */
@Embeddable
public class RptContactHistoryKnowledgePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "contact_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date contactDate;
    @Basic(optional = false)
    @Column(name = "knowledgebase_id")
    private int knowledgebaseId;

    public RptContactHistoryKnowledgePK() {
    }

    public RptContactHistoryKnowledgePK(Date contactDate, int knowledgebaseId) {
        this.contactDate = contactDate;
        this.knowledgebaseId = knowledgebaseId;
    }

    public Date getContactDate() {
        return contactDate;
    }

    public void setContactDate(Date contactDate) {
        this.contactDate = contactDate;
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
        hash += (contactDate != null ? contactDate.hashCode() : 0);
        hash += (int) knowledgebaseId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RptContactHistoryKnowledgePK)) {
            return false;
        }
        RptContactHistoryKnowledgePK other = (RptContactHistoryKnowledgePK) object;
        if ((this.contactDate == null && other.contactDate != null) || (this.contactDate != null && !this.contactDate.equals(other.contactDate))) {
            return false;
        }
        if (this.knowledgebaseId != other.knowledgebaseId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.RptContactHistoryKnowledgePK[contactDate=" + contactDate + ", knowledgebaseId=" + knowledgebaseId + "]";
    }

}
