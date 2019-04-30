/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.OpOut;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public class OpOutDAO {
   
   private static Logger log = Logger.getLogger(OpOutDAO.class);
    
   @PersistenceContext
    private EntityManager em;

    public void create(OpOut opOut) throws Exception {
        this.validate(opOut);
        System.out.println("Create : " + opOut.getCreateDate());
        em.persist(opOut);
        updateOpOutCustomer(opOut);
    }

    public void edit(OpOut opOut) throws Exception {
        System.out.println("Edit : " + opOut.getCreateDate());
        opOut = em.merge(opOut);
        updateOpOutCustomer(opOut);
    }

     public void destroy(Integer id) throws Exception {
        OpOut opOut;
        try {
            opOut = em.getReference(OpOut.class, id);
            opOut.getId();
        } catch (Exception enfe) {
            throw new Exception("The opOut with id " + id + " no longer exists.", enfe);
        }
        em.remove(opOut);
    }
     
    public void updateOpOutCustomer(OpOut opOut) {
        if (opOut.getCheckName() || opOut.getCheckSurname() || opOut.getCheckTelephone()) {
            String sql = "update customer set op_out = 1, op_out_date = GETDATE() where 1=1";
            try{
                if (opOut.getCheckName()) {
                    sql += " and LTRIM(RTRIM(ISNULL(name,''))) = '" + opOut.getName().trim() + "'";
                }
                if (opOut.getCheckSurname()) {
                    sql += " and LTRIM(RTRIM(ISNULL(surname,''))) = '" + opOut.getSurname().trim() + "'";   
                }
                if (opOut.getCheckTelephone()) {
                    sql += " and (LTRIM(RTRIM(ISNULL(home_phone,''))) = '" + opOut.getTelephone1().trim() + "'";   
                    sql += " or LTRIM(RTRIM(ISNULL(office_phone,''))) = '" + opOut.getTelephone1().trim() + "'";   
                    sql += " or LTRIM(RTRIM(ISNULL(mobile_phone,''))) = '" + opOut.getTelephone1().trim() + "')";
                }
                Query q = em.createNativeQuery(sql);
                //System.out.println("SQL: " + sql);
                q.executeUpdate();
            }catch(Exception e){
                log.error(e);
            }
        }
    }
     
    public List<OpOut> findOpOutEntities() {
        return findOpOutEntities(true, -1, -1);
    }

    public List<OpOut> findOpOutEntities(int maxResults, int firstResult) {
        return findOpOutEntities(false, maxResults, firstResult);
    }

    private List<OpOut> findOpOutEntities(boolean all, int maxResults, int firstResult) {   
        Query q = em.createQuery("select object(o) from OpOut as o order by o.id");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public OpOut findOpOut(Integer id) {
        return em.find(OpOut.class, id);
       
    }

    public List<OpOut> findOpOutById(Integer id) {   
        Query q = em.createQuery("select object(o) from OpOut as o where o.id = ?1");
        q.setParameter(1, id);
        return q.getResultList();
        
    }
    
    public List<OpOut> findOpOutBySearch(String name, String surname, String telephone) {
        String sql = "select object(o) from OpOut as o where 1=1";

        if (name != null && !name.isEmpty()) {
            sql += " and LTRIM(RTRIM(ISNULL(name,''))) like '%" + name.trim() + "%'";
        }
        if (surname != null && !surname.isEmpty()) {
            sql += " and LTRIM(RTRIM(ISNULL(surname,''))) like '%" + surname.trim() + "%'";
        }
        if (telephone != null && !telephone.isEmpty()) {
            sql += " and LTRIM(RTRIM(ISNULL(telephone1,''))) like '%" + telephone.trim() + "%'";
        }
        sql += " order by o.name, o.surname, o.telephone1";
        Query q = em.createQuery(sql);
        System.out.println("SQL: " + sql);
        return q.getResultList();
    }

    public int getOpOutCount() {
        Query q = em.createQuery("select count(o) from OpOut as o");
        return ((Long) q.getSingleResult()).intValue();
    }

    public void validate(OpOut opOut) throws PreexistingEntityException {
        if (findOpOutById(opOut.getId()).size()>0)
            throw new PreexistingEntityException("Duplicate Id");
    }
}

