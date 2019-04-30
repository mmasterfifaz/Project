package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.MediaPlan;
import com.maxelyz.core.model.entity.MediaPlanImportLog;
import com.maxelyz.core.model.entity.MediaPlanTemp;
import com.maxelyz.core.model.entity.ModuleAuditLog;
import com.maxelyz.utils.JSFUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor = Exception.class)
public class MediaPlanDAO {

    private static Logger log = Logger.getLogger(MarketingDAO.class);
    @PersistenceContext
    private EntityManager em;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    public void create(MediaPlan mediaPlan) throws PreexistingEntityException, Exception {
        em.persist(mediaPlan);
    }

    public void edit(MediaPlan mediaPlan) throws IllegalOrphanException, NonexistentEntityException, Exception {
        mediaPlan = em.merge(mediaPlan);
    }

    public void create(MediaPlanTemp mediaPlanTemp) throws PreexistingEntityException, Exception {
        em.persist(mediaPlanTemp);
    }

    public void create(ModuleAuditLog moduleAuditLog) throws PreexistingEntityException, Exception {
        em.persist(moduleAuditLog);
    }

    public void create(MediaPlanImportLog mediaPlanImportLog) throws PreexistingEntityException, Exception {
        em.persist(mediaPlanImportLog);
    }

    public MediaPlan findMediaPlan(Integer id) {
        return em.find(MediaPlan.class, id);
    }

    public List<MediaPlan> findMediaPlanEntities() {
        return findMediaPlanEntities(true, -1, -1);
    }

    public List<MediaPlan> findMediaPlanEntities(int maxResults, int firstResult) {
        return findMediaPlanEntities(false, maxResults, firstResult);
    }

    private List<MediaPlan> findMediaPlanEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from MediaPlan as o where o.enable = true");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public List<MediaPlanImportLog> findMediaPlanImortLogEntities() {
        return findMediaPlanImortLogEntities(true, -1, -1);
    }

    public List<MediaPlanImportLog> findMediaPlanImortLogEntities(int maxResults, int firstResult) {
        return findMediaPlanImortLogEntities(false, maxResults, firstResult);
    }

    private List<MediaPlanImportLog> findMediaPlanImortLogEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from MediaPlanImportLog as o ");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public List<MediaPlan> findMediaPlanFromSearch(String spotRefID, String channel, String programName, Date fromDate, Date toDate) {
//        Query q = em.createQuery("select object(o) from Marketing as o where totalAssigned is not null and startDate <= ?1 and endDate >= ?1 and enable = true and status = true order by code");
        String where = " where o.enable = true ";
        if (spotRefID != null && !spotRefID.isEmpty()) {
            where += " and o.spotRefId like '%" + spotRefID + "%' ";
        }
        if (channel != null && !channel.isEmpty()) {
            where += " and o.channel like '%" + channel + "%' ";
        }
        if (programName != null && !programName.isEmpty()) {
            where += " and o.programName like '%" + programName + "%' ";
        }
        if (fromDate != null) {
            String df = sdf.format(fromDate);
            where += " and o.dateOfSpot >= '" + df + "' ";
        }
        if (toDate != null) {
            String dt = sdf.format(toDate);
            where += " and o.dateOfSpot <= '" + dt + "' ";
        }
//        System.out.println("where statement : " + where);
        Query q = em.createQuery("select object(o) from MediaPlan as o " + where);
        return q.getResultList();
    }

    public MediaPlan findExistingMediaPlanByRefID(String refID) {
        Query q = em.createQuery("select object(o) from MediaPlan as o where o.spotRefId = ?1");
        q.setParameter(1, refID);
        try {
            q.setMaxResults(1);
            return (MediaPlan) q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public List<ModuleAuditLog> findModuleAuditLogByRefID(String moduleName ,Integer refID) {
        Query q = em.createQuery("select object(o) from ModuleAuditLog as o where o.refNo = ?1 and o.moduleName = ?2");
        q.setParameter(1, refID);
        q.setParameter(2, moduleName);
        return q.getResultList();
    }

    public void deleteMediaPlanTemp(List<MediaPlanTemp> tepmList) throws IllegalOrphanException, NonexistentEntityException {
        MediaPlanTemp t;
        for (MediaPlanTemp obj : tepmList) {
            t = new MediaPlanTemp();
            try {
                t = em.getReference(MediaPlanTemp.class, obj.getId());
                t.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The media plan temp with id " + obj.getId() + " no longer exists.", enfe);
            }
            em.remove(t);
        }
    }

    public List<String[]> getLoadedList(String strQuery) {
        List<String[]> list = null;
        String where = "";
        String sql;
        if (strQuery != null && strQuery.length() > 1) {
            where = " where id in (" + strQuery.subSequence(0, strQuery.length() - 1) + ") ";
            sql = "select spot_ref_id, spot_type, day_of_spot, date_of_spot, spot_telephone_number, channel,program_name, program_type, show_time_start,show_time_end,actual_on_air_time, section_break, copy_length, tape, net_cost_per_spot,status,media_agency_remark, mtl_remark1,mtl_remark2, mtl_remark3, mtl_remark4, Product_Assign, ProductCode "
                    + " from media_plan_temp "
                    + where;
            Query q = em.createNativeQuery(sql);
            try {
                list = q.getResultList();
            } catch (Exception e) {
                log.error(e);
            }
        } else {
            list = new ArrayList<String[]>();
        }
        return list;
    }
}
