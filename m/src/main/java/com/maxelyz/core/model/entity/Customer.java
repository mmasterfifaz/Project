/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Oat
 */
@Entity
@Table(name = "customer")
@NamedQueries({@NamedQuery(name = "Customer.findAll", query = "SELECT c FROM Customer c")})
public class Customer implements Serializable {
    @OneToMany(mappedBy = "customer")
    private Collection<CampaignCustomer> campaignCustomerCollection;
    @Column(name =     "dob")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dob;
    @Column(name = "weight")
    private Double weight;
    @Column(name = "height")
    private Double height;
    @Column(name = "current_country_name", length = 200)
    private String currentCountryName;
    @Column(name = "home_country_name", length = 200)
    private String homeCountryName;
    @Column(name = "billing_country_name", length = 200)
    private String billingCountryName;
    @Column(name = "shipping_country_name", length = 200)
    private String shippingCountryName;
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(name = "telephone_default", length = 100)
    private String telephoneDefault;
    @Column(name = "home_phone_close")
    private Boolean homePhoneClose;
    @Column(name = "office_phone_close")
    private Boolean officePhoneClose;
    @Column(name = "mobile_phone_close")
    private Boolean mobilePhoneClose;
    @Column(name = "contact_phone1_close")
    private Boolean contactPhone1Close;
    @Column(name = "contact_phone2_close")
    private Boolean contactPhone2Close;
    @Column(name = "contact_phone3_close")
    private Boolean contactPhone3Close;
    @Column(name = "contact_phone4_close")
    private Boolean contactPhone4Close;
    @Column(name = "contact_phone5_close")
    private Boolean contactPhone5Close;    
    
    @Column(name = "home_phone_reason_id")
    private Integer homePhoneReasonId;
    @Column(name = "office_phone_reason_id")
    private Integer officePhoneReasonId;
    @Column(name = "mobile_phone_reason_id")
    private Integer mobilePhoneReasonId;
    @Column(name = "contact_phone1_reason_id")
    private Integer contactPhone1ReasonId;
    @Column(name = "contact_phone2_reason_id")
    private Integer contactPhone2ReasonId;
    @Column(name = "contact_phone3_reason_id")
    private Integer contactPhone3ReasonId;
    @Column(name = "contact_phone4_reason_id")
    private Integer contactPhone4ReasonId;
    @Column(name = "contact_phone5_reason_id")
    private Integer contactPhone5ReasonId;
    
    @Column(name = "home_phone_reason", length = 100)
    private String homePhoneReason;
    @Column(name = "office_phone_reason", length = 100)
    private String officePhoneReason;
    @Column(name = "mobile_phone_reason", length = 100)
    private String mobilePhoneReason;
    @Column(name = "contact_phone1_reason", length = 100)
    private String contactPhone1Reason;
    @Column(name = "contact_phone2_reason", length = 100)
    private String contactPhone2Reason;
    @Column(name = "contact_phone3_reason", length = 100)
    private String contactPhone3Reason;
    @Column(name = "contact_phone4_reason", length = 100)
    private String contactPhone4Reason;
    @Column(name = "contact_phone5_reason", length = 100)
    private String contactPhone5Reason;
    @Column(name = "current_address_line3", length = 200)
    private String currentAddressLine3;
    @Column(name = "current_address_line4", length = 200)
    private String currentAddressLine4;
    @Column(name = "current_address_line5", length = 200)
    private String currentAddressLine5;
    @Column(name = "current_address_line6", length = 200)
    private String currentAddressLine6;
    @Column(name = "current_address_line7", length = 200)
    private String currentAddressLine7;
    @Column(name = "current_address_line8", length = 200)
    private String currentAddressLine8;
    @Column(name = "current_district_name", length = 200)
    private String currentDistrictName;
    @Column(name = "current_province_name", length = 200)
    private String currentProvinceName;
    @Column(name = "home_address_line3", length = 200)
    private String homeAddressLine3;
    @Column(name = "home_address_line4", length = 200)
    private String homeAddressLine4;
    @Column(name = "home_address_line5", length = 200)
    private String homeAddressLine5;
    @Column(name = "home_address_line6", length = 200)
    private String homeAddressLine6;
    @Column(name = "home_address_line7", length = 200)
    private String homeAddressLine7;
    @Column(name = "home_address_line8", length = 200)
    private String homeAddressLine8;
    @Column(name = "home_district_name", length = 200)
    private String homeDistrictName;
    @Column(name = "home_province_name", length = 200)
    private String homeProvinceName;
    @Column(name = "billing_address_line3", length = 200)
    private String billingAddressLine3;
    @Column(name = "billing_address_line4", length = 200)
    private String billingAddressLine4;
    @Column(name = "billing_address_line5", length = 200)
    private String billingAddressLine5;
    @Column(name = "billing_address_line6", length = 200)
    private String billingAddressLine6;
    @Column(name = "billing_address_line7", length = 200)
    private String billingAddressLine7;
    @Column(name = "billing_address_line8", length = 200)
    private String billingAddressLine8;
    @Column(name = "billing_district_name", length = 200)
    private String billingDistrictName;
    @Column(name = "billing_province_name", length = 200)
    private String billingProvinceName;
    @Column(name = "shipping_address_line3", length = 200)
    private String shippingAddressLine3;
    @Column(name = "shipping_address_line4", length = 200)
    private String shippingAddressLine4;
    @Column(name = "shipping_address_line5", length = 200)
    private String shippingAddressLine5;
    @Column(name = "shipping_address_line6", length = 200)
    private String shippingAddressLine6;
    @Column(name = "shipping_address_line7", length = 200)
    private String shippingAddressLine7;
    @Column(name = "shipping_address_line8", length = 200)
    private String shippingAddressLine8;
    @Column(name = "shipping_district_name", length = 200)
    private String shippingDistrictName;
    @Column(name = "shipping_province_name", length = 200)
    private String shippingProvinceName;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue
    private Integer id;
    @Column(name = "reference_no")
    private String referenceNo;
    @Column(name = "initial")
    private String initial;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "idno")
    private String idno;
    @Column(name = "gender")
    private String gender;
    @Column(name = "nationality")
    private String nationality;
    @Column(name = "occupation")
    private String occupation;
    @Column(name = "home_phone")
    private String homePhone;
    @Column(name = "home_ext")
    private String homeExt;
    @Column(name = "office_phone")
    private String officePhone;
    @Column(name = "office_ext")
    private String officeExt;
    @Column(name = "mobile_phone")
    private String mobilePhone;
    @Column(name = "fax")
    private String fax;
    @Column(name = "fax_ext")
    private String faxExt;
    @Column(name = "email")
    private String email;
    @Column(name = "privilege")
    private String privilege;
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
   // @Column(name = "home_district_id")
   // private Integer homeDistrictId;
    @Column(name = "home_postal_code")
    private String homePostalCode;
    @Column(name = "billing_fullname")
    private String billingFullname;
    @Column(name = "billing_address_line1")
    private String billingAddressLine1;
    @Column(name = "billing_address_line2")
    private String billingAddressLine2;
   // @Column(name = "billing_district_id")
   // private Integer billingDistrictId;
    @Column(name = "billing_postal_code")
    private String billingPostalCode;
    @Column(name = "shipping_fullname")
    private String shippingFullname;
    @Column(name = "shipping_address_line1")
    private String shippingAddressLine1;
    @Column(name = "shipping_address_line2")
    private String shippingAddressLine2;
  //  @Column(name = "shipping_district_id")
  // private Integer shippingDistrictId;
    @Column(name = "shipping_postal_code")
    private String shippingPostalCode;
    @Column(name = "contact_phone1")
    private String contactPhone1;
    @Column(name = "contact_phone2")
    private String contactPhone2;
    @Column(name = "contact_phone3")
    private String contactPhone3;
    @Column(name = "contact_phone4")
    private String contactPhone4;
    @Column(name = "contact_phone5")
    private String contactPhone5;
    @Column(name = "contact_ext1")
    private String contactExt1;
    @Column(name = "contact_ext2")
    private String contactExt2;
    @Column(name = "contact_ext3")
    private String contactExt3;
    @Column(name = "contact_ext4")
    private String contactExt4;
    @Column(name = "contact_ext5")
    private String contactExt5;
    @Column(name = "emoicon")
    private String emoicon;
    @Column(name = "picture")
    private String picture;
    @Lob
    @Column(name = "remark")
    private String remark;    
    @Column(name = "flexfield1")
    private String flexfield1;
    @Column(name = "flexfield2")
    private String flexfield2;
    @Column(name = "flexfield3")
    private String flexfield3;
    @Column(name = "flexfield4")
    private String flexfield4;
    @Column(name = "flexfield5")
    private String flexfield5;
    @Column(name = "flexfield6")
    private String flexfield6;
    @Column(name = "flexfield7")
    private String flexfield7;
    @Column(name = "flexfield8")
    private String flexfield8;
    @Column(name = "flexfield9")
    private String flexfield9;
    @Column(name = "flexfield10")
    private String flexfield10;
    @Column(name = "flexfield11")
    private String flexfield11;
    @Column(name = "flexfield12")
    private String flexfield12;
    @Column(name = "flexfield13")
    private String flexfield13;
    @Column(name = "flexfield14")
    private String flexfield14;
    @Column(name = "flexfield15")
    private String flexfield15;
    @Column(name = "flexfield16")
    private String flexfield16;
    @Column(name = "flexfield17")
    private String flexfield17;
    @Column(name = "flexfield18")
    private String flexfield18;
    @Column(name = "flexfield19")
    private String flexfield19;
    @Column(name = "flexfield20")
    private String flexfield20;
    @Column(name = "flexfield21")		    
    private String flexfield21;
    @Column(name = "flexfield22")		    
    private String flexfield22;
    @Column(name = "flexfield23")		    
    private String flexfield23;
    @Column(name = "flexfield24")		    
    private String flexfield24;
    @Column(name = "flexfield25")		    
    private String flexfield25;
    @Column(name = "flexfield26")		    
    private String flexfield26;
    @Column(name = "flexfield27")		    
    private String flexfield27;
    @Column(name = "flexfield28")		    
    private String flexfield28;
    @Column(name = "flexfield29")		    
    private String flexfield29;
    @Column(name = "flexfield30")		    
    private String flexfield30;
    @Column(name = "flexfield31")		    
    private String flexfield31;
    @Column(name = "flexfield32")		    
    private String flexfield32;
    @Column(name = "flexfield33")		    
    private String flexfield33;
    @Column(name = "flexfield34")		    
    private String flexfield34;
    @Column(name = "flexfield35")		    
    private String flexfield35;
    @Column(name = "flexfield36")		    
    private String flexfield36;
    @Column(name = "flexfield37")		    
    private String flexfield37;
    @Column(name = "flexfield38")		    
    private String flexfield38;
    @Column(name = "flexfield39")		    
    private String flexfield39;
    @Column(name = "flexfield40")		    
    private String flexfield40;
    @Column(name = "flexfield41")		    
    private String flexfield41;
    @Column(name = "flexfield42")		    
    private String flexfield42;
    @Column(name = "flexfield43")		    
    private String flexfield43;
    @Column(name = "flexfield44")		    
    private String flexfield44;
    @Column(name = "flexfield45")		    
    private String flexfield45;
    @Column(name = "flexfield46")		    
    private String flexfield46;
    @Column(name = "flexfield47")		    
    private String flexfield47;
    @Column(name = "flexfield48")		    
    private String flexfield48;
    @Column(name = "flexfield49")		    
    private String flexfield49;
    @Column(name = "flexfield50")		    
    private String flexfield50;
    @Column(name = "flexfield51")		    
    private String flexfield51;
    @Column(name = "flexfield52")		    
    private String flexfield52;
    @Column(name = "flexfield53")		    
    private String flexfield53;
    @Column(name = "flexfield54")		    
    private String flexfield54;
    @Column(name = "flexfield55")		    
    private String flexfield55;
    @Column(name = "flexfield56")		    
    private String flexfield56;
    @Column(name = "flexfield57")		    
    private String flexfield57;
    @Column(name = "flexfield58")		    
    private String flexfield58;
    @Column(name = "flexfield59")		    
    private String flexfield59;
    @Column(name = "flexfield60")		    
    private String flexfield60;
    @Column(name = "flexfield61")		    
    private String flexfield61;
    @Column(name = "flexfield62")		    
    private String flexfield62;
    @Column(name = "flexfield63")		    
    private String flexfield63;
    @Column(name = "flexfield64")		    
    private String flexfield64;
    @Column(name = "flexfield65")		    
    private String flexfield65;
    @Column(name = "flexfield66")		    
    private String flexfield66;
    @Column(name = "flexfield67")		    
    private String flexfield67;
    @Column(name = "flexfield68")		    
    private String flexfield68;
    @Column(name = "flexfield69")		    
    private String flexfield69;
    @Column(name = "flexfield70")		    
    private String flexfield70;
    @Column(name = "flexfield71")		    
    private String flexfield71;
    @Column(name = "flexfield72")		    
    private String flexfield72;
    @Column(name = "flexfield73")		    
    private String flexfield73;
    @Column(name = "flexfield74")		    
    private String flexfield74;
    @Column(name = "flexfield75")		    
    private String flexfield75;
    @Column(name = "flexfield76")		    
    private String flexfield76;
    @Column(name = "flexfield77")		    
    private String flexfield77;
    @Column(name = "flexfield78")		    
    private String flexfield78;
    @Column(name = "flexfield79")		    
    private String flexfield79;
    @Column(name = "flexfield80")		    
    private String flexfield80;
    @Column(name = "flexfield81")		    
    private String flexfield81;
    @Column(name = "flexfield82")		    
    private String flexfield82;
    @Column(name = "flexfield83")		    
    private String flexfield83;
    @Column(name = "flexfield84")		    
    private String flexfield84;
    @Column(name = "flexfield85")		    
    private String flexfield85;
    @Column(name = "flexfield86")		    
    private String flexfield86;
    @Column(name = "flexfield87")		    
    private String flexfield87;
    @Column(name = "flexfield88")		    
    private String flexfield88;
    @Column(name = "flexfield89")		    
    private String flexfield89;
    @Column(name = "flexfield90")		    
    private String flexfield90;
    @Column(name = "flexfield91")		    
    private String flexfield91;
    @Column(name = "flexfield92")		    
    private String flexfield92;
    @Column(name = "flexfield93")		    
    private String flexfield93;
    @Column(name = "flexfield94")		    
    private String flexfield94;
    @Column(name = "flexfield95")		    
    private String flexfield95;
    @Column(name = "flexfield96")		    
    private String flexfield96;
    @Column(name = "flexfield97")		    
    private String flexfield97;
    @Column(name = "flexfield98")		    
    private String flexfield98;
    @Column(name = "flexfield99")		    
    private String flexfield99;
    @Column(name = "flexfield100")		    
    private String flexfield100;
    @Column(name = "op_out")
    private Boolean opOut;
    @Column(name = "op_out_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date opOutDate;
    @Column(name = "create_by")
    private String createBy;
    @Column(name = "update_by")
    private String updateBy;
    @JoinTable(name = "customer_related", joinColumns = {@JoinColumn(name = "customer_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "related_customer_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Customer> customerCollection;
    @ManyToMany(mappedBy = "customerCollection")
    private Collection<Customer> customerCollection1;
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @ManyToOne
    private Account accountId;
    @JoinColumn(name = "customer_type", referencedColumnName = "id")
    @ManyToOne
    private CustomerType customerType;
    @JoinColumn(name = "current_district_id", referencedColumnName = "id")
    @ManyToOne
    private District currentDistrictId;
    @JoinColumn(name = "home_district_id", referencedColumnName = "id")
    @ManyToOne
    private District homeDistrictId;
    @JoinColumn(name = "billing_district_id", referencedColumnName = "id")
    @ManyToOne
    private District billingDistrictId;
    @JoinColumn(name = "shipping_district_id", referencedColumnName = "id")
    @ManyToOne
    private District shippingDistrictId;
    @JoinColumn(name = "idcard_type_id", referencedColumnName = "id")
    @ManyToOne
    private IdcardType idcardTypeId;

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
//    private Collection<PurchaseOrder> purchaseOrderCollection;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerId")
//    private Collection<AssignmentDetail> assignmentDetailCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    private Collection<MarketingCustomer> marketingCustomerCollection;
/*    @OneToOne(cascade = CascadeType.ALL, mappedBy = "customer")
    private PurchaseHistory purchaseHistory;*/
    @OneToMany(cascade = CascadeType.ALL, mappedBy= "customerId")
    private Collection<ContactCase> contactCaseCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    private Collection<TempPurchaseOrder> tempPurchaseOrderCollection;

        
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
    
    @Column(name = "car_brand")
    private String carBrand;
    @Column(name = "car_model")
    private String carModel;
    @Column(name = "car_year")
    private Integer carYear;
    @Column(name = "car_character_group")
    private String carCharacterGroup;
    @Column(name = "car_number")
    private String carNumber;
    @Column(name = "car_province")
    private String carProvince;
    @Column(name = "manual_insert")
    private Boolean manualInsert;
    @Column(name = "created_by_cs")
    private Boolean createdByCs;
    
    @Column(name = "parent_id")
    private Integer parentId;

    public Customer() {
    }

    public Customer(Integer id) {
        this.id = id;
    }

    public Customer(Integer id, String referenceNo) {
        this.id = id;
        this.referenceNo = referenceNo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
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

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getHomeExt() {
        return homeExt;
    }

    public void setHomeExt(String homeExt) {
        this.homeExt = homeExt;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getOfficeExt() {
        return officeExt;
    }

    public void setOfficeExt(String officeExt) {
        this.officeExt = officeExt;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getFaxExt() {
        return faxExt;
    }

    public void setFaxExt(String faxExt) {
        this.faxExt = faxExt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
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

    public District getHomeDistrictId() {
        return homeDistrictId;
    }

    public void setHomeDistrictId(District homeDistrictId) {
        this.homeDistrictId = homeDistrictId;
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

    public District getBillingDistrictId() {
        return billingDistrictId;
    }

    public void setBillingDistrictId(District billingDistrictId) {
        this.billingDistrictId = billingDistrictId;
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

    public District getShippingDistrictId() {
        return shippingDistrictId;
    }

    public void setShippingDistrictId(District shippingDistrictId) {
        this.shippingDistrictId = shippingDistrictId;
    }

    public String getShippingPostalCode() {
        return shippingPostalCode;
    }

    public void setShippingPostalCode(String shippingPostalCode) {
        this.shippingPostalCode = shippingPostalCode;
    }

    public String getContactPhone1() {
        return contactPhone1;
    }

    public void setContactPhone1(String contactPhone1) {
        this.contactPhone1 = contactPhone1;
    }

    public String getContactPhone2() {
        return contactPhone2;
    }

    public void setContactPhone2(String contactPhone2) {
        this.contactPhone2 = contactPhone2;
    }

    public String getContactPhone3() {
        return contactPhone3;
    }

    public void setContactPhone3(String contactPhone3) {
        this.contactPhone3 = contactPhone3;
    }

    public String getContactPhone4() {
        return contactPhone4;
    }

    public void setContactPhone4(String contactPhone4) {
        this.contactPhone4 = contactPhone4;
    }

    public String getContactPhone5() {
        return contactPhone5;
    }

    public void setContactPhone5(String contactPhone5) {
        this.contactPhone5 = contactPhone5;
    }

    public String getContactExt1() {
        return contactExt1;
    }

    public void setContactExt1(String contactExt1) {
        this.contactExt1 = contactExt1;
    }

    public String getContactExt2() {
        return contactExt2;
    }

    public void setContactExt2(String contactExt2) {
        this.contactExt2 = contactExt2;
    }

    public String getContactExt3() {
        return contactExt3;
    }

    public void setContactExt3(String contactExt3) {
        this.contactExt3 = contactExt3;
    }

    public String getContactExt4() {
        return contactExt4;
    }

    public void setContactExt4(String contactExt4) {
        this.contactExt4 = contactExt4;
    }

    public String getContactExt5() {
        return contactExt5;
    }

    public void setContactExt5(String contactExt5) {
        this.contactExt5 = contactExt5;
    }

    

    public String getEmoicon() {
        return emoicon;
    }

    public void setEmoicon(String emoicon) {
        this.emoicon = emoicon;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFlexfield1() {
        return flexfield1;
    }

    public void setFlexfield1(String flexfield1) {
        this.flexfield1 = flexfield1;
    }

    public String getFlexfield2() {
        return flexfield2;
    }

    public void setFlexfield2(String flexfield2) {
        this.flexfield2 = flexfield2;
    }

    public String getFlexfield3() {
        return flexfield3;
    }

    public void setFlexfield3(String flexfield3) {
        this.flexfield3 = flexfield3;
    }

    public String getFlexfield4() {
        return flexfield4;
    }

    public void setFlexfield4(String flexfield4) {
        this.flexfield4 = flexfield4;
    }

    public String getFlexfield5() {
        return flexfield5;
    }

    public void setFlexfield5(String flexfield5) {
        this.flexfield5 = flexfield5;
    }

    public String getFlexfield6() {
        return flexfield6;
    }

    public void setFlexfield6(String flexfield6) {
        this.flexfield6 = flexfield6;
    }

    public String getFlexfield7() {
        return flexfield7;
    }

    public void setFlexfield7(String flexfield7) {
        this.flexfield7 = flexfield7;
    }

    public String getFlexfield8() {
        return flexfield8;
    }

    public void setFlexfield8(String flexfield8) {
        this.flexfield8 = flexfield8;
    }

    public String getFlexfield9() {
        return flexfield9;
    }

    public void setFlexfield9(String flexfield9) {
        this.flexfield9 = flexfield9;
    }

    public String getFlexfield10() {
        return flexfield10;
    }

    public void setFlexfield10(String flexfield10) {
        this.flexfield10 = flexfield10;
    }

    public String getFlexfield11() {
        return flexfield11;
    }

    public void setFlexfield11(String flexfield11) {
        this.flexfield11 = flexfield11;
    }

    public String getFlexfield12() {
        return flexfield12;
    }

    public void setFlexfield12(String flexfield12) {
        this.flexfield12 = flexfield12;
    }

    public String getFlexfield13() {
        return flexfield13;
    }

    public void setFlexfield13(String flexfield13) {
        this.flexfield13 = flexfield13;
    }

    public String getFlexfield14() {
        return flexfield14;
    }

    public void setFlexfield14(String flexfield14) {
        this.flexfield14 = flexfield14;
    }

    public String getFlexfield15() {
        return flexfield15;
    }

    public void setFlexfield15(String flexfield15) {
        this.flexfield15 = flexfield15;
    }

    public String getFlexfield16() {
        return flexfield16;
    }

    public void setFlexfield16(String flexfield16) {
        this.flexfield16 = flexfield16;
    }

    public String getFlexfield17() {
        return flexfield17;
    }

    public void setFlexfield17(String flexfield17) {
        this.flexfield17 = flexfield17;
    }

    public String getFlexfield18() {
        return flexfield18;
    }

    public void setFlexfield18(String flexfield18) {
        this.flexfield18 = flexfield18;
    }

    public String getFlexfield19() {
        return flexfield19;
    }

    public void setFlexfield19(String flexfield19) {
        this.flexfield19 = flexfield19;
    }

    public String getFlexfield20() {
        return flexfield20;
    }

    public void setFlexfield20(String flexfield20) {
        this.flexfield20 = flexfield20;
    }

    public String getFlexfield21() {
        return flexfield21;
    }

    public void setFlexfield21(String flexfield21) {
        this.flexfield21 = flexfield21;
    }

    public String getFlexfield22() {
        return flexfield22;
    }

    public void setFlexfield22(String flexfield22) {
        this.flexfield22 = flexfield22;
    }

    public String getFlexfield23() {
        return flexfield23;
    }

    public void setFlexfield23(String flexfield23) {
        this.flexfield23 = flexfield23;
    }

    public String getFlexfield24() {
        return flexfield24;
    }

    public void setFlexfield24(String flexfield24) {
        this.flexfield24 = flexfield24;
    }

    public String getFlexfield25() {
        return flexfield25;
    }

    public void setFlexfield25(String flexfield25) {
        this.flexfield25 = flexfield25;
    }

    public String getFlexfield26() {
        return flexfield26;
    }

    public void setFlexfield26(String flexfield26) {
        this.flexfield26 = flexfield26;
    }

    public String getFlexfield27() {
        return flexfield27;
    }

    public void setFlexfield27(String flexfield27) {
        this.flexfield27 = flexfield27;
    }

    public String getFlexfield28() {
        return flexfield28;
    }

    public void setFlexfield28(String flexfield28) {
        this.flexfield28 = flexfield28;
    }

    public String getFlexfield29() {
        return flexfield29;
    }

    public void setFlexfield29(String flexfield29) {
        this.flexfield29 = flexfield29;
    }

    public String getFlexfield30() {
        return flexfield30;
    }

    public void setFlexfield30(String flexfield30) {
        this.flexfield30 = flexfield30;
    }

    public String getFlexfield31() {
        return flexfield31;
    }

    public void setFlexfield31(String flexfield31) {
        this.flexfield31 = flexfield31;
    }

    public String getFlexfield32() {
        return flexfield32;
    }

    public void setFlexfield32(String flexfield32) {
        this.flexfield32 = flexfield32;
    }

    public String getFlexfield33() {
        return flexfield33;
    }

    public void setFlexfield33(String flexfield33) {
        this.flexfield33 = flexfield33;
    }

    public String getFlexfield34() {
        return flexfield34;
    }

    public void setFlexfield34(String flexfield34) {
        this.flexfield34 = flexfield34;
    }

    public String getFlexfield35() {
        return flexfield35;
    }

    public void setFlexfield35(String flexfield35) {
        this.flexfield35 = flexfield35;
    }

    public String getFlexfield36() {
        return flexfield36;
    }

    public void setFlexfield36(String flexfield36) {
        this.flexfield36 = flexfield36;
    }

    public String getFlexfield37() {
        return flexfield37;
    }

    public void setFlexfield37(String flexfield37) {
        this.flexfield37 = flexfield37;
    }

    public String getFlexfield38() {
        return flexfield38;
    }

    public void setFlexfield38(String flexfield38) {
        this.flexfield38 = flexfield38;
    }

    public String getFlexfield39() {
        return flexfield39;
    }

    public void setFlexfield39(String flexfield39) {
        this.flexfield39 = flexfield39;
    }

    public String getFlexfield40() {
        return flexfield40;
    }

    public void setFlexfield40(String flexfield40) {
        this.flexfield40 = flexfield40;
    }

    public String getFlexfield41() {
        return flexfield41;
    }

    public void setFlexfield41(String flexfield41) {
        this.flexfield41 = flexfield41;
    }

    public String getFlexfield42() {
        return flexfield42;
    }

    public void setFlexfield42(String flexfield42) {
        this.flexfield42 = flexfield42;
    }

    public String getFlexfield43() {
        return flexfield43;
    }

    public void setFlexfield43(String flexfield43) {
        this.flexfield43 = flexfield43;
    }

    public String getFlexfield44() {
        return flexfield44;
    }

    public void setFlexfield44(String flexfield44) {
        this.flexfield44 = flexfield44;
    }

    public String getFlexfield45() {
        return flexfield45;
    }

    public void setFlexfield45(String flexfield45) {
        this.flexfield45 = flexfield45;
    }

    public String getFlexfield46() {
        return flexfield46;
    }

    public void setFlexfield46(String flexfield46) {
        this.flexfield46 = flexfield46;
    }

    public String getFlexfield47() {
        return flexfield47;
    }

    public void setFlexfield47(String flexfield47) {
        this.flexfield47 = flexfield47;
    }

    public String getFlexfield48() {
        return flexfield48;
    }

    public void setFlexfield48(String flexfield48) {
        this.flexfield48 = flexfield48;
    }

    public String getFlexfield49() {
        return flexfield49;
    }

    public void setFlexfield49(String flexfield49) {
        this.flexfield49 = flexfield49;
    }

    public String getFlexfield50() {
        return flexfield50;
    }

    public void setFlexfield50(String flexfield50) {
        this.flexfield50 = flexfield50;
    }

    public String getFlexfield51() {
        return flexfield51;
    }

    public void setFlexfield51(String flexfield51) {
        this.flexfield51 = flexfield51;
    }

    public String getFlexfield52() {
        return flexfield52;
    }

    public void setFlexfield52(String flexfield52) {
        this.flexfield52 = flexfield52;
    }

    public String getFlexfield53() {
        return flexfield53;
    }

    public void setFlexfield53(String flexfield53) {
        this.flexfield53 = flexfield53;
    }

    public String getFlexfield54() {
        return flexfield54;
    }

    public void setFlexfield54(String flexfield54) {
        this.flexfield54 = flexfield54;
    }

    public String getFlexfield55() {
        return flexfield55;
    }

    public void setFlexfield55(String flexfield55) {
        this.flexfield55 = flexfield55;
    }

    public String getFlexfield56() {
        return flexfield56;
    }

    public void setFlexfield56(String flexfield56) {
        this.flexfield56 = flexfield56;
    }

    public String getFlexfield57() {
        return flexfield57;
    }

    public void setFlexfield57(String flexfield57) {
        this.flexfield57 = flexfield57;
    }

    public String getFlexfield58() {
        return flexfield58;
    }

    public void setFlexfield58(String flexfield58) {
        this.flexfield58 = flexfield58;
    }

    public String getFlexfield59() {
        return flexfield59;
    }

    public void setFlexfield59(String flexfield59) {
        this.flexfield59 = flexfield59;
    }

    public String getFlexfield60() {
        return flexfield60;
    }

    public void setFlexfield60(String flexfield60) {
        this.flexfield60 = flexfield60;
    }

    public String getFlexfield61() {
        return flexfield61;
    }

    public void setFlexfield61(String flexfield61) {
        this.flexfield61 = flexfield61;
    }

    public String getFlexfield62() {
        return flexfield62;
    }

    public void setFlexfield62(String flexfield62) {
        this.flexfield62 = flexfield62;
    }

    public String getFlexfield63() {
        return flexfield63;
    }

    public void setFlexfield63(String flexfield63) {
        this.flexfield63 = flexfield63;
    }

    public String getFlexfield64() {
        return flexfield64;
    }

    public void setFlexfield64(String flexfield64) {
        this.flexfield64 = flexfield64;
    }

    public String getFlexfield65() {
        return flexfield65;
    }

    public void setFlexfield65(String flexfield65) {
        this.flexfield65 = flexfield65;
    }

    public String getFlexfield66() {
        return flexfield66;
    }

    public void setFlexfield66(String flexfield66) {
        this.flexfield66 = flexfield66;
    }

    public String getFlexfield67() {
        return flexfield67;
    }

    public void setFlexfield67(String flexfield67) {
        this.flexfield67 = flexfield67;
    }

    public String getFlexfield68() {
        return flexfield68;
    }

    public void setFlexfield68(String flexfield68) {
        this.flexfield68 = flexfield68;
    }

    public String getFlexfield69() {
        return flexfield69;
    }

    public void setFlexfield69(String flexfield69) {
        this.flexfield69 = flexfield69;
    }

    public String getFlexfield70() {
        return flexfield70;
    }

    public void setFlexfield70(String flexfield70) {
        this.flexfield70 = flexfield70;
    }

    public String getFlexfield71() {
        return flexfield71;
    }

    public void setFlexfield71(String flexfield71) {
        this.flexfield71 = flexfield71;
    }

    public String getFlexfield72() {
        return flexfield72;
    }

    public void setFlexfield72(String flexfield72) {
        this.flexfield72 = flexfield72;
    }

    public String getFlexfield73() {
        return flexfield73;
    }

    public void setFlexfield73(String flexfield73) {
        this.flexfield73 = flexfield73;
    }

    public String getFlexfield74() {
        return flexfield74;
    }

    public void setFlexfield74(String flexfield74) {
        this.flexfield74 = flexfield74;
    }

    public String getFlexfield75() {
        return flexfield75;
    }

    public void setFlexfield75(String flexfield75) {
        this.flexfield75 = flexfield75;
    }

    public String getFlexfield76() {
        return flexfield76;
    }

    public void setFlexfield76(String flexfield76) {
        this.flexfield76 = flexfield76;
    }

    public String getFlexfield77() {
        return flexfield77;
    }

    public void setFlexfield77(String flexfield77) {
        this.flexfield77 = flexfield77;
    }

    public String getFlexfield78() {
        return flexfield78;
    }

    public void setFlexfield78(String flexfield78) {
        this.flexfield78 = flexfield78;
    }

    public String getFlexfield79() {
        return flexfield79;
    }

    public void setFlexfield79(String flexfield79) {
        this.flexfield79 = flexfield79;
    }

    public String getFlexfield80() {
        return flexfield80;
    }

    public void setFlexfield80(String flexfield80) {
        this.flexfield80 = flexfield80;
    }

    public String getFlexfield81() {
        return flexfield81;
    }

    public void setFlexfield81(String flexfield81) {
        this.flexfield81 = flexfield81;
    }

    public String getFlexfield82() {
        return flexfield82;
    }

    public void setFlexfield82(String flexfield82) {
        this.flexfield82 = flexfield82;
    }

    public String getFlexfield83() {
        return flexfield83;
    }

    public void setFlexfield83(String flexfield83) {
        this.flexfield83 = flexfield83;
    }

    public String getFlexfield84() {
        return flexfield84;
    }

    public void setFlexfield84(String flexfield84) {
        this.flexfield84 = flexfield84;
    }

    public String getFlexfield85() {
        return flexfield85;
    }

    public void setFlexfield85(String flexfield85) {
        this.flexfield85 = flexfield85;
    }

    public String getFlexfield86() {
        return flexfield86;
    }

    public void setFlexfield86(String flexfield86) {
        this.flexfield86 = flexfield86;
    }

    public String getFlexfield87() {
        return flexfield87;
    }

    public void setFlexfield87(String flexfield87) {
        this.flexfield87 = flexfield87;
    }

    public String getFlexfield88() {
        return flexfield88;
    }

    public void setFlexfield88(String flexfield88) {
        this.flexfield88 = flexfield88;
    }

    public String getFlexfield89() {
        return flexfield89;
    }

    public void setFlexfield89(String flexfield89) {
        this.flexfield89 = flexfield89;
    }

    public String getFlexfield90() {
        return flexfield90;
    }

    public void setFlexfield90(String flexfield90) {
        this.flexfield90 = flexfield90;
    }

    public String getFlexfield91() {
        return flexfield91;
    }

    public void setFlexfield91(String flexfield91) {
        this.flexfield91 = flexfield91;
    }

    public String getFlexfield92() {
        return flexfield92;
    }

    public void setFlexfield92(String flexfield92) {
        this.flexfield92 = flexfield92;
    }

    public String getFlexfield93() {
        return flexfield93;
    }

    public void setFlexfield93(String flexfield93) {
        this.flexfield93 = flexfield93;
    }

    public String getFlexfield94() {
        return flexfield94;
    }

    public void setFlexfield94(String flexfield94) {
        this.flexfield94 = flexfield94;
    }

    public String getFlexfield95() {
        return flexfield95;
    }

    public void setFlexfield95(String flexfield95) {
        this.flexfield95 = flexfield95;
    }

    public String getFlexfield96() {
        return flexfield96;
    }

    public void setFlexfield96(String flexfield96) {
        this.flexfield96 = flexfield96;
    }

    public String getFlexfield97() {
        return flexfield97;
    }

    public void setFlexfield97(String flexfield97) {
        this.flexfield97 = flexfield97;
    }

    public String getFlexfield98() {
        return flexfield98;
    }

    public void setFlexfield98(String flexfield98) {
        this.flexfield98 = flexfield98;
    }

    public String getFlexfield99() {
        return flexfield99;
    }

    public void setFlexfield99(String flexfield99) {
        this.flexfield99 = flexfield99;
    }

    public String getFlexfield100() {
        return flexfield100;
    }

    public void setFlexfield100(String flexfield100) {
        this.flexfield100 = flexfield100;
    }
    
    public Boolean getOpOut() {
        return opOut;
    }

    public void setOpOut(Boolean opOut) {
        this.opOut = opOut;
    }

    public Date getOpOutDate() {
        return opOutDate;
    }

    public void setOpOutDate(Date opOutDate) {
        this.opOutDate = opOutDate;
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

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Collection<Customer> getCustomerCollection() {
        return customerCollection;
    }

    public void setCustomerCollection(Collection<Customer> customerCollection) {
        this.customerCollection = customerCollection;
    }

    public Collection<Customer> getCustomerCollection1() {
        return customerCollection1;
    }

    public void setCustomerCollection1(Collection<Customer> customerCollection1) {
        this.customerCollection1 = customerCollection1;
    }

    public Account getAccountId() {
        return accountId;
    }

    public void setAccountId(Account accountId) {
        this.accountId = accountId;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    public District getCurrentDistrictId() {
        return currentDistrictId;
    }

    public void setCurrentDistrictId(District currentDistrictId) {
        this.currentDistrictId = currentDistrictId;
    }

    public IdcardType getIdcardTypeId() {
        return idcardTypeId;
    }

    public void setIdcardTypeId(IdcardType idcardTypeId) {
        this.idcardTypeId = idcardTypeId;
    }

/*    public Collection<PurchaseOrder> getPurchaseOrderCollection() {
        return purchaseOrderCollection;
    }

    public void setPurchaseOrderCollection(Collection<PurchaseOrder> purchaseOrderCollection) {
        this.purchaseOrderCollection = purchaseOrderCollection;
    }*/

//    public Collection<AssignmentDetail> getAssignmentDetailCollection() {
//        return assignmentDetailCollection;
//    }
//
//    public void setAssignmentDetailCollection(Collection<AssignmentDetail> assignmentDetailCollection) {
//        this.assignmentDetailCollection = assignmentDetailCollection;
//    }

    public Collection<MarketingCustomer> getMarketingCustomerCollection() {
        return marketingCustomerCollection;
    }

    public void setMarketingCustomerCollection(Collection<MarketingCustomer> marketingCustomerCollection) {
        this.marketingCustomerCollection = marketingCustomerCollection;
    }

 /*   public PurchaseHistory getPurchaseHistory() {
        return purchaseHistory;
    }

    public void setPurchaseHistory(PurchaseHistory purchaseHistory) {
        this.purchaseHistory = purchaseHistory;
    }*/

    /**
     * @return the contactCaseCollection
     */

    public Collection<ContactCase> getContactCaseCollection() {
        return contactCaseCollection;
    }

    /**
     * @param contactCaseCollection the contactCaseCollection to set
     */
    public void setContactCaseCollection(Collection<ContactCase> contactCaseCollection) {
        this.contactCaseCollection = contactCaseCollection;
    }

    public Collection<TempPurchaseOrder> getTempPurchaseOrderCollection() {
        return tempPurchaseOrderCollection;
    }

    public void setTempPurchaseOrderCollection(Collection<TempPurchaseOrder> tempPurchaseOrderCollection) {
        this.tempPurchaseOrderCollection = tempPurchaseOrderCollection;
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
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.Customer[id=" + id + "]";
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

    public String getCurrentDistrictName() {
        return currentDistrictName;
    }

    public void setCurrentDistrictName(String currentDistrictName) {
        this.currentDistrictName = currentDistrictName;
    }

    public String getCurrentProvinceName() {
        return currentProvinceName;
    }

    public void setCurrentProvinceName(String currentProvinceName) {
        this.currentProvinceName = currentProvinceName;
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

    public String getHomeDistrictName() {
        return homeDistrictName;
    }

    public void setHomeDistrictName(String homeDistrictName) {
        this.homeDistrictName = homeDistrictName;
    }

    public String getHomeProvinceName() {
        return homeProvinceName;
    }

    public void setHomeProvinceName(String homeProvinceName) {
        this.homeProvinceName = homeProvinceName;
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

    public String getBillingDistrictName() {
        return billingDistrictName;
    }

    public void setBillingDistrictName(String billingDistrictName) {
        this.billingDistrictName = billingDistrictName;
    }

    public String getBillingProvinceName() {
        return billingProvinceName;
    }

    public void setBillingProvinceName(String billingProvinceName) {
        this.billingProvinceName = billingProvinceName;
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

    public String getShippingDistrictName() {
        return shippingDistrictName;
    }

    public void setShippingDistrictName(String shippingDistrictName) {
        this.shippingDistrictName = shippingDistrictName;
    }

    public String getShippingProvinceName() {
        return shippingProvinceName;
    }

    public void setShippingProvinceName(String shippingProvinceName) {
        this.shippingProvinceName = shippingProvinceName;
    }

   

    public String getCurrentCountryName() {
        return currentCountryName;
    }

    public void setCurrentCountryName(String currentCountryName) {
        this.currentCountryName = currentCountryName;
    }

    public String getHomeCountryName() {
        return homeCountryName;
    }

    public void setHomeCountryName(String homeCountryName) {
        this.homeCountryName = homeCountryName;
    }

    public String getBillingCountryName() {
        return billingCountryName;
    }

    public void setBillingCountryName(String billingCountryName) {
        this.billingCountryName = billingCountryName;
    }

    public String getShippingCountryName() {
        return shippingCountryName;
    }

    public void setShippingCountryName(String shippingCountryName) {
        this.shippingCountryName = shippingCountryName;
    }

    public String getTelephoneDefault() {
        return telephoneDefault;
    }

    public void setTelephoneDefault(String telephoneDefault) {
        this.telephoneDefault = telephoneDefault;
    }

    public Boolean getHomePhoneClose() {
        return homePhoneClose;
    }

    public void setHomePhoneClose(Boolean homePhoneClose) {
        this.homePhoneClose = homePhoneClose;
    }

    public Boolean getOfficePhoneClose() {
        return officePhoneClose;
    }

    public void setOfficePhoneClose(Boolean officePhoneClose) {
        this.officePhoneClose = officePhoneClose;
    }

    public Boolean getMobilePhoneClose() {
        return mobilePhoneClose;
    }

    public void setMobilePhoneClose(Boolean mobilePhoneClose) {
        this.mobilePhoneClose = mobilePhoneClose;
    }

    public Boolean getContactPhone1Close() {
        return contactPhone1Close;
    }

    public void setContactPhone1Close(Boolean contactPhone1Close) {
        this.contactPhone1Close = contactPhone1Close;
    }

    public Boolean getContactPhone2Close() {
        return contactPhone2Close;
    }

    public void setContactPhone2Close(Boolean contactPhone2Close) {
        this.contactPhone2Close = contactPhone2Close;
    }

    public Boolean getContactPhone3Close() {
        return contactPhone3Close;
    }

    public void setContactPhone3Close(Boolean contactPhone3Close) {
        this.contactPhone3Close = contactPhone3Close;
    }

    public Boolean getContactPhone4Close() {
        return contactPhone4Close;
    }

    public void setContactPhone4Close(Boolean contactPhone4Close) {
        this.contactPhone4Close = contactPhone4Close;
    }

    public Boolean getContactPhone5Close() {
        return contactPhone5Close;
    }

    public void setContactPhone5Close(Boolean contactPhone5Close) {
        this.contactPhone5Close = contactPhone5Close;
    }

    public String getHomePhoneReason() {
        return homePhoneReason;
    }

    public void setHomePhoneReason(String homePhoneReason) {
        this.homePhoneReason = homePhoneReason;
    }

    public String getOfficePhoneReason() {
        return officePhoneReason;
    }

    public void setOfficePhoneReason(String officePhoneReason) {
        this.officePhoneReason = officePhoneReason;
    }

    public String getMobilePhoneReason() {
        return mobilePhoneReason;
    }

    public void setMobilePhoneReason(String mobilePhoneReason) {
        this.mobilePhoneReason = mobilePhoneReason;
    }

    public String getContactPhone1Reason() {
        return contactPhone1Reason;
    }

    public void setContactPhone1Reason(String contactPhone1Reason) {
        this.contactPhone1Reason = contactPhone1Reason;
    }

    public String getContactPhone2Reason() {
        return contactPhone2Reason;
    }

    public void setContactPhone2Reason(String contactPhone2Reason) {
        this.contactPhone2Reason = contactPhone2Reason;
    }

    public String getContactPhone3Reason() {
        return contactPhone3Reason;
    }

    public void setContactPhone3Reason(String contactPhone3Reason) {
        this.contactPhone3Reason = contactPhone3Reason;
    }

    public String getContactPhone4Reason() {
        return contactPhone4Reason;
    }

    public void setContactPhone4Reason(String contactPhone4Reason) {
        this.contactPhone4Reason = contactPhone4Reason;
    }

    public String getContactPhone5Reason() {
        return contactPhone5Reason;
    }

    public void setContactPhone5Reason(String contactPhone5Reason) {
        this.contactPhone5Reason = contactPhone5Reason;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getContactPhone1ReasonId() {
        return contactPhone1ReasonId;
    }

    public void setContactPhone1ReasonId(Integer contactPhone1ReasonId) {
        this.contactPhone1ReasonId = contactPhone1ReasonId;
    }

    public Integer getContactPhone2ReasonId() {
        return contactPhone2ReasonId;
    }

    public void setContactPhone2ReasonId(Integer contactPhone2ReasonId) {
        this.contactPhone2ReasonId = contactPhone2ReasonId;
    }

    public Integer getContactPhone3ReasonId() {
        return contactPhone3ReasonId;
    }

    public void setContactPhone3ReasonId(Integer contactPhone3ReasonId) {
        this.contactPhone3ReasonId = contactPhone3ReasonId;
    }

    public Integer getContactPhone4ReasonId() {
        return contactPhone4ReasonId;
    }

    public void setContactPhone4ReasonId(Integer contactPhone4ReasonId) {
        this.contactPhone4ReasonId = contactPhone4ReasonId;
    }

    public Integer getContactPhone5ReasonId() {
        return contactPhone5ReasonId;
    }

    public void setContactPhone5ReasonId(Integer contactPhone5ReasonId) {
        this.contactPhone5ReasonId = contactPhone5ReasonId;
    }

    public Integer getHomePhoneReasonId() {
        return homePhoneReasonId;
    }

    public void setHomePhoneReasonId(Integer homePhoneReasonId) {
        this.homePhoneReasonId = homePhoneReasonId;
    }

    public Integer getMobilePhoneReasonId() {
        return mobilePhoneReasonId;
    }

    public void setMobilePhoneReasonId(Integer mobilePhoneReasonId) {
        this.mobilePhoneReasonId = mobilePhoneReasonId;
    }

    public Integer getOfficePhoneReasonId() {
        return officePhoneReasonId;
    }

    public void setOfficePhoneReasonId(Integer officePhoneReasonId) {
        this.officePhoneReasonId = officePhoneReasonId;
    }

    public Collection<CampaignCustomer> getCampaignCustomerCollection() {
        return campaignCustomerCollection;
    }

    public void setCampaignCustomerCollection(Collection<CampaignCustomer> campaignCustomerCollection) {
        this.campaignCustomerCollection = campaignCustomerCollection;
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

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public Integer getCarYear() {
        return carYear;
    }

    public void setCarYear(Integer carYear) {
        this.carYear = carYear;
    }

    public String getCarCharacterGroup() {
        return carCharacterGroup;
    }

    public void setCarCharacterGroup(String carCharacterGroup) {
        this.carCharacterGroup = carCharacterGroup;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getCarProvince() {
        return carProvince;
    }

    public void setCarProvince(String carProvince) {
        this.carProvince = carProvince;
    }
    
    public Boolean getManualInsert() {
        return manualInsert;
    }

    public void setManualInsert(Boolean manualInsert) {
        this.manualInsert = manualInsert;
    }

    public Boolean getCreatedByCs() {
        return createdByCs;
    }

    public void setCreatedByCs(Boolean createdByCs) {
        this.createdByCs = createdByCs;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

}
