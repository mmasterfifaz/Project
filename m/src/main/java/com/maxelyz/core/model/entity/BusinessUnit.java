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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author oat
 */
@Entity
@Table(name = "business_unit")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BusinessUnit.findAll", query = "SELECT b FROM BusinessUnit b")})
public class BusinessUnit implements Serializable {
    @Column(name =     "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name =     "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "businessUnit")
    private Collection<KnowledgebaseLocation> knowledgebaseLocationCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "businessUnit")
    private Collection<UserGroupLocation> userGroupLocationCollection;
    @JoinTable(name = "service_type_business_unit", joinColumns = {
        @JoinColumn(name = "business_unit_id", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "service_type_id", referencedColumnName = "id", nullable = false)})
    @ManyToMany
    private Collection<ServiceType> serviceTypeCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Column(name = "create_by", length = 100)
    private String createBy;
    @Column(name = "update_by", length = 100)
    private String updateBy;
    @Basic(optional = false)
    @Column(name = "enable", nullable = false)
    private boolean enable;
    @Column(name = "status")
    private Boolean status;
    @OneToMany(mappedBy = "businessUnit")
    private Collection<Location> locationCollection;

    public BusinessUnit() {
    }

    public BusinessUnit(Integer id) {
        this.id = id;
    }

    public BusinessUnit(Integer id, String name) {
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
        if (!(object instanceof BusinessUnit)) {
            return false;
        }
        BusinessUnit other = (BusinessUnit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.BusinessUnit[ id=" + id + " ]";
    }

    public boolean getEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @XmlTransient
    public Collection<Location> getLocationCollection() {
        return locationCollection;
    }

    public void setLocationCollection(Collection<Location> locationCollection) {
        this.locationCollection = locationCollection;
    }

    @XmlTransient
    public Collection<ServiceType> getServiceTypeCollection() {
        return serviceTypeCollection;
    }

    public void setServiceTypeCollection(Collection<ServiceType> serviceTypeCollection) {
        this.serviceTypeCollection = serviceTypeCollection;
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
