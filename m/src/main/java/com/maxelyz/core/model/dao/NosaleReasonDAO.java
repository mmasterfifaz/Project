/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.NosaleReason;
import com.maxelyz.core.model.entity.Province;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class NosaleReasonDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(NosaleReason nosaleReason) throws PreexistingEntityException, Exception {
        em.persist(nosaleReason);
    }

    public void edit(NosaleReason nosaleReason) throws NonexistentEntityException, Exception {
        nosaleReason = em.merge(nosaleReason);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        NosaleReason nosaleReason;
        try {
            nosaleReason = em.getReference(NosaleReason.class, id);
            nosaleReason.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The nosaleReason with id " + id + " no longer exists.", enfe);
        }
        em.remove(nosaleReason);
    }

    public List<NosaleReason> findNosaleReasonEntities() {
        return findNosaleReasonEntities(true, -1, -1);
    }

    public List<NosaleReason> findNosaleReasonEntities(int maxResults, int firstResult) {
        return findNosaleReasonEntities(false, maxResults, firstResult);
    }

    private List<NosaleReason> findNosaleReasonEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from NosaleReason as o where o.enable = true and o.status = true order by o.code");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public List<NosaleReason> findAllNosaleReason() {
        Query q = em.createQuery("select object(o) from NosaleReason as o where o.enable = true order by o.code");
        return q.getResultList();
    }
    
    public List<NosaleReason> findByFilter(String txtSearch, String status){
        List<NosaleReason> list = null;
        try{
            String sql = "select object(o) from NosaleReason as o where o.enable = true";
            if(txtSearch != null && !txtSearch.equals("")){
                sql += " and (UPPER(o.name) like :txtSearch)";
            }
            if(status != null && !status.equals("")){
                if(status.equals("0")){
                    sql += " and (o.status = false or o.status is null)";
                }else if(status.equals("1")) {
                    sql += " and o.status = true";
                }
            }
            sql += " order by o.code";
            
            Query q = em.createQuery(sql);
            if(txtSearch != null && !txtSearch.equals("")){
                q.setParameter("txtSearch", "%" + txtSearch.toUpperCase() + "%");
            }
            
            list = q.getResultList();
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
    
    public NosaleReason findNosaleReason(Integer id) {
        return em.find(NosaleReason.class, id);
    }

    public int getNosaleReasonCount() {
        return ((Long) em.createQuery("select count(o) from NosaleReason as o").getSingleResult()).intValue();
    }

    public Map<String, Integer> getNosaleReasonList() {
        List<NosaleReason> list = this.findNosaleReasonEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (NosaleReason obj : list) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
    
    public int checkNosaleReasonName(String name, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from NosaleReason as o where o.name =:name and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from NosaleReason as o where o.name =:name and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("name", name);
        return ((Long) q.getSingleResult()).intValue();
    }

}
