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
public class TalktimeReport extends AbstractReportBaseController {
    private static Logger log = Logger.getLogger(TalktimeReport.class);
    @PostConstruct
    public void initialize() {
        //if (!SecurityUtil.isPermitted("report:talktimereport:view")) {
        //    SecurityUtil.redirectUnauthorize();  
        //}
        super.init();
        
    }

    @Override
    public String getReportPath() {
        return "/report/talktimeReport.jasper";
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
        return parameterMap;
    }

    @Override
    public String getQuery() {
        String select = "{call GetTalktimeData('"+ sdf.format(fromDate) + "','" + sdf.format(toDate) +"')}";
        
        
        //String select = "select b.name + ISNULL(' ' + b.surname, '') as user_name" +
                        //" , type as inout_type" +
                        //" , timestamp as inout_time" +
                        //" , ISNULL(logoff_reason, '') as logout_reason" +
                        //" from users_time_attendance a" +
                        //" inner join users b on a.user_id = b.id" +
                        //" inner join user_group c on b.user_group_id = c.id";
                        //" where CONVERT(VARCHAR(10), a.timestamp, 120) = '2015-03-10'" +
                        //" and a.user_id = 171" +
                        //" order by a.id";
               
        //String where = " where a.timestamp between '" + sdf.format(fromDate) + "' and '" + sdf.format(toDate) + "'";
        //if (userGroupId != 0) {
        //    this.getUsersDAO().getAllAgentBySup(JSFUtil.getUserSession().getUsers());
        //    where += " AND b.user_group_id = " + userGroupId + " ";
        //}
        //if (userId != 0) {
        //    where += " AND b.id = " + userId + " ";
        //}
      
        //String orderBy = " ORDER BY a.id";
        String query = select;// + where + orderBy;
      
        return query;
    }

}
