/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

//import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.AuditLog;
//import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import com.ntier.utils.ParameterMap;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author oat
 */

@Transactional
public class AuditLogDAO {

    @PersistenceContext
    private EntityManager em;

    private List<ParameterMap> params;
        
    public void create(AuditLog accessLog) throws Exception {
        em.persist(accessLog);        
    }

    public void edit(AuditLog accessLog) throws Exception {
        accessLog = em.merge(accessLog); 
    }

    public List<AuditLog> findAccessLogEntities() {
        return findAccessLogEntities(true, -1, -1);
    }

    public List<AuditLog> findAccessLogEntities(int maxResults, int firstResult) {
        return findAccessLogEntities(false, maxResults, firstResult);
    }

    private List<AuditLog> findAccessLogEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from AuditLog as o order by o.createDate desc");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    
    }
       
    public List<AuditLog> findAccessLogEntities(Date fromDate, Date toDate, String category, String userName, String customerName ) {

        String query;
        String select="SELECT object(o) FROM AuditLog o ";
  
        String where="WHERE 1 = 1 ";
        params = new ArrayList<ParameterMap>();
        ParameterMap param = new ParameterMap();
        
        if (fromDate != null){
            where += "and o.createDate >= :fromDate ";
            param = new ParameterMap();
            param.setName("fromDate");
            param.setValue(fromDate);
            params.add(param);
        }
        
        if (toDate != null){
            where += "and o.createDate  <= :toDate ";
            param = new ParameterMap();
            param.setName("toDate");

            Calendar date = Calendar.getInstance();
            date.setTime(toDate);
            date.add(Calendar.DATE, 1);
            param.setValue(date.getTime());
            params.add(param);
        }
        
        if (category != null && category.toString().trim().length() > 0){
            category = String.format("%s%s%s", "%", category, "%");
            where += "and o.category  like :category ";
            param = new ParameterMap();
            param.setName("category");
            param.setValue(category);
            params.add(param);
        }
                
        if (userName != null && userName.toString().trim().length() > 0){
            userName = String.format("%s%s%s", "%", userName, "%");
            where += "and o.createBy like :userName ";
            param = new ParameterMap();
            param.setName("userName");
            param.setValue(userName);
            params.add(param);
        }
  
        if (customerName != null && customerName.toString().trim().length() > 0){
            customerName = String.format("%s%s%s", "%", customerName, "%");
            where += "and o.customerName like :customerName ";
            param = new ParameterMap();
            param.setName("customerName");
            param.setValue(customerName);
            params.add(param);
        }
        
        String orderBy = "order by o.createDate desc";
        query = select+where+orderBy;
        Query q = em.createQuery(query);
        
   	if (category==null)
            category="";
        
   	if (userName==null)
            userName="";

   	if (customerName==null)
            customerName="";

        for (ParameterMap parm:params) {
            q.setParameter(parm.getName(), parm.getValue());
        }
                
        q.setMaxResults(2000);
        return q.getResultList();
        
    }

       public List<AuditLog> findCustomerLogEntities(Date fromDate, Date toDate, String category, String userName, Integer customerId) {

        String query;
        String select="SELECT object(o) FROM AuditLog o ";
  
        String where="WHERE o.customerId = " + customerId + " ";
        params = new ArrayList<ParameterMap>();
        ParameterMap param = new ParameterMap();
        
        if (fromDate != null){
            where += "and o.createDate >= :fromDate ";
            param = new ParameterMap();
            param.setName("fromDate");
            param.setValue(fromDate);
            params.add(param);
        }
        
        if (toDate != null){
            where += "and o.createDate  <= :toDate ";
            param = new ParameterMap();
            param.setName("toDate");

            Calendar date = Calendar.getInstance();
            date.setTime(toDate);
            date.add(Calendar.DATE, 1);
            param.setValue(date.getTime());
            params.add(param);
        }
        
        if (category != null && category.toString().trim().length() > 0){
            category = String.format("%s%s%s", "%", category, "%");
            where += "and o.category  like :category ";
            param = new ParameterMap();
            param.setName("category");
            param.setValue(category);
            params.add(param);
        }
                
        if (userName != null && userName.toString().trim().length() > 0){
            userName = String.format("%s%s%s", "%", userName, "%");
            where += "and o.createBy like :userName ";
            param = new ParameterMap();
            param.setName("userName");
            param.setValue(userName);
            params.add(param);
        }
        
        String orderBy = "order by o.createDate desc";
        query = select+where+orderBy;
        Query q = em.createQuery(query);
        
   	if (category==null)
            category="";
        
   	if (userName==null)
            userName="";

        for (ParameterMap parm:params) {
            q.setParameter(parm.getName(), parm.getValue());
        }
                
        return q.getResultList();
        
    }

    public AuditLog findAccessLog(Integer id) {
        return em.find(AuditLog.class, id);
    }

    public int getAccessLogCount() {
        Query q = em.createQuery("select count(o) from AuditLog as o");
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public List<AuditLog> findUsersByName(String nameKeyword) {
        Query q = em.createQuery("SELECT object(o) FROM AuditLog o WHERE o.createBy like :name ");
        q.setParameter("name", "%" + nameKeyword + "%");
        return q.getResultList();
       
    }
    
    public List<ParameterMap> getParams() {
        return params;
    }

    public void setParms(List<ParameterMap> params) {
        this.params = params;
    }
    
}
