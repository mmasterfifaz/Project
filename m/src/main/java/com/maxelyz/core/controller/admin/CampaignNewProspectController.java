/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.*;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class CampaignNewProspectController {
    private static Logger log = Logger.getLogger(CampaignNewProspectController.class);
    private static String REDIRECT_PAGE = "campaign.jsf";
    private static String SUCCESS = "campaignsummary.xhtml";
    private static String FAILURE = "campaignnewprospect.xhtml";
    
    private Campaign campaign;
    private Customer customer;    
    private Map<String, Integer> productList = new LinkedHashMap<String, Integer>();
    private List<Integer> selectedProduct;
    private int marketingId;
    private int userGroupId;
    private int userId;
    private String messageDup = "";
    private String meassgeFlexField1Dup = "";
    
    private List<MarketingCustomer> customerList = new ArrayList<MarketingCustomer>();
    private String spotRefId = "";
    private Customer referCustomer;
    private MarketingCustomer referMarketingCustomer;
    private List<FlexfieldMappingDetail> fxDetail;
    private boolean isCopy = false, canCopy = true;
    private String custName, custSurname , mobilePhone, referCustName;
    private int marketingSearchId;
     
    @ManagedProperty(value = "#{campaignDAO}")
    private CampaignDAO campaignDAO;
    @ManagedProperty(value = "#{marketingDAO}")
    private MarketingDAO marketingDAO;
    @ManagedProperty(value = "#{userGroupDAO}")
    private UserGroupDAO userGroupDAO;
    @ManagedProperty(value = "#{usersDAO}")
    private UsersDAO usersDAO;
    @ManagedProperty(value = "#{customerDAO}")
    private CustomerDAO customerDAO;
    @ManagedProperty(value = "#{assignmentDAO}")
    private AssignmentDAO assignmentDAO;
    @ManagedProperty(value = "#{marketingCustomerDAO}")
    private MarketingCustomerDAO marketingCustomerDAO;
    @ManagedProperty(value = "#{flexFieldMappingDetailDAO}")
    private FlexFieldMappingDetailDAO flexFieldMappingDetailDAO;
    
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:campaignassignment:add")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        String selectedID  = (String) JSFUtil.getRequestParameterMap("id");
        if (selectedID==null) {
            JSFUtil.redirect(REDIRECT_PAGE);
        } else {
            CampaignDAO dao = getCampaignDAO();
            campaign = dao.findCampaign(new Integer(selectedID));
            setCustomer(new Customer());
            setProductList();
            messageDup = "";
            referCustomer  = new Customer();
        }
    }
    
    public void initializeListener(ActionEvent event) {
        marketingId = 0;
        userGroupId = 0;
        userId = 0;
        if(selectedProduct != null)
            selectedProduct.clear();
        initialize();
    }
    
    public String backAction() {
        return SUCCESS;
    }
    
    public String saveAction(ActionEvent event) {
        messageDup = "";
        meassgeFlexField1Dup = "";
//        if(checkCustomer()) {
          if(checkCustomerFlexfield1()) {
              try {
                  Customer c = this.getCustomerDAO().findDuplicateCustomerByName(customer);
                  if (c != null) {
                      customer.setParentId(c.getId());
                  }
                  insertAssignment();
              } catch (Exception e) {
                  log.error(e.getMessage());
              }
              JSFUtil.getServletContext().setAttribute("id", campaign.getId());
              JSFUtil.getServletContext().setAttribute("tab", "assignment");
              JSFUtil.redirect("campaignsummary.jsf");
          }{
              meassgeFlexField1Dup = "CustomerId is already taken!";
              return null;
        }
//        } else
//            messageDup = "Duplicate customer";   
    }
        
    private void insertAssignment() {
        customer.setCustomerType(new CustomerType(2));
        customer.setManualInsert(Boolean.TRUE);
        customer.setFlexfield20("R");
        if(isCopy){
            copyReferCustomerProfile();
        }else{
            customer.setFlexfield13("9999999");
            customer.setFlexfield15(spotRefId);
        }
        List<Product> products = this.getSelectedProductCollection();
        this.getAssignmentDAO().assignNewProspect(customer, campaign, marketingId, userId, products);
    }
    
    private void copyReferCustomerProfile(){
        if(referCustomer != null && referCustomer.getId() != null){
            customer.setAccountId(referCustomer.getAccountId());
            customer.setBillingAddressLine1(referCustomer.getBillingAddressLine1());
            customer.setBillingAddressLine2(referCustomer.getBillingAddressLine2());
            customer.setBillingAddressLine3(referCustomer.getBillingAddressLine3());
            customer.setBillingAddressLine4(referCustomer.getBillingAddressLine4());
            customer.setBillingAddressLine5(referCustomer.getBillingAddressLine5());
            customer.setBillingAddressLine6(referCustomer.getBillingAddressLine6());
            customer.setBillingAddressLine7(referCustomer.getBillingAddressLine7());
            customer.setBillingAddressLine8(referCustomer.getBillingAddressLine8());
            customer.setBillingCountryName(referCustomer.getBillingCountryName());
            customer.setBillingDistrictId(referCustomer.getBillingDistrictId());
            customer.setBillingDistrictName(referCustomer.getBillingDistrictName());
            customer.setBillingFullname(referCustomer.getBillingFullname());
            customer.setBillingPostalCode(referCustomer.getBillingPostalCode());
            customer.setBillingProvinceName(referCustomer.getBillingProvinceName());
            customer.setBillingSubDistrict(referCustomer.getBillingSubDistrict());
//            customer.setCampaignCustomerCollection(referCustomer.getCampaignCustomerCollection());
            customer.setCarBrand(referCustomer.getCarBrand());
            customer.setCarCharacterGroup(referCustomer.getCarCharacterGroup());
            customer.setCarModel(referCustomer.getCarModel());
            customer.setCarNumber(referCustomer.getCarNumber());
            customer.setCarProvince(referCustomer.getCarProvince());
            customer.setCarYear(referCustomer.getCarYear());
//            customer.setContactCaseCollection(referCustomer.getContactCaseCollection());
            customer.setContactExt1(referCustomer.getContactExt1());
            customer.setContactExt2(referCustomer.getContactExt2());
            customer.setContactExt3(referCustomer.getContactExt3());
            customer.setContactExt4(referCustomer.getContactExt4());
            customer.setContactExt5(referCustomer.getContactExt5());
//            customer.setContactPhone1(referCustomer.getContactPhone1());
//            customer.setContactPhone1Close(referCustomer.getContactPhone1Close());
//            customer.setContactPhone1Reason(referCustomer.getContactPhone1Reason());
//            customer.setContactPhone1ReasonId(referCustomer.getContactPhone1ReasonId());
//            customer.setContactPhone2(referCustomer.getContactPhone2());
//            customer.setContactPhone2Close(referCustomer.getContactPhone2Close());
//            customer.setContactPhone2Reason(referCustomer.getContactPhone2Reason());
//            customer.setContactPhone2ReasonId(referCustomer.getContactPhone2ReasonId());
//            customer.setContactPhone3(referCustomer.getContactPhone3());
//            customer.setContactPhone3Close(referCustomer.getContactPhone3Close());
//            customer.setContactPhone3Reason(referCustomer.getContactPhone3Reason());
//            customer.setContactPhone3ReasonId(referCustomer.getContactPhone3ReasonId());
//            customer.setContactPhone4(referCustomer.getContactPhone4());
//            customer.setContactPhone4Close(referCustomer.getContactPhone4Close());
//            customer.setContactPhone4Reason(referCustomer.getContactPhone4Reason());
//            customer.setContactPhone4ReasonId(referCustomer.getContactPhone4ReasonId());
//            customer.setContactPhone5(referCustomer.getContactPhone5());
//            customer.setContactPhone5Close(referCustomer.getContactPhone5Close());
//            customer.setContactPhone5Reason(referCustomer.getContactPhone5Reason());
//            customer.setContactPhone5ReasonId(referCustomer.getContactPhone5ReasonId());
            customer.setCurrentAddressLine1(referCustomer.getCurrentAddressLine1());
            customer.setCurrentAddressLine2(referCustomer.getCurrentAddressLine2());
            customer.setCurrentAddressLine3(referCustomer.getCurrentAddressLine3());
            customer.setCurrentAddressLine4(referCustomer.getCurrentAddressLine4());
            customer.setCurrentAddressLine5(referCustomer.getCurrentAddressLine5());
            customer.setCurrentAddressLine6(referCustomer.getCurrentAddressLine6());
            customer.setCurrentAddressLine7(referCustomer.getCurrentAddressLine7());
            customer.setCurrentAddressLine8(referCustomer.getCurrentAddressLine8());
            customer.setCurrentCountryName(referCustomer.getCurrentCountryName());
            customer.setCurrentDistrictId(referCustomer.getCurrentDistrictId());
            customer.setCurrentDistrictName(referCustomer.getCurrentDistrictName());
            customer.setCurrentFullname(referCustomer.getCurrentFullname());
            customer.setCurrentPostalCode(referCustomer.getCurrentPostalCode());
            customer.setCurrentProvinceName(referCustomer.getCurrentProvinceName());
            customer.setCurrentSubDistrict(referCustomer.getCurrentSubDistrict());
            customer.setDob(referCustomer.getDob());
            customer.setEmail(referCustomer.getEmail());
            customer.setEmoicon(referCustomer.getEmoicon());
            customer.setFax(referCustomer.getFax());
            customer.setFaxExt(referCustomer.getFaxExt());
            //customer.setFlexfield1("9999999");
            customer.setFlexfield2(referCustomer.getFlexfield2());
            customer.setFlexfield3(referCustomer.getFlexfield3());
            customer.setFlexfield4(referCustomer.getFlexfield4());
            customer.setFlexfield5(referCustomer.getFlexfield5());
            customer.setFlexfield6(referCustomer.getFlexfield6());
            customer.setFlexfield7(referCustomer.getFlexfield7());
            customer.setFlexfield8(referCustomer.getFlexfield8());
            customer.setFlexfield9(referCustomer.getFlexfield9());
            customer.setFlexfield10(referCustomer.getFlexfield10());
            customer.setFlexfield11(referCustomer.getFlexfield11());
            customer.setFlexfield12(referCustomer.getFlexfield12());
            //customer.setFlexfield13(referCustomer.getFlexfield13());
            customer.setFlexfield13("9999999");
            customer.setFlexfield14(referCustomer.getFlexfield14());
            customer.setFlexfield15(spotRefId);
            customer.setFlexfield16(referCustomer.getFlexfield16());
            customer.setFlexfield17(referCustomer.getFlexfield17());
            customer.setFlexfield18(referCustomer.getFlexfield18());
            customer.setFlexfield19(referCustomer.getFlexfield19());
            customer.setFlexfield20("R");
            customer.setGender(referCustomer.getGender());
            //        Customer Name,Customer Surname,Mobile Phone,Home Phone ,Home Phone (ext.),Office Phone,Office Phone (ext.)
            customer.setHeight(referCustomer.getHeight());
            customer.setNationality(referCustomer.getNationality());
            customer.setOccupation(referCustomer.getOccupation());
//            customer.setOpOut(referCustomer.getOpOut());
            customer.setPicture(referCustomer.getPicture());
            customer.setPrivilege(referCustomer.getPrivilege());
            customer.setReferenceNo(referCustomer.getReferenceNo());
            customer.setRemark(referCustomer.getRemark());
            customer.setShippingAddressLine1(referCustomer.getShippingAddressLine1());
            customer.setShippingAddressLine2(referCustomer.getShippingAddressLine2());
            customer.setShippingAddressLine3(referCustomer.getShippingAddressLine3());
            customer.setShippingAddressLine4(referCustomer.getShippingAddressLine4());
            customer.setShippingAddressLine5(referCustomer.getShippingAddressLine5());
            customer.setShippingAddressLine6(referCustomer.getShippingAddressLine6());
            customer.setShippingAddressLine7(referCustomer.getShippingAddressLine7());
            customer.setShippingAddressLine8(referCustomer.getShippingAddressLine8());
            customer.setShippingCountryName(referCustomer.getShippingCountryName());
            customer.setShippingDistrictId(referCustomer.getShippingDistrictId());
            customer.setShippingDistrictName(referCustomer.getShippingDistrictName());
            customer.setShippingFullname(referCustomer.getShippingFullname());
            customer.setShippingPostalCode(referCustomer.getShippingPostalCode());
            customer.setShippingProvinceName(referCustomer.getShippingProvinceName());
            customer.setShippingSubDistrict(referCustomer.getShippingSubDistrict());
            customer.setWeight(referCustomer.getWeight());
            customer.setHomeDistrictName(referCustomer.getHomeDistrictName());
            customer.setHomeAddressLine1(referCustomer.getHomeAddressLine1());
            customer.setHomeAddressLine2(referCustomer.getHomeAddressLine2());
            customer.setHomeAddressLine3(referCustomer.getHomeAddressLine3());
            customer.setHomeAddressLine4(referCustomer.getHomeAddressLine4());
            customer.setHomeAddressLine5(referCustomer.getHomeAddressLine5());
            customer.setHomeAddressLine6(referCustomer.getHomeAddressLine6());
            customer.setHomeAddressLine7(referCustomer.getHomeAddressLine7());
            customer.setHomeAddressLine8(referCustomer.getHomeAddressLine8());
            customer.setHomeCountryName(referCustomer.getHomeCountryName());
            customer.setHomeDistrictId(referCustomer.getHomeDistrictId());
            customer.setHomeFullname(referCustomer.getHomeFullname());
            customer.setHomeProvinceName(referCustomer.getHomeProvinceName());
            customer.setHomeSubDistrict(referCustomer.getHomeSubDistrict());
            customer.setHomePostalCode(referCustomer.getHomePostalCode());           
        }
    }

    public boolean checkCustomer() {
        if(getCustomerDAO().checkDuplicateCustomerByName(customer) > 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean checkCustomerFlexfield1() {
        if(getCustomerDAO().checkDuplicateCustomerByFlexField1(customer) > 0) {
            return false;
        } else {
            return true;
        }
    }
    
    public void initialReferCustomer(ActionEvent event) {
        customerList = new ArrayList<MarketingCustomer>();
        customerList = this.getMarketingCustomerDAO().findReferMarketingCustomer(null, null, null, 0);
//        fxDetail = this.getFlexFieldMappingDetailDAO().findFlexfieldMappingDetailEntities();
    }
    
    public void clearReferCustomer(ActionEvent event){
        custName = "";
        custSurname = "";
        mobilePhone = "";
        marketingSearchId = 0;
        referMarketingCustomer = new MarketingCustomer();
        referCustomer = new Customer();
        canCopy = true;
        isCopy = false;
        spotRefId = "";
        referCustName = "";
    }
    
    public void searchActionListener(){
        this.search();
    }
    
    public void marketingListener(){
        if (selectedProduct != null) {
            selectedProduct.clear();
        }
        this.setProductList();
    }
    
    private void search(){
        try{
            customerList = this.getMarketingCustomerDAO().findReferMarketingCustomer(custName, custSurname, mobilePhone, marketingSearchId);
        }catch(Exception e){
            log.error(e);
        }
    
    }
    
    public void referCustomerAction(ActionEvent action){
        String mId = JSFUtil.getRequestParameterMap("marketingId");
        String cId = JSFUtil.getRequestParameterMap("customerId");
        MarketingCustomerPK mc = new MarketingCustomerPK();
        mc.setCustomerId(new Integer(cId));
        mc.setMarketingId(new Integer(mId));
        referMarketingCustomer = this.getMarketingCustomerDAO().findMarketingCustomer(mc);
        if(referMarketingCustomer != null){
            canCopy = false;
            referCustomer = referMarketingCustomer.getCustomer();
            if(referMarketingCustomer.getCustomer() != null && referMarketingCustomer.getCustomer().getFlexfield15() != null && !referMarketingCustomer.getCustomer().getFlexfield15().isEmpty()){
                spotRefId = referMarketingCustomer.getCustomer().getFlexfield15();
            }
            referCustName = referCustomer.getName() + " " + referCustomer.getSurname();
        }
    }
    
    
    //List to UI----------------------------------------------------------------
    public Map<String, Integer> getMarketingList() {
        if (JSFUtil.getUserSession().getUserGroup().getRole().contains("CampaignManager")) {
            if(JSFUtil.getUserSession().getUsers().getIsAdministrator()){
                return this.getMarketingDAO().getAvaialableAllMarketList();
            } else {
                return this.getMarketingDAO().getAvaialableAllMarketListByIsAdminFlag(JSFUtil.getUserSession().getUsers().getUserGroup().getId());
            }
        } else {
            return this.getMarketingDAO().getAvaialableAllMarketList();
        }
    }
    
    public Map<String, Integer> getUserGroupList() {
        return getUserGroupDAO().getUserGroupListByAgent();
    }
    
    public Map<String, Integer> getUserList() {
        if(userGroupId == 0)
            return getUsersDAO().getAllAgentBySup(JSFUtil.getUserSession().getUsers());
        else
            return getUsersDAO().getUserListByUserGroup(userGroupId);
    }
    
    /*
    public void setProductList() {
        List<Product> products = (List<Product>)this.campaign.getProductCollection();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Product product: products) {
            if (product.getEnable().booleanValue()) {
                if(product.getProductCategory().getCategoryType().equals("motor")){
                    values.put(product.getName() + "(" + product.getModelType().getName() + "," + product.getModelYear() + "," + product.getProductCategory().getName() + ")",product.getId());
                }else{
                    values.put(product.getName(), product.getId());
                }
            }
        }
        productList = values;
    }
    */
    
    public void setProductList() {
        List<Product> products = (List<Product>) this.campaign.getProductCollection();
        Marketing marketing = null;
        if (this.marketingId > 0) {
            marketing = this.getMarketingDAO().findMarketing(this.marketingId);
        }
        if (marketing != null && marketing.getIsLinkProduct() != null) {
            if (marketing.getIsLinkProduct().booleanValue()) {
                List<Product> marketingProducts = (List<Product>) marketing.getProductCollection();
                setMarketingProductList(products, marketingProducts);
                return;
            }
        }
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Product product: products) {
            if (product.getEnable().booleanValue()) {
                if(product.getProductCategory().getCategoryType().equals("motor")){
                    values.put(product.getName() + "(" + product.getModelType().getName() + "," + product.getModelYear() + "," + product.getProductCategory().getName() + ")",product.getId());
                }else{
                    values.put(product.getName(), product.getId());
                }
            }
        }
        productList = values;
    }
    
    public Map<String, Integer> getProductList() {
        return productList;
    }
    
    public void setMarketingProductList(List<Product> campaignProducts, List<Product> marketingProducts) {
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Product campaignProduct : campaignProducts) {
            for (Product marketingProduct : marketingProducts) {
                if (campaignProduct.getId().intValue() == marketingProduct.getId().intValue()) {
                    if (campaignProduct.getEnable().booleanValue()) {
                        if (campaignProduct.getProductCategory().getCategoryType().equals("motor")) {
                            values.put(campaignProduct.getName() + "(" + campaignProduct.getModelType().getName() + "," + campaignProduct.getModelYear() + "," + campaignProduct.getProductCategory().getName() + ")", campaignProduct.getId());
                        } else {
                            values.put(campaignProduct.getName(), campaignProduct.getId());
                        }
                    }
                }
            }
        }
        productList = values;
    }
    
    public List<Product> getSelectedProductCollection() {
        List<Product> products = new ArrayList<Product>();
        for (int pid : getSelectedProduct()) {
            Product p = new Product();
            p.setId(pid);
            products.add(p);
        }
        return products;
    }
    
    public List<Integer> getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(List<Integer> selectedProduct) {
        this.selectedProduct = selectedProduct;
    }
    
    //get set properties
    public CampaignDAO getCampaignDAO() {
        return campaignDAO;
    }

    public void setCampaignDAO(CampaignDAO campaignDAO) {
        this.campaignDAO = campaignDAO;
    }

    public MarketingDAO getMarketingDAO() {
        return marketingDAO;
    }

    public void setMarketingDAO(MarketingDAO marketingDAO) {
        this.marketingDAO = marketingDAO;
    }

    public CustomerDAO getCustomerDAO() {
        return customerDAO;
    }

    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public UserGroupDAO getUserGroupDAO() {
        return userGroupDAO;
    }

    public void setUserGroupDAO(UserGroupDAO userGroupDAO) {
        this.userGroupDAO = userGroupDAO;
    }

    public UsersDAO getUsersDAO() {
        return usersDAO;
    }

    public void setUsersDAO(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public AssignmentDAO getAssignmentDAO() {
        return assignmentDAO;
    }

    public void setAssignmentDAO(AssignmentDAO assignmentDAO) {
        this.assignmentDAO = assignmentDAO;
    }
    
    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public int getMarketingId() {
        return marketingId;
    }

    public void setMarketingId(int marketingId) {
        this.marketingId = marketingId;
    }

    public int getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(int userGroupId) {
        this.userGroupId = userGroupId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }

    public List<MarketingCustomer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<MarketingCustomer> customerList) {
        this.customerList = customerList;
    }

    public MarketingCustomerDAO getMarketingCustomerDAO() {
        return marketingCustomerDAO;
    }

    public void setMarketingCustomerDAO(MarketingCustomerDAO marketingCustomerDAO) {
        this.marketingCustomerDAO = marketingCustomerDAO;
    }

    public String getSpotRefId() {
        return spotRefId;
    }

    public void setSpotRefId(String spotRefId) {
        this.spotRefId = spotRefId;
    }

    public Customer getReferCustomer() {
        return referCustomer;
    }

    public void setReferCustomer(Customer referCustomer) {
        this.referCustomer = referCustomer;
    }

    public List<FlexfieldMappingDetail> getFxDetail() {
        return fxDetail;
    }

    public void setFxDetail(List<FlexfieldMappingDetail> fxDetail) {
        this.fxDetail = fxDetail;
    }

    public FlexFieldMappingDetailDAO getFlexFieldMappingDetailDAO() {
        return flexFieldMappingDetailDAO;
    }

    public void setFlexFieldMappingDetailDAO(FlexFieldMappingDetailDAO flexFieldMappingDetailDAO) {
        this.flexFieldMappingDetailDAO = flexFieldMappingDetailDAO;
    }

    public boolean getIsCopy() {
        return isCopy;
    }

    public void setIsCopy(boolean isCopy) {
        this.isCopy = isCopy;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustSurname() {
        return custSurname;
    }

    public void setCustSurname(String custSurname) {
        this.custSurname = custSurname;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public int getMarketingSearchId() {
        return marketingSearchId;
    }

    public void setMarketingSearchId(int marketingSearchId) {
        this.marketingSearchId = marketingSearchId;
    }

    public boolean isCanCopy() {
        return canCopy;
    }

    public void setCanCopy(boolean canCopy) {
        this.canCopy = canCopy;
    }

    public String getReferCustName() {
        return referCustName;
    }

    public void setReferCustName(String referCustName) {
        this.referCustName = referCustName;
    }

    public String getMeassgeFlexField1Dup() {
        return meassgeFlexField1Dup;
    }

    public void setMeassgeFlexField1Dup(String meassgeFlexField1Dup) {
        this.meassgeFlexField1Dup = meassgeFlexField1Dup;
    }
}
