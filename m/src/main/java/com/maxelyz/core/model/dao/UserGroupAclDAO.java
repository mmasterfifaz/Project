/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.UserGroupAcl;
import com.maxelyz.core.model.entity.UserGroupAclPK;
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
public class UserGroupAclDAO {
    

    @PersistenceContext
    private EntityManager em;

    public void create(UserGroupAcl userGroupAcl) throws PreexistingEntityException, Exception {
        em.persist(userGroupAcl);
    }

    public void edit(UserGroupAcl userGroupAcl) throws IllegalOrphanException, NonexistentEntityException, Exception {
        userGroupAcl = em.merge(userGroupAcl);
    }

    public void createUserGroupAcl(List<UserGroupAcl> userGroupAclList) {
        for (UserGroupAcl value : userGroupAclList) {
            try{
                em.persist(value);

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void destroy(UserGroupAclPK userGroupAclPK) throws IllegalOrphanException, NonexistentEntityException {
        UserGroupAcl userGroupAcl;
        try {
            userGroupAcl = em.getReference(UserGroupAcl.class, userGroupAclPK);
//            userGroupAcl.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The userGroupAcl with id " + userGroupAclPK + " no longer exists.", enfe);
        }
        em.remove(userGroupAcl);
    }

    public List<UserGroupAcl> findUserGroupAclEntities() {
        return findUserGroupAclEntities(true, -1, -1);
    }

    public List<UserGroupAcl> findUserGroupAclEntities(int maxResults, int firstResult) {
        return findUserGroupAclEntities(false, maxResults, firstResult);
    }

    private List<UserGroupAcl> findUserGroupAclEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from UserGroupAcl as o order by o.user_group_id");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public UserGroupAcl findUserGroupAcl(Integer usergroupid) {
        return em.find(UserGroupAcl.class, usergroupid);
    }

    public List<UserGroupAcl> findUserGroupAclByUserGroupId(Integer userGroupId) {

        Query q = em.createQuery("select object(o) from UserGroupAcl as o where o.userGroupAclPK.userGroupId = ?1 order by o.userGroupAclPK.aclCode");
        q.setParameter(1, userGroupId);

        return q.getResultList();

    }

    public int getUserGroupAclCount() {
        return ((Long) em.createQuery("select count(o) from UserGroupAcl as o").getSingleResult()).intValue();
    }
/*
    public List<UserGroupAcl> findUserGroupAclByName(String nameKeyword) {
        Query q = em.createNamedQuery("UserGroupAcl.findByName");
        q.setParameter("name", nameKeyword);
        return q.getResultList();
    }*/

    public Map<String, Integer> getUserGroupAclerGroupList() {
        List<UserGroupAcl> userGroupAcls = findUserGroupAclEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (UserGroupAcl obj : userGroupAcls) {
  //          values.put(obj.getAuthorizationCode(), obj.getId());
        }
        return values;
    }
/*
    public static Map<String, String> getRoleList() {
        Map<String, String> values = new LinkedHashMap<String, String>();
        values.put("Customer Services", JSFUtil.getBundleValue("roleCs"));
        values.put("Telesales", JSFUtil.getBundleValue("roleTs"));
        values.put("Sale Approval", JSFUtil.getBundleValue("roleSaleApprove"));
        values.put("Payment Approval", JSFUtil.getBundleValue("rolePaymentApprove"));  
        values.put("Quality Assurance", JSFUtil.getBundleValue("roleQc"));
        values.put("Report", JSFUtil.getBundleValue("roleReport"));
        values.put("Administrator", JSFUtil.getBundleValue("roleAdmin"));
        values.put("Use Telephony Bar", JSFUtil.getBundleValue("roleTelephonyBar"));
        return values;
    }*/
}

