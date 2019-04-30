/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.NewsDAO;
import com.maxelyz.core.model.entity.News;
import com.maxelyz.core.model.entity.NewsUser;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 *
 * @author Administrator
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class NewsUsersController {
    
    private static Log log = LogFactory.getLog(NewsUsersController.class);
    private static String NEWS = "news.xhtml?faces-redirect=true";
    private static String REDIRECT_PAGE = "news.jsf";
    private News news;
    private List<NewsUser> newsUserList;
        
    @ManagedProperty(value = "#{newsDAO}")
    private NewsDAO newsDAO;
        
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:news:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        if (JSFUtil.getRequestParameterMap("id") != null) {
            Integer id = Integer.parseInt(JSFUtil.getRequestParameterMap("id"));
            news = newsDAO.findNews(id);
            newsUserList = (List)news.getNewsUserCollection();
        } else {
            JSFUtil.redirect(REDIRECT_PAGE);
        }
    }

    public String backAction(){
        return NEWS;
    }
        
    public NewsDAO getNewsDAO() {
        return newsDAO;
    }

    public void setNewsDAO(NewsDAO newsDAO) {
        this.newsDAO = newsDAO;
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public List<NewsUser> getNewsUserList() {
        return newsUserList;
    }

    public void setNewsUserList(List<NewsUser> newsUserList) {
        this.newsUserList = newsUserList;
    }
      
}
