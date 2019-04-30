/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.value.front.customerHandling;

import com.maxelyz.core.model.entity.Customer;
import com.maxelyz.core.model.entity.District;
import com.maxelyz.core.model.entity.Subdistrict;
import com.maxelyz.utils.JSFUtil;
import java.util.Calendar;
import java.util.Date;

import com.ntier.utils.DateUtil;
import javax.persistence.Column;

/**
 *
 * @author Manop
 */
public class CustomerInfoValue {
    private Integer id;
    private Integer accountId;
    private String referenceNo;
    private String accountName;
    private String initial;
    private String name;
    private String surname;
    private Integer cardTypeId;
    private String cardType;
    private String idno;
    private String gender;
    private Date dob;
    private Long age;
    private Integer creditLimit;
    private Integer creditTerm;
    private String privilege;
    private Double weight;
    private Double height;
    private String nationality;
    private String occupation;
    private String homePhone = "";
    private String homeExt = "";
    private String officePhone = "";
    private String officeExt = "";
    private String mobilePhone = "";
    private String fax;
    private String faxExt;
    private String email;
    private Integer customerTypeid;
    private String customerType;
    private String currentFullname;
    private String currentAddressLine1;
    private String currentAddressLine2;
    private String currentSubdistrictName;
    private String currentDistrictName;
    private String currentProvinceName;
    private String currentPostalCode;
    
    private String homeFullname;
    private String homeAddressLine1;
    private String homeAddressLine2;
    private String homeSubdistrictName;
    private String homeDistrictName;
    private String homeProvinceName;
    private String homePostalCode;
    
    private String billingFullname;
    private String billingAddressLine1;
    private String billingAddressLine2;
    private String billingSubdistrictName;
    private String billingDistrictName;
    private String billingProvinceName;
    private String billingPostalCode;
    
    private String shippingFullname;
    private String shippingAddressLine1;
    private String shippingAddressLine2;
    private String shippingSubdistrictName;
    private String shippingDistrictName;
    private String shippingProvinceName;
    private String shippingPostalCode;
    
    private String picture;
    private String emoticon;
    private String emotion;
    private String remark;
    private Long totalCases;
    private Long pendingCases;
    private Long closedCases;
    private String flexfield1;
    private String flexfield2;
    private String flexfield3;
    private String flexfield4;
    private String flexfield5;
    private String flexfield6;
    private String flexfield7;
    private String flexfield8;
    private String flexfield9;
    private String flexfield10; 
    private String flexfield11;
    private String flexfield12;
    private String flexfield13;
    private String flexfield14;
    private String flexfield15;
    private String flexfield16; 
    private String flexfield17;
    private String flexfield18;
    private String flexfield19;
    private String flexfield20;
    private String flexfield21;
    private String flexfield22;
    private String flexfield23;
    private String flexfield24;
    private String flexfield25;
    private String flexfield26;
    private String flexfield27;
    private String flexfield28;
    private String flexfield29;
    private String flexfield30;
    private String flexfield31;
    private String flexfield32;
    private String flexfield33;
    private String flexfield34;
    private String flexfield35;
    private String flexfield36;
    private String flexfield37;
    private String flexfield38;
    private String flexfield39;
    private String flexfield40;
    private String flexfield41;
    private String flexfield42;
    private String flexfield43;
    private String flexfield44;
    private String flexfield45;
    private String flexfield46;
    private String flexfield47;
    private String flexfield48;
    private String flexfield49;
    private String flexfield50;
    private String flexfield51;
    private String flexfield52;
    private String flexfield53;
    private String flexfield54;
    private String flexfield55;
    private String flexfield56;
    private String flexfield57;
    private String flexfield58;
    private String flexfield59;
    private String flexfield60;
    private String flexfield61;
    private String flexfield62;
    private String flexfield63;
    private String flexfield64;
    private String flexfield65;
    private String flexfield66;
    private String flexfield67;
    private String flexfield68;
    private String flexfield69;
    private String flexfield70;
    private String flexfield71;
    private String flexfield72;
    private String flexfield73;
    private String flexfield74;
    private String flexfield75;
    private String flexfield76;
    private String flexfield77;
    private String flexfield78;
    private String flexfield79;
    private String flexfield80;
    private String flexfield81;
    private String flexfield82;
    private String flexfield83;
    private String flexfield84;
    private String flexfield85;
    private String flexfield86;
    private String flexfield87;
    private String flexfield88;
    private String flexfield89;
    private String flexfield90;
    private String flexfield91;
    private String flexfield92;
    private String flexfield93;
    private String flexfield94;
    private String flexfield95;
    private String flexfield96;
    private String flexfield97;
    private String flexfield98;
    private String flexfield99;
    private String flexfield100;
    
    private String contactPhone1 = "";
    private String contactExt1 = "";
    private String contactPhone2 = "";
    private String contactExt2 = "";
    private String contactPhone3 = "";
    private String contactExt3 = "";
    private String contactPhone4 = "";
    private String contactExt4 = "";
    private String contactPhone5 = "";
    private String contactExt5 = "";
    
    private String telephoneDefault;
    private Boolean homePhoneClose;
    private Boolean officePhoneClose;
    private Boolean mobilePhoneClose;
    private Boolean contactPhone1Close;
    private Boolean contactPhone2Close;
    private Boolean contactPhone3Close;
    private Boolean contactPhone4Close;
    private Boolean contactPhone5Close;  
    
    Subdistrict currentSubdistrict;
    District currentDistrict;
    Subdistrict homeSubdistrict;
    District homeDistrict;
    Subdistrict billingSubdistrict;
    District billingDistrict;
    Subdistrict shippingSubdistrict; 
    District shippingDistrict;

    private String defaultPhoneNo = "";
    private String defaultPhoneNoExt = "";
    private String mobilePhoneHide = "";
    private String homePhoneHide = "";
    private String officePhoneHide = "";
    private String contactPhone1Hide = "";
    private String contactPhone2Hide = "";
    private String contactPhone3Hide = "";
    private String contactPhone4Hide = "";
    private String contactPhone5Hide = "";
    private String defaultPhoneNoHide = "";
    private String defaultPhoneNoExtHide = "";
    

    public CustomerInfoValue(){}

    public CustomerInfoValue(Customer customer) {
        this.id = customer.getId();
        this.referenceNo = customer.getReferenceNo();
        this.accountId = customer.getAccountId() == null ? null : customer.getAccountId().getId();
        this.accountName = customer.getAccountId() == null ? null : customer.getAccountId().getName();
        this.initial = customer.getInitial();
        this.name = customer.getName();
        this.surname = customer.getSurname();
        this.cardTypeId = customer.getIdcardTypeId() == null ? null : customer.getIdcardTypeId().getId();
        this.cardType = customer.getIdcardTypeId() == null ? null : customer.getIdcardTypeId().getName();
        this.idno = customer.getIdno();
        this.gender = customer.getGender();
        this.dob = customer.getDob();

        if (dob != null) {
            DateUtil dateUtil = new DateUtil();
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.setTime(dob);
            this.age = dateUtil.getDateDiffInYear(calendar, Calendar.getInstance());
        } else {
            this.age = Long.valueOf("0");
        }

        this.creditLimit = 0;
        this.creditTerm = 0;
        this.privilege = customer.getPrivilege();
        this.weight = customer.getWeight();
        this.height = customer.getHeight();
        this.nationality = customer.getNationality();
        this.occupation = customer.getOccupation();
        
        this.homePhone = customer.getHomePhone() != null ? customer.getHomePhone() : "";
        this.homeExt = customer.getHomeExt() != null ? customer.getHomeExt() : "";
        this.officePhone = customer.getOfficePhone() != null ? customer.getOfficePhone() : "";
        this.officeExt = customer.getOfficeExt() != null ? customer.getOfficeExt() : "";
        this.mobilePhone = customer.getMobilePhone() != null ? customer.getMobilePhone() : "";
        this.fax = customer.getFax();
        this.faxExt = customer.getFaxExt();
        this.email = customer.getEmail();
        
        this.customerTypeid = customer.getCustomerType() != null ? customer.getCustomerType().getId(): 0;
        this.customerType = customer.getCustomerType() != null ? customer.getCustomerType().getName(): "";
        this.currentFullname = customer.getCurrentFullname();
        this.currentAddressLine1 = customer.getCurrentAddressLine1();
        this.currentAddressLine2 = customer.getCurrentAddressLine2();
        if(customer.getCurrentSubDistrict() != null)
            this.currentSubdistrictName = customer.getCurrentSubDistrict().getName();
        if(customer.getCurrentDistrictId() != null) {
            this.currentDistrictName = customer.getCurrentDistrictId().getName();
            this.currentProvinceName = customer.getCurrentDistrictId().getProvinceId().getName();
        }
        this.currentPostalCode = customer.getCurrentPostalCode();
        
        this.homeFullname = customer.getHomeFullname();
        this.homeAddressLine1 = customer.getHomeAddressLine1();
        this.homeAddressLine2 = customer.getHomeAddressLine2();
        if(customer.getHomeSubDistrict() != null)
            this.homeSubdistrictName = customer.getHomeSubDistrict().getDistrictName();
        if(customer.getHomeDistrictId() != null) {
            this.homeDistrictName = customer.getHomeDistrictId().getName();
            this.homeProvinceName = customer.getHomeDistrictId().getProvinceId().getName();
        }
        this.homePostalCode = customer.getHomePostalCode();
        
        this.billingFullname = customer.getBillingFullname();
        this.billingAddressLine1 = customer.getBillingAddressLine1();
        this.billingAddressLine2 = customer.getBillingAddressLine2();
        if(customer.getBillingSubDistrict() != null)
            this.billingSubdistrictName = customer.getBillingSubDistrict().getName();
        if(billingDistrict != null) {
            this.billingDistrictName = customer.getBillingDistrictId().getName();
            this.billingProvinceName = customer.getBillingDistrictId().getProvinceId().getName();
        }
        this.billingPostalCode = customer.getBillingPostalCode();
        
        this.shippingFullname = customer.getShippingFullname();
        this.shippingAddressLine1 = customer.getShippingAddressLine1();
        this.shippingAddressLine2 = customer.getShippingAddressLine2();
        if(customer.getShippingSubDistrict() != null)
            this.shippingSubdistrictName = customer.getShippingSubDistrict().getName();
        if(customer.getShippingDistrictId() != null) {
            this.shippingDistrictName = customer.getShippingDistrictId().getName();
            this.shippingProvinceName = customer.getShippingDistrictId().getProvinceId().getName();
        }
        this.shippingPostalCode = customer.getShippingPostalCode();
        
        this.picture = customer.getPicture();
        this.emoticon = customer.getEmoicon();
        this.remark = customer.getRemark();
        /*
        this.totalCases = totalCases;
        this.pendingCases = pendingCases;
        this.closedCases = closedCases;
        */
        this.flexfield1 = customer.getFlexfield1();
        this.flexfield2 = customer.getFlexfield2();
        this.flexfield3 = customer.getFlexfield3();
        this.flexfield4 = customer.getFlexfield4();
        this.flexfield5 = customer.getFlexfield5();
        this.flexfield6 = customer.getFlexfield6();
        this.flexfield7 = customer.getFlexfield7();
        this.flexfield8 = customer.getFlexfield8();
        this.flexfield9 = customer.getFlexfield9();
        this.flexfield10 = customer.getFlexfield10();
        this.flexfield11 = customer.getFlexfield11();
        this.flexfield12 = customer.getFlexfield12();
        this.flexfield13 = customer.getFlexfield13();
        this.flexfield14 = customer.getFlexfield14();
        this.flexfield15 = customer.getFlexfield15();
        this.flexfield16 = customer.getFlexfield16();
        this.flexfield17 = customer.getFlexfield17();
        this.flexfield18 = customer.getFlexfield18();
        this.flexfield19 = customer.getFlexfield19();
        this.flexfield20 = customer.getFlexfield20();
        this.flexfield21 = customer.getFlexfield21();
        this.flexfield22 = customer.getFlexfield22();
        this.flexfield23 = customer.getFlexfield23();
        this.flexfield24 = customer.getFlexfield24();
        this.flexfield25 = customer.getFlexfield25();
        this.flexfield26 = customer.getFlexfield26();
        this.flexfield27 = customer.getFlexfield27();
        this.flexfield28 = customer.getFlexfield28();
        this.flexfield29 = customer.getFlexfield29();
        this.flexfield30 = customer.getFlexfield30();
        this.flexfield31 = customer.getFlexfield31();
        this.flexfield32 = customer.getFlexfield32();
        this.flexfield33 = customer.getFlexfield33();
        this.flexfield34 = customer.getFlexfield34();
        this.flexfield35 = customer.getFlexfield35();
        this.flexfield36 = customer.getFlexfield36();
        this.flexfield37 = customer.getFlexfield37();
        this.flexfield38 = customer.getFlexfield38();
        this.flexfield39 = customer.getFlexfield39();
        this.flexfield40 = customer.getFlexfield40();
        this.flexfield41 = customer.getFlexfield41();
        this.flexfield42 = customer.getFlexfield42();
        this.flexfield43 = customer.getFlexfield43();
        this.flexfield44 = customer.getFlexfield44();
        this.flexfield45 = customer.getFlexfield45();
        this.flexfield46 = customer.getFlexfield46();
        this.flexfield47 = customer.getFlexfield47();
        this.flexfield48 = customer.getFlexfield48();
        this.flexfield49 = customer.getFlexfield49();
        this.flexfield50 = customer.getFlexfield50();
        this.flexfield51 = customer.getFlexfield51();
        this.flexfield52 = customer.getFlexfield52();
        this.flexfield53 = customer.getFlexfield53();
        this.flexfield54 = customer.getFlexfield54();
        this.flexfield55 = customer.getFlexfield55();
        this.flexfield56 = customer.getFlexfield56();
        this.flexfield57 = customer.getFlexfield57();
        this.flexfield58 = customer.getFlexfield58();
        this.flexfield59 = customer.getFlexfield59();
        this.flexfield60 = customer.getFlexfield60();
        this.flexfield61 = customer.getFlexfield61();
        this.flexfield62 = customer.getFlexfield62();
        this.flexfield63 = customer.getFlexfield63();
        this.flexfield64 = customer.getFlexfield64();
        this.flexfield65 = customer.getFlexfield65();
        this.flexfield66 = customer.getFlexfield66();
        this.flexfield67 = customer.getFlexfield67();
        this.flexfield68 = customer.getFlexfield68();
        this.flexfield69 = customer.getFlexfield69();
        this.flexfield70 = customer.getFlexfield70();
        this.flexfield71 = customer.getFlexfield71();
        this.flexfield72 = customer.getFlexfield72();
        this.flexfield73 = customer.getFlexfield73();
        this.flexfield74 = customer.getFlexfield74();
        this.flexfield75 = customer.getFlexfield75();
        this.flexfield76 = customer.getFlexfield76();
        this.flexfield77 = customer.getFlexfield77();
        this.flexfield78 = customer.getFlexfield78();
        this.flexfield79 = customer.getFlexfield79();
        this.flexfield80 = customer.getFlexfield80();
        this.flexfield81 = customer.getFlexfield81();
        this.flexfield82 = customer.getFlexfield82();
        this.flexfield83 = customer.getFlexfield83();
        this.flexfield84 = customer.getFlexfield84();
        this.flexfield85 = customer.getFlexfield85();
        this.flexfield86 = customer.getFlexfield86();
        this.flexfield87 = customer.getFlexfield87();
        this.flexfield88 = customer.getFlexfield88();
        this.flexfield89 = customer.getFlexfield89();
        this.flexfield90 = customer.getFlexfield90();
        this.flexfield91 = customer.getFlexfield91();
        this.flexfield92 = customer.getFlexfield92();
        this.flexfield93 = customer.getFlexfield93();
        this.flexfield94 = customer.getFlexfield94();
        this.flexfield95 = customer.getFlexfield95();
        this.flexfield96 = customer.getFlexfield96();
        this.flexfield97 = customer.getFlexfield97();
        this.flexfield98 = customer.getFlexfield98();
        this.flexfield99 = customer.getFlexfield99();
        this.flexfield100 = customer.getFlexfield100();
        
        this.contactPhone1 = customer.getContactPhone1() != null ? customer.getContactPhone1() : "";   
        this.contactExt1 = customer.getContactExt1() != null ? customer.getContactExt1() : "";
        this.contactPhone2 = customer.getContactPhone2() != null ? customer.getContactPhone2() : "";
        this.contactExt2 = customer.getContactExt2() != null ? customer.getContactExt2() : "";
        this.contactPhone3 = customer.getContactPhone3() != null ? customer.getContactPhone3() : "";
        this.contactExt3 = customer.getContactExt3() != null ? customer.getContactExt3() : "";
        this.contactPhone4 = customer.getContactPhone4() != null ? customer.getContactPhone4() : "";
        this.contactExt4 = customer.getContactExt4() != null ? customer.getContactExt4() : "";
        this.contactPhone5 = customer.getContactPhone5() != null ? customer.getContactPhone5() : "";
        this.contactExt5 = customer.getContactExt5() != null ? customer.getContactExt5() : "";
        
        this.telephoneDefault = customer.getTelephoneDefault();
        this.homePhoneClose = customer.getHomePhoneClose();
        this.officePhoneClose = customer.getOfficePhoneClose();
        this.mobilePhoneClose = customer.getMobilePhoneClose();
        this.contactPhone1Close = customer.getContactPhone1Close();
        this.contactPhone2Close = customer.getContactPhone2Close();
        this.contactPhone3Close = customer.getContactPhone3Close();
        this.contactPhone4Close = customer.getContactPhone4Close();
        this.contactPhone5Close = customer.getContactPhone5Close();

        this.mobilePhoneHide = getHidePhoneNo(this.mobilePhone);
        this.homePhoneHide = getHidePhoneNo(this.homePhone);
        this.officePhoneHide = getHidePhoneNo(this.officePhone);
        this.contactPhone1Hide = getHidePhoneNo(this.contactPhone1);
        this.contactPhone2Hide = getHidePhoneNo(this.contactPhone2);
        this.contactPhone3Hide = getHidePhoneNo(this.contactPhone3);
        this.contactPhone4Hide = getHidePhoneNo(this.contactPhone4);
        this.contactPhone5Hide = getHidePhoneNo(this.contactPhone5);
      
        if(this.telephoneDefault != null && !this.telephoneDefault.equals("")){
            if(this.telephoneDefault.equals("mobile")){
                this.defaultPhoneNo = this.mobilePhone;
                this.defaultPhoneNoExt = "";
                this.defaultPhoneNoHide = this.mobilePhoneHide;
                this.defaultPhoneNoExtHide = "";
            }else if(this.telephoneDefault.equals("office")){
                this.defaultPhoneNo = this.officePhone;
                this.defaultPhoneNoExt = this.officeExt;
                this.defaultPhoneNoHide = this.officePhoneHide;
                this.defaultPhoneNoExtHide = this.officeExt;
            }else if(this.telephoneDefault.equals("home")){
                this.defaultPhoneNo = this.homePhone;
                this.defaultPhoneNoExt = this.homeExt;
                this.defaultPhoneNoHide = this.homePhoneHide;
                this.defaultPhoneNoExtHide = this.homeExt;
            }else if(this.telephoneDefault.equals("contact1")){
                this.defaultPhoneNo = this.contactPhone1;
                this.defaultPhoneNoExt = this.contactExt1;
                this.defaultPhoneNoHide = this.contactPhone1Hide;
                this.defaultPhoneNoExtHide = this.contactExt1;
            }else if(this.telephoneDefault.equals("contact2")){
                this.defaultPhoneNo = this.contactPhone2;
                this.defaultPhoneNoExt = this.contactExt2;
                this.defaultPhoneNoHide = this.contactPhone2Hide;
                this.defaultPhoneNoExtHide = this.contactExt2;
            }else if(this.telephoneDefault.equals("contact3")){
                this.defaultPhoneNo = this.contactPhone3;
                this.defaultPhoneNoExt = this.contactExt3;
                this.defaultPhoneNoHide = this.contactPhone3Hide;
                this.defaultPhoneNoExtHide = this.contactExt3;
            }else if(this.telephoneDefault.equals("contact4")){
                this.defaultPhoneNo = this.contactPhone4;
                this.defaultPhoneNoExt = this.contactExt4;
                this.defaultPhoneNoHide = this.contactPhone4Hide;
                this.defaultPhoneNoExtHide = this.contactExt4;
            }else if(this.telephoneDefault.equals("contact5")){
                this.defaultPhoneNo = this.contactPhone5;
                this.defaultPhoneNoExt = this.contactExt5;
                this.defaultPhoneNoHide = this.contactPhone5Hide;
                this.defaultPhoneNoExtHide = this.contactExt5;
            }    
        }else{
            if(this.mobilePhone != null && !this.mobilePhone.equals("")){
                this.defaultPhoneNo = this.mobilePhone;
                this.defaultPhoneNoExt = "";
                this.defaultPhoneNoHide = this.mobilePhoneHide;
                this.defaultPhoneNoExtHide = "";
            }else if(this.officePhone != null && !this.officePhone.equals("")){
                this.defaultPhoneNo = this.officePhone;
                this.defaultPhoneNoExt = this.officeExt;
                this.defaultPhoneNoHide = this.officePhoneHide;
                this.defaultPhoneNoExtHide = this.officeExt;
            }else if(this.homePhone != null && !this.homePhone.equals("")){
                this.defaultPhoneNo = this.homePhone;
                this.defaultPhoneNoExt = this.homeExt;
                this.defaultPhoneNoHide = this.homePhoneHide;
                this.defaultPhoneNoExtHide = this.homeExt;
            }
        }
    }
    
    public CustomerInfoValue(
            Integer id, String referenceNo, Integer accountId,String accountName, String initial, String name, String surname,Integer cardTypeId, String cardType, String idno, String gender,
            Date dob, Integer creditLimit, Integer creditTerm, String privilege, Double weight, Double height, String nationality,
            String occupation, String homePhone, String homeExt, String officePhone, String officeExt, String mobilePhone,
            String fax, String faxExt, String email, Integer customerTypeid, String customerType, 
            String currentFullname, String currentAddressLine1, String currentAddressLine2, Subdistrict currentSubdistrict,
            District currentDistrict, String currentPostalCode, 
            String homeFullname, String homeAddressLine1, String homeAddressLine2, Subdistrict homeSubdistrict, 
            District homeDistrict, String homePostalCode, 
            String billingFullname, String billingAddressLine1, String billingAddressLine2, Subdistrict billingSubdistrict, 
            District billingDistrict, String billingPostalCode, 
            String shippingFullname, String shippingAddressLine1, String shippingAddressLine2,  Subdistrict shippingSubdistrict, 
            District shippingDistrict, String shippingPostalCode, 
            String picture, String emoticon, String remark, Long totalCases, Long pendingCases,
            Long closedCases, String flexfield1, String flexfield2, String flexfield3, String flexfield4, String flexfield5, 
            String flexfield6, String flexfield7, String flexfield8, String flexfield9, String flexfield10,
            String flexfield11, String flexfield12, String flexfield13, String flexfield14, String flexfield15,
            String flexfield16, String flexfield17, String flexfield18, String flexfield19, String flexfield20,
            String flexfield21, String flexfield22, String flexfield23, String flexfield24, String flexfield25, 
            String flexfield26, String flexfield27, String flexfield28, String flexfield29, String flexfield30, 
            String flexfield31, String flexfield32, String flexfield33, String flexfield34, String flexfield35, 
            String flexfield36, String flexfield37, String flexfield38, String flexfield39, String flexfield40, 
            String flexfield41, String flexfield42, String flexfield43, String flexfield44, String flexfield45, 
            String flexfield46, String flexfield47, String flexfield48, String flexfield49, String flexfield50, 
            String flexfield51, String flexfield52, String flexfield53, String flexfield54, String flexfield55, 
            String flexfield56, String flexfield57, String flexfield58, String flexfield59, String flexfield60, 
            String flexfield61, String flexfield62, String flexfield63, String flexfield64, String flexfield65, 
            String flexfield66, String flexfield67, String flexfield68, String flexfield69, String flexfield70, 
            String flexfield71, String flexfield72, String flexfield73, String flexfield74, String flexfield75, 
            String flexfield76, String flexfield77, String flexfield78, String flexfield79, String flexfield80, 
            String flexfield81, String flexfield82, String flexfield83, String flexfield84, String flexfield85, 
            String flexfield86, String flexfield87, String flexfield88, String flexfield89, String flexfield90, 
            String flexfield91, String flexfield92, String flexfield93, String flexfield94, String flexfield95, 
            String flexfield96, String flexfield97, String flexfield98, String flexfield99, String flexfield100, 
            String contactPhone1, String contactExt1,
            String contactPhone2, String contactExt2,
            String contactPhone3, String contactExt3,
            String contactPhone4, String contactExt4,
            String contactPhone5, String contactExt5,
            String telephoneDefault,
            Boolean homePhoneClose,
            Boolean officePhoneClose,
            Boolean mobilePhoneClose,
            Boolean contactPhone1Close,
            Boolean contactPhone2Close,
            Boolean contactPhone3Close,
            Boolean contactPhone4Close,
            Boolean contactPhone5Close  
            ){
        this.id = id;
        this.referenceNo = referenceNo;
        this.accountId = accountId;
        this.accountName = accountName;
        this.initial = initial;
        this.name = name;
        this.surname = surname;
        this.cardTypeId = cardTypeId;
        this.cardType = cardType;
        this.idno = idno;
        this.gender = gender;
        this.dob = dob;

        if (dob != null) {
            DateUtil dateUtil = new DateUtil();
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.setTime(dob);
            this.age = dateUtil.getDateDiffInYear(calendar, Calendar.getInstance());
        } else {
            this.age = Long.valueOf("0");
        }

        this.creditLimit = creditLimit;
        this.creditTerm = creditTerm;
        this.privilege = privilege;
        this.weight = weight;
        this.height = height;
        this.nationality = nationality;
        this.occupation = occupation;
        this.homePhone = homePhone != null ? homePhone : "";
        this.homeExt = homeExt != null ? homeExt : "";
        this.officePhone = officePhone != null ? officePhone : "";
        this.officeExt = officeExt != null ? officeExt : "";
        this.mobilePhone = mobilePhone != null ? mobilePhone : "";
        this.fax = fax;
        this.faxExt = faxExt;
        this.email = email;
        this.customerTypeid = customerTypeid;
        this.customerType = customerType;
        this.currentFullname = currentFullname;
        this.currentAddressLine1 = currentAddressLine1;
        this.currentAddressLine2 = currentAddressLine2;
        if(currentSubdistrict != null)
            this.currentSubdistrictName = currentSubdistrict.getName();
        if(currentDistrict != null) {
            this.currentDistrictName = currentDistrict.getName();
            this.currentProvinceName = currentDistrict.getProvinceId().getName();
        }
        this.currentPostalCode = currentPostalCode;
        
        this.homeFullname = homeFullname;
        this.homeAddressLine1 = homeAddressLine1;
        this.homeAddressLine2 = homeAddressLine2;
        if(homeSubdistrict != null)
            this.homeSubdistrictName = homeSubdistrict.getName();
        if(homeDistrict != null) {
            this.homeDistrictName = homeDistrict.getName();
            this.homeProvinceName = homeDistrict.getProvinceId().getName();
        }
        this.homePostalCode = homePostalCode;
        
        this.billingFullname = billingFullname;
        this.billingAddressLine1 = billingAddressLine1;
        this.billingAddressLine2 = billingAddressLine2;
        if(billingSubdistrict != null)
            this.billingSubdistrictName = billingSubdistrict.getName();
        if(billingDistrict != null) {
            this.billingDistrictName = billingDistrict.getName();
            this.billingProvinceName = billingDistrict.getProvinceId().getName();
        }
        this.billingPostalCode = billingPostalCode;
        
        this.shippingFullname = shippingFullname;
        this.shippingAddressLine1 = shippingAddressLine1;
        this.shippingAddressLine2 = shippingAddressLine2;
        if(shippingSubdistrict != null)
            this.shippingSubdistrictName = shippingSubdistrict.getName();
        if(shippingDistrict != null) {
            this.shippingDistrictName = shippingDistrict.getName();
            this.shippingProvinceName = shippingDistrict.getProvinceId().getName();
        }
        this.shippingPostalCode = shippingPostalCode;
        
        this.picture = picture;
        this.emoticon = emoticon;
        this.remark = remark;
        this.totalCases = totalCases;
        this.pendingCases = pendingCases;
        this.closedCases = closedCases;
        this.flexfield1 = flexfield1;
        this.flexfield2 = flexfield2;
        this.flexfield3 = flexfield3;
        this.flexfield4 = flexfield4;
        this.flexfield5 = flexfield5;
        this.flexfield6 = flexfield6;
        this.flexfield7 = flexfield7;
        this.flexfield8 = flexfield8;
        this.flexfield9 = flexfield9;
        this.flexfield10 = flexfield10;
        this.flexfield11 = flexfield11;
        this.flexfield12 = flexfield12;
        this.flexfield13 = flexfield13;
        this.flexfield14 = flexfield14;
        this.flexfield15 = flexfield15;
        this.flexfield16 = flexfield16;
        this.flexfield17 = flexfield17;
        this.flexfield18 = flexfield18;
        this.flexfield19 = flexfield19;
        this.flexfield20 = flexfield20;
        this.flexfield21 = flexfield21;
        this.flexfield22 = flexfield22;
        this.flexfield23 = flexfield23;
        this.flexfield24 = flexfield24;
        this.flexfield25 = flexfield25;
        this.flexfield26 = flexfield26;
        this.flexfield27 = flexfield27;
        this.flexfield28 = flexfield28;
        this.flexfield29 = flexfield29;
        this.flexfield30 = flexfield30;
        this.flexfield31 = flexfield31;
        this.flexfield32 = flexfield32;
        this.flexfield33 = flexfield33;
        this.flexfield34 = flexfield34;
        this.flexfield35 = flexfield35;
        this.flexfield36 = flexfield36;
        this.flexfield37 = flexfield37;
        this.flexfield38 = flexfield38;
        this.flexfield39 = flexfield39;
        this.flexfield40 = flexfield40;
        this.flexfield41 = flexfield41;
        this.flexfield42 = flexfield42;
        this.flexfield43 = flexfield43;
        this.flexfield44 = flexfield44;
        this.flexfield45 = flexfield45;
        this.flexfield46 = flexfield46;
        this.flexfield47 = flexfield47;
        this.flexfield48 = flexfield48;
        this.flexfield49 = flexfield49;
        this.flexfield50 = flexfield50;
        this.flexfield51 = flexfield51;
        this.flexfield52 = flexfield52;
        this.flexfield53 = flexfield53;
        this.flexfield54 = flexfield54;
        this.flexfield55 = flexfield55;
        this.flexfield56 = flexfield56;
        this.flexfield57 = flexfield57;
        this.flexfield58 = flexfield58;
        this.flexfield59 = flexfield59;
        this.flexfield60 = flexfield60;
        this.flexfield61 = flexfield61;
        this.flexfield62 = flexfield62;
        this.flexfield63 = flexfield63;
        this.flexfield64 = flexfield64;
        this.flexfield65 = flexfield65;
        this.flexfield66 = flexfield66;
        this.flexfield67 = flexfield67;
        this.flexfield68 = flexfield68;
        this.flexfield69 = flexfield69;
        this.flexfield70 = flexfield70;
        this.flexfield71 = flexfield71;
        this.flexfield72 = flexfield72;
        this.flexfield73 = flexfield73;
        this.flexfield74 = flexfield74;
        this.flexfield75 = flexfield75;
        this.flexfield76 = flexfield76;
        this.flexfield77 = flexfield77;
        this.flexfield78 = flexfield78;
        this.flexfield79 = flexfield79;
        this.flexfield80 = flexfield80;
        this.flexfield81 = flexfield81;
        this.flexfield82 = flexfield82;
        this.flexfield83 = flexfield83;
        this.flexfield84 = flexfield84;
        this.flexfield85 = flexfield85;
        this.flexfield86 = flexfield86;
        this.flexfield87 = flexfield87;
        this.flexfield88 = flexfield88;
        this.flexfield89 = flexfield89;
        this.flexfield90 = flexfield90;
        this.flexfield91 = flexfield91;
        this.flexfield92 = flexfield92;
        this.flexfield93 = flexfield93;
        this.flexfield94 = flexfield94;
        this.flexfield95 = flexfield95;
        this.flexfield96 = flexfield96;
        this.flexfield97 = flexfield97;
        this.flexfield98 = flexfield98;
        this.flexfield99 = flexfield99;
        this.flexfield100 = flexfield100;
        
        this.contactPhone1 = contactPhone1 != null ? contactPhone1 : "";
        this.contactExt1 = contactExt1 != null ? contactExt1 : "";
        this.contactPhone2 = contactPhone2 != null ? contactPhone2 : "";
        this.contactExt2 = contactExt2 != null ? contactExt2 : "";
        this.contactPhone3 = contactPhone3 != null ? contactPhone3 : "";
        this.contactExt3 = contactExt3 != null ? contactExt3 : "";
        this.contactPhone4 = contactPhone4 != null ? contactPhone4 : "";
        this.contactExt4 = contactExt4 != null ? contactExt4 : "";
        this.contactPhone5 = contactPhone5 != null ? contactPhone5 : "";
        this.contactExt5 = contactExt5 != null ? contactExt5 : "";
        
        this.telephoneDefault = telephoneDefault;
        this.homePhoneClose = homePhoneClose;
        this.officePhoneClose = officePhoneClose;
        this.mobilePhoneClose = mobilePhoneClose;
        this.contactPhone1Close = contactPhone1Close;
        this.contactPhone2Close = contactPhone2Close;
        this.contactPhone3Close = contactPhone3Close;
        this.contactPhone4Close = contactPhone4Close;
        this.contactPhone5Close = contactPhone5Close;

        this.mobilePhoneHide = getHidePhoneNo(this.mobilePhone);
        this.homePhoneHide = getHidePhoneNo(this.homePhone);
        this.officePhoneHide = getHidePhoneNo(this.officePhone);
        this.contactPhone1Hide = getHidePhoneNo(this.contactPhone1);
        this.contactPhone2Hide = getHidePhoneNo(this.contactPhone2);
        this.contactPhone3Hide = getHidePhoneNo(this.contactPhone3);
        this.contactPhone4Hide = getHidePhoneNo(this.contactPhone4);
        this.contactPhone5Hide = getHidePhoneNo(this.contactPhone5);
        
        if(telephoneDefault != null && !telephoneDefault.equals("")){
            
            if(telephoneDefault.equals("mobile")){
                this.defaultPhoneNo = this.mobilePhone;
                this.defaultPhoneNoExt = "";
                this.defaultPhoneNoHide = this.mobilePhoneHide;
                this.defaultPhoneNoExtHide = "";
            }else if(telephoneDefault.equals("office")){
                this.defaultPhoneNo = this.officePhone;
                this.defaultPhoneNoExt = this.officeExt;
                this.defaultPhoneNoHide = this.officePhoneHide;
                this.defaultPhoneNoExtHide = this.officeExt;
            }else if(telephoneDefault.equals("home")){
                this.defaultPhoneNo = this.homePhone;
                this.defaultPhoneNoExt = this.homeExt;
                this.defaultPhoneNoHide = this.homePhoneHide;
                this.defaultPhoneNoExtHide = this.homeExt;
            }else if(telephoneDefault.equals("contact1")){
                this.defaultPhoneNo = this.contactPhone1;
                this.defaultPhoneNoExt = this.contactExt1;
                this.defaultPhoneNoHide = this.contactPhone1Hide;
                this.defaultPhoneNoExtHide = this.contactExt1;
            }else if(telephoneDefault.equals("contact2")){
                this.defaultPhoneNo = this.contactPhone2;
                this.defaultPhoneNoExt = this.contactExt2;
                this.defaultPhoneNoHide = this.contactPhone2Hide;
                this.defaultPhoneNoExtHide = this.contactExt2;
            }else if(telephoneDefault.equals("contact3")){
                this.defaultPhoneNo = this.contactPhone3;
                this.defaultPhoneNoExt = this.contactExt3;
                this.defaultPhoneNoHide = this.contactPhone3Hide;
                this.defaultPhoneNoExtHide = this.contactExt3;
            }else if(telephoneDefault.equals("contact4")){
                this.defaultPhoneNo = this.contactPhone4;
                this.defaultPhoneNoExt = this.contactExt4;
                this.defaultPhoneNoHide = this.contactPhone4Hide;
                this.defaultPhoneNoExtHide = this.contactExt4;
            }else if(telephoneDefault.equals("contact5")){
                this.defaultPhoneNo = this.contactPhone5;
                this.defaultPhoneNoExt = this.contactExt5;
                this.defaultPhoneNoHide = this.contactPhone5Hide;
                this.defaultPhoneNoExtHide = this.contactExt5;
    }
        }else{
            if(this.mobilePhone != null && !this.mobilePhone.equals("")){
                this.defaultPhoneNo = this.mobilePhone;
                this.defaultPhoneNoExt = "";
                this.defaultPhoneNoHide = this.mobilePhoneHide;
                this.defaultPhoneNoExtHide = "";
            }else if(this.officePhone != null && !this.officePhone.equals("")){
                this.defaultPhoneNo = this.officePhone;
                this.defaultPhoneNoExt = this.officeExt;
                this.defaultPhoneNoHide = this.officePhoneHide;
                this.defaultPhoneNoExtHide = this.officeExt;
            }else if(this.homePhone != null && !this.homePhone.equals("")){
                this.defaultPhoneNo = this.homePhone;
                this.defaultPhoneNoExt = this.homeExt;
                this.defaultPhoneNoHide = this.homePhoneHide;
                this.defaultPhoneNoExtHide = this.homeExt;
            }
        }
    
    }
    
    //for t1c
    public CustomerInfoValue(Integer id, Long totalCases, Long pendingCases,Long closedCases){
        this.id = id;
        this.totalCases = totalCases;
        this.pendingCases = pendingCases;
        this.closedCases = closedCases;
    }

    private String getHidePhoneNo(String no){
        String str = no;
        String hide = JSFUtil.getUserSession().getUserGroup().getHidePhoneNo();
        if(hide != null && !hide.equals("") && no != null && !no.equals("") && no.substring(0, 1).equals("0")){
            if(no.length() == 10){
                if(hide.equals("hideall")){
                    str = no.replaceAll(no, "**********");
                }else if(hide.equals("hideleft")){
                    str = "***" + no.substring(3);
                }else if(hide.equals("hideright")){
                    str = no.substring(0, no.length() - 3) + "***";
                }else if(hide.equals("hidecenter")){
                    str = no.substring(0, 3) + "***" + no.substring(6, no.length());
                }
            }else if(no.length() == 9){
                if(hide.equals("hideall")){
                    str = no.replaceAll(no, "*********");
                }else if(hide.equals("hideleft")){
                    str = "***" + no.substring(3);
                }else if(hide.equals("hideright")){
                    str = no.substring(0, no.length() - 3) + "***";
                }else if(hide.equals("hidecenter")){
                    str = no.substring(0, 2) + "***" + no.substring(5, no.length());
                }
            }
        }
        
        return str;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the accountId
     */
    public Integer getAccountId() {
        return accountId;
    }

    /**
     * @param accountId the accountId to set
     */
    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }
    /**
     * @return the accountName
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * @param accountName 
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    /**
     * @return the initial
     */
    public String getInitial() {
        return initial;
    }

    /**
     * @param initial the initial to set
     */
    public void setInitial(String initial) {
        this.initial = initial;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @param surname the surname to set
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * @return the idno
     */
    public String getIdno() {
        return idno;
    }

    /**
     * @param idno the idno to set
     */
    public void setIdno(String idno) {
        this.idno = idno;
    }

    /**
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return the dob
     */
    public Date getDob() {
        return dob;
    }

    /**
     * @param dob the dob to set
     */
    public void setDob(Date dob) {
        this.dob = dob;
    }

    /**
     * @return the age
     */
    public Long getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(Long age) {
        this.age = age;
    }

    /**
     * @return the creditLimit
     */
    public Integer getCreditLimit() {
        return creditLimit;
    }

    /**
     * @param creditLimit the creditLimit to set
     */
    public void setCreditLimit(Integer creditLimit) {
        this.creditLimit = creditLimit;
    }

    /**
     * @return the creditTerm
     */
    public Integer getCreditTerm() {
        return creditTerm;
    }

    /**
     * @param creditTerm the creditTerm to set
     */
    public void setCreditTerm(Integer creditTerm) {
        this.creditTerm = creditTerm;
    }

    /**
     * @return the priority
     */
    public String getPrivilege() {
        return privilege;
    }

    /**
     * @param priority the priority to set
     */
    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    /**
     * @return the weight
     */
    public Double getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(Double weight) {
        this.weight = weight;
    }

    /**
     * @return the height
     */
    public Double getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(Double height) {
        this.height = height;
    }

    /**
     * @return the nationality
     */
    public String getNationality() {
        return nationality;
    }

    /**
     * @param nationality the nationality to set
     */
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    /**
     * @return the occupation
     */
    public String getOccupation() {
        return occupation;
    }

    /**
     * @param occupation the occupation to set
     */
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    /**
     * @return the homePhone
     */
    public String getHomePhone() {
        return homePhone;
    }

    /**
     * @param homePhone the homePhone to set
     */
    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    /**
     * @return the homeExt
     */
    public String getHomeExt() {
        return homeExt;
    }

    /**
     * @param homeExt the homeExt to set
     */
    public void setHomeExt(String homeExt) {
        this.homeExt = homeExt;
    }

    /**
     * @return the officePhone
     */
    public String getOfficePhone() {
        return officePhone;
    }

    /**
     * @param officePhone the officePhone to set
     */
    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    /**
     * @return the officeExt
     */
    public String getOfficeExt() {
        return officeExt;
    }

    /**
     * @param officeExt the officeExt to set
     */
    public void setOfficeExt(String officeExt) {
        this.officeExt = officeExt;
    }

    /**
     * @return the mobilePhone
     */
    public String getMobilePhone() {
        return mobilePhone;
    }

    /**
     * @param mobilePhone the mobilePhone to set
     */
    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    /**
     * @return the fax
     */
    public String getFax() {
        return fax;
    }

    /**
     * @param fax the fax to set
     */
    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the customerTypeid
     */
    public Integer getCustomerTypeid() {
        return customerTypeid;
    }

    /**
     * @param customerTypeid the customerTypeid to set
     */
    public void setCustomerTypeid(Integer customerTypeid) {
        this.customerTypeid = customerTypeid;
    }

    /**
     * @return the customerType
     */
    public String getCustomerType() {
        return customerType;
    }

    /**
     * @param customerType the customerType to set
     */
    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    /**
     * @return the currentFullname
     */
    public String getCurrentFullname() {
        return currentFullname;
    }

    /**
     * @param currentFullname the currentFullname to set
     */
    public void setCurrentFullname(String currentFullname) {
        this.currentFullname = currentFullname;
    }

    /**
     * @return the currentAddressLine1
     */
    public String getCurrentAddressLine1() {
        return currentAddressLine1;
    }

    /**
     * @param currentAddressLine1 the currentAddressLine1 to set
     */
    public void setCurrentAddressLine1(String currentAddressLine1) {
        this.currentAddressLine1 = currentAddressLine1;
    }

    /**
     * @return the currentAddressLine2
     */
    public String getCurrentAddressLine2() {
        return currentAddressLine2;
    }

    /**
     * @param currentAddressLine2 the currentAddressLine2 to set
     */
    public void setCurrentAddressLine2(String currentAddressLine2) {
        this.currentAddressLine2 = currentAddressLine2;
    }

    /**
     * @return the currentPostalCode
     */
    public String getCurrentPostalCode() {
        return currentPostalCode;
    }

    /**
     * @param currentPostalCode the currentPostalCode to set
     */
    public void setCurrentPostalCode(String currentPostalCode) {
        this.currentPostalCode = currentPostalCode;
    }

    /**
     * @return the picture
     */
    public String getPicture() {
        return picture;
    }

    /**
     * @param picture the picture to set
     */
    public void setPicture(String picture) {
        this.picture = picture;
    }

    /**
     * @return the emoticon
     */
    public String getEmoticon() {
        return emoticon;
    }

    /**
     * @param emoticon the emoticon to set
     */
    public void setEmoticon(String emoticon) {
        this.emoticon = emoticon;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return the totalCases
     */
    public Long getTotalCases() {
        return totalCases;
    }

    /**
     * @param totalCases the totalCases to set
     */
    public void setTotalCases(Long totalCases) {
        this.totalCases = totalCases;
    }

    /**
     * @return the pendingCases
     */
    public Long getPendingCases() {
        return pendingCases;
    }

    /**
     * @param pendingCases the pendingCases to set
     */
    public void setPendingCases(Long pendingCases) {
        this.pendingCases = pendingCases;
    }

    /**
     * @return the closedCases
     */
    public Long getClosedCases() {
        return closedCases;
    }

    /**
     * @param closedCases the closedCases to set
     */
    public void setClosedCases(Long closedCases) {
        this.closedCases = closedCases;
    }

    public String getFaxExt() {
        return faxExt;
    }

    public void setFaxExt(String faxExt) {
        this.faxExt = faxExt;
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

    public String getBillingFullname() {
        return billingFullname;
    }

    public void setBillingFullname(String billingFullname) {
        this.billingFullname = billingFullname;
    }

    public String getBillingPostalCode() {
        return billingPostalCode;
    }

    public void setBillingPostalCode(String billingPostalCode) {
        this.billingPostalCode = billingPostalCode;
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

    public String getHomeFullname() {
        return homeFullname;
    }

    public void setHomeFullname(String homeFullname) {
        this.homeFullname = homeFullname;
    }

    public String getHomePostalCode() {
        return homePostalCode;
    }

    public void setHomePostalCode(String homePostalCode) {
        this.homePostalCode = homePostalCode;
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

    public String getShippingFullname() {
        return shippingFullname;
    }

    public void setShippingFullname(String shippingFullname) {
        this.shippingFullname = shippingFullname;
    }

    public String getShippingPostalCode() {
        return shippingPostalCode;
    }

    public void setShippingPostalCode(String shippingPostalCode) {
        this.shippingPostalCode = shippingPostalCode;
    }

    public Integer getCardTypeId() {
        return cardTypeId;
    }

    public void setCardTypeId(Integer cardTypeId) {
        this.cardTypeId = cardTypeId;
    }
    
    
    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
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

    public Boolean getHomePhoneClose() {
        return homePhoneClose;
    }

    public void setHomePhoneClose(Boolean homePhoneClose) {
        this.homePhoneClose = homePhoneClose;
    }

    public Boolean getMobilePhoneClose() {
        return mobilePhoneClose;
    }

    public void setMobilePhoneClose(Boolean mobilePhoneClose) {
        this.mobilePhoneClose = mobilePhoneClose;
    }

    public Boolean getOfficePhoneClose() {
        return officePhoneClose;
    }

    public void setOfficePhoneClose(Boolean officePhoneClose) {
        this.officePhoneClose = officePhoneClose;
    }

    public String getTelephoneDefault() {
        return telephoneDefault;
    }

    public void setTelephoneDefault(String telephoneDefault) {
        this.telephoneDefault = telephoneDefault;
    }
    
    public String getEmotion() {
        return emotion;
}

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public void setShippingSubdistrict(String shippingSubdistrictName) {
        this.shippingSubdistrictName = shippingSubdistrictName;
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

    public String getBillingSubdistrictName() {
        return billingSubdistrictName;
    }

    public void setBillingSubdistrictName(String billingSubdistrictName) {
        this.billingSubdistrictName = billingSubdistrictName;
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

    public String getCurrentSubdistrictName() {
        return currentSubdistrictName;
    }

    public void setCurrentSubdistrictName(String currentSubdistrictName) {
        this.currentSubdistrictName = currentSubdistrictName;
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

    public String getHomeSubdistrictName() {
        return homeSubdistrictName;
    }

    public void setHomeSubdistrictName(String homeSubdistrictName) {
        this.homeSubdistrictName = homeSubdistrictName;
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

    public String getShippingSubdistrictName() {
        return shippingSubdistrictName;
    }

    public void setShippingSubdistrictName(String shippingSubdistrictName) {
        this.shippingSubdistrictName = shippingSubdistrictName;
    }

    public District getBillingDistrict() {
        return billingDistrict;
    }

    public void setBillingDistrict(District billingDistrict) {
        this.billingDistrict = billingDistrict;
    }

    public Subdistrict getBillingSubdistrict() {
        return billingSubdistrict;
    }

    public void setBillingSubdistrict(Subdistrict billingSubdistrict) {
        this.billingSubdistrict = billingSubdistrict;
    }

    public District getCurrentDistrict() {
        return currentDistrict;
    }

    public void setCurrentDistrict(District currentDistrict) {
        this.currentDistrict = currentDistrict;
    }

    public Subdistrict getCurrentSubdistrict() {
        return currentSubdistrict;
    }

    public void setCurrentSubdistrict(Subdistrict currentSubdistrict) {
        this.currentSubdistrict = currentSubdistrict;
    }

    public District getHomeDistrict() {
        return homeDistrict;
    }

    public void setHomeDistrict(District homeDistrict) {
        this.homeDistrict = homeDistrict;
    }

    public Subdistrict getHomeSubdistrict() {
        return homeSubdistrict;
    }

    public void setHomeSubdistrict(Subdistrict homeSubdistrict) {
        this.homeSubdistrict = homeSubdistrict;
    }

    public District getShippingDistrict() {
        return shippingDistrict;
    }

    public void setShippingDistrict(District shippingDistrict) {
        this.shippingDistrict = shippingDistrict;
    }

    public Subdistrict getShippingSubdistrict() {
        return shippingSubdistrict;
    }

    public void setShippingSubdistrict(Subdistrict shippingSubdistrict) {
        this.shippingSubdistrict = shippingSubdistrict;
    }

    public String getMobilePhoneHide() {
        return mobilePhoneHide;
    }
    
    public void setMobilePhoneHide(String mobilePhoneHide) {
        this.mobilePhoneHide = mobilePhoneHide;
}

    public String getHomePhoneHide() {
        return homePhoneHide;
    }

    public void setHomePhoneHide(String homePhoneHide) {
        this.homePhoneHide = homePhoneHide;
    }

    public String getOfficePhoneHide() {
        return officePhoneHide;
    }

    public void setOfficePhoneHide(String officePhoneHide) {
        this.officePhoneHide = officePhoneHide;
    }

    public String getContactPhone1Hide() {
        return contactPhone1Hide;
    }

    public void setContactPhone1Hide(String contactPhone1Hide) {
        this.contactPhone1Hide = contactPhone1Hide;
    }

    public String getContactPhone2Hide() {
        return contactPhone2Hide;
    }

    public void setContactPhone2Hide(String contactPhone2Hide) {
        this.contactPhone2Hide = contactPhone2Hide;
    }

    public String getContactPhone3Hide() {
        return contactPhone3Hide;
    }

    public void setContactPhone3Hide(String contactPhone3Hide) {
        this.contactPhone3Hide = contactPhone3Hide;
    }

    public String getContactPhone4Hide() {
        return contactPhone4Hide;
    }

    public void setContactPhone4Hide(String contactPhone4Hide) {
        this.contactPhone4Hide = contactPhone4Hide;
    }

    public String getContactPhone5Hide() {
        return contactPhone5Hide;
    }

    public void setContactPhone5Hide(String contactPhone5Hide) {
        this.contactPhone5Hide = contactPhone5Hide;
    }

    public String getDefaultPhoneNo() {
        return defaultPhoneNo;
    }

    public void setDefaultPhoneNo(String defaultPhoneNo) {
        this.defaultPhoneNo = defaultPhoneNo;
    }

    public String getDefaultPhoneNoExt() {
        return defaultPhoneNoExt;
    }

    public void setDefaultPhoneNoExt(String defaultPhoneNoExt) {
        this.defaultPhoneNoExt = defaultPhoneNoExt;
    }

    public String getDefaultPhoneNoHide() {
        return defaultPhoneNoHide;
    }

    public void setDefaultPhoneNoHide(String defaultPhoneNoHide) {
        this.defaultPhoneNoHide = defaultPhoneNoHide;
    }

    public String getDefaultPhoneNoExtHide() {
        return defaultPhoneNoExtHide;
    }

    public void setDefaultPhoneNoExtHide(String defaultPhoneNoExtHide) {
        this.defaultPhoneNoExtHide = defaultPhoneNoExtHide;
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
    
    
}
