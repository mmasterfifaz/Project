/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.value.front.campaign;

import com.maxelyz.core.model.entity.AssignmentDetail;
import com.maxelyz.core.model.entity.PurchaseOrder;
import com.ntier.utils.DateUtil;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author Manop
 */
public class CampaignValue {
    private AssignmentDetail assignmentDetail;
    private Integer assignmentDetailId;
    private Integer campaignId;
    private String campaign;
    private String marketingCode;
    private Integer customerId;
    private String customerName;
    private String customerType;
    private String gender;
    private Date dob;
    private Date assignDate;
    private String status;
    private String saleResult;
    private Long age;
    private String iconFlag;
    private Date updateDate;
    private Date followupsaleDate;
    private String contactResult;
    private String remark;
    private String contactRemark;
    private Date contactDate;
    private Collection<PurchaseOrder> purchaseOrderCollection;
    private String customerCurrentAdd2;

    public CampaignValue(){}

    public CampaignValue(Integer assignmentDetailId, String campaign, String marketingCode, Integer customerId, String customerName, String customerType,
            String gender, Date dob, Date assignDate, String status, Date updateDate, Date followupsaleDate, String saleResult, String contactResult, String contactRemark, Date contactDate){
        this.assignmentDetailId = assignmentDetailId;
        this.campaign = campaign;
        this.marketingCode = marketingCode;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerType = customerType;
        this.gender = gender;
        this.dob = dob;
        this.assignDate = assignDate;
        this.status = status;
        this.updateDate = updateDate;
        this.followupsaleDate = followupsaleDate;
        this.saleResult = saleResult;
        this.contactResult = contactResult;
        this.contactRemark = contactRemark;
        this.contactDate = contactDate;

        if(dob != null){
            DateUtil dateUtil = new DateUtil();
            Calendar calendar = Calendar.getInstance(Locale.US);
            calendar.clear();
            calendar.setTime(dob);
            Long calAge = dateUtil.getDateDiffInYear(calendar, Calendar.getInstance(Locale.US));
//            this.age = calAge + 1;
            this.age = calAge;
        }

        if (status.equals("viewed")){
            this.iconFlag = "viewed";
        }else if (status.equals("assigned")){
            this.iconFlag = "assigned";
        }else if (status.equals("closed") && (saleResult != null && saleResult.equals("Y"))){
            this.iconFlag = "YC";
        }else if (status.equals("closed") && (saleResult != null && saleResult.equals("N"))){
            this.iconFlag = "NC";
        }else{
            this.iconFlag = "";
        }
    }

    public CampaignValue(Integer assignmentDetailId, String campaign, String marketingCode, Integer customerId, String customerName, String customerType,
                         String gender, Date dob, Date assignDate, String status, Date updateDate, Date followupsaleDate, String saleResult, String contactResult, String contactRemark, Date contactDate, String customerCurrentAdd2){
        this.assignmentDetailId = assignmentDetailId;
        this.campaign = campaign;
        this.marketingCode = marketingCode;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerType = customerType;
        this.gender = gender;
        this.dob = dob;
        this.assignDate = assignDate;
        this.status = status;
        this.updateDate = updateDate;
        this.followupsaleDate = followupsaleDate;
        this.saleResult = saleResult;
        this.contactResult = contactResult;
        this.contactRemark = contactRemark;
        this.contactDate = contactDate;

        if(dob != null){
            DateUtil dateUtil = new DateUtil();
            Calendar calendar = Calendar.getInstance(Locale.US);
            calendar.clear();
            calendar.setTime(dob);
            Long calAge = dateUtil.getDateDiffInYear(calendar, Calendar.getInstance(Locale.US));
//            this.age = calAge + 1;
            this.age = calAge;
        }

        if (status.equals("viewed")){
            this.iconFlag = "viewed";
        }else if (status.equals("assigned")){
            this.iconFlag = "assigned";
        }else if (status.equals("closed") && (saleResult != null && saleResult.equals("Y"))){
            this.iconFlag = "YC";
        }else if (status.equals("closed") && (saleResult != null && saleResult.equals("N"))){
            this.iconFlag = "NC";
        }else{
            this.iconFlag = "";
        }
        this.customerCurrentAdd2 = customerCurrentAdd2;
    }

    /**
     * @return the campaignId
     */
    public Integer getCampaignId() {
        return campaignId;
    }

    /**
     * @param campaignId the campaignId to set
     */
    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }

    /**
     * @return the campaign
     */
    public String getCampaign() {
        return campaign;
    }

    /**
     * @param campaign the campaign to set
     */
    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    /**
     * @return the marketingCode
     */
    public String getMarketingCode() {
        return marketingCode;
    }

    /**
     * @param marketingCode the marketingCode to set
     */
    public void setMarketingCode(String marketingCode) {
        this.marketingCode = marketingCode;
    }

    /**
     * @return the customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName the customerName to set
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * @return the customerType
     */
    public String getCustomerType() {
        return customerType;
    }

    /**
     * @param customerType the customerType to set
     */
    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    /**
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return the dob
     */
    public Date getDob() {
        return dob;
    }

    /**
     * @param dob the dob to set
     */
    public void setDob(Date dob) {
        this.dob = dob;
    }

    /**
     * @return the assignDate
     */
    public Date getAssignDate() {
        return assignDate;
    }

    /**
     * @param assignDate the assignDate to set
     */
    public void setAssignDate(Date assignDate) {
        this.assignDate = assignDate;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the iconFlag
     */
    public String getIconFlag() {
        return iconFlag;
    }

    /**
     * @param iconFlag the iconFlag to set
     */
    public void setIconFlag(String iconFlag) {
        this.iconFlag = iconFlag;
    }

    /**
     * @return the age
     */
    public Long getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(Long age) {
        this.age = age;
    }

    /**
     * @return the saleResult
     */
    public String getSaleResult() {
        return saleResult;
    }

    /**
     * @param saleResult the saleResult to set
     */
    public void setSaleResult(String saleResult) {
        this.saleResult = saleResult;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getAssignmentDetailId() {
        return assignmentDetailId;
    }

    public void setAssignmentDetailId(Integer assignmentDetailId) {
        this.assignmentDetailId = assignmentDetailId;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public AssignmentDetail getAssignmentDetail() {
        return assignmentDetail;
    }

    public void setAssignmentDetail(AssignmentDetail assignmentDetail) {
        this.assignmentDetail = assignmentDetail;
    }

    public Date getFollowupsaleDate() {
        return followupsaleDate;
    }

    public void setFollowupsaleDate(Date followupsaleDate) {
        this.followupsaleDate = followupsaleDate;
    }

    public Collection<PurchaseOrder> getPurchaseOrderCollection() {
        return purchaseOrderCollection;
    }

    public void setPurchaseOrderCollection(Collection<PurchaseOrder> purchaseOrderCollection) {
        this.purchaseOrderCollection = purchaseOrderCollection;
    }

    public String getContactResult() {
        return contactResult;
    }

    public void setContactResult(String contactResult) {
        this.contactResult = contactResult;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getContactRemark() {
        return contactRemark;
    }

    public void setContactRemark(String contactRemark) {
        this.contactRemark = contactRemark;
    }

    public Date getContactDate() {
        return contactDate;
    }

    public void setContactDate(Date contactDate) {
        this.contactDate = contactDate;
    }
    
    public String getCustomerCurrentAdd2() {
        return customerCurrentAdd2;
    }

    public void setCustomerCurrentAdd2(String customerCurrentAdd2) {
        this.customerCurrentAdd2 = customerCurrentAdd2;
}


}
