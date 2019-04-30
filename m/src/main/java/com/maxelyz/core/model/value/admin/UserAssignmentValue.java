package com.maxelyz.core.model.value.admin;

import com.maxelyz.core.model.entity.Users;

/**
 *
 * @author Oat.
 */
public class UserAssignmentValue {
    private boolean selected;
    private Users user;
    private Long record; //Assigned Marketing Customer Records
    private Long totals; 
    private Long new1; 
    private Long used; 
    private Long simulate; 

    public UserAssignmentValue() {
    }

    public UserAssignmentValue(Users user) {
        this(false,user,null);
    }

    public UserAssignmentValue(Users user, Long record) {
        this(false,user,record);
    }

    public UserAssignmentValue(Users user, int record) {
        this(false,user,new Long(record));
    }
    
    public UserAssignmentValue(boolean selected, Users user, Long record) {
        this.selected = selected;
        this.user = user;
        this.record = record;
    }
    
    public UserAssignmentValue(Users user, Long totals, int record) {
        this.user = user;
        this.totals = totals;
        this.record = (long) record;
    }       

    public UserAssignmentValue(Users user, Long totals, Long new1, Long used, int record) {
        this.user = user;
        this.totals = totals;
        this.new1 = new1;
        this.used = used;
        this.record = (long) record;
    }   
        
    public UserAssignmentValue(boolean selected, Users user, Long record, Long totals) {
        this.selected = selected;
        this.user = user;
        this.record = record;
        this.totals = totals;                
    }
    
    public UserAssignmentValue(boolean selected, Users user, Long record, Long totals, Long new1, Long used) {
        this.selected = selected;
        this.user = user;
        this.record = record;
        this.totals = totals;   
        this.new1 = new1;
        this.used = used;
    }
    
    public UserAssignmentValue(boolean selected, Users user, Long record, Long totals, Long simulate) {
        this.selected = selected;
        this.user = user;
        this.record = record;
        this.totals = totals;
        if(totals == null) {
            this.simulate = 0 + record;
        } else {
            this.simulate = totals + record;
        }
    }
    
    public UserAssignmentValue(boolean selected, Users user, Long record, Long totals, Long new1, Long used, Long simulate) {
        this.selected = selected;
        this.user = user;
        this.record = record;
        this.totals = totals;
        this.new1 = new1;
        this.used = used;
        if(totals == null) {
            this.simulate = 0 + record;
        } else {
            this.simulate = totals + record;
        }
    }
        
    public Long getRecord() {
        return record;
    }

    public void setRecord(Long record) {
        this.record = record;
    }
           
    public Long getTotals() {
        return totals;
    }

    public void setTotals(Long totals) {
        this.totals = totals;
    }
    
    public Long getNew1() {
        return new1;
    }

    public void setNew1(Long new1) {
        this.new1 = new1;
    }
    
    public Long getUsed() {
        return used;
    }

    public void setUsed(Long used) {
        this.used = used;
    }
    
    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Long getSimulate() {
        return simulate;
    }

    public void setSimulate(Long simulate) {
        this.simulate = simulate;
    }
}
