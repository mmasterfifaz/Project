/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "contact_result_group")
@NamedQueries({
    @NamedQuery(name = "ContactResultGroup.findAll", query = "SELECT c FROM ContactResultGroup c")})
public class ContactResultGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Size(max = 50)
    @Column(name = "code")
    private String code;
    @Size(max = 500)
    @Column(name = "name")
    private String name;
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    @ManyToOne
    private ContactResultGroup parentContactResultGroup;
    @Basic(optional = false)
    @Column(name = "seq_no")
    private Integer seqNo;
    @Basic(optional = false)
    @Column(name = "enable")
    private Boolean enable;

    public ContactResultGroup() {
    }

    public ContactResultGroup(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
    
    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public ContactResultGroup getParentContactResultGroup() {
        return parentContactResultGroup;
    }

    public void setParentContactResultGroup(ContactResultGroup parentContactResultGroup) {
        this.parentContactResultGroup = parentContactResultGroup;
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
        if (!(object instanceof ContactResultGroup)) {
            return false;
        }
        ContactResultGroup other = (ContactResultGroup) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.ContactResultGroup[ id=" + id + " ]";
    }
    
}
