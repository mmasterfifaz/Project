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
@Table(name = "qa_trans")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "QaTrans.findAll", query = "SELECT q FROM QaTrans q")})
public class QaTrans implements Serializable {
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "qa_form_id")
    private Integer qaFormId;
    @Basic(optional = false)
    @Column(name = "qa_form_name", nullable = false, length = 100)
    private String qaFormName;
    @Column(name = "create_by", length = 100)
    private String createBy;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "qaTrans")
    private Collection<QaTransDetail> qaTransDetailCollection;

    public QaTrans() {
    }

    public QaTrans(Integer id) {
        this.id = id;
    }

    public QaTrans(Integer id, String qaFormName) {
        this.id = id;
        this.qaFormName = qaFormName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQaFormId() {
        return qaFormId;
    }

    public void setQaFormId(Integer qaFormId) {
        this.qaFormId = qaFormId;
    }

    public String getQaFormName() {
        return qaFormName;
    }

    public void setQaFormName(String qaFormName) {
        this.qaFormName = qaFormName;
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

    public Collection<QaTransDetail> getQaTransDetailCollection() {
        return qaTransDetailCollection;
    }

    public void setQaTransDetailCollection(Collection<QaTransDetail> qaTransDetailCollection) {
        this.qaTransDetailCollection = qaTransDetailCollection;
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
        if (!(object instanceof QaTrans)) {
            return false;
        }
        QaTrans other = (QaTrans) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.QaTrans[ id=" + id + " ]";
    }

    
}
