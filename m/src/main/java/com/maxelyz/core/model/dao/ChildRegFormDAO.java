/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.ChildRegForm;
import com.maxelyz.core.model.entity.ChildRegType;
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
 * @author Songwisit
 */
@Transactional
public class ChildRegFormDAO {
    
    @PersistenceContext
    private EntityManager em;

    public void create(ChildRegForm childRegForm) throws PreexistingEntityException, Exception {
        em.persist(childRegForm);
    }

    public void edit(ChildRegForm childRegForm) throws IllegalOrphanException, NonexistentEntityException, Exception {
        childRegForm = em.merge(childRegForm);
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        ChildRegForm childRegForm;
        try {
            childRegForm = em.getReference(ChildRegForm.class, id);
            childRegForm.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The child register form with id " + id + " no longer exists.", enfe);
        }
        em.remove(childRegForm);
    }
    
    public ChildRegForm findChildRegForm(Integer id) {
        return em.find(ChildRegForm.class, id);
    }
    
    public List<ChildRegForm> findChildRegFormEntities() {
        return findChildRegFormEntities(true, -1, -1);
    }

    public List<ChildRegForm> findChildRegFormEntities(int maxResults, int firstResult) {
        return findChildRegFormEntities(false, maxResults, firstResult);
    }

    private List<ChildRegForm> findChildRegFormEntities(boolean all, int maxResults, int firstResult) {
        try {
            Query q = em.createQuery("select object(o) from ChildRegForm as o where o.enable = true order by o.code, o.name");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } catch(Exception ex) {
            return null;
        }
    }
    
    public List<ChildRegForm> findChildRegFormByName(String nameKeyword) {
        Query q = em.createNamedQuery("ChildRegForm.findByName");
        q.setParameter("name", nameKeyword);
        return q.getResultList();
    }
    
    public int checkChildRegFormName(String name, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from ChildRegForm as o where o.name =:name and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from ChildRegForm as o where o.name =:name and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("name", name);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public int checkChildRegFormCode(String code, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from ChildRegForm as o where o.code =:code and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from ChildRegForm as o where o.code =:code and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("code", code);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public Map<String, String> getMergeDataViewList() {
        List<Object> doc = this.getMergeDataViewTable();
        Map<String, String> values = new LinkedHashMap<String, String>();
        for (Object obj : doc) {
            values.put(obj.toString(), obj.toString());
        }
        return values;
    }
        
    public List<Object> getMergeDataViewTable()
    {
        Query q = em.createNativeQuery("select name from sys.views where name like 'vwChildReg%' ");
//        List<Object[]> l = q.getResultList();
        return q.getResultList();
    }
    
    public List<String> findChildRegFormListById(String idList) {
        
//        String[] list = idList.split(",");
        
        
        Query q = em.createQuery("select o.name from ChildRegForm as o where o.enable = true and o.id IN ("+idList+") order by o.name");
//        q.setParameter(1, idList);
        return q.getResultList();
    }
    
    public List<ChildRegForm> findChildRegFormListByChildRegTypeId(Integer childRegTypeId) {
        
//        String[] list = idList.split(",");
        
        
        Query q = em.createQuery("select object(o) from ChildRegForm as o where o.enable = true and o.childRegType.id =:childRegTypeId order by o.name");
        q.setParameter("childRegTypeId", childRegTypeId);
        return q.getResultList();
    }
    
}