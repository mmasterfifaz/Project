/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

/**
 *
 * @author admin
 */
import com.maxelyz.core.model.dao.MarketingCriteriaDAO;
import com.maxelyz.core.model.entity.MarketingCriteria;
import com.maxelyz.utils.JSFUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;

@ManagedBean
@RequestScoped
@ViewScoped
public class MarketingCriteriaController {
    private static Logger log = Logger.getLogger(MarketingCriteriaController.class);
    
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private Integer marketingId;
    private List<MarketingCriteria> marketingCriteriaList;
    
    @ManagedProperty(value = "#{marketingCriteriaDAO}")
    private MarketingCriteriaDAO marketingCriteriaDAO;
    
    @PostConstruct
    public void initialize() {
        try {
            marketingId = Integer.parseInt(JSFUtil.getRequestParameterMap("marketingId"));
            if(marketingId != null && marketingId != 0){
                marketingCriteriaList = marketingCriteriaDAO.findByMarketingId(marketingId);
            }
        }
        catch(Exception e){
            //log.equals(e.toString());
        }
    
    }

    public MarketingCriteriaDAO getMarketingCriteriaDAO() {
        return marketingCriteriaDAO;
    }

    public void setMarketingCriteriaDAO(MarketingCriteriaDAO marketingCriteriaDAO) {
        this.marketingCriteriaDAO = marketingCriteriaDAO;
    }

    public Integer getMarketingId() {
        return marketingId;
    }

    public void setMarketingId(Integer marketingId) {
        this.marketingId = marketingId;
    }

    public List<MarketingCriteria> getMarketingCriteriaList() {
        return marketingCriteriaList;
    }

    public void setMarketingCriteriaList(List<MarketingCriteria> marketingCriteriaList) {
        this.marketingCriteriaList = marketingCriteriaList;
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }
    
}
