/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.service;

import com.maxelyz.core.model.entity.*;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 *
 * @author oat
 */
public interface CustomerService {
    List<CustomerHistoricalGroup> findCustomerHistoricalGroupEntities();
    CustomerHistoricalGroup findCustomerHistoricalGroup(Integer id);
    Map<String, String> createCustomerHistorical(CustomerHistoricalGroup customerHistoricalGroup, File excelFile) throws Exception;
    void editCustomerHistoricalGroup(CustomerHistoricalGroup customerHistoricalGroup) throws Exception;
    void purgeCustomerHistoricalGroup(CustomerHistoricalGroup customerHistoricalGroup);
    List<CustomerHistorical> findCustomerHistoricalByRefNo(String customerReferenceNo, Integer customerHistoricalGroupId);
    List<CustomerHistoricalColumn> findCustomerHistoricalColumnByGroup(CustomerHistoricalGroup customerHistoricalGroup);  
    List<CustomerHistoricalColumn> findCustomerHistoricalHighlightColumnByGroup(CustomerHistoricalGroup customerHistoricalGroup,String highlightCol);
    public List<Customer> findCustomerByReferenceNo(String referenceNo);
    List<CustomerHistorical> findCustomerHistoricalByReferenceCustomer(CustomerHistoricalGroup customerHistoricalGroup, String refCustomer);
    CustomerHistorical findCustomerHistoricalById(Integer id);
    
}