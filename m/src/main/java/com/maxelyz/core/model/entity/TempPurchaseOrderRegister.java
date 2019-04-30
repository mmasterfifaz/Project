/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
 * @author Oat
 */
@Entity
@Table(name = "temp_purchase_order_register")
@NamedQueries({
    @NamedQuery(name = "TempPurchaseOrderRegister.findAll", query = "SELECT t FROM TempPurchaseOrderRegister t")})
public class TempPurchaseOrderRegister implements Serializable {
    @JoinColumn(name = "occupation_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Occupation occupation;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "no")
    private int no;
    @Column(name = "initial")
    private String initial;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "idcard_type_id")
    private Integer idcardTypeId;
    @Column(name = "idno")
    private String idno;
    @Column(name = "gender")
    private String gender;
    @Column(name = "dob")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dob;
    @Column(name = "weight")
    private Double weight;
    @Column(name = "height")
    private Double height;
    @Column(name = "marital_status")
    private String maritalStatus;
    @Column(name = "nationality")
    private String nationality;
    @Column(name = "jobdescription")
    private String jobDescription;
    @Column(name = "home_phone")
    private String homePhone;
    @Column(name = "office_phone")
    private String officePhone;
    @Column(name = "mobile_phone")
    private String mobilePhone;
    @Column(name = "email")
    private String email;
    @Column(name = "current_fullname")
    private String currentFullname;
    @Column(name = "current_address_line1")
    private String currentAddressLine1;
    @Column(name = "current_address_line2")
    private String currentAddressLine2;
    @Column(name = "current_postal_code")
    private String currentPostalCode;
    @Column(name = "home_fullname")
    private String homeFullname;
    @Column(name = "home_address_line1")
    private String homeAddressLine1;
    @Column(name = "home_address_line2")
    private String homeAddressLine2;
    @Column(name = "home_postal_code")
    private String homePostalCode;
    @Column(name = "billing_fullname")
    private String billingFullname;
    @Column(name = "billing_address_line1")
    private String billingAddressLine1;
    @Column(name = "billing_address_line2")
    private String billingAddressLine2;
    @Column(name = "billing_postal_code")
    private String billingPostalCode;
    @Column(name = "shipping_fullname")
    private String shippingFullname;
    @Column(name = "shipping_address_line1")
    private String shippingAddressLine1;
    @Column(name = "shipping_address_line2")
    private String shippingAddressLine2;
    @Column(name = "shipping_postal_code")
    private String shippingPostalCode;
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

    @JoinColumn(name = "purchase_order_detail_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TempPurchaseOrderDetail tempPurchaseOrderDetail;
    @JoinColumn(name = "shipping_district_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private District shippingDistrict;
    @JoinColumn(name = "billing_district_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private District billingDistrict;
    @JoinColumn(name = "home_district_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private District homeDistrict;
    @JoinColumn(name = "current_district_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private District currentDistrict;

    public TempPurchaseOrderRegister() {
    }

    public TempPurchaseOrderRegister(Long id) {
        this.id = id;
    }

    public TempPurchaseOrderRegister(Long id, int no) {
        this.id = id;
        this.no = no;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Integer getIdcardTypeId() {
        return idcardTypeId;
    }

    public void setIdcardTypeId(Integer idcardTypeId) {
        this.idcardTypeId = idcardTypeId;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCurrentFullname() {
        return currentFullname;
    }

    public void setCurrentFullname(String currentFullname) {
        this.currentFullname = currentFullname;
    }

    public String getCurrentAddressLine1() {
        return currentAddressLine1;
    }

    public void setCurrentAddressLine1(String currentAddressLine1) {
        this.currentAddressLine1 = currentAddressLine1;
    }

    public String getCurrentAddressLine2() {
        return currentAddressLine2;
    }

    public void setCurrentAddressLine2(String currentAddressLine2) {
        this.currentAddressLine2 = currentAddressLine2;
    }

    public String getCurrentPostalCode() {
        return currentPostalCode;
    }

    public void setCurrentPostalCode(String currentPostalCode) {
        this.currentPostalCode = currentPostalCode;
    }

    public String getHomeFullname() {
        return homeFullname;
    }

    public void setHomeFullname(String homeFullname) {
        this.homeFullname = homeFullname;
    }

    public String getHomeAddressLine1() {
        return homeAddressLine1;
    }

    public void setHomeAddressLine1(String homeAddressLine1) {
        this.homeAddressLine1 = homeAddressLine1;
    }

    public String getHomeAddressLine2() {
        return homeAddressLine2;
    }

    public void setHomeAddressLine2(String homeAddressLine2) {
        this.homeAddressLine2 = homeAddressLine2;
    }

    public String getHomePostalCode() {
        return homePostalCode;
    }

    public void setHomePostalCode(String homePostalCode) {
        this.homePostalCode = homePostalCode;
    }

    public String getBillingFullname() {
        return billingFullname;
    }

    public void setBillingFullname(String billingFullname) {
        this.billingFullname = billingFullname;
    }

    public String getBillingAddressLine1() {
        return billingAddressLine1;
    }

    public void setBillingAddressLine1(String billingAddressLine1) {
        this.billingAddressLine1 = billingAddressLine1;
    }

    public String getBillingAddressLine2() {
        return billingAddressLine2;
    }

    public void setBillingAddressLine2(String billingAddressLine2) {
        this.billingAddressLine2 = billingAddressLine2;
    }

    public String getBillingPostalCode() {
        return billingPostalCode;
    }

    public void setBillingPostalCode(String billingPostalCode) {
        this.billingPostalCode = billingPostalCode;
    }

    public String getShippingFullname() {
        return shippingFullname;
    }

    public void setShippingFullname(String shippingFullname) {
        this.shippingFullname = shippingFullname;
    }

    public String getShippingAddressLine1() {
        return shippingAddressLine1;
    }

    public void setShippingAddressLine1(String shippingAddressLine1) {
        this.shippingAddressLine1 = shippingAddressLine1;
    }

    public String getShippingAddressLine2() {
        return shippingAddressLine2;
    }

    public void setShippingAddressLine2(String shippingAddressLine2) {
        this.shippingAddressLine2 = shippingAddressLine2;
    }

    public String getShippingPostalCode() {
        return shippingPostalCode;
    }

    public void setShippingPostalCode(String shippingPostalCode) {
        this.shippingPostalCode = shippingPostalCode;
    }

    public TempPurchaseOrderDetail getTempPurchaseOrderDetail() {
        return tempPurchaseOrderDetail;
    }

    public void setTempPurchaseOrderDetail(TempPurchaseOrderDetail tempPurchaseOrderDetail) {
        this.tempPurchaseOrderDetail = tempPurchaseOrderDetail;
    }

    public District getBillingDistrict() {
        return billingDistrict;
    }

    public void setBillingDistrict(District billingDistrict) {
        this.billingDistrict = billingDistrict;
    }

    public District getCurrentDistrict() {
        return currentDistrict;
    }

    public void setCurrentDistrict(District currentDistrict) {
        this.currentDistrict = currentDistrict;
    }

    public District getHomeDistrict() {
        return homeDistrict;
    }

    public void setHomeDistrict(District homeDistrict) {
        this.homeDistrict = homeDistrict;
    }

    public District getShippingDistrict() {
        return shippingDistrict;
    }

    public void setShippingDistrict(District shippingDistrict) {
        this.shippingDistrict = shippingDistrict;
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



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TempPurchaseOrderRegister)) {
            return false;
        }
        TempPurchaseOrderRegister other = (TempPurchaseOrderRegister) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.TempPurchaseOrderRegister[id=" + id + "]";
    }

    public Occupation getOccupation() {
        return occupation;
    }

    public void setOccupation(Occupation occupation) {
        this.occupation = occupation;
    }

}
