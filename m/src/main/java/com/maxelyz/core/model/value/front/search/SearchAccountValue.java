/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.value.front.search;

/**
 *
 * @author Manop
 */
public class SearchAccountValue {
    private int accountId;
    private String accountName;
    private String department;
    private String addressLine1;
    private String addressLine2;
    private Integer districtId;
    private String district;
    private Integer provinceId;
    private String province;
    private String postalCode;
    private String remark;
    private String telephone;

    public SearchAccountValue(){}

    public SearchAccountValue(int accountId, String accountName, String addressLine1, String addressLine2, String district, String province){
        this.accountId = accountId;
        this.accountName = accountName;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.district = district;
        this.province = province;
    }

    public SearchAccountValue(int accountId, String accountName, String department, String addressLine1, String addressLine2, Integer districtId, String district,
            Integer provinceId, String province, String postalCode, String remark, String telephone
            ){
        this.accountId = accountId;
        this.accountName = accountName;
        this.department = department;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.districtId = districtId;
        this.district = district;
        this.provinceId = provinceId;
        this.province = province;
        this.postalCode = postalCode;
        this.remark = remark;
        this.telephone = telephone;
    }

    /**
     * @return the accountId
     */
    public int getAccountId() {
        return accountId;
    }

    /**
     * @param accountId the accountId to set
     */
    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    /**
     * @return the accountName
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * @param accountName the accountName to set
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    /**
     * @return the addressLine1
     */
    public String getAddressLine1() {
        return addressLine1;
    }

    /**
     * @return the addressLine2
     */
    public String getAddressLine2() {
        return addressLine2;
    }

    /**
     * @return the district
     */
    public String getDistrict() {
        return district;
    }

    /**
     * @return the province
     */
    public String getProvince() {
        return province;
    }

    /**
     * @param province the province to set
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * @return the department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * @param addressLine1 the addressLine1 to set
     */
    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    /**
     * @param addressLine2 the addressLine2 to set
     */
    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    /**
     * @return the districtId
     */
    public Integer getDistrictId() {
        return districtId;
    }

    /**
     * @param districtId the districtId to set
     */
    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    /**
     * @param district the district to set
     */
    public void setDistrict(String district) {
        this.district = district;
    }

    /**
     * @return the provinceId
     */
    public Integer getProvinceId() {
        return provinceId;
    }

    /**
     * @param provinceId the provinceId to set
     */
    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    /**
     * @return the postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @param postalCode the postalCode to set
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return the telephone
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * @param telephone the telephone to set
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
