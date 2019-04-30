package com.maxelyz.core.model.value.admin;

import java.util.Date;

public class ReferMarketingCustomerValue {

    private Integer marketingId;
    private Integer customerId;
    private String name;
    private String spotRef;
    private String caseNo;
    private String age;
    private String disposition;
    private String dob;
    private String desc1;
    private String desc2;
    private String desc3;
    private String desc4;
    private String desc5;
    private String marketingName;
    private String campaignName;
    private Date assignDate;

    public ReferMarketingCustomerValue() {

    }
    
    public ReferMarketingCustomerValue(Integer marketingId,Integer customerId,String name,String spotRef,String caseNo,String age,String disposition,
            String dob,String desc1,String desc2,String desc3,String desc4,String desc5,String marketingName,String campaignName,Date assignDate){
        this.marketingId = marketingId;
        this.customerId = customerId;
        this.name = name;
        this.spotRef = spotRef;
        this.caseNo = caseNo;
        this.age = age;
        this.disposition = disposition;
        this.dob = dob;
        this.desc1 = desc1;
        this.desc2 = desc2;
        this.desc3 = desc3;
        this.desc4 = desc4;
        this.desc5 = desc5;
        this.marketingName = marketingName;
        this.campaignName = campaignName;
        this.assignDate = assignDate;
    }

    public Integer getMarketingId() {
        return marketingId;
    }

    public void setMarketingId(Integer marketingId) {
        this.marketingId = marketingId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpotRef() {
        return spotRef;
    }

    public void setSpotRef(String spotRef) {
        this.spotRef = spotRef;
    }

    public String getCaseNo() {
        return caseNo;
    }

    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDisposition() {
        return disposition;
    }

    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDesc1() {
        return desc1;
    }

    public void setDesc1(String desc1) {
        this.desc1 = desc1;
    }

    public String getDesc2() {
        return desc2;
    }

    public void setDesc2(String desc2) {
        this.desc2 = desc2;
    }

    public String getDesc3() {
        return desc3;
    }

    public void setDesc3(String desc3) {
        this.desc3 = desc3;
    }

    public String getDesc4() {
        return desc4;
    }

    public void setDesc4(String desc4) {
        this.desc4 = desc4;
    }

    public String getDesc5() {
        return desc5;
    }

    public void setDesc5(String desc5) {
        this.desc5 = desc5;
    }

    public String getMarketingName() {
        return marketingName;
    }

    public void setMarketingName(String marketingName) {
        this.marketingName = marketingName;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public Date getAssignDate() {
        return assignDate;
    }

    public void setAssignDate(Date assignDate) {
        this.assignDate = assignDate;
    }
    
}
