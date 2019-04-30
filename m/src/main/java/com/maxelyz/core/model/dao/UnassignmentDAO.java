/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.controller.admin.exceptions.IllegalOrphanException;
import com.maxelyz.core.controller.admin.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.Marketing;
import com.maxelyz.core.model.entity.MarketingUserGroup;
import com.maxelyz.core.model.entity.Unassignment;
import com.maxelyz.core.model.value.admin.CriteriaListValue;
import com.maxelyz.core.model.value.admin.UserAssignmentValue;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import com.maxelyz.utils.JSFUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Oat
 */
@Transactional
public class UnassignmentDAO {

    @PersistenceContext
    private EntityManager em;

    private String listToString(List<Integer> l) {
        StringBuilder sb = new StringBuilder();
        for (int id : l) {
            sb.append(id);
            sb.append(',');
        }
        return sb.toString();
    }

    private void updateAssignmentDetail(
            Unassignment unassignment, UserAssignmentValue user, List<Integer> productIds,
            boolean contactStatusAssigned, boolean contactStatusOpened, boolean contactStatusViewed, boolean contactStatusFollowup, 
            List<Integer> followupIds, List<Integer> openIds, List<Integer> marketingIds, String listType, String role, boolean selectAge, boolean selectName, boolean selectSurname,
            boolean selectGender, boolean selectHomephone, boolean selectMobilePhone, boolean selectOfficePhone, int fromage, int toage, String customerName, String customerSurname,
            String gender, String homePhonePrefix, String officePhonePrefix, String mobilePhonePrefix, List<CriteriaListValue> selectedAdvanceCriteria) {
        
        String productStr = this.listToString(productIds);
        String followupStr = this.listToString(followupIds);
        String openStr = this.listToString(openIds);
        String marketingStr = this.listToString(marketingIds);

        String sqlSelect = "";
        if(role.equals("Manager")) {
            sqlSelect = "update assignment_detail set unassign=1, unassign_date=getdate(), unassignment_id=?1 where id in ("
                + "select distinct TOP("+user.getRecord().intValue()+") ad.id from assignment_detail ad "
                + "join customer cus on cus.id = ad.customer_id "
                + "join assignment a on a.id = ad.assignment_id "
                + "join marketing m on a.marketing_id = m.id "
                + "join assignment_product ap on ap.assignment_id = a.id "
                + "where m.start_date <= ?2 and m.end_date >= ?2 and m.enable = 1 and m.status = 1 "
                + "and a.campaign_id=" + unassignment.getCampaign().getId() + " "
                + "and ad.user_id = " + user.getUser().getId() + " and ad.unassign is null ";
        } else if(role.equals("Supervisor")) {
                sqlSelect = "update assignment_detail set unassign=1, unassign_date=getdate(), unassignment_id=?1 where id in ("
                    + "select distinct TOP("+user.getRecord().intValue()+") ad.id from assignment_detail ad "
                    + "join customer cus on cus.id = ad.customer_id "
                    + "join assignment a on a.id = ad.assignment_id "
                    + "join marketing m on a.marketing_id = m.id "
                    + "left join assignment_supervisor asup on asup.assignment_id = ad.id "
                    + "join assignment_supervisor_detail asd on ad.customer_id = asd.customer_id "
                    + "join assignment_product ap on ap.assignment_id = a.id "
//                  + "where m.start_date <= GETDATE() and m.end_date >= GETDATE() "
                    + "where m.start_date <= ?2 and m.end_date >= ?2 and m.enable = 1 and m.status = 1 "
                    + "and a.campaign_id=" + unassignment.getCampaign().getId() + " "
                    + "and ad.user_id = " + user.getUser().getId() + " and ad.unassign is null and asd.unassign is null ";
        }

        StringBuilder sqlWhere = new StringBuilder();
        String criteria="";
        //where marketing customer list type
        if(listType.equals("newList")){
            sqlWhere.append("and ad.new_list = 1 ");
        } else if(listType.equals("oldList")) {
            sqlWhere.append("and ad.new_list = 0 ");
        }
            
        //where product
        if (productStr.length() > 0) {
            productStr = productStr.substring(0, productStr.length() - 1); //remove last comma
            sqlWhere.append("and ap.product_id in (");
            sqlWhere.append(productStr);
            sqlWhere.append(") ");
        }
        //where marketing
        if (marketingStr.length() > 0) {
            marketingStr = marketingStr.substring(0, marketingStr.length() - 1); //remove last comma
            sqlWhere.append("and a.marketing_id in (");
            sqlWhere.append(marketingStr);
            sqlWhere.append(") ");
        }
        //where contact status
        if (!(contactStatusAssigned || contactStatusViewed || contactStatusOpened || contactStatusFollowup)) {
            sqlWhere.append("and (ad.status <> 'closed') ");
//             contactStatusAssigned = true;
//             contactStatusOpened = true;
//             contactStatusViewed = true;
//             contactStatusFollowup = true;
        }
        if (contactStatusAssigned || contactStatusViewed) {
            String status = "";
            status += contactStatusAssigned ? "'assigned'," : "";           
            status += contactStatusViewed ? "'viewed'," : "";
         
            status = status.substring(0, status.length() - 1);
            sqlWhere.append("and (ad.status in (");
            sqlWhere.append(status);
            sqlWhere.append(") ");
        }
        
        if (contactStatusOpened || openStr.length() > 0) {                
            if (contactStatusAssigned || contactStatusViewed) {
                sqlWhere.append("or ");
            } else {
                sqlWhere.append("and ");
            }
            if (contactStatusFollowup || followupStr.length() > 0) {
                sqlWhere.append("( ");
            }

            sqlWhere.append("(ad.status = 'opened' ");
            if (openStr.length() > 0) {
                sqlWhere.append("and ad.contact_result_id in (");
                if(openStr.length() > 0 ) {
                    openStr = openStr.substring(0, openStr.length() - 1);
                    sqlWhere.append(openStr);
                } 
                sqlWhere.append(") ");                
            }
            sqlWhere.append(") ");
        }

        if (contactStatusFollowup || followupStr.length() > 0) {                
            if (contactStatusAssigned || contactStatusViewed || contactStatusOpened || openStr.length() > 0) {
                sqlWhere.append("or ");
            } else {
                sqlWhere.append("and ");
            }
            sqlWhere.append("(ad.status = 'followup' ");
            if (followupStr.length() > 0) {
                sqlWhere.append("and ad.contact_result_id in (");
                if(followupStr.length() > 0) {
                    followupStr = followupStr.substring(0, followupStr.length() - 1);
                    sqlWhere.append(followupStr);
                }
                sqlWhere.append(") ");                
            }
            sqlWhere.append(") ");
            if (contactStatusOpened || openStr.length() > 0) {
                sqlWhere.append(") ");    
            }
        }

        if (contactStatusAssigned || contactStatusViewed) 
            sqlWhere.append(") ");
        
        // Criteria Customer
        if(selectAge)
            criteria += "and (DATEDIFF(Year,cus.dob,getdate()) >= "+fromage+" and DATEDIFF(Year,cus.dob,getdate()) <= "+toage+" )";
        if(selectName)
            criteria += "and (cus.name like '%" + customerName + "%') ";
        if(selectSurname)
            criteria += "and (cus.surname like '%" + customerSurname + "%') ";
        if(selectGender)
            criteria += "and cus.gender like '"+gender+"%' "; //criteria += "and cus.gender like '"+gender.substring(0, 1)+"%' ";
        if(selectHomephone) {
            if(homePhonePrefix.equals("notspecific")) {
                criteria += "and (cus.home_phone IS NOT NULL and cus.home_phone <> '') ";
            } else if(homePhonePrefix.equals("upc")) {
                criteria +="and (cus.home_phone like '053%' or cus.home_phone like '054%' or cus.home_phone like '055%' or cus.home_phone like '056%' "
                    + "or cus.home_phone like '032%' or cus.home_phone like '034%' or cus.home_phone like '035%' or cus.home_phone like '036%' or cus.home_phone like '037%' or cus.home_phone like '038%' or cus.home_phone like '039%' "
                    + "or cus.home_phone like '042%' or cus.home_phone like '043%' or cus.home_phone like '044%' or cus.home_phone like '045%' "
                    + "or cus.home_phone like '073%' or cus.home_phone like '074%' or cus.home_phone like '075%' or cus.home_phone like '076%' or cus.home_phone like '077%') ";
            } else {
                criteria += "and cus.home_phone like '"+homePhonePrefix.trim()+"%' ";
            }
        }
        if(selectOfficePhone) {
            if(officePhonePrefix.equals("notspecific")) {
                criteria += "and (cus.office_phone IS NOT NULL and cus.office_phone <> '') ";
            } else if(officePhonePrefix.equals("upc")) {
                criteria += "and (cus.office_phone like '053%' or cus.office_phone like '054%' or cus.office_phone like '055%' or cus.office_phone like '056%' "
                    + "or cus.office_phone like '032%' or cus.office_phone like '034%' or cus.office_phone like '035%' or cus.office_phone like '036%' or cus.office_phone like '037%' or cus.office_phone like '038%' or cus.office_phone like '039%' "
                    + "or cus.office_phone like '042%' or cus.office_phone like '043%' or cus.office_phone like '044%' or cus.office_phone like '045%' "
                    + "or cus.office_phone like '073%' or cus.office_phone like '074%' or cus.office_phone like '075%' or cus.office_phone like '076%' or cus.office_phone like '077%') ";
            } else {
                criteria += "and cus.office_phone like '"+officePhonePrefix.trim()+"%' ";
            }
        }
        if(selectMobilePhone) {
            if(mobilePhonePrefix.equals("notspecific")) {
                criteria += "and (cus.mobile_phone IS NOT NULL and cus.mobile_phone <> '') ";
            } else {
                criteria += "and cus.mobile_phone like '"+mobilePhonePrefix.trim()+"%' ";
            }
        }
        
        String advCriteria = createAdvSearchSQLCmd(selectedAdvanceCriteria);
        
        String sql = sqlSelect + sqlWhere + criteria + advCriteria + ")";
//        System.out.println(sql);
        Query q = em.createNativeQuery(sql);
        q.setParameter(1, unassignment.getId());
        q.setParameter(2, JSFUtil.toDateWithoutTime(new Date()));
        q.executeUpdate();
    }
    
    public void updateAssignmentSupervisorDetail(Unassignment unassignment, String role) {
        if (unassignment != null) {
            if(role.equals("Manager")) {
                Query q1 = em.createNativeQuery("update assignment_supervisor_detail set unassign = 1, unassign_date=getdate(), unassignment_id=?1 "
                        + "from assignment_supervisor_detail asd "
                        + "join assignment_detail ad on asd.customer_id = ad.customer_id "
                        + "where ad.unassignment_id = ?1 and asd.unassign is null");                
                q1.setParameter(1, unassignment.getId());
                try{
                    q1.executeUpdate();
                }catch(Exception e){
                    e.printStackTrace();
                }
            } else  if(role.equals("Supervisor")) {
                Query q1 = em.createQuery("select asd.assignmentSupervisorId.id, count(asd.id) from AssignmentSupervisorDetail asd "
                        + "where asd.customerId in (select customerId from AssignmentDetail where unassignment = ?1) "
                        + "and asd.assign = true and asd.unassign is null "
                        + "group by asd.assignmentSupervisorId.id");
                q1.setParameter(1, unassignment);
                List<Object[]> l = q1.getResultList();

                Query q2 = em.createQuery("update AssignmentSupervisor set noUsed = noUsed - ?1 where id = ?2");
                for (Object[] obj : l) {
                    q2.setParameter(1, ((Long)obj[1]).intValue()); //no of unassign record
                    q2.setParameter(2, obj[0]); //assignmentSupervisorId
                    try{
                        q2.executeUpdate();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                
                Query q3 = em.createNativeQuery("update assignment_supervisor_detail set assign = 0, new_list = ad.new_list "
                        + "from assignment_supervisor_detail asd join assignment_detail ad on asd.customer_id = ad.customer_id "
                        + "where ad.unassignment_id = ?1 and asd.assign = 1 and asd.unassign is null ");
                q3.setParameter(1, unassignment.getId());
                try{
                    q3.executeUpdate();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
   
    private void updateMarketing(Unassignment unassignment, UserAssignmentValue user) {//(List<Integer> assignmentDetails) {
        Query q1 = em.createNativeQuery("select a.marketing_id, count(ad.id) from assignment_detail ad "
                + "join assignment a on a.id = ad.assignment_id "
                + "where ad.user_id = "+ user.getUser().getId() +" and ad.unassignment_id = "+unassignment.getId()+" "
                + "group by a.marketing_id");
        List<Object[]> l = q1.getResultList();

        Query q2 = em.createQuery("update Marketing set totalAssigned = totalAssigned - ?1 where id = ?2");
        for (Object[] obj : l) {
            q2.setParameter(1, obj[1]); //no of unassign record
            q2.setParameter(2, obj[0]); //marketing.id
            try{
                q2.executeUpdate();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public void unassignAssignmentSupervisorDetail(Unassignment unassignment, List<UserAssignmentValue> unassignmentUsers, List<Integer> productIds, 
            List<Integer> marketingIds, String listType, boolean selectAge, boolean selectName, boolean selectSurname, boolean selectGender, boolean selectHomephone, 
            boolean selectMobilePhone, boolean selectOfficePhone, int fromage, int toage, String customerName, String customerSurname, String gender, String homePhonePrefix, 
            String officePhonePrefix, String mobilePhonePrefix, List<CriteriaListValue> selectedAdvanceCriteria) {
        String productStr = this.listToString(productIds);
        String marketingStr = this.listToString(marketingIds);
         if (productStr.length() > 0) 
            productStr = productStr.substring(0, productStr.length() - 1); //remove last comma
        if (marketingStr.length() > 0) 
            marketingStr = marketingStr.substring(0, marketingStr.length() - 1); //remove last comma
        
        for(UserAssignmentValue u: unassignmentUsers) {
//            String sqlSelect = "update TOP(?2) assignment_supervisor_detail set unassign=1, unassign_date=getdate(), unassignment_id=?1 "
//                    + "from assignment_supervisor_detail asd "
             String sqlSelect = "update assignment_supervisor_detail set unassign=1, unassign_date=getdate(), unassignment_id=?1 "
                    + "where id in "
                    + "(select distinct TOP(?2) asd.id from assignment_supervisor_detail asd "
                    + "join customer cus on cus.id = asd.customer_id "
                    + "join assignment_supervisor ad on asd.assignment_supervisor_id = ad.id "
                    + "join assignment a on ad.assignment_id = a.id "
                    + "join marketing m on a.marketing_id = m.id "
                    + "join assignment_product ap on ap.assignment_id = a.id "
//                    + "where m.start_date <= GETDATE() and m.end_date >= GETDATE() "
                    + "where m.start_date <= ?3 and m.end_date >= ?3 and m.enable = 1 and m.status = 1 "
                    + "and a.campaign_id = " + unassignment.getCampaign().getId() + " "
                    + "and ad.user_id = " + u.getUser().getId() + " and asd.unassign is null and asd.[assign] = 0 ";
            StringBuilder sqlWhere = new StringBuilder();
            String criteria="";
            //where marketing customer list type
            if(listType.equals("newList")){
                sqlWhere.append("and asd.new_list = 1 ");
            } else if(listType.equals("oldList")) {
                sqlWhere.append("and asd.new_list = 0 ");
            }

            //where product
            if (productStr.length() > 0) {
               // productStr = productStr.substring(0, productStr.length() - 1); //remove last comma
                sqlWhere.append("and ap.product_id in (");
                sqlWhere.append(productStr);
                sqlWhere.append(") ");
            }
            //where marketing
            if (marketingStr.length() > 0) {
               // marketingStr = marketingStr.substring(0, marketingStr.length() - 1); //remove last comma
                sqlWhere.append("and a.marketing_id in (");
                sqlWhere.append(marketingStr);
                sqlWhere.append(") ");
            }
            // Criteria Customer
            if(selectAge)
                criteria += "and (DATEDIFF(Year,cus.dob,getdate()) >= "+fromage+" and DATEDIFF(Year,cus.dob,getdate()) <= "+toage+" )";
            if(selectName)
                criteria += "and (cus.name like '%" + customerName + "%') ";
            if(selectSurname)
                criteria += "and (cus.surname like '%" + customerSurname + "%') ";
            if(selectGender)
                criteria += "and cus.gender like '"+gender+"%' "; //criteria += "and cus.gender like '"+gender.substring(0, 1)+"%' ";
            if(selectHomephone) {
                if(homePhonePrefix.equals("notspecific")) {
                    criteria += "and (cus.home_phone IS NOT NULL and cus.home_phone <> '') ";
                } else if(homePhonePrefix.equals("upc")) {
                    criteria +="and (cus.home_phone like '053%' or cus.home_phone like '054%' or cus.home_phone like '055%' or cus.home_phone like '056%' "
                        + "or cus.home_phone like '032%' or cus.home_phone like '034%' or cus.home_phone like '035%' or cus.home_phone like '036%' or cus.home_phone like '037%' or cus.home_phone like '038%' or cus.home_phone like '039%' "
                        + "or cus.home_phone like '042%' or cus.home_phone like '043%' or cus.home_phone like '044%' or cus.home_phone like '045%' "
                        + "or cus.home_phone like '073%' or cus.home_phone like '074%' or cus.home_phone like '075%' or cus.home_phone like '076%' or cus.home_phone like '077%') ";
                } else {
                    criteria += "and cus.home_phone like '"+homePhonePrefix.trim()+"%' ";
                }
            }
            if(selectOfficePhone) {
                if(officePhonePrefix.equals("notspecific")) {
                    criteria += "and (cus.office_phone IS NOT NULL and cus.office_phone <> '') ";
                } else if(officePhonePrefix.equals("upc")) {
                        criteria += "and (cus.office_phone like '053%' or cus.office_phone like '054%' or cus.office_phone like '055%' or cus.office_phone like '056%' "
                            + "or cus.office_phone like '032%' or cus.office_phone like '034%' or cus.office_phone like '035%' or cus.office_phone like '036%' or cus.office_phone like '037%' or cus.office_phone like '038%' or cus.office_phone like '039%' "
                            + "or cus.office_phone like '042%' or cus.office_phone like '043%' or cus.office_phone like '044%' or cus.office_phone like '045%' "
                            + "or cus.office_phone like '073%' or cus.office_phone like '074%' or cus.office_phone like '075%' or cus.office_phone like '076%' or cus.office_phone like '077%') ";
                    } else {
                        criteria += "and cus.office_phone like '"+officePhonePrefix.trim()+"%' ";
                    }
            }
            if(selectMobilePhone) {
                if(mobilePhonePrefix.equals("notspecific")) {
                    criteria += "and (cus.mobile_phone IS NOT NULL and cus.mobile_phone <> '') ";
                } else {
                    criteria += "and cus.mobile_phone like '"+mobilePhonePrefix.trim()+"%' ";
                }
            }

            String advCriteria = createAdvSearchSQLCmd(selectedAdvanceCriteria);
             
            String sql = sqlSelect + sqlWhere + criteria + advCriteria + ")";
//            System.out.println(sql);
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, unassignment.getId());
            q.setParameter(2, u.getRecord().intValue());
            q.setParameter(3, JSFUtil.toDateWithoutTime(new Date()));
            try{
                q.executeUpdate();
            }catch(Exception e){
                e.printStackTrace();
            }  
        }
    }     
    
    public void updateAsssignmentSupervisor(Unassignment unassignment) {
        if (unassignment != null) {
            Query q = em.createNativeQuery("select ad.id, COUNT(ad.id) from assignment_supervisor_detail asd "
                    + "join assignment_supervisor ad on asd.assignment_supervisor_id = ad.id "
                    + "where asd.unassignment_id = ?1 "
                    + "group by ad.id ");
            q.setParameter(1, unassignment.getId());
            List<Object[]> l = q.getResultList();

            Query q1 = em.createQuery("update AssignmentSupervisor ad set ad.noUsed = ad.noUsed + ?1 where ad.id = ?2");
            for (Object[] obj : l) {
                q1.setParameter(1, obj[1]);
                q1.setParameter(2, obj[0]); 
                try{
                    q1.executeUpdate();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void updateMarketingFromSup(Unassignment unassignment) {
        if (unassignment != null) {
            Query q1 = em.createNativeQuery("select a.marketing_id , count(asd.id) from assignment_supervisor_detail asd "
                    + "join assignment_supervisor ad on asd.assignment_supervisor_id = ad.id "
                    + "join assignment a on a.id = ad.assignment_id "
                    + "where asd.unassignment_id = ?1 "
                    + "group by a.marketing_id ");
            q1.setParameter(1, unassignment.getId());
            List<Object[]> l = q1.getResultList();
            
            Query q2 = em.createQuery("update Marketing set totalAssigned = totalAssigned - ?1 where id = ?2");
            for (Object[] obj : l) {
                q2.setParameter(1, obj[1]); //no of unassign record
                q2.setParameter(2, obj[0]); //marketing.id
                try{
                    q2.executeUpdate();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }  

     public int countUnassignmentSupervisorDetail(Unassignment unassignment) { 
        Query q;   
        q = em.createQuery("select count(asd) from AssignmentSupervisorDetail asd where unassignment = ?1 ");
        q.setParameter(1, unassignment);
        return ((Long) q.getSingleResult()).intValue();
    } 
     
    public synchronized void createUnassignment(String unAssignmentMode, Unassignment unassignment,
            List<UserAssignmentValue> unassignmentUsers, List<Integer> productIds,
            boolean contactStatusAssigned, boolean contactStatusOpened, boolean contactStatusViewed, boolean contactStatusFollowup, 
            List<Integer> followupIds, List<Integer> openIds, List<Integer> marketingIds, String listType , boolean selectAge, boolean selectName, 
            boolean selectSurname, boolean selectGender, boolean selectHomephone, boolean selectMobilePhone, boolean selectOfficePhone, 
            int fromage, int toage, String customerName, String customerSurname, String gender, String homePhonePrefix, String officePhonePrefix, 
            String mobilePhonePrefix, List<CriteriaListValue> selectedAdvanceCriteria) {
        
        this.create(unassignment);
        String role = "";
        if(JSFUtil.getUserSession().getUserGroup().getRole().contains("CampaignManager")) {
            role = "Manager";
        } else {
            role = "Supervisor";
        }
        
        int noUnassign = 0;
        if(unAssignmentMode.equals("supervisor")) {
            for(UserAssignmentValue u: unassignmentUsers) {
                updateAssignmentDetail(unassignment, u, productIds, contactStatusAssigned, contactStatusOpened, contactStatusViewed, contactStatusFollowup, 
                        followupIds, openIds, marketingIds, listType, role, selectAge, selectName, selectSurname, selectGender, selectHomephone, 
                        selectMobilePhone, selectOfficePhone, fromage, toage, customerName, customerSurname, gender, homePhonePrefix, officePhonePrefix, 
                        mobilePhonePrefix, selectedAdvanceCriteria);               
                if(role.equals("Manager")) {
                    this.updateMarketing(unassignment, u);
                }
                noUnassign += u.getRecord();
            }
            updateAssignmentSupervisorDetail(unassignment, role); 
            
            //update unassignment;
            String sql = "update unassignment set no_user = ?1, no_record = ?2 where id = ?3 ";
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, unassignmentUsers.size());
            q.setParameter(2, noUnassign);
            q.setParameter(3, unassignment);
            try{
                q.executeUpdate();
            }catch(Exception e){
                e.printStackTrace();
            }
        } else {
            unassignAssignmentSupervisorDetail(unassignment, unassignmentUsers, productIds, marketingIds,listType, selectAge, selectName, selectSurname, 
                    selectGender, selectHomephone, selectMobilePhone, selectOfficePhone, fromage, toage, customerName, customerSurname, gender, 
                    homePhonePrefix, officePhonePrefix, mobilePhonePrefix, selectedAdvanceCriteria);
            updateAsssignmentSupervisor(unassignment);
            updateMarketingFromSup(unassignment);
            
            //update unassignment;
            int cntUnassgin = this.countUnassignmentSupervisorDetail(unassignment);
            String sql = "update unassignment set no_user = ?1, no_record = ?2 where id = ?3 ";
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, unassignmentUsers.size());
            q.setParameter(2, cntUnassgin);
            q.setParameter(3, unassignment);
            try{
                q.executeUpdate();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        if(role.contains("Manager")) {
             //update marketing customer
            updateMarketingCustomer(unAssignmentMode, unassignment.getId());
        }
    }

    private void updateMarketingCustomer(String unAssignmentMode, Integer unassignmentId){
        String sql = "";
        if(unAssignmentMode.equals("manager")) {
            sql = "update marketing_customer set assign = 0, new_list = asd.new_list "
                    + "from marketing_customer mc "
                    + "inner join assignment_supervisor_detail asd on mc.customer_id = asd.customer_id "
                    + "where asd.unassignment_id = :unassignmentId";
        } else if(unAssignmentMode.equals("supervisor")) {
            sql = "update marketing_customer set assign = 0, new_list = ad.new_list "
                    + "from marketing_customer mc "
                    + "inner join assignment_detail ad on mc.customer_id = ad.customer_id "
                    + "where ad.unassignment_id = :unassignmentId";
        }
        Query q = em.createNativeQuery(sql);
        q.setParameter("unassignmentId", unassignmentId);
        try{
            q.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void create(Unassignment unassignment) {
        em.persist(unassignment);
    }

    public void edit(Unassignment unassignment) throws IllegalOrphanException, NonexistentEntityException, Exception {
        unassignment = em.merge(unassignment);
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        Unassignment unassignment;
        try {
            unassignment = em.getReference(Unassignment.class, id);
            unassignment.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The unassignment with id " + id + " no longer exists.", enfe);
        }
        em.remove(unassignment);

    }

    public List<Unassignment> findUnassignmentEntities() {
        return findUnassignmentEntities(true, -1, -1);
    }

    public List<Unassignment> findUnassignmentEntities(int maxResults, int firstResult) {
        return findUnassignmentEntities(false, maxResults, firstResult);
    }

    private List<Unassignment> findUnassignmentEntities(boolean all, int maxResults, int firstResult) {

        Query q = em.createQuery("select object(o) from Unassignment as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Unassignment findUnassignment(Integer id) {
        return em.find(Unassignment.class, id);
    }

    public int getUnassignmentCount() {
        Query q = em.createQuery("select count(o) from Unassignment as o");
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<Unassignment> findUnassignmentEntitiesByCampaign(int campaignId) {
        Query q = em.createQuery("select object(o) from Unassignment as o where o.campaign.id=?1 order by o.createDate desc");
        q.setParameter(1, campaignId);
        q.setMaxResults(500);
        return q.getResultList();
    }
    
    public List<Unassignment> findUnassignmentEntitiesByCampaignAndMarketing(int campaignId, int ugId) {
        String where  = findMarketingByUserGroup(ugId);
        Query q = em.createQuery("select object(o) from Unassignment as o where o.campaign.id=?1 " + where + " order by o.createDate desc");
        q.setParameter(1, campaignId);
        q.setMaxResults(500);
        return q.getResultList();
    }
    
    private String findMarketingByUserGroup(Integer userGroupId) {
        List<Marketing> result = new ArrayList<Marketing>();
        List<MarketingUserGroup> mug = findMarketingUserGroupByUserGroup(userGroupId);
        String whereId = " ";
        if (mug != null && mug.size() > 0) {
            whereId = " and (";
            for (MarketingUserGroup m : mug) {
                whereId += " o.marketingList like '%" + m.getMarketingUserGroupPK().getMarketingId() + "%' or ";
            }
            whereId = whereId.substring(0, whereId.length() - 3);
            whereId += " ) ";
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
    
    public String createAdvSearchSQLCmd(List<CriteriaListValue> selectedAdvanceCriteria) {
        String strWhere = " ";
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

    public int countAssignmentDetail(String unAssignmentMode, Integer userId, List<Integer> productIds,
            boolean contactStatusAssigned, boolean contactStatusOpened, boolean contactStatusViewed, boolean contactStatusFollowup, 
            List<Integer> followupIds, List<Integer> openIds, List<Integer> marketingIds, String listType, Integer campaignId, boolean selectAge, boolean selectName, boolean selectSurname,
            boolean selectGender, boolean selectHomephone, boolean selectMobilePhone, boolean selectOfficePhone, int fromage, int toage, String customerName, String customerSurname,
            String gender, String homePhonePrefix, String officePhonePrefix, String mobilePhonePrefix, List<CriteriaListValue> selectedAdvanceCriteria) {
        
        String sql = "";
        String role;
        if(JSFUtil.getUserSession().getUserGroup().getRole().contains("CampaignManager")) {
            role = "Manager";
        } else {
            role = "Supervisor";
        }
        if(unAssignmentMode.equals("manager")) {
            String productStr = this.listToString(productIds);
            String marketingStr = this.listToString(marketingIds);
            
            String sqlSelect = "select count(distinct asd.id) from assignment_supervisor_detail asd "
                    + "join customer cus on cus.id = asd.customer_id "
                    + "join assignment_supervisor ad on asd.assignment_supervisor_id = ad.id "
                    + "join assignment a on ad.assignment_id = a.id "
                    + "join marketing m on a.marketing_id = m.id "
                    + "join assignment_product ap on ap.assignment_id = a.id "
                    + "where m.start_date <= ?1 and m.end_date >= ?1 and m.enable = 1 and m.status = 1 "
                    + "and ad.user_id = " + userId + " and a.campaign_id = " + campaignId + " and asd.unassign is null and asd.[assign] = 0 ";
            StringBuilder sqlWhere = new StringBuilder();
            String criteria="";
            //where marketing customer list type
            if(listType.equals("newList")){
                sqlWhere.append("and asd.new_list = 1 ");
            } else if(listType.equals("oldList")) {
                sqlWhere.append("and asd.new_list = 0 ");
            }
            //where product
            if (productStr.length() > 0) {
                productStr = productStr.substring(0, productStr.length() - 1); //remove last comma
                sqlWhere.append("and ap.product_id in (");
                sqlWhere.append(productStr);
                sqlWhere.append(") ");
            }
            //where marketing
            if (marketingStr.length() > 0) {
                marketingStr = marketingStr.substring(0, marketingStr.length() - 1); //remove last comma
                sqlWhere.append("and a.marketing_id in (");
                sqlWhere.append(marketingStr);
                sqlWhere.append(") ");
            }
            // Criteria Customer
            if(selectAge)
                criteria += "and (DATEDIFF(Year,cus.dob,getdate()) >= "+fromage+" and DATEDIFF(Year,cus.dob,getdate()) <= "+toage+" )";
            if(selectName)
                criteria += "and (cus.name like '%" + customerName + "%') ";
            if(selectSurname)
                criteria += "and (cus.surname like '%" + customerSurname + "%') ";
            if(selectGender)
                criteria += "and cus.gender like '"+gender+"%' "; //criteria += "and cus.gender like '"+gender.substring(0, 1)+"%' ";
            if(selectHomephone) {
                if(homePhonePrefix.equals("notspecific")) {
                    criteria += "and (cus.home_phone IS NOT NULL and cus.home_phone <> '') ";
                } else if(homePhonePrefix.equals("upc")) {
                    criteria +="and (cus.home_phone like '053%' or cus.home_phone like '054%' or cus.home_phone like '055%' or cus.home_phone like '056%' "
                        + "or cus.home_phone like '032%' or cus.home_phone like '034%' or cus.home_phone like '035%' or cus.home_phone like '036%' or cus.home_phone like '037%' or cus.home_phone like '038%' or cus.home_phone like '039%' "
                        + "or cus.home_phone like '042%' or cus.home_phone like '043%' or cus.home_phone like '044%' or cus.home_phone like '045%' "
                        + "or cus.home_phone like '073%' or cus.home_phone like '074%' or cus.home_phone like '075%' or cus.home_phone like '076%' or cus.home_phone like '077%') ";
                } else {
                    criteria += "and cus.home_phone like '"+homePhonePrefix.trim()+"%' ";
                }
            }
            if(selectOfficePhone) {
                if(officePhonePrefix.equals("notspecific")) {
                    criteria += "and (cus.office_phone IS NOT NULL and cus.office_phone <> '') ";
                } else if(officePhonePrefix.equals("upc")) {
                        criteria += "and (cus.office_phone like '053%' or cus.office_phone like '054%' or cus.office_phone like '055%' or cus.office_phone like '056%' "
                            + "or cus.office_phone like '032%' or cus.office_phone like '034%' or cus.office_phone like '035%' or cus.office_phone like '036%' or cus.office_phone like '037%' or cus.office_phone like '038%' or cus.office_phone like '039%' "
                            + "or cus.office_phone like '042%' or cus.office_phone like '043%' or cus.office_phone like '044%' or cus.office_phone like '045%' "
                            + "or cus.office_phone like '073%' or cus.office_phone like '074%' or cus.office_phone like '075%' or cus.office_phone like '076%' or cus.office_phone like '077%') ";
                    } else {
                        criteria += "and cus.office_phone like '"+officePhonePrefix.trim()+"%' ";
                    }
            }
            if(selectMobilePhone) {
                 if(mobilePhonePrefix.equals("notspecific")) {
                    criteria += "and (cus.mobile_phone IS NOT NULL and cus.mobile_phone <> '') ";
                } else {
                    criteria += "and cus.mobile_phone like '"+mobilePhonePrefix.trim()+"%' ";
                 }
            }

            //CREATE ADVANCE CRITERIA
            String advCriteria = createAdvSearchSQLCmd(selectedAdvanceCriteria);
            
            sql = sqlSelect + sqlWhere + criteria + advCriteria; 
//            System.out.println(sql);
        } else if(unAssignmentMode.equals("supervisor")) {
            String productStr = this.listToString(productIds);
            String followupStr = this.listToString(followupIds);
            String openStr = this.listToString(openIds);
            String marketingStr = this.listToString(marketingIds);       
            
            String sqlSelect = "";
            if(role.equals("Manager")) {
                sqlSelect = "select count(distinct ad.id) from assignment_detail ad "
                    + "join customer cus on cus.id = ad.customer_id "
                    + "join assignment a on a.id = ad.assignment_id "
                    + "join marketing m on a.marketing_id = m.id "
                    + "join assignment_product ap on ap.assignment_id = a.id "
                    + "where m.start_date <= ?1 and m.end_date >= ?1 and m.enable = 1 and m.status = 1  "
                    + "and ad.user_id=" + userId + " and a.campaign_id=" + campaignId + " and ad.unassign is null ";
            } else if(role.equals("Supervisor")) {
                    sqlSelect = "select count(distinct ad.id) from assignment_detail ad "
                        + "join customer cus on cus.id = ad.customer_id "
                        + "join assignment a on a.id = ad.assignment_id "
                        + "left join assignment_supervisor asup on asup.assignment_id = ad.id "
                        + "join assignment_supervisor_detail asd on ad.customer_id = asd.customer_id "
                        + "join assignment_product ap on ap.assignment_id = a.id "
                        + "join marketing m on a.marketing_id = m.id "
                        + "where m.start_date <= ?1 and m.end_date >= ?1 and m.enable = 1 and m.status = 1 "
                        + "and ad.user_id = " + userId + " and a.campaign_id = " + campaignId + " and ad.unassign is null and asd.unassign is null ";
            }

            StringBuilder sqlWhere = new StringBuilder();
            String criteria="";
            //where marketing customer list type
            if(listType.equals("newList")){
                sqlWhere.append("and ad.new_list = 1 ");
            } else if(listType.equals("oldList")) {
                sqlWhere.append("and ad.new_list = 0 ");
            }
            //where product
            if (productStr.length() > 0) {
                productStr = productStr.substring(0, productStr.length() - 1); //remove last comma
                sqlWhere.append("and ap.product_id in (");
                sqlWhere.append(productStr);
                sqlWhere.append(") ");
            }
            //where marketing
            if (marketingStr.length() > 0) {
                marketingStr = marketingStr.substring(0, marketingStr.length() - 1); //remove last comma
                sqlWhere.append("and a.marketing_id in (");
                sqlWhere.append(marketingStr);
                sqlWhere.append(") ");
            }
            //where contact status
            if (!(contactStatusAssigned || contactStatusViewed || contactStatusOpened || contactStatusFollowup)) {
                sqlWhere.append("and (ad.status <> 'closed') ");
//                contactStatusAssigned = true;
//                contactStatusOpened = true;
//                contactStatusViewed = true;
//                contactStatusFollowup = true;
            }
            if (contactStatusAssigned || contactStatusViewed) {
                String status = "";
                status += contactStatusAssigned ? "'assigned'," : "";           
                status += contactStatusViewed ? "'viewed'," : "";

                status = status.substring(0, status.length() - 1);
                sqlWhere.append("and (ad.status in (");
                sqlWhere.append(status);
                sqlWhere.append(") ");
            }
            
            if (contactStatusOpened || openStr.length() > 0) {                
                if (contactStatusAssigned || contactStatusViewed) {
                    sqlWhere.append("or ");
                } else {
                    sqlWhere.append("and ");
                }
                if (contactStatusFollowup || followupStr.length() > 0) {
                    sqlWhere.append("( ");
                }
                
                sqlWhere.append("(ad.status = 'opened' ");
                if (openStr.length() > 0) {
                    sqlWhere.append("and ad.contact_result_id in (");
                    if(openStr.length() > 0 ) {
                        openStr = openStr.substring(0, openStr.length() - 1);
                        sqlWhere.append(openStr);
                    } 
                    sqlWhere.append(") ");                
                }
                sqlWhere.append(") ");
            }
            
            if (contactStatusFollowup || followupStr.length() > 0) {                
                if (contactStatusAssigned || contactStatusViewed || contactStatusOpened || openStr.length() > 0) {
                    sqlWhere.append("or ");
                } else {
                    sqlWhere.append("and ");
                }
                sqlWhere.append("(ad.status = 'followup' ");
                if (followupStr.length() > 0) {
                    sqlWhere.append("and ad.contact_result_id in (");
                    if(followupStr.length() > 0) {
                        followupStr = followupStr.substring(0, followupStr.length() - 1);
                        sqlWhere.append(followupStr);
                    }
                    sqlWhere.append(") ");                
                }
                sqlWhere.append(") ");
                if (contactStatusOpened || openStr.length() > 0) {
                    sqlWhere.append(") ");    
                }
            }
            
            if (contactStatusAssigned || contactStatusViewed) 
                sqlWhere.append(") ");
            
            // Criteria Customer
            if(selectAge)
                criteria += "and (DATEDIFF(Year,cus.dob,getdate()) >= "+fromage+" and DATEDIFF(Year,cus.dob,getdate()) <= "+toage+" )";
            if(selectName)
                criteria += "and (cus.name like '%" + customerName + "%') ";
            if(selectSurname)
                criteria += "and (cus.surname like '%" + customerSurname + "%') ";
            if(selectGender)
                criteria += "and cus.gender like '"+gender+"%' "; //criteria += "and cus.gender like '"+gender.substring(0, 1)+"%' ";
            if(selectHomephone) {
                 if(homePhonePrefix.equals("notspecific")) {
                    criteria += "and (cus.home_phone IS NOT NULL and cus.home_phone <> '') ";
                } else if(homePhonePrefix.equals("upc")) {
                    criteria +="and (cus.home_phone like '053%' or cus.home_phone like '054%' or cus.home_phone like '055%' or cus.home_phone like '056%' "
                        + "or cus.home_phone like '032%' or cus.home_phone like '034%' or cus.home_phone like '035%' or cus.home_phone like '036%' or cus.home_phone like '037%' or cus.home_phone like '038%' or cus.home_phone like '039%' "
                        + "or cus.home_phone like '042%' or cus.home_phone like '043%' or cus.home_phone like '044%' or cus.home_phone like '045%' "
                        + "or cus.home_phone like '073%' or cus.home_phone like '074%' or cus.home_phone like '075%' or cus.home_phone like '076%' or cus.home_phone like '077%') ";
                } else {
                    criteria += "and cus.home_phone like '"+homePhonePrefix.trim()+"%' ";
                }
            }
            if(selectOfficePhone) {
                 if(officePhonePrefix.equals("notspecific")) {
                    criteria += "and (cus.office_phone IS NOT NULL and cus.office_phone <> '') ";
                } else if(officePhonePrefix.equals("upc")) {
                        criteria += "and (cus.office_phone like '053%' or cus.office_phone like '054%' or cus.office_phone like '055%' or cus.office_phone like '056%' "
                            + "or cus.office_phone like '032%' or cus.office_phone like '034%' or cus.office_phone like '035%' or cus.office_phone like '036%' or cus.office_phone like '037%' or cus.office_phone like '038%' or cus.office_phone like '039%' "
                            + "or cus.office_phone like '042%' or cus.office_phone like '043%' or cus.office_phone like '044%' or cus.office_phone like '045%' "
                            + "or cus.office_phone like '073%' or cus.office_phone like '074%' or cus.office_phone like '075%' or cus.office_phone like '076%' or cus.office_phone like '077%') ";
                    } else {
                        criteria += "and cus.office_phone like '"+officePhonePrefix.trim()+"%' ";
                    }
            }
            if(selectMobilePhone) {
                if(mobilePhonePrefix.equals("notspecific")) {
                    criteria += "and (cus.mobile_phone IS NOT NULL and cus.mobile_phone <> '') ";
                } else {
                    criteria += "and cus.mobile_phone like '"+mobilePhonePrefix.trim()+"%' ";
                }
            }

            //CREATE ADVANCE CRITERIA
            String advCriteria = createAdvSearchSQLCmd(selectedAdvanceCriteria);
            
            sql = sqlSelect + sqlWhere + criteria + advCriteria; 
//            System.out.println(sql);
        }
        try { 
            Query q = em.createNativeQuery(sql);            
            q.setParameter(1, JSFUtil.toDateWithoutTime(new Date()));
            return Integer.parseInt(q.getSingleResult().toString());
        } catch (NoResultException ex) { 
            return 0;
        }
    }
}
