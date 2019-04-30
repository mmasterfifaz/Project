/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.CaseDetail;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class CaseDetailDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(CaseDetail caseDetail) throws Exception {
//        this.validate(caseDetail);
        em.persist(caseDetail);
    }

    public void edit(CaseDetail caseDetail) throws IllegalOrphanException, NonexistentEntityException, Exception {
        caseDetail = em.merge(caseDetail);
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        CaseDetail caseDetail;
        try {
            caseDetail = em.getReference(CaseDetail.class, id);
            caseDetail.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The caseDetail with id " + id + " no longer exists.", enfe);
        }
        em.remove(caseDetail);
    }

    public List<CaseDetail> findCaseDetailEntities() {
        return findCaseDetailEntities(true, -1, -1);
    }

    public List<CaseDetail> findCaseDetailEntities(int maxResults, int firstResult) {
        return findCaseDetailEntities(false, maxResults, firstResult);
    }

    private List<CaseDetail> findCaseDetailEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from CaseDetail as o where o.enable = true order by o.code");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public List<CaseDetail> findCaseDetailEntitiesByStatus() {
        Query q = em.createQuery("select object(o) from CaseDetail as o where o.enable = true order by o.code");
        return q.getResultList();
    }
//    public List<CaseDetail> findCaseDetailByCaseTopic(Integer caseTopicId) {
//        Query q = em.createQuery(
//                "select object(o) " +
//                "from CaseDetail o join o.caseTopicId p join p.caseTypeId q " +
//                "where o.enable=true and p.id = ?1 "+
//                "order by q.name, p.name, o.code ");
//
//        q.setParameter(1, caseTopicId);
//        return q.getResultList();
//    }

    public CaseDetail findCaseDetail(Integer id) {
        return em.find(CaseDetail.class, id);
    }

    public int getCaseDetailCount() {
        Query q = em.createQuery("select count(o) from CaseDetail as o");
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<CaseDetail> findCaseDetailByCaseTopic(Integer caseTopicId) {
        Query q = em.createQuery("select object(o) from CaseDetail as o where o.caseTopicId.id = :caseTopicId");
        q.setParameter("caseTopicId", caseTopicId);

        return q.getResultList();
    }

    public List<CaseDetail> findCaseDetailByCaseTopicStatus(Integer caseTopicId) {
        Query q = em.createQuery("select object(o) from CaseDetail as o "
                + "where o.caseTopicId.id = :caseTopicId and o.enable = true order by o.code");
        q.setParameter("caseTopicId", caseTopicId);

        return q.getResultList();
    }
    
    public List<CaseDetail> findAvailableCaseDetailByCaseTopic(Integer caseTopicId) {
        Query q = em.createQuery("select object(o) from CaseDetail as o "
                + "where o.caseTopicId.id = :caseTopicId and o.enable = true and o.status = true order by o.code");
        q.setParameter("caseTopicId", caseTopicId);

        return q.getResultList();
    }
    
    public List<CaseDetail> findCaseDetailByCaseType(Integer caseTypeId) {
        Query q = em.createQuery("select object(o) from CaseDetail as o "
                + "join o.caseTopicId caseTopic "
                + "join caseTopic.caseTypeId caseType "
                + "where o.enable = true and caseType.id = :caseTypeId");
        q.setParameter("caseTypeId", caseTypeId);
        return q.getResultList();
    }
    
     public List<CaseDetail> findCaseDetailByCode(String code) {    
        Query q = em.createQuery("select object(o) from CaseDetail as o where code = ?1");
        q.setParameter(1, code);
        return q.getResultList();
    }

    private void validate(CaseDetail caseDetail) throws PreexistingEntityException {
         if (findCaseDetailByCode(caseDetail.getCode()).size()>0)
            throw new PreexistingEntityException("Duplicate Code");
    }
        
    public int checkCaseDetailCode(String code, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from CaseDetail as o where o.code =:code and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from CaseDetail as o where o.code =:code and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("code", code);
        return ((Long) q.getSingleResult()).intValue();
    }
}
