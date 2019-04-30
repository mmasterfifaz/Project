package com.maxelyz.core.controller.report;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.apache.log4j.Logger;

@ManagedBean
@RequestScoped
public class SalePerformanceByUserReportOld extends AbstractReportBaseController {
    private static Logger log = Logger.getLogger(SalePerformanceByUserReportOld.class);
    @PostConstruct
    public void initialize() {
        super.init();
    }

    @Override
    public String getReportPath() {
        return "/report/salePerformanceByUserOld.jasper";
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

        String select = "select u.name+' '+u.surname as user_name, "
                + "sum(list_used_new) as list_used_new, "
                + "sum(list_used_old) as list_used_old, "
                + "sum(call_attempt) as call_attempt, "
                + "sum(call_success) as call_success, "
                + "sum(sale_attempt) as sale_attempt, "
                + "sum(sale_offering) as sale_offering, "
                + "sum(yes_sale) as yes_sale, "
                + "sum(no_sale) as no_sale, "
                + "sum(contactable) as contactable, "
                + "sum(follow_up) as follow_up, "
                + "sum(uncontactable) as uncontactable, "
                + "sum(total_amount) as total_amount "
                + "from rpt_sale_performance rpt "
                + "inner join users u on u.id = rpt.user_id "
                + "inner join user_group g on g.id = u.user_group_id ";

        String where = "WHERE rpt.sale_date between  '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' ";
        if (userGroupId!=0)
            where += "AND g.id = " + userGroupId + " ";
        if (userId!=0)
            where += "AND u.id = " + userId + " ";
        if (campaignId!=0)
            where += "AND rpt.campaign_id = " + campaignId + " ";
        if (marketingId!=0)
            where += "AND rpt.marketing_id = " + marketingId + " ";
       /* if (productId!=0)
            where += "";*/

        String groupBy = "GROUP BY u.name, u.surname ";
        String orderBy = "ORDER BY u.name, u.surname ";
        String query = select+where+groupBy+orderBy;
        
        return query;
    }

}
