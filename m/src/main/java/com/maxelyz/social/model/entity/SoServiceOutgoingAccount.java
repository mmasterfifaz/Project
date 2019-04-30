/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.social.model.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author ukritj
 */
@Entity
@Table(name = "so_service_outgoing_account")
@NamedQueries({
    @NamedQuery(name = "SoServiceOutgoingAccount.findAll", query = "SELECT s FROM SoServiceOutgoingAccount s")})
public class SoServiceOutgoingAccount implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "so_service_id")
    private Integer soServiceId;
    @Size(max = 50)
    @Column(name = "outgoing_account_code")
    private String outgoingAccountCode;
    @Size(max = 100)
    @Column(name = "outgoing_account_name")
    private String outgoingAccountName;
    @JoinColumn(name = "so_service_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private SoService soService;

    public SoServiceOutgoingAccount() {
    }

    public SoServiceOutgoingAccount(Integer soServiceId) {
        this.soServiceId = soServiceId;
    }

    public Integer getSoServiceId() {
        return soServiceId;
    }

    public void setSoServiceId(Integer soServiceId) {
        this.soServiceId = soServiceId;
    }

    public String getOutgoingAccountCode() {
        return outgoingAccountCode;
    }

    public void setOutgoingAccountCode(String outgoingAccountCode) {
        this.outgoingAccountCode = outgoingAccountCode;
    }

    public String getOutgoingAccountName() {
        return outgoingAccountName;
    }

    public void setOutgoingAccountName(String outgoingAccountName) {
        this.outgoingAccountName = outgoingAccountName;
    }

    public SoService getSoService() {
        return soService;
    }

    public void setSoService(SoService soService) {
        this.soService = soService;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (soServiceId != null ? soServiceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SoServiceOutgoingAccount)) {
            return false;
        }
        SoServiceOutgoingAccount other = (SoServiceOutgoingAccount) object;
        if ((this.soServiceId == null && other.soServiceId != null) || (this.soServiceId != null && !this.soServiceId.equals(other.soServiceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.social.model.entity.SoServiceOutgoingAccount[ soServiceId=" + soServiceId + " ]";
    }
    
}
