/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "phone_directory")
@NamedQueries({
    @NamedQuery(name = "PhoneDirectory.findAll", query = "SELECT p FROM PhoneDirectory p")})
public class PhoneDirectory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @JoinColumn(name = "phone_directory_category_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private PhoneDirectoryCategory phoneDirectoryCategory;
    @Column(name = "name", length = 100)
    private String name;
    @Column(name = "surname", length = 100)
    private String surname;
    @Column(name = "email", length = 250)
    private String email;
    @Column(name = "description")
    private String description;
    @Column(name = "status")
    private Boolean status;
    @Column(name = "enable")
    private Boolean enable;
    @Column(name = "create_by", length = 100)
    private String createBy;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "update_by", length = 100)
    private String updateBy;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "phoneDirectory")
    private Collection<PhoneDirectoryTelephone> phoneDirectoryTelephoneCollection;

    public PhoneDirectory() {
    }

    public PhoneDirectory(Integer id) {
        this.id = id;
    }

    public PhoneDirectory(Integer id, PhoneDirectoryCategory phoneDirectoryCategory) {
        this.id = id;
        this.phoneDirectoryCategory = phoneDirectoryCategory;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PhoneDirectoryCategory getPhoneDirectoryCategory() {
        return phoneDirectoryCategory;
    }

    public void setPhoneDirectoryCategory(PhoneDirectoryCategory phoneDirectoryCategory) {
        this.phoneDirectoryCategory = phoneDirectoryCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
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

    public Collection<PhoneDirectoryTelephone> getPhoneDirectoryTelephoneCollection() {
        return phoneDirectoryTelephoneCollection;
    }

    public void setPhoneDirectoryTelephoneCollection(Collection<PhoneDirectoryTelephone> phoneDirectoryTelephoneCollection) {
        this.phoneDirectoryTelephoneCollection = phoneDirectoryTelephoneCollection;
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
        if (!(object instanceof PhoneDirectory)) {
            return false;
        }
        PhoneDirectory other = (PhoneDirectory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.PhoneDirectory[ id=" + id + " ]";
    }
    
}
