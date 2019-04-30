/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.value.front.customerHandling;

import java.util.ArrayList;
import java.util.List;

public class ProductPlanDetailInfo {
    private String plan_detail_code;
    private String number_of_coverage;
    private List<CoverageInfo> coverage_info;

    public ProductPlanDetailInfo()
    {
        this.plan_detail_code = "";
        this.number_of_coverage = "";
        this.coverage_info = new ArrayList<CoverageInfo>();
    }
    
    public String getPlan_detail_code() {
        return plan_detail_code;
    }

    public void setPlan_detail_code(String plan_detail_code) {
        this.plan_detail_code = plan_detail_code;
    }

    public String getNumber_of_coverage() {
        return number_of_coverage;
    }

    public void setNumber_of_coverage(String number_of_coverage) {
        this.number_of_coverage = number_of_coverage;
    }

    public List<CoverageInfo> getCoverage_info() {
        return coverage_info;
    }

    public void setCoverage_info(List<CoverageInfo> coverage_info) {
        this.coverage_info = coverage_info;
    }
     
    
}
