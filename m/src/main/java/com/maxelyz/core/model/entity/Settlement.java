/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author oat
 */
@Entity
@Table(name = "settlement")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Settlement.findAll", query = "SELECT s FROM Settlement s")})
public class Settlement implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "type", nullable = false, length = 50)
    private String type;
    @Basic(optional = false)
    @Column(name = "total_record", nullable = false)
    private int totalRecord;
    @Basic(optional = false)
    @Column(name = "complete_record", nullable = false)
    private int completeRecord;
    @Basic(optional = false)
    @Column(name = "create_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "create_by", length = 100)
    private String createBy;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "settlement")
    private Collection<SettlementDetail> settlementDetailCollection;

    public Settlement() {
    }

    public Settlement(Integer id) {
        this.id = id;
    }

    public Settlement(Integer id, String type, int totalRecord, int completeRecord, Date createDate) {
        this.id = id;
        this.type = type;
        this.totalRecord = totalRecord;
        this.completeRecord = completeRecord;
        this.createDate = createDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public int getCompleteRecord() {
        return completeRecord;
    }

    public void setCompleteRecord(int completeRecord) {
        this.completeRecord = completeRecord;
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

    @XmlTransient
    public Collection<SettlementDetail> getSettlementDetailCollection() {
        return settlementDetailCollection;
    }

    public void setSettlementDetailCollection(Collection<SettlementDetail> settlementDetailCollection) {
        this.settlementDetailCollection = settlementDetailCollection;
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
        if (!(object instanceof Settlement)) {
            return false;
        }
        Settlement other = (Settlement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.Settlement[ id=" + id + " ]";
    }
    
}
