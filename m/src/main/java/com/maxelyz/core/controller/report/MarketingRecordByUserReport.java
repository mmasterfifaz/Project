/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.report;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.apache.log4j.Logger;
import com.maxelyz.utils.SecurityUtil;
import java.util.Locale;
import java.util.LinkedHashMap;

@ManagedBean
@RequestScoped
public class MarketingRecordByUserReport extends AbstractReportBaseController {
    private static Logger log = Logger.getLogger(MarketingRecordByUserReport.class);
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:marketingrecordbyuser:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        super.init();
    }

    @Override
    public String getReportPath() {
        return "/report/marketingrecordbyuser.jasper";
    }

    @Override
    public Map getParameterMap() { 
        Map parameterMap = new LinkedHashMap();
        parameterMap.put("from", fromDate);
        parameterMap.put("to", toDate);
        parameterMap.put("user_group_id", userGroupId);
        parameterMap.put("user_group_name", groupName);
        parameterMap.put("user_id", userId);
        parameterMap.put("user_name", userName);
        parameterMap.put("campaign_id", campaignId);
        parameterMap.put("campaign_name", campaignName);
        parameterMap.put("marketing_id", marketingId);
        parameterMap.put("marketing_name", marketingName);
        parameterMap.put("product_id", productId);
        parameterMap.put("product_name", productName);

        return parameterMap;
    }

    @Override
    public String getQuery() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

        String select = "select (u.name+' '+u.surname) as fullname "
                + ",ug.role as userrole "
                + ",ps.name as mksource "
                + ",mk.code as mkcode "
                + ",mk.name as mkname "
                + ",a.assign_date as asdate "
                + ",c.code as cpcode "
                + ",c.name as cpname "
                + ",a.no_customer as totalas "
                + ",(select count(an.assignment_id) from assignment_detail an where an.assignment_id = a.id and an.new_list = 1 ) as totalnew "
                + ",(select count(an.assignment_id) from assignment_detail an where an.assignment_id = a.id and an.new_list = 0 ) as totalold "
                + ",a.assignment_mode as asmode "
                + ",a.assignment_type as astype "
                + ",a.id as assid "
                + ",ad.assto as assto "
                + ",ad.new_list as new_list "
                + ",ad.old_list as old_list "                 
                + " from  assignment a "
                + " inner join marketing mk on mk.id = a.marketing_id "
                + " inner join campaign c on c.id = a.campaign_id "
                + " inner join prospectlist_sponsor ps on ps.id = mk.prospectlist_sponsor_id "
                + " inner join (select ad1.assignment_id ,(u1.name+' '+u1.surname) as assto, sum(case when ad1.new_list = 1 then 1 else 0 end )as new_list,sum(case when ad1.new_list = 0 then 1 else 0 end )as old_list from assignment_detail ad1 inner join users u1 on u1.id  = ad1.user_id where ad1.assignment_id > 1 group by ad1.assignment_id,u1.name, u1.surname) ad on ad.assignment_id = a.id "
                + " inner join users u on u.id  = a.create_by_id "
                + " inner join user_group ug on ug.id = u.user_group_id "
                + " where a.assign_date between  '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' ";
        
        if (campaignId>0) {
            select += " and a.campaign_id = '" + campaignId  + "'";
        }
        if (marketingId>0) {
            select += " and a.marketing_id = '" + marketingId  + "'";
        } 
        if (userGroupId!=0){
            select += "AND u.user_group_id = " + userGroupId + " ";}
        if (userId!=0){
            select += "AND u.id = " + userId + " ";}
        
                
                
        select += " group by u.name,u.surname,ug.role,ps.name,mk.code,mk.name,a.assign_date,c.code,c.name,a.id,a.no_customer "
                + " ,a.assignment_mode,a.assignment_type,ad.assto,ad.new_list,ad.old_list ";
        
        String query = select;
        return query;
    }

}

