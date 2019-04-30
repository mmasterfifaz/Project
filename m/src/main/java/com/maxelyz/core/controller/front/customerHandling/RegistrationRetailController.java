/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.front.customerHandling;

/**
 *
 * @author admin
 */
import com.maxelyz.core.controller.UserSession;
import com.maxelyz.core.model.dao.AssignmentDetailDAO;
import com.maxelyz.core.model.dao.CustomerDAO;
import com.maxelyz.core.model.dao.DistrictDAO;
import com.maxelyz.core.model.dao.FollowupsaleReasonDAO;
import com.maxelyz.core.model.dao.NosaleReasonDAO;
import com.maxelyz.core.model.dao.ProductDAO;
import com.maxelyz.core.model.dao.ProvinceDAO;
import com.maxelyz.core.model.dao.PurchaseOrderDAO;
import com.maxelyz.core.model.dao.PurchaseOrderDetailDAO;
import com.maxelyz.core.model.entity.AssignmentDetail;
import com.maxelyz.core.model.entity.Customer;
import com.maxelyz.core.model.entity.District;
import com.maxelyz.core.model.entity.FollowupsaleReason;
import com.maxelyz.core.model.entity.NosaleReason;
import com.maxelyz.core.model.entity.Product;
import com.maxelyz.core.model.entity.Province;
import com.maxelyz.core.model.entity.PurchaseOrder;
import com.maxelyz.core.model.entity.PurchaseOrderDetail;
import com.maxelyz.utils.JSFUtil;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.bean.ViewScoped;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

@ManagedBean
@RequestScoped
@ViewScoped
public class RegistrationRetailController {

    private static Logger log = Logger.getLogger(RegistrationFrontController.class);
    private String SUCCESS = "/front/customerHandling/saleApproaching.jsf";
    private static String REDIRECT_PAGE = "/front/campaign/campaignAssignment.jsf";

    private Integer billingProvinceId;
    private Integer billingDistrictId;
    private Integer shippingProvinceId;
    private Integer shippingDistrictId;
    private Integer nosaleReasonId;
    private Integer followupsaleReasonId;    

    private UserSession userSession;
    private AssignmentDetail assignmentDetail;
    private PurchaseOrder purchaseOrder;

    private List<Province> provinceList;
    private Collection<District> billingDistrictList;
    private Collection<District> shippingDistrictList;
    private Collection<PurchaseOrderDetail> purchaseOrderDetailList;
    private List<NosaleReason> nosaleReasonList;
    private List<FollowupsaleReason> followupsaleReasonList;

    @ManagedProperty(value = "#{purchaseOrderDAO}")
    private PurchaseOrderDAO purchaseOrderDAO;
    @ManagedProperty(value = "#{purchaseOrderDetailDAO}")
    private PurchaseOrderDetailDAO purchaseOrderDetailDAO;
    @ManagedProperty(value = "#{customerDAO}")
    private CustomerDAO customerDAO;
    @ManagedProperty(value = "#{provinceDAO}")
    private ProvinceDAO provinceDAO;
    @ManagedProperty(value = "#{districtDAO}")
    private DistrictDAO districtDAO;
    @ManagedProperty(value = "#{nosaleReasonDAO}")
    private NosaleReasonDAO nosaleReasonDAO;
    @ManagedProperty(value = "#{followupsaleReasonDAO}")
    private FollowupsaleReasonDAO followupsaleReasonDAO;
    @ManagedProperty(value = "#{productDAO}")
    private ProductDAO productDAO;
    @ManagedProperty(value = "#{assignmentDetailDAO}")
    private AssignmentDetailDAO assignmentDetailDAO;

    @PostConstruct
    public void initialize() {
        try{
            userSession = JSFUtil.getUserSession();
            initAssignmentDetail();
            provinceList = provinceDAO.findProvinceEntities();
            nosaleReasonList = nosaleReasonDAO.findNosaleReasonEntities();
            followupsaleReasonList = followupsaleReasonDAO.findFollowupsaleReasonEntities();

            Integer poId = (Integer) JSFUtil.getRequest().getAttribute("poId");
            purchaseOrder = this.getPurchaseOrderDAO().findPurchaseOrder(poId);
//            tempPurchaseOrderDetail = this.getPurchaseOrderDAO().findTempPurchaseOrderDetailByProduct(tempPoId, productId);
//            product = this.getProductDAO().findProduct(productId);
//            if (tempPurchaseOrder == null || tempPurchaseOrderDetail == null || product == null) {
//                throw new Exception("No Data Found, tempPurchaseOrder or tempPurchaseOrderDetail or product is null");
//            }

            if(purchaseOrder != null){
                purchaseOrderDetailList = purchaseOrderDetailDAO.findByPurchaseOrder(purchaseOrder.getId());
                billingDistrictId = purchaseOrder.getBillingDistrict() != null ? purchaseOrder.getBillingDistrict().getId() : null;
                billingProvinceId = purchaseOrder.getBillingDistrict() != null ? purchaseOrder.getBillingDistrict().getProvinceId().getId() : null;
                billingDistrictList = purchaseOrder.getBillingDistrict() != null ? purchaseOrder.getBillingDistrict().getProvinceId().getDistrictCollection() : null;
                shippingDistrictId = purchaseOrder.getShippingDistrict() != null ? purchaseOrder.getShippingDistrict().getId() : null;
                shippingProvinceId= purchaseOrder.getShippingDistrict() != null ? purchaseOrder.getShippingDistrict().getProvinceId().getId() : null;
                shippingDistrictList = purchaseOrder.getShippingDistrict() != null ? purchaseOrder.getShippingDistrict().getProvinceId().getDistrictCollection() : null;
            }
        } catch (Exception e) {
            log.error(e);
            JSFUtil.redirect(REDIRECT_PAGE);
            return;
        } finally {
            JSFUtil.getRequest().removeAttribute("poId");
        }
    }

    private void initAssignmentDetail() {
        if (StringUtils.isNotEmpty(JSFUtil.getRequestParameterMap("selectedId")) && !StringUtils.equals(JSFUtil.getRequestParameterMap("selectedId"), "0")) {
            Integer assignmentDetailId = Integer.parseInt(JSFUtil.getRequestParameterMap("selectedId"));
            assignmentDetail = assignmentDetailDAO.findAssignmentDetail(assignmentDetailId);
        } else if (userSession.getAssignmentDetail() != null) {
            assignmentDetail = userSession.getAssignmentDetail();
        } else {
            assignmentDetail = purchaseOrder.getAssignmentDetail();
        }
    }

    public void billingProvinceListener(ValueChangeEvent event) {
        billingProvinceId = (Integer) event.getNewValue();
        billingDistrictList = districtDAO.findDistrictByProvinceId(billingProvinceId);
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void shippingProvinceListener(ValueChangeEvent event) {
        shippingProvinceId = (Integer) event.getNewValue();
        shippingDistrictList = districtDAO.findDistrictByProvinceId(shippingProvinceId);
        FacesContext.getCurrentInstance().renderResponse();
    }

    public String save(){

        try{
            purchaseOrder.setBillingDistrict(billingDistrictId != null ? districtDAO.findDistrict(billingDistrictId) : null);
            purchaseOrder.setShippingDistrict(shippingDistrictId != null ? districtDAO.findDistrict(shippingDistrictId) : null);
            purchaseOrder.setNosaleReason(nosaleReasonId != null ? nosaleReasonDAO.findNosaleReason(nosaleReasonId) : null);
            purchaseOrder.setFollowupsaleReason(followupsaleReasonId != null ? followupsaleReasonDAO.findFollowupsaleReason(followupsaleReasonId) : null);
            purchaseOrderDAO.editRetail(purchaseOrder);

        }catch(Exception e){
            log.error(e);
            return "registrationRetail.xhtml";
        }

        return "saleHistory.xhtml?faces-redirect=true";
    }

    public AssignmentDetail getAssignmentDetail() {
        return assignmentDetail;
    }

    public void setAssignmentDetail(AssignmentDetail assignmentDetail) {
        this.assignmentDetail = assignmentDetail;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
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

    public CustomerDAO getCustomerDAO() {
        return customerDAO;
    }

    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public Integer getShippingDistrictId() {
        return shippingDistrictId;
    }

    public void setShippingDistrictId(Integer shippingDistrictId) {
        this.shippingDistrictId = shippingDistrictId;
    }

    public Integer getShippingProvinceId() {
        return shippingProvinceId;
    }

    public void setShippingProvinceId(Integer shippingProvinceId) {
        this.shippingProvinceId = shippingProvinceId;
    }

    public Integer getBillingDistrictId() {
        return billingDistrictId;
    }

    public void setBillingDistrictId(Integer billingDistrictId) {
        this.billingDistrictId = billingDistrictId;
    }

    public Collection<District> getBillingDistrictList() {
        return billingDistrictList;
    }

    public void setBillingDistrictList(Collection<District> billingDistrictList) {
        this.billingDistrictList = billingDistrictList;
    }

    public Integer getBillingProvinceId() {
        return billingProvinceId;
    }

    public void setBillingProvinceId(Integer billingProvinceId) {
        this.billingProvinceId = billingProvinceId;
    }

    public List<Province> getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(List<Province> provinceList) {
        this.provinceList = provinceList;
    }

    public Collection<District> getShippingDistrictList() {
        return shippingDistrictList;
    }

    public void setShippingDistrictList(Collection<District> shippingDistrictList) {
        this.shippingDistrictList = shippingDistrictList;
    }

    public ProvinceDAO getProvinceDAO() {
        return provinceDAO;
    }

    public void setProvinceDAO(ProvinceDAO provinceDAO) {
        this.provinceDAO = provinceDAO;
    }

    public DistrictDAO getDistrictDAO() {
        return districtDAO;
    }

    public void setDistrictDAO(DistrictDAO districtDAO) {
        this.districtDAO = districtDAO;
    }

    public Integer getFollowupsaleReasonId() {
        return followupsaleReasonId;
    }

    public void setFollowupsaleReasonId(Integer followupsaleReasonId) {
        this.followupsaleReasonId = followupsaleReasonId;
    }

    public Integer getNosaleReasonId() {
        return nosaleReasonId;
    }

    public void setNosaleReasonId(Integer nosaleReasonId) {
        this.nosaleReasonId = nosaleReasonId;
    }

    public NosaleReasonDAO getNosaleReasonDAO() {
        return nosaleReasonDAO;
    }

    public void setNosaleReasonDAO(NosaleReasonDAO nosaleReasonDAO) {
        this.nosaleReasonDAO = nosaleReasonDAO;
    }

    public List<FollowupsaleReason> getFollowupsaleReasonList() {
        return followupsaleReasonList;
    }

    public void setFollowupsaleReasonList(List<FollowupsaleReason> followupsaleReasonList) {
        this.followupsaleReasonList = followupsaleReasonList;
    }

    public List<NosaleReason> getNosaleReasonList() {
        return nosaleReasonList;
    }

    public void setNosaleReasonList(List<NosaleReason> nosaleReasonList) {
        this.nosaleReasonList = nosaleReasonList;
    }

    public FollowupsaleReasonDAO getFollowupsaleReasonDAO() {
        return followupsaleReasonDAO;
    }

    public void setFollowupsaleReasonDAO(FollowupsaleReasonDAO followupsaleReasonDAO) {
        this.followupsaleReasonDAO = followupsaleReasonDAO;
    }

    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public AssignmentDetailDAO getAssignmentDetailDAO() {
        return assignmentDetailDAO;
    }

    public void setAssignmentDetailDAO(AssignmentDetailDAO assignmentDetailDAO) {
        this.assignmentDetailDAO = assignmentDetailDAO;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public Collection<PurchaseOrderDetail> getPurchaseOrderDetailList() {
        return purchaseOrderDetailList;
    }

    public void setPurchaseOrderDetailList(Collection<PurchaseOrderDetail> purchaseOrderDetailList) {
        this.purchaseOrderDetailList = purchaseOrderDetailList;
    }

}
