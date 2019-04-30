/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.front.customerHandling;

import com.maxelyz.core.controller.UserSession;
import com.maxelyz.core.model.dao.AssignmentDetailDAO;
import com.maxelyz.core.model.dao.CustomerDAO;
import com.maxelyz.core.model.dao.ProductDAO;
import com.maxelyz.core.model.dao.PurchaseOrderDAO;
import com.maxelyz.core.model.dao.PurchaseOrderDetailDAO;
import com.maxelyz.core.model.entity.AssignmentDetail;
import com.maxelyz.core.model.entity.Customer;
import com.maxelyz.core.model.entity.Product;
import com.maxelyz.core.model.entity.PurchaseOrder;
import com.maxelyz.core.model.entity.PurchaseOrderDetail;
import com.maxelyz.core.model.entity.PurchaseOrderQuestionaire;
import com.maxelyz.core.model.entity.PurchaseOrderRegister;
import com.maxelyz.core.model.entity.TempPurchaseOrder;
import com.maxelyz.core.model.entity.TempPurchaseOrderDetail;
import com.maxelyz.core.model.entity.TempPurchaseOrderQuestionaire;
import com.maxelyz.core.model.entity.TempPurchaseOrderRegister;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author admin
 */
@ManagedBean
@RequestScoped
@ViewScoped
public class ShoppingCartController {

    private static Logger log = Logger.getLogger(ShoppingCartController.class);
    private String SALE_APPROACHING = "/front/customerHandling/saleApproaching.xhmtl?faces-redirect=true";
    private String REGISTRATION_FORM = "/front/customerHandling/registrationpo.xhmtl";
    private String REGISTRATION_RETAIL_FORM = "/front/customerHandling/registrationRetail.xhmtl";
    private String FOLLOWUP = "followup";
    private boolean btnCheckOut;
    private Integer tpoId;

    private UserSession userSession;
    private TempPurchaseOrderDetail selectedValue;
//    private Set<Integer> rowsToUpdate;
    private TempPurchaseOrder tempPurchaseOrder;
    private List<TempPurchaseOrder> tempPurchaseOrderList;
    private List<TempPurchaseOrderDetail> tempPurchaseOrderDetailList;
    private String categoryType = "";

    @ManagedProperty(value = "#{customerDAO}")
    private CustomerDAO customerDAO;
    @ManagedProperty(value = "#{purchaseOrderDAO}")
    private PurchaseOrderDAO purchaseOrderDAO;
    @ManagedProperty(value = "#{purchaseOrderDetailDAO}")
    private PurchaseOrderDetailDAO purchaseOrderDetailDAO;
    @ManagedProperty(value = "#{assignmentDetailDAO}")
    private AssignmentDetailDAO assignmentDetailDAO;
    @ManagedProperty(value = "#{productDAO}")
    private ProductDAO productDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("shoppingcart:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        userSession = JSFUtil.getUserSession();
        tempPurchaseOrderDetailList = new ArrayList<TempPurchaseOrderDetail>();
        if (userSession.getCustomerDetail().getId() != null && userSession.getCustomerDetail().getId() != 0) {
            Customer customer = customerDAO.findCustomer(userSession.getCustomerDetail().getId());
            tempPurchaseOrderList = purchaseOrderDAO.findTempByCustomer(customer);
        }
        for(TempPurchaseOrder tpo : tempPurchaseOrderList){
            Collection<TempPurchaseOrderDetail> tpodList = purchaseOrderDAO.findTpodByPoId(tpo.getId());
            for(TempPurchaseOrderDetail tpod : tpodList){
                tempPurchaseOrderDetailList.add(tpod);
                if(categoryType == null || categoryType.equals("")){
                    categoryType = tpod.getProduct().getProductCategory().getCategoryType();
                }
            }
        }
        if(tempPurchaseOrderDetailList != null && !tempPurchaseOrderDetailList.isEmpty()){
            btnCheckOut = true;
        }else{
            btnCheckOut = false;
        }
//        if (tempPurchaseOrderList != null) {
//            tempPurchaseOrderDetailList = purchaseOrderDetailDAO.findByTempPurchaseOrder(tempPurchaseOrder);
//        }
//        rowsToUpdate = new HashSet<Integer>();
    }

    public void removeItem(ActionEvent event) {
        try {
            Integer id = Integer.parseInt(StringUtils.trim(JSFUtil.getRequestParameterMap("selectedId")));
            TempPurchaseOrderDetail tpod = purchaseOrderDetailDAO.findTempPurchaseOrderDetail(id);
            TempPurchaseOrder tpo = tpod.getTempPurchaseOrder();
            if(tpo.getTempPurchaseOrderDetailCollection().size() == 1){
                purchaseOrderDAO.destroyTemp(tpo.getId());
            }else{
                purchaseOrderDetailDAO.deleteTpod(id);
            }

            initialize();
        } catch (Exception e) {
            log.error(e);
        }
//        FacesContext.getCurrentInstance().renderResponse();
    }

    public void updateTpod(ActionEvent event) {
        try {
            if (selectedValue != null) {
                Double unitPrice = Double.valueOf(selectedValue.getProduct().getPrice());
                Integer quantity = selectedValue.getQuantity();
                Double amount = Double.valueOf(unitPrice * quantity);
                purchaseOrderDetailDAO.saveTpod(selectedValue.getId(), unitPrice, quantity, amount);
            }
            initialize();
        } catch (Exception e) {
            log.error(e);
        }
    }

    public String checkOut() {
        userSession = JSFUtil.getUserSession();
        String status = "closed";
        Date flDate = null;
        if (userSession.getCustomerDetail().getId() != null && userSession.getCustomerDetail().getId() != 0) {
            Customer customer = customerDAO.findCustomer(userSession.getCustomerDetail().getId());
            tempPurchaseOrderList = purchaseOrderDAO.findTempByCustomer(customer);
        }
        for (TempPurchaseOrder tpo : tempPurchaseOrderList) {
            if(String.valueOf(tpo.getSaleResult()).equals("F")){
                status = FOLLOWUP;
                flDate = tpo.getFollowupsaleDate();
            }
            boolean b = this.updatePurchaseOrder(tpo);
            if (b) {
                this.clearTempPurchaseOrder(tpo);// clear TempPurchaseOrder and TempPurchaseOrderDetail

            }
        }
        updateAssignmentDetail(status, flDate);
//        if (userSession.getCustomerDetail().getId() != null && userSession.getCustomerDetail().getId() != 0) {
//            Customer customer = customerDAO.findCustomer(userSession.getCustomerDetail().getId());
//            tempPurchaseOrderList = purchaseOrderDAO.findTempByCustomer(customer);
//        }
        if(categoryType.equals("retail")){
            return REGISTRATION_RETAIL_FORM;
        }else{
            JSFUtil.getRequest().setAttribute("page", "shoppingCart");
            return REGISTRATION_FORM;
        }

//        String categoryType = null;
//        Product product = null;
//        for(TempPurchaseOrderDetail tpod : tempPurchaseOrderDetailList){
//            categoryType = tpod.getProduct().getProductCategory().getCategoryType();
//            product = tpod.getProduct();
//            break;
//        }
//        if (categoryType.equals("insurance")) {
//            JSFUtil.getRequest().setAttribute("tempPurchaseOrderId", tempPurchaseOrder.getId());
//            JSFUtil.getRequest().setAttribute("productId", product.getId());
//            return REGISTRATION_FORM;
//        } else {
//            return REGISTRATION_RETAIL_FORM;
//        }
    }

    private void updateAssignmentDetail(String status, Date flDate) {
        AssignmentDetail assignmentDetail = null;
        try {
            if (userSession.getAssignmentDetail() != null) {
                assignmentDetail = userSession.getAssignmentDetail();
            } else {
                assignmentDetail = tempPurchaseOrder.getAssignmentDetail();
            }

            if (assignmentDetail != null) {
                assignmentDetail.setStatus(status);
                assignmentDetail.setFollowupsaleDate(flDate);
                assignmentDetail.setUpdateDate(new Date());
                assignmentDetailDAO.edit(assignmentDetail);
            }
            JSFUtil.getUserSession().setAssignmentDetail(assignmentDetail);
        } catch (Exception e) {
            log.error(e);
        }
    }

    private boolean updatePurchaseOrder(TempPurchaseOrder tpo) {
        try {

            if (tpo != null) {

                PurchaseOrder po = new PurchaseOrder();
                
                po.setRefNo(tpo.getRefNo());
                po.setCustomer(tpo.getCustomer());
                po.setAssignmentDetail(tpo.getAssignmentDetail());
                po.setPurchaseDate(new Date());
                po.setTotalAmount(tpo.getTotalAmount());
                po.setChannelType(tpo.getChannelType());
                po.setSaleResult(tpo.getSaleResult());
                po.setSaleDate(tpo.getSaleDate());
                po.setNosaleReason(tpo.getNosaleReason());
                po.setFollowupsaleDate(tpo.getFollowupsaleDate());
                po.setFollowupsaleReason(tpo.getFollowupsaleReason());
                po.setBillingFullname(tpo.getBillingFullname());
                po.setBillingAddressLine1(tpo.getBillingAddressLine1());
                po.setBillingAddressLine2(tpo.getBillingAddressLine2());
                po.setBillingDistrict(tpo.getBillingDistrict());
                po.setBillingPostalCode(tpo.getBillingPostalCode());
                po.setBillingPhone(tpo.getBillingPhone());
                po.setShippingFullname(tpo.getShippingFullname());
                po.setShippingAddressLine1(tpo.getShippingAddressLine1());
                po.setShippingAddressLine2(tpo.getShippingAddressLine2());
                po.setShippingDistrict(tpo.getShippingDistrict());
                po.setShippingPostalCode(tpo.getShippingPostalCode());
                po.setShippingPhone(tpo.getShippingPhone());
                po.setPaymentMethod(tpo.getPaymentMethod());
                po.setPaymentType(tpo.getPaymentType());
                po.setCardType(tpo.getCardType());
                po.setCardHolderName(tpo.getCardHolderName());
                po.setCardNo(tpo.getCardNo());
                po.setCardExpiryMonth(tpo.getCardExpiryMonth());
                po.setCardExpiryYear(tpo.getCardExpiryYear());
                po.setPaymentAmount(tpo.getPaymentAmount());
                po.setTraceNo(tpo.getTraceNo());
                po.setApprovalStatus(tpo.getApprovalStatus());
                po.setApproveDate(tpo.getApproveDate());
                po.setApproveBy(tpo.getApproveBy());
                po.setApproveByUser(tpo.getApproveByUser());
                po.setQcStatus(tpo.getQcStatus());
                po.setQcDate(tpo.getQcDate());
                po.setQcBy(tpo.getQcBy());
                po.setQcByUser(tpo.getQcByUser());
                po.setSettlement(tpo.getSettlement());
                po.setSettlementDate(tpo.getSettlementDate());
                po.setSettlementBy(tpo.getSettlementBy());
                po.setCreateDate(tpo.getCreateDate());
                po.setCreateBy(tpo.getCreateBy());
                po.setCreateByUser(tpo.getCreateByUser());
                po.setUpdateDate(new Date());
                po.setUpdateBy(userSession.getUserName());
                po.setUpdateByUser(userSession.getUsers());
                po.setFx1(tpo.getFx1());
                po.setFx2(tpo.getFx2());
                po.setFx3(tpo.getFx3());
                po.setFx4(tpo.getFx4());
                po.setFx5(tpo.getFx5());
                po.setFx6(tpo.getFx6());
                po.setFx7(tpo.getFx7());
                po.setFx8(tpo.getFx8());
                po.setFx9(tpo.getFx9());
                po.setFx10(tpo.getFx10());
                po.setFx11(tpo.getFx11());
                po.setFx12(tpo.getFx12());
                po.setFx13(tpo.getFx13());
                po.setFx14(tpo.getFx14());
                po.setFx15(tpo.getFx15());

                PurchaseOrderDetail pod = null;
                Collection<PurchaseOrderDetail> podList = new ArrayList<PurchaseOrderDetail>();
                Collection<TempPurchaseOrderDetail> tpodList = tpo.getTempPurchaseOrderDetailCollection();
                for (TempPurchaseOrderDetail tpod : tpodList) {
                    pod = this.setPurchaseOrderDetail(po, tpod);
                    podList.add(pod);
                }

                po.setPurchaseOrderDetailCollection(podList);

                purchaseOrderDAO.create(po);

                if(po.getId() != null && po.getId() != 0){
                    JSFUtil.getRequest().setAttribute("poId", po.getId());
                }

                JSFUtil.getUserSession().setPurchaseOrders(po); //set po to usersession
            }
        } catch (Exception e) {
            log.error(e);
            return false;
        }
        return true;
    }

    private PurchaseOrderDetail setPurchaseOrderDetail(PurchaseOrder po, TempPurchaseOrderDetail tpod){
        PurchaseOrderDetail pod = new PurchaseOrderDetail();
//        pod.setId(tpod.getId());
        pod.setPurchaseOrder(po);
        //pod.setAssignmentDetail(assignmentDetailDAO.findAssignmentDetail(tpod.getAssignmentDetailId()));
        pod.setProduct(tpod.getProduct());
        pod.setProductPlan(tpod.getProductPlan());
        pod.setPaymentMode(tpod.getPaymentMode());
        pod.setDeliveryMethod(tpod.getDeliveryMethod());
        pod.setUnitPrice(tpod.getUnitPrice());
        pod.setQuantity(tpod.getQuantity());
        pod.setAmount(tpod.getAmount());
        pod.setCreateDate(new Date());

        PurchaseOrderRegister por = null;
        Collection<TempPurchaseOrderRegister> tporList = tpod.getTempPurchaseOrderRegisterCollection();
        Collection<PurchaseOrderRegister> porList = new ArrayList<PurchaseOrderRegister>();
        for(TempPurchaseOrderRegister tpor : tporList){
            por = this.setPurchaseOrderRegister(pod, tpor);
            porList.add(por);
        }

        PurchaseOrderQuestionaire poq = null;
        Collection<TempPurchaseOrderQuestionaire> tpoqList = tpod.getTempPurchaseOrderQuestionaireCollection();
        Collection<PurchaseOrderQuestionaire> poqList = new ArrayList<PurchaseOrderQuestionaire>();
        for(TempPurchaseOrderQuestionaire tpoq : tpoqList){
            poq = this.setPurchaseOrderQuestionaire(pod, tpoq);
            poqList.add(poq);
        }

        pod.setPurchaseOrderRegisterCollection(porList);
        pod.setPurchaseOrderQuestionaireCollection(poqList);

        return pod;
    }

    private PurchaseOrderRegister setPurchaseOrderRegister(PurchaseOrderDetail pod, TempPurchaseOrderRegister tpor){
        PurchaseOrderRegister por = new PurchaseOrderRegister();
//        por.setId(tpor.getId());
        por.setPurchaseOrderDetail(pod);
        por.setNo(tpor.getNo());
        por.setInitial(tpor.getInitial());
        por.setName(tpor.getName());
        por.setSurname(tpor.getSurname());
        por.setIdcardTypeId(tpor.getIdcardTypeId());
        por.setIdno(tpor.getIdno());
        por.setGender(tpor.getGender());
        por.setDob(tpor.getDob());
        por.setWeight(tpor.getWeight());
        por.setHeight(tpor.getHeight());
        por.setMaritalStatus(tpor.getMaritalStatus());
        por.setNationality(tpor.getNationality());
        por.setOccupation(tpor.getOccupation());
        por.setJobDescription(tpor.getJobDescription());
        por.setHomePhone(tpor.getHomePhone());
        por.setOfficePhone(tpor.getOfficePhone());
        por.setMobilePhone(tpor.getMobilePhone());
        por.setEmail(tpor.getEmail());
        por.setCurrentFullname(tpor.getCurrentFullname());
        por.setCurrentAddressLine1(tpor.getCurrentAddressLine1());
        por.setCurrentAddressLine2(tpor.getCurrentAddressLine2());
        por.setCurrentDistrict(tpor.getCurrentDistrict());
        por.setCurrentPostalCode(tpor.getCurrentPostalCode());
        por.setHomeFullname(tpor.getHomeFullname());
        por.setHomeAddressLine1(tpor.getHomeAddressLine1());
        por.setHomeAddressLine2(tpor.getHomeAddressLine2());
        por.setHomeDistrict(tpor.getHomeDistrict());
        por.setHomePostalCode(tpor.getHomePostalCode());
        por.setBillingFullname(tpor.getBillingFullname());
        por.setBillingAddressLine1(tpor.getBillingAddressLine1());
        por.setBillingAddressLine2(tpor.getBillingAddressLine2());
        por.setBillingDistrict(tpor.getBillingDistrict());
        por.setBillingPostalCode(tpor.getBillingPostalCode());
        por.setShippingFullname(tpor.getShippingFullname());
        por.setShippingAddressLine1(tpor.getShippingAddressLine1());
        por.setShippingAddressLine2(tpor.getShippingAddressLine2());
        por.setShippingDistrict(tpor.getShippingDistrict());
        por.setShippingPostalCode(tpor.getShippingPostalCode());
        por.setFx1(tpor.getFx1());
        por.setFx2(tpor.getFx2());
        por.setFx3(tpor.getFx3());
        por.setFx4(tpor.getFx4());
        por.setFx5(tpor.getFx5());
        por.setFx6(tpor.getFx6());
        por.setFx7(tpor.getFx7());
        por.setFx8(tpor.getFx8());
        por.setFx9(tpor.getFx9());
        por.setFx10(tpor.getFx10());
        por.setFx11(tpor.getFx11());
        por.setFx12(tpor.getFx12());
        por.setFx13(tpor.getFx13());
        por.setFx14(tpor.getFx14());
        por.setFx15(tpor.getFx15());
        por.setFx16(tpor.getFx16());
        por.setFx17(tpor.getFx17());
        por.setFx18(tpor.getFx18());
        por.setFx19(tpor.getFx19());
        por.setFx20(tpor.getFx20());

        return por;
    }

    private PurchaseOrderQuestionaire setPurchaseOrderQuestionaire(PurchaseOrderDetail pod, TempPurchaseOrderQuestionaire tpoq){
        PurchaseOrderQuestionaire poq = new PurchaseOrderQuestionaire();
//        poq.setId(null);
        poq.setPurchaseOrderDetail(pod);
        poq.setSeqNo(tpoq.getSeqNo());
        poq.setQuestion(tpoq.getQuestion());
        poq.setAnswerChoice(tpoq.getAnswerChoice());
        poq.setAnswer(tpoq.getAnswer());
        poq.setDescription(tpoq.getDescription());

        return poq;
    }

    private void clearTempPurchaseOrder(TempPurchaseOrder tpo) {
        try {
            if (tpo != null) {
                purchaseOrderDAO.destroyTemp(tpo.getId());
            }
        } catch (Exception e) {
            log.error(e);
        }
    }

    public String toRegistrationForm(){
        Integer tempPoId = Integer.valueOf(JSFUtil.getRequestParameterMap("tempPurchaseOrderId"));
        Integer productId = Integer.valueOf(JSFUtil.getRequestParameterMap("productId"));
        JSFUtil.getRequest().setAttribute("tempPurchaseOrderId", tempPoId);
        JSFUtil.getRequest().setAttribute("productId", productId);

        Product product = productDAO.findProduct(productId);
        String categoryType = product.getProductCategory().getCategoryType();
        if(categoryType.equals("insurance")){
            return REGISTRATION_FORM;
        }else if(categoryType.equals("retail")){
            return REGISTRATION_RETAIL_FORM;
        }else {
            return SALE_APPROACHING;
        }
    }

    public TempPurchaseOrder getTempPurchaseOrder() {
        return tempPurchaseOrder;
    }

    public void setTempPurchaseOrder(TempPurchaseOrder tempPurchaseOrder) {
        this.tempPurchaseOrder = tempPurchaseOrder;
    }

    public PurchaseOrderDAO getPurchaseOrderDAO() {
        return purchaseOrderDAO;
    }

    public void setPurchaseOrderDAO(PurchaseOrderDAO purchaseOrderDAO) {
        this.purchaseOrderDAO = purchaseOrderDAO;
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

    public List<TempPurchaseOrderDetail> getTempPurchaseOrderDetailList() {
        return tempPurchaseOrderDetailList;
    }

    public void setTempPurchaseOrderDetailList(List<TempPurchaseOrderDetail> tempPurchaseOrderDetailList) {
        this.tempPurchaseOrderDetailList = tempPurchaseOrderDetailList;
    }

    public PurchaseOrderDetailDAO getPurchaseOrderDetailDAO() {
        return purchaseOrderDetailDAO;
    }

    public void setPurchaseOrderDetailDAO(PurchaseOrderDetailDAO purchaseOrderDetailDAO) {
        this.purchaseOrderDetailDAO = purchaseOrderDetailDAO;
    }

    public TempPurchaseOrderDetail getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(TempPurchaseOrderDetail selectedValue) {
        this.selectedValue = selectedValue;
    }

    public List<TempPurchaseOrder> getTempPurchaseOrderList() {
        return tempPurchaseOrderList;
    }

    public void setTempPurchaseOrderList(List<TempPurchaseOrder> tempPurchaseOrderList) {
        this.tempPurchaseOrderList = tempPurchaseOrderList;
    }

    public AssignmentDetailDAO getAssignmentDetailDAO() {
        return assignmentDetailDAO;
    }

    public void setAssignmentDetailDAO(AssignmentDetailDAO assignmentDetailDAO) {
        this.assignmentDetailDAO = assignmentDetailDAO;
    }

    public boolean isBtnCheckOut() {
        return btnCheckOut;
    }

    public void setBtnCheckOut(boolean btnCheckOut) {
        this.btnCheckOut = btnCheckOut;
    }

    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public Integer getTpoId() {
        return tpoId;
    }

    public void setTpoId(Integer tpoId) {
        this.tpoId = tpoId;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }
    
}
