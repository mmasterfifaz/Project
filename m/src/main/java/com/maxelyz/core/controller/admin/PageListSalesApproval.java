/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.value.admin.SaleApprovalValue;
import java.util.AbstractList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class PageListSalesApproval extends AbstractList<SaleApprovalValue> {
    
    private final List<SaleApprovalValue> l;
    private final int total;
    private final int pageLimit;
 
    public PageListSalesApproval(List<SaleApprovalValue> l, int total, int pageLimit) {
        this.l = l;
        this.total = total;
        this.pageLimit = pageLimit;
    }
 
    public int size() {
        return total;
    }
 
    public SaleApprovalValue get(int i) {
        i = i % this.pageLimit;
        return l.get(i);
    }
}
