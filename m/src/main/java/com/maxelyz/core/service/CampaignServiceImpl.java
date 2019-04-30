
package com.maxelyz.core.service;

import com.maxelyz.core.model.dao.AssignmentDAO;
import com.maxelyz.core.model.dao.AssignmentDetailDAO;
import com.maxelyz.core.model.dao.CampaignDAO;
import com.maxelyz.core.model.dao.MarketingCustomerDAO;
import com.maxelyz.core.model.dao.MarketingDAO;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.core.model.value.admin.UserAssignmentValue;
import com.maxelyz.core.model.value.front.campaign.CampaignValue;
import com.maxelyz.utils.JSFUtil;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;


public class CampaignServiceImpl implements CampaignService, Serializable {
    private static Logger log = Logger.getLogger(CampaignServiceImpl.class);
    
    private CampaignDAO campaignDAO;
    private com.maxelyz.core.model.dao.front.campaign.CampaignDAO campaignFrontDAO;
    private AssignmentDAO assignmentDAO;
    private AssignmentDetailDAO assignmentDetailDAO;
    private MarketingCustomerDAO marketingCustomerDAO;
    private MarketingDAO marketingDAO;

    @Override
    @Transactional
    public synchronized void createAssignmentDetailFromPooling(Users user) {
        /*
         * 1.find marketing pool by user
         *    select assignment_pooling where user_id = [user]
         * 2.find customer from pooling  
         *      for each marketing pool
         *           select top 1 marketing_customer where assign = false and markeitng_id=[]
         * 3.insert assignment_detail
         * 4.update assignment_pooling set no_used=no_used+1
         * 5.insert or update assignment_pooling_daily
         */

        List<AssignmentPooling> assignmentPoolingList = null;
        AssignmentPoolingDaily asmpd = null;
        Date dateNow = new Date();
        Boolean enable = true;
        Boolean newList = true;
        try {
            assignmentPoolingList = assignmentDAO.findAssignmentPooling(user, true);
            for (AssignmentPooling asmp : assignmentPoolingList) {
                enable = true;
                newList = asmp.getAssignment().getNewList();
                Marketing marketing = asmp.getAssignment().getMarketing();
                List<AssignmentDetail> ad = assignmentDetailDAO.findAssignmentDetailPooling(user.getId(), marketing.getId());
                if (asmp.getAssignment().getAssignmentType().equals("poolingdaily")) {
                    asmpd = assignmentDAO.findAssignmentPoolingDaily(asmp.getId(), dateNow);
                    if (asmpd != null) {
                        if(asmpd.getNoUsed() == asmp.getNoCustomer()){
                            enable = false;
                        }
                    }
                }
                if (ad == null || ad.isEmpty()) {
                    Customer customer = null;
                    AssignmentSupervisorDetail asd = null;
                    
                    if(asmp.getAssignment().getCreateByUser().getUserGroup().getRole().contains("Supervisor") && asmp.getAssignment().getAssignmentMode().equalsIgnoreCase("supervisor")){
                        asd = assignmentDAO.findAssignmentSupervisorDetail(asmp.getAssignment().getCreateByUser().getId(), marketing.getId(), asmp.getAssignment().getCampaign().getId(), asmp.getAssignment().getNewList());
                        if(asd != null){
                            customer = asd.getCustomerId();
                        }
                    }else if(asmp.getAssignment().getCreateByUser().getUserGroup().getRole().contains("CampaignManager") && asmp.getAssignment().getAssignmentMode().equalsIgnoreCase("supervisor")){
                        customer = assignmentDAO.findUnAssignedCustomer(marketing.getId(), asmp.getAssignment().getNewList());
                    }
                    
                    if (customer != null) {
                        if (enable) {
                            
                            MarketingCustomerPK mcPK = new MarketingCustomerPK(marketing.getId(), customer.getId());
                            MarketingCustomer mc = marketingCustomerDAO.findMarketingCustomer(mcPK);
                            if(mc != null){
                                mc.setAssign(true);
                                marketingCustomerDAO.edit1(mc);
                            }
                            
                            if(asmp.getAssignment().getCreateByUser().getUserGroup().getRole().contains("Supervisor") && asmp.getAssignment().getAssignmentMode().equalsIgnoreCase("supervisor")){
                                if(asd != null){
                                    asd.setAssign(true);
                                    asd.setAssignDate(dateNow);
                                    assignmentDAO.editAssignmentSupervisorDetail1(asd);
                                    
                                    AssignmentSupervisor as = asd.getAssignmentSupervisorId();
                                    Integer i = as.getNoUsed() + 1;
                                    as.setNoUsed(i);
                                    assignmentDAO.editAssignmentSupervisor1(as);
                                }
                            }else if(asmp.getAssignment().getCreateByUser().getUserGroup().getRole().contains("CampaignManager") && asmp.getAssignment().getAssignmentMode().equalsIgnoreCase("supervisor")){
                                
                                Marketing m = asmp.getAssignment().getMarketing();
                                Integer j = m.getTotalAssigned() + 1;
                                m.setTotalAssigned(j);
                                marketingDAO.edit1(marketing);
                            }
                            
                            AssignmentDetail assignmentDetail = new AssignmentDetail();
                            assignmentDetail.setAssignmentId(asmp.getAssignment());
                            assignmentDetail.setUsers(user);
                            assignmentDetail.setCustomerId(customer);//*
                            assignmentDetail.setCalled(false);
                            assignmentDetail.setStatus("assigned");
                            assignmentDetail.setCallAttempt(0);
                            assignmentDetail.setDmc(false);
                            assignmentDetail.setCallAttempt2(0);
                            assignmentDetail.setSaleAttempt(0);
                            assignmentDetail.setCustomerName(customer.getName() + (customer.getSurname() == null ? "" : " " + customer.getSurname())); //*
                            assignmentDetail.setCustomerType(customer.getCustomerType().getName()); ///*
                            assignmentDetail.setDob(customer.getDob()); //*
                            assignmentDetail.setAssignDate(new Date());
                            assignmentDetail.setMarketingCode(marketing.getCode());
                            assignmentDetail.setMaxCall(asmp.getAssignment().getCampaign().getMaxCall());
                            assignmentDetail.setMaxCall2(asmp.getAssignment().getCampaign().getMaxCall2());
                            assignmentDetail.setCampaignName(asmp.getAssignment().getCampaign().getName());
                            assignmentDetail.setGender(customer.getGender());
                            assignmentDetail.setUpdateDate(new Date());
                            assignmentDetail.setNewList(newList);
                            assignmentDetailDAO.create(assignmentDetail);

                            //assignmentDAO.updateMarketingCustomer(marketing.getId(), customer.getId());
                            int c = asmp.getNoUsed() + 1;
                            if (asmp.getAssignment().getAssignmentType().equals("pooling")) {
                                //
                            } else if (asmp.getAssignment().getAssignmentType().equals("poolingcustom")) {
                                if (asmp.getNoCustomer() == c) {
                                    asmp.setEnable(false);
                                }
                            } else if (asmp.getAssignment().getAssignmentType().equals("poolingdaily")) {
                                int i = 0;
                                if(asmpd == null){
                                    i = i + 1;
                                    AssignmentPoolingDailyPK apk = new AssignmentPoolingDailyPK(dateNow, asmp.getId());
                                    asmpd = new AssignmentPoolingDaily(apk, i);
                                    assignmentDAO.createAssignmentPoolingDaily(asmpd);
                                }else{
                                    i = asmpd.getNoUsed() + 1;
                                    asmpd.setNoUsed(i);
                                    assignmentDAO.updateAssignmentPoolingDaily1(asmpd);
                                }                                
                            }
                            asmp.setNoUsed(c);
                        }
                    } else {
                        asmp.setEnable(false);
                    }
                    
                    assignmentDAO.editAssignmentPooling1(asmp);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void createAssignment(String assignmentType, Campaign campaign, List<UserAssignmentValue> assignmentUsers, Marketing marketing, Map parameterMap) {
        
    }  

    @Override
    public List<CampaignValue> findCampaignValue(String page, Integer userId, int firstResult, int maxResults, String txtSearch, String assignedStartDate, String assignedEndDate, String gender, String saleResult, String followupStartDate, String followupEndDate, String customerType, String customerName, String campaignName, String ageStart, String ageEnd, String approvalStatus, String lastContactStartDate, String lastContactEndDate, Integer contactResultId, String remark, String updateStartDate, String updateEndDate) {
       return campaignFrontDAO.findCampaignValue(page, userId, firstResult, maxResults, txtSearch, assignedStartDate, assignedEndDate, gender, saleResult, followupStartDate, followupEndDate, customerType, customerName, campaignName, ageStart, ageEnd, approvalStatus, lastContactStartDate, lastContactEndDate, contactResultId, remark, updateStartDate, updateEndDate);
          
    }
    
    
    //DAO
    public AssignmentDAO getAssignmentDAO() {
        return assignmentDAO;
    }

    public void setAssignmentDAO(AssignmentDAO assignmentDAO) {
        this.assignmentDAO = assignmentDAO;
    }

    public CampaignDAO getCampaignDAO() {
        return campaignDAO;
    }

    public void setCampaignDAO(CampaignDAO campaignDAO) {
        this.campaignDAO = campaignDAO;
    }

    public com.maxelyz.core.model.dao.front.campaign.CampaignDAO getCampaignFrontDAO() {
        return campaignFrontDAO;
    }

    public void setCampaignFrontDAO(com.maxelyz.core.model.dao.front.campaign.CampaignDAO campaignFrontDAO) {
        this.campaignFrontDAO = campaignFrontDAO;
    }

    public AssignmentDetailDAO getAssignmentDetailDAO() {
        return assignmentDetailDAO;
    }

    public void setAssignmentDetailDAO(AssignmentDetailDAO assignmentDetailDAO) {
        this.assignmentDetailDAO = assignmentDetailDAO;
    }

    public MarketingCustomerDAO getMarketingCustomerDAO() {
        return marketingCustomerDAO;
    }

    public void setMarketingCustomerDAO(MarketingCustomerDAO marketingCustomerDAO) {
        this.marketingCustomerDAO = marketingCustomerDAO;
    }
    
    public MarketingDAO getMarketingDAO() {
        return marketingDAO;
}

    public void setMarketingDAO(MarketingDAO marketingDAO) {
        this.marketingDAO = marketingDAO;
    }
    
}
