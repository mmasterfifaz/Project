/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.report;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.apache.log4j.Logger;
import com.maxelyz.utils.SecurityUtil;
import java.util.Locale;
import java.util.LinkedHashMap;

@ManagedBean
@RequestScoped
public class CustomerStatByChannelReport extends AbstractReportBaseController {

    private static Logger log = Logger.getLogger(CustomerStatByChannelReport.class);

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:customerstatbychannel:view")) {
            SecurityUtil.redirectUnauthorize();
        }
        super.init();
    }

    @Override
    public String getReportPath() {
        return "/report/customerstatbychannel.jasper";
    }

    @Override
    public Map getParameterMap() {
        Map parameterMap = new LinkedHashMap();
        parameterMap.put("from", fromDate);
        parameterMap.put("to", toDate);
        parameterMap.put("channelType", channelType);
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
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

        String select = "select "
                + " CONVERT(date,ch.create_date) as  report_date"
                + " , CASE WHEN CONVERT(date,ch.create_date)=CONVERT(date,c2.create_date) THEN 1 ELSE 0  END AS  new "
                + ", CASE WHEN CONVERT(date,ch.create_date)<> CONVERT(date,c2.create_date) THEN 1 ELSE 0  END AS   existing";
        //+ ", (select count(*) from  contact_history  call1 inner join customer c1 on c1.id = call1.customer_id where CONVERT(date,call1.create_date)= CONVERT(date,ch.create_date) and CONVERT(date,call1.create_date)=CONVERT(date,c1.create_date) and call1.customer_id is not null  )as new"
        //+ ", (select count(*) from  contact_history  call2 inner join customer c2 on c2.id = call2.customer_id where CONVERT(date,call2.create_date)= CONVERT(date,ch.create_date) and CONVERT(date,call2.create_date)<>CONVERT(date,c2.create_date) and call2.customer_id is not null  )as existing"
        if (channelType.isEmpty()) {
            select += ", 'Inbound/Outbound' as channelType ";
        } else {
            select += ", cha.type as channelType ";
        }
        select += ", cha.name as channel "
                + " from contact_history ch "
                + " inner join channel cha on cha.id = ch.channel_id"
                + " inner join customer c2 on c2.id = ch.customer_id"
                + " inner join users u on u.id = ch.create_by_id"
                + " where ch.customer_id is not null "
                + " and ch.create_date between  '" + sdfs.format(fromDate) + "' AND '" + sdfs.format(toDate) + "'";
        if (!channelType.isEmpty()) {
            select += " and cha.type='" + channelType.trim() + "'";
        }

                if (userGroupId!=0){
            select += "AND u.user_group_id = " + userGroupId + " ";}
        if (userId!=0){
            select += "AND u.id = " + userId + " ";}

        // + " group by ch.channel_id,cha.type, cha.name,CONVERT(date,ch.create_date)"
        select += " group by ch.customer_id,ch.channel_id";
        if (!channelType.isEmpty()) {
            select += ",cha.type";
        }
        select += ",cha.name,CONVERT(date,ch.create_date),CONVERT(date,c2.create_date)"
                + " order by report_date";

 System.out.println("Sql:"+select);
        String query = select;

        return query;
    }
}
