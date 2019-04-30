/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "payment_method")
@NamedQueries({
    @NamedQuery(name = "PaymentMethod.findAll", query = "SELECT p FROM PaymentMethod p")})
public class PaymentMethod implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "code", nullable = false, length = 50)
    private String code;
    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    @Basic(optional = false)
    @Column(name = "online", nullable = false)
    private boolean online;
    @Basic(optional = false)
    @Column(name = "creditcard", nullable = false)
    private boolean creditcard;
    @Basic(optional = false)
    @Column(name = "debitcard", nullable = false)
    private boolean debitcard;
    @Basic(optional = false)
    @Column(name = "seq_no", nullable = false)
    private int seqNo;
    @Basic(optional = false)
    @Column(name = "enable", nullable = false)
    private boolean enable;
    @Basic(optional = false)
    @Column(name = "status", nullable = false)
    private boolean status;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "create_by", length = 100)
    private String createBy;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "update_by", length = 100)
    private String updateBy;
    @Column(name = "yes_file_title", length = 100)
    private String yesFileTitle;
    @Column(name = "account_no", nullable = false)
    private Integer accountNo ;
    @Column(name = "expected_date", nullable = false)
    private Integer expectedDate;
    @Column(name = "remarks", nullable = false)
    private Integer remarks;

    public PaymentMethod() {
    }

    public PaymentMethod(Integer id) {
        this.id = id;
    }

    public PaymentMethod(Integer id, String code, String name, boolean online, int seqNo, boolean enable, boolean status) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.online = online;
        this.seqNo = seqNo;
        this.enable = enable;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public boolean getEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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

    public boolean isCreditcard() {
        return creditcard;
    }

    public void setCreditcard(boolean creditcard) {
        this.creditcard = creditcard;
    }

    public String getYesFileTitle() {
        return yesFileTitle;
    }

    public void setYesFileTitle(String yesFileTitle) {
        this.yesFileTitle = yesFileTitle;
    }

    public boolean isDebitcard() {
        return debitcard;
    }

    public void setDebitcard(boolean debitcard) {
        this.debitcard = debitcard;
    }

    public Integer getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(Integer accountNo) {
        this.accountNo = accountNo;
    }

    public Integer getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(Integer expectedDate) {
        this.expectedDate = expectedDate;
    }

    public Integer getRemarks() {
        return remarks;
    }

    public void setRemarks(Integer remarks) {
        this.remarks = remarks;
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
        if (!(object instanceof PaymentMethod)) {
            return false;
        }
        PaymentMethod other = (PaymentMethod) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.PaymentMethod[ id=" + id + " ]";
    }
    
}
