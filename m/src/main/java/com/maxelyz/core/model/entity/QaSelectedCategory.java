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
@Table(name = "qa_selected_category")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "QaSelectedCategory.findAll", query = "SELECT q FROM QaSelectedCategory q")})
public class QaSelectedCategory implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected QaSelectedCategoryPK qaSelectedCategoryPK;
    @Basic(optional = false)
    @Column(name = "seq_no", nullable = false)
    private int seqNo;
    @JoinColumn(name = "qa_form_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private QaForm qaForm;
    @JoinColumn(name = "qa_category_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private QaCategory qaCategory;

    public QaSelectedCategory() {
    }

    public QaSelectedCategory(QaSelectedCategoryPK qaSelectedCategoryPK) {
        this.qaSelectedCategoryPK = qaSelectedCategoryPK;
    }

    public QaSelectedCategory(QaSelectedCategoryPK qaSelectedCategoryPK, int seqNo) {
        this.qaSelectedCategoryPK = qaSelectedCategoryPK;
        this.seqNo = seqNo;
    }

    public QaSelectedCategory(int qaFormId, int qaCategoryId) {
        this.qaSelectedCategoryPK = new QaSelectedCategoryPK(qaFormId, qaCategoryId);
    }

    public QaSelectedCategoryPK getQaSelectedCategoryPK() {
        return qaSelectedCategoryPK;
    }

    public void setQaSelectedCategoryPK(QaSelectedCategoryPK qaSelectedCategoryPK) {
        this.qaSelectedCategoryPK = qaSelectedCategoryPK;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public QaForm getQaForm() {
        return qaForm;
    }

    public void setQaForm(QaForm qaForm) {
        this.qaForm = qaForm;
    }

    public QaCategory getQaCategory() {
        return qaCategory;
    }

    public void setQaCategory(QaCategory qaCategory) {
        this.qaCategory = qaCategory;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (qaSelectedCategoryPK != null ? qaSelectedCategoryPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QaSelectedCategory)) {
            return false;
        }
        QaSelectedCategory other = (QaSelectedCategory) object;
        if ((this.qaSelectedCategoryPK == null && other.qaSelectedCategoryPK != null) || (this.qaSelectedCategoryPK != null && !this.qaSelectedCategoryPK.equals(other.qaSelectedCategoryPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.QaSelectedCategory[ qaSelectedCategoryPK=" + qaSelectedCategoryPK + " ]";
    }
    
}
