/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author oat
 */
@Entity
@Table(name = "customer_historical_column")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CustomerHistoricalColumn.findAll", query = "SELECT c FROM CustomerHistoricalColumn c")})
public class CustomerHistoricalColumn implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "column_no", nullable = false)
    private int columnNo;
    @Basic(optional = false)
    @Column(name = "column_name", nullable = false, length = 100)
    private String columnName;
    @JoinColumn(name = "customer_historical_group_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private CustomerHistoricalGroup customerHistoricalGroup;

    public CustomerHistoricalColumn() {
    }

    public CustomerHistoricalColumn(Integer id) {
        this.id = id;
    }

    public CustomerHistoricalColumn(Integer id, int columnNo, String columnName) {
        this.id = id;
        this.columnNo = columnNo;
        this.columnName = columnName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getColumnNo() {
        return columnNo;
    }

    public void setColumnNo(int columnNo) {
        this.columnNo = columnNo;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public CustomerHistoricalGroup getCustomerHistoricalGroup() {
        return customerHistoricalGroup;
    }

    public void setCustomerHistoricalGroup(CustomerHistoricalGroup customerHistoricalGroup) {
        this.customerHistoricalGroup = customerHistoricalGroup;
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
        if (!(object instanceof CustomerHistoricalColumn)) {
            return false;
        }
        CustomerHistoricalColumn other = (CustomerHistoricalColumn) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.CustomerHistoricalColumn[ id=" + id + " ]";
    }
    
}
