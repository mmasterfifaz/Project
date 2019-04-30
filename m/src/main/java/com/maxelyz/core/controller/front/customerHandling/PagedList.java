/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.front.customerHandling;

import com.maxelyz.core.model.entity.ProductPlan;
import java.util.AbstractList;
import java.util.List;

/**
 *
 * @author admin
 */
public final class PagedList extends AbstractList<ProductPlan> {
 
    private final List<ProductPlan> l;
    private final int total;
    private final int pageLimit;
 
    public PagedList(List<ProductPlan> l, int total, int pageLimit) {
        this.l = l;
        this.total = total;
        this.pageLimit = pageLimit;
    }
 
    public int size() {
        return total;
    }
 
    public ProductPlan get(int i) {
        i = i % this.pageLimit;
        return l.get(i);
    }
}
