/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.utils;

import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.TransactionException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.transaction.TransactionDefinition;

/**
 *
 * @author ukritj
 */
public class HibernateExtendedJpaDialect extends HibernateJpaDialect {

    @Override
    public Object beginTransaction(EntityManager entityManager,
            TransactionDefinition definition) throws PersistenceException,
            SQLException, TransactionException {

        Session session = (Session) entityManager.getDelegate();
        DataSourceUtils.prepareConnectionForTransaction(session.connection(), definition);

        entityManager.getTransaction().begin();

        return prepareTransaction(entityManager, definition.isReadOnly(), definition.getName());
    }

}