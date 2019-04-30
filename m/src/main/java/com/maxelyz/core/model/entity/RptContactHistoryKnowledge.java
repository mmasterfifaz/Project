/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Date;
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
@Table(name = "rpt_contact_history_knowledge")
@NamedQueries({
    @NamedQuery(name = "RptContactHistoryKnowledge.findAll", query = "SELECT r FROM RptContactHistoryKnowledge r")})
public class RptContactHistoryKnowledge implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RptContactHistoryKnowledgePK rptContactHistoryKnowledgePK;
    @Basic(optional = false)
    @Column(name = "total")
    private int total;
    @Basic(optional = false)
    @Column(name = "useful_total")
    private int usefulTotal;
    @JoinColumn(name = "knowledgebase_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Knowledgebase knowledgebase;

    public RptContactHistoryKnowledge() {
    }

    public RptContactHistoryKnowledge(RptContactHistoryKnowledgePK rptContactHistoryKnowledgePK) {
        this.rptContactHistoryKnowledgePK = rptContactHistoryKnowledgePK;
    }

    public RptContactHistoryKnowledge(RptContactHistoryKnowledgePK rptContactHistoryKnowledgePK, int total) {
        this.rptContactHistoryKnowledgePK = rptContactHistoryKnowledgePK;
        this.total = total;
    }

    public RptContactHistoryKnowledge(Date contactDate, int knowledgebaseId) {
        this.rptContactHistoryKnowledgePK = new RptContactHistoryKnowledgePK(contactDate, knowledgebaseId);
    }

    public RptContactHistoryKnowledgePK getRptContactHistoryKnowledgePK() {
        return rptContactHistoryKnowledgePK;
    }

    public void setRptContactHistoryKnowledgePK(RptContactHistoryKnowledgePK rptContactHistoryKnowledgePK) {
        this.rptContactHistoryKnowledgePK = rptContactHistoryKnowledgePK;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getUsefulTotal() {
        return usefulTotal;
    }

    public void setUsefulTotal(int usefulTotal) {
        this.usefulTotal = usefulTotal;
    }
   

    public Knowledgebase getKnowledgebase() {
        return knowledgebase;
    }

    public void setKnowledgebase(Knowledgebase knowledgebase) {
        this.knowledgebase = knowledgebase;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rptContactHistoryKnowledgePK != null ? rptContactHistoryKnowledgePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RptContactHistoryKnowledge)) {
            return false;
        }
        RptContactHistoryKnowledge other = (RptContactHistoryKnowledge) object;
        if ((this.rptContactHistoryKnowledgePK == null && other.rptContactHistoryKnowledgePK != null) || (this.rptContactHistoryKnowledgePK != null && !this.rptContactHistoryKnowledgePK.equals(other.rptContactHistoryKnowledgePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.RptContactHistoryKnowledge[rptContactHistoryKnowledgePK=" + rptContactHistoryKnowledgePK + "]";
    }

}
