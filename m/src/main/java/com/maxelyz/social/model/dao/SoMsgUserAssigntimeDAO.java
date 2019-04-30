/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.social.model.entity.SoMsgUserAssigntime;
import com.maxelyz.utils.MxString;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Transactional
public class SoMsgUserAssigntimeDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(SoMsgUserAssigntime soMsgUserAssigntime) throws PreexistingEntityException {
//        this.validate(soMsgUserAssigntime);
        em.persist(soMsgUserAssigntime);
    }

    public void edit(SoMsgUserAssigntime soMsgUserAssigntime) throws NonexistentEntityException, Exception {
        soMsgUserAssigntime = em.merge(soMsgUserAssigntime);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        SoMsgUserAssigntime soMsgUserAssigntime;
        try {
            soMsgUserAssigntime = em.getReference(SoMsgUserAssigntime.class, id);
            soMsgUserAssigntime.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The soMsgUserAssigntime with id " + id + " no longer exists.", enfe);
        }
        em.remove(soMsgUserAssigntime);
    }

    public List<SoMsgUserAssigntime> findSoMsgUserAssigntimeEntities() {
        return findSoMsgUserAssigntimeEntities(true, -1, -1);
    }

    public List<SoMsgUserAssigntime> findSoMsgUserAssigntimeEntities(int maxResults, int firstResult) {
        return findSoMsgUserAssigntimeEntities(false, maxResults, firstResult);
    }

    private List<SoMsgUserAssigntime> findSoMsgUserAssigntimeEntities(boolean all, int maxResults, int firstResult) {
//        Query q = em.createQuery("select object(o) from SoMsgUserAssigntime as o where o.enable = true order by o.name");
        Query q = em.createQuery("select object(o) from SoMsgUserAssigntime as o order by o.name");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

//    public List<SoMsgUserAssigntime> findSoMsgUserAssigntimeStatus() {
//        Query q = em.createQuery("select object(o) from SoMsgUserAssigntime as o where o.enable = true and o.status = true order by o.seqNo");
//        return q.getResultList();
//    }

    public SoMsgUserAssigntime findSoMsgUserAssigntime(Integer id) {
        return em.find(SoMsgUserAssigntime.class, id);
    }

    public int getSoMsgUserAssigntimeCount() {
        Query q = em.createQuery("select count(o) from SoMsgUserAssigntime as o");
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<SoMsgUserAssigntime> findBysoMessageId(Integer soMessageId) {
        Query q = em.createQuery("select object(o) from SoMsgUserAssigntime as o where soMessageId = ?1");
        q.setParameter(1, soMessageId);
        return q.getResultList();
    }

    public List<SoMsgUserAssigntime> findBysoMessageIdOpen(Integer soMessageId) {
        Query q = em.createQuery("select object(o) from SoMsgUserAssigntime as o where soMessageId = ?1 and turnaroundEnd is null");
        q.setParameter(1, soMessageId);
        return q.getResultList();
    }

//    private void validate(SoMsgUserAssigntime soMsgUserAssigntime) throws PreexistingEntityException {
//        if (findSoMsgUserAssigntime(soMsgUserAssigntime.getId()).size() > 0) {
//            throw new PreexistingEntityException("Duplicate Code");
//        }
//    }

//    public Map<String, Integer> getSoMsgUserAssigntimeList() {
//        List<SoMsgUserAssigntime> soMsgUserAssigntimes = this.findSoMsgUserAssigntimeStatus();
//        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
//        for (SoMsgUserAssigntime obj : soMsgUserAssigntimes) {
//            values.put(obj.getName(), obj.getId());
//        }
//        return values;
//    }

//    public int checkSoMsgUserAssigntimeName(String name, Integer id) {
//        Query q;
//        if(id == 0){
//            q = em.createQuery("select count(o) from SoMsgUserAssigntime as o where o.name =:name and o.enable = true");
//        } else {
//            q = em.createQuery("select count(o) from SoMsgUserAssigntime as o where o.name =:name and o.enable = true and o.id <> :id");
//            q.setParameter("id", id);
//        }
//        q.setParameter("name", name);
//        return ((Long) q.getSingleResult()).intValue();
//    }

    public void closeTransfer(Integer soMessageId, Integer userId) {
        Query q = em.createNativeQuery("update so_msg_user_assigntime set turnaround_end = GETDATE()\n" +
//                ",duration_milis = datediff(ms, turnaround_start, GETDATE())\n" +
                ",duration_milis = datediff(s, turnaround_start, GETDATE())\n" +
                ",update_date = GETDATE(), update_by = ?1\n" +
                "where so_message_id = ?2 and turnaround_end is null");
        q.setParameter(1, MxString.chkIntNull(userId));
        q.setParameter(2, soMessageId);
        q.executeUpdate();
    }

    public void insertTransfer(Integer soMessageId, Integer assigneeId, Integer userId, Integer fromUserId){
        String sql = "insert into so_msg_user_assigntime (so_message_id, prev_assignee_id, assignee_id, type, logdate, create_by, create_date, turnaround_start)\n" +
                "values (%s,%s,%s,%s,%s,%s,%s,%s)";
        sql = String.format(sql, soMessageId
                , fromUserId
                , assigneeId
                , MxString.quotedStrNull("TR")
                , "getdate()"
                , MxString.chkIntNull(userId)
                ,"getdate()"
                ,"getdate()");

            Query q = em.createNativeQuery(sql);
//            q.setParameter("soServiceId", soServiceId);
//            q.setParameter("userId", userId);
            q.executeUpdate();
    }

}
