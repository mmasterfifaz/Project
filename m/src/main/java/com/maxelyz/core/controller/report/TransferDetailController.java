/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
/**
 *
 * @author Administrator
 */
public class TransferDetailController extends AbstractReportBaseController {
    
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:multicase:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        super.init();
    }

    @Override
    public String getReportPath() {
        return "/report/transferdetail.jasper";
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
        parameterMap.put("phone_category_id", phoneCategoryId);
        parameterMap.put("phone_category_name", phoneCategoryName);
        
        return parameterMap;
    }

    @Override
    public String getQuery() {
        String select = "select ct.unique_id as 'Unique ID',ct.request_date as 'Transfer Time',ct.create_by as 'User Name',ct.ani as 'Incoming Call',ct.ani_name as 'ANI Name', "
                    + "ct.call_count as 'Transfer Attempt',ct.dnis as 'Transfer To',ct.dnis_name as 'DNIS Name',pc.name as 'Category',result as 'Result', "
                    + "RIGHT('0' + CONVERT(varchar(2), (ch.talk_time % 3600) / 60), 2)+ ':' + RIGHT('0' + CONVERT(varchar(2), ch.talk_time % 60), 2) as 'Talk Time (Min.)' "
                    + "from contact_transfer ct "
                    + "left join phone_directory pd on  pd.name + ' ' + pd.surname = ct.dnis_name "
                    + "left join phone_directory_category pc on pd.phone_directory_category_id = pc.id "
                    + "left join contact_history ch on ct.contact_history_id = ch.id "
                    + "left join users u on u.id = ct.create_by_id "
                    + "left join user_group g on g.id = u.user_group_id ";
//        
        String where = "WHERE ct.request_date between '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' ";
        if (userGroupId != 0)
            where += "AND g.id = " + userGroupId + " ";
        if (userId != 0)
            where += "AND ct.create_by_id  = " + userId + " ";
        if (phoneCategoryId != 0)
            where += "AND pc.id  = " + phoneCategoryId + " ";
        
        String groupBy = "group by ct.unique_id,ct.create_by,ct.request_date,ct.ani,ct.ani_name,ct.call_count,ct.dnis,ct.dnis_name,pc.name,ct.result,ch.talk_time ";
        String orderBy = "order by ct.request_date ";  //String orderBy = "order by ct.unique_id ";
        String query = select+where+groupBy+orderBy;

        return query;
    }
}
