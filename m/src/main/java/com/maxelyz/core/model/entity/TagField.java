/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "tag_field")
@NamedQueries({@NamedQuery(name = "TagField.findAll", query = "SELECT a FROM TagField a")})
public class TagField implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name="tag_field")
    private String tagField;
    @Column(name = "tag_type")
    private String tagType;
    @Column(name = "enable")
    private Boolean enable;
    
    public TagField() {
    }

    public TagField(Integer id) {
        this.id = id;
    }
    
    public TagField(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
        
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTagField() {
        return tagField;
    }

    public void setTagField(String tagField) {
        this.tagField = tagField;
    }

    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
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
        if (!(object instanceof TagField)) {
            return false;
        }
        TagField other = (TagField) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.TagField[ id=" + id + " ]";
    }
    
}
