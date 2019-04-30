package com.maxelyz.social.controller.report;

import com.maxelyz.core.controller.report.AbstractReportBaseController;
import com.maxelyz.utils.SecurityUtil;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.apache.log4j.Logger;

@ManagedBean
@RequestScoped
public class ProductPlanReport extends AbstractReportBaseController {
    private static Logger log = Logger.getLogger(ProductPlanReport.class);
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:saleperformancebyuser:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        super.init();
    }

    @Override
    protected String getReportPath() {
         return "/report/productplan.jasper";
    }

    @Override
    protected Map getParameterMap() {
        Map parameterMap = new LinkedHashMap();
        parameterMap.put("from", fromDate);
        parameterMap.put("to", toDate);
        
        return parameterMap;
    }

    @Override
    protected String getQuery() {
        String query = "select pp.id, ppr.child_product_plan_id as child_id, pp.code, pp.name as plan_name, p.name as product_name, ppd.gender, ppd.from_val, ppd.to_val, ppd.net_premium " +
                "from product_plan pp " +
                "inner join product_plan_detail as ppd on pp.id = ppd.product_plan_id  " +
                "inner join product as p on pp.product_id = p.id " +
                "inner join product_plan_relation as ppr on ppd.product_plan_id = ppr.master_product_plan_id " +
                "where pp.enable = '1' and p.product_plan_coverage = '1' and p.family_product = '1' and p.family_product_logic = '3'";
        return query;
    }
}
