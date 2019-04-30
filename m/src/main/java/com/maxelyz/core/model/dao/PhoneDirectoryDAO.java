/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;
import com.maxelyz.core.model.dao.exceptions.IllegalOrphanException;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.PhoneDirectory;
import com.maxelyz.core.model.entity.PhoneDirectoryTelephone;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import java.util.*;
import javax.persistence.*;
import org.springframework.transaction.annotation.Transactional;
/**
 *
 * @author Administrator
 */
@Transactional
public class PhoneDirectoryDAO {
    
    @PersistenceContext
    private EntityManager em;

    public void create(PhoneDirectory phoneDirectory) throws PreexistingEntityException, Exception {
        em.persist(phoneDirectory);     
    }
    
    public void edit(PhoneDirectory phoneDirectory) throws IllegalOrphanException, NonexistentEntityException, Exception {
       phoneDirectory = em.merge(phoneDirectory);       
    }
    
    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        PhoneDirectory phoneDirectory;
        try {
            phoneDirectory = em.getReference(PhoneDirectory.class, id);
            phoneDirectory.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The phoneDirectory with id " + id + " no longer exists.", enfe);
        }
        List<String> illegalOrphanMessages = null;
        em.remove(phoneDirectory);        
    }
    
    public List<PhoneDirectory> findPhoneDirectoryEntities() {
        return findPhoneDirectoryEntities(true, -1, -1);
    }

    public List<PhoneDirectory> findPhoneDirectoryEntities(int maxResults, int firstResult) {
        return findPhoneDirectoryEntities(false, maxResults, firstResult);
    }

    private List<PhoneDirectory> findPhoneDirectoryEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from PhoneDirectory as o where o.enable=true order by o.name");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public PhoneDirectory findPhoneDirectory(Integer id) {
        return em.find(PhoneDirectory.class, id);
    }

    public int getPhoneDirectoryCount() {
        Query q = em.createQuery("select count(o) from PhoneDirectory as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public void editPhoneDirectory(PhoneDirectory phoneDirectory) throws IllegalOrphanException, NonexistentEntityException, Exception {
       Query q = em.createQuery("Delete from PhoneDirectoryTelephone p where p.phoneDirectory = ?1");
       q.setParameter(1, phoneDirectory);
       q.executeUpdate();

       phoneDirectory = em.merge(phoneDirectory);       
    }
    
    public List<PhoneDirectory> findPhoneDirectoryByCategory(Integer id) {
        Query q = em.createQuery("select distinct object(o) from PhoneDirectory as o "
                + "left join o.phoneDirectoryTelephoneCollection as p "
                + "where o.enable=true and o.phoneDirectoryCategory.id = :id order by o.name");
        q.setParameter("id", id);
        return q.getResultList();
    }
    
    public List<PhoneDirectory> findPhoneDirectorySearchTel(String phoneNo) {
        String sql = "select distinct object(o) from PhoneDirectory as o "
                + "join o.phoneDirectoryTelephoneCollection as p "
                + "where o.enable = true ";
        if(!phoneNo.isEmpty()) {
            sql += "and p.phoneNo = '"+phoneNo+"' ";
        }
        Query q = em.createQuery(sql);
        return q.getResultList();
    }
    
    public List<PhoneDirectory> findPhoneDirectoryBySearch(Integer categoryId, String name, String surname, String phoneNo, String email) {
        String sql = "select distinct object(o) from PhoneDirectory as o "
                + "left join o.phoneDirectoryTelephoneCollection as p "
                + "where o.enable = true and o.phoneDirectoryCategory.status = true and o.phoneDirectoryCategory.enable = true ";
        
        if(categoryId != null && categoryId != 0) {
            sql += "and o.phoneDirectoryCategory.id = "+categoryId+" ";
        }
        if(!name.isEmpty()) {
            sql += "and o.name like '%"+name+"%' ";
        }
        if(!surname.isEmpty()) {
            sql += "and o.surname like '%"+surname+"%' ";
        }
        if(!email.isEmpty()) {
            sql += "and o.email like '%"+email+"%' ";
        }
        if(!phoneNo.isEmpty()) {
            sql += "and p.phoneNo like '%"+phoneNo+"%' ";
        }
        
        sql += "order by o.name ";
        Query q = em.createQuery(sql);
        return q.getResultList();
    }
    
    public List<PhoneDirectory> findAddressDirectoryBySearch(Integer categoryId, String keyword) {
        String sql = "select distinct object(o) from PhoneDirectory as o "
                + "where o.enable = true and o.phoneDirectoryCategory.status = true and o.phoneDirectoryCategory.enable = true ";
        
        if(categoryId != null && categoryId != 0) {
            sql += "and o.phoneDirectoryCategory.id = "+categoryId+" ";
        }
        if(!keyword.isEmpty()) {
            sql += "and (o.name like '%"+keyword+"%' or o.surname like '%"+keyword+"%' or o.email like '%"+keyword+"%')";
        }
        sql += "order by o.name ";
        Query q = em.createQuery(sql);
        return q.getResultList();
    }
//    public List<PhoneDirectory> findPhoneDirectoryBySearch(Integer categoryId, String name, String surname, String phoneNo, String description, String keyword) {
//        String sql = "select distinct object(o) from PhoneDirectory as o "
//                + "join o.phoneDirectoryCategory as c "
//                + "join o.phoneDirectoryTelephoneCollection as p "
//                + "where c.enable = true and c.status = true and o.enable = true and o.status = true ";
//        
//        if(categoryId != null && categoryId != 0) {
//            sql += "and o.phoneDirectoryCategory.id = "+categoryId+" ";
//        }
//        if(!name.isEmpty()) {
//            sql += "and o.name like '%"+name+"%' ";
//        }
//        if(!surname.isEmpty()) {
//            sql += "and o.surname like '%"+surname+"%' ";
//        }
//        if(!phoneNo.isEmpty()) {
//            sql += "and p.phoneNo like '%"+phoneNo+"%' ";
//        }
//        if(!description.isEmpty()) {
//            sql += "and (c.description like '%"+description+"%' or o.description like '%"+description+"%' or p.description like '%"+description+"%') ";
//        }
//        if(!keyword.isEmpty()) {
//            sql += "and (c.name like '%"+keyword+"%' or c.description like '%"+keyword+"%' or o.name like '%"+keyword+"%' or o.surname like '%"+keyword+"%'"
//                + "or o.description like '%"+keyword+"%' or p.phoneNo like '%"+keyword+"%' or p.description like '%"+keyword+"%') ";
//        }
//        sql += "order by o.name ";
//        Query q = em.createQuery(sql);
//        return q.getResultList();
//    }
    
    public List<PhoneDirectory> findPhoneDirectoryBySearch(Integer categoryId, String name, String surname, String phoneNo, String description, String keyword) {
        String sql = "select distinct object(o) from PhoneDirectory as o "
                + "join o.phoneDirectoryCategory as c "
                + "join o.phoneDirectoryTelephoneCollection as p "
                + "where c.enable = true and c.status = true and o.enable = true and o.status = true ";
        
        if(categoryId != null && categoryId != 0) {
            sql += "and o.phoneDirectoryCategory.id = :categoryId ";
        }
        if(!name.isEmpty()) {
            sql += "and o.name like :name ";
        }
        if(!surname.isEmpty()) {
            sql += "and o.surname like :surname ";
        }
        if(!phoneNo.isEmpty()) {
            sql += "and p.phoneNo like :phoneNo ";
        }
        if(!description.isEmpty()) {
            sql += "and (c.description like :description or o.description like :description or p.description like :description) ";
        }
        if(!keyword.isEmpty()) {
            sql += "and (c.name like :keyword or c.description like :keyword or o.name like :keyword or o.surname like :keyword "
                + "or o.description like :keyword or p.phoneNo like :keyword or p.description like :keyword) ";
        }
        sql += "order by o.name ";
        Query q = em.createQuery(sql);
         if(categoryId != null && categoryId != 0) {
            q.setParameter("categoryId", categoryId);
        }
        if(!name.isEmpty()) {
            name = String.format("%s%s%s", "%", name, "%");
            q.setParameter("name", name);
        }
        if(!surname.isEmpty()) {
            surname = String.format("%s%s%s", "%", surname, "%");
            q.setParameter("surname", surname);
        }
        if(!phoneNo.isEmpty()) {
            phoneNo = String.format("%s%s%s", "%", phoneNo, "%");
            q.setParameter("phoneNo", phoneNo);
        }
        if(!description.isEmpty()) {
            description = String.format("%s%s%s", "%", description, "%");
            q.setParameter("description", description);
        }
        if(!keyword.isEmpty()) {
            keyword = String.format("%s%s%s", "%", keyword, "%");
            q.setParameter("keyword", keyword);
        }
        return q.getResultList();
    }
}
