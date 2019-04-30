/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.model.dao.front.campaign;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.PersistenceContext;

import com.maxelyz.core.model.value.front.campaign.CampaignValue;
import com.maxelyz.utils.JSFUtil;
import com.ntier.utils.ParameterMap;

/**
 *
 * @author Manop
 */
public class CampaignDAO {

    @PersistenceContext
    private EntityManager em;
    private ParameterMap params;
    //-----------
    public List<CampaignValue> findCampaignValue() {
        return findCampaignValue("", null, 0, Integer.MAX_VALUE, "", "", "", "","","","","","","","","","","","",0,"","","");
    }
    
    public List<CampaignValue> findCampaignValue(String page, Integer userId, int firstResult, int maxResults, String txtSearch, String assignedStartDate, 
            String assignedEndDate, String gender, String saleResult, String followupStartDate, String followupEndDate, String customerType, 
            String customerName, String campaignName, String ageStart, String ageEnd, String approvalStatus, String lastContactStartDate,
            String lastContactEndDate, Integer contactResultId, String remark, String updateStartDate, String updateEndDate) {
        List<CampaignValue> list = null;
        String SQL_QUERY = generatefindCampaignValueSQLCmd(page, txtSearch, assignedStartDate, assignedEndDate, gender, saleResult, followupStartDate,
                followupEndDate, customerType, customerName, campaignName, ageStart, ageEnd, approvalStatus, lastContactStartDate,  lastContactEndDate,
                contactResultId, remark, updateStartDate, updateEndDate);
        
        Query query = em.createQuery(SQL_QUERY);

        if (maxResults > 0) {
            query.setMaxResults(maxResults);
        }
        if (firstResult >= 0) {
            query.setFirstResult(firstResult);
        }
        
        query.setParameter("userId", userId);
        if(!txtSearch.equals("")){
            query.setParameter("txtSearch", "%"+txtSearch+"%");
        }
        if(contactResultId != null && contactResultId != 0){
            query.setParameter("contactResultId", contactResultId);
        }
        if(!remark.equals("")){
            query.setParameter("remark", "%"+remark+"%");
        }
        if(!lastContactStartDate.equals("")){
            query.setParameter("lastContactStartDate", lastContactStartDate);
        }
        if(!lastContactEndDate.equals("")){
            query.setParameter("lastContactEndDate", lastContactEndDate);
        }
        
        if(!ageStart.equals("") && !ageStart.equals("0")){
            query.setParameter("ageStart", ageStart);
        }
       
        if(!ageEnd.equals("") && !ageEnd.equals("0")){
            query.setParameter("ageEnd", ageEnd);            
        }
        
        if(!customerType.equals("")){
            query.setParameter("customerType", "%"+customerType+"%");
        }
        if(!customerName.equals("")){
            query.setParameter("customerName", "%"+customerName+"%");
        }
        if(!campaignName.equals("")){
            query.setParameter("campaignName", "%"+campaignName+"%");
        }
        if(!followupStartDate.equals("")){
            query.setParameter("followupStartDate", followupStartDate);
        }
        if(!followupEndDate.equals("")){
            query.setParameter("followupEndDate", followupEndDate);
        }
        if(!assignedStartDate.equals("")){
            query.setParameter("assignedStartDate", assignedStartDate);
        }
        if(!assignedEndDate.equals("")){
            query.setParameter("assignedEndDate", assignedEndDate);
        }
//        if(!gender.equals("")){
//            query.setParameter("gender", gender);
//        }
        if(!saleResult.equals("")){
            query.setParameter("saleResult", saleResult);
        }
        if(!updateStartDate.equals("")){
            query.setParameter("updateStartDate", updateStartDate);
        }
        if(!updateEndDate.equals("")){
            query.setParameter("updateEndDate", updateEndDate);
        }
        try{
            list = query.getResultList();
        }catch(Exception sqle){
            sqle.printStackTrace();
        }
        return list;
    }
  
    /**
     * @return the params
     */
    
    private String generatefindCampaignValueSQLCmd(String page, String txtSearch, String assignedStartDate, String assignedEndDate, String gender,
            String saleResult, String followupStartDate,  String followupEndDate, String customerType, String customerName, String campaignName,
            String ageStart, String ageEnd, String approvalStatus, String lastContactStartDate, String lastContactEndDate, Integer contactResultId,
            String remark, String updateStartDate, String updateEndDate) {
       
        String SQL_SELECT = "SELECT "
                + "NEW "
                + CampaignValue.class.getName()
                + "( "
                + "ad.id, "
                + "ad.campaignName, "
                + "ad.marketingCode, "
                + "customer.id, "
                + "ad.customerName, "
                + "ad.customerType, "
                + "ad.gender, "
                + "customer.dob, "
                + "ad.assignDate, "
                + "ad.status, "
                + "ad.updateDate, "
                + "ad.followupsaleDate, "
                + "ad.saleResult,"
                + "ad.contactResult,"
                + "ad.contactRemark, "
                + "ad.contactDate "
                + ") ";
        String SQL_FROM = "FROM AssignmentDetail ad "
                + "join ad.customerId customer ";

//        if (page.equals("AS")) {
            SQL_FROM += "join ad.assignmentId assignment "
                    + "join assignment.campaign campaign ";
//        }

        String SQL_WHERE = "WHERE campaign.status = 1 and campaign.enable = 1 and (GETDATE() between campaign.startDate and campaign.endDate) "
                        + "and assignment.marketing.status = 1 and assignment.marketing.enable = 1 and (GETDATE() between assignment.marketing.startDate and assignment.marketing.endDate) "
                        + "and isnull(customer.opOut,0) <> 1 ";
        
        if (page.equals("AS")) {
            SQL_WHERE += "and ad.users.id = :userId and ad.unassign is null and (ad.status = 'assigned' or ad.status = 'viewed') ";
                    //+ "and (GETDATE() between campaign.startDate and campaign.endDate) ";
        }else if (page.equals("OP")) {
            SQL_WHERE += "and ad.users.id = :userId and ad.status = 'opened' and ad.unassign is null ";
        }else if (page.equals("FL")) {
            SQL_WHERE += "and ad.users.id = :userId and ad.status = 'followup' and ad.unassign is null ";
        }else if (page.equals("CL")) {
            SQL_WHERE += "and ad.users.id = :userId and ad.status = 'closed' ";
            if(!updateStartDate.equals("") && !updateEndDate.equals("")){
                SQL_WHERE += "and CONVERT(varchar(8), ad.updateDate, 112) between :updateStartDate and :updateEndDate ";
            }else if(!updateStartDate.equals("") && updateEndDate.equals("")){
                SQL_WHERE += "and CONVERT(varchar(8), ad.updateDate, 112) >= :updateStartDate ";
            }else if(updateStartDate.equals("") && !updateEndDate.equals("")){
                SQL_WHERE += "and CONVERT(varchar(8), ad.updateDate, 112) <= :updateEndDate ";
            } else {
                SQL_WHERE += "and (ad.updateDate between DATEADD(d, -"+JSFUtil.getApplication().getCloseAssignmentList()+", DATEDIFF(d, 0, GETDATE())) and DATEADD(d, 1, DATEDIFF(d, 0, GETDATE()))) ";
            }
        }
        
        if(!txtSearch.equals("")){
            SQL_WHERE += "and (ad.customerName like :txtSearch ";
            SQL_WHERE += "or ad.id in (select ad1.id from AssignmentDetail ad1 join ad1.contactHistoryCollection ch where ad1.users.id = :userId and ad1.status = 'followup' and ad1.unassign is null and ch.contactResult.name like :txtSearch)) ";
        }
        
        if(!remark.equals("")){
            SQL_WHERE += "and ad.contactRemark like :remark ";
        }
        
        if(!assignedStartDate.equals("") && !assignedEndDate.equals("")){
            SQL_WHERE += "and CONVERT(varchar(8), ad.assignDate, 112) between :assignedStartDate and :assignedEndDate ";
        }else if(!assignedStartDate.equals("") && assignedEndDate.equals("")){
            SQL_WHERE += "and CONVERT(varchar(8), ad.assignDate, 112) >= :assignedStartDate ";
        }else if(assignedStartDate.equals("") && !assignedEndDate.equals("")){
            SQL_WHERE += "and CONVERT(varchar(8), ad.assignDate, 112) <= :assignedEndDate ";
        }   
        
        if(contactResultId != null && contactResultId != 0){
            SQL_WHERE += "and ad.contactResultId.id = :contactResultId ";
            //SQL_WHERE += "and ad.id in (select ad1.id from AssignmentDetail ad1 join ad1.contactHistoryCollection ch where ad1.users.id = :userId and ad1.status = 'followup' and ad1.unassign is null and ch.contactResult.id = :contactResultId) ";
        }   
        
        if(!customerType.equals("")){
            SQL_WHERE += "and ad.customerType like :customerType ";
        }
        
        if(!customerName.equals("")){
            SQL_WHERE += "and ad.customerName like :customerName ";
        }
        
        if(!campaignName.equals("")){
            SQL_WHERE += "and ad.campaignName like :campaignName ";
        }
        
        int fromageSearch = 0,toageSearch = 0;
        if(!ageStart.equals("")) {
            fromageSearch = Integer.parseInt(ageStart);
        }
        if(!ageEnd.equals("")) {
            toageSearch = Integer.parseInt(ageEnd);
        }
        if(fromageSearch > 0 && toageSearch > 0) {
            SQL_WHERE += "and customer.dob is not null and (DATEDIFF(Year,customer.dob,getdate()) >= :ageStart and DATEDIFF(Year,customer.dob,getdate()) <= :ageEnd) ";
        } else if(fromageSearch > 0 && toageSearch == 0) {
            SQL_WHERE += "and customer.dob is not null and (DATEDIFF(Year,customer.dob,getdate()) >= :ageStart) ";
        } else if(fromageSearch == 0  && toageSearch > 0) {
            SQL_WHERE += "and customer.dob is not null and (DATEDIFF(Year,customer.dob,getdate()) <= :ageEnd) ";
        }
        
        if(!lastContactStartDate.equals("") && !lastContactEndDate.equals("")){
            SQL_WHERE += "and CONVERT(varchar(8), ad.contactDate, 112) between :lastContactStartDate and :lastContactEndDate ";
        }else if(!lastContactStartDate.equals("") && lastContactEndDate.equals("")){
            SQL_WHERE += "and CONVERT(varchar(8), ad.contactDate, 112) >= :lastContactStartDate ";
        }else if(lastContactStartDate.equals("") && !lastContactEndDate.equals("")){
            SQL_WHERE += "and CONVERT(varchar(8), ad.contactDate, 112) <= :lastContactEndDate ";
        }
        
        if(!followupStartDate.equals("") && !followupEndDate.equals("")){
            SQL_WHERE += "and CONVERT(varchar(8), ad.followupsaleDate, 112) between :followupStartDate and :followupEndDate ";
        }else if(!followupStartDate.equals("") && followupEndDate.equals("")){
            SQL_WHERE += "and CONVERT(varchar(8), ad.followupsaleDate, 112) >= :followupStartDate ";
        }else if(followupStartDate.equals("") && !followupEndDate.equals("")){
            SQL_WHERE += "and CONVERT(varchar(8), ad.followupsaleDate, 112) <= :followupEndDate ";
        }
                     
        if(!gender.equals("")){
            SQL_WHERE += "and ad.gender like '" + gender.trim() + "%' ";
        }
        if(!saleResult.equals("")){
            SQL_WHERE += "and ad.saleResult = :saleResult ";
        }
        String SQL_ORDER = "ORDER BY ";
        if (page.equals("AS")) {
            SQL_ORDER += "ad.id ";
        }else if (page.equals("OP")) {
            SQL_ORDER += "ad.updateDate desc, ad.assignmentId.assignDate desc ";
        }else if (page.equals("FL")) {
            SQL_ORDER += "ad.followupsaleDate desc, ad.updateDate desc, ad.assignmentId.assignDate desc ";
        }else if (page.equals("CL")) {
            SQL_ORDER += "ad.updateDate desc, ad.assignmentId.assignDate desc ";
        }

        return SQL_SELECT + SQL_FROM + SQL_WHERE + SQL_ORDER;
    }

    public ParameterMap getParams() {
        return params;
    }

    /**
     * @param params the params to set
     */
    public void setParams(ParameterMap params) {
        this.params = params;
    }
}
