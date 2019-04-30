/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "phone_directory_telephone")
@NamedQueries({
    @NamedQuery(name = "PhoneDirectoryTelephone.findAll", query = "SELECT p FROM PhoneDirectoryTelephone p")})
public class PhoneDirectoryTelephone implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "phone_no", nullable = false, length = 50)
    private String phoneNo;
    @Column(name = "phone_type", length = 50)
    private String phoneType;
    @Column(name = "call_type", length = 50)
    private String callType;
    @Column(name = "description")
    private String description;
    @JoinColumn(name = "phone_directory_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    //@ManyToOne(fetch = FetchType.EAGER)
    private PhoneDirectory phoneDirectory;

    public PhoneDirectoryTelephone() {
    }

    public PhoneDirectoryTelephone(Integer id) {
        this.id = id;
    }

    public PhoneDirectoryTelephone(Integer id, String phoneNo) {
        this.id = id;
        this.phoneNo = phoneNo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public PhoneDirectory getPhoneDirectory() {
        return phoneDirectory;
    }

    public void setPhoneDirectory(PhoneDirectory phoneDirectory) {
        this.phoneDirectory = phoneDirectory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        if (!(object instanceof PhoneDirectoryTelephone)) {
            return false;
        }
        PhoneDirectoryTelephone other = (PhoneDirectoryTelephone) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.PhoneDirectoryTelephone[ id=" + id + " ]";
    }
    
}
