/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.CaseRequest;
import com.maxelyz.core.model.entity.WorkflowCase;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class CaseRequestDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(CaseRequest caseRequest) throws Exception {
//        this.validate(caseRequest);
        em.persist(caseRequest);
    }

    public void edit(CaseRequest caseRequest) throws NonexistentEntityException, Exception {
        caseRequest = em.merge(caseRequest);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        CaseRequest caseRequest;
        try {
            caseRequest = em.getReference(CaseRequest.class, id);
            caseRequest.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The caseRequest with id " + id + " no longer exists.", enfe);
        }
        em.remove(caseRequest);
    }

    public List<CaseRequest> findCaseRequestEntities() {
        return findCaseRequestEntities(true, -1, -1);
    }

    public List<CaseRequest> findCaseRequestEntities(int maxResults, int firstResult) {
        return findCaseRequestEntities(false, maxResults, firstResult);
    }

    private List<CaseRequest> findCaseRequestEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from CaseRequest as o where o.enable = true");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public CaseRequest findCaseRequest(Integer id) {
        return em.find(CaseRequest.class, id);
    }

    public int getCaseRequestCount() {
        Query q = em.createQuery("select count(o) from CaseRequest as o");
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<CaseRequest> findCaseRequestByCaseDetail(Integer caseDetailId) {
        Query q = em.createQuery("select object(o) from CaseRequest as o where o.caseDetailId.id = :caseDetailId");
        q.setParameter("caseDetailId", caseDetailId);

        return q.getResultList();
    }

    public List<CaseRequest> findCaseRequestByCaseDetailStatus(Integer caseDetailId) {
        Query q = em.createQuery("select object(o) from CaseRequest as o where o.caseDetailId.id = :caseDetailId and o.enable = true and o.status = true");
        q.setParameter("caseDetailId", caseDetailId);

        return q.getResultList();
    }
    
    public List<CaseRequest> findCaseRequestByCaseType(Integer caseTypeId) {
        Query q = em.createQuery("select object(o) from CaseRequest as o "
                + "join o.caseDetailId caseDetail "
                + "join caseDetail.caseTopicId caseTopic "
                + "join caseTopic.caseTypeId caseType "
                + "where o.enable = true and caseType.id = :caseTypeId order by o.code");
        q.setParameter("caseTypeId", caseTypeId);
        return q.getResultList();
    }
    
    public List<CaseRequest> findCaseRequestByCaseTopic(Integer caseTopicId) {
        Query q = em.createQuery("select object(o) from CaseRequest as o "
                + "join o.caseDetailId caseDetail "
                + "join caseDetail.caseTopicId caseTopic "
                + "where caseTopic.id = :caseTopicId and o.enable = true order by o.code");
        q.setParameter("caseTopicId", caseTopicId);

        return q.getResultList();
    }
    
    public List<CaseRequest> findCaseRequestByCaseDetailId(Integer caseDetailId) {
        Query q = em.createQuery("select object(o) from CaseRequest as o "
                + "join o.caseDetailId caseDetail "
                + "where caseDetail.id = :caseDetailId and o.enable = true order by o.code");
        q.setParameter("caseDetailId", caseDetailId);

        return q.getResultList();
    }
        
    public List<CaseRequest> findCaseRequestByCode(String code) {
        Query q = em.createQuery("select object(o) from CaseRequest as o where code = ?1");
        q.setParameter(1, code);
        return q.getResultList();
    }

    private void validate(CaseRequest caseRequest) throws PreexistingEntityException {
         if (findCaseRequestByCode(caseRequest.getCode()).size()>0)
            throw new PreexistingEntityException("Duplicate Code");
    }
    
    public int checkCaseRequestCode(String code, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from CaseRequest as o where o.code =:code and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from CaseRequest as o where o.code =:code and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("code", code);
        return ((Long) q.getSingleResult()).intValue();
    }
    
//    public List<CaseRequest> findCaseRequestByWorkflowCase(List<WorkflowCase> workflowCaseList) {
//        String requestId = "";
//        String sql = "";
//        String where = "";
//        String order = "";
//        
//        sql = "select object(o) from CaseRequest as o where o.enable = true and o.status = true ";
//        
//        if(!workflowCaseList.isEmpty()) {
//            for(WorkflowCase obj: workflowCaseList)
//                if(obj.getCaseRequest() != null)
//                    requestId += obj.getCaseRequest().getId() + ",";
//            if(requestId.length() > 0)
//                requestId = requestId.substring(0, requestId.length()-1);
//            where = "and o.id not in ("+requestId+") ";
//        }
//        order = "order by o.name";
//        Query q = em.createQuery(sql+where+order);
//        
//        return q.getResultList();
//    }
        
    /*
      select * from case_request cr 
 where cr.id not in
 (select case_request_id from workflow_case wc
 where wc.user_group_id = 5 and wc.service_type_id = 4)
 and cr.enable = 1 and cr.status = 1
 order by cr.name
 
     */
    public List<CaseRequest> findWorkflowCaseAvailable(Integer userGroupId, Integer serviceTypeId, Integer businessUnitId, Integer locationId) {
        Query q = em.createQuery("select object(o) from CaseRequest as o where o.enable = true and o.status = true and o.id not in "
            + "(select wc.caseRequest.id from WorkflowCase as wc where wc.userGroup.id = :userGroupId and wc.serviceType.id = :serviceTypeId "
            + "and wc.businessUnit.id = :businessUnitId and wc.location.id = :locationId) "
            + "order by o.caseDetailId.caseTopicId.caseTypeId.name, o.caseDetailId.caseTopicId.name, o.caseDetailId.name, o.name");
        
        q.setParameter("userGroupId", userGroupId);
        q.setParameter("serviceTypeId", serviceTypeId);
        q.setParameter("businessUnitId", businessUnitId);
        q.setParameter("locationId", locationId);

        return q.getResultList();
    }
    
    public List<CaseRequest> findCaseRequestEditByWorkflowCase(List<WorkflowCase> workflowCaseList) {
        String requestId = "";
        String sql = "";
        String where = "";
        String order = "";
        
        sql = "select object(o) from CaseRequest as o where o.enable = true and o.status = true ";
        
        if(!workflowCaseList.isEmpty()) {
            for(WorkflowCase obj: workflowCaseList)
                if(obj.getCaseRequest() != null)
                    requestId += obj.getCaseRequest().getId() + ",";
            if(requestId.length() > 0)
                requestId = requestId.substring(0, requestId.length()-1);
            where = "and o.id in ("+requestId+") ";
        }
        order = "order by o.caseDetailId.caseTopicId.caseTypeId.name, o.caseDetailId.caseTopicId.name, o.caseDetailId.name, o.name";
        Query q = em.createQuery(sql+where+order);
        
        return q.getResultList();
    }

}
