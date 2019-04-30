/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.service;

import java.util.Map;

/**
 *
 * @author oat
 */
public interface OrderService {
    public Map<String, String> bblPost(String userName, String ip_postData, String ip_pageUrl) throws Exception;
    
}
