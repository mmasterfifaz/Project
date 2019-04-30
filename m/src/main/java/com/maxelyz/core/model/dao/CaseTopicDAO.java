/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.CaseTopic;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class CaseTopicDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(CaseTopic caseTopic) throws PreexistingEntityException {
//        this.validate(caseTopic);
        em.persist(caseTopic);
    }

    public void edit(CaseTopic caseTopic) throws NonexistentEntityException, Exception {
        caseTopic = em.merge(caseTopic);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        CaseTopic caseTopic;
        try {
            caseTopic = em.getReference(CaseTopic.class, id);
            caseTopic.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The caseTopic with id " + id + " no longer exists.", enfe);
        }
        em.remove(caseTopic);
    }

    public List<CaseTopic> findCaseTopicEntities() {
        return findCaseTopicEntities(true, -1, -1);
    }

    public List<CaseTopic> findCaseTopicEntities(int maxResults, int firstResult) {
        return findCaseTopicEntities(false, maxResults, firstResult);
    }

    private List<CaseTopic> findCaseTopicEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from CaseTopic as o where o.enable = true order by o.code");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

//    public List<CaseTopic> findCaseTopicByCaseType(int caseTypeId) {
//        Query q = em.createQuery(
//                "select object(o) "
//                + "from CaseTopic as o join o.caseTypeId p "
//                + "where o.enable = true and p.id = ?1 "
//                + "order by o.code");
//        q.setParameter(1, caseTypeId);
//        return q.getResultList();
//    }
    public CaseTopic findCaseTopic(Integer id) {
        return em.find(CaseTopic.class, id);
    }

    public int getCaseTopicCount() {
        Query q = em.createQuery("select count(o) from CaseTopic as o");
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<CaseTopic> findCaseTopicByCaseType(Integer caseTypeId) {
        Query q = em.createQuery("select object(o) from CaseTopic as o where o.caseTypeId.id = :caseTypeId");
        q.setParameter("caseTypeId", caseTypeId);

        return q.getResultList();
    }

    public List<CaseTopic> findCaseTopicByCaseTypeStatus(Integer caseTypeId) {
        Query q = em.createQuery("select object(o) from CaseTopic as o where o.caseTypeId.id = :caseTypeId and o.enable = true and o.status = true order by o.code");
        q.setParameter("caseTypeId", caseTypeId);

        return q.getResultList();
    }

    public List<CaseTopic> findCaseTopicByCode(String code) {
        Query q = em.createQuery("select object(o) from CaseTopic as o where code = ?1");
        q.setParameter(1, code);
        return q.getResultList();
    }

    private void validate(CaseTopic caseTopic) throws PreexistingEntityException {
        if (findCaseTopicByCode(caseTopic.getCode()).size() > 0) {
            throw new PreexistingEntityException("Duplicate Code");
        }
    }
    
    public int checkCaseTopicCode(String code, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from CaseTopic as o where o.code =:code and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from CaseTopic as o where o.code =:code and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("code", code);
        return ((Long) q.getSingleResult()).intValue();
    }
}
