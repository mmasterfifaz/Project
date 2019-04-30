/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.ProductPlan;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ProductPlanDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(ProductPlan productPlan) throws PreexistingEntityException, Exception {
        em.persist(productPlan);
    }

    public void edit(ProductPlan productPlan) throws NonexistentEntityException, Exception {
        productPlan = em.merge(productPlan);
    }

    public void destroy(Integer id) throws NonexistentEntityException {

        ProductPlan productPlan;
        try {
            productPlan = em.getReference(ProductPlan.class, id);
            productPlan.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The productPlan with id " + id + " no longer exists.", enfe);
        }
        em.remove(productPlan);
    }

    public List<ProductPlan> findProductPlanEntities() {
        return findProductPlanEntities(true, -1, -1);
    }

    public List<ProductPlan> findProductPlanEntities(int maxResults, int firstResult) {
        return findProductPlanEntities(false, maxResults, firstResult);
    }

    private List<ProductPlan> findProductPlanEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from ProductPlan as o where enable = true");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public ProductPlan findProductPlan(Integer id) {
        ProductPlan p = em.find(ProductPlan.class, id);
        p.getProductPlanDetailCollection().size();
        return p;
    }

    public ProductPlan findProductPlanId(Integer id) {
        return em.find(ProductPlan.class, id);
    }

    public int getProductPlanCount() {
        return ((Long) em.createQuery("select count(o) from ProductPlan as o").getSingleResult()).intValue();
    }

    public List<ProductPlan> findProductPlanByName(String nameKeyword) {
        Query q = em.createNamedQuery("ProductPlan.findByName");
        q.setParameter("name", nameKeyword);
        return q.getResultList();
    }

    public List<ProductPlan> findProductPlanEntitiesByProduct(int productId) {
        try {
//            Query q = em.createQuery("select o from ProductPlan o where (o.product.id="+productId+") and o.enable=true order by o.no, o.code, o.name");
            Query q = em.createQuery("select o from ProductPlan o where (o.product.id="+productId+") and o.enable=true order by o.id, o.code, o.name");
            boolean all = true;
            if (!all) {
                q.setMaxResults(-1);
                q.setFirstResult(-1);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<ProductPlan> findProductPlanEntitiesByProductPaymentMode(int productId, String paymentModeId) {
        try {
//            Query q = em.createQuery("select o from ProductPlan o where (o.product.id="+productId+") and (o.paymentMode="+paymentModeId+") and o.enable=true order by o.no, o.code, o.name");
            Query q = em.createQuery("select o from ProductPlan o where (o.product.id="+productId+") and (o.paymentMode="+paymentModeId+") and o.enable=true order by o.id, o.code, o.name");
            boolean all = true;
            if (!all) {
                q.setMaxResults(-1);
                q.setFirstResult(-1);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Object> findProductPlanEntitiesByProductPaymentMode(int productId, String paymentModeId, String gender) {
        try {
            String sql = "select o.id,o.code,o.name from ProductPlan o join o.productPlanDetailCollection ppd where o.enable=true and (o.paymentMode="+paymentModeId+") and (o.product.id="+productId+")";
            if(gender != null && !gender.isEmpty()) {
                sql += " and ppd.gender = '"+gender+"'";
            }
            sql += " group by o.id,o.code,o.name order by o.id, o.code, o.name";
            Query q = em.createQuery(sql);
            boolean all = true;
            if (!all) {
                q.setMaxResults(-1);
                q.setFirstResult(-1);
            }
            return (List<Object>) q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Object> findProductPlanEntitiesByProductPlan(int productPlanId, String paymentModeId, String gender) {
        try {
            String sql = "select o.id,o.code,o.name from ProductPlan o join o.productPlanDetailCollection ppd where o.enable=true and (o.paymentMode="+paymentModeId+") and (o.id="+productPlanId+")";
            if(gender != null && !gender.isEmpty()) {
                sql += " and ppd.gender = '"+gender+"'";
            }
            sql += " group by o.id,o.code,o.name order by o.id, o.code, o.name";
            Query q = em.createQuery(sql);
            boolean all = true;
            if (!all) {
                q.setMaxResults(-1);
                q.setFirstResult(-1);
            }
            return (List<Object>) q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Object> findProductPlanEntitiesProductPaymentModeByFamily(int productId, String paymentModeId, String gender) {
        try {
            String sql = "select o.id,o.code,o.name from ProductPlan o join o.productPlanDetailCollection ppd where o.enable=true and (o.paymentMode=" + paymentModeId + ") and (o.product.id=" + productId + ") and (o.masterPlan = true)";
            if (gender != null && !gender.isEmpty()) {
                sql += " and ppd.gender = '" + gender + "'";
            }
            sql += " group by o.id,o.code,o.name order by o.id, o.code, o.name";
            Query q = em.createQuery(sql);
            boolean all = true;
            if (!all) {
                q.setMaxResults(-1);
                q.setFirstResult(-1);
            }
            return (List<Object>) q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Object> findProductPlanEntitiesProductPaymentModeByFamily(int productId, String paymentModeId, boolean masterPlan, String gender) {
        try {
            String sql = "select o.id,o.code,o.name from ProductPlan o join o.productPlanDetailCollection ppd where o.enable=true and o.paymentMode=" + paymentModeId + " and o.product.id=" + productId + " and o.masterPlan="+ masterPlan + "";
            if (gender != null && !gender.isEmpty()) {
                sql += " and ppd.gender = '" + gender + "'";
            }
            sql += " group by o.id,o.code,o.name order by o.id, o.code, o.name";
            Query q = em.createQuery(sql);
            boolean all = true;
            if (!all) {
                q.setMaxResults(-1);
                q.setFirstResult(-1);
            }
            return (List<Object>) q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Object> findProductPlanEntitiesProductPaymentModeByFamilyIsSpouse(Integer producPlanId, String paymentModeId) {
        try {
            String sql = "select ppmc.id,ppmc.code,ppmc.name from ProductPlan pp join pp.productPlanMasterCollection ppmc where pp.enable = true and pp.masterPlan = true"
                       + " and pp.id = " + producPlanId + " and ppmc.paymentMode = " + paymentModeId + "";
            sql += " order by pp.id, pp.code, pp.name";
            Query q = em.createQuery(sql);
            boolean all = true;
            if (!all) {
                q.setMaxResults(-1);
                q.setFirstResult(-1);
            }
            return (List<Object>) q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Object> findProductPlanEntitiesProductPaymentModeByFamilyDiscount(Integer producPlanId, String paymentModeId, boolean masterPlan) {
        try {
            String sql = "select pp.id, pp.code, pp.name from ProductPlan pp where pp.enable = true and pp.masterPlan = " + masterPlan
                       + " and pp.id = " + producPlanId + " and pp.paymentMode = " + paymentModeId + "";
            sql += " order by pp.id, pp.code, pp.name";
            Query q = em.createQuery(sql);
            boolean all = true;
            if (!all) {
                q.setMaxResults(-1);
                q.setFirstResult(-1);
            }
            return (List<Object>) q.getResultList();
        } finally {
            em.close();
        }
    }

    public ProductPlan findProductPlanByIdPaymentMode(Integer id, String paymentMode) {
        //public ProductPlan findProductPlanByIdPaymentMode(Integer id) {
        String sql = "select object(o) from ProductPlan o where o.id = ?1";
        if(paymentMode != null && !paymentMode.equals("")){
            sql += " and o.paymentMode = ?2";
        }
        Query q = em.createQuery(sql);
        //Query q = em.createQuery("select o from ProductPlan o where o.id=?1");
        q.setParameter(1, id);
        if(paymentMode != null && !paymentMode.equals("")){
            q.setParameter(2, paymentMode);
        }
        ProductPlan pp = null;
        try{
            pp = (ProductPlan) q.getSingleResult();
        }catch(Exception e){
            pp = null;
        }
        return pp;
    }

    public List<ProductPlan> findProductPlanByNo(int no) {
        Query q = em.createQuery("select object(o) from ProductPlan as o where o.no = ?1 and o.enable = true");
        q.setParameter(1, no);
        q.setMaxResults(1);
        return q.getResultList();
    }

    public List<ProductPlan> findProductPlanByNoAndProductId(int no, int productId) {
        Query q = em.createQuery("select object(o) from ProductPlan as o where o.no = ?1 and o.product.id = ?2 and o.enable = true");
        q.setParameter(1, no);
        q.setParameter(2, productId);
        q.setMaxResults(1);
        return q.getResultList();
    }

    public List<ProductPlan> findProductPlanIsFamilyEntitiesByProduct(int productId) {
        try {
            Query q = em.createQuery("select o from ProductPlan o where (o.product.id = " + productId + ") and (o.product.familyProduct = true) and (o.masterPlan = true) and o.enable=true order by o.id, o.code, o.name");
            boolean all = true;
            if (!all) {
                q.setMaxResults(-1);
                q.setFirstResult(-1);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<ProductPlan> findProductPlanIsFamilyAndSpouseByProduct(int productId) {
        try {
            Query q = em.createQuery("select o from ProductPlan o where (o.product.id = " + productId + ") and (o.product.familyProduct = true) and (o.masterPlan = false) and o.enable=true order by o.id, o.code, o.name");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<ProductPlan> findProductPlanSamePaymentModeWithMaster(int productId, String paymentMode) {
        try {
            Query q = em.createQuery("select o from ProductPlan o "
                    + "where o.product.id = ?1 "
                    + "and o.paymentMode = ?2 and o.masterPlan = false and o.enable = true and o.product.familyProduct = true "
                    + "order by o.id, o.code, o.name");
            q.setParameter(1, productId);
            q.setParameter(2, paymentMode);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public ProductPlan findProductPlanIsFamilyAndSpouseByProductAndProductPlanId(int productPlanId) {
        try {
            Query q = em.createQuery("select object(o) from ProductPlan o where (o.product.familyProduct = true) and (o.masterPlan = true) and o.enable=true and o.id ="+productPlanId+" order by o.id, o.code, o.name");
            return (ProductPlan) q.getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }

    public void clearProductPlanRelation(Integer productId) {
        try {
            Query q = em.createQuery("select * from ProductPlan where product.id = ?1 ");
            q.setParameter(1, productId);
            q.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
    }

    public List<ProductPlan> findProductPlanByProductID(Integer productId) {
        try{
            Query q = em.createQuery("select object(o) from ProductPlan as o where product.id =:productId and o.masterPlan = true and o.enable =true");
            q.setParameter("productId", productId);
            return q.getResultList();
        }catch(NoResultException e){
            return null;
        }
    }

    public Integer findProductPlanRelationByProductPlanID(Integer productplanId) {
        try{
            Query q = em.createQuery(" select ppmc.id from ProductPlan pp join pp.productPlanMasterCollection ppmc where pp.enable=true and (pp.masterPlan = true) and pp.id =:productplanId");
            q.setParameter("productplanId", productplanId);
            q.setMaxResults(1);
            return ((Integer) q.getSingleResult());
        }catch(NoResultException e){
            return 0;
        }
    }

    public void insertCopyProductPlanRelation(Integer productPlanAfterCopy,Integer productPlanAfterCopyRelation){
        try{
            Query q = em.createNativeQuery("INSERT INTO product_plan_relation (master_product_plan_id,child_product_plan_id) VALUES (?1,?2)");
            q.setParameter(1, productPlanAfterCopy);
            q.setParameter(2, productPlanAfterCopyRelation);
            q.executeUpdate();
        }catch(Exception e){
           e.printStackTrace();
        }
    }

}
