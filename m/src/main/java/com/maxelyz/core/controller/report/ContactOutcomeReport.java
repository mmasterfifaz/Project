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
public class ContactOutcomeReport extends AbstractReportBaseController {
    private static Logger log = Logger.getLogger(ContactOutcomeReport.class);
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:calloutcome:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        super.init();
    }

    @Override
    public String getReportPath() {
        return "/report/contactOutcome.jasper";
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
        String select = "select "
                + "case when cr.contact_status = 'Contactable' then 'Uncontactable' else "
                + "     case when cr.contact_status = 'Uncontactable' then 'Unreachable' else "
                + "	     case when cr.contact_status = 'DMC' then 'Contactable' else cr.contact_status end "
                + "	end "
                + "end as contact_status "                
                + ", cr.name, count(ch.id) as total "
                + ", ROUND(count(ch.id)*100.0 /sum(count(ch.id)) OVER (),2) as ratio  "
                + "from contact_history ch "
                + "inner join contact_result cr on ch.contact_result_id = cr.id "
                + "inner join contact_history_assignment cha on cha.contact_history_id = ch.id "
                + "inner join assignment_detail ad on ad.id = cha.assignment_detail_id "
                + "inner join assignment a on a.id = ad.assignment_id "
                + "inner join users u on u.id = ch.create_by_id "
                + "inner join user_group g on g.id = u.user_group_id ";
          

        String where = "WHERE ch.create_date between  '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' ";
        where += "and cr.contact_status <> 'inbound' ";
        if (userGroupId!=0)
            where += "AND g.id = " + userGroupId + " ";
        if (userId!=0)
            where += "AND u.id = " + userId + " ";
        if (campaignId!=0)
            where += "AND a.campaign_id = " + campaignId + " ";
        if (marketingId!=0)
            where += "AND a.marketing_id = " + marketingId + " ";


        String groupBy = "GROUP BY cr.contact_status, cr.name, seq_no ";
        String orderBy = "ORDER BY cr.contact_status, cr.seq_no, cr.name";
        String query = select+where+groupBy+orderBy;
        
        return query;
    }

}
