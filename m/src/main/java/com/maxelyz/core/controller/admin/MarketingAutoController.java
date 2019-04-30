/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.MarketingAutoDAO;
import com.maxelyz.core.model.entity.MarketingAuto;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import org.apache.log4j.Logger;

/**
 *
 * @author support
 */
@ManagedBean
@RequestScoped
public class MarketingAutoController {

    private static Logger log = Logger.getLogger(MarketingAutoController.class);
    private static final String REFRESH = "marketingauto.xhtml?faces-redirect=true";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private static final String EDIT = "marketingautoedit.xhtml";
    private List<MarketingAuto> marketingAutos;
    private MarketingAuto marketingAuto;

    @ManagedProperty(value = "#{marketingAutoDAO}")
    private MarketingAutoDAO marketingAutoDAO;

    @PostConstruct
    public void initialize() {

        if (!SecurityUtil.isPermitted("admin:marketingauto:view")) {
            SecurityUtil.redirectUnauthorize();
        }

        MarketingAutoDAO dao = getMarketingAutoDAO();
        marketingAutos = dao.findMarketingAutoEntities();

    }
   
    public String deleteAction() throws Exception {
        MarketingAutoDAO dao = getMarketingAutoDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    marketingAuto = dao.findMarketingAutoId(item.getKey());
                    marketingAuto.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    marketingAuto.setUpdateDate(new Date());
                    marketingAuto.setEnable(false);
                    dao.edit(marketingAuto);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }

    public String editAction() {
        return EDIT;
    }
    
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:marketingauto:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:marketingauto:delete");
    }

    public List<MarketingAuto> getList() {
        return getMarketingAutos();
    }

    public List<MarketingAuto> getMarketingAutos() {
        return marketingAutos;
    }

    public void setMarketingAutos(List<MarketingAuto> marketingAutos) {
        this.marketingAutos = marketingAutos;
    }

    public MarketingAuto getMarketingAuto() {
        return marketingAuto;
    }

    public void setMarketingAuto(MarketingAuto marketingAuto) {
        this.marketingAuto = marketingAuto;
    }

    public MarketingAutoDAO getMarketingAutoDAO() {
        return marketingAutoDAO;
    }

    public void setMarketingAutoDAO(MarketingAutoDAO marketingAutoDAO) {
        this.marketingAutoDAO = marketingAutoDAO;
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

}
