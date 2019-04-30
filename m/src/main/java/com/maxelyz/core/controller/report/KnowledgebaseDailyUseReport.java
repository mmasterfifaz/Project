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
public class KnowledgebaseDailyUseReport extends AbstractReportBaseController {
    private static Logger log = Logger.getLogger(KnowledgebaseDailyUseReport.class);
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:kbdailyuse:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        super.init();
    }

    @Override
    public String getReportPath() {
        return "/report/knowledgebaseDailyUse.jasper";
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
        String select = "SELECT rpt.contact_date, kb.topic as knowledgebase, rpt.total, rpt.useful_total "
                + "FROM rpt_contact_history_knowledge rpt "
                + "INNER JOIN knowledgebase kb on rpt.knowledgebase_id = kb.id ";
        String where = "WHERE rpt.contact_date between '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' AND kb.enable = 1 ";      
        String orderBy = "ORDER BY rpt.contact_date, kb.topic ";
        String query = select+where+orderBy;
        
        return query;
    }

}
