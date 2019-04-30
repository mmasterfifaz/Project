/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.RptContactChannel;
import com.maxelyz.core.model.entity.RptContactChannelPK;
import com.maxelyz.core.model.value.admin.RptContactCaseTypeValue;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import com.maxelyz.core.model.value.admin.RptContactChannelValue;
import java.util.Date;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class RptContactChannelDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(RptContactChannel rptContactChannel) {
        em.persist(rptContactChannel);
    }

    public void edit(RptContactChannel rptContactChannel) {
        rptContactChannel = em.merge(rptContactChannel);
    }

    public void destroy(RptContactChannelPK id) throws NonexistentEntityException {
        RptContactChannel rptContactChannel;
        try {
            rptContactChannel = em.getReference(RptContactChannel.class, id);
            rptContactChannel.getRptContactChannelPK();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The rptContactChannel with id " + id + " no longer exists.", enfe);
        }

        em.remove(rptContactChannel);

    }

    public List<RptContactChannel> findRptContactChannelEntities() {
        return findRptContactChannelEntities(true, -1, -1);
    }

    public List<RptContactChannel> findRptContactChannelEntities(int maxResults, int firstResult) {
        return findRptContactChannelEntities(false, maxResults, firstResult);
    }

    private List<RptContactChannel> findRptContactChannelEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from RptContactChannel as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public RptContactChannel findRptContactChannel(RptContactChannelPK id) {
        return em.find(RptContactChannel.class, id);
    }

    public int getRptContactChannelCount() {
        Query q = em.createQuery("select count(o) from RptContactChannel as o");
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<RptContactChannelValue> findContactCaseByChannel(Date contactDate) {
        Query q = em.createQuery("select new "+RptContactChannelValue.class.getName() +"( rpt.rptContactChannelPK.contactDate, ch.name, sum(rpt.total), sum(rpt.pending), sum(rpt.closed)) "
                + "from RptContactChannel as rpt join rpt.channel ch "
                + "where rpt.rptContactChannelPK.contactDate = ?1 group by rpt.rptContactChannelPK.contactDate, ch.name ");
        q.setParameter(1, contactDate);
        return q.getResultList();
    }

    public List<RptContactCaseTypeValue> findContactCaseByCaseType(Date contactDate) {
        Query q = em.createQuery("select new "+RptContactCaseTypeValue.class.getName() +"( rpt.rptContactChannelPK.contactDate, ch.name, sum(rpt.total), sum(rpt.pending), sum(rpt.closed), sum(rpt.firstcallResolution) ) "
                + "from RptContactChannel as rpt join rpt.caseDetail caseDetail join caseDetail.caseTopicId caseTopic join caseTopic.caseTypeId ch "
                + "where rpt.rptContactChannelPK.contactDate = ?1 group by rpt.rptContactChannelPK.contactDate, ch.name ");
       
        q.setParameter(1, contactDate );
        return q.getResultList();
    }
}
