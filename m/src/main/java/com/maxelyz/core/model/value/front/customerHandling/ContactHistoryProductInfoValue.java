package com.maxelyz.core.model.value.front.customerHandling;

import com.maxelyz.core.model.entity.Knowledgebase;
import com.maxelyz.core.model.entity.Product;


public class ContactHistoryProductInfoValue {
    private Product product;
    private Boolean isUseful;
    private String remark;

    public ContactHistoryProductInfoValue(Product product, Boolean isUseful, String remark) {
        this.product = product;
        this.isUseful = isUseful;
        this.remark = remark;
    }

    
    public Boolean getIsUseful() {
        return isUseful;
    }

    public void setIsUseful(Boolean isUseful) {
        this.isUseful = isUseful;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


}
