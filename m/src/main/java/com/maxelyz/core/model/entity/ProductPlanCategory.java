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
@Table(name = "product_plan_category")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductPlanCategory.findAll", query = "SELECT p FROM ProductPlanCategory p")})
public class ProductPlanCategory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "code", nullable = false, length = 10)
    private String code;
    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    public ProductPlanCategory() {
    }

    public ProductPlanCategory(Integer id) {
        this.id = id;
    }

    public ProductPlanCategory(Integer id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductPlanCategory)) {
            return false;
        }
        ProductPlanCategory other = (ProductPlanCategory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.ProductPlanCategory[ id=" + id + " ]";
    }
    
}
