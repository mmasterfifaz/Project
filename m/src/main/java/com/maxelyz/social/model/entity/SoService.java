/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.social.model.entity;

import com.maxelyz.core.model.entity.Users;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author ukritj
 */
@Entity
@Table(name = "so_service")
@NamedQueries({
    @NamedQuery(name = "SoService.findAll", query = "SELECT s FROM SoService s")})
public class SoService implements Serializable {
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "soService")
    private SoServiceOutgoingAccount soServiceOutgoingAccount;
    @JoinTable(name = "so_service_account", joinColumns = {
        @JoinColumn(name = "so_service_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "so_account_id", referencedColumnName = "id")})
    @ManyToMany(cascade = CascadeType.ALL)
    private Collection<SoAccount> soAccountCollection;
    @JoinTable(name = "so_service_user", joinColumns = {
        @JoinColumn(name = "so_service_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "user_id", referencedColumnName = "id")})
    @ManyToMany(cascade = CascadeType.ALL)
    private Collection<Users> usersCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Column(name = "service_priority")
    private int servicePriority;
    @Size(max = 100)
    @Column(name = "message_priority")
    private String messagePriority;
    @Size(max = 100)
    @Column(name = "sentimental")
    private String sentimental;
    @Size(max = 100)
    @Column(name = "routing_type")
    private String routingType;
    @Column(name = "routing_option")
    private Boolean routingOption;
    @Column(name = "routing_same_user")
    private Boolean routingSameUser;
    @Column(name = "routing_user_group_id")
    private Integer routingUserGroupId;
    @Column(name = "require_approval")
    private Boolean requireApproval;
    @Size(max = 100)
    @Column(name = "create_by")
    private String createBy;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Size(max = 100)
    @Column(name = "update_by")
    private String updateBy;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "enable")
    private Boolean enable;
    @Column(name = "status")
    private Boolean status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "soService", fetch = FetchType.EAGER)
    private Collection<SoServiceKeyword> soServiceKeywordCollection;

    public SoService() {
    }

    public SoService(Integer id) {
        this.id = id;
    }

    public SoService(Integer id, String name, int servicePriority) {
        this.id = id;
        this.name = name;
        this.servicePriority = servicePriority;
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

    public int getServicePriority() {
        return servicePriority;
    }

    public void setServicePriority(int servicePriority) {
        this.servicePriority = servicePriority;
    }

    public String getMessagePriority() {
        return messagePriority;
    }

    public void setMessagePriority(String messagePriority) {
        this.messagePriority = messagePriority;
    }

    public String getSentimental() {
        return sentimental;
    }

    public void setSentimental(String sentimental) {
        this.sentimental = sentimental;
    }

    public String getRoutingType() {
        return routingType;
    }

    public void setRoutingType(String routingType) {
        this.routingType = routingType;
    }

    public Boolean getRoutingOption() {
        return routingOption;
    }

    public void setRoutingOption(Boolean routingOption) {
        this.routingOption = routingOption;
    }

    public Boolean getRoutingSameUser() {
        return routingSameUser;
    }

    public void setRoutingSameUser(Boolean routingSameUser) {
        this.routingSameUser = routingSameUser;
    }

    public Integer getRoutingUserGroupId() {
        return routingUserGroupId;
    }

    public void setRoutingUserGroupId(Integer routingUserGroupId) {
        this.routingUserGroupId = routingUserGroupId;
    }

    public Boolean getRequireApproval() {
        return requireApproval;
    }

    public void setRequireApproval(Boolean requireApproval) {
        this.requireApproval = requireApproval;
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

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Collection<SoServiceKeyword> getSoServiceKeywordCollection() {
        return soServiceKeywordCollection;
    }

    public void setSoServiceKeywordCollection(Collection<SoServiceKeyword> soServiceKeywordCollection) {
        this.soServiceKeywordCollection = soServiceKeywordCollection;
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
        if (!(object instanceof SoService)) {
            return false;
        }
        SoService other = (SoService) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.social.model.entity.SoService[ id=" + id + " ]";
    }

    public Collection<Users> getUsersCollection() {
        return usersCollection;
    }

    public void setUsersCollection(Collection<Users> usersCollection) {
        this.usersCollection = usersCollection;
    }

    public Collection<SoAccount> getSoAccountCollection() {
        return soAccountCollection;
    }

    public void setSoAccountCollection(Collection<SoAccount> soAccountCollection) {
        this.soAccountCollection = soAccountCollection;
    }

    public SoServiceOutgoingAccount getSoServiceOutgoingAccount() {
        return soServiceOutgoingAccount;
    }

    public void setSoServiceOutgoingAccount(SoServiceOutgoingAccount soServiceOutgoingAccount) {
        this.soServiceOutgoingAccount = soServiceOutgoingAccount;
    }
    
}
