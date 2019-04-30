/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.value.admin;

import com.maxelyz.core.model.entity.PurchaseOrder;
import com.maxelyz.core.model.entity.PurchaseOrderApprovalLog;
import com.maxelyz.utils.JSFUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class SaleApprovalValue {
    private Integer id;
    private String refNo;
    private String refNo2;
    private Integer customerId;
    private String customerName;
    private String campaignName;
    private Date purchaseDate;
    private Date saleDate;
    private char saleResult;
    private String tmrName;
    private String updateBy;
    private String paymentStatus;
    private String approvalStatus;
    private String qcStatus;
    private String uwStatus;
    private String productName;
    private String productPlanName;
    private String approveBy;
    private String qcBy;
    private String paymentBy;
    private String insurePerson;
    private String policyNo;
    private Date issueDate;
    private String reason;
    private String reasonCode;
    private String approvalSupStatus;
    private String approveSupBy;
    private boolean confirmFlag;
    private String latestStatus;
    private Date latestStatusDate;
    private String latestStatusBy;
    private String paymentMethodName;
    private Integer mainPoId;
    private boolean familyProduct;
    private String relationMainInsured;
    private String poFx2;

    public SaleApprovalValue(Integer id
            , String refNo
            , Integer customerId
            , String customerName
            , Date saleDate
            , char saleResult
            , String tmrName
            , String updateBy
            , String approvalStatus) {
        this.id = id;
        this.refNo = refNo;
        this.customerId = customerId;
        this.customerName = customerName;
        this.saleDate = saleDate;
        this.saleResult = saleResult;
        this.tmrName = tmrName;
        this.updateBy = updateBy;
        this.approvalStatus = approvalStatus;
    }

    //10
    public SaleApprovalValue(Integer id
            , String refNo
            , Integer customerId
            , String customerName
            , Date saleDate
            , char saleResult
            , String tmrName
            , String updateBy
            , String approvalStatus
            , String paymentStatus
    ) {
        this.id = id;
        this.refNo = refNo;
        this.customerId = customerId;
        this.customerName = customerName;
        this.saleDate = saleDate;
        this.saleResult = saleResult;
        this.tmrName = tmrName;
        this.updateBy = updateBy;
        this.approvalStatus = approvalStatus;
        this.paymentStatus = paymentStatus;
    }

    //11
    public SaleApprovalValue(Integer id
            , String refNo
            , Integer customerId
            , String customerName
            , Date saleDate
            , char saleResult
            , String tmrName
            , String updateBy
            , String approvalStatus
            , String paymentStatus
            , Integer mainPoId
    ) {
        this.id = id;
        this.refNo = refNo;
        this.customerId = customerId;
        this.customerName = customerName;
        this.saleDate = saleDate;
        this.saleResult = saleResult;
        this.tmrName = tmrName;
        this.updateBy = updateBy;
        this.approvalStatus = approvalStatus;
        this.paymentStatus = paymentStatus;
        this.mainPoId = mainPoId;
    }

    public SaleApprovalValue(Integer id
            , String refNo
            , String refNo2
            , Integer customerId
            , String customerName
            , String campaignName
            , Date purchaseDate
            , String tmrName
            , String uwStatus
            , String paymentStatus
            , String qcStatus
            , String updateBy) {
        this.id = id;
        this.refNo = refNo;
        this.refNo2 = refNo2;
        this.customerId = customerId;
        this.customerName = customerName;
        this.campaignName = campaignName;
        this.purchaseDate = purchaseDate;
        this.tmrName = tmrName;
        this.uwStatus = uwStatus;
        this.paymentStatus = paymentStatus;
        this.qcStatus = qcStatus;
        this.updateBy = updateBy;
    }

    //16
    public SaleApprovalValue(Integer id
            , String refNo
            , String refNo2
            , Integer customerId
            , String customerName
            , String campaignName
            , Date purchaseDate
            , String tmrName
            , String uwStatus
            , String paymentStatus
            , String qcStatus
            , String updateBy
            , String productName
            , String approveBy
            , String qcBy
            , String paymentBy
    ) {
        this.id = id;
        this.refNo = refNo;
        this.refNo2 = refNo2;
        this.customerId = customerId;
        this.customerName = customerName;
        this.campaignName = campaignName;
        this.purchaseDate = purchaseDate;
        this.tmrName = tmrName;
        this.uwStatus = uwStatus;
        this.paymentStatus = paymentStatus;
        this.qcStatus = qcStatus;
        this.updateBy = updateBy;
        this.productName = productName;
        this.approveBy = approveBy;
        this.qcBy = qcBy;
        this.paymentBy = paymentBy;
    }

    //18
    public SaleApprovalValue(Integer id
            , String refNo
            , String refNo2
            , Integer customerId
            , String customerName
            , String campaignName
            , Date purchaseDate
            , String tmrName
            , String uwStatus
            , String paymentStatus
            , String qcStatus
            , String updateBy
            , String productName
            , String approveBy
            , String qcBy
            , String paymentBy
            , Integer mainPoId
            , boolean familyProduct
    ) {
        this.id = id;
        this.refNo = refNo;
        this.refNo2 = refNo2;
        this.customerId = customerId;
        this.customerName = customerName;
        this.campaignName = campaignName;
        this.purchaseDate = purchaseDate;
        this.tmrName = tmrName;
        this.uwStatus = uwStatus;
        this.paymentStatus = paymentStatus;
        this.qcStatus = qcStatus;
        this.updateBy = updateBy;
        this.productName = productName;
        this.approveBy = approveBy;
        this.qcBy = qcBy;
        this.paymentBy = paymentBy;
        this.mainPoId = mainPoId;
        this.familyProduct = familyProduct;
    }

    //17
    public SaleApprovalValue(Integer id
            , String refNo
            , String refNo2
            , Integer customerId
            , String customerName
            , String campaignName
            , Date purchaseDate
            , String tmrName
            , String uwStatus
            , String paymentStatus
            , String qcStatus
            , String updateBy
            , String productName
            , String approveBy
            , String qcBy
            , String paymentBy
                             //  , PurchaseOrder po
            , String insurePerson) {
        this.id = id;
        this.refNo = refNo;
        this.refNo2 = refNo2;
        this.customerId = customerId;
        this.customerName = customerName;
        this.campaignName = campaignName;
        this.purchaseDate = purchaseDate;
        this.tmrName = tmrName;
        this.uwStatus = uwStatus;
        this.paymentStatus = paymentStatus;
        this.qcStatus = qcStatus;
        this.updateBy = updateBy;
        this.productName = productName;
        this.approveBy = approveBy;
        this.qcBy = qcBy;
        this.paymentBy = paymentBy;
        this.insurePerson = insurePerson;
//        PurchaseOrderDetail pod = ((List<PurchaseOrderDetail>) po.getPurchaseOrderDetailCollection()).get(0);
//        List<PurchaseOrderRegister> porList = (List<PurchaseOrderRegister>) pod.getPurchaseOrderRegisterCollection();
//        this.insurePerson = this.genInsurePerson(porList);
    }

    //26 fields
    public SaleApprovalValue(Integer id
            , String refNo
            , String refNo2
            , Integer customerId
            , String customerName
            , String campaignName
            , Date purchaseDate
            , String tmrName
            , String uwStatus
            , String paymentStatus
            , String qcStatus
            , String updateBy
            , String productName
            , String productPlanName
            , String approveBy
            , String qcBy
            , String paymentBy
            , String insurePerson
            , String policyNo
            , Date issueDate
            , String reason
            , String reasonCode
            , String approvalSupStatus
            , String approveSupBy
            , boolean confirmFlag
            , PurchaseOrder po
    ) {
        this.id = id;
        this.refNo = refNo;
        this.refNo2 = refNo2;
        this.customerId = customerId;
        this.customerName = customerName;
        this.campaignName = campaignName;
        this.purchaseDate = purchaseDate;
        this.tmrName = tmrName;
        this.uwStatus = uwStatus;
        this.paymentStatus = paymentStatus;
        this.qcStatus = qcStatus;
        this.updateBy = updateBy;
        this.productName = productName;
        this.productPlanName = productPlanName;
        this.approveBy = approveBy;
        this.qcBy = qcBy;
        this.paymentBy = paymentBy;
        this.insurePerson = insurePerson;
        this.policyNo = policyNo;
        this.issueDate = issueDate;
        this.reason = reason;
        this.reasonCode = reasonCode;
        this.approvalSupStatus = approvalSupStatus;
        this.approveSupBy = approveSupBy;
        this.confirmFlag = confirmFlag;
        if(po != null){
            /*** Comment by Songwisit on 30 August 2016 ***/
            //Collection<PurchaseOrderApprovalLog> list = po.getPurchaseOrderApprovalLogCollection();
            //List<PurchaseOrderApprovalLog> purchaseOrderApprovalLogList = new ArrayList<PurchaseOrderApprovalLog>(list);

            class poApprovalLogCompare implements Comparator<PurchaseOrderApprovalLog> {

                @Override
                public int compare(PurchaseOrderApprovalLog u1, PurchaseOrderApprovalLog u2) {
                    // write comparison logic here like below , it's just a sample
                    return u1.getId().compareTo(u2.getId());
                }
            }

            //Collections.sort(purchaseOrderApprovalLogList, new poApprovalLogCompare());
            ArrayList<PurchaseOrderApprovalLog> purchaseOrderApprovalLogList = new ArrayList<PurchaseOrderApprovalLog>(po.getPurchaseOrderApprovalLogCollection());
            Collections.sort(purchaseOrderApprovalLogList, new poApprovalLogCompare());
            this.latestStatus = purchaseOrderApprovalLogList.get(purchaseOrderApprovalLogList.size()-1).getApprovalStatus();
            this.latestStatusBy = purchaseOrderApprovalLogList.get(purchaseOrderApprovalLogList.size()-1).getCreateBy();
            this.latestStatusDate = purchaseOrderApprovalLogList.get(purchaseOrderApprovalLogList.size()-1).getCreateDate();

            /*** Comment by Songwisit on 30 August 2016 ***/
            /*
            int i = 0;
            for(PurchaseOrderApprovalLog poal : list) {
                i++;
                if(i == list.size()) {
                    this.latestStatus = poal.getApprovalStatus();
                    this.latestStatusBy = poal.getCreateBy();
                    this.latestStatusDate = poal.getCreateDate();
                }
            }
            */
        }
    }

    //27 fields
    public SaleApprovalValue(Integer id
            , String refNo
            , String refNo2
            , Integer customerId
            , String customerName
            , String campaignName
            , Date purchaseDate
            , String tmrName
            , String uwStatus
            , String paymentStatus
            , String qcStatus
            , String updateBy
            , String productName
            , String productPlanName
            , String approveBy
            , String qcBy
            , String paymentBy
            , String insurePerson
            , String policyNo
            , Date issueDate
            , String reason
            , String reasonCode
            , String approvalSupStatus
            , String approveSupBy
            , boolean confirmFlag
            , PurchaseOrder po
            , String paymentMethodName
    ) {
        this.id = id;
        this.refNo = refNo;
        this.refNo2 = refNo2;
        this.customerId = customerId;
        this.customerName = customerName;
        this.campaignName = campaignName;
        this.purchaseDate = purchaseDate;
        this.tmrName = tmrName;
        this.uwStatus = uwStatus;
        this.paymentStatus = paymentStatus;
        this.qcStatus = qcStatus;
        this.updateBy = updateBy;
        this.productName = productName;
        this.productPlanName = productPlanName;
        this.approveBy = approveBy;
        this.qcBy = qcBy;
        this.paymentBy = paymentBy;
        this.insurePerson = insurePerson;
        this.policyNo = policyNo;
        this.issueDate = issueDate;
        this.reason = reason;
        this.reasonCode = reasonCode;
        this.approvalSupStatus = approvalSupStatus;
        this.approveSupBy = approveSupBy;
        this.confirmFlag = confirmFlag;
        this.paymentMethodName = paymentMethodName;
        if(po != null){
            /*** Comment by Songwisit on 30 August 2016 ***/
            //Collection<PurchaseOrderApprovalLog> list = po.getPurchaseOrderApprovalLogCollection();
            //List<PurchaseOrderApprovalLog> purchaseOrderApprovalLogList = new ArrayList<PurchaseOrderApprovalLog>(list);

            class poApprovalLogCompare implements Comparator<PurchaseOrderApprovalLog> {

                @Override
                public int compare(PurchaseOrderApprovalLog u1, PurchaseOrderApprovalLog u2) {
                    // write comparison logic here like below , it's just a sample
                    return u1.getId().compareTo(u2.getId());
                }
            }

            //Collections.sort(purchaseOrderApprovalLogList, new poApprovalLogCompare());
            ArrayList<PurchaseOrderApprovalLog> purchaseOrderApprovalLogList = new ArrayList<PurchaseOrderApprovalLog>(po.getPurchaseOrderApprovalLogCollection());
            Collections.sort(purchaseOrderApprovalLogList, new poApprovalLogCompare());
            this.latestStatus = purchaseOrderApprovalLogList.get(purchaseOrderApprovalLogList.size()-1).getApprovalStatus();
            this.latestStatusBy = purchaseOrderApprovalLogList.get(purchaseOrderApprovalLogList.size()-1).getCreateBy();
            this.latestStatusDate = purchaseOrderApprovalLogList.get(purchaseOrderApprovalLogList.size()-1).getCreateDate();

            /*** Comment by Songwisit on 30 August 2016 ***/
            /*
            int i = 0;
            for(PurchaseOrderApprovalLog poal : list) {
                i++;
                if(i == list.size()) {
                    this.latestStatus = poal.getApprovalStatus();
                    this.latestStatusBy = poal.getCreateBy();
                    this.latestStatusDate = poal.getCreateDate();
                }
            }
            */
        }
    }

    //30 fields
    public SaleApprovalValue(Integer id
            , String refNo
            , String refNo2
            , Integer customerId
            , String customerName
            , String campaignName
            , Date purchaseDate
            , String tmrName
            , String uwStatus
            , String paymentStatus
            , String qcStatus
            , String updateBy
            , String productName
            , String productPlanName
            , String approveBy
            , String qcBy
            , String paymentBy
            , String insurePerson
            , String policyNo
            , Date issueDate
            , String reason
            , String reasonCode
            , String approvalSupStatus
            , String approveSupBy
            , boolean confirmFlag
            , PurchaseOrder po
            , String paymentMethodName
            , Integer mainPoId
            , boolean familyProduct
            , String relationMainInsured
    ) {
        this.id = id;
        this.refNo = refNo;
        this.refNo2 = refNo2;
        this.customerId = customerId;
        this.customerName = customerName;
        this.campaignName = campaignName;
        this.purchaseDate = purchaseDate;
        this.tmrName = tmrName;
        this.uwStatus = uwStatus;
        this.paymentStatus = paymentStatus;
        this.qcStatus = qcStatus;
        this.updateBy = updateBy;
        this.productName = productName;
        this.productPlanName = productPlanName;
        this.approveBy = approveBy;
        this.qcBy = qcBy;
        this.paymentBy = paymentBy;
        this.insurePerson = insurePerson;
        this.policyNo = policyNo;
        this.issueDate = issueDate;
        this.reason = reason;
        this.reasonCode = reasonCode;
        this.approvalSupStatus = approvalSupStatus;
        this.approveSupBy = approveSupBy;
        this.confirmFlag = confirmFlag;
        this.paymentMethodName = paymentMethodName;
        this.mainPoId = mainPoId;
        this.familyProduct = familyProduct;
        if (this.familyProduct) {
            if (relationMainInsured.equalsIgnoreCase("S")) {
                this.relationMainInsured = "Spouse";
            } else if (relationMainInsured.equalsIgnoreCase("K")) {
                this.relationMainInsured = "Child";
            } else if (relationMainInsured.equalsIgnoreCase("P")) {
                this.relationMainInsured = "Parent";
            } else {
                this.relationMainInsured = "Main Insure";
            }       
        }

        if(po != null){
            /*** Comment by Songwisit on 30 August 2016 ***/
            //Collection<PurchaseOrderApprovalLog> list = po.getPurchaseOrderApprovalLogCollection();
            //List<PurchaseOrderApprovalLog> purchaseOrderApprovalLogList = new ArrayList<PurchaseOrderApprovalLog>(list);

            class poApprovalLogCompare implements Comparator<PurchaseOrderApprovalLog> {

                @Override
                public int compare(PurchaseOrderApprovalLog u1, PurchaseOrderApprovalLog u2) {
                    // write comparison logic here like below , it's just a sample
                    return u1.getId().compareTo(u2.getId());
                }
            }

            //Collections.sort(purchaseOrderApprovalLogList, new poApprovalLogCompare());
            ArrayList<PurchaseOrderApprovalLog> purchaseOrderApprovalLogList = new ArrayList<PurchaseOrderApprovalLog>(po.getPurchaseOrderApprovalLogCollection());
            Collections.sort(purchaseOrderApprovalLogList, new poApprovalLogCompare());
            this.latestStatus = purchaseOrderApprovalLogList.get(purchaseOrderApprovalLogList.size()-1).getApprovalStatus();
            this.latestStatusBy = purchaseOrderApprovalLogList.get(purchaseOrderApprovalLogList.size()-1).getCreateBy();
            this.latestStatusDate = purchaseOrderApprovalLogList.get(purchaseOrderApprovalLogList.size()-1).getCreateDate();

            /*** Comment by Songwisit on 30 August 2016 ***/
            /*
            int i = 0;
            for(PurchaseOrderApprovalLog poal : list) {
                i++;
                if(i == list.size()) {
                    this.latestStatus = poal.getApprovalStatus();
                    this.latestStatusBy = poal.getCreateBy();
                    this.latestStatusDate = poal.getCreateDate();
                }
            }
            */
        }
    }

    //31 fields
    public SaleApprovalValue(Integer id
            , String refNo
            , String refNo2
            , Integer customerId
            , String customerName
            , String campaignName
            , Date purchaseDate
            , String tmrName
            , String uwStatus
            , String paymentStatus
            , String qcStatus
            , String updateBy
            , String productName
            , String productPlanName
            , String approveBy
            , String qcBy
            , String paymentBy
            , String insurePerson
            , String policyNo
            , Date issueDate
            , String reason
            , String reasonCode
            , String approvalSupStatus
            , String approveSupBy
            , boolean confirmFlag
            , PurchaseOrder po
            , String paymentMethodName
            , Integer mainPoId
            , boolean familyProduct
            , String relationMainInsured
            , String poFx2
    ) {
        this.id = id;
        this.refNo = refNo;
        this.refNo2 = refNo2;
        this.customerId = customerId;
        this.customerName = customerName;
        this.campaignName = campaignName;
        this.purchaseDate = purchaseDate;
        this.tmrName = tmrName;
        this.uwStatus = uwStatus;
        this.paymentStatus = paymentStatus;
        this.qcStatus = qcStatus;
        this.updateBy = updateBy;
        this.productName = productName;
        this.productPlanName = productPlanName;
        this.approveBy = approveBy;
        this.qcBy = qcBy;
        this.paymentBy = paymentBy;
        this.insurePerson = insurePerson;
        this.policyNo = policyNo;
        this.issueDate = issueDate;
        this.reason = reason;
        this.reasonCode = reasonCode;
        this.approvalSupStatus = approvalSupStatus;
        this.approveSupBy = approveSupBy;
        this.confirmFlag = confirmFlag;
        this.paymentMethodName = paymentMethodName;
        this.mainPoId = mainPoId;
        this.familyProduct = familyProduct;
        this.poFx2 = poFx2;
        if (this.familyProduct) {
            if (relationMainInsured.equalsIgnoreCase("S")) {
                this.relationMainInsured = "Spouse";
            } else if (relationMainInsured.equalsIgnoreCase("K")) {
                this.relationMainInsured = "Child";
            } else if (relationMainInsured.equalsIgnoreCase("P")) {
                this.relationMainInsured = "Parent";
            } else {
                this.relationMainInsured = "Main Insure";
            }
        }

        if(po != null){
            /*** Comment by Songwisit on 30 August 2016 ***/
            //Collection<PurchaseOrderApprovalLog> list = po.getPurchaseOrderApprovalLogCollection();
            //List<PurchaseOrderApprovalLog> purchaseOrderApprovalLogList = new ArrayList<PurchaseOrderApprovalLog>(list);

            class poApprovalLogCompare implements Comparator<PurchaseOrderApprovalLog> {

                @Override
                public int compare(PurchaseOrderApprovalLog u1, PurchaseOrderApprovalLog u2) {
                    // write comparison logic here like below , it's just a sample
                    return u1.getId().compareTo(u2.getId());
                }
            }

            //Collections.sort(purchaseOrderApprovalLogList, new poApprovalLogCompare());
            ArrayList<PurchaseOrderApprovalLog> purchaseOrderApprovalLogList = new ArrayList<PurchaseOrderApprovalLog>(po.getPurchaseOrderApprovalLogCollection());
            Collections.sort(purchaseOrderApprovalLogList, new poApprovalLogCompare());
            this.latestStatus = purchaseOrderApprovalLogList.get(purchaseOrderApprovalLogList.size()-1).getApprovalStatus();
            this.latestStatusBy = purchaseOrderApprovalLogList.get(purchaseOrderApprovalLogList.size()-1).getCreateBy();
            this.latestStatusDate = purchaseOrderApprovalLogList.get(purchaseOrderApprovalLogList.size()-1).getCreateDate();

            /*** Comment by Songwisit on 30 August 2016 ***/
            /*
            int i = 0;
            for(PurchaseOrderApprovalLog poal : list) {
                i++;
                if(i == list.size()) {
                    this.latestStatus = poal.getApprovalStatus();
                    this.latestStatusBy = poal.getCreateBy();
                    this.latestStatusDate = poal.getCreateDate();
                }
            }
            */
        }
    }

    private String getStatusToShow(String str) {
        try{
            if (str.equals(JSFUtil.getBundleValue("approvalWaitingValue"))) {
                return JSFUtil.getBundleValue("approvalWaiting");
            } else if (str.equals(JSFUtil.getBundleValue("approvalPendingValue"))) {
                return JSFUtil.getBundleValue("approvalPending");
            } else if (str.equals(JSFUtil.getBundleValue("approvalApprovedValue"))) {
                return JSFUtil.getBundleValue("approvalApproved");
            } else if (str.equals(JSFUtil.getBundleValue("approvalRejectedValue"))) {
                return JSFUtil.getBundleValue("approvalRejected");
            } else if (str.equals(JSFUtil.getBundleValue("approvalReconfirmValue"))) {
                return JSFUtil.getBundleValue("approvalReconfirm");
            } else if (str.equals(JSFUtil.getBundleValue("approvalRequestValue"))) {
                return JSFUtil.getBundleValue("approvalRequest");
            } else if (str.equals(JSFUtil.getBundleValue("approvalReopenValue"))) {
                return JSFUtil.getBundleValue("approvalReopen");
            } else if (str.equals(JSFUtil.getBundleValue("approvalResendValue"))) {
                return JSFUtil.getBundleValue("approvalResend");
            } else {
                return "";
            }
        }catch(Exception e){
            return "";
        }
    }

//    private String genInsurePerson(List<PurchaseOrderRegister> list){
//        String str = "";
//        for(PurchaseOrderRegister por : list){
//            str += por.getName() + " " + por.getSurname() + ",";
//        }
//        if(!str.equals("")){
//            str = str.substring(0, str.length() - 1);
//        }
//        return str;
//    }

    public String getApprovalStatus() {
        try{
            return this.getStatusToShow(approvalStatus);
        }catch(Exception e){
            return "";
        }
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }


    public String getCustomerName() {
        return customerName;
    }

    public Integer getId() {
        return id;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public String getRefNo() {
        return refNo;
    }

    public String getSaleResult() {
        if (saleResult=='Y')
            return "Yes";
        else if (saleResult=='N')
            return "No";
        else if (saleResult=='F')
            return "Follow Up";
        else
            return "";
    }

    public String getTmrName() {
        return tmrName;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public String getPaymentStatus() {
        try{
            return this.getStatusToShow(paymentStatus);
        }catch(Exception e){
            return "";
        }
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getQcStatus() {
        try{
            return this.getStatusToShow(qcStatus);
        }catch(Exception e){
            return "";
        }
    }

    public void setQcStatus(String qcStatus) {
        this.qcStatus = qcStatus;
    }

    public String getUwStatus() {
        try{
            return this.getStatusToShow(uwStatus);
        }catch(Exception e){
            return "";
        }
        //return uwStatus;
    }

    public void setUwStatus(String uwStatus) {
        this.uwStatus = uwStatus;
    }

    public String getRefNo2() {
        return refNo2;
    }

    public void setRefNo2(String refNo2) {
        this.refNo2 = refNo2;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getApproveBy() {
        return approveBy;
    }

    public void setApproveBy(String approveBy) {
        this.approveBy = approveBy;
    }

    public String getPaymentBy() {
        return paymentBy;
    }

    public void setPaymentBy(String paymentBy) {
        this.paymentBy = paymentBy;
    }

    public String getQcBy() {
        return qcBy;
    }

    public void setQcBy(String qcBy) {
        this.qcBy = qcBy;
    }

    public String getInsurePerson() {
        return insurePerson;
    }

    public void setInsurePerson(String insurePerson) {
        this.insurePerson = insurePerson;
    }

    public String getProductPlanName() {
        return productPlanName;
    }

    public void setProductPlanName(String productPlanName) {
        this.productPlanName = productPlanName;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getApprovalSupStatus() {
        return approvalSupStatus;
    }

    public void setApprovalSupStatus(String approvalSupStatus) {
        this.approvalSupStatus = approvalSupStatus;
    }

    public String getApproveSupBy() {
        return approveSupBy;
    }

    public void setApproveSupBy(String approveSupBy) {
        this.approveSupBy = approveSupBy;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public boolean isConfirmFlag() {
        return confirmFlag;
    }

    public void setConfirmFlag(boolean confirmFlag) {
        this.confirmFlag = confirmFlag;
    }

    public String getLatestStatus() {
        return latestStatus;
    }

    public void setLatestStatus(String latestStatus) {
        this.latestStatus = latestStatus;
    }

    public Date getLatestStatusDate() {
        return latestStatusDate;
    }

    public void setLatestStatusDate(Date latestStatusDate) {
        this.latestStatusDate = latestStatusDate;
    }

    public String getLatestStatusBy() {
        return latestStatusBy;
    }

    public void setLatestStatusBy(String latestStatusBy) {
        this.latestStatusBy = latestStatusBy;
    }

    public String getPaymentMethodName() {
        return paymentMethodName;
    }

    public void setPaymentMethodName(String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }

    public Integer getMainPoId() {
        return mainPoId;
    }

    public void setMainPoId(Integer mainPoId) {
        this.mainPoId = mainPoId;
    }

    public boolean isFamilyProduct() {
        return familyProduct;
    }

    public void setFamilyProduct(boolean familyProduct) {
        this.familyProduct = familyProduct;
    }
    
    public String getRelationMainInsured() {
        return relationMainInsured;
    }

    public void setRelationMainInsured(String relationMainInsured) {
        this.relationMainInsured = relationMainInsured;
    }

    public String getPoFx2() {
        return poFx2;
    }

    public void setPoFx2(String poFx2) {
        this.poFx2 = poFx2;
    }
}