package com.maxelyz.core.controller.admin;

import java.io.Serializable;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.maxelyz.core.model.dao.MessageBroadcastDAO;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.MessageBroadcast;
import com.maxelyz.core.service.SecurityService;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class MessageBroadcastController implements Serializable{
    private static Log log = LogFactory.getLog(MessageBroadcastController.class);

    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>(); //use for checkbox
    private List<MessageBroadcast> messageBroadcast;
    private static String REFRESH = "messagebroadcast.xhtml?faces-redirect=true";
    private static String EDIT = "messagebroadcastedit.xhtml";
    @ManagedProperty(value = "#{messageBroadcastDAO}")
    private MessageBroadcastDAO messageBroadcastDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:messagebroadcast:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
     //   if(JSFUtil.getUserSession().getUserGroup().getRole().contains("CampaignManager"))
           // messageBroadcast = getMessageBroadcastDAO().findMessageBroadcastEntities();
       // else
            messageBroadcast = getMessageBroadcastDAO().findMessageBroadcastByUserCreate(JSFUtil.getUserSession().getUsers());
    }
    
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:messagebroadcast:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:messagebroadcast:delete");
    }
    
    public List<MessageBroadcast> getList() {
        return messageBroadcast;
    }

    public String editAction() {
       return EDIT;
    }

    public String deleteAction() {
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    getMessageBroadcastDAO().destroy(item.getKey());
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<MessageBroadcast> getMessageBroadcast() {
        return messageBroadcast;
    }

    public void setMessageBroadcast(List<MessageBroadcast> messageBroadcast) {
        this.messageBroadcast = messageBroadcast;
    }

    public MessageBroadcastDAO getMessageBroadcastDAO() {
        return messageBroadcastDAO;
    }

    public void setMessageBroadcastDAO(MessageBroadcastDAO messageBroadcastDAO) {
        this.messageBroadcastDAO = messageBroadcastDAO;
    }
    
    public String changPriority(Integer priority){
        
        if(priority == 1)
            return "Highest";
        else if(priority == 2)
                 return "High";
             else if(priority == 3)
                 return "Normal";
             else if(priority == 4)
                 return "Low";
             else
                 return "Lowest";
    }
}
