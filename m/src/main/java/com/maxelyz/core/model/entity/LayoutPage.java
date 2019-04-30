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
@Table(name = "layout_page")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LayoutPage.findAll", query = "SELECT l FROM LayoutPage l")})
public class LayoutPage implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected LayoutPagePK layoutPagePK;
    @Basic(optional = false)
    @Column(name = "seq_no", nullable = false)
    private int seqNo;
    @Column(name = "col_no")
    private Integer colNo;

    
    public LayoutPage() {
    }

    public LayoutPage(LayoutPagePK layoutPagePK) {
        this.layoutPagePK = layoutPagePK;
    }

    public LayoutPage(LayoutPagePK layoutPagePK, int seqNo) {
        this.layoutPagePK = layoutPagePK;
        this.seqNo = seqNo;
    }

    public LayoutPage(String pageName, String fieldName) {
        this.layoutPagePK = new LayoutPagePK(pageName, fieldName);
    }

    public LayoutPagePK getLayoutPagePK() {
        return layoutPagePK;
    }

    public void setLayoutPagePK(LayoutPagePK layoutPagePK) {
        this.layoutPagePK = layoutPagePK;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public Integer getColNo() {
        return colNo;
    }

    public void setColNo(Integer colNo) {
        this.colNo = colNo;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (layoutPagePK != null ? layoutPagePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LayoutPage)) {
            return false;
        }
        LayoutPage other = (LayoutPage) object;
        if ((this.layoutPagePK == null && other.layoutPagePK != null) || (this.layoutPagePK != null && !this.layoutPagePK.equals(other.layoutPagePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.LayoutPage[ layoutPagePK=" + layoutPagePK + " ]";
    }
    
}
