/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao.front.todolist;

import com.maxelyz.core.model.value.front.todolist.IncomingCaseAcceptanceValue;
import com.maxelyz.core.model.value.front.todolist.ToDoListValue;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Niranriths
 */
public class TodoListDAO {

    @PersistenceContext
    private EntityManager em;

    public List<ToDoListValue> findIncomingValues(int userId) {
        return findIncomingValues(userId, 0, Integer.MAX_VALUE);
    }

    public List<ToDoListValue> findIncomingValues(int userId, int firstResult, int maxResults) {

        String SQL_SELECT = "SELECT "
                + "NEW "
                + ToDoListValue.class.getName()
                + "( "
                + "cc.id as contactCaseId, "
                + "a.id as activityId, "
                + "a.dueDate as dueDate, "
                + "a.activityDate as activityDate, "
                + "cc.code as caseId, "
                + "a.createDate as sendDate, "
                + "s.loginName as sender, "
                + "c.name + ' ' + c.surname as customer, "
                + "cty.name as caseType, "
                + "cto.name as caseTopic, "
                + "cd.name as caseDetail, "
                + "a.slaAcceptDate as slaAcceptDate, "
                + "cc.slaCloseDate as slaCloseDate, "
                + "r.loginName as receiver, "
                + "' ', "
                + "cc.priority, "
                + "cc.attachFile "
                + ") ";
        String SQL_FROM = "FROM ContactCase as cc "
                + "JOIN cc.activityCollection as a "
                + "JOIN cc.caseDetailId as cd "
                + "JOIN cd.caseTopicId as cto "
                + "JOIN cto.caseTypeId as cty "
                + "JOIN a.userSenderId as s "
                + "JOIN a.userReceiverId as r "
                + "LEFT JOIN r.userMemberCollection as m "
                + "JOIN cc.customerId as c "
                + "";
        String SQL_WHERE = "WHERE cc.status = 'pending' and a.receiveStatus is null and (a.userReceiverId.id = :userId or m.usersMember.id = :userId) "
                + " ";

        String SQL_GROUP = "group by cc.id,a.id,a.dueDate,a.activityDate,cc.code,a.createDate,s.loginName,c.name + ' ' + c.surname,cty.name,cto.name,cd.name,a.slaAcceptDate,cc.slaCloseDate,r.loginName,cc.priority,cc.attachFile ";

        String SQL_HAVING = "";

        String SQL_ORDER = "ORDER BY a.createDate";

        String SQL_QUERY = SQL_SELECT + SQL_FROM + SQL_WHERE + SQL_GROUP + SQL_HAVING + SQL_ORDER;
        Query query = em.createQuery(SQL_QUERY);

        if ( maxResults > 0 ) {
            query.setMaxResults(maxResults);
        }//if
        if ( firstResult >= 0 ) {
            query.setFirstResult(firstResult);
        }//if

        query.setParameter("userId", userId);

        return (List<ToDoListValue>)query.getResultList();
    }

    public List<Integer> findContactCasePending(Integer userId){
        List<Integer> list = null;
        String SQL_QUERY = "select distinct(cc.id) from ContactCase cc "
                + "left join cc.activityCollection a "
                + "where cc.status not like 'closed' "
                + "and ((cc.usersCreate.id = :userId or a.userSenderId.id = :userId) or (a.userReceiverId.id = :userId and a.receiveStatus = 'accepted') "
                + "or (a.userSenderId.id = :userId and a.receiveStatus = 'reject')) "
                + "order by cc.id ";
        Query query = em.createQuery(SQL_QUERY);
        query.setParameter("userId", userId);
        list = (List<Integer>) query.getResultList();

        return list;
    }

    public List<ToDoListValue> findPendingValues(int userId) {
        return findPendingValues(userId, 0, Integer.MAX_VALUE);
    }

    public List<ToDoListValue> findPendingValues(int userId, int firstResult, int maxResults) {

        String SQL_SELECT = "SELECT "
                + "NEW "
                + ToDoListValue.class.getName()
                + "( "
                + "cc.id as contactCaseId, "
                + "cc.code as caseId, "
                + "c.name + ' ' + c.surname as customer, "
                + "cc.createDate as createDate, "
                + "cty.name as caseType, "
                + "cto.name as caseTopic, "
                + "cd.name as caseDetail, "
                + "cc.slaCloseDate as slaCloseDate, "
                + "cc.description as description"
                + ") ";

        String SQL_FROM = "FROM ContactCase as cc "
                + "JOIN cc.activityCollection as a "
                + "JOIN cc.caseDetailId as cd "
                + "JOIN cd.caseTopicId as cto "
                + "JOIN cto.caseTypeId as cty "
                + "JOIN cc.customerId as c "
                + "";
        
//        String SQL_WHERE = "WHERE cc.status <> 'closed' and a.receiveStatus = 'accepted' and a.userReceiverId.id = :userId "; // first time add case, don't have activity
        String SQL_WHERE = "WHERE (cc.status = 'pending' and cc.usersCreate.id = :userId) or (a.userReceiverId.id = :userId and a.receiveStatus = 'accepted') ";

        String SQL_GROUP = "";

        String SQL_HAVING = "";

        String SQL_ORDER = "ORDER BY cc.code";

        String SQL_QUERY = SQL_SELECT + SQL_FROM + SQL_WHERE + SQL_GROUP + SQL_HAVING + SQL_ORDER;
        Query query = em.createQuery(SQL_QUERY);

        if ( maxResults > 0 ) {
            query.setMaxResults(maxResults);
        }//if
        if ( firstResult >= 0 ) {
            query.setFirstResult(firstResult);
        }//if

        query.setParameter("userId", userId);

        return (List<ToDoListValue>)query.getResultList();
    }

    public IncomingCaseAcceptanceValue findIncomingCaseAcceptanceValue(int activityId) {

        String SQL_SELECT = "SELECT "
                + "NEW "
                + IncomingCaseAcceptanceValue.class.getName()
                + "( "
                + "cc.id as contactCaseId, "
                + "a.id as activityId, "
                + "ch.type as serviceType, "
                + "cc.code as caseID, "
                + "cty.name as caseType, "
                + "cto.name as caseTopic, "
                + "cd.name as caseDetail, "
                + "cr.name as reasonOfRequest, "
                + "cc.description as description, "
                + "cc.status as status, "
                + "cc.contactDate as contactDate, "
                + "ch.name as contactChanel, "
                + "cc.createDate as createdDate, "
                + "cc.createBy as createdBy, "
                + "cc.updateDate as lastUpdate, "
                + "cc.updateBy as updatedBy, "
                + "' ' as attachment, " // ดึงแยกจาก query นี้ เพราะอาจจะมีมากกว่า 1 รายการ
                + "cc.remark as remark"
                + ") ";

        String SQL_FROM = "FROM "
                + "ContactCase as cc "
                + "JOIN cc.activityCollection as a "
                + "JOIN cc.caseDetailId as cd "
                + "JOIN cd.caseTopicId as cto "
                + "JOIN cto.caseTypeId as cty "
                + "JOIN cc.channelId as ch "
                + "LEFT JOIN cc.caseRequestId as cr "
                + "";

         String SQL_WHERE = "WHERE "
                + "a.id = :activityId "
                + "";

        String SQL_ORDER = "ORDER BY a.updateDate desc "
                + "";

        String SQL_QUERY = SQL_SELECT + SQL_FROM + SQL_WHERE + SQL_ORDER;
        Query query = em.createQuery(SQL_QUERY);

        query.setParameter("activityId", activityId);
        return (IncomingCaseAcceptanceValue)query.getSingleResult();
    }
}
