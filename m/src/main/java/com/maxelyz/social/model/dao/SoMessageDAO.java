/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.Users;
import com.maxelyz.social.model.entity.SoEmailSignature;
import com.maxelyz.social.model.entity.SoIgnoreReason;
import com.maxelyz.social.model.entity.SoMessage;
import com.maxelyz.social.model.entity.SoMsgCasetypeMap;
import com.maxelyz.social.model.entity.SoMsgUserAssigntime;
import com.maxelyz.social.model.entity.SoServiceOutgoingAccount;
import com.maxelyz.social.model.entity.SoUserSignature;
import com.maxelyz.utils.JSFUtil;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Transactional
public class SoMessageDAO {

    private static Logger log = Logger.getLogger(SoMessage.class);
    @PersistenceContext
    private EntityManager em;

    public void create(SoMessage soMessage) throws PreexistingEntityException, Exception {
        em.persist(soMessage);
    }

    public void edit(SoMessage soMessage) throws NonexistentEntityException, Exception {
        soMessage = em.merge(soMessage);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        SoMessage soMessage;
        try {
            soMessage = em.getReference(SoMessage.class, id);
            soMessage.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The soMessage with id " + id + " no longer exists.", enfe);
        }
        em.remove(soMessage);
    }

    public List<SoMessage> findSoMessageEntities() {
        return findSoMessageEntities(true, -1, -1);
    }

    public List<SoMessage> findSoMessageEntities(int maxResults, int firstResult) {
        return findSoMessageEntities(false, maxResults, firstResult);
    }

    private List<SoMessage> findSoMessageEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from SoMessage as o order by o.id");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public SoMessage findSoMessage(Integer id) {
        return em.find(SoMessage.class, id);
    }

    public int getSoMessageCount() {
        return ((Long) em.createQuery("select count(o) from SoMessage as o").getSingleResult()).intValue();
    }

    public List<SoMessage> findSoMessageByReceivedById(Integer userId, String pageType) {

        String sql;
//        if (pageType==1) {
//            sql = "select object(o) from SoMessage o where o.receive_by_id = ?1 and o.message_status is null order by o.assign_date";
//        } else {
//            sql = "select object(o) from SoMessage o where o.receive_by_id = ?1 and o.case_status=?2 order by o.assign_date";
//        }
        //sql = "select object(o) from SoMessage o where o.receive_by_id = ?1 and o.case_status=?2 order by o.assign_date desc";
        sql = "select object(o) from SoMessage o where o.lastReceiveByUsers.id = ?1";
        if(pageType.equals("AS") || pageType.equals("PS")) {
            sql += " and (o.case_status = 'AS' or o.case_status = 'PS')";
        } else {
            sql += " and o.case_status = '" + pageType + "'";
        }
        if(pageType.equals("AS") || pageType.equals("PS")) {
            sql += " order by o.assign_date desc, o.activity_time desc";
        } else {
            sql += " order by o.update_date desc, o.assign_date desc";
        }

        Query q = em.createQuery(sql);

        q.setParameter(1, userId);

        return q.getResultList();
    }

    public List<SoMessage> findSoMessageRelatedById(Integer id) {

        String sql = "select object(o) from SoMessage o where o.related_id in (select oo.related_id from SoMessage oo where oo.id = ?1) order by o.activity_time";
        Query q = em.createQuery(sql);

        q.setParameter(1, id);
        return q.getResultList();
    }

    public List<SoMessage> findSoMessageByUserId(String user_id, String src) {

        String sql = "select object(o) from SoMessage o where o.user_id=?1 and o.source_type_enum_id = ?2 order by o.activity_time desc";
        Query q = em.createQuery(sql);

        q.setParameter(1, user_id);
        q.setParameter(2, src);
        return q.getResultList();
    }

    public int getMessageCount(String user_id, String src) {
        try {
            Query q = em.createQuery("select count(o) from SoMessage as o where o.user_id=?1 and o.source_type_enum_id = ?2");
            q.setParameter(1, user_id);
            q.setParameter(2, src);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

//    public void deleteActivityAttachment(Integer activityId) {
//        //delete ActivityAttachment
//        Query q = em.createQuery("Delete from ActivityAttachment aa where aa.activity.id = ?1");
//        q.setParameter(1, activityId);
//        q.executeUpdate();
//    }


    public int checkSoMessageDup(String source_id, BigInteger related_id, Integer activity_time) {
        Query q;
        q = em.createQuery("select count(o) from SoMessage as o where o.source_id =:source_id and o.related_id = :related_id and o.activity_time = :activity_time");
        q.setParameter("source_id", source_id);
        q.setParameter("related_id", related_id);
        q.setParameter("activity_time", activity_time);

        return ((Long) q.getSingleResult()).intValue();
    }

    public int checkSoMessageDup(String activity_id) {
        Query q;
        q = em.createQuery("select count(o) from SoMessage as o where o.activity_id = :activity_id");
        q.setParameter("activity_id", activity_id);

        return ((Long) q.getSingleResult()).intValue();
    }
    
    public List<SoMessage> findByAccountId(Integer userId, Integer accountId, String pageType) {
        List<SoMessage> list = null;
        String sql = "select a.* from so_message a"
                + " inner join users u on u.id = a.last_receive_by_id"
                + " where ((a.so_account_id = :accountId and a.source_subtype_enum_id = 'EM_IN'";
        if(pageType.equals("OP") || pageType.equals("PS")) {
            sql += " and (a.case_status = 'DF' or a.case_status = 'PS')";
        } else if(pageType.equals("CL") || pageType.equals("IG")) {
            sql += " and (a.case_status = 'CL' or a.case_status = 'IG')";
        } else {
            sql += " and a.case_status = '" + pageType + "'";
        }
        sql += " )";
        if(pageType.equals("OP")) {
            sql += " or (a.source_subtype_enum_id = 'EM_OUT' and a.case_status = 'DF' and a.parent_id is null)";
        }else if(pageType.equals("WF")) {
            sql += " or (a.source_subtype_enum_id = 'EM_OUT' and a.case_status = 'WF' and a.parent_id is null)";
        }else if(pageType.equals("CL")) {
            sql += " or (a.source_subtype_enum_id = 'EM_OUT' and a.case_status = 'IG' and a.parent_id is null)";
        }
        sql += " )";
        
        if (JSFUtil.getUserSession().getUserGroup().getRole().contains("Supervisor")
                && pageType.equals("WF")) { //JSFUtil.getUserSession().getUserGroup().getRole().contains("Agent")
                sql += " and (u.user_group_id IN (SELECT g.id FROM user_group g WHERE "
                     + " (g.role like '%Agent%' AND g.parent_id = " + JSFUtil.getUserSession().getUserGroup().getId() + ")) or a.last_receive_by_id = :userId)";
        }else if (JSFUtil.getUserSession().getUserGroup().getRole().contains("Agent")) {
            sql += " and a.last_receive_by_id = :userId ";
        }
        
        if(pageType.equals("AS")) {
            sql += " order by a.assign_date desc, a.activity_time desc";
        } else {
            sql += " order by a.update_date desc, a.assign_date desc";
        }
        
        
        Query q = em.createNativeQuery(sql, SoMessage.class);
        q.setParameter("accountId", accountId);
        if (JSFUtil.getUserSession().getUserGroup().getRole().contains("Supervisor") && pageType.equals("WF")) {
            q.setParameter("userId", userId);
        }else if (JSFUtil.getUserSession().getUserGroup().getRole().contains("Agent")) {
            q.setParameter("userId", userId);
        }
        
        list = q.getResultList();
        
        return list;
    }
    
    public List<SoMessage> findComposeByAccountId(Integer userId, Integer accountId, String pageType) {
    
        List<SoMessage> list = null;
        String sql = "select a.* from so_message a"
                + " inner join users u on u.id = a.last_receive_by_id"
                + " where a.source_subtype_enum_id = 'EM_OUT' and a.case_status = 'CL'"
                + " and a.last_receive_by_id = :userId "
                + " order by a.update_date desc, a.assign_date desc";
        
        
        Query q = em.createNativeQuery(sql, SoMessage.class);
        q.setParameter("userId", userId);
        
        list = q.getResultList();
        
        return list;
    }
    
    public SoMessage findDraft(Integer parentId){
        List<SoMessage> list = null;
        String sql = "select object(a) from SoMessage a"
                + " where a.parentSoMessage.id = :parentId"
                + " and a.case_status = 'DF'";
        Query q = em.createQuery(sql);
        q.setParameter("parentId", parentId);
        
        list = q.getResultList();
        SoMessage sm = null;
        if(!list.isEmpty()){
            sm = list.get(0);
        }
        
        return sm;
    
    }
    
    public List<SoEmailSignature> findAllSoEmailSignature(){
        List<SoEmailSignature> list = null;
        String sql = "select object(a) from SoEmailSignature a order by a.seqId";
        Query q = em.createQuery(sql);
        
        list = q.getResultList();
        
        return list;
    
    }
    
    public List<SoEmailSignature> findSoEmailSignatureByAccountId(Integer accountId){
        List<SoEmailSignature> list = null;
        String sql = "select object(a) from SoEmailSignature a where a.soEmailAccount.soAccount.id = :accountId and a.status = true and a.enable = true order by a.seqId";
        Query q = em.createQuery(sql);
        q.setParameter("accountId", accountId);
        
        list = q.getResultList();
        
        return list;
    
    }
    
    public SoEmailSignature findSoEmailSignatureById(Integer id){
        return em.find(SoEmailSignature.class, id);
    }
    
    public List<SoServiceOutgoingAccount> findAllSoServiceOutgoingAccount(){
        List<SoServiceOutgoingAccount> list = null;
        String sql = "select object(a) from SoServiceOutgoingAccount a order by a.id";
        Query q = em.createQuery(sql);
        
        list = q.getResultList();
        
        return list;
    
    }
    
    public SoServiceOutgoingAccount findSoServiceOutgoingAccountById(Integer id){
        return em.find(SoServiceOutgoingAccount.class, id);
    }
    
    public List<SoMessage> findByEmail(String user_id){
        List<SoMessage> list = null;
        String sql = "select a.* from so_message a"
                + " inner join so_email_message e on a.id = e.so_message_id"
                + " where ((e.direction = 'INB' and e.send_status = 'NO') or (e.direction = 'OUT' and e.send_status = 'CP'))"
                + " and a.user_id = '" + user_id + "'"
                + " order by a.id desc";
        Query q = em.createNativeQuery(sql, SoMessage.class);
        q.setMaxResults(50);
        
        list = q.getResultList();
        
        return list;
    
    }
    
    public List<SoUserSignature> findSoUserSignatureByUserId(Integer userId){
        List<SoUserSignature> list = null;
        String sql = "select object(a) from SoUserSignature a where a.users.id = :userId and a.status = true and a.enable = true order by a.seqId";
        Query q = em.createQuery(sql);
        q.setParameter("userId", userId);
        
        list = q.getResultList();
        
        return list;
    }
    
    public SoUserSignature findSoUserSignatureById(Integer id){
        return em.find(SoUserSignature.class, id);
    }
    
    public List<SoIgnoreReason> findAllSoIgnoreReason(){
        List<SoIgnoreReason> list = null;
        String sql = "select object(a) from SoIgnoreReason a where a.status = true and a.enable = true order by a.name";
        Query q = em.createQuery(sql);
        
        list = q.getResultList();
        
        return list;
    }
    
    public void saveSoMsgCaseTypeMap(SoMsgCasetypeMap soMsgCasetypeMap) throws PreexistingEntityException, Exception {
        em.persist(soMsgCasetypeMap);
    }
    
    public SoMsgUserAssigntime findSoMsgUserAssigntimeBySoMessageId(Integer soMessageId){
        List<SoMsgUserAssigntime> list = null;
        String sql = "select object(a) from SoMsgUserAssigntime a where a.soMessageId = :soMessageId and a.turnaroundEnd is null";
        Query q = em.createQuery(sql);
        q.setParameter("soMessageId", soMessageId);
        
        list = q.getResultList();
        
        if(list.isEmpty())
            return null;
        else
            return (SoMsgUserAssigntime) list.get(0);
    }
    
    public void saveSoMsgUserAssigntime(SoMsgUserAssigntime soMsgUserAssigntime) throws NonexistentEntityException, Exception {
        em.merge(soMsgUserAssigntime);
    }

    public void assignToUser(Integer id, Users users) {
//        String s = String.format("update SoMessage set receive_by_id = %s, receive_by_name = %s where id = %s", users.getId(), users.getLoginName(), id);
//        String s = "update SoMessage set receive_by_id = "+users.getId()+", receive_by_name = "+users.getLoginName()+" where id = "+ id.toString();
//        System.out.println(s);

        Query q = em.createQuery("update SoMessage set lastReceiveByUsers = ?1, lastReceiveByName = ?2, lastAssignDate = getdate(), lastReceiveDate = getdate(), update_date = getdate(), update_by = ?3 where id = ?4");
        q.setParameter(1, users);
        q.setParameter(2, users.getLoginName());
        q.setParameter(3, JSFUtil.getUserSession().getUsers().getId());
        q.setParameter(4, id);
        q.executeUpdate();
    }

    public void assignToUserFirst(Integer id, Users users) {

        Query q = em.createQuery("update SoMessage set case_status='AS', receive_by_id=?5, receive_by_name=?6, assign_date = getdate(), receive_date = getdate()" +
                ", lastReceiveByUsers = ?1, lastReceiveByName = ?2, lastAssignDate = getdate(), lastReceiveDate = getdate(), update_date = getdate(), update_by = ?3 where id = ?4");
        q.setParameter(1, users);
        q.setParameter(2, users.getLoginName());
        q.setParameter(3, JSFUtil.getUserSession().getUsers().getId());
        q.setParameter(4, id);
        q.setParameter(5, users.getId());
        q.setParameter(6, users.getLoginName());
        q.executeUpdate();
    }
    
    public List<String[]> findComposePerformance(List<Users> userList,String dateFrom, String dateTo){
        Query q = null;
        List<String[]> list = new ArrayList<String[]>();
        String[] stra = null;
        String sql = "";
        String whereDateFrom = " and create_date >= '" + dateFrom + "'";
        String whereDateTo = " and create_date <= '" + dateTo + "'";
        String whereDate = "";
        if(!dateFrom.equals("")){
            whereDate += whereDateFrom;
        }
        if(!dateTo.equals("")){
            whereDate += whereDateTo;
        }
        
        Integer c = 0;
        for(Users u : userList){
            String where = " source_subtype_enum_id = 'EM_OUT' and (parent_id is null or parent_id = '') and create_by = " + u.getId().intValue() + whereDate;
            stra = new String[6];
            stra[0] = u.getName() + (u.getSurname() != null ? u.getSurname() : "");
            
            //count all
            sql = "select count(id) from so_message where" + where;
            q = em.createNativeQuery(sql);
            c = (Integer) q.getSingleResult();
            stra[1] = (c != null ? c.toString() : "0");
            
            if(c != null && c != 0){
                //count open
                sql = "select count(id) from so_message where" + where
                        + " and case_status = 'DF'";
                q = em.createNativeQuery(sql);
                c = (Integer) q.getSingleResult();
                stra[2] = (c != null ? c.toString() : "0");

                //count waiting for approve
                sql = "select count(id) from so_message where" + where
                        + " and case_status = 'WF'";
                q = em.createNativeQuery(sql);
                c = (Integer) q.getSingleResult();
                stra[3] = (c != null ? c.toString() : "0");

                //count ignored
                sql = "select count(id) from so_message where" + where
                        + " and case_status = 'IG'";
                q = em.createNativeQuery(sql);
                c = (Integer) q.getSingleResult();
                stra[4] = (c != null ? c.toString() : "0");

                //count sent
                sql = "select count(id) from so_message where" + where
                        + " and case_status = 'CL'";
                q = em.createNativeQuery(sql);
                c = (Integer) q.getSingleResult();
                stra[5] = (c != null ? c.toString() : "0");

                list.add(stra);
            }
        }
        
        return list;
    }
    
    public synchronized String getTicketId() {
        String ticketId = "";
        try{
            Query q = em.createNativeQuery("{call GetNextSeqEmail_v2(:tableName)}");
            q.setParameter("tableName", "EMAIL_TICKET_ID");
            Object o = q.getSingleResult();
            ticketId = o.toString();
        } catch (Exception e){
            log.error(e);
        }
        return ticketId;
    }
}
