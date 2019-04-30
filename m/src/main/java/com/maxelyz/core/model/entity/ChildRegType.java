/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Songwisit
 */
@Entity
@Table(name = "child_reg_type")
@NamedQueries({@NamedQuery(name = "ChildRegType.findAll", query = "SELECT c FROM ChildRegType c")})
public class ChildRegType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "rules")
    private String rules;
    @Column(name = "seq_no")
    private Integer seqNo;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "create_by")
    private String createBy;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "update_by")
    private String updateBy;
    @Column(name = "enable")
    private Boolean enable;
   
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "childRegType")
    private Collection<ProductChildRegType> productChildRegTypeCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "childRegType")
    private Collection<ChildRegForm> childRegFormCollection;

    public ChildRegType() {
    }

    public ChildRegType(Integer id) {
        this.id = id;
    }

    public ChildRegType(Integer id, String name) {
        this.id = id;
        this.name = name;
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

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }
    
    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }
    
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    
    public Collection<ProductChildRegType> getProductChildRegTypeCollection() {
        return productChildRegTypeCollection;
    }

    public void setProductChildRegTypeCollection(Collection<ProductChildRegType> productChildRegTypeCollection) {
        this.productChildRegTypeCollection = productChildRegTypeCollection;
    }

    public Collection<ChildRegForm> getChildRegFormCollection() {
        return childRegFormCollection;
    }

    public void setChildRegFormCollection(Collection<ChildRegForm> childRegFormCollection) {
        this.childRegFormCollection = childRegFormCollection;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ChildRegType)) {
            return false;
        }
        ChildRegType other = (ChildRegType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.ChildRegType[id=" + id + "]";
    }

}