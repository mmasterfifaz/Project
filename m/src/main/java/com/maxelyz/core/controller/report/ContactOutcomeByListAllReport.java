package com.maxelyz.core.controller.report;


import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.apache.log4j.Logger;
import com.maxelyz.utils.SecurityUtil;
import java.util.LinkedHashMap;

@ManagedBean
@RequestScoped
public class ContactOutcomeByListAllReport extends AbstractReportBaseController {
    private static Logger log = Logger.getLogger(ContactOutcomeByListAllReport.class);
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:calloutcomebylistall:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        super.init();
    }

    @Override
    public String getReportPath() {
        return "/report/contactOutcomeByListAll.jasper";
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

        String where = "and ch.create_date between  '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' ";
        if (userGroupId!=0)
            where += "AND g.id = " + userGroupId + " ";
        if (userId!=0)
            where += "AND u.id = " + userId + " ";
        if (campaignId!=0)
            where += "AND a.campaign_id = " + campaignId + " ";

        String wheremain = where + "AND cr.contact_status <> 'inbound' ";
        
        String select = "select mk.name as marketing_name, cr.contact_status as contact_status, cr.name as name, count(ch.id) as total, 0.0 as ratio, seq_no "
                + "from assignment_detail ad "
                + "inner join assignment a on a.id = ad.assignment_id "
                + "inner join marketing mk on mk.id = a.marketing_id "
                + "inner join ("
                + "select max(ch.id) as contact_history_id, cha.assignment_detail_id as assignment_detail_id from contact_history ch "
                + " inner join contact_history_assignment cha on cha.contact_history_id = ch.id "
                + " where ch.contact_date between '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' "
                + " group by cha.assignment_detail_id"
                + ") cha on cha.assignment_detail_id = ad.id "
                + "inner join contact_history ch on ch.id = cha.contact_history_id "
                + "inner join contact_result cr on ch.contact_result_id = cr.id "
                + "inner join users u on u.id = ch.create_by_id "
                + "inner join user_group g on g.id = u.user_group_id "
                + "where ad.status = 'closed' "
                + wheremain
                + "group by mk.name, cr.contact_status, cr.name, cr.seq_no ";
        

        String orderBy = "order by mk.name, contact_status, cr.name";
          

        String query = select+orderBy;
        
        return query;
    }

}
