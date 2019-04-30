/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.ConfigurationAutocase;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Oat
 */
@Transactional
public class ConfigurationAutocaseDAO  {

    @PersistenceContext
    private EntityManager em;


    public void create(ConfigurationAutocase configurationAutocase) throws Exception {
        em.persist(configurationAutocase);         
    }

    public void edit(ConfigurationAutocase configurationAutocase) throws Exception {    
        configurationAutocase = em.merge(configurationAutocase);           
    }


    public List<ConfigurationAutocase> findConfigurationAutocaseEntities() {
        return findConfigurationAutocaseEntities(true, -1, -1);
    }

    public List<ConfigurationAutocase> findConfigurationAutocaseEntities(int maxResults, int firstResult) {
        return findConfigurationAutocaseEntities(false, maxResults, firstResult);
    }

    private List<ConfigurationAutocase> findConfigurationAutocaseEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from ConfigurationAutocase as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
       
    }

    public ConfigurationAutocase findConfigurationAutocase(Integer id) {
        return em.find(ConfigurationAutocase.class, id);  
    }

    public int getConfigurationAutocaseCount() {
        Query q = em.createQuery("select count(o) from ConfigurationAutocase as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
}
