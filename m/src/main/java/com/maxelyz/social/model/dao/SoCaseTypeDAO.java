/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.social.model.entity.SoCaseType;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Transactional
public class SoCaseTypeDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(SoCaseType soCaseType) throws PreexistingEntityException {
//        this.validate(soCaseType);
        em.persist(soCaseType);
    }

    public void edit(SoCaseType soCaseType) throws NonexistentEntityException, Exception {
        soCaseType = em.merge(soCaseType);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        SoCaseType soCaseType;
        try {
            soCaseType = em.getReference(SoCaseType.class, id);
            soCaseType.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The soCaseType with id " + id + " no longer exists.", enfe);
        }
        em.remove(soCaseType);
    }

    public List<SoCaseType> findSoCaseTypeEntities() {
        return findSoCaseTypeEntities(true, -1, -1);
    }

    public List<SoCaseType> findSoCaseTypeEntities(int maxResults, int firstResult) {
        return findSoCaseTypeEntities(false, maxResults, firstResult);
    }

    private List<SoCaseType> findSoCaseTypeEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from SoCaseType as o where o.enable = true order by o.code");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }
    
    public void deleteCaseTopicByCaseTypeId(int caseTypeId) {
       Query q = em.createQuery("update SoCaseType set enable = false where soCaseType.id = ?1");
       q.setParameter(1, caseTypeId);
       q.executeUpdate();
    }
    
    public List<SoCaseType> findSoCaseTypeStatus() {
        Query q = em.createQuery("select object(o) from SoCaseType as o where o.enable = true and o.status = true and o.soCaseType is null order by o.code");
        return q.getResultList();
    }

    public List<SoCaseType> findSoCaseTypeList() {
        Query q = em.createQuery("select object(o) from SoCaseType as o where o.enable = true and o.soCaseType is null order by o.code");
        return q.getResultList();
    }
        
    public List<SoCaseType> findSoCaseTopicList() {
        Query q = em.createQuery("select object(o) from SoCaseType as o where o.enable = true and o.soCaseType is not null order by o.code");
        return q.getResultList();
    }
    
    public List<SoCaseType> findSoCaseTopicListById(Integer caseTypeId) {
        Query q = em.createQuery("select object(o) from SoCaseType as o where o.enable = true and o.status = true and o.soCaseType.id = ?1 order by o.code");
        q.setParameter(1, caseTypeId);
        return q.getResultList();
    }
        
    public SoCaseType findSoCaseType(Integer id) {
        return em.find(SoCaseType.class, id);
    }

    public int getSoCaseTypeCount() {
        Query q = em.createQuery("select count(o) from SoCaseType as o");
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<SoCaseType> findSoCaseTypeByCode(String code) {
        Query q = em.createQuery("select object(o) from SoCaseType as o where code = ?1");
        q.setParameter(1, code);
        return q.getResultList();
    }

    private void validate(SoCaseType soCaseType) throws PreexistingEntityException {
        if (findSoCaseTypeByCode(soCaseType.getCode()).size() > 0) {
            throw new PreexistingEntityException("Duplicate Code");
        }
    }

    public Map<String, Integer> getSoCaseTypeList() {
        List<SoCaseType> soCaseTypes = this.findSoCaseTypeStatus();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (SoCaseType obj : soCaseTypes) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }

    public Map<String, String> getSoCaseTypeCodeList() {
        List<SoCaseType> soCaseTypes = this.findSoCaseTypeStatus();
        Map<String, String> values = new LinkedHashMap<String, String>();
        for (SoCaseType obj : soCaseTypes) {
            values.put(obj.getName(), obj.getCode());
        }
        return values;
    }
    
    public int checkSoCaseTypeCode(String code, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from SoCaseType as o where o.soCaseType is null and o.enable = true and o.code =:code");        
        } else {
            q = em.createQuery("select count(o) from SoCaseType as o where o.soCaseType is null and o.enable = true and o.code =:code and o.id <> :id");
            q.setParameter("id", id);
        }
        q.setParameter("code", code);
        return ((Long) q.getSingleResult()).intValue();
    }
        
    public int checkSoCaseTopicCode(String code, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from SoCaseType as o where o.soCaseType is not null and o.enable = true and o.code =:code");        
        } else {
            q = em.createQuery("select count(o) from SoCaseType as o where o.soCaseType is not null and o.enable = true and o.code =:code and o.id <> :id");
            q.setParameter("id", id);
        }
        q.setParameter("code", code);
        return ((Long) q.getSingleResult()).intValue();
    }
}
