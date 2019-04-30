/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.front.notification;

import com.maxelyz.core.model.dao.NotificationDAO;
import com.maxelyz.core.model.entity.Notification;
import com.maxelyz.utils.JSFUtil;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author ukritj
 */
@ManagedBean
@RequestScoped
@ViewScoped
public class PopupFollowupController {
    
    @ManagedProperty(value="#{notificationDAO}")
    private NotificationDAO notificationDAO;
    
    private Integer id;
    
    @PostConstruct
    public void initialize() {
        
    }    
    
    public void acceptActionListener(ActionEvent event){
        if(id != null && id != 0){
            Notification noti = notificationDAO.findNotification(id);
            noti.setStatus(Boolean.FALSE);
            notificationDAO.edit(noti);
        }
    }

    public NotificationDAO getNotificationDAO() {
        return notificationDAO;
    }

    public void setNotificationDAO(NotificationDAO notificationDAO) {
        this.notificationDAO = notificationDAO;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
}
