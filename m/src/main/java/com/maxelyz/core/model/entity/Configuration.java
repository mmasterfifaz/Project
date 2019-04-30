/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "configuration")
@NamedQueries({@NamedQuery(name = "Configuration.findAll", query = "SELECT c FROM Configuration c")})
public class Configuration implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "property")
    private String property;
    @Basic(optional = false)
    @Column(name = "value")
    private String value;
    @Basic(optional = false)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "datatype")
    private String datatype;

    public Configuration() {
    }

    public Configuration(String property) {
        this.property = property;
    }
    
    public Configuration(String property, String value, String description, String datatype) {
        this.property = property;
        this.value = value;
        this.description = description;
        this.datatype = datatype;
    }

    public Configuration(String property, String value, String datatype) {
        this(property,value,"",datatype);
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (property != null ? property.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Configuration)) {
            return false;
        }
        Configuration other = (Configuration) object;
        if ((this.property == null && other.property != null) || (this.property != null && !this.property.equals(other.property))) {
            return false;
        }
        return true;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    
    
    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.Configuration[property=" + property + "]";
    }

}
