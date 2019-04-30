/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.value.front.search;

/**
 *
 * @author Manop
 */
public class SearchCustomerValue {
    private int customerId;
    private String customerName;
    private String telephone;
    private String customerType;
    private Long  pendingCase;
    private Long closedCase;
    private String carBrand;
    private String carModel;
    private Integer carYear;
    private String carCharacterGroup;
    private String carNumber;
    
    //Primetime
    private String email;
    private String displayName;
    private String wsCustId;
    

    public SearchCustomerValue(){

    }
    
    //Primetime
    public SearchCustomerValue(int customerId, String customerName, String telephone, String customerType, String email, String wsCustId, String displayName){
        this.customerId = customerId;
        this.customerName = customerName;
        this.telephone = telephone;
        this.customerType = customerType;
        this.email = email;
        this.displayName = displayName;
        this.wsCustId = wsCustId;
    }
        
    public SearchCustomerValue(int customerId, String customerName, String telephone, String customerType, Long pendingCase, Long closedCase){
        this.customerId = customerId;
        this.customerName = customerName;
        this.telephone = telephone;
        this.customerType = customerType;
        this.pendingCase = pendingCase;
        this.closedCase = closedCase;
    }
    
    public SearchCustomerValue(int customerId, String customerName, String telephone, String customerType, Long pendingCase, Long closedCase, String carBrand, String carModel, Integer carYear, String carCharacterGroup, String carNumber){
        this.customerId = customerId;
        this.customerName = customerName;
        this.telephone = telephone;
        this.customerType = customerType;
        this.pendingCase = pendingCase;
        this.closedCase = closedCase;
        this.carBrand = carBrand;
        this.carModel = carModel;
        this.carYear = carYear;
        this.carCharacterGroup = carCharacterGroup;
        this.carNumber = carNumber;
    }
    /**
     * @return the customerId
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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
     * @return the pendingCase
     */
    public Long getPendingCase() {
        return pendingCase;
    }

    /**
     * @param pendingCase the pendingCase to set
     */
    public void setPendingCase(Long pendingCase) {
        this.pendingCase = pendingCase;
    }

    /**
     * @return the closedCase
     */
    public Long getClosedCase() {
        return closedCase;
    }

    /**
     * @param closedCase the closedCase to set
     */
    public void setClosedCase(Long closedCase) {
        this.closedCase = closedCase;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public Integer getCarYear() {
        return carYear;
    }

    public void setCarYear(Integer carYear) {
        this.carYear = carYear;
    }

    public String getCarCharacterGroup() {
        return carCharacterGroup;
    }

    public void setCarCharacterGroup(String carCharacterGroup) {
        this.carCharacterGroup = carCharacterGroup;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getWsCustId() {
        return wsCustId;
    }

    public void setWsCustId(String wsCustId) {
        this.wsCustId = wsCustId;
    }
    
    

}
