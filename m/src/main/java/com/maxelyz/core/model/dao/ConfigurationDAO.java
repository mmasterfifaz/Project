/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.Configuration;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ConfigurationDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(Configuration configuration) throws PreexistingEntityException, Exception {
        em.persist(configuration);
    }

    public void edit(Configuration configuration) throws NonexistentEntityException, Exception {
        configuration = em.merge(configuration);
    }

    public void destroy(String id) throws NonexistentEntityException {
        Configuration configuration;
        try {
            configuration = em.getReference(Configuration.class, id);
            configuration.getProperty();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The configuration with id " + id + " no longer exists.", enfe);
        }
        em.remove(configuration);
    }

    public List<Configuration> findConfigurationEntities() {
        return findConfigurationEntities(true, -1, -1);
    }

    public List<Configuration> findConfigurationEntities(int maxResults, int firstResult) {
        return findConfigurationEntities(false, maxResults, firstResult);
    }

    private List<Configuration> findConfigurationEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from Configuration as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

        
    public Configuration findConfiguration(String id) {
        return em.find(Configuration.class, id);
    }

    public int getConfigurationCount() {
        return ((Long) em.createQuery("select count(o) from Configuration as o").getSingleResult()).intValue();
    }
}
