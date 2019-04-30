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
 * @author User
 */
@Transactional
public class DeclarationFormDAO {
    
    @PersistenceContext
    private EntityManager em;

    public void create(DeclarationForm declarationForm) throws PreexistingEntityException, Exception {
        em.persist(declarationForm);
    }

    public void edit(DeclarationForm declarationForm) throws IllegalOrphanException, NonexistentEntityException, Exception {
        //delete registratonfield
        Query q = em.createQuery("Delete from DeclarationField reg where reg.declarationForm = ?1");
        q.setParameter(1, declarationForm);
        q.executeUpdate();

        declarationForm = em.merge(declarationForm);
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
        {Query q = em.createQuery("select object(o) from DeclarationForm as o order by o.name");
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
    
    public List<DeclarationForm> findDeclarationFormEntitiesEnable() {
        try
        {Query q = em.createQuery("select object(o) from DeclarationForm as o where enable = true order by o.name");

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
    
    public DeclarationForm getDeclarationFormByName(String name) {
        Query q = em.createQuery("select object(o) from DeclarationForm as o where o.name =?1");
        q.setParameter(1, name);
        q.setMaxResults(1);
       
        return (DeclarationForm)q.getSingleResult();
    }
    
    public DeclarationForm findDeclarationFormById(Integer declarationFormId) {
        Query q = em.createQuery("select object(o) from DeclarationForm as o where o.id =?1");
        q.setParameter(1, declarationFormId);
        q.setMaxResults(1);
       
        return (DeclarationForm)q.getSingleResult();
    }
/*
    public DeclarationForm findDeclarationFormByProductId(Integer productId) {
        Query q = em.createQuery("select object(reg) from Product p join p.declarationForm reg where p.id=?1");
        q.setParameter(1, productId);
        q.setMaxResults(1);
       
        return (DeclarationForm)q.getSingleResult();
    }
*/
    
    public List<DeclarationForm> findDeclarationFormByProductId(Integer productId) {
        Query q = em.createQuery("SELECT object(o) FROM DeclarationForm as o WHERE o.enable = true"
                //+ " AND o.id IN (1,2,3)");
                //+ " AND o.id = ?1");
                + " AND o.id IN (SELECT pdm.productDeclarationMappingPK.declarationFormId FROM ProductDeclarationMapping pdm WHERE pdm.productDeclarationMappingPK.productId = ?1) ORDER BY o.name");
        //System.out.println("SQL : SELECT object(o) FROM DeclarationForm as o WHERE o.enable = true AND o.id = " + productId);
        q.setParameter(1, productId);
        return q.getResultList();
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
    
    public List<Object> getMergeDataViewTable()
    {
        Query q = em.createNativeQuery("select name from sys.views where name like 'vwDeclare%' ");
//        List<Object[]> l = q.getResultList();
        return q.getResultList();
    }
    
    public String findViewNameByDeclarationId(Integer declarationId) {
        Query q = em.createQuery("select distinct o.viewName from DeclarationForm as o where o.id =:declarationId and o.enable=true");
        q.setParameter("declarationId", declarationId);
        String s = ((String) q.getSingleResult());
        return s;
    }
    
    public String findNameByDeclarationId(Integer declarationId) {
        Query q = em.createQuery("select distinct o.name from DeclarationForm as o where o.id =:declarationId and o.enable=true");
        q.setParameter("declarationId", declarationId);
        String s = ((String) q.getSingleResult());
        return s;
    }
    
    public String findmergefileByDeclarationId(Integer declarationId) {
        Query q = em.createQuery("select distinct o.merge_file from DeclarationForm as o where o.id =:declarationId and o.enable=true");
        q.setParameter("declarationId", declarationId);
        String s = ((String) q.getSingleResult());
        return s;
    }
    
    public Map<String, String> getMergeDataViewList() {
        List<Object> doc = this.getMergeDataViewTable();
        Map<String, String> values = new LinkedHashMap<String, String>();
        for (Object obj : doc) {
            values.put(obj.toString(), obj.toString());
        }
        return values;
    }
}
