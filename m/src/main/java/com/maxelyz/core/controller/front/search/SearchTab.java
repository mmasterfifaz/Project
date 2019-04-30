/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.front.search;

import javax.faces.bean.*;
import com.maxelyz.utils.SecurityUtil;
/**
 *
 * @author vee
 */
@ManagedBean
public class SearchTab {
     
    public boolean isSearchCasePermitted() {
        return SecurityUtil.isPermitted("searchcase:view");
    }
    
    public boolean isSearchCustomerPermitted() {
        return SecurityUtil.isPermitted("searchcustomer:view");
    }
        
    public boolean isSearchAccountPermitted() {
        return SecurityUtil.isPermitted("searchaccount:view");
    }
    
    public boolean isSearchMessagePermitted() {
        return SecurityUtil.isPermitted("searchmessage:view");
    }
    
}
