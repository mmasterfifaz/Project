/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.value.admin;

import com.maxelyz.core.model.entity.FxMapping;
import com.maxelyz.core.model.entity.CustomerLayoutFxMapping;
/**
 *
 * @author Administrator
 */
public class FlexFieldValue {
    private FxMapping fxMapping;
    private CustomerLayoutFxMapping customerLayoutFxMapping;
    private boolean selected;
    
    // FOR FLEX FIELD MAPPING PAGE
    public FlexFieldValue() {
    }

    public FlexFieldValue(FxMapping fxMapping) {
        this(false, fxMapping);
    }
         
    public FlexFieldValue(boolean selected, FxMapping fxMapping) {
        this.selected = selected;
        this.fxMapping = fxMapping;
    }
    
    public FxMapping getFxMapping() {
        return fxMapping;
    }
    
    public void setFxMapping(FxMapping fxMapping) {
        this.fxMapping = fxMapping;
    }
    
    // FOR CUSTOMER LAYOUT FLEX FIELD MAPPING PAGE
    public FlexFieldValue(CustomerLayoutFxMapping customerLayoutFxMapping) {
        this(false, customerLayoutFxMapping);
    }
         
    public FlexFieldValue(boolean selected, CustomerLayoutFxMapping customerLayoutFxMapping) {
        this.selected = selected;
        this.customerLayoutFxMapping = customerLayoutFxMapping;
    }

    public CustomerLayoutFxMapping getCustomerLayoutFxMapping() {
        return customerLayoutFxMapping;
    }

    public void setCustomerLayoutFxMapping(CustomerLayoutFxMapping customerLayoutFxMapping) {
        this.customerLayoutFxMapping = customerLayoutFxMapping;
    }
    
    // SELECT CHECKBOX    
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
}
