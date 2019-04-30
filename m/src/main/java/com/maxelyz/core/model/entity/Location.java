/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "location")
public class Location implements Serializable {
    @Column(name =     "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "location")
    private Collection<KnowledgebaseLocation> knowledgebaseLocationCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "location")
    private Collection<UserGroupLocation> userGroupLocationCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "create_by")
    private String createBy;
    @Column(name = "update_by")
    private String updateBy;
    @Basic(optional = false)
    @Column(name = "enable", nullable = false)
    private boolean enable;
    @Column(name = "status")
    private Boolean status;
    @JoinColumn(name = "business_unit_id", referencedColumnName = "id")
    @ManyToOne
    private BusinessUnit businessUnit;

    public Location() {
    }

    public Location(Integer id) {
        this.id = id;
    }

    public Location(Integer id, String name, Boolean enable) {
        this.id = id;
        this.name = name;
        this.enable = enable;
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

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    
    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
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
        if (!(object instanceof Location)) {
            return false;
        }
        Location other = (Location) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.Location[id=" + id + "]";
    }

    public boolean getEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public BusinessUnit getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
    }

    @XmlTransient
    public Collection<UserGroupLocation> getUserGroupLocationCollection() {
        return userGroupLocationCollection;
    }

    public void setUserGroupLocationCollection(Collection<UserGroupLocation> userGroupLocationCollection) {
        this.userGroupLocationCollection = userGroupLocationCollection;
    }

    @XmlTransient
    public Collection<KnowledgebaseLocation> getKnowledgebaseLocationCollection() {
        return knowledgebaseLocationCollection;
    }

    public void setKnowledgebaseLocationCollection(Collection<KnowledgebaseLocation> knowledgebaseLocationCollection) {
        this.knowledgebaseLocationCollection = knowledgebaseLocationCollection;
    }


}
