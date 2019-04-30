
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.AutoAssignmentList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class AutoAssignmentListDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(AutoAssignmentList autoAssignmentList) {
        em.persist(autoAssignmentList);
    }

    public void edit(AutoAssignmentList autoAssignmentList) {
        autoAssignmentList = em.merge(autoAssignmentList);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        AutoAssignmentList autoAssignmentList;
        try {
            autoAssignmentList = em.getReference(AutoAssignmentList.class, id);
            autoAssignmentList.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The autoAssignmentList with id " + id + " no longer exists.", enfe);
        }
        em.remove(autoAssignmentList);

    }

    public AutoAssignmentList findAutoAssignmentListEntities() {
        Query q = em.createQuery("select object(o) from AutoAssignmentList as o");
        try { 
            return (AutoAssignmentList)q.getSingleResult();
        } catch (NoResultException ex) { 
            return null;
        }
    }
    
    public AutoAssignmentList findAutoAssignmentList(Integer id) {
        AutoAssignmentList autoAssignmentList = em.find(AutoAssignmentList.class, id);
        return autoAssignmentList;
    }
    
    public List<AutoAssignmentList> findAutoAssignmentList(Date dateFrom, Date dateTo, String status) {
        SimpleDateFormat sdfFrom = new SimpleDateFormat("yyyy-MM-dd 00:00:00", Locale.US);
        SimpleDateFormat sdfTo = new SimpleDateFormat("yyyy-MM-dd 23:59:59", Locale.US);
        String sql = "select object(o) from AutoAssignmentList as o where 1 = 1 ";

        if (dateFrom != null){
            sql += "and o.createDate >= '"+sdfFrom.format(dateFrom)+"' ";
        }
        
        if (dateTo != null){
            sql += "and o.createDate  <= '"+sdfTo.format(dateTo)+"' ";
        }
        
        if(!status.isEmpty()){
            if(status.equals("pending")) {
                sql += "and o.assignStatus is null ";
            } else {
                sql += "and o.assignStatus = '"+status+"'  ";
            }
        }
        
        sql += "order by o.createDate ";
        Query q = em.createQuery(sql);
        
        return q.getResultList();
    }
    
}
