/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;
import com.maxelyz.core.model.entity.AssignmentDetail;
import java.util.AbstractList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class PageList extends AbstractList<AssignmentDetail> {
    
    private final List<AssignmentDetail> l;
    private final int total;
    private final int pageLimit;
 
    public PageList(List<AssignmentDetail> l, int total, int pageLimit) {
        this.l = l;
        this.total = total;
        this.pageLimit = pageLimit;
    }
 
    @Override
    public int size() {
        return total;
    }
 
    @Override
    public AssignmentDetail get(int i) {
        i = i % this.pageLimit;
        return l.get(i);
    }
}
