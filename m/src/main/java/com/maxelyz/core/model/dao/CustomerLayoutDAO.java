/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.CustomerLayout;
import com.maxelyz.core.model.entity.CustomerLayoutDetail;
import com.maxelyz.core.model.entity.CustomerLayoutFxMapping;
import com.maxelyz.core.model.value.admin.FlexFieldValue;
import com.maxelyz.utils.JSFUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public class CustomerLayoutDAO  {

    @PersistenceContext
    private EntityManager em;
    
    public void create(CustomerLayout customerLayout) throws PreexistingEntityException, Exception {
        em.persist(customerLayout);
    }
    
    public void edit(CustomerLayout customerLayout) throws NonexistentEntityException, Exception {
       customerLayout = em.merge(customerLayout);
    }

    public List<CustomerLayout> findCustomerLayoutEntities() {
        return findCustomerLayoutEntities(true, -1, -1);
    }

    public List<CustomerLayout> findCustomerLayoutEntities(int maxResults, int firstResult) {
        return findCustomerLayoutEntities(false, maxResults, firstResult);
    }

    private List<CustomerLayout> findCustomerLayoutEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from CustomerLayout as o where o.enable = true order by o.name");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }
    
    public CustomerLayout findDefaultCustomerLayout() throws NoResultException {   
        Query q = em.createQuery("select object(o) from CustomerLayout as o where o.enable = true and o.isDefault = true ");
        q.setMaxResults(1);
        return (CustomerLayout) q.getSingleResult();
    }
    
    public CustomerLayout findCustomerLayout(Integer id) {
        return em.find(CustomerLayout.class, id);       
    }
     
    public int checkCustomerLayoutName(String name, Integer id) { 
        Query q;
        if(id == 0){
            q = em.createQuery("select count(o) from CustomerLayout as o where o.name =:name and o.enable = true");        
        } else {
            q = em.createQuery("select count(o) from CustomerLayout as o where o.name =:name and o.enable = true and o.id not like :id");
            q.setParameter("id", id);
        }
        q.setParameter("name", name);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public Map<String, Integer> getCustomerLayoutList() {
        List<CustomerLayout> customerLayout = findCustomerLayoutEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (CustomerLayout obj : customerLayout) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
     
    // METHOD FOR CUSTOMER LAYOUT FLEXFIELD MAPPING    
    public String findCustomName(int customerLayoutID, String fieldName) {
        Query q = em.createQuery("select o.customName from CustomerLayoutFxMapping as o "
                + "where o.customerLayout.id = ?1 and o.fieldName = ?2");
        q.setParameter(1, customerLayoutID);
        q.setParameter(2, fieldName);
        return q.getSingleResult().toString();
    }
        
    public List<CustomerLayoutFxMapping> findCustomerLayoutFxMapping(int customerLayoutID) {
        Query q = em.createQuery("select object(o) from CustomerLayoutFxMapping as o "
                + "where o.customerLayout.id = ?1 order by o.fieldName");
        q.setParameter(1, customerLayoutID);
        return q.getResultList();
    }
    
    public void deleteCustomerLayoutFxMapping(int customerLayoutID) {
        Query q = em.createQuery("Delete CustomerLayoutFxMapping as o where o.customerLayout.id  = ?1");
        q.setParameter(1, customerLayoutID);
        q.executeUpdate();
    }
    
    public void createCustomerLayoutFxMapping(List<FlexFieldValue> selectedFxFiedls, CustomerLayout customerLayout) throws PreexistingEntityException {
        try {        
            // CLEAR OLD CUSTOMER LAYOUT DETAIL
            deleteCustomerLayoutDetail(customerLayout.getId());
            
            // CLEAR OLD CUSTOMER LAYOUT FLEXFIELD MAPPING
            deleteCustomerLayoutFxMapping(customerLayout.getId());
            
            List<CustomerLayoutFxMapping> custFxList = new ArrayList<CustomerLayoutFxMapping>();
            for(FlexFieldValue obj : selectedFxFiedls) {
                custFxList.add(obj.getCustomerLayoutFxMapping());
            }
            
            customerLayout.setCustomerLayoutFxMappingCollection(custFxList);
            customerLayout.setUpdateBy(JSFUtil.getUserSession().getUserName());
            customerLayout.setUpdateDate(new Date());
            edit(customerLayout);
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // METHOD FOR CUSTOMER LAYOUT DETAIL
    public void deleteCustomerLayoutDetail(int customerLayoutID) {
        Query q = em.createQuery("Delete CustomerLayoutDetail as o where o.customerLayout.id = ?1");
        q.setParameter(1, customerLayoutID);
        q.executeUpdate();
    }
    
    public void deleteCustomerLayoutDetailColumn(int customerLayoutID, int col) {
        Query q = em.createQuery("Delete CustomerLayoutDetail as o where o.customerLayout.id = ?1 and o.colNo = ?2 ");
        q.setParameter(1, customerLayoutID);
        q.setParameter(2, col);
        q.executeUpdate();
    }
    
    public List<CustomerLayoutDetail> findLayoutPageColByPageName(int customerLayoutID, int colNo) {
        Query q = em.createQuery("select object(o) from CustomerLayoutDetail as o "
                + "where o.customerLayout.id = ?1 and o.colNo =?2 order by o.seqNo");
        q.setParameter(1, customerLayoutID);
        q.setParameter(2, colNo);
   
        return q.getResultList();
    }
    
    public List<CustomerLayoutDetail> findLayoutPageByField(int customerLayoutID, String fieldName) {
        Query q = em.createQuery("select object(o) from CustomerLayoutDetail as o "
                + "where o.customerLayout.id = ?1 and o.fieldName = ?2");
        q.setParameter(1, customerLayoutID);
        q.setParameter(2, fieldName);
   
        return q.getResultList();
    }
    
    public void createCustomerLayoutDetail(List<CustomerLayoutDetail> layoutPages, CustomerLayout customerLayout, int col) throws PreexistingEntityException {
       
        // CLEAR OLD CUSTOMER LAYOUT DETAIL BY COLUMN
        deleteCustomerLayoutDetailColumn(customerLayout.getId(), col);

        for (CustomerLayoutDetail value : layoutPages) {
            if (findLayoutPageByField(customerLayout.getId(), value.getFieldName()).size() > 0){
                throw new PreexistingEntityException("Duplicate Field: "+value.getFieldName()+"");
            } else {
                em.persist(value);
            }
        }
            
        try {
            customerLayout.setUpdateBy(JSFUtil.getUserSession().getUserName());
            customerLayout.setUpdateDate(new Date());
            edit(customerLayout);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
     }
    
}
