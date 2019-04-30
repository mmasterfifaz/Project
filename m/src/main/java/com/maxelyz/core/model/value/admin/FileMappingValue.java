/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.value.admin;

/**
 *
 * @author Oat
 */
public class FileMappingValue {
    private int fileColumnNo;
    private String fileColumnName;
    private int customerFieldId;
    private String dateFormat;
    private Boolean adYear;
    
    private String fieldMapping;
    private String fieldType;
    private String pattern;
    private String defaultValue;

    public int getCustomerFieldId() {
        return customerFieldId;
    }

    public void setCustomerFieldId(int customerFieldId) {
        this.customerFieldId = customerFieldId;
    }

    public String getFileColumnName() {
        return fileColumnName;
    }

    public void setFileColumnName(String fileColumnName) {
        this.fileColumnName = fileColumnName;
    }

    public int getFileColumnNo() {
        return fileColumnNo;
    }

    public void setFileColumnNo(int fileColumnNo) {
        this.fileColumnNo = fileColumnNo;
    }

    public Boolean getAdYear() {
        return adYear;
    }

    public void setAdYear(Boolean adYear) {
        this.adYear = adYear;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }  

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getFieldMapping() {
        return fieldMapping;
    }

    public void setFieldMapping(String fieldMapping) {
        this.fieldMapping = fieldMapping;
    }

    
}
