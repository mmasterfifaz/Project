/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.value.front.campaign;

import com.maxelyz.core.model.entity.News;
import java.util.Date;

/**
 *
 * @author Administrator
 */
public class NewsUserValue {
    private News news;
    private Boolean viewStatus;
    private Date viewDate;
    
    public NewsUserValue(News news, Boolean viewStatus, Date viewDate){ 
        this.news = news;
        this.viewStatus = viewStatus;
        this.viewDate = viewDate;
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public Date getViewDate() {
        return viewDate;
    }

    public void setViewDate(Date viewDate) {
        this.viewDate = viewDate;
    }

    public Boolean getViewStatus() {
        return viewStatus;
    }

    public void setViewStatus(Boolean viewStatus) {
        this.viewStatus = viewStatus;
    }
    
}
