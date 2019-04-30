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
public class MKTListPerformanceByChannelReport  extends AbstractReportBaseController {

    private static Logger log = Logger.getLogger(MKTListPerformanceByChannelReport.class);

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:marketinglistbychannel:view")) {
            SecurityUtil.redirectUnauthorize();
        }
         super.init();
    }

    @Override
    public String getReportPath() {
        return "/report/marketinglistbychannel.jasper";
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
        parameterMap.put("sponsor_id", sponsorId);
        parameterMap.put("sponsor_name", sponsorName);
        return parameterMap;
    }

    @Override
    public String getQuery() {
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

        String select = " select sp.name as sponsor_name "
                + " , mk.code as marketing_code "
                + " , sum(mk.total_Record) as total_Record "
                + " , sum(isnull(po.total_amount,0)) as total_amount "
                + " , sum(CASE WHEN  cp.inbound=1 and po.approval_status = 'approved' and po.qc_status = 'approved' and po.payment_status = 'approved' THEN 1 ELSE 0  END) AS  in_net_app "
                + " , sum(CASE WHEN  cp.inbound=1 and po.approval_status = 'approved' and po.qc_status = 'approved' and po.payment_status = 'approved' THEN isnull(po.total_amount,0) ELSE 0  END) AS  in_net_tarp "
                + " , sum(CASE WHEN  cp.inbound=1 and (po.approval_status <> 'approved' or po.qc_status <> 'approved' or po.payment_status <> 'approved') THEN 1 ELSE 0  END) AS  in_gross_app "
                + " , sum(CASE WHEN  cp.inbound=1 and (po.approval_status <> 'approved' or po.qc_status <> 'approved' or po.payment_status <> 'approved') THEN isnull(po.total_amount,0) ELSE 0  END) AS  in_gross_tarp "

                + " , sum(CASE WHEN  cp.inbound=0 and po.approval_status = 'approved' and po.qc_status = 'approved' and po.payment_status = 'approved' THEN 1 ELSE 0  END) AS  out_net_app "
                + " , sum(CASE WHEN  cp.inbound=0 and po.approval_status = 'approved' and po.qc_status = 'approved' and po.payment_status = 'approved' THEN isnull(po.total_amount,0) ELSE 0  END) AS  out_net_tarp "
                + " , sum(CASE WHEN  cp.inbound=0 and (po.approval_status <> 'approved' or po.qc_status <> 'approved' or po.payment_status <> 'approved') THEN 1 ELSE 0  END) AS  out_gross_app "
                + " , sum(CASE WHEN  cp.inbound=0 and (po.approval_status <> 'approved' or po.qc_status <> 'approved' or po.payment_status <> 'approved') THEN isnull(po.total_amount,0) ELSE 0  END) AS  out_gross_tarp "
        
                
                + " from marketing mk "
                + " inner join prospectlist_sponsor sp on sp.id = mk.prospectlist_sponsor_id "
                + " inner join assignment a on a.marketing_id = mk.id "
                + " inner join assignment_detail ad on ad.assignment_id = a.id "
                + " inner join campaign cp on cp.id = a.campaign_id "
                + " inner join purchase_order po on po.assignment_detail_id = ad.id "
                + " inner join purchase_order_detail pod on pod.purchase_order_id = po.id "
                + " inner join users u on u.id = po.create_by_id"
                + " where po.sale_result = 'Y' "
        + " and po.purchase_date between  '" + sdfs.format(fromDate) + "' AND '" + sdfs.format(toDate) + "' ";
        
        if (campaignId>0) {
            select += " and cp.id = '" + campaignId  + "'";
        }
        if (sponsorId>0) {
            select += " and sp.id = '" + sponsorId  + "'";
        }
        if (marketingId>0) {
            select += " and mk.id = '" + marketingId  + "'";
        }
        if (userGroupId!=0){
            select += "AND u.user_group_id = " + userGroupId + " ";}
        if (userId!=0){
            select += "AND u.id = " + userId + " ";}
        
        select += "  group by sp.id,sp.name,mk.code ";
        String query = select;

        return query;
    }
}