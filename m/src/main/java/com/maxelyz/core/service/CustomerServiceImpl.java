/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.service;

import com.maxelyz.core.model.dao.CustomerDAO;
import com.maxelyz.core.model.dao.CustomerHistoricalColumnDAO;
import com.maxelyz.core.model.dao.CustomerHistoricalDAO;
import com.maxelyz.core.model.dao.CustomerHistoricalGroupDAO;
import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.Customer;
import com.maxelyz.core.model.entity.CustomerHistorical;
import com.maxelyz.core.model.entity.CustomerHistoricalColumn;
import com.maxelyz.core.model.entity.CustomerHistoricalGroup;
import com.maxelyz.core.model.value.front.customerHandling.CustomerHistoricalValue;
import java.io.File;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author oat
 */
@Transactional
public class CustomerServiceImpl implements CustomerService{
    private static Logger log = Logger.getLogger(CustomerServiceImpl.class);
    private CustomerDAO customerDAO;
    private CustomerHistoricalDAO customerHistoricalDAO;
    private CustomerHistoricalColumnDAO customerHistoricalColumnDAO;
    private CustomerHistoricalGroupDAO customerHistoricalGroupDAO;
    private CustomerHistoricalValue customerHistoricalDetail;
    
    @Override
    public List<CustomerHistoricalGroup> findCustomerHistoricalGroupEntities() {
        return customerHistoricalGroupDAO.findCustomerHistoricalGroupEntities();
    }
        
    @Override
    public CustomerHistoricalGroup findCustomerHistoricalGroup(Integer id) {
        return customerHistoricalGroupDAO.findCustomerHistoricalGroup(id);
    }
        
    @Override
    public Map<String, String> createCustomerHistorical(CustomerHistoricalGroup customerHistoricalGroup, File excelFile) throws Exception {
        return null;
    }
    
    @Override
    public void editCustomerHistoricalGroup(CustomerHistoricalGroup customerHistoricalGroup) throws Exception {
        customerHistoricalGroupDAO.editCustomerHistroricalGroup(customerHistoricalGroup);
    }
    
    @Override
    public void purgeCustomerHistoricalGroup(CustomerHistoricalGroup customerHistoricalGroup) {
        
    }
    
    @Override
    public List<CustomerHistorical> findCustomerHistoricalByRefNo(String customerReferenceNo, Integer customerHistoricalGroupId) {
        return null;
    }
    
    @Override
    public List<CustomerHistoricalColumn> findCustomerHistoricalColumnByGroup(CustomerHistoricalGroup customerHistoricalGroup) {
        return customerHistoricalColumnDAO.findCustomerHistoricalColumnByGroup(customerHistoricalGroup);
    }
    
    @Override
    public List<Customer> findCustomerByReferenceNo(String referenceNo) {
        return customerDAO.findCustomerByReferenceNo(referenceNo);
    }
    
    @Override
    public List<CustomerHistoricalColumn> findCustomerHistoricalHighlightColumnByGroup(CustomerHistoricalGroup customerHistoricalGroup,String highlightCol) {
        return customerHistoricalColumnDAO.findCustomerHistoricalHighlightColumnByGroup(customerHistoricalGroup, highlightCol);
    }
    
    @Override
    public List<CustomerHistorical> findCustomerHistoricalByReferenceCustomer(CustomerHistoricalGroup customerHistoricalGroup, String refCustomer) {
        return customerHistoricalDAO.findCustomerHistoricalByReferenceCustomer(customerHistoricalGroup, refCustomer);
    }
    
    @Override
    public CustomerHistorical findCustomerHistoricalById(Integer id) {
        return customerHistoricalDAO.findCustomerHistorical(id);
    }
    //DAO

    public CustomerDAO getCustomerDAO() {
        return customerDAO;
    }

    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public CustomerHistoricalColumnDAO getCustomerHistoricalColumnDAO() {
        return customerHistoricalColumnDAO;
    }

    public void setCustomerHistoricalColumnDAO(CustomerHistoricalColumnDAO customerHistoricalColumnDAO) {
        this.customerHistoricalColumnDAO = customerHistoricalColumnDAO;
    }

    public CustomerHistoricalDAO getCustomerHistoricalDAO() {
        return customerHistoricalDAO;
    }

    public void setCustomerHistoricalDAO(CustomerHistoricalDAO customerHistoricalDAO) {
        this.customerHistoricalDAO = customerHistoricalDAO;
    }

    public CustomerHistoricalGroupDAO getCustomerHistoricalGroupDAO() {
        return customerHistoricalGroupDAO;
    }

    public void setCustomerHistoricalGroupDAO(CustomerHistoricalGroupDAO customerHistoricalGroupDAO) {
        this.customerHistoricalGroupDAO = customerHistoricalGroupDAO;
    }

  
    public CustomerHistoricalValue getCustomerHistoricalDetail() {
        return customerHistoricalDetail;
    }

    public void setCustomerHistoricalDetail(CustomerHistoricalValue customerHistoricalDetail) {
        this.customerHistoricalDetail = customerHistoricalDetail;
    }
    
    
}
