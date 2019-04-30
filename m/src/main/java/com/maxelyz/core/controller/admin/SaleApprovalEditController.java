package com.maxelyz.core.controller.admin;

import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.PurchaseOrderDAO;
import com.maxelyz.core.model.entity.PurchaseOrder;
import com.maxelyz.core.model.entity.PurchaseOrderApprovalLog;
import com.maxelyz.core.model.entity.PurchaseOrderDetail;
import com.maxelyz.utils.JSFUtil;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.bean.ViewScoped;
import com.maxelyz.utils.SecurityUtil;


@ManagedBean
@RequestScoped
@ViewScoped
public class SaleApprovalEditController {

    private static Logger log = Logger.getLogger(SaleApprovalEditController.class);
    private static String REDIRECT_PAGE = "saleapproval.jsf";
    private static String SUCCESS = "saleapproval.xhtml?faces-redirect=true";
    private static String FAILURE = "saleapprovaledit.xhtml";
    private String message;
    private PurchaseOrder purchaseOrder;
    private Integer flowApproval;//1:Payment->QC, 2:QC->Payment
    private String comment;
    private String approvalPaymentStatus = "";
    private String approvalQcStatus = "";
    private boolean showPaymentStatus;
    private boolean showQcStatus;
    private String oldapprovalStatus;
    private String paymentMode;
    private boolean btnSave = false;
    private List<PurchaseOrderApprovalLog> purchaseOrderApprovalLogs;
    @ManagedProperty(value = "#{purchaseOrderDAO}")
    private PurchaseOrderDAO purchaseOrderDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:salesapproval:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        flowApproval = JSFUtil.getApplication().getSaleApproveFlow();
        showPaymentStatus = false;
        showQcStatus = false;
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");
        
        JSFUtil.getRequest().setAttribute("purchaseOrderId", Integer.parseInt(selectedID)); //for registrationformpo
        purchaseOrder = this.getPurchaseOrderDAO().findPurchaseOrder(Integer.parseInt(selectedID));
        List<PurchaseOrderDetail> purchaseOrderDetails = (List<PurchaseOrderDetail>) purchaseOrder.getPurchaseOrderDetailCollection();
        PurchaseOrderDetail pDetail = purchaseOrderDetails.get(0);
        paymentMode = pDetail.getPaymentMode();
        approvalPaymentStatus = purchaseOrder.getPaymentStatus() == null ? "" : purchaseOrder.getPaymentStatus();
        approvalQcStatus = purchaseOrder.getApprovalStatus() == null ? "" : purchaseOrder.getApprovalStatus();
        oldapprovalStatus = purchaseOrder.getApprovalStatus();
        if (purchaseOrder == null) {
            JSFUtil.redirect(REDIRECT_PAGE);
            return;
        }
        purchaseOrderApprovalLogs = (List<PurchaseOrderApprovalLog>) purchaseOrder.getPurchaseOrderApprovalLogCollection();
        checkFlowControll();
    }
                
    private void checkFlowControll(){
        if(flowApproval == 1){//payment --> qc
            flowPayment();
            flowQc();
        }else if(flowApproval == 2){//qc --> payment
            flowQc();
            flowPayment();
        }
    }

    private void flowPayment(){
        //if (JSFUtil.getUserSession().getUsers().getUserGroup().getRole().toLowerCase().indexOf("paymentapprove") != -1) {
        if (SecurityUtil.isPermitted("admin:salesapproval:paymentapproval")) {
            showPaymentStatus = true;
            if(approvalPaymentStatus.equals(JSFUtil.getBundleValue("approvalApprovedValue"))){
                btnSave = false;
            }else{
                btnSave = true;
            }
        }
    }

    private void flowQc(){
        //if (JSFUtil.getUserSession().getUsers().getUserGroup().getRole().toLowerCase().indexOf("saleapprove") != -1) {
        if (SecurityUtil.isPermitted("admin:salesapproval:qcapproval")) {
            showQcStatus = true;
            if(approvalQcStatus.equals(JSFUtil.getBundleValue("approvalApprovedValue"))){
                btnSave = false;
            }else{
                btnSave = true;
            }
        }
    }

    public String saveAction() {
        try {
            Date now = new Date();
            boolean newApproval = false;
            //reload data
            purchaseOrder = this.getPurchaseOrderDAO().findPurchaseOrder(purchaseOrder.getId());
            List<PurchaseOrderDetail> purchaseOrderDetails = (List<PurchaseOrderDetail>) purchaseOrder.getPurchaseOrderDetailCollection();
            PurchaseOrderDetail pDetail = purchaseOrderDetails.get(0);
            paymentMode = pDetail.getPaymentMode();
            //end reload data
            //purchaseOrder.setApproveBy(JSFUtil.getUserSession().getUserName());
            //if (!purchaseOrder.getApprovalStatus().equals(approvalPaymentStatus) && approvalPaymentStatus.equals(JSFUtil.getBundleValue("approvalApprovedValue"))) {
            //    newApproval = true;
            //}
            //purchaseOrder.setApprovalStatus(approvalPaymentStatus);
            //purchaseOrder.setApproveDate(now);
            //purchaseOrder.setApproveByUser(JSFUtil.getUserSession().getUsers());

            if (flowApproval == 1) {
                if (showPaymentStatus && !approvalPaymentStatus.equals("")) {
                    purchaseOrder.setPaymentStatus(approvalPaymentStatus);
                    purchaseOrder.setPaymentDate(now);
                    purchaseOrder.setPaymentBy(JSFUtil.getUserSession().getUserName());
                    purchaseOrder.setPaymentByUser(JSFUtil.getUserSession().getUsers());
                    if (approvalPaymentStatus.equals(JSFUtil.getBundleValue("approvalApprovedValue"))) {
                        if (purchaseOrder.getSaleDate()==null) {
                            purchaseOrder.setSaleDate(now);//oat
                        }
                    }
                }
                if (showQcStatus && !approvalPaymentStatus.equals("")
                        && !approvalQcStatus.equals("")) {
                    purchaseOrder.setApprovalStatus(approvalQcStatus);
                    purchaseOrder.setApproveDate(now);
                    purchaseOrder.setApproveBy(JSFUtil.getUserSession().getUserName());
                    purchaseOrder.setApproveByUser(JSFUtil.getUserSession().getUsers());
                }
            } else if (flowApproval == 2) {
                if (showQcStatus && !approvalQcStatus.equals("")) {
                    purchaseOrder.setApprovalStatus(approvalQcStatus);
                    purchaseOrder.setApproveDate(now);
                    purchaseOrder.setApproveBy(JSFUtil.getUserSession().getUserName());
                    purchaseOrder.setApproveByUser(JSFUtil.getUserSession().getUsers());
                }
                if (showPaymentStatus && !approvalPaymentStatus.equals("")
                        && !approvalQcStatus.equals("")) {
                    purchaseOrder.setPaymentStatus(approvalPaymentStatus);
                    purchaseOrder.setPaymentDate(now);
                    purchaseOrder.setPaymentBy(JSFUtil.getUserSession().getUserName());
                    purchaseOrder.setPaymentByUser(JSFUtil.getUserSession().getUsers());
                    if (approvalPaymentStatus.equals(JSFUtil.getBundleValue("approvalApprovedValue"))) {
                        if (purchaseOrder.getSaleDate()==null) {
                            purchaseOrder.setSaleDate(now);//oat
                        }
                    }
                }
            }
            if(( purchaseOrder.getRefNo() == null || purchaseOrder.getRefNo().equals(""))
                    && approvalQcStatus.equals(JSFUtil.getBundleValue("approvalApprovedValue"))){
                newApproval = true;
            }
         
            if(purchaseOrder.getSaleResult() == 'Y' 
                    && approvalPaymentStatus.equals(JSFUtil.getBundleValue("approvalApprovedValue"))
                    && approvalQcStatus.equals(JSFUtil.getBundleValue("approvalApprovedValue"))){
                //purchaseOrder.setSaleDate(now);
                purchaseOrder.setSettlement(true);
            }

            this.getPurchaseOrderDAO().editApproval(purchaseOrder, paymentMode, newApproval);

            PurchaseOrderApprovalLog purchaseOrderApprovalLog = new PurchaseOrderApprovalLog();
            purchaseOrderApprovalLog.setPurchaseOrder(purchaseOrder);
            if(showPaymentStatus){
                //purchaseOrderApprovalLog.setPaymentStatus(purchaseOrder.getPaymentStatus());
            }
            if(showQcStatus){
                purchaseOrderApprovalLog.setApprovalStatus(purchaseOrder.getApprovalStatus());
            }
            //purchaseOrderApprovalLog.setComment(this.comment);
            purchaseOrderApprovalLog.setCreateBy(JSFUtil.getUserSession().getUserName());
            purchaseOrderApprovalLog.setUsers(JSFUtil.getUserSession().getUsers());
            purchaseOrderApprovalLog.setCreateDate(now);
            this.getPurchaseOrderDAO().createApprovalLog(purchaseOrderApprovalLog);

        } catch (Exception e) {
            log.error(e);
            message = e.getMessage();
            return FAILURE;
        }
        return SUCCESS;
    }

    public String backAction() {
        return SUCCESS;
    }

    //List
    public List<PurchaseOrderApprovalLog> getPurchaseOrderApprovalLogList() {
        return purchaseOrderApprovalLogs;
    }

    public String getOldapprovalStatus() {
        return oldapprovalStatus;
    }
    //Properties

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public String getApprovalPaymentStatus() {
        return approvalPaymentStatus;
    }

    public void setApprovalPaymentStatus(String approvalPaymentStatus) {
        this.approvalPaymentStatus = approvalPaymentStatus;
    }

    public String getApprovalQcStatus() {
        return approvalQcStatus;
    }

    public void setApprovalQcStatus(String approvalQcStatus) {
        this.approvalQcStatus = approvalQcStatus;
    }

    //Managed Properties
    public PurchaseOrderDAO getPurchaseOrderDAO() {
        return purchaseOrderDAO;
    }

    public void setPurchaseOrderDAO(PurchaseOrderDAO purchaseOrderDAO) {
        this.purchaseOrderDAO = purchaseOrderDAO;
    }

    public boolean isShowPaymentStatus() {
        return showPaymentStatus;
    }

    public void setShowPaymentStatus(boolean showPaymentStatus) {
        this.showPaymentStatus = showPaymentStatus;
    }

    public boolean isShowQcStatus() {
        return showQcStatus;
    }

    public void setShowQcStatus(boolean showQcStatus) {
        this.showQcStatus = showQcStatus;
    }

    public Integer getFlowApproval() {
        return flowApproval;
    }

    public void setFlowApproval(Integer flowApproval) {
        this.flowApproval = flowApproval;
    }

    public boolean isBtnSave() {
        return btnSave;
    }

    public void setBtnSave(boolean btnSave) {
        this.btnSave = btnSave;
    }
    
}
