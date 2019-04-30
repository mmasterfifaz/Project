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
public class SalePerformanceByGroupReport extends AbstractReportBaseController {
    private static Logger log = Logger.getLogger(SalePerformanceByGroupReport.class);
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:saleperformancebyusergroup:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        super.init();
    }

    @Override
    public String getReportPath() {
        return "/report/salePerformanceByGroup.jasper";
//        return "/report/custom/lmg/salePerformanceByGroup.jasper";
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

    public String getQuery() {
        String subQueryWhere = "and po1.purchase_date between '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' ";
        /*
        if (userGroupId!=0) {
            subQueryWhere += "AND u1.user_group_id = " + userGroupId + " ";
        }*/
        /*
        if (userId!=0) {
            subQueryWhere += "AND u1.id = " + userId + " ";
        }*/
        if (campaignId!=0) {
            subQueryWhere += "AND a.campaign_id = " + campaignId + " ";
        }
        if (marketingId!=0) {
            subQueryWhere += "AND a.marketing_id = " + marketingId + " ";
        }

        String select = "select g.name as user_group,"
                + "sum(list_assigned) as list_assigned, "
                + "sum(list_used) as list_used, "
                + "sum(ISNULL(list_new,0)) as list_new, "
//                + "sum(list_used)- sum(ISNULL(list_new,0)) as list_old, "
                + "sum(list_old) as list_old, "
                + "sum(list_opened) as list_opened, "
                + "sum(list_followup) as list_followup, "
                + "sum(list_closed_contactable) as list_closed_contactable, "
                + "sum(list_closed_uncontactable) as list_closed_uncontactable, "
                + "sum(list_closed_firstend) as list_closed_firstend, "
                + "sum(list_used) - sum(list_opened)- sum(list_followup)-sum(list_closed_contactable)-sum(list_closed_uncontactable) as list_closed_unreachable, "
                + "sum(call_attempt) as call_attempt, "
                + "sum(call_success) as call_success, "
                + "sum(call_fail) as call_fail, "
                + "sum(list_yes_sale) as list_yes_sale, "
                //+ "sum(product_yes_sale) as product_yes_sale, "
                + "("
                + "select count(po1.id) from purchase_order po1 " //use subquery instead of rpt.product_yes_sale
                + "inner join assignment_detail ad on ad.id = po1.assignment_detail_id "
                + "inner join assignment a on a.id = ad.assignment_id "
                + "inner join users u1 on u1.id = po1.create_by_id "
                + "where po1.sale_result = 'Y' "
                + "and u1.user_group_id = g.id "
                + "and po1.approval_status = 'approved' and po1.payment_status = 'approved' and po1.qc_status = 'approved' "
                + subQueryWhere
                + ") product_yes_sale, "
                + "sum(product_yes_sale_today) as product_yes_sale_today, "
                + "sum(product_yes_pending_sale) as product_yes_pending_sale, "
                + "sum(list_no_sale) as list_no_sale, "
                + "sum(product_no_sale) as product_no_sale, "
                + "sum(list_followup_sale) as list_followup_sale, "
                + "sum(product_followup_sale) as product_followup_sale, "
                //+ "sum(total_amount) as total_amount, "
                + "("
                + "select sum(po1.annual_net_premium) from purchase_order po1 " //use subquery instead of rpt.total_amount
                + "inner join purchase_order_detail pod on pod.purchase_order_id = po1.id "
                + "inner join purchase_order_register reg on reg.purchase_order_detail_id = pod.id and reg.no=1 "
                + "inner join assignment_detail ad on ad.id = po1.assignment_detail_id "
                + "inner join assignment a on a.id = ad.assignment_id "
                + "inner join users u1 on u1.id = po1.create_by_id "
                + "where po1.sale_result = 'Y' "
                + "and u1.user_group_id = g.id "
                + "and po1.approval_status = 'approved' and po1.payment_status = 'approved' and po1.qc_status = 'approved' "
                + subQueryWhere
                + ") total_amount, "
                + "sum(total_amount_today) as total_amount_today, "
                + "sum(total_pending_amount) as total_pending_amount, "
                + "count(distinct user_id) as no_user, "
                + "sum(list_new_contact) as list_new_contact, "
//                + "sum(total_new_contact) as total_new_contact, "
                + "sum(list_old_contact) as list_old_contact, "
//                + "sum(total_old_contact) as total_old_contact, "
                + "sum(list_new_net_premium_credit) as list_new_net_premium_credit, "
                + "sum(list_new_net_premium_debit) as list_new_net_premium_debit, "
                + "sum(list_new_gross_premium_credit) as list_new_gross_premium_credit, "
                + "sum(list_new_gross_premium_debit) as list_new_gross_premium_debit, "
                + "sum(list_old_net_premium_credit) as list_old_net_premium_credit, "
                + "sum(list_old_net_premium_debit) as list_old_net_premium_debit, "
                + "sum(list_old_gross_premium_credit) as list_old_gross_premium_credit, "
                + "sum(list_old_gross_premium_debit) as list_old_gross_premium_debit "
                + "from rpt_sale_performance1 rpt "
                + "inner join users u on u.id = rpt.user_id "
                + "inner join user_group g on g.id = u.user_group_id ";

        String where = "WHERE rpt.sale_date between  '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' ";
        
        if (campaignId!=0)
            where += "AND rpt.campaign_id = " + campaignId + " ";
        if (marketingId!=0)
            where += "AND rpt.marketing_id = " + marketingId + " ";
       /* if (productId!=0)
            where += "";*/

        String groupBy = "GROUP BY g.id, g.name ";
        String orderBy = "ORDER BY g.name asc ";
        String query = select+where+groupBy+orderBy;
        
        return query;
    }
    
    public String getQuery9() {
        String select = "select g.name as group_name,"
                + "sum(list_assigned) as list_assigned, "
                + "sum(list_used) as list_used, "
                + "sum(list_opened) as list_opened, "
                + "sum(list_followup) as list_followup, "
                + "sum(list_closed_contactable) as list_closed_contactable, "
                + "sum(list_closed_uncontactable) as list_closed_uncontactable, "
                + "sum(list_closed_firstend) as list_closed_firstend, "
                + "sum(list_closed_unreachable) as list_closed_unreachable, "
                + "sum(call_attempt) as call_attempt, "
                + "sum(call_success) as call_success, "
                + "sum(call_fail) as call_fail, "
                + "sum(list_yes_sale) as list_yes_sale, "
                + "sum(product_yes_sale) as product_yes_sale, "
                + "sum(list_no_sale) as list_no_sale, "
                + "sum(product_no_sale) as product_no_sale, "
                + "sum(list_followup_sale) as list_followup_sale, "
                + "sum(product_followup_sale) as product_followup_sale, "
                + "sum(total_amount) as total_amount "
                + "from rpt_sale_performance1 rpt "
                + "inner join users u on u.id = rpt.user_id "
                + "inner join user_group g on g.id = u.user_group_id ";

        String where = "WHERE rpt.sale_date between  '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' ";
        /*if (userGroupId!=0)
            where += "AND g.id = " + userGroupId + " ";
        if (userId!=0)
            where += "AND u.id = " + userId + " ";*/
        if (campaignId!=0)
            where += "AND rpt.campaign_id = " + campaignId + " ";
        if (marketingId!=0)
            where += "AND rpt.marketing_id = " + marketingId + " ";
       /* if (productId!=0)
            where += "";*/

        String groupBy = "GROUP BY g.name ";
        String orderBy = "ORDER BY g.name asc ";
        String query = select+where+groupBy+orderBy;
        
        return query;
    }

}
