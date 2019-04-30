/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.value.front.customerHandling;


public class CoverageInfo {
    private String id;
    private String code;
    private String description_TH;
    private String description_EN;
    private String sum_Insured;
    private String deduct;
    private String premium_Amt;
    private String sum_Insured_PlanDetail;
    private String deduct_PlanDetail;
    
    public CoverageInfo()
    {
        this.id = "";
        this.code = "";
        this.description_TH = "";
        this.description_EN = "";
        this.sum_Insured = "";
        this.deduct = "";
        this.premium_Amt = "";
        this.sum_Insured_PlanDetail = "";
        this.deduct_PlanDetail = "";
    }
    
    public CoverageInfo(String id,String code,String description_TH,String description_EN,String sum_Insured,String deduct,String premium_Amt,String sum_Insured_PlanDetail,String deduct_PlanDetail)
    {
        this.id = id;
        this.code = code;
        this.description_TH = description_TH;
        this.description_EN = description_EN;
        this.sum_Insured = sum_Insured;
        this.deduct = deduct;
        this.premium_Amt = premium_Amt;
        this.sum_Insured_PlanDetail = sum_Insured_PlanDetail;
        this.deduct_PlanDetail = deduct_PlanDetail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription_TH() {
        return description_TH;
    }

    public void setDescription_TH(String description_TH) {
        this.description_TH = description_TH;
    }

    public String getDescription_EN() {
        return description_EN;
    }

    public void setDescription_EN(String description_EN) {
        this.description_EN = description_EN;
    }

    public String getSum_Insured() {
        return sum_Insured;
    }

    public void setSum_Insured(String sum_Insured) {
        this.sum_Insured = sum_Insured;
    }

    public String getDeduct() {
        return deduct;
    }

    public void setDeduct(String deduct) {
        this.deduct = deduct;
    }

    public String getPremium_Amt() {
        return premium_Amt;
    }

    public void setPremium_Amt(String premium_Amt) {
        this.premium_Amt = premium_Amt;
    }

    public String getSum_Insured_PlanDetail() {
        return sum_Insured_PlanDetail;
    }

    public void setSum_Insured_PlanDetail(String sum_Insured_PlanDetail) {
        this.sum_Insured_PlanDetail = sum_Insured_PlanDetail;
    }

    public String getDeduct_PlanDetail() {
        return deduct_PlanDetail;
    }

    public void setDeduct_PlanDetail(String deduct_PlanDetail) {
        this.deduct_PlanDetail = deduct_PlanDetail;
    } 
}
