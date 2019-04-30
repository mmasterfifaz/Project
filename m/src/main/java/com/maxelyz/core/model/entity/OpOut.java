/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author WK
 */
@Entity
@Table(name = "op_out")
@NamedQueries({@NamedQuery(name = "OpOut.findAll", query = "SELECT o FROM OpOut o")})
public class OpOut implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)       
    private Integer id;
    @Column(name = "name", length = 100)
    private String name;
    @Column(name = "surname", length = 100)
    private String surname;
    @Column(name = "idcard", length = 50)
    private String idcard;
    @Column(name = "telephone1", length = 50)
    private String telephone1;
    @Column(name = "telephone2", length = 50)
    private String telephone2;
    @Column(name = "telephone3", length = 50)
    private String telephone3;
    @Column(name = "email", length = 50)
    private String email;
    @Column(name = "remark", length = 500)
    private String remark;
    @Column(name = "check_name")
    private Boolean checkName;
    @Column(name = "check_surname")
    private Boolean checkSurname;
    @Column(name = "check_telephone")
    private Boolean checkTelephone;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "create_by", length = 100)
    private String createBy;
    @Column(name = "dob")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dob;
    @Column(name = "op_out_period")
    private Integer opOutPeriod;
    @Column(name = "last_activity_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastActivityDate;
    @Column(name = "source", length = 500)
    private String source;
    @Column(name = "effect", length = 500)
    private String effect;
    @Column(name = "access_level", length = 50)
    private String accessLevel;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "update_by", length = 100)
    private String updateBy;

    public OpOut() {
    }

    public OpOut(Integer id) {
        this.id = id;
    }
    
    public OpOut(Integer id, String name) {
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getTelephone1() {
        return telephone1;
    }

    public void setTelephone1(String telephone1) {
        this.telephone1 = telephone1;
    }

    public String getTelephone2() {
        return telephone2;
    }

    public void setTelephone2(String telephone2) {
        this.telephone2 = telephone2;
    }

    public String getTelephone3() {
        return telephone3;
    }

    public void setTelephone3(String telephone3) {
        this.telephone3 = telephone3;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public Boolean getCheckName() {
        return checkName;
    }

    public void setCheckName(Boolean checkName) {
        this.checkName = checkName;
    }
    
    public Boolean getCheckSurname() {
        return checkSurname;
    }

    public void setCheckSurname(Boolean checkSurname) {
        this.checkSurname = checkSurname;
    }
    
    public Boolean getCheckTelephone() {
        return checkTelephone;
    }

    public void setCheckTelephone(Boolean checkTelephone) {
        this.checkTelephone = checkTelephone;
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

    public Integer getOpOutPeriod() {
        return opOutPeriod;
    }

    public void setOpOutPeriod(Integer opOutPeriod) {
        this.opOutPeriod = opOutPeriod;
    }
    
    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Date getLastActivityDate() {
        return lastActivityDate;
    }

    public void setLastActivityDate(Date lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
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
        if (!(object instanceof OpOut)) {
            return false;
        }
        OpOut other = (OpOut) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.OpOut[ id=" + id + " ]";
    }
    
}
