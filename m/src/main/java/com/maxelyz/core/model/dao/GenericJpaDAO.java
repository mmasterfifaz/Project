/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author oat
 */
@Transactional
public class GenericJpaDAO<T, ID extends Serializable> implements GenericDAO<T, ID> {

    @PersistenceContext
    protected EntityManager em;
    
    @Override
    public void create(T entity) {
        em.persist(entity); 
    }

    @Override
    public void edit(T entity) {
        entity = em.merge(entity);
    }

    
    
}
