/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.CaseType;
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
public class CaseTypeDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(CaseType caseType) throws PreexistingEntityException {
//        this.validate(caseType);
        em.persist(caseType);
    }

    public void edit(CaseType caseType) throws NonexistentEntityException, Exception {
        caseType = em.merge(caseType);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        CaseType caseType;
        try {
            caseType = em.getReference(CaseType.class, id);
            caseType.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The caseType with id " + id + " no longer exists.", enfe);
        }
        em.remove(caseType);
    }

    public List<CaseType> findCaseTypeEntities() {
        return findCaseTypeEntities(true, -1, -1);
    }

    public List<CaseType> findCaseTypeEntities(int maxResults, int firstResult) {
        return findCaseTypeEntities(false, maxResults, firstResult);
    }

    private List<CaseType> findCaseTypeEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from CaseType as o where o.enable = true order by o.code");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public List<CaseType> findCaseTypeStatus() {
        Query q = em.createQuery("select object(o) from CaseType as o where o.enable = true and o.status = true order by o.code");
        return q.getResultList();
    }

    public CaseType findCaseType(Integer id) {
        return em.find(CaseType.class, id);
    }

    public int getCaseTypeCount() {
        Query q = em.createQuery("select count(o) from CaseType as o");
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<CaseType> findCaseTypeByCode(String code) {
        Query q = em.createQuery("select object(o) from CaseType as o where code = ?1");
        q.setParameter(1, code);
        return q.getResultList();
    }

    private void validate(CaseType caseType) throws PreexistingEntityException {
        if (findCaseTypeByCode(caseType.getCode()).size() > 0) {
            throw new PreexistingEntityException("Duplicate Code");
        }
    }

    public Map<String, Integer> getCaseTypeList() {
        List<CaseType> caseTypes = this.findCaseTypeStatus();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (CaseType obj : caseTypes) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
    
    public int checkCaseTypeCode(String code, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from CaseType as o where o.code =:code and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from CaseType as o where o.code =:code and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("code", code);
        return ((Long) q.getSingleResult()).intValue();
    }
        
}
