/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.front.notification;

import com.maxelyz.core.common.front.dispatch.FrontDispatcher;
import com.maxelyz.core.common.front.dispatch.RegistrationPouch;
import com.maxelyz.core.model.dao.ApprovalReasonDAO;
import com.maxelyz.core.model.dao.NotificationDAO;
import com.maxelyz.core.model.entity.Notification;
import com.maxelyz.utils.JSFUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;

/**
 *
 * @author ukritj
 */
@ManagedBean
@RequestScoped
@ViewScoped
public class NotificationController {
    private static Logger log = Logger.getLogger(NotificationController.class);
    private static String REFRESH = "/front/notification/notification.xhtml?faces-redirect=true";
    private List<Notification> notificationList;
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private int page = 1;
    private String status;
    private String type;
    private String emessage = null;
    
    @ManagedProperty(value="#{notificationDAO}")
    private NotificationDAO notificationDAO;
    
    @PostConstruct
    public void initialize() {
        try {
            JSFUtil.getUserSession().resetProducts();
            JSFUtil.getUserSession().resetPurchaseOrders();
            JSFUtil.getUserSession().removeCustomerSession(); //remove customer session when exit
            JSFUtil.getUserSession().removeContactHistory();
            JSFUtil.getUserSession().setHasClick(false);
        } catch (Exception e) {
            log.error(e);
        }
        search();
    }
    
    private void search(){
        
        Boolean sta = null;
        if(status != null && status.equals("1")){
            sta = true;
        }else if(status != null && status.equals("0")){
            sta = false;
        }else if(status == null || status.equals("")){
            sta = null;
        }
        notificationList = notificationDAO.findNewNotification(JSFUtil.getUserSession().getUsers().getId(), sta, type);
    }
    
    public void resetValueActionListener(ActionEvent event){
        try {
            JSFUtil.getUserSession().resetProducts();
            JSFUtil.getUserSession().resetPurchaseOrders();
            JSFUtil.getUserSession().removeCustomerSession(); //remove customer session when exit
            JSFUtil.getUserSession().removeContactHistory();
            JSFUtil.getUserSession().setHasClick(false);
        } catch (Exception e) {
            log.error(e);
        }
    }
    
    public void searchActionListener(ActionEvent event){
         page = 1;
         search();    
    }
    
    public String gotoNotification(){
        return REFRESH;
    }
    
    public void clickActionListener(ActionEvent event){
         String str = JSFUtil.getRequestParameterMap("paramId");
         if(str != null && !str.equals("") && !str.equals("0")){
             Integer id = Integer.parseInt(str);
             Notification noti = notificationDAO.findNotification(id);
             noti.setStatus(Boolean.FALSE);
             notificationDAO.edit(noti);
         }
         this.search();
    }
     
     public String toRegistrationForm(Integer purchaseOrderId) {
        this.emessage   = null;
        try{  
            RegistrationPouch pouch = new RegistrationPouch(); // create pouch to carry data for purchase order            
            pouch.registeredPouchType(RegistrationPouch.SENDER_TYPE.NOTIFICATION, RegistrationPouch.RECEIVER_MODE.EDIT);
            pouch.putEditModeParameter(purchaseOrderId);
            FrontDispatcher.keepPouch(pouch); // registered pouch on dispatcher
            return FrontDispatcher.getPouchReceiver(pouch); // get receiver
        } catch (Exception e) {
            log.error("Error when dispatch from notification to registration form : "+e.getMessage());
            this.emessage  = "Cannot forward to registration form. [Cause: "+e.getMessage()+"] ";
            return null;
        }        
    } 

    public String getEmessage() {
        return emessage;
    }

    public String deleteAction() {
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    Notification noti = notificationDAO.findNotification(item.getKey());
                    noti.setEnable(false);
                    notificationDAO.edit(noti);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }

    public List<Notification> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    public NotificationDAO getNotificationDAO() {
        return notificationDAO;
    }

    public void setNotificationDAO(NotificationDAO notificationDAO) {
        this.notificationDAO = notificationDAO;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }
    
}
