/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.ContactResult;
import com.maxelyz.core.model.entity.Product;
import com.maxelyz.core.model.entity.ProductPlan;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ProductDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(Product product) {
        em.persist(product);
    }

    public void edit(Product product) throws IllegalOrphanException, NonexistentEntityException, Exception {
        product = em.merge(product);
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        Product product;
        try {
            product = em.getReference(Product.class, id);
            product.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The product with id " + id + " no longer exists.", enfe);
        }
        em.remove(product);
    }

    public List<Product> findProductEntities() {
        return findProductEntities(true, -1, -1);
    }

    public List<Product> findProductEntities(int maxResults, int firstResult) {
        return findProductEntities(false, maxResults, firstResult);
    }

    private List<Product> findProductEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from Product as o where o.enable = true order by o.code");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Product findProduct(Integer id) {
        return em.find(Product.class, id);
    }

    public int getProductCount() {
        Query q = em.createQuery("select count(o) from Product as o");
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<Product> findProductByName(String nameKeyword) {
        Query q = em.createQuery("select object(o) from Product as o where o.name like ?1 and o.enable = true order by o.code");
        q.setParameter(1, "%" + nameKeyword + "%");
        return q.getResultList();
    }

    public List<Product> findProductByCategoryId(Integer productCategoryId) {
        Query q = em.createQuery("select object(o) from Product as o join o.productCategory c where c.id = ?1 and o.enable = true order by o.code ");
        q.setParameter(1, productCategoryId);
        return q.getResultList();
    }

    public List<Product> findProductById(Integer Id) {
        Query q = em.createQuery("select object(o) from Product where id =:Id and o.enable = true order by o.code ");
        q.setParameter(1, Id);
        return q.getResultList();
    }

    public int getProductMaxId() {
        Query q = em.createQuery("select max(o.id) from Product as o");
        return ((Integer) q.getSingleResult()).intValue();
    }

    public Map<String, Integer> getProductList() {
        List<Product> products = this.findProductEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Product product : products) {
            values.put(product.getName(), product.getId());
        }
        return values;
    }

    private String genSQL(Integer userId, Integer customerId, String categoryType, Integer campaignId, List<Integer> productCategoryIdList,
            List<Integer> productPlanCategoryIdList, Integer brandId, Integer modelId, Integer modelTypeId, Integer modelYear,
            Integer sumInsuredMin, Integer sumInsuredMax, String claimType, String sqlType) {
        String sql = "";
        int i = 0;

        if (sqlType.equals("list")) {
            sql = "select object(pp) ";
        } else if (sqlType.equals("count")) {
            sql = "select count(*) ";
        }
        sql += " from Assignment a"
                + " inner join a.assignmentProductCollection ap"
                + " inner join ap.product p"
                + " left join p.productPlanCollection pp"
                + " inner join a.assignmentDetailCollection ad"
                + " inner join p.productCategory pc"
                + " where ad.unassignment is null and pc.categoryType = '" + categoryType + "'"
                + " and a.campaign.id = " + campaignId
                + " and ad.users.id = " + userId
                + " and ad.customerId.id = " + customerId;
        if (!productCategoryIdList.isEmpty()) {
            i = 0;
            sql += " and (";
            for (Integer id : productCategoryIdList) {
                i++;
                sql += " p.productCategory.id = " + id;
                if (i < productCategoryIdList.size()) {
                    sql += " or";
                }
            }
            sql += " )";
        }
        if (!productPlanCategoryIdList.isEmpty()) {
            i = 0;
            sql += " and (";
            for (Integer id : productPlanCategoryIdList) {
                i++;
                sql += " pp.productPlanCategory = " + id;
                if (i < productPlanCategoryIdList.size()) {
                    sql += " or";
                }
            }
            sql += " )";
        }
        if (brandId != 0) {
            sql += " and p.model.brand.id = " + brandId;
        }
        if (modelId != 0) {
            sql += " and p.model.id = " + modelId;
        }
        if (modelTypeId != 0) {
            sql += " and p.modelType.id = " + modelTypeId;
        }
        if (modelYear != 0) {
            //sql += " and p.modelYear = " + modelYear;
            sql += " and p.modelFromYear <= " + modelYear;
            sql += " and p.modelToYear >= " + modelYear;
        }
        if (sumInsuredMin != 0) {
            sql += " and pp.motorLoss >= " + sumInsuredMin;
        }
        if (sumInsuredMax != 0) {
            sql += " and pp.motorLoss <= " + sumInsuredMax;
        }
        if (!claimType.equals("")) {
            sql += " and pp.claimType = '" + claimType + "'";
        }

        return sql;
    }

    public List<ProductPlan> findProductBy(Integer userId, Integer customerId, String categoryType, Integer campaignId, List<Integer> productCategoryIdList,
            List<Integer> productPlanCategoryIdList, Integer brandId, Integer modelId, Integer modelTypeId, Integer modelYear,
            Integer sumInsuredMin, Integer sumInsuredMax, String claimType, int firstResult, int maxResults) {

        List<ProductPlan> list = new ArrayList<ProductPlan>();
        String sql = genSQL(userId, customerId, categoryType, campaignId, productCategoryIdList,
                productPlanCategoryIdList, brandId, modelId, modelTypeId, modelYear, sumInsuredMin, sumInsuredMax, claimType, "list");
        String sqlGroupBy = "";
        String sqlOrder = " order by p.model.brand.name, p.name, p.modelYear, pp.motorLoss, pp.netPremium";

        Query q = em.createQuery(sql + sqlGroupBy + sqlOrder);
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        list = (List<ProductPlan>) q.getResultList();
        return list;
    }

    public List<ProductPlan> findProductByInbound(String categoryType, Integer campaignId, List<Integer> productCategoryIdList,
            List<Integer> productPlanCategoryIdList, Integer brandId, Integer modelId, Integer modelTypeId, Integer modelYear,
            Integer sumInsuredMin, Integer sumInsuredMax, String claimType, int firstResult, int maxResults) {

        List<ProductPlan> list = new ArrayList<ProductPlan>();
        String sql = genSQLInbound(categoryType, campaignId, productCategoryIdList,
                productPlanCategoryIdList, brandId, modelId, modelTypeId, modelYear, sumInsuredMin, sumInsuredMax, claimType, "list");
        String sqlGroupBy = "";
        String sqlOrder = " order by p.model.brand.name, p.name, p.modelYear, pp.motorLoss, pp.netPremium";

        Query q = em.createQuery(sql + sqlGroupBy + sqlOrder);
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        list = (List<ProductPlan>) q.getResultList();
        return list;
    }

    private String genSQLInbound(String categoryType, Integer campaignId, List<Integer> productCategoryIdList,
            List<Integer> productPlanCategoryIdList, Integer brandId, Integer modelId, Integer modelTypeId, Integer modelYear,
            Integer sumInsuredMin, Integer sumInsuredMax, String claimType, String sqlType) {
        String sql = "";
        int i = 0;

        if (sqlType.equals("list")) {
            sql = "select object(pp) ";
        } else if (sqlType.equals("count")) {
            sql = "select count(*) ";
        }
        sql += " from Assignment a"
                + " inner join a.assignmentProductCollection ap"
                + " inner join ap.product p"
                + " left join p.productPlanCollection pp"
                + " inner join p.productCategory pc"
                + " where pc.categoryType = '" + categoryType + "'"
                + " and a.campaign.id = " + campaignId;
        if (!productCategoryIdList.isEmpty()) {
            i = 0;
            sql += " and (";
            for (Integer id : productCategoryIdList) {
                i++;
                sql += " p.productCategory.id = " + id;
                if (i < productCategoryIdList.size()) {
                    sql += " or";
                }
            }
            sql += " )";
        }
        if (!productPlanCategoryIdList.isEmpty()) {
            i = 0;
            sql += " and (";
            for (Integer id : productPlanCategoryIdList) {
                i++;
                sql += " pp.productPlanCategory = " + id;
                if (i < productPlanCategoryIdList.size()) {
                    sql += " or";
                }
            }
            sql += " )";
        }
        if (brandId != 0) {
            sql += " and p.model.brand.id = " + brandId;
        }
        if (modelId != 0) {
            sql += " and p.model.id = " + modelId;
        }
        if (modelTypeId != 0) {
            sql += " and p.modelType.id = " + modelTypeId;
        }
        if (modelYear != 0) {
            //sql += " and p.modelYear = " + modelYear;
            sql += " and p.modelFromYear <= " + modelYear;
            sql += " and p.modelToYear >= " + modelYear;
        }
        if (sumInsuredMin != 0) {
            sql += " and pp.motorLoss >= " + sumInsuredMin;
        }
        if (sumInsuredMax != 0) {
            sql += " and pp.motorLoss <= " + sumInsuredMax;
        }
        if (!claimType.equals("")) {
            sql += " and pp.claimType = '" + claimType + "'";
        }

        return sql;
    }

    public Integer countProductBy(Integer userId, Integer customerId, String categoryType, Integer campaignId, List<Integer> productCategoryIdList,
            List<Integer> productPlanCategoryIdList, Integer brandId, Integer modelId, Integer modelTypeId, Integer modelYear,
            Integer sumInsuredMin, Integer sumInsuredMax, String claimType) {

        String sql = genSQL(userId, customerId, categoryType, campaignId, productCategoryIdList,
                productPlanCategoryIdList, brandId, modelId, modelTypeId, modelYear, sumInsuredMin, sumInsuredMax, claimType, "count");
        String sqlGroupBy = "";
        String sqlOrder = "";

        Query q = em.createQuery(sql + sqlGroupBy + sqlOrder);

        return Integer.valueOf(((Long) q.getSingleResult()).intValue());
    }

    public Integer countProductByInbound(String categoryType, Integer campaignId, List<Integer> productCategoryIdList,
            List<Integer> productPlanCategoryIdList, Integer brandId, Integer modelId, Integer modelTypeId, Integer modelYear,
            Integer sumInsuredMin, Integer sumInsuredMax, String claimType) {

        String sql = genSQLInbound(categoryType, campaignId, productCategoryIdList,
                productPlanCategoryIdList, brandId, modelId, modelTypeId, modelYear, sumInsuredMin, sumInsuredMax, claimType, "count");
        String sqlGroupBy = "";
        String sqlOrder = "";

        Query q = em.createQuery(sql + sqlGroupBy + sqlOrder);

        return Integer.valueOf(((Long) q.getSingleResult()).intValue());
    }

    public List<Object> findProductBy(Integer userId, Integer customerId, String categoryType, Integer campaignId, Integer productCategoryId, Integer assignmentId) {
        int i = 0;
        List<Object> list = new ArrayList<Object>();
        String sql = "select p.id, p.name,p.contactResultPlan.id from Assignment a"
                + " inner join a.assignmentProductCollection ap"
                + " inner join ap.product p"
                + " inner join a.assignmentDetailCollection ad"
                + " inner join p.productCategory pc"
                + " where pc.categoryType = '" + categoryType + "'"
                + " and a.campaign.id = " + campaignId
                + " and ad.users.id = " + userId
                + " and ad.customerId.id = " + customerId
                + " and p.enable = true";

        if (productCategoryId != 0) {
            sql += " and pc.id = " + productCategoryId;
        }

        if (assignmentId != 0) {
            sql += " and a.id = " + assignmentId;
        }

        String sqlGroupBy = " group by p.id, p.name , p.contactResultPlan.id"; //low performance
        //String sqlGroupBy = "";
        String sqlOrder = "";

        Query q = em.createQuery(sql + sqlGroupBy + sqlOrder);
        List<Object> pidList = (List<Object>) q.getResultList();
//        for(Integer id : pidList){
//            Product p = this.findProduct(id);
//            list.add(p);
//        }

        return pidList;
    }

    public List<Object> findProductBy(String categoryType, Integer campaignId, Integer productCategoryId, Integer assignmentId) {
        int i = 0;
        List<Object> list = new ArrayList<Object>();
        String sql = "select p.id, p.name, p.contactResultPlan.id from Assignment a"
                + " inner join a.assignmentProductCollection ap"
                + " inner join ap.product p"
                + " inner join p.productCategory pc"
                + " where pc.categoryType = '" + categoryType + "'"
                + " and a.campaign.id = " + campaignId;

        if (productCategoryId != 0) {
            sql += " and pc.id = " + productCategoryId;
        }

        if (assignmentId != 0) {
            sql += " and a.id = " + assignmentId;
        }

        String sqlGroupBy = " group by p.id, p.name, p.contactResultPlan.id"; //low performance
        //String sqlGroupBy = "";
        String sqlOrder = "";

        Query q = em.createQuery(sql + sqlGroupBy + sqlOrder);
        List<Object> pidList = (List<Object>) q.getResultList();
//        for(Integer id : pidList){
//            Product p = this.findProduct(id);
//            list.add(p);
//        }

        return pidList;
    }

    public Map<String, Integer> findProductBy2(String productCategoryType,
            String selectedPpcIds,
            String selectedProductCategoryIds,
            String selectedProductSponsorIds,
            Integer modelTypeId,
            Integer modelId,
            Integer modelFromyear,
            Integer modelToyear) {
        int i = 0;
        List<Integer> list = new ArrayList<Integer>();
        String sql = "select distinct p.id from Product as p"
                + " left join p.productCategory as pc"
                + " left join p.productPlanCollection as pp"
                + " left join pp.productPlanCategory as ppc"
                + " left join p.productSponsor as ps"
                + " where 1=1";
        if (productCategoryType != null && !productCategoryType.equals("")) {
            sql += " and pc.categoryType = '" + productCategoryType + "'";
        }
        if (selectedPpcIds != null && !selectedPpcIds.equals("")) {
            sql += " and ppc.id in (" + selectedPpcIds + ")";
        }
        if (selectedProductCategoryIds != null && !selectedProductCategoryIds.equals("")) {
            sql += " and pc.id in (" + selectedProductCategoryIds + ")";
        }
        if (selectedProductSponsorIds != null && !selectedProductSponsorIds.equals("")) {
            sql += " and ps.id in (" + selectedProductSponsorIds + ")";
        }
        if (modelTypeId != null && modelTypeId != 0) {
            sql += " and p.modelType.id = " + modelTypeId;
        }
        if (modelId != null && modelId != 0) {
            sql += " and p.model.id = " + modelId;
        }
        if (modelFromyear != null && modelFromyear != 0) {
            sql += " and p.modelFromYear >= " + modelFromyear;
        }
        if (modelToyear != null && modelToyear != 0) {
            sql += " and p.modelToYear <= " + modelToyear;
        }

        String sqlGroupBy = "";
        String sqlOrder = "";

        Query q = em.createQuery(sql + sqlGroupBy + sqlOrder);
        list = q.getResultList();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Integer id : list) {
            Product product = this.findProduct(id);
            if (product.getProductCategory().getCategoryType().equals("motor")) {
                values.put(product.getName() + "(" + product.getModelType().getName() + "," + product.getModelYear() + "," + product.getProductCategory().getName() + ")", product.getId());
            } else {
                values.put(product.getName(), product.getId());
            }
        }

        return values;
    }

    public int checkProductCode(String code, Integer id) {
        Query q;
        if (id == 0) {
            q = em.createQuery("select count(o) from Product as o where o.code =:code and o.enable = true");
        } else {
            q = em.createQuery("select count(o) from Product as o where o.code =:code and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("code", code);
        return ((Long) q.getSingleResult()).intValue();
    }

    public String getFX11(Integer productId) {
        Query q = em.createQuery("select o.fx11 from Product as o where o.id = :productId");
        q.setParameter("productId", productId);
        return ((String) q.getSingleResult());
    }

    public int getProductByRefNumber(String listRefNumber) {

        Query statement = em.createQuery("select pod.product.id"
                + " from PurchaseOrderDetail pod join pod.purchaseOrder po"
                + " where po.refNo in (:listRefNumber)");

        statement.setParameter("listRefNumber", listRefNumber);
        try {
            return ((Integer) statement.getSingleResult()).intValue();
        } catch (NoResultException ex) {
            return 0;
        }
    }

    public int checkFamily(Integer id) {
        Query q;
        q = em.createQuery("select count(o) from Product as o where o.familyProduct = true and o.enable = true and o.id = :id");
        q.setParameter("id", id);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public List<Product> linkProduct(Integer contactResultPlanID) {       
        Query q = em.createQuery("select object(o) from Product as o where o.contactResultPlan.id =:contactResultPlanID and o.enable = true");
        q.setParameter("contactResultPlanID", contactResultPlanID);      
        return q.getResultList();
    }
    
    public int productNameCount(Integer contactResultPlanID) {
        Query q = em.createQuery("select count(o.name) from Product as o where o.contactResultPlan.id =:contactResultPlanID");
        q.setParameter("contactResultPlanID", contactResultPlanID);       
        return ((Long) q.getSingleResult()).intValue();
    }
    
}
