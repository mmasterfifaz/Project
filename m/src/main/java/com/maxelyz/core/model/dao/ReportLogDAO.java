/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.ReportLog;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ReportLogDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(ReportLog reportLog) throws PreexistingEntityException, Exception {
        em.persist(reportLog);
    }

    public void edit(ReportLog reportLog) throws NonexistentEntityException, Exception {
        reportLog = em.merge(reportLog);
    }

    public void destroy(Integer id) throws NonexistentEntityException {

        ReportLog reportLog;
        try {
            reportLog = em.getReference(ReportLog.class, id);
            reportLog.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The reportLog with id " + id + " no longer exists.", enfe);
        }
        em.remove(reportLog);
    }

    public List<ReportLog> findReportLogEntities() {
        return findReportLogEntities(true, -1, -1);
    }

    public List<ReportLog> findReportLogEntities(int maxResults, int firstResult) {
        return findReportLogEntities(false, maxResults, firstResult);
    }

    private List<ReportLog> findReportLogEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from ReportLog as o order by o.createDate desc");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public ReportLog findReportLog(Integer id) {
        ReportLog p = em.find(ReportLog.class, id);
        return p;
    }

    public int getReportLogCount() {
        return ((Long) em.createQuery("select count(o) from ReportLog as o").getSingleResult()).intValue();
    }

}
