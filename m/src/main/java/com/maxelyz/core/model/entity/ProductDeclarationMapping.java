/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import javax.persistence.*;

/**
 *
 * @author User
 */
@Entity
@Table(name = "product_declaration_mapping")
@XmlRootElement
@NamedQueries({
@NamedQuery(name = "ProductDeclarationMapping.findAll", query = "SELECT p FROM ProductDeclarationMapping p")})
public class ProductDeclarationMapping implements Serializable  {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    protected ProductDeclarationMappingPK productDeclarationMappingPK;
    
    public ProductDeclarationMapping() {
    }

    public ProductDeclarationMapping(ProductDeclarationMappingPK productDeclarationMappingPK) {
        this.productDeclarationMappingPK = productDeclarationMappingPK;
    }

    public ProductDeclarationMapping(Integer productId, Integer declarationFormId) {
        this.productDeclarationMappingPK = new ProductDeclarationMappingPK(productId, declarationFormId);
    }

//    @Id
//    @Basic(optional = false)
//    @JoinColumn(name = "product_id", referencedColumnName = "id")
//    @ManyToOne
//    private Product productId;
//    @Id
//    @Basic(optional = false)
//    @JoinColumn(name = "declaration_form_id", referencedColumnName = "id")
//    @ManyToOne
//    private DeclarationForm declarationFormId;
//
//    public Product getProductId() {
//        return productId;
//    }
//
//    public void setProductId(Product productId) {
//        this.productId = productId;
//    }
//
//    public DeclarationForm getDeclarationFormId() {
//        return declarationFormId;
//    }
//
//    public void setDeclarationFormId(DeclarationForm declarationFormId) {
//        this.declarationFormId = declarationFormId;
//    }

    public ProductDeclarationMappingPK getProductDeclarationMappingPK() {
        return productDeclarationMappingPK;
    }

    public void setProductDeclarationMappingPK(ProductDeclarationMappingPK productDeclarationMappingPK) {
        this.productDeclarationMappingPK = productDeclarationMappingPK;
    }

    
    
    
}
