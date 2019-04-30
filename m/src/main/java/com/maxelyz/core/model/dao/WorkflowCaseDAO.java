/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.WorkflowCase;
import com.maxelyz.core.model.entity.WorkflowCasePK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Oat
 */
@Transactional
public class WorkflowCaseDAO  {

    @PersistenceContext
    private EntityManager em;

    public void create(WorkflowCase workflowCase)  {
        em.persist(workflowCase);         
    }

    public void edit(WorkflowCase workflowCase) throws  Exception {
        workflowCase = em.merge(workflowCase);     
    }

    public void destroy(WorkflowCasePK id) throws NonexistentEntityException {
        WorkflowCase workflowCase;
        try {
            workflowCase = em.getReference(WorkflowCase.class, id);
            workflowCase.getWorkflowCasePK();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The workflowCase with id " + id + " no longer exists.", enfe);
        }
        em.remove(workflowCase);      
    }

    public List<WorkflowCase> findWorkflowCaseEntities() {
        return findWorkflowCaseEntities(true, -1, -1);
    }

    public List<WorkflowCase> findWorkflowCaseEntities(int maxResults, int firstResult) {
        return findWorkflowCaseEntities(false, maxResults, firstResult);
    }

    private List<WorkflowCase> findWorkflowCaseEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from WorkflowCase as o "
                + "order by o.userGroup, o.serviceType, o.caseDetail.caseTopicId.caseTypeId.name, "
                + "o.caseDetail.caseTopicId.name, o.caseDetail.name, o.caseRequest.name");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public WorkflowCase findWorkflowCase(WorkflowCasePK id) {
         return em.find(WorkflowCase.class, id);
    }

    public int getWorkflowCaseCount() {
        Query q = em.createQuery("select count(o) from WorkflowCase as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
//    public List<WorkflowCase> findWorkflowCase(Integer userGroupId, Integer serviceTypeId) {
//        Query q = em.createQuery("select object(o) from WorkflowCase as o where o.userGroup.id = :userGroupId and o.serviceType.id = :serviceTypeId ");
//        q.setParameter("userGroupId", userGroupId);
//        q.setParameter("serviceTypeId", serviceTypeId);
//
//        return q.getResultList();
//    }
    
    public void deleteWorkflowCase(WorkflowCasePK workflowCasePK) {
        Query q = em.createQuery("Delete from WorkflowCase where workflowCasePK = :workflowCasePK");
        q.setParameter("workflowCasePK", workflowCasePK);

        q.executeUpdate();
    }
    
        
    public List<WorkflowCase> findWorkflowCaseSameRule(Integer userGroupId, Integer serviceTypeId, Integer businessUnitId, Integer locationId, 
                                                       Integer lowRuleId , Integer mediumRuleId, Integer highRuleId, Integer immediatelyRuleId) {
        String sql = "select object(o) from WorkflowCase as o where o.userGroup.id = :userGroupId and o.serviceType.id = :serviceTypeId "
                   + "and o.businessUnit.id = :businessUnitId and o.location.id = :locationId";
     
        String where = "";
        if(lowRuleId != null)
            where += " and o.lowPriorityWorkflowRuleId.id = "+lowRuleId;
        
        if(mediumRuleId != null)
            where += " and o.mediumPriorityWorkflowRuleId.id = "+mediumRuleId;    
        
        if(highRuleId != null)
            where += " and o.highPriorityWorkflowRuleId.id = "+highRuleId;  
        
        if(immediatelyRuleId != null)
            where += " and o.immediatelyPriorityWorkflowRuleId.id = "+immediatelyRuleId;    
        
        Query q = em.createQuery(sql+where);
        q.setParameter("userGroupId", userGroupId);
        q.setParameter("serviceTypeId", serviceTypeId);
        q.setParameter("businessUnitId", businessUnitId);
        q.setParameter("locationId", locationId);

        return q.getResultList();
    }
    
//    public List<WorkflowCase> findWorkflowCaseSameRule(Integer userGroupId, Integer serviceTypeId, Integer lowRuleId , Integer mediumRuleId,
//                                                       Integer highRuleId, Integer immediatelyRuleId) {
//        String sql = "select object(o) from WorkflowCase as o where o.userGroup.id = :userGroupId and o.serviceType.id = :serviceTypeId";
//     
//        String where = "";
//        if(lowRuleId != null)
//            where += " and o.lowPriorityWorkflowRuleId.id = "+lowRuleId;
//        
//        if(mediumRuleId != null)
//            where += " and o.mediumPriorityWorkflowRuleId.id = "+mediumRuleId;    
//        
//        if(highRuleId != null)
//            where += " and o.highPriorityWorkflowRuleId.id = "+highRuleId;  
//        
//        if(immediatelyRuleId != null)
//            where += " and o.immediatelyPriorityWorkflowRuleId.id = "+immediatelyRuleId;    
//        
//        Query q = em.createQuery(sql+where);
//        q.setParameter("userGroupId", userGroupId);
//        q.setParameter("serviceTypeId", serviceTypeId);
//
//        return q.getResultList();
//    }
        
    public List<WorkflowCase> getWorkflowCaseBySearch(Integer caseTypeId, Integer caseTopicId, Integer caseDetailId, Integer caseRequestId,
                                                        Integer serviceTypeId, Integer businessUnitId, Integer locationId, Integer userGroupId) {
        String sql="select object(o) from WorkflowCase as o where 1=1";
        String where="";
        if(caseTypeId != 0)
            where += " and o.caseDetail.caseTopicId.caseTypeId.id = "+caseTypeId;
        if(caseTopicId != 0)
            where += " and o.caseDetail.caseTopicId.id = "+caseTopicId;
        if(caseDetailId != 0)
            where += " and o.caseDetail.id = "+caseDetailId;
        if(caseRequestId != 0)
            where += " and o.caseRequest.id = "+caseRequestId;
        if(serviceTypeId != 0)
            where += " and o.serviceType.id = "+serviceTypeId;
        if(businessUnitId != 0)
            where += " and o.businessUnit.id = "+businessUnitId;
        if(locationId != 0)
            where += " and o.location.id = "+locationId;
        if(userGroupId != 0)
            where += " and o.userGroup.id = "+userGroupId;
        
        String order=" order by o.userGroup, o.serviceType, o.caseDetail.caseTopicId.caseTypeId.name, "
                    + "o.caseDetail.caseTopicId.name, o.caseDetail.name, o.caseRequest.name ";
        
        Query q = em.createQuery(sql+where+order);
        return q.getResultList();
    }
}
