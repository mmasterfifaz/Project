/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.front.customerHandling;

import com.maxelyz.core.constant.CrmConstant;
import com.maxelyz.core.controller.UserSession;
import com.maxelyz.core.model.dao.AccountDAO;
import com.maxelyz.core.model.dao.CustomerDAO;
import com.maxelyz.core.model.dao.CustomerTypeDAO;
import com.maxelyz.core.model.dao.IdcardTypeDAO;
import com.maxelyz.core.model.dao.ProvinceDAO;
import com.maxelyz.core.model.dao.DistrictDAO;
import com.maxelyz.core.model.dao.SubdistrictDAO;
import com.maxelyz.core.model.dao.front.customerHandling.CustomerHandlingDAO;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.core.model.value.front.customerHandling.CustomerInfoValue;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import javax.faces.bean.ViewScoped;
import org.apache.commons.lang3.StringUtils;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;
//import org.richfaces.event.UploadEvent;
//import org.richfaces.model.UploadItem;

/**
 *
 * @author prawait
 */
@ManagedBean
@ViewScoped
public class CustomerEditController {
    private static final Logger log = Logger.getLogger(CustomerEditController.class);
    private Customer customer;
    private Integer customerId;
    private String mode;
    private Boolean homeSameCurrent;
    private Boolean billingSameCurrent;
    private Boolean shippingSameCurrent;
    private Integer idCardTypeId;
    private Integer customerTypeId;
    private Integer accountId;
    private String accountName;
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
    private List<IdcardType> creditTypeList = new ArrayList<IdcardType>();
    private List<CustomerType> customerTypeList = new ArrayList<CustomerType>();
    private List<Province> provinceList = new ArrayList<Province>();
    private List<Account> accountList = new ArrayList<Account>();
    private List<SelectItem> currentDistricts;
    private List<SelectItem> homeDistricts;
    private List<SelectItem> billingDistricts;
    private List<SelectItem> shippingDistricts;
    private List<SelectItem> currentSubDistrictList;
    private List<SelectItem> homeSubDistrictList;
    private List<SelectItem> billingSubDistrictList;
    private List<SelectItem> shippingSubDistrictList;
    private Map<String, String> currentPostalList;
    private Map<String, String> homePostalList;
    private Map<String, String> billingPostalList;
    private Map<String, String> shippingPostalList;
    private static String SUCCESS = "customerDetail.xhtml?faces-redirect=true";
    private static String FAILURE = "customerEdit.xhtml";
    @ManagedProperty(value = "#{customerDAO}")
    private CustomerDAO customerDAO;
    @ManagedProperty(value = "#{idcardTypeDAO}")
    private IdcardTypeDAO idcardTypeDAO;
    @ManagedProperty(value = "#{customerTypeDAO}")
    private CustomerTypeDAO customerTypeDAO;
    @ManagedProperty(value = "#{provinceDAO}")
    private ProvinceDAO provinceDAO;
    @ManagedProperty(value = "#{accountDAO}")
    private AccountDAO accountDAO;
    @ManagedProperty(value = "#{districtDAO}")
    private DistrictDAO districtDAO;
    @ManagedProperty(value = "#{subdistrictDAO}")
    private SubdistrictDAO subdistrictDAO;
    @ManagedProperty(value = "#{customerHandlingDAO}")
    private CustomerHandlingDAO customerHandlingDAO;
    private String fileName;
    private String fileNameFront;
    private String originalFile;
    private String fullFileName;
    private String ext;

    private Integer recCustomerId;
    private String recCustomerName;
    private String[] stra = new String[2];
    private List<String[]> recCustomers =  new ArrayList<String[]>();
    private List<Customer> referees;
    private String uploadPath = JSFUtil.getuploadPath(); //JSFUtil.getRealPath() + "upload\\";
    private String customerPath = JSFUtil.getuploadPath()+"customer/";//JSFUtil.getRealPath() + "upload\\customer\\";
    private String tmpPath = JSFUtil.getuploadPath()+"temp/"; //JSFUtil.getRealPath() + "upload\\temp\\";

    @PostConstruct
    public void initialize() {
       	if (!SecurityUtil.isPermitted("customerprofile:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        String selectedId = null;
        String phoneNo = "";
        try {
            if(JSFUtil.getRequestParameterMap("phoneNo") != null){
                phoneNo = JSFUtil.getRequestParameterMap("phoneNo");
            }
            //initial id card type
            selectedId = JSFUtil.getRequestParameterMap("selectId");
            if(selectedId != null && !selectedId.isEmpty()){
                customerId = Integer.parseInt(selectedId);
                CustomerInfoValue customerInfoValue = getCustomerHandlingDAO().findCustomerHandling(customerId);
                JSFUtil.getUserSession().setCustomerDetail(customerInfoValue);
            }else{
                CustomerInfoValue c = JSFUtil.getUserSession().getCustomerDetail();
                if(c != null){
                    customerId = c.getId();
                }else{
                    customerId = null;
                }
            }
            if (customerId == null) {
                if (!SecurityUtil.isPermitted("searchcustomer:add")) {
                    SecurityUtil.redirectUnauthorize();
                }
            }
            
            setCreditTypeList(getIdcardTypeDAO().findIdcardTypeEntities());
            setCustomerTypeList(getCustomerTypeDAO().findCustomerTypeEntities());
            setProvinceList(getProvinceDAO().findProvinceEntities());
            setAccountList(getAccountDAO().findAccountEntities());
        } catch (Exception e) {
            System.out.println("######" + e);
        }
        if (customerId == null) {
//            customerId = null;
//            setMode(CrmConstant.ADD_MODE);
            setCustomer(new Customer());
            customer.setEmoicon("1");
            if(!phoneNo.equals("")){ customer.setMobilePhone(phoneNo); }
            customerTypeId = 1;
        } else {
//            setMode(CrmConstant.EDIT_MODE);
//            customerId = Integer.parseInt(selectedID);
            customer = customerDAO.findCustomer(customerId);
            if(customer.getCustomerType() != null)
                customerTypeId = customer.getCustomerType().getId();
            if(customer.getAccountId() != null){
                accountId = customer.getAccountId().getId();
                accountName = customer.getAccountId().getName();
            }else{
                accountId = null;
                accountName = null;
            }
            if(customer.getCurrentDistrictId() != null){
                currentDistrictId = customer.getCurrentDistrictId().getId();
                currentProvinceId = customer.getCurrentDistrictId().getProvinceId().getId();
                currentDistricts = getDistrictList(currentProvinceId);
            }
            if(customer.getCurrentSubDistrict() != null && customer.getCurrentSubDistrict().getId() != null) {
                currentSubDistrictId = customer.getCurrentSubDistrict().getId();
                currentSubDistrictList = getSubDistrictList(currentDistrictId);
                currentPostalList = getPostalList(currentSubDistrictId);
                /*currentDistrictId = customer.getCurrentSubDistrict().getDistrict().getId();
                currentProvinceId = customer.getCurrentSubDistrict().getDistrict().getProvinceId().getId();
                currentDistricts = getDistrictList(currentProvinceId);*/
            }
            if(customer.getHomeDistrictId() != null){
                homeDistrictId = customer.getHomeDistrictId().getId();
                homeProvinceId = districtDAO.findDistrict(homeDistrictId).getProvinceId().getId();
                homeDistricts = getDistrictList(homeProvinceId);
            }
            if(customer.getHomeSubDistrict() != null && customer.getHomeSubDistrict().getId() != null) {
                homeSubDistrictId = customer.getHomeSubDistrict().getId();
                homeSubDistrictList = getSubDistrictList(homeDistrictId);
                homePostalList = getPostalList(homeSubDistrictId);    
            }
            if(customer.getBillingDistrictId() != null){
                billingDistrictId = customer.getBillingDistrictId().getId();
                billingProvinceId = districtDAO.findDistrict(billingDistrictId).getProvinceId().getId();
                billingDistricts = getDistrictList(billingProvinceId);
            }
            if(customer.getBillingSubDistrict() != null && customer.getBillingSubDistrict().getId() != null) {
                billingSubDistrictId = customer.getBillingSubDistrict().getId();
                billingSubDistrictList = getSubDistrictList(billingDistrictId);
                billingPostalList = getPostalList(billingSubDistrictId);
            }
            if(customer.getShippingDistrictId() != null){
                shippingDistrictId = customer.getShippingDistrictId().getId();
                shippingProvinceId = districtDAO.findDistrict(shippingDistrictId).getProvinceId().getId();
                shippingDistricts = getDistrictList(shippingProvinceId);
            }
            if(customer.getShippingSubDistrict() != null && customer.getShippingSubDistrict().getId() != null) {
                shippingSubDistrictId = customer.getShippingSubDistrict().getId();
                shippingSubDistrictList = getSubDistrictList(shippingDistrictId);
                shippingPostalList = getPostalList(shippingSubDistrictId);
            }

            String extName = "";
            if(customer.getPicture() != null && !customer.getPicture().isEmpty()){
                extName =  customer.getPicture().substring(customer.getPicture().indexOf("."), customer.getPicture().length());
            }

            File f = new File(customerPath + customer.getId() + "\\" + customer.getId() + extName);
            File fUnix = new File(customerPath + customer.getId() + "/" + customer.getId() + extName);
            if(f.exists()){
                originalFile = customer.getId() + extName;
                fileNameFront = "customer/" + customer.getId() + "/" + customer.getId() + extName;
            } if(fUnix.exists()) {
                originalFile = customer.getId() + extName;
                fileNameFront = "customer/" + customer.getId() + "/" + customer.getId() + extName;
            }else{
                originalFile = null;
                fileNameFront = null;
            }
//            fullFileName = StringUtils.defaultString(customer.getPicture());
            try{
                List<Customer> cList = (List<Customer>) customer.getCustomerCollection();
                recCustomers = new ArrayList<String[]>();
                for(Customer c : cList){
                    stra = new String[2];
                    stra[0] = c.getId().toString();
                    stra[1] = c.getName() + " " + c.getSurname();
                    recCustomers.add(stra);
                }
            }catch(Exception e){
                e.printStackTrace();
            }

            referees = (List<Customer>) customer.getCustomerCollection1();

//            CustomerInfoValue customerInfoValue = getCustomerHandlingDAO().findCustomerHandling(customerId);
//            JSFUtil.getUserSession().setCustomerDetail(customerInfoValue);
//            FacesContext context = FacesContext.getCurrentInstance();
//            HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
//            session.setAttribute("customerDetail", customerInfoValue);
        }
    }

    public String editCustomer(){
//        initialize();
        if(customerId != null){
//            CustomerInfoValue c = JSFUtil.getUserSession().getCustomerDetail();
//            if(c == null){
//                CustomerInfoValue customerInfoValue = getCustomerHandlingDAO().findCustomerHandling(customerId);
//                JSFUtil.getUserSession().setCustomerDetail(customerInfoValue);
//            }
            return "customerEdit.xhtml";
        }else{
            return "customerAdd.jsf";
        }
    }

    public boolean isSavePermitted() {
        return SecurityUtil.isPermitted("searchcustomer:add") ; 
    }
        
    public String back(){
        if(customerId != null){
//            UserSession userSession = JSFUtil.getUserSession();
//            CustomerInfoValue c = userSession.getCustomerDetail();
//            if(c == null){
//                CustomerInfoValue customerInfoValue = getCustomerHandlingDAO().findCustomerHandling(customerId);
//                userSession.setCustomerDetail(customerInfoValue);
//            }
            return "/front/customerHandling/customerDetail.xhtml";
        }else{
            return "/front/search/searchCustomer.jsf";
        }
    }

    /**
     * pack and save customer detail.
     */
    public String saveAction() {
        CustomerDAO dao = getCustomerDAO();
        
        //ADD CUSTOMER
        if (customerId == null || customerId == 0) {
            try {
                //fix refernceNo
                if(getCustomerTypeId() != null) {
                    CustomerType customerType = getCustomerTypeDAO().findCustomerType(getCustomerTypeId());
                    if (customerType != null) {
                        getCustomer().setCustomerType(customerType);
                    }
                }
                
                if(getAccountId() != null) {
                    Account account = getAccountDAO().findAccount(getAccountId());
                    if (account != null) {
                        getCustomer().setAccountId(account);
                    }
                }                

                if(getIdCardTypeId() != null) {
                    IdcardType idCardType = getIdcardTypeDAO().findIdcardType(getIdCardTypeId());
                    if (idCardType != null) {
                        getCustomer().setIdcardTypeId(idCardType);
                    }
                }

                if (currentDistrictId != null && !currentDistrictId.equals(0)) {
                    getCustomer().setCurrentDistrictId(getDistrictDAO().findDistrict(currentDistrictId));
                }
                if (currentSubDistrictId != null && !currentSubDistrictId.equals(0)) {
                    getCustomer().setCurrentSubDistrict(getSubdistrictDAO().findSubDistrict(currentSubDistrictId)); 
                }
                
                if (!getHomeSameCurrent()) {
                    if (homeDistrictId != null && !homeDistrictId.equals(0)) {
                        District homeDistrict = getDistrictDAO().findDistrict(homeDistrictId);
                        if (homeDistrict != null) {
                            getCustomer().setHomeDistrictId(homeDistrict);
                        }
                        if (homeSubDistrictId != null && !homeSubDistrictId.equals(0)) {
                            getCustomer().setHomeSubDistrict(getSubdistrictDAO().findSubDistrict(homeSubDistrictId)); 
                        }
                    }
                } else {
                    getCustomer().setHomeAddressLine1(getCustomer().getCurrentAddressLine1());
                    getCustomer().setHomeAddressLine2(getCustomer().getCurrentAddressLine2());
                    getCustomer().setHomeSubDistrict(getCustomer().getCurrentSubDistrict());
                    getCustomer().setHomeDistrictId(getCustomer().getCurrentDistrictId());
                    getCustomer().setHomeFullname(getCustomer().getCurrentFullname());
                    getCustomer().setHomePostalCode(getCustomer().getCurrentPostalCode());
                }

                if (!getBillingSameCurrent()) {
                    if (billingDistrictId != null && !billingDistrictId.equals(0)) {
                        District billingDistrict = getDistrictDAO().findDistrict(billingDistrictId);
                        if (billingDistrict != null) {
                            getCustomer().setBillingDistrictId(billingDistrict);
                        }
                        if (billingSubDistrictId != null && !billingSubDistrictId.equals(0)) {
                            getCustomer().setBillingSubDistrict(getSubdistrictDAO().findSubDistrict(billingSubDistrictId)); 
                        }
                    }
                } else {
                    getCustomer().setBillingAddressLine1(getCustomer().getCurrentAddressLine1());
                    getCustomer().setBillingAddressLine2(getCustomer().getCurrentAddressLine2());
                    getCustomer().setBillingSubDistrict(getCustomer().getCurrentSubDistrict());
                    getCustomer().setBillingDistrictId(getCustomer().getCurrentDistrictId());
                    getCustomer().setBillingFullname(getCustomer().getCurrentFullname());
                    getCustomer().setBillingPostalCode(getCustomer().getCurrentPostalCode());
                }

                if (!getShippingSameCurrent()) {
                    if (shippingDistrictId != null && !shippingDistrictId.equals(0)) {
                        District shippingDistrict = getDistrictDAO().findDistrict(shippingDistrictId);
                        if (shippingDistrict != null) {
                            getCustomer().setShippingDistrictId(shippingDistrict);
                        }
                        if (shippingSubDistrictId != null && !shippingSubDistrictId.equals(0)) {
                            getCustomer().setShippingSubDistrict(getSubdistrictDAO().findSubDistrict(shippingSubDistrictId)); 
                        }
                    }
                } else {
                    getCustomer().setShippingAddressLine1(getCustomer().getCurrentAddressLine1());
                    getCustomer().setShippingAddressLine2(getCustomer().getCurrentAddressLine2());
                    getCustomer().setShippingSubDistrict(getCustomer().getCurrentSubDistrict());
                    getCustomer().setShippingDistrictId(getCustomer().getCurrentDistrictId());
                    getCustomer().setShippingFullname(getCustomer().getCurrentFullname());
                    getCustomer().setShippingPostalCode(getCustomer().getCurrentPostalCode());
                }

                getCustomer().setCreateBy(JSFUtil.getUserSession().getUsers().getName());
                getCustomer().setCreateDate(new Date());
                getCustomer().setCustomerCollection(getRecCusts(recCustomers));
                getCustomer().setCreatedByCs(Boolean.TRUE);
                dao.create(getCustomer());

//                getCustomer().setPicture(getPictureName());
                if(fileName != null){
                    File f = new File(tmpPath + fileName);
                    if(f.exists()){
                        setExt(fileName);
                        File fDir = new File(customerPath + customer.getId());
                        if(!fDir.exists()){
                            fDir.mkdir();
                        }
                        File fNew = new File(customerPath + customer.getId() + "\\" + customer.getId() + "." + ext);
                        f.renameTo(fNew);

                        getCustomer().setPicture(customer.getId() + "." + ext);
                    }
                }

                dao.edit(getCustomer());

                if(JSFUtil.getUserSession().getCustomerDetail() == null){
                    CustomerInfoValue customerInfoValue = getCustomerHandlingDAO().findCustomerHandling(customer.getId());
                    JSFUtil.getUserSession().setCustomerDetail(customerInfoValue);
                }

            } catch (Exception e) {
                log.error(e);
                System.out.println("########################" + e);
                return FAILURE;
            }
        } else {
            //EDIT CUSTOMER
            
            try {
                if(getCustomerTypeId() != null) {
                    CustomerType customerType = getCustomerTypeDAO().findCustomerType(getCustomerTypeId());
                    if (customerType != null) {
                        getCustomer().setCustomerType(customerType);
                    }
                }
                
                if(getAccountId() != null) {
                    Account account = getAccountDAO().findAccount(getAccountId());
                    if (account != null) {
                        getCustomer().setAccountId(account);
                    }else{
                        getCustomer().setAccountId(null);
                    }
                } 

                if(getIdCardTypeId() != null) {
                    IdcardType idCardType = getIdcardTypeDAO().findIdcardType(getIdCardTypeId());
                    if (idCardType != null) {
                        getCustomer().setIdcardTypeId(idCardType);
                    }
                }

                if (currentProvinceId == null || currentProvinceId.equals(0)) {
                     currentDistrictId = null;
                     currentSubDistrictId = null; 
                     getCustomer().setCurrentPostalCode(null);
                } 
                if (homeProvinceId == null || homeProvinceId.equals(0)) {
                    homeDistrictId = null;
                    homeSubDistrictId = null;
                    getCustomer().setHomePostalCode(null);
                }
                if (billingProvinceId == null || billingProvinceId.equals(0)) {
                    billingDistrictId = null;
                    billingSubDistrictId = null;
                    getCustomer().setBillingPostalCode(null);
                }
                if (shippingProvinceId == null || shippingProvinceId.equals(0)) {
                    shippingDistrictId = null;
                    shippingSubDistrictId = null;
                    getCustomer().setShippingPostalCode(null);
                }
                
                if (currentDistrictId != null && !currentDistrictId.equals(0)) {
                    getCustomer().setCurrentDistrictId(getDistrictDAO().findDistrict(currentDistrictId));
                } else {
                    getCustomer().setCurrentDistrictId(null);
                    currentSubDistrictId = null;
                }
                
                if (currentSubDistrictId != null && !currentSubDistrictId.equals(0)) {
                    getCustomer().setCurrentSubDistrict(getSubdistrictDAO().findSubDistrict(currentSubDistrictId)); 
                } else {
                    getCustomer().setCurrentSubDistrict(null); 
                    getCustomer().setCurrentPostalCode(null);
                }
                
                if (!getHomeSameCurrent()) {
                    if (homeDistrictId != null && !homeDistrictId.equals(0)) {
                        District homeDistrict = getDistrictDAO().findDistrict(homeDistrictId);
                        if (homeDistrict != null) {
                            getCustomer().setHomeDistrictId(homeDistrict);
                        }                         
                    } else {
                        getCustomer().setHomeDistrictId(null);
                        homeSubDistrictId = null;
                        getCustomer().setHomeSubDistrict(null); 
                        getCustomer().setHomePostalCode(null);
                    }
                    
                    if (homeSubDistrictId != null && !homeSubDistrictId.equals(0)) {
                        getCustomer().setHomeSubDistrict(getSubdistrictDAO().findSubDistrict(homeSubDistrictId)); 
                    } 
                    
                } else {
                    getCustomer().setHomeAddressLine1(getCustomer().getCurrentAddressLine1());
                    getCustomer().setHomeAddressLine2(getCustomer().getCurrentAddressLine2());
                    getCustomer().setHomeSubDistrict(getCustomer().getCurrentSubDistrict());
                    getCustomer().setHomeDistrictId(getCustomer().getCurrentDistrictId());
                    getCustomer().setHomeFullname(getCustomer().getCurrentFullname());
                    getCustomer().setHomePostalCode(getCustomer().getCurrentPostalCode());
                }

                if (!getBillingSameCurrent()) {
                    if (billingDistrictId != null && !billingDistrictId.equals(0)) {
                        District billingDistrict = getDistrictDAO().findDistrict(billingDistrictId);
                        if (billingDistrict != null) {
                            getCustomer().setBillingDistrictId(billingDistrict);
                        } 
                    } else {
                        getCustomer().setBillingDistrictId(null);
                        billingSubDistrictId = null;
                        getCustomer().setBillingSubDistrict(null); 
                        getCustomer().setBillingPostalCode(null);
                    }
                    
                    if (billingSubDistrictId != null && !billingSubDistrictId.equals(0)) {
                        getCustomer().setBillingSubDistrict(getSubdistrictDAO().findSubDistrict(billingSubDistrictId)); 
                    }
                    
                } else {
                    getCustomer().setBillingAddressLine1(getCustomer().getCurrentAddressLine1());
                    getCustomer().setBillingAddressLine2(getCustomer().getCurrentAddressLine2());
                    getCustomer().setBillingSubDistrict(getCustomer().getCurrentSubDistrict());
                    getCustomer().setBillingDistrictId(getCustomer().getCurrentDistrictId());
                    getCustomer().setBillingFullname(getCustomer().getCurrentFullname());
                    getCustomer().setBillingPostalCode(getCustomer().getCurrentPostalCode());
                }

                if (!getShippingSameCurrent()) {
                    if (shippingDistrictId != null && !shippingDistrictId.equals(0)) {
                        District shippingDistrict = getDistrictDAO().findDistrict(shippingDistrictId);
                        if (shippingDistrict != null) {
                            getCustomer().setShippingDistrictId(shippingDistrict);
                        }                         
                    } else {
                        getCustomer().setShippingDistrictId(null);
                        shippingSubDistrictId = null;
                        getCustomer().setShippingSubDistrict(null); 
                        getCustomer().setShippingPostalCode(null);
                    }
                    
                    if (shippingSubDistrictId != null && !shippingSubDistrictId.equals(0)) {
                        getCustomer().setShippingSubDistrict(getSubdistrictDAO().findSubDistrict(shippingSubDistrictId)); 
                    } 
                    
                } else {
                    getCustomer().setShippingAddressLine1(getCustomer().getCurrentAddressLine1());
                    getCustomer().setShippingAddressLine2(getCustomer().getCurrentAddressLine2());
                    getCustomer().setShippingSubDistrict(getCustomer().getCurrentSubDistrict());
                    getCustomer().setShippingDistrictId(getCustomer().getCurrentDistrictId());
                    getCustomer().setShippingFullname(getCustomer().getCurrentFullname());
                    getCustomer().setShippingPostalCode(getCustomer().getCurrentPostalCode());
                }

                getCustomer().setCustomerCollection(getRecCusts(recCustomers));
                if(fileName != null){
                    if(!fileName.equals(originalFile)){
                        File fDel = new File(customerPath + customer.getId() + "\\" + originalFile);
                        fDel.delete();
                        File f = new File(tmpPath + fileName);
                        if(f.exists()){
                            setExt(fileName);
                            File fDir = new File(customerPath + customer.getId());
                            if(!fDir.exists()){
                                fDir.mkdir();
                            }
                            File fNew = new File(customerPath + customer.getId() + "\\" + customer.getId() + "." + ext);
                            f.renameTo(fNew);
                            getCustomer().setPicture(customer.getId() + "." + ext);
                        }
                    }
                }

                dao.edit(getCustomer());

                CustomerInfoValue customerInfoValue = getCustomerHandlingDAO().findCustomerHandling(customerId);
                JSFUtil.getUserSession().setCustomerDetail(customerInfoValue);
                
            } catch (Exception e) {
                log.error(e);
                System.out.println("########################" + e);
                return FAILURE;
            }
        }
        return SUCCESS;
    }

    public void selectAccountListener(ActionEvent event) {
        accountName = JSFUtil.getRequestParameterMap("accountName");
        String tmpId = JSFUtil.getRequestParameterMap("accountId");
        if(tmpId.isEmpty()){
            accountId = null;
        }else{
            accountId = Integer.parseInt(tmpId);
        }
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void clearAccountListener(ActionEvent event) {
        setAccountId(null);
        setAccountName(null);
    }

    public void selectRecCustomerListener(ActionEvent event) {
        stra = new String[2];
        stra[0] = JSFUtil.getRequestParameterMap("recCustomerId");
        stra[1] = JSFUtil.getRequestParameterMap("recCustomerName");
//        List<String[]> list = new ArrayList<String[]>();
        boolean exist = false;
        if(customerId != null){
            if(customerId == Integer.parseInt(stra[0])){
                exist = true;
            }
        }
        for (String[] s : recCustomers) {
            if(StringUtils.equals(StringUtils.trim(stra[0]), StringUtils.trim(s[0]))){
                exist = true;
                break;
            }
        }
        if(!exist){
            recCustomers.add(stra);
        }
//        recCustomers = list;
//        FacesContext.getCurrentInstance().renderResponse();
    }

    public void removeRecCustomers(ActionEvent event) {
        String str = StringUtils.trim(JSFUtil.getRequestParameterMap("removeCustomerId"));
        List<String[]> list = new ArrayList<String[]>();
        for (String[] s : recCustomers) {
            if(!StringUtils.equals(str, StringUtils.trim(s[0]))){
                list.add(s);
            }
        }
        recCustomers = list;
//        FacesContext.getCurrentInstance().renderResponse();
    }

    private ArrayList<Customer> getRecCusts(List<String[]> arr){
        ArrayList<Customer> custArr = new ArrayList<Customer>();
        Customer c = null;
        for (String[] stra : arr) {
            c = customerDAO.findCustomer(Integer.parseInt(stra[0]));
            custArr.add(c);
        }
        return custArr;
    }

    public List getRecCustomers() {
        return recCustomers;
    }

    public void setRecCustomers(List recCustomers) {
        this.recCustomers = recCustomers;
    }

    public String[] getStra() {
        return stra;
    }

    public void setStra(String[] stra) {
        this.stra = stra;
    }

    public Integer getRecCustomerId() {
        return recCustomerId;
    }

    public void setRecCustomerId(Integer recCustomerId) {
        this.recCustomerId = recCustomerId;
    }

    public String getRecCustomerName() {
        return recCustomerName;
    }

    public void setRecCustomerName(String recCustomerName) {
        this.recCustomerName = recCustomerName;
    }

    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * @return the mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * @return the homeSameCurrent
     */
    public Boolean getHomeSameCurrent() {
        return homeSameCurrent;
    }

    /**
     * @param homeSameCurrent the homeSameCurrent to set
     */
    public void setHomeSameCurrent(Boolean homeSameCurrent) {
        this.homeSameCurrent = homeSameCurrent;
    }

    /**
     * @return the billingSameCurrent
     */
    public Boolean getBillingSameCurrent() {
        return billingSameCurrent;
    }

    /**
     * @param billingSameCurrent the billingSameCurrent to set
     */
    public void setBillingSameCurrent(Boolean billingSameCurrent) {
        this.billingSameCurrent = billingSameCurrent;
    }

    /**
     * @return the shippingSameCurrent
     */
    public Boolean getShippingSameCurrent() {
        return shippingSameCurrent;
    }

    /**
     * @param shippingSameCurrent the shippingSameCurrent to set
     */
    public void setShippingSameCurrent(Boolean shippingSameCurrent) {
        this.shippingSameCurrent = shippingSameCurrent;
    }

    /**
     * @return the idCardTypeId
     */
    public Integer getIdCardTypeId() {
        return idCardTypeId;
    }

    /**
     * @param idCardTypeId the idCardTypeId to set
     */
    public void setIdCardTypeId(Integer idCardTypeId) {
        this.idCardTypeId = idCardTypeId;
    }

    /**
     * @return the customerTypeId
     */
    public Integer getCustomerTypeId() {
        return customerTypeId;
    }

    /**
     * @param customerTypeId the customerTypeId to set
     */
    public void setCustomerTypeId(Integer customerTypeId) {
        this.customerTypeId = customerTypeId;
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
     * @return the currentProvinceId
     */
    public Integer getCurrentProvinceId() {
        return currentProvinceId;
    }

    /**
     * @param currentProvinceId the currentProvinceId to set
     */
    public void setCurrentProvinceId(Integer currentProvinceId) {
        this.currentProvinceId = currentProvinceId;
    }

    /**
     * @return the homeProvinceId
     */
    public Integer getHomeProvinceId() {
        return homeProvinceId;
    }

    /**
     * @param homeProvinceId the homeProvinceId to set
     */
    public void setHomeProvinceId(Integer homeProvinceId) {
        this.homeProvinceId = homeProvinceId;
    }

    /**
     * @return the billingProvinceId
     */
    public Integer getBillingProvinceId() {
        return billingProvinceId;
    }

    /**
     * @param billingProvinceId the billingProvinceId to set
     */
    public void setBillingProvinceId(Integer billingProvinceId) {
        this.billingProvinceId = billingProvinceId;
    }

    /**
     * @return the shippingProvinceId
     */
    public Integer getShippingProvinceId() {
        return shippingProvinceId;
    }

    /**
     * @param shippingProvinceId the shippingProvinceId to set
     */
    public void setShippingProvinceId(Integer shippingProvinceId) {
        this.shippingProvinceId = shippingProvinceId;
    }

    /**
     * @return the currentDistrictId
     */
    public Integer getCurrentDistrictId() {
        return currentDistrictId;
    }

    /**
     * @param currentDistrictId the currentDistrictId to set
     */
    public void setCurrentDistrictId(Integer currentDistrictId) {
        this.currentDistrictId = currentDistrictId;
    }

    /**
     * @return the homeDistrictId
     */
    public Integer getHomeDistrictId() {
        return homeDistrictId;
    }

    /**
     * @param homeDistrictId the homeDistrictId to set
     */
    public void setHomeDistrictId(Integer homeDistrictId) {
        this.homeDistrictId = homeDistrictId;
    }

    /**
     * @return the billingDistrictId
     */
    public Integer getBillingDistrictId() {
        return billingDistrictId;
    }

    /**
     * @param billingDistrictId the billingDistrictId to set
     */
    public void setBillingDistrictId(Integer billingDistrictId) {
        this.billingDistrictId = billingDistrictId;
    }

    /**
     * @return the shippingDistrictId
     */
    public Integer getShippingDistrictId() {
        return shippingDistrictId;
    }

    /**
     * @param shippingDistrictId the shippingDistrictId to set
     */
    public void setShippingDistrictId(Integer shippingDistrictId) {
        this.shippingDistrictId = shippingDistrictId;
    }

    /**
     * @return the creditTypeList
     */
    public List<IdcardType> getCreditTypeList() {
        return creditTypeList;
    }

    public List<SelectItem> getCreditTypes() {
        List<SelectItem> idCardTypeValueList = new ArrayList<SelectItem>();
        idCardTypeValueList.add(new SelectItem(null, CrmConstant.PLEASE_SELECT));
        for (IdcardType idCardType : creditTypeList) {
            if (idCardType.getId() != null) {
                idCardTypeValueList.add(new SelectItem(idCardType.getId(), idCardType.getName()));
            }
        }
        return idCardTypeValueList;
    }

    /**
     * @param creditTypeList the creditTypeList to set
     */
    public void setCreditTypeList(List<IdcardType> creditTypeList) {
        this.creditTypeList = creditTypeList;
    }

    /**
     * @return the customerTypeList
     */
    public List<CustomerType> getCustomerTypeList() {
        return customerTypeList;
    }

    public List<SelectItem> getCustomerTypes() {
        List<SelectItem> customerTypeValueList = new ArrayList<SelectItem>();
        customerTypeValueList.add(new SelectItem(null, CrmConstant.PLEASE_SELECT));
        for (CustomerType customerType : customerTypeList) {
            if (customerType.getId() != null) {
                customerTypeValueList.add(new SelectItem(customerType.getId(), customerType.getName()));
            }
        }
        return customerTypeValueList;
    }

    /**
     * @param customerTypeList the customerTypeList to set
     */
    public void setCustomerTypeList(List<CustomerType> customerTypeList) {
        this.customerTypeList = customerTypeList;
    }

    /**
     * @return the provinceList
     */
    public List<Province> getProvinceList() {
        return provinceList;
    }

    public List<SelectItem> getProvinces() {
        List<SelectItem> provinceValueList = new ArrayList<SelectItem>();
//        provinceValueList.add(new SelectItem(null, CrmConstant.PLEASE_SELECT));
        for (Province province : provinceList) {
            if (province.getId() != null) {
                provinceValueList.add(new SelectItem(province.getId(), province.getName()));
            }
        }
        return provinceValueList;

    }

    /**
     * @param provinceList the provinceList to set
     */
    public void setProvinceList(List<Province> provinceList) {
        this.provinceList = provinceList;
    }

    /**
     * @return the accountList
     */
    public List<Account> getAccountList() {
        return accountList;
    }

    public List<SelectItem> getAccounts() {
        List<SelectItem> accountValueList = new ArrayList<SelectItem>();
        accountValueList.add(new SelectItem(null, CrmConstant.PLEASE_SELECT));
        for (Account account : accountList) {
            if (account.getId() != null) {
                accountValueList.add(new SelectItem(account.getId(), account.getName()));
            }
        }
        return accountValueList;
    }

    /**
     * @param accountList the accountList to set
     */
    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }

    /**
     * @return the customerDAO
     */
    public CustomerDAO getCustomerDAO() {
        return customerDAO;
    }

    /**
     * @param customerDAO the customerDAO to set
     */
    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    /**
     * @return the idcardTypeDAO
     */
    public IdcardTypeDAO getIdcardTypeDAO() {
        return idcardTypeDAO;
    }

    /**
     * @param idcardTypeDAO the idcardTypeDAO to set
     */
    public void setIdcardTypeDAO(IdcardTypeDAO idcardTypeDAO) {
        this.idcardTypeDAO = idcardTypeDAO;
    }

    /**
     * @return the customerTypeDAO
     */
    public CustomerTypeDAO getCustomerTypeDAO() {
        return customerTypeDAO;
    }

    /**
     * @param customerTypeDAO the customerTypeDAO to set
     */
    public void setCustomerTypeDAO(CustomerTypeDAO customerTypeDAO) {
        this.customerTypeDAO = customerTypeDAO;
    }

    /**
     * @return the provinceDAO
     */
    public ProvinceDAO getProvinceDAO() {
        return provinceDAO;
    }

    /**
     * @param provinceDAO the provinceDAO to set
     */
    public void setProvinceDAO(ProvinceDAO provinceDAO) {
        this.provinceDAO = provinceDAO;
    }

    /**
     * @return the accountDAO
     */
    public AccountDAO getAccountDAO() {
        return accountDAO;
    }

    /**
     * @param accountDAO the accountDAO to set
     */
    public void setAccountDAO(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    /**
     * @return the districtDAO
     */
    public DistrictDAO getDistrictDAO() {
        return districtDAO;
    }

    /**
     * @param districtDAO the districtDAO to set
     */
    public void setDistrictDAO(DistrictDAO districtDAO) {
        this.districtDAO = districtDAO;
    }

    private List<SelectItem> getDistrictList(Integer provinceId) {
        List<District> districtList = getDistrictDAO().findDistrictByProvinceId(provinceId);
        List<SelectItem> values = new ArrayList<SelectItem>();
        for (District obj : districtList) {
            values.add(new SelectItem(obj.getId(), obj.getName()));
        }

        return values;
    }

    /**
     *  Listener for CurrentProvince changed
     */
    public void currentProvinceListener(ValueChangeEvent event) {
        Integer provinceId = (Integer) event.getNewValue();
        setCurrentDistricts(provinceId);
        currentDistrictId = 0;
        if(currentSubDistrictList != null) {
            currentSubDistrictList.clear();
            currentSubDistrictId = 0;
        }
        if(currentPostalList != null)
            currentPostalList.clear();
        //FacesContext.getCurrentInstance().renderResponse();
    }

    /**
     * @return the currentDistricts
     */
    public List<SelectItem> getCurrentDistricts() {
        return currentDistricts;
    }

    /**
     * @param provinceId the provinceId to find districts
     */
    public void setCurrentDistricts(Integer provinceId) {
        if (this.currentDistricts != null) {
            this.currentDistricts.clear();
        }
        currentSubDistrictId = 0;
        this.currentDistricts = getDistrictList(provinceId);
    }

    /**
     *  Listener for HomeProvince changed
     */
    public void homeProvinceListener(ValueChangeEvent event) {
        Integer provinceId = (Integer) event.getNewValue();
        setHomeDistricts(provinceId);
        homeDistrictId = 0;
        if(homeSubDistrictList != null) {
            homeSubDistrictList.clear();
            homeSubDistrictId = 0;
        }
        if(homePostalList != null)
            homePostalList.clear();
        //FacesContext.getCurrentInstance().renderResponse();
    }

    /**
     * @return the homeDistricts
     */
    public List<SelectItem> getHomeDistricts() {
        return homeDistricts;
    }

    /**
     * @param provinceId the provinceId to find districts
     */
    public void setHomeDistricts(Integer provinceId) {
        if (this.homeDistricts != null) {
            this.homeDistricts.clear();
        }
        homeSubDistrictId = 0;
        this.homeDistricts = getDistrictList(provinceId);
    }

    /**
     *  Listener for BillingProvince changed
     */
    public void billingProvinceListener(ValueChangeEvent event) {
        Integer provinceId = (Integer) event.getNewValue();
        setBillingDistricts(provinceId);
        billingDistrictId = 0;
        if(billingSubDistrictList != null) {
            billingSubDistrictList.clear();
            billingSubDistrictId = 0;
        }
        if(billingPostalList != null)
            billingPostalList.clear();
        //FacesContext.getCurrentInstance().renderResponse();
    }

    /**
     * @return the billingDistricts
     */
    public List<SelectItem> getBillingDistricts() {
        return billingDistricts;
    }

    /**
     * @param provinceId the provinceId to find districts
     */
    public void setBillingDistricts(Integer provinceId) {
        if (this.billingDistricts != null) {
            this.billingDistricts.clear();
        }
        billingSubDistrictId = 0;
        this.billingDistricts = getDistrictList(provinceId);
    }

    /**
     *  Listener for CurrentProvince changed
     */
    public void shippingProvinceListener(ValueChangeEvent event) {
        Integer provinceId = (Integer) event.getNewValue();
        setShippingDistricts(provinceId);
        shippingDistrictId = 0;
        if(shippingSubDistrictList != null) {
            shippingSubDistrictList.clear();
            shippingSubDistrictId = 0;
        }
        if(shippingPostalList != null)
            shippingPostalList.clear();
        //FacesContext.getCurrentInstance().renderResponse();
    }

    /**
     * @return the shippingDistricts
     */
    public List<SelectItem> getShippingDistricts() {
        return shippingDistricts;
    }

    /**
     * @param provinceId the provinceId to find districts
     */
    public void setShippingDistricts(Integer provinceId) {
        if (this.shippingDistricts != null) {
            this.shippingDistricts.clear();
        }
        shippingSubDistrictId = 0;
        this.shippingDistricts = getDistrictList(provinceId);
    }

    /**
     * @return the customerHandlingDAO
     */
    public CustomerHandlingDAO getCustomerHandlingDAO() {
        return customerHandlingDAO;
    }

    /**
     * @param customerHandlingDAO the customerHandlingDAO to set
     */
    public void setCustomerHandlingDAO(CustomerHandlingDAO customerHandlingDAO) {
        this.customerHandlingDAO = customerHandlingDAO;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExt() {
        return ext;
    }

    public String getFullFileName() {
        return fullFileName;
    }

    public void setFullFileName(String fullFileName) {
        this.fullFileName = fullFileName;
    }

    public void setExt(String strFileName) {
        String str = "";
        int extDot = strFileName.lastIndexOf('.');
        if(extDot > 0){
            str = strFileName.substring(extDot +1);
        }
        this.ext = str;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public List<Customer> getReferees() {
        return referees;
    }

    public void setReferees(List<Customer> referees) {
        this.referees = referees;
    }

    public String getFileNameFront() {
        return fileNameFront;
    }

    public void setFileNameFront(String fileNameFront) {
        this.fileNameFront = fileNameFront;
    }

    public String getOriginalFile() {
        return originalFile;
    }

    public void setOriginalFile(String originalFile) {
        this.originalFile = originalFile;
    }

    public void uploadListener(FileUploadEvent event) throws Exception {
        Date date = new Date();
        String[] stra = new String[3];
        UploadedFile item = event.getUploadedFile();
        String str = item.getName();
        str = getRealFileName(str);
        stra[0] = str;//file name
        setExt(str);
        setFileName("tmp_" + date.getTime() + "." + ext);
        str = tmpPath + fileName;
        File file = new File(str);
        if(saveToFile(item.getInputStream(),file)) {  
            setFileNameFront("temp/" + fileName);
        }
    }
   
     private boolean saveToFile(InputStream inputStream, File file) {    
        FileOutputStream fos = null;
        BufferedInputStream bis = null;   
        boolean result = false;        
        try { 
            byte[] buffer = new byte[1024];
            fos = new FileOutputStream(file);            
            bis = new BufferedInputStream(inputStream, buffer.length);            
            int numRead = -1;            
            while ((numRead = bis.read(buffer, 0, buffer.length)) != -1) {                
                fos.write(buffer, 0, numRead);
            }          
            result = true;        
        } catch (IOException ex) {            
            log.error(ex, ex);
            log.error("Exception during download file " + file.getAbsolutePath());        
        } finally {            
            try {                
                fos.close();
            } catch (IOException ex) {                
                log.error(ex, ex);
                log.error("Exception during closing file output stream " + file.getAbsolutePath());            
            }            
            try {                
                bis.close();
            } catch (IOException ex) {                
                log.error(ex, ex);
                log.error("Exception during closing buffered input stream " + file.getAbsolutePath());            
            }   
        }    
        return result; 
    }
    
    public String getRealFileName(String str) {
        String str1 = str;
        if(str.lastIndexOf("/") != -1){
            str1 = StringUtils.substring(str, (str.lastIndexOf("/")) + 1, str.length());
        }else if(str.lastIndexOf("\\") != -1){
            str1 = StringUtils.substring(str, (str.lastIndexOf("\\")) + 1, str.length());
        }
        return str1;
    }

    public String getPictureName(){
        if (StringUtils.isNotEmpty(fileName)) {
            if (!fileName.equals(fullFileName)) {
                File file1 = new File("D:\\Development\\Maxar\\build\\web\\upload\\customer\\" + fileName);
                if (file1.exists()) {
                    fullFileName = fileName.substring(fileName.indexOf("_") + 1, fileName.length());
                    fileName = fullFileName;
                    File file2 = new File("D:\\Development\\Maxar\\build\\web\\upload\\customer\\" + fullFileName);
                    file1.renameTo(file2);
                }
            }
        }
        return fullFileName;
    }

    public Map<String, String> getBillingPostalList() {
        return billingPostalList;
    }

    public void setBillingPostalList(Map<String, String> billingPostalList) {
        this.billingPostalList = billingPostalList;
    }

    public Integer getBillingSubDistrictId() {
        return billingSubDistrictId;
    }

    public void setBillingSubDistrictId(Integer billingSubDistrictId) {
        this.billingSubDistrictId = billingSubDistrictId;
    }

    public List<SelectItem> getBillingSubDistrictList() {
        return billingSubDistrictList;
    }

    public void setBillingSubDistrictList(List<SelectItem> billingSubDistrictList) {
        this.billingSubDistrictList = billingSubDistrictList;
    }

    public Map<String, String> getCurrentPostalList() {
        return currentPostalList;
    }

    public void setCurrentPostalList(Map<String, String> currentPostalList) {
        this.currentPostalList = currentPostalList;
    }

    public Integer getCurrentSubDistrictId() {
        return currentSubDistrictId;
    }

    public void setCurrentSubDistrictId(Integer currentSubDistrictId) {
        this.currentSubDistrictId = currentSubDistrictId;
    }

    public List<SelectItem> getCurrentSubDistrictList() {
        return currentSubDistrictList;
    }

    public void setCurrentSubDistrictList(List<SelectItem> currentSubDistrictList) {
        this.currentSubDistrictList = currentSubDistrictList;
    }

    public Map<String, String> getHomePostalList() {
        return homePostalList;
    }

    public void setHomePostalList(Map<String, String> homePostalList) {
        this.homePostalList = homePostalList;
    }

    public Integer getHomeSubDistrictId() {
        return homeSubDistrictId;
    }

    public void setHomeSubDistrictId(Integer homeSubDistrictId) {
        this.homeSubDistrictId = homeSubDistrictId;
    }

    public List<SelectItem> getHomeSubDistrictList() {
        return homeSubDistrictList;
    }

    public void setHomeSubDistrictList(List<SelectItem> homeSubDistrictList) {
        this.homeSubDistrictList = homeSubDistrictList;
    }

    public Map<String, String> getShippingPostalList() {
        return shippingPostalList;
    }

    public void setShippingPostalList(Map<String, String> shippingPostalList) {
        this.shippingPostalList = shippingPostalList;
    }

    public Integer getShippingSubDistrictId() {
        return shippingSubDistrictId;
    }

    public void setShippingSubDistrictId(Integer shippingSubDistrictId) {
        this.shippingSubDistrictId = shippingSubDistrictId;
    }

    public List<SelectItem> getShippingSubDistrictList() {
        return shippingSubDistrictList;
    }

    public void setShippingSubDistrictList(List<SelectItem> shippingSubDistrictList) {
        this.shippingSubDistrictList = shippingSubDistrictList;
    }

    public SubdistrictDAO getSubdistrictDAO() {
        return subdistrictDAO;
    }

    public void setSubdistrictDAO(SubdistrictDAO subdistrictDAO) {
        this.subdistrictDAO = subdistrictDAO;
    }
    
    private List<SelectItem> getSubDistrictList(Integer districtId) {
        List<SelectItem> values = new ArrayList<SelectItem>();
        if(districtId != null && districtId != 0) {
            List<Subdistrict> subDistrictList = subdistrictDAO.findSubDistrictByDistrictId(districtId); 
            for (Subdistrict obj : subDistrictList) {
                values.add(new SelectItem(obj.getId(), obj.getName()));
            }
        }
        return values;
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
       
    public void currentDistrictListener(ValueChangeEvent event) {
        Integer districtId = (Integer) event.getNewValue();
        currentSubDistrictList = getSubDistrictList(districtId);
        currentSubDistrictId = 0;
        if(currentPostalList != null)
            currentPostalList.clear();
    }
        
    public void homeDistrictListener(ValueChangeEvent event) {
        Integer districtId = (Integer) event.getNewValue();
        homeSubDistrictList = getSubDistrictList(districtId);
        homeSubDistrictId = 0;
        if(homePostalList != null)
            homePostalList.clear();
    }
        
    public void shippingDistrictListener(ValueChangeEvent event) {
        Integer districtId = (Integer) event.getNewValue();
        shippingSubDistrictList = getSubDistrictList(districtId);
        shippingSubDistrictId = 0;
        if(shippingPostalList != null)
            shippingPostalList.clear();
    }
        
    public void billingDistrictListener(ValueChangeEvent event) {
        Integer districtId = (Integer) event.getNewValue();
        billingSubDistrictList = getSubDistrictList(districtId);
        billingSubDistrictId = 0;
        if(billingPostalList != null)
            billingPostalList.clear();
    }
        
    public void currentSubDistrictListener(ValueChangeEvent event) {
        Integer subdistrictId = (Integer) event.getNewValue();
        if(currentPostalList != null) {
            currentPostalList.clear();
        }
        if(subdistrictId != null) {
            currentPostalList = getPostalList(subdistrictId);
        }
    }
        
    public void homeSubDistrictListener(ValueChangeEvent event) {
        Integer subdistrictId = (Integer) event.getNewValue();
        if(homePostalList != null) {
            homePostalList.clear();
        }
        if(subdistrictId != null) {
            homePostalList = getPostalList(subdistrictId);
        }
    }
        
    public void shippingSubDistrictListener(ValueChangeEvent event) {
        Integer subdistrictId = (Integer) event.getNewValue();
        if(shippingPostalList != null) {
            shippingPostalList.clear();
        }
        if(subdistrictId != null) {
            shippingPostalList = getPostalList(subdistrictId);
        }
    }
        
    public void billingSubDistrictListener(ValueChangeEvent event) {
        Integer subdistrictId = (Integer) event.getNewValue();
        if(billingPostalList != null) {
            billingPostalList.clear();
        }
        if(subdistrictId != null) {
            billingPostalList = getPostalList(subdistrictId);
        }
    }
    
}
