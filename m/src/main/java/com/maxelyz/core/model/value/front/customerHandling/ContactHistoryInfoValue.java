/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.value.front.customerHandling;

import com.maxelyz.core.model.entity.ContactHistory;
import java.util.List;

/**
 *
 * @author admin
 */
public class ContactHistoryInfoValue {
    
    private ContactHistory contactHistory;
    private List<ContactCaseInfoValue> contactCaseInfoValueList;
    
    public ContactHistoryInfoValue(){
        
    }
    
    public ContactHistoryInfoValue(ContactHistory contactHistory, List<ContactCaseInfoValue> contactCaseInfoValueList){
        this.contactHistory = contactHistory;
        this.contactCaseInfoValueList = contactCaseInfoValueList;
    }

    public List<ContactCaseInfoValue> getContactCaseInfoValueList() {
        return contactCaseInfoValueList;
    }

    public void setContactCaseInfoValueList(List<ContactCaseInfoValue> contactCaseInfoValueList) {
        this.contactCaseInfoValueList = contactCaseInfoValueList;
    }

    public ContactHistory getContactHistory() {
        return contactHistory;
    }

    public void setContactHistory(ContactHistory contactHistory) {
        this.contactHistory = contactHistory;
    }
    
}
