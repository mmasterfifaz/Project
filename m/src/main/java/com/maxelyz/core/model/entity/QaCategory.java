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
@Table(name = "qa_category")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "QaCategory.findAll", query = "SELECT q FROM QaCategory q")})
public class QaCategory implements Serializable {
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @OneToMany(mappedBy = "qaCategory")
    private Collection<QaTransDetail> qaTransDetailCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Column(name = "description", length = 500)
    private String description;
    @Basic(optional = false)
    @Column(name = "enable", nullable = false)
    private boolean enable;
    @Column(name = "create_by", length = 100)
    private String createBy;
    @Column(name = "update_by", length = 100)
    private String updateBy;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "qaCategory")
    private Collection<QaSelectedCategory> qaSelectedCategoryCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "qaCategory")
    private Collection<QaQuestion> qaQuestionCollection;

    public QaCategory() {
    }

    public QaCategory(Integer id) {
        this.id = id;
    }

    public QaCategory(Integer id, String name, boolean enable) {
        this.id = id;
        this.name = name;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
    public Collection<QaSelectedCategory> getQaSelectedCategoryCollection() {
        return qaSelectedCategoryCollection;
    }

    public void setQaSelectedCategoryCollection(Collection<QaSelectedCategory> qaSelectedCategoryCollection) {
        this.qaSelectedCategoryCollection = qaSelectedCategoryCollection;
    }

    @XmlTransient
    public Collection<QaQuestion> getQaQuestionCollection() {
        return qaQuestionCollection;
    }

    public void setQaQuestionCollection(Collection<QaQuestion> qaQuestionCollection) {
        this.qaQuestionCollection = qaQuestionCollection;
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
        if (!(object instanceof QaCategory)) {
            return false;
        }
        QaCategory other = (QaCategory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.QaCategory[ id=" + id + " ]";
    }

    @XmlTransient
    public Collection<QaTransDetail> getQaTransDetailCollection() {
        return qaTransDetailCollection;
    }

    public void setQaTransDetailCollection(Collection<QaTransDetail> qaTransDetailCollection) {
        this.qaTransDetailCollection = qaTransDetailCollection;
    }
    
}
