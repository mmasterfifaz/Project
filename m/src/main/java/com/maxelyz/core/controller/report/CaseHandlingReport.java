package com.maxelyz.core.controller.report;

import com.maxelyz.utils.JSFUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;
//use this controller in /front/customerhandling/caseHandling.xhtml
@ManagedBean
@RequestScoped
public class CaseHandlingReport extends AbstractReportBaseController {
    private static Logger log = Logger.getLogger(CaseHandlingReport.class);
    private int contactCaseId;

    @PostConstruct
    public void initialize() {
        super.init();
        this.useResultSet=false; //change to use query in jasper report
        this.outputFormat=PDF;
        try {
            contactCaseId = Integer.parseInt(JSFUtil.getRequestParameterMap("contact_case_id"));
        } catch (Exception e) {
            log.error(e);
        }
    }


    @Override
    public String getReportPath() {
        return "/report/caseHandling.jasper";
    }

    @Override
    public Map getParameterMap() {
        Map parameterMap = new LinkedHashMap();
        parameterMap.put("contact_case_id", contactCaseId);
        return parameterMap;
    }

    @Override
    public String getQuery() {
        return null;
    }

}
