/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.value.admin;

import com.maxelyz.core.model.entity.BusinessUnit;
import com.maxelyz.core.model.entity.Location;
import java.util.List;

/**
 *
 * @author admin
 */
public class BusinessUnitValue {
    private BusinessUnit businessUnit;
    private boolean selected;
    private List<LocationValue> locationValueList;
    
    public BusinessUnitValue(BusinessUnit businessUnit){
        this.businessUnit = businessUnit;
    }

    public BusinessUnit getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public List<LocationValue> getLocationValueList() {
        return locationValueList;
    }

    public void setLocationValueList(List<LocationValue> locationValueList) {
        this.locationValueList = locationValueList;
    }
    
}
