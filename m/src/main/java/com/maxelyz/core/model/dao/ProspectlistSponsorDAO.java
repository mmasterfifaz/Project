/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.ProspectlistSponsor;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ProspectlistSponsorDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(ProspectlistSponsor prospectlistSponsor) throws PreexistingEntityException, Exception {
        em.persist(prospectlistSponsor);
    }

    public void edit(ProspectlistSponsor prospectlistSponsor) throws NonexistentEntityException, Exception {
        prospectlistSponsor = em.merge(prospectlistSponsor);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        ProspectlistSponsor prospectlistSponsor;
        try {
            prospectlistSponsor = em.getReference(ProspectlistSponsor.class, id);
            prospectlistSponsor.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The prospectlistSponsor with id " + id + " no longer exists.", enfe);
        }
        em.remove(prospectlistSponsor);
    }

    public List<ProspectlistSponsor> findProspectlistSponsorEntities() {
        return findProspectlistSponsorEntities(true, -1, -1);
    }

    public List<ProspectlistSponsor> findProspectlistSponsorEntities(int maxResults, int firstResult) {
        return findProspectlistSponsorEntities(false, maxResults, firstResult);
    }

    private List<ProspectlistSponsor> findProspectlistSponsorEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from ProspectlistSponsor as o where enable = true");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public List<ProspectlistSponsor> findProspectlistSponsorByName(String nameKeyword) {
        String sql = "select object(o) from ProspectlistSponsor as o where enable = true";
        if(!nameKeyword.isEmpty()){
            sql += " and o.name like :nameKeyword";
        }
        Query q = em.createQuery(sql);
        if(!nameKeyword.isEmpty()){
            q.setParameter("nameKeyword", "%" + nameKeyword + "%");
        }
        return q.getResultList();
    }

    public ProspectlistSponsor findProspectlistSponsor(Integer id) {
        return em.find(ProspectlistSponsor.class, id);
    }

    public int getProspectlistSponsorCount() {
        return ((Long) em.createQuery("select count(o) from ProspectlistSponsor as o").getSingleResult()).intValue();
    }

    public Map<String, Integer> getProspectlistSponsorList() {
        List<ProspectlistSponsor> prospectlistSponsor = this.findProspectlistSponsorEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (ProspectlistSponsor obj : prospectlistSponsor) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }

    public Map<String, Integer> getProspectlistSponsorMap() {
        List<ProspectlistSponsor> prospectlistSponsors = this.findProspectlistSponsorEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (ProspectlistSponsor obj : prospectlistSponsors) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
    
    public int checkProspectListName(String name, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from ProspectlistSponsor as o where o.name =:name and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from ProspectlistSponsor as o where o.name =:name and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("name", name);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public List<ProspectlistSponsor> findProspectListSponsorGetDropdown() {
        Query q = em.createQuery("SELECT object(p)  FROM ProspectlistSponsor as p where p.enable = true order by p.name");
        return q.getResultList();
    }
}
