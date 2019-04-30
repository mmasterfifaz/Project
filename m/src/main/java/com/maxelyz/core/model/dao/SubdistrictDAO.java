/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.District;
import com.maxelyz.core.model.entity.Province;
import com.maxelyz.core.model.entity.Subdistrict;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author oat
 */
@Transactional
public class SubdistrictDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(Subdistrict subdistrict) throws PreexistingEntityException, Exception {
        em.persist(subdistrict);
    }

    public void edit(Subdistrict subdistrict) throws NonexistentEntityException, Exception {
        subdistrict = em.merge(subdistrict);
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        Subdistrict subdistrict;
        try {
            subdistrict = em.getReference(Subdistrict.class, id);
            subdistrict.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The province with id " + id + " no longer exists.", enfe);
        }
        em.remove(subdistrict);
    }

    public List<Subdistrict> findSubDistrictEntities() {
        return findSubDistrictEntities(true, -1, -1);
    }

    public List<Subdistrict> findSubDistrictEntities(int maxResults, int firstResult) {
        return findSubDistrictEntities(false, maxResults, firstResult);
    }

    private List<Subdistrict> findSubDistrictEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from Subdistrict as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Subdistrict findSubDistrict(Integer id) {
        return em.find(Subdistrict.class, id);
    }

    public List<Subdistrict> findSubDistrictByDistrictId(Integer districtId) {
        List<Subdistrict> list = null;
        try {
            Query q = em.createQuery("select object(o) from Subdistrict as o where o.district.id = :districtId order by o.name");
            q.setParameter("districtId", districtId);
            list = q.getResultList();

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return list;
    }

    public List findSubDistrictByDistrictId1(Integer districtId, String postalCode) {
        List list = null;
        try {
            Query q = null;
            if(postalCode != null && !postalCode.equals("")) {
                q = em.createQuery("select o.id, o.name from Subdistrict as o where o.district.id = :districtId and o.postalCode = :postalCode order by o.name");
                q.setParameter("districtId", districtId);
                q.setParameter("postalCode", postalCode);
            } else {
                q = em.createQuery("select o.id, o.name from Subdistrict as o where o.district.id = :districtId order by o.name");
                q.setParameter("districtId", districtId);
            }
                        
            list = q.getResultList();

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return list;
    }

    public List findSubDistrictByDistrictId2(Integer districtId) {
        List list = null;
        try {
            Query q = em.createQuery("select o.id, o.name, o.postalCode from Subdistrict as o where o.district.id = :districtId order by o.name");
            q.setParameter("districtId", districtId);
            list = q.getResultList();

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return list;
    }

    public Subdistrict findShortenSubDistrictById(Integer subDistrictId) throws Exception {
        Subdistrict subDistrict = null;
        Query q = em.createNativeQuery("select o.id, o.name, o.postal_code, o.district_id, o.district_name, o.province_id, o.province_name from subdistrict as o where o.id=:id ");
        q.setParameter("id", subDistrictId);
        List<Object[]> list = q.getResultList();
        if (list != null && list.size() > 0) {
            Object[] vals = list.get(0);
            subDistrict = new Subdistrict((Integer) vals[0], (String) vals[1], (String) vals[2]);
            subDistrict.setDistrict(new District((Integer) vals[3], (String) vals[4]));
            subDistrict.getDistrict().setProvinceId(new Province((Integer) vals[5], (String) vals[6]));
        }
        return subDistrict;
    }

    // create function delete subdistrict by current subdistrict id
    public void findSubdistrictDeleteById(Integer currentSubdistrictId) throws Exception {
        // set statement for delete subdistrict by subdistrict id
        Query statement = em.createQuery("delete Subdistrict o where o.id = :currentSubdistrictId");
        statement.setParameter("currentSubdistrictId", currentSubdistrictId);
        try {
            statement.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public List findSubDistrictByPostalCode(String postalCode) {
        List list = null;
        try {
            Query q = em.createQuery("select o.id, o.name from Subdistrict as o where o.postalCode = :postalCode order by o.name");
            q.setParameter("postalCode", postalCode);
            list = q.getResultList();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return list;
    }
    
    
}
