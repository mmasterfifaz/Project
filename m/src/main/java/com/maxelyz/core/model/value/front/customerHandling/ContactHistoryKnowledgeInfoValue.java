package com.maxelyz.core.model.value.front.customerHandling;

import com.maxelyz.core.model.entity.Knowledgebase;


public class ContactHistoryKnowledgeInfoValue {
    private Knowledgebase knowledgebase;
    private Boolean isUseful;
    private String remark;

    public ContactHistoryKnowledgeInfoValue(Knowledgebase knowledgebase, Boolean isUseful, String remark) {
        this.knowledgebase = knowledgebase;
        this.isUseful = isUseful;
        this.remark = remark;
    }


    public Boolean getIsUseful() {
        return isUseful;
    }

    public void setIsUseful(Boolean isUseful) {
        this.isUseful = isUseful;
    }

    public Knowledgebase getKnowledgebase() {
        return knowledgebase;
    }

    public void setKnowledgebase(Knowledgebase knowledgebase) {
        this.knowledgebase = knowledgebase;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


}
