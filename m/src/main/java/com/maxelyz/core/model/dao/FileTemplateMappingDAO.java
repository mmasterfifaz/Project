/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.FileTemplateMapping;
import com.maxelyz.core.model.entity.FileTemplateMappingPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class FileTemplateMappingDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(FileTemplateMapping fileTemplateMapping) throws PreexistingEntityException, Exception {
        em.persist(fileTemplateMapping);
    }

    public void edit(FileTemplateMapping fileTemplateMapping) throws NonexistentEntityException, Exception {
        fileTemplateMapping = em.merge(fileTemplateMapping);
    }

    public void destroy(FileTemplateMappingPK id) throws NonexistentEntityException {
        FileTemplateMapping fileTemplateMapping;
        try {
            fileTemplateMapping = em.getReference(FileTemplateMapping.class, id);
            fileTemplateMapping.getFileTemplateMappingPK();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The fileTemplateMapping with id " + id + " no longer exists.", enfe);
        }
        em.remove(fileTemplateMapping);
    }

    public void destroyByFileTemplateId(int id) {
       Query q = em.createQuery("delete from FileTemplateMapping as o where o.fileTemplateMappingPK.fileTemplateId = ?1");
       q.setParameter(1, id);
       q.executeUpdate();
    }


    public List<FileTemplateMapping> findFileTemplateMappingEntitiesByFileTemplateId(int id) {
        Query q = em.createQuery("select object(o) from FileTemplateMapping as o where o.fileTemplateMappingPK.fileTemplateId = ?1 order by o.fileColumnNo");
        q.setParameter(1, id);
        return q.getResultList();
    }

    public List<FileTemplateMapping> findFileTemplateMappingEntities(int maxResults, int firstResult) {
        return findFileTemplateMappingEntities(false, maxResults, firstResult);
    }

    private List<FileTemplateMapping> findFileTemplateMappingEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from FileTemplateMapping as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public FileTemplateMapping findFileTemplateMapping(FileTemplateMappingPK id) {
        return em.find(FileTemplateMapping.class, id);
    }

    public int getFileTemplateMappingCount() {
        return ((Long) em.createQuery("select count(o) from FileTemplateMapping as o").getSingleResult()).intValue();
    }
    
    public List<FileTemplateMapping> findFileTemplateMappingById(int id) {
        Query q = em.createQuery("select object(o) from FileTemplateMapping as o "
                + "where o.fileTemplateMappingPK.fileTemplateId = ?1 and o.customerField.id <> 0 "
                + "order by fileColumnNo");
        q.setParameter(1, id);
        return q.getResultList();
    }
     
    public List<FileTemplateMapping> findFileTemplateMappingByIdContactResult(int id) {
        Query q = em.createQuery("select object(o) from FileTemplateMapping as o "
                + "where o.fileTemplateMappingPK.fileTemplateId = ?1 "
                + "order by fileColumnNo");
        q.setParameter(1, id);
        return q.getResultList();
    }
     
}
