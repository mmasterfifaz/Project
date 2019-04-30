/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "marketing_temp_customer")
@NamedQueries({
    @NamedQuery(name = "MarketingTempCustomer.findAll", query = "SELECT m FROM MarketingTempCustomer m")})
public class MarketingTempCustomer implements Serializable {
    @Column(name = "weight")
    private Double weight;
    @Column(name = "height")
    private Double height;
    @Size(max = 50)
    @Column(name = "fax_ext")
    private String faxExt;
    @Size(max = 500)
    @Column(name = "privilege")
    private String privilege;
    @Size(max = 200)
    @Column(name = "current_address_line3")
    private String currentAddressLine3;
    @Size(max = 200)
    @Column(name = "current_address_line4")
    private String currentAddressLine4;
    @Size(max = 200)
    @Column(name = "current_address_line5")
    private String currentAddressLine5;
    @Size(max = 200)
    @Column(name = "current_address_line6")
    private String currentAddressLine6;
    @Size(max = 200)
    @Column(name = "current_address_line7")
    private String currentAddressLine7;
    @Size(max = 200)
    @Column(name = "current_address_line8")
    private String currentAddressLine8;
    @Size(max = 200)
    @Column(name = "current_country_name")
    private String currentCountryName;
    @Size(max = 200)
    @Column(name = "home_address_line3")
    private String homeAddressLine3;
    @Size(max = 200)
    @Column(name = "home_address_line4")
    private String homeAddressLine4;
    @Size(max = 200)
    @Column(name = "home_address_line5")
    private String homeAddressLine5;
    @Size(max = 200)
    @Column(name = "home_address_line6")
    private String homeAddressLine6;
    @Size(max = 200)
    @Column(name = "home_address_line7")
    private String homeAddressLine7;
    @Size(max = 200)
    @Column(name = "home_address_line8")
    private String homeAddressLine8;
    @Size(max = 200)
    @Column(name = "home_country_name")
    private String homeCountryName;
    @Size(max = 200)
    @Column(name = "billing_address_line3")
    private String billingAddressLine3;
    @Size(max = 200)
    @Column(name = "billing_address_line4")
    private String billingAddressLine4;
    @Size(max = 200)
    @Column(name = "billing_address_line5")
    private String billingAddressLine5;
    @Size(max = 200)
    @Column(name = "billing_address_line6")
    private String billingAddressLine6;
    @Size(max = 200)
    @Column(name = "billing_address_line7")
    private String billingAddressLine7;
    @Size(max = 200)
    @Column(name = "billing_address_line8")
    private String billingAddressLine8;
    @Size(max = 200)
    @Column(name = "billing_country_name")
    private String billingCountryName;
    @Size(max = 200)
    @Column(name = "shipping_address_line3")
    private String shippingAddressLine3;
    @Size(max = 200)
    @Column(name = "shipping_address_line4")
    private String shippingAddressLine4;
    @Size(max = 200)
    @Column(name = "shipping_address_line5")
    private String shippingAddressLine5;
    @Size(max = 200)
    @Column(name = "shipping_address_line6")
    private String shippingAddressLine6;
    @Size(max = 200)
    @Column(name = "shipping_address_line7")
    private String shippingAddressLine7;
    @Size(max = 200)
    @Column(name = "shipping_address_line8")
    private String shippingAddressLine8;
    @Size(max = 200)
    @Column(name = "shipping_country_name")
    private String shippingCountryName;
    @Size(max = 50)
    @Column(name = "contact_ext1")
    private String contactExt1;
    @Size(max = 50)
    @Column(name = "contact_ext2")
    private String contactExt2;
    @Size(max = 50)
    @Column(name = "contact_ext3")
    private String contactExt3;
    @Size(max = 50)
    @Column(name = "contact_ext4")
    private String contactExt4;
    @Size(max = 50)
    @Column(name = "contact_ext5")
    private String contactExt5;
    @Size(max = 1000)
    @Column(name = "list_value_001")
    private String listValue001;
    @Size(max = 1000)
    @Column(name = "list_value_002")
    private String listValue002;
    @Size(max = 1000)
    @Column(name = "list_value_003")
    private String listValue003;
    @Size(max = 1000)
    @Column(name = "list_value_004")
    private String listValue004;
    @Size(max = 1000)
    @Column(name = "list_value_005")
    private String listValue005;
    @Size(max = 1000)
    @Column(name = "list_value_006")
    private String listValue006;
    @Size(max = 1000)
    @Column(name = "list_value_007")
    private String listValue007;
    @Size(max = 1000)
    @Column(name = "list_value_008")
    private String listValue008;
    @Size(max = 1000)
    @Column(name = "list_value_009")
    private String listValue009;
    @Size(max = 1000)
    @Column(name = "list_value_010")
    private String listValue010;
    @Size(max = 1000)
    @Column(name = "list_value_011")
    private String listValue011;
    @Size(max = 1000)
    @Column(name = "list_value_012")
    private String listValue012;
    @Size(max = 1000)
    @Column(name = "list_value_013")
    private String listValue013;
    @Size(max = 1000)
    @Column(name = "list_value_014")
    private String listValue014;
    @Size(max = 1000)
    @Column(name = "list_value_015")
    private String listValue015;
    @Size(max = 1000)
    @Column(name = "list_value_016")
    private String listValue016;
    @Size(max = 1000)
    @Column(name = "list_value_017")
    private String listValue017;
    @Size(max = 1000)
    @Column(name = "list_value_018")
    private String listValue018;
    @Size(max = 1000)
    @Column(name = "list_value_019")
    private String listValue019;
    @Size(max = 1000)
    @Column(name = "list_value_020")
    private String listValue020;
    @Size(max = 1000)
    @Column(name = "list_value_021")
    private String listValue021;
    @Size(max = 1000)
    @Column(name = "list_value_022")
    private String listValue022;
    @Size(max = 1000)
    @Column(name = "list_value_023")
    private String listValue023;
    @Size(max = 1000)
    @Column(name = "list_value_024")
    private String listValue024;
    @Size(max = 1000)
    @Column(name = "list_value_025")
    private String listValue025;
    @Size(max = 1000)
    @Column(name = "list_value_026")
    private String listValue026;
    @Size(max = 1000)
    @Column(name = "list_value_027")
    private String listValue027;
    @Size(max = 1000)
    @Column(name = "list_value_028")
    private String listValue028;
    @Size(max = 1000)
    @Column(name = "list_value_029")
    private String listValue029;
    @Size(max = 1000)
    @Column(name = "list_value_030")
    private String listValue030;
    @Size(max = 1000)
    @Column(name = "list_value_031")
    private String listValue031;
    @Size(max = 1000)
    @Column(name = "list_value_032")
    private String listValue032;
    @Size(max = 1000)
    @Column(name = "list_value_033")
    private String listValue033;
    @Size(max = 1000)
    @Column(name = "list_value_034")
    private String listValue034;
    @Size(max = 1000)
    @Column(name = "list_value_035")
    private String listValue035;
    @Size(max = 1000)
    @Column(name = "list_value_036")
    private String listValue036;
    @Size(max = 1000)
    @Column(name = "list_value_037")
    private String listValue037;
    @Size(max = 1000)
    @Column(name = "list_value_038")
    private String listValue038;
    @Size(max = 1000)
    @Column(name = "list_value_039")
    private String listValue039;
    @Size(max = 1000)
    @Column(name = "list_value_040")
    private String listValue040;
    @Size(max = 1000)
    @Column(name = "list_value_041")
    private String listValue041;
    @Size(max = 1000)
    @Column(name = "list_value_042")
    private String listValue042;
    @Size(max = 1000)
    @Column(name = "list_value_043")
    private String listValue043;
    @Size(max = 1000)
    @Column(name = "list_value_044")
    private String listValue044;
    @Size(max = 1000)
    @Column(name = "list_value_045")
    private String listValue045;
    @Size(max = 1000)
    @Column(name = "list_value_046")
    private String listValue046;
    @Size(max = 1000)
    @Column(name = "list_value_047")
    private String listValue047;
    @Size(max = 1000)
    @Column(name = "list_value_048")
    private String listValue048;
    @Size(max = 1000)
    @Column(name = "list_value_049")
    private String listValue049;
    @Size(max = 1000)
    @Column(name = "list_value_050")
    private String listValue050;
    @Size(max = 1000)
    @Column(name = "list_value_051")
    private String listValue051;
    @Size(max = 1000)
    @Column(name = "list_value_052")
    private String listValue052;
    @Size(max = 1000)
    @Column(name = "list_value_053")
    private String listValue053;
    @Size(max = 1000)
    @Column(name = "list_value_054")
    private String listValue054;
    @Size(max = 1000)
    @Column(name = "list_value_055")
    private String listValue055;
    @Size(max = 1000)
    @Column(name = "list_value_056")
    private String listValue056;
    @Size(max = 1000)
    @Column(name = "list_value_057")
    private String listValue057;
    @Size(max = 1000)
    @Column(name = "list_value_058")
    private String listValue058;
    @Size(max = 1000)
    @Column(name = "list_value_059")
    private String listValue059;
    @Size(max = 1000)
    @Column(name = "list_value_060")
    private String listValue060;
    @Size(max = 1000)
    @Column(name = "list_value_061")
    private String listValue061;
    @Size(max = 1000)
    @Column(name = "list_value_062")
    private String listValue062;
    @Size(max = 1000)
    @Column(name = "list_value_063")
    private String listValue063;
    @Size(max = 1000)
    @Column(name = "list_value_064")
    private String listValue064;
    @Size(max = 1000)
    @Column(name = "list_value_065")
    private String listValue065;
    @Size(max = 1000)
    @Column(name = "list_value_066")
    private String listValue066;
    @Size(max = 1000)
    @Column(name = "list_value_067")
    private String listValue067;
    @Size(max = 1000)
    @Column(name = "list_value_068")
    private String listValue068;
    @Size(max = 1000)
    @Column(name = "list_value_069")
    private String listValue069;
    @Size(max = 1000)
    @Column(name = "list_value_070")
    private String listValue070;
    @Size(max = 1000)
    @Column(name = "list_value_071")
    private String listValue071;
    @Size(max = 1000)
    @Column(name = "list_value_072")
    private String listValue072;
    @Size(max = 1000)
    @Column(name = "list_value_073")
    private String listValue073;
    @Size(max = 1000)
    @Column(name = "list_value_074")
    private String listValue074;
    @Size(max = 1000)
    @Column(name = "list_value_075")
    private String listValue075;
    @Size(max = 1000)
    @Column(name = "list_value_076")
    private String listValue076;
    @Size(max = 1000)
    @Column(name = "list_value_077")
    private String listValue077;
    @Size(max = 1000)
    @Column(name = "list_value_078")
    private String listValue078;
    @Size(max = 1000)
    @Column(name = "list_value_079")
    private String listValue079;
    @Size(max = 1000)
    @Column(name = "list_value_080")
    private String listValue080;
    @Size(max = 1000)
    @Column(name = "list_value_081")
    private String listValue081;
    @Size(max = 1000)
    @Column(name = "list_value_082")
    private String listValue082;
    @Size(max = 1000)
    @Column(name = "list_value_083")
    private String listValue083;
    @Size(max = 1000)
    @Column(name = "list_value_084")
    private String listValue084;
    @Size(max = 1000)
    @Column(name = "list_value_085")
    private String listValue085;
    @Size(max = 1000)
    @Column(name = "list_value_086")
    private String listValue086;
    @Size(max = 1000)
    @Column(name = "list_value_087")
    private String listValue087;
    @Size(max = 1000)
    @Column(name = "list_value_088")
    private String listValue088;
    @Size(max = 1000)
    @Column(name = "list_value_089")
    private String listValue089;
    @Size(max = 1000)
    @Column(name = "list_value_090")
    private String listValue090;
    @Size(max = 1000)
    @Column(name = "list_value_091")
    private String listValue091;
    @Size(max = 1000)
    @Column(name = "list_value_092")
    private String listValue092;
    @Size(max = 1000)
    @Column(name = "list_value_093")
    private String listValue093;
    @Size(max = 1000)
    @Column(name = "list_value_094")
    private String listValue094;
    @Size(max = 1000)
    @Column(name = "list_value_095")
    private String listValue095;
    @Size(max = 1000)
    @Column(name = "list_value_096")
    private String listValue096;
    @Size(max = 1000)
    @Column(name = "list_value_097")
    private String listValue097;
    @Size(max = 1000)
    @Column(name = "list_value_098")
    private String listValue098;
    @Size(max = 1000)
    @Column(name = "list_value_099")
    private String listValue099;
    @Size(max = 1000)
    @Column(name = "list_value_100")
    private String listValue100;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "account_id")
    private Integer accountId;
    @Column(name = "reference_no", length = 50)
    private String referenceNo;
    @Column(name = "initial", length = 50)
    private String initial;
    @Column(name = "name", length = 100)
    private String name;
    @Column(name = "surname", length = 100)
    private String surname;
    @Column(name = "idcard_type_id")
    private Integer idcardTypeId;
    @Column(name = "idno", length = 50)
    private String idno;
    @Column(name = "gender", length = 10)
    private String gender;
    @Column(name = "dob")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dob;
    @Column(name = "nationality", length = 50)
    private String nationality;
    @Column(name = "occupation", length = 100)
    private String occupation;
    @Column(name = "home_phone", length = 100)
    private String homePhone;
    @Column(name = "home_ext", length = 50)
    private String homeExt;
    @Column(name = "office_phone", length = 100)
    private String officePhone;
    @Column(name = "office_ext", length = 50)
    private String officeExt;
    @Column(name = "mobile_phone", length = 100)
    private String mobilePhone;
    @Column(name = "fax", length = 100)
    private String fax;
    @Column(name = "email", length = 100)
    private String email;
    @Column(name = "customer_type")
    private Integer customerType;
    @Column(name = "current_fullname", length = 100)
    private String currentFullname;
    @Column(name = "current_address_line1", length = 200)
    private String currentAddressLine1;
    @Column(name = "current_address_line2", length = 200)
    private String currentAddressLine2;
    @Column(name = "current_district_name")
    private String currentDistrictName;
    @Column(name = "current_province_name")
    private String currentProvinceName;
    //@Column(name = "current_district_id")
    //private Integer currentDistrictId;
    @Column(name = "current_postal_code", length = 5)
    private String currentPostalCode;
    @Column(name = "home_fullname", length = 100)
    private String homeFullname;
    @Column(name = "home_address_line1", length = 200)
    private String homeAddressLine1;
    @Column(name = "home_address_line2", length = 200)
    private String homeAddressLine2;
    @Column(name = "home_district_name")
    private String homeDistrictName;
    @Column(name = "home_province_name")
    private String homeProvinceName;
    //@Column(name = "home_district_id")
    //private Integer homeDistrictId;
    @Column(name = "home_postal_code", length = 5)
    private String homePostalCode;
    @Column(name = "billing_fullname", length = 100)
    private String billingFullname;
    @Column(name = "billing_address_line1", length = 200)
    private String billingAddressLine1;
    @Column(name = "billing_address_line2", length = 200)
    private String billingAddressLine2;
    @Column(name = "billing_district_name")
    private String billingDistrictName;
    @Column(name = "billing_province_name")
    private String billingProvinceName;
    //@Column(name = "billing_district_id")
    //private Integer billingDistrictId;
    @Column(name = "billing_postal_code", length = 5)
    private String billingPostalCode;
    @Column(name = "shipping_fullname", length = 100)
    private String shippingFullname;
    @Column(name = "shipping_address_line1", length = 200)
    private String shippingAddressLine1;
    @Column(name = "shipping_address_line2", length = 200)
    private String shippingAddressLine2;
    @Column(name = "shipping_district_name")
    private String shippingDistrictName;
    @Column(name = "shipping_province_name")
    private String shippingProvinceName;
    //@Column(name = "shipping_district_id")
    //private Integer shippingDistrictId;
    @Column(name = "shipping_postal_code", length = 5)
    private String shippingPostalCode;
    @Column(name = "contact_phone1", length = 100)
    private String contactPhone1;
    @Column(name = "contact_phone2", length = 100)
    private String contactPhone2;
    @Column(name = "contact_phone3", length = 100)
    private String contactPhone3;
    @Column(name = "contact_phone4", length = 100)
    private String contactPhone4;
    @Column(name = "contact_phone5", length = 100)
    private String contactPhone5;
    @Column(name = "emoicon", length = 50)
    private String emoicon;
    //@Column(name = "emotion", length = 50)
    //private String emotion;
    @Column(name = "picture", length = 50)
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
    @Basic(optional = false)
    @Column(name = "session_id", nullable = false, length = 100)
    private String sessionId;
    @Basic(optional = false)
    @Column(name = "list_status", nullable = false, length = 100)
    private String listStatus;
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @ManyToOne
    private Customer customer;
    
    @Column(name = "dup_within")
    private Boolean dupWithin;
    @Column(name = "dup_yes")
    private Boolean dupYes;
    @Column(name = "dup")
    private Boolean dup;
    @Column(name = "opout")
    private Boolean opout;
    @Column(name = "blacklist")
    private Boolean blacklist;
    @Column(name = "badphone")
    private Boolean badphone;
    @Column(name = "badformat")
    private Boolean badformat;
    @Column(name = "dnc")
    private Boolean dnc;
    @Column(name = "mib")
    private Boolean mib;
    @Column(name = "marketing_id")
    private Integer marketingId;
    
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

    public MarketingTempCustomer() {
    }

    public MarketingTempCustomer(Integer id) {
        this.id = id;
    }

    public MarketingTempCustomer(Integer id, String sessionId, String listStatus) {
        this.id = id;
        this.sessionId = sessionId;
        this.listStatus = listStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Integer customerType) {
        this.customerType = customerType;
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

//    public Integer getCurrentDistrictId() {
//        return currentDistrictId;
//    }
//
//    public void setCurrentDistrictId(Integer currentDistrictId) {
//        this.currentDistrictId = currentDistrictId;
//    }

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

//    public Integer getHomeDistrictId() {
//        return homeDistrictId;
//    }
//
//    public void setHomeDistrictId(Integer homeDistrictId) {
//        this.homeDistrictId = homeDistrictId;
//    }

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

//    public Integer getBillingDistrictId() {
//        return billingDistrictId;
//    }
//
//    public void setBillingDistrictId(Integer billingDistrictId) {
//        this.billingDistrictId = billingDistrictId;
//    }

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

//    public Integer getShippingDistrictId() {
//        return shippingDistrictId;
//    }
//
//    public void setShippingDistrictId(Integer shippingDistrictId) {
//        this.shippingDistrictId = shippingDistrictId;
//    }

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

    public String getEmoicon() {
        return emoicon;
    }

    public void setEmoicon(String emoicon) {
        this.emoicon = emoicon;
    }

//    public String getEmotion() {
//        return emotion;
//    }
//
//    public void setEmotion(String emotion) {
//        this.emotion = emotion;
//    }

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

    
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getListStatus() {
        return listStatus;
    }

    public void setListStatus(String listStatus) {
        this.listStatus = listStatus;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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

    public Boolean getDupWithin() {
        return dupWithin;
    }

    public void setDupWithin(Boolean dupWithin) {
        this.dupWithin = dupWithin;
    }

    public Boolean getDupYes() {
        return dupYes;
    }

    public void setDupYes(Boolean dupYes) {
        this.dupYes = dupYes;
    }

    public Boolean getDup() {
        return dup;
    }

    public void setDup(Boolean dup) {
        this.dup = dup;
    }

    public Boolean getOpout() {
        return opout;
    }

    public void setOpout(Boolean opout) {
        this.opout = opout;
    }

    public Boolean getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(Boolean blacklist) {
        this.blacklist = blacklist;
    }

    public Boolean getBadphone() {
        return badphone;
    }

    public void setBadphone(Boolean badphone) {
        this.badphone = badphone;
    }

    public Integer getMarketingId() {
        return marketingId;
    }

    public void setMarketingId(Integer marketingId) {
        this.marketingId = marketingId;
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
        if (!(object instanceof MarketingTempCustomer)) {
            return false;
        }
        MarketingTempCustomer other = (MarketingTempCustomer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.maxelyz.core.model.entity.MarketingTempCustomer[ id=" + id + " ]";
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

    public String getFaxExt() {
        return faxExt;
    }

    public void setFaxExt(String faxExt) {
        this.faxExt = faxExt;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
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

    public String getCurrentCountryName() {
        return currentCountryName;
    }

    public void setCurrentCountryName(String currentCountryName) {
        this.currentCountryName = currentCountryName;
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

    public String getHomeCountryName() {
        return homeCountryName;
    }

    public void setHomeCountryName(String homeCountryName) {
        this.homeCountryName = homeCountryName;
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

    public String getBillingCountryName() {
        return billingCountryName;
    }

    public void setBillingCountryName(String billingCountryName) {
        this.billingCountryName = billingCountryName;
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

    public String getShippingCountryName() {
        return shippingCountryName;
    }

    public void setShippingCountryName(String shippingCountryName) {
        this.shippingCountryName = shippingCountryName;
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

    public String getListValue001() {
        return listValue001;
    }

    public void setListValue001(String listValue001) {
        this.listValue001 = listValue001;
    }

    public String getListValue002() {
        return listValue002;
    }

    public void setListValue002(String listValue002) {
        this.listValue002 = listValue002;
    }

    public String getListValue003() {
        return listValue003;
    }

    public void setListValue003(String listValue003) {
        this.listValue003 = listValue003;
    }

    public String getListValue004() {
        return listValue004;
    }

    public void setListValue004(String listValue004) {
        this.listValue004 = listValue004;
    }

    public String getListValue005() {
        return listValue005;
    }

    public void setListValue005(String listValue005) {
        this.listValue005 = listValue005;
    }

    public String getListValue006() {
        return listValue006;
    }

    public void setListValue006(String listValue006) {
        this.listValue006 = listValue006;
    }

    public String getListValue007() {
        return listValue007;
    }

    public void setListValue007(String listValue007) {
        this.listValue007 = listValue007;
    }

    public String getListValue008() {
        return listValue008;
    }

    public void setListValue008(String listValue008) {
        this.listValue008 = listValue008;
    }

    public String getListValue009() {
        return listValue009;
    }

    public void setListValue009(String listValue009) {
        this.listValue009 = listValue009;
    }

    public String getListValue010() {
        return listValue010;
    }

    public void setListValue010(String listValue010) {
        this.listValue010 = listValue010;
    }

    public String getListValue011() {
        return listValue011;
    }

    public void setListValue011(String listValue011) {
        this.listValue011 = listValue011;
    }

    public String getListValue012() {
        return listValue012;
    }

    public void setListValue012(String listValue012) {
        this.listValue012 = listValue012;
    }

    public String getListValue013() {
        return listValue013;
    }

    public void setListValue013(String listValue013) {
        this.listValue013 = listValue013;
    }

    public String getListValue014() {
        return listValue014;
    }

    public void setListValue014(String listValue014) {
        this.listValue014 = listValue014;
    }

    public String getListValue015() {
        return listValue015;
    }

    public void setListValue015(String listValue015) {
        this.listValue015 = listValue015;
    }

    public String getListValue016() {
        return listValue016;
    }

    public void setListValue016(String listValue016) {
        this.listValue016 = listValue016;
    }

    public String getListValue017() {
        return listValue017;
    }

    public void setListValue017(String listValue017) {
        this.listValue017 = listValue017;
    }

    public String getListValue018() {
        return listValue018;
    }

    public void setListValue018(String listValue018) {
        this.listValue018 = listValue018;
    }

    public String getListValue019() {
        return listValue019;
    }

    public void setListValue019(String listValue019) {
        this.listValue019 = listValue019;
    }

    public String getListValue020() {
        return listValue020;
    }

    public void setListValue020(String listValue020) {
        this.listValue020 = listValue020;
    }

    public String getListValue021() {
        return listValue021;
    }

    public void setListValue021(String listValue021) {
        this.listValue021 = listValue021;
    }

    public String getListValue022() {
        return listValue022;
    }

    public void setListValue022(String listValue022) {
        this.listValue022 = listValue022;
    }

    public String getListValue023() {
        return listValue023;
    }

    public void setListValue023(String listValue023) {
        this.listValue023 = listValue023;
    }

    public String getListValue024() {
        return listValue024;
    }

    public void setListValue024(String listValue024) {
        this.listValue024 = listValue024;
    }

    public String getListValue025() {
        return listValue025;
    }

    public void setListValue025(String listValue025) {
        this.listValue025 = listValue025;
    }

    public String getListValue026() {
        return listValue026;
    }

    public void setListValue026(String listValue026) {
        this.listValue026 = listValue026;
    }

    public String getListValue027() {
        return listValue027;
    }

    public void setListValue027(String listValue027) {
        this.listValue027 = listValue027;
    }

    public String getListValue028() {
        return listValue028;
    }

    public void setListValue028(String listValue028) {
        this.listValue028 = listValue028;
    }

    public String getListValue029() {
        return listValue029;
    }

    public void setListValue029(String listValue029) {
        this.listValue029 = listValue029;
    }

    public String getListValue030() {
        return listValue030;
    }

    public void setListValue030(String listValue030) {
        this.listValue030 = listValue030;
    }

    public String getListValue031() {
        return listValue031;
    }

    public void setListValue031(String listValue031) {
        this.listValue031 = listValue031;
    }

    public String getListValue032() {
        return listValue032;
    }

    public void setListValue032(String listValue032) {
        this.listValue032 = listValue032;
    }

    public String getListValue033() {
        return listValue033;
    }

    public void setListValue033(String listValue033) {
        this.listValue033 = listValue033;
    }

    public String getListValue034() {
        return listValue034;
    }

    public void setListValue034(String listValue034) {
        this.listValue034 = listValue034;
    }

    public String getListValue035() {
        return listValue035;
    }

    public void setListValue035(String listValue035) {
        this.listValue035 = listValue035;
    }

    public String getListValue036() {
        return listValue036;
    }

    public void setListValue036(String listValue036) {
        this.listValue036 = listValue036;
    }

    public String getListValue037() {
        return listValue037;
    }

    public void setListValue037(String listValue037) {
        this.listValue037 = listValue037;
    }

    public String getListValue038() {
        return listValue038;
    }

    public void setListValue038(String listValue038) {
        this.listValue038 = listValue038;
    }

    public String getListValue039() {
        return listValue039;
    }

    public void setListValue039(String listValue039) {
        this.listValue039 = listValue039;
    }

    public String getListValue040() {
        return listValue040;
    }

    public void setListValue040(String listValue040) {
        this.listValue040 = listValue040;
    }

    public String getListValue041() {
        return listValue041;
    }

    public void setListValue041(String listValue041) {
        this.listValue041 = listValue041;
    }

    public String getListValue042() {
        return listValue042;
    }

    public void setListValue042(String listValue042) {
        this.listValue042 = listValue042;
    }

    public String getListValue043() {
        return listValue043;
    }

    public void setListValue043(String listValue043) {
        this.listValue043 = listValue043;
    }

    public String getListValue044() {
        return listValue044;
    }

    public void setListValue044(String listValue044) {
        this.listValue044 = listValue044;
    }

    public String getListValue045() {
        return listValue045;
    }

    public void setListValue045(String listValue045) {
        this.listValue045 = listValue045;
    }

    public String getListValue046() {
        return listValue046;
    }

    public void setListValue046(String listValue046) {
        this.listValue046 = listValue046;
    }

    public String getListValue047() {
        return listValue047;
    }

    public void setListValue047(String listValue047) {
        this.listValue047 = listValue047;
    }

    public String getListValue048() {
        return listValue048;
    }

    public void setListValue048(String listValue048) {
        this.listValue048 = listValue048;
    }

    public String getListValue049() {
        return listValue049;
    }

    public void setListValue049(String listValue049) {
        this.listValue049 = listValue049;
    }

    public String getListValue050() {
        return listValue050;
    }

    public void setListValue050(String listValue050) {
        this.listValue050 = listValue050;
    }

    public String getListValue051() {
        return listValue051;
    }

    public void setListValue051(String listValue051) {
        this.listValue051 = listValue051;
    }

    public String getListValue052() {
        return listValue052;
    }

    public void setListValue052(String listValue052) {
        this.listValue052 = listValue052;
    }

    public String getListValue053() {
        return listValue053;
    }

    public void setListValue053(String listValue053) {
        this.listValue053 = listValue053;
    }

    public String getListValue054() {
        return listValue054;
    }

    public void setListValue054(String listValue054) {
        this.listValue054 = listValue054;
    }

    public String getListValue055() {
        return listValue055;
    }

    public void setListValue055(String listValue055) {
        this.listValue055 = listValue055;
    }

    public String getListValue056() {
        return listValue056;
    }

    public void setListValue056(String listValue056) {
        this.listValue056 = listValue056;
    }

    public String getListValue057() {
        return listValue057;
    }

    public void setListValue057(String listValue057) {
        this.listValue057 = listValue057;
    }

    public String getListValue058() {
        return listValue058;
    }

    public void setListValue058(String listValue058) {
        this.listValue058 = listValue058;
    }

    public String getListValue059() {
        return listValue059;
    }

    public void setListValue059(String listValue059) {
        this.listValue059 = listValue059;
    }

    public String getListValue060() {
        return listValue060;
    }

    public void setListValue060(String listValue060) {
        this.listValue060 = listValue060;
    }

    public String getListValue061() {
        return listValue061;
    }

    public void setListValue061(String listValue061) {
        this.listValue061 = listValue061;
    }

    public String getListValue062() {
        return listValue062;
    }

    public void setListValue062(String listValue062) {
        this.listValue062 = listValue062;
    }

    public String getListValue063() {
        return listValue063;
    }

    public void setListValue063(String listValue063) {
        this.listValue063 = listValue063;
    }

    public String getListValue064() {
        return listValue064;
    }

    public void setListValue064(String listValue064) {
        this.listValue064 = listValue064;
    }

    public String getListValue065() {
        return listValue065;
    }

    public void setListValue065(String listValue065) {
        this.listValue065 = listValue065;
    }

    public String getListValue066() {
        return listValue066;
    }

    public void setListValue066(String listValue066) {
        this.listValue066 = listValue066;
    }

    public String getListValue067() {
        return listValue067;
    }

    public void setListValue067(String listValue067) {
        this.listValue067 = listValue067;
    }

    public String getListValue068() {
        return listValue068;
    }

    public void setListValue068(String listValue068) {
        this.listValue068 = listValue068;
    }

    public String getListValue069() {
        return listValue069;
    }

    public void setListValue069(String listValue069) {
        this.listValue069 = listValue069;
    }

    public String getListValue070() {
        return listValue070;
    }

    public void setListValue070(String listValue070) {
        this.listValue070 = listValue070;
    }

    public String getListValue071() {
        return listValue071;
    }

    public void setListValue071(String listValue071) {
        this.listValue071 = listValue071;
    }

    public String getListValue072() {
        return listValue072;
    }

    public void setListValue072(String listValue072) {
        this.listValue072 = listValue072;
    }

    public String getListValue073() {
        return listValue073;
    }

    public void setListValue073(String listValue073) {
        this.listValue073 = listValue073;
    }

    public String getListValue074() {
        return listValue074;
    }

    public void setListValue074(String listValue074) {
        this.listValue074 = listValue074;
    }

    public String getListValue075() {
        return listValue075;
    }

    public void setListValue075(String listValue075) {
        this.listValue075 = listValue075;
    }

    public String getListValue076() {
        return listValue076;
    }

    public void setListValue076(String listValue076) {
        this.listValue076 = listValue076;
    }

    public String getListValue077() {
        return listValue077;
    }

    public void setListValue077(String listValue077) {
        this.listValue077 = listValue077;
    }

    public String getListValue078() {
        return listValue078;
    }

    public void setListValue078(String listValue078) {
        this.listValue078 = listValue078;
    }

    public String getListValue079() {
        return listValue079;
    }

    public void setListValue079(String listValue079) {
        this.listValue079 = listValue079;
    }

    public String getListValue080() {
        return listValue080;
    }

    public void setListValue080(String listValue080) {
        this.listValue080 = listValue080;
    }

    public String getListValue081() {
        return listValue081;
    }

    public void setListValue081(String listValue081) {
        this.listValue081 = listValue081;
    }

    public String getListValue082() {
        return listValue082;
    }

    public void setListValue082(String listValue082) {
        this.listValue082 = listValue082;
    }

    public String getListValue083() {
        return listValue083;
    }

    public void setListValue083(String listValue083) {
        this.listValue083 = listValue083;
    }

    public String getListValue084() {
        return listValue084;
    }

    public void setListValue084(String listValue084) {
        this.listValue084 = listValue084;
    }

    public String getListValue085() {
        return listValue085;
    }

    public void setListValue085(String listValue085) {
        this.listValue085 = listValue085;
    }

    public String getListValue086() {
        return listValue086;
    }

    public void setListValue086(String listValue086) {
        this.listValue086 = listValue086;
    }

    public String getListValue087() {
        return listValue087;
    }

    public void setListValue087(String listValue087) {
        this.listValue087 = listValue087;
    }

    public String getListValue088() {
        return listValue088;
    }

    public void setListValue088(String listValue088) {
        this.listValue088 = listValue088;
    }

    public String getListValue089() {
        return listValue089;
    }

    public void setListValue089(String listValue089) {
        this.listValue089 = listValue089;
    }

    public String getListValue090() {
        return listValue090;
    }

    public void setListValue090(String listValue090) {
        this.listValue090 = listValue090;
    }

    public String getListValue091() {
        return listValue091;
    }

    public void setListValue091(String listValue091) {
        this.listValue091 = listValue091;
    }

    public String getListValue092() {
        return listValue092;
    }

    public void setListValue092(String listValue092) {
        this.listValue092 = listValue092;
    }

    public String getListValue093() {
        return listValue093;
    }

    public void setListValue093(String listValue093) {
        this.listValue093 = listValue093;
    }

    public String getListValue094() {
        return listValue094;
    }

    public void setListValue094(String listValue094) {
        this.listValue094 = listValue094;
    }

    public String getListValue095() {
        return listValue095;
    }

    public void setListValue095(String listValue095) {
        this.listValue095 = listValue095;
    }

    public String getListValue096() {
        return listValue096;
    }

    public void setListValue096(String listValue096) {
        this.listValue096 = listValue096;
    }

    public String getListValue097() {
        return listValue097;
    }

    public void setListValue097(String listValue097) {
        this.listValue097 = listValue097;
    }

    public String getListValue098() {
        return listValue098;
    }

    public void setListValue098(String listValue098) {
        this.listValue098 = listValue098;
    }

    public String getListValue099() {
        return listValue099;
    }

    public void setListValue099(String listValue099) {
        this.listValue099 = listValue099;
    }

    public String getListValue100() {
        return listValue100;
    }

    public void setListValue100(String listValue100) {
        this.listValue100 = listValue100;
    }

    public Boolean getBadformat() {
        return badformat;
    }

    public void setBadformat(Boolean badformat) {
        this.badformat = badformat;
    }
    
    public Boolean getDnc() {
        return dnc;
    }

    public void setDnc(Boolean dnc) {
        this.dnc = dnc;
    }
    
    public Boolean getMib() {
        return mib;
    }

    public void setMib(Boolean mib) {
        this.mib = mib;
    }
}
