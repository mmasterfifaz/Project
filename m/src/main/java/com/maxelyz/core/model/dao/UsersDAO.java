/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.Acl;
import com.maxelyz.core.model.entity.UserGroup;
import com.maxelyz.core.model.entity.Users;
import com.maxelyz.core.model.entity.WorkflowRuleDetail;
import com.maxelyz.utils.JSFUtil;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.mail.Session;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class UsersDAO {

    @PersistenceContext
    private EntityManager em;

    public List<Users> findUsersByLoginName(String loginName) {
        Query q = em.createQuery("select object(o) from Users as o where UPPER(o.loginName) =:loginName and o.enable = true and o.status = true");
        q.setParameter("loginName", loginName.toUpperCase());
        return q.getResultList();
    }

    public List<Users> findUsersByTelephonyLoginName(String telephonyLoginName) { 
        Query q = em.createQuery("select object(o) from Users as o where UPPER(o.telephonyLoginName) =:telephonyLoginName and o.enable = true and o.status = true");
        q.setParameter("telephonyLoginName", telephonyLoginName.toUpperCase());
        return q.getResultList();
    }

    public void create(Users users) throws PreexistingEntityException, Exception {
        String username = JSFUtil.getUserSession().getUserName();
        users.setCreateBy(username);
        users.setCreateDate(new Date());
        users.setLastChangedPassword(new Date());
        em.persist(users);
    }

    public void edit(Users users) {
        String username = JSFUtil.getUserSession().getUserName();
        users.setUpdateBy(username);
        users.setUpdateDate(new Date());
        users = em.merge(users);
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        Users users;
        try {
            users = em.getReference(Users.class, id);
            users.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The users with id " + id + " no longer exists.", enfe);
        }

        em.remove(users);

    }

    public List<Users> findSupervisor(Users users) {
        Query q = em.createQuery("SELECT object(o) FROM Users as o WHERE o.enable = true and o.status= true "
                + "and o.userGroup.id IN (SELECT g.id  FROM UserGroup as g WHERE  g.role like '%Supervisor%' AND g.parentId = ?1) order by o.name");
        q.setParameter(1, users.getUserGroup().getId());
        return q.getResultList();
    }

     public List<Users> findAgentWithSystem(Users users) { 
        String sql = "SELECT object(o) FROM Users as o WHERE (o.enable = true and o.status= true or o.isSystem = true) ";
        if (users.getUserGroup().getRole().contains("CampaignManager")) {
            sql += "AND o.userGroup.id IN (SELECT g.id FROM UserGroup g WHERE (g.role like '%Agent%' AND g.parentId = ?1) "
                    + "OR g.parentId IN (SELECT u.id  FROM UserGroup u WHERE  u.role like '%Supervisor%' AND u.parentId = ?1)) order by o.name";
        } else {
            sql += "AND o.userGroup.id IN (SELECT g.id FROM UserGroup g WHERE (g.role LIKE '%Agent%' AND g.id = ?1) "
                    + "OR (g.role LIKE '%Agent%' AND g.parentId = ?1)) order by o.name";
        }
        Query q = em.createQuery(sql);
        q.setParameter(1, users.getUserGroup().getId());
        return q.getResultList();
    }

     public List<Users> findAgent(Users users) { 
        String sql = "SELECT object(o) FROM Users as o WHERE o.enable = true and o.status= true ";
        if (users.getUserGroup().getRole().contains("CampaignManager")) {
            sql += "AND o.userGroup.id IN (SELECT g.id FROM UserGroup g WHERE (g.role like '%Agent%' AND g.parentId = ?1) "
                + "OR g.parentId IN (SELECT u.id  FROM UserGroup u WHERE  u.role like '%Supervisor%' AND u.parentId = ?1)) order by o.name";
        } else {
            sql += "AND o.userGroup.id IN (SELECT g.id FROM UserGroup g WHERE (g.role LIKE '%Agent%' AND g.id = ?1) "
                    + "OR (g.role LIKE '%Agent%' AND g.parentId = ?1)) order by o.name";
        }
        Query q = em.createQuery(sql);
        q.setParameter(1, users.getUserGroup().getId()); 
        return q.getResultList();
    }
     
    public List<Users> findAgentByGroupId(Integer groupId) {
        String sql = "SELECT object(o) FROM Users as o WHERE o.enable = true and o.status= true and o.userGroup.id = ?1 order by o.name";
        Query q = em.createQuery(sql);
        q.setParameter(1, groupId);
        return q.getResultList();
    }

    public List<Users> findSoAgentByGroupId(Integer groupId) {
        //String sql = "SELECT object(o) FROM Users as o WHERE o.enable = true and o.status= true and o.userGroup.id = ?1 order by o.name";
        String sql = "select distinct u.id from users u"
                + " inner join so_service_user su on su.user_id = u.id"
                + " inner join so_service s on s.id = su.so_service_id"
                + " where s.enable = 1 and s.status = 1"
                + " and u.user_group_id = :groupId"
                + " order by u.id";
        Query q = em.createNativeQuery(sql);
        q.setParameter("groupId", groupId);
        List<Integer> list = q.getResultList();
        List<Users> usersList = new ArrayList<Users>();
        for (Integer i : list) {
            usersList.add(findUsers(i));
        }
        return usersList;
    }

    public List<Users> findPoolingAgent(Users users, int marketingId) {
        String sql = "SELECT object(o) FROM Users as o WHERE 1=1 and o.enable = true and o.status= true ";
        if (users.getUserGroup().getRole().contains("CampaignManager")) {
            sql += "AND o.userGroup.id IN (SELECT g.id FROM UserGroup g "
                    + "WHERE (g.role like '%Agent%' AND g.parentId = ?1) OR g.parentId IN "
                    + "(SELECT u.id  FROM UserGroup u WHERE  u.role like '%Supervisor%' AND u.parentId = ?1)) AND "
                    + "o.id not in (select p.user.id from AssignmentPooling as p join p.assignment a where a.marketing.id = ?2 and p.enable = true) "
                    + "order by o.name";
        } else {
            sql += "AND o.userGroup.id IN (SELECT g.id FROM UserGroup g "
                    + "WHERE (g.role LIKE '%Agent%' AND g.id = ?1) OR (g.role LIKE '%Agent%' AND g.parentId = ?1)) AND "
                    + "o.id not in (select p.user.id from AssignmentPooling as p join p.assignment a where a.marketing.id = ?2 and p.enable = true) "
                    + "order by o.name";
        }
        Query q = em.createQuery(sql);
        q.setParameter(1, users.getUserGroup().getId());
        q.setParameter(2, marketingId);
        return q.getResultList();
    }

    public Map<String, Integer> getUserListByUserGroup(int userGroupId) {
        List<Users> usersList = findUsersEntitiesByUserGroup(userGroupId);
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Users obj : usersList) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }

    public Map<String, Integer> getAllAgentBySup(Users user) {
        List<Users> usersList = findAgent(user);
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Users obj : usersList) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }

     public List<Users> findUsersWithSystemEntitiesByUserGroup(int userGroupId) {
        String sql = "select o from Users o where (o.enable = true and o.status = true or o.isSystem = true) ";
        if (userGroupId != 0) {
            sql += " and o.userGroup.id = " + userGroupId;
        }
        sql += " order by o.name, o.surname";

        Query q = em.createQuery(sql);
        boolean all = true;
        if (!all) {
            q.setMaxResults(-1);
            q.setFirstResult(-1);
        }
        return q.getResultList();
    }

    public List<Users> findUsersEntitiesByUserGroup(int userGroupId) {
        String sql = "select o from Users o where o.enable = true and o.status= true ";
        if (userGroupId != 0) {
            sql += " and o.userGroup.id = " + userGroupId;
        }
        sql += " order by o.name, o.surname";

        Query q = em.createQuery(sql);
        boolean all = true;
        if (!all) {
            q.setMaxResults(-1);
            q.setFirstResult(-1);
        }
        return q.getResultList();
    }
     
    public List<Users> findPoolingAgentByUserGroup(int userGroupId, int marketingId) {
        String sql = "select o from Users o where o.enable = true and o.status= true ";
        if (userGroupId != 0) {
            sql += " and o.userGroup.id = " + userGroupId;
            sql += " and o.id not in (select p.user.id from AssignmentPooling as p join p.assignment a where a.marketing.id = " + marketingId;
            sql += " and p.enable = true)"; //and a.poolingToDate > getdate()))";
        }
        sql += " order by o.name, o.surname";

        Query q = em.createQuery(sql);
        boolean all = true;
        if (!all) {
            q.setMaxResults(-1);
            q.setFirstResult(-1);
        }
        return q.getResultList();
    }

    public List<Users> findUsersEntities() {
        return findUsersEntities(true, -1, -1);
    }

    public List<Users> findUsersEntities(int maxResults, int firstResult) {
        return findUsersEntities(false, maxResults, firstResult);
    }

    private List<Users> findUsersEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from Users as o where enable = true and status = true order by name, surname");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

//    private List<Users> findUsersServiceEntities(boolean all, int maxResults, int firstResult) {
//        Query q = em.createQuery("select * from Users as o where enable = true and status = true " +
//                "and  id in (select su.user_id from so_service s, so_service_user su where s.id = su.so_service_id and s.enable = 1 and s.status = 1) " +
//                "order by name, surname");
//        if (!all) {
//            q.setMaxResults(maxResults);
//            q.setFirstResult(firstResult);
//        }
//        return q.getResultList();
//    }

    public List<Users> findAllUsersEntities() {
        Query q = em.createQuery("select object(o) from Users as o where enable = true order by name, surname");
        return q.getResultList();
    }

    public Users findUsers(Integer id) {
        return em.find(Users.class, id);
    }

    public int getUsersCount() {
        return ((Long) em.createQuery("select count(o) from Users as o").getSingleResult()).intValue();
    }

    public List<Users> findUsersByName(String nameKeyword) {
        Query q = em.createQuery("SELECT object(u) FROM Users u WHERE u.enable = true and (u.name like :name or u.surname like :name or u.loginName like :name)");
        q.setParameter("name", "%" + nameKeyword + "%");
        return q.getResultList();
    }

    public List<Users> findUsersDelegateByName(String nameKeyword) {
        String sql = "";
        String order = "";
        sql = "SELECT object(u) FROM Users u WHERE u.enable = true and status = true and (u.name like :name or u.surname like :name or u.loginName like :name) ";
        order = "order by name, surname";

        Query q = em.createQuery(sql + order);
        q.setParameter("name", "%" + nameKeyword + "%");
        return q.getResultList();
    }

    public Map<String, Integer> getUsersListByUserGroup(int userGroupId) {
        List<Users> users = this.findUsersEntitiesByUserGroup(userGroupId);
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Users obj : users) {
            values.put(obj.getName() + " " + obj.getSurname(), obj.getId());
        }
        return values;
    }

    public Map<String, Integer> getUsersList() {
        List<Users> users = this.findUsersEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Users obj : users) {
            values.put(obj.getName() + " " + obj.getSurname(), obj.getId());
        }
        return values;
    }

    public List<Users> findSoSupervisor() {
        Query q = em.createQuery("select object(o) from Users o join o.userGroup u where o.enable = true and o.status = true "
                + "and u.role like '%SOSupervisor%' and u.enable = true and u.status = true order by o.name, o.surname");
        return q.getResultList();
    }

    public Map<String, Integer> getSupApprovalList() {
        List<Users> users = this.findSoSupervisor();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Users obj : users) {
            values.put(obj.getName() + " " + obj.getSurname(), obj.getId());
        }
        return values;
    }

    public boolean forceChangePassword(Integer userId) {
        Users user = findUsers(userId);
        if (user.getForceChangePassword() != null && user.getForceChangePassword() == 1) {
            return true;
        }
        return false;
    }

    public static boolean isValidPassword(String password) {
        if (password != null) {
            if (password.length() >= 8) {
                return true;
            }
        }
        return false;
    }

    public Users findAgentForAutoDialer() {
        try {
            Query q = em.createQuery("select object(o) from Users as o where isSystem = true order by createDate desc");
            q.setMaxResults(1);
            return (Users) q.getSingleResult();
        } catch (NoResultException ne) {
            return null;
        }
    }

    public List<Users> findUsersUnderParentBySearch(Integer userGroupId, String keyword, String status, String license, Date dateFrom, Date dateTo, String login, String telephony, String ldap, Integer parentId) {
        SimpleDateFormat sdfFrom = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat sdfTo = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String query;
        String select = "select object(o) from Users o where (o.enable = true or o.isSystem = true) ";

        String where = "";
        if (!JSFUtil.getUserSession().getUsers().getIsAdministrator()) {
            where = "and o.userGroup.id in (select id from UserGroup where enable = true and status = true and "
                    + "parentId = " + parentId + " or (parentId in (select id from UserGroup where parentId = " + parentId + ") and enable = true and status = true))";
        }

        if ("enable".equals(status)) {
            where += "and (o.status = 1 or o.isSystem = true) ";
        } else if ("disable".equals(status)) {
            where += "and (o.status = 0 or o.isSystem = true) ";
        } else {
            where += "";
        }
        if (userGroupId != null && userGroupId > 0) {
            where += "and o.userGroup.id = " + userGroupId + " ";
        }

        if (keyword != null && !keyword.isEmpty()) {
            where += "and (o.name like '%" + keyword.trim() + "%' or o.surname like '%" + keyword.trim() + "%') ";
        }
        if (login != null && !login.isEmpty()) {
            where += "and o.loginName like '%" + login.trim() + "%' ";
        }
        if (license != null && !license.isEmpty()) {
            where += "and o.licenseNo like '%" + license.trim() + "%' ";
        }
        if (telephony != null && !telephony.isEmpty()) {
            where += "and o.telephonyLoginName like " + "'%" + telephony.trim() + "%' ";
        }
        if (ldap != null && !ldap.isEmpty()) {
            where += "and o.ldapLogin like " + "'%" + ldap.trim() + "%' ";
        }
        if (dateFrom != null && dateTo != null) {
            where += "and (o.hiredDate >= '" + sdfFrom.format(dateFrom) + "' and o.hiredDate <= '" + sdfTo.format(dateTo) + "') ";
        }

        String orderBy = "order by o.name, o.surname";
        query = select + where + orderBy;
        Query q = em.createQuery(query);

        return q.getResultList();
    }

    public List<Users> findUsersBySearch(Integer userGroupId, String keyword, String status, String license, Date dateFrom, Date dateTo, String login, String telephony, String ldap) {
        SimpleDateFormat sdfFrom = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat sdfTo = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String query;
        String select = "select object(o) from Users o where o.enable = true ";

        String where;
        if ("enable".equals(status)) {
            where = "and o.status = 1 ";
        } else if ("disable".equals(status)) {
            where = "and o.status = 0 ";
        } else {
            where = "";
        }
        if (userGroupId != null && userGroupId > 0) {
            where += "and o.userGroup.id=" + userGroupId + " ";
        }

        if (keyword != null && !keyword.isEmpty()) {
            where += "and (o.name like '%" + keyword.trim() + "%' or o.surname like '%" + keyword.trim() + "%') ";
        }
        if (login != null && !login.isEmpty()) {
            where += "and o.loginName like '%" + login.trim() + "%' ";
        }
        if (license != null && !license.isEmpty()) {
            where += "and o.licenseNo like '%" + license.trim() + "%' ";
        }
        if (telephony != null && !telephony.isEmpty()) {
            where += "and o.telephonyLoginName like " + "'%" + telephony.trim() + "%' ";
        }
        if (ldap != null && !ldap.isEmpty()) {
            where += "and o.ldapLogin like " + "'%" + ldap.trim() + "%' ";
        }
        if (dateFrom != null && dateTo != null) {
            where += "and (o.hiredDate >= '" + sdfFrom.format(dateFrom) + "' and o.hiredDate <= '" + sdfTo.format(dateTo) + "') ";
        }

        String orderBy = "order by o.name, o.surname";
        query = select + where + orderBy;
        Query q = em.createQuery(query);

        return q.getResultList();
    }

    public List<Acl> findAclByUser(Users users) {
        Query q = em.createQuery("select a from UserGroupAcl g join g.acl a where g.userGroup = ?1");
        q.setParameter(1, users.getUserGroup());
        return q.getResultList();
    }

    public List<Users> checkUsersByLoginName(String loginName, Integer id) {
        Query q;
        if (id == 0) {
            q = em.createQuery("select object(o) from Users as o where o.loginName =:loginName and o.enable = true");
        } else {
            q = em.createQuery("select object(o) from Users as o where o.loginName =:loginName and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("loginName", loginName);
        return q.getResultList();
    }

    public List<Users> findUserUnderParentId(Users users) {
        String sql = "SELECT object(o) FROM Users as o WHERE o.enable = true and o.status= true ";
        if (users.getUserGroup().getRole().contains("CampaignManager")) {
            sql += "AND o.userGroup.id IN (SELECT g.id FROM UserGroup g WHERE (g.role like '%Agent%' AND g.parentId = ?1) "
                    + "OR g.parentId IN (SELECT u.id  FROM UserGroup u WHERE  u.role like '%Supervisor%' AND u.parentId = ?1)) "
                    + "OR o.userGroup.id IN (SELECT g.id  FROM UserGroup as g WHERE  g.role like '%Supervisor%' AND g.parentId = ?1) order by o.name";
        } else {
            sql += "AND o.userGroup.id IN (SELECT g.id FROM UserGroup g WHERE (g.role like '%Agent%' AND g.parentId = ?1) "
                    + "OR g.parentId IN (SELECT u.id  FROM UserGroup u WHERE  u.role like '%Supervisor%' AND u.parentId = ?1)) "
                    + "order by o.name";
        }
        Query q = em.createQuery(sql);
        q.setParameter(1, users.getUserGroup().getId());
        return q.getResultList();
    }

    public List<Users> findAgentUnderParentId(Users users) {
        String sql = "SELECT object(o) FROM Users as o WHERE o.enable = true and o.status= true ";
        if (users.getUserGroup().getRole().contains("CampaignManager")) {
            sql += "AND o.userGroup.id IN (SELECT g.id FROM UserGroup g WHERE (g.role like '%Agent%' AND g.parentId = ?1) "
                    + "OR g.parentId IN (SELECT u.id  FROM UserGroup u WHERE  u.role like '%Supervisor%' AND u.parentId = ?1)) "
                    + "order by o.name";
        } else {
            sql += "AND o.userGroup.id IN (SELECT g.id FROM UserGroup g WHERE (g.role like '%Agent%' AND g.parentId = ?1)) "
                    + "order by o.name";
        }
        Query q = em.createQuery(sql);
        q.setParameter(1, users.getUserGroup().getId());
        return q.getResultList();
    }

    public void deleteUserMember(Integer userId) {
        Query q = em.createQuery("Delete from UserMember where users.id = ?1");
        q.setParameter(1, userId);
        q.executeUpdate();
    }

    public List<Users> findSupervisorByParentId(Integer parentId) {
        Query q = em.createQuery("SELECT object(o) FROM Users as o WHERE o.enable = true and o.status= true "
                + "and o.userGroup.id = ?1 and o.userGroup.role like '%Supervisor%'");
        q.setParameter(1, parentId);
        return q.getResultList();
    }

    public List<Users> findByRole(String role) {

        List<Users> list = null;
        try {
            String sql = "select object(u) from Users u"
                    + " where upper(u.userGroup.role) like '%" + role + "%'";

            Query q = em.createQuery(sql);

            list = q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return list;
    }

    public void updateInboxQuota(List<Users> users) {
        for (Users u : users) {
            Query q = em.createQuery("Update Users set soOnhandQuota = ?2, soInboxQuota = ?3 where id = ?1");
            q.setParameter(1, u.getId());
            q.setParameter(2, u.getSoOnhandQuota());
            q.setParameter(3, u.getSoInboxQuota());
            q.executeUpdate();
        }
    }

    public List<Users> findSoAgentBySearch(Integer userGroupId, String keyword) {
        String query;
        String select = "select object(o) from Users o join o.userGroup u where o.enable = true and u.role like '%SOAgent%' "
                + "and u.enable = true and u.status = true ";

        String where = "";
        if (userGroupId != null && userGroupId > 0) {
            where += "and u.id=" + userGroupId + " ";
        }
        if (keyword != null && !keyword.isEmpty()) {
            where += "and o.loginName like '%" + keyword.trim() + "%' ";
        }

        String orderBy = "order by o.loginName";
        query = select + where + orderBy;
        Query q = em.createQuery(query);

        return q.getResultList();
    }

    public List<Users> findSoAgentList() {
        Query q = em.createQuery("select object(o) from Users as o join o.userGroup u where o.enable = true and o.status = true "
                + "and u.role like '%SOAgent%' and u.enable = true and u.status = true order by o.loginName");
        return q.getResultList();
    }

    public List<Users> findSoAgentListOrderByName() {
        Query q = em.createQuery("select object(o) from Users as o join o.userGroup u where o.enable = true and o.status = true "
                + "and u.role like '%SOAgent%' and u.enable = true and u.status = true order by o.name, o.surname");
        return q.getResultList();
    }

    public Map<String, Integer> getAgentList() {
        List<Users> users = this.findAgentList();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Users obj : users) {
            values.put(obj.getName() + " " + obj.getSurname(), obj.getId());
        }
        return values;
    }

    public List<Users> findAgentList() {
        Query q = em.createQuery("select object(o) from Users as o join o.userGroup u where o.enable = true and o.status = true "
                + "and u.role like '%Agent%' and u.enable = true and u.status = true order by o.name, o.surname");
        return q.getResultList();
    }

    public void updateNote(Integer userId, String makenote) {
        Query q = em.createQuery("update Users u set u.makenote = :makenote where u.id = :id");
        q.setParameter("makenote", makenote);
        q.setParameter("id", userId);
        q.executeUpdate();
    }

    public List<Users> checkUsersByExternalId(String externalUserId,int id) {
        Query q;
        q = em.createQuery("select object(o) from Users as o where o.externalUserId =:externalUserId and o.enable = true and o.externalUserId !='' and id not like :id");
        q.setParameter("id", id);
        q.setParameter("externalUserId", externalUserId);
        return q.getResultList();
    }

    public List<Users> findManagerUserByCampaginManage() {
        Query q = em.createQuery("SELECT object(o) FROM Users as o WHERE o.enable = true and o.status= true "
                + "and o.userGroup.id IN (SELECT g.id  FROM UserGroup as g WHERE  g.role like '%CampaignManager%' and g.enable = true and g.status= true) order by o.name");
        return q.getResultList();
    }

    public long checkCitizenID(String citizenId, Integer id) {
        Query q;
        if (id == 0) {
            q = em.createQuery("select count(o) from Users as o where o.citizenId =:citizenId and o.enable = true");
        } else {
            q = em.createQuery("select count(o) from Users as o where o.citizenId =:citizenId and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("citizenId", citizenId);
        return ((Long)q.getSingleResult()).intValue();
    }

    public List<String> getUserListByUserGroupRole() {
        Query q = this.em.createNativeQuery("select distinct concat(u.name, ' ', u.surname) as name from users u"
                + " join user_group ug on ug.id = u.user_group_id"
                + " where ug.role like '%qc%'");
        return q.getResultList();
    }
}
