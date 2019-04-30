/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.social.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.social.model.entity.SclContactStats;
import com.maxelyz.utils.JSFUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import org.apache.log4j.Logger;

@Transactional
public class SclContactStatsDAO {

    private static Logger log = Logger.getLogger(SclContactStats.class);
    @PersistenceContext
    private EntityManager em;

    public void create(SclContactStats sclContactStats) throws PreexistingEntityException, Exception {
        em.persist(sclContactStats);
    }

    public void edit(SclContactStats sclContactStats) throws NonexistentEntityException, Exception {
        sclContactStats = em.merge(sclContactStats);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        SclContactStats sclContactStats;
        try {
            sclContactStats = em.getReference(SclContactStats.class, id);
            sclContactStats.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The sclContactStats with id " + id + " no longer exists.", enfe);
        }
        em.remove(sclContactStats);
    }

    public List<SclContactStats> findSclContactStatsEntities() {
        return findSclContactStatsEntities(true, -1, -1);
    }

    public List<SclContactStats> findSclContactStatsEntities(int maxResults, int firstResult) {
        return findSclContactStatsEntities(false, maxResults, firstResult);
    }

    private List<SclContactStats> findSclContactStatsEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from SclContactStats as o order by o.id");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public SclContactStats findSclContactStats(Integer id) {
        return em.find(SclContactStats.class, id);
    }

    public int getSclContactStatsCount() {
        return ((Long) em.createQuery("select count(o) from SclContactStats as o").getSingleResult()).intValue();
    }

    public List<SclContactStats> findSclContactStatsToday() {
        String sql = "select object(o) from SclContactStats as o"
                + " where o.workDate between ?1 and ?2"
                + " order by o.id";
        Query q = em.createQuery(sql);

        Date today = new Date();
        q.setParameter(1, JSFUtil.toDateWithoutTime(today));
        q.setParameter(2, JSFUtil.toDateWithMaxTime(today));
        return q.getResultList();
    }

    public List<SclContactStats> findSclContactStatsByDate(Date beginDate, Date endDate) {
//1        SimpleDateFormat sdfFrom = new SimpleDateFormat("yyyy-MM-dd 00:00:00", Locale.US);
//1        SimpleDateFormat sdfTo = new SimpleDateFormat("yyyy-MM-dd 23:59:59", Locale.US);
//1        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

        Query q = null;
        try {
            String sql = "select object(o) from SclContactStats as o"
//                    + " where o.workDate between ?1 and ?2"
                    + " order by o.id";
            q = em.createQuery(sql);

//            q.setParameter(1, JSFUtil.toDateWithoutTime(beginDate));
//            q.setParameter(2, JSFUtil.toDateWithMaxTime(endDate));
//1            q.setParameter(1, sdf.parse(sdfFrom.format(beginDate)));
//1            q.setParameter(2, sdf.parse(sdfTo.format(endDate)));
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return q.getResultList();
    }

//    public Long sumTotalPostByDate(Date beginDate, Date endDate) {
//        try {
//            String sql = "select count(o) from SoMessage as o"
//                    + " where o.create_date between ?1 and ?2 and so_service_id is not null";
//            Query q = em.createQuery(sql);
//
//            q.setParameter(1, JSFUtil.toDateWithoutTime(beginDate));
//            q.setParameter(2, JSFUtil.toDateWithMaxTime(endDate));
//
//            return (Long) q.getSingleResult();
//        } catch (Exception e) {
//            log.error(e);
//            return 0L;
//        }
//    }

    public Integer sumTotalPostByDateInt(Date beginDate, Date endDate) {
        try {
//            String sql = "select count(o) from SoMessage as o"
//                    + " where o.create_date between ?1 and ?2 and so_service_id is not null";
//            Query q = em.createQuery(sql);
            String sql = "select count(m.id) from so_message m, so_email_message em, so_account ac, so_email_account eac\n" +
                    " where m.so_account_id = ac.id and ac.id = eac.so_account_id\n" +
                    " and m.id = em.so_message_id\n" +
                    " and eac.auto_response = 0\n" +
                    " and em.direction = 'INB'" +
                    " and m.create_date between ?1 and ?2";
            Query q = em.createNativeQuery(sql);

            q.setParameter(1, JSFUtil.toDateWithoutTime(beginDate));
            q.setParameter(2, JSFUtil.toDateWithMaxTime(endDate));

            return (Integer) q.getSingleResult();
        } catch (Exception e) {
            log.error(e);
            return 0;
        }
    }

    public Long sumTotalAssignByDate(Date beginDate, Date endDate) {
        try {
            String sql = "select count(o) from SoMessage as o"
                    + " where o.create_date between ?1 and ?2 and so_service_id is not null and receive_by_id is not null";
            Query q = em.createQuery(sql);

            q.setParameter(1, JSFUtil.toDateWithoutTime(beginDate));
            q.setParameter(2, JSFUtil.toDateWithMaxTime(endDate));

            return (Long) q.getSingleResult();
        } catch (Exception e) {
            log.error(e);
            return 0L;
        }
    }

    public Integer sumTotalAssignByDate2(Date beginDate, Date endDate) {
        try {
            String sql = "select COUNT(distinct so_message_id) from so_msg_user_assigntime"
                    + " where turnaround_start between ?1 and ?2";
            Query q = em.createNativeQuery(sql);

            q.setParameter(1, JSFUtil.toDateWithoutTime(beginDate));
            q.setParameter(2, JSFUtil.toDateWithMaxTime(endDate));

            return (Integer) q.getSingleResult();
        } catch (Exception e) {
            log.error(e);
            return 0;
        }
    }

   public Long sumTotalRemainQByDate(Date beginDate, Date endDate) {
        try {
            String sql = "select count(o) from SoMessage as o"
                    //+ " where o.create_date between ?1 and ?2 and so_service_id is not null and case_status in ('NW','PL') ";
                    + " where o.create_date between ?1 and ?2 and so_service_id is not null and receive_by_id is null";
            Query q = em.createQuery(sql);

            q.setParameter(1, JSFUtil.toDateWithoutTime(beginDate));
            q.setParameter(2, JSFUtil.toDateWithMaxTime(endDate));

            return (Long) q.getSingleResult();
        } catch (Exception e) {
            log.error(e);
            return 0L;
        }
    }

   public Integer sumTotalRemainQByDateInt(Date beginDate, Date endDate) {
        try {
            String sql = "select count(m.id) from so_message m, so_email_message em, so_account ac, so_email_account eac\n" +
                    " where m.so_account_id = ac.id and ac.id = eac.so_account_id\n" +
                    " and m.id = em.so_message_id\n" +
                    //" and eac.auto_response = 0\n" +
                    " and em.direction = 'INB'" +
                    " and m.case_status = 'NW'" +
                    " and m.receive_by_id is null" +
                    " and m.create_date between ?1 and ?2";
            Query q = em.createNativeQuery(sql);

            q.setParameter(1, JSFUtil.toDateWithoutTime(beginDate));
            q.setParameter(2, JSFUtil.toDateWithMaxTime(endDate));

            return (Integer) q.getSingleResult();
        } catch (Exception e) {
            log.error(e);
            return 0;
        }
    }

   public Integer sumTotalRemainQAllInt() {
        try {
            String sql = "select count(m.id) from so_message m, so_email_message em, so_account ac, so_email_account eac\n" +
                    " where m.so_account_id = ac.id and ac.id = eac.so_account_id\n" +
                    " and m.id = em.so_message_id\n" +
                    //" and eac.auto_response = 0\n" +
                    " and em.direction = 'INB'" +
                    " and m.case_status = 'NW'" +
                    " and m.receive_by_id is null";
            Query q = em.createNativeQuery(sql);

            return (Integer) q.getSingleResult();
        } catch (Exception e) {
            log.error(e);
            return 0;
        }
    }

    public Integer sumTotalWorkingTimeByDate(Date beginDate, Date endDate) {
        try {
            String sql = "select case count(o) when 0 then 0 else sum(totalWorkingTime) end from SclContactStats as o"
                    + " where o.workDate between ?1 and ?2";
            Query q = em.createQuery(sql);

            q.setParameter(1, JSFUtil.toDateWithoutTime(beginDate));
            q.setParameter(2, JSFUtil.toDateWithMaxTime(endDate));

            return (Integer) q.getSingleResult();
        } catch (Exception e) {
            log.error(e);
            return 0;
        }
    }

    public String sumTotalWorkingTimeByDateStr() {
        try {
            String sql = "select dbo.ConvertTimeToDDHHMMSS(SUM(total_working_time)/SUM(received_post+transfer),24) from scl_contact_stats";
            Query q = em.createNativeQuery(sql);

            return (String) q.getSingleResult();
        } catch (Exception e) {
            log.error(e);
            return "0 D :00:00:00";
        }
    }

    public Integer sumTotalResponseTimeByDate(Date beginDate, Date endDate) {
        try {
            String sql = "select case count(o) when 0 then 0 else sum(totalResponseTime) end from SclContactStats as o"
                    + " where o.workDate between ?1 and ?2";
            Query q = em.createQuery(sql);

            return (Integer) q.getSingleResult();
        } catch (Exception e) {
            log.error(e);
            return 0;
        }
    }

    public String sumTotalResponseTimeByDateStr() {
        try {
            String sql = "select dbo.ConvertTimeToDDHHMMSS(SUM(total_response_time)/SUM(received_post+transfer),24) from scl_contact_stats";
            Query q = em.createNativeQuery(sql);

            return (String) q.getSingleResult();
        } catch (Exception e) {
            log.error(e);
            return "0 D :00:00:00";
        }
    }

    public Integer sumReceivedPostByDate(Date beginDate, Date endDate) {
        try {
            String sql = "select case count(o) when 0 then 0 else sum(receivedPost) end from SclContactStats as o"
                    + " where o.workDate between ?1 and ?2";
            Query q = em.createQuery(sql);

            q.setParameter(1, JSFUtil.toDateWithoutTime(beginDate));
            q.setParameter(2, JSFUtil.toDateWithMaxTime(endDate));

            return (Integer) q.getSingleResult();
        } catch (Exception e) {
            log.error(e);
            return 0;
        }
    }

//    public List<Object> findStaticChannel(Date beginDate, Date endDate, Integer channelId) {
//        String SQL_SELECT = "select s.channelId as id"
//                + ", sum(s.replyPost) as cnt";
//
//        String SQL_FROM = " from SclContactStats s";
////                + " left join a.campaign c"
//
//        String SQL_WHERE = " where s.workDate between ?1 and ?2 and s.channelId = ?3";
//
//        String SQL_GROUP = " group by s.channelId";
//
//        String SQL_HAVING = "";
//
//        String SQL_ORDER = "";
//
//        String SQL_QUERY = SQL_SELECT + SQL_FROM + SQL_WHERE + SQL_GROUP + SQL_HAVING + SQL_ORDER;
//        Query query = em.createQuery(SQL_QUERY);
//
//        query.setParameter(1, JSFUtil.toDateWithoutTime(beginDate));
//        query.setParameter(2, JSFUtil.toDateWithMaxTime(endDate));
//        query.setParameter(3, channelId);
//        query.setMaxResults(1);
//
//        return (List<Object>) query.getResultList();
//    }

    public Integer sumReplyPostByChannel(Date beginDate, Date endDate, Integer id) {
        try {
            String SQL_SELECT = "select case count(s) when 0 then 0 else sum(s.replyPost) end";

            String SQL_FROM = " from SclContactStats s";
//                + " left join a.campaign c"

            String SQL_WHERE = " where s.workDate between ?1 and ?2";

            if (id != null) {
                SQL_WHERE += " and s.channelId = ?3";
            }

            String SQL_GROUP = " group by s.channelId";

            String SQL_HAVING = "";

            String SQL_ORDER = "";

            String SQL_QUERY = SQL_SELECT + SQL_FROM + SQL_WHERE + SQL_GROUP + SQL_HAVING + SQL_ORDER;
            SQL_QUERY = SQL_SELECT + SQL_FROM ;
            Query query = em.createQuery(SQL_QUERY);

//            query.setParameter(1, JSFUtil.toDateWithoutTime(beginDate));
//            query.setParameter(2, JSFUtil.toDateWithMaxTime(endDate));
            if (id != null) {
//            query.setParameter(3, id);
            }

//            return (Long) query.getSingleResult();
//        } catch (Exception e) {
//            log.error(e);
//            return Long.parseLong("0");
//        }
            return (Integer) query.getSingleResult();
        } catch (Exception e) {
            log.error(e);
            return 0;
        }
    }

   public Integer sumReplyPostByPostType(Date beginDate, Date endDate, Integer id) {
        try {
            String SQL_SELECT = "select case count(s) when 0 then 0 else sum(s.replyPost) end";

            String SQL_FROM = " from SclContactStats s";
//                + " left join a.campaign c"

            String SQL_WHERE = " where s.workDate between ?1 and ?2";
            if (id != null) {
                SQL_WHERE += " and s.postTypeId = ?3";
            }

            String SQL_GROUP = " group by s.postType";

            String SQL_HAVING = "";

            String SQL_ORDER = "";

            String SQL_QUERY = SQL_SELECT + SQL_FROM + SQL_WHERE + SQL_GROUP + SQL_HAVING + SQL_ORDER;
            SQL_QUERY = SQL_SELECT + SQL_FROM ;
            Query query = em.createQuery(SQL_QUERY);

//            query.setParameter(1, JSFUtil.toDateWithoutTime(beginDate));
//            query.setParameter(2, JSFUtil.toDateWithMaxTime(endDate));
            if (id != null) {
//            query.setParameter(3, id);
            }

            return (Integer) query.getSingleResult();
        } catch (Exception e) {
            log.error(e);
            return 0;
        }
    }

    public Integer sumReplyPostByCustFeeling(Date beginDate, Date endDate, Integer id) {
        try {
            String SQL_SELECT = "select case count(s) when 0 then 0 else sum(s.replyPost) end";

            String SQL_FROM = " from SclContactStats s";
//                + " left join a.campaign c"

            String SQL_WHERE = " where s.workDate between ?1 and ?2";
            if (id != null) {
                SQL_WHERE += " and s.custFeelingId = ?3";
            }

            String SQL_GROUP = " group by s.postType";

            String SQL_HAVING = "";

            String SQL_ORDER = "";

            String SQL_QUERY = SQL_SELECT + SQL_FROM + SQL_WHERE + SQL_GROUP + SQL_HAVING + SQL_ORDER;
            SQL_QUERY = SQL_SELECT + SQL_FROM ;
            Query query = em.createQuery(SQL_QUERY);

//            query.setParameter(1, JSFUtil.toDateWithoutTime(beginDate));
//            query.setParameter(2, JSFUtil.toDateWithMaxTime(endDate));
            if (id != null) {
//            query.setParameter(3, id);
            }

            return (Integer) query.getSingleResult();
        } catch (Exception e) {
            log.error(e);
            return 0;
        }
    }
}
