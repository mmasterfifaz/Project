/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.DeclarationForm;
import com.maxelyz.core.model.entity.ProductDeclarationMapping;
import com.maxelyz.core.model.entity.ProductDeclarationMappingPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author User
 */
@Transactional(rollbackFor = Exception.class)
public class ProductDeclarationMappingDAO {
    @PersistenceContext
    private EntityManager em;

    public void create(ProductDeclarationMapping productDeclarationMapping) throws PreexistingEntityException, Exception {
        em.persist(productDeclarationMapping);
    }

    public void edit(ProductDeclarationMapping productDeclarationMapping) throws IllegalOrphanException, NonexistentEntityException, Exception {
        //delete
        Query q = em.createQuery("Delete from ProductDeclarationMapping reg where reg.productDeclarationMappingPK.productId = ?1");
        q.setParameter(1, productDeclarationMapping.getProductDeclarationMappingPK().getProductId());
        q.executeUpdate();

        productDeclarationMapping = em.merge(productDeclarationMapping);
        
    }
    
    public void edit1(Integer productId,Integer declarationfieldId) throws IllegalOrphanException, NonexistentEntityException, Exception {
     
        Query sql = em.createNativeQuery("INSERT INTO product_declaration_mapping (product_id,declaration_form_id) VALUES (:product , :declaration)");
        sql.setParameter("product", productId);
        sql.setParameter("declaration", declarationfieldId);
        
        try
        {
            sql.executeUpdate();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
   
    }
    
    public void delete(Integer productId) throws IllegalOrphanException, NonexistentEntityException, Exception {
            String a = "delete from product_declaration_mapping where product_id = :product";
        Query q = em.createNativeQuery(a);
        q.setParameter("product", productId);
        try{
        q.executeUpdate();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        DeclarationForm declarationForm;
        try {
            declarationForm = em.getReference(DeclarationForm.class, id);
            declarationForm.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The declaration form with id " + id + " no longer exists.", enfe);
        }
        em.remove(declarationForm);
    }

    public List<DeclarationForm> findDeclarationFormEntities() {
        return findDeclarationFormEntities(true, -1, -1);
    }

    public List<DeclarationForm> findDeclarationFormEntities(int maxResults, int firstResult) {
        return findDeclarationFormEntities(false, maxResults, firstResult);
    }

    private List<DeclarationForm> findDeclarationFormEntities(boolean all, int maxResults, int firstResult) {
        try
        {Query q = em.createQuery("select object(o) from DeclarationForm as o where enable = true order by o.name");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
        }
        catch(Exception ex)
        {
            return null;
        }
    }

    public DeclarationForm findDeclarationForm(Integer id) {
        return em.find(DeclarationForm.class, id);
    }

    public int getDeclarationFormCount() {
        return ((Long) em.createQuery("select count(o) from DeclarationForm as o").getSingleResult()).intValue();
    }

    public List<DeclarationForm> findDeclarationFormByName(String nameKeyword) {
        Query q = em.createNamedQuery("DeclarationForm.findByName");
        q.setParameter("name", nameKeyword);
        return q.getResultList();
    }

//    public List<ProductDeclarationMapping> findProductDeclarationMappingByProductId(Integer productId) {
//        try{
//        Query q = em.createQuery("select object(reg) from ProductDeclarationMapping as reg where reg.productId = :product_id");
//        q.setParameter("product_id", productId);
//        
//       
//        return q.getResultList();
//        }
//        catch (Exception ex)
//        {
//            return null;
//        }
//    }
    
    public List<ProductDeclarationMapping> findProductDeclarationMappingByProductId(Integer productId) {
        try{
        Query q = em.createQuery("select object(o) from ProductDeclarationMapping as o where o.productDeclarationMappingPK.productId = :product_id");
        q.setParameter("product_id", productId);

        return q.getResultList();
        }catch (Exception ex)
        {
            return null;
        }
    }
    
    public int checkDeclarationFormName(String name, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from DeclarationForm as o where o.name =:name and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from DeclarationForm as o where o.name =:name and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("name", name);
        return ((Long) q.getSingleResult()).intValue();
    }
}
