/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.social.model.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ukritj
 */
@Entity
@Table(name = "so_service_keyword")
@NamedQueries({
    @NamedQuery(name = "SoServiceKeyword.findAll", query = "SELECT s FROM SoServiceKeyword s")})
public class SoServiceKeyword implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "keyword")
    private String keyword;
    @Basic(optional = false)
    @NotNull
    @Column(name = "match_all")
    private boolean matchAll;
    @Basic(optional = false)
    @NotNull
    @Column(name = "order_id")
    private int orderId;
    @JoinColumn(name = "so_service_id", referencedColumnName = "id", insertable = true, updatable = true)
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    private SoService soService;

    public SoServiceKeyword() {
    }

    public SoServiceKeyword(Integer id) {
        this.id = id;
    }

    public SoServiceKeyword(Integer id, String keyword, boolean matchAll, int orderId) {
        this.id = id;
        this.keyword = keyword;
        this.matchAll = matchAll;
        this.orderId = orderId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public boolean getMatchAll() {
        return matchAll;
    }

    public void setMatchAll(boolean matchAll) {
        this.matchAll = matchAll;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public SoService getSoService() {
        return soService;
    }

    public void setSoService(SoService soService) {
        this.soService = soService;
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
        if (!(object instanceof SoServiceKeyword)) {
            return false;
        }
        SoServiceKeyword other = (SoServiceKeyword) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.social.model.entity.SoServiceKeyword[ id=" + id + " ]";
    }
    
}
