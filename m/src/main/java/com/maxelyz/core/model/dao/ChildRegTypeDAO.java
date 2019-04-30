/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.ChildRegType;
import com.maxelyz.core.model.entity.ProductChildRegType;
import com.maxelyz.core.model.entity.Product;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Songwisit
 */
@Transactional
public class ChildRegTypeDAO {
    
    @PersistenceContext
    private EntityManager em;

    public void create(ChildRegType childRegType) throws PreexistingEntityException, Exception {
        em.persist(childRegType);
    }

    public void edit(ChildRegType childRegType) throws IllegalOrphanException, NonexistentEntityException, Exception {
//        Query q = em.createQuery("delete from ChildRegForm form where form.childRegType = ?1");
//        q.setParameter(1, childRegType);
//        q.executeUpdate();

        childRegType = em.merge(childRegType);
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        ChildRegType childRegType;
        try {
            childRegType = em.getReference(ChildRegType.class, id);
            childRegType.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The child register type with id " + id + " no longer exists.", enfe);
        }
        em.remove(childRegType);
    }

    public List<ChildRegType> findChildRegTypeEntities() {
        return findChildRegTypeEntities(true, -1, -1);
    }

    public List<ChildRegType> findChildRegTypeEntities(int maxResults, int firstResult) {
        return findChildRegTypeEntities(false, maxResults, firstResult);
    }

    private List<ChildRegType> findChildRegTypeEntities(boolean all, int maxResults, int firstResult) {
        try
        {
            Query q = em.createQuery("select object(o) from ChildRegType as o where o.enable =1 order by o.name");
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

    public ChildRegType findChildRegType(Integer id) {
        return em.find(ChildRegType.class, id);
    }
    
    public ChildRegType findChildRegTypeById(Integer id) {
        Query q = em.createQuery("select object(o) from DeclarationForm as o where o.id =?1");
        q.setParameter(1, id);
        q.setMaxResults(1);    
        return (ChildRegType)q.getSingleResult();
    }
    
    public List<ChildRegType> findChildRegTypeByName(String name) {
        Query q = em.createQuery("select object(o) from ChildRegType as o where o.name =?1");
        q.setParameter(1, name);   
        return q.getResultList();
    }
    
/*
    public DeclarationForm findDeclarationFormByProductId(Integer productId) {
        Query q = em.createQuery("select object(reg) from Product p join p.declarationForm reg where p.id=?1");
        q.setParameter(1, productId);
        q.setMaxResults(1);
       
        return (DeclarationForm)q.getSingleResult();
    }
*/
    
    public List<ChildRegType> findChildRegTypeByProduct(Integer productId) {
        Query q = em.createQuery("SELECT object(o) FROM ChildRegType o JOIN o.productChildRegTypeCollection p WHERE p.product.id = ?1 and p.enable = true ORDER BY o.seqNo");
        q.setParameter(1, productId);
        return q.getResultList();
    }
    
    public Map<String, Integer> getChildRegTypeList() {
        List<ChildRegType> doc = this.findChildRegTypeEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (ChildRegType obj : doc) {
            values.put(obj.getName(),obj.getId());
        }
        return values;
    }
    
    public List<Object> getChildRegTypeTable()
    {
        Query q = em.createNativeQuery("select name from sys.views where name like 'vwChildReg%' ");
//        List<Object[]> l = q.getResultList();
        return q.getResultList();
    }
    
    public int checkChildRegTypeName(String name, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from ChildRegType as o where o.name =:name and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from ChildRegType as o where o.name =:name and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("name", name);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public void updateSetEnableFalseChildRegFormByChildRegTypeId(Integer childRegTypeId)
    {
        Query q = em.createQuery("UPDATE ChildRegForm form SET form.enable = 0 WHERE form.childRegType.id = ?1");
        q.setParameter(1, childRegTypeId);
        q.executeUpdate();
    }
    
}