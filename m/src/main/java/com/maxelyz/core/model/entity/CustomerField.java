/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "customer_field")
@NamedQueries({@NamedQuery(name = "CustomerField.findAll", query = "SELECT c FROM CustomerField c")})
public class CustomerField implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "mapping_field")
    private String mappingField;
    @Basic(optional = false)
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @Column(name = "enable")
    private boolean enable;
    @Column(name = "readonly")
    private Boolean readonly;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerField")
    private Collection<FileTemplateMapping> fileTemplateMappingCollection;
    @OneToMany(mappedBy = "refId")
    private Collection<CustomerField> customerFieldCollection;
    @JoinColumn(name = "ref_id", referencedColumnName = "id")
    @ManyToOne
    private CustomerField refId;
    @Column(name = "seq")
    private Integer seq;
    
    public CustomerField() {
    }

    public CustomerField(Integer id) {
        this.id = id;
    }

    public CustomerField(Integer id, String name, String mappingField, String type, boolean enable) {
        this.id = id;
        this.name = name;
        this.mappingField = mappingField;
        this.type = type;
        this.enable = enable;
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

    public String getMappingField() {
        return mappingField;
    }

    public void setMappingField(String mappingField) {
        this.mappingField = mappingField;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Boolean getReadonly() {
        return readonly;
    }

    public void setReadonly(Boolean readonly) {
        this.readonly = readonly;
    }

    public Collection<FileTemplateMapping> getFileTemplateMappingCollection() {
        return fileTemplateMappingCollection;
    }

    public void setFileTemplateMappingCollection(Collection<FileTemplateMapping> fileTemplateMappingCollection) {
        this.fileTemplateMappingCollection = fileTemplateMappingCollection;
    }

    public Collection<CustomerField> getCustomerFieldCollection() {
        return customerFieldCollection;
    }

    public void setCustomerFieldCollection(Collection<CustomerField> customerFieldCollection) {
        this.customerFieldCollection = customerFieldCollection;
    }

    public CustomerField getRefId() {
        return refId;
    }

    public void setRefId(CustomerField refId) {
        this.refId = refId;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
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
        if (!(object instanceof CustomerField)) {
            return false;
        }
        CustomerField other = (CustomerField) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.CustomerField[id=" + id + "]";
    }

}
