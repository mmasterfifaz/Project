package com.maxelyz.core.controller.report;

import com.maxelyz.utils.JSFUtil;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;
import com.maxelyz.utils.SecurityUtil;
import java.util.LinkedHashMap;

@ManagedBean
//@RequestScoped
@ViewScoped
public class CaseListReport extends AbstractReportBaseController {
    private static Logger log = Logger.getLogger(CaseListReport.class);
    private static int MAIN_CASE = 1;
    private static int RELATED_CASE = 2;
    private int caseCondition;
    private Boolean taskDelegate;
    private Integer users_id;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:caselist:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        super.init();
        caseCondition=0;
    }

    public int getCaseCondition() {
        return caseCondition;
    }

    public void setCaseCondition(int caseCondition) {
        this.caseCondition = caseCondition;
    }

    public Boolean getTaskDelegate() {
        return taskDelegate;
    }

    public void setTaskDelegate(Boolean taskDelegate) {
        this.taskDelegate = taskDelegate;
    }



    @Override
    public String getReportPath() {
        return "/report/caseList.jasper";
    }

    @Override
    public Map getParameterMap() {
        String caseTypeName = "All";
        if (caseTypeId!=0)
            caseTypeName = getCaseTypeDAO().findCaseType(caseTypeId).getName();

        String caseStatusName="All";
        if (JSFUtil.getBundleValue("pendingValue").equals(caseStatus))
            caseStatusName = JSFUtil.getBundleValue("pending");
        else if (JSFUtil.getBundleValue("closedValue").equals(caseStatus))
            caseStatusName = JSFUtil.getBundleValue("closed");

        Map parameterMap = new LinkedHashMap();
        parameterMap.put("from", fromDate);
        parameterMap.put("to", toDate);
        parameterMap.put("case_status_key", caseStatus);
        parameterMap.put("case_status_value", caseStatusName);
        parameterMap.put("case_type_id", caseTypeId);
        parameterMap.put("case_type_name", caseTypeName);
        parameterMap.put("user_group_id", userGroupId);
        parameterMap.put("user_group_name", groupName);
        parameterMap.put("user_id", userId);
        parameterMap.put("user_name", userName);
        parameterMap.put("service_type_id", serviceTypeId);
        parameterMap.put("service_type_name", serviceTypeName);
        
        parameterMap.put("business_unit_id", businessUnitId);
        parameterMap.put("business_unit_name", businessUnitName);
        parameterMap.put("location_id", locationId);
        parameterMap.put("location_name", locationName);
        
        parameterMap.put("printed_by", JSFUtil.getUserSession().getUserName());
        
        return parameterMap;
    }

    @Override
    public String getQuery() {
        users_id = JSFUtil.getUserSession().getUserGroup().getId();
        String select = "SELECT cases.id, cases.code, cases.contact_date, "
                + "cases.contact_person, d.name as case_request, c.name as case_type, "
                + "b.name as case_topic, a.name as case_detail, cases.contact_person, "
                + "customer.name+' '+customer.surname as customer_name, "
                + "service_type.name as service_type_name, cases.location_name, "
                + "relationship.name relationship_name, channel.short_name as channel_name,"
                + "activity_channel.short_name as activity_channel_name,"
                + "u.name as create_by, u_closed.name as closed_by, cases.description as case_description, "
                + "cases.remark as case_remark, cases.priority, cases.sla_close, cases.sla_accept, "
                + "cases.sla_close_date, cases.closed_date, "
                + "(select code from contact_case where id = cases.ref_id) as related_case, "
                + "cases.status as case_status, act_type.name as activity_type, "
                + "act.activity_date as activity_date, act.description as activity_description, "
                + "act.due_date as due_date, act.receive_status as receive_status, user_receiver.name as user_receiver_name, user_sender.name as user_sender_name "
                + "FROM contact_case as cases "
                + "INNER JOIN case_detail AS a ON a.id = cases.case_detail_id "
                + "INNER JOIN case_topic AS b ON b.id = a.case_topic_id "
                + "INNER JOIN case_type AS c ON c.id = b.case_type_id "
                + "INNER JOIN channel ON channel.id = cases.channel_id "
                + "INNER JOIN service_type ON service_type.id = cases.service_type_id "
                + "INNER JOIN customer ON customer.id = cases.customer_id "
                + "LEFT JOIN case_request AS d ON d.id = cases.case_request_id "
                + "LEFT JOIN relationship ON relationship.id = cases.relationship_id "
                + "LEFT JOIN activity AS act ON act.contact_case_id = cases.id "
                + "LEFT JOIN channel as activity_channel ON activity_channel.id = act.channel_id "
                + "LEFT JOIN activity_type as act_type ON act_type.id = act.activity_type_id "
                + "LEFT JOIN users AS u ON u.id = cases.create_by_id "
                + "LEFT JOIN users AS u_closed ON u_closed.id = cases.closed_by_id "
                + "LEFT JOIN users AS user_receiver ON user_receiver.id = act.user_receiver_id "
                + "LEFT JOIN users AS user_sender ON user_sender.id = act.user_sender_id "
                + "RIGHT JOIN (SELECT * FROM user_group_location WHERE user_group_id = '" + users_id + "') AS l ON l.location_id = cases.location_id AND l.service_type_id = cases.service_type_id "
                + "INNER JOIN user_group AS g ON g.id = u.user_group_id ";

        String where = "WHERE cases.contact_date between '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' ";
        if (caseTypeId!=0) 
           where += "AND c.id = "+caseTypeId+" ";
        if (userGroupId!=0)
            where += "AND g.id = " + userGroupId + " ";
        if (userId!=0) 
            where += "AND u.id = " + userId + " ";
        if (serviceTypeId!=0)
            where += "AND cases.service_type_id = " + serviceTypeId + " ";
         //---------------------
        if (businessUnitId!=0){
            where += "AND cases.business_unit_id = " + businessUnitId + " ";
            if (locationId!=0)
                where += "AND cases.location_id = " + locationId + " ";
        }
        //----------------
        if (caseStatus.equals(CASE_PENDING)) {
           where += "AND cases.status = '"+JSFUtil.getBundleValue("pendingValue")+"' ";
        } else if (caseStatus.equals(CASE_CLOSED)) {
           where += "AND cases.status = '"+JSFUtil.getBundleValue("closedValue")+"' ";
        }
        if (caseCondition==MAIN_CASE)
            where += "AND cases.ref_id is null ";
        else   if (caseCondition==RELATED_CASE)
            where += "AND cases.ref_id is not null ";
        if (taskDelegate)
            where += "AND act_type.task_delegate = 1 ";
        String orderBy = "ORDER BY cases.contact_date, cases.code, act.activity_date ";
        String query = select+where+orderBy;
  
        return query;
    }

}
