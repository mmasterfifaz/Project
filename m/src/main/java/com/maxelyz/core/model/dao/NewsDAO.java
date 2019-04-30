/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.News;
import com.maxelyz.core.model.entity.NewsUserPK;
import com.maxelyz.core.model.entity.Users;
import com.maxelyz.core.model.value.front.campaign.NewsUserValue;
import com.maxelyz.utils.JSFUtil;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class NewsDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(News news) {
        em.persist(news);
    }

    public void edit(News news) throws NonexistentEntityException, Exception {
        news = em.merge(news);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        News news;
        try {
            news = em.getReference(News.class, id);
            news.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The news with id " + id + " no longer exists.", enfe);
        }
        em.remove(news);
    }

    public List<News> findNewsEntities() {
        return findNewsEntities(true, -1, -1);
    }

    public List<News> findNewsEntities(int maxResults, int firstResult) {
        return findNewsEntities(false, maxResults, firstResult);
    }

    private List<News> findNewsEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from News as o where o.enable = true order by o.id desc");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public News findNews(Integer id) {
        return em.find(News.class, id);
    }

    public int getNewsCount() {
        return ((Long) em.createQuery("select count(o) from News as o").getSingleResult()).intValue();
    }
    
    public int checkTitleName(String title, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from News as o where o.title =:title and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from News as o where o.title =:title and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("title", title);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public List<News> findNewsByUserCreate(Users user) {
        Query q;
        if(user.getUserGroup().getRole().contains("CampaignManager")) {
            q = em.createQuery("select object(o) from News as o where o.enable = true and o.status = true and (o.createByUser = ?1 "
                    + "or o.createByUser.userGroup.parentId = ?2) order by o.startDate ");
            q.setParameter(2, user.getUserGroup().getId()); 
        } else {
            q = em.createQuery("select object(o) from News as o where o.enable = true and o.status = true and o.createByUser =?1 order by o.startDate ");
        }
        q.setParameter(1, user);
        return q.getResultList();
    }
    
    public List<News> SearchNewsByUserCreate(Users user, String title, Date dateFrom, Date dateTo, String status) {
        SimpleDateFormat sdfFrom = new SimpleDateFormat("yyyy-MM-dd 00:00:00", Locale.US);
        SimpleDateFormat sdfTo = new SimpleDateFormat("yyyy-MM-dd 23:59:59", Locale.US);

        String sql="";
        if(user.getUserGroup().getRole().contains("CampaignManager")) {
            sql += "select object(o) from News as o where o.enable = true and (o.createByUser = ?1 "
                    + "or o.createByUser.userGroup.parentId = "+user.getUserGroup().getId()+") ";
        } else {
            sql += "select object(o) from News as o where o.enable = true and o.createByUser =?1 ";
        }
        if(dateFrom != null && dateTo != null){
            sql += " and ("
                + "   (('"+sdfFrom.format(dateFrom)+"'  >= o.startDate and '"+sdfFrom.format(dateFrom)+"'  <= o.endDate) or ('"+sdfTo.format(dateTo)+"' >= o.startDate and '"+sdfTo.format(dateTo)+"' <= o.endDate))"
                + "   or"
                + "   ((o.startDate >= '"+sdfFrom.format(dateFrom)+"' and o.startDate <= '"+sdfTo.format(dateTo)+"') or (o.endDate >= '"+sdfFrom.format(dateFrom)+"' and o.endDate <= '"+sdfTo.format(dateTo)+"'))"
                + "  )";
         } 
         if(dateFrom == null && dateTo == null && title.isEmpty() && status.isEmpty()){
            sql += " and (o.startDate <= GETDATE() and o.endDate >= GETDATE())";
         }
        if(!title.isEmpty()) {
            sql += " and o.title like '%"+title+"%' ";
        }
        if(!status.isEmpty()){
            if(status.equals("enable")){
                sql += " and o.status = true ";
            }else if(status.equals("disable")){
                sql += " and o.status = false ";
            }
        }
        sql += " order by o.startDate ";
        Query q = em.createQuery(sql);
        q.setParameter(1, user);
        return q.getResultList();
    }
    
    public List<NewsUserValue> findNewsByUser(Users user) {
        Query q = em.createQuery("select new " + NewsUserValue.class.getName() + " (o, u.viewStatus, u.viewDate) "
                + "from News as o "
                + "join o.newsUserCollection u "
                + "where o.enable = true and o.status = true and (u.users = ?1) "
                + "and o.startDate <= ?2 and o.endDate >= ?2 "
                + "order by o.startDate desc ");
        q.setParameter(1, user); 
        q.setParameter(2, JSFUtil.toDateWithoutTime(new Date()));
        return q.getResultList();
    }
    
    public void updateViewStatus(NewsUserPK newsUserPK) throws NonexistentEntityException, Exception {
        Query q = em.createQuery("update NewsUser set viewStatus = 1, "
                + "viewDate = :viewDate "
                + "where newsUserPK = :newsUserPK and  viewStatus = 0 ");
         q.setParameter("viewDate", new Date());
         q.setParameter("newsUserPK", newsUserPK);
         q.executeUpdate();
    }
}
