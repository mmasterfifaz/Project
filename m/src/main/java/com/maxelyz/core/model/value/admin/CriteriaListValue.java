/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.value.admin;

//import com.maxelyz.core.model.entity.CustomerField;

import java.util.Date;


/**
 *
 * @author vee
 */
public class CriteriaListValue {
    //m.id,m.name,fm.customer_field,cf.mapping_field,cf.name
    private boolean selected;
    private String mappingField;
    private String mappingName;
    private String keyword;
    private String type;
    private String pattern;
    private String criteria;
    private String fromValue;
    private String toValue;
    private Date fromValueDate;
    private Date toValueDate;
    
    
//    public CriteriaListValue(String mappingField, String mappingName){
//        this.mappingField = mappingField;
//        this.mappingName = mappingName;
//    }

//    public CriteriaListValue(String mappingField, String mappingName, String keyword){
//        this.selected = false;
////        this.selected = selected;
//        this.mappingField = mappingField;
//        this.mappingName = mappingName;
//        this.keyword = keyword;
//    }
    
    public CriteriaListValue(String mappingField, String mappingName, String keyword, String fromValue, String toValue){
        this.selected = false;        
        this.mappingField = mappingField;
        this.mappingName = mappingName;
        this.keyword = keyword;
        this.fromValue = fromValue;
        this.toValue = toValue;
    }
        
//    public CriteriaListValue(String mappingField, String mappingName, String type, String pattern){
//        this.mappingField = mappingField;
//        this.mappingName = mappingName;
//        this.type = type;
//        this.pattern = pattern;
//    }
        
    public CriteriaListValue(String mappingField, String mappingName, String type, String pattern, String keyword, String criteria, String fromValue, String toValue){
        this.selected = false;
        this.mappingField = mappingField;
        this.mappingName = mappingName;
        this.type = type;
        this.pattern = pattern;
        this.keyword = keyword;
        this.criteria = criteria;
        this.fromValue = fromValue;
        this.toValue = toValue;
        this.fromValueDate = new Date();
        this.toValueDate = new Date();
    }
       
    public String getMappingField() {
        return mappingField;
    }

    public void setMappingField(String mappingField) {
        this.mappingField = mappingField;
    }

    public String getMappingName() {
        return mappingName;
    }

    public void setMappingName(String mappingName) {
        this.mappingName = mappingName;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public String getFromValue() {
        return fromValue;
    }

    public void setFromValue(String fromValue) {
        this.fromValue = fromValue;
    }

    public String getToValue() {
        return toValue;
    }

    public void setToValue(String toValue) {
        this.toValue = toValue;
    }

    public Date getFromValueDate() {
        return fromValueDate;
    }

    public void setFromValueDate(Date fromValueDate) {
        this.fromValueDate = fromValueDate;
    }

    public Date getToValueDate() {
        return toValueDate;
    }

    public void setToValueDate(Date toValueDate) {
        this.toValueDate = toValueDate;
    }
    

}
