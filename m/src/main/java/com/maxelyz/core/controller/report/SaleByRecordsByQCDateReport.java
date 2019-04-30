package com.maxelyz.core.controller.report;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.apache.log4j.Logger;
import com.maxelyz.utils.SecurityUtil;
import com.maxelyz.core.controller.report.AbstractReportBaseController;
import java.util.LinkedHashMap;

@ManagedBean
@RequestScoped
public class SaleByRecordsByQCDateReport extends AbstractReportBaseController {
    private static Logger log = Logger.getLogger(SaleByRecordsByQCDateReport.class);
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:salebyrecords:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        super.init();
    }

    @Override
    public String getReportPath() {
        return "/report/salesByRecords.jasper";
    }

    @Override
    public Map getParameterMap() { 
        Map parameterMap = new LinkedHashMap();
        parameterMap.put("from", fromDate);
        parameterMap.put("to", toDate);
        parameterMap.put("campaign_id", campaignId);
        parameterMap.put("campaign_name", campaignName);
        return parameterMap;
    }

    @Override
    public String getQuery() {
        String select = "select mk.name as list,po.ref_no2 as ref_no, po.purchase_date as sale_date , reg.name+' '+isnull(reg.surname,'') as customer_name, u.agent_code as agent_code, u.name+' '+u.surname as tsr, g.name as team, "
                + "po.annual_net_premium api, po.annual_price api_notax, po.qc_date, "
                + "case when po.qc_status = 'pending' then 'Hold' "
                + "when po.qc_status in ('reconfirm' ,'request') then 'Reconfirm' "
                + "when po.qc_status = 'rejected' then 'Cancel' "
                + "when po.qc_status = 'waiting' then 'Waiting QA' "
                + "when po.qc_status = 'approved' then 'Approve' "
                + "else po.qc_status+'_' end as qc_status, "
                + "po.remark "
                + "from purchase_order po "
                + "inner join purchase_order_detail pod on pod.purchase_order_id = po.id "
                + "inner join purchase_order_register reg on reg.purchase_order_detail_id = pod.id and reg.no = 1 "
                + "inner join customer c on c.id = po.customer_id "
                + "inner join users u on u.id = po.create_by_id "
                + "inner join user_group g on g.id = u.user_group_id "
                + "inner join assignment_detail ad on ad.id = po.assignment_detail_id "
                + "inner join assignment a on a.id = ad.assignment_id "
                + "inner join marketing mk on mk.id = a.marketing_id ";
               
        String where = "where po.qc_date between '"+sdf.format(fromDate)+"' and '"+sdf.format(toDate)+"' and po.sale_result= 'Y' ";
        if (userGroupId!=0) {
            where += "AND u.user_group_id = " + userGroupId + " ";
        }
        if (userId!=0) {
            where += "AND u.id = " + userId + " ";
        }
        if (campaignId!=0) {
            where += "and a.campaign_id = " + campaignId + " ";
        }
      
        String orderBy = "ORDER BY po.qc_date, mk.name, c.name";
        String query = select+where+orderBy;
      
        return query;
    }

}
