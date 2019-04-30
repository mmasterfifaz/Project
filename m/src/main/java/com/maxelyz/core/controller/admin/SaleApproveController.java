/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.*;
import com.maxelyz.core.model.entity.Campaign;
import com.maxelyz.core.model.entity.PaymentMethod;
import com.maxelyz.core.model.entity.Product;
import com.maxelyz.core.model.entity.PurchaseOrder;
import com.maxelyz.core.model.entity.PurchaseOrderDetail;
import com.maxelyz.core.model.entity.UserGroup;
import com.maxelyz.core.model.entity.Users;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.value.admin.SaleApprovalValue;
import com.maxelyz.utils.JSFUtil;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.bean.ViewScoped;
import com.maxelyz.utils.SecurityUtil;
import java.io.Serializable;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

/**
 *
 * @author admin
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class SaleApproveController implements Serializable{
    private static Logger log = Logger.getLogger(SaleApproveController.class);
    private static String REFRESH = "saleapprove.xhtml?faces-redirect=true";
    private static String EDIT = "saleapproveedit.xhtml";
    
    private List<SaleApprovalValue> saleApprovalValues;
    private List<Campaign> campaignList;
    private List<Product> productList;
    private List<UserGroup> userGroupList;
    private List<Users> userList;
    private Date fromDate, toDate;
    private Integer campaignId;
    private Integer productId;
    private String qcStatus;
    private String paymentStatus;
    private String uwStatus;
    private String confirmStatus;
    private Integer userGroupId=0;
    private Integer userId;
    private String refNo = "";
    private String policyNo = "";
    private String idcard = "";
    private String customerName = "";
    
    private List<PaymentMethod> paymentMethodList;
    private Integer paymentMethodId;
    
    //paging
    private int itemPerPage = JSFUtil.getApplication().getMaxrows();
    private int page = 1;
    private Integer rows = 0;
    
    private String orderBy = "po.purchaseDate";
    private String orderType = "asc";
        
    @ManagedProperty(value="#{campaignDAO}")
    private CampaignDAO campaignDAO;        
    @ManagedProperty(value="#{productDAO}")
    private ProductDAO productDAO;    
    @ManagedProperty(value="#{purchaseOrderDAO}")
    private PurchaseOrderDAO purchaseOrderDAO;
    @ManagedProperty(value="#{purchaseOrderDetailDAO}")
    private PurchaseOrderDetailDAO purchaseOrderDetailDAO;
    @ManagedProperty(value="#{usersDAO}")
    private UsersDAO usersDAO;    
    @ManagedProperty(value="#{userGroupDAO}")
    private UserGroupDAO userGroupDAO;
    @ManagedProperty(value="#{paymentMethodDAO}")
    private PaymentMethodDAO paymentMethodDAO;
    
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:salesapproval:view")) {
            SecurityUtil.redirectUnauthorize();
        }
        
        String role = JSFUtil.getUserSession().getUserGroup().getRole();
        if(role.contains("Supervisor")) {
            userGroupList = userGroupDAO.findUserGroupUnderParentId(JSFUtil.getUserSession().getUsers());
        } else {
            userGroupList = userGroupDAO.findUserGroupEntities();
        }
        if(role.contains("Supervisor")) {
            userList = usersDAO.findAgentUnderParentId(JSFUtil.getUserSession().getUsers());
        } else {
            if(userGroupId != null && userGroupId != 0){
                userList = usersDAO.findUsersEntitiesByUserGroup(userGroupId.intValue());
            } else {
                userList = usersDAO.findUsersEntities();
            }
        }
        campaignList = campaignDAO.findAll();
        productList = productDAO.findProductEntities();
        paymentMethodList = paymentMethodDAO.findPaymentMethodEntities();
        
        //แสดง record ที่มีสถานะ pending กับ waiting เป็น default โดยจำแนกตาม role ของ User ที่เข้าใช้งาน ได้แก่ Uw,Payment,Qc,
        if(role.contains("Qc")) {
            qcStatus = "both";
        } else { qcStatus = "0"; }
        if(role.contains("Payment")) {
            paymentStatus = "both";
        } else { paymentStatus = "0"; }
        
        if(role.contains("Uw")) {
            uwStatus = "both";
        } else { uwStatus = "0"; }
        
    }
    
    public void search() {
        int firstResult = ((page - 1) * itemPerPage);
        List<SaleApprovalValue> list = null;
        list = purchaseOrderDAO.findSaleApproval(refNo, policyNo, campaignId, fromDate, toDate, qcStatus, paymentStatus, uwStatus, confirmStatus, userId, idcard, userGroupId, productId, customerName, paymentMethodId, orderBy, orderType, firstResult, itemPerPage);
        rows = purchaseOrderDAO.countSaleApproval(refNo, policyNo, campaignId, fromDate, toDate, qcStatus, paymentStatus, uwStatus, confirmStatus, userId, idcard, userGroupId, productId, customerName, paymentMethodId); //, orderBy, orderType);

        saleApprovalValues = list != null ? new PageListSalesApproval(list, rows, itemPerPage) : null;
    }
    
    public void searchActionListener(ActionEvent event){
         page = 1;
         search();    
    }
        
    public boolean isEditApprovalPermitted() {  
        return SecurityUtil.isPermitted("admin:salesapproval:edit");
    }
        
    public String editAction(){
        return EDIT;
    }

    public void userGroupChangeListener(ValueChangeEvent event) {
        userGroupId = (Integer) event.getNewValue();
        if(userGroupId != 0){
            userList = usersDAO.findUsersEntitiesByUserGroup(userGroupId.intValue());
        } else {
            if(JSFUtil.getUserSession().getUserGroup().getRole().contains("Supervisor")) {
                userList = usersDAO.findAgentUnderParentId(JSFUtil.getUserSession().getUsers());
            } else {
                userList = usersDAO.findUsersEntities();
            }
        }
    }

    public CampaignDAO getCampaignDAO() {
        return campaignDAO;
    }

    public void setCampaignDAO(CampaignDAO campaignDAO) {
        this.campaignDAO = campaignDAO;
    }

    public List<Campaign> getCampaignList() {
        return campaignList;
    }

    public void setCampaignList(List<Campaign> campaignList) {
        this.campaignList = campaignList;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getQcStatus() {
        return qcStatus;
    }

    public void setQcStatus(String qcStatus) {
        this.qcStatus = qcStatus;
    }

    public String getUwStatus() {
        return uwStatus;
    }

    public void setUwStatus(String uwStatus) {
        this.uwStatus = uwStatus;
    }

    public Integer getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Integer userGroupId) {
        this.userGroupId = userGroupId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<SaleApprovalValue> getSaleApprovalValues() {
        return saleApprovalValues;
    }

    public void setSaleApprovalValues(List<SaleApprovalValue> saleApprovalValues) {
        this.saleApprovalValues = saleApprovalValues;
    }

    public PurchaseOrderDAO getPurchaseOrderDAO() {
        return purchaseOrderDAO;
    }

    public void setPurchaseOrderDAO(PurchaseOrderDAO purchaseOrderDAO) {
        this.purchaseOrderDAO = purchaseOrderDAO;
    }
    
    public PurchaseOrderDetailDAO getPurchaseOrderDetailDAO() {
        return purchaseOrderDetailDAO;
    }

    public void setPurchaseOrderDetailDAO(PurchaseOrderDetailDAO purchaseOrderDetailDAO) {
        this.purchaseOrderDetailDAO = purchaseOrderDetailDAO;
    }

    public Integer getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public List<Users> getUserList() {
        return userList;
    }

    public void setUserList(List<Users> userList) {
        this.userList = userList;
    }

    public UsersDAO getUsersDAO() {
        return usersDAO;
    }

    public void setUsersDAO(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public UserGroupDAO getUserGroupDAO() {
        return userGroupDAO;
    }

    public void setUserGroupDAO(UserGroupDAO userGroupDAO) {
        this.userGroupDAO = userGroupDAO;
    }

    public List<UserGroup> getUserGroupList() {
        return userGroupList;
    }

    public void setUserGroupList(List<UserGroup> userGroupList) {
        this.userGroupList = userGroupList;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
        this.search();
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
    
    public String getPolicyNo() {
        return policyNo;
}

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(String confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    public List<PaymentMethod> getPaymentMethodList() {
        return paymentMethodList;
    }

    public void setPaymentMethodList(List<PaymentMethod> paymentMethodList) {
        this.paymentMethodList = paymentMethodList;
    }


    public Integer getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Integer paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public PaymentMethodDAO getPaymentMethodDAO() {
        return paymentMethodDAO;
    }

    public void setPaymentMethodDAO(PaymentMethodDAO paymentMethodDAO) {
        this.paymentMethodDAO = paymentMethodDAO;
    }
    
    public PurchaseOrder getPurchaseOrder(Integer purchaseOrderId) {
       return purchaseOrderDAO.findPurchaseOrder(purchaseOrderId);
    }
    
    public List<PurchaseOrder> getFamilyPurchaseOrder(PurchaseOrder purchaseOrder) {
        if(purchaseOrder.getMainPoId() != null && purchaseOrder.getMainPoId() != 0) {
            return purchaseOrderDAO.findFamilyPurchaseOrder(purchaseOrder.getMainPoId());
        }else{
            return purchaseOrderDAO.findFamilyPurchaseOrder(purchaseOrder.getId());
        }
    }
}
