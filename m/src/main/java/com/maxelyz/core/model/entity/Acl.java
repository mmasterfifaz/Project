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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "acl")
@NamedQueries({
    @NamedQuery(name = "Acl.findAll", query = "SELECT a FROM Acl a")})
public class Acl implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "code", nullable = false, length = 50)
    private String code;
    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Column(name = "description", length = 1000)
    private String description;
    @Column(name = "custom")
    private Boolean custom;
    @Column(name = "seq_no")
    private Integer seqNo;
    @OneToMany(mappedBy = "parentCode")
    private Collection<Acl> aclCollection;
    @JoinColumn(name = "parent_code", referencedColumnName = "code")
    @ManyToOne
    private Acl parentCode;

    transient private Boolean selected;

    public Acl() {
    }

    public Acl(String code) {
        this.code = code;
    }

    public Acl(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getCustom() {
        return custom;
    }

    public void setCustom(Boolean custom) {
        this.custom = custom;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public Collection<Acl> getAclCollection() {
        return aclCollection;
    }

    public void setAclCollection(Collection<Acl> aclCollection) {
        this.aclCollection = aclCollection;
    }

    public Acl getParentCode() {
        return parentCode;
    }

    public void setParentCode(Acl parentCode) {
        this.parentCode = parentCode;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (code != null ? code.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Acl)) {
            return false;
        }
        Acl other = (Acl) object;
        if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.Acl[code=" + code + "]";
    }

}
