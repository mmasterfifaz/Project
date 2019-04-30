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
public class KnowledgebaseTop20Report extends AbstractReportBaseController {
    private static Logger log = Logger.getLogger(KnowledgebaseTop20Report.class);
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:top20kb:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        super.init();
        Calendar c =  Calendar.getInstance();
        c.roll(Calendar.YEAR, -1);
        this.fromDate = c.getTime();
    }

    @Override
    public String getReportPath() {
        return "/report/knowledgebasetop20.jasper";
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
        String innerselect = "SELECT TOP 1000 kb.topic, SUM(rpt.useful_total) as total "
                + "FROM rpt_contact_history_knowledge rpt "
                + "inner join knowledgebase kb on rpt.knowledgebase_id = kb.id ";
        String where = "WHERE rpt.contact_date between '" + sdf.format(fromDate) + "' AND '" + sdf.format(toDate) + "' AND kb.enable = 1 ";
        String groupBy = "group by kb.topic ";
        String orderBy = "order by total desc, kb.topic";

        String query = "select ROW_NUMBER() OVER (ORDER BY total desc, topic asc) AS no, kbrpt.topic as topic, kbrpt.total as total "
                + "from ("+innerselect+where+groupBy+orderBy+") as kbrpt ";
        
        return query;
    }

}
