/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.core.model.value.admin.CriteriaListValue;
import com.maxelyz.utils.SecurityUtil;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.bean.ViewScoped;
/**
 *
 * @author Administrator
 */
@ManagedBean
@ViewScoped
public class MarketingSegmentationController {
    private static Logger log = Logger.getLogger(MarketingSegmentationController.class);
    private static String REDIRECT_PAGE = "marketing.jsf";
    private static String SUCCESS = "marketing.xhtml?faces-redirect=true";
    private static String FAILURE = "marketingsegmentation.xhtml";
    
    private Marketing marketing;
    private String message;
    private String messageMarketing;
    private String messageDup;
    private String messageDate;       
    private Map<String, Integer> marketingList = new LinkedHashMap<String, Integer>();
    private Map<String, Integer> contactResultList = new LinkedHashMap<String, Integer>();
    private Map<String, Integer> noSaleResultList = new LinkedHashMap<String, Integer>();
    List<Object[]> customerList = new ArrayList<Object[]>();
    private int simulateRecord;
    
    //Criteria    
    private Integer fileTemplateId;
    private Integer prospectlistSponsorId;
    private boolean selectContactResult;
    private boolean selectGender;
    private boolean selectAge;
    private boolean selectHomePhone;
    private boolean selectOfficePhone;
    private boolean selectMobilePhone;
    private boolean selectSaleResult;
    private boolean selectYesSale;
    private boolean selectNoSale;
    private List<Integer> selectedMarketingList;
    private List<Integer> selectedContactResultList; 
    private List<Integer> selectedNoSaleResultList; 
    private String gender;
    private int fromAge, toAge;
    private String homePhonePrefix;
    private String officePhonePrefix;
    private String mobilePhonePrefix;
    private String chkYesInProduct;
    private List<Integer> selectedProduct = new ArrayList<Integer>();
    
    private List<CriteriaListValue> selectedAdvanceCriteria;
    
    @ManagedProperty(value = "#{marketingDAO}")
    private MarketingDAO marketingDAO;
    @ManagedProperty(value = "#{marketingCustomerDAO}")
    private MarketingCustomerDAO marketingCustomerDAO;
    @ManagedProperty(value = "#{customerDAO}")
    private CustomerDAO customerDAO;
    @ManagedProperty(value = "#{contactResultDAO}")
    private ContactResultDAO contactResultDAO;
    @ManagedProperty(value = "#{nosaleReasonDAO}")
    private NosaleReasonDAO nosaleReasonDAO;
    @ManagedProperty(value = "#{fileTemplateDAO}")
    private FileTemplateDAO fileTemplateDAO;
    @ManagedProperty(value = "#{prospectlistSponsorDAO}")
    private ProspectlistSponsorDAO prospectlistSponsorDAO;
    @ManagedProperty(value = "#{productDAO}")
    private ProductDAO productDAO;
            
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:marketinglistupload:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        marketing = new Marketing();
        marketing.setEnable(Boolean.TRUE);
        marketing.setStatus(Boolean.TRUE);
//        setMarketingList();
        setContactResultList();
        setNoSaleResultList();
        selectedAdvanceCriteria = new ArrayList<CriteriaListValue>();
    }
    
//     public void setMarketingList() {
//        List<Marketing> marketings = getMarketingDAO().findMarketingEntities();
//        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
//        for (Marketing o: marketings) {
//            values.put(o.getCode()+":"+o.getName(), o.getId());
//        }
//        marketingList = values;
//    }
//
//    public Map<String, Integer> getMarketingList() {
//        return marketingList;
//    }
     
     public void setContactResultList() {
        List<ContactResult> contactResults = getContactResultDAO().findContactResultEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (ContactResult o: contactResults) {
            values.put(o.getName(), o.getId());
        }
        contactResultList = values;
    }

    public Map<String, Integer> getContactResultList() {
        return contactResultList;
    }
    
    public void setNoSaleResultList() {
        List<NosaleReason> nosaleReasons = getNosaleReasonDAO().findNosaleReasonEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (NosaleReason o: nosaleReasons) {
            values.put(o.getName(), o.getId());
        }
        noSaleResultList = values;
    }

    public Map<String, Integer> getNoSaleResultList() {
        return noSaleResultList;
    }
              
    public Map<String, Integer> getProductList() {
        ProductDAO dao = getProductDAO();
        List<Product> products = dao.findProductEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Product product : products) {
            if (product.getProductCategory().getCategoryType().equals("motor")) {
                values.put(product.getName() + "(" + product.getModelType().getName() + ", " + product.getModelFromYear() + "-" + product.getModelToYear() + ", " + product.getProductCategory().getName() + ")", product.getId());
            } else {
                values.put(product.getName(), product.getId());
            }          
        }

//        for (Integer pid : selectedProduct) {
//            Boolean exist = false;
//            for (Map.Entry<String, Integer> item : values.entrySet()) {
//                if (pid.intValue() == item.getValue()) {
//                    exist = true;
//                    break;
//                }
//            }
//            if (!exist) {
//                Product p = productDAO.findProduct(pid);
//                values.put(p.getName(), p.getId());
//            }
//        }
        return values;
    }
    
    public boolean isSavePermitted() {
       return SecurityUtil.isPermitted("admin:marketinglistupload:add");
    } 
    
    public void resetSimulateListener() {
        simulateRecord = 0;
    }
    
    public void changeMarketingCriteria() {
        simulateRecord = 0;
        if(!marketingList.isEmpty()) {
            marketingList.clear();
        }
        
        if(prospectlistSponsorId != null && fileTemplateId != null) {
            List<Marketing> marketings = getMarketingDAO().findMarketingByFileTemplateAndProspect(prospectlistSponsorId, fileTemplateId);
            Map<String, Integer> values = new LinkedHashMap<String, Integer>();
            for (Marketing o: marketings) {
                values.put(o.getCode()+":"+o.getName(), o.getId());
            }
            marketingList = values;
        }
        if(fileTemplateId != null) {
            initAdvanceCriteria(fileTemplateId);
        }
        
    }
  
    public void initAdvanceCriteria(Integer fileTemplateId) {       
        // CREATE CRITERIA CUSTOMER FLEXFIELD
        if(!selectedAdvanceCriteria.isEmpty()) {
            selectedAdvanceCriteria.clear();    
        }
        selectedAdvanceCriteria = marketingDAO.findMarketinAdvanceCriteriaByFileTemplate(fileTemplateId);
    }
    
    public String saveAction() {
        messageDup="";
        if(checkCode(marketing)) {
            if(marketing.getStartDate().before(marketing.getEndDate())) {
                if(customerList.size() > 0) {
                    try {
                        marketing.setProspectlistSponsor(new ProspectlistSponsor(prospectlistSponsorId));
                        marketing.setFileTemplate(new FileTemplate(fileTemplateId));
                        getMarketingDAO().createSegmentationData(marketing, customerList);
                    } catch (Exception e) {
                        log.error(e);
                        message = e.getMessage();
                        return null;
                    }
                }
                return SUCCESS;
            } else {
                messageDate = " Effective Date From over Date To";
                return null;
            }
        } else {
            messageDup = " Code is already taken";
            return null; 
        }
    }
    
    public void simulateAction() {
        messageMarketing = "";
        if(selectedMarketingList != null && !selectedMarketingList.isEmpty()) {
            customerList = getMarketingDAO().criteriaSegmentationMarketing(marketing,selectedMarketingList,selectContactResult,
                            selectGender,selectAge,selectHomePhone,selectOfficePhone,selectMobilePhone,selectSaleResult,selectYesSale,selectNoSale,
                            selectedContactResultList,gender,fromAge,toAge,homePhonePrefix,officePhonePrefix,mobilePhonePrefix,selectedNoSaleResultList,
                            selectedAdvanceCriteria, chkYesInProduct, selectedProduct);
            simulateRecord = customerList.size();
        } else {
            simulateRecord = 0;
            messageMarketing = "Selected Marketing is required";
        }
    }
    
    public Boolean checkCode(Marketing marketing) {
        String code = marketing.getCode();
        Integer id=0; 
        if(marketing.getId() != null)
            id = marketing.getId();
        MarketingDAO dao = getMarketingDAO();
        
        Integer cnt = dao.checkMarketingCode(code, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }

    public String backAction() {
        return SUCCESS;
    }
    
    // Get Set DAO
    public CustomerDAO getCustomerDAO() {
        return customerDAO;
    }

    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public MarketingCustomerDAO getMarketingCustomerDAO() {
        return marketingCustomerDAO;
    }

    public void setMarketingCustomerDAO(MarketingCustomerDAO marketingCustomerDAO) {
        this.marketingCustomerDAO = marketingCustomerDAO;
    }

    public MarketingDAO getMarketingDAO() {
        return marketingDAO;
    }

    public void setMarketingDAO(MarketingDAO marketingDAO) {
        this.marketingDAO = marketingDAO;
    }

    public ContactResultDAO getContactResultDAO() {
        return contactResultDAO;
    }

    public void setContactResultDAO(ContactResultDAO contactResultDAO) {
        this.contactResultDAO = contactResultDAO;
    }

    public NosaleReasonDAO getNosaleReasonDAO() {
        return nosaleReasonDAO;
    }

    public void setNosaleReasonDAO(NosaleReasonDAO nosaleReasonDAO) {
        this.nosaleReasonDAO = nosaleReasonDAO;
    }
        
    // Get Set Properties
    public Marketing getMarketing() {
        return marketing;
    }

    public void setMarketing(Marketing marketing) {
        this.marketing = marketing;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }

    public boolean isSelectAge() {
        return selectAge;
    }

    public void setSelectAge(boolean selectAge) {
        this.selectAge = selectAge;
    }

    public boolean isSelectContactResult() {
        return selectContactResult;
    }

    public void setSelectContactResult(boolean selectContactResult) {
        this.selectContactResult = selectContactResult;
    }

    public boolean isSelectGender() {
        return selectGender;
    }

    public void setSelectGender(boolean selectGender) {
        this.selectGender = selectGender;
    }

    public List<Integer> getSelectedContactResultList() {
        return selectedContactResultList;
    }

    public void setSelectedContactResultList(List<Integer> selectedContactResultList) {
        this.selectedContactResultList = selectedContactResultList;
    }

    public List<Integer> getSelectedMarketingList() {
        return selectedMarketingList;
    }

    public void setSelectedMarketingList(List<Integer> selectedMarketingList) {
        this.selectedMarketingList = selectedMarketingList;
    }

    public int getFromAge() {
        return fromAge;
    }

    public void setFromAge(int fromAge) {
        this.fromAge = fromAge;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getToAge() {
        return toAge;
    }

    public void setToAge(int toAge) {
        this.toAge = toAge;
    }

    public String getHomePhonePrefix() {
        return homePhonePrefix;
    }

    public void setHomePhonePrefix(String homePhonePrefix) {
        this.homePhonePrefix = homePhonePrefix;
    }

    public String getMobilePhonePrefix() {
        return mobilePhonePrefix;
    }

    public void setMobilePhonePrefix(String mobilePhonePrefix) {
        this.mobilePhonePrefix = mobilePhonePrefix;
    }

    public String getOfficePhonePrefix() {
        return officePhonePrefix;
    }

    public void setOfficePhonePrefix(String officePhonePrefix) {
        this.officePhonePrefix = officePhonePrefix;
    }

    public boolean isSelectHomePhone() {
        return selectHomePhone;
    }

    public void setSelectHomePhone(boolean selectHomePhone) {
        this.selectHomePhone = selectHomePhone;
    }

    public boolean isSelectMobilePhone() {
        return selectMobilePhone;
    }

    public void setSelectMobilePhone(boolean selectMobilePhone) {
        this.selectMobilePhone = selectMobilePhone;
    }

    public boolean isSelectNoSale() {
        return selectNoSale;
    }

    public void setSelectNoSale(boolean selectNoSale) {
        this.selectNoSale = selectNoSale;
    }

    public boolean isSelectOfficePhone() {
        return selectOfficePhone;
    }

    public void setSelectOfficePhone(boolean selectOfficePhone) {
        this.selectOfficePhone = selectOfficePhone;
    }

    public boolean isSelectSaleResult() {
        return selectSaleResult;
    }

    public void setSelectSaleResult(boolean selectSaleResult) {
        this.selectSaleResult = selectSaleResult;
    }

    public boolean isSelectYesSale() {
        return selectYesSale;
    }

    public void setSelectYesSale(boolean selectYesSale) {
        this.selectYesSale = selectYesSale;
    }

    public List<Integer> getSelectedNoSaleResultList() {
        return selectedNoSaleResultList;
    }

    public void setSelectedNoSaleResultList(List<Integer> selectedNoSaleResultList) {
        this.selectedNoSaleResultList = selectedNoSaleResultList;
    }

    public int getSimulateRecord() {
        return simulateRecord;
    }

    public void setSimulateRecord(int simulateRecord) {
        this.simulateRecord = simulateRecord;
    }

    public String getMessageMarketing() {
        return messageMarketing;
    }

    public void setMessageMarketing(String messageMarketing) {
        this.messageMarketing = messageMarketing;
    }

    public List<CriteriaListValue> getSelectedAdvanceCriteria() {
        return selectedAdvanceCriteria;
    }

    public void setSelectedAdvanceCriteria(List<CriteriaListValue> selectedAdvanceCriteria) {
        this.selectedAdvanceCriteria = selectedAdvanceCriteria;
    }

    public ProspectlistSponsorDAO getProspectlistSponsorDAO() {
        return prospectlistSponsorDAO;
    }

    public void setProspectlistSponsorDAO(ProspectlistSponsorDAO prospectlistSponsorDAO) {
        this.prospectlistSponsorDAO = prospectlistSponsorDAO;
    }
    
    public Map<String, Integer> getProspectlistSponsors() {
        return this.getProspectlistSponsorDAO().getProspectlistSponsorMap();
    }    
    
    public Integer getProspectlistSponsorId() {
        return prospectlistSponsorId;
    }

    public void setProspectlistSponsorId(Integer prospectlistSponsorId) {
        this.prospectlistSponsorId = prospectlistSponsorId;
    }
        
    public FileTemplateDAO getFileTemplateDAO() {
        return fileTemplateDAO;
    }

    public void setFileTemplateDAO(FileTemplateDAO fileTemplateDAO) {
        this.fileTemplateDAO = fileTemplateDAO;
    }
    
    public Map<String, Integer> getFileTemplates() {
        return this.getFileTemplateDAO().getFileTemplateMap();
    }
    
    public Integer getFileTemplateId() {
        return fileTemplateId;
    }

    public void setFileTemplateId(Integer fileTemplateId) {
        this.fileTemplateId = fileTemplateId;
    }

    public Map<String, Integer> getMarketingList() {
        return marketingList;
    }

    public void setMarketingList(Map<String, Integer> marketingList) {
        this.marketingList = marketingList;
    }

    public String getChkYesInProduct() {
        return chkYesInProduct;
    }

    public void setChkYesInProduct(String chkYesInProduct) {
        this.chkYesInProduct = chkYesInProduct;
    }

    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public List<Integer> getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(List<Integer> selectedProduct) {
        this.selectedProduct = selectedProduct;
    }
    
}
