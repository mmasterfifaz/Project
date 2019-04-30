/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.PurchaseOrderDetail;
import com.maxelyz.core.model.entity.PurchaseOrderRegister;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class PurchaseOrderRegisterDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(PurchaseOrderRegister urchaseOrderRegister) throws PreexistingEntityException, Exception {
        em.persist(urchaseOrderRegister);
    }

    public void edit(PurchaseOrderRegister urchaseOrderRegister) throws NonexistentEntityException, Exception {
        urchaseOrderRegister = em.merge(urchaseOrderRegister);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        PurchaseOrderRegister urchaseOrderRegister;
        try {
            urchaseOrderRegister = em.getReference(PurchaseOrderRegister.class, id);
            urchaseOrderRegister.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The urchaseOrderRegister with id " + id + " no longer exists.", enfe);
        }
        em.remove(urchaseOrderRegister);
    }

    public List<PurchaseOrderRegister> findPurchaseOrderRegisterEntities() {
        return findPurchaseOrderRegisterEntities(true, -1, -1);
    }

    public List<PurchaseOrderRegister> findPurchaseOrderRegisterEntities(int maxResults, int firstResult) {
        return findPurchaseOrderRegisterEntities(false, maxResults, firstResult);
    }

    private List<PurchaseOrderRegister> findPurchaseOrderRegisterEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from PurchaseOrderRegister as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public PurchaseOrderRegister findPurchaseOrderRegister(Integer id) {
        return em.find(PurchaseOrderRegister.class, id);
    }

    public int getPurchaseOrderRegisterCount() {
        return ((Long) em.createQuery("select count(o) from PurchaseOrderRegister as o").getSingleResult()).intValue();
    }
    
    public List<PurchaseOrderRegister> findPurchaseOrderRegisterByPurchaseDetailId(Integer id) {
        Query q = em.createQuery("select object(o) from PurchaseOrderRegister as o where o.purchaseOrderDetail.id = ?1 order by o.no desc");
        q.setParameter("1", id);
        return q.getResultList();
    }

    public boolean updatePurchaseOrderRegisterInfoFromBCIB(String companyCode, String packagecode, String leadId) {
        try {
            Query q = em.createNativeQuery("update por set por.fx44 =?1, por.fx50=?2 from purchase_order po " +
                    "inner join purchase_order_detail pod on pod.purchase_order_id = po.id\n" +
                    "inner join customer c on c.id = po.customer_id \n" +
                    "inner join purchase_order_register por on por.purchase_order_detail_id = pod.id where c.flexfield1 =?3");
            q.setParameter(1, companyCode);
            q.setParameter(2, packagecode);
            q.setParameter(3, leadId);
            q.executeUpdate();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
