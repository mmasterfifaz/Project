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
import com.maxelyz.utils.JSFUtil;
import java.util.LinkedHashMap;

@ManagedBean
@RequestScoped
public class SaleByRecordsReport extends AbstractReportBaseController {

    private static Logger log = Logger.getLogger(SaleByRecordsReport.class);

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:salebyrecords:view")) {
            SecurityUtil.redirectUnauthorize();
        }
        super.init();
        // setup variable to generate excel
        decorateXLSEmptyWhenNull = true;
        decorateXLSStringLine = true;
        // list of excel column would be decorate line style., count col index at buildPrimaryQuery method
        decorateXLSStringLineColumnIdList = new int[]{11, 15, 19, 30};
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
        parameterMap.put("marketing_id", marketingId);
        parameterMap.put("marketing_name", marketingName);
        parameterMap.put("listtype_status", listTypeStatus);
        parameterMap.put("paymentmethod_id", paymentMethodId);
        parameterMap.put("paymentmethod_name", paymentMethodName);
        parameterMap.put("product_id", productId);
        parameterMap.put("product_name", productName);
        
        return parameterMap;
    }
//select mk.name as list,po.ref_no2 as ref_no, po.purchase_date as sale_date , reg.name+' '+isnull(reg.surname,'') as customer_name, u.agent_code, u.name+' '+u.surname as tsr, g.name as team, ….

    @Override
    public String getQuery() {
        return buildPrimaryQuery(false);
    }

    @Override
    public String getExcelQuery() {
        return buildPrimaryQuery(true);
    }

    private String buildPrimaryQuery(boolean withTitleReportColumn) {
//        System.out.println(marketingId+listTypeStatus+paymentMethodId+productId);
        StringBuilder select = new StringBuilder("with tx as ( \n");
        select.append("     select po.id as id, p.code as product_code, pp.name as product_plan, mk.name as list,po.ref_no as ref_no,  po.purchase_date ,dbo.fnGetSaleDateMTL(po.id, po.create_by_id) as effective_date, reg.name+' '+isnull(reg.surname,'') as customer_name, u.agent_code as agent_code, u.name+' '+u.surname as tsr, g.name as team, ")
                .append("         CASE WHEN cast(convert(varchar(10), dbo.[fnGetFirstContactDate](po.assignment_detail_id), 126) as datetime) = cast(convert(varchar(10), po.purchase_date, 126) as datetime) THEN 'New' ELSE 'Old' END as contact_type, ")
                .append("         CASE WHEN pmtd.creditcard = 1 THEN 'Credit' WHEN pmtd.debitcard = 1 THEN 'Debit' ELSE 'Unknown' END payment_type, ")
                .append("         po.net_premium as net_premium, po.total_amount as total_premium, ")
                .append("         po.annual_net_premium afyp, po.qc_date, ")
                .append("         po.approve_date as uw_date, po.approval_status as uw_status, po.payment_date, po.payment_status, po.qc_status, ")
                .append("         [dbo].[fnMappingRegistrationFieldWithRegistrationData](p.id,'fx44',reg.fx44) as [fx44]," +
                        "         [dbo].[fnMappingRegistrationFieldWithRegistrationData](p.id,'fx45',reg.fx45) as [fx45]," +
                        "         [dbo].[fnMappingRegistrationFieldWithRegistrationData](p.id,'fx46',reg.fx46) as [fx46]," +
                        "         [dbo].[fnMappingRegistrationFieldWithRegistrationData](p.id,'fx47',reg.fx47) as [fx47]," +
                        "         [dbo].[fnMappingRegistrationFieldWithRegistrationData](p.id,'fx48',reg.fx48) as [fx48]," +
                        "         [dbo].[fnMappingRegistrationFieldWithRegistrationData](p.id,'fx50',reg.fx50) as [fx50]")
                .append("     from purchase_order po ")
                .append("         inner join purchase_order_detail pod on pod.purchase_order_id = po.id ")
                .append("         inner join purchase_order_register reg on reg.purchase_order_detail_id = pod.id and reg.no = 1 ")
                .append("         inner join product p on p.id = pod.product_id ")
                .append("         inner join product_plan pp on pp.id = pod.product_plan_id ")
                .append("         inner join customer c on c.id = po.customer_id ")
                .append("         inner join users u on u.id = po.create_by_id ")
                .append("         inner join user_group g on g.id = u.user_group_id ")
                .append("         inner join assignment_detail ad on ad.id = po.assignment_detail_id ")
                .append("         inner join assignment a on a.id = ad.assignment_id ")
                .append("         inner join marketing mk on mk.id = a.marketing_id ")
                .append("         inner join dbo.payment_method pmtd on po.payment_method = pmtd.id ");

        String where = "\n    where po.purchase_date between '" + sdf.format(fromDate) + "' and '" + sdf.format(toDate) + "' and po.sale_result= 'Y' ";
        if (userGroupId != 0) {
            this.getUsersDAO().getAllAgentBySup(JSFUtil.getUserSession().getUsers());
            where += "AND u.user_group_id = " + userGroupId + " ";
        }
        if (userId != 0) {
            where += "AND u.id = " + userId + " ";
        }
        if (campaignId != 0) {
            where += "and a.campaign_id = " + campaignId + " ";
        }
        if (marketingId!=0) {
            where += "AND a.marketing_id = " + marketingId + " ";
        }
        if (productId!=0) {
            where += "AND pod.product_id = " + productId + " ";
        }
        if (paymentMethodId != 0) {
            where += "and po.payment_method = " + paymentMethodId + " ";
        }
        if (listTypeStatus.equalsIgnoreCase("new")) {
            where += "AND cast(convert(varchar(10), dbo.[fnGetFirstContactDate](po.assignment_detail_id), 126) as datetime) = cast(convert(varchar(10), po.purchase_date, 126) as datetime) ";
        } else if (listTypeStatus.equalsIgnoreCase("old")) {
            where += "AND cast(convert(varchar(10), dbo.[fnGetFirstContactDate](po.assignment_detail_id), 126) as datetime) <> cast(convert(varchar(10), po.purchase_date, 126) as datetime) ";
        }

        select.append(where)
                .append("\n), \n");

        select.append("apps_list  as (\n")
                .append("       select appl.purchase_order_id,appl.approval_status,appl.create_by_role,appl.message , appr.name as name ,\n")
                .append("           ROW_NUMBER() over (partition by appl.purchase_order_id, appl.create_by_role order by appl.id desc) as log_seq\n")
                .append("       from purchase_order_approval_log appl \n")
                .append("           join tx on appl.purchase_order_id=tx.id \n")
                .append("           join approval_reason appr on appl.approval_reason_id=appr.id \n")
                .append("   ),    \n")
                .append("apps_byrole_list as (\n")
                .append("       select CASE WHEN pmt.pmid  is not null THEN  pmt.pmid WHEN qct.qcid is not null THEN  qct.qcid  WHEN uwt.uwid  is not null THEN  uwt.uwid  ELSE null END as apoid, \n")
                .append("           vuw_reason,vuw_message,vqc_reason,vqc_message,vpm_reason,vpm_message \n")
                .append("       from \n")
                .append("           ( select uw.purchase_order_id as uwid, uw.approval_status as vuw_appstatus, uw.name as vuw_reason, uw.message as vuw_message \n")
                .append("               from apps_list uw where uw.log_seq=1 and create_by_role='Uw' ) uwt \n")
                .append("		full outer join \n")
                .append("           ( select qc.purchase_order_id as qcid, qc.approval_status as vqc_appstatus, qc.name as vqc_reason, qc.message as vqc_message \n")
                .append("               from apps_list qc where qc.log_seq=1 and create_by_role='Qc' ) qct on qct.qcid=uwt.uwid \n")
                .append("		full outer join \n")
                .append("           ( select pm.purchase_order_id as pmid, pm.approval_status as vpm_appstatus, pm.name as vpm_reason, pm.message as vpm_message \n")
                .append("               from apps_list pm where pm.log_seq=1 and create_by_role='Payment' ) pmt on pmt.pmid=qct.qcid\n")
                .append("  )");

        //Step : Create Column title
        if (withTitleReportColumn) {
            select.append("select " +
                    "tx.list as 'Marketing Name', tx.product_code as 'Product Code', tx.product_plan as 'Product Plan',tx.ref_no as 'Reference No.',tx.purchase_date as 'Purchase Date',tx.effective_date as 'Effective Date',\n" +
                    "")
                    .append("   tx.customer_name as 'Customer Name',tx.tsr as 'User Name',tx.agent_code as 'Agent Code',tx.team as 'User Group',\n")
                    .append("   tx.contact_type as 'Contact Type',\n")
                    .append("   tx.payment_type as 'Payment Type',\n")
                    .append("   tx.net_premium as 'Premium',\n")
                    .append("   tx.total_premium as 'Total Premium',\n")
                    .append("   tx.afyp as 'AFYP',\n")
                    .append("   tx.uw_date as 'UW Date' ,  tx.uw_status as 'UW Status' , apps_byrole_list.vuw_reason as 'UW Reason', apps_byrole_list.vuw_message as 'UW Remark' ,\n")
                    .append("   tx.payment_date as 'Payment Date' ,  tx.payment_status as 'Payment Status' , apps_byrole_list.vpm_reason as 'Payment Reason', apps_byrole_list.vpm_message as 'Payment Remark' ,\n")
                    .append("   tx.qc_date as 'QC Date' ,  tx.qc_status as 'QC Status' , apps_byrole_list.vqc_reason as 'QC Reason', apps_byrole_list.vqc_message as 'QC Remark', \n")
                    .append("   cast(tx.fx44 as varchar(4000)) as 'ชื่อบริษัท' ,  cast(tx.fx45 as varchar(4000)) as 'Product' , CONVERT(varchar(4000),tx.fx46) as 'Class', cast(tx.fx47 as varchar(4000)) as 'เบี้ยประกันรวม', cast(tx.fx48 as varchar(4000)) as 'เบี้ยประกันหลังหักส่วนลด', cast(tx.fx50 as varchar(4000)) as 'บันทึกการขาย' \n");
        } else {
            select.append("select tx.* , apps_byrole_list.* \n ");
        }
        //Step : Create from and order
        select.append("from tx left join apps_byrole_list on tx.id=apps_byrole_list.apoid  order by  tx.purchase_date, tx.list , tx.customer_name ");
        //System.out.print(select.toString());
        log.info(this.getClass().getName() + ".buildPrimaryQuery [Print report with SQL :] => " + select.toString());
        return select.toString();

    }

}
