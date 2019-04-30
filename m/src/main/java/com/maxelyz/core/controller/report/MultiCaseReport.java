package com.maxelyz.core.controller.report;

import com.maxelyz.utils.JSFUtil;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import com.maxelyz.utils.SecurityUtil;
import java.util.LinkedHashMap;

@ManagedBean
@RequestScoped
@ViewScoped
public class MultiCaseReport extends AbstractReportBaseController {

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:multicase:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        super.init();
    }

    @Override
    public String getReportPath() {
        return "/report/multicase.jasper";
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
        //Integer users_id = JSFUtil.getUserSession().getUserGroup().getId();
        
        String select = "select ch.id as contact_history_id, ch.contact_date, channel.short_name as channel_name, customer.name+' '+customer.surname as customer_name, cc.code as case_code, a.name+'/'+b.name+'/'+c.name as case_type, sr.name as service_type_name, cc.status as case_status from contact_case cc "
                + "inner join contact_history ch on ch.id = cc.contact_history_id "
                + "inner join "
                + "(select cc.contact_history_id from contact_case cc "
                + "group by cc.contact_history_id "
                + "having count(cc.contact_history_id) > 1) ch1 on ch1.contact_history_id = cc.contact_history_id "
                + "inner join channel  on channel.id = ch.channel_id "
                + "inner join customer on customer.id = ch.customer_id "
                + "inner join case_detail c on c.id = cc.case_detail_id "
                + "inner join case_topic b on b.id = c.case_topic_id "
                + "inner join case_type a on a.id = b.case_type_id "
                + "inner join service_type sr on sr.id = cc.service_type_id "
                + "INNER JOIN users AS u ON u.id = ch.create_by_id "
                + "INNER JOIN user_group AS g ON g.id = u.user_group_id ";
             
        String where = "WHERE ch.contact_date between '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' ";
        if (userGroupId!=0)
            where += "AND g.id = " + userGroupId + " ";
        if (userId!=0)
            where += "AND u.id = " + userId + " ";
    
        String orderBy = "order by ch.contact_date ";
        String query = select+where+orderBy;

        return query;
    }

}
