/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.controller.front.campaign;

import com.maxelyz.core.model.entity.ContactHistory;
import com.maxelyz.utils.JSFUtil;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;
import java.io.Serializable;
/**
 *
 * @author admin
 */
@ManagedBean
@RequestScoped
@ViewScoped
public class PopupContactHistoryController {
    private static Logger log = Logger.getLogger(PopupContactHistoryController.class);
    private static String SUCCESS = "/front/campaign/popupContactHistory.xhtml";
    private Integer customerId;

    private List<ContactHistory> contactHistoryList = new ArrayList<ContactHistory>();

    @PostConstruct
    public void initialize() {
        if(customerId != null){
            System.out.println(customerId);
        }

    }

    public void contactHistoryListener(ActionEvent event) {
        if (customerId != null) {
            System.out.println(customerId);
        }

    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public List<ContactHistory> getContactHistoryList() {
        return contactHistoryList;
    }

    public void setContactHistoryList(List<ContactHistory> contactHistoryList) {
        this.contactHistoryList = contactHistoryList;
    }
    
    
}
