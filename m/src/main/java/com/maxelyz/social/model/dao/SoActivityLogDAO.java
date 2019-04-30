/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.social.model.entity.SoActivityLog;
import com.maxelyz.utils.MxString;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Transactional
public class SoActivityLogDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(SoActivityLog soActivityLog) throws PreexistingEntityException {
        em.persist(soActivityLog);
    }

    public void edit(SoActivityLog soActivityLog) throws NonexistentEntityException, Exception {
        em.merge(soActivityLog);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        SoActivityLog soActivityLog;
        try {
            soActivityLog = em.getReference(SoActivityLog.class, id);
            soActivityLog.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The soActivityLog with id " + id + " no longer exists.", enfe);
        }
        em.remove(soActivityLog);
    }

    public List<SoActivityLog> findSoActivityLogEntities() {
        return findSoActivityLogEntities(true, -1, -1);
    }

    public List<SoActivityLog> findSoActivityLogEntities(int maxResults, int firstResult) {
        return findSoActivityLogEntities(false, maxResults, firstResult);
    }

    private List<SoActivityLog> findSoActivityLogEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from SoActivityLog as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public SoActivityLog findSoActivityLog(Integer id) {
        return em.find(SoActivityLog.class, id);
    }

    public void insertActivityLog(Integer soMessageId, Integer userId, Integer logTypeId, String reason, String remark){
        String sql = "insert into so_activity_log (log_date, log_type_id, log_type, create_by, so_message_id, reason, remark)\n" +
                "values (%s,%s,(select name from so_activity_type where id=%s),%s,%s,%s,%s)";
        sql = String.format(sql, "getdate()"
                ,logTypeId
                ,logTypeId
                ,userId
                ,soMessageId
                ,MxString.quotedStrNull(reason)
                ,MxString.quotedStrNull(remark));

        Query q = em.createNativeQuery(sql);
//            q.setParameter("soServiceId", soServiceId);
//            q.setParameter("userId", userId);
        q.executeUpdate();
    }

    public SoActivityLog findSoActivityTransferBySoMessage(Integer soMessageId) {
        String sql = "select object(o) from SoActivityLog as o where o.soActivityType.id in (11,12) and o.soMessage.id = :soMessageId order by o.id desc";
        Query q = em.createQuery(sql);
        q.setParameter("soMessageId", soMessageId);
        return (SoActivityLog) q.getResultList().get(0);
    }

}
