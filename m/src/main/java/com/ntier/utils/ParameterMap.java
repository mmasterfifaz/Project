/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ntier.utils;

/**
 *
 * @author Manop
 */
public class ParameterMap {
    private String name;
    private Object value;

    public ParameterMap(){}

    public ParameterMap(String name, Object value){
        this.name = name;
        this.value = value;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Object value) {
        this.value = value;
    }

}
