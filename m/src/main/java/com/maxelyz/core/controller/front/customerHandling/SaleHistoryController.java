/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.front.customerHandling;

import com.maxelyz.core.common.front.dispatch.FrontDispatcher;
import com.maxelyz.core.common.front.dispatch.RegistrationPouch;
import com.maxelyz.core.controller.UserSession;
import com.maxelyz.core.model.dao.AssignmentDetailDAO;
import com.maxelyz.core.model.dao.CustomerDAO;
import com.maxelyz.core.model.dao.ProductDAO;
import com.maxelyz.core.model.dao.PurchaseOrderDAO;
import com.maxelyz.core.model.dao.front.customerHandling.CustomerHandlingDAO;
import com.maxelyz.core.model.entity.AssignmentDetail;
import com.maxelyz.core.model.entity.Product;
import com.maxelyz.core.model.entity.PurchaseOrder;
import com.maxelyz.core.model.entity.PurchaseOrderDetail;
import com.maxelyz.core.model.value.front.customerHandling.CustomerInfoValue;
import com.maxelyz.core.model.value.front.customerHandling.SaleHistoryInfoValue;
import com.maxelyz.core.model.value.front.customerHandling.TotalPremiumInfoValue;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;
/**
 *
 * @author admin
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class SaleHistoryController {
    private String SUCCESS = "/front/customerHandling/saleHistory.xhtml";
    private String FAIL = "/campaign/followupList.xhtml";
    private String REGISTRATION_RETAIL_FORM = "/front/customerHandling/registrationRetail.xhmtl";
    private UserSession userSession;

    private List<PurchaseOrderDetail> purchaseOrderDetailList;
    private List<PurchaseOrder> purchaseOrderList;
    private List<SaleHistoryInfoValue> saleHistoryList;
    
    // POPUP TOTAL PREMIUM FOR FAMILY PRODUCT
    private List<TotalPremiumInfoValue> totalPremiumList;
    private Double totalPremium;
    
    private String emessage;
    private Product product;

    @ManagedProperty(value = "#{customerDAO}")
    private CustomerDAO customerDAO;
    @ManagedProperty(value = "#{customerHandlingDAO}")
    private CustomerHandlingDAO customerHandlingDAO;
    @ManagedProperty(value = "#{purchaseOrderDAO}")
    private PurchaseOrderDAO purchaseOrderDAO;
    @ManagedProperty(value = "#{assignmentDetailDAO}")
    private AssignmentDetailDAO assignmentDetailDAO;
    @ManagedProperty(value = "#{productDAO}")
    private ProductDAO productDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("salehistory:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        this.emessage = null;
        userSession = JSFUtil.getUserSession();
        JSFUtil.getUserSession().setRefNo(null);
        JSFUtil.getUserSession().setMode("hide"); //Dial Panel Hide
        JSFUtil.getUserSession().setTelephonyEdit("view"); //Dial Panel View
        
        String assignmentDetailId = JSFUtil.getRequestParameterMap("assignmentDetailId");
        if (assignmentDetailId != null && !assignmentDetailId.isEmpty()) {
            AssignmentDetail assignmentDetail = assignmentDetailDAO.findAssignmentDetail(Integer.parseInt(assignmentDetailId));
                userSession.setAssignmentDetail(assignmentDetail);
            }
        
        if (userSession.getCustomerDetail() == null && JSFUtil.getRequestParameterMap("selectedId") != null) {
            Integer customerId = Integer.parseInt(JSFUtil.getRequestParameterMap("selectedId"));
            if (customerId != null && customerId != 0) {
                CustomerInfoValue customerInfoValue = customerHandlingDAO.findCustomerHandling(customerId);
                userSession.setCustomerDetail(customerInfoValue);
            }
        }
//        if(purchaseOrderDetailList == null || purchaseOrderDetailList.isEmpty()){
//            purchaseOrderDetailList = new ArrayList<PurchaseOrderDetail>();
//            if (userSession.getCustomerDetail().getId() != null && userSession.getCustomerDetail().getId() != 0) {
//                Customer customer = customerDAO.findCustomer(userSession.getCustomerDetail().getId());
//                purchaseOrderList = purchaseOrderDAO.findByCustomer(customer);
//                for(PurchaseOrder po : purchaseOrderList){
//                    if(po.getPurchaseOrderDetailCollection() != null){
//                        for(PurchaseOrderDetail pod : po.getPurchaseOrderDetailCollection()){
//                            purchaseOrderDetailList.add(pod);
//                        }
//                    }
//                }
//            }
//        }
        saleHistoryList = purchaseOrderDAO.findSaleHistory(userSession.getCustomerDetail().getId());
        
         UserSession u =  JSFUtil.getUserSession();
         u.setContactResultPlanId(null);
         
    }
    
    public boolean showFamilyButtonByMasterAndSaleResult(String poId) {  // controller 
        int chkFamilyButton = purchaseOrderDAO.findFamilyYesSaleMasterPlan(Integer.parseInt(poId));
        if(chkFamilyButton == 0){
            return false;
        }else {
            return true;
        }
    }

    public void showTotalPremium() {
        Integer poId = 0;
        totalPremium = 0.0;
        totalPremiumList = new ArrayList<TotalPremiumInfoValue>();
        
        if(JSFUtil.getRequestParameterMap("poId") != null) {
            poId = Integer.parseInt(JSFUtil.getRequestParameterMap("poId"));
        }
        
        if(poId != 0) {
            totalPremiumList = this.getPurchaseOrderDAO().findTotalPremiumFamily(poId);
            if(totalPremiumList != null)  {
                for(TotalPremiumInfoValue obj: totalPremiumList) {
                    if(obj.getSaleResult().equals("Yes") && !obj.getApprovalStatus().equals("rejected") && !obj.getQcStatus().equals("rejected") && !obj.getPaymentStatus().equals("rejected")) {
                        totalPremium += obj.getNetPremium();
                    }
                }
            }
        }
    }
            
    public String toSaleHistory(){
        try {
            initialize();
            return SUCCESS;
        } catch (Exception e) {
            return FAIL;
        }
    }
    
    public String toRegistrationForm(String action,Integer purchaseOrderId) {
        this.emessage = null;
        try{ 
        Integer productId = Integer.parseInt(JSFUtil.getRequestParameterMap("productId"));
        Integer contactResultPlanId = Integer.parseInt(JSFUtil.getRequestParameterMap("contactResultPlanId"));
        product = productDAO.findProduct(productId);
//            System.out.println("productId "+productId);
//            System.out.println("contactResultPlanId "+contactResultPlanId );
//            System.out.println("product "+ product);
//        When click product name then add product into session
        if (JSFUtil.getUserSession().getProducts() != null) {
            JSFUtil.getUserSession().setProducts(product);
            UserSession u =  JSFUtil.getUserSession();
            u.setContactResultPlanId(contactResultPlanId);
            u.setPurchaseOrderId(purchaseOrderId);
        }
        
            RegistrationPouch pouch = new RegistrationPouch(); // create pouch to carry data for purchase order            
            if (action==null) throw new Exception("Not found action.");
            else if (action.equals("edit")){
                pouch.registeredPouchType(RegistrationPouch.SENDER_TYPE.SALEHISTORY, RegistrationPouch.RECEIVER_MODE.EDIT);
                pouch.putEditModeParameter(purchaseOrderId);
            }else if ( action.equals("copy")){
                pouch.registeredPouchType(RegistrationPouch.SENDER_TYPE.SALEHISTORY, RegistrationPouch.RECEIVER_MODE.COPY);
                pouch.putCopyModeParameter(purchaseOrderId);
            } 
            FrontDispatcher.keepPouch(pouch); // registered pouch on dispatcher
            return FrontDispatcher.getPouchReceiver(pouch); // get receiver
        } catch (Exception e) {
            System.out.println("Error when dispatch from saleHistory to registration form : "+e.getMessage());
            this.emessage = "Cannot forward to registration form. Please select Sale History menu and try again. [Cause: "+e.getMessage()+"] ";
            return null;
        }        
    }
    
    public String toRegistrationFormPoRetail(){
        try {
            Integer poId = Integer.parseInt(JSFUtil.getRequestParameterMap("poId"));
            if (poId != null && poId != 0) {
                JSFUtil.getRequest().setAttribute("poId", poId);
            }
            return REGISTRATION_RETAIL_FORM;
        } catch (Exception e) {
            return SUCCESS;
        }

    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public CustomerDAO getCustomerDAO() {
        return customerDAO;
    }

    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public PurchaseOrderDAO getPurchaseOrderDAO() {
        return purchaseOrderDAO;
    }

    public void setPurchaseOrderDAO(PurchaseOrderDAO purchaseOrderDAO) {
        this.purchaseOrderDAO = purchaseOrderDAO;
    }

    public List<PurchaseOrder> getPurchaseOrderList() {
        return purchaseOrderList;
    }

    public void setPurchaseOrderList(List<PurchaseOrder> purchaseOrderList) {
        this.purchaseOrderList = purchaseOrderList;
    }

    public List<PurchaseOrderDetail> getPurchaseOrderDetailList() {
        return purchaseOrderDetailList;
    }

    public void setPurchaseOrderDetailList(List<PurchaseOrderDetail> purchaseOrderDetailList) {
        this.purchaseOrderDetailList = purchaseOrderDetailList;
    }

    public CustomerHandlingDAO getCustomerHandlingDAO() {
        return customerHandlingDAO;
    }

    public void setCustomerHandlingDAO(CustomerHandlingDAO customerHandlingDAO) {
        this.customerHandlingDAO = customerHandlingDAO;
    }

    public AssignmentDetailDAO getAssignmentDetailDAO() {
        return assignmentDetailDAO;
    }

    public void setAssignmentDetailDAO(AssignmentDetailDAO assignmentDetailDAO) {
        this.assignmentDetailDAO = assignmentDetailDAO;
    }

    public List<SaleHistoryInfoValue> getSaleHistoryList() {
        return saleHistoryList;
    }

    public void setSaleHistoryList(List<SaleHistoryInfoValue> saleHistoryList) {
        this.saleHistoryList = saleHistoryList;
    }

    public String getEmessage() {
        return emessage;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public List<TotalPremiumInfoValue> getTotalPremiumList() {
        return totalPremiumList;
    }

    public void setTotalPremiumList(List<TotalPremiumInfoValue> totalPremiumList) {
        this.totalPremiumList = totalPremiumList;
    }

    public Double getTotalPremium() {
        return totalPremium;
    }

    public void setTotalPremium(Double totalPremium) {
        this.totalPremium = totalPremium;
    }
    
}
