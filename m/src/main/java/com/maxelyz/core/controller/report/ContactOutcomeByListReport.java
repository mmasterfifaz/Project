package com.maxelyz.core.controller.report;

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
public class ContactOutcomeByListReport extends AbstractReportBaseController {
    private static Logger log = Logger.getLogger(ContactOutcomeByListReport.class);
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:calloutcomebylist:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        super.init();
    }

    @Override
    public String getReportPath() {
        return "/report/contactOutcomeByList.jasper";
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

        String where = "and ch.create_date between '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' ";
        if (userGroupId!=0)
            where += "AND g.id = " + userGroupId + " ";
        if (userId!=0)
            where += "AND u.id = " + userId + " ";
        if (campaignId!=0)
            where += "AND a.campaign_id = " + campaignId + " ";
        if (marketingId!=0)
            where += "AND a.marketing_id = " + marketingId + " ";
        String wheremain = where + "AND cr.contact_status <> 'inbound' ";
        
        String select = "select cr.contact_status as contact_status, cr.name as name, count(ch.id) as total, 0.0 as ratio, seq_no "
                + "from assignment_detail ad "
                + "inner join assignment a on a.id = ad.assignment_id "
                + "inner join contact_history_assignment cha on cha.assignment_detail_id = ad.id "
                + "inner join contact_history ch on ch.id = cha.contact_history_id "
                + "inner join contact_result cr on ch.contact_result_id = cr.id "
                + "inner join users u on u.id = ch.create_by_id "
                + "inner join user_group g on g.id = u.user_group_id "
                + "INNER JOIN ( \n"
                + "select max(ch.id) ch_id from contact_history ch  \n"
                + "inner join contact_history_assignment cha on  cha.contact_history_id = ch.id  \n"
                + "and ch.create_date between  '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' "
                + "group by cha.assignment_detail_id) ch_max ON ch.id= ch_max.ch_id "
//                + "where ch.id in ("
//                + " select max(ch.id) from contact_history ch "
//                + " inner join contact_history_assignment cha on  cha.contact_history_id = ch.id "
//                + " and ch.create_date between  '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' "
//                + " group by cha.assignment_detail_id) "
                + "where ad.status = 'closed' "
                + wheremain
                + "group by cr.contact_status, cr.name, cr.seq_no ";
        
        String callBack = "";       
        for (int i=1;i<=5;i++) {
            String opr = (i==5)?">=":"=";
            
            callBack += "union "
                    + "select 'InProgress' as contact_status, 'Callback "+i+"' as name, count(id) as total, 0.0 as ratio, 0 as seq_no from assignment_detail "
                    + "where id in "
                    + "(select ad.id from assignment_detail ad "
                    + "inner join assignment a on a.id = ad.assignment_id "
                    + "inner join contact_history_assignment cha on cha.assignment_detail_id = ad.id "
                    + "inner join contact_history ch on ch.id = cha.contact_history_id "
                    + "inner join users u on u.id = ch.create_by_id "
                    + "inner join user_group g on g.id = u.user_group_id "
                    + "where  ad.status = 'opened' "
                    + where
                    + "group by ad.id "
                    + "having count(ch.id) "+opr+i+") ";
        }
        String followup = "";       
        for (int i=1;i<=5;i++) {
            String opr = (i==5)?">=":"=";
        
            followup += "union "
                    + "select 'InProgress' as contact_status, 'Follow up "+i+"' as name, count(id) as total, 0.0 as ratio, 0 as seq_no from assignment_detail "
                    + "where id in "
                    + "(select ad.id from assignment_detail ad "
                    + "inner join assignment a on a.id = ad.assignment_id "
                    + "inner join contact_history_assignment cha on cha.assignment_detail_id = ad.id "
                    + "inner join contact_history ch on ch.id = cha.contact_history_id "
                    + "inner join users u on u.id = ch.create_by_id "
                     + "inner join user_group g on g.id = u.user_group_id "
                    + "where  ad.status = 'followup' "
                    + where
                    + "group by ad.id "
                    + "having count(ch.id) "+opr+i+") ";
        }

        String orderBy = "order by contact_status";
          
        select = "select contact_status, name, total, total*100.0/(sum(total) over ()) as ratio from ("+select+callBack+followup+") as sub ";
        String groupby = "group by contact_status, name, seq_no, total "; 
        String orderby = "order by contact_status, seq_no, name, total "; 
        String query = select+groupby+orderby;
        
        return query;
    }

}
