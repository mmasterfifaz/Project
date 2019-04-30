/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author apichatt
 */
@Entity
@Table(name = "so_account_user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SoAccountUser.findAll", query = "SELECT s FROM SoAccountUser s"),
    @NamedQuery(name = "SoAccountUser.findById", query = "SELECT s FROM SoAccountUser s WHERE s.id = :id"),
    @NamedQuery(name = "SoAccountUser.findByUserId", query = "SELECT s FROM SoAccountUser s WHERE s.userId = :userId"),
    @NamedQuery(name = "SoAccountUser.findByUserName", query = "SELECT s FROM SoAccountUser s WHERE s.userName = :userName")})
public class SoAccountUser implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "user_id")
    private long userId;
    @Column(name = "user_name")
    private String userName;

    @JoinColumn(name = "so_account_id", referencedColumnName = "id")
    @ManyToOne
    private SoAccount soAccount;

    public SoAccountUser() {
    }

    public SoAccountUser(Integer id) {
        this.id = id;
    }

    public SoAccountUser(Integer id, long userId) {
        this.id = id;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public SoAccount getSoAccount() {
        return soAccount;
    }

    public void setSoAccount(SoAccount soAccount) {
        this.soAccount = soAccount;
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
        if (!(object instanceof SoAccountUser)) {
            return false;
        }
        SoAccountUser other = (SoAccountUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.SoAccountUser[ id=" + id + " ]";
    }
    
}
