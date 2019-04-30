/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.social.model.dao;

import com.maxelyz.social.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.social.model.dao.exceptions.PreexistingEntityException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.maxelyz.core.model.entity.Users;
import org.springframework.transaction.annotation.Transactional;
import com.maxelyz.social.model.entity.SoService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
/**
 *
 * @author ukritj
 */
@Transactional
public class SoServiceDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(SoService soService) throws PreexistingEntityException {
        em.persist(soService);
    }

    public void edit(SoService soService) throws NonexistentEntityException, Exception {
        soService = em.merge(soService);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        SoService soService;
        try {
            soService = em.getReference(SoService.class, id);
            soService.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The soService with id " + id + " no longer exists.", enfe);
        }
        em.remove(soService);
    }

    public List<SoService> findSoServiceEntities() {
        return findSoServiceEntities(true, -1, -1);
    }

    public List<SoService> findSoServiceEntities(int maxResults, int firstResult) {
        return findSoServiceEntities(false, maxResults, firstResult);
    }

    private List<SoService> findSoServiceEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from SoService as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public SoService findSoService(Integer id) {
        return em.find(SoService.class, id);
    }

    public List<Users> findSoServiceUsers(Integer id) {
        Query q = em.createQuery("select object(o) from SoService as o where o.id=?1");
        q.setParameter(1, id);

        List<SoService> soServices = q.getResultList();

        List<Users> userses = new ArrayList<Users>();
        for (SoService s : soServices){
            Collection<Users> us = s.getUsersCollection();
            for (Users u : us){
                userses.add(u);
            }
        }

        return userses;

    }

    public List<SoService> findSoServiceList(Integer priority, String keyword, String status) {
        String sql = "select object(o) from SoService as o where o.enable = true";
        if(priority != null && priority != 0){
            sql += " and o.servicePriority = :priority";
        }
        if(keyword != null && !keyword.isEmpty()){
            sql += " and o.name like :keyword";
        }
        if(status != null && !status.isEmpty()){
            if(status.equals("enable")){
                sql += " and o.status = true";
            }else if(status.equals("disable")){
                sql += " and o.status = false";
            }
        }
        Query q = em.createQuery(sql);
        if(priority != null && priority != 0){
            q.setParameter("priority", priority);
        }
        if(keyword != null && !keyword.isEmpty()){
            q.setParameter("keyword", keyword);
        }
        return q.getResultList();
    }
    
    public void removeSoAccountBySoServiceId(Integer soServiceId){
        try{
            String sql = "delete from so_service_account where so_service_id = ?1";
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, soServiceId);
            q.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void removeSoServiceKeywordBySoServiceId(Integer soServiceId){
        try{
            String sql = "delete from so_service_keyword where so_service_id = ?1";
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, soServiceId);
            q.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void removeSoServiceUserBySoServiceId(Integer soServiceId){
        try{
            String sql = "delete from so_service_user where so_service_id = ?1";
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, soServiceId);
            q.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void insertUser(Integer soServiceId, Integer userId){
        try{
            String sql = "insert so_service_user(so_service_id, user_id) values(:soServiceId, :userId)";
            Query q = em.createNativeQuery(sql);
            q.setParameter("soServiceId", soServiceId);
            q.setParameter("userId", userId);
            q.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public List<SoService> findSoServiceStatus() {
        Query q = em.createQuery("select object(o) from SoService as o where o.enable = true and o.status = true order by o.name");
        return q.getResultList();
    }
    
    public Map<String, Integer> getSoServiceList() {
        List<SoService> services = this.findSoServiceStatus();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (SoService obj : services) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
    
      public Map<String, Integer> getUsersListByService(int serviceId) {
        List<Users> users =  this.findSoServiceUsers(serviceId);
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Users obj : users) {
            values.put(obj.getName()+" "+obj.getSurname(), obj.getId());
        }
        return values;
    }
}
