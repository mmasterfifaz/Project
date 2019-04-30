/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.NewsDAO;
import com.maxelyz.core.model.entity.News;
import com.maxelyz.utils.JSFUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;
import com.maxelyz.core.service.SecurityService;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import javax.faces.event.ActionEvent;

/**
 *
 * @author admin
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class NewsController {

    private static Log log = LogFactory.getLog(NewsController.class);
    private static String EDIT = "/admin/newsedit.xhtml";
    private static String VIEW = "/admin/newsusers.xhtml";
    private static String REFRESH = "/admin/news.xhtml?faces-redirect=true";

    private List<News> newsList;
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();

    //search
    private String title = "";
    private Date dateFrom;
    private Date dateTo;
    private String status = "";
    
    @ManagedProperty(value = "#{newsDAO}")
    private NewsDAO newsDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:news:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
        newsList = newsDAO.findNewsByUserCreate(JSFUtil.getUserSession().getUsers());
    }

    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:news:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:news:delete");
    }
    
    public String editAction() {
        return EDIT;
    }
    
    public String viewAction() {
       return VIEW;
    }
    
    public String deleteAction() {
        NewsDAO dao = getNewsDAO();
        News news = null;
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    news = dao.findNews(item.getKey());
                    news.setUpdateBy(JSFUtil.getUserSession().getUsers().getLoginName());
                    news.setUpdateDate(new Date());
                    news.setEnable(false);
                    dao.edit(news);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }
    
    public void searchActionListener(ActionEvent event){
        this.search();
    }
    
    private void search(){
        newsList = newsDAO.SearchNewsByUserCreate(JSFUtil.getUserSession().getUsers(),title, dateFrom, dateTo, status);
//        newsList = newsDAO.findNewsByUserCreate(JSFUtil.getUserSession().getUsers());
    }
    
    public NewsDAO getNewsDAO() {
        return newsDAO;
    }

    public void setNewsDAO(NewsDAO newsDAO) {
        this.newsDAO = newsDAO;
    }

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    
}
