/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
@Table(name = "purchase_order_register")
@NamedQueries({
    @NamedQuery(name = "PurchaseOrderRegister.findAll", query = "SELECT p FROM PurchaseOrderRegister p")})
public class PurchaseOrderRegister implements Serializable {

    @JoinColumn(name = "occupation_id", referencedColumnName = "id")
    @ManyToOne
    private Occupation occupation;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "no")
    private int no;
    @Column(name = "initial")
    private String initial;
    @Column(name = "initial_text")
    private String initialLabel;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "idcard_type_id")
    private Integer idcardTypeId;
    @Column(name = "idno")
    private String idno;
    @Column(name = "idcard_issue_district")
    private String idCardIssueDistrict;
    @Column(name = "idcard_issue_province")
    private String idCardIssueProvince;
    @Column(name = "idcard_expiry_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date idcardExpiryDate;
    @Column(name = "gender")
    private String gender;
    @Column(name = "dob")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dob;
    @Column(name = "age")
    private Integer age;
    @Column(name = "weight")
    private Double weight;
    @Column(name = "height")
    private Double height;
    @Column(name = "marital_status")
    private String maritalStatus;
    @Column(name = "nationality")
    private String nationality;
    @Column(name = "nationality_text")
    private String nationalityLabel;
    @Column(name = "race")
    private String race;
    @Column(name = "race_text")
    private String raceLabel;
    @Column(name = "religion")
    private String religion;
    @Column(name = "religion_text")
    private String religionLabel;
    @Column(name = "jobdescription")
    private String jobDescription;
    @Column(name = "position")
    private String position;
    @Column(name = "occupation")
    private String occupationText;
    @Column(name = "occupation_text")
    private String occupationLabel;
    @Column(name = "business_type")
    private String businessType;
    @Column(name = "income")
    private String income;
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
    @Column(name = "current_address_line3")
    private String currentAddressLine3;
    @Column(name = "current_address_line4")
    private String currentAddressLine4;
    @Column(name = "current_address_line5")
    private String currentAddressLine5;
    @Column(name = "current_address_line6")
    private String currentAddressLine6;
    @Column(name = "current_address_line7")
    private String currentAddressLine7;
    @Column(name = "current_address_line8")
    private String currentAddressLine8;
    @Column(name = "current_postal_code")
    private String currentPostalCode;
    @Column(name = "current_telephone1")
    private String currentTelephone1;
    @Column(name = "current_telephone2")
    private String currentTelephone2;
    @Column(name = "home_fullname")
    private String homeFullname;
    @Column(name = "home_address_line1")
    private String homeAddressLine1;
    @Column(name = "home_address_line2")
    private String homeAddressLine2;
    @Column(name = "home_address_line3")
    private String homeAddressLine3;
    @Column(name = "home_address_line4")
    private String homeAddressLine4;
    @Column(name = "home_address_line5")
    private String homeAddressLine5;
    @Column(name = "home_address_line6")
    private String homeAddressLine6;
    @Column(name = "home_address_line7")
    private String homeAddressLine7;
    @Column(name = "home_address_line8")
    private String homeAddressLine8;
    @Column(name = "home_postal_code")
    private String homePostalCode;
    @Column(name = "home_telephone1")
    private String homeTelephone1;
    @Column(name = "home_telephone2")
    private String homeTelephone2;
    @Column(name = "billing_fullname")
    private String billingFullname;
    @Column(name = "billing_address_line1")
    private String billingAddressLine1;
    @Column(name = "billing_address_line2")
    private String billingAddressLine2;
    @Column(name = "billing_address_line3")
    private String billingAddressLine3;
    @Column(name = "billing_address_line4")
    private String billingAddressLine4;
    @Column(name = "billing_address_line5")
    private String billingAddressLine5;
    @Column(name = "billing_address_line6")
    private String billingAddressLine6;
    @Column(name = "billing_address_line7")
    private String billingAddressLine7;
    @Column(name = "billing_address_line8")
    private String billingAddressLine8;
    @Column(name = "billing_postal_code")
    private String billingPostalCode;
    @Column(name = "billing_telephone1")
    private String billingTelephone1;
    @Column(name = "billing_telephone2")
    private String billingTelephone2;
    @Column(name = "shipping_fullname")
    private String shippingFullname;
    @Column(name = "shipping_address_line1")
    private String shippingAddressLine1;
    @Column(name = "shipping_address_line2")
    private String shippingAddressLine2;
    @Column(name = "shipping_address_line3")
    private String shippingAddressLine3;
    @Column(name = "shipping_address_line4")
    private String shippingAddressLine4;
    @Column(name = "shipping_address_line5")
    private String shippingAddressLine5;
    @Column(name = "shipping_address_line6")
    private String shippingAddressLine6;
    @Column(name = "shipping_address_line7")
    private String shippingAddressLine7;
    @Column(name = "shipping_address_line8")
    private String shippingAddressLine8;
    @Column(name = "shipping_postal_code")
    private String shippingPostalCode;
    @Column(name = "shipping_telephone1")
    private String shippingTelephone1;
    @Column(name = "shipping_telephone2")
    private String shippingTelephone2;
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
    @Column(name = "fx21")
    private String fx21;
    @Column(name = "fx22")
    private String fx22;
    @Column(name = "fx23")
    private String fx23;
    @Column(name = "fx24")
    private String fx24;
    @Column(name = "fx25")
    private String fx25;
    @Column(name = "fx26")
    private String fx26;
    @Column(name = "fx27")
    private String fx27;
    @Column(name = "fx28")
    private String fx28;
    @Column(name = "fx29")
    private String fx29;
    @Column(name = "fx30")
    private String fx30;
    @Column(name = "fx31")
    private String fx31;
    @Column(name = "fx32")
    private String fx32;
    @Column(name = "fx33")
    private String fx33;
    @Column(name = "fx34")
    private String fx34;
    @Column(name = "fx35")
    private String fx35;
    @Column(name = "fx36")
    private String fx36;
    @Column(name = "fx37")
    private String fx37;
    @Column(name = "fx38")
    private String fx38;
    @Column(name = "fx39")
    private String fx39;
    @Column(name = "fx40")
    private String fx40;
    @Column(name = "fx41")
    private String fx41;
    @Column(name = "fx42")
    private String fx42;
    @Column(name = "fx43")
    private String fx43;
    @Column(name = "fx44")
    private String fx44;
    @Column(name = "fx45")
    private String fx45;
    @Column(name = "fx46")
    private String fx46;
    @Column(name = "fx47")
    private String fx47;
    @Column(name = "fx48")
    private String fx48;
    @Column(name = "fx49")
    private String fx49;
    @Column(name = "fx50")
    private String fx50;
    @JoinColumn(name = "purchase_order_detail_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private PurchaseOrderDetail purchaseOrderDetail;

    @JoinColumn(name = "shipping_district_id", referencedColumnName = "id")
    @ManyToOne
    private District shippingDistrict;

    @JoinColumn(name = "billing_district_id", referencedColumnName = "id")
    @ManyToOne
    private District billingDistrict;

    @JoinColumn(name = "home_district_id", referencedColumnName = "id")
    @ManyToOne
    private District homeDistrict;

    @JoinColumn(name = "current_district_id", referencedColumnName = "id")
    @ManyToOne
    private District currentDistrict;

    @JoinColumn(name = "shipping_subdistrict_id", referencedColumnName = "id")
    @ManyToOne
    private Subdistrict shippingSubDistrict;

    @JoinColumn(name = "billing_subdistrict_id", referencedColumnName = "id")
    @ManyToOne
    private Subdistrict billingSubDistrict;

    @JoinColumn(name = "home_subdistrict_id", referencedColumnName = "id")
    @ManyToOne
    private Subdistrict homeSubDistrict;

    @JoinColumn(name = "current_subdistrict_id", referencedColumnName = "id")
    @ManyToOne
    private Subdistrict currentSubDistrict;

    @Column(name = "relation_main_insured")
    private String relationMainInsured;

    public String getDobThai() {
        String ret;

        try {
            if (dob != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("th", "th"));
                ret = sdf.format(dob);
            } else {
                ret = null;
            }

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            ret = null;
        }

        return ret;
    }

    public PurchaseOrderRegister() {
    }

    public PurchaseOrderRegister(Long id) {
        this.id = id;
    }

    public PurchaseOrderRegister(Long id, int no) {
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

    public String getIdCardIssueDistrict() {
        return idCardIssueDistrict;
    }

    public void setIdCardIssueDistrict(String idCardIssueDistrict) {
        this.idCardIssueDistrict = idCardIssueDistrict;
    }

    public String getIdCardIssueProvince() {
        return idCardIssueProvince;
    }

    public void setIdCardIssueProvince(String idCardIssueProvince) {
        this.idCardIssueProvince = idCardIssueProvince;
    }

    public Date getIdcardExpiryDate() {
        return idcardExpiryDate;
    }

    public void setIdcardExpiryDate(Date idcardExpiryDate) {
        this.idcardExpiryDate = idcardExpiryDate;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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

    public String getOccupationText() {
        return occupationText;
    }

    public void setOccupationText(String occupationText) {
        this.occupationText = occupationText;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
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

    public String getCurrentAddressLine3() {
        return currentAddressLine3;
    }

    public void setCurrentAddressLine3(String currentAddressLine3) {
        this.currentAddressLine3 = currentAddressLine3;
    }

    public String getCurrentAddressLine4() {
        return currentAddressLine4;
    }

    public void setCurrentAddressLine4(String currentAddressLine4) {
        this.currentAddressLine4 = currentAddressLine4;
    }

    public String getCurrentAddressLine5() {
        return currentAddressLine5;
    }

    public void setCurrentAddressLine5(String currentAddressLine5) {
        this.currentAddressLine5 = currentAddressLine5;
    }

    public String getCurrentAddressLine6() {
        return currentAddressLine6;
    }

    public void setCurrentAddressLine6(String currentAddressLine6) {
        this.currentAddressLine6 = currentAddressLine6;
    }

    public String getCurrentAddressLine7() {
        return currentAddressLine7;
    }

    public void setCurrentAddressLine7(String currentAddressLine7) {
        this.currentAddressLine7 = currentAddressLine7;
    }

    public String getCurrentAddressLine8() {
        return currentAddressLine8;
    }

    public void setCurrentAddressLine8(String currentAddressLine8) {
        this.currentAddressLine8 = currentAddressLine8;
    }

    public String getCurrentTelephone1() {
        return currentTelephone1;
    }

    public void setCurrentTelephone1(String currentTelephone1) {
        this.currentTelephone1 = currentTelephone1;
    }

    public String getCurrentTelephone2() {
        return currentTelephone2;
    }

    public void setCurrentTelephone2(String currentTelephone2) {
        this.currentTelephone2 = currentTelephone2;
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

    public String getHomeAddressLine3() {
        return homeAddressLine3;
    }

    public void setHomeAddressLine3(String homeAddressLine3) {
        this.homeAddressLine3 = homeAddressLine3;
    }

    public String getHomeAddressLine4() {
        return homeAddressLine4;
    }

    public void setHomeAddressLine4(String homeAddressLine4) {
        this.homeAddressLine4 = homeAddressLine4;
    }

    public String getHomeAddressLine5() {
        return homeAddressLine5;
    }

    public void setHomeAddressLine5(String homeAddressLine5) {
        this.homeAddressLine5 = homeAddressLine5;
    }

    public String getHomeAddressLine6() {
        return homeAddressLine6;
    }

    public void setHomeAddressLine6(String homeAddressLine6) {
        this.homeAddressLine6 = homeAddressLine6;
    }

    public String getHomeAddressLine7() {
        return homeAddressLine7;
    }

    public void setHomeAddressLine7(String homeAddressLine7) {
        this.homeAddressLine7 = homeAddressLine7;
    }

    public String getHomeAddressLine8() {
        return homeAddressLine8;
    }

    public void setHomeAddressLine8(String homeAddressLine8) {
        this.homeAddressLine8 = homeAddressLine8;
    }

    public String getHomeTelephone1() {
        return homeTelephone1;
    }

    public void setHomeTelephone1(String homeTelephone1) {
        this.homeTelephone1 = homeTelephone1;
    }

    public String getHomeTelephone2() {
        return homeTelephone2;
    }

    public void setHomeTelephone2(String homeTelephone2) {
        this.homeTelephone2 = homeTelephone2;
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

    public String getBillingAddressLine3() {
        return billingAddressLine3;
    }

    public void setBillingAddressLine3(String billingAddressLine3) {
        this.billingAddressLine3 = billingAddressLine3;
    }

    public String getBillingAddressLine4() {
        return billingAddressLine4;
    }

    public void setBillingAddressLine4(String billingAddressLine4) {
        this.billingAddressLine4 = billingAddressLine4;
    }

    public String getBillingAddressLine5() {
        return billingAddressLine5;
    }

    public void setBillingAddressLine5(String billingAddressLine5) {
        this.billingAddressLine5 = billingAddressLine5;
    }

    public String getBillingAddressLine6() {
        return billingAddressLine6;
    }

    public void setBillingAddressLine6(String billingAddressLine6) {
        this.billingAddressLine6 = billingAddressLine6;
    }

    public String getBillingAddressLine7() {
        return billingAddressLine7;
    }

    public void setBillingAddressLine7(String billingAddressLine7) {
        this.billingAddressLine7 = billingAddressLine7;
    }

    public String getBillingAddressLine8() {
        return billingAddressLine8;
    }

    public void setBillingAddressLine8(String billingAddressLine8) {
        this.billingAddressLine8 = billingAddressLine8;
    }

    public String getBillingTelephone1() {
        return billingTelephone1;
    }

    public void setBillingTelephone1(String billingTelephone1) {
        this.billingTelephone1 = billingTelephone1;
    }

    public String getBillingTelephone2() {
        return billingTelephone2;
    }

    public void setBillingTelephone2(String billingTelephone2) {
        this.billingTelephone2 = billingTelephone2;
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

    public String getShippingAddressLine3() {
        return shippingAddressLine3;
    }

    public void setShippingAddressLine3(String shippingAddressLine3) {
        this.shippingAddressLine3 = shippingAddressLine3;
    }

    public String getShippingAddressLine4() {
        return shippingAddressLine4;
    }

    public void setShippingAddressLine4(String shippingAddressLine4) {
        this.shippingAddressLine4 = shippingAddressLine4;
    }

    public String getShippingAddressLine5() {
        return shippingAddressLine5;
    }

    public void setShippingAddressLine5(String shippingAddressLine5) {
        this.shippingAddressLine5 = shippingAddressLine5;
    }

    public String getShippingAddressLine6() {
        return shippingAddressLine6;
    }

    public void setShippingAddressLine6(String shippingAddressLine6) {
        this.shippingAddressLine6 = shippingAddressLine6;
    }

    public String getShippingAddressLine7() {
        return shippingAddressLine7;
    }

    public void setShippingAddressLine7(String shippingAddressLine7) {
        this.shippingAddressLine7 = shippingAddressLine7;
    }

    public String getShippingAddressLine8() {
        return shippingAddressLine8;
    }

    public void setShippingAddressLine8(String shippingAddressLine8) {
        this.shippingAddressLine8 = shippingAddressLine8;
    }

    public String getShippingTelephone1() {
        return shippingTelephone1;
    }

    public void setShippingTelephone1(String shippingTelephone1) {
        this.shippingTelephone1 = shippingTelephone1;
    }

    public String getShippingTelephone2() {
        return shippingTelephone2;
    }

    public void setShippingTelephone2(String shippingTelephone2) {
        this.shippingTelephone2 = shippingTelephone2;
    }

    public String getShippingPostalCode() {
        return shippingPostalCode;
    }

    public void setShippingPostalCode(String shippingPostalCode) {
        this.shippingPostalCode = shippingPostalCode;
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

    public String getFx21() {
        return fx21;
    }

    public void setFx21(String fx21) {
        this.fx21 = fx21;
    }

    public String getFx22() {
        return fx22;
    }

    public void setFx22(String fx22) {
        this.fx22 = fx22;
    }

    public String getFx23() {
        return fx23;
    }

    public void setFx23(String fx23) {
        this.fx23 = fx23;
    }

    public String getFx24() {
        return fx24;
    }

    public void setFx24(String fx24) {
        this.fx24 = fx24;
    }

    public String getFx25() {
        return fx25;
    }

    public void setFx25(String fx25) {
        this.fx25 = fx25;
    }

    public String getFx26() {
        return fx26;
    }

    public void setFx26(String fx26) {
        this.fx26 = fx26;
    }

    public String getFx27() {
        return fx27;
    }

    public void setFx27(String fx27) {
        this.fx27 = fx27;
    }

    public String getFx28() {
        return fx28;
    }

    public void setFx28(String fx28) {
        this.fx28 = fx28;
    }

    public String getFx29() {
        return fx29;
    }

    public void setFx29(String fx29) {
        this.fx29 = fx29;
    }

    public String getFx30() {
        return fx30;
    }

    public void setFx30(String fx30) {
        this.fx30 = fx30;
    }

    public String getFx31() {
        return fx31;
    }

    public void setFx31(String fx31) {
        this.fx31 = fx31;
    }

    public String getFx32() {
        return fx32;
    }

    public void setFx32(String fx32) {
        this.fx32 = fx32;
    }

    public String getFx33() {
        return fx33;
    }

    public void setFx33(String fx33) {
        this.fx33 = fx33;
    }

    public String getFx34() {
        return fx34;
    }

    public void setFx34(String fx34) {
        this.fx34 = fx34;
    }

    public String getFx35() {
        return fx35;
    }

    public void setFx35(String fx35) {
        this.fx35 = fx35;
    }

    public String getFx36() {
        return fx36;
    }

    public void setFx36(String fx36) {
        this.fx36 = fx36;
    }

    public String getFx37() {
        return fx37;
    }

    public void setFx37(String fx37) {
        this.fx37 = fx37;
    }

    public String getFx38() {
        return fx38;
    }

    public void setFx38(String fx38) {
        this.fx38 = fx38;
    }

    public String getFx39() {
        return fx39;
    }

    public void setFx39(String fx39) {
        this.fx39 = fx39;
    }

    public String getFx40() {
        return fx40;
    }

    public void setFx40(String fx40) {
        this.fx40 = fx40;
    }

    public String getFx41() {
        return fx41;
    }

    public void setFx41(String fx41) {
        this.fx41 = fx41;
    }

    public String getFx42() {
        return fx42;
    }

    public void setFx42(String fx42) {
        this.fx42 = fx42;
    }

    public String getFx43() {
        return fx43;
    }

    public void setFx43(String fx43) {
        this.fx43 = fx43;
    }

    public String getFx44() {
        return fx44;
    }

    public void setFx44(String fx44) {
        this.fx44 = fx44;
    }

    public String getFx45() {
        return fx45;
    }

    public void setFx45(String fx45) {
        this.fx45 = fx45;
    }

    public String getFx46() {
        return fx46;
    }

    public void setFx46(String fx46) {
        this.fx46 = fx46;
    }

    public String getFx47() {
        return fx47;
    }

    public void setFx47(String fx47) {
        this.fx47 = fx47;
    }

    public String getFx48() {
        return fx48;
    }

    public void setFx48(String fx48) {
        this.fx48 = fx48;
    }

    public String getFx49() {
        return fx49;
    }

    public void setFx49(String fx49) {
        this.fx49 = fx49;
    }

    public String getFx50() {
        return fx50;
    }

    public void setFx50(String fx50) {
        this.fx50 = fx50;
    }

    public PurchaseOrderDetail getPurchaseOrderDetail() {
        return purchaseOrderDetail;
    }

    public void setPurchaseOrderDetail(PurchaseOrderDetail purchaseOrderDetail) {
        this.purchaseOrderDetail = purchaseOrderDetail;
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

    public String getInitialLabel() {
        return initialLabel;
    }

    public void setInitialLabel(String initialLabel) {
        this.initialLabel = initialLabel;
    }

    public String getOccupationLabel() {
        return occupationLabel;
    }

    public void setOccupationLabel(String occupationLabel) {
        this.occupationLabel = occupationLabel;
    }

    public String getNationalityLabel() {
        return nationalityLabel;
    }

    public void setNationalityLabel(String nationalityLabel) {
        this.nationalityLabel = nationalityLabel;
    }

    public String getRaceLabel() {
        return raceLabel;
    }

    public void setRaceLabel(String raceLabel) {
        this.raceLabel = raceLabel;
    }

    public String getReligionLabel() {
        return religionLabel;
    }

    public void setReligionLabel(String religionLabel) {
        this.religionLabel = religionLabel;
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
        if (!(object instanceof PurchaseOrderRegister)) {
            return false;
        }
        PurchaseOrderRegister other = (PurchaseOrderRegister) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.PurchaseOrderRegister[id=" + id + "]";
    }

    public Occupation getOccupation() {
        return occupation;
    }

    public void setOccupation(Occupation occupation) {
        this.occupation = occupation;
    }

    public Subdistrict getBillingSubDistrict() {
        return billingSubDistrict;
    }

    public void setBillingSubDistrict(Subdistrict billingSubDistrict) {
        this.billingSubDistrict = billingSubDistrict;
    }

    public Subdistrict getCurrentSubDistrict() {
        return currentSubDistrict;
    }

    public void setCurrentSubDistrict(Subdistrict currentSubDistrict) {
        this.currentSubDistrict = currentSubDistrict;
    }

    public Subdistrict getHomeSubDistrict() {
        return homeSubDistrict;
    }

    public void setHomeSubDistrict(Subdistrict homeSubDistrict) {
        this.homeSubDistrict = homeSubDistrict;
    }

    public Subdistrict getShippingSubDistrict() {
        return shippingSubDistrict;
    }

    public void setShippingSubDistrict(Subdistrict shippingSubDistrict) {
        this.shippingSubDistrict = shippingSubDistrict;
    }

    public String getRelationMainInsured() {
        return relationMainInsured;
    }

    public void setRelationMainInsured(String relationMainInsured) {
        this.relationMainInsured = relationMainInsured;
    }


    

}
