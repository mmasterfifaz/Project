package com.maxelyz.core.job;

import com.maxelyz.core.model.dao.RptSalePerformanceDAO;
import java.util.Date;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.apache.log4j.Logger;
/**
 *
 * @author Oat
 */
public class RptUpdateJob extends QuartzJobBean {
    private static Logger log = Logger.getLogger(RptUpdateJob.class);
    private RptSalePerformanceDAO rptSalePerformanceDAO;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Sale Report Job executing at "+new Date());
        rptSalePerformanceDAO.updateSalePerformanceReport();
    }

    public void setRptSalePerformanceDAO(RptSalePerformanceDAO rptSalePerformanceDAO) {
        this.rptSalePerformanceDAO = rptSalePerformanceDAO;
    }


}
