/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.social.model.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ukritj
 */
@Entity
@Table(name = "so_email_account")
@NamedQueries({
    @NamedQuery(name = "SoEmailAccount.findAll", query = "SELECT s FROM SoEmailAccount s")})
public class SoEmailAccount implements Serializable {
    @Column(name = "username")
    private String username;
    @Column(name = "inc_protocal")
    private String incProtocal;
    @Column(name = "inc_tls_enable")
    private Boolean incTlsEnable;
    @Column(name = "inc_fallback_enable")
    private Boolean incFallbackEnable;
    @Column(name = "inc_auth_enable")
    private Boolean incAuthEnable;
    @Column(name = "out_server_name")
    private String outServerName;
    @Column(name = "out_protocal")
    private String outProtocal;
    @Column(name = "out_tls_enable")
    private Boolean outTlsEnable;
    @Column(name = "out_fallback_enable")
    private Boolean outFallbackEnable;
    @Column(name = "out_auth_enable")
    private Boolean outAuthEnable;
    @Column(name = "connection_timeout")
    private Integer connectionTimeout;
    @Column(name = "auto_response")
    private Boolean autoResponse;
    @Column(name = "inc_remove")
    private Boolean incRemove;
    @OneToMany(mappedBy = "soEmailAccount")
    private List<SoEmailSignature> soEmailSignatureList;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "fullname")
    private String fullname;
    @Column(name = "email_address")
    private String emailAddress;
    @Column(name = "email_alias")
    private String emailAlias;
    @Column(name = "password")
    private String password;
    @Column(name = "inc_server_name")
    private String incServerName;
    @Column(name = "inc_port")
    private Integer incPort;
    @Column(name = "inc_ssl_enable")
    private Boolean incSslEnable;
    @Column(name = "inc_ssl_certificate_path")
    private String incSslCertificatePath;
    @Column(name = "inc_auth_type")
    private String incAuthType;
    @Column(name = "inc_remove_time")
    private Integer incRemoveTime;
    @Column(name = "out_port")
    private Integer outPort;
    @Column(name = "out_ssl_enable")
    private Boolean outSslEnable;
    @Column(name = "out_ssl_certificate_path")
    private String outSslCertificatePath;
    @Column(name = "out_auth_type")
    private String outAuthType;
    @Column(name = "create_by")
    private String createBy;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "update_by")
    private String updateBy;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "enable")
    private Boolean enable;
    @Column(name = "status")
    private Boolean status;
    @JoinColumn(name = "so_account_id", referencedColumnName = "id")
    @OneToOne(optional = false)
    private SoAccount soAccount;

    public SoEmailAccount() {
    }

    public SoEmailAccount(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIncServerName() {
        return incServerName;
    }

    public void setIncServerName(String incServerName) {
        this.incServerName = incServerName;
    }

    public Integer getIncPort() {
        return incPort;
    }

    public void setIncPort(Integer incPort) {
        this.incPort = incPort;
    }

    public Boolean getIncSslEnable() {
        return incSslEnable;
    }

    public void setIncSslEnable(Boolean incSslEnable) {
        this.incSslEnable = incSslEnable;
    }

    public String getIncSslCertificatePath() {
        return incSslCertificatePath;
    }

    public void setIncSslCertificatePath(String incSslCertificatePath) {
        this.incSslCertificatePath = incSslCertificatePath;
    }

    public String getIncAuthType() {
        return incAuthType;
    }

    public void setIncAuthType(String incAuthType) {
        this.incAuthType = incAuthType;
    }

    public Integer getIncRemoveTime() {
        return incRemoveTime;
    }

    public void setIncRemoveTime(Integer incRemoveTime) {
        this.incRemoveTime = incRemoveTime;
    }

    public Integer getOutPort() {
        return outPort;
    }

    public void setOutPort(Integer outPort) {
        this.outPort = outPort;
    }

    public Boolean getOutSslEnable() {
        return outSslEnable;
    }

    public void setOutSslEnable(Boolean outSslEnable) {
        this.outSslEnable = outSslEnable;
    }

    public String getOutSslCertificatePath() {
        return outSslCertificatePath;
    }

    public void setOutSslCertificatePath(String outSslCertificatePath) {
        this.outSslCertificatePath = outSslCertificatePath;
    }

    public String getOutAuthType() {
        return outAuthType;
    }

    public void setOutAuthType(String outAuthType) {
        this.outAuthType = outAuthType;
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
        if (!(object instanceof SoEmailAccount)) {
            return false;
        }
        SoEmailAccount other = (SoEmailAccount) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.social.model.entity.SoEmailAccount[ id=" + id + " ]";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIncProtocal() {
        return incProtocal;
    }

    public void setIncProtocal(String incProtocal) {
        this.incProtocal = incProtocal;
    }

    public Boolean getIncTlsEnable() {
        return incTlsEnable;
    }

    public void setIncTlsEnable(Boolean incTlsEnable) {
        this.incTlsEnable = incTlsEnable;
    }

    public Boolean getIncFallbackEnable() {
        return incFallbackEnable;
    }

    public void setIncFallbackEnable(Boolean incFallbackEnable) {
        this.incFallbackEnable = incFallbackEnable;
    }

    public Boolean getIncAuthEnable() {
        return incAuthEnable;
    }

    public void setIncAuthEnable(Boolean incAuthEnable) {
        this.incAuthEnable = incAuthEnable;
    }

    public String getOutServerName() {
        return outServerName;
    }

    public void setOutServerName(String outServerName) {
        this.outServerName = outServerName;
    }

    public String getOutProtocal() {
        return outProtocal;
    }

    public void setOutProtocal(String outProtocal) {
        this.outProtocal = outProtocal;
    }

    public Boolean getOutTlsEnable() {
        return outTlsEnable;
    }

    public void setOutTlsEnable(Boolean outTlsEnable) {
        this.outTlsEnable = outTlsEnable;
    }

    public Boolean getOutFallbackEnable() {
        return outFallbackEnable;
    }

    public void setOutFallbackEnable(Boolean outFallbackEnable) {
        this.outFallbackEnable = outFallbackEnable;
    }

    public Boolean getOutAuthEnable() {
        return outAuthEnable;
    }

    public void setOutAuthEnable(Boolean outAuthEnable) {
        this.outAuthEnable = outAuthEnable;
    }

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Boolean getAutoResponse() {
        return autoResponse;
    }

    public void setAutoResponse(Boolean autoResponse) {
        this.autoResponse = autoResponse;
    }

    public Boolean getIncRemove() {
        return incRemove;
    }

    public void setIncRemove(Boolean incRemove) {
        this.incRemove = incRemove;
    }

    public List<SoEmailSignature> getSoEmailSignatureList() {
        return soEmailSignatureList;
    }

    public void setSoEmailSignatureList(List<SoEmailSignature> soEmailSignatureList) {
        this.soEmailSignatureList = soEmailSignatureList;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmailAlias() {
        return emailAlias;
    }

    public void setEmailAlias(String emailAlias) {
        this.emailAlias = emailAlias;
    }
}
