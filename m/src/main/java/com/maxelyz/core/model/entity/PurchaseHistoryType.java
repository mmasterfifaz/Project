/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "purchase_history_type")
@NamedQueries({@NamedQuery(name = "PurchaseHistoryType.findAll", query = "SELECT p FROM PurchaseHistoryType p")})
public class PurchaseHistoryType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "purchaseHistoryTypeId")
    private Collection<PurchaseHistory> purchaseHistoryCollection;

    public PurchaseHistoryType() {
    }

    public PurchaseHistoryType(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<PurchaseHistory> getPurchaseHistoryCollection() {
        return purchaseHistoryCollection;
    }

    public void setPurchaseHistoryCollection(Collection<PurchaseHistory> purchaseHistoryCollection) {
        this.purchaseHistoryCollection = purchaseHistoryCollection;
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
        if (!(object instanceof PurchaseHistoryType)) {
            return false;
        }
        PurchaseHistoryType other = (PurchaseHistoryType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.PurchaseHistoryType[id=" + id + "]";
    }

}
