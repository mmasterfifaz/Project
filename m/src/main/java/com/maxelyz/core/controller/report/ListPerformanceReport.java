package com.maxelyz.core.controller.report;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.maxelyz.utils.JSFUtil;
import org.apache.log4j.Logger;
import com.maxelyz.utils.SecurityUtil;
import java.util.LinkedHashMap;

@ManagedBean
@RequestScoped
public class ListPerformanceReport extends AbstractReportBaseController {
    private static Logger log = Logger.getLogger(ListPerformanceReport.class);
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:marketinglistperformance:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        super.init();
    }

    @Override
    public String getReportPath() {
        return "/report/listPerformance.jasper";
//        return "/report/custom/lmg/listPerformance.jasper";
    }

    @Override
    public Map getParameterMap() { 
        Map parameterMap = new LinkedHashMap();
        parameterMap.put("from", fromDate);
        parameterMap.put("to", toDate);
        parameterMap.put("campaign_id", campaignId);
        parameterMap.put("campaign_name", campaignName);
        parameterMap.put("marketing_id", marketingId);
        parameterMap.put("marketing_name", marketingName);
        parameterMap.put("listtype_status", listTypeStatus);
        parameterMap.put("paymentmethod_id", paymentMethodId);
        parameterMap.put("paymentmethod_name", paymentMethodName);
        
        return parameterMap;
    }

    @Override
    public String getQuery() {
//        System.out.println(marketingId+listTypeStatus+paymentMethodId);
        /*String select = "select mk.code, mk.name, mk.start_date, mk.end_date, mk.total_record, mk.total_assigned, "
                + "mk.assigned_no , mk.assigned_latest_date, "
                + "(sum(rpt.list_opened)+sum(rpt.list_followup)) as contactable_active,"
                + "sum(rpt.list_closed_contactable) as contactable_inactive,"
                + "sum(rpt.list_uncontactable) as uncontactable_active,"
                + "sum(rpt.list_closed_uncontactable) as uncontactacle_inactive,"
                + "sum(rpt.list_unreachable) as unreachable_active,"
                + "sum(rpt.list_closed_unreachable) as unreachable_inactive,"
                + "sum(rpt.list_yes_sale) as list_yes_sale, "
                + "sum(rpt.product_yes_pending_sale) as list_yes_pending_sale, "
                + "sum(rpt.list_no_sale) as list_no_sale, "
                + "sum(rpt.list_followup) as list_followup, "
                + "CASE WHEN mk.total_assigned=0 THEN 0 ELSE sum(rpt.list_opened+rpt.list_followup+rpt.list_closed_contactable)*100.0/mk.total_assigned  END  as contact_rate, "
                + "CASE WHEN mk.total_assigned=0 THEN 0 ELSE sum(rpt.list_yes_sale)*100.0/mk.total_assigned END as conversion_rate "
                + "from marketing mk "
                + "inner join rpt_sale_performance1 rpt on mk.id = rpt.marketing_id ";
          String where = "where rpt.sale_date between '"+sdf.format(fromDate)+"' and '"+sdf.format(toDate)+ "' ";
        if (campaignId!=0)
            where += "and rpt.campaign_id = " + campaignId + " ";
        String groupBy = "GROUP BY mk.code, mk.name, mk.start_date, mk.end_date, mk.total_record, mk.total_assigned, mk.assigned_no, mk.assigned_latest_date ";
        String orderBy = "ORDER BY mk.code";
         */
/*
        String select = "select mk.code, mk.name, mk.start_date, mk.end_date, mk.total_record,  "
                + "sum(case when cr.contact_status = 'Contactable' and ad.status <> 'closed' then 1 else 0 end ) "
                + "+sum(case when cr.contact_status = 'Contactable' and ad.status = 'closed' then 1 else 0 end ) "
                + "+sum(case when cr.contact_status = 'Uncontactable' and ad.status <> 'closed' then 1 else 0 end ) "
                + "+sum(case when cr.contact_status = 'Uncontactable' and ad.status = 'closed' then 1 else 0 end ) "
                + "+sum(case when cr.contact_status in ('DMC','DMCNotOffer') and ad.status <> 'closed' then 1 else 0 end ) "
                + "+sum(case when cr.contact_status in ('DMC','DMCNotOffer') and ad.status = 'closed' then 1 else 0 end ) as total_assigned, "
                + ""
                + "mk.total_record "
                + "-sum(case when cr.contact_status = 'Contactable' and ad.status <> 'closed' then 1 else 0 end ) "
                + "-sum(case when cr.contact_status = 'Contactable' and ad.status = 'closed' then 1 else 0 end ) "
                + "-sum(case when cr.contact_status = 'Uncontactable' and ad.status <> 'closed' then 1 else 0 end ) "
                + "-sum(case when cr.contact_status = 'Uncontactable' and ad.status = 'closed' then 1 else 0 end ) "
                + "-sum(case when cr.contact_status in ('DMC','DMCNotOffer') and ad.status <> 'closed' then 1 else 0 end ) "
                + "-sum(case when cr.contact_status in ('DMC','DMCNotOffer') and ad.status = 'closed' then 1 else 0 end ) as list_balance, "
                + ""
                + "(select count(distinct(customer_id)) from assignment_detail ad2 "
                + "inner join assignment a2 on a2.id = ad2.assignment_id "
                + "where a2.marketing_id = mk.id and ad2.unassign = 1 "
                + "and ad2.customer_id not in (select customer_id from assignment_detail where unassign is null)) as list_balance_from_list_used, "

                + "sum(case when cr.contact_status = 'Uncontactable' and ad.status <> 'closed' then 1 else 0 end )as unreachable_active, "
                + "sum(case when cr.contact_status = 'Uncontactable' and ad.status = 'closed' then 1 else 0 end )as unreachable_inactive, "
                + "sum(case when cr.contact_status = 'Contactable' and ad.status <> 'closed' then 1 else 0 end )as uncontactable_active, "
                + "sum(case when cr.contact_status = 'Contactable' and ad.status = 'closed' then 1 else 0 end )as uncontactable_inactive, "
                + "sum(case when cr.contact_status in ('DMC','DMCNotOffer') and ad.status <> 'closed' then 1 else 0 end )as contactable_active, "
                + "sum(case when cr.contact_status in ('DMC','DMCNotOffer') and ad.status = 'closed' then 1 else 0 end )as contactable_inactive, "

                + "(select count(po.id) from purchase_order po "
                + "inner join assignment_detail ad2 on ad2.id = po.assignment_detail_id "
                + "inner join assignment a2 on a2.id = ad2.assignment_id "
                + "where a2.marketing_id = mk.id and po.sale_result = 'Y' "
                + "and po.sale_date between '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' "
                + "and po.approval_status = 'approved' and po.payment_status='approved' and po.qc_status='approved') as list_net_sale, "
                + ""
                + "(select count(po.id) from purchase_order po "
                + "inner join assignment_detail ad2 on ad2.id = po.assignment_detail_id "
                + "inner join assignment a2 on a2.id = ad2.assignment_id "
                + "where a2.marketing_id = mk.id and po.sale_result = 'Y' "
                + "and po.purchase_date between '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' "
                + ") as list_gross_sale, "
                + ""
                + "(select sum(po.annual_net_premium) from purchase_order po "
                + "inner join purchase_order_detail pod on pod.purchase_order_id = po.id "
                + "inner join assignment_detail ad2 on ad2.id = po.assignment_detail_id "
                + "inner join assignment a2 on a2.id = ad2.assignment_id "
                //+ "inner join product_plan pp on pp.id = pod.product_plan_id "
                //+ "inner join product_plan_detail ppd on ppd.product_plan_id = pp.id and ppd.seq_no = 1 "
                + "where a2.marketing_id = mk.id and po.sale_result = 'Y' "
                + "and po.sale_date between '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' "
                + "and po.approval_status = 'approved' and po.payment_status='approved' and po.qc_status='approved') as net_premium, "
                + ""
                + "(select sum(po.annual_net_premium) from purchase_order po "
                + "inner join purchase_order_detail pod on pod.purchase_order_id = po.id "
                + "inner join assignment_detail ad2 on ad2.id = po.assignment_detail_id "
                + "inner join assignment a2 on a2.id = ad2.assignment_id "
                //+ "inner join product_plan pp on pp.id = pod.product_plan_id "
                //+ "inner join product_plan_detail ppd on ppd.product_plan_id = pp.id and ppd.seq_no = 1 "
                + "where a2.marketing_id = mk.id and po.sale_result = 'Y' "
                + "and po.purchase_date between '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' "
                + ") as gross_premium, "
                //+ "and po.approval_status = 'approved'  ) as gross_premium, "

                + " (select count(distinct ad.id) "
                + " from assignment_detail ad "
                + " inner join assignment a on a.id = ad.assignment_id"
//                + " inner join (select min(ch.id) as contact_history_id, cha.assignment_detail_id as assignment_detail_id from contact_history ch "
//                + " inner join contact_history_assignment cha on cha.contact_history_id = ch.id "
//                + " group by cha.assignment_detail_id) as cha on cha.assignment_detail_id = ad.id "
//                + " inner join contact_history on contact_history.id = cha.contact_history_id "
//                + " inner join users u on u.id = ad.user_id"
                + " inner join purchase_order po on po.assignment_detail_id = ad.id"
                + " where ad.update_date between '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' "
                + " and a.marketing_id = mk.id and po.sale_result = 'Y' "
//                + " and cast(convert(varchar(10), contact_history.create_date, 126) as datetime) = cast(convert(varchar(10), po.purchase_date, 126) as datetime) "
                + " AND cast(convert(varchar(10), dbo.[fnGetFirstContactDate](po.assignment_detail_id), 126) as datetime) = cast(convert(varchar(10), po.purchase_date, 126) as datetime) "
                + " ) as list_new_contact, "

                + " (select count(distinct ad.id) "
                + " from assignment_detail ad "
                + " inner join assignment a on a.id = ad.assignment_id"
//                + " inner join (select min(ch.id) as contact_history_id, cha.assignment_detail_id as assignment_detail_id from contact_history ch "
//                + " inner join contact_history_assignment cha on cha.contact_history_id = ch.id "
//                + " group by cha.assignment_detail_id) as cha on cha.assignment_detail_id = ad.id "
//                + " inner join contact_history on contact_history.id = cha.contact_history_id "
//                + " inner join users u on u.id = ad.user_id"
                + " inner join purchase_order po on po.assignment_detail_id = ad.id"
                + " where ad.update_date between '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' "
                + " and a.marketing_id = mk.id and po.sale_result = 'Y' "
//                + " and cast(convert(varchar(10), contact_history.create_date, 126) as datetime) < cast(convert(varchar(10), po.purchase_date, 126) as datetime) "
                + " AND cast(convert(varchar(10), dbo.[fnGetFirstContactDate](po.assignment_detail_id), 126) as datetime) <> cast(convert(varchar(10), po.purchase_date, 126) as datetime) "
                + " ) as list_old_contact, "

                + ""
                + "(select sum(po.annual_net_premium) from purchase_order po "
//                + "inner join purchase_order_detail pod on pod.purchase_order_id = po.id "
                + "inner join assignment_detail ad2 on ad2.id = po.assignment_detail_id "
                + "inner join assignment a2 on a2.id = ad2.assignment_id "
                + "inner join payment_method pmc on pmc.id = po.payment_method and pmc.creditcard = 1 "
                + "where a2.marketing_id = mk.id and po.sale_result = 'Y' "
                + "and po.sale_date between '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' "
                + "AND cast(convert(varchar(10), dbo.[fnGetFirstContactDate](po.assignment_detail_id), 126) as datetime) = cast(convert(varchar(10), po.purchase_date, 126) as datetime) "
                + "and po.approval_status = 'approved' and po.payment_status='approved' and po.qc_status='approved') as list_new_net_premium_credit, "
                + ""
                
                + "(select sum(po.annual_net_premium) from purchase_order po "
//                + "inner join purchase_order_detail pod on pod.purchase_order_id = po.id "
                + "inner join assignment_detail ad2 on ad2.id = po.assignment_detail_id "
                + "inner join assignment a2 on a2.id = ad2.assignment_id "
                + "inner join payment_method pmd on pmd.id = po.payment_method and pmd.debitcard = 1 "
                + "where a2.marketing_id = mk.id and po.sale_result = 'Y' "
                + "and po.sale_date between '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' "
                + "AND cast(convert(varchar(10), dbo.[fnGetFirstContactDate](po.assignment_detail_id), 126) as datetime) = cast(convert(varchar(10), po.purchase_date, 126) as datetime) "
                + "and po.approval_status = 'approved' and po.payment_status='approved' and po.qc_status='approved') as list_new_net_premium_debit, "
                + ""
                
                +"(select sum(po.annual_net_premium) from purchase_order po "
//                + "inner join purchase_order_detail pod on pod.purchase_order_id = po.id "
                + "inner join assignment_detail ad2 on ad2.id = po.assignment_detail_id "
                + "inner join assignment a2 on a2.id = ad2.assignment_id "
                + "inner join payment_method pmc on pmc.id = po.payment_method and pmc.creditcard = 1 "
                + "where a2.marketing_id = mk.id and po.sale_result = 'Y' "
                + "and po.purchase_date between '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' "
                + "AND cast(convert(varchar(10), dbo.[fnGetFirstContactDate](po.assignment_detail_id), 126) as datetime) = cast(convert(varchar(10), po.purchase_date, 126) as datetime) "
                + ") as list_new_gross_premium_credit, "
                
                +"(select sum(po.annual_net_premium) from purchase_order po "
//                + "inner join purchase_order_detail pod on pod.purchase_order_id = po.id "
                + "inner join assignment_detail ad2 on ad2.id = po.assignment_detail_id "
                + "inner join assignment a2 on a2.id = ad2.assignment_id "
                + "inner join payment_method pmd on pmd.id = po.payment_method and pmd.debitcard = 1 "
                + "where a2.marketing_id = mk.id and po.sale_result = 'Y' "
                + "and po.purchase_date between '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' "
                + "AND cast(convert(varchar(10), dbo.[fnGetFirstContactDate](po.assignment_detail_id), 126) as datetime) = cast(convert(varchar(10), po.purchase_date, 126) as datetime) "
                + ") as list_new_gross_premium_debit, "
                
                + ""
                + "(select sum(po.annual_net_premium) from purchase_order po "
//                + "inner join purchase_order_detail pod on pod.purchase_order_id = po.id "
                + "inner join assignment_detail ad2 on ad2.id = po.assignment_detail_id "
                + "inner join assignment a2 on a2.id = ad2.assignment_id "
                + "inner join payment_method pmc on pmc.id = po.payment_method and pmc.creditcard = 1 "
                + "where a2.marketing_id = mk.id and po.sale_result = 'Y' "
                + "and po.sale_date between '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' "
                + "AND cast(convert(varchar(10), dbo.[fnGetFirstContactDate](po.assignment_detail_id), 126) as datetime) <> cast(convert(varchar(10), po.purchase_date, 126) as datetime) "
                + "and po.approval_status = 'approved' and po.payment_status='approved' and po.qc_status='approved') as list_old_net_premium_credit, "
                + ""

                + "(select sum(po.annual_net_premium) from purchase_order po "
//                + "inner join purchase_order_detail pod on pod.purchase_order_id = po.id "
                + "inner join assignment_detail ad2 on ad2.id = po.assignment_detail_id "
                + "inner join assignment a2 on a2.id = ad2.assignment_id "
                + "inner join payment_method pmd on pmd.id = po.payment_method and pmd.debitcard = 1 "
                + "where a2.marketing_id = mk.id and po.sale_result = 'Y' "
                + "and po.sale_date between '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' "
                + "AND cast(convert(varchar(10), dbo.[fnGetFirstContactDate](po.assignment_detail_id), 126) as datetime) <> cast(convert(varchar(10), po.purchase_date, 126) as datetime) "
                + "and po.approval_status = 'approved' and po.payment_status='approved' and po.qc_status='approved') as list_old_net_premium_debit, "
                + ""

                +"(select sum(po.annual_net_premium) from purchase_order po "
//                + "inner join purchase_order_detail pod on pod.purchase_order_id = po.id "
                + "inner join assignment_detail ad2 on ad2.id = po.assignment_detail_id "
                + "inner join assignment a2 on a2.id = ad2.assignment_id "
                + "inner join payment_method pmc on pmc.id = po.payment_method and pmc.creditcard = 1 "
                + "where a2.marketing_id = mk.id and po.sale_result = 'Y' "
                + "and po.purchase_date between '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' "
                + "AND cast(convert(varchar(10), dbo.[fnGetFirstContactDate](po.assignment_detail_id), 126) as datetime) <> cast(convert(varchar(10), po.purchase_date, 126) as datetime) "
                + ") as list_old_gross_premium_credit, "

                +"(select sum(po.annual_net_premium) from purchase_order po "
//                + "inner join purchase_order_detail pod on pod.purchase_order_id = po.id "
                + "inner join assignment_detail ad2 on ad2.id = po.assignment_detail_id "
                + "inner join assignment a2 on a2.id = ad2.assignment_id "
                + "inner join payment_method pmd on pmd.id = po.payment_method and pmd.debitcard = 1 "
                + "where a2.marketing_id = mk.id and po.sale_result = 'Y' "
                + "and po.purchase_date between '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' "
                + "AND cast(convert(varchar(10), dbo.[fnGetFirstContactDate](po.assignment_detail_id), 126) as datetime) <> cast(convert(varchar(10), po.purchase_date, 126) as datetime) "
                + ") as list_old_gross_premium_debit "

                + "from marketing mk "
                + "inner join assignment a on a.marketing_id = mk.id "
                + "inner join assignment_detail ad on ad.assignment_id = a.id "
                + "inner join ("
                + "select max(cha.contact_history_id) as contact_history_id, assignment_detail_id "
                + "from contact_history_assignment cha "
                + "inner join contact_history ch on ch.id = cha.contact_history_id "
                + "where ch.contact_date between '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' "
                + "group by cha.assignment_detail_id"
                + ") cha on cha.assignment_detail_id = ad.id "
                + "inner join contact_history ch on ch.id = cha.contact_history_id "
                + "inner join contact_result cr on cr.id = ch.contact_result_id "
//                + "where cha.contact_history_id in ("
//                + "select max(cha.contact_history_id) contact_history_id "
//                + "from contact_history_assignment cha "
//                + "inner join contact_history ch on ch.id = cha.contact_history_id "
//                + "where ch.contact_date between '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' "
//                + "group by cha.assignment_detail_id"
//                + ") "
                + "where ch.contact_date between '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' ";

              
        String where = "and ad.unassign is null ";
        if (campaignId!=0)
            where += "and a.campaign_id = " + campaignId + " ";
        if (marketingId!=0)
            where += "AND a.marketing_id = " + marketingId + " ";
        String groupBy = "group by mk.id,mk.code, mk.name, mk.start_date, mk.end_date, mk.total_record ";
        String orderBy = "ORDER BY code";      
//        String innerquery = select+where+groupBy;
//        String query = "select *, contactable_active+contactable_inactive+uncontactable_active+uncontactable_inactive+unreachable_active+unreachable_inactive as total_assigned from ("+innerquery+") q "+orderBy;
//        System.out.println(query);
        String innerquery = select+where+groupBy+orderBy;

        System.out.println(innerquery);
        return innerquery;
*/
        String query = "exec spRptListPerformance " + JSFUtil.getUserSession().getUsers().getId() + ", '" + sdf.format(fromDate) + "', '" + sdf.format(toDate) + "', " + campaignId.toString() + ", " + marketingId.toString();
        System.out.println(query);
        return query;
    }

}
