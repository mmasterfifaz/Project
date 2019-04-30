package com.maxelyz.core.model.value.front.customerHandling;

import com.maxelyz.core.model.dao.DistrictDAO;
import com.maxelyz.core.model.dao.SubdistrictDAO;
import com.maxelyz.core.model.dao.OccupationDAO;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.District;
import com.maxelyz.core.model.entity.Occupation;
import com.maxelyz.core.model.entity.Province;
import com.maxelyz.core.model.entity.PurchaseOrderRegister;
import com.maxelyz.core.model.entity.RegistrationField;
import com.maxelyz.core.model.entity.RegistrationForm;
import com.maxelyz.core.model.entity.Subdistrict;
import com.maxelyz.core.model.entity.TempPurchaseOrderRegister;
import com.maxelyz.utils.JSFUtil;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.event.ActionEvent;

public class RegistrationInfoValue<T> {

    public static int MAX_FLEX_FIELD = 50;
    public static String GROUPTITLE_LABEL = "Group Title";
    public static String GROUPTITLE_FIELD = "grouptitle";
    public static String INITIAL_LABEL = "Initial";
    public static String INITIAL_FIELD = "initial";
    public static String NAME_LABEL = "Name";
    public static String NAME_FIELD = "name";
    public static String SURNAME_LABEL = "Surname";
    public static String SURNAME_FIELD = "surname";
    public static String IDCARDTYPEID_LABEL = "ID Card Type";
    public static String IDCARDTYPEID_FIELD = "idcardtypeid";
    public static String IDNO_LABEL = "ID Card";
    public static String IDNO_FIELD = "idno";
    public static String IDCARD_ISSUE_LABEL = "ID Card Issue";
    public static String IDCARD_ISSUE_FIELD = "idcardissue";
    public static String IDCARD_EXPIRYDATE_LABEL = "ID Card Expiry Date";
    public static String IDCARD_EXPIRYDATE_FIELD = "idcardexpirydate";
    public static String DOB_LABEL = "Date of Birth";
    public static String DOB_FIELD = "dob";
    public static String GENDER_LABEL = "Gender";
    public static String GENDER_FIELD = "gender";
    public static String WEIGHT_LABEL = "Weight (kg.)";
    public static String WEIGHT_FIELD = "weight";
    public static String HEIGHT_LABEL = "Height (cm.)";
    public static String HEIGHT_FIELD = "height";
    public static String MARITALSTATUS_LABEL = "Marital Status";
    public static String MARITALSTATUS_FIELD = "maritalstatus";
    public static String BMI_LABEL = "BMI";
    public static String BMI_FIELD = "bmi";
    public static String CURRENTADDRESS_FIELD = "currentaddress";
    public static String CURRENTADDRESS_LABEL = "Current Address";
    public static String CURRENTADDRESS_FULLNAME_LABEL = "Current Full Name";
    public static String CURRENTADDRESS_FULLNAME_FIELD = "currentaddressfullname";
    public static String CURRENTADDRESS1_LABEL = "Current Address1";
    public static String CURRENTADDRESS1_FIELD = "currentaddress1";
    public static String CURRENTADDRESS2_LABEL = "Current Address2";
    public static String CURRENTADDRESS2_FIELD = "currentaddress2";
    public static String CURRENTADDRESS3_LABEL = "Current Address3";
    public static String CURRENTADDRESS3_FIELD = "currentaddress3";
    public static String CURRENTADDRESS4_LABEL = "Current Address4";
    public static String CURRENTADDRESS4_FIELD = "currentaddress4";
    public static String CURRENTADDRESS5_LABEL = "Current Address5";
    public static String CURRENTADDRESS5_FIELD = "currentaddress5";
    public static String CURRENTADDRESS6_LABEL = "Current Address6";
    public static String CURRENTADDRESS6_FIELD = "currentaddress6";
    public static String CURRENTADDRESS7_LABEL = "Current Address7";
    public static String CURRENTADDRESS7_FIELD = "currentaddress7";
    public static String CURRENTADDRESS8_LABEL = "Current Address8";
    public static String CURRENTADDRESS8_FIELD = "currentaddress8";
    public static String CURRENTADDRESS_PROVINCE_LABEL = "Current Province";
    public static String CURRENTADDRESS_PROVINCE_FIELD = "currentaddressprovince";
    public static String CURRENTADDRESS_POSTAL_LABEL = "Current Postal";
    public static String CURRENTADDRESS_POSTAL_FIELD = "currentaddresspostal";
    public static String CURRENTADDRESS_TELEPHONE1_LABEL = "Current Telephone1";
    public static String CURRENTADDRESS_TELEPHONE1_FIELD = "currentaddresstelephone1";
    public static String CURRENTADDRESS_TELEPHONE2_LABEL = "Current Telephone2";
    public static String CURRENTADDRESS_TELEPHONE2_FIELD = "currentaddresstelephone2";
  
    public static String HOMEADDRESS_FIELD = "homeaddress";
    public static String HOMEADDRESS_LABEL = "Home Address";
    public static String HOMEADDRESS_FULLNAME_LABEL = "Home Full Name";
    public static String HOMEADDRESS_FULLNAME_FIELD = "homeaddressfullname";
    public static String HOMEADDRESS1_LABEL = "Home Address1";
    public static String HOMEADDRESS1_FIELD = "homeaddress1";
    public static String HOMEADDRESS2_LABEL = "Home Address2";
    public static String HOMEADDRESS2_FIELD = "homeaddress2";
    public static String HOMEADDRESS3_LABEL = "Home Address3";
    public static String HOMEADDRESS3_FIELD = "homeaddress3";
    public static String HOMEADDRESS4_LABEL = "Home Address4";
    public static String HOMEADDRESS4_FIELD = "homeaddress4";
    public static String HOMEADDRESS5_LABEL = "Home Address5";
    public static String HOMEADDRESS5_FIELD = "homeaddress5";
    public static String HOMEADDRESS6_LABEL = "Home Address6";
    public static String HOMEADDRESS6_FIELD = "homeaddress6";
    public static String HOMEADDRESS7_LABEL = "Home Address7";
    public static String HOMEADDRESS7_FIELD = "homeaddress7";
    public static String HOMEADDRESS8_LABEL = "Home Address8";
    public static String HOMEADDRESS8_FIELD = "homeaddress8";
    public static String HOMEADDRESS_PROVINCE_LABEL = "Home Province";
    public static String HOMEADDRESS_PROVINCE_FIELD = "homeaddressprovince";
    public static String HOMEADDRESS_POSTAL_LABEL = "Home Postal";
    public static String HOMEADDRESS_POSTAL_FIELD = "homeaddresspostal";
    public static String HOMEADDRESS_TELEPHONE1_LABEL = "Home Telephone1";
    public static String HOMEADDRESS_TELEPHONE1_FIELD = "homeaddresstelephone1";
    public static String HOMEADDRESS_TELEPHONE2_LABEL = "Home Telephone2";
    public static String HOMEADDRESS_TELEPHONE2_FIELD = "homeaddresstelephone2";

    public static String BILLINGADDRESS_FIELD = "billingaddress";
    public static String BILLINGADDRESS_LABEL = "Billing Address";
    public static String BILLINGADDRESS_FULLNAME_LABEL = "Billing Full Name";
    public static String BILLINGADDRESS_FULLNAME_FIELD = "billingaddressfullname";
    public static String BILLINGADDRESS1_LABEL = "Billing Address1";
    public static String BILLINGADDRESS1_FIELD = "billingaddress1";
    public static String BILLINGADDRESS2_LABEL = "Billing Address2";
    public static String BILLINGADDRESS2_FIELD = "billingaddress2";
    public static String BILLINGADDRESS3_LABEL = "Billing Address3";
    public static String BILLINGADDRESS3_FIELD = "billingaddress3";
    public static String BILLINGADDRESS4_LABEL = "Billing Address4";
    public static String BILLINGADDRESS4_FIELD = "billingaddress4";
    public static String BILLINGADDRESS5_LABEL = "Billing Address5";
    public static String BILLINGADDRESS5_FIELD = "billingaddress5";
    public static String BILLINGADDRESS6_LABEL = "Billing Address6";
    public static String BILLINGADDRESS6_FIELD = "billingaddress6";
    public static String BILLINGADDRESS7_LABEL = "Billing Address7";
    public static String BILLINGADDRESS7_FIELD = "billingaddress7";
    public static String BILLINGADDRESS8_LABEL = "Billing Address8";
    public static String BILLINGADDRESS8_FIELD = "billingaddress8";
    public static String BILLINGADDRESS_PROVINCE_LABEL = "Billing Province";
    public static String BILLINGADDRESS_PROVINCE_FIELD = "billingaddressprovince";
    public static String BILLINGADDRESS_POSTAL_LABEL = "Billing Postal";
    public static String BILLINGADDRESS_POSTAL_FIELD = "billingaddresspostal";
    public static String BILLINGADDRESS_TELEPHONE1_LABEL = "Billing Telephone1";
    public static String BILLINGADDRESS_TELEPHONE1_FIELD = "billingaddresstelephone1";
    public static String BILLINGADDRESS_TELEPHONE2_LABEL = "Billing Telephone2";
    public static String BILLINGADDRESS_TELEPHONE2_FIELD = "billingaddresstelephone2";
    
    public static String SHIPPINGADDRESS_FIELD = "shippingaddress";
    public static String SHIPPINGADDRESS_LABEL = "Shipping Address";
    public static String SHIPPINGADDRESS_FULLNAME_LABEL = "Shipping Full Name";
    public static String SHIPPINGADDRESS_FULLNAME_FIELD = "shippingaddressfullname";
    public static String SHIPPINGADDRESS1_LABEL = "Shipping Address1";
    public static String SHIPPINGADDRESS1_FIELD = "shippingaddress1";
    public static String SHIPPINGADDRESS2_LABEL = "Shipping Address2";
    public static String SHIPPINGADDRESS2_FIELD = "shippingaddress2";
    public static String SHIPPINGADDRESS3_LABEL = "Shipping Address3";
    public static String SHIPPINGADDRESS3_FIELD = "shippingaddress3";
    public static String SHIPPINGADDRESS4_LABEL = "Shipping Address4";
    public static String SHIPPINGADDRESS4_FIELD = "shippingaddress4";
    public static String SHIPPINGADDRESS5_LABEL = "Shipping Address5";
    public static String SHIPPINGADDRESS5_FIELD = "shippingaddress5";
    public static String SHIPPINGADDRESS6_LABEL = "Shipping Address6";
    public static String SHIPPINGADDRESS6_FIELD = "shippingaddress6";
    public static String SHIPPINGADDRESS7_LABEL = "Shipping Address7";
    public static String SHIPPINGADDRESS7_FIELD = "shippingaddress7";
    public static String SHIPPINGADDRESS8_LABEL = "Shipping Address8";
    public static String SHIPPINGADDRESS8_FIELD = "shippingaddress8";
    public static String SHIPPINGADDRESS_PROVINCE_LABEL = "Shipping Province";
    public static String SHIPPINGADDRESS_PROVINCE_FIELD = "shippingaddressprovince";
    public static String SHIPPINGADDRESS_POSTAL_LABEL = "Shipping Postal";
    public static String SHIPPINGADDRESS_POSTAL_FIELD = "shippingaddresspostal";
    public static String SHIPPINGADDRESS_TELEPHONE1_LABEL = "Shipping Telephone1";
    public static String SHIPPINGADDRESS_TELEPHONE1_FIELD = "shippingaddresstelephone1";
    public static String SHIPPINGADDRESS_TELEPHONE2_LABEL = "Shipping Telephone2";
    public static String SHIPPINGADDRESS_TELEPHONE2_FIELD = "shippingaddresstelephone2";
    
    public static String HOMEPHONE_LABEL = "Home Phone";
    public static String HOMEPHONE_FIELD = "homephone";
    public static String OFFICEPHONE_LABEL = "Office Phone";
    public static String OFFICEPHONE_FIELD = "officephone";
    public static String MOBILEPHONE_LABEL = "Mobile Phone";
    public static String MOBILEPHONE_FIELD = "mobilephone";
    public static String EMAIL_LABEL = "Email";
    public static String EMAIL_FIELD = "email";
    public static String NATIONALITY_LABEL = "Nationality";
    public static String NATIONALITY_FIELD = "nationality";
    public static String RACE_LABEL = "Race";
    public static String RACE_FIELD = "race";
    public static String RELIGION_LABEL = "Religion";
    public static String RELIGION_FIELD = "religion";
    public static String OCCUPATION_LABEL = "Occupation";
    public static String OCCUPATION_FIELD = "occupationlist";
    public static String OCCUPATIONTEXT_LABEL = "Occupation";
    public static String OCCUPATIONTEXT_FIELD = "occupation";
    public static String JOB_LABEL = "Job Description";
    public static String JOB_FIELD = "jobdescription";
    public static String JOBPOSITION_LABEL = "Position";
    public static String JOBPOSITION_FIELD = "position";
    public static String BUSINESSTYPE_LABEL = "Business Type";
    public static String BUSINESSTYPE_FIELD = "businesstype";
    public static String INCOME_LABEL = "Income";
    public static String INCOME_FIELD = "income";
    public static String[] FLEX_LABEL = getInititialFlexLabel();
    public static String[] FLEX_FIELD = getInititialFlexField();
    private List<Province> provinceList = new ArrayList<Province>();
    private List currentDistrictList;
    private List homeDistrictList;
    private List billingDistrictList;
    private List shippingDistrictList;
    private List occupationList;
    private List currentSubDistrictList;
    private List homeSubDistrictList;
    private List billingSubDistrictList;
    private List shippingSubDistrictList;
    private List<SelectItem> initialList;
    private List<SelectItem> idCardIssueList;
    private List<SelectItem> nationalityList;
    private List<SelectItem> raceList;
    private List<SelectItem> religionList;
    
    private List currentPostalList;
    private List homePostalList;
    private List billingPostalList;
    private List shippingPostalList;
    
    private Integer currentProvinceId;
    private Integer homeProvinceId;
    private Integer billingProvinceId;
    private Integer shippingProvinceId;
    private Integer currentDistrictId;
    private Integer homeDistrictId;
    private Integer billingDistrictId;
    private Integer shippingDistrictId;
    private Integer currentSubDistrictId;
    private Integer homeSubDistrictId;
    private Integer billingSubDistrictId;
    private Integer shippingSubDistrictId;
    private Integer occupationId;
    private Integer occupationCategoryId;

    private District currentDistrict;
    private District homeDistrict;
    private District billingDistrict;
    private District shippingDistrict;
    private Subdistrict currentSubDistrict;
    private Subdistrict homeSubDistrict;
    private Subdistrict billingSubDistrict;
    private Subdistrict shippingSubDistrict;
    private Occupation occ;
    
    private String currentPostal;
    private String homePostal;
    private String billingPostal;
    private String shippingPostal;

    private List<FlexFieldInfoValue> flexFields = new ArrayList<FlexFieldInfoValue>();
    private int age = 0;
    private int ageCompleteYear = 0;  // จำนวนปีของอายุ                  ldk 20/08/2015
    private int ageDayAfterCompleteYear = 0; // จำนวนวันของอายุที่เกินปี            ldk 20/08/2015
    private Double bmi;
    private RegistrationForm registrationForm;
    private Map<String, RegistrationField> regisMap = new ConcurrentHashMap<String, RegistrationField>();
    private T po; //PurchaseOrderRegister
    private DistrictDAO districtDAO;
    private SubdistrictDAO subdistrictDAO;
    private OccupationDAO occupationDAO;
    
    private String ageCalMethod;
    private Integer ageCalMonth;
    private Integer ageCalDay;

    private String id1;
    private String id2;
    private String id3;
    private String id4;
    private String id5;
    private String citizenId;
    private String msgIdno;

    private Map<String, String> addrCopyForCurrent;
    private Map<String, String> addrCopyForHome;
    private Map<String, String> addrCopyForShipping;
    private Map<String, String> addrCopyForBilling;
    
    private String previewHomeAdd1;
    private String previewHomeAdd2;
    private String previewHomeAdd3;
    private String previewCurrentAdd1;
    private String previewCurrentAdd2;
    private String previewCurrentAdd3;
    private String previewShippingAdd1;
    private String previewShippingAdd2;
    private String previewShippingAdd3;
    private String previewBillingAdd1;
    private String previewBillingAdd2;
    private String previewBillingAdd3;
                
    private String msgPostalCode;
    
    public RegistrationInfoValue() {
    }

    public RegistrationInfoValue(RegistrationForm registrationForm, List<RegistrationField> registrationFields, int groupNo, DistrictDAO districtDAO, SubdistrictDAO subdistrictDAO, OccupationDAO occupationDAO, T tpor, String pageForm) throws NonexistentEntityException {
        boolean found = false;
        this.registrationForm = registrationForm;
        this.districtDAO = districtDAO;
        this.subdistrictDAO = subdistrictDAO;
        this.occupationDAO = occupationDAO;
        for (RegistrationField f : registrationFields) {
            if (groupNo == f.getGroupNo()) {
                regisMap.put(f.getName(), f);
                found = true;
            }
        }
        if (!found) {
            throw new NonexistentEntityException("No Data Found");
        }

        //init occulationlist
        this.setOccupationList(1); 
        
        //init po

        if (tpor != null) {
            po = tpor;
            try {
                if(po instanceof TempPurchaseOrderRegister){
                    TempPurchaseOrderRegister por1 = (TempPurchaseOrderRegister) po;

                    if(por1.getCurrentDistrict() != null && por1.getCurrentDistrict().getId() != null){
                        currentDistrictId = por1.getCurrentDistrict().getId();
                        if(por1.getCurrentDistrict().getProvinceId() == null){
                            currentDistrict = districtDAO.findDistrict(currentDistrictId);
                        }else{
                            currentDistrict = por1.getCurrentDistrict();
                        }
                        currentProvinceId = currentDistrict.getProvinceId().getId();
                        currentDistrictList = getDistrictList1(currentProvinceId, currentPostal);
                    }
                    if(por1.getHomeDistrict() != null && por1.getHomeDistrict().getId() != null){
                        homeDistrictId = por1.getHomeDistrict().getId();
                        if(por1.getHomeDistrict().getProvinceId() == null){
                            homeDistrict = districtDAO.findDistrict(homeDistrictId);
                        }else{
                            homeDistrict = por1.getHomeDistrict();
                        }
                        homeProvinceId = homeDistrict.getProvinceId().getId();
                        homeDistrictList = getDistrictList1(homeProvinceId, homePostal);
                    }
                    if(por1.getBillingDistrict() != null && por1.getBillingDistrict().getId() != null){
                        billingDistrictId = por1.getBillingDistrict().getId();
                        if(por1.getBillingDistrict().getProvinceId() == null){
                            billingDistrict = districtDAO.findDistrict(billingDistrictId);
                        }else{
                            billingDistrict = por1.getBillingDistrict();
                        }
                        billingProvinceId = billingDistrict.getProvinceId().getId();
                        billingDistrictList = getDistrictList1(billingProvinceId, billingPostal);
                    }
                    if(por1.getShippingDistrict() != null && por1.getShippingDistrict().getId() != null){
                        shippingDistrictId = por1.getShippingDistrict().getId();
                        if(por1.getShippingDistrict().getProvinceId() == null){
                            shippingDistrict = districtDAO.findDistrict(shippingDistrictId);
                        }else{
                            shippingDistrict = por1.getShippingDistrict();
                        }
                        shippingProvinceId = shippingDistrict.getProvinceId().getId();
                        shippingDistrictList = getDistrictList1(shippingProvinceId, shippingPostal);
                    }

                    //Occupation
                    if(por1.getOccupation() != null && por1.getOccupation().getId() != null){
                        occupationId = por1.getOccupation().getId();
                        if(por1.getOccupation().getOccupationCategory() == null){
                            occ = occupationDAO.findOccupation(occupationId);
                        }else{
                            occ = por1.getOccupation();
                        }
                        occupationCategoryId = occ.getOccupationCategory().getId();
                        this.setOccupationList(occupationCategoryId);
                    }

                } else if(po instanceof PurchaseOrderRegister){
                    PurchaseOrderRegister por2 = (PurchaseOrderRegister) po;

                    //initial
                    if(por2.getInitial() != null && !por2.getInitial().equals("")) {
                        por2.setInitialLabel(changeCodeToText(por2.getInitial(),"initial", registrationFields));
                    }   
                    //nationality
                    if(por2.getNationality() != null && !por2.getNationality().equals("")) {
                        por2.setNationalityLabel(changeCodeToText(por2.getNationality(),"nationality", registrationFields));
                    }  
                    //race
                    if(por2.getRace() != null && !por2.getRace().equals("")) {
                        por2.setRaceLabel(changeCodeToText(por2.getRace(),"race", registrationFields));
                    }  
                    //religion
                    if(por2.getReligion() != null && !por2.getReligion().equals("")) {
                        por2.setReligionLabel(changeCodeToText(por2.getReligion(),"religion", registrationFields));
                    } 
                     //Occupation 
                    if(por2.getOccupationText() != null && !por2.getOccupationText().equals("")) {
                        por2.setOccupationLabel(changeCodeToText(por2.getOccupationText(),"occupation", registrationFields));
                    }
                    if(por2.getOccupation() != null && por2.getOccupation().getId() != null){
                        this.setOccupationId(por2.getOccupation().getId());
                        if(por2.getOccupation().getOccupationCategory() == null){
                            occ = occupationDAO.findOccupation(occupationId);
                        }else{
                            occ = por2.getOccupation();
                        }
                        occupationCategoryId = occ.getOccupationCategory().getId();
                        this.setOccupationList(occupationCategoryId);
                    }
                    
                    if(por2.getCurrentDistrict() != null && por2.getCurrentDistrict().getId() != null){
                        currentDistrictId = por2.getCurrentDistrict().getId();
                        if(por2.getCurrentDistrict().getProvinceId() == null){
                            currentDistrict = districtDAO.findDistrict(currentDistrictId);
                        }else{
                            currentDistrict = por2.getCurrentDistrict();
                        }
                        currentProvinceId = currentDistrict.getProvinceId().getId();
                        currentDistrictList = getDistrictList(currentProvinceId);
                    }
                    if(por2.getHomeDistrict() != null && por2.getHomeDistrict().getId() != null){
                        homeDistrictId = por2.getHomeDistrict().getId();
                        if(por2.getHomeDistrict().getProvinceId() == null){
                            homeDistrict = districtDAO.findDistrict(homeDistrictId);
                        }else{
                            homeDistrict = por2.getHomeDistrict();
                        }
                        homeProvinceId = homeDistrict.getProvinceId().getId();
                        homeDistrictList = getDistrictList1(homeProvinceId, por2.getHomePostalCode());
                        homePostal = por2.getHomePostalCode();
                    }
                    if(por2.getBillingDistrict() != null && por2.getBillingDistrict().getId() != null){
                        billingDistrictId = por2.getBillingDistrict().getId();
                        if(por2.getBillingDistrict().getProvinceId() == null){
                            billingDistrict = districtDAO.findDistrict(billingDistrictId);
                        }else{
                            billingDistrict = por2.getBillingDistrict();
                        }
                        billingProvinceId = billingDistrict.getProvinceId().getId();
                        billingDistrictList = getDistrictList1(billingProvinceId, por2.getBillingPostalCode());
                        billingPostal = por2.getBillingPostalCode();
                    }
                    if(por2.getShippingDistrict() != null && por2.getShippingDistrict().getId() != null){
                        shippingDistrictId = por2.getShippingDistrict().getId();
                        if(por2.getShippingDistrict().getProvinceId() == null){
                            shippingDistrict = districtDAO.findDistrict(shippingDistrictId);
                        }else{
                            shippingDistrict = por2.getShippingDistrict();
                        }
                        shippingProvinceId = shippingDistrict.getProvinceId().getId();
                        shippingDistrictList = getDistrictList1(shippingProvinceId,  por2.getShippingPostalCode());
                        shippingPostal = por2.getShippingPostalCode();
                    }
                    
                    //SubDistrict
                    if(por2.getCurrentSubDistrict() != null && por2.getCurrentSubDistrict().getId() != null){
                        currentSubDistrictId = por2.getCurrentSubDistrict().getId();
                        if(por2.getCurrentSubDistrict().getDistrict() == null){
                            currentSubDistrict = subdistrictDAO.findSubDistrict(currentSubDistrictId);
                        }else{
                            currentSubDistrict = por2.getCurrentSubDistrict();
                        }
                        currentDistrictId = currentSubDistrict.getDistrict().getId();
                        currentSubDistrictList = getSubDistrictList1(currentDistrictId, por2.getCurrentPostalCode());
                        currentPostalList = getPostalList1(currentSubDistrictId);
                    }
                    if(por2.getHomeSubDistrict() != null && por2.getHomeSubDistrict().getId() != null){
                        homeSubDistrictId = por2.getHomeSubDistrict().getId();
                        if(por2.getHomeSubDistrict().getDistrict() == null){
                            homeSubDistrict = subdistrictDAO.findSubDistrict(homeSubDistrictId);
                        }else{
                            homeSubDistrict = por2.getHomeSubDistrict();
                        }
                        homeDistrictId = homeSubDistrict.getDistrict().getId();
                        homeSubDistrictList = getSubDistrictList1(homeDistrictId, por2.getHomePostalCode());
                        homePostalList = getPostalList1(homeSubDistrictId);
                    }
                    if(por2.getBillingSubDistrict() != null && por2.getBillingSubDistrict().getId() != null){
                        billingSubDistrictId = por2.getBillingSubDistrict().getId();
                        if(por2.getBillingSubDistrict().getDistrict() == null){
                            billingSubDistrict = subdistrictDAO.findSubDistrict(billingSubDistrictId);
                        }else{
                            billingSubDistrict = por2.getBillingSubDistrict();
                        }
                        billingDistrictId = billingSubDistrict.getDistrict().getId();
                        billingSubDistrictList = getSubDistrictList1(billingDistrictId, por2.getBillingPostalCode());
                        billingPostalList = getPostalList1(billingSubDistrictId);
                    }
                    if(por2.getShippingSubDistrict() != null && por2.getShippingSubDistrict().getId() != null){
                        shippingSubDistrictId = por2.getShippingSubDistrict().getId();
                        if(por2.getShippingSubDistrict().getDistrict() == null){
                            shippingSubDistrict = subdistrictDAO.findSubDistrict(shippingSubDistrictId);
                        }else{
                            shippingSubDistrict = por2.getShippingSubDistrict();
                        }
                        shippingDistrictId = shippingSubDistrict.getDistrict().getId();
                        shippingSubDistrictList = getSubDistrictList1(shippingDistrictId, por2.getShippingPostalCode());
                        shippingPostalList = getPostalList1(shippingSubDistrictId);
                    }
                    
                    //postal code
                    if(por2.getCurrentPostalCode() != null && !por2.getCurrentPostalCode().equals("")){
                        currentPostal = por2.getCurrentPostalCode();
                    }
                    if(por2.getHomePostalCode() != null && !por2.getHomePostalCode().equals("")){
                        homePostal = por2.getHomePostalCode();
                    }
                    if(por2.getBillingPostalCode() != null && !por2.getBillingPostalCode().equals("")){
                        billingPostal = por2.getBillingPostalCode();
                    }
                    if(por2.getShippingPostalCode() != null && !por2.getShippingPostalCode().equals("")){
                        shippingPostal = por2.getShippingPostalCode();
                    }                  
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        //set flexfield
        this.setFlexFields(pageForm);
    }
    
    private String changeCodeToText(String poFlexField, String field, List<RegistrationField> registrationFields) {
        String value = "";
        for(RegistrationField t: registrationFields){
            if(t.getName().equals(field) && t.getControlType().equals("combobox")) {
                String[] tmp = t.getItems().split(",");
                for(String item : tmp) {
                    String[] text = item.split(":");
                    if(text[0].equals(poFlexField)) {
                        value = text[1];
                        break;
                    }
                }
            }
            if(!value.equals("")) {
                break;
            }
        }
        return value;
    }
    
    public void flexFieldDateListener(ActionEvent e) {
        int fieldIndex = Integer.parseInt(JSFUtil.getRequestParameterMap("flexFieldNo").toString()) - 1;
        String strDate = JSFUtil.getRequestParameterMap("flexFieldDate");
        //System.out.println(fieldIndex);
        //System.out.println(strDate);
        
        Date fieldDate;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("th", "th"));
            fieldDate = sdf.parse(strDate);         
            System.out.println(sdf.format(fieldDate));
            this.getFlexFields().get(fieldIndex).setPoFlexField(sdf.format(fieldDate));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public Integer findProvinceId(Integer id){
        District district = districtDAO.findDistrict(id);
        return district.getProvinceId().getId();
    }

    public T getPo() {
        return po;
    }

    public void setPoReg(T po) {
        this.po = po;
    }

    private String getField(String fieldName, String defaultLabel) {
        String result = null;
        RegistrationField f = regisMap.get(fieldName);
        if (f != null) {
            if (!"".equals(f.getCustomName())) {
                result = f.getCustomName();
            } else {
                result = defaultLabel;
            }
        }
        return result;
    }

    
    private boolean getRequireField(String fieldName) {
        boolean result = false;
        RegistrationField f = regisMap.get(fieldName);
        if (f != null) {
            result = f.getRequired();
        }
        return result;
    }
    
    private String getControlType(String fieldName) {
        String result = null;
        RegistrationField f = regisMap.get(fieldName);
        if (f != null) {
            result = f.getControlType();
        }
        return result;
    } 
    
    private Integer getSeqNo(String fieldName) {
        Integer result = null;
        RegistrationField f = regisMap.get(fieldName);
        if (f != null) {
            result = f.getSeqNo();
        }
        return result;
    }

    private List<SelectItem> getItemList(String fieldName) {
        List<SelectItem> result = null;
        RegistrationField f = regisMap.get(fieldName);
        if (f != null) {
            result = new ArrayList<SelectItem>();
            String items = f.getItems();
            try {
                StringTokenizer st1 = new StringTokenizer(items,",");      
                while (st1.hasMoreTokens()) {
                    String token = st1.nextToken();
                    StringTokenizer st2 = new StringTokenizer(token,":");
                    String key = st2.nextToken();
                    String value = st2.nextToken();
                    if (this.getControlType(fieldName).equals("combobox")) {
                        result.add(new SelectItem(key, value));     
                    } else {
                        result.add(new SelectItem(value, key));
                    }
                }
            } catch(Exception e) {
               result.clear(); 
            }
            
        }
        return result;
    }

    private List<SelectItem> getDistrictList(Integer provinceId) {
        List<SelectItem> values = new ArrayList<SelectItem>();
        if(provinceId != 0 && provinceId != null){
            List<District> districtList = districtDAO.findDistrictByProvinceId(provinceId);
            for (District obj : districtList) {
                values.add(new SelectItem(obj.getId(), obj.getName()));
            }
        }
        return values;
    }

    private List getDistrictList1(Integer provinceId, String postalCode) {
        List districtList = null;
        if(provinceId != null && provinceId != 0){
            districtList = districtDAO.findDistrictByProvinceId1(provinceId, postalCode);
        }
        return districtList;
    }
    
    private List<SelectItem> getSubDistrictList(Integer districtId) {
        List<SelectItem> values = new ArrayList<SelectItem>();
        if(districtId != 0 && districtId != null) {
            List<Subdistrict> subDistrictList = subdistrictDAO.findSubDistrictByDistrictId(districtId); 
            for (Subdistrict obj : subDistrictList) {
                values.add(new SelectItem(obj.getId(), obj.getName()));
            }
        }
        return values;
    }
    
    private List getSubDistrictList1(Integer districtId, String postalCode) {
        List subDistrictList = null;
        if(districtId != 0 && districtId != null) {
            subDistrictList = subdistrictDAO.findSubDistrictByDistrictId1(districtId, postalCode); 
        }
        return subDistrictList;
    }
        
    private Map<String, String> getPostalList(Integer subdistrictId) {       
        Map<String, String> values = new LinkedHashMap<String, String>(); 
        if(subdistrictId != 0 && subdistrictId != null) {
            Subdistrict subdistrict = subdistrictDAO.findSubDistrict(subdistrictId);
            String[] postal = subdistrict.getPostalCode().split(",");
            for(String obj : postal) {
                values.put(obj.toString() , obj.toString());
            }
        }
        return values;
    }
        
    private List getPostalList1(Integer subdistrictId) {       
        List list = new ArrayList();
        if(subdistrictId != 0 && subdistrictId != null) {
            Subdistrict subdistrict = subdistrictDAO.findSubDistrict(subdistrictId);
            String[] postal = subdistrict.getPostalCode().split(",");
            for(String obj : postal) {
                list.add(obj.toString());
            }
        }
        return list;
    }
    
    private static String[] getInititialFlexField() {
        String[] obj = new String[MAX_FLEX_FIELD];
        for (int i = 0; i < MAX_FLEX_FIELD; i++) {
            obj[i] = "fx" + (i + 1);
        }
        return obj;
    }

    private static String[] getInititialFlexLabel() {
        String[] obj = new String[MAX_FLEX_FIELD];
        for (int i = 0; i < MAX_FLEX_FIELD; i++) {
            obj[i] = "Flex Field" + (i + 1);
        }
        return obj;
    }

    public void setRegIdcardExpiryDate(String strDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("th", "th"));

            if (po instanceof PurchaseOrderRegister) {
                ((PurchaseOrderRegister) po).setIdcardExpiryDate(sdf.parse(strDate));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public String getRegIdcardExpiryDate() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("th", "th"));
            if (po instanceof PurchaseOrderRegister) {
                return sdf.format(((PurchaseOrderRegister) po).getIdcardExpiryDate());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    //Listener
    public void idcardExpiryDateListener(ActionEvent e) {
        String strDate = JSFUtil.getRequestParameterMap("expirydate");
        Date expiryDate;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("th", "th"));
            expiryDate = sdf.parse(strDate);         
            if (po instanceof PurchaseOrderRegister) {
                ((PurchaseOrderRegister) po).setIdcardExpiryDate(expiryDate);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void regIdcardExpiryDateListener(ValueChangeEvent e) {
        String strDate = (String) e.getNewValue();
        Date expiryDate;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("th", "th"));
            expiryDate = sdf.parse(strDate);         
            if (po instanceof PurchaseOrderRegister) {
                ((PurchaseOrderRegister) po).setIdcardExpiryDate(expiryDate);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void dobListener(ValueChangeEvent e) {
        if(e.getNewValue() != null) {
            Date dobDate = (Date) e.getNewValue();
            this.setAge(dobDate);
        }
    }
    
    public void dobThaiListener(ActionEvent e) {
        String strDate = JSFUtil.getRequestParameterMap("param1");
        Date dobDate;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("th", "th"));
            dobDate = sdf.parse(strDate);
            this.setAge(dobDate);
        } catch (Exception ex) {
            ex.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void weightListener(ValueChangeEvent e) {
        if (po instanceof TempPurchaseOrderRegister) {
            TempPurchaseOrderRegister poReg = (TempPurchaseOrderRegister) po;
            poReg.setWeight((e.getNewValue() != null && !e.getNewValue().equals("")) ? new Double(e.getNewValue().toString()) : new Double(0));
            bmi = computeBmi(poReg.getWeight(), poReg.getHeight());
        } else if (po instanceof PurchaseOrderRegister) {
            PurchaseOrderRegister poReg = (PurchaseOrderRegister) po;
            poReg.setWeight((e.getNewValue() != null && !e.getNewValue().equals("")) ? new Double(e.getNewValue().toString()) : new Double(0));
            bmi = computeBmi(poReg.getWeight(), poReg.getHeight());
        }
    }

    public void heightListener(ValueChangeEvent e) {
        if (po instanceof TempPurchaseOrderRegister) {
            TempPurchaseOrderRegister poReg = (TempPurchaseOrderRegister) po;
            poReg.setHeight((e.getNewValue() != null && !e.getNewValue().equals("")) ? new Double(e.getNewValue().toString()) : new Double(0));
            bmi = computeBmi(poReg.getWeight(), poReg.getHeight());
        } else if (po instanceof PurchaseOrderRegister) {
            PurchaseOrderRegister poReg = (PurchaseOrderRegister) po;
            poReg.setHeight((e.getNewValue() != null && !e.getNewValue().equals("")) ? new Double(e.getNewValue().toString()) : new Double(0));
            bmi = computeBmi(poReg.getWeight(), poReg.getHeight());
        }
    }

    public void occupationCategoryListener(ValueChangeEvent event) {
        Integer occupationCategoryId = (Integer) event.getNewValue();
        this.setOccupationList(occupationCategoryId);
    }

    public void currentProvinceListener(ValueChangeEvent event) {
        Integer provinceId = (Integer) event.getNewValue();
        if(provinceId != null && provinceId != 0)
            currentDistrictList = getDistrictList1(provinceId, currentPostal);
        else if(currentDistrictList != null)
                currentDistrictList.clear();
        currentDistrictId = 0;
        if(currentSubDistrictList != null) {
            currentSubDistrictList.clear();
            currentSubDistrictId = 0;
        }
        currentPostal = "";
        msgPostalCode = "";
    }

    public void homeProvinceListener(ValueChangeEvent event) {
        Integer provinceId = (Integer) event.getNewValue();
        if(provinceId != null && provinceId != 0)
            this.setHomeDistrictList(provinceId, homePostal);
        else if(homeDistrictList != null)
                homeDistrictList.clear();
        homeDistrictId = 0;
        if(homeSubDistrictList != null) {
            homeSubDistrictList.clear();
            homeSubDistrictId = 0;
        }
        homePostal = "";
        msgPostalCode = "";
    }

    public void billingProvinceListener(ValueChangeEvent event) {
        Integer provinceId = (Integer) event.getNewValue();
        if(provinceId != null && provinceId != 0)
            this.setBillingDistrictList(provinceId, billingPostal);
        else if(billingDistrictList != null)
                billingDistrictList.clear();
        billingDistrictId = 0;
        if(billingSubDistrictList != null) {
            billingSubDistrictList.clear();
            billingSubDistrictId = 0;
        }
        billingPostal = "";
        msgPostalCode = "";
    }

    public void shippingProvinceListener(ValueChangeEvent event) {
        Integer provinceId = (Integer) event.getNewValue();
        if(provinceId != null && provinceId != 0)
            this.setShippingDistrictList(provinceId, shippingPostal);
        else if(shippingDistrictList != null)
                shippingDistrictList.clear();
        shippingDistrictId = 0;
        if(shippingSubDistrictList != null) {
            shippingSubDistrictList.clear();
            shippingSubDistrictId = 0;
        }
        shippingPostal = "";
        msgPostalCode = "";
    }
    
    public void currentDistrictListener(ValueChangeEvent event) {
        Integer districtId = (Integer) event.getNewValue();
        if(districtId != null && districtId != 0) {
            currentSubDistrictList = getSubDistrictList1(districtId, currentPostal);
        }
        currentSubDistrictId = 0;
        currentPostal = "";
        msgPostalCode = "";
    }
        
    public void homeDistrictListener(ValueChangeEvent event) {
        Integer districtId = (Integer) event.getNewValue();
        if(districtId != null && districtId != 0)
            homeSubDistrictList = getSubDistrictList1(districtId, homePostal);
        homeSubDistrictId = 0;
        homePostal = "";
        msgPostalCode = "";
    }
        
    public void shippingDistrictListener(ValueChangeEvent event) {
        Integer districtId = (Integer) event.getNewValue();
        if(districtId != null && districtId != 0)
            shippingSubDistrictList = getSubDistrictList1(districtId, shippingPostal);
        shippingSubDistrictId = 0;
        shippingPostal = "";
        msgPostalCode = "";
    }
        
    public void billingDistrictListener(ValueChangeEvent event) {
        Integer districtId = (Integer) event.getNewValue();
        if(districtId != null && districtId != 0)
            billingSubDistrictList = getSubDistrictList1(districtId, billingPostal);
        billingSubDistrictId = 0;
        billingPostal = "";
        msgPostalCode = "";
    }
        
    public void currentSubDistrictListener(ValueChangeEvent event) {
        Integer subdistrictId = (Integer) event.getNewValue();
        if(subdistrictId != null && subdistrictId != 0){
            Subdistrict subdistrict = subdistrictDAO.findSubDistrict(subdistrictId);
            currentPostal = subdistrict.getPostalCode();
        }
    }
        
    public void homeSubDistrictListener(ValueChangeEvent event) {
        Integer subdistrictId = (Integer) event.getNewValue();
        if(subdistrictId != null && subdistrictId != 0){
            Subdistrict subdistrict = subdistrictDAO.findSubDistrict(subdistrictId);
            homePostal = subdistrict.getPostalCode();
        }
    }
        
    public void shippingSubDistrictListener(ValueChangeEvent event) {
        Integer subdistrictId = (Integer) event.getNewValue();
        if(subdistrictId != null && subdistrictId != 0){
            Subdistrict subdistrict = subdistrictDAO.findSubDistrict(subdistrictId);
            shippingPostal = subdistrict.getPostalCode();
        }
    }
        
    public void billingSubDistrictListener(ValueChangeEvent event) {
        Integer subdistrictId = (Integer) event.getNewValue();
        if(subdistrictId != null && subdistrictId != 0){
            Subdistrict subdistrict = subdistrictDAO.findSubDistrict(subdistrictId);
            billingPostal = subdistrict.getPostalCode();
        }
    }
    
    public Double computeBmi(Double w, Double h) {
        Double bmi = null;
        if (w != null && h != null && h > 0) {
            h = h / 100;
            bmi = w / (h * h);
            bmi = Math.round(bmi * 10) / 10.0;

        } else {
            bmi = null;
        }
        return bmi;
    }

    public final void setFlexFields(String pageForm) {
        flexFields.clear();
        for (int i = 0; i < MAX_FLEX_FIELD; i++) {
            String fx = this.getField(FLEX_FIELD[i], FLEX_LABEL[i]);
            Boolean req = this.getRequireField(FLEX_FIELD[i]);
            String controlType = this.getControlType(FLEX_FIELD[i]);
            Integer seqNo = this.getSeqNo(FLEX_FIELD[i]);
            List<SelectItem> items = this.getItemList(FLEX_FIELD[i]);
            if (fx != null) {
                String valueF = getPoFlexField(po, i + 1);
                FlexFieldInfoValue value = new FlexFieldInfoValue(i + 1, fx, req, valueF, controlType, items, pageForm, seqNo);
                flexFields.add(value);
            }
        }

        class FlexFieldInfoValueCompare implements Comparator<FlexFieldInfoValue> {
            @Override
            public int compare(FlexFieldInfoValue o1, FlexFieldInfoValue o2) {
                return o1.getSeqNo().compareTo(o2.getSeqNo());
            }
        }
        Collections.sort(flexFields, new FlexFieldInfoValueCompare());

    }

    private String getPoFlexField(T poReg, int no) {
        String str = "";
        if (poReg instanceof TempPurchaseOrderRegister) {
            TempPurchaseOrderRegister po = (TempPurchaseOrderRegister) poReg;
            switch (no) {
                case 1:
                    str = po.getFx1();
                    break;
                case 2:
                    str = po.getFx2();
                    break;
                case 3:
                    str = po.getFx3();
                    break;
                case 4:
                    str = po.getFx4();
                    break;
                case 5:
                    str = po.getFx5();
                    break;
                case 6:
                    str = po.getFx6();
                    break;
                case 7:
                    str = po.getFx7();
                    break;
                case 8:
                    str = po.getFx8();
                    break;
                case 9:
                    str = po.getFx9();
                    break;
                case 10:
                    str = po.getFx10();
                    break;
                case 11:
                    str = po.getFx11();
                    break;
                case 12:
                    str = po.getFx12();
                    break;
                case 13:
                    str = po.getFx13();
                    break;
                case 14:
                    str = po.getFx14();
                    break;
                case 15:
                    str = po.getFx15();
                    break;
                case 16:
                    str = po.getFx16();
                    break;
                case 17:
                    str = po.getFx17();
                    break;
                case 18:
                    str = po.getFx18();
                    break;
                case 19:
                    str = po.getFx19();
                    break;
                case 20:
                    str = po.getFx20();
                    break;
                default:
                    str = "";
                    break;
            }
        } else if (poReg instanceof PurchaseOrderRegister) {
            PurchaseOrderRegister po = (PurchaseOrderRegister) poReg;
            switch (no) {
                case 1:
                    str = po.getFx1();
                    break;
                case 2:
                    str = po.getFx2();
                    break;
                case 3:
                    str = po.getFx3();
                    break;
                case 4:
                    str = po.getFx4();
                    break;
                case 5:
                    str = po.getFx5();
                    break;
                case 6:
                    str = po.getFx6();
                    break;
                case 7:
                    str = po.getFx7();
                    break;
                case 8:
                    str = po.getFx8();
                    break;
                case 9:
                    str = po.getFx9();
                    break;
                case 10:
                    str = po.getFx10();
                    break;
                case 11:
                    str = po.getFx11();
                    break;
                case 12:
                    str = po.getFx12();
                    break;
                case 13:
                    str = po.getFx13();
                    break;
                case 14:
                    str = po.getFx14();
                    break;
                case 15:
                    str = po.getFx15();
                    break;
                case 16:
                    str = po.getFx16();
                    break;
                case 17:
                    str = po.getFx17();
                    break;
                case 18:
                    str = po.getFx18();
                    break;
                case 19:
                    str = po.getFx19();
                    break;
                case 20:
                    str = po.getFx20();
                    break;
                case 21:
                    str = po.getFx21();
                    break;
                case 22:
                    str = po.getFx22();
                    break;
                case 23:
                    str = po.getFx23();
                    break;
                case 24:
                    str = po.getFx24();
                    break;
                case 25:
                    str = po.getFx25();
                    break;
                case 26:
                    str = po.getFx26();
                    break;
                case 27:
                    str = po.getFx27();
                    break;
                case 28:
                    str = po.getFx28();
                    break;
                case 29:
                    str = po.getFx29();
                    break;
                case 30:
                    str = po.getFx30();
                    break;
                case 31:
                    str = po.getFx31();
                    break;
                case 32:
                    str = po.getFx32();
                    break;
                case 33:
                    str = po.getFx33();
                    break;
                case 34:
                    str = po.getFx34();
                    break;
                case 35:
                    str = po.getFx35();
                    break;
                case 36:
                    str = po.getFx36();
                    break;
                case 37:
                    str = po.getFx37();
                    break;
                case 38:
                    str = po.getFx38();
                    break;
                case 39:
                    str = po.getFx39();
                    break;  
                case 40:
                    str = po.getFx40();
                    break;
                case 41:
                    str = po.getFx41();
                    break;
                case 42:
                    str = po.getFx42();
                    break;
                case 43:
                    str = po.getFx43();
                    break;
                case 44:
                    str = po.getFx44();
                    break;
                case 45:
                    str = po.getFx45();
                    break;
                case 46:
                    str = po.getFx46();
                    break;
                case 47:
                    str = po.getFx47();
                    break;
                case 48:
                    str = po.getFx48();
                    break;
                case 49:
                    str = po.getFx49();
                    break; 
                case 50:
                    str = po.getFx50();
                    break; 
                default:
                    str = "";
                    break;
            }
        }
        return str;
    }

    public List<FlexFieldInfoValue> getFlexFields() {
        return flexFields;
    }

    public List getCurrentDistrictList() {
        return currentDistrictList;
    }

    public void setCurrentDistrictList(List currentDistrictList) {
        this.currentDistrictList = currentDistrictList;//getDistrictList(provinceId);
    }

    public List getHomeDistrictList() {
        return homeDistrictList;
    }

    public List getOccupationList() {
        return occupationList;
    }

    public void setOccupationList(Integer occupationCategoryId) {
        //List<SelectItem> values = new ArrayList<SelectItem>();
        List occupations = occupationDAO.findOccupationByOccupationCategoryId1(occupationCategoryId);
        //for (Occupation obj : occupations) {
        //    values.add(new SelectItem(obj.getId(), obj.getName()));
        //}
        occupationList = occupations;
    }



    public void setHomeDistrictList(Integer provinceId, String postalCode) {
        this.homeDistrictList = getDistrictList1(provinceId, postalCode);
    }

    public List getBillingDistrictList() {
        return billingDistrictList;
    }

    public void setBillingDistrictList(Integer provinceId, String postalCode) {
        this.billingDistrictList = getDistrictList1(provinceId, postalCode);
    }

    public List getShippingDistrictList() {
        return shippingDistrictList;
    }

    public void setShippingDistrictList(Integer provinceId, String postalCode) {
        this.shippingDistrictList = getDistrictList1(provinceId, postalCode);
    }

    public Integer getBillingProvinceId() {
        return billingProvinceId;
    }

    public void setBillingProvinceId(Integer billingProvinceId) {
        this.billingProvinceId = billingProvinceId;
    }

    public Integer getCurrentProvinceId() {
        return currentProvinceId;
    }

    public void setCurrentProvinceId(Integer currentProvinceId) {
        this.currentProvinceId = currentProvinceId;
    }

    public Integer getHomeProvinceId() {
        return homeProvinceId;
    }

    public void setHomeProvinceId(Integer homeProvinceId) {
        this.homeProvinceId = homeProvinceId;
    }

    public Integer getShippingProvinceId() {
        return shippingProvinceId;
    }

    public void setShippingProvinceId(Integer shippingProvinceId) {
        this.shippingProvinceId = shippingProvinceId;
    }

    public Integer getOccupationId() {
        return occupationId;
    }

    public void setOccupationId(Integer occupationId) {
        this.occupationId = occupationId;
    }

    public Integer getOccupationCategoryId() {
        return occupationCategoryId;
    }

    public void setOccupationCategoryId(Integer occupationCategoryId) {
        this.occupationCategoryId = occupationCategoryId;
    }

    public String getGroupTitle() {
        return this.getField(GROUPTITLE_FIELD, GROUPTITLE_LABEL);
    }

    public boolean isRequireGroupTitle() {
        return this.getRequireField(GROUPTITLE_FIELD);
    }

    public String getInitial() {
        return this.getField(INITIAL_FIELD, INITIAL_LABEL);
    }

    public boolean isRequireInitial() {
        return this.getRequireField(INITIAL_FIELD);
    }
    
    public String getInitialControlType() {
        return this.getControlType(INITIAL_FIELD);
    }
    
    public List<SelectItem> getInitialList() {
        if (initialList==null) {
            initialList = this.getItemList(INITIAL_FIELD);
        }
        return initialList;
    }

    public String getName() {
        return this.getField(NAME_FIELD, NAME_LABEL);
    }

    public boolean isRequireName() {
        return this.getRequireField(NAME_FIELD);
    }
    
    public String getSurname() {
        return this.getField(SURNAME_FIELD, SURNAME_LABEL);
    }

    public boolean isRequireSurname() {
        return this.getRequireField(SURNAME_FIELD);
    }
    
    public String getIdcardTypeId() {
        return this.getField(IDCARDTYPEID_FIELD, IDCARDTYPEID_LABEL);
    }

    public boolean isRequireIdcardTypeId() {
        return this.getRequireField(IDCARDTYPEID_FIELD);
    }
    
    public String getIdno() {
        return this.getField(IDNO_FIELD, IDNO_LABEL);
    }

    public boolean isRequireIdno() {
        return this.getRequireField(IDNO_FIELD);
    }
    
    public String getIdCardIssue() {
        return this.getField(IDCARD_ISSUE_FIELD, IDCARD_ISSUE_LABEL);
    }

    public boolean isRequireIdCardIssue() {
        return this.getRequireField(IDCARD_ISSUE_FIELD);
    }
    
    public String getIdCardIssueControlType() {
        return this.getControlType(IDCARD_ISSUE_FIELD);
    }
    
    public List<SelectItem> getIdCardIssueList() {
        if (idCardIssueList==null) {
            idCardIssueList = this.getItemList(IDCARD_ISSUE_FIELD);
        }
        return idCardIssueList;
    }
    
    public String getIdcardExpiryDate() {
        return this.getField(IDCARD_EXPIRYDATE_FIELD, IDCARD_EXPIRYDATE_LABEL);
    }

    public boolean isRequireIdcardExpiryDate() {
        return this.getRequireField(IDCARD_EXPIRYDATE_FIELD);
    }


    public String getDob() {
        return this.getField(DOB_FIELD, DOB_LABEL);
    }

    public boolean isRequireDob() {
        return this.getRequireField(DOB_FIELD);
    }

    public void setAge(Date dobDate) {
        int noMonth = (ageCalMonth != null) ? ageCalMonth.intValue() : 0;
        int noDay = (ageCalDay != null) ? ageCalDay.intValue() : 0;
        Calendar dob = Calendar.getInstance(Locale.US);
        dob.setTime(dobDate);
        Calendar today = Calendar.getInstance(Locale.US);
        int doby = dob.get(Calendar.YEAR);
        int tdy = today.get(Calendar.YEAR);
        int dobm = dob.get(Calendar.MONTH);
        int tdm = today.get(Calendar.MONTH);
        int dobd = dob.get(Calendar.DATE);
        int tdd = today.get(Calendar.DATE);
        age = (tdy - doby);        
        
        int ageYear = this.calculateAge(dob.getTime(), "year");
        int ageMonth = this.calculateAge(dob.getTime(), "month");
        int ageDay = this.calculateAge(dob.getTime(), "day");
        int dobDayOfYear = dob.get(Calendar.DAY_OF_YEAR);
        int todayDayOfYear = today.get(Calendar.DAY_OF_YEAR);
        int diffDayOfYear = 0;
        //System.out.println("Y = " + ageYear);
        //System.out.println("M = " + ageMonth);
        //System.out.println("D = " + ageDay);
        if(ageCalMethod != null && !ageCalMethod.equals("")){
            if(ageCalMethod.equals("day")){ 
                if(dobm > tdm){
                    age--;
                }else if(dobm == tdm && dobd > tdd){
                    age--;
                }
            } else if(ageCalMethod.equals("month")) {
                //Integer diff = this.differenceInMonths(dob, today);
                age = ageYear;
                if(ageMonth >= noMonth) {
                    age++;
                }
            } else if(ageCalMethod.equals("dayrange")) {
//                if(todayDayOfYear >= dobDayOfYear) {
//                    diffDayOfYear = todayDayOfYear - dobDayOfYear;
//                    if(diffDayOfYear >= noDay) {
//                        age++;
//                    }
//                }
                int[] ageYearAndDay = this.calculateAgeByCompleteYearAndDayFragment(dobDate);
                ageCompleteYear = ageYearAndDay[0];
                ageDayAfterCompleteYear = ageYearAndDay[1];

                age = ageCompleteYear;
                if (ageDayAfterCompleteYear>=noDay) {
                    age++;
                }
            }

        } 
        
        if (po instanceof PurchaseOrderRegister) {
            ((PurchaseOrderRegister) po).setAge(age);
            ((PurchaseOrderRegister) po).setDob(dobDate);
        }
        //if ((m1 > m2) || (m1 == m2 && d1 > d2)) //ถ้าเดือนเกิดมากกว่าเดือนปัจจุบัน หรือวันเกิดมากกว่าวันปัจจุบัน -> ยังไม่ครบปี
        //{
        //    age--;
        //}
    }

    private Integer getAgeCustomer(Calendar beginningDate, Calendar endingDate) {
        if (beginningDate == null || endingDate == null) {
            return 0;
        }
        int m1 = beginningDate.get(Calendar.YEAR) * 12 + beginningDate.get(Calendar.MONTH);
        int m2 = endingDate.get(Calendar.YEAR) * 12 + endingDate.get(Calendar.MONTH);
        Integer m3 = m2-m1;
        int i = new Double(Math.floor(m3/12)).intValue();
        return new Integer(i);
    }

    private Integer differenceInMonths(Calendar beginningDate, Calendar endingDate) {
        if (beginningDate == null || endingDate == null) {
            return 0;
        }
        int m1 = beginningDate.get(Calendar.YEAR) * 12 + beginningDate.get(Calendar.MONTH);
        int m2 = endingDate.get(Calendar.YEAR) * 12 + endingDate.get(Calendar.MONTH);
        Integer m3 = m2-m1;
        
        return (m3 % 12);
    }
    
    private int calculateAge(Date birthDate, String param) {
        int years = 0;
        int months = 0;
        int days = 0;
        //create calendar object for birth day
        Calendar birthDay = Calendar.getInstance(Locale.US);
        birthDay.setTimeInMillis(birthDate.getTime());
        //create calendar object for current day
        long currentTime = System.currentTimeMillis();
        Calendar now = Calendar.getInstance(Locale.US);
        now.setTimeInMillis(currentTime);
        //Get difference between years
        years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
        int currMonth = now.get(Calendar.MONTH) + 1;
        int birthMonth = birthDay.get(Calendar.MONTH) + 1;
        //Get difference between months
        months = currMonth - birthMonth;
        //if month difference is in negative then reduce years by one and calculate the number of months.
        if (months < 0) {
            years--;
            months = 12 - birthMonth + currMonth;
            if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
                months--;
            }
        } else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
            years--;
            months = 11;
        }
        //Calculate the days
        if (now.get(Calendar.DATE) > birthDay.get(Calendar.DATE)) {
            days = now.get(Calendar.DATE) - birthDay.get(Calendar.DATE);
        } else if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
            int today = now.get(Calendar.DAY_OF_MONTH);
            now.add(Calendar.MONTH, -1);
            days = now.getActualMaximum(Calendar.DAY_OF_MONTH) - birthDay.get(Calendar.DAY_OF_MONTH) + today;
            months--;
        } else {
            days = 0;
            if (months == 12) {
                years++;
                months = 0;
            }
        }
        
        int i = 0;
        if(param.equals("year")){
            i = years;
        }else if(param.equals("month")){
            i = months;
        }else if(param.equals("day")){
            i = days;
        }
        
        return i;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return this.getField(GENDER_FIELD, GENDER_LABEL);
    }

    public boolean isRequireGender() {
        return this.getRequireField(GENDER_FIELD);
    }

    public String getWeight() {
        return this.getField(WEIGHT_FIELD, WEIGHT_LABEL);
    }

    public boolean isRequireWeight() {
        return this.getRequireField(WEIGHT_FIELD);
    }

    public String getHeight() {
        return this.getField(HEIGHT_FIELD, HEIGHT_LABEL);
    }

    public boolean isRequireHeight() {
        return this.getRequireField(HEIGHT_FIELD);
    }

    public String getMaritalStatus() {
        return this.getField(MARITALSTATUS_FIELD, MARITALSTATUS_LABEL);
    }

    public boolean isRequireMaritalStatus() {
        return this.getRequireField(MARITALSTATUS_FIELD);
    }

    public String getBmi() {
        return this.getField(BMI_FIELD, BMI_LABEL);
    }

    public boolean isRequireBmi() {
        return this.getRequireField(BMI_FIELD);
    }

    public Double getBmiValue() {
        if (po instanceof PurchaseOrderRegister) {
            PurchaseOrderRegister poReg = (PurchaseOrderRegister) po;
            if(poReg != null && poReg.getWeight() != null && poReg.getHeight() != null){
                bmi = computeBmi(poReg.getWeight(), poReg.getHeight());
            }
        }
        return bmi;
    }
    
    public String getCurrentAddress() {
        return this.getField(CURRENTADDRESS_FIELD, CURRENTADDRESS_LABEL);
    }

    public String getCurrentAddressFullName() {
        return this.getField(CURRENTADDRESS_FULLNAME_FIELD, CURRENTADDRESS_FULLNAME_LABEL);
    }

    public boolean isRequireCurrentAddressFullName() {
        return this.getRequireField(CURRENTADDRESS_FULLNAME_FIELD);
    }
    
    public String getCurrentAddress1() {
        return this.getField(CURRENTADDRESS1_FIELD, CURRENTADDRESS1_LABEL);
    }

    public boolean isRequireCurrentAddress1() {
        return this.getRequireField(CURRENTADDRESS1_FIELD);
    }
    
    public String getCurrentAddress2() {
        return this.getField(CURRENTADDRESS2_FIELD, CURRENTADDRESS2_LABEL);
    }

    public boolean isRequireCurrentAddress2() {
        return this.getRequireField(CURRENTADDRESS2_FIELD);
    }
    
    public String getCurrentAddress3() {
        return this.getField(CURRENTADDRESS3_FIELD, CURRENTADDRESS3_LABEL);
    }

    public boolean isRequireCurrentAddress3() {
        return this.getRequireField(CURRENTADDRESS3_FIELD);
    }
    
    public String getCurrentAddress4() {
        return this.getField(CURRENTADDRESS4_FIELD, CURRENTADDRESS4_LABEL);
    }

    public boolean isRequireCurrentAddress4() {
        return this.getRequireField(CURRENTADDRESS4_FIELD);
    }
    
    public String getCurrentAddress5() {
        return this.getField(CURRENTADDRESS5_FIELD, CURRENTADDRESS5_LABEL);
    }

    public boolean isRequireCurrentAddress5() {
        return this.getRequireField(CURRENTADDRESS5_FIELD);
    }
    
    public String getCurrentAddress6() {
        return this.getField(CURRENTADDRESS6_FIELD, CURRENTADDRESS6_LABEL);
    }

    public boolean isRequireCurrentAddress6() {
        return this.getRequireField(CURRENTADDRESS6_FIELD);
    }

    public String getCurrentAddress7() {
        return this.getField(CURRENTADDRESS7_FIELD, CURRENTADDRESS7_LABEL);
    }

    public boolean isRequireCurrentAddress7() {
        return this.getRequireField(CURRENTADDRESS7_FIELD);
    }
    
    public String getCurrentAddress8() {
        return this.getField(CURRENTADDRESS8_FIELD, CURRENTADDRESS8_LABEL);
    }

    public boolean isRequireCurrentAddress8() {
        return this.getRequireField(CURRENTADDRESS8_FIELD);
    }
    
    public String getCurrentAddressProvince() {
        return this.getField(CURRENTADDRESS_PROVINCE_FIELD, CURRENTADDRESS_PROVINCE_LABEL);
    }

    public boolean isRequireCurrentAddressProvince() {
        return this.getRequireField(CURRENTADDRESS_PROVINCE_FIELD);
    }
    
    public String getCurrentAddressPostal() {
        return this.getField(CURRENTADDRESS_POSTAL_FIELD, CURRENTADDRESS_POSTAL_LABEL);
    }

    public boolean isRequireCurrentAddressPostal() {
        return this.getRequireField(CURRENTADDRESS_POSTAL_FIELD);
    }
    
    public String getCurrentAddressTelephone1() {
        return this.getField(CURRENTADDRESS_TELEPHONE1_FIELD, CURRENTADDRESS_TELEPHONE1_LABEL);
    }

    public boolean isRequireCurrentAddressTelephone1() {
        return this.getRequireField(CURRENTADDRESS_TELEPHONE1_FIELD);
    }
    
    public String getCurrentAddressTelephone2() {
        return this.getField(CURRENTADDRESS_TELEPHONE2_FIELD, CURRENTADDRESS_TELEPHONE2_LABEL);
    }

    public boolean isRequireCurrentAddressTelephone2() {
        return this.getRequireField(CURRENTADDRESS_TELEPHONE2_FIELD);
    }
    //------------------------
    public String getHomeAddress() {
        return this.getField(HOMEADDRESS_FIELD, HOMEADDRESS_LABEL);
    }
        
    public String getHomeAddressFullName() {
        return this.getField(HOMEADDRESS_FULLNAME_FIELD, HOMEADDRESS_FULLNAME_LABEL);
    }

    public boolean isRequireHomeAddressFullName() {
        return this.getRequireField(HOMEADDRESS_FULLNAME_FIELD);
    }
    
    public String getHomeAddress1() {
        return this.getField(HOMEADDRESS1_FIELD, HOMEADDRESS1_LABEL);
    }

    public boolean isRequireHomeAddress1() {
        return this.getRequireField(HOMEADDRESS1_FIELD);
    }
    
    public String getHomeAddress2() {
        return this.getField(HOMEADDRESS2_FIELD, HOMEADDRESS2_LABEL);
    }

    public boolean isRequireHomeAddress2() {
        return this.getRequireField(HOMEADDRESS2_FIELD);
    }
    
    public String getHomeAddress3() {
        return this.getField(HOMEADDRESS3_FIELD, HOMEADDRESS3_LABEL);
    }

    public boolean isRequireHomeAddress3() {
        return this.getRequireField(HOMEADDRESS3_FIELD);
    }
    
    public String getHomeAddress4() {
        return this.getField(HOMEADDRESS4_FIELD, HOMEADDRESS4_LABEL);
    }

    public boolean isRequireHomeAddress4() {
        return this.getRequireField(HOMEADDRESS4_FIELD);
    }
    
    public String getHomeAddress5() {
        return this.getField(HOMEADDRESS5_FIELD, HOMEADDRESS5_LABEL);
    }

    public boolean isRequireHomeAddress5() {
        return this.getRequireField(HOMEADDRESS5_FIELD);
    }
    
    public String getHomeAddress6() {
        return this.getField(HOMEADDRESS6_FIELD, HOMEADDRESS6_LABEL);
    }

    public boolean isRequireHomeAddress6() {
        return this.getRequireField(HOMEADDRESS6_FIELD);
    }

    public String getHomeAddress7() {
        return this.getField(HOMEADDRESS7_FIELD, HOMEADDRESS7_LABEL);
    }

    public boolean isRequireHomeAddress7() {
        return this.getRequireField(HOMEADDRESS7_FIELD);
    }
    
    public String getHomeAddress8() {
        return this.getField(HOMEADDRESS8_FIELD, HOMEADDRESS8_LABEL);
    }

    public boolean isRequireHomeAddress8() {
        return this.getRequireField(HOMEADDRESS8_FIELD);
    }
    
    public String getHomeAddressProvince() {
        return this.getField(HOMEADDRESS_PROVINCE_FIELD, HOMEADDRESS_PROVINCE_LABEL);
    }

    public boolean isRequireHomeAddressProvince() {
        return this.getRequireField(HOMEADDRESS_PROVINCE_FIELD);
    }
    
    public String getHomeAddressPostal() {
        return this.getField(HOMEADDRESS_POSTAL_FIELD, HOMEADDRESS_POSTAL_LABEL);
    }

    public boolean isRequireHomeAddressPostal() {
        return this.getRequireField(HOMEADDRESS_POSTAL_FIELD);
    }
    
    public String getHomeAddressTelephone1() {
        return this.getField(HOMEADDRESS_TELEPHONE1_FIELD, HOMEADDRESS_TELEPHONE1_LABEL);
    }

    public boolean isRequireHomeAddressTelephone1() {
        return this.getRequireField(HOMEADDRESS_TELEPHONE1_FIELD);
    }
    
    public String getHomeAddressTelephone2() {
        return this.getField(HOMEADDRESS_TELEPHONE2_FIELD, HOMEADDRESS_TELEPHONE2_LABEL);
    }

    public boolean isRequireHomeAddressTelephone2() {
        return this.getRequireField(HOMEADDRESS_TELEPHONE2_FIELD);
    } 

//----
    public String getBillingAddress() {
        return this.getField(BILLINGADDRESS_FIELD, BILLINGADDRESS_LABEL);
    }
    
    public String getBillingAddressFullName() {
        return this.getField(BILLINGADDRESS_FULLNAME_FIELD, BILLINGADDRESS_FULLNAME_LABEL);
    }

    public boolean isRequireBillingAddressFullName() {
        return this.getRequireField(BILLINGADDRESS_FULLNAME_FIELD);
    }
    
    public String getBillingAddress1() {
        return this.getField(BILLINGADDRESS1_FIELD, BILLINGADDRESS1_LABEL);
    }

    public boolean isRequireBillingAddress1() {
        return this.getRequireField(BILLINGADDRESS1_FIELD);
    }
    
    public String getBillingAddress2() {
        return this.getField(BILLINGADDRESS2_FIELD, BILLINGADDRESS2_LABEL);
    }

    public boolean isRequireBillingAddress2() {
        return this.getRequireField(BILLINGADDRESS2_FIELD);
    }
    
    public String getBillingAddress3() {
        return this.getField(BILLINGADDRESS3_FIELD, BILLINGADDRESS3_LABEL);
    }

    public boolean isRequireBillingAddress3() {
        return this.getRequireField(BILLINGADDRESS3_FIELD);
    }
    
    public String getBillingAddress4() {
        return this.getField(BILLINGADDRESS4_FIELD, BILLINGADDRESS4_LABEL);
    }

    public boolean isRequireBillingAddress4() {
        return this.getRequireField(BILLINGADDRESS4_FIELD);
    }
    
    public String getBillingAddress5() {
        return this.getField(BILLINGADDRESS5_FIELD, BILLINGADDRESS5_LABEL);
    }

    public boolean isRequireBillingAddress5() {
        return this.getRequireField(BILLINGADDRESS5_FIELD);
    }
    
    public String getBillingAddress6() {
        return this.getField(BILLINGADDRESS6_FIELD, BILLINGADDRESS6_LABEL);
    }

    public boolean isRequireBillingAddress6() {
        return this.getRequireField(BILLINGADDRESS6_FIELD);
    }

    public String getBillingAddress7() {
        return this.getField(BILLINGADDRESS7_FIELD, BILLINGADDRESS7_LABEL);
    }

    public boolean isRequireBillingAddress7() {
        return this.getRequireField(BILLINGADDRESS7_FIELD);
    }
    
    public String getBillingAddress8() {
        return this.getField(BILLINGADDRESS8_FIELD, BILLINGADDRESS8_LABEL);
    }

    public boolean isRequireBillingAddress8() {
        return this.getRequireField(BILLINGADDRESS8_FIELD);
    }
    
    public String getBillingAddressProvince() {
        return this.getField(BILLINGADDRESS_PROVINCE_FIELD, BILLINGADDRESS_PROVINCE_LABEL);
    }

    public boolean isRequireBillingAddressProvince() {
        return this.getRequireField(BILLINGADDRESS_PROVINCE_FIELD);
    }
    
    public String getBillingAddressPostal() {
        return this.getField(BILLINGADDRESS_POSTAL_FIELD, BILLINGADDRESS_POSTAL_LABEL);
    }

    public boolean isRequireBillingAddressPostal() {
        return this.getRequireField(BILLINGADDRESS_POSTAL_FIELD);
    }
    
    public String getBillingAddressTelephone1() {
        return this.getField(BILLINGADDRESS_TELEPHONE1_FIELD, BILLINGADDRESS_TELEPHONE1_LABEL);
    }

    public boolean isRequireBillingAddressTelephone1() {
        return this.getRequireField(BILLINGADDRESS_TELEPHONE1_FIELD);
    }
    
    public String getBillingAddressTelephone2() {
        return this.getField(BILLINGADDRESS_TELEPHONE2_FIELD, BILLINGADDRESS_TELEPHONE2_LABEL);
    }

    public boolean isRequireBillingAddressTelephone2() {
        return this.getRequireField(BILLINGADDRESS_TELEPHONE2_FIELD);
    } 
    //----
    public String getShippingAddress() {
        return this.getField(SHIPPINGADDRESS_FIELD, SHIPPINGADDRESS_LABEL);
    }

    public String getShippingAddressFullName() {
        return this.getField(SHIPPINGADDRESS_FULLNAME_FIELD, SHIPPINGADDRESS_FULLNAME_LABEL);
    }

    public boolean isRequireShippingAddressFullName() {
        return this.getRequireField(SHIPPINGADDRESS_FULLNAME_FIELD);
    }
    
    public String getShippingAddress1() {
        return this.getField(SHIPPINGADDRESS1_FIELD, SHIPPINGADDRESS1_LABEL);
    }

    public boolean isRequireShippingAddress1() {
        return this.getRequireField(SHIPPINGADDRESS1_FIELD);
    }
    
    public String getShippingAddress2() {
        return this.getField(SHIPPINGADDRESS2_FIELD, SHIPPINGADDRESS2_LABEL);
    }

    public boolean isRequireShippingAddress2() {
        return this.getRequireField(SHIPPINGADDRESS2_FIELD);
    }
    
    public String getShippingAddress3() {
        return this.getField(SHIPPINGADDRESS3_FIELD, SHIPPINGADDRESS3_LABEL);
    }

    public boolean isRequireShippingAddress3() {
        return this.getRequireField(SHIPPINGADDRESS3_FIELD);
    }
    
    public String getShippingAddress4() {
        return this.getField(SHIPPINGADDRESS4_FIELD, SHIPPINGADDRESS4_LABEL);
    }

    public boolean isRequireShippingAddress4() {
        return this.getRequireField(SHIPPINGADDRESS4_FIELD);
    }
    
    public String getShippingAddress5() {
        return this.getField(SHIPPINGADDRESS5_FIELD, SHIPPINGADDRESS5_LABEL);
    }

    public boolean isRequireShippingAddress5() {
        return this.getRequireField(SHIPPINGADDRESS5_FIELD);
    }
    
    public String getShippingAddress6() {
        return this.getField(SHIPPINGADDRESS6_FIELD, SHIPPINGADDRESS6_LABEL);
    }

    public boolean isRequireShippingAddress6() {
        return this.getRequireField(SHIPPINGADDRESS6_FIELD);
    }

    public String getShippingAddress7() {
        return this.getField(SHIPPINGADDRESS7_FIELD, SHIPPINGADDRESS7_LABEL);
    }

    public boolean isRequireShippingAddress7() {
        return this.getRequireField(SHIPPINGADDRESS7_FIELD);
    }
    
    public String getShippingAddress8() {
        return this.getField(SHIPPINGADDRESS8_FIELD, SHIPPINGADDRESS8_LABEL);
    }

    public boolean isRequireShippingAddress8() {
        return this.getRequireField(SHIPPINGADDRESS8_FIELD);
    }
    
    public String getShippingAddressProvince() {
        return this.getField(SHIPPINGADDRESS_PROVINCE_FIELD, SHIPPINGADDRESS_PROVINCE_LABEL);
    }

    public boolean isRequireShippingAddressProvince() {
        return this.getRequireField(SHIPPINGADDRESS_PROVINCE_FIELD);
    }
    
    public String getShippingAddressPostal() {
        return this.getField(SHIPPINGADDRESS_POSTAL_FIELD, SHIPPINGADDRESS_POSTAL_LABEL);
    }

    public boolean isRequireShippingAddressPostal() {
        return this.getRequireField(SHIPPINGADDRESS_POSTAL_FIELD);
    }
    
    public String getShippingAddressTelephone1() {
        return this.getField(SHIPPINGADDRESS_TELEPHONE1_FIELD, SHIPPINGADDRESS_TELEPHONE1_LABEL);
    }

    public boolean isRequireShippingAddressTelephone1() {
        return this.getRequireField(SHIPPINGADDRESS_TELEPHONE1_FIELD);
    }
    
    public String getShippingAddressTelephone2() {
        return this.getField(SHIPPINGADDRESS_TELEPHONE2_FIELD, SHIPPINGADDRESS_TELEPHONE2_LABEL);
    }

    public boolean isRequireShippingAddressTelephone2() {
        return this.getRequireField(SHIPPINGADDRESS_TELEPHONE2_FIELD);
    } 
    //------


    public String getHomePhone() {
        return this.getField(HOMEPHONE_FIELD, HOMEPHONE_LABEL);
    }

    public boolean isRequireHomePhone() {
        return this.getRequireField(HOMEPHONE_FIELD);
    }

    public String getOfficePhone() {
        return this.getField(OFFICEPHONE_FIELD, OFFICEPHONE_LABEL);
    }

    public boolean isRequireOfficePhone() {
        return this.getRequireField(OFFICEPHONE_FIELD);
    }

    public String getMobilePhone() {
        return this.getField(MOBILEPHONE_FIELD, MOBILEPHONE_LABEL);
    }

    public boolean isRequireMobilePhone() {
        return this.getRequireField(MOBILEPHONE_FIELD);
    }

    public String getEmail() {
        return this.getField(EMAIL_FIELD, EMAIL_LABEL);
    }

    public boolean isRequireEmail() {
        return this.getRequireField(EMAIL_FIELD);
    }

    public String getNationality() {
        return this.getField(NATIONALITY_FIELD, NATIONALITY_LABEL);
    }

    public boolean isRequireNationality() {
        return this.getRequireField(NATIONALITY_FIELD);
    }

    public String getNationalityControlType() {
        return this.getControlType(NATIONALITY_FIELD);
    }
    
    public List<SelectItem> getNationalityList() {
        if (nationalityList==null) {
            nationalityList = this.getItemList(NATIONALITY_FIELD);
        }
        return nationalityList;
    }
    
    public String getRace() {
        return this.getField(RACE_FIELD, RACE_LABEL);
    }

    public boolean isRequireRace() {
        return this.getRequireField(RACE_FIELD);
    }

    public String getRaceControlType() {
        return this.getControlType(RACE_FIELD);
    }
    
    public List<SelectItem> getRaceList() {
        if (raceList==null) {
            raceList = this.getItemList(RACE_FIELD);
        }
        return raceList;
    }
    
    public String getReligion() {
        return this.getField(RELIGION_FIELD, RELIGION_LABEL);
    }

    public boolean isRequireReligion() {
        return this.getRequireField(RELIGION_FIELD);
    }

    public String getReligionControlType() {
        return this.getControlType(RELIGION_FIELD);
    }
    
    public List<SelectItem> getReligionList() {
        if (religionList==null) {
            religionList = this.getItemList(RELIGION_FIELD);
        }
        return religionList;
    }
    
    public String getOccupation() {
        return this.getField(OCCUPATION_FIELD, OCCUPATION_LABEL);
    }

    public boolean isRequireOccupation() {
        return this.getRequireField(OCCUPATION_FIELD);
    }
    
    public String getOccupationText() {
        return this.getField(OCCUPATIONTEXT_FIELD, OCCUPATIONTEXT_LABEL);
    }

    public boolean isRequireOccupationText() {
        return this.getRequireField(OCCUPATIONTEXT_FIELD);
    }    
    
    public String getOccupationTextControlType() {
        return this.getControlType(OCCUPATIONTEXT_FIELD);
    }
    
    public List<SelectItem> getOccupationTextList() {
        return this.getItemList(OCCUPATIONTEXT_FIELD);
    }
    
    public String getJob() {
        return this.getField(JOB_FIELD, JOB_LABEL);
    }

    public boolean isRequireJob() {
        return this.getRequireField(JOB_FIELD);
    }
    
    public String getJobPosition() {
        return this.getField(JOBPOSITION_FIELD, JOBPOSITION_LABEL);
    }

    public boolean isRequireJobPosition() {
        return this.getRequireField(JOBPOSITION_FIELD);
    }
    
    public String getBusinessType() {
        return this.getField(BUSINESSTYPE_FIELD, BUSINESSTYPE_LABEL);
    }

    public boolean isRequireBusinessType() {
        return this.getRequireField(BUSINESSTYPE_FIELD);
    }
    
    public String getIncome() {
        return this.getField(INCOME_FIELD, INCOME_LABEL);
    }

    public boolean isRequireIncome() {
        return this.getRequireField(INCOME_FIELD);
    }
       
    

    public Integer getBillingDistrictId() {
        return billingDistrictId;
    }

    public void setBillingDistrictId(Integer billingDistrictId) {
        this.billingDistrictId = billingDistrictId;
    }

    public Integer getCurrentDistrictId() {
        return currentDistrictId;
    }

    public void setCurrentDistrictId(Integer currentDistrictId) {
        this.currentDistrictId = currentDistrictId;
    }

    public Integer getHomeDistrictId() {
        return homeDistrictId;
    }

    public void setHomeDistrictId(Integer homeDistrictId) {
        this.homeDistrictId = homeDistrictId;
    }

    public Integer getShippingDistrictId() {
        return shippingDistrictId;
    }

    public void setShippingDistrictId(Integer shippingDistrictId) {
        this.shippingDistrictId = shippingDistrictId;
    }

//    public Double getBmi() {
//        return bmi;
//    }
//
//    public void setBmi(Double bmi) {
//        this.bmi = bmi;
//    }

    public DistrictDAO getDistrictDAO() {
        return districtDAO;
    }

    public void setDistrictDAO(DistrictDAO districtDAO) {
        this.districtDAO = districtDAO;
    }

    public OccupationDAO getOccupationDAO() {
        return occupationDAO;
    }

    public void setOccupationDAO(OccupationDAO occupationDAO) {
        this.occupationDAO = occupationDAO;
    }

    public List<Province> getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(List<Province> provinceList) {
        this.provinceList = provinceList;
    }

    public Map<String, RegistrationField> getRegisMap() {
        return regisMap;
    }

    public void setRegisMap(Map<String, RegistrationField> regisMap) {
        this.regisMap = regisMap;
    }

    public RegistrationForm getRegistrationForm() {
        return registrationForm;
    }

    public void setRegistrationForm(RegistrationForm registrationForm) {
        this.registrationForm = registrationForm;
    }

    public Occupation getOcc() {
        return occ;
    }

    public void setOcc(Occupation occ) {
        this.occ = occ;
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

    public Subdistrict getBillingSubDistrict() {
        return billingSubDistrict;
    }

    public void setBillingSubDistrict(Subdistrict billingSubDistrict) {
        this.billingSubDistrict = billingSubDistrict;
    }

    public Integer getBillingSubDistrictId() {
        return billingSubDistrictId;
    }

    public void setBillingSubDistrictId(Integer billingSubDistrictId) {
        this.billingSubDistrictId = billingSubDistrictId;
    }

    public List getBillingSubDistrictList() {
        return billingSubDistrictList;
    }

    public void setBillingSubDistrictList(List billingSubDistrictList) {
        this.billingSubDistrictList = billingSubDistrictList;
    }

    public Subdistrict getCurrentSubDistrict() {
        return currentSubDistrict;
    }

    public void setCurrentSubDistrict(Subdistrict currentSubDistrict) {
        this.currentSubDistrict = currentSubDistrict;
    }

    public Integer getCurrentSubDistrictId() {
        return currentSubDistrictId;
    }

    public void setCurrentSubDistrictId(Integer currentSubDistrictId) {
        this.currentSubDistrictId = currentSubDistrictId;
    }

    public List getCurrentSubDistrictList() {
        return currentSubDistrictList;
    }

    public void setCurrentSubDistrictList(List currentSubDistrictList) {
        this.currentSubDistrictList = currentSubDistrictList;
    }

    public Subdistrict getHomeSubDistrict() {
        return homeSubDistrict;
    }

    public void setHomeSubDistrict(Subdistrict homeSubDistrict) {
        this.homeSubDistrict = homeSubDistrict;
    }

    public Integer getHomeSubDistrictId() {
        return homeSubDistrictId;
    }

    public void setHomeSubDistrictId(Integer homeSubDistrictId) {
        this.homeSubDistrictId = homeSubDistrictId;
    }

    public List getHomeSubDistrictList() {
        return homeSubDistrictList;
    }

    public void setHomeSubDistrictList(List homeSubDistrictList) {
        this.homeSubDistrictList = homeSubDistrictList;
    }

    public Subdistrict getShippingSubDistrict() {
        return shippingSubDistrict;
    }

    public void setShippingSubDistrict(Subdistrict shippingSubDistrict) {
        this.shippingSubDistrict = shippingSubDistrict;
    }

    public Integer getShippingSubDistrictId() {
        return shippingSubDistrictId;
    }

    public void setShippingSubDistrictId(Integer shippingSubDistrictId) {
        this.shippingSubDistrictId = shippingSubDistrictId;
    }

    public List getShippingSubDistrictList() {
        return shippingSubDistrictList;
    }

    public void setShippingSubDistrictList(List shippingSubDistrictList) {
        this.shippingSubDistrictList = shippingSubDistrictList;
    }

    public SubdistrictDAO getSubdistrictDAO() {
        return subdistrictDAO;
    }

    public void setSubdistrictDAO(SubdistrictDAO subdistrictDAO) {
        this.subdistrictDAO = subdistrictDAO;
    }

    public List getBillingPostalList() {
        return billingPostalList;
    }

    public void setBillingPostalList(List billingPostalList) {
        this.billingPostalList = billingPostalList;
    }

    public List getCurrentPostalList() {
        return currentPostalList;
    }

    public void setCurrentPostalList(List currentPostalList) {
        this.currentPostalList = currentPostalList;
    }

    public List getHomePostalList() {
        return homePostalList;
    }

    public void setHomePostalList(List homePostalList) {
        this.homePostalList = homePostalList;
    }

    public List getShippingPostalList() {
        return shippingPostalList;
    }

    public void setShippingPostalList(List shippingPostalList) {
        this.shippingPostalList = shippingPostalList;
    }

    public String getCurrentPostal() {
        return currentPostal;
    }

    public void setCurrentPostal(String currentPostal) {
        this.currentPostal = currentPostal;
    }

    public String getHomePostal() {
        return homePostal;
    }

    public void setHomePostal(String homePostal) {
        this.homePostal = homePostal;
    }

    public String getBillingPostal() {
        return billingPostal;
    }

    public void setBillingPostal(String billingPostal) {
        this.billingPostal = billingPostal;
    }

    public String getShippingPostal() {
        return shippingPostal;
    }

    public void setShippingPostal(String shippingPostal) {
        this.shippingPostal = shippingPostal;
    }

    public String getAgeCalMethod() {
        return ageCalMethod;
    }

    public void setAgeCalMethod(String ageCalMethod) {
        this.ageCalMethod = ageCalMethod;
    }

    public Integer getAgeCalMonth() {
        return ageCalMonth;
    }

    public void setAgeCalMonth(Integer ageCalMonth) {
        this.ageCalMonth = ageCalMonth;
    }
    
    public String getId1() {
        return id1;
}

    public void setId1(String id1) {
        this.id1 = id1;
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }

    public String getId3() {
        return id3;
    }

    public void setId3(String id3) {
        this.id3 = id3;
    }

    public String getId4() {
        return id4;
    }

    public void setId4(String id4) {
        this.id4 = id4;
    }

    public String getId5() {
        return id5;
    }

    public void setId5(String id5) {
        this.id5 = id5;
    }

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public String getMsgIdno() {
        return msgIdno;
    }

    public void setMsgIdno(String msgIdno) {
        this.msgIdno = msgIdno;
    }

    public Integer getAgeCalDay() {
        return ageCalDay;
    }

    public void setAgeCalDay(Integer ageCalDay) {
        this.ageCalDay = ageCalDay;
    }

    public int getAgeCompleteYear() {
        return ageCompleteYear;
    }

    public void setAgeCompleteYear(int ageCompleteYear) {
        this.ageCompleteYear = ageCompleteYear;
    }

    public int getAgeDayAfterCompleteYear() {
        return ageDayAfterCompleteYear;
    }
    public void setAgeDayAfterCompleteYear(int ageDayAfterCompleteYear) {
        this.ageDayAfterCompleteYear = ageDayAfterCompleteYear;
    }

    // return {completeYear, dayFragment} of nows - dob
    public int[] calculateAgeByCompleteYearAndDayFragment(Date dob){
        Calendar now = Calendar.getInstance();
        Calendar dobCal = Calendar.getInstance();
        dobCal.setTimeInMillis(dob.getTime());
        int completeYear = 0;
        int dayAfterCompleteYear = 0;

        if ( now.get(Calendar.DATE) == dobCal.get(Calendar.DATE)
                &&  now.get(Calendar.MONTH) == dobCal.get(Calendar.MONTH) ){
            completeYear = now.get(Calendar.YEAR) - dobCal.get(Calendar.YEAR);
        }else{
            Calendar dobYearNow = Calendar.getInstance();
            dobYearNow.set( now.get(Calendar.YEAR), dobCal.get(Calendar.MONTH), dobCal.get(Calendar.DATE));
            boolean reachDob = now.get(Calendar.DAY_OF_YEAR)>=dobYearNow.get(Calendar.DAY_OF_YEAR);
            if (reachDob){
                completeYear = now.get(Calendar.YEAR) - dobCal.get(Calendar.YEAR) ;
                dayAfterCompleteYear = now.get(Calendar.DAY_OF_YEAR)-dobYearNow.get(Calendar.DAY_OF_YEAR) ;
            }else{
                completeYear = now.get(Calendar.YEAR) - dobCal.get(Calendar.YEAR) - 1 ; // fragment
                // find fragment
                Calendar endDateOfCompleteYear = Calendar.getInstance();
                dobCal.set(Calendar.YEAR, now.get(Calendar.YEAR)-1 ) ;
                endDateOfCompleteYear.set(dobCal.get(Calendar.YEAR),Calendar.DECEMBER, 31);
                dayAfterCompleteYear =  endDateOfCompleteYear.get(Calendar.DAY_OF_YEAR) - dobCal.get(Calendar.DAY_OF_YEAR);
                dayAfterCompleteYear += now.get(Calendar.DAY_OF_YEAR);
            }

        }
        return new int[]{completeYear,dayAfterCompleteYear };
    }

    public Map<String, String> getAddrCopyForCurrent() {
        return addrCopyForCurrent;
    }

    public void setAddrCopyForCurrent(Map<String, String> addrCopyForCurrent) {
        this.addrCopyForCurrent = addrCopyForCurrent;
    }

    public Map<String, String> getAddrCopyForHome() {
        return addrCopyForHome;
    }

    public void setAddrCopyForHome(Map<String, String> addrCopyForHome) {
        this.addrCopyForHome = addrCopyForHome;
    }

    public Map<String, String> getAddrCopyForShipping() {
        return addrCopyForShipping;
    }

    public void setAddrCopyForShipping(Map<String, String> addrCopyForShipping) {
        this.addrCopyForShipping = addrCopyForShipping;
    }

    public Map<String, String> getAddrCopyForBilling() {
        return addrCopyForBilling;
    }

    public void setAddrCopyForBilling(Map<String, String> addrCopyForBilling) {
        this.addrCopyForBilling = addrCopyForBilling;
    }

    public String getPreviewHomeAdd1() {
        return previewHomeAdd1;
    }

    public void setPreviewHomeAdd1(String previewHomeAdd1) {
        this.previewHomeAdd1 = previewHomeAdd1;
    }

    public String getPreviewHomeAdd2() {
        return previewHomeAdd2;
    }

    public void setPreviewHomeAdd2(String previewHomeAdd2) {
        this.previewHomeAdd2 = previewHomeAdd2;
    }

    public String getPreviewHomeAdd3() {
        return previewHomeAdd3;
    }

    public void setPreviewHomeAdd3(String previewHomeAdd3) {
        this.previewHomeAdd3 = previewHomeAdd3;
    }

    public String getPreviewCurrentAdd1() {
        return previewCurrentAdd1;
    }

    public void setPreviewCurrentAdd1(String previewCurrentAdd1) {
        this.previewCurrentAdd1 = previewCurrentAdd1;
    }

    public String getPreviewCurrentAdd2() {
        return previewCurrentAdd2;
    }

    public void setPreviewCurrentAdd2(String previewCurrentAdd2) {
        this.previewCurrentAdd2 = previewCurrentAdd2;
    }

    public String getPreviewCurrentAdd3() {
        return previewCurrentAdd3;
    }

    public void setPreviewCurrentAdd3(String previewCurrentAdd3) {
        this.previewCurrentAdd3 = previewCurrentAdd3;
    }

    public String getPreviewShippingAdd1() {
        return previewShippingAdd1;
    }

    public void setPreviewShippingAdd1(String previewShippingAdd1) {
        this.previewShippingAdd1 = previewShippingAdd1;
    }

    public String getPreviewShippingAdd2() {
        return previewShippingAdd2;
    }

    public void setPreviewShippingAdd2(String previewShippingAdd2) {
        this.previewShippingAdd2 = previewShippingAdd2;
    }

    public String getPreviewShippingAdd3() {
        return previewShippingAdd3;
    }

    public void setPreviewShippingAdd3(String previewShippingAdd3) {
        this.previewShippingAdd3 = previewShippingAdd3;
    }

    public String getPreviewBillingAdd1() {
        return previewBillingAdd1;
    }

    public void setPreviewBillingAdd1(String previewBillingAdd1) {
        this.previewBillingAdd1 = previewBillingAdd1;
    }

    public String getPreviewBillingAdd2() {
        return previewBillingAdd2;
    }

    public void setPreviewBillingAdd2(String previewBillingAdd2) {
        this.previewBillingAdd2 = previewBillingAdd2;
    }

    public String getPreviewBillingAdd3() {
        return previewBillingAdd3;
    }

    public void setPreviewBillingAdd3(String previewBillingAdd3) {
        this.previewBillingAdd3 = previewBillingAdd3;
    }

    public String getMsgPostalCode() {
        return msgPostalCode;
    }

    public void setMsgPostalCode(String msgPostalCode) {
        this.msgPostalCode = msgPostalCode;
    }
    
    
}
