/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.ProductCategory;
import com.maxelyz.core.model.entity.ProductDocument;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ProductDocumentDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(ProductDocument productDocument) {
        em.persist(productDocument);
    }

    public void edit(ProductDocument productDocument) throws NonexistentEntityException, Exception {
        productDocument = em.merge(productDocument);
    }
     
    public void destroy(Integer id) throws NonexistentEntityException {
        ProductDocument productDocument;
        try {
            productDocument = em.getReference(ProductDocument.class, id);
            productDocument.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The productDocument with id " + id + " no longer exists.", enfe);
        }
        em.remove(productDocument);
    }

    public List<ProductDocument> findProductDocumentEntities() {
        return findProductDocumentEntities(true, -1, -1);
    }

    public List<ProductDocument> findProductDocumentEntities(int maxResults, int firstResult) {
        return findProductDocumentEntities(false, maxResults, firstResult);
    }

    private List<ProductDocument> findProductDocumentEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from ProductDocument as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public ProductDocument findProductDocument(Integer id) {
        return em.find(ProductDocument.class, id);
    }
    
    public List<ProductDocument> findProductDocumentByProductId(Integer productId){
        Query q = em.createQuery("select object(o) from ProductDocument as o where o.product.id =:productId and o.enable=true");
        q.setParameter("productId", productId);
        
        return q.getResultList();
    }
    
    public List<Object> getMergeDataView()
    {
        Query q = em.createNativeQuery("select name from sys.views where name like 'vwMTL_po_app%' " +
//                "or name like 'generali_po_app%' " +
                " or name like 'vwStd_po_app%'" +
                " or (name LIKE 'vwF%' AND name LIKE '%_po_app%')" +
                " or (name LIKE 'vwNewb%' AND name LIKE '%_po_app%')" +
                " order by name");
//        List<Object[]> l = q.getResultList();
        return q.getResultList();
    }
    
        public List<Object> getSpecificMergeDataView(String viewName)
    {
        Query q = em.createNativeQuery("select name from sys.views where name =:viewName");
        q.setParameter("viewName", viewName);
//        List<Object[]> l = q.getResultList();
        return q.getResultList();
    }
    
//    public List<ProductDocument> findViewNameByDocumentId(Integer documentId){
//        Query q = em.createQuery("select object(o) from ProductDocument as o where o.document.id =:documentId and o.enable=true");
//        q.setParameter("documentId", documentId);
//        
//        return q.getResultList();
//    }
//    public Object findMergeDataView(){
//        Query q = em.createQuery("select object(o) from sys.views as o where o.name like 'vc_maxar%'");
//        
//        return q.getResultList();
//    }
    
    public Map<String, String> getMergeDataViewList() {
        List<Object> doc = this.getMergeDataView();
        Map<String, String> values = new LinkedHashMap<String, String>();
        for (Object obj : doc) {
            values.put(obj.toString(), obj.toString());
        }
        return values;
    }
    
    public Map<String, String> getSpecificMergeDataViewList(String viewName) {
        List<Object> doc = this.getSpecificMergeDataView(viewName);
        Map<String, String> values = new LinkedHashMap<String, String>();
        for (Object obj : doc) {
            values.put(obj.toString(), obj.toString());
        }
        return values;
    }
    
    public int getProductDocumentCount() {
        Query q = em.createQuery("select count(o) from ProductDocument as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public String findViewNameByDocumentId(Integer documentId) {
        Query q = em.createQuery("select distinct o.viewName from ProductDocument as o where o.document.id =:documentId and o.enable=true");
        q.setParameter("documentId", documentId);
        String s = ((String) q.getSingleResult());
        return s;
    }

    public String findViewNameByDocumentId(Integer documentId, Integer productId) {
        Query q = em.createQuery("select distinct o.viewName from ProductDocument as o where o.document.id =:documentId and o.product.id =:productId and o.enable=true");
        q.setParameter("documentId", documentId);
        q.setParameter("productId", productId);
        String s = ((String) q.getSingleResult());
        return s;
    }

    public String findColumnNameByDocumentId(Integer documentId) {
        Query q = em.createQuery("select distinct o.columnName from ProductDocument as o where o.document.id =:documentId and o.enable=true");
        q.setParameter("documentId", documentId);
        String s = ((String) q.getSingleResult());
        return s;
    }
    
    public String checkMergeViewbyDocumentIdAndProductId(Integer documentId,Integer productId)
    {
        try{
        Query q = em.createQuery("Select distinct o.viewName from ProductDocument as o where o.document.id =:documentId and o.product.id =:productId and o.enable = true");
        q.setParameter("documentId", documentId);
        q.setParameter("productId", productId);
        
            return ((String)q.getSingleResult());
        }
        catch(Exception ex)
        {
            return "";
        }
    }
    
    public List<String> checkStatusbyDocumentIdAndProductId(Integer documentId,Integer productId)
    {
        try{
        Query q = em.createQuery("Select distinct o.purchaseOrderStatus from ProductDocument as o where o.document.id =:documentId and o.product.id =:productId and o.enable = true");
        q.setParameter("documentId", documentId);
        q.setParameter("productId", productId);
        
            return q.getResultList();
        }
        catch(Exception ex)
        {
            List<String> emptyList = new ArrayList<String>(); 
            return emptyList;
        }
    }

    public List findColumnName(String viewName)
    {
        Query q = em.createNativeQuery("SELECT COLUMN_NAME FROM information_schema.columns WHERE table_name =:viewName");
        q.setParameter("viewName", viewName);
        
        return q.getResultList();
    }
    
    public List selectQuery(String sql)
    {
        Query q = em.createNativeQuery(sql);
        
        return q.getResultList();
    }
}
