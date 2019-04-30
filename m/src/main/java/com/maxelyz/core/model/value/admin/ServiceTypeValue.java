/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.value.admin;

import com.maxelyz.core.model.entity.ServiceType;
import java.util.List;

/**
 *
 * @author admin
 */
public class ServiceTypeValue {
    private ServiceType serviceType;
    private List<BusinessUnitValue> businessUnitValueList;
    private boolean selected;
    
    public ServiceTypeValue(ServiceType serviceType){
        this.serviceType = serviceType;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public List<BusinessUnitValue> getBusinessUnitValueList() {
        return businessUnitValueList;
    }

    public void setBusinessUnitValueList(List<BusinessUnitValue> businessUnitValueList) {
        this.businessUnitValueList = businessUnitValueList;
    }

    
}
