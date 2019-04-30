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
public class SalePerformanceByProductReport  extends AbstractReportBaseController {
    private static Logger log = Logger.getLogger(SalePerformanceByProductReport.class);
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:saleperformancebyproduct:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        super.init();
    }

    @Override
    public String getReportPath() {
        return "/report/saleperformancebyproduct.jasper";
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

        String select = " select  mk.code as marketing_code "
                + " , cp.name as campaign_name "
                + " , prod.code  as product_code "
                + " , prod.name  as product_name "
                + " , sum(mk.total_Record) as total_Record "
                + " , sum(isnull(po.total_amount,0)) as total_amount "
                + " , sum(CASE WHEN  po.approval_status = 'approved' and po.qc_status = 'approved' and po.payment_status = 'approved' THEN 1 ELSE 0  END) AS  net_app "
                + " , sum(CASE WHEN  po.approval_status = 'approved' and po.qc_status = 'approved' and po.payment_status = 'approved' THEN isnull(po.total_amount,0) ELSE 0  END) AS  net_tarp "
                + " , sum(CASE WHEN  (po.approval_status <> 'approved' or po.qc_status <> 'approved' or po.payment_status <> 'approved') THEN 1 ELSE 0  END) AS  gross_app "
                + " , sum(CASE WHEN  (po.approval_status <> 'approved' or po.qc_status <> 'approved' or po.payment_status <> 'approved') THEN isnull(po.total_amount,0) ELSE 0  END) AS  gross_tarp "
                + " ,isnull((select count(ss.wd) as wd2 from (select ut.user_id,CONVERT(date,ut.timestamp) as wd from users_time_attendance ut  where  ut.type = 'login' group by ut.user_id,CONVERT(date,ut.timestamp)) ss where ss.user_id = ad.user_id group by ss.user_id),0)  AS workingday "
                + " from marketing mk "
                + " inner join prospectlist_sponsor sp on sp.id = mk.prospectlist_sponsor_id "
                + " inner join assignment a on a.marketing_id = mk.id "
                + " inner join assignment_detail ad on ad.assignment_id = a.id "
                + " inner join campaign cp on cp.id = a.campaign_id "
                + " inner join purchase_order po on po.assignment_detail_id = ad.id "
                + " inner join purchase_order_detail pod on pod.purchase_order_id = po.id "
                + " inner join product prod on prod.id = pod.product_id "
                + " inner join users u on u.id = po.create_by_id"
                + " where po.sale_result = 'Y' ";
        

 
        if (campaignId>0) {
            select += " and cp.id = '" + campaignId  + "'";
        }
        if (userGroupId!=0){
            select += "AND u.user_group_id = " + userGroupId + " ";}
        if (userId!=0){
            select += "AND u.id = " + userId + " ";}
         
        select += " group by mk.code,cp.name,prod.code,prod.name,ad.user_id ";
        
 System.out.println("Sql:"+select);
        String query = select;
        
        return query;
    }

}
