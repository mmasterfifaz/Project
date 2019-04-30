/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.District;
import com.maxelyz.core.model.entity.Province;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class DistrictDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(District district) {
        em.persist(district);
    }

    public void edit(District district) {
        district = em.merge(district);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        District district;
        try {
            district = em.getReference(District.class, id);
            district.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The district with id " + id + " no longer exists.", enfe);
        }
        em.remove(district);
    }

    public List<District> findDistrictEntities() {
        return findDistrictEntities(true, -1, -1);
    }

    public List<District> findDistrictEntities(int maxResults, int firstResult) {
        return findDistrictEntities(false, maxResults, firstResult);
    }

    private List<District> findDistrictEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from District as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public District findDistrict(Integer id) {
        return em.find(District.class, id);
    }

    public int getDistrictCount() {
        return ((Long) em.createQuery("select count(o) from District as o").getSingleResult()).intValue();
    }

    public List<District> findDistrictByProvinceId(Integer provinceId) {
        List<District> list = null;
        try {
            Query q = em.createQuery("select object(o) from District as o where o.provinceId.id = :provinceId order by o.name");
            q.setParameter("provinceId", provinceId);
            list = q.getResultList();

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return list;
    }
    
    public List findDistrictByProvinceId1(Integer provinceId, String postalCode) {
        List list = null;
        try {
            Query q = null;
            if(postalCode != null && !postalCode.equals("")) {
                q = em.createQuery("select o.id, o.name from District as o where o.provinceId.id = :provinceId "
                        + "and o.id in (select s.district.id from Subdistrict s where s.postalCode = :postalCode) order by o.name");
                q.setParameter("provinceId", provinceId);
                q.setParameter("postalCode", postalCode);
            } else {
                q = em.createQuery("select o.id, o.name from District as o where o.provinceId.id = :provinceId order by o.name");
                q.setParameter("provinceId", provinceId);
            }
                        
            list = q.getResultList();

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return list;
    }

//    public List findDistrictByProvinceId1(Integer provinceId) {
//        List list = null;
//        try {
//            Query q = em.createQuery("select o.id, o.name from District as o where o.provinceId.id = :provinceId order by o.name");
//            q.setParameter("provinceId", provinceId);
//            list = q.getResultList();
//
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//        return list;
//    }

    public District findShortenDistrictById(Integer districtId) throws Exception {
        District district = null;
        Query q = em.createNativeQuery("select o.id, o.name, o.province_id, o.province_name from district as o where o.id=:id ");
        q.setParameter("id", districtId);
        List<Object[]> list = q.getResultList();
        if (list != null && list.size() > 0) {
            Object[] vals = list.get(0);
            district = new District((Integer) vals[0], (String) vals[1]);
            district.setProvinceId(new Province((Integer) vals[2], (String) vals[3]));
        }
        return district;
    }

    // create function delete district by current district id
    public void findDistrictDeleteById(Integer currentDistrictId) throws Exception {
        // set statement delete subdistrict by current district id
        Query statement1 = em.createQuery("delete Subdistrict o where o.district.id = :currentDistrictId");
        statement1.setParameter("currentDistrictId", currentDistrictId);
        try {
            statement1.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // set statement delete district by current district id
        Query statement2 = em.createQuery("delete District o where o.id = :currentDistrictId");
        statement2.setParameter("currentDistrictId", currentDistrictId);
        try {
            statement2.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public List getListDistrictByPostalCode(String postalCode){
        List list = null;
        try {
            Query q = em.createQuery("select o.id, o.name from District as o where o.id in "
                    + "(select d.district.id from Subdistrict as d where d.postalCode = :postalCode) order by o.name");
            q.setParameter("postalCode", postalCode);
            list = q.getResultList();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return list;
    }
    
    public List<District> findDistrictByPostalCode(String postalCode){
        List list = null;
        try {
            Query q = em.createQuery("select  object(o) from District as o where o.id in "
                    + "(select d.district.id from Subdistrict as d where d.postalCode = :postalCode) order by o.name");
            q.setParameter("postalCode", postalCode);
            list = q.getResultList();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return list;
    }

}
