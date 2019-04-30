/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.CardExclusionDAO;
import com.maxelyz.core.model.entity.CardExclusion;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
/**
 *
 * @author admin
 */
@ManagedBean
@RequestScoped
public class CardExclusionController implements Serializable{
    private static Logger log = Logger.getLogger(CardExclusionController.class);
    private static String REFRESH = "cardexclusion.xhtml?faces-redirect=true";
    private static String EDIT = "cardexclusionedit.xhtml";
  
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<CardExclusion> cardExclusions;
    private CardExclusion cardExclusion;
    
    @ManagedProperty(value="#{cardExclusionDAO}")
    private CardExclusionDAO cardExclusionDAO;

    @PostConstruct
    public void initialize() {        
        if (!SecurityUtil.isPermitted("admin:cardexclusion:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        CardExclusionDAO dao = cardExclusionDAO;
        cardExclusions = dao.findCardExclusionEntities();       
        
    }
       
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:cardexclusion:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:cardexclusion:delete");
    }
    
    public List<CardExclusion> getList() {
        return cardExclusions;
    }

     public String editAction() {
       return EDIT;
    }

    public String deleteAction() {
        CardExclusionDAO dao = cardExclusionDAO;
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    cardExclusion = dao.findCardExclusion(item.getKey());
                    cardExclusion.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    cardExclusion.setUpdateDate(new Date());
                    cardExclusion.setEnable(false);
                    dao.edit(cardExclusion);
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

    public void setCardExclusion(List<CardExclusion> cardExclusions) {
        this.cardExclusions = cardExclusions;
    }

    public void setCardExclusionDAO(CardExclusionDAO cardExclusionDAO) {
        this.cardExclusionDAO = cardExclusionDAO;
    }
}
