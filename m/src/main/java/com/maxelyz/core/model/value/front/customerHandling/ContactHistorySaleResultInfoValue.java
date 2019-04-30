package com.maxelyz.core.model.value.front.customerHandling;

import com.maxelyz.core.model.entity.Knowledgebase;
import com.maxelyz.core.model.entity.Product;
import com.maxelyz.core.model.entity.PurchaseOrder;
import java.util.Date;


public class ContactHistorySaleResultInfoValue {
    private PurchaseOrder purchaseOrder;
    private Product product;
    private Integer nosaleReasonId;
    private Integer followupsaleReasonId;
    private Date followupDate;

    public ContactHistorySaleResultInfoValue(PurchaseOrder purchaseOrder, Product product) {
        this.purchaseOrder = purchaseOrder;
        this.product = product;
        if (purchaseOrder.getNosaleReason()!=null) {
            nosaleReasonId = purchaseOrder.getNosaleReason().getId();
        }
        if (purchaseOrder.getFollowupsaleReason()!=null) {
            followupsaleReasonId = purchaseOrder.getFollowupsaleReason().getId();
        }
        if (purchaseOrder.getFollowupsaleDate() != null) {
            followupDate = purchaseOrder.getFollowupsaleDate();
        }
    }

  

    
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public Integer getNosaleReasonId() {
        return nosaleReasonId;
    }

    public void setNosaleReasonId(Integer nosaleReasonId) {
        this.nosaleReasonId = nosaleReasonId;
    }

    public Integer getFollowupsaleReasonId() {
        return followupsaleReasonId;
    }

    public void setFollowupsaleReasonId(Integer followupsaleReasonId) {
        this.followupsaleReasonId = followupsaleReasonId;
    }

    public Date getFollowupDate() {
        return followupDate;
    }

    public void setFollowupDate(Date followupDate) {
        this.followupDate = followupDate;
    }
}
