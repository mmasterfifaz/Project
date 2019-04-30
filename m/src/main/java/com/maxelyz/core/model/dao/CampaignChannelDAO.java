/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.CampaignChannel;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author support
 */
@Transactional
public class CampaignChannelDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(CampaignChannel campaignChannel) throws PreexistingEntityException, Exception {
//        this.validate(followupsaleReason);
        em.persist(campaignChannel);
    }

    public void edit(CampaignChannel campaignChannel) throws NonexistentEntityException, Exception {
        campaignChannel = em.merge(campaignChannel);
    }

    /*
    public void destroy(Integer id) throws NonexistentEntityException {
        CampaignChannel campaignChannel;
        try {
            campaignChannel = em.getReference(CampaignChannel.class, id);
            campaignChannel.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The campaign Channel with id " + id + " no longer exists.", enfe);
        }
        em.remove(campaignChannel);
    }
     */
    public CampaignChannel findCampaignChannel(Integer id) {
        return em.find(CampaignChannel.class, id);
    }

    public List<CampaignChannel> findCampaignChannelEntities() {
        return findCampaignChannelEntities(true, -1, -1);
    }

    public List<CampaignChannel> findCampaignChannelEntities(int maxResults, int firstResult) {
        return findCampaignChannelEntities(false, maxResults, firstResult);
    }

    private List<CampaignChannel> findCampaignChannelEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from CampaignChannel as o where o.enable = true order by o.code");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public int checkCampaignChannelCode(String code, Integer id) {
        Query q;
        if (id == 0) {
            q = em.createQuery("select count(o) from CampaignChannel as o where o.code =:code and o.enable = true");
        } else {
            q = em.createQuery("select count(o) from CampaignChannel as o where o.code =:code and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("code", code);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public List<CampaignChannel> findCampaignChannelDropDown() {
        Query q = em.createQuery("select object(o) from CampaignChannel as o where o.enable=true and o.status=true order by o.code");
        return q.getResultList();
    }

    /* 
    public int checkCampiagnChannelName(String name, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from CampaignChannel as o where o.name =:name and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from CampaignChannel as o where o.name =:name and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("name", name);
        return ((Long) q.getSingleResult()).intValue();
    } */
}
