/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao;

import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.dao.exceptions.PreexistingEntityException;
import com.maxelyz.core.model.entity.AssignmentDetail;
import com.maxelyz.core.model.entity.Campaign;
import com.maxelyz.core.model.entity.Users;
import com.maxelyz.core.model.value.admin.CriteriaListValue;
import com.maxelyz.core.model.value.admin.UserAssignmentValue;
import com.maxelyz.utils.JSFUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Oat
 */
@Transactional
public class AssignmentDetailDAO {

    @PersistenceContext
    private EntityManager em;

    public void create(AssignmentDetail assignmentDetail) throws PreexistingEntityException, Exception {
        em.persist(assignmentDetail);
    }

    public void edit(AssignmentDetail assignmentDetail) throws NonexistentEntityException, Exception {
        assignmentDetail = em.merge(assignmentDetail);
    }

    public void editDmc(AssignmentDetail assignmentDetail, boolean dmc) throws NonexistentEntityException, Exception {
        Query q = em.createQuery("update AssignmentDetail set dmc = ?1, updateDate = GETDATE() where id =?2");
        q.setParameter(1, dmc);
        q.setParameter(2, assignmentDetail.getId());
        q.executeUpdate();
    }

    public void editStatus(AssignmentDetail assignmentDetail,String status) throws NonexistentEntityException, Exception {
        Query q = em.createQuery("update AssignmentDetail set status = ?1, updateDate = GETDATE() where id =?2");// and status <> 'assigned'");
//        Query q = em.createQuery("update AssignmentDetail set status = ?1, updateDate = GETDATE() where id =?2 and status in ('viewed','opened','followup')");
        q.setParameter(1, status);
        q.setParameter(2, assignmentDetail.getId());
        q.executeUpdate();
    }
    
    public void setOpenStatus(AssignmentDetail assignmentDetail) throws NonexistentEntityException, Exception {
        Query q = em.createQuery("update AssignmentDetail set status = 'opened', newList = ?1, updateDate = GETDATE() where id =?2 and status in ('viewed')");
        q.setParameter(1, assignmentDetail.getNewList());
        q.setParameter(2, assignmentDetail.getId());
        
        q.executeUpdate();
    }
    
    public void setNewToOldList(AssignmentDetail assignmentDetail) throws NonexistentEntityException, Exception {
        Query q = em.createQuery("update AssignmentDetail set newList = ?1, updateDate = GETDATE() where id =?2");
        q.setParameter(1, assignmentDetail.getNewList());
        q.setParameter(2, assignmentDetail.getId());
        
        q.executeUpdate();
    }
    
    public void saveMaxCallAutoClose(AssignmentDetail assignmentDetail) throws NonexistentEntityException, Exception {
        Query q = em.createQuery("update AssignmentDetail set status = 'closed', contactResult = ?1, contactResultId = ?2, updateDate = GETDATE() "
                + "where id =?3 and status <> 'assigned'");
//                + "where id =?3 and status in ('viewed','opened','followup','closed')");
        
        q.setParameter(1, assignmentDetail.getContactResult());        
        q.setParameter(2, assignmentDetail.getContactResultId());
        q.setParameter(3, assignmentDetail.getId());

        q.executeUpdate();
    }

    public void updateAssignmentDetail(AssignmentDetail ad) throws NonexistentEntityException, Exception {
        Query q = em.createQuery("update AssignmentDetail set status = ?1, saleResult = ?2, followupsaleDate = ?3, updateDate = GETDATE() where id = ?5");
        q.setParameter(1, ad.getStatus());
        q.setParameter(2, ad.getSaleResult());
        q.setParameter(3, ad.getFollowupsaleDate());
        q.setParameter(5, ad.getId());
        q.executeUpdate();
    }

    public void updateStatus(AssignmentDetail assignmentDetail,String status) throws NonexistentEntityException, Exception {
        Query q = em.createQuery("update AssignmentDetail set status = ?1, updateDate = GETDATE() where id =?2");
        q.setParameter(1, status);
        q.setParameter(2, assignmentDetail.getId());
        q.executeUpdate();
    }

    public void saveCall(Integer id, Integer count) throws NonexistentEntityException, Exception {
        Query q = em.createQuery("update AssignmentDetail set callAttempt = ?1, updateDate = GETDATE() where id =?2");
        q.setParameter(1, count);
        q.setParameter(2, id);
        q.executeUpdate();
    }

    public void saveCall2(Integer id, Integer count2) throws NonexistentEntityException, Exception {
        Query q = em.createQuery("update AssignmentDetail set callAttempt2 = ?1, updateDate = GETDATE() where id =?2");
        q.setParameter(1, count2);
        q.setParameter(2, id);
        q.executeUpdate();
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        AssignmentDetail assignmentDetail;
        try {
            assignmentDetail = em.getReference(AssignmentDetail.class, id);
            assignmentDetail.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The assignmentDetail with id " + id + " no longer exists.", enfe);
        }
        em.remove(assignmentDetail);
    }

    public List<AssignmentDetail> findAssignmentDetailEntities() {
        return findAssignmentDetailEntities(true, -1, -1);
    }

    public List<AssignmentDetail> findAssignmentDetailEntities(int maxResults, int firstResult) {
        return findAssignmentDetailEntities(false, maxResults, firstResult);
    }

    private List<AssignmentDetail> findAssignmentDetailEntities(boolean all, int maxResults, int firstResult) {
        Query q = em.createQuery("select object(o) from AssignmentDetail as o");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public AssignmentDetail findAssignmentDetail(Integer id) {
        return em.find(AssignmentDetail.class, id);
    }

    public int getAssignmentDetailCount() {
        return ((Long) em.createQuery("select count(o) from AssignmentDetail as o").getSingleResult()).intValue();
    }

    public List<AssignmentDetail> findAssignmentDetailPooling(Integer userId, Integer marketingId) {
        Query q = em.createQuery("select object(o) from AssignmentDetail as o"
                + " where o.assignmentId.marketing.id = ?1"
                + " and o.users.id = ?2"
                + " and o.assignmentId.assignmentType like ?3"
                + " and (o.status = 'assigned' or o.status = 'viewed')"
                + " and (o.unassign is null or o.unassign = false)");
        q.setParameter(1, marketingId);
        q.setParameter(2, userId);
        q.setParameter(3, "pooling%");
        
        return q.getResultList();
    }

    public UserAssignmentValue findAssignmentDetailStatus(Users users, Campaign campaign, String assignmentMode) {
         Query q;
        if(assignmentMode.equals("manager")) {
            q = em.createQuery("select NEW "
                +UserAssignmentValue.class.getName()  
                + "(s.userId, (SUM(s.noCustomer) - SUM(s.noUsed)) as remain, 0) "
                + "from AssignmentSupervisor as s "
                + "join s.assignmentId a "
                + "where a.marketing.startDate <= ?3 and a.marketing.endDate >= ?3 and a.campaign = ?2 and s.userId.id = ?1 "
                + "group by s.userId.id");
        } else {
            q = em.createQuery("select NEW "
                +UserAssignmentValue.class.getName()  
                + "(o.users, "
                + "(select COUNT(ad.status) from AssignmentDetail ad join ad.assignmentId a where a.marketing.startDate <= ?3 and a.marketing.endDate >= ?3 "
                + "and ad.status not like 'closed' and a.campaign = ?2 and ad.users.id =?1 and ad.unassign is null group by ad.users.id) as totals, "
                + "(select COUNT(ad.status) from AssignmentDetail ad join ad.assignmentId a where a.marketing.startDate <= ?3 and a.marketing.endDate >= ?3 "
                + "and (ad.status like 'assigned' or ad.status like 'viewed') and a.campaign = ?2 and ad.users.id =?1 and ad.unassign is null group by ad.users.id) as new1, "
                + "(select COUNT(ad.status) from AssignmentDetail ad join ad.assignmentId a where a.marketing.startDate <= ?3 and a.marketing.endDate >= ?3 "
                + "and (ad.status like 'followup' or ad.status like 'opened') and a.campaign = ?2 and ad.users.id =?1 and ad.unassign is null group by ad.users.id) as used, "
                + "0)"
                + "from AssignmentDetail as o "
                + "where o.status not like 'closed' and o.unassign is null and o.users.id = ?1 "
                + "group by o.users.id");
        }
        q.setParameter(1, users.getId());
        q.setParameter(2, campaign);
        q.setParameter(3, JSFUtil.toDateWithoutTime(new Date()));
        try { 
            return (UserAssignmentValue)q.getSingleResult();  
        } catch (NoResultException ex) { 
            return null;
        }
    }
    
    public List<UserAssignmentValue> countAssignSelectedUsers(List<Users> selectedUser, Campaign campaign, String assignmentMode) {
        List<UserAssignmentValue> selectedAssignmentUser = new ArrayList<UserAssignmentValue>();
        if(assignmentMode.equals("manager")) {
            if (selectedUser != null) {
                for (Users u : selectedUser) {
                    UserAssignmentValue uCnt = findAssignmentDetailStatus(u,campaign,assignmentMode);
                    UserAssignmentValue value;
                    if(uCnt == null) {
                        value =  new UserAssignmentValue(true,u,(long)0,(long)0);
                    } else {
                        value =  new UserAssignmentValue(true,u,uCnt.getRecord()==null?0:uCnt.getRecord(),uCnt.getTotals()==null?0:uCnt.getTotals());
                    }
                    selectedAssignmentUser.add(value);
                }
            }
        } else {
            if (selectedUser != null) {
                for (Users u : selectedUser) {
                    UserAssignmentValue uCnt = findAssignmentDetailStatus(u,campaign,assignmentMode);
                    UserAssignmentValue value;
                    if(uCnt == null) {
                        value =  new UserAssignmentValue(true,u,(long)0,(long)0,(long)0,(long)0);
                    } else {
                        value =  new UserAssignmentValue(true,u,(long)0,uCnt.getTotals()==null?0:uCnt.getTotals(),uCnt.getNew1()==null?0:uCnt.getNew1(),uCnt.getUsed()==null?0:uCnt.getUsed());
                    }
                    selectedAssignmentUser.add(value);
                }
            }
        }
        return selectedAssignmentUser;
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
    
    public UserAssignmentValue countAssignRemain(Integer fromUserId, boolean contactStatusAssigned, boolean contactStatusOpened, 
            boolean contactStatusViewed, boolean contactStatusFollowup, Integer marketingId, Integer campaignId, boolean selectAge, boolean selectName, 
            boolean selectSurname, boolean selectGender, boolean selectHomephone, boolean selectMobilePhone, boolean selectOfficePhone, 
            int fromage, int toage, String customerName, String customerSurname, String gender, String homePhonePrefix, String officePhonePrefix, 
            String mobilePhonePrefix, List<CriteriaListValue> selectedAdvanceCriteria) {
        
        String sqlSelect = "select NEW "
                +UserAssignmentValue.class.getName()  
                + "(o.users, COUNT(o), 0)"
                + "from AssignmentDetail as o "
                + "join o.assignmentId a "
                + "join o.customerId cus "
                + "where o.status not like 'closed' and o.unassign is null and a.campaign.id=" + campaignId + " and "
                + "a.marketing.startDate <= ?1 and a.marketing.endDate >= ?1 and o.users.id=" + fromUserId ;
       StringBuilder sqlWhere = new StringBuilder();
        
        //search marketing id and a.marketing.id =" + marketingId + "
        if (marketingId != 0) {
            sqlSelect += " and a.marketing.id =" + marketingId;
        }
        
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
            sqlWhere.append(" and o.status in (");
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
                        + "or cus.officePhone like '073%' or cus.officePhone like '074%' or cus.officePhone like '075%' or cus.officePhone like '076%' or cus.officePhone like '077%') ";
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
        
        String groupBy = " group by o.users.id";
        String sql = sqlSelect + sqlWhere + criteria + advCriteria + groupBy;
//        System.out.println(sqlSelect + sqlWhere + criteria + advCriteria + groupBy);
        try { 
            Query q = em.createQuery(sql);
            q.setParameter(1, JSFUtil.toDateWithoutTime(new Date()));
            return (UserAssignmentValue)q.getSingleResult();  
        } catch (NoResultException ex) { 
            return null;
        }
    }
    
//    public UserAssignmentValue countAssignRemain(Integer fromUserId, String customerName, boolean contactStatusAssigned, boolean contactStatusOpened, boolean contactStatusViewed, boolean contactStatusFollowup, Integer marketingId, Integer campaignId) {
//        String sqlSelect = "select NEW "
//                +UserAssignmentValue.class.getName()  
//                + "(o.users, COUNT(o) , 0)"
//                + "from AssignmentDetail as o "
//                + "join o.assignmentId a "
//                + "where o.status not like 'closed' and o.unassign is null and a.campaign.id=" + campaignId + " and "
//                + "a.marketing.startDate <= ?1 and a.marketing.endDate >= ?1 and o.users.id=" + fromUserId ;
//       StringBuilder sqlWhere = new StringBuilder();
//        
//        //search marketing id and a.marketing.id =" + marketingId + "
//        if (marketingId != 0) {
//            sqlSelect += " and a.marketing.id =" + marketingId;
//        }
//        //search customer name
//        if (customerName != null && customerName.toString().trim().length() > 0) {
//            sqlSelect += " and o.customerName like '%" + customerName + "%' ";
//        }
//        //where contact status
//        if (!(contactStatusAssigned || contactStatusOpened || contactStatusViewed || contactStatusFollowup)) {
//             contactStatusAssigned = true;
//             contactStatusOpened = true;
//             contactStatusViewed = true;
//             contactStatusFollowup = true;
//        }
//        if (contactStatusAssigned || contactStatusOpened || contactStatusViewed || contactStatusFollowup) {
//            String status = "";
//            status += contactStatusAssigned ? "'assigned'," : "";
//            status += contactStatusOpened ? "'opened'," : "";
//            status += contactStatusViewed ? "'viewed'," : "";
//            status += contactStatusFollowup ? "'followup'," : "";
//            status = status.substring(0, status.length() - 1);
//            sqlWhere.append(" and o.status in (");
//            sqlWhere.append(status);
//            sqlWhere.append(") ");
//        }
//        String groupBy = " group by o.users.id";
//        String sql = sqlSelect + sqlWhere + groupBy;
//        System.out.println(sql);
//        try { 
//            Query q = em.createQuery(sql);
//            q.setParameter(1, JSFUtil.toDateWithoutTime(new Date()));
//            return (UserAssignmentValue)q.getSingleResult();  
//        } catch (NoResultException ex) { 
//            return null;
//        }
//    }

    public int countRecordRemain(Integer fromUserId, Integer campaignId) {
        String sql = "select count(o) "
                + "from AssignmentDetail as o "
                + "join o.assignmentId a "
                + "where o.status not like 'closed' and o.unassign is null and a.campaign.id=" + campaignId + " and "
                + "a.marketing.startDate <= ?1 and a.marketing.endDate >= ?1 and o.users.id=" + fromUserId ;

        try { 
            Query q = em.createQuery(sql);
            q.setParameter(1, JSFUtil.toDateWithoutTime(new Date()));
            return ((Long) q.getSingleResult()).intValue();
        } catch (NoResultException ex) { 
            return 0;
        }
    }
    
    public List<AssignmentDetail> findAssignmentDetailBySearch(String refNo, String customerName, String telephoneNo, Integer campaignId, Integer userGroupId, Integer userId, Boolean contactStatusAssigned, Boolean contactStatusViewed, Boolean contactStatusOpened, Boolean contactStatusFollowup, Boolean ontactStatusClosed, int firstResult, int maxResults) {
        List<AssignmentDetail> list = new ArrayList<AssignmentDetail>();
        String sql = "select distinct object(ad) from AssignmentDetail as ad "
                + "join ad.assignmentId a ";
        String sqlWhere = "where a.campaign.enable = true and a.campaign.endDate >= ?1 and ad.unassign is null ";
        if(!refNo.isEmpty() && refNo != null) {
            sqlWhere += "and ad.customerId.referenceNo like '%"+refNo+"%' ";
        }
        if(!customerName.isEmpty() && customerName != null) {
            sqlWhere += "and ad.customerName like '%"+customerName+"%' ";
        }
        if(!telephoneNo.isEmpty() && telephoneNo != null) {
            sqlWhere += "and (ad.customerId.homePhone like '%"+telephoneNo.trim()+"%' or ad.customerId.officePhone like '%"+telephoneNo.trim()+"%' or ad.customerId.mobilePhone like '%"+telephoneNo.trim()+"%') ";
        }
        if(campaignId!= 0 && campaignId != null) {
            sqlWhere += "and a.campaign.id = "+campaignId+" ";
        }
        if(userGroupId!= 0 && userGroupId != null) {
            sqlWhere += "and ad.users.userGroup.id = "+userGroupId+" ";
        }
        if(userId!= 0 && userId != null) {
            sqlWhere += "and ad.users.id = "+userId+" ";
        }
        //where contact status
        if (contactStatusAssigned || contactStatusOpened || contactStatusViewed  || contactStatusFollowup || ontactStatusClosed) {
            String status = "";
            status += contactStatusAssigned ? "'assigned'," : "";
            status += contactStatusOpened ? "'opened'," : "";
            status += contactStatusViewed ? "'viewed'," : "";
            status += contactStatusFollowup ? "'followup'," : "";
            status += ontactStatusClosed ? "'closed'," : "";
            status = status.substring(0, status.length() - 1);
            sqlWhere += "and ad.status in ("+status+")";
        }

        Query q = em.createQuery(sql + sqlWhere);
        q.setParameter(1, JSFUtil.toDateWithoutTime(new Date()));
        if (maxResults > 0) {
            q.setMaxResults(maxResults);
        }
        if (firstResult >= 0) {
            q.setFirstResult(firstResult);
        }
        list = (List<AssignmentDetail>) q.getResultList();
        return list;
    }
    
     public Integer countAssignmentDetailBySearch(String refNo, String customerName, String telephoneNo, Integer campaignId, Integer userGroupId, Integer userId, Boolean contactStatusAssigned, Boolean contactStatusViewed, Boolean contactStatusOpened, Boolean contactStatusFollowup, Boolean ontactStatusClosed) {
        List<AssignmentDetail> list = new ArrayList<AssignmentDetail>();
        String sql = "select count(*) from AssignmentDetail as ad "
                + "join ad.assignmentId a ";
        String sqlWhere = "where ad.unassign is null ";
        if(!refNo.isEmpty() && refNo != null) {
            sqlWhere += "and ad.customerId.referenceNo like '%"+refNo+"%' ";
        }
        if(!customerName.isEmpty() && customerName != null) {
            sqlWhere += "and ad.customerName like '%"+customerName+"%' ";
        }
        if(!telephoneNo.isEmpty() && telephoneNo != null) {
            sqlWhere += "and (a.customer.homePhone like '%"+telephoneNo.trim()+"%' or a.customer.officePhone like '%"+telephoneNo.trim()+"%' or a.customer.mobilePhone like '%"+telephoneNo.trim()+"%') ";
        }
        if(campaignId!= 0 && campaignId != null) {
            sqlWhere += "and a.campaign.id = "+campaignId+" ";
        }
        if(userGroupId!= 0 && userGroupId != null) {
            sqlWhere += "and ad.users.userGroup.id = "+userGroupId+" ";
        }
        if(userId!= 0 && userId != null) {
            sqlWhere += "and ad.users.id = "+userId+" ";
        }
        //where contact status
        if (contactStatusAssigned || contactStatusOpened || contactStatusViewed  || contactStatusFollowup || ontactStatusClosed) {
            String status = "";
            status += contactStatusAssigned ? "'assigned'," : "";
            status += contactStatusOpened ? "'opened'," : "";
            status += contactStatusViewed ? "'viewed'," : "";
            status += contactStatusFollowup ? "'followup'," : "";
            status += ontactStatusClosed ? "'closed'," : "";
            status = status.substring(0, status.length() - 1);
            sqlWhere += "and ad.status in ("+status+")";
        }

        Query q = em.createQuery(sql + sqlWhere);
        return Integer.valueOf(((Long) q.getSingleResult()).intValue());
    }
     
     public void editMaxcall(AssignmentDetail assignmentDetail) throws NonexistentEntityException, Exception {
        Query q = em.createQuery("update AssignmentDetail set status = ?1, maxCall = ?3, maxCall2 = ?4, updateDate = GETDATE() where id =?2");
        q.setParameter(1, assignmentDetail.getStatus());
        q.setParameter(2, assignmentDetail.getId());
        q.setParameter(3, assignmentDetail.getMaxCall());
        q.setParameter(4, assignmentDetail.getMaxCall2());
        q.executeUpdate();
    }

    public void saveContactStatus(AssignmentDetail assignmentDetail) throws NonexistentEntityException, Exception {
        Query q = em.createQuery("update AssignmentDetail set contactStatus = ?1, latestContactStatus = ?2, status = ?3, contactResult = ?4, contactDate = ?5, updateDate = GETDATE(), contactResultId = ?6  where id =?7");
        q.setParameter(1, assignmentDetail.getContactStatus());
        q.setParameter(2, assignmentDetail.getLatestContactStatus());
        q.setParameter(3, assignmentDetail.getStatus());
        q.setParameter(4, assignmentDetail.getContactResult());
        q.setParameter(5, assignmentDetail.getContactDate());
        q.setParameter(6, assignmentDetail.getContactResultId());
        q.setParameter(7, assignmentDetail.getId());
        q.executeUpdate();
    }

    public void setNewListToOldList(Integer asId, Integer customerId) throws NonexistentEntityException, Exception {
        Query q = em.createQuery("update AssignmentSupervisorDetail as o set o.newList = 0 where o.assignmentSupervisorId.id = ?1 and o.customerId.id = ?2");
        q.setParameter(1, asId);
        q.setParameter(2, customerId);
        q.executeUpdate();
    }
    
    public List<AssignmentDetail> findAssignmentDetailByAgent(String campaignId, String userId, String mode) {
        String sql = "select distinct object(ad) from AssignmentDetail as ad "
                + "join ad.assignmentId a ";
        String sqlWhere = "where ad.unassign is null ";
        
        if(campaignId != null && !campaignId.equals("0")) {
            sqlWhere += "and a.campaign.id = "+campaignId+" ";
        } else {
            sqlWhere += "and a.campaign.status = 1 and a.campaign.enable = 1 and (a.campaign.startDate <= GETDATE() and a.campaign.endDate >= GETDATE()) ";
        }
        
        if(userId != null && !userId.equals("")) {
            sqlWhere += "and ad.users.id = "+userId+" ";
        }
        
        if(mode.equals("today")) {
            sqlWhere += "and ad.updateDate between ?1 and ?2 ";
        } else if (mode.equals("realtime")) {
            sqlWhere += "and a.marketing.status = 1 and a.marketing.enable = 1 and (GETDATE() between a.marketing.startDate and a.marketing.endDate) "
                      + "and (ad.status <> 'closed' or ad.status = 'closed' and ad.updateDate between DATEADD(d, -"+JSFUtil.getApplication().getCloseAssignmentList()+", DATEDIFF(d, 0, GETDATE())) and DATEADD(d, 1, DATEDIFF(d, 0, GETDATE()))) ";
        }
        
        String orderBy = "order by ad.status, ad.customerName ";
        
        Query q = em.createQuery(sql + sqlWhere + orderBy);
        
        if(mode.equals("today")) {
            Date today = new Date();
            q.setParameter(1, JSFUtil.toDateWithoutTime(today));
            q.setParameter(2, JSFUtil.toDateWithMaxTime(today));
        }
        
        return (List<AssignmentDetail>) q.getResultList();
    }
    
    public AssignmentDetail findAssignmentDetailByCustomer(Integer agentId, Integer customerId, Integer campaignId) {
        try {        
            Query q = em.createQuery("select object(ad) from AssignmentDetail as ad "
                    + "where ad.unassign is null and ad.status not like 'closed' and ad.users.id = ?1 "
                    + "and ad.customerId.id = ?2 and ad.assignmentId.campaign.id = ?3 "
                    + "order by ad.assignDate desc ");
            
            q.setParameter(1, agentId);
            q.setParameter(2, customerId);
            q.setParameter(3, campaignId);
            
            q.setFirstResult(0);
            q.setMaxResults(1);

            return (AssignmentDetail) q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    
    public List<AssignmentDetail> findAssignmentDetailByAssignmentID(Integer assignmentId) {
        Query q = em.createQuery("select object(o) from AssignmentDetail as o"
                + " where o.assignmentId.id = ?1");
        q.setParameter(1, assignmentId);
        
        return q.getResultList();
    }

    public List<AssignmentDetail> findAssignmentDetailByMarketingId(int marketingId){
        Query q = em.createQuery("select object(o) from AssignmentDetail as o where o.assignmentId.marketing.id = :marketingId"
                + " and o.status not in ('closed', 'assigned')");
        q.setParameter("marketingId", marketingId);
        return q.getResultList();
    }

    public List<AssignmentDetail> findFollowUpSaleDate(String today, Integer userId){
        Query q = em.createQuery("select object(ad) from AssignmentDetail ad"
                + " where (ad.status = 'followup' or ad.status = 'opened')"
                + " and convert(varchar, ad.followupsaleDate, 112) = :today"
                + " and ad.users.id = :userId"
                + " and ad.unassign IS NULL"
                + " and ad.customerId.opOut IS NULL"
                + " and ad.assignmentId.marketing.enable = true and ad.assignmentId.marketing.status = true and convert(varchar, ad.assignmentId.marketing.endDate, 112) >= :today"
                + " and ad.assignmentId.campaign.enable = true and ad.assignmentId.campaign.status = true and convert(varchar, ad.assignmentId.campaign.endDate, 112) >= :today"
                + " order by convert(varchar, ad.followupsaleDate, 126)");
        q.setParameter("today", today);
        q.setParameter("userId", userId);
        return q.getResultList();
    }
}
