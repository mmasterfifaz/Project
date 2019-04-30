/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "customer_layout_fx_mapping")
@NamedQueries({@NamedQuery(name = "CustomerLayoutFxMapping.findAll", query = "SELECT c FROM CustomerLayoutFxMapping c")})
public class CustomerLayoutFxMapping implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "field_name")
    private String fieldName;
    @Column(name = "custom_name")
    private String customName;
    @JoinColumn(name = "customer_layout_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private CustomerLayout customerLayout;


    public CustomerLayoutFxMapping() {
    }

    public CustomerLayoutFxMapping(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public CustomerLayout getCustomerLayout() {
        return customerLayout;
    }

    public void setCustomerLayout(CustomerLayout customerLayout) {
        this.customerLayout = customerLayout;
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
        if (!(object instanceof CustomerLayoutFxMapping)) {
            return false;
        }
        CustomerLayoutFxMapping other = (CustomerLayoutFxMapping) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.CustomerLayoutFxMapping[ id=" + id + " ]";
    }
    
}
