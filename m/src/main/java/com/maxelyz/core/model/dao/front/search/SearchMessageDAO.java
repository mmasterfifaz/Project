/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao.front.search;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import com.maxelyz.core.model.value.front.search.SearchMessageValue;
import com.ntier.utils.ParameterMap;
import java.util.Calendar;

/**
 *
 * @author Manop
 */
@Transactional
public class SearchMessageDAO {

    @PersistenceContext
    private EntityManager em;
    private List<ParameterMap> params;

    public List<SearchMessageValue> searchMessage() {
        return searchMessage(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, Integer.MAX_VALUE, null);
    }

    public List<SearchMessageValue> searchMessage(
            Integer sourceTypeID, String emailFrom, String emailTo, Integer accountID, Integer messageID, Date contactDateFrom, Date contactDateTo,
            Integer refMessageID, String status, String subject, String priority, Integer userId, Integer userGroupId,
            String content, Integer serviceID, int firstResult, int maxResults, String ticketId) {

        String SQL_QUERY = generateSearchMessageSQLCmd(sourceTypeID, emailFrom, emailTo, accountID, messageID, contactDateFrom, contactDateTo, refMessageID, 
                status, subject, priority, userId, userGroupId, content, serviceID, ticketId);
        
        Query query = em.createQuery(SQL_QUERY);

        for (ParameterMap parm : params) {
            query.setParameter(parm.getName(), parm.getValue());
        }

        if (maxResults > 0) {
            query.setMaxResults(maxResults);
        }//if
        if (firstResult >= 0) {
            query.setFirstResult(firstResult);
        }//if

        return (List<SearchMessageValue>) query.getResultList();

    }

    public Integer searchMessageeRowCount(
            Integer sourceTypeID, String emailFrom, String emailTo, Integer accountID, Integer messageID, Date contactDateFrom, Date contactDateTo,
            Integer refMessageID, String status, String subject, String priority, Integer userId, Integer userGroupId,
            String content, Integer serviceID) {
        String SQL_QUERY = generateSearchMessageSQLCmdCount(sourceTypeID, emailFrom, emailTo, accountID, messageID, contactDateFrom, contactDateTo, refMessageID, 
                status, subject, priority, userId, userGroupId, content, serviceID);
        Query query = em.createQuery(SQL_QUERY);

        for (ParameterMap parm : params) {
            query.setParameter(parm.getName(), parm.getValue());
        }

        return query.getResultList().size();
    }

    private String generateSearchMessageSQLCmd(
            Integer sourceTypeID, String emailFrom, String emailTo, Integer accountID, Integer messageID, Date contactDateFrom, Date contactDateTo,
            Integer refMessageID, String status, String subject, String priority, Integer userId, Integer userGroupId,
            String content, Integer serviceID, String ticketId) {
        String SQL_SELECT = "SELECT "
                + "NEW "
                + SearchMessageValue.class.getName()
                + "( "
                + "c.name as source, "
                + "a.name as accountID, "
                + "em.soMessage.id as msgID, "
                + "em.emailFrom as emailFrom, "
                + "em.emailTo as emailTo, "
                + "m.activity_time_format as contactDate, "
                + "m.case_status as status, "
                + "em.subject as subject, "
                + "m.user_name as customerName, "
                + "u.name + ' ' + u.surname as lastReceiveByName, "
                + "m.source_subtype_enum_id as sourceSub, "
                + "m.parentSoMessage.id as parentId, "
                + "m.ticketId as ticketId "
                + ") ";
        String SQL_FROM = "FROM SoEmailMessage em "
                + "left join em.soMessage m "
                + "left join m.soService s "
                + "left join m.soAccount a "
                + "left join m.lastReceiveByUsers u "
                + "left join a.soChannel c ";
        String SQL_WHERE = "WHERE 1 = 1 ";
        if(status.equals("ST")){
            SQL_WHERE += "and m.source_subtype_enum_id = 'EM_OUT' ";
        }else if(status.equals("PS")){
            SQL_WHERE += "and (m.source_subtype_enum_id = 'EM_IN' or (m.source_subtype_enum_id = 'EM_OUT' and m.parentSoMessage is null and m.case_status = 'DF')) ";
        }else if(status.equals("WF")){
            SQL_WHERE += "and (m.source_subtype_enum_id = 'EM_IN' or (m.source_subtype_enum_id = 'EM_OUT' and m.parentSoMessage is null and m.case_status = 'WF')) ";
        }else if(status.equals("IG")){
            SQL_WHERE += "and (m.source_subtype_enum_id = 'EM_IN' or (m.source_subtype_enum_id = 'EM_OUT' and m.parentSoMessage is null and m.case_status = 'IG')) ";
        }else{
            SQL_WHERE += "and (m.source_subtype_enum_id = 'EM_IN' or (m.source_subtype_enum_id = 'EM_OUT' and m.parentSoMessage is null)) ";
        }
        
        params = new ArrayList<ParameterMap>();
        ParameterMap param = new ParameterMap();

        if (sourceTypeID != null && sourceTypeID != 0) {
            SQL_WHERE += "and c.id = :sourceTypeID ";
            param = new ParameterMap();
            param.setName("sourceTypeID");
            param.setValue(sourceTypeID);
            params.add(param);
        }

        if (emailFrom != null && emailFrom.toString().trim().length() > 0) {
            emailFrom = String.format("%s%s%s", "%", emailFrom, "%");
            SQL_WHERE += "and em.emailFrom like :emailFrom ";
            param = new ParameterMap();
            param.setName("emailFrom");
            param.setValue(emailFrom);
            params.add(param);
        }
        
        if (emailTo != null && emailTo.toString().trim().length() > 0) {
            emailTo = String.format("%s%s%s", "%", emailTo, "%");
            SQL_WHERE += "and em.emailTo like :emailTo ";
            param = new ParameterMap();
            param.setName("emailTo");
            param.setValue(emailTo);
            params.add(param);
        }
                
        if (accountID != null && accountID != 0) {
            SQL_WHERE += "and a.id = :accountID ";
            param = new ParameterMap();
            param.setName("accountID");
            param.setValue(accountID);
            params.add(param);
        }
        
        if (messageID != null && messageID != 0) {
            SQL_WHERE += "and m.id = :messageID ";
            param = new ParameterMap();
            param.setName("messageID");
            param.setValue(messageID);
            params.add(param);
        }
        
        if (ticketId != null && !ticketId.isEmpty()) {
            ticketId = String.format("%s%s%s", "%", ticketId, "%");
            SQL_WHERE += "and m.ticketId like :ticketId ";
            param = new ParameterMap();
            param.setName("ticketId");
            param.setValue(ticketId);
            params.add(param);
        }
        
        if (contactDateFrom != null) {
            SQL_WHERE += "and m.activity_time_format >= :contactDateFrom ";
            param = new ParameterMap();
            param.setName("contactDateFrom");
                        param.setValue(contactDateFrom);
            params.add(param);
        }

        if (contactDateTo != null) {
            SQL_WHERE += "and m.activity_time_format <= :contactDateTo ";
            param = new ParameterMap();
            param.setName("contactDateTo");

            Calendar date = Calendar.getInstance();
            date.setTime(contactDateTo);
            date.add(Calendar.DATE, 1);
            param.setValue(date.getTime());
            params.add(param);
        }
        
        if (refMessageID != null && refMessageID != 0) {
            SQL_WHERE += "and m.parentSoMessage.id = :refMessageID ";
            param = new ParameterMap();
            param.setName("refMessageID");
            param.setValue(refMessageID);
            params.add(param);
        }
        
        if (status != null && status.toString().trim().length() > 0) {
            if(status.equals("PS")){
                SQL_WHERE += "and (m.case_status = 'PS' or m.case_status = 'DF') ";
            } else if(status.equals("ST")){
                SQL_WHERE += "and (m.case_status = 'CL') ";
            } else {
                status = String.format("%s%s%s", "%", status, "%");
                SQL_WHERE += "and m.case_status like :status ";
                param = new ParameterMap();
                param.setName("status");
                param.setValue(status);
                params.add(param);
            }
        }
        
        if (subject != null && subject.toString().trim().length() > 0) {
            subject = String.format("%s%s%s", "%", subject, "%");
            SQL_WHERE += "and em.subject like :subject ";
            param = new ParameterMap();
            param.setName("subject");
            param.setValue(subject);
            params.add(param);
        }
        
        if (priority != null && priority.toString().trim().length() > 0) {
            priority = String.format("%s%s%s", "%", priority, "%");
            SQL_WHERE += "and m.priority_enum_id like :priority ";
            param = new ParameterMap();
            param.setName("priority");
            param.setValue(priority);
            params.add(param);
        }

        if (userId != null && userId != 0) {
            SQL_WHERE += "and u.id = :userId ";
            param = new ParameterMap();
            param.setName("userId");
            param.setValue(userId);
            params.add(param);
        }
        
        if (userGroupId != null && userGroupId != 0) {
            SQL_WHERE += "and u.id in (SELECT o.id FROM Users as o WHERE o.enable = true and o.status= true and o.userGroup.id = :userGroupId ) ";
            param = new ParameterMap();
            param.setName("userGroupId");
            param.setValue(userGroupId);
            params.add(param);
        }
        
        if (content != null && content.toString().trim().length() > 0) {
            content = String.format("%s%s%s", "%", content, "%");
            SQL_WHERE += "and em.body like :content ";
            param = new ParameterMap();
            param.setName("content");
            param.setValue(content);
            params.add(param);
        }
        
        if (serviceID != null && serviceID != 0) {
            SQL_WHERE += "and s.id = :serviceID ";
            param = new ParameterMap();
            param.setName("serviceID");
            param.setValue(serviceID);
            params.add(param);
        }

        String SQL_GROUP = "";

        String SQL_HAVING = "";

        String SQL_ORDER = "";

        return SQL_SELECT + SQL_FROM + SQL_WHERE + SQL_GROUP + SQL_HAVING + SQL_ORDER;
    }

    private String generateSearchMessageSQLCmdCount(
            Integer sourceTypeID, String emailFrom, String emailTo, Integer accountID, Integer messageID, Date contactDateFrom, Date contactDateTo,
            Integer refMessageID, String status, String subject, String priority, Integer userId, Integer userGroupId,
            String content, Integer serviceID) {
        String SQL_SELECT = "SELECT "
                + "NEW "
                + SearchMessageValue.class.getName()
                + "( "
                + "c.name as source, "
                + "a.name as accountID, "
                + "em.soMessage.id as msgID, "
                + "em.emailFrom as emailFrom, "
                + "em.emailTo as emailTo, "
                + "m.activity_time_format as contactDate, "
                + "m.case_status as status, "
                + "em.subject as subject, "
                + "m.user_name as customerName, "
                + "u.name + ' ' + u.surname as lastReceiveByName "
                + ") ";
        String SQL_FROM = "FROM SoEmailMessage em "
                + "left join em.soMessage m "
                + "left join m.soService s "
                + "left join m.soAccount a "
                + "left join m.lastReceiveByUsers u "
                + "left join a.soChannel c ";
        String SQL_WHERE = "WHERE 1 = 1 ";
        if(status.equals("ST")){
            SQL_WHERE += "and m.source_subtype_enum_id = 'EM_OUT' ";
        }else if(status.equals("PS")){
            SQL_WHERE += "and (m.source_subtype_enum_id = 'EM_IN' or (m.source_subtype_enum_id = 'EM_OUT' and m.case_status = 'DF')) ";
        }else if(status.equals("WF")){
            SQL_WHERE += "and (m.source_subtype_enum_id = 'EM_IN' or (m.source_subtype_enum_id = 'EM_OUT' and m.case_status = 'WF')) ";
        }else if(status.equals("IG")){
            SQL_WHERE += "and (m.source_subtype_enum_id = 'EM_IN' or (m.source_subtype_enum_id = 'EM_OUT' and m.case_status = 'IG')) ";
        }else{
            SQL_WHERE += "and (m.source_subtype_enum_id = 'EM_IN' or m.source_subtype_enum_id = 'EM_OUT') ";
        }
        
        params = new ArrayList<ParameterMap>();
        ParameterMap param = new ParameterMap();

        if (sourceTypeID != null && sourceTypeID != 0) {
            SQL_WHERE += "and c.id = :sourceTypeID ";
            param = new ParameterMap();
            param.setName("sourceTypeID");
            param.setValue(sourceTypeID);
            params.add(param);
        }

        if (emailFrom != null && emailFrom.toString().trim().length() > 0) {
            emailFrom = String.format("%s%s%s", "%", emailFrom, "%");
            SQL_WHERE += "and em.emailFrom like :emailFrom ";
            param = new ParameterMap();
            param.setName("emailFrom");
            param.setValue(emailFrom);
            params.add(param);
        }
        
        if (emailTo != null && emailTo.toString().trim().length() > 0) {
            emailTo = String.format("%s%s%s", "%", emailTo, "%");
            SQL_WHERE += "and em.emailTo like :emailTo ";
            param = new ParameterMap();
            param.setName("emailTo");
            param.setValue(emailTo);
            params.add(param);
        }

        if (accountID != null && accountID != 0) {
            SQL_WHERE += "and a.id = :accountID ";
            param = new ParameterMap();
            param.setName("accountID");
            param.setValue(accountID);
            params.add(param);
        }
        
        if (messageID != null && messageID != 0) {
            SQL_WHERE += "and m.id = :messageID ";
            param = new ParameterMap();
            param.setName("messageID");
            param.setValue(messageID);
            params.add(param);
        }
        
        if (contactDateFrom != null) {
            SQL_WHERE += "and m.activity_time_format >= :contactDateFrom ";
            param = new ParameterMap();
            param.setName("contactDateFrom");

            Calendar date = Calendar.getInstance();
            date.setTime(contactDateFrom);
            date.add(Calendar.DATE, 1);
            param.setValue(date.getTime());
            params.add(param);
        }

        if (contactDateTo != null) {
            SQL_WHERE += "and m.activity_time_format <= :contactDateTo ";
            param = new ParameterMap();
            param.setName("contactDateTo");

            Calendar date = Calendar.getInstance();
            date.setTime(contactDateTo);
            date.add(Calendar.DATE, 1);
            param.setValue(date.getTime());
            params.add(param);
        }
        
        if (refMessageID != null && refMessageID != 0) {
            SQL_WHERE += "and m.parentSoMessage.id = :refMessageID ";
            param = new ParameterMap();
            param.setName("refMessageID");
            param.setValue(refMessageID);
            params.add(param);
        }
        
        if (status != null && status.toString().trim().length() > 0) {
            if(status.equals("PS")){
                SQL_WHERE += "and (m.case_status = 'PS' or m.case_status = 'DF') ";
            } else if(status.equals("ST")){
                SQL_WHERE += "and (m.case_status = 'CL') ";
            }else{
                status = String.format("%s%s%s", "%", status, "%");
                SQL_WHERE += "and m.case_status = :status ";
                param = new ParameterMap();
                param.setName("status");
                param.setValue(status);
                params.add(param);
            }
        }
        
        if (subject != null && subject.toString().trim().length() > 0) {
            subject = String.format("%s%s%s", "%", subject, "%");
            SQL_WHERE += "and em.subject like :subject ";
            param = new ParameterMap();
            param.setName("subject");
            param.setValue(subject);
            params.add(param);
        }
                
        if (priority != null && priority.toString().trim().length() > 0) {
            priority = String.format("%s%s%s", "%", priority, "%");
            SQL_WHERE += "and m.priority_enum_id = :priority ";
            param = new ParameterMap();
            param.setName("priority");
            param.setValue(priority);
            params.add(param);
        }
        
        if (userId != null && userId != 0) {
            SQL_WHERE += "and u.id = :userId ";
            param = new ParameterMap();
            param.setName("userId");
            param.setValue(userId);
            params.add(param);
        }
        
        if (userGroupId != null && userGroupId != 0) {
            SQL_WHERE += "and u.id in (SELECT o.id FROM Users as o WHERE o.enable = true and o.status= true and o.userGroup.id = :userGroupId ) ";
            param = new ParameterMap();
            param.setName("userGroupId");
            param.setValue(userGroupId);
            params.add(param);
        }
        
        if (content != null && content.toString().trim().length() > 0) {
            content = String.format("%s%s%s", "%", content, "%");
            SQL_WHERE += "and em.body like :content ";
            param = new ParameterMap();
            param.setName("content");
            param.setValue(content);
            params.add(param);
        }
        
        if (serviceID != null && serviceID != 0) {
            SQL_WHERE += "and s.id = :serviceID ";
            param = new ParameterMap();
            param.setName("serviceID");
            param.setValue(serviceID);
            params.add(param);
        }

        String SQL_GROUP = "";

//        String SQL_GROUP = "GROUP BY c.id ";

        String SQL_HAVING = "";

        String SQL_ORDER = "";

        return SQL_SELECT + SQL_FROM + SQL_WHERE + SQL_GROUP + SQL_HAVING + SQL_ORDER;

    }

    public List<ParameterMap> getParams() {
        return params;
    }

    public void setParams(List<ParameterMap> params) {
        this.params = params;
    }
}
