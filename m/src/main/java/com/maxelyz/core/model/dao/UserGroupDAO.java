/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.utils.JSFUtil;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class UserGroupDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(UserGroup userGroup) throws PreexistingEntityException, Exception {
        em.persist(userGroup);
    }

    public void edit(UserGroup userGroup) throws IllegalOrphanException, NonexistentEntityException, Exception {
        userGroup = em.merge(userGroup);
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        UserGroup userGroup;
        try {
            userGroup = em.getReference(UserGroup.class, id);
            userGroup.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The userGroup with id " + id + " no longer exists.", enfe);
        }
        em.remove(userGroup);
    }

    public List<UserGroup> findUserGroupEntities() {
        return findUserGroupEntities(true, -1, -1);
    }

    public List<UserGroup> findUserGroupEntities(int maxResults, int firstResult) {
        return findUserGroupEntities(false, maxResults, firstResult);
    }

    private List<UserGroup> findUserGroupEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from UserGroup as o where enable = true order by name");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public UserGroup findUserGroup(Integer id) {
        return em.find(UserGroup.class, id);
    }

    public int getUserGroupCount() {
        return ((Long) em.createQuery("select count(o) from UserGroup as o").getSingleResult()).intValue();
    }

    public List<UserGroup> findUserGroupByName(String nameKeyword) {
        //Query q = em.createNamedQuery("UserGroup.findByName");
        Query q = em.createQuery("SELECT u FROM UserGroup u where name = :name and enable=true and status = true order by name");
        
        q.setParameter("name", nameKeyword);
        return q.getResultList();
    }

    public Map<String, Integer> getUserGroupList() {
        List<UserGroup> userGroups = findUserGroupEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (UserGroup obj : userGroups) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }

    public Map<String, Integer> getUserGroupListExcept(Integer id) {
        List<UserGroup> userGroups = findUserGroupEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (UserGroup obj : userGroups) {
            if(obj.getId().intValue() != id.intValue()){
                values.put(obj.getName(), obj.getId());
            }
        }
        return values;
    }

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
    }

    public List<Integer> findServiceId(Integer userGroupId) {
        Query q = em.createQuery("select distinct o.serviceType.id from UserGroupLocation as o where o.userGroup.id = ?1 order by o.serviceType.id");
        q.setParameter(1, userGroupId);
        return q.getResultList();
    }
    
    public List<ServiceType> getServiceTypeList(Integer userGroupId) {
        Query q = em.createQuery("select distinct o.serviceType from UserGroupLocation as o "
                + "where o.userGroup.id= ?1 and o.serviceType.enable = true order by o.serviceType.name");
        q.setParameter(1, userGroupId);
        return q.getResultList();
    }
    
    public Map<String, Integer> getServiceTypeByUserGroup(int userGroupId) {
        List<ServiceType> serviceTypes =  this.getServiceTypeList(userGroupId);
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (ServiceType obj : serviceTypes) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }

    public List<Integer> findBusinessUnitId(Integer userGroupId, Integer serviceTypeId) {
        Query q = em.createQuery("select distinct o.businessUnit.id from UserGroupLocation as o "
                + "where o.userGroup.id = ?1 and o.serviceType.id = ?2 order by o.businessUnit.id");
        q.setParameter(1, userGroupId);
        q.setParameter(2, serviceTypeId);
        return q.getResultList();
    }
    
    public List<BusinessUnit> getBusinessUnitList(Integer userGroupId, Integer serviceTypeId) {
        Query q = em.createQuery("select distinct o.businessUnit from UserGroupLocation as o "
                + "where o.userGroup.id = ?1 and o.serviceType.id = ?2 and o.businessUnit.enable = true order by o.businessUnit.name");
        q.setParameter(1, userGroupId);
        q.setParameter(2, serviceTypeId);
        return q.getResultList();
    }
    
    public List<Integer> findLocationId(Integer userGroupId, Integer serviceTypeId, Integer businessUnitId) {
        Query q = em.createQuery("select distinct o.location.id from UserGroupLocation as o "
                + "where o.userGroup.id = ?1 and o.serviceType.id = ?2 and o.businessUnit.id = ?3 order by o.location.id");
        q.setParameter(1, userGroupId);
        q.setParameter(2, serviceTypeId);
        q.setParameter(3, businessUnitId);
        return q.getResultList();
    }
    
    public List<Location> getLocationList(Integer userGroupId, Integer serviceTypeId, Integer businessUnitId) {
        Query q = em.createQuery("select distinct o.location from UserGroupLocation as o "
                + "where o.userGroup.id = ?1 and o.serviceType.id = ?2 and o.businessUnit.id = ?3 and o.location.enable = true order by o.location.name");
        q.setParameter(1, userGroupId);
        q.setParameter(2, serviceTypeId);
        q.setParameter(3, businessUnitId);
        return q.getResultList();
    }

    public List<UserGroupLocation> findUserGroupLocationByUserGroupId(Integer userGroupId) {

        Query q = em.createQuery("select object(o) from UserGroupLocation as o where o.userGroupLocationPK.userGroupId = ?1 order by o.userGroupLocationPK.locationId");
        q.setParameter(1, userGroupId);

        return q.getResultList();

    }
    //vee
    public List<BusinessUnit> findBusinessUnitIdByUserGroupId(Integer userGroupId) {
        Query q = em.createQuery("select distinct o.businessUnit.id from UserGroupLocation as o where o.userGroup.id = ?1");
        q.setParameter(1, userGroupId);
        return q.getResultList();
    }
    //vee
    public void destroyUserGroupLocation(UserGroupLocationPK userGroupLocationPK) throws IllegalOrphanException, NonexistentEntityException {
        UserGroupLocation userGroupLocation;
        try {
            userGroupLocation = em.getReference(UserGroupLocation.class, userGroupLocationPK);
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The userGroupLocation with id " + userGroupLocationPK + " no longer exists.", enfe);
        }
        em.remove(userGroupLocation);
    }
    
    public void createUserGroupLocation(UserGroupLocation userGroupLocation) {
        try {
            em.persist(userGroupLocation);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Map<String, Integer> getUserGroupListBySupervisor() {
        List<UserGroup> userGroups = findUserGroupBySup();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (UserGroup obj : userGroups) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
    
    public List<UserGroup> findUserGroupBySup() {
        Query q = em.createQuery("select object(o) from UserGroup as o where role like '%Supervisor%' and parentId = ?1 and enable = true order by name");
        q.setParameter(1, JSFUtil.getUserSession().getUserGroup().getId());
        return q.getResultList();
    }
       
    public Map<String, Integer> getUserGroupListByAgent() {
        List<UserGroup> userGroups = findUserGroupByAgent();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (UserGroup obj : userGroups) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
    
    public List<UserGroup> findUserGroupByAgent() {
        String sql = "SELECT object(o) FROM UserGroup as o WHERE enable = true and status=true ";
        if(JSFUtil.getUserSession().getUserGroup().getRole().contains("CampaignManager")){
            sql += "AND ((role like '%Agent%' AND parentId = ?1) OR parentId IN (SELECT u.id  FROM UserGroup u WHERE  u.role like '%Supervisor%' AND u.parentId = ?1))";
        }
        else{
            sql += "AND (role LIKE '%Agent%' AND id = ?1) OR (role LIKE '%Agent%' AND parentId = ?1) order by name";
        }       
        Query q = em.createQuery(sql);
        q.setParameter(1, JSFUtil.getUserSession().getUserGroup().getId());
        return q.getResultList();
    }
    
    
           
    public Map<String, Integer> getUserGroupListByRole() {
        List<UserGroup> userGroups = findUserGroupByRole();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (UserGroup obj : userGroups) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
    
    public List<UserGroup> findUserGroupByRole() {
        String sql = "SELECT object(o) FROM UserGroup as o WHERE role LIKE ?1 and enable = true order by name";
   
        Query q = em.createQuery(sql);
        q.setParameter(1, JSFUtil.getUserSession().getUserGroup().getRole());
        return q.getResultList();
        
    }
    
    public List<UserGroup> findUserGroupBy(String name, Integer userGroupId, String role, String status) {
        String sql = "SELECT object(o) FROM UserGroup as o"
                + " WHERE o.enable = true";
        if(StringUtils.isNotEmpty(name)){
            sql += " and o.name like :name";
        }
        if(userGroupId != null && userGroupId != 0){
            sql += " and o.parentId = :userGroupId";
        }
        if(StringUtils.isNotEmpty(role)){
            sql += " and o.role like :role";
        }
        if(StringUtils.isNotEmpty(status)){
            if(StringUtils.equals(status, "enable")){
                sql += " and o.status = true";
            }else if(StringUtils.equals(status, "disable")){
                sql += " and o.status = false";
            }
        }
        sql += " order by name "; 
        Query q = em.createQuery(sql);
        if(StringUtils.isNotEmpty(name)){
            q.setParameter("name", "%" + name + "%");
        }
        if(userGroupId != null && userGroupId != 0){
            q.setParameter("userGroupId", userGroupId);
        }
        if(StringUtils.isNotEmpty(role)){
            q.setParameter("role", "%" + role + "%");
        }
        
        return q.getResultList();
        
    }
         
    public List<UserGroup> checkUserGroupByName(String name, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select object(o) from UserGroup as o where o.name =:name and o.enable = true");        
        } else {
            q = em.createQuery("select object(o) from UserGroup as o where o.name =:name and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("name", name);
        return q.getResultList();
    }
    
    public Map<String, Integer> getUserGroupListUnderParent(Users users) {
        List<UserGroup> userGroups = findUserGroupUnderParentId(users);
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (UserGroup obj : userGroups) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
  
    public List<UserGroup> findUserGroupUnderParentId(Users users) {       
        String sql = "select object(o) from UserGroup as o where o.enable = true and o.status= true ";
        if(users.getUserGroup().getRole().contains("CampaignManager")){
            sql += "and o.role like '%Supervisor%' and o.parentId = ?1 "
                + "or (role like '%Agent%' and parentId IN (SELECT u.id  FROM UserGroup u WHERE  u.role like '%Supervisor%' and u.parentId = ?1)) "
                + "order by o.name";
        } else {
            sql += "and (role LIKE '%Agent%' AND id = ?1) OR (role LIKE '%Agent%' AND parentId = ?1) order by name ";
        }
        Query q = em.createQuery(sql);
        
        q.setParameter(1, users.getUserGroup().getId());
        return q.getResultList();
    }
    
    
    private List<UserGroup> findSoAgentGroup() {
        Query q = em.createQuery("select object(o) from UserGroup as o where o.enable = true and o.status = true and o.role like '%SOAgent%' order by o.name");
       
        return q.getResultList();
    }
    
    public Map<String, Integer> getSoAgentGroupList() {
        List<UserGroup> userGroups = findSoAgentGroup();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (UserGroup obj : userGroups) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
    
    public List<UserGroup> findUserGroupByRoleUwPaymentQc(){
        Query q = em.createQuery("select object(o) from UserGroup as o where o.enable = true and (o.role like '%Qc%' or o.role like '%Payment%' or o.role like '%Uw%') order by o.name");
        return q.getResultList();
    }
    
//    public List<Object[]> findUserGroupByParentId(Integer parentId){
//        Query q = em.createNativeQuery("SELECT id, name FROM user_group WHERE id IN (SELECT id FROM user_group where parent_id = ?1 and enable = 1 union all select id from user_group where parent_id in (SELECT id FROM user_group where parent_id = ?1) and id <> ?1 and enable = 1 )");
//        q.setParameter(1, parentId);
//        List<Object[]> result = q.getResultList();
//        return result;
//    }
    
    private List<UserGroup> findAllUserGroupUnderParentId(Integer parentId) {
//        SELECT * 
//FROM user_group 
//where enable = 1 and status = 1 and parent_id = 137  
//or (parent_id in (SELECT id FROM user_group where parent_id = 137) and enable = 1 and status = 1)
        
        String sql = "";
        
        if(parentId.intValue() == 0) {  // GET ALL USER GROUP FOR ADMINISTRATOR
            sql = "select object(o) from UserGroup as o where enable = true and status = true";
        } else {
            sql = "select object(o) from UserGroup as o where enable = true and status = true and parentId = ?1 "
                + "or (parentId in (select id from UserGroup where parentId = ?1) and enable = true and status = true) order by name";
        }
        
        Query q = em.createQuery(sql);
        
        if(parentId.intValue() != 0) {
            q.setParameter(1, parentId);
        }
        
        return q.getResultList();
    }
        
    public Map<String, Integer> getUserGroupListByParentId(Integer parentId) {
        List<UserGroup> userGroups = findAllUserGroupUnderParentId(parentId);
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (UserGroup obj : userGroups) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
    
}
