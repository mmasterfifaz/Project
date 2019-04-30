/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.front.campaign;

import com.maxelyz.core.model.entity.PurchaseOrder;
import com.maxelyz.core.model.value.front.campaign.PendingListInfoValue;
import java.util.AbstractList;
import java.util.List;

/**
 *
 * @author admin
 */
public final class PagedPurchaseOrderList extends AbstractList<PendingListInfoValue> {
 
    private final List<PendingListInfoValue> l;
    private final int total;
    private final int pageLimit;
 
    public PagedPurchaseOrderList(List<PendingListInfoValue> l, int total, int pageLimit) {
        this.l = l;
        this.total = total;
        this.pageLimit = pageLimit;
    }
 
    public int size() {
        return total;
    }
 
    public PendingListInfoValue get(int i) {
        i = i % this.pageLimit;
        return l.get(i);
    }
}
