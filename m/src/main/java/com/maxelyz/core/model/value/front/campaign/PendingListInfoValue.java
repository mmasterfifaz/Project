/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.value.front.campaign;

import com.maxelyz.core.model.entity.PurchaseOrder;
import com.maxelyz.core.model.entity.PurchaseOrderDetail;
import com.maxelyz.core.model.entity.PurchaseOrderRegister;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class PendingListInfoValue {
    private Integer id;
    private String refNo;
    private String campaingName;
    private String insurePerson;
    private String customerName;
    private Date purchaseDate;
    private String latestReason;
    private String latestStatus;
    private String remark;
    private String qcStatus;
    private String approvalStatus;
    private String paymentStatus;
    private String createByAgentName;
    private Integer mainPoId;
    private Integer productId;
    private Integer contactResultPlanId;
    
    public PendingListInfoValue(PurchaseOrder po){ 
        this.id = po.getId();
        this.refNo = po.getRefNo();
        this.campaingName = po.getAssignmentDetail().getCampaignName();
        
        PurchaseOrderDetail pod = ((List<PurchaseOrderDetail>) po.getPurchaseOrderDetailCollection()).get(0);
        List<PurchaseOrderRegister> porList = (List<PurchaseOrderRegister>) pod.getPurchaseOrderRegisterCollection();
        this.insurePerson = this.genInsurePerson(porList);
        
        this.customerName = po.getAssignmentDetail().getCustomerName();
        this.qcStatus = po.getQcStatus();
        this.approvalStatus = po.getApprovalStatus();
        this.paymentStatus = po.getPaymentStatus();
        this.purchaseDate = po.getPurchaseDate();
        this.remark = po.getRemark();
        this.latestReason = po.getLatestReason();
        this.latestStatus = po.getLatestDelegateToRole();
        this.createByAgentName = po.getCreateBy();
        this.mainPoId =po.getMainPoId();
        this.productId =pod.getProduct().getId();
        this.contactResultPlanId = pod.getProduct().getContactResultPlan().getId();
    }
    
    private String genInsurePerson(List<PurchaseOrderRegister> list){
        String str = "";
        Integer i = 0;
        for(PurchaseOrderRegister por : list){
            i++;
            //if(i > 1){
                str += por.getName() + " " + por.getSurname() + ",";
            //}
        }
        if(!str.equals("")){
            str = str.substring(0, str.length() - 1);
        }
        return str;    
    }
        
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getCampaingName() {
        return campaingName;
    }

    public void setCampaingName(String campaingName) {
        this.campaingName = campaingName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getInsurePerson() {
        return insurePerson;
    }

    public void setInsurePerson(String insurePerson) {
        this.insurePerson = insurePerson;
    }

    public String getLatestReason() {
        return latestReason;
    }

    public void setLatestReason(String latestReason) {
        this.latestReason = latestReason;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getQcStatus() {
        return qcStatus;
    }

    public void setQcStatus(String qcStatus) {
        this.qcStatus = qcStatus;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLatestStatus() {
        return latestStatus;
    }

    public void setLatestStatus(String latestStatus) {
        this.latestStatus = latestStatus;
    }

    public String getCreateByAgentName() {
        return createByAgentName;
    }

    public void setCreateByAgentName(String createByAgentName) {
        this.createByAgentName = createByAgentName;
    }

    public Integer getMainPoId() {
        return mainPoId;
    }

    public void setMainPoId(Integer mainPoId) {
        this.mainPoId = mainPoId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getContactResultPlanId() {
        return contactResultPlanId;
    }

    public void setContactResultPlanId(Integer contactResultPlanId) {
        this.contactResultPlanId = contactResultPlanId;
    }
    

    
    
   
    
    
            
}
