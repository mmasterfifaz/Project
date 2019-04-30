
package com.maxelyz.core.model.dao;

import java.io.Serializable;

/**
 *
 * @author oat
 */
public interface GenericDAO<T, ID extends Serializable> {
    void create(T entity);
    void edit(T entity);
    //void destroy(T entity);
}
