/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;

import com.maxelyz.core.model.entity.AssignmentDetail;
import com.maxelyz.core.model.entity.AssignmentTransfer;
import com.maxelyz.core.model.entity.AssignmentTransferDetail;
import com.maxelyz.core.model.entity.Marketing;
import com.maxelyz.core.model.entity.MarketingUserGroup;
import com.maxelyz.core.model.entity.Users;
import java.util.*;
import javax.persistence.*;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.maxelyz.core.model.value.admin.AssignmentTransferValue;
import com.maxelyz.core.model.value.admin.CriteriaListValue;
import com.maxelyz.core.model.value.admin.UserAssignmentValue;
import com.maxelyz.utils.JSFUtil;
import java.text.SimpleDateFormat;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author vee
 */
@Transactional
public class AssignmentTransferDAO  {


    @PersistenceContext
    private EntityManager em; 

    public void create(AssignmentTransfer assignmentTransfer) {
         em.persist(assignmentTransfer);      
    }
    
    public void create(AssignmentTransfer assignmentTransfer, List<UserAssignmentValue> assignmentUsers, Users toUser, boolean contactStatusAssigned, 
            boolean contactStatusOpened, boolean contactStatusViewed, boolean contactStatusFollowup, Integer marketingId, Integer campaignId, 
            boolean selectAge, boolean selectName, boolean selectSurname, boolean selectGender, boolean selectHomephone, boolean selectMobilePhone, 
            boolean selectOfficePhone, int fromage, int toage, String customerName, String customerSurname, String gender, String homePhonePrefix, 
            String officePhonePrefix, String mobilePhonePrefix, List<CriteriaListValue> selectedAdvanceCriteria) {
        
        this.create(assignmentTransfer);
        
        //update assignmentdetail
        this.assignTransferDetail(assignmentTransfer, assignmentUsers, toUser, contactStatusAssigned, contactStatusOpened, 
                contactStatusViewed, contactStatusFollowup, marketingId, campaignId, selectAge, selectName, selectSurname, selectGender, 
                selectHomephone, selectMobilePhone, selectOfficePhone, fromage, toage, customerName, customerSurname, gender, homePhonePrefix, 
                officePhonePrefix, mobilePhonePrefix, selectedAdvanceCriteria);
    }
    
    public void edit(AssignmentTransfer assignmentTransfer) throws NonexistentEntityException, Exception {
         assignmentTransfer = em.merge(assignmentTransfer);  
    }

    public List<AssignmentTransfer> findAssignmentTransferEntities() {
        return findAssignmentTransferEntities(true, -1, -1);
    }

    public List<AssignmentTransfer> findAssignmentTransferEntities(int maxResults, int firstResult) {
        return findAssignmentTransferEntities(false, maxResults, firstResult);
    }

    private List<AssignmentTransfer> findAssignmentTransferEntities(boolean all, int maxResults, int firstResult) {      
        Query q = em.createQuery("select object(o) from AssignmentTransfer as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
       
    }

    public AssignmentTransfer findAssignmentTransfer(Integer id) {
        return em.find(AssignmentTransfer.class, id);
    }
    
    public List<AssignmentTransferValue> findAssignmentTransferEntitiesByCampaign(int campaignId) {
         String sql = "select new " + AssignmentTransferValue.class.getName() +  " (a.transferDate, a.noCustomer, a.toUser, a.createBy) "
                    + "from AssignmentTransfer a "
                    + "where a.campaign.id=?1 order by a.transferDate desc";
        Query q = em.createQuery(sql);
        q.setParameter(1, campaignId);
        q.setMaxResults(500);
        return q.getResultList();
    }
    
    public List<AssignmentTransferValue> findAssignmentTransferEntitiesByCampaignAndMarketing(int campaignId, int ugId) {
        String where  = findMarketingByUserGroup(ugId);
         String sql = "select new " + AssignmentTransferValue.class.getName() +  " (a.transferDate, a.noCustomer, a.toUser, a.createBy) "
                    + "from AssignmentTransfer a "
                    + "where a.campaign.id=?1 " + where + " order by a.transferDate desc";
        Query q = em.createQuery(sql);
        q.setParameter(1, campaignId);
        q.setMaxResults(500);
        return q.getResultList();
    }
    
    private String findMarketingByUserGroup(Integer userGroupId) {
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
    
    private List<MarketingUserGroup> findMarketingUserGroupByUserGroup(Integer userGroupId) {
        Query q = em.createQuery("select object(mu) from MarketingUserGroup as mu where mu.marketingUserGroupPK.userGroupId = :userGroupId");
        q.setParameter("userGroupId", userGroupId);
        return q.getResultList();
    }
    
    public int getAssignmentTransferCount() {
        Query q = em.createQuery("select count(o) from AssignmentTransfer as o");
        return ((Long) q.getSingleResult()).intValue();
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
//                                Double fromValue = Double.parseDouble(obj.getFromValue());
                                if(obj.getCriteria().equals("between")) {
//                                    Double toValue = Double.parseDouble(obj.getToValue());
                                     strWhere += "and " + fieldName + " between '" + obj.getFromValue() + "' and '" + obj.getToValue() + "' ";
                                     //and flexfield8 BETWEEN 10000 AND 25000
                                } else {
                                    strWhere += "and " + fieldName + " " + obj.getCriteria().trim() + " '" + obj.getFromValue() +"' ";
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
    
    private String getAssignmentDetailSQL(
            Integer fromUserId, boolean contactStatusAssigned, boolean contactStatusOpened, boolean contactStatusViewed,
            boolean contactStatusFollowup, Integer marketingId, Integer campaignId, boolean selectAge, boolean selectName, 
            boolean selectSurname, boolean selectGender, boolean selectHomephone, boolean selectMobilePhone, boolean selectOfficePhone, 
            int fromage, int toage, String customerName, String customerSurname, String gender, String homePhonePrefix, String officePhonePrefix, 
            String mobilePhonePrefix, List<CriteriaListValue> selectedAdvanceCriteria) {

        String sqlSelect = "select distinct object(ad) from AssignmentDetail ad "
                + "join ad.assignmentId a "                
                + "join ad.customerId cus "
                + "where ad.status not like 'closed' and ad.unassign is null and a.campaign.id=" + campaignId + " and "
                + "a.marketing.startDate <= ?1 and a.marketing.endDate >= ?1 and ad.users.id=" + fromUserId ;
        StringBuilder sqlWhere = new StringBuilder();
        
        //search marketing id and a.marketing.id =" + marketingId + "
        if (marketingId != 0) {
            sqlSelect += " and a.marketing.id =" + marketingId;
        }
        
        //search customer name
//        if (customerName != null && customerName.toString().trim().length() > 0) {
//            sqlSelect += " and ad.customerName like '%" + customerName + "%' ";
//        }
        
        //where contact status
        if (!(contactStatusAssigned || contactStatusOpened || contactStatusViewed || contactStatusFollowup)) {
             contactStatusAssigned = true;
             contactStatusOpened = true;
             contactStatusViewed = true;
             contactStatusFollowup = true;
        }
        if (contactStatusAssigned || contactStatusOpened || contactStatusViewed || contactStatusFollowup) {
            String status = "";
            status += contactStatusAssigned ? "'assigned'," : "";
            status += contactStatusOpened ? "'opened'," : "";
            status += contactStatusViewed ? "'viewed'," : "";
            status += contactStatusFollowup ? "'followup'," : "";
            status = status.substring(0, status.length() - 1);
            sqlWhere.append(" and ad.status in (");
            sqlWhere.append(status);
            sqlWhere.append(") ");
        }
        
        // Criteria Customer
        String criteria = "";
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
                criteria += "and (cus.homePhone IS NOT NULL and cus.homePhone <> '') ";
            } else if(homePhonePrefix.equals("upc")) {
                criteria +="and (cus.homePhone like '053%' or cus.homePhone like '054%' or cus.homePhone like '055%' or cus.homePhone like '056%' "
                    + "or cus.homePhone like '032%' or cus.homePhone like '034%' or cus.homePhone like '035%' or cus.homePhone like '036%' or cus.homePhone like '037%' or cus.homePhone like '038%' or cus.homePhone like '039%' "
                    + "or cus.homePhone like '042%' or cus.homePhone like '043%' or cus.homePhone like '044%' or cus.homePhone like '045%' "
                    + "or cus.homePhone like '073%' or cus.homePhone like '074%' or cus.homePhone like '075%' or cus.homePhone like '076%' or cus.homePhone like '077%') ";
            } else {
                criteria += "and cus.homePhone like '"+homePhonePrefix.trim()+"%' ";
            }
        }
        if(selectOfficePhone) {
            if(officePhonePrefix.equals("notspecific")) {
                criteria += "and (cus.officePhone IS NOT NULL and cus.officePhone <> '') ";
            } else if(officePhonePrefix.equals("upc")) {
                    criteria += "and (cus.officePhone like '053%' or cus.officePhone like '054%' or cus.officePhone like '055%' or cus.officePhone like '056%' "
                        + "or cus.officePhone like '032%' or cus.officePhone like '034%' or cus.officePhone like '035%' or cus.officePhone like '036%' or cus.officePhone like '037%' or cus.officePhone like '038%' or cus.officePhone like '039%' "
                        + "or cus.officePhone like '042%' or cus.officePhone like '043%' or cus.officePhone like '044%' or cus.officePhone like '045%' "
                        + "or cus.officePhone like '073%' or cus.officePhone like '0phone74%' or cus.officePhone like '075%' or cus.officePhone like '076%' or cus.officePhone like '077%') ";
                } else {
                    criteria += "and cus.officePhone like '"+officePhonePrefix.trim()+"%' ";
                }
        }
        if(selectMobilePhone) {
             if(mobilePhonePrefix.equals("notspecific")) {
                criteria += "and (cus.mobilePhone IS NOT NULL and cus.mobilePhone <> '') ";
             } else {
                criteria += "and cus.mobilePhone like '"+mobilePhonePrefix.trim()+"%' ";
             }
        }

        //CREATE ADVANCE CRITERIA
        String advCriteria = createAdvSearchSQLCmd(selectedAdvanceCriteria);
        
        String sql = sqlSelect + sqlWhere + criteria + advCriteria;
//        System.out.println(sqlSelect + sqlWhere + criteria + advCriteria);
        return sql;
    }

//    public List<AssignmentDetail> findAssignmentDetail(Integer fromUserId, String customerName, boolean contactStatusAssigned, boolean contactStatusOpened, boolean contactStatusViewed, boolean contactStatusFollowup, Integer marketingId, Integer campaignId) {
//        String sql = this.getAssignmentDetailSQL(fromUserId, customerName,contactStatusAssigned, contactStatusOpened, contactStatusViewed, contactStatusFollowup, marketingId, campaignId);
//        Query q = em.createQuery(sql);
//        q.setParameter(1, JSFUtil.toDateWithoutTime(new Date()));
//       // q.setMaxResults(500);
//        return q.getResultList();
//    }
    
    public List<AssignmentDetail> findAssignmentDetail(Integer fromUserId, boolean contactStatusAssigned, boolean contactStatusOpened, 
            boolean contactStatusViewed, boolean contactStatusFollowup, Integer marketingId, Integer campaignId, boolean selectAge, boolean selectName, 
            boolean selectSurname, boolean selectGender, boolean selectHomephone, boolean selectMobilePhone, boolean selectOfficePhone, 
            int fromage, int toage, String customerName, String customerSurname, String gender, String homePhonePrefix, String officePhonePrefix, 
            String mobilePhonePrefix, List<CriteriaListValue> selectedAdvanceCriteria) {
        
        String sql = this.getAssignmentDetailSQL(fromUserId, contactStatusAssigned, contactStatusOpened, contactStatusViewed, 
                contactStatusFollowup, marketingId, campaignId, selectAge, selectName, selectSurname, selectGender, 
                selectHomephone, selectMobilePhone, selectOfficePhone, fromage, toage, customerName, customerSurname, gender, homePhonePrefix, 
                officePhonePrefix, mobilePhonePrefix, selectedAdvanceCriteria);
        
        Query q = em.createQuery(sql);
        q.setParameter(1, JSFUtil.toDateWithoutTime(new Date()));
       // q.setMaxResults(500);
        return q.getResultList();
    }
    
    public List<AssignmentDetail> findAssignmentDetailForTransfer(Integer fromUserId, boolean contactStatusAssigned, boolean contactStatusOpened, 
            boolean contactStatusViewed, boolean contactStatusFollowup, Integer marketingId, Integer campaignId, boolean selectAge, boolean selectName, 
            boolean selectSurname, boolean selectGender, boolean selectHomephone, boolean selectMobilePhone, boolean selectOfficePhone, 
            int fromage, int toage, String customerName, String customerSurname, String gender, String homePhonePrefix, String officePhonePrefix, 
            String mobilePhonePrefix, List<CriteriaListValue> selectedAdvanceCriteria, long noRecord) {
        
        String sql = this.getAssignmentDetailSQL(fromUserId, contactStatusAssigned, contactStatusOpened, contactStatusViewed, contactStatusFollowup, 
                marketingId, campaignId, selectAge, selectName, selectSurname, selectGender, selectHomephone, selectMobilePhone, selectOfficePhone, 
                fromage, toage, customerName, customerSurname, gender, homePhonePrefix, officePhonePrefix, mobilePhonePrefix, selectedAdvanceCriteria);
    
        Query q = em.createQuery(sql);
        q.setParameter(1, JSFUtil.toDateWithoutTime(new Date()));
        q.setMaxResults((int)noRecord);
        return q.getResultList();
    }
    
    private synchronized void assignTransferDetail(AssignmentTransfer assignmentTransfer, List<UserAssignmentValue> assignmentUsers, 
            Users toUser, boolean contactStatusAssigned, boolean contactStatusOpened, boolean contactStatusViewed, 
            boolean contactStatusFollowup, Integer marketingId, Integer campaignId, boolean selectAge, boolean selectName, 
            boolean selectSurname, boolean selectGender, boolean selectHomephone, boolean selectMobilePhone, boolean selectOfficePhone, 
            int fromage, int toage, String customerName, String customerSurname, String gender, String homePhonePrefix, String officePhonePrefix, 
            String mobilePhonePrefix, List<CriteriaListValue> selectedAdvanceCriteria) {
        
        for (UserAssignmentValue u : assignmentUsers) {
            List<AssignmentDetail> assignmentDetailList = findAssignmentDetailForTransfer(u.getUser().getId(), contactStatusAssigned, contactStatusOpened, 
                    contactStatusViewed, contactStatusFollowup, marketingId, campaignId, selectAge, selectName, selectSurname, selectGender, 
                selectHomephone, selectMobilePhone, selectOfficePhone, fromage, toage, customerName, customerSurname, gender, homePhonePrefix, 
                officePhonePrefix, mobilePhonePrefix, selectedAdvanceCriteria, u.getRecord());

            for (AssignmentDetail aid : assignmentDetailList) {
                Query q = em.createQuery("select object(ad) from AssignmentDetail as ad where ad.id = ?1");
                q.setParameter(1, aid.getId());
                AssignmentDetail assignmentDetail = (AssignmentDetail) q.getSingleResult();

                // create Assignment Transfer Detail
                AssignmentTransferDetail assignmentTransferDetail = new AssignmentTransferDetail();
                assignmentTransferDetail.setAssignmentTransfer(assignmentTransfer);
                assignmentTransferDetail.setCustomer(assignmentDetail.getCustomerId());
                em.persist(assignmentTransferDetail);

                // update Assignment Detail
                assignmentDetail.setUsers(toUser);
                assignmentDetail.setTransfer(true);
                assignmentDetail.setTransferDate(new Date());
                assignmentDetail.setAssignmentTransferDetail(assignmentTransferDetail);
                em.merge(assignmentDetail);
            }
            
        }
    }

}
