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
import java.util.LinkedHashMap;

@ManagedBean
@RequestScoped
public class SalesRecordByStatusReport  extends AbstractReportBaseController {
    private static Logger log = Logger.getLogger(SalesRecordByStatusReport.class);
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:salerecordbystatus:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        super.init();
    }

    @Override
    public String getReportPath() {
        return "/report/salesrecordbystatus.jasper";
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String select = " select po.id as po_id "
                + " ,po.channel_type  as channel_type "
                + " ,mk.code as mk_code "
                + " ,po.ref_no as ref_no "
                + " ,po.customer_id as cus_id "
                + " ,ad.customer_name as cus_name "
                + " ,reg1.name+' '+reg1.surname as insure_person "
                + " ,prod.code as prod_code "
                + " ,pplan.name as prod_plan "
                + " ,(u.name+' '+u.surname) as user_name "
                + " ,CASE WHEN po.approval_status = 'waiting' and po.qc_status='waiting' and po.payment_status='waiting' THEN 'Waiting' "
                + " WHEN po.approval_status not in ('waiting','approved','rejected') and po.qc_status not in ('waiting','approved','rejected') and po.payment_status not in ('waiting','approved','rejected') THEN 'Pending' "
                + " WHEN po.approval_status='approved' and po.qc_status='approved' and po.payment_status='approved' THEN 'Success' "
                + " WHEN po.approval_status='rejected' or po.qc_status='rejected' or po.payment_status='rejected' THEN 'Reject' END as sale_status "
                + " ,po.approval_status as approval_status "
                + " ,po.payment_status as payment_status "
                + " ,po.qc_status as qc_status "
                + " ,po.approval_reason_id as approval_reason "
                + " ,po.payment_reason_id as payment_reason "
                + " ,po.qc_reason_id as qc_reason "                
                + " from purchase_order po "
                + " inner join purchase_order_detail pod on pod.purchase_order_id = po.id "
                + " inner join assignment_detail ad on ad.id  = po.assignment_detail_id "
                + " inner join assignment a on a.id = ad.assignment_id "
                + " inner join marketing mk on mk.id = a.marketing_id "
                + " inner join product prod on prod.id = pod.product_id "
                + " inner join product_plan pplan on pplan.id = pod.product_plan_id "
                + " inner join users u on u.id = ad.user_id "
                + " inner join purchase_order_register reg1 on reg1.purchase_order_detail_id = pod.id and reg1.no = 1 "
                + " where 1=1 ";
        

 
        if (campaignId>0) {
            select += " and cp.id = '" + campaignId  + "'";
        }
        if (userGroupId!=0){
            select += "AND u.user_group_id = " + userGroupId + " ";}
        if (userId!=0){
            select += "AND u.id = " + userId + " ";}
         
        
 System.out.println("Sql:"+select);
        String query = select;
        
        return query;
    }

}
