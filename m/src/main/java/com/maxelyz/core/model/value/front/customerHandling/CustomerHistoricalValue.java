/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.value.front.customerHandling;


/**
 *
 * @author Administrator
 */
public class CustomerHistoricalValue {
    private Integer id;
    private String columnName;
    private String detail;
    
    public CustomerHistoricalValue(){}
    
    public CustomerHistoricalValue(Integer id, String columnName, String detail) {
        this.id = id;
        this.columnName = columnName;
        this.detail = detail;
    }
     
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }


    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
