package com.maxelyz.core.controller.report;

import com.maxelyz.utils.JSFUtil;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;
import com.maxelyz.utils.SecurityUtil;
import java.util.LinkedHashMap;

@ManagedBean
@RequestScoped
@ViewScoped
public class FirstCallResolutionReport extends AbstractReportBaseController {
    private static Logger log = Logger.getLogger(FirstCallResolutionReport.class);
    private Integer users_id;
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:firstcall:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        super.init();
    }

    @Override
    public String getReportPath() {
        return "/report/firstCallResolution.jasper";
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
        parameterMap.put("case_type_id", caseTypeId);
        parameterMap.put("case_type_name", caseTypeName);
        parameterMap.put("service_type_id", serviceTypeId);
        parameterMap.put("service_type_name", serviceTypeName);
        
        parameterMap.put("business_unit_id", businessUnitId);
        parameterMap.put("business_unit_name", businessUnitName);
        parameterMap.put("location_id", locationId);
        parameterMap.put("location_name", locationName);
        
        return parameterMap;
    }

    @Override
    public String getQuery() {
        users_id = JSFUtil.getUserSession().getUserGroup().getId();
        String select = "SELECT c.name as case_type_name, b.name as case_topic_name, a.name as case_detail_name, SUM(rpt.total) as total, "
                + "SUM(rpt.firstcall_resolution) as first_call_resolution, "
                + "CASE WHEN SUM(rpt.total)=0 THEN 0 ELSE SUM(rpt.firstcall_resolution)*100.0/SUM(rpt.total) END as first_call_percent "
                + "FROM rpt_contact_channel AS rpt "
                + "INNER JOIN case_detail AS a ON a.id = rpt.case_detail_id "
                + "INNER JOIN case_topic AS b ON b.id = a.case_topic_id "
                + "INNER JOIN case_type AS c ON c.id = b.case_type_id "
                + "INNER JOIN users AS u ON u.id = rpt.user_id "
                + "INNER JOIN user_group AS g ON g.id = u.user_group_id "
                + "RIGHT JOIN (SELECT * FROM user_group_location WHERE user_group_id = '" + users_id + "') AS l ON l.location_id = rpt.location_id AND l.service_type_id = rpt.service_type_id ";
        String where = "WHERE rpt.contact_date between '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' ";
        if (caseTypeId!=0)
            where += "AND c.id = " + caseTypeId + " ";
        if (serviceTypeId!=0)
            where += "AND rpt.service_type_id = " + serviceTypeId + " ";
        //---------------------
        if (businessUnitId!=0){
            where += "AND l.business_unit_id = " + businessUnitId + " ";
            if (locationId!=0)
                where += "AND rpt.location_id = " + locationId + " ";
        }
        //----------------
        if (userGroupId!=0)
            where += "AND g.id = " + userGroupId + " ";
        if (userId!=0)
            where += "AND u.id = " + userId + " ";

        String groupBy = "GROUP BY c.name, b.name, a.name ";
        String orderBy = "ORDER BY c.name, b.name, a.name";
        String query = select+where+groupBy+orderBy;
        
        return query;
    }

}
