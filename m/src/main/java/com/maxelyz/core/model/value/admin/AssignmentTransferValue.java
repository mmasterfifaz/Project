/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.value.admin;

import com.maxelyz.core.model.entity.Users;
import java.util.Date;
/**
 *
 * @author Administrator
 */
public class AssignmentTransferValue {
   
    private Date transferDate;
    private int noCustomer;
    private Users toUser;
    private String createBy;
    

    public AssignmentTransferValue() {
    }
    
    public AssignmentTransferValue(Date transferDate, Integer noCustomer, Users toUser, String createBy) {
        this.transferDate = transferDate;
        this.noCustomer = noCustomer;
        this.toUser = toUser;
        this.createBy = createBy;
    }
    
    public Users getToUser() {
        return toUser;
    }

    public void setToUser(Users toUser) {
        this.toUser = toUser;
    }
    
    public Date getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }
    
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    
    public Integer getNoCustomer() {
        return noCustomer;
    }

    public void setNoCustomer(Integer noCustomer) {
        this.noCustomer = noCustomer;
    }

}
