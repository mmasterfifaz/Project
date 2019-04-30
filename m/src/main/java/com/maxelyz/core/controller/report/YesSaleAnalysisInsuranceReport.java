package com.maxelyz.core.controller.report;

import java.text.SimpleDateFormat;
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
public class YesSaleAnalysisInsuranceReport extends AbstractReportBaseController {
    private static Logger log = Logger.getLogger(YesSaleAnalysisInsuranceReport.class);
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:yessaleanalysis:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        super.init();
    }

    @Override
    public String getReportPath() {
        return "/report/yesSaleAnalysisInsurance.jasper";
    }

    @Override
    public Map getParameterMap() { 
        Map parameterMap = new LinkedHashMap();
        parameterMap.put("from", fromDate);
        parameterMap.put("to", toDate);
        parameterMap.put("campaign_id", campaignId);
        parameterMap.put("campaign_name", campaignName);
        return parameterMap;
    }

    @Override
    public String getQuery() {
        String select = "select mk.code marketing_code, mk.name marketing_name, reg.name+' '+reg.surname as customer_name, reg.idno, reg.gender, reg.age , reg.marital_status, "
                + "reg.occupation_text as occupation, current_province.name as current_province, "
                + "po.sum_insured, po.net_premium, po.annual_net_premium afyp, po.create_by, po.purchase_date, po.ref_no "
                + "from purchase_order po "
                + "inner join assignment_detail ad on ad.id = po.assignment_detail_id "
                + "inner join assignment a on a.id = ad.assignment_id "
                + "inner join marketing mk on mk.id = a.marketing_id "
                + "inner join purchase_order_detail pod on pod.purchase_order_id = po.id "
                + "inner join purchase_order_register reg on reg.purchase_order_detail_id = pod.id and reg.no=1 "
                + "left join district current_dis on current_dis.id = reg.current_district_id "
                + "left join province current_province on current_province.id = current_dis.province_id ";
              
        String where = "where po.sale_date between '"+sdf.format(fromDate)+"' and '"+sdf.format(toDate)+"' and po.sale_result= 'Y' and po.approval_status = 'approved' and po.payment_status = 'approved' ";
        if (campaignId!=0) {
            where += "and a.campaign_id = " + campaignId + " ";
        }
        String orderBy = "ORDER BY mk.code";
        String query = select+where+orderBy;
        
        return query;
    }

}
