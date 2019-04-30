/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.DeliveryMethodDAO;
import com.maxelyz.core.model.entity.DeliveryMethod;
import com.maxelyz.utils.JSFUtil;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;

/**
 *
 * @author Administrator
 */
@ManagedBean
@RequestScoped
public class DeliveryMethodController {
    
    private static Logger log = Logger.getLogger(DeliveryMethodController.class);
    private static String REFRESH = "deliverymethod.xhtml?faces-redirect=true";
    private static String EDIT = "deliverymethodedit.xhtml";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<DeliveryMethod> list;
    private DeliveryMethod deliveryMethod;
    
    @ManagedProperty(value = "#{deliveryMethodDAO}")
    private DeliveryMethodDAO deliveryMethodDAO;

    @PostConstruct
    public void initialize() {      
        
        if (!SecurityUtil.isPermitted("admin:deliverymethod:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        list = deliveryMethodDAO.findDeliveryMethodEntities();
    }

    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:deliverymethod:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:deliverymethod:delete");
    }

    public String editAction() {
        return EDIT;
    }

    public String deleteAction() {
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    deliveryMethod = deliveryMethodDAO.findDeliveryMethod(item.getKey());
                    deliveryMethod.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    deliveryMethod.setUpdateDate(new Date());
                    deliveryMethod.setEnable(false);
                    deliveryMethodDAO.edit(deliveryMethod);
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

    public List<DeliveryMethod> getList() {
        return list;
    }

    public void setList(List<DeliveryMethod> list) {
        this.list = list;
    }

    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public DeliveryMethodDAO getDeliveryMethodDAO() {
        return deliveryMethodDAO;
    }

    public void setDeliveryMethodDAO(DeliveryMethodDAO deliveryMethodDAO) {
        this.deliveryMethodDAO = deliveryMethodDAO;
    }
}
