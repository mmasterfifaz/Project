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
@Table(name = "customer_historical_group")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CustomerHistoricalGroup.findAll", query = "SELECT c FROM CustomerHistoricalGroup c")})
public class CustomerHistoricalGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "name", length = 50)
    private String name;
    @Basic(optional = false)
    @Column(name = "no_column", nullable = false)
    private int noColumn;
    @Basic(optional = false)
    @Column(name = "highlight_column", nullable = false, length = 100)
    private String highlightColumn;
    @Column(name = "enable")
    private Boolean enable;
    @Column(name = "update_by", length = 100)
    private String updateBy;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerHistoricalGroup")
    private Collection<CustomerHistoricalColumn> customerHistoricalColumnCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerHistoricalGroup")
    private Collection<CustomerHistorical> customerHistoricalCollection;
    
    public CustomerHistoricalGroup() {
    }

    public CustomerHistoricalGroup(Integer id) {
        this.id = id;
    }

    public CustomerHistoricalGroup(Integer id, int noColumn, String highlightColumn) {
        this.id = id;
        this.noColumn = noColumn;
        this.highlightColumn = highlightColumn;
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

    public int getNoColumn() {
        return noColumn;
    }

    public void setNoColumn(int noColumn) {
        this.noColumn = noColumn;
    }

    public String getHighlightColumn() {
        return highlightColumn;
    }

    public void setHighlightColumn(String highlightColumn) {
        this.highlightColumn = highlightColumn;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
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
    
    @XmlTransient
    public Collection<CustomerHistorical> getCustomerHistoricalCollection() {
        return customerHistoricalCollection;
    }

    public void setCustomerHistoricalCollection(Collection<CustomerHistorical> customerHistoricalCollection) {
        this.customerHistoricalCollection = customerHistoricalCollection;
    }
    
    @XmlTransient
    public Collection<CustomerHistoricalColumn> getCustomerHistoricalColumnCollection() {
        return customerHistoricalColumnCollection;
    }

    public void setCustomerHistoricalColumnCollection(Collection<CustomerHistoricalColumn> customerHistoricalColumnCollection) {
        this.customerHistoricalColumnCollection = customerHistoricalColumnCollection;
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
        if (!(object instanceof CustomerHistoricalGroup)) {
            return false;
        }
        CustomerHistoricalGroup other = (CustomerHistoricalGroup) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.CustomerHistoricalGroup[ id=" + id + " ]";
    }
    
}
