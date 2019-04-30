/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.social.model.entity.SoRptPost;
import com.maxelyz.social.model.entity.SoRptPostAccountByDateDTO;
import com.maxelyz.social.model.entity.SoRptPostDTO;
import com.maxelyz.utils.JSFUtil;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

import org.apache.log4j.Logger;

@Transactional
public class SoRptPostDAO {

    private static Logger log = Logger.getLogger(SoRptPost.class);
    @PersistenceContext
    private EntityManager em;

    public void create(SoRptPost soRptPost) throws PreexistingEntityException {
//        this.validate(soRptPost);
        em.persist(soRptPost);
    }

    public void edit(SoRptPost soRptPost) throws NonexistentEntityException, Exception {
        soRptPost = em.merge(soRptPost);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        SoRptPost soRptPost;
        try {
            soRptPost = em.getReference(SoRptPost.class, id);
            soRptPost.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The soRptPost with id " + id + " no longer exists.", enfe);
        }
        em.remove(soRptPost);
    }

    public List<SoRptPost> findSoRptPostEntities() {
        return findSoRptPostEntities(true, -1, -1);
    }

    public List<SoRptPost> findSoRptPostEntities(int maxResults, int firstResult) {
        return findSoRptPostEntities(false, maxResults, firstResult);
    }

    private List<SoRptPost> findSoRptPostEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from SoRptPost as o order by o.transDate");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

//    private List<SoRptPost> findSoRptPostEntities(boolean all, int maxResults, int firstResult) {
//        Query q = em.createQuery("select object(o) from SoRptPost as o where o.enable = true order by o.name");
//        if (!all) {
//            q.setMaxResults(maxResults);
//            q.setFirstResult(firstResult);
//        }
//        return q.getResultList();
//    }
//
//    public List<SoRptPost> findSoRptPostStatus() {
//        Query q = em.createQuery("select object(o) from SoRptPost as o where o.enable = true and o.status = true order by o.seqNo");
//        return q.getResultList();
//    }

    public SoRptPost findSoRptPost(Integer id) {
        return em.find(SoRptPost.class, id);
    }

    public int getSoRptPostCount() {
        Query q = em.createQuery("select count(o) from SoRptPost as o");
        return ((Long) q.getSingleResult()).intValue();
    }

//    public List<SoRptPost> findSoRptPostByName(String name) {
//        Query q = em.createQuery("select object(o) from SoRptPost as o where name = ?1");
//        q.setParameter(1, name);
//        return q.getResultList();
//    }

//    private void validate(SoRptPost soRptPost) throws PreexistingEntityException {
//        if (findSoRptPostByName(soRptPost.getName()).size() > 0) {
//            throw new PreexistingEntityException("Duplicate Code");
//        }
//    }
//
//    public Map<String, Integer> getSoRptPostList() {
//        List<SoRptPost> soRptPosts = this.findSoRptPostStatus();
//        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
//        for (SoRptPost obj : soRptPosts) {
//            values.put(obj.getName(), obj.getId());
//        }
//        return values;
//    }
//
//    public int checkSoRptPostName(String name, Integer id) {
//        Query q;
//        if(id == 0){
//            q = em.createQuery("select count(o) from SoRptPost as o where o.name =:name and o.enable = true");
//        } else {
//            q = em.createQuery("select count(o) from SoRptPost as o where o.name =:name and o.enable = true and o.id <> :id");
//            q.setParameter("id", id);
//        }
//        q.setParameter("name", name);
//        return ((Long) q.getSingleResult()).intValue();
//    }

    public Integer countByDate(Date beginDate, Date endDate) {
        try {
            String sql = "select case count(o) when 0 then 0 else sum(totalPost) end from SoRptPost as o"
                    + " where o.transDate between ?1 and ?2";
            Query q = em.createQuery(sql);

            q.setParameter(1, JSFUtil.toDateWithoutTime(beginDate));
            q.setParameter(2, JSFUtil.toDateWithMaxTime(endDate));

            return (Integer) q.getSingleResult();
        } catch (Exception e) {
            log.error(e);
            return 0;
        }
    }

    public List<SoRptPostDTO> findTotalByDate(Date beginDate, Date endDate, String soAccountStringX) {
//        String sql = "select new "+SoRptPostDTO.class.getName()+"( o.transDate)" +
//                " from SoRptPost o" +
//                " where o.transDate between ?1 and ?2" +
//                " group by o.transDate";

        String sql = "select new "+SoRptPostDTO.class.getName()+"( o.transDate, SUM(totalPost), SUM(reply), SUM(ignore)" +
                ", SUM(totalPost-reply-ignore)" +
                ", SUM(pendingPost), SUM(closedPost)" +
                ", SUM(totalPost-pendingPost-closedPost)" +
                ", SUM(negative), SUM(neutral), SUM(positive)" +
                ", SUM(totalPost-negative-neutral-positive)" +
                ") from SoRptPost o" +
                " where o.transDate between ?1 and ?2 and soAccountId in (" +soAccountStringX+ ")" +
                " group by o.transDate";
        Query q = em.createQuery(sql);

        q.setParameter(1, JSFUtil.toDateWithoutTime(beginDate));
        q.setParameter(2, JSFUtil.toDateWithMaxTime(endDate));

        return q.getResultList();
    }

    public List<SoRptPostAccountByDateDTO> findAccountByDate(Date beginDate, Date endDate, String soAccountStringX) {

        String sql = "select new "+SoRptPostAccountByDateDTO.class.getName()+"( o.transDate" +
                ", sum(case when soAccountId=1 then totalPost else 0 end)" +
                ", sum(case when soAccountId=3 then totalPost else 0 end)" +
                ", sum(case when soAccountId=4 then totalPost else 0 end)" +
                ", sum(case when soAccountId=2 then totalPost else 0 end)" +
                ", sum(case when soAccountId=5 then totalPost else 0 end)" +
                ") from SoRptPost o" +
                " where o.transDate between ?1 and ?2 and soAccountId in (" +soAccountStringX+ ")" +
                " group by o.transDate";
        Query q = em.createQuery(sql);

        q.setParameter(1, JSFUtil.toDateWithoutTime(beginDate));
        q.setParameter(2, JSFUtil.toDateWithMaxTime(endDate));

        return q.getResultList();
    }

    public List<SoRptPostDTO> findTotalByAccount(Date beginDate, Date endDate, String soAccountStringX) {
//        String sql = "select new "+SoRptPostDTO.class.getName()+"( o.transDate)" +
//                " from SoRptPost o" +
//                " where o.transDate between ?1 and ?2" +
//                " group by o.transDate";
        String sql = "select new "+SoRptPostDTO.class.getName()+"( o.soAccountName, SUM(totalPost), SUM(reply), SUM(ignore)" +
                ", SUM(totalPost-reply-ignore)" +
                ", SUM(negative), SUM(neutral), SUM(positive)" +
                ", SUM(totalPost-negative-neutral-positive)" +
                ", SUM(pendingPost), SUM(closedPost)" +
                ", SUM(totalPost-pendingPost-closedPost)" +
                ") from SoRptPost o" +
                " where o.transDate between ?1 and ?2 and soAccountId in (" +soAccountStringX+ ")" +
                " group by o.soAccountName" +
                " order by o.soAccountName";
        Query q = em.createQuery(sql);

        q.setParameter(1, JSFUtil.toDateWithoutTime(beginDate));
        q.setParameter(2, JSFUtil.toDateWithMaxTime(endDate));

        return q.getResultList();
    }

//    public List<RptContactCaseTypeValue> findContactCaseByCaseType(Date contactDate) {
//        Query q = em.createQuery("select new "+RptContactCaseTypeValue.class.getName() +"( rpt.rptContactChannelPK.contactDate, ch.name, sum(rpt.total), sum(rpt.pending), sum(rpt.closed), sum(rpt.firstcallResolution) ) "
//                + "from RptContactChannel as rpt join rpt.caseDetail caseDetail join caseDetail.caseTopicId caseTopic join caseTopic.caseTypeId ch "
//                + "where rpt.rptContactChannelPK.contactDate = ?1 group by rpt.rptContactChannelPK.contactDate, ch.name ");
//
//        q.setParameter(1, contactDate );
//        return q.getResultList();
//    }
}
