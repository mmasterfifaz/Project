/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.value.front.search;

import java.util.List;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerWSValue {
    private int page_index;
    private int page_size;
    private int page_total;
    private List<CustomerWSValue> elements;
    private String error;
    private String id;
    private String email;
    private String display_name;
    private String first_name;
    private String last_name;
    private String phone_no;
    
    public CustomerWSValue(){

    }
    
    public CustomerWSValue(String error) {      //Error Not Found Data
        this.error = error;
    }
        
    public CustomerWSValue(String id, String email, String display_name, String first_name, String last_name, String phone_no){ //1 Record
        this.id = id;
        this.email = email;
        this.display_name = display_name;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_no = phone_no;
    }

     public CustomerWSValue(int page_index, int page_size, int page_total, List<CustomerWSValue> elements) {    //Multiple Record
        this.page_index = page_index;
        this.page_size = page_size;
        this.page_total = page_total;
        this.elements = elements;
    }
     
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public int getPage_index() {
        return page_index;
    }

    public void setPage_index(int page_index) {
        this.page_index = page_index;
    }

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    public int getPage_total() {
        return page_total;
    }

    public void setPage_total(int page_total) {
        this.page_total = page_total;
    }

    public List<CustomerWSValue> getElements() {
        return elements;
    }

    public void setElements(List<CustomerWSValue> elements) {
        this.elements = elements;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    
}
