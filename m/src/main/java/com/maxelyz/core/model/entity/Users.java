/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import com.maxelyz.social.model.entity.SoActivityLog;
import com.maxelyz.social.model.entity.SoService;
import com.maxelyz.social.model.entity.SoUserSignature;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "users")
@NamedQueries({@NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u")})
public class Users implements Serializable {
    @OneToMany(mappedBy = "createBy")
    private Collection<SoActivityLog> soActivityLogCollection;
    @Column(name = "so_onhand_quota")
    private Integer soOnhandQuota;
    @OneToMany(mappedBy = "users")
    private Collection<SoUserSignature> soUserSignatureCollection;
    @ManyToMany(mappedBy = "usersCollection")
    private Collection<SoService> soServiceCollection;
    @Size(max = 50)
    @Column(name = "code")
    private String code;
    @Size(max = 50)
    @Column(name = "license_name")
    private String licenseName;
    @Size(max = 50)
    @Column(name = "agent_code")
    private String agentCode;
    @Column(name = "external_user_id ")
    private String externalUserId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "monthly_target")
    private BigDecimal monthlyTarget;
    @Column(name = "yearly_target")
    private BigDecimal yearlyTarget;
    @Size(max = 100)
    @Column(name = "fx1")
    private String fx1;
    @Size(max = 100)
    @Column(name = "fx2")
    private String fx2;
    @Size(max = 100)
    @Column(name = "fx3")
    private String fx3;
    @Size(max = 100)
    @Column(name = "fx4")
    private String fx4;
    @Size(max = 100)
    @Column(name = "fx5")
    private String fx5;
    @Size(max = 100)
    @Column(name = "fx6")
    private String fx6;
    @Size(max = 100)
    @Column(name = "fx7")
    private String fx7;
    @Size(max = 100)
    @Column(name = "fx8")
    private String fx8;
    @Size(max = 100)
    @Column(name = "fx9")
    private String fx9;
    @Size(max = 100)
    @Column(name = "fx10")
    private String fx10;
    @Size(max = 100)
    @Column(name = "fx11")
    private String fx11;
    @Size(max = 100)
    @Column(name = "fx12")
    private String fx12;
    @Size(max = 100)
    @Column(name = "fx13")
    private String fx13;
    @Size(max = 100)
    @Column(name = "fx14")
    private String fx14;
    @Size(max = 100)
    @Column(name = "fx15")
    private String fx15;
    @Size(max = 100)
    @Column(name = "fx16")
    private String fx16;
    @Size(max = 100)
    @Column(name = "fx17")
    private String fx17;
    @Size(max = 100)
    @Column(name = "fx18")
    private String fx18;
    @Size(max = 100)
    @Column(name = "fx19")
    private String fx19;
    @Size(max = 100)
    @Column(name = "fx20")
    private String fx20;
    @Column(name = "so_inbox_quota")
    private Integer soInboxQuota;
    @Column(name = "income_message")
    private Integer incomeMessage;
    @Column(name = "date_of_birth")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfBirth;
    @Column(name = "hired_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date hiredDate;
    @Column(name = "resigned_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date resignedDate;
    @Basic(optional = false)
    @Column(name = "last_changed_password", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastChangedPassword;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Basic(optional = false)
    @Column(name = "force_change_password", nullable = false)
    private int forceChangePassword;
    @Basic(optional = false)
    @Column(name = "count_change_password", nullable = false)
    private int countChangePassword;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "users")
    private Collection<UsersScript> usersScriptCollection;  
    @JoinColumn(name = "user_group_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UserGroup userGroup;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "nickname")
    private String nickname;
    @Column(name = "mobile")
    private String mobile;
    @Column(name = "email")
    private String email;
    @Column(name = "license_no")
    private String licenseNo;
    @Basic(optional = false)
    @Column(name = "login_name")
    private String loginName;
    @Basic(optional = false)
    @Column(name = "user_password")
    private String userPassword;
    @Column(name = "description")
    private String description;
    @Column(name = "enable")
    private Boolean enable;
    @Column(name = "status")
    private Boolean status;
    @Column(name = "telephony_login_name")
    private String telephonyLoginName;
    
    @Column(name = "aspect_password")
//    @Column(name = "telephony_password")    
    private String telephonyPassword;
    
    
    @Column(name = "station_id")
    private String stationId;
    @Column(name = "service_id")
    private String serviceId;
    @Column(name = "photo")
    private String photo;
    @Lob
    @Column(name = "makenote")
    private String makenote;
    @Column(name = "allow_case_type")
    private String allowCaseType;
    @Column(name = "create_by")
    private String createBy;
    @Column(name = "update_by")
    private String updateBy;
    @Column(name = "ldap_login")
    private String ldapLogin;
    @Column(name = "base_dn")
    private String baseDn;
    @Column(name = "telephony_sso")
    private Boolean telephonySso;
    @Column(name = "user_type")
    private String userType;
    @Column(name = "default_page")
    private String defaultPage;
    @Column(name = "is_administrator", nullable = false)
    private Boolean isAdministrator = false;
    @Column(name = "is_system")
    private Boolean isSystem = false;
    @Column(name = "citizen_id")
    private String citizenId;
    @Column(name = "image")
    private String image;
    @Column(name = "users_status_id")
    private Integer usersStatusId;
    @Column(name = "user_expire")
    private Boolean userExpire = false;
    @Column(name = "user_expire_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date userExpireDate;
    
    @ManyToMany(mappedBy = "usersCollection")
    private Collection<MessageBroadcast> messageBroadcastCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "users")
    private Collection<UserMember> userMemberCollection;  

    public Users() {
    }

    public Users(Integer id) {
        this.id = id;
    }

    public Users(Integer id, String name, String loginName, String userPassword) {
        this.id = id;
        this.name = name;
        this.loginName = loginName;
        this.userPassword = userPassword;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
    
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public Date getHiredDate() {
        return hiredDate;
    }

    public void setHiredDate(Date hiredDate) {
        this.hiredDate = hiredDate;
    }

    public Date getResignedDate() {
        return resignedDate;
    }

    public void setResignedDate(Date resignedDate) {
        this.resignedDate = resignedDate;
    }

    public String getLoginName() {
        return loginName != null ? loginName.trim() : null;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getTelephonyLoginName() {
        return telephonyLoginName != null ? telephonyLoginName.trim() : null;
    }

    public void setTelephonyLoginName(String telephonyLoginName) {
        this.telephonyLoginName = telephonyLoginName;
    }

    public String getTelephonyPassword() {
        return telephonyPassword;
    }

    public void setTelephonyPassword(String telephonyPassword) {
        this.telephonyPassword = telephonyPassword;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Date getLastChangedPassword() {
        return lastChangedPassword;
    }

    public void setLastChangedPassword(Date lastChangedPassword) {
        this.lastChangedPassword = lastChangedPassword;
    }

    public Integer getForceChangePassword() {
        return forceChangePassword;
    }

    public void setForceChangePassword(Integer forceChangePassword) {
        this.forceChangePassword = forceChangePassword;
    }

    public Integer getCountChangePassword() {
        return countChangePassword;
    }

    public void setCountChangePassword(Integer countChangePassword) {
        this.countChangePassword = countChangePassword;
    }

    public String getMakenote() {
        return makenote;
    }

    public void setMakenote(String makenote) {
        this.makenote = makenote;
    }

    public String getAllowCaseType() {
        return allowCaseType;
    }

    public void setAllowCaseType(String allowCaseType) {
        this.allowCaseType = allowCaseType;
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

    public Collection<MessageBroadcast> getMessageBroadcastCollection() {
        return messageBroadcastCollection;
    }

    public void setMessageBroadcastCollection(Collection<MessageBroadcast> messageBroadcastCollection) {
        this.messageBroadcastCollection = messageBroadcastCollection;
    }


    /*public Collection<AssignmentDetail> getAssignmentDetailCollection() {
        return assignmentDetailCollection;
    }

    public void setAssignmentDetailCollection(Collection<AssignmentDetail> assignmentDetailCollection) {
        this.assignmentDetailCollection = assignmentDetailCollection;
    }*/


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.Users[id=" + id + "]";
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }
    
    public Collection<UsersScript> getUsersScriptCollection() {
        return usersScriptCollection;
    }

    public void setUsersScriptCollection(Collection<UsersScript> usersScriptCollection) {
        this.usersScriptCollection = usersScriptCollection;
    }


    public void setCountChangePassword(int countChangePassword) {
        this.countChangePassword = countChangePassword;
    }

    public String getBaseDn() {
        return baseDn;
    }

    public void setBaseDn(String baseDn) {
        this.baseDn = baseDn;
    }

    public String getLdapLogin() {
        return ldapLogin != null ? ldapLogin.trim() : null;
    }

    public void setLdapLogin(String ldapLogin) {
        this.ldapLogin = ldapLogin;
    }

    public Boolean getTelephonySso() {
        return telephonySso;
    }

    public void setTelephonySso(Boolean telephonySso) {
        this.telephonySso = telephonySso;
    }

    public Collection<UserMember> getUserMemberCollection() {
        return userMemberCollection;
    }

    public void setUserMemberCollection(Collection<UserMember> userMemberCollection) {
        this.userMemberCollection = userMemberCollection;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLicenseName() {
        return licenseName;
    }

    public void setLicenseName(String licenseName) {
        this.licenseName = licenseName;
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
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

    public String getFx1() {
        return fx1;
    }

    public void setFx1(String fx1) {
        this.fx1 = fx1;
    }

    public String getFx2() {
        return fx2;
    }

    public void setFx2(String fx2) {
        this.fx2 = fx2;
    }

    public String getFx3() {
        return fx3;
    }

    public void setFx3(String fx3) {
        this.fx3 = fx3;
    }

    public String getFx4() {
        return fx4;
    }

    public void setFx4(String fx4) {
        this.fx4 = fx4;
    }

    public String getFx5() {
        return fx5;
    }

    public void setFx5(String fx5) {
        this.fx5 = fx5;
    }

    public String getFx6() {
        return fx6;
    }

    public void setFx6(String fx6) {
        this.fx6 = fx6;
    }

    public String getFx7() {
        return fx7;
    }

    public void setFx7(String fx7) {
        this.fx7 = fx7;
    }

    public String getFx8() {
        return fx8;
    }

    public void setFx8(String fx8) {
        this.fx8 = fx8;
    }

    public String getFx9() {
        return fx9;
    }

    public void setFx9(String fx9) {
        this.fx9 = fx9;
    }

    public String getFx10() {
        return fx10;
    }

    public void setFx10(String fx10) {
        this.fx10 = fx10;
    }

    public String getFx11() {
        return fx11;
    }

    public void setFx11(String fx11) {
        this.fx11 = fx11;
    }

    public String getFx12() {
        return fx12;
    }

    public void setFx12(String fx12) {
        this.fx12 = fx12;
    }

    public String getFx13() {
        return fx13;
    }

    public void setFx13(String fx13) {
        this.fx13 = fx13;
    }

    public String getFx14() {
        return fx14;
    }

    public void setFx14(String fx14) {
        this.fx14 = fx14;
    }

    public String getFx15() {
        return fx15;
    }

    public void setFx15(String fx15) {
        this.fx15 = fx15;
    }

    public String getFx16() {
        return fx16;
    }

    public void setFx16(String fx16) {
        this.fx16 = fx16;
    }

    public String getFx17() {
        return fx17;
    }

    public void setFx17(String fx17) {
        this.fx17 = fx17;
    }

    public String getFx18() {
        return fx18;
    }

    public void setFx18(String fx18) {
        this.fx18 = fx18;
    }

    public String getFx19() {
        return fx19;
    }

    public void setFx19(String fx19) {
        this.fx19 = fx19;
    }

    public String getFx20() {
        return fx20;
    }

    public void setFx20(String fx20) {
        this.fx20 = fx20;
    }

    public Integer getSoInboxQuota() {
        return soInboxQuota;
    }

    public void setSoInboxQuota(Integer soInboxQuota) {
        this.soInboxQuota = soInboxQuota;
    }

    public Integer getIncomeMessage() {
        return incomeMessage;
    }

    public void setIncomeMessage(Integer incomeMessage) {
        this.incomeMessage = incomeMessage;
    }

    public Collection<SoService> getSoServiceCollection() {
        return soServiceCollection;
    }

    public void setSoServiceCollection(Collection<SoService> soServiceCollection) {
        this.soServiceCollection = soServiceCollection;
    }

    public String getDefaultPage() {
        return defaultPage;
    }

    public void setDefaultPage(String defaultPage) {
        this.defaultPage = defaultPage;
    }

    public Integer getSoOnhandQuota() {
        return soOnhandQuota;
    }

    public void setSoOnhandQuota(Integer soOnhandQuota) {
        this.soOnhandQuota = soOnhandQuota;
    }

    public Collection<SoUserSignature> getSoUserSignatureCollection() {
        return soUserSignatureCollection;
    }

    public void setSoUserSignatureCollection(Collection<SoUserSignature> soUserSignatureCollection) {
        this.soUserSignatureCollection = soUserSignatureCollection;
    }

    public Collection<SoActivityLog> getSoActivityLogCollection() {
        return soActivityLogCollection;
    }

    public void setSoActivityLogCollection(Collection<SoActivityLog> soActivityLogCollection) {
        this.soActivityLogCollection = soActivityLogCollection;
    }

    public Boolean getIsAdministrator() {
        return isAdministrator;
    }

    public void setIsAdministrator(Boolean isAdministrator) {
        this.isAdministrator = isAdministrator;
    }

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getUsersStatusId() {
        return usersStatusId;
    }

    public void setUsersStatusId(Integer usersStatusId) {
        this.usersStatusId = usersStatusId;
    }

    public Boolean getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(Boolean isSystem) {
        this.isSystem = isSystem;
    }
    
    public String getExternalUserId() {
        return externalUserId;
    }

    public void setExternalUserId(String externalUserId) {
        this.externalUserId = externalUserId;
    }

    public Boolean getUserExpire() {
        return userExpire;
    }

    public void setUserExpire(Boolean userExpire) {
        this.userExpire = userExpire;
    }

    public Date getUserExpireDate() {
        return userExpireDate;
    }

    public void setUserExpireDate(Date userExpireDate) {
        this.userExpireDate = userExpireDate;
    }
}
