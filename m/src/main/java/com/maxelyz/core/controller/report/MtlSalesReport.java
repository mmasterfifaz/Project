package com.maxelyz.core.controller.report;

import com.maxelyz.core.model.dao.CampaignDAO;
import com.maxelyz.core.model.dao.MarketingDAO;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.log4j.Logger;
import com.maxelyz.utils.SecurityUtil;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFCreationHelper;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;

@ManagedBean
@RequestScoped
public class MtlSalesReport {
    private static Logger log = Logger.getLogger(MtlSalesReport.class);
    
    private Integer campaignId;
    private String campaignName = "All";
    private Integer marketingId;
    private String marketingName = "All";
    private Date fromDate;
    private Date toDate;
    private Integer period;
    private Integer group;
    
    
    @ManagedProperty(value = "#{dataSource}")
    private DataSource dataSource;
    @ManagedProperty(value = "#{campaignDAO}")
    private CampaignDAO campaignDAO;
    @ManagedProperty(value = "#{marketingDAO}")
    private MarketingDAO marketingDAO;
    
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:salesreport:view")) {
            SecurityUtil.redirectUnauthorize();
        }
        
        fromDate = new Date();
        toDate = new Date();
    }
    
    public CampaignDAO getCampaignDAO() {
        return campaignDAO;
    }

    public void setCampaignDAO(CampaignDAO campaignDAO) {
        this.campaignDAO = campaignDAO;
    }

    public MarketingDAO getMarketingDAO() {
        return marketingDAO;
    }

    public void setMarketingDAO(MarketingDAO marketingDAO) {
        this.marketingDAO = marketingDAO;
    }
    
    public Map<String, Integer> getCampaignList() {
        return this.getCampaignDAO().getCampaignList();
    }

    public Map<String, Integer> getMarketingList() {
        return this.getMarketingDAO().getMarketList();
    }   
    
    public String getCampaignName() {
        return campaignName;
    }
    
    public Integer getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
        this.campaignName = "All";
        try {
            if (campaignId!=null && campaignId!=0) {
                this.campaignName = this.getCampaignDAO().findCampaign(campaignId).getName();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
    
    public Integer getMarketingId() {
        return marketingId;
    }

    public void setMarketingId(Integer marketingId) {
        this.marketingId = marketingId;
        this.marketingName = "All";
        try {
            if (marketingId != null && marketingId != 0) {
                this.marketingName = this.getMarketingDAO().findMarketing(marketingId).getName();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
    
    public String getMarketingName() {
        return marketingName;
    }
     
    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(toDate);
        cal.set(Calendar.HOUR, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        this.toDate = cal.getTime();
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }
    
    public Integer getPeriod() {
        return period;
    }
    
    public void setGroup(Integer group) {
        this.group = group;
    }
    
    public Integer getGroup() {
        return group;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public String getExcelQuery() {
        //return getQuery();
        return getQuery(this.group);
    }

    public String getQuery() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.US);  
        String query = "SELECT "
		+ "CASE "
		+ "     WHEN [Year] IS NOT NULL AND [Month] IS NOT NULL AND [Contact Date] IS NOT NULL THEN 'EOD' "
		+ "	WHEN [Year] IS NOT NULL AND [Month] IS NOT NULL AND [Contact Date] IS NULL THEN 'EOM' "
		+ "	WHEN [Year] IS NOT NULL AND [Month] IS NULL AND [Contact Date] IS NULL THEN 'EOY' "
		+ "	WHEN [Year] IS NULL AND [Month] IS NULL AND [Contact Date] IS NULL THEN 'ALL' "
		+ "END AS [Row],"
		+ "CASE "
		+ "	WHEN [Year] IS NOT NULL AND [Month] IS NOT NULL AND [Contact Date] IS NOT NULL THEN [Contact Date] "
		+ "	WHEN [Year] IS NOT NULL AND [Month] IS NOT NULL AND [Contact Date] IS NULL THEN [Month] + ' ' + [Year] "
		+ "	WHEN [Year] IS NOT NULL AND [Month] IS NULL AND [Contact Date] IS NULL THEN 'YTD ' + [Year] "
		+ "	WHEN [Year] IS NULL AND [Month] IS NULL AND [Contact Date] IS NULL THEN 'Total' "
		+ "END AS [Contact Date], "
                + "SUM([Total Lead Use]) AS [Total Lead Use], "
		+ "SUM([New Lead]) AS [New Lead], "
                + "SUM([In-progress Lead]) AS [In-progress Lead], "
		//+ "SUM([AVG/TSR]) AS [AVG/TSR], "
                + "SUM([Total Ineffective Contact]) AS [Total Ineffective Contact], "
                + "SUM([Unreachable]) AS [Unreachable], "
		+ "SUM([Not Update]) AS [Not Update], "
		+ "SUM([First Call Sales]) AS [First Call Sales], " 
		+ "SUM([CC]) AS [CC], SUM([DC]) AS [DC], SUM([CASH]) AS [CASH], SUM([DA]) AS [DA], SUM([Line]) AS [Line], "
		+ "SUM([Net Sales]) AS [Net Sales], SUM([Yes Sales]) AS [Yes Sales], SUM([No Sales]) AS [No Sales], SUM([Total Effective Contact]) AS [Total Effective Contact], "
		+ "SUM([% Sales Conv.]) AS [% Sales Conv.], "
		+ "SUM([% No Sales]) AS [% No Sales], "
		+ "SUM([% Effective Contact]) AS [% Effective Contact], "
		+ "SUM([Duplicated]) AS [Duplicated], SUM([Non-Target]) AS [Non-Target], SUM([Total Lead Closed]) AS [Total Lead Closed], "
		+ "SUM([Total In Progress]) AS [Total In Progress], SUM([Follow Doc]) AS [Follow Doc], SUM([Follow Up]) AS [Follow Up], SUM([Call Back]) AS [Call Back], "
		+ "SUM([New List Conv.]) AS [New List Conv.], "
		+ "SUM([List Closed Conv.]) AS [List Closed Conv.], "
		+ "SUM([Total Contactable]) AS [Total Contactable], "
		+ "SUM([% Total Contactable]) AS [% Total Contactable], "
		+ "SUM([AFYP CC]) AS [AFYP CC], SUM([AFYP DC]) AS [AFYP DC], SUM([AFYP CASH]) AS [AFYP CASH], SUM([AFYP DA]) AS [AFYP DA], SUM([AFYP Line]), SUM([Total AFYP]) AS [Total AFYP], "
		+ "SUM([AARP]) AS [AARP], "
		+ "SUM([TSR]) AS [TSR], "
                + "SUM([Call Attemp]) AS [Call Attempt], "
		+ "SUM([Working Day]) AS [Working Day] "
		+ "FROM (SELECT * FROM [dbo].[udf_sales_report](" + campaignId + "," + marketingId + ",'" + sdf.format(fromDate) + "','" + sdf.format(toDate) + "')) AS sales "
		+ "GROUP BY [Year], [Month], [Contact Date] WITH ROLLUP ";
        //System.out.println("Query : " + query);
        return query;
    }
    
    public String getQuery(Integer group) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
        String query = "";
        
        if (group == 1 || group == 2) {
            query = "SELECT "
		+ "CASE "
		+ "     WHEN [Year] IS NOT NULL AND [Month] IS NOT NULL AND [Contact Date] IS NOT NULL THEN 'EOD' "
		+ "	WHEN [Year] IS NOT NULL AND [Month] IS NOT NULL AND [Contact Date] IS NULL THEN 'EOM' "
		+ "	WHEN [Year] IS NOT NULL AND [Month] IS NULL AND [Contact Date] IS NULL THEN 'EOY' "
		+ "	WHEN [Year] IS NULL AND [Month] IS NULL AND [Contact Date] IS NULL THEN 'ALL' "
		+ "END AS [Row],"
		+ "CASE "
		+ "	WHEN [Year] IS NOT NULL AND [Month] IS NOT NULL AND [Contact Date] IS NOT NULL THEN [Contact Date] "
		+ "	WHEN [Year] IS NOT NULL AND [Month] IS NOT NULL AND [Contact Date] IS NULL THEN DATENAME(MONTH, DATEADD(MONTH, [Month], -1)) + ' ' + [Year] "
		+ "	WHEN [Year] IS NOT NULL AND [Month] IS NULL AND [Contact Date] IS NULL THEN 'YTD ' + [Year] "
		+ "	WHEN [Year] IS NULL AND [Month] IS NULL AND [Contact Date] IS NULL THEN 'Total' "
		+ "END AS [Contact Date], "
                + "SUM([Total Lead Use]) AS [Total Lead Use], "
		+ "SUM([New Lead]) AS [New Lead], "
                + "SUM([In-progress Lead]) AS [In-progress Lead], "
                + "SUM([Total Ineffective Contact]) AS [Total Ineffective Contact], "
                + "SUM([Unreachable]) AS [Unreachable], "
		+ "SUM([Not Update]) AS [Not Update], "
		+ "SUM([First Call Sales]) AS [First Call Sales], " 
                + "SUM([CC]) AS [CC], SUM([DC]) AS [DC], SUM([CASH]) AS [CASH], SUM([DD]) AS [DD], SUM([DP]) AS [DP], SUM([DA]) AS [DA], SUM([LINE]) AS [LINE], "
                + "SUM([Net Sales]) AS [Net Sales], SUM([No Sales]) AS [No Sales], SUM([Total Effective Contact]) AS [Total Effective Contact], "
                + "CONVERT(decimal(20,2), CASE WHEN SUM([Net Sales]) + SUM([No Sales]) = 0 THEN 0.00 ELSE CONVERT(decimal, SUM([Net Sales])) / (SUM([Net Sales]) + SUM([No Sales])) * 100 END) AS [% Sales Conv.], "
		+ "CONVERT(decimal(20,2), CASE WHEN SUM([Net Sales]) + SUM([No Sales]) = 0 THEN 0.00 ELSE CONVERT(decimal, SUM([No Sales])) / (SUM([Net Sales]) + SUM([No Sales])) * 100 END) AS [% No Sales], "
		+ "CONVERT(decimal(20,2), CASE WHEN SUM([Total Ineffective Contact]) + SUM([Net Sales]) + SUM([No Sales]) + SUM([Duplicated]) + SUM([Non-Target]) = 0 THEN 0.00 ELSE CONVERT(decimal, SUM([Net Sales]) + SUM([No Sales])) / (SUM([Total Ineffective Contact]) + SUM([Net Sales]) + SUM([No Sales]) + SUM([Duplicated]) + SUM([Non-Target])) * 100 END) AS [% Effective Contact], "                   
		+ "SUM([Duplicated]) AS [Duplicated], SUM([Non-Target]) AS [Non-Target], SUM([Total Lead Closed]) AS [Total Lead Closed], "
		+ "SUM([Total In Progress]) AS [Total In Progress], SUM([Follow Doc]) AS [Follow Doc], SUM([Follow Up]) AS [Follow Up], SUM([Call Back]) AS [Call Back], "
		+ "CONVERT(decimal(20,2), CASE WHEN SUM([New Lead]) = 0 THEN 0.00 ELSE CONVERT(decimal, SUM([Net Sales])) / SUM([New Lead]) * 100 END) AS [New List Conv.], "
		+ "CONVERT(decimal(20,2), CASE WHEN (SUM([Total Ineffective Contact]) + SUM([Net Sales]) + SUM([No Sales]) + SUM([Duplicated]) + SUM([Non-Target])) = 0 THEN 0.00 ELSE CONVERT(decimal, SUM([Net Sales])) / (SUM([Total Ineffective Contact]) + SUM([Net Sales]) + SUM([No Sales]) + SUM([Duplicated]) + SUM([Non-Target])) * 100 END) AS [List Closed Conv.], "
		+ "SUM([Total Contactable]) AS [Total Contactable], "
                + "CONVERT(decimal(20,2), CASE WHEN (SUM([Total Ineffective Contact]) + SUM([Net Sales]) + SUM([No Sales]) + SUM([Duplicated]) + SUM([Non-Target])) = 0 THEN 0.00 ELSE CONVERT(decimal, (SUM([Net Sales]) + SUM([No Sales]) + SUM([Duplicated]) + SUM([Non-Target]))) / (SUM([Total Ineffective Contact]) + SUM([Net Sales]) + SUM([No Sales]) + SUM([Duplicated]) + SUM([Non-Target])) * 100 END) AS [% Total Contactable], "
                + "SUM([AFYP CC]) AS [AFYP CC], SUM([AFYP DC]) AS [AFYP DC], SUM([AFYP CASH]) AS [AFYP CASH], SUM([AFYP DD]) AS [AFYP DD], SUM([AFYP DP]) AS [AFYP DP], SUM([AFYP DA]) AS [AFYP DA], SUM([AFYP LINE]) AS [AFYP LINE], SUM([Total AFYP]) AS [Total AFYP], "
		+ "CONVERT(decimal(20,2), CASE WHEN SUM([Net Sales]) = 0 THEN 0.00 ELSE CONVERT(decimal, SUM([Total AFYP]) / SUM([Net Sales])) END) AS [AARP], "
                + "CASE WHEN [Year] IS NOT NULL AND [Month] IS NOT NULL AND [Contact Date] IS NOT NULL THEN CONVERT(varchar, SUM([TSR])) ELSE CONVERT(varchar, CONVERT(decimal(20,2), CONVERT(decimal, SUM([TSR])) / SUM([Working Day]))) END AS [TSR],"
                + "SUM([Call Attempt]) AS [Call Attempt], "
		+ "SUM([Working Day]) AS [Working Day], "
                + "SUM([Yes Sales]) AS [Yes Sales], "
                + "SUM([Total Yes Sales AFYP]) AS [Total Yes Sales AFYP] "
		+ "FROM [dbo].[udf_mtl_sales_rpt_by_contact_date](" + campaignId + "," + marketingId + ",'" + sdf.format(fromDate) + "','" + sdf.format(toDate) + "') "
		+ "GROUP BY [Year], [Month], [Contact Date] WITH ROLLUP";
        }else if(group == 3) {
            query = "SELECT "
		+ "CASE "
		+ "   WHEN [TSR Group] IS NULL THEN 'EOY' "
		+ "   WHEN [TSR Name] IS NULL THEN 'EOM' "
		+ "   ELSE 'EOD' "
		+ "END AS [Row], "
		+ "CASE "
		+ "   WHEN [TSR Group] IS NULL THEN 'Grand Total' "
		+ "   WHEN [TSR Name] IS NULL THEN 'Total' "
		+ "   ELSE [TSR Name] "
		+ "END AS [TSR Name], "
                + "SUM([Total Lead Use]) AS [Total Lead Use], "
		+ "SUM([New Lead]) AS [New Lead], "
                + "SUM([In-progress Lead]) AS [In-progress Lead], "
                + "SUM([Total Ineffective Contact]) AS [Total Ineffective Contact], "
                + "SUM([Unreachable]) AS [Unreachable], "
		+ "SUM([Not Update]) AS [Not Update], "
		+ "SUM([First Call Sales]) AS [First Call Sales], " 
                + "SUM([CC]) AS [CC], SUM([DC]) AS [DC], SUM([CASH]) AS [CASH], SUM([DD]) AS [DD], SUM([DP]) AS [DP], SUM([DA]) AS [DA], SUM([LINE]) AS [LINE], "
                + "SUM([Net Sales]) AS [Net Sales], SUM([No Sales]) AS [No Sales], SUM([Total Effective Contact]) AS [Total Effective Contact], "
                + "CONVERT(decimal(20,2), CASE WHEN SUM([Net Sales]) + SUM([No Sales]) = 0 THEN 0.00 ELSE CONVERT(decimal, SUM([Net Sales])) / (SUM([Net Sales]) + SUM([No Sales])) * 100 END) AS [% Sales Conv.], "
		+ "CONVERT(decimal(20,2), CASE WHEN SUM([Net Sales]) + SUM([No Sales]) = 0 THEN 0.00 ELSE CONVERT(decimal, SUM([No Sales])) / (SUM([Net Sales]) + SUM([No Sales])) * 100 END) AS [% No Sales], "
		+ "CONVERT(decimal(20,2), CASE WHEN SUM([Total Ineffective Contact]) + SUM([Net Sales]) + SUM([No Sales]) + SUM([Duplicated]) + SUM([Non-Target]) = 0 THEN 0.00 ELSE CONVERT(decimal, SUM([Net Sales]) + SUM([No Sales])) / (SUM([Total Ineffective Contact]) + SUM([Net Sales]) + SUM([No Sales]) + SUM([Duplicated]) + SUM([Non-Target])) * 100 END) AS [% Effective Contact], "                   
		+ "SUM([Duplicated]) AS [Duplicated], SUM([Non-Target]) AS [Non-Target], SUM([Total Lead Closed]) AS [Total Lead Closed], "
		+ "SUM([Total In Progress]) AS [Total In Progress], SUM([Follow Doc]) AS [Follow Doc], SUM([Follow Up]) AS [Follow Up], SUM([Call Back]) AS [Call Back], "
		+ "CONVERT(decimal(20,2), CASE WHEN SUM([New Lead]) = 0 THEN 0.00 ELSE CONVERT(decimal, SUM([Net Sales])) / SUM([New Lead]) * 100 END) AS [New List Conv.], "
		+ "CONVERT(decimal(20,2), CASE WHEN (SUM([Total Ineffective Contact]) + SUM([Net Sales]) + SUM([No Sales]) + SUM([Duplicated]) + SUM([Non-Target])) = 0 THEN 0.00 ELSE CONVERT(decimal, SUM([Net Sales])) / (SUM([Total Ineffective Contact]) + SUM([Net Sales]) + SUM([No Sales]) + SUM([Duplicated]) + SUM([Non-Target])) * 100 END) AS [List Closed Conv.], "
		+ "SUM([Total Contactable]) AS [Total Contactable], "
                + "CONVERT(decimal(20,2), CASE WHEN (SUM([Total Ineffective Contact]) + SUM([Net Sales]) + SUM([No Sales]) + SUM([Duplicated]) + SUM([Non-Target])) = 0 THEN 0.00 ELSE CONVERT(decimal, (SUM([Net Sales]) + SUM([No Sales]) + SUM([Duplicated]) + SUM([Non-Target]))) / (SUM([Total Ineffective Contact]) + SUM([Net Sales]) + SUM([No Sales]) + SUM([Duplicated]) + SUM([Non-Target])) * 100 END) AS [% Total Contactable], "
                + "SUM([AFYP CC]) AS [AFYP CC], SUM([AFYP DC]) AS [AFYP DC], SUM([AFYP CASH]) AS [AFYP CASH], SUM([AFYP DD]) AS [AFYP DD], SUM([AFYP DP]) AS [AFYP DP], SUM([AFYP DA]) AS [AFYP DA], SUM([AFYP LINE]) AS [AFYP LINE], SUM([Total AFYP]) AS [Total AFYP], "
                + "CONVERT(decimal(20,2), CASE WHEN SUM([Net Sales]) = 0 THEN 0.00 ELSE CONVERT(decimal, SUM([Total AFYP]) / SUM([Net Sales])) END) AS [AARP], "
		+ "SUM([TSR]) AS [TSR], "
                + "SUM([Call Attempt]) AS [Call Attempt], "
		+ "MAX([Working Day]) AS [Working Day], "
                + "SUM([Yes Sales]) AS [Yes Sales], "
                + "SUM([Total Yes Sales AFYP]) AS [Total Yes Sales AFYP] "
		+ "FROM [dbo].[udf_mtl_sales_rpt_by_tsr](" + campaignId + "," + marketingId + ",'" + sdf.format(fromDate) + "','" + sdf.format(toDate) + "') "
                + "WHERE [Total Lead Use] + [Total Ineffective Contact] + [Total Effective Contact] + [Yes Sales] + [Call Attempt] > 0 "
		+ "GROUP BY [TSR Group], [TSR Name] WITH ROLLUP ";
        }else{
            query = "SELECT "
		+ "CASE "
		+ "   WHEN [Marketing List] IS NULL THEN 'EOY' "
		+ "   ELSE 'EOD' "
		+ "END AS [Row], "
                + "ISNULL([Marketing List], 'Total') AS [Marketing List], "
                + "SUM([Total Lead Use]) AS [Total Lead Use], "
		+ "SUM([New Lead]) AS [New Lead], "
                + "SUM([In-progress Lead]) AS [In-progress Lead], "
                + "SUM([Total Ineffective Contact]) AS [Total Ineffective Contact], "
                + "SUM([Unreachable]) AS [Unreachable], "
		+ "SUM([Not Update]) AS [Not Update], "
		+ "SUM([First Call Sales]) AS [First Call Sales], " 
                + "SUM([CC]) AS [CC], SUM([DC]) AS [DC], SUM([CASH]) AS [CASH], SUM([DD]) AS [DD], SUM([DP]) AS [DP], SUM([DA]) AS [DA], SUM([LINE]) AS [LINE], "
		+ "SUM([Net Sales]) AS [Net Sales], SUM([No Sales]) AS [No Sales], SUM([Total Effective Contact]) AS [Total Effective Contact], "
                + "CONVERT(decimal(20,2), CASE WHEN SUM([Net Sales]) + SUM([No Sales]) = 0 THEN 0.00 ELSE CONVERT(decimal, SUM([Net Sales])) / (SUM([Net Sales]) + SUM([No Sales])) * 100 END) AS [% Sales Conv.], "
		+ "CONVERT(decimal(20,2), CASE WHEN SUM([Net Sales]) + SUM([No Sales]) = 0 THEN 0.00 ELSE CONVERT(decimal, SUM([No Sales])) / (SUM([Net Sales]) + SUM([No Sales])) * 100 END) AS [% No Sales], "
		+ "CONVERT(decimal(20,2), CASE WHEN SUM([Total Ineffective Contact]) + SUM([Net Sales]) + SUM([No Sales]) + SUM([Duplicated]) + SUM([Non-Target]) = 0 THEN 0.00 ELSE CONVERT(decimal, SUM([Net Sales]) + SUM([No Sales])) / (SUM([Total Ineffective Contact]) + SUM([Net Sales]) + SUM([No Sales]) + SUM([Duplicated]) + SUM([Non-Target])) * 100 END) AS [% Effective Contact], "                   
		+ "SUM([Duplicated]) AS [Duplicated], SUM([Non-Target]) AS [Non-Target], SUM([Total Lead Closed]) AS [Total Lead Closed], "
		+ "SUM([Total In Progress]) AS [Total In Progress], SUM([Follow Doc]) AS [Follow Doc], SUM([Follow Up]) AS [Follow Up], SUM([Call Back]) AS [Call Back], "
		+ "CONVERT(decimal(20,2), CASE WHEN SUM([New Lead]) = 0 THEN 0.00 ELSE CONVERT(decimal, SUM([Net Sales])) / SUM([New Lead]) * 100 END) AS [New List Conv.], "
		+ "CONVERT(decimal(20,2), CASE WHEN (SUM([Total Ineffective Contact]) + SUM([Net Sales]) + SUM([No Sales]) + SUM([Duplicated]) + SUM([Non-Target])) = 0 THEN 0.00 ELSE CONVERT(decimal, SUM([Net Sales])) / (SUM([Total Ineffective Contact]) + SUM([Net Sales]) + SUM([No Sales]) + SUM([Duplicated]) + SUM([Non-Target])) * 100 END) AS [List Closed Conv.], "
		+ "SUM([Total Contactable]) AS [Total Contactable], "
                + "CONVERT(decimal(20,2), CASE WHEN (SUM([Total Ineffective Contact]) + SUM([Net Sales]) + SUM([No Sales]) + SUM([Duplicated]) + SUM([Non-Target])) = 0 THEN 0.00 ELSE CONVERT(decimal, (SUM([Net Sales]) + SUM([No Sales]) + SUM([Duplicated]) + SUM([Non-Target]))) / (SUM([Total Ineffective Contact]) + SUM([Net Sales]) + SUM([No Sales]) + SUM([Duplicated]) + SUM([Non-Target])) * 100 END) AS [% Total Contactable], "
                + "SUM([AFYP CC]) AS [AFYP CC], SUM([AFYP DC]) AS [AFYP DC], SUM([AFYP CASH]) AS [AFYP CASH], SUM([AFYP DD]) AS [AFYP DD], SUM([AFYP DP]) AS [AFYP DP], SUM([AFYP DA]) AS [AFYP DA], SUM([AFYP LINE]) AS [AFYP LINE], SUM([Total AFYP]) AS [Total AFYP], "
                + "CONVERT(decimal(20,2), CASE WHEN SUM([Net Sales]) = 0 THEN 0.00 ELSE CONVERT(decimal, SUM([Total AFYP]) / SUM([Net Sales])) END) AS [AARP], "
		+ "SUM([TSR]) AS [TSR], "
                + "SUM([Call Attempt]) AS [Call Attempt], "
		+ "MAX([Working Day]) AS [Working Day], "
                + "SUM([Yes Sales]) AS [Yes Sales], "
                + "SUM([Total Yes Sales AFYP]) AS [Total Yes Sales AFYP] "
                + "FROM [dbo].[udf_mtl_sales_rpt_by_marketing](" + campaignId + "," + marketingId + ",'" + sdf.format(fromDate) + "','" + sdf.format(toDate) + "') "
		+ "WHERE [Total Lead Use] + [Total Ineffective Contact] + [Total Effective Contact] + [Yes Sales] > 0 "
		+ "GROUP BY [Marketing List] WITH ROLLUP ";
        }
        //System.out.println("Query : " + query);
        return query;
    }
    
    //private void generateXLS() {
    private void generateXLS(Integer group) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.US);
        String date = df.format(new Date());
        String fileName = "SalesReport_" + date + ".xls";
        
        FacesContext.getCurrentInstance().getExternalContext().setResponseContentType("application/vnd.ms-excel");
        FacesContext.getCurrentInstance().getExternalContext().setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Report");
            
        HSSFFont font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        
        HSSFCellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFont(font);
        titleStyle.setAlignment(CellStyle.ALIGN_LEFT);
        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        
        HSSFRow trow = sheet.createRow(1);
        
        trow.createCell(1).setCellValue("Summary Sales Report - Team"); trow.getCell(1).setCellStyle(titleStyle);
        trow = sheet.createRow(4);
        trow.createCell(1).setCellValue("Campaign : " + this.campaignName); trow.getCell(1).setCellStyle(titleStyle);
        trow = sheet.createRow(5);
        trow.createCell(1).setCellValue("Marketing: " + this.marketingName); trow.getCell(1).setCellStyle(titleStyle);
        trow = sheet.createRow(6);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",Locale.US);
        trow.createCell(1).setCellValue("Period: " + sdf.format(fromDate) + " - " + sdf.format(toDate)); trow.getCell(1).setCellStyle(titleStyle);
          
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        HSSFCellStyle cellAltStyle = workbook.createCellStyle();
        cellAltStyle.setFont(font);
        cellAltStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellAltStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellAltStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellAltStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellAltStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellAltStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        
        HSSFPalette palette = workbook.getCustomPalette(); 
        HSSFColor color = palette.findSimilarColor(255, 153, 204);
        HSSFColor colorAlt = palette.findSimilarColor(192, 192, 192);
        
        // get the palette index of that color 
        short palIndex = color.getIndex();
        short palAltIndex = colorAlt.getIndex();
        
        // code to get the style for the cell goes here
        cellStyle.setFillForegroundColor(palIndex);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellAltStyle.setFillForegroundColor(palAltIndex);
        cellAltStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); 
        
        HSSFRow hrow1 = sheet.createRow(10);
        HSSFRow hrow2 = sheet.createRow(11);
        
        int colnum = 0;
        if (group == 1 || group == 2) {
            hrow1.createCell(colnum).setCellValue("Date"); hrow1.getCell(colnum).setCellStyle(cellStyle);
        }else if (group == 3) {
            hrow1.createCell(colnum).setCellValue("TSR"); hrow1.getCell(colnum).setCellStyle(cellStyle);
        }else{
            hrow1.createCell(colnum).setCellValue("Marketing List"); hrow1.getCell(colnum).setCellStyle(cellStyle);
        }
        hrow2.createCell(colnum).setCellValue(""); hrow2.getCell(colnum++).setCellStyle(cellStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf("A11:A12"));
        hrow1.createCell(colnum).setCellValue("LEAD USE"); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue("Total Lead Use"); hrow2.getCell(colnum++).setCellStyle(cellAltStyle);
        hrow1.createCell(colnum).setCellValue(""); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue("New Lead"); hrow2.getCell(colnum++).setCellStyle(cellAltStyle);
        hrow1.createCell(colnum).setCellValue(""); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue("In-Progress Lead"); hrow2.getCell(colnum++).setCellStyle(cellAltStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf("B11:D11"));
        hrow1.createCell(colnum).setCellValue("INEFFECTIVE CONTACT"); hrow1.getCell(colnum).setCellStyle(cellStyle);
        hrow2.createCell(colnum).setCellValue("Total InEffective Contact"); hrow2.getCell(colnum++).setCellStyle(cellStyle);
        hrow1.createCell(colnum).setCellValue(""); hrow1.getCell(colnum).setCellStyle(cellStyle);
        hrow2.createCell(colnum).setCellValue("Unreachable"); hrow2.getCell(colnum++).setCellStyle(cellStyle);
        hrow1.createCell(colnum).setCellValue(""); hrow1.getCell(colnum).setCellStyle(cellStyle);
        hrow2.createCell(colnum).setCellValue("Not Update"); hrow2.getCell(colnum++).setCellStyle(cellStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf("E11:G11"));
        hrow1.createCell(colnum).setCellValue("EFFECTIVE CONTACT"); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue("First Call Sales"); hrow2.getCell(colnum++).setCellStyle(cellAltStyle);
        hrow1.createCell(colnum).setCellValue(""); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue("CC"); hrow2.getCell(colnum++).setCellStyle(cellAltStyle);
        hrow1.createCell(colnum).setCellValue(""); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue("DC"); hrow2.getCell(colnum++).setCellStyle(cellAltStyle);
        hrow1.createCell(colnum).setCellValue(""); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue("CASH"); hrow2.getCell(colnum++).setCellStyle(cellAltStyle);
        hrow1.createCell(colnum).setCellValue(""); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue("DD"); hrow2.getCell(colnum++).setCellStyle(cellAltStyle);
        hrow1.createCell(colnum).setCellValue(""); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue("DP"); hrow2.getCell(colnum++).setCellStyle(cellAltStyle);
        hrow1.createCell(colnum).setCellValue(""); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue("DA"); hrow2.getCell(colnum++).setCellStyle(cellAltStyle);
        hrow1.createCell(colnum).setCellValue(""); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue("Line"); hrow2.getCell(colnum++).setCellStyle(cellAltStyle);
        hrow1.createCell(colnum).setCellValue(""); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue("Total Net Sales"); hrow2.getCell(colnum++).setCellStyle(cellAltStyle);
        hrow1.createCell(colnum).setCellValue(""); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue("No Sales"); hrow2.getCell(colnum++).setCellStyle(cellAltStyle);
        hrow1.createCell(colnum).setCellValue(""); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue("Total Effective Contact"); hrow2.getCell(colnum++).setCellStyle(cellAltStyle);
        hrow1.createCell(colnum).setCellValue(""); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue("% Sales Conv."); hrow2.getCell(colnum++).setCellStyle(cellAltStyle);
        hrow1.createCell(colnum).setCellValue(""); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue("% No Sales"); hrow2.getCell(colnum++).setCellStyle(cellAltStyle);
        hrow1.createCell(colnum).setCellValue(""); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue("% Effective Contact"); hrow2.getCell(colnum++).setCellStyle(cellAltStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf("H11:U11"));
        hrow1.createCell(colnum).setCellValue("Duplicated"); hrow1.getCell(colnum).setCellStyle(cellStyle);
        hrow2.createCell(colnum).setCellValue(""); hrow2.getCell(colnum++).setCellStyle(cellStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf("V11:V12"));
        hrow1.createCell(colnum).setCellValue("Non-Target"); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue(""); hrow2.getCell(colnum++).setCellStyle(cellAltStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf("W11:W12"));
        hrow1.createCell(colnum).setCellValue("Total Lead Closed"); hrow1.getCell(colnum).setCellStyle(cellStyle);
        hrow2.createCell(colnum).setCellValue(""); hrow2.getCell(colnum++).setCellStyle(cellStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf("X11:X12"));
        hrow1.createCell(colnum).setCellValue("IN-PROGRESS"); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue("Total In-Progress"); hrow2.getCell(colnum++).setCellStyle(cellAltStyle);
        hrow1.createCell(colnum).setCellValue(""); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue("Follow Doc"); hrow2.getCell(colnum++).setCellStyle(cellAltStyle);
        hrow1.createCell(colnum).setCellValue(""); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue("Follow Up"); hrow2.getCell(colnum++).setCellStyle(cellAltStyle);
        hrow1.createCell(colnum).setCellValue(""); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue("Call Back"); hrow2.getCell(colnum++).setCellStyle(cellAltStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf("Y11:AB11"));
        hrow1.createCell(colnum).setCellValue("LIST CONVERSION RATE"); hrow1.getCell(colnum).setCellStyle(cellStyle);
        hrow2.createCell(colnum).setCellValue("New List Conv."); hrow2.getCell(colnum++).setCellStyle(cellStyle);
        hrow1.createCell(colnum).setCellValue(""); hrow1.getCell(colnum).setCellStyle(cellStyle);
        hrow2.createCell(colnum).setCellValue("List Closed Conv"); hrow2.getCell(colnum++).setCellStyle(cellStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf("AC11:AD11"));
        hrow1.createCell(colnum).setCellValue("CONTACTABLE"); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue("Total"); hrow2.getCell(colnum++).setCellStyle(cellAltStyle);
        hrow1.createCell(colnum).setCellValue(""); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue("%"); hrow2.getCell(colnum++).setCellStyle(cellAltStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf("AE11:AF11"));
        hrow1.createCell(colnum).setCellValue("AFYP"); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue("CC"); hrow2.getCell(colnum++).setCellStyle(cellAltStyle);
        hrow1.createCell(colnum).setCellValue(""); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue("DC"); hrow2.getCell(colnum++).setCellStyle(cellAltStyle);
        hrow1.createCell(colnum).setCellValue(""); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue("CASH"); hrow2.getCell(colnum++).setCellStyle(cellAltStyle);
        hrow1.createCell(colnum).setCellValue(""); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue("DD"); hrow2.getCell(colnum++).setCellStyle(cellAltStyle);
        hrow1.createCell(colnum).setCellValue(""); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue("DP"); hrow2.getCell(colnum++).setCellStyle(cellAltStyle);
        hrow1.createCell(colnum).setCellValue(""); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue("DA"); hrow2.getCell(colnum++).setCellStyle(cellAltStyle);
        hrow1.createCell(colnum).setCellValue(""); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue("Line"); hrow2.getCell(colnum++).setCellStyle(cellAltStyle);
        hrow1.createCell(colnum).setCellValue(""); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue("Total"); hrow2.getCell(colnum++).setCellStyle(cellAltStyle);
        hrow1.createCell(colnum).setCellValue(""); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf("AG11:AN11"));
        hrow1.createCell(colnum).setCellValue("AARP"); hrow1.getCell(colnum).setCellStyle(cellStyle);
        hrow2.createCell(colnum).setCellValue(""); hrow2.getCell(colnum++).setCellStyle(cellStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf("AO11:AO12"));
        hrow1.createCell(colnum).setCellValue("TSRs"); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue(""); hrow2.getCell(colnum++).setCellStyle(cellAltStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf("AP11:AP12"));
        hrow1.createCell(colnum).setCellValue("Call Attemp"); hrow1.getCell(colnum).setCellStyle(cellStyle);
        hrow2.createCell(colnum).setCellValue(""); hrow2.getCell(colnum++).setCellStyle(cellStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf("AQ11:AQ12"));;
        hrow1.createCell(colnum).setCellValue("Working Day"); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue(""); hrow2.getCell(colnum++).setCellStyle(cellAltStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf("AR11:AR12"));
        hrow1.createCell(colnum).setCellValue("Total Yes Sales"); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue(""); hrow2.getCell(colnum++).setCellStyle(cellAltStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf("AS11:AS12"));
        hrow1.createCell(colnum).setCellValue("Total AFYP"); hrow1.getCell(colnum).setCellStyle(cellAltStyle);
        hrow2.createCell(colnum).setCellValue(""); hrow2.getCell(colnum++).setCellStyle(cellAltStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf("AT11:AT12"));
        
        try {
            retrieveData(workbook, sheet);
            workbook.write(FacesContext.getCurrentInstance().getExternalContext().getResponseOutputStream());
        } catch (Exception e) {
            log.error(e);
        }
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void retrieveData(HSSFWorkbook workbook, HSSFSheet sheet) throws SQLException {    
        HSSFRow dataRow;
        HSSFCell dataCell;      
        HSSFCreationHelper createHelper = workbook.getCreationHelper();

        HSSFCellStyle dateRowCellStyle = workbook.createCellStyle();
        dateRowCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        dateRowCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        dateRowCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        dateRowCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        dateRowCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        dateRowCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        
        HSSFPalette palette = workbook.getCustomPalette(); 
        HSSFColor colorTotal = palette.findSimilarColor(255, 105, 180);
        short palTotalIndex = colorTotal.getIndex();
        
        HSSFCellStyle monthRowCellStyle = workbook.createCellStyle();
        HSSFFont monthFont = workbook.createFont();
        //monthFont.setColor(IndexedColors.WHITE.getIndex());
        monthFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        monthRowCellStyle.setFont(monthFont);
        monthRowCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        monthRowCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        monthRowCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        monthRowCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        monthRowCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        monthRowCellStyle.setBorderLeft(CellStyle.BORDER_THIN);        
        //monthRowCellStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        monthRowCellStyle.setFillForegroundColor(palTotalIndex);
        monthRowCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        HSSFCellStyle yearRowCellStyle = workbook.createCellStyle();
        HSSFFont yearFont = workbook.createFont();
        //yearFont.setColor(IndexedColors.WHITE.getIndex());
        yearFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        yearRowCellStyle.setFont(yearFont);
        yearRowCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        yearRowCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        yearRowCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        yearRowCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        yearRowCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        yearRowCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        //yearRowCellStyle.setFillForegroundColor(HSSFColor.BLUE_GREY.index);
        yearRowCellStyle.setFillForegroundColor(palTotalIndex);
        yearRowCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            connection = dataSource.getConnection();
            
            CallableStatement callStatement = connection.prepareCall("{call [dbo].[sp_mtl_load_contact_history]}");
            callStatement.executeUpdate();
            
            statement = connection.createStatement();
            rs = statement.executeQuery(this.getExcelQuery());
            ResultSetMetaData rsmd = rs.getMetaData();
            int numCol = rsmd.getColumnCount();
            int numStartRow = 12;
            if (rs.next()) {
                do {
                    if (rs.getString(1).equalsIgnoreCase("EOD") || rs.getString(1).equalsIgnoreCase("EOM") || rs.getString(1).equalsIgnoreCase("EOY")) {
                        if (!(rs.getString(1).equalsIgnoreCase("EOD") && this.group == 2)) {
                            dataRow = sheet.createRow(numStartRow);
                            for (int i = 2; i <= numCol; i++) {
                                dataCell = dataRow.createCell(i-2);
                                if (rs.getString(1).equalsIgnoreCase("EOD")) {
                                    dataCell.setCellStyle(dateRowCellStyle);
                                }else if (rs.getString(1).equalsIgnoreCase("EOM")) {
                                    dataCell.setCellStyle(monthRowCellStyle);
                                }else if (rs.getString(1).equalsIgnoreCase("EOY")) {
                                    dataCell.setCellStyle(yearRowCellStyle);
                                }
                                //System.out.println("rs.getString(numOfCol) : " + rs.getString(numCol) + " | " + rs.getString(numCol).contains("Summary"));
                                
                                if (i == 2) {
                                    CellUtil.setCellStyleProperty(dataCell, workbook, CellUtil.ALIGNMENT, CellStyle.ALIGN_LEFT);
                                    dataCell.setCellValue(rs.getString(i));
                                }else if (i == 20 || i == 21 || i == 22 || i == 30 || i == 31 || i == 33) {
                                    //CellUtil.setCellStyleProperty(dataCell, workbook, CellUtil.DATA_FORMAT, createHelper.createDataFormat().getFormat("###,###,##0.00%"));
                                    //dataCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                                    //dataCell.setCellValue(Float.parseFloat(rs.getString(i)));
                                    dataCell.setCellValue(rs.getString(i)+"%");
                                }else if (i == 34 || i == 35 || i == 36 || i == 37 || i == 38 || i == 39 || i == 40 || i == 41 || i == 42 || i == 47) {
                                    CellUtil.setCellStyleProperty(dataCell, workbook, CellUtil.DATA_FORMAT, createHelper.createDataFormat().getFormat("###,###,##0.00"));
                                    dataCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                                    //dataCell.setCellValue(Float.parseFloat(rs.getString(i)));
                                    dataCell.setCellValue(rs.getDouble(i));
                                }else if (i == 43 && this.group <= 2 && !rs.getString(1).equalsIgnoreCase("EOD")) {
                                    CellUtil.setCellStyleProperty(dataCell, workbook, CellUtil.DATA_FORMAT, createHelper.createDataFormat().getFormat("###,###,##0.00"));
                                    dataCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                                    //dataCell.setCellValue(Float.parseFloat(rs.getString(i)));
                                    dataCell.setCellValue(rs.getDouble(i));
                                }else{
                                    CellUtil.setCellStyleProperty(dataCell, workbook, CellUtil.DATA_FORMAT, createHelper.createDataFormat().getFormat("###,###,##0"));
                                    dataCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                                    dataCell.setCellValue(Integer.parseInt(rs.getString(i)));
                                }
                            }
                            numStartRow++;
                        }
                    }
                } while (rs.next());
                
                for (int col = 0; col < 47; col++) {
                    //workbook.getSheetAt(0).autoSizeColumn(col);
                    workbook.getSheetAt(0).setColumnWidth(col, 5200);
                }
            }
            
        } catch (Exception e) {
            log.error(e);
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }
/*
    public void configFillData(HSSFCell dataCell, ResultSet rs, ResultSetMetaData rsmd, int i) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yy", Locale.US);
        try {
            if (rsmd.getColumnTypeName(i).equalsIgnoreCase("varchar")) {
                dataCell.setCellValue(rs.getString(i));
                if (i == 8) {
                    if (rs.getString(i) == null || (rs.getString(i) != null && rs.getString(i).equalsIgnoreCase("null"))) {
                        dataCell.setCellValue("0");
                    }
                }
            } else if (rsmd.getColumnTypeName(i).equalsIgnoreCase("integer")) {
                dataCell.setCellValue("integer " + rs.getInt(i));
            } else if (rsmd.getColumnTypeName(i).equalsIgnoreCase("char")) {
                dataCell.setCellValue("char " + rs.getString(i));
            } else if (rsmd.getColumnTypeName(i).equalsIgnoreCase("float")) {
                dataCell.setCellValue("float " + rs.getFloat(i));
            } else if (rsmd.getColumnTypeName(i).equalsIgnoreCase("DATE")) {
                dataCell.setCellValue(rs.getDate(i).toString());
            } else if (rsmd.getColumnTypeName(i).equalsIgnoreCase("DATETIME")) {
                if (rs.getTimestamp(i) != null) {
                    dataCell.setCellValue(sdf1.format(new Date(rs.getTimestamp(i).getTime())));
                } else {
                    dataCell.setCellValue(" ");
                }
            } else {
                if (rs.getString(i) != null) {
                    dataCell.setCellValue(rs.getString(i));
                } else {
                    dataCell.setCellValue(" ");
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
    }
*/    
    public void reportListener(ActionEvent event) {
        generateXLS(this.group);
    }

}
