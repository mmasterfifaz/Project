/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.report;
import com.maxelyz.utils.SecurityUtil;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.apache.log4j.Logger;

@ManagedBean
@RequestScoped
public class AgentPerformanceReport extends AbstractReportBaseController {

    private static Logger log = Logger.getLogger(AgentPerformanceReport.class);

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:agentperformance:view")) {
            SecurityUtil.redirectUnauthorize();
        }
        super.init();
    }

    @Override
    public String getReportPath() {
        return "/report/agentperformance.jasper";
    }

    @Override
    public Map getParameterMap() {
        Map parameterMap = new LinkedHashMap();
        parameterMap.put("from", fromDate);
        parameterMap.put("to", toDate);
        parameterMap.put("channelType", channelType);
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
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

        String select = " select "
                + " ad.user_id as id "
                + " ,(u.name+' '+u.surname) as fullname "
                + " ,CASE WHEN  u.status = 1 THEN 'Active' ELSE 'Resigned'  END as userstatus "
                + " ,sum(CASE WHEN  ad.new_list=1 THEN 1 ELSE 0  END) AS  ass_new "
                + " ,sum(CASE WHEN  ad.new_list<>1 THEN 1 ELSE 0  END) AS  ass_old "
                + " ,sum(CASE WHEN  ad.status not in ('assigned','viewed') THEN 1 ELSE 0  END) AS  ass_used "
                + " ,isnull((select count(ss.wd) as wd2 from (select ut.user_id,CONVERT(date,ut.timestamp) as wd from users_time_attendance ut  where  ut.type = 'login' group by ut.user_id,CONVERT(date,ut.timestamp)) ss where ss.user_id = ad.user_id group by ss.user_id),0)  AS workingday "
                + " , sum(CASE WHEN  po.sale_result = 'Y' and po.approval_status = 'approved' and po.qc_status = 'approved' and po.payment_status = 'approved' THEN 1 ELSE 0  END) AS  net_app "
                + " , sum(CASE WHEN  po.sale_result = 'Y' and po.approval_status = 'approved' and po.qc_status = 'approved' and po.payment_status = 'approved' THEN isnull(po.total_amount,0) ELSE 0  END) AS  net_tarp "
                + " , sum(CASE WHEN po.sale_result = 'Y' and (po.qc_status <> 'approved' or po.payment_status <> 'approved' or po.approval_status <> 'approved') THEN 1 ELSE 0  END) AS  gross_app "
                + " , sum(CASE WHEN po.sale_result = 'Y' and (po.qc_status <> 'approved' or po.payment_status <> 'approved' or po.approval_status <> 'approved') THEN isnull(po.total_amount,0) ELSE 0  END) AS  gross_tarp "
                + " from assignment_detail ad "
                + " inner join users u on u.id = ad.user_id " 
                + " inner join purchase_order po on po.assignment_detail_id = ad.id "
                + " inner join purchase_order_detail pod on pod.purchase_order_id = po.id "
                + " inner join assignment a on a.id = ad.assignment_id "
                + " inner join campaign cp on cp.id = a.campaign_id "
                + " where po.sale_result = 'Y'  "
                + " and po.purchase_date between  '" + sdfs.format(fromDate) + "' AND '" + sdfs.format(toDate) + "' ";
        
        if (campaignId>0) {
            select += " and cp.id = '" + campaignId  + "'";
        }
        
        if (!channelType.isEmpty()) {
            select += " and po.channel_type = '" + channelType  + "'";
        }
        
        if (productId >0) {
            select += " and pod.product_id = '" + productId  + "'";
        }
        
        if (userGroupId!=0){
            select += "AND u.user_group_id = " + userGroupId + " ";}
        if (userId!=0){
            select += "AND u.id = " + userId + " ";}
        
        select += " group by ad.user_id,u.name,u.surname,u.status "
                + " order by  ad.user_id asc  ";
        
 System.out.println("Sql:"+select);
        String query = select;

        return query;
    }
}

