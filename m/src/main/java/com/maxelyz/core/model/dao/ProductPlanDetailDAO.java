/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.Product;
import com.maxelyz.core.model.entity.ProductPlan;
import com.maxelyz.core.model.entity.ProductPlanDetail;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ProductPlanDetailDAO {

    private static Logger log = Logger.getLogger(ProductPlanDetailDAO.class);

    @PersistenceContext
    private EntityManager em;

    public void create(ProductPlanDetail productPlanDetail) throws PreexistingEntityException, Exception {
        em.persist(productPlanDetail);
    }

    public void edit(ProductPlanDetail productPlanDetail) throws NonexistentEntityException, Exception {
        productPlanDetail = em.merge(productPlanDetail);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        ProductPlanDetail productPlanDetail;
        try {
            productPlanDetail = em.getReference(ProductPlanDetail.class, id);
            productPlanDetail.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The productPlanDetail with id " + id + " no longer exists.", enfe);
        }
        em.remove(productPlanDetail);
    }

    public List<ProductPlanDetail> findProductPlanDetailEntities() {
        return findProductPlanDetailEntities(true, -1, -1);
    }

    public List<ProductPlanDetail> findProductPlanDetailEntities(int maxResults, int firstResult) {
        return findProductPlanDetailEntities(false, maxResults, firstResult);
    }

    private List<ProductPlanDetail> findProductPlanDetailEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from ProductPlanDetail as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public ProductPlanDetail findProductPlanDetail(Integer id) {
        return em.find(ProductPlanDetail.class, id);
    }

    public int getProductPlanDetailCount() {
        return ((Long) em.createQuery("select count(o) from ProductPlanDetail as o").getSingleResult()).intValue();
    }

    public ProductPlanDetail findByAge(ProductPlan productPlan, Integer age) {
        try{
            Query q = em.createQuery("select p from ProductPlanDetail as p where p.productPlan = :productPlan and p.fromVal <= :age and p.toVal >= :age");
            q.setParameter("productPlan", productPlan);
            q.setParameter("age", age);

            return (ProductPlanDetail) q.getSingleResult();

        }catch(Exception e){
            return null;
        }
    }
    
    public ProductPlanDetail findProductPlanDetail(Product product, Integer productPlanId, Integer age, String gender, String paymentModeId) {
        try{
            String sql = "select object(p) from ProductPlanDetail as p where 1=1";
            //if(productPlanId != null && productPlanId != 0){
                sql += " and p.productPlan.id = :productPlanId";
            //}
            //if(age != null && age != 0){
                sql += " and p.fromVal <= :age and p.toVal >= :age";
            //}
            if(product != null && product.getGender() != null && product.getGender()){
                sql += " and p.gender = :gender";
            }
            if(paymentModeId != null && !paymentModeId.equals("")){
                sql += " and p.productPlan.paymentMode = :paymentModeId";
            }
            Query q = em.createQuery(sql);
            //if(productPlanId != null && productPlanId != 0){
                q.setParameter("productPlanId", productPlanId);
            //}
            //if(age != null && age != 0){
                q.setParameter("age", age);
            //}
            if(product != null && product.getGender() != null && product.getGender()){
                q.setParameter("gender", gender);
            }
            if(paymentModeId != null && !paymentModeId.equals("")){
                q.setParameter("paymentModeId", paymentModeId);
            }
            
            List<ProductPlanDetail> list = q.getResultList();
            if(list.isEmpty()){
                return null;
            } else if(list.size() > 1) {
                return null;
            } else {
                return list.get(0);
            }

        }catch(Exception e){
            log.error(e);
            return null;
        }
    }

    public ProductPlanDetail findProductPlanDetailByTokio(Integer pSeqNo, Integer pProductPlanId) {
        String sql = "select object(o) from ProductPlanDetail o where o.seqNo = ?1 and o.productPlan.id = ?2";

        Query q = em.createQuery(sql);
        //Query q = em.createQuery("select o from ProductPlan o where o.id=?1");
        q.setParameter(1, pSeqNo);
        q.setParameter(2, pProductPlanId);
        q.setMaxResults(1);
        
        ProductPlanDetail pp = null;
        try {
            pp = (ProductPlanDetail) q.getSingleResult();
        } catch (Exception e) {
            pp = null;
        }
        return pp;
    }    
    
    public ProductPlanDetail findLastProductPlanDetailByProductAndPlanNo(int planNo, int productId){
        Query q = em.createQuery("select object(o) from ProductPlanDetail as o where o.productPlan.enable = true and o.productPlan.no = ?1 and o.productPlan.product.id = ?2 order by o.seqNo desc");
        q.setParameter(1, planNo);
        q.setParameter(2, productId);
        q.setMaxResults(1);
        ProductPlanDetail pp = null;
        try {
            pp = (ProductPlanDetail) q.getSingleResult();
        } catch (Exception e) {
            pp = null;
        }
        return pp;
    }   
    
    public ProductPlanDetail findMainInsurePlan(Integer poId, Integer age, String gender) {     
        Query q = em.createQuery("select object(ppd) from PurchaseOrder po "
                + "join po.purchaseOrderDetailCollection pod "
                + "join pod.productPlan pp "
                + "join pp.productPlanDetailCollection ppd "
                + "where pp.enable = true and po.id = :poId "
                + "and ppd.fromVal <= :age and ppd.toVal >= :age "
                + "and ppd.gender = :gender");
            
        q.setParameter("poId", poId);
        q.setParameter("age", age);
        q.setParameter("gender", gender);
        q.setMaxResults(1);
        try {
            return (ProductPlanDetail) q.getSingleResult();
        } catch (NoResultException ex) { 
            return null;
        }
    }    
    
    public List<ProductPlanDetail> findRelationChlidPlan(Integer poId, Integer age, String gender) {
        List<ProductPlanDetail> productPlanDetailList = null;
        
        Query q = em.createQuery("select object(o) from ProductPlanDetail as o "
                + "join o.productPlan pp "
                + "join pp.productPlanSpouseCollection ppsc "
                + "where pp.enable = true and ppsc.id in "
                + "(select pod.productPlan.id from PurchaseOrder po join po.purchaseOrderDetailCollection pod where po.id = :poId) "
                + "and o.fromVal <= :age and o.toVal >= :age "
                + "and o.gender = :gender "
                + "order by pp.id");
            
        q.setParameter("poId", poId);
        q.setParameter("age", age);
        q.setParameter("gender", gender);
        try {
            productPlanDetailList = q.getResultList();
            return productPlanDetailList;
        } catch (NoResultException ex) { 
            return null;
        }
    }
    
     public List<ProductPlanDetail> findProductPlanDetailByProductPlan(Integer productPlanId){
        try {
            Query q = em.createQuery("select object(o) from ProductPlanDetail as o where o.productPlan.id =?1");
            q.setParameter(1, productPlanId);
            return q.getResultList();
        }catch (NoResultException e){
            return null;
        }
    }
}
