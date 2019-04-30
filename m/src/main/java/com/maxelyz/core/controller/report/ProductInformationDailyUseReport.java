package com.maxelyz.core.controller.report;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
public class ProductInformationDailyUseReport extends AbstractReportBaseController {
    private static Logger log = Logger.getLogger(ProductInformationDailyUseReport.class);
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:productinfodailyuse:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        super.init();
    }

    @Override
    public String getReportPath() {
        return "/report/productinformationDailyUse.jasper";
    }

    @Override
    public Map getParameterMap() {  
        Map parameterMap = new LinkedHashMap();
        parameterMap.put("from", fromDate);
        parameterMap.put("to", toDate);
        return parameterMap;
    }

    @Override
    public String getQuery() {
        String select = "SELECT rpt.contact_date, p.name as product, rpt.total, rpt.useful_total "
                + "FROM rpt_contact_history_product rpt "
                + "INNER JOIN product p on rpt.product_id = p.id ";

        String where = "WHERE rpt.contact_date between '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' ";      
        String orderBy = "ORDER BY rpt.contact_date, p.name ";
        String query = select+where+orderBy;
        
        return query;
    }

}
