/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.CardExclusion;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
/**
 *
 * @author admin
 */
@Transactional
public class CardExclusionDAO {
    
    @PersistenceContext
    private EntityManager em;

    public void create(CardExclusion cardExclusion ) throws PreexistingEntityException, Exception {
        em.persist(cardExclusion);
    }

    public void edit(CardExclusion cardExclusion) throws NonexistentEntityException, Exception {
        cardExclusion = em.merge(cardExclusion);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        CardExclusion cardExclusion;
        try {
            cardExclusion = em.getReference(CardExclusion.class, id);
            cardExclusion.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The cardExclusion with id " + id + " no longer exists.", enfe);
        }
        em.remove(cardExclusion);
    }

    public List<CardExclusion> findCardExclusionEntities() {
        return findCardExclusionEntities(true, -1, -1);
    }

    public List<CardExclusion> findCardExclusionEntities(int maxResults, int firstResult) {
        return findCardExclusionEntities(false, maxResults, firstResult);
    }

    private List<CardExclusion> findCardExclusionEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from CardExclusion as o where o.enable = true order by name");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }
  
    public List<CardExclusion> findCardExclusionByName(String nameKeyword) {
        Query q = em.createQuery("select object(o) from CardExclusion as o where o.name like ?1 and o.enable = true order by name");
        q.setParameter(1, "%"+nameKeyword+"%");
        return q.getResultList();
    }

    public CardExclusion findCardExclusion(Integer id) {
        return em.find(CardExclusion.class, id);
    }

    public int getCardExclusionCount() {
        return ((Long) em.createQuery("select count(o) from CardExclusion as o").getSingleResult()).intValue();
    }
    
    public int checkCardExclusionName(String name, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from CardExclusion as o where o.name =:name and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from CardExclusion as o where o.name =:name and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("name", name);
        return ((Long) q.getSingleResult()).intValue();
    }
}
