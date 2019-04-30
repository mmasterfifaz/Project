/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "user_group")
@NamedQueries({
    @NamedQuery(name = "UserGroup.findAll", query = "SELECT u FROM UserGroup u where enable=true and status = true order by name")})
public class UserGroup implements Serializable {
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "monthly_target")
    private BigDecimal monthlyTarget;
    @Column(name = "yearly_target")
    private BigDecimal yearlyTarget;
    @Column(name =     "show_all_contact_history")
    private Boolean showAllContactHistory;
    @Column(name =     "show_telephone_no")
    private Boolean showTelephoneNo;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "status")
    private Boolean status;
    @Column(name = "enable")
    private Boolean enable;
    @Column(name = "hide_phone_no")
    private String hidePhoneNo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userGroup")
    private Collection<UserGroupLocation> userGroupLocationCollection;
    @Column(name = "parent_id")
    private Integer parentId;
    @Column(name = "auth_service_type", length = 1000)
    private String authServiceType;
    @Column(name = "auth_location", length = 1000)
    private String authLocation;
    @Column(name = "auth_business_unit", length = 1000)
    private String authBusinessUnit;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userGroup")
    private Collection<UserGroupAcl> userGroupAclCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userGroup")
    private Collection<Users> usersCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "role")
    private String role;
    @Column(name = "telephony_service_id")
    private String telephonyServiceId;
    @Column(name = "create_by")
    private String createBy;
    @Column(name = "update_by")
    private String updateBy;
    @Column(name = "concurrent_group")
    private String concurrentGroup;
    @Column(name = "allow_edit_telephone")
    private Boolean allowEditTelephone;
    @Column(name = "allow_add_telephone")
    private Boolean allowAddTelephone;
    
    @JoinColumn(name = "cti_adapter_id", referencedColumnName = "id")
    @ManyToOne
    private CtiAdapter ctiAdapter;
    
    public UserGroup() {
    }

    public UserGroup(Integer id) {
        this.id = id;
    }

    public UserGroup(Integer id, String name) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTelephonyServiceId() {
        return telephonyServiceId;
    }

    public void setTelephonyServiceId(String telephonyServiceId) {
        this.telephonyServiceId = telephonyServiceId;
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

    public Collection<Users> getUsersCollection() {
        return usersCollection;
    }

    public void setUsersCollection(Collection<Users> usersCollection) {
        this.usersCollection = usersCollection;
    }

    public String getConcurrentGroup() {
        return concurrentGroup;
    }

    public void setConcurrentGroup(String concurrentGroup) {
        this.concurrentGroup = concurrentGroup;
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
        if (!(object instanceof UserGroup)) {
            return false;
        }
        UserGroup other = (UserGroup) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.UserGroup[id=" + id + "]";
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getAuthServiceType() {
        return authServiceType;
    }

    public void setAuthServiceType(String authServiceType) {
        this.authServiceType = authServiceType;
    }

    public String getAuthLocation() {
        return authLocation;
    }

    public void setAuthLocation(String authLocation) {
        this.authLocation = authLocation;
    }

    public String getAuthBusinessUnit() {
        return authBusinessUnit;
    }

    public void setAuthBusinessUnit(String authBusinessUnit) {
        this.authBusinessUnit = authBusinessUnit;
    }

    @XmlTransient
    public Collection<UserGroupAcl> getUserGroupAclCollection() {
        return userGroupAclCollection;
    }

    public void setUserGroupAclCollection(Collection<UserGroupAcl> userGroupAclCollection) {
        this.userGroupAclCollection = userGroupAclCollection;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getHidePhoneNo() {
        return hidePhoneNo;
    }

    public void setHidePhoneNo(String hidePhoneNo) {
        this.hidePhoneNo = hidePhoneNo;
    }

    @XmlTransient
    public Collection<UserGroupLocation> getUserGroupLocationCollection() {
        return userGroupLocationCollection;
    }

    public void setUserGroupLocationCollection(Collection<UserGroupLocation> userGroupLocationCollection) {
        this.userGroupLocationCollection = userGroupLocationCollection;
    }

    public Boolean getShowAllContactHistory() {
        return showAllContactHistory;
    }

    public void setShowAllContactHistory(Boolean showAllContactHistory) {
        this.showAllContactHistory = showAllContactHistory;
    }

    public Boolean getShowTelephoneNo() {
        return showTelephoneNo;
    }

    public void setShowTelephoneNo(Boolean showTelephoneNo) {
        this.showTelephoneNo = showTelephoneNo;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getAllowEditTelephone() {
        return allowEditTelephone;
    }

    public void setAllowEditTelephone(Boolean allowEditTelephone) {
        this.allowEditTelephone = allowEditTelephone;
    }

    public Boolean getAllowAddTelephone() {
        return allowAddTelephone;
    }

    public void setAllowAddTelephone(Boolean allowAddTelephone) {
        this.allowAddTelephone = allowAddTelephone;
    }

    public BigDecimal getMonthlyTarget() {
        return monthlyTarget;
    }

    public void setMonthlyTarget(BigDecimal monthlyTarget) {
        this.monthlyTarget = monthlyTarget;
    }

    public BigDecimal getYearlyTarget() {
        return yearlyTarget;
    }

    public void setYearlyTarget(BigDecimal yearlyTarget) {
        this.yearlyTarget = yearlyTarget;
    }

    public CtiAdapter getCtiAdapter() {
        return ctiAdapter;
    }

    public void setCtiAdapter(CtiAdapter ctiAdapter) {
        this.ctiAdapter = ctiAdapter;
    }
    
    
}
