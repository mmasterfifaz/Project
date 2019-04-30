/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.core.model.value.admin.AssignmentListMonitorValue;
import com.maxelyz.core.model.value.admin.CriteriaListValue;
import com.maxelyz.core.model.value.admin.UserAssignmentValue;
import com.maxelyz.utils.JSFUtil;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.persistence.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Oat
 */
@Transactional
public class AssignmentDAO {

    private static Logger log = Logger.getLogger(AssignmentDAO.class);
    private static String ASSIGNMENT_AVERAGE = "average";
    private static String ASSIGNMENT_CUSTOM = "custom";
    private static String ASSIGNMENT_POOLING = "pooling";
    private static String ASSIGNMENT_POOLINGCUSTOM = "poolingcustom";
    private static String ASSIGNMENT_POOLINGDAILY = "poolingdaily";    
    private static String ASSIGNMENT_AUTODIALER = "autodialer";
    private static String MODE_MANAGER = "manager";
    private static String MODE_SUPERVISOR = "supervisor";
    private int actualNoUser;
    private int actualNoCustomer;
    
    @PersistenceContext
    private EntityManager em;

    public void create(Assignment assignment) {
        em.persist(assignment);
    }

    public void createAssignmentSupervisor(AssignmentSupervisor assignmentSupervisor) {
        em.persist(assignmentSupervisor);

    }
    
    public synchronized void keepLatestAssignmentDetailFromSupervisorDetail(Integer assignmentID, int marketingID, Integer campaignID) {
        try {
            String sqlCmd = "update newAD " 
                        + "set newAD.status = oldAD.status, newAD.followupsale_date = oldAD.followupsale_date, newAD.followupsale_reason_id = oldAD.followupsale_reason_id, " 
                        + "newAD.call_attempt = oldAD.call_attempt, newAD.dmc = oldAD.dmc, newAD.call_attempt2 = oldAD.call_attempt2, newAD.sale_attempt = oldAD.sale_attempt, " 
                        + "newAD.customer_name = oldAD.customer_name, newAD.customer_type = oldAD.customer_type, newAD.dob = oldAD.dob, newAD.sale_result = oldAD.sale_result, " 
                        + "newAD.contact_result = oldAD.contact_result, newAD.contact_remark = oldAD.contact_remark, newAD.contact_date = oldAD.contact_date, " 
                        + "newAD.new_list = oldAD.new_list, newAD.contact_status = oldAD.contact_status, newAD.latest_contact_status = oldAD.latest_contact_status, "
                        + "newAD.contact_result_id = oldAD.contact_result_id, newAD.remark = oldAD.remark " 
                        + "from assignment_detail newAD " 
                        + "join assignment_supervisor_detail asd on asd.customer_id = newAD.customer_id "
                        + "join assignment_supervisor s on s.id = asd.assignment_supervisor_id "
                        + "join assignment a on a.id = s.assignment_id "
                        + "join assignment_detail oldAD on newAD.customer_id = oldAD.customer_id "                     
                        + "where newAD.assignment_id = ?1 and asd.unassign is null and asd.assign = 1 "
                        + "and a.marketing_id = ?2 and a.campaign_id = ?3 and asd.reset_new_list = 0 "                    
                        + "and oldAD.id in " 
                        + "(SELECT MAX(ad.id) as adid " 
                        + "FROM assignment_detail ad " 
                        + "JOIN assignment a ON ad.assignment_id = a.id " 
                        + "WHERE a.marketing_id = ?2 and ad.unassign is not null and ad.status <> 'closed' " 
                        + "GROUP BY ad.customer_id)";        
            
            Query q = em.createNativeQuery(sqlCmd);
            q.setParameter(1, assignmentID);
            q.setParameter(2, marketingID);
            q.setParameter(3, campaignID);

            q.executeUpdate();
        } catch (Exception e) {
            log.error(e);
        }        
    }
    
    public synchronized void keepLatestAssignmentDetail(Integer assignmentID, int marketingID) {
        try {
            String sqlCmd = "update newAD " 
                        + "set newAD.status = oldAD.status, newAD.followupsale_date = oldAD.followupsale_date, newAD.followupsale_reason_id = oldAD.followupsale_reason_id, " 
                        + "newAD.call_attempt = oldAD.call_attempt, newAD.dmc = oldAD.dmc, newAD.call_attempt2 = oldAD.call_attempt2, newAD.sale_attempt = oldAD.sale_attempt, " 
                        + "newAD.customer_name = oldAD.customer_name, newAD.customer_type = oldAD.customer_type, newAD.dob = oldAD.dob, newAD.sale_result = oldAD.sale_result, " 
                        + "newAD.contact_result = oldAD.contact_result, newAD.contact_remark = oldAD.contact_remark, newAD.contact_date = oldAD.contact_date, " 
                        + "newAD.new_list = oldAD.new_list, newAD.contact_status = oldAD.contact_status, newAD.latest_contact_status = oldAD.latest_contact_status, "
                        + "newAD.contact_result_id = oldAD.contact_result_id, newAD.remark = oldAD.remark " 
                        + "from assignment_detail newAD " 
                        + "join assignment_detail oldAD on newAD.customer_id = oldAD.customer_id " 
                        + "where newAD.assignment_id = ?1 and oldAD.id in " 
                        + "(SELECT MAX(ad.id) as adid " 
                        + "FROM assignment_detail ad " 
                        + "JOIN assignment a ON ad.assignment_id = a.id " 
                        + "WHERE a.marketing_id = ?2 and ad.unassign is not null and ad.status <> 'closed' " 
                        + "GROUP BY ad.customer_id)";
        
            Query q = em.createNativeQuery(sqlCmd);
            q.setParameter(1, assignmentID);
            q.setParameter(2, marketingID);

            q.executeUpdate();
        } catch (Exception e) {
            log.error(e);
        }        
    }

//    public void create(String assignmentMode, String assignmentType, Campaign campaign, Assignment assignment, List<UserAssignmentValue> assignmentUsers,
//            int marketingId, Integer norecord, String listType, String assignmentAlgo, List<Object[]> customerList, boolean resetNewList) {
    public void create(String assignmentMode, String assignmentType, Campaign campaign, Assignment assignment, List<UserAssignmentValue> assignmentUsers,
            int marketingId, Integer norecord, String listType, String assignmentAlgo, boolean resetNewList, String uniqueID) {
        this.create(assignment);        
        
        // CREATE ALL RECORD FOR ASSIGNMENT
//        this.assignMarketing(assignmentMode, assignmentType, campaign, assignment, assignmentUsers, marketingId, norecord, listType, assignmentAlgo, customerList, resetNewList);  
        this.assignMarketing(assignmentMode, assignmentType, campaign, assignment, assignmentUsers, marketingId, norecord, listType, assignmentAlgo, resetNewList, uniqueID);  
        
        // KEEP OLD CONTACT STATUS FROM LATEST ASSIGNMENT DETAIL
        if(JSFUtil.getUserSession().getUserGroup().getRole().contains("CampaignManager")) {
            if(!resetNewList && listType.equals("oldList")) {
                keepLatestAssignmentDetail(assignment.getId(), marketingId);
            }
        } else if(JSFUtil.getUserSession().getUserGroup().getRole().contains("Supervisor")) {
            keepLatestAssignmentDetailFromSupervisorDetail(assignment.getId(), marketingId, campaign.getId());
        }
                
        assignment.setNoUser(actualNoUser);
        assignment.setNoCustomer(actualNoCustomer);
        this.edit(assignment);
    }

    public void edit(Assignment assignment) {
        assignment = em.merge(assignment);
    }

    public void editAssignmentSupervisorDetail(AssignmentSupervisorDetail asd){
        em.merge(asd);
    }
    
    public void editAssignmentSupervisorDetail1(AssignmentSupervisorDetail asd){
        try{
            Query q = em.createNativeQuery("update assignment_supervisor_detail set assign = ?1, assign_date = ?2 where id = ?3");
            q.setParameter(1, asd.getAssign());
            q.setParameter(2, asd.getAssignDate());
            q.setParameter(3, asd.getId());

            q.executeUpdate();
        }catch(Exception e){
            log.error(e);
        }
    }
    
    public void editAssignmentSupervisor(AssignmentSupervisor as){
        em.merge(as);
    }
    
    public void editAssignmentSupervisor1(AssignmentSupervisor as){
        try{
            Query q = em.createNativeQuery("update assignment_supervisor set no_used = ?1 where id = ?2");
            q.setParameter(1, as.getNoUsed());
            q.setParameter(2, as.getId());

            q.executeUpdate();
        }catch(Exception e){
            log.error(e);
        }
    }
    
    public void editAssignmentPooling(AssignmentPooling assignmentPooling) {
        assignmentPooling = em.merge(assignmentPooling);
    }

    public void editAssignmentPooling1(AssignmentPooling assignmentPooling) {
        try{
            Query q = em.createNativeQuery("update assignment_pooling set no_used = ?1, enable = ?2 where id = ?3");
            q.setParameter(1, assignmentPooling.getNoUsed());
            q.setParameter(2, assignmentPooling.getEnable());
            q.setParameter(3, assignmentPooling.getId());

            q.executeUpdate();
        }catch(Exception e){
            log.error(e);
        }
    }

    public void updateAssignmentSupervisorDetail(Assignment assignment, UserAssignmentValue u){
        if(assignment != null) { 
            Query q = em.createNativeQuery("select asd.assignment_supervisor_id, count(asd.id) "
                    + "from assignment_supervisor_detail asd "
                    + "join assignment_detail ad on asd.customer_id = ad.customer_id "
                    + "where ad.assignment_id = ?1 and ad.user_id = ?2 and asd.assign = 0 and asd.unassign is null "
                    + "group by asd.assignment_supervisor_id");  
            q.setParameter(1, assignment.getId());
            q.setParameter(2, u.getUser().getId());
            List<Object[]> l = q.getResultList();       

            Query q2 = em.createNativeQuery("update assignment_supervisor_detail set assign = 1, assign_date = getdate() "
                    + "from assignment_supervisor_detail asd join assignment_detail ad on asd.customer_id = ad.customer_id "
                    + "where ad.assignment_id = ?1  and ad.user_id = ?2 and asd.unassign is null");
            q2.setParameter(1, assignment.getId());
            q2.setParameter(2, u.getUser().getId());
            try {
                q2.executeUpdate();
            } catch(Exception e) {
                e.printStackTrace();
            }

            Query q3 = em.createQuery("update AssignmentSupervisor set noUsed = noUsed + ?4 where id = ?5");
            int ParentAssignmentSupervisorId = 0;
            for (Object[] obj : l) {
                q3.setParameter(4, obj[1]); //no of assign record
                q3.setParameter(5, obj[0]); //assignmentSupervisorId
                try{
                    q3.executeUpdate();
                }catch(Exception e){
                    e.printStackTrace();
                }
                ParentAssignmentSupervisorId = Integer.parseInt(obj[0].toString());
            }

            Query q4 = em.createQuery("select object(s) from AssignmentSupervisor as s where s.id = ?6");
            q4.setParameter(6, ParentAssignmentSupervisorId);
            AssignmentSupervisor assignmentSupervisor = (AssignmentSupervisor) q4.getSingleResult();
            assignment.setParentAssignmentSupervisorId(assignmentSupervisor.getAssignmentId().getId()); 
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        Assignment assignment;
        try {
            assignment = em.getReference(Assignment.class, id);
            assignment.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The assignment with id " + id + " no longer exists.", enfe);
        }
        em.remove(assignment);
    }

    public List<Assignment> findAssignmentEntities() {
        return findAssignmentEntities(true, -1, -1);
    }

    public List<Assignment> findAssignmentEntities(int maxResults, int firstResult) {
        return findAssignmentEntities(false, maxResults, firstResult);
    }

    private List<Assignment> findAssignmentEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from Assignment as o order by assignDate desc");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Assignment findAssignment(Integer id) {
        return em.find(Assignment.class, id);
    }

    public int getAssignmentCount() {
        return ((Long) em.createQuery("select count(o) from Assignment as o").getSingleResult()).intValue();
    }

    public List<Product> findAssignmentProduct(int assignmentId) {
        Query q = em.createQuery("select p from Assignment a join a.assignmentProductCollection ap join ap.product p where a.id = ?1");
        q.setParameter(1, assignmentId);
        return q.getResultList();
    }

    public List<Assignment> findAssignmentEntitiesByCampaign(int campaignId) {
        Query q = em.createQuery("select object(o) from Assignment as o where o.campaign.id=?1 order by o.assignDate desc");
        q.setParameter(1, campaignId);
        q.setMaxResults(500);
        return q.getResultList();
    }
    
    public List<Assignment> findAssignmentEntitiesByAdministrator(Campaign campaign) {
        Query q = em.createQuery("select object(o) from Assignment as o where o.campaign.id=?1"
                + " order by o.assignDate desc");
        q.setParameter(1, campaign.getId().intValue());
        return q.getResultList();
    }
    
    public List<Assignment> findAssignmentEntitiesByManager(Campaign campaign, Users user) {
        Query q = em.createQuery("select object(o) from Assignment as o where o.campaign.id=?1"
                + " and (o.createByUser.id=?2 or o.parentAssignmentSupervisorId in (select o.id from Assignment as o where o.campaign.id=?1 and o.createByUser.id=?2))"
                + " order by o.assignDate desc");
        q.setParameter(1, campaign.getId().intValue());
        q.setParameter(2, user.getId().intValue());
        return q.getResultList();
    }
    
    public List<Assignment> findAssignmentEntitiesByUser(Campaign campaign, Users user) {
        Query q = em.createQuery("select object(o) from Assignment as o where o.campaign.id=?1 and o.createByUser.id=?2"
                + " order by o.assignDate desc");
        q.setParameter(1, campaign.getId().intValue());
        q.setParameter(2, user.getId().intValue());
        return q.getResultList();
    }
    
    public List<Assignment> findAssignmentEntitiesByCampaignAndMarketing(int campaignId, int ugId) {
        String where = findMarketingByUserGroup(ugId);
        Query q = em.createQuery("select object(o) from Assignment as o where o.campaign.id=?1 " + where + " order by o.assignDate desc ");
        q.setParameter(1, campaignId);
        q.setMaxResults(500);
        return q.getResultList();
    }
    
    private String findMarketingByUserGroup(Integer userGroupId) {
        List<Marketing> result = new ArrayList<Marketing>();
        List<MarketingUserGroup> mug = findMarketingUserGroupByUserGroup(userGroupId);
        String whereId = " ";
        if (mug != null && mug.size() > 0) {
            whereId = " and o.marketing.id in (";
            for (MarketingUserGroup m : mug) {
                whereId += m.getMarketingUserGroupPK().getMarketingId() + ",";
            }
            whereId = whereId.substring(0, whereId.length() - 1);
            whereId += ") ";
        } else {
            whereId = " ";
        }
        return whereId;
    }
    
    private List<MarketingUserGroup> findMarketingUserGroupByUserGroup(Integer userGroupId) {
        Query q = em.createQuery("select object(mu) from MarketingUserGroup as mu where mu.marketingUserGroupPK.userGroupId = :userGroupId");
        q.setParameter("userGroupId", userGroupId);
        return q.getResultList();
    }
    
    private List<Marketing> findMarketingGroupById(String where) {
        String sql = "select object(o) from Marketing as o where o.id in " + where + " and o.enable = true order by o.createDate desc";
        Query q = em.createQuery(sql);
        return q.getResultList();
    }
    
    public List<Assignment> findEnableAssignmentPooling(int campaignId) {
        Query q = em.createQuery("select object(o) from Assignment as o where o.campaign.id=?1 and o.createByUser.id=?2 and o.id in "
                + "(select assignment.id from AssignmentPooling p where p.enable = true "
                + "group by p.assignment.id) order by o.assignDate desc");
        q.setParameter(1, campaignId);
        q.setParameter(2, JSFUtil.getUserSession().getUsers().getId());
        q.setMaxResults(500);
        return q.getResultList();
    }
    
    public List<Assignment> findEnableAssignmentPoolingByMarketing(int campaignId, int ugId) {
        String where  = findMarketingByUserGroup(ugId);
        Query q = em.createQuery("select object(o) from Assignment as o where o.campaign.id=?1 and o.createByUser.id=?2 and o.id in "
                + "(select assignment.id from AssignmentPooling p where p.enable = true "
                + "group by p.assignment.id) " + where + "order by o.assignDate desc");
        q.setParameter(1, campaignId);
        q.setParameter(2, JSFUtil.getUserSession().getUsers().getId());
        q.setMaxResults(500);
        return q.getResultList();
    }
    
    public List<Marketing> findUsedMarketingByCampaign(int campaignId, String listType) {
        Query q;
        if(listType.equals("allList")) {
            q = em.createQuery("select distinct a.marketing from Assignment a "
                    + "where a.marketing.startDate <= ?3 and a.marketing.endDate >= ?3 and a.campaign.id=?1 and a.marketing.enable = true and a.marketing.status = true order by a.marketing.code");
            } else {
                q = em.createQuery("select distinct m from Assignment a "
                        + "join a.marketing m "
                        + "where m.startDate <= ?3 and m.endDate >= ?3 and a.campaign.id=?1 and m.enable = true and m.status = true and m.id in "
                        + "(select mc.marketingCustomerPK.marketingId "
                        + "from MarketingCustomer mc where mc.newList = ?2 group by mc.marketingCustomerPK.marketingId) "
                        + " order by a.marketing.code");
                q.setParameter(2, listType.equals("newList") ? true : listType.equals("oldList") ? false : null);
        }
        q.setParameter(1, campaignId);
        q.setParameter(3, JSFUtil.toDateWithoutTime(new Date()));
        return q.getResultList();
    }

    public List<UserAssignmentValue> findUserAssignmentByAssignmentId(int assignmentId, String assignmentMode, String assignmentType) {
        if(assignmentMode.equals("supervisor") && (assignmentType.equals(ASSIGNMENT_AVERAGE) || assignmentType.equals(ASSIGNMENT_CUSTOM))){
            String sql = "select new " + UserAssignmentValue.class.getName() + " (u, count(a) as records) "
                    + "from AssignmentDetail a join a.users u "
                    + "where a.assignmentId.id = ?1 "
                    + "group by u,u.name order by u.name";
            Query q = em.createQuery(sql);
            q.setParameter(1, assignmentId);
            return q.getResultList();
        } else if(assignmentMode.equals("supervisor") && (assignmentType.equals(ASSIGNMENT_POOLING) || assignmentType.equals(ASSIGNMENT_POOLINGCUSTOM)  || assignmentType.equals(ASSIGNMENT_POOLINGDAILY))){
            String sql = "select new " + UserAssignmentValue.class.getName() + " (u, p.noCustomer as records) "
                    + "from AssignmentPooling p join p.user u  "
                    + "where p.assignment.id = ?1 order by u.name";
            Query q = em.createQuery(sql);
            q.setParameter(1, assignmentId);
            return q.getResultList();
        }else {
            String sql = "select new " + UserAssignmentValue.class.getName() + " (u, a.noCustomer as records) "
            + "from AssignmentSupervisor a join a.userId u  "
            + "where a.assignmentId.id = ?1 order by u.name";
            Query q = em.createQuery(sql);
            q.setParameter(1, assignmentId);
            return q.getResultList();
        }
    }
          
    public String createSQLCmd(String listType, List<CriteriaListValue> selectedCriteria, List<CriteriaListValue> selectedAdvanceCriteria) {
        
        String strWhere = "";        
        if (listType.equals("newList")) {
            strWhere = "and mc.new_list = 1 ";
        } else if (listType.equals("oldList")) {
            strWhere = "and mc.new_list = 0 ";
        }
            
        if (selectedCriteria != null) {
            for (CriteriaListValue obj : selectedCriteria) {
                if (obj.isSelected()) { 
                    if(obj.getMappingField().equals("customer.name")) {
                        strWhere += "and cus.name like '%" + obj.getKeyword().trim() + "%' ";
                    } else if(obj.getMappingField().equals("customer.surname")) {
                        strWhere += "and cus.surname like '%" + obj.getKeyword().trim() + "%' ";
                    } else if(obj.getMappingField().equals("customer.dob")) {
                            int ageFrom = Integer.parseInt(obj.getFromValue());
                            int ageTo = Integer.parseInt(obj.getToValue());
                            strWhere += "and (DATEDIFF(Year,cus.dob,getdate()) >= " + ageFrom + " and DATEDIFF(Year,cus.dob,getdate()) <= " +ageTo + " )";
                    } else if(obj.getMappingField().equals("customer.gender")) {
                        strWhere += "and cus.gender like '" + obj.getKeyword() + "%' "; 
                    } else if(obj.getMappingField().equals("customer.weight")) {
                            double weightFrom = Double.parseDouble(obj.getFromValue());
                            double weightTo = Double.parseDouble(obj.getToValue());
                            if(weightFrom <= weightTo){
                                strWhere += "and cus.weight >= " + weightFrom + " and and cus.weight <= " + weightTo + " )";
                            }
                    } else if(obj.getMappingField().equals("customer.height")) {
                            double heightFrom = Double.parseDouble(obj.getFromValue());
                            double heightTo = Double.parseDouble(obj.getToValue());
                            if(heightFrom <= heightTo){
                                strWhere += "and cus.height >= " + heightFrom + " and and cus.height <= " + heightTo + " )";
                            }
                    } else if(obj.getMappingField().equals("customer.home_phone")) {
                        if(obj.getKeyword().equals("notspecific")) {
                            strWhere += "and (cus.home_phone IS NOT NULL and cus.home_phone <> '') ";
                        } else if(obj.getKeyword().equals("upc")) {
                            strWhere += "and (cus.home_phone like '053%' or cus.home_phone like '054%' or cus.home_phone like '055%' or cus.home_phone like '056%' "
                                    + "or cus.home_phone like '032%' or cus.home_phone like '034%' or cus.home_phone like '035%' or cus.home_phone like '036%' or cus.home_phone like '037%' or cus.home_phone like '038%' or cus.home_phone like '039%' "
                                    + "or cus.home_phone like '042%' or cus.home_phone like '043%' or cus.home_phone like '044%' or cus.home_phone like '045%' "
                                    + "or cus.home_phone like '073%' or cus.home_phone like '074%' or cus.home_phone like '075%' or cus.home_phone like '076%' or cus.home_phone like '077%') ";
                        } else {
                            strWhere += "and cus.home_phone like '" + obj.getKeyword().trim() + "%' ";
                        }
                     } else if(obj.getMappingField().equals("customer.office_phone")) {
                        if(obj.getKeyword().equals("notspecific")) {
                            strWhere += "and (cus.office_phone IS NOT NULL and cus.office_phone <> '') ";
                        } else if(obj.getKeyword().equals("upc")) {
                            strWhere += "and (cus.office_phone like '053%' or cus.office_phone like '054%' or cus.office_phone like '055%' or cus.office_phone like '056%' "
                                    + "or cus.office_phone like '032%' or cus.office_phone like '034%' or cus.office_phone like '035%' or cus.office_phone like '036%' or cus.office_phone like '037%' or cus.office_phone like '038%' or cus.office_phone like '039%' "
                                    + "or cus.office_phone like '042%' or cus.office_phone like '043%' or cus.office_phone like '044%' or cus.office_phone like '045%' "
                                    + "or cus.office_phone like '073%' or cus.office_phone like '074%' or cus.office_phone like '075%' or cus.office_phone like '076%' or cus.office_phone like '077%') ";
                        } else {
                            strWhere += "and cus.office_phone like '" + obj.getKeyword().trim() + "%' ";
                        }
                    } else if(obj.getMappingField().equals("customer.mobile_phone")) {
                        if(obj.getKeyword().equals("notspecific")) {
                            strWhere += "and (cus.mobile_phone IS NOT NULL and cus.mobile_phone <> '') ";
                        } else {                            
                            strWhere += "and cus.mobile_phone like '" + obj.getKeyword().trim() + "%' ";
                        }
                    } else if(obj.getMappingField().equals("customer.privilege")) {
                        strWhere += "and cus.privilege like '" + obj.getKeyword().trim() + "%' ";
                    } else if(obj.getMappingField().equals("customer.current_address_line8")) {
                        strWhere += "and cus.current_address_line8 like '" + obj.getKeyword().trim() + "%' ";
                    } else if(obj.getMappingField().equals("customer.current_province_name")) {
                        strWhere += "and cus.current_province_name like '" + obj.getKeyword().trim() + "%' ";
                    } else if(obj.getMappingField().equals("customer.current_district_name")) {
                        strWhere += "and cus.current_district_name like '" + obj.getKeyword().trim() + "%' ";
                    } else if(obj.getMappingField().equals("customer.car_brand")) {
                        strWhere += "and cus.car_brand like '%" + obj.getKeyword().trim() + "%' ";
                    } else if(obj.getMappingField().equals("customer.car_model")) {
                        strWhere += "and cus.car_model like '%" + obj.getKeyword().trim() + "%' ";
                    } else if(obj.getMappingField().equals("customer.car_year")) {
                            int carFromYear = Integer.parseInt(obj.getFromValue());
                            int carToYear = Integer.parseInt(obj.getToValue());

                            if(carFromYear <= carToYear){
                                strWhere += "and cus.car_year >= " + carFromYear + " and cus.car_year <= " + carToYear + " ";
                            }
                    }
                }
            }
        }
        
        if (selectedAdvanceCriteria != null) {
            for (CriteriaListValue obj : selectedAdvanceCriteria) {
                if (obj.isSelected()) { 
                    String fieldName = "";
                    fieldName = StringUtils.substring(obj.getMappingField(), (obj.getMappingField().lastIndexOf(".")) + 1, obj.getMappingField().length());

                    if(obj.getType().equals("Date")) {
                        try {
                            SimpleDateFormat outputFormat = new SimpleDateFormat(obj.getPattern());    
                            String outFromDate = outputFormat.format(obj.getFromValueDate());
                            
                            if(obj.getCriteria().equals("on")) {
                                strWhere += "and " + fieldName + " = '" + outFromDate + "' ";
                            } else if(obj.getCriteria().equals("before")) {
                                strWhere += "and " + fieldName + " < '" + outFromDate + "' ";
                            } else if(obj.getCriteria().equals("after")) {
                                strWhere += "and " + fieldName + " > '" + outFromDate + "' ";
                            } else if(obj.getCriteria().equals("between")) {                                
                                String outToDate = outputFormat.format(obj.getToValueDate());                                
                                strWhere += "and " + fieldName + " between '" + outFromDate + "' and '" + outToDate + "' "; 
                            } else if(obj.getCriteria().equals("notEqual")) {
                                strWhere += "and " + fieldName + " <> '" + outFromDate + "' ";
                            }
                        } catch (Exception exn) {
                            exn.getMessage();
                        }
                    } else if (obj.getType().equals("Number")) {
                        if(!obj.getCriteria().isEmpty()) {
                            try {
                                Double fromValue = Double.parseDouble(obj.getFromValue());
                                if(obj.getCriteria().equals("between")) {
                                    Double toValue = Double.parseDouble(obj.getToValue());
                                     strWhere += "and CAST("+fieldName+" as DECIMAL(9,4)) between " + fromValue + " and " + toValue + " ";
                                     //and flexfield8 BETWEEN 10000 AND 25000
                                } else {
                                    strWhere += "and CAST("+fieldName+" as DECIMAL(9,4)) " + obj.getCriteria().trim() + " " + fromValue +" ";
                                }                              
                            } catch (Exception exn) {
                                exn.getMessage();
                            }                            
                        }
                    } else if (obj.getType().equals("String")) {
                        strWhere += "and " + fieldName + " like '%"+obj.getKeyword().trim()+"%' ";
                    }          
                }
            }
        }      
        return strWhere;
    }
    
    private String listToString(List<Integer> l) {
        StringBuilder sb = new StringBuilder();
        for (int id : l) {
            sb.append(id);
            sb.append(',');
        }
        return sb.toString();
    }
    
    public String createSQLCmdContact(String listType,boolean contactStatusAssigned, boolean contactStatusViewed, boolean contactStatusOpened, 
                                      boolean contactStatusFollowup, List<Integer> openIds, List<Integer> followupIds) {
        
        String stmt = "";
        
        if(!listType.equals("newList") && (contactStatusAssigned || contactStatusViewed || contactStatusOpened  || openIds.size() > 0 || contactStatusFollowup || followupIds.size() > 0)) {
            stmt = "and cus.id in "
                + "(SELECT contactAD.customer_id FROM "
                + "(SELECT DISTINCT ad.customer_id, MAX(ad.id) as id "
                + "FROM assignment_detail ad "
                + "JOIN assignment a ON ad.assignment_id = a.id "
                + "WHERE a.marketing_id = ?1 and ad.unassign is not null and ad.status <> 'closed' "
                + "GROUP BY ad.customer_id) oldAD "
                + "JOIN "
                + "(SELECT * FROM assignment_detail) contactAD "                    
                + "on contactAD.id = oldAD.id "
                + "WHERE contactAD.unassign is not null ";
 
            String followupStr = this.listToString(followupIds);
            String openStr = this.listToString(openIds);
            
            if (contactStatusAssigned || contactStatusViewed) {
                String status = "";
                status += contactStatusAssigned ? "'assigned'," : "";           
                status += contactStatusViewed ? "'viewed'," : "";

                status = status.substring(0, status.length() - 1);
                stmt += "and (contactAD.status in (" + status + ") ";
            }
            if (contactStatusOpened || openStr.length() > 0) { 
                if (contactStatusAssigned || contactStatusViewed) {
                    stmt += "or ";
                } else {
                    stmt += "and ";
                }
                if (contactStatusFollowup || followupStr.length() > 0) {
                    stmt += "( ";
                }

                stmt += "(contactAD.status = 'opened' ";
                if (openStr.length() > 0) {
                    stmt += "and contactAD.contact_result_id in (";
                    if(openStr.length() > 0 ) {
                        openStr = openStr.substring(0, openStr.length() - 1);
                        stmt += openStr;
                    } 
                    stmt += ") ";                
                }
                stmt += ") ";
            }

            if (contactStatusFollowup || followupStr.length() > 0) {                
                if (contactStatusAssigned || contactStatusViewed || contactStatusOpened || openStr.length() > 0) {
                    stmt += "or ";
                } else {
                    stmt += "and ";
                }
                stmt += "(contactAD.status = 'followup' ";
                if (followupStr.length() > 0) {
                    stmt += "and contactAD.contact_result_id in (";
                    if(followupStr.length() > 0) {
                        followupStr = followupStr.substring(0, followupStr.length() - 1);
                        stmt += followupStr;
                    }
                    stmt += ") ";                
                }
                stmt += ") ";
                if (contactStatusOpened || openStr.length() > 0) {
                    stmt += ") ";    
                }
            }
            if (contactStatusAssigned || contactStatusViewed) {
                stmt += ") ";
            }
            stmt += ") ";
        }
        return stmt;
    }
    
    public int countUnAssignedCustomer(int marketingId, String listType, List<CriteriaListValue> selectedCriteria, 
                                          List<CriteriaListValue> selectedAdvanceCriteria, boolean contactStatusAssigned, boolean contactStatusViewed, 
                                          boolean contactStatusOpened, boolean contactStatusFollowup, List<Integer> openIds, List<Integer> followupIds, 
                                          String uniqueID) {
        try {
            clearReferencForAssign(uniqueID);
//            String refUserAssign = JSFUtil.getUserSession().getUsers().getLoginName();
            String SELECT = "update customer set reference_no_2 = ?2 "
                        + "where id in (select cus.id from marketing_customer mc "
                        + "join customer cus on mc.customer_id = cus.id and isnull(cus.op_out,0) <> 1 "
                        + "where mc.marketing_id = ?1 and mc.assign = 0 ";

            String CRITERIA = createSQLCmd(listType, selectedCriteria, selectedAdvanceCriteria);

            String CRITERIACONTACT = createSQLCmdContact(listType, contactStatusAssigned, contactStatusViewed, contactStatusOpened, contactStatusFollowup, openIds, followupIds);

            String FULLQUERY = SELECT + CRITERIA + CRITERIACONTACT + ")";
    //        System.out.println(SELECT + CRITERIA + CRITERIACONTACT);

            Query q = em.createNativeQuery(FULLQUERY);        
            q.setParameter(1, marketingId); 
            q.setParameter(2, uniqueID);        
            q.executeUpdate();
            
            Query q2 = em.createNativeQuery("select count(id) from customer where reference_no_2 = '"+uniqueID+"' ");
//            int test = ((BigInteger) em.createNativeQuery("select count(id) from customer where reference_no_2 = '"+uniqueID+"' ").getSingleResult()).intValue(); 
            int cntList = ((Integer) q2.getSingleResult()).intValue();
            return cntList; 
        } catch (Exception ex) {            
            return 0;
        }
    }
    
//    public List<Object[]> findUnAssignedCustomer(int marketingId, String listType, List<CriteriaListValue> selectedCriteria, 
//                                              List<CriteriaListValue> selectedAdvanceCriteria, boolean contactStatusAssigned, boolean contactStatusViewed, 
//                                              boolean contactStatusOpened, boolean contactStatusFollowup, List<Integer> openIds, List<Integer> followupIds) {
        
//        String SELECT = "select cus.* from marketing_customer mc "
//                    + "join customer cus on mc.customer_id = cus.id and isnull(cus.op_out,0) <> 1 "
//                    + "where mc.marketing_id = ?1 and mc.assign = 0 ";
//        
//        String CRITERIA = createSQLCmd(listType, selectedCriteria, selectedAdvanceCriteria);
//        
//        String CRITERIACONTACT = createSQLCmdContact(listType, contactStatusAssigned, contactStatusViewed, contactStatusOpened, contactStatusFollowup, openIds, followupIds);
//        
////        System.out.println(SELECT + CRITERIA + CRITERIACONTACT);
//        
//        Query q = em.createNativeQuery(SELECT + CRITERIA + CRITERIACONTACT, Customer.class);        
//        q.setParameter(1, marketingId);        
//        List<Object[]> customers = q.getResultList();
//        return customers;
//    }
    
    public int countUnAssignedCustomerFromsup(int marketingId, Campaign campaign, String listType, List<CriteriaListValue> selectedCriteria, 
                                              List<CriteriaListValue> selectedAdvanceCriteria, boolean contactStatusAssigned, boolean contactStatusViewed, 
                                              boolean contactStatusOpened, boolean contactStatusFollowup, List<Integer> openIds, List<Integer> followupIds,
                                              String uniqueID) {
        try{
            clearReferencForAssign(uniqueID);
//            String refUserAssign = JSFUtil.getUserSession().getUsers().getLoginName();
//            String SELECT = "update customer set cus.reference_no_2 = '"+uniqueID+"' "
//                        + "where id in (select mc.customer_id from marketing_customer mc "
            String SELECT = "update customer set reference_no_2 = ?4 "
                    + "where id in (select cus.id from assignment_supervisor_detail asd "
                    + "join marketing_customer mc on asd.customer_id = mc.customer_id "
                    + "join customer cus on mc.customer_id = cus.id and isnull(cus.op_out,0) <> 1 "
                    + "join assignment_supervisor s on asd.assignment_supervisor_id = s.id "
                    + "join assignment a on s.assignment_id = a.id "
                    + "where mc.marketing_id = ?1 and a.campaign_id = ?3 and s.user_id = ?2 and asd.assign = 0 and asd.unassign is null ";

            String CRITERIA = createSQLCmd(listType, selectedCriteria, selectedAdvanceCriteria);

            String CRITERIACONTACT = createSQLCmdContact(listType, contactStatusAssigned, contactStatusViewed, contactStatusOpened, contactStatusFollowup, openIds, followupIds);

            String FULLQUERY = SELECT + CRITERIA + CRITERIACONTACT + ")";
    //        System.out.println(SELECT + CRITERIA + CRITERIACONTACT);

            Query q = em.createNativeQuery(FULLQUERY);        
            q.setParameter(1, marketingId);
            q.setParameter(2, JSFUtil.getUserSession().getUsers().getId());
            q.setParameter(3, campaign.getId());
            q.setParameter(4, uniqueID);
            q.executeUpdate();
            
            Query q2 = em.createNativeQuery("select count(id) from customer where reference_no_2 = '"+uniqueID+"' ");
            int cntList = ((Integer) q2.getSingleResult()).intValue();
            return cntList; 
        } catch (Exception ex) {            
            return 0;
        }
    }
    
//    public List<Object[]> findUnAssignedCustomerFromsup(int marketingId, Campaign campaign, String listType, List<CriteriaListValue> selectedCriteria, 
//                                              List<CriteriaListValue> selectedAdvanceCriteria, boolean contactStatusAssigned, boolean contactStatusViewed, 
//                                              boolean contactStatusOpened, boolean contactStatusFollowup, List<Integer> openIds, List<Integer> followupIds) {
//
//        String SELECT = "select cus.* from assignment_supervisor_detail asd "
//                    + "join marketing_customer mc on asd.customer_id = mc.customer_id "
//                    + "join customer cus on mc.customer_id = cus.id and isnull(cus.op_out,0) <> 1 "
//                    + "join assignment_supervisor s on asd.assignment_supervisor_id = s.id "
//                    + "join assignment a on s.assignment_id = a.id "
//                    + "where mc.marketing_id = ?1 and a.campaign_id = ?3 and s.user_id = ?2 and asd.assign = 0 and asd.unassign is null ";
//        
//        String CRITERIA = createSQLCmd(listType, selectedCriteria, selectedAdvanceCriteria);
//        
//        String CRITERIACONTACT = createSQLCmdContact(listType, contactStatusAssigned, contactStatusViewed, contactStatusOpened, contactStatusFollowup, openIds, followupIds);
//        
////        System.out.println(SELECT + CRITERIA + CRITERIACONTACT);
//        
//        Query q = em.createNativeQuery(SELECT + CRITERIA + CRITERIACONTACT, Customer.class);        
//        q.setParameter(1, marketingId);
//        q.setParameter(2, JSFUtil.getUserSession().getUsers().getId());
//        q.setParameter(3, campaign.getId());
//        List<Object[]> customers = q.getResultList();
//        return customers;
//    }
    
//    public List<Object[]> findUnAssignedCustomer(int marketingId, String listType, List<CriteriaListValue> selectedCriteria, List<CriteriaListValue> selectedAdvanceCriteria) {
//        
//        String SELECT = "select cus.* from marketing_customer mc "
//                    + "join customer cus on mc.customer_id = cus.id and isnull(cus.op_out,0) <> 1 "
//                    + "where mc.marketing_id = ?1 and mc.assign = 0 ";
//        
//        String CRITERIA = CreateSQLCmd(listType, selectedCriteria, selectedAdvanceCriteria);
//        
//        Query q = em.createNativeQuery(SELECT+CRITERIA, Customer.class);
//        q.setParameter(1, marketingId);        
//        List<Object[]> customers = q.getResultList();
//        return customers;
//        
//    }
    
//    public List<Object[]>  findUnAssignedCustomerFromsup(int marketingId, Campaign campaign, String listType, List<CriteriaListValue> selectedCriteria, List<CriteriaListValue> selectedAdvanceCriteria) {
//
//        String SELECT = "select cus.* from assignment_supervisor_detail asd "
//                    + "join marketing_customer mc on asd.customer_id = mc.customer_id "
//                    + "join customer cus on mc.customer_id = cus.id and isnull(cus.op_out,0) <> 1 "
//                    + "join assignment_supervisor s on asd.assignment_supervisor_id = s.id "
//                    + "join assignment a on s.assignment_id = a.id "
//                    + "where mc.marketing_id = ?1 and a.campaign_id = ?3 and s.user_id = ?2 and asd.assign = 0 and asd.unassign is null ";
//        
//        String CRITERIA = createSQLCmd(listType, selectedCriteria, selectedAdvanceCriteria);
//        
//        Query q = em.createNativeQuery(SELECT+CRITERIA, Customer.class);
//        q.setParameter(1, marketingId);
//        q.setParameter(2, JSFUtil.getUserSession().getUsers().getId());
//        q.setParameter(3, campaign.getId());
//        List<Object[]> customers = q.getResultList();
//        return customers;
//    }
    
    public boolean checkPoolingUser(int userId,int marketingId) {     
        Query q = em.createQuery("select object(o) from AssignmentPooling as o "
                + "join o.assignment a "
                + "where o.user.id = ?1 and a.marketing.id = ?2 and o.enable = true");
        q.setParameter(1, userId);
        q.setParameter(2, marketingId);
        
        if(q.getResultList().isEmpty())
            return true;
        else
            return false;
    }   
    
    public Customer findUnAssignedCustomer(Integer marketingId, Boolean newList) {
        String sql = "select object(cus) from MarketingCustomer mc "
                + "join mc.customer cus "
                + "where mc.marketingCustomerPK.marketingId = ?1 "
                + "and (mc.assign is null or mc.assign = false) ";
        
        if(newList != null){
            sql += "and mc.newList = :newList ";
        }
        Query q = em.createQuery(sql);
        q.setParameter(1, marketingId);
        
        if(newList != null){
            q.setParameter("newList", newList);
        }
        q.setMaxResults(1);
        List<Customer> list = q.getResultList();
        if(!list.isEmpty()){
            return (Customer) q.getResultList().get(0);
        }else{
            return null;
        }
    }

    public AssignmentSupervisorDetail findAssignmentSupervisorDetail(Integer userId, Integer marketingId, Integer campaignId, Boolean newList) {
        String sql = "select object(a) from AssignmentSupervisorDetail a "
                + "join a.assignmentSupervisorId b "
                + "join b.assignmentId c "
                + "join c.marketing d "
                + "join b.userId e "
                + "join c.campaign f "
                + "where d.id = :marketingId and e.id = :userId and f.id = :campaignId "
                + "and (a.assign is null or a.assign = false) "
                + "and a.unassign is null ";
        
        if(newList != null){
            sql += "and a.newList = :newList ";
        }
        Query q = em.createQuery(sql);
        q.setParameter("userId", userId);
        q.setParameter("marketingId", marketingId);
        q.setParameter("campaignId", campaignId);
        
        if(newList != null){
            q.setParameter("newList", newList);
        }
        q.setMaxResults(1);
        List<AssignmentSupervisorDetail> list = q.getResultList();
        if(!list.isEmpty()){
            return (AssignmentSupervisorDetail) q.getResultList().get(0);
        }else{
            return null;
        }
    }
    
    private synchronized void insertCampaignCustomer(Campaign campaign, Assignment assignment) {
        if(assignment != null) {
            String sql = "insert into campaign_customer (campaign_id, customer_id) "
                    + "select ?1, ad.customer_id "
                    + "from assignment_detail ad join assignment a on ad.assignment_id = a.id "
                    + "where ad.assignment_id = ?2 and a.campaign_id = ?1 "
                    + "and ad.customer_id not in (select customer_id from campaign_customer where campaign_id = ?1)";
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, campaign.getId());
            q.setParameter(2, assignment.getId());
            //q.setParameter(3, u.getUser().getId());
            try{
                q.executeUpdate();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public void assignProduct(Assignment assignment, List<Product> selectedProduct) {  
        StringBuilder sb = new StringBuilder();
        for (Product p : selectedProduct) {
            sb.append(p.getId());
            sb.append(',');
        }
        String productIds = sb.toString();
        if (productIds.length() > 0) {
            productIds = productIds.substring(0, productIds.length() - 1);

            String sql = "insert into assignment_product (assignment_id, product_id, product_code, product_name) "
                    + "select ?1, p.id, p.code, p.name from product p "
                    + "where p.id in ("+productIds+") ";
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, assignment.getId());
            try{
                q.executeUpdate();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
        
//    public void insertAssignmentDetail(Marketing marketing, Campaign campaign, UserAssignmentValue u, Assignment assignment, String role, String customerListID) {
//        String query = "";
//
//        if(role.equals("Supervisor")) {
//            query = "insert into assignment_detail "
//                    + "(assignment_id,user_id,customer_id,called,status,call_attempt,dmc,call_attempt2,sale_attempt,customer_name,new_list,customer_type, "
//                    + "dob,assign_date,marketing_code,max_call,max_call2,campaign_name,gender,update_date) "
//                    + "select TOP(?10) ?4,?5,cus.id,0,'assigned',0,0,0,0,cus.name+' '+isnull(cus.surname,''),asd.new_list,ct.name, "
//                    + "cus.dob,getdate(),?6,?7,?8,?9,cus.gender,getdate() "
//                    + "from assignment_supervisor_detail asd "
//                    + "join assignment_supervisor s on s.id = asd.assignment_supervisor_id "
//                    + "join assignment a on a.id = s.assignment_id "
//                    + "join customer cus on cus.id = asd.customer_id "
//                    + "join customer_type ct on cus.customer_type = ct.id "
//                    + "where a.marketing_id = ?1 and a.campaign_id = ?3 and s.user_id = ?2 and asd.assign = 0 and asd.unassign is null "
//                    + "and cus.id in ("+customerListID+") ";            
//        } else if(role.equals("Manager")) {
//            query = "insert into assignment_detail "
//                    + "(assignment_id,user_id,customer_id,called,status,call_attempt,dmc,call_attempt2,sale_attempt,customer_name,new_list,customer_type, "
//                    + "dob,assign_date,marketing_code,max_call,max_call2,campaign_name,gender,update_date) "  
//                    + "select TOP(?10) ?4,?5,cus.id,0,'assigned',0,0,0,0,cus.name+' '+isnull(cus.surname,''),mc.new_list,ct.name, "
//                    + "cus.dob,getdate(),?6,?7,?8,?9,cus.gender,getdate() "
//                    + "from marketing_customer mc "
//                    + "join customer cus on cus.id = mc.customer_id "
//                    + "join customer_type ct on cus.customer_type = ct.id "
//                    + "where mc.marketing_id = ?1 and mc.assign = 0 "
//                    + "and mc.customer_id  in ("+customerListID+") ";
//        }        
//        
//        Query q = em.createNativeQuery(query, Customer.class);
//        q.setParameter(1, marketing.getId());
//        if(role.equals("Supervisor")) {
//            q.setParameter(2, JSFUtil.getUserSession().getUsers().getId());
//            q.setParameter(3, campaign);  
//        }
//        q.setParameter(4, assignment.getId());
//        q.setParameter(5, u.getUser().getId());
//        q.setParameter(6, marketing.getCode());
//        q.setParameter(7, campaign.getMaxCall());
//        q.setParameter(8, campaign.getMaxCall2());
//        q.setParameter(9, campaign.getName());
//        q.setParameter(10, u.getRecord().intValue());
//        try{
//            q.executeUpdate();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }
                  
    public void insertAssignmentDetail(Marketing marketing, Campaign campaign, UserAssignmentValue u, Assignment assignment, String role, String uniqueID) {
        String query = "";
//        String refUserAssign = JSFUtil.getUserSession().getUsers().getLoginName();
        if(role.equals("Supervisor")) {
            query = "insert into assignment_detail "
                    + "(assignment_id,user_id,customer_id,called,status,call_attempt,dmc,call_attempt2,sale_attempt,customer_name,new_list,customer_type, "
                    + "dob,assign_date,marketing_code,max_call,max_call2,campaign_name,gender,update_date) "
                    + "select TOP(?10) ?4,?5,cus.id,0,'assigned',0,0,0,0,cus.name+' '+isnull(cus.surname,''),asd.new_list,ct.name, "
                    + "cus.dob,getdate(),?6,?7,?8,?9,cus.gender,getdate() "
                    + "from assignment_supervisor_detail asd "
                    + "join assignment_supervisor s on s.id = asd.assignment_supervisor_id "
                    + "join assignment a on a.id = s.assignment_id "
                    + "join customer cus on cus.id = asd.customer_id "
                    + "join customer_type ct on cus.customer_type = ct.id "
                    + "where a.marketing_id = ?1 and a.campaign_id = ?3 and s.user_id = ?2 and asd.assign = 0 and asd.unassign is null "
                    + "and cus.reference_no_2 = ?11";
        } else if(role.equals("Manager")) {
            query = "insert into assignment_detail "
                    + "(assignment_id,user_id,customer_id,called,status,call_attempt,dmc,call_attempt2,sale_attempt,customer_name,new_list,customer_type, "
                    + "dob,assign_date,marketing_code,max_call,max_call2,campaign_name,gender,update_date) "  
                    + "select TOP(?10) ?4,?5,cus.id,0,'assigned',0,0,0,0,cus.name+' '+isnull(cus.surname,''),mc.new_list,ct.name, "
                    + "cus.dob,getdate(),?6,?7,?8,?9,cus.gender,getdate() "
                    + "from marketing_customer mc "
                    + "join customer cus on cus.id = mc.customer_id "
                    + "join customer_type ct on cus.customer_type = ct.id "
                    + "where mc.marketing_id = ?1 and mc.assign = 0 "
                    + "and cus.reference_no_2 = ?11";
        }        
        
        Query q = em.createNativeQuery(query, Customer.class);
        q.setParameter(1, marketing.getId());
        if(role.equals("Supervisor")) {
            q.setParameter(2, JSFUtil.getUserSession().getUsers().getId());
            q.setParameter(3, campaign);  
        }
        q.setParameter(4, assignment.getId());
        q.setParameter(5, u.getUser().getId());
        q.setParameter(6, marketing.getCode());
        q.setParameter(7, campaign.getMaxCall());
        q.setParameter(8, campaign.getMaxCall2());
        q.setParameter(9, campaign.getName());
        q.setParameter(10, u.getRecord().intValue());
        q.setParameter(11, uniqueID);
        try{
            q.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public String getCustomerIDFromListObj(List<Object[]> customerList) {
        Iterator icusts = customerList.iterator();
        String custIDs = "";
        while (icusts.hasNext()) {
            Customer customer = (Customer) icusts.next();
            custIDs += customer.getId().toString()+",";           
        }
        if(custIDs.length() > 0) {
            custIDs = custIDs.substring(0, custIDs.length() - 1); //remove last comma
        }
        
        return custIDs;
    }
    
//    public void insertAssignmentSupervisorDetail(int marketingId, int uRecord, AssignmentSupervisor assignmentSupervisor, String createByName, String customerListID, boolean resetNewList) {            
//        String query = "insert assignment_supervisor_detail (assignment_supervisor_id, customer_id, assign, create_date, create_by, new_list, reset_new_list) "
//                    + "select TOP(?4) ?2, cus.id, 0, getdate(), ?3, mc.new_list, ?5 "
//                    + "from marketing_customer mc "
//                    + "join customer cus on cus.id = mc.customer_id "
//                    + "where mc.marketing_id = ?1 and mc.assign = 0 "
//                    + "and mc.customer_id in ("+customerListID+")";
//      
//        Query q = em.createNativeQuery(query);
//        q.setParameter(1, marketingId);
//        q.setParameter(2, assignmentSupervisor.getId());
//        q.setParameter(3, createByName);
//        q.setParameter(4, uRecord);
//        q.setParameter(5, resetNewList);
//        try{
//            q.executeUpdate();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }
    
    public void insertAssignmentSupervisorDetail(int marketingId, int uRecord, AssignmentSupervisor assignmentSupervisor, String createByName, 
                                                 boolean resetNewList, String uniqueID) {            
//        String refUserAssign = JSFUtil.getUserSession().getUsers().getLoginName();
        String query = "insert assignment_supervisor_detail (assignment_supervisor_id, customer_id, assign, create_date, create_by, new_list, reset_new_list) "
                    + "select TOP(?4) ?2, cus.id, 0, getdate(), ?3, mc.new_list, ?5 "
                    + "from marketing_customer mc "
                    + "join customer cus on cus.id = mc.customer_id "
                    + "where mc.marketing_id = ?1 and mc.assign = 0 "
                    + "and cus.reference_no_2 = ?6";
      
        Query q = em.createNativeQuery(query);
        q.setParameter(1, marketingId);
        q.setParameter(2, assignmentSupervisor.getId());
        q.setParameter(3, createByName);
        q.setParameter(4, uRecord);
        q.setParameter(5, resetNewList);        
        q.setParameter(6, uniqueID);
        try{
            q.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public List<Object[]>  findCustomerForDistributed(String uniqueID) {
//        String refUserAssign = JSFUtil.getUserSession().getUsers().getLoginName();
        String SELECT = "select * from customer where reference_no_2 = ?1 ";
                
        Query q = em.createNativeQuery(SELECT, Customer.class);
        q.setParameter(1, uniqueID);
        List<Object[]> customers = q.getResultList();
        
        return customers;
    }

//    private synchronized void assignMarketing(String assignmentMode, String assignmentType, Campaign campaign, Assignment assignment, 
//            List<UserAssignmentValue> assignmentUsers, int marketingId, Integer norecord, String listType, String assignmentAlgo, 
//            List<Object[]> customerList, boolean resetNewList) {
    private synchronized void assignMarketing(String assignmentMode, String assignmentType, Campaign campaign, Assignment assignment, 
            List<UserAssignmentValue> assignmentUsers, int marketingId, Integer norecord, String listType, String assignmentAlgo, boolean resetNewList, 
            String uniqueID) {
        
        String role="";
        if(JSFUtil.getUserSession().getUserGroup().getRole().contains("CampaignManager") || JSFUtil.getUserSession().getUserGroup().getRole().contains("Supervisor")) {
            if(JSFUtil.getUserSession().getUserGroup().getRole().contains("CampaignManager")) {
                role = "Manager";
            } else {
                role = "Supervisor";
            }
        }
        
        Query q = em.createQuery("select object(m) from Marketing as m where m.id = ?1");
        q.setParameter(1, marketingId);
        Marketing marketing = (Marketing) q.getSingleResult();

        String createByName = JSFUtil.getUserSession().getUsers().getLoginName();
        Integer createById =  JSFUtil.getUserSession().getUsers().getId();
//        String customerListID = getCustomerIDFromListObj(customerList);
        Date today = new Date();
        int countTotalAssignedCustomer = 0;   //count for update marketing
        actualNoUser = 0;
        actualNoCustomer = 0;
        if(assignmentAlgo.equals("sequential") || assignmentType.equals(ASSIGNMENT_POOLING) || assignmentType.equals(ASSIGNMENT_POOLINGCUSTOM) || assignmentType.equals(ASSIGNMENT_POOLINGDAILY)) {
            for (UserAssignmentValue u : assignmentUsers) {
                if(((assignmentType.equals(ASSIGNMENT_AVERAGE) || assignmentType.equals(ASSIGNMENT_CUSTOM) || assignmentType.equals(ASSIGNMENT_AUTODIALER)) && u.getRecord() > 0) 
                        || assignmentType.equals(ASSIGNMENT_POOLING) || assignmentType.equals(ASSIGNMENT_POOLINGCUSTOM) || assignmentType.equals(ASSIGNMENT_POOLINGDAILY)) {   //count user get assign record
                    actualNoUser++;
                    actualNoCustomer+=u.getRecord();

                    if(role.contains("Manager") && assignmentMode.equals(MODE_MANAGER)) { 
                        AssignmentSupervisor assignmentSupervisor = new AssignmentSupervisor();
                        assignmentSupervisor.setUserId(u.getUser());
                        assignmentSupervisor.setAssignmentId(assignment);
                        assignmentSupervisor.setNoCustomer(u.getRecord().intValue());
                        assignmentSupervisor.setNoUsed(0);
                        assignmentSupervisor.setResetNewList(resetNewList);
                        assignmentSupervisor.setCreateDate(today);
                        assignmentSupervisor.setCreateBy(createByName);
                        assignmentSupervisor.setCreateById(createById);
                        em.persist(assignmentSupervisor);
//                        insertAssignmentSupervisorDetail(marketingId, u.getRecord().intValue(), assignmentSupervisor, createByName, customerListID, resetNewList);                        
                        insertAssignmentSupervisorDetail(marketingId, u.getRecord().intValue(), assignmentSupervisor, createByName, resetNewList, uniqueID);                        
                        updateMarketingCustomerModeManager(marketingId, assignmentSupervisor);
                        countTotalAssignedCustomer += u.getRecord();  
                    } else if((role.contains("Manager") && assignmentMode.equals(MODE_SUPERVISOR)) || role.contains("Supervisor")) {
                            if(assignmentType.equals(ASSIGNMENT_AVERAGE) || assignmentType.equals(ASSIGNMENT_CUSTOM) || assignmentType.equals(ASSIGNMENT_AUTODIALER)) {
//                                insertAssignmentDetail(marketing, campaign, u, assignment, role, customerListID);
                                insertAssignmentDetail(marketing, campaign, u, assignment, role, uniqueID);
                                if(role.contains("Supervisor")) {
                                    updateAssignmentSupervisorDetail(assignment, u);
                                } else if(role.contains("Manager")) {
                                    updateMarketingCustomerModeSupervisor(marketingId, assignment.getId(), u.getUser().getId());
                                    countTotalAssignedCustomer += u.getRecord(); 
                                }
                            } else if(assignmentType.equals(ASSIGNMENT_POOLING) || assignmentType.equals(ASSIGNMENT_POOLINGCUSTOM) || assignmentType.equals(ASSIGNMENT_POOLINGDAILY)) {                 
                                        if(checkPoolingUser(u.getUser().getId(),marketingId)){ //check user
                                            AssignmentPooling assignmentPooling;
                                            assignmentPooling = new AssignmentPooling();
                                            assignmentPooling.setAssignment(assignment);
                                            assignmentPooling.setEnable(true);
                                            assignmentPooling.setNoCustomer(u.getRecord().intValue());
                                            assignmentPooling.setNoUsed(0);
                                            assignmentPooling.setUser(u.getUser());
                                            em.persist(assignmentPooling);
                                        } else {
                                            actualNoUser--;
                                            actualNoCustomer = actualNoCustomer - u.getRecord().intValue();
                                        }
                                }
                        }
                }
            } // End for User
        } else if(assignmentAlgo.equals("distributed")) {
                List<Object[]> customers = findCustomerForDistributed(uniqueID);
//                if(role.contains("Manager")) {
////                    customers = this.findUnAssignedCustomer(marketingId, nameSearch, surnameSearch, gender, fromageSearch, toageSearch, homePhoneSearch, officePhoneSearch, mobilePhoneSearch, fromWeighSearch, toWeighSearch, fromHeightSearch, toHeightSearch, listType, norecord);
//                } else {
////                    customers = this.findUnAssignedCustomerFromsup(marketingId, campaign, nameSearch, surnameSearch, gender, fromageSearch, toageSearch, homePhoneSearch, officePhoneSearch, mobilePhoneSearch, fromWeighSearch, toWeighSearch, fromHeightSearch, toHeightSearch, listType, norecord);
//                }
                List<String> customerForUser = new ArrayList<String>();
                for (UserAssignmentValue u : assignmentUsers) {
                    customerForUser.add("");
                }
                int round = 0;
                int cntRec = 0;
                Iterator icusts = customers.iterator();
                while (icusts.hasNext() && cntRec < norecord) {
                    int i= 0;
                    for (UserAssignmentValue u : assignmentUsers) {
                        if(round < u.getRecord()) {     //round for assign custom
                            Customer customer = (Customer) icusts.next();
                            String tmp = customer.getId().toString()+","+customerForUser.get(i);
                            customerForUser.set(i, tmp);
                        }
                        i++;
                        if (!icusts.hasNext()) {        //break assign average
                            break;
                        }
                    }
                    round++;
                    cntRec++;
                }
                int i = 0; 
                for (UserAssignmentValue u : assignmentUsers) {
                    if(u.getRecord() > 0)
                        actualNoUser++;
                    
                    if(role.contains("Manager") && assignmentMode.equals(MODE_MANAGER)) {  //check manager
                        AssignmentSupervisor assignmentSupervisor = new AssignmentSupervisor();
                        assignmentSupervisor.setUserId(u.getUser());
                        assignmentSupervisor.setAssignmentId(assignment);
                        assignmentSupervisor.setNoCustomer(u.getRecord().intValue());
                        assignmentSupervisor.setNoUsed(0);
                        assignmentSupervisor.setResetNewList(resetNewList);
                        assignmentSupervisor.setCreateDate(today);
                        assignmentSupervisor.setCreateBy(createByName);
                        assignmentSupervisor.setCreateById(createById);
                        em.persist(assignmentSupervisor);
                        insertAssignmentSupervisorDetailDistributed(marketingId, assignmentSupervisor, createByName, customerForUser.get(i), resetNewList);  //slow
                    } else if((role.contains("Manager") && assignmentMode.equals(MODE_SUPERVISOR)) || role.contains("Supervisor")) {   //check supervisor
                                insertAssignmentDetailDistributed(marketing, campaign, assignment, u, customerForUser.get(i));
                                if(role.contains("Supervisor")) {
                                    updateAssignmentSupervisorDetail(assignment, u);
                                }
                    }
                    i++;
                    actualNoCustomer += u.getRecord();
                    countTotalAssignedCustomer += u.getRecord();  
                }
                 //update marketing customer
                if(role.contains("Manager"))
                    this.updateMarketingCustomer(marketingId, assignmentMode, assignment.getId());
        }
        
        if((role.contains("Manager") && assignmentMode.equals(MODE_SUPERVISOR)) || role.contains("Supervisor")) {
            insertCampaignCustomer(campaign, assignment);  
        }
        if(role.contains("Manager")) {
           //update marketing table for statistic
            int assignNo = (marketing.getAssignedNo()!=null) ? marketing.getAssignedNo() : 0;
            assignNo++;
            int totalAssigned = (marketing.getTotalAssigned()!=null) ? marketing.getTotalAssigned() : 0;
            marketing.setAssignedLatestDate(new Date());
            marketing.setAssignedNo(assignNo);
            marketing.setTotalAssigned(totalAssigned+countTotalAssignedCustomer);
            em.merge(marketing);
        }
    }
    
    private void updateMarketingCustomerModeManager(Integer marketingId, AssignmentSupervisor assignmentSupervisor){
        String sql = "";
        sql = "update marketing_customer set assign = 1 "
            + "from marketing_customer mc "
            + "inner join assignment_supervisor_detail asd on mc.customer_id = asd.customer_id "
            + "inner join assignment_supervisor asu on asd.assignment_supervisor_id = asu.id "
            + "where asu.id = :assignmentSupervisorId and mc.marketing_id = :marketingId";
        Query q = em.createNativeQuery(sql);
        q.setParameter("marketingId", marketingId);
        q.setParameter("assignmentSupervisorId", assignmentSupervisor.getId());
        
        try{
            q.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void updateMarketingCustomerModeSupervisor(Integer marketingId, Integer assignmentId, Integer userId){
        String sql = "";
        sql = "update marketing_customer set assign = 1 "
            + "from marketing_customer mc "
            + "inner join assignment_detail ad on mc.customer_id = ad.customer_id "
            + "where ad.assignment_id = :assignmentId and ad.user_id = :userId and mc.marketing_id = :marketingId";
        Query q = em.createNativeQuery(sql);
        q.setParameter("marketingId", marketingId);
        q.setParameter("assignmentId", assignmentId);
        q.setParameter("userId", userId);
        
        try{
            q.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
        
    private void updateMarketingCustomer(Integer marketingId, String assignmentMode, Integer assignmentId){
        String sql = "";
        if(assignmentMode.equals(MODE_MANAGER)) {
            sql = "update marketing_customer set assign = 1 "
                    + "from marketing_customer mc "
                    + "inner join assignment_supervisor_detail asd on mc.customer_id = asd.customer_id "
                    + "inner join assignment_supervisor asu on asd.assignment_supervisor_id = asu.id "
                    + "where asu.assignment_id = :assignmentId and mc.marketing_id = :marketingId";
        } else if(assignmentMode.equals(MODE_SUPERVISOR)) {
            sql = "update marketing_customer set assign = 1 "
                    + "from marketing_customer mc "
                    + "inner join assignment_detail ad on mc.customer_id = ad.customer_id "
                    + "where ad.assignment_id = :assignmentId and mc.marketing_id = :marketingId";
        }
        Query q = em.createNativeQuery(sql);
        q.setParameter("marketingId", marketingId);
        q.setParameter("assignmentId", assignmentId);
        
        try{
            q.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public List<AssignmentListMonitorValue> findAssignmentListTodayByUser(Integer campaignId, int userGroupId) {
        
        String select, where, groupBy;
        
        if(campaignId != 0 && campaignId != null) {
            select = "select NEW "
                    +AssignmentListMonitorValue.class.getName()  
                    + "(c.name, c.id, u.name, u.surname, ad.status, count(distinct ad.id), u.id, "
                    + "sum(CASE WHEN po.saleResult = 'Y' THEN 1 ELSE 0 END), "
                    + "sum(CASE WHEN po.saleResult = 'Y' and po.approvalStatus = 'approved' and po.qcStatus = 'approved' and po.paymentStatus = 'approved' THEN 1 ELSE 0 END)) "
                    + "from AssignmentDetail ad "
                    + "join ad.users u  "
                    + "join ad.assignmentId a  "
                    + "join a.campaign c "
                    + "left join ad.purchaseOrderCollection po ";
        
            where = "where u.userGroup.id = ?1 and u.enable = true and u.status = true and ad.updateDate between ?2 and ?3 and ad.unassign is null and a.campaign.id = "+campaignId+" ";
            
            groupBy = "group by c.name, c.id, u.name, u.surname, ad.status, u.id "
                    + "order by c.name, c.id, u.name, u.surname, ad.status, u.id ";
        } else {
            select = "select NEW "
                    +AssignmentListMonitorValue.class.getName()  
                    + "(u.name, u.surname, ad.status, count(distinct ad.id), u.id, "
                    + "sum(CASE WHEN po.saleResult = 'Y' THEN 1 ELSE 0 END), "
                    + "sum(CASE WHEN po.saleResult = 'Y' and po.approvalStatus = 'approved' and po.qcStatus = 'approved' and po.paymentStatus = 'approved' THEN 1 ELSE 0 END)) "
                    + "from AssignmentDetail ad "
                    + "join ad.users u  "
                    + "join ad.assignmentId a  "
                    + "join a.campaign c "
                    + "left join ad.purchaseOrderCollection po ";
        
            where = "where u.userGroup.id = ?1 and u.enable = true and u.status = true and ad.updateDate between ?2 and ?3 and ad.unassign is null "
                  + "and c.status = 1 and c.enable = 1 and (c.startDate <= GETDATE() and c.endDate >= GETDATE()) ";

            groupBy = "group by u.name, u.surname, ad.status, u.id "
                   + "order by u.name, u.surname, ad.status, u.id ";
        }        
//        String sql = "select NEW "
//                    +AssignmentListMonitorValue.class.getName()  
//                    + "(c.name, c.id, u.name, u.surname, ad.status, count(distinct ad.id), u.id, "
//                    + "sum(CASE WHEN po.saleResult = 'Y' THEN 1 ELSE 0 END), "
//                    + "sum(CASE WHEN po.saleResult = 'Y' and po.approvalStatus = 'approved' and po.qcStatus = 'approved' and po.paymentStatus = 'approved' THEN 1 ELSE 0 END)) "
//                    + "from AssignmentDetail ad "
//                    + "join ad.users u  "
//                    + "join ad.assignmentId a  "
//                    + "join a.campaign c "
//                    + "left join ad.purchaseOrderCollection po ";
//        
//        String where = "where u.userGroup.id = ?1 and u.enable = true and u.status = true and ad.updateDate between ?2 and ?3 and ad.unassign is null ";
//        if(campaignId!= 0 && campaignId != null) {
//            where += "and a.campaign.id = "+campaignId+" ";
//        } else {
//            where += "and c.status = 1 and c.enable = 1 and (c.startDate <= GETDATE() and c.endDate >= GETDATE()) ";
//        }
//        
//        String groupBy = "group by c.name, c.id, u.name, u.surname, ad.status, u.id "
//                       + "order by c.name, c.id, u.name, u.surname, ad.status, u.id ";
//        
        Query q = em.createQuery(select + where + groupBy);
        
        Date today = new Date();
        q.setParameter(1, userGroupId);
        q.setParameter(2, JSFUtil.toDateWithoutTime(today));
        q.setParameter(3, JSFUtil.toDateWithMaxTime(today));
        return q.getResultList();
    }

    public List<AssignmentListMonitorValue> findAssignmentListAllByUser(Integer campaignId, int userGroupId) {
        
        String select, where, groupBy;
        
        if(campaignId != 0 && campaignId != null) {
            select = "select NEW "
                + AssignmentListMonitorValue.class.getName()    
                + "(c.name, c.id, u.name, u.surname, ad.status, count(distinct ad.id), u.id, "
                + "sum(CASE WHEN po.saleResult = 'Y' THEN 1 ELSE 0 END), "
                + "sum(CASE WHEN po.saleResult = 'Y' and po.approvalStatus = 'approved' and po.qcStatus = 'approved' and po.paymentStatus = 'approved' THEN 1 ELSE 0 END)) "
                + "from AssignmentDetail ad "
                + "join ad.users u  "
                + "join ad.assignmentId a  "
                + "join a.campaign c  "
                + "left join ad.purchaseOrderCollection po ";
        
            where = "where u.userGroup.id = ?1 and u.enable = true and u.status = true and ad.unassign is null and a.campaign.id = "+campaignId+" ";
            
            groupBy = "group by c.name, c.id, u.name, u.surname, ad.status, u.id "
                   + "order by c.name, c.id, u.name, u.surname, ad.status, u.id ";
        } else {
            select = "select NEW "
                + AssignmentListMonitorValue.class.getName()    
                + "(u.name, u.surname, ad.status, count(distinct ad.id), u.id, "
                + "sum(CASE WHEN po.saleResult = 'Y' THEN 1 ELSE 0 END), "
                + "sum(CASE WHEN po.saleResult = 'Y' and po.approvalStatus = 'approved' and po.qcStatus = 'approved' and po.paymentStatus = 'approved' THEN 1 ELSE 0 END)) "
                + "from AssignmentDetail ad "
                + "join ad.users u  "
                + "join ad.assignmentId a  "
                + "join a.campaign c  "
                + "left join ad.purchaseOrderCollection po ";
        
            where = "where u.userGroup.id = ?1 and u.enable = true and u.status = true and ad.unassign is null "
                    + "and c.status = 1 and c.enable = 1 and (c.startDate <= GETDATE() and c.endDate >= GETDATE()) ";

            groupBy = "group by u.name, u.surname, ad.status, u.id "
                   + "order by u.name, u.surname, ad.status, u.id ";
        }
        
        Query q = em.createQuery(select + where + groupBy);
        
        q.setParameter(1, userGroupId);
        
        return q.getResultList();
    }
    
    public List<AssignmentListMonitorValue> findAssignmentListRealTimeByUser(Integer campaignId, int userGroupId) {
        
        String select, where, groupBy;
        
        if(campaignId != 0 && campaignId != null) {
            select = "select NEW "
                    +AssignmentListMonitorValue.class.getName()  
                    + "(c.name, c.id, u.name, u.surname, ad.status, count(distinct ad.id), u.id, "
                    + "sum(CASE WHEN po.saleResult = 'Y' THEN 1 ELSE 0 END), "
                    + "sum(CASE WHEN po.saleResult = 'Y' and po.approvalStatus = 'approved' and po.qcStatus = 'approved' and po.paymentStatus = 'approved' THEN 1 ELSE 0 END)) "
                    + "from AssignmentDetail ad "
                    + "join ad.users u  "
                    + "join ad.assignmentId a  "
                    + "join a.campaign c "
                    + "left join ad.purchaseOrderCollection po ";
        
            where = "where u.userGroup.id = ?1 and u.enable = true and u.status = true "
                    + "and ad.unassign is null and c.id = "+campaignId+" "
                    + "and c.status = 1 and c.enable = 1 and (GETDATE() between c.startDate and c.endDate) "
                    + "and a.marketing.status = 1 and a.marketing.enable = 1 and (GETDATE() between a.marketing.startDate and a.marketing.endDate) ";
            
            where += "and (ad.status <> 'closed' or ad.status = 'closed' and ad.updateDate between DATEADD(d, -"+JSFUtil.getApplication().getCloseAssignmentList()+", DATEDIFF(d, 0, GETDATE())) and DATEADD(d, 1, DATEDIFF(d, 0, GETDATE()))) ";
            
            groupBy = "group by c.name, c.id, u.name, u.surname, ad.status, u.id "
                    + "order by c.name, c.id, u.name, u.surname, ad.status, u.id ";
        } else {
            select = "select NEW "
                    +AssignmentListMonitorValue.class.getName()  
                    + "(u.name, u.surname, ad.status, count(distinct ad.id), u.id, "
                    + "sum(CASE WHEN po.saleResult = 'Y' THEN 1 ELSE 0 END), "
                    + "sum(CASE WHEN po.saleResult = 'Y' and po.approvalStatus = 'approved' and po.qcStatus = 'approved' and po.paymentStatus = 'approved' THEN 1 ELSE 0 END)) "
                    + "from AssignmentDetail ad "
                    + "join ad.users u  "
                    + "join ad.assignmentId a  "
                    + "join a.campaign c "
                    + "left join ad.purchaseOrderCollection po ";

            where = "where u.userGroup.id = ?1 and u.enable = true and u.status = true "
                    + "and ad.unassign is null "
                    + "and c.status = 1 and c.enable = 1 and (GETDATE() between c.startDate and c.endDate) "
                    + "and a.marketing.status = 1 and a.marketing.enable = 1 and (GETDATE() between a.marketing.startDate and a.marketing.endDate) ";
            
            where += "and (ad.status <> 'closed' or ad.status = 'closed' and ad.updateDate between DATEADD(d, -"+JSFUtil.getApplication().getCloseAssignmentList()+", DATEDIFF(d, 0, GETDATE())) and DATEADD(d, 1, DATEDIFF(d, 0, GETDATE()))) ";
            
            groupBy = "group by u.name, u.surname, ad.status, u.id "
                   + "order by u.name, u.surname, ad.status, u.id ";
        }        
        
        Query q = em.createQuery(select + where + groupBy);
        
        q.setParameter(1, userGroupId);
        return q.getResultList();
    }
        
    public List<AssignmentPooling> findAssignmentPooling(Users user, Boolean status) {
        Query q = em.createQuery("select object(o) from AssignmentPooling as o"
                + " where o.user.id = :userId"
                + " and o.enable = :status"
                + " and CONVERT(VARCHAR(10), getdate(), 120) between CONVERT(VARCHAR(10), o.assignment.poolingFromDate, 120) and CONVERT(VARCHAR(10), DATEADD(d, 0, DATEDIFF(d, 0, o.assignment.poolingToDate)), 120)");
        
        q.setParameter("userId", user.getId());
        q.setParameter("status", status);
        
        return q.getResultList();
    }
    
    public AssignmentPoolingDaily findAssignmentPoolingDaily(Integer assignmentId, Date dateNow) {
        Query q = em.createQuery("select object(o) from AssignmentPoolingDaily as o"
                + " where o.assignmentPoolingDailyPK.assignmentPoolingId = ?1"
                + " and CONVERT(VARCHAR(10), o.assignmentPoolingDailyPK.transDate, 120) = CONVERT(VARCHAR(10), ?2, 120)");
        q.setParameter(1, assignmentId);
        q.setParameter(2, dateNow);
        q.setMaxResults(1);
        List<AssignmentPoolingDaily> list = q.getResultList();
        if(!list.isEmpty()){
            return (AssignmentPoolingDaily) q.getResultList().get(0);
        }else{
            return null;
        }
    }    
    
    public void createAssignmentPoolingDaily(AssignmentPoolingDaily assignmentPoolingDaily) {
        em.persist(assignmentPoolingDaily);
    } 
    
    public void updateAssignmentPoolingDaily(AssignmentPoolingDaily assignmentPoolingDaily) {
        em.merge(assignmentPoolingDaily);
    }
    
    public void updateAssignmentPoolingDaily1(AssignmentPoolingDaily assignmentPoolingDaily) {
        try{
            Query q = em.createNativeQuery("update assignment_pooling_daily set no_used = ?1 where assignment_pooling_id = ?2 and trans_date = ?3");
            q.setParameter(1, assignmentPoolingDaily.getNoUsed());
            q.setParameter(2, assignmentPoolingDaily.getAssignmentPoolingDailyPK().getAssignmentPoolingId());
            q.setParameter(3, assignmentPoolingDaily.getAssignmentPoolingDailyPK().getTransDate());

            q.executeUpdate();
        }catch(Exception e){
            log.error(e);
        }
    }
    
    public List<AssignmentPooling>  findAssignmentPoolingByAssignment(Integer assignmentId){   
        Query q = em.createQuery("select object(o) from AssignmentPooling as o join o.user u where o.assignment.id=?1 and o.enable = true order by u.name");
        q.setParameter(1, assignmentId);
        return q.getResultList();
    }
    
    public AssignmentPooling findAssignmentPooling(Integer selectedId){   
        Query q = em.createQuery("select object(o) from AssignmentPooling as o where o.id=?1");
        q.setParameter(1, selectedId);
        q.setMaxResults(1);
        List<AssignmentPooling> list = q.getResultList();
        if(!list.isEmpty()){
            return (AssignmentPooling) q.getResultList().get(0);
        }else{
            return null;
        }
    }
    
    public Assignment findByCampaign(Integer campaignId){   
        Query q = em.createQuery("select object(o) from Assignment as o where o.campaign.id = ?1");
        q.setParameter(1, campaignId);
        q.setMaxResults(1);
        List<Assignment> list = q.getResultList();
        if(!list.isEmpty()){
            return (Assignment) list.get(0);
        }else{
            return null;
        }
    }
    
    public int findAllCustomer(int marketingId, String listType) { 
        Query q;   
        if(listType.equals("allList")) {
            q = em.createQuery("select count(cus) from MarketingCustomer mc "
                + "join mc.customer cus "
                + "where mc.marketingCustomerPK.marketingId = ?1 and mc.assign = false and isnull(cus.opOut,0) <> 1 ");
        } else {
            q = em.createQuery("select count(cus) from MarketingCustomer mc "
                + "join mc.customer cus "
                + "where mc.newList = ?2 and mc.marketingCustomerPK.marketingId = ?1 and mc.assign = false and isnull(cus.opOut,0) <> 1 ");
           q.setParameter(2, listType.equals("newList") ? true : listType.equals("oldList") ? false : null);
        }
        q.setParameter(1, marketingId);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public int findAllCustomerFromsup(int marketingId, Campaign campaign, String listType) {
        Query q;
        if(listType.equals("allList")) {
            q = em.createQuery("select count(asd) from AssignmentSupervisorDetail asd "
                    + "join asd.assignmentSupervisorId s "
                    + "join s.assignmentId a "
                    + "join asd.customerId cus "
                    + "where a.marketing.id = ?1 and a.campaign = ?3 and s.userId.id = ?2 and asd.assign = false and asd.unassign is null and isnull(cus.opOut,0) <> 1 ");  
        } else {
            q = em.createQuery("select count(asd) from AssignmentSupervisorDetail asd "
                    + "join asd.assignmentSupervisorId s "
                    + "join s.assignmentId a "
                    + "join asd.customerId cus "
                    + "where asd.newList = ?4 and a.marketing.id = ?1 and a.campaign = ?3 "
                    + "and s.userId.id = ?2 and asd.assign = false and asd.unassign is null and isnull(cus.opOut,0) <> 1 ");
            q.setParameter(4, listType.equals("newList") ? true : listType.equals("oldList") ? false : null);
        }
        q.setParameter(1, marketingId);
        q.setParameter(2, JSFUtil.getUserSession().getUsers().getId());
        q.setParameter(3, campaign);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    //For Distributed Assignment     
//    public List<Object[]> findUnAssignedCustomer(int marketingId, String nameSearch, String surnameSearch, String gender, int fromageSearch, 
//            int toageSearch, String homePhoneSearch, String officePhoneSearch, String mobilePhoneSearch, double fromWeighSearch, double toWeighSearch, 
//            double fromHeightSearch, double toHeightSearch, String listType, Integer recordQuery) {
//        String query;
//        String select ="";
//        String where = "";
//        
//        select = "select cus.* from marketing_customer mc "
//            + "join customer cus on cus.id = mc.customer_id "
//            + "where mc.marketing_id = ?1 and mc.assign = 0 ";
//           
//        if (listType.equals("newList")) {
//            where = "and mc.new_list = 1 ";
//        } else if (listType.equals("oldList")) {
//            where = "and mc.new_list = 0 ";
//        }
//        
//        if (nameSearch != null && !nameSearch.isEmpty())
//            where += "and cus.name like '%"+nameSearch.trim()+"%' ";
//        if (surnameSearch != null && !surnameSearch.isEmpty())
//            where += "and cus.surname like '%"+surnameSearch.trim()+"%' ";
//        if (homePhoneSearch != null && !homePhoneSearch.isEmpty())
//            if(homePhoneSearch.equals("upc")) {
//                where += "and (cus.home_phone like '053%' or cus.home_phone like '054%' or cus.home_phone like '055%' or cus.home_phone like '056%' "
//                        + "or cus.home_phone like '032%' or cus.home_phone like '034%' or cus.home_phone like '035%' or cus.home_phone like '036%' or cus.home_phone like '037%' or cus.home_phone like '038%' or cus.home_phone like '039%' "
//                        + "or cus.home_phone like '042%' or cus.home_phone like '043%' or cus.home_phone like '044%' or cus.home_phone like '045%' "
//                        + "or cus.home_phone like '073%' or cus.home_phone like '074%' or cus.home_phone like '075%' or cus.home_phone like '076%' or cus.home_phone like '077%') ";
//            } else {
//                where += "and cus.home_phone like '"+homePhoneSearch.trim()+"%' ";
//            }
//        if (officePhoneSearch != null && !officePhoneSearch.isEmpty())
//            if(officePhoneSearch.equals("upc")) {
//                where += "and (cus.office_phone like '053%' or cus.office_phone like '054%' or cus.office_phone like '055%' or cus.office_phone like '056%' "
//                        + "or cus.office_phone like '032%' or cus.office_phone like '034%' or cus.office_phone like '035%' or cus.office_phone like '036%' or cus.office_phone like '037%' or cus.office_phone like '038%' or cus.office_phone like '039%' "
//                        + "or cus.office_phone like '042%' or cus.office_phone like '043%' or cus.office_phone like '044%' or cus.office_phone like '045%' "
//                        + "or cus.office_phone like '073%' or cus.office_phone like '074%' or cus.office_phone like '075%' or cus.office_phone like '076%' or cus.office_phone like '077%') ";
//                } else {
//                    where += "and cus.office_phone like '"+officePhoneSearch.trim()+"%' ";
//                }
//        if (mobilePhoneSearch != null && !mobilePhoneSearch.isEmpty())
//            where += "and cus.mobile_phone like '"+mobilePhoneSearch.trim()+"%' ";
//        if (gender != null && !gender.isEmpty())
//            where += "and cus.gender like '"+gender+"%' "; //where += "and cus.gender like '"+gender.substring(0, 1)+"%' "; 
//        if(fromWeighSearch > 0 && toWeighSearch > 0 && (fromWeighSearch < toWeighSearch))
//            where += "and cus.weight >= "+fromWeighSearch+" and and cus.weight <= "+toWeighSearch+" )";
//        if(fromHeightSearch > 0 && toHeightSearch > 0 && (fromHeightSearch < toHeightSearch))
//            where += "and cus.height >= "+fromHeightSearch+" and and cus.height <= "+toHeightSearch+" )";
//        if(fromageSearch > 0 && toageSearch > 0)
//            where += "and (DATEDIFF(Year,cus.dob,getdate()) >= "+fromageSearch+" and DATEDIFF(Year,cus.dob,getdate()) <= "+toageSearch+" )";
//        
//        query = select+where;
//        Query q = em.createNativeQuery(query, Customer.class);
//        q.setParameter(1, marketingId);
//        q.setMaxResults(recordQuery);
//        List<Object[]> customers = q.getResultList();
//        return customers;
//    }

//    public List<Object[]> findUnAssignedCustomerFromsup(int marketingId, Campaign campaign, String nameSearch, String surnameSearch, String gender,
//            int fromageSearch, int toageSearch, String homePhoneSearch, String officePhoneSearch, String mobilePhoneSearch, double fromWeighSearch,
//            double toWeighSearch, double fromHeightSearch, double toHeightSearch, String listType, Integer recordQuery) {
//        String query;
//        String select ="";
//        String where = "";
//
//          select = "select cus.* from assignment_supervisor_detail asd "
//                + "join assignment_supervisor s on s.id = asd.assignment_supervisor_id "
//                + "join assignment a on a.id = s.assignment_id "
//                + "join customer cus on cus.id = asd.customer_id "
//                + "where a.marketing_id = ?1 and a.campaign_id = ?3 and s.user_id = ?2 and asd.assign = 0 and asd.unassign is null ";
//
//        if (listType.equals("newList")) {
//            where = "and asd.new_list = 1 ";
//        } else if (listType.equals("oldList")) {
//            where = "and asd.new_list = 0 ";
//        }
//        if (nameSearch != null && !nameSearch.isEmpty())
//            where += "and cus.name like '%"+nameSearch.trim()+"%' ";
//        if (surnameSearch != null && !surnameSearch.isEmpty())
//            where += "and cus.surname like '%"+surnameSearch.trim()+"%' ";
//        if (homePhoneSearch != null && !homePhoneSearch.isEmpty())
//            if(homePhoneSearch.equals("upc")) {
//                where += "and (cus.home_phone like '053%' or cus.home_phone like '054%' or cus.home_phone like '055%' or cus.home_phone like '056%' "
//                        + "or cus.home_phone like '032%' or cus.home_phone like '034%' or cus.home_phone like '035%' or cus.home_phone like '036%' or cus.home_phone like '037%' or cus.home_phone like '038%' or cus.home_phone like '039%' "
//                        + "or cus.home_phone like '042%' or cus.home_phone like '043%' or cus.home_phone like '044%' or cus.home_phone like '045%' "
//                        + "or cus.home_phone like '073%' or cus.home_phone like '074%' or cus.home_phone like '075%' or cus.home_phone like '076%' or cus.home_phone like '077%') ";
//            } else {
//                where += "and cus.home_phone like '"+homePhoneSearch.trim()+"%' ";
//            }
//        if (officePhoneSearch != null && !officePhoneSearch.isEmpty())
//            if(officePhoneSearch.equals("upc")) {
//                where += "and (cus.office_phone like '053%' or cus.office_phone like '054%' or cus.office_phone like '055%' or cus.office_phone like '056%' "
//                        + "or cus.office_phone like '032%' or cus.office_phone like '034%' or cus.office_phone like '035%' or cus.office_phone like '036%' or cus.office_phone like '037%' or cus.office_phone like '038%' or cus.office_phone like '039%' "
//                        + "or cus.office_phone like '042%' or cus.office_phone like '043%' or cus.office_phone like '044%' or cus.office_phone like '045%' "
//                        + "or cus.office_phone like '073%' or cus.office_phone like '074%' or cus.office_phone like '075%' or cus.office_phone like '076%' or cus.office_phone like '077%') ";
//                } else {
//                    where += "and cus.office_phone like '"+officePhoneSearch.trim()+"%' ";
//                }
//        if (mobilePhoneSearch != null && !mobilePhoneSearch.isEmpty())
//            where += "and cus.mobile_phone like '"+mobilePhoneSearch.trim()+"%' ";
//        if (gender != null && !gender.isEmpty())
//            where += "and cus.gender like '"+gender+"%' "; //where += "and cus.gender like '"+gender.substring(0, 1)+"%' "; 
//        if(fromWeighSearch > 0 && toWeighSearch > 0 && (fromWeighSearch < toWeighSearch))
//            where += "and cus.weight >= "+fromWeighSearch+" and and cus.weight <= "+toWeighSearch+" )";
//        if(fromHeightSearch > 0 && toHeightSearch > 0 && (fromHeightSearch < toHeightSearch))
//            where += "and cus.height >= "+fromHeightSearch+" and and cus.height <= "+toHeightSearch+" )";
//        if(fromageSearch > 0 && toageSearch > 0)
//            where += "and (DATEDIFF(Year,cus.dob,getdate()) >= "+fromageSearch+" and DATEDIFF(Year,cus.dob,getdate()) <= "+toageSearch+" )";
//        query = select+where;
//        Query q = em.createNativeQuery(query, Customer.class);
//
//        q.setParameter(1, marketingId);
//        q.setParameter(2, JSFUtil.getUserSession().getUsers().getId());
//        q.setParameter(3, campaign);
//        q.setMaxResults(recordQuery);
//        List<Object[]> customers = q.getResultList();
//        return customers;
//    }
    
    public void insertAssignmentSupervisorDetailDistributed(int marketingId, AssignmentSupervisor assignmentSupervisor, String createByName, String customerId, boolean resetNewList) {
        //slow performance
        if (customerId.length() > 0) {
            customerId = customerId.substring(0, customerId.length() - 1); //remove last comma
            Query q = em.createNativeQuery("insert assignment_supervisor_detail (assignment_supervisor_id, customer_id, assign, create_date, create_by, new_list, reset_new_list) "
                + "select ?1, mc.customer_id, 0, getdate(), ?2, mc.new_list, ?4 "
                + "from marketing_customer mc "
                + "where mc.marketing_id = ?3 and mc.customer_id in ("+customerId+")");

            q.setParameter(1, assignmentSupervisor.getId());
            q.setParameter(2, createByName);
            q.setParameter(3, marketingId);
            q.setParameter(4, resetNewList);
            try{
                q.executeUpdate();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public void insertAssignmentDetailDistributed(Marketing marketing, Campaign campaign, Assignment assignment, UserAssignmentValue u , String customerId) {
        if (customerId.length() > 0) {
            customerId = customerId.substring(0, customerId.length() - 1); //remove last comma
            Query q = em.createNativeQuery("insert into assignment_detail "
                    + "(assignment_id,user_id,customer_id,called,status,call_attempt,dmc,call_attempt2,sale_attempt,customer_name,new_list,"
                    + "customer_type,dob,assign_date,marketing_code,max_call,max_call2,campaign_name,gender,update_date) "
                    + "select ?2,?3,cus.id,0,'assigned',0,0,0,0,cus.name+' '+isnull(cus.surname,''),mc.new_list,ct.name, "
                    + "cus.dob,getdate(),?4,?5,?6,?7,cus.gender,getdate() "
                    + "from marketing_customer mc "
                    + "join customer cus on cus.id = mc.customer_id "
                    + "join customer_type ct on cus.customer_type = ct.id "
                    + "where mc.marketing_id = ?1 and "
                    + "mc.customer_id in ("+customerId+")");

            q.setParameter(1, marketing.getId());
            q.setParameter(2, assignment.getId());
            q.setParameter(3, u.getUser().getId());
            q.setParameter(4, marketing.getCode());
            q.setParameter(5, campaign.getMaxCall());
            q.setParameter(6, campaign.getMaxCall2());
            q.setParameter(7, campaign.getName());
            try{
                q.executeUpdate();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public synchronized void assignNewProspect(Customer customer, Campaign campaign, Integer marketingId, Integer userId, List<Product> selectedProduct) {
        Date today = new Date();
        Users user = JSFUtil.getUserSession().getUsers();
        String role = user.getUserGroup().getRole();
        Query q = em.createQuery("select object(m) from Marketing as m where m.id = ?1");
        q.setParameter(1, marketingId);
        Marketing marketing = (Marketing) q.getSingleResult();
        
        //1. create customer
        customer.setCreateDate(today);
        customer.setCreateBy(JSFUtil.getUserSession().getUserName());
        em.persist(customer);
        
        //2. insert marketing customer
        MarketingCustomer marketingCustomer = new MarketingCustomer();
        MarketingCustomerPK marketingCustomerPK = new MarketingCustomerPK();
        marketingCustomerPK.setMarketingId(marketingId);
        marketingCustomerPK.setCustomerId(customer.getId());
        marketingCustomer.setMarketingCustomerPK(marketingCustomerPK);
        marketingCustomer.setAssign(Boolean.TRUE);
        marketingCustomer.setNewList(Boolean.TRUE);
        marketingCustomer.setCreateDate(today);
        em.persist(marketingCustomer);
        
        //3. update marketing
        marketing.setTotalRecord(marketing.getTotalRecord()+1);
        marketing.setTotalAssigned((marketing.getTotalAssigned() != null ? marketing.getTotalAssigned() : 0) + 1);
        em.merge(marketing);
        
        //4. insert campaign customer
        CampaignCustomer campaignCustomer = new CampaignCustomer();
        CampaignCustomerPK campaignCustomerPK = new CampaignCustomerPK();
        campaignCustomerPK.setCampaignId(campaign.getId());
        campaignCustomerPK.setCustomerId(customer.getId());
        campaignCustomer.setCampaignCustomerPK(campaignCustomerPK);
        em.persist(campaignCustomer);
        
        //5. create assignment
        Assignment assignment = new Assignment();
        assignment.setAssignDate(today);
        if(role.contains("CampaignManager")) {
            assignment.setAssignmentMode("manager");
        } else if(role.contains("Supervisor")) {
            assignment.setAssignmentMode("supervisor");
        }
        assignment.setAssignmentType("custom");
        assignment.setCampaign(campaign);
        assignment.setCreateBy(user.getName());
        assignment.setCreateByUser(user);
        assignment.setCreateDate(today);
        assignment.setMarketing(marketing);
        assignment.setNoCustomer(1);
        assignment.setNoUser(1);
        create(assignment);
        
        //6. create assignment supervisor
        if(!role.contains("CampaignManager") && role.contains("Supervisor")) {
            AssignmentSupervisor assignmentSupervisor = new AssignmentSupervisor();
            assignmentSupervisor.setAssignmentId(assignment);
            assignmentSupervisor.setCreateBy(user.getName());
            assignmentSupervisor.setCreateById(user.getId());
            assignmentSupervisor.setCreateDate(today);
            assignmentSupervisor.setNoCustomer(1);
            assignmentSupervisor.setNoUsed(1);
            assignmentSupervisor.setUserId(user);
            em.persist(assignmentSupervisor);

            //7. create assignment supervisor detail
            AssignmentSupervisorDetail assignmentSupervisorDetail = new AssignmentSupervisorDetail();
            assignmentSupervisorDetail.setAssign(Boolean.TRUE);
            assignmentSupervisorDetail.setAssignDate(today);
            assignmentSupervisorDetail.setAssignmentSupervisorId(assignmentSupervisor);
            assignmentSupervisorDetail.setCreateBy(user.getName());
            assignmentSupervisorDetail.setCreateDate(today);
            assignmentSupervisorDetail.setCustomerId(customer);
            assignmentSupervisorDetail.setNewList(Boolean.TRUE);
            em.persist(assignmentSupervisorDetail);
        }
        
        //8. create assignment detail
        AssignmentDetail assignmentDetail = new AssignmentDetail();
        assignmentDetail.setAssignmentId(assignment);
        assignmentDetail.setUsers(new Users(userId));
        assignmentDetail.setCustomerId(customer);
        assignmentDetail.setCalled(false);
        assignmentDetail.setStatus("assigned");
        assignmentDetail.setCallAttempt(0);
        assignmentDetail.setDmc(false);
        assignmentDetail.setCallAttempt2(0);
        assignmentDetail.setSaleAttempt(0);
        if(customer.getSurname() != null) {
            assignmentDetail.setCustomerName(customer.getName()+' '+customer.getSurname());         
        } else {
            assignmentDetail.setCustomerName(customer.getName());    
        }
        assignmentDetail.setNewList(Boolean.TRUE);
        assignmentDetail.setCustomerType("Prospect");
        assignmentDetail.setDob(customer.getDob());
        assignmentDetail.setAssignDate(today);
        assignmentDetail.setMarketingCode(marketing.getCode());
        assignmentDetail.setMaxCall(campaign.getMaxCall());
        assignmentDetail.setMaxCall2(campaign.getMaxCall2());
        assignmentDetail.setCampaignName(campaign.getName());
        assignmentDetail.setGender(customer.getGender());
        assignmentDetail.setUpdateDate(today);
        em.persist(assignmentDetail);
        
        //9. create assignment product
        assignProduct(assignment,selectedProduct);
    }
    
    public List<Marketing> findUsedMarketingByCampaignAndUserGroupMarketing(int campaignId, String listType, int ugId) {
        String where = findMarketingByUserGroupAndCampaign(ugId);
        Query q;
        if(listType.equals("allList")) {
            q = em.createQuery("select distinct a.marketing from Assignment a "
                    + "where a.marketing.startDate <= ?3 and a.marketing.endDate >= ?3 and a.campaign.id=?1 and a.marketing.enable = true and a.marketing.status = true " + where + " order by a.marketing.code");
            } else {
                q = em.createQuery("select distinct m from Assignment a "
                        + "join a.marketing m "
                        + "where m.startDate <= ?3 and m.endDate >= ?3 and a.campaign.id=?1 and m.enable = true and m.status = true and m.id in "
                        + "(select mc.marketingCustomerPK.marketingId "
                        + "from MarketingCustomer mc where mc.newList = ?2 group by mc.marketingCustomerPK.marketingId) " + where + " "
                        + " order by a.marketing.code");
                q.setParameter(2, listType.equals("newList") ? true : listType.equals("oldList") ? false : null);
        }
        q.setParameter(1, campaignId);
        q.setParameter(3, JSFUtil.toDateWithoutTime(new Date()));
        return q.getResultList();
    }
    
    private String findMarketingByUserGroupAndCampaign(Integer userGroupId) {
        List<Marketing> result = new ArrayList<Marketing>();
        List<MarketingUserGroup> mug = findMarketingUserGroupByUserGroup(userGroupId);
        String whereId = " ";
        if (mug != null && mug.size() > 0) {
            whereId = " and a.marketing.id in (";
            for (MarketingUserGroup m : mug) {
                whereId += m.getMarketingUserGroupPK().getMarketingId() + ",";
            }
            whereId = whereId.substring(0, whereId.length() - 1);
            whereId += ") ";
        } else {
            whereId = " ";
        }
        return whereId;
    }
    
    public void clearReferencForAssign(String uniqueID) {
        String SELECT = "update customer set reference_no_2 = NULL where reference_no_2 = ?1";

        Query q = em.createNativeQuery(SELECT);      
        q.setParameter(1, uniqueID);
        q.executeUpdate();
    }
    
    public void clearAllReferencForAssign() {
        String SELECT = "update customer set reference_no_2 = NULL";

        Query q = em.createNativeQuery(SELECT); 
        q.executeUpdate();
    }

    public String findCampaignNameByMarketingId(Integer marketingId){
        try{
            Query q= null;
            q = em.createQuery("select c.name from Assignment a join a.campaign c where a.marketing.id =:marketingId order by c.id ");
            q.setMaxResults(1);
            q.setParameter("marketingId", marketingId);
            return (String) q.getSingleResult();
        }catch(NoResultException e){
            return "NoCampaingName";
        }
    }

    public List<RegistrationField> findRegistrationFieldByFromIdAndName(String name) {
        Query q = em.createQuery("select object(o) from RegistrationField as o where (o.registrationForm.id = 1103 or o.registrationForm.id = 1104)"
                + " and o.name = ?1");
        q.setParameter(1, name);
        return q.getResultList();
    }
    
}
