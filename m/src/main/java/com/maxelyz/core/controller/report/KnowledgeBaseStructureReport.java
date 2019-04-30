package com.maxelyz.core.controller.report;

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
public class KnowledgeBaseStructureReport extends AbstractReportBaseController {
    private static Logger log = Logger.getLogger(KnowledgeBaseStructureReport.class);
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:kbstructure:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        super.init();
    }

    @Override
    public String getReportPath() {
        return "/report/knowledgebasestructure.jasper";
    }

    @Override
    public Map getParameterMap() {
        Map parameterMap = new LinkedHashMap();
        //parameterMap.put("from", fromDate);
        //parameterMap.put("to", toDate);
        return parameterMap;
    }

    @Override
    public String getQuery() {
        String select = "select kb1.topic as topic1, kb2.topic as topic2, kb3.topic as topic3, kb4.topic as topic4, kb5.topic as topic5 from knowledgebase kb1 "
                + "left join knowledgebase kb2 on kb1.id = kb2.ref_id and kb2.enable = 1 "
                + "left join knowledgebase kb3 on kb2.id = kb3.ref_id and kb3.enable = 1 "
                + "left join knowledgebase kb4 on kb3.id = kb4.ref_id and kb4.enable = 1 "
                + "left join knowledgebase kb5 on kb4.id = kb5.ref_id and kb5.enable = 1 ";
        String where = "where kb1.ref_id is null and kb1.enable = 1  ";
        String orderBy = "order by kb1.topic, kb2.topic, kb3.topic, kb4.topic, kb5.topic ";
        String query = select+where+orderBy;

        return query;
    }

}
