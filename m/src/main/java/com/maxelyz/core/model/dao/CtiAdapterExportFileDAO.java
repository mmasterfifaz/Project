/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.CtiAdapterExportFile;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
/**
 *
 * @author Oat
 */
@Transactional
public class CtiAdapterExportFileDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(CtiAdapterExportFile ctiAdapterExportFile) {
        em.persist(ctiAdapterExportFile);
    }

    public void edit(CtiAdapterExportFile ctiAdapterExportFile) {
         ctiAdapterExportFile = em.merge(ctiAdapterExportFile);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        CtiAdapterExportFile ctiAdapterExportFile;
        try {
            ctiAdapterExportFile = em.getReference(CtiAdapterExportFile.class, id);
            ctiAdapterExportFile.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The CtiAdapterExportFile with id " + id + " no longer exists.", enfe);
        }
        em.remove(ctiAdapterExportFile);
    }

    public List<CtiAdapterExportFile> findCtiAdapterExportFileEntities() {
        return findCtiAdapterExportFileEntities(true, -1, -1);
    }

    public List<CtiAdapterExportFile> findCtiAdapterExportFileEntities(int maxResults, int firstResult) {
        return findCtiAdapterExportFileEntities(false, maxResults, firstResult);
    }

    private List<CtiAdapterExportFile> findCtiAdapterExportFileEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from CtiAdapterExportFile as o order by o.id");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public CtiAdapterExportFile findCtiAdapterExportFile(Integer id) {
        return em.find(CtiAdapterExportFile.class, id);
    }

    public int getCtiAdapterExportFileCount() {
        return ((Long) em.createQuery("select count(o) from CtiAdapterExportFile as o").getSingleResult()).intValue(); 
    }

}
