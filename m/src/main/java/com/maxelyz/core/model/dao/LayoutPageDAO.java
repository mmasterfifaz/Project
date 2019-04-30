/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.LayoutPage;
import com.maxelyz.core.model.entity.LayoutPagePK;
import com.maxelyz.core.model.entity.FxMapping;
import com.maxelyz.core.model.entity.FxMappingPK;
import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.transaction.UserTransaction;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author oat
 */
@Transactional
public class LayoutPageDAO  {

    @PersistenceContext
    private EntityManager em;
    
    public void create(LayoutPage layoutPage) throws PreexistingEntityException, Exception {
        em.persist(layoutPage);
    }
    
    public void edit(LayoutPage layoutPage) throws NonexistentEntityException, Exception {
       layoutPage = em.merge(layoutPage);
    }

    public void destroy(LayoutPagePK id) throws NonexistentEntityException {
        LayoutPage layoutPage;
        try {
            layoutPage = em.getReference(LayoutPage.class, id);
            layoutPage.getLayoutPagePK();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The layoutPage with id " + id + " no longer exists.", enfe);
        }
        em.remove(layoutPage);        
    }

    public List<LayoutPage> findLayoutPageEntities() {
        return findLayoutPageEntities(true, -1, -1);
    }

    public List<LayoutPage> findLayoutPageEntities(int maxResults, int firstResult) {
        return findLayoutPageEntities(false, maxResults, firstResult);
    }

    private List<LayoutPage> findLayoutPageEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from LayoutPage as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public LayoutPage findLayoutPage(LayoutPagePK id) {
        return em.find(LayoutPage.class, id);
    }
    
    public List<LayoutPage> findLayoutPageByPageName(String pageName) {
        Query q = em.createQuery("select object(o) from LayoutPage as o where o.layoutPagePK.pageName =?1 order by o.seqNo");
        q.setParameter(1, pageName);
   
        return q.getResultList();
    }
        
    public List<LayoutPage> findLayoutPageColByPageName(String pageName, int colNo) {
        Query q = em.createQuery("select object(o) from LayoutPage as o where o.layoutPagePK.pageName =?1 and o.colNo =?2 order by o.seqNo");
        q.setParameter(1, pageName);
        q.setParameter(2, colNo);
   
        return q.getResultList();
    }
    
    public List<LayoutPage> findLayoutPageByField(String pageName, String fieldName) {
        Query q = em.createQuery("select object(o) from LayoutPage as o where o.layoutPagePK.pageName =?1 and o.layoutPagePK.fieldName =?2");
        q.setParameter(1, pageName);
        q.setParameter(2, fieldName);
   
        return q.getResultList();
    }
        
    public int getLayoutPageCount() {
        Query q = em.createQuery("select count(o) from LayoutPage as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public void deleteLayoutPage(String pageName, int colNo) {
        Query q = em.createQuery("Delete from LayoutPage as o where o.layoutPagePK.pageName =?1 and o.colNo =?2");
        q.setParameter(1, pageName);
        q.setParameter(2, colNo);
        q.executeUpdate();
    }
       
    public void deleteLayoutPageName(String pageName) {
        Query q = em.createQuery("Delete from LayoutPage as o where o.layoutPagePK.pageName =?1");
        q.setParameter(1, pageName);
        q.executeUpdate();
    }
        
     public void create(List<LayoutPage> layoutPages, String pageName, int col) throws PreexistingEntityException {
        deleteLayoutPage(pageName,col);
        for (LayoutPage value : layoutPages) {
            if (findLayoutPageByField(value.getLayoutPagePK().getPageName(),value.getLayoutPagePK().getFieldName()).size()>0){
                throw new PreexistingEntityException("Duplicate Field: "+value.getLayoutPagePK().getFieldName()+"");
            }
            else {
                em.persist(value);
            }
        }
     }
     
}
