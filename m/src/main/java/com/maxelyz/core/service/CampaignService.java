/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.service;

import com.maxelyz.core.model.entity.Campaign;
import com.maxelyz.core.model.entity.Marketing;
import com.maxelyz.core.model.entity.Users;
import com.maxelyz.core.model.value.admin.UserAssignmentValue;
import com.maxelyz.core.model.value.front.campaign.CampaignValue;
import java.util.List;
import java.util.Map;

/**
 *
 * @author oat
 */
public interface CampaignService {
    void createAssignmentDetailFromPooling(Users user);    
    void createAssignment(String assignmentType, Campaign campaign, List<UserAssignmentValue> assignmentUsers, Marketing marketing, Map parameterMap);   
    List<CampaignValue> findCampaignValue(String page, Integer userId, int firstResult, int maxResults, String txtSearch, String assignedStartDate, String assignedEndDate, String gender, String saleResult, String followupStartDate, String followupEndDate, String customerType, String customerName, String campaignName, String ageStart, String ageEnd, String approvalStatus, String lastContactStartDate, String lastContactEndDate, Integer contactResultId, String remark, String updateStartDate, String updateEndDate);
    
}