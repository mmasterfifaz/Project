/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.value.front.customerHandling;

import com.maxelyz.core.model.dao.AssignmentDetailDAO;
import com.maxelyz.core.model.dao.ContactHistoryDAO;
import com.maxelyz.core.model.entity.PurchaseOrder;
import com.maxelyz.core.model.entity.PurchaseOrderDetail;
import com.maxelyz.core.model.entity.PurchaseOrderInstallment;
import com.maxelyz.core.model.entity.PurchaseOrderRegister;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedProperty;

/**
 *
 * @author admin
 */
public class SaleHistoryInfoValue {
    private String refNo;
    private String channel;
    private String insurePerson;
    private String productName;
    private String planName;
    //private String paymentMethod;
    private Double sumInsure;
    private Double totalCollect;
    private Date purchaseDate;
    private Date saleDate;
    private Date updateDate;
    private String tmrName;
    private String status;
    
    private Integer productId;
    private Integer poId;
    private String productThumbnail;
    private String categoryType;
    private Boolean familyProduct;
    private Integer parentPurchaseOrderId;
    private Boolean enableCopy;
    private Integer productPlanId;
    private Integer mainPoId;
    private Integer contactResultPlanId;    
    private Boolean masterPlan;
    
    public SaleHistoryInfoValue(PurchaseOrder po){
        PurchaseOrderDetail pod = ((List<PurchaseOrderDetail>) po.getPurchaseOrderDetailCollection()).get(0);
        List<PurchaseOrderRegister> porList = (List<PurchaseOrderRegister>) pod.getPurchaseOrderRegisterCollection();
        
        this.poId = po.getId();
        this.refNo = po.getRefNo();
        this.channel = "";
        this.insurePerson = this.genInsurePerson(porList);
        this.productName = pod.getProduct().getName();
        this.planName = pod.getProductPlan().getName();
        this.masterPlan = pod.getProductPlan().getMasterPlan();
        this.productPlanId = pod.getProductPlan().getId();
        this.mainPoId = pod.getPurchaseOrder().getMainPoId();
        this.contactResultPlanId = pod.getProduct().getContactResultPlan().getId();
        //this.paymentMethod = //po.getPaymentMethod();
        this.sumInsure = pod.getProductPlan().getSumInsured();
        this.totalCollect = po.getTotalAmount();
        this.purchaseDate = po.getPurchaseDate();
        this.saleDate = po.getSaleDate();
        this.updateDate = po.getUpdateDate();
        this.tmrName = po.getCreateBy();
        this.status = String.valueOf(po.getSaleResult());
        this.productId = pod.getProduct().getId();
        this.productThumbnail = pod.getProduct().getProductThumbnail();
        this.categoryType = pod.getProduct().getProductCategory().getCategoryType();
        this.familyProduct = pod.getProduct().getFamilyProduct();
        this.parentPurchaseOrderId = po.getParentPurchaseOrder() != null ? po.getParentPurchaseOrder().getId() : null;
        if(po.getPurchaseDate() != null && String.valueOf(po.getSaleResult()).equals("Y")){
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String purchaseDateStr = formatter.format(po.getPurchaseDate());
            String nowDateStr = formatter.format(new Date());
            if(purchaseDateStr.equals(nowDateStr)){
                this.enableCopy = true;
            }else{
                this.enableCopy = false;
            }
        }else{
            this.enableCopy = true;
        }
    }
/*
    public SaleHistoryInfoValue(PurchaseOrder po, PurchaseOrderInstallment poi){
        PurchaseOrderDetail pod = ((List<PurchaseOrderDetail>) po.getPurchaseOrderDetailCollection()).get(0);
        List<PurchaseOrderRegister> porList = (List<PurchaseOrderRegister>) pod.getPurchaseOrderRegisterCollection();
        
        this.poId = po.getId();
        this.refNo = po.getRefNo2();
        this.channel = "";
        this.insurePerson = this.genInsurePerson(porList);
        this.productName = pod.getProduct().getName();
        this.planName = pod.getProductPlan().getName();
        if(poi != null && poi.getPaymentMethod() != null)
            this.paymentMethod = poi.getPaymentMethod();
        else
            this.paymentMethod = "";
        this.sumInsure = pod.getProductPlan().getSumInsured();
        this.totalCollect = po.getTotalAmount();
        this.purchaseDate = po.getPurchaseDate();
        this.updateDate = po.getUpdateDate();
        this.tmrName = po.getCreateBy();
        this.status = String.valueOf(po.getSaleResult());
        this.productId = pod.getProduct().getId();
        this.productThumbnail = pod.getProduct().getProductThumbnail();
        this.categoryType = pod.getProduct().getProductCategory().getCategoryType();
    }
*/
    private String genInsurePerson(List<PurchaseOrderRegister> list){
        String str = "";
        for(PurchaseOrderRegister por : list){
            //if(por.getNo() > 1){
                str += por.getName() + " " + por.getSurname() + ",";
            //}
        }
        if(!str.equals("")){
            str = str.substring(0, str.length() - 1);
        }
        return str;    
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getInsurePerson() {
        return insurePerson;
    }

    public void setInsurePerson(String insurePerson) {
        this.insurePerson = insurePerson;
    }
/*
    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
*/
    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getSumInsure() {
        return sumInsure;
    }

    public void setSumInsure(Double sumInsure) {
        this.sumInsure = sumInsure;
    }

    public String getTmrName() {
        return tmrName;
    }

    public void setTmrName(String tmrName) {
        this.tmrName = tmrName;
    }

    public Double getTotalCollect() {
        return totalCollect;
    }

    public void setTotalCollect(Double totalCollect) {
        this.totalCollect = totalCollect;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getPoId() {
        return poId;
    }

    public void setPoId(Integer poId) {
        this.poId = poId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductThumbnail() {
        return productThumbnail;
    }

    public void setProductThumbnail(String productThumbnail) {
        this.productThumbnail = productThumbnail;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public Boolean getFamilyProduct() {
        return familyProduct;
    }

    public void setFamilyProduct(Boolean familyProduct) {
        this.familyProduct = familyProduct;
    }

    public Integer getParentPurchaseOrderId() {
        return parentPurchaseOrderId;
    }

    public void setParentPurchaseOrderId(Integer parentPurchaseOrderId) {
        this.parentPurchaseOrderId = parentPurchaseOrderId;
    }

    public Boolean getEnableCopy() {
        return enableCopy;
    }

    public void setEnableCopy(Boolean enableCopy) {
        this.enableCopy = enableCopy;
    }

    public Integer getProductPlanId() {
        return productPlanId;
    }

    public void setProductPlanId(Integer productPlanId) {
        this.productPlanId = productPlanId;
    }

    public Integer getMainPoId() {
        return mainPoId;
    }

    public void setMainPoId(Integer mainPoId) {
        this.mainPoId = mainPoId;
    }

    public Integer getContactResultPlanId() {
        return contactResultPlanId;
    }

    public void setContactResultPlanId(Integer contactResultPlanId) {
        this.contactResultPlanId = contactResultPlanId;
    }

    public Boolean getMasterPlan() {
        return masterPlan;
    }

    public void setMasterPlan(Boolean masterPlan) {
        this.masterPlan = masterPlan;
    }
    
    
}
