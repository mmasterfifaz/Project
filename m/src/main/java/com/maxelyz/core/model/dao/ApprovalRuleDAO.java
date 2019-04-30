/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.ApprovalRule;
import com.maxelyz.core.model.entity.ApprovalRuleDetail;
import com.maxelyz.core.model.entity.ApprovalRuleDetailPK;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author oat
 */
@Transactional
public class ApprovalRuleDAO {

    @PersistenceContext
    protected EntityManager em;


    public void create(ApprovalRule approvalRule) {
        em.persist(approvalRule);           
    }
    
    public void createApprovalRuleDetail(ApprovalRuleDetail approvalRuleDetail) {
        em.persist(approvalRuleDetail);           
    }

    public void edit(ApprovalRule approvalRule) throws IllegalOrphanException, NonexistentEntityException, Exception {
        approvalRule = em.merge(approvalRule);  
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        ApprovalRule approvalRule;
        try {
            approvalRule = em.getReference(ApprovalRule.class, id);
            approvalRule.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The approvalRule with id " + id + " no longer exists.", enfe);
        }
        em.remove(approvalRule);         
    }

    public void removeApprovalRuleDetail(ApprovalRuleDetailPK approvalRuleDetailPK) throws IllegalOrphanException, NonexistentEntityException {
        ApprovalRuleDetail approvalRuleDetail;
        try {
            approvalRuleDetail = em.getReference(ApprovalRuleDetail.class, approvalRuleDetailPK);
//            userGroupAcl.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The approvalRuleDetail with id " + approvalRuleDetailPK + " no longer exists.", enfe);
        }
        em.remove(approvalRuleDetail);
    }

    public List<ApprovalRule> findApprovalRuleEntities() {
        return findApprovalRuleEntities(true, -1, -1);
    }

    public List<ApprovalRule> findApprovalRuleEntities(int maxResults, int firstResult) {
        return findApprovalRuleEntities(false, maxResults, firstResult);
    }

    private List<ApprovalRule> findApprovalRuleEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from ApprovalRule as o where o.enable = true order by o.name");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public ApprovalRule findApprovalRule(Integer id) throws Exception {
        return em.find(ApprovalRule.class, id);

    }

    public int getApprovalRuleCount() {
        Query q = em.createQuery("select count(o) from ApprovalRule as o");
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<ApprovalRuleDetail> findApprovalRuleDetail(Integer id, String type) {
        Query q = em.createQuery("select object(o) from ApprovalRuleDetail as o"
                + " where o.approvalRuleDetailPK.approvalRuleId = ?1"
                + " and o.approvalRuleDetailPK.type = ?2"
                + " order by o.approvalRuleDetailPK.seqNo");
        q.setParameter(1, id);
        q.setParameter(2, type);
        return q.getResultList();
    }

    public Boolean checkParam(Integer param, String operator, String txtValue) {
        Query q = em.createNativeQuery("SELECT (CASE WHEN (" + param + " " + operator + " " + txtValue + ") THEN 'true' ELSE 'false' END) as b");
        
        return Boolean.valueOf((String) q.getSingleResult());
    }

    public Boolean checkBmi(Double bmi, String operator, String txtValue) {
        Query q = em.createNativeQuery("SELECT (CASE WHEN (" + bmi + " " + operator + " " + txtValue + ") THEN 'true' ELSE 'false' END) as b");
        
        return Boolean.valueOf((String) q.getSingleResult());
    }
    
    public int checkApprovalRuleName(String name, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from ApprovalRule as o where o.name =:name and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from ApprovalRule as o where o.name =:name and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("name", name);
        return ((Long) q.getSingleResult()).intValue();
    }
}
