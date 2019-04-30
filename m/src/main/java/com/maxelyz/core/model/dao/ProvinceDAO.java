/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.Province;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ProvinceDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(Province province) throws PreexistingEntityException, Exception {
        em.persist(province);
    }

    public void edit(Province province) throws NonexistentEntityException, Exception {
        province = em.merge(province);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        Province province;
        try {
            province = em.getReference(Province.class, id);
            province.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The province with id " + id + " no longer exists.", enfe);
        }
        em.remove(province);
    }

    public List<Province> findProvinceEntities() {
        return findProvinceEntities(true, -1, -1);
    }

    public List<Province> findProvinceEntities(int maxResults, int firstResult) {
        return findProvinceEntities(false, maxResults, firstResult);
    }

    private List<Province> findProvinceEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from Province as o order by o.name");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Province findProvince(Integer id) {
        return em.find(Province.class, id);
    }

    public int getProvinceCount() {
        return ((Long) em.createQuery("select count(o) from Province as o").getSingleResult()).intValue();
    }

    public Map<String, Integer> getProvinceList() {
        List<Province> provinces = this.findProvinceEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Province obj : provinces) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }

    public List getProvinceList1() {
        Query q = em.createQuery("select o.id, o.name from Province as o order by o.name");
        return q.getResultList();
    }

    public Map<String, String> getProvinceList2() {
        List<Province> provinces = this.findProvinceEntities();
        Map<String, String> values = new LinkedHashMap<String, String>();
        for (Province obj : provinces) {
            values.put(obj.getName(), obj.getName());
        }
        return values;
    }

    public Province findShortenProvinceById(Integer provinceId) {
        Query q = em.createQuery("select new Province(o.id, o.name) from Province as o where o.id=:id ");
        q.setParameter("id", provinceId);
        return (Province) q.getSingleResult();
    }

    // create function delete by id from current province id
    public void findProvinceDeleteById(Integer currentProvinceId) throws Exception {

        // set statement for delete subdistrict by current province id
        Query statement1 = em.createQuery("delete Subdistrict o where o.province.id = :currentProvinceId");
        statement1.setParameter("currentProvinceId", currentProvinceId);
        try {
            statement1.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // set statement for delete district by current province id
        Query statement2 = em.createQuery("delete District o where o.provinceId.id = :currentProvinceId");
        statement2.setParameter("currentProvinceId", currentProvinceId);
        try {
            statement2.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // set statement for delete province by current province id
        Query statement3 = em.createQuery("delete Province o where o.id = :currentProvinceId");
        statement3.setParameter("currentProvinceId", currentProvinceId);
        try {
            statement3.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
