/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.entity.WorkflowRule;
import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.WorkflowRuleDetail;
import com.maxelyz.utils.JSFUtil;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Administrator
 */
@Transactional
public class WorkflowRuleDAO {
    
    @PersistenceContext
    private EntityManager em;

    public void create(WorkflowRule workflowRule) {
        em.persist(workflowRule);
    }

    public void edit(WorkflowRule workflowRule) throws NonexistentEntityException, Exception {
        workflowRule = em.merge(workflowRule);
    }
    
    public void editWorkflowRule(WorkflowRule workflowRule) throws IllegalOrphanException, NonexistentEntityException, Exception {
       Query q = em.createQuery("Delete from WorkflowRuleDetail where workflowRule = ?1");
       q.setParameter(1, workflowRule);
       q.executeUpdate();

       workflowRule = em.merge(workflowRule);       
    }
    
    public void destroy(Integer id) throws NonexistentEntityException {
        WorkflowRule workflowRule;
        try {
            workflowRule = em.getReference(WorkflowRule.class, id);
            workflowRule.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The workflowRule with id " + id + " no longer exists.", enfe);
        }
        em.remove(workflowRule);
    }

    public List<WorkflowRule> findWorkflowRuleEntities() {
        return findWorkflowRuleEntities(true, -1, -1);
    }

    public List<WorkflowRule> findWorkflowRuleEntities(int maxResults, int firstResult) {
        return findWorkflowRuleEntities(false, maxResults, firstResult);
    }

    private List<WorkflowRule> findWorkflowRuleEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from WorkflowRule as o where o.enable = true order by o.name");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public WorkflowRule findWorkflowRule(Integer id) {
        return em.find(WorkflowRule.class, id);
    }

    public int getWorkflowRuleCount() {
        return ((Long) em.createQuery("select count(o) from WorkflowRule as o where o.enable = true").getSingleResult()).intValue();
    }
    
    public int checkName(String name, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from WorkflowRule as o where o.name =:name and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from WorkflowRule as o where o.name =:name and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("name", name);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public List<WorkflowRule> findWorkflowRuleByName(String nameKeyword) {
        Query q = em.createQuery("SELECT object(o) FROM WorkflowRule o WHERE o.enable = true and o.name like :name "
                + "order by o.name");
        q.setParameter("name", "%" + nameKeyword + "%");
        return q.getResultList();
    }
    
    public Map<String, Integer> getWorkflowRuleList() {
        List<WorkflowRule> workflowRules = findWorkflowRuleEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (WorkflowRule obj : workflowRules) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
    
    public List<WorkflowRuleDetail> findWorkflowCaseRule(Integer caseRequestId, Integer serviceTypeId, Integer businessUnitId, Integer locationId, String priority) {
        String sql="SELECT object(o) FROM WorkflowRuleDetail o WHERE 1 = 1 "; 
        String where="";
        if(priority != null && !priority.isEmpty()) {
            if(priority.equals("low")){
                where += "and o.workflowRule.id = (SELECT distinct(lowPriorityWorkflowRuleId.id) ";
            }else if(priority.equals("medium")){
                    where += "and o.workflowRule.id = (SELECT distinct(mediumPriorityWorkflowRuleId.id) ";
            }else if(priority.equals("high")){
                    where += "and o.workflowRule.id = (SELECT distinct(highPriorityWorkflowRuleId.id) ";
            }else if(priority.equals("immediate")){
                    where += "and o.workflowRule.id = (SELECT distinct(immediatelyPriorityWorkflowRuleId.id) ";
            }
            where += "FROM WorkflowCase where userGroup.id = :userGroupId and serviceType.id = :serviceTypeId "
                    + "and businessUnit.id = :businessUnitId and location.id = :locationId and caseRequest.id = :caseRequestId) ";
        }
        String order = "order by o.seqNo";
        
        Query q = em.createQuery(sql+where+order);
        q.setParameter("userGroupId", JSFUtil.getUserSession().getUserGroup().getId());
        q.setParameter("serviceTypeId", serviceTypeId);
        q.setParameter("businessUnitId", businessUnitId);
        q.setParameter("locationId", locationId);
        q.setParameter("caseRequestId", caseRequestId);
        return q.getResultList();
    }

}
