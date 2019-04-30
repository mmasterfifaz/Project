/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "purchase_order_declaration")
@NamedQueries({
    @NamedQuery(name = "PurchaseOrderDeclaration.findAll", query = "SELECT p FROM PurchaseOrderDeclaration p")})
public class PurchaseOrderDeclaration implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @JoinColumn(name = "purchase_order_detail_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private PurchaseOrderDetail purchaseOrderDetail;
    @JoinColumn(name = "declaration_form_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DeclarationForm declarationForm;
    
    @Column(name = "enable")
    private Boolean enable;
    
    @Column(name = "fx1")
    private String fx1;
    @Column(name = "fx2")
    private String fx2;
    @Column(name = "fx3")
    private String fx3;
    @Column(name = "fx4")
    private String fx4;
    @Column(name = "fx5")
    private String fx5;
    @Column(name = "fx6")
    private String fx6;
    @Column(name = "fx7")
    private String fx7;
    @Column(name = "fx8")
    private String fx8;
    @Column(name = "fx9")
    private String fx9;
    @Column(name = "fx10")
    private String fx10;
    @Column(name = "fx11")
    private String fx11;
    @Column(name = "fx12")
    private String fx12;
    @Column(name = "fx13")
    private String fx13;
    @Column(name = "fx14")
    private String fx14;
    @Column(name = "fx15")
    private String fx15;
    @Column(name = "fx16")
    private String fx16;
    @Column(name = "fx17")
    private String fx17;
    @Column(name = "fx18")
    private String fx18;
    @Column(name = "fx19")
    private String fx19;
    @Column(name = "fx20")
    private String fx20;
    
    public PurchaseOrderDeclaration() {
    }

    public PurchaseOrderDeclaration(Integer id) {
        this.id = id;
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

    public String getFx1() {
        return fx1;
    }

    public void setFx1(String fx1) {
        this.fx1 = fx1;
    }

    public String getFx2() {
        return fx2;
    }

    public void setFx2(String fx2) {
        this.fx2 = fx2;
    }

    public String getFx3() {
        return fx3;
    }

    public void setFx3(String fx3) {
        this.fx3 = fx3;
    }

    public String getFx4() {
        return fx4;
    }

    public void setFx4(String fx4) {
        this.fx4 = fx4;
    }

    public String getFx5() {
        return fx5;
    }

    public void setFx5(String fx5) {
        this.fx5 = fx5;
    }

    public String getFx6() {
        return fx6;
    }

    public void setFx6(String fx6) {
        this.fx6 = fx6;
    }

    public String getFx7() {
        return fx7;
    }

    public void setFx7(String fx7) {
        this.fx7 = fx7;
    }

    public String getFx8() {
        return fx8;
    }

    public void setFx8(String fx8) {
        this.fx8 = fx8;
    }

    public String getFx9() {
        return fx9;
    }

    public void setFx9(String fx9) {
        this.fx9 = fx9;
    }

    public String getFx10() {
        return fx10;
    }

    public void setFx10(String fx10) {
        this.fx10 = fx10;
    }

    public String getFx11() {
        return fx11;
    }

    public void setFx11(String fx11) {
        this.fx11 = fx11;
    }

    public String getFx12() {
        return fx12;
    }

    public void setFx12(String fx12) {
        this.fx12 = fx12;
    }

    public String getFx13() {
        return fx13;
    }

    public void setFx13(String fx13) {
        this.fx13 = fx13;
    }

    public String getFx14() {
        return fx14;
    }

    public void setFx14(String fx14) {
        this.fx14 = fx14;
    }

    public String getFx15() {
        return fx15;
    }

    public void setFx15(String fx15) {
        this.fx15 = fx15;
    }

    public String getFx16() {
        return fx16;
    }

    public void setFx16(String fx16) {
        this.fx16 = fx16;
    }

    public String getFx17() {
        return fx17;
    }

    public void setFx17(String fx17) {
        this.fx17 = fx17;
    }

    public String getFx18() {
        return fx18;
    }

    public void setFx18(String fx18) {
        this.fx18 = fx18;
    }

    public String getFx19() {
        return fx19;
    }

    public void setFx19(String fx19) {
        this.fx19 = fx19;
    }

    public String getFx20() {
        return fx20;
    }

    public void setFx20(String fx20) {
        this.fx20 = fx20;
    }

    public PurchaseOrderDetail getPurchaseOrderDetail() {
        return purchaseOrderDetail;
    }

    public void setPurchaseOrderDetail(PurchaseOrderDetail purchaseOrderDetail) {
        this.purchaseOrderDetail = purchaseOrderDetail;
    }

    public DeclarationForm getDeclarationForm() {
        return declarationForm;
    }

    public void setDeclarationForm(DeclarationForm declarationForm) {
        this.declarationForm = declarationForm;
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
        if (!(object instanceof PurchaseOrderDeclaration)) {
            return false;
        }
        PurchaseOrderDeclaration other = (PurchaseOrderDeclaration) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.PurchaseOrderDeclaration[id=" + id + "]";
    }

}