/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author oat
 */
@Entity
@Table(name = "settlement_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SettlementDetail.findAll", query = "SELECT s FROM SettlementDetail s")})
public class SettlementDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "sale_ref_no", nullable = false, length = 50)
    private String saleRefNo;
    @Basic(optional = false)
    @Column(name = "customer_ref_no", nullable = false, length = 50)
    private String customerRefNo;
    @Basic(optional = false)
    @Column(name = "trans_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date transDate;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "amount", nullable = false, precision = 9, scale = 2)
    private BigDecimal amount;
    @Column(name = "result", length = 100)
    private String result;
    @JoinColumn(name = "settlement_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Settlement settlement;

    public SettlementDetail() {
    }

    public SettlementDetail(Integer id) {
        this.id = id;
    }

    public SettlementDetail(Integer id, String saleRefNo, String customerRefNo, Date transDate, BigDecimal amount) {
        this.id = id;
        this.saleRefNo = saleRefNo;
        this.customerRefNo = customerRefNo;
        this.transDate = transDate;
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSaleRefNo() {
        return saleRefNo;
    }

    public void setSaleRefNo(String saleRefNo) {
        this.saleRefNo = saleRefNo;
    }

    public String getCustomerRefNo() {
        return customerRefNo;
    }

    public void setCustomerRefNo(String customerRefNo) {
        this.customerRefNo = customerRefNo;
    }

    public Date getTransDate() {
        return transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Settlement getSettlement() {
        return settlement;
    }

    public void setSettlement(Settlement settlement) {
        this.settlement = settlement;
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
        if (!(object instanceof SettlementDetail)) {
            return false;
        }
        SettlementDetail other = (SettlementDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.SettlementDetail[ id=" + id + " ]";
    }
    
}
