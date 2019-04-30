/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import com.maxelyz.core.model.entity.ProductCategory;
import com.maxelyz.utils.JSFUtil;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ProductCategoryDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(ProductCategory productCategory) throws PreexistingEntityException, Exception {
        em.persist(productCategory);
    }

    public void edit(ProductCategory productCategory) throws IllegalOrphanException, NonexistentEntityException, Exception {
        productCategory = em.merge(productCategory);
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        ProductCategory productCategory;
        try {
            productCategory = em.getReference(ProductCategory.class, id);
            productCategory.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The product category with id " + id + " no longer exists.", enfe);
        }
        em.remove(productCategory);
    }

    public List<ProductCategory> findProductCategoryEntities() {
        return findProductCategoryEntities(true, -1, -1);
    }

    public List<ProductCategory> findProductCategoryEntities(int maxResults, int firstResult) {
        return findProductCategoryEntities(false, maxResults, firstResult);
    }

    private List<ProductCategory> findProductCategoryEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from ProductCategory as o where enable = true order by o.name");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public ProductCategory findProductCategory(Integer id) {
        return em.find(ProductCategory.class, id);
    }

    public int getProductCategoryCount() {
        return ((Long) em.createQuery("select count(o) from ProductCategory as o").getSingleResult()).intValue();
    }

    public Map<String, Integer> getProductCateogryList() {
        List<ProductCategory> cats = this.findProductCategoryEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (ProductCategory obj : cats) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }

    public List<Object> getProductCateogryListBy(Integer userId, Integer customerId, Integer campaignId, String categoryType) {
        String sql = "select pc.id, pc.name from Assignment as a"
                + " inner join a.assignmentProductCollection ap"
                + " inner join ap.product p"
                + " inner join p.productCategory pc"
                + " inner join a.assignmentDetailCollection ad"
                + " where a.campaign.id = :campaignId"
                + " and pc.categoryType = :categoryType"
                + " and ad.users.id = :userId"
                + " and ad.customerId.id = :customerId"
                + " group by pc.id, pc.name";
        Query q = em.createQuery(sql);
        q.setParameter("userId", userId);
        q.setParameter("customerId", customerId);
        q.setParameter("campaignId", campaignId);
        q.setParameter("categoryType", categoryType);
        
        List<Object> cats = (List<Object>) q.getResultList();
//        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
//        List<ProductCategory> list = new ArrayList<ProductCategory>();
//        for (Integer id : cats) {
//            ProductCategory pc = this.findProductCategory(id);
//            list.add(pc);
//        }
        return cats;
        }

    public List<Object> getProductCateogryListBy1(Integer campaignId, String categoryType) {
        String sql = "select pc.id, pc.name from Assignment as a"
                + " inner join a.assignmentProductCollection ap"
                + " inner join ap.product p"
                + " inner join p.productCategory pc"
                + " where a.campaign.id = :campaignId and pc.categoryType = :categoryType"
                + " group by pc.id, pc.name";
        Query q = em.createQuery(sql);
        q.setParameter("campaignId", campaignId);
        q.setParameter("categoryType", categoryType);
        
        List<Object> cats = (List<Object>) q.getResultList();
//        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
//        List<ProductCategory> list = new ArrayList<ProductCategory>();
//        for (Integer id : cats) {
//            ProductCategory pc = this.findProductCategory(id);
//            list.add(pc);
//        }
        return cats;
        }

    public Map<String, String> getProductCateogryTypeList() {
        Map<String, String> values = new LinkedHashMap<String, String>();
        values.put(JSFUtil.getBundleValue("life"), "life");
        values.put(JSFUtil.getBundleValue("nonlife"), "nonlife");
        values.put(JSFUtil.getBundleValue("motor"), "motor");
//        values.put(JSFUtil.getBundleValue("motoradvance"), "motoradvance");
        values.put(JSFUtil.getBundleValue("retail"), "retail");
//        values.put(obj.getName(), obj.getId());
        return values;
    }
    
    public List<String> findProductCategoryType(Integer campaignId, Integer userId, Integer customerId){
        
        String sql = "select distinct pc.categoryType from Assignment a"
                + " inner join a.assignmentProductCollection ap"
                + " inner join ap.product p"
                + " inner join a.assignmentDetailCollection ad"
                + " inner join p.productCategory pc"
                + " where a.campaign.id = :campaignId"
                + " and ad.users.id = :userId"
                + " and ad.customerId.id = :customerId";
        Query q = em.createQuery(sql);
        q.setParameter("campaignId", campaignId);
        q.setParameter("userId", userId);
        q.setParameter("customerId", customerId);
        
        List<String> list = q.getResultList();
        
        return list;        
    }
    
    public List<String> findProductCategoryTypeByCampaign(Integer campaignId){
        
        String sql = "select distinct pc.categoryType from Assignment a"
                + " inner join a.assignmentProductCollection ap"
                + " inner join ap.product p"
                + " inner join p.productCategory pc"
                + " where a.campaign.id = :campaignId";
        Query q = em.createQuery(sql);
        q.setParameter("campaignId", campaignId);
        
        List<String> list = q.getResultList();
        
        return list;        
    }
    
    public int checkProductCategorytName(String name, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from ProductCategory as o where o.name =:name and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from ProductCategory as o where o.name =:name and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("name", name);
        return ((Long) q.getSingleResult()).intValue();
    }
}
