/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.value.admin;

import com.maxelyz.core.model.entity.Customer;


public class MarketingCustomerValue {
    private Customer customer;
    private Boolean newList;

    public MarketingCustomerValue(Customer customer, Boolean newList) {
        this.customer = customer;
        this.newList = newList;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Boolean getNewList() {
        return newList;
    }

    public void setNewList(Boolean newList) {
        this.newList = newList;
    }
}
