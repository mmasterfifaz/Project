/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.controller.front.customerHandling;

import com.maxelyz.core.controller.common.BaseController;
import com.maxelyz.core.model.dao.CustomerDAO;
import com.maxelyz.core.model.entity.Customer;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author prawait
 */
@ManagedBean
@RequestScoped
public class CustomerHandlingController extends BaseController{

    private String SUCCESS = "../customerHandling/customerHandling.jsf";
    private String FAIL = "searchCustomer.jsf";
    private Customer customer;

    @ManagedProperty(value="#{customerDAO}")
    private CustomerDAO customerDAO;

    @PostConstruct
    public void initialize() {

    }

    public String getCustomerInfo(){

       String selectedId=null;
       selectedId = getRequest("selectID");

       if (selectedId==null || selectedId.isEmpty()) {
            return FAIL;
       } else {

            customer = getCustomerDAO().findCustomer(Integer.parseInt(selectedId));
    }

       return SUCCESS;
    }

    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * @return the customerDAO
     */
    public CustomerDAO getCustomerDAO() {
        return customerDAO;
    }

    /**
     * @param customerDAO the customerDAO to set
     */
    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

}
