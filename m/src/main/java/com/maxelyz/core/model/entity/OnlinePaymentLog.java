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
 * @author oat
 */
@Entity
@Table(name = "online_payment_log")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OnlinePaymentLog.findAll", query = "SELECT o FROM OnlinePaymentLog o"),
    @NamedQuery(name = "OnlinePaymentLog.findById", query = "SELECT o FROM OnlinePaymentLog o WHERE o.id = :id"),
    @NamedQuery(name = "OnlinePaymentLog.findByPostParam", query = "SELECT o FROM OnlinePaymentLog o WHERE o.postParam = :postParam"),
    @NamedQuery(name = "OnlinePaymentLog.findByReturnParam", query = "SELECT o FROM OnlinePaymentLog o WHERE o.returnParam = :returnParam"),
    @NamedQuery(name = "OnlinePaymentLog.findByCreateDate", query = "SELECT o FROM OnlinePaymentLog o WHERE o.createDate = :createDate"),
    @NamedQuery(name = "OnlinePaymentLog.findByCreateBy", query = "SELECT o FROM OnlinePaymentLog o WHERE o.createBy = :createBy")})
public class OnlinePaymentLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "post_param", length = 1000)
    private String postParam;
    @Column(name = "return_param", length = 5000)
    private String returnParam;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "create_by", length = 100)
    private String createBy;

    public OnlinePaymentLog() {
    }

    public OnlinePaymentLog(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPostParam() {
        return postParam;
    }

    public void setPostParam(String postParam) {
        this.postParam = postParam;
    }

    public String getReturnParam() {
        return returnParam;
    }

    public void setReturnParam(String returnParam) {
        this.returnParam = returnParam;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OnlinePaymentLog)) {
            return false;
        }
        OnlinePaymentLog other = (OnlinePaymentLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.OnlinePaymentLog[ id=" + id + " ]";
    }
    
}
