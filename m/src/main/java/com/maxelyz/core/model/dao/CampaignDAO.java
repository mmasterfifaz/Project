/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.*;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.core.model.value.admin.CampaignValue;
import com.maxelyz.utils.JSFUtil;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class CampaignDAO   {
 
    @PersistenceContext
    private EntityManager em;
    
    public void create(Campaign campaign) {
        em.persist(campaign);
    }

    public void edit(Campaign campaign) {
        campaign = em.merge(campaign);   
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        Campaign campaign;
        try {
            campaign = em.getReference(Campaign.class, id);
            campaign.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The Campaign with id " + id + " no longer exists.", enfe);
        }
        em.remove(campaign);
    }

    public List<Campaign> findCampaignEntities() {
        return findCampaignEntities(true, -1, -1);
    }

    public List<Campaign> findCampaignEntities(int maxResults, int firstResult) {
        return findCampaignEntities(false, maxResults, firstResult);
    }

    private List<Campaign> findCampaignEntities(boolean all, int maxResults, int firstResult) {   
        Query q = em.createQuery("select object(o) from Campaign as o where enable=true and startDate <= ?1 and endDate >= ?1");
        q.setParameter(1, JSFUtil.toDateWithoutTime(new Date()));
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public List<Campaign> findAll() {
        Query q = em.createQuery("select object(o) from Campaign as o where enable=true order by o.createDate desc");
        return q.getResultList();
    }

    public Campaign findCampaign(Integer id) {
        Campaign c = em.find(Campaign.class, id);
        c.getProductCollection().size();
        return  c;
    }

    public int getCampaignCount() {
        Query q = em.createQuery("select count(o) from Campaign as o");
        return ((Long) q.getSingleResult()).intValue();
    }

    public Map<String, Integer> getCampaignList() {
        List<Campaign> campaigns = this.findCampaignEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Campaign obj : campaigns) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }

    public List<Campaign> findCampaignBy(Integer userId, Integer customerId) {
        String sql = "select distinct object(c) from Assignment as a"
                + " left join a.campaign c"
                + " left join a.assignmentDetailCollection ad"
                + " where c.inbound <> 1 and ad.users.id = :userId and ad.customerId.id = :customerId";
        Query q = em.createQuery(sql);
        q.setParameter("userId", userId);
        q.setParameter("customerId", customerId);
        
        return q.getResultList();
    }

    public List<Campaign> findCampaignInbound() {
        String sql = "select object(c) from Campaign c"
                + " where c.inbound = 1 and status = 1 and enable = 1"
                + " order by c.name";
        Query q = em.createQuery(sql);
        
        return q.getResultList();
    }
    
    public Map<String, Integer> getCampaignEnableList() {
        List<Campaign> campaigns = this.findCampaignEnableList();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Campaign obj : campaigns) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
        
    private List<Campaign> findCampaignEnableList() {   
        String sql = "select object(c) from Campaign c"
                + " where status = 1 and enable = 1"
                + " and (c.startDate <= GETDATE() and c.endDate >= GETDATE()) order by c.name";
        Query q = em.createQuery(sql);
        
        return q.getResultList();
    }
     
    public List<CampaignValue> searchCampaignBy(String name, Date dateFrom, Date dateTo, String status){
        SimpleDateFormat sdfFrom = new SimpleDateFormat("yyyy-MM-dd 00:00:00", Locale.US);
        SimpleDateFormat sdfTo = new SimpleDateFormat("yyyy-MM-dd 23:59:59", Locale.US);
        Date today = new Date();
        String role = "";
        if(JSFUtil.getUserSession().getUserGroup().getRole().contains("CampaignManager")){
            role = "manager";
        } else{
            role = "supervisor";
        }
        String sql = "select new " + CampaignValue.class.getName() + " (c.id,c.code,c.name,c.startDate,c.endDate,c.status,COUNT(distinct ad.customerName),"
                + "(select COUNT(listUsed) from c.campaignCustomerCollection),(select COUNT(listReused) from c.campaignCustomerCollection),c.description) "
                + " from AssignmentDetail ad"
                + " join ad.assignmentId a"
                + " right join a.campaign c"
                + " where c.enable = true";
        if(role.equals("supervisor")) {
            sql += " and c.id in (select aa.campaign.id from AssignmentSupervisor s "
                    + "join s.assignmentId aa where s.userId.id = "+ JSFUtil.getUserSession().getUsers().getId() +") ";

        }
        if(dateFrom != null && dateTo != null){
            sql += " and ("
                + "   (('"+sdfFrom.format(dateFrom)+"'  >= c.startDate and '"+sdfFrom.format(dateFrom)+"'  <= c.endDate) or ('"+sdfTo.format(dateTo)+"' >= c.startDate and '"+sdfTo.format(dateTo)+"' <= c.endDate))"
                + "   or"
                + "   ((c.startDate >= '"+sdfFrom.format(dateFrom)+"' and c.startDate <= '"+sdfTo.format(dateTo)+"') or (c.endDate >= '"+sdfFrom.format(dateFrom)+"' and c.endDate <= '"+sdfTo.format(dateTo)+"'))"
                + "  )";
         } 
         if(dateFrom == null && dateTo == null && name.isEmpty() && status.isEmpty()){
            sql += " and (c.startDate <= GETDATE() and c.endDate >= GETDATE())";
         }
        if(!name.isEmpty()) {
            sql += " and c.name like '%"+name+"%' ";
        }
        if(!status.isEmpty()){
            if(status.equals("enable")){
                sql += " and c.status = true ";
            }else if(status.equals("disable")){
                sql += " and c.status = false ";
            }
        }
        sql += " group by c.id,c.code,c.name,c.startDate,c.endDate,c.status,c.description";
        Query q = em.createQuery(sql);

        return q.getResultList();
    }

    public CampaignCustomer findCampaignCustomer(CampaignCustomerPK id) {
        return em.find(CampaignCustomer.class, id);
    }

    public void editCampaignCustomer(CampaignCustomer campaignCustomer) throws NonexistentEntityException, Exception {
        campaignCustomer = em.merge(campaignCustomer);
    }

    public void createCampaignCustomer(CampaignCustomer campaignCustomer) throws NonexistentEntityException, Exception {
        em.persist(campaignCustomer);
    }
    
    public int checkCampaignCode(String code, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from Campaign as o where o.code =:code and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from Campaign as o where o.code =:code and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("code", code);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public List<Campaign> findByMarketingInbound(Integer marketingId){
        List<Campaign> list = null;
        try{
            String sql = "select object(a) from Campaign as a"
                    + " where a.marketingInbound.id = :marketingId and a.inbound = true";
            
            Query q = em.createQuery(sql);
            q.setParameter("marketingId", marketingId);
            
            list = q.getResultList();
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
        
    public int checkMarketingInbound(Integer marketingId) { 
        Query q = em.createQuery("select count(a) from Campaign as a where a.enable = true and a.status = true and a.marketingInbound.id = :marketingId");        
       
        q.setParameter("marketingId", marketingId);
        return ((Long) q.getSingleResult()).intValue();
    }
}
