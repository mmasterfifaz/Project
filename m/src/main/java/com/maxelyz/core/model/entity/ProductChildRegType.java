/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Songwisit
 */
@Entity
@Table(name = "product_child_reg_type")
@NamedQueries({@NamedQuery(name = "ProductChildRegType.findAll", query = "SELECT p FROM ProductChildRegType p")})
public class ProductChildRegType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @EmbeddedId
    protected ProductChildRegTypePK productChildRegTypePK;
    @Column(name = "child_reg_form_list")
    private String childRegFormList;
    @Column(name = "rules")
    private String rules;
    @Column(name = "seq_no")
    private Integer seqNo;
    @Column(name = "enable")
    private Boolean enable;
    
//    @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Product product;
//    @JoinColumn(name = "child_reg_type_id", referencedColumnName = "id", insertable = false, updatable = false)
    @JoinColumn(name = "child_reg_type_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ChildRegType childRegType;

    public ProductChildRegType() {
    }

    public ProductChildRegType(ProductChildRegTypePK productChildRegTypePK) {
        this.productChildRegTypePK = productChildRegTypePK;
    }

    public ProductChildRegType(int productId, int childRegTypeId) {
        this.productChildRegTypePK = new ProductChildRegTypePK(productId, childRegTypeId);
    }

    public ProductChildRegTypePK getProductChildRegTypePK() {
        return productChildRegTypePK;
    }

    public void setProductChildRegTypePK(ProductChildRegTypePK productChildRegTypePK) {
        this.productChildRegTypePK = productChildRegTypePK;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ChildRegType getChildRegType() {
        return childRegType;
    }

    public void setChildRegType(ChildRegType childRegType) {
        this.childRegType = childRegType;
    }
    
    public String getChildRegFormList() {
        return childRegFormList;
    }

    public void setChildRegFormList(String childRegFormList) {
        this.childRegFormList = childRegFormList;
    }
    
    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }
    
    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    
  
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productChildRegTypePK != null ? productChildRegTypePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductChildRegType)) {
            return false;
        }
        ProductChildRegType other = (ProductChildRegType) object;
        if ((this.productChildRegTypePK == null && other.productChildRegTypePK != null) || (this.productChildRegTypePK != null && !this.productChildRegTypePK.equals(other.productChildRegTypePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.productChildRegType[productChildRegTypePK=" + productChildRegTypePK + "]";
    }

}
