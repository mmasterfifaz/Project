/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author oat
 */
@Embeddable
public class QaSelectedCategoryPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "qa_form_id", nullable = false)
    private int qaFormId;
    @Basic(optional = false)
    @Column(name = "qa_category_id", nullable = false)
    private int qaCategoryId;

    public QaSelectedCategoryPK() {
    }

    public QaSelectedCategoryPK(int qaFormId, int qaCategoryId) {
        this.qaFormId = qaFormId;
        this.qaCategoryId = qaCategoryId;
    }

    public int getQaFormId() {
        return qaFormId;
    }

    public void setQaFormId(int qaFormId) {
        this.qaFormId = qaFormId;
    }

    public int getQaCategoryId() {
        return qaCategoryId;
    }

    public void setQaCategoryId(int qaCategoryId) {
        this.qaCategoryId = qaCategoryId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) qaFormId;
        hash += (int) qaCategoryId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QaSelectedCategoryPK)) {
            return false;
        }
        QaSelectedCategoryPK other = (QaSelectedCategoryPK) object;
        if (this.qaFormId != other.qaFormId) {
            return false;
        }
        if (this.qaCategoryId != other.qaCategoryId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.QaSelectedCategoryPK[ qaFormId=" + qaFormId + ", qaCategoryId=" + qaCategoryId + " ]";
    }
    
}
