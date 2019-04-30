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
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;


@ManagedBean
@RequestScoped
public class FwdSalesReport {
    private static Logger log = Logger.getLogger(FwdSalesReport.class);
    
    private Integer campaignId = 0;
    private String campaignName = "All";
    private Integer marketingId = 0;
    private String marketingName = "All";
    private Date fromDate;
    private Date toDate;
    private Integer group;
      
    @ManagedProperty(value = "#{dataSource}")
    private DataSource dataSource;
    @ManagedProperty(value = "#{campaignDAO}")
    private CampaignDAO campaignDAO;
    @ManagedProperty(value = "#{marketingDAO}")
    private MarketingDAO marketingDAO;
    
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:fwdsalesreport:view")) {
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
        String fileName = "FWD_SalesReport_" + date + ".xls";
        
        FacesContext.getCurrentInstance().getExternalContext().setResponseContentType("application/vnd.ms-excel");
        FacesContext.getCurrentInstance().getExternalContext().setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Report");
        
        HSSFFont titleFont = workbook.createFont();
        titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        titleFont.setColor(HSSFColor.BLACK.index);
        
        HSSFCellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(CellStyle.ALIGN_LEFT);
        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        
        HSSFRow trow;
        //trow = sheet.createRow(1);
        //trow.createCell(1).setCellValue("Summary Sales Report - Team"); trow.getCell(1).setCellStyle(titleStyle);
        trow = sheet.createRow(2);
        trow.createCell(0).setCellValue("Campaign"); trow.getCell(0).setCellStyle(titleStyle);
        trow.createCell(1).setCellValue(this.campaignName); trow.getCell(1).setCellStyle(titleStyle);
        trow = sheet.createRow(3);
        SimpleDateFormat sdtf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",Locale.US);
        trow.createCell(0).setCellValue("Launch Date"); trow.getCell(0).setCellStyle(titleStyle);
        trow.createCell(1).setCellValue(sdtf.format(new Date())); trow.getCell(1).setCellStyle(titleStyle);
        trow = sheet.createRow(4);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",Locale.US);
        trow.createCell(0).setCellValue("Start - End Report"); trow.getCell(0).setCellStyle(titleStyle);
        trow.createCell(1).setCellValue(sdf.format(fromDate) + " - " + sdf.format(toDate)); trow.getCell(1).setCellStyle(titleStyle);
        trow = sheet.createRow(5);
        trow.createCell(0).setCellValue("Total Working Days"); trow.getCell(0).setCellStyle(titleStyle);
        trow.createCell(1).setCellValue("0");
        
        HSSFFont font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.WHITE.index);
        
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cellStyle.setWrapText(true);
        
        HSSFCellStyle cellAltStyle = workbook.createCellStyle();
        cellAltStyle.setFont(font);
        cellAltStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellAltStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellAltStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellAltStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellAltStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellAltStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cellAltStyle.setWrapText(true);
        
        HSSFPalette palette = workbook.getCustomPalette();
        HSSFColor color = palette.findSimilarColor(22, 54, 92);
        HSSFColor colorAlt = palette.findSimilarColor(49, 134, 155);
        
        // get the palette index of that color 
        short palIndex = color.getIndex();
        short palAltIndex = colorAlt.getIndex();
        
        // code to get the style for the cell goes here
        cellStyle.setFillForegroundColor(palIndex);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellAltStyle.setFillForegroundColor(palAltIndex);
        cellAltStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); 
             
        int colnum = 0;
        int rownum = 9;
        
        HSSFRow hrow1 = sheet.createRow(rownum);
        HSSFRow hrow2 = sheet.createRow(rownum + 1);
        HSSFRow hrow3 = sheet.createRow(rownum + 2);
        
        hrow1.createCell(colnum).setCellValue("Date"); hrow1.getCell(colnum).setCellStyle(cellStyle);
        hrow2.createCell(colnum).setCellValue(""); hrow2.getCell(colnum).setCellStyle(cellStyle);
        hrow3.createCell(colnum).setCellValue(""); hrow3.getCell(colnum++).setCellStyle(cellAltStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colnum - 1), rownum + 1, CellReference.convertNumToColString(colnum - 1), rownum + 3)));
        
        hrow2.createCell(colnum).setCellValue("New"); hrow2.getCell(colnum).setCellStyle(cellStyle);
        hrow3.createCell(colnum).setCellValue(""); hrow3.getCell(colnum++).setCellStyle(cellStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colnum - 1), rownum + 2, CellReference.convertNumToColString(colnum - 1), rownum + 3)));
        
        hrow1.createCell(colnum).setCellValue(""); hrow1.getCell(colnum).setCellStyle(cellStyle);
        hrow2.createCell(colnum).setCellValue("Follow"); hrow2.getCell(colnum).setCellStyle(cellStyle);
        hrow3.createCell(colnum).setCellValue(""); hrow3.getCell(colnum++).setCellStyle(cellStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colnum - 1), rownum + 2, CellReference.convertNumToColString(colnum - 1), rownum + 3)));
        
        hrow1.createCell(colnum).setCellValue(""); hrow1.getCell(colnum).setCellStyle(cellStyle);
        hrow2.createCell(colnum).setCellValue("Total"); hrow2.getCell(colnum).setCellStyle(cellStyle);
        hrow3.createCell(colnum).setCellValue(""); hrow3.getCell(colnum++).setCellStyle(cellStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colnum - 1), rownum + 2, CellReference.convertNumToColString(colnum - 1), rownum + 3)));
        
        hrow1.createCell(colnum -3).setCellValue("List Solicited"); hrow1.getCell(colnum - 3).setCellStyle(cellStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colnum - 3), rownum + 1, CellReference.convertNumToColString(colnum - 1), rownum + 1)));
        
        hrow1.createCell(colnum).setCellValue("Call Attempt No."); hrow1.getCell(colnum).setCellStyle(cellStyle);
        hrow2.createCell(colnum).setCellValue(""); hrow2.getCell(colnum).setCellStyle(cellStyle);
        hrow3.createCell(colnum).setCellValue(""); hrow3.getCell(colnum++).setCellStyle(cellStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colnum - 1), rownum + 1, CellReference.convertNumToColString(colnum - 1), rownum + 3)));
        
        hrow1.createCell(colnum).setCellValue("Call Attempt Rate"); hrow1.getCell(colnum).setCellStyle(cellStyle);
        hrow2.createCell(colnum).setCellValue(""); hrow2.getCell(colnum).setCellStyle(cellStyle);
        hrow3.createCell(colnum).setCellValue(""); hrow3.getCell(colnum++).setCellStyle(cellStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colnum - 1), rownum + 1, CellReference.convertNumToColString(colnum - 1), rownum + 3)));
        
        hrow1.createCell(colnum).setCellValue("Accumulate Total List Used"); hrow1.getCell(colnum).setCellStyle(cellStyle);
        hrow2.createCell(colnum).setCellValue(""); hrow2.getCell(colnum).setCellStyle(cellStyle);
        hrow3.createCell(colnum).setCellValue(""); hrow3.getCell(colnum++).setCellStyle(cellStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colnum - 1), rownum + 1, CellReference.convertNumToColString(colnum - 1), rownum + 3)));
        
        hrow1.createCell(colnum).setCellValue("Daily Contactable"); hrow1.getCell(colnum).setCellStyle(cellStyle);
        hrow2.createCell(colnum).setCellValue(""); hrow2.getCell(colnum).setCellStyle(cellStyle);
        hrow3.createCell(colnum).setCellValue(""); hrow3.getCell(colnum++).setCellStyle(cellStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colnum - 1), rownum + 1, CellReference.convertNumToColString(colnum - 1), rownum + 3)));
        
        hrow1.createCell(colnum).setCellValue("Accumulate Daily Contactable"); hrow1.getCell(colnum).setCellStyle(cellStyle);
        hrow2.createCell(colnum).setCellValue(""); hrow2.getCell(colnum).setCellStyle(cellStyle);
        hrow3.createCell(colnum).setCellValue(""); hrow3.getCell(colnum++).setCellStyle(cellStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colnum - 1), rownum + 1, CellReference.convertNumToColString(colnum - 1), rownum + 3)));
        
        hrow1.createCell(colnum).setCellValue("Daily Contactable %"); hrow1.getCell(colnum).setCellStyle(cellStyle);
        hrow2.createCell(colnum).setCellValue(""); hrow2.getCell(colnum).setCellStyle(cellStyle);
        hrow3.createCell(colnum).setCellValue(""); hrow3.getCell(colnum++).setCellStyle(cellStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colnum - 1), rownum + 1, CellReference.convertNumToColString(colnum - 1), rownum + 3)));
        
        hrow1.createCell(colnum).setCellValue("Accumulate Daily Contactable %"); hrow1.getCell(colnum).setCellStyle(cellStyle);
        hrow2.createCell(colnum).setCellValue(""); hrow2.getCell(colnum).setCellStyle(cellStyle);
        hrow3.createCell(colnum).setCellValue(""); hrow3.getCell(colnum++).setCellStyle(cellStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colnum - 1), rownum + 1, CellReference.convertNumToColString(colnum - 1), rownum + 3)));
        
        hrow1.createCell(colnum).setCellValue("Apps. Response"); hrow1.getCell(colnum).setCellStyle(cellStyle);
        hrow2.createCell(colnum).setCellValue(""); hrow2.getCell(colnum).setCellStyle(cellStyle);
        hrow3.createCell(colnum).setCellValue(""); hrow3.getCell(colnum++).setCellStyle(cellStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colnum - 1), rownum + 1, CellReference.convertNumToColString(colnum - 1), rownum + 3)));
        
        hrow1.createCell(colnum).setCellValue("Sales Close Rate %"); hrow1.getCell(colnum).setCellStyle(cellStyle);
        hrow2.createCell(colnum).setCellValue(""); hrow2.getCell(colnum).setCellStyle(cellStyle);
        hrow3.createCell(colnum).setCellValue(""); hrow3.getCell(colnum++).setCellStyle(cellStyle);
        sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colnum - 1), rownum + 1, CellReference.convertNumToColString(colnum - 1), rownum + 3)));
        
        try {
            retrieveData(workbook, sheet);
            workbook.write(FacesContext.getCurrentInstance().getExternalContext().getResponseOutputStream());
        } catch (Exception e) {
            log.error(e);
        }
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void retrieveData(HSSFWorkbook workbook, HSSFSheet sheet) throws SQLException {
        //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",Locale.US);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
              
        HSSFFont font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.WHITE.index);
        
        HSSFCellStyle headCellStyle = workbook.createCellStyle();
        headCellStyle.setFont(font);
        headCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        headCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        headCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        headCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        headCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        headCellStyle.setWrapText(true);
        
        HSSFCellStyle headCellAltStyle = workbook.createCellStyle();
        headCellAltStyle.setFont(font);
        headCellAltStyle.setBorderTop(CellStyle.BORDER_THIN);
        headCellAltStyle.setBorderRight(CellStyle.BORDER_THIN);
        headCellAltStyle.setBorderLeft(CellStyle.BORDER_THIN);
        headCellAltStyle.setBorderBottom(CellStyle.BORDER_THIN);
        headCellAltStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headCellAltStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        headCellAltStyle.setWrapText(true);
        
        HSSFPalette palette = workbook.getCustomPalette(); 
        HSSFColor color = palette.findSimilarColor(22, 54, 92);
        HSSFColor colorAlt = palette.findSimilarColor(49, 134, 155);
        
        // get the palette index of that color 
        short palIndex = color.getIndex();
        short palAltIndex = colorAlt.getIndex();
        
        // code to get the style for the cell goes here
        headCellStyle.setFillForegroundColor(palIndex);
        headCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        headCellAltStyle.setFillForegroundColor(palIndex);
        headCellAltStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); 
        
        HSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        dateCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        dateCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        dateCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        dateCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        dateCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        
        HSSFCellStyle numberCellStyle = workbook.createCellStyle();
        numberCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        numberCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        numberCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        numberCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        numberCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        numberCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        
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
        
        HSSFRow dataRow;
        HSSFCell dataCell;      
        //HSSFCreationHelper createHelper = workbook.getCreationHelper();
        
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            connection = dataSource.getConnection();
            
            //CallableStatement callStatement = connection.prepareCall("{call [dbo].[sp_fwd_load_contact_history]}");
            //callStatement.executeUpdate();
            
            CallableStatement callStatement = connection.prepareCall("{call [dbo].[sp_fwd_sale_report_by_date](?,?,?,?)}");
            callStatement.setInt(1, campaignId);
            callStatement.setInt(2, marketingId);
            callStatement.setString(3, sdf.format(fromDate));
            callStatement.setString(4, sdf.format(toDate));
            rs = callStatement.executeQuery();          
            //statement = connection.createStatement();
            //rs = statement.executeQuery(this.getExcelQuery());
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();
            int startRowIndx = 12;
            int rowIndx = startRowIndx; 
            int rowCount = 0;
            
            int startColSubmitAppsIndx = 0;
            int startColSuccessAppsIndx = 0;
            int startColIssuedRate = 0;
            if (rs.next()) {                    
                do {
                    int colIndx = 0;
                    boolean isSubmitApps = false; boolean isSubmitAFYP = false;
                    boolean isSuccessApps = false; boolean isSuccessAFYP = false;
                    boolean isIssuedRate = false;
                    
                    dataRow = sheet.createRow(rowIndx);
                    for (int i = 1; i <= colCount; i++) {                       
                        if (rs.getMetaData().getColumnName(i).equalsIgnoreCase("Submit Apps")){
                            startColSubmitAppsIndx = colIndx;
                            isSubmitApps = true;
                            continue;
                        }else if (rs.getMetaData().getColumnName(i).equalsIgnoreCase("Submit AFYP")){
                            colIndx = startColSubmitAppsIndx + 1;
                            isSubmitApps = false;
                            isSubmitAFYP = true;
                            continue;
                        }else if (rs.getMetaData().getColumnName(i).equalsIgnoreCase("Total Submit Apps")){
                            colIndx = colIndx - 1;
                            isSubmitAFYP = false;                         
                        }else if (rs.getMetaData().getColumnName(i).equalsIgnoreCase("Success Apps")){
                            startColSuccessAppsIndx = colIndx;
                            isSuccessApps = true;
                            continue;
                        }else if (rs.getMetaData().getColumnName(i).equalsIgnoreCase("Success AFYP")){
                            colIndx = startColSuccessAppsIndx + 1;
                            isSuccessApps = false;
                            isSuccessAFYP = true;
                            continue;
                        }else if (rs.getMetaData().getColumnName(i).equalsIgnoreCase("Total Success Apps")){
                            colIndx = colIndx - 1;
                            isSuccessAFYP = false;
                            sheet.getRow(startRowIndx - 3).createCell(colIndx).setCellValue("Total");
                            sheet.getRow(startRowIndx - 3).getCell(colIndx).setCellStyle(headCellStyle);
                            sheet.getRow(startRowIndx - 3).createCell(colIndx + 1).setCellValue("");
                            sheet.getRow(startRowIndx - 3).getCell(colIndx + 1).setCellStyle(headCellStyle);
                            sheet.getRow(startRowIndx - 3).createCell(colIndx + 2).setCellValue("");
                            sheet.getRow(startRowIndx - 3).getCell(colIndx + 2).setCellStyle(headCellStyle);
                            
                            sheet.getRow(startRowIndx - 3).createCell(colIndx + 3).setCellValue("List Conversion %");
                            sheet.getRow(startRowIndx - 3).getCell(colIndx + 3).setCellStyle(headCellStyle);
                            sheet.getRow(startRowIndx - 2).createCell(colIndx + 3).setCellStyle(headCellStyle);
                            sheet.getRow(startRowIndx - 1).createCell(colIndx + 3).setCellStyle(headCellStyle);
                            sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colIndx + 3), startRowIndx - 2, CellReference.convertNumToColString(colIndx + 3), startRowIndx)));
                        }else if (rs.getMetaData().getColumnName(i).equalsIgnoreCase("% Issued Rate")){
                            startColIssuedRate = colIndx;
                            isIssuedRate = true;
                            continue;
                        }else if (rs.getMetaData().getColumnName(i).equalsIgnoreCase("Total Issued Rate")){
                            isIssuedRate = false;
                            
                        }else if (rs.getMetaData().getColumnName(i).equalsIgnoreCase("TSR")){
                            sheet.getRow(startRowIndx - 3).createCell(colIndx).setCellValue("TSRs");
                            sheet.getRow(startRowIndx - 3).getCell(colIndx).setCellStyle(headCellStyle);
                            //sheet.getRow(startRowIndx - 2).createCell(colIndx).setCellValue("");
                            sheet.getRow(startRowIndx - 2).createCell(colIndx).setCellStyle(headCellStyle);
                            //sheet.getRow(startRowIndx - 1).createCell(colIndx).setCellValue("");
                            sheet.getRow(startRowIndx - 1).createCell(colIndx).setCellStyle(headCellStyle);
                            sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colIndx), startRowIndx - 2, CellReference.convertNumToColString(colIndx), startRowIndx)));
                            
                            sheet.getRow(startRowIndx - 3).createCell(colIndx + 1).setCellValue("AVG Sales/TSR");
                            sheet.getRow(startRowIndx - 3).getCell(colIndx + 1).setCellStyle(headCellStyle);
                            //sheet.getRow(startRowIndx - 2).createCell(colIndx).setCellValue("");
                            sheet.getRow(startRowIndx - 2).createCell(colIndx + 1).setCellStyle(headCellStyle);
                            //sheet.getRow(startRowIndx - 1).createCell(colIndx).setCellValue("");
                            sheet.getRow(startRowIndx - 1).createCell(colIndx + 1).setCellStyle(headCellStyle);
                            sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colIndx + 1), startRowIndx - 2, CellReference.convertNumToColString(colIndx + 1), startRowIndx)));
                        }else if (rs.getMetaData().getColumnName(i).equalsIgnoreCase("Date")) {
                            continue;
                        }else{
                            
                        }
                        
                        dataCell = dataRow.createCell(colIndx);
                        dataCell.setCellStyle(numberCellStyle);
                        
                        if (isSubmitApps) {
                            // Apps Number Cell
                            sheet.getRow(rowIndx).createCell(colIndx).setCellValue(rs.getDouble(i));
                            sheet.getRow(rowIndx).getCell(colIndx).setCellStyle(numberCellStyle);
                            
                            // Set Header
                            sheet.getRow(startRowIndx - 1).createCell(colIndx).setCellValue("APP");
                            sheet.getRow(startRowIndx - 1).getCell(colIndx).setCellStyle(headCellStyle);
                            sheet.getRow(startRowIndx - 1).createCell(colIndx + 1).setCellValue("AFYP");
                            sheet.getRow(startRowIndx - 1).getCell(colIndx + 1).setCellStyle(headCellStyle);
                            sheet.getRow(startRowIndx - 1).createCell(colIndx + 2).setCellValue("AARP");
                            sheet.getRow(startRowIndx - 1).getCell(colIndx + 2).setCellStyle(headCellStyle);
                            
                            sheet.getRow(startRowIndx - 2).createCell(colIndx).setCellValue(rs.getMetaData().getColumnName(i)); // Product Name
                            sheet.getRow(startRowIndx - 2).getCell(colIndx).setCellStyle(headCellStyle);
                            //sheet.getRow(startRowIndx - 2).createCell(colIndx + 1).setCellValue("");
                            //sheet.getRow(startRowIndx - 2).createCell(colIndx + 1).setCellStyle(headCellStyle);
                            //sheet.getRow(startRowIndx - 2).createCell(colIndx + 2).setCellValue("");
                            //sheet.getRow(startRowIndx - 2).createCell(colIndx + 2).setCellStyle(headCellStyle);

                            //sheet.getRow(startRowIndx - 3).createCell(colIndx).setCellValue("");
                            sheet.getRow(startRowIndx - 3).createCell(colIndx).setCellStyle(headCellStyle);
                            //sheet.getRow(startRowIndx - 3).createCell(colIndx + 1).setCellValue("");
                            sheet.getRow(startRowIndx - 3).createCell(colIndx + 1).setCellStyle(headCellStyle);
                            //sheet.getRow(startRowIndx - 3).createCell(colIndx + 2).setCellValue("");
                            sheet.getRow(startRowIndx - 3).createCell(colIndx + 2).setCellStyle(headCellStyle);
                            
                            //System.out.println("Column Index : " + String.valueOf(colIndx));
                            //System.out.println("Merge Submit App : " + String.format("%s%d:%s%d", CellReference.convertNumToColString(colIndx), startRowIndx - 1, CellReference.convertNumToColString(colIndx + 2), startRowIndx - 1));
                            // Merge Product Name for Submit Apps Cell  
                            sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colIndx), startRowIndx - 1, CellReference.convertNumToColString(colIndx + 2), startRowIndx - 1)));
                            colIndx = colIndx + 2;
                        }else if (isSubmitAFYP) {
                            // AFYP Cell
                            sheet.getRow(rowIndx).createCell(colIndx).setCellValue(rs.getDouble(i));
                            sheet.getRow(rowIndx).getCell(colIndx).setCellStyle(numberCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(colIndx++), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                            
                            // AARP Cell
                            sheet.getRow(rowIndx).createCell(colIndx).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(rowIndx).getCell(colIndx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(colIndx - 2), rowIndx + 1, CellReference.convertNumToColString(colIndx - 1), rowIndx + 1, CellReference.convertNumToColString(colIndx - 2), rowIndx + 1));
                            sheet.getRow(rowIndx).getCell(colIndx).setCellStyle(numberCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(colIndx++), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                            //colIndx = colIndx + 2;
                        }else if (rs.getMetaData().getColumnName(i).equalsIgnoreCase("Total Submit Apps")){
                            // Set Header & Merge Submit Apps Cell
                            sheet.getRow(startRowIndx - 3).createCell(startColSubmitAppsIndx).setCellValue("Submit");
                            sheet.getRow(startRowIndx - 3).getCell(startColSubmitAppsIndx).setCellStyle(headCellStyle);
                            sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(startColSubmitAppsIndx), startRowIndx - 2, CellReference.convertNumToColString(colIndx - 1), startRowIndx - 2)));
                            
                            // Total Submit Apps
                            sheet.getRow(rowIndx).createCell(colIndx).setCellValue(rs.getDouble(i));
                            sheet.getRow(rowIndx).getCell(colIndx).setCellStyle(numberCellStyle);
                            
                            sheet.getRow(startRowIndx - 1).createCell(colIndx).setCellValue("APP");
                            sheet.getRow(startRowIndx - 1).getCell(colIndx).setCellStyle(headCellStyle);
                            sheet.getRow(startRowIndx - 1).createCell(colIndx + 1).setCellValue("AFYP");
                            sheet.getRow(startRowIndx - 1).getCell(colIndx + 1).setCellStyle(headCellStyle);
                            sheet.getRow(startRowIndx - 1).createCell(colIndx + 2).setCellValue("AARP");
                            sheet.getRow(startRowIndx - 1).getCell(colIndx + 2).setCellStyle(headCellStyle);
                            //sheet.getRow(startRowIndx - 2).createCell(colIndx).setCellValue("");
                            sheet.getRow(startRowIndx - 2).createCell(colIndx).setCellStyle(headCellStyle);
                            //sheet.getRow(startRowIndx - 2).createCell(colIndx + 1).setCellValue("");
                            sheet.getRow(startRowIndx - 2).createCell(colIndx + 1).setCellStyle(headCellStyle);
                            //sheet.getRow(startRowIndx - 2).createCell(colIndx + 2).setCellValue("");
                            sheet.getRow(startRowIndx - 2).createCell(colIndx + 2).setCellStyle(headCellStyle);
                            
                            sheet.getRow(startRowIndx - 3).createCell(colIndx).setCellValue("Total");
                            sheet.getRow(startRowIndx - 3).getCell(colIndx).setCellStyle(headCellStyle);
                            //sheet.getRow(startRowIndx - 3).createCell(colIndx + 1).setCellValue("");
                            sheet.getRow(startRowIndx - 3).createCell(colIndx + 1).setCellStyle(headCellStyle);
                            //sheet.getRow(startRowIndx - 3).createCell(colIndx + 2).setCellValue("");
                            sheet.getRow(startRowIndx - 3).createCell(colIndx + 2).setCellStyle(headCellStyle);
                            
                            sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colIndx), startRowIndx - 2, CellReference.convertNumToColString(colIndx + 2), startRowIndx - 1)));
                            
                        }else if (rs.getMetaData().getColumnName(i).equalsIgnoreCase("Total Submit AFYP")){
                            // Set Total Submit AFYP
                            sheet.getRow(rowIndx).createCell(colIndx).setCellValue(rs.getDouble(i));
                            sheet.getRow(rowIndx).getCell(colIndx).setCellStyle(numberCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(colIndx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                            
                            // Set Total Submit AARP
                            colIndx = colIndx + 1;
                            sheet.getRow(rowIndx).createCell(colIndx).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(rowIndx).getCell(colIndx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(colIndx - 2), rowIndx + 1, CellReference.convertNumToColString(colIndx - 1), rowIndx + 1, CellReference.convertNumToColString(colIndx - 2), rowIndx + 1));
                            sheet.getRow(rowIndx).getCell(colIndx).setCellStyle(numberCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(colIndx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                        }else if (isSuccessApps) {
                            sheet.getRow(rowIndx).createCell(colIndx).setCellValue(rs.getDouble(i));
                            sheet.getRow(rowIndx).getCell(colIndx).setCellStyle(numberCellStyle);
                            
                            sheet.getRow(startRowIndx - 1).createCell(colIndx).setCellValue("APP");
                            sheet.getRow(startRowIndx - 1).getCell(colIndx).setCellStyle(headCellStyle);
                            sheet.getRow(startRowIndx - 1).createCell(colIndx + 1).setCellValue("AFYP");
                            sheet.getRow(startRowIndx - 1).getCell(colIndx + 1).setCellStyle(headCellStyle);
                            sheet.getRow(startRowIndx - 1).createCell(colIndx + 2).setCellValue("AARP");
                            sheet.getRow(startRowIndx - 1).getCell(colIndx + 2).setCellStyle(headCellStyle);
                            sheet.getRow(startRowIndx - 2).createCell(colIndx).setCellValue(rs.getMetaData().getColumnName(i));
                            sheet.getRow(startRowIndx - 2).getCell(colIndx).setCellStyle(headCellStyle);
                            sheet.getRow(startRowIndx - 2).createCell(colIndx + 1).setCellValue("");
                            sheet.getRow(startRowIndx - 2).getCell(colIndx + 1).setCellStyle(headCellStyle);
                            sheet.getRow(startRowIndx - 2).createCell(colIndx + 2).setCellValue("");
                            sheet.getRow(startRowIndx - 2).getCell(colIndx + 2).setCellStyle(headCellStyle);
                            sheet.getRow(startRowIndx - 2).getCell(colIndx).setCellStyle(headCellStyle);
                            sheet.getRow(startRowIndx - 3).createCell(colIndx).setCellValue("");
                            sheet.getRow(startRowIndx - 3).getCell(colIndx).setCellStyle(headCellStyle);
                            sheet.getRow(startRowIndx - 3).createCell(colIndx + 1).setCellValue("");
                            sheet.getRow(startRowIndx - 3).getCell(colIndx + 1).setCellStyle(headCellStyle);
                            sheet.getRow(startRowIndx - 3).createCell(colIndx + 2).setCellValue("");
                            sheet.getRow(startRowIndx - 3).getCell(colIndx + 2).setCellStyle(headCellStyle);
                            sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colIndx), startRowIndx - 1, CellReference.convertNumToColString(colIndx + 2), startRowIndx - 1)));
                            colIndx = colIndx + 2;
                        }else if (isSuccessAFYP) {              
                            sheet.getRow(rowIndx).createCell(colIndx).setCellValue(rs.getDouble(i));
                            sheet.getRow(rowIndx).getCell(colIndx).setCellStyle(numberCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(colIndx++), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                            
                            sheet.getRow(rowIndx).createCell(colIndx).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(rowIndx).getCell(colIndx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(colIndx - 2), rowIndx + 1, CellReference.convertNumToColString(colIndx - 1), rowIndx + 1, CellReference.convertNumToColString(colIndx - 2), rowIndx + 1));
                            sheet.getRow(rowIndx).getCell(colIndx).setCellStyle(numberCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(colIndx++), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                            //colIndx = colIndx + 2;       
                        }else if (rs.getMetaData().getColumnName(i).equalsIgnoreCase("Total Success Apps")){
                            sheet.getRow(rowIndx).createCell(colIndx).setCellValue(rs.getDouble(i));
                            sheet.getRow(rowIndx).getCell(colIndx).setCellStyle(numberCellStyle);
                            
                            sheet.getRow(startRowIndx - 1).createCell(colIndx).setCellValue("APP");
                            sheet.getRow(startRowIndx - 1).getCell(colIndx).setCellStyle(headCellStyle);
                            sheet.getRow(startRowIndx - 2).createCell(colIndx).setCellValue("");
                            sheet.getRow(startRowIndx - 2).getCell(colIndx).setCellStyle(headCellStyle);
                            sheet.getRow(startRowIndx - 2).createCell(colIndx + 1).setCellValue("");
                            sheet.getRow(startRowIndx - 2).getCell(colIndx + 1).setCellStyle(headCellStyle);
                            sheet.getRow(startRowIndx - 2).createCell(colIndx + 2).setCellValue("");                        
                            sheet.getRow(startRowIndx - 2).getCell(colIndx + 2).setCellStyle(headCellStyle);
                            sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colIndx), startRowIndx - 2, CellReference.convertNumToColString(colIndx + 2), startRowIndx - 1)));
                            sheet.getRow(startRowIndx - 3).getCell(startColSuccessAppsIndx).setCellValue("Success");
                            sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(startColSuccessAppsIndx), startRowIndx - 2, CellReference.convertNumToColString(colIndx - 1), startRowIndx - 2)));
                        }else if (rs.getMetaData().getColumnName(i).equalsIgnoreCase("Total Success AFYP")){
                            sheet.getRow(rowIndx).createCell(colIndx).setCellValue(rs.getDouble(i));
                            sheet.getRow(rowIndx).getCell(colIndx).setCellStyle(numberCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(colIndx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                            
                            //AARP
                            sheet.getRow(rowIndx).createCell(colIndx + 1).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(rowIndx).getCell(colIndx + 1).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(colIndx - 1), rowIndx + 1, CellReference.convertNumToColString(colIndx), rowIndx + 1, CellReference.convertNumToColString(colIndx - 1), rowIndx + 1));
                            sheet.getRow(rowIndx).getCell(colIndx + 1).setCellStyle(numberCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(colIndx + 1), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                            
                            //List Conversion
                            sheet.getRow(rowIndx).createCell(colIndx + 2).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(rowIndx).getCell(colIndx + 2).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(1), rowIndx + 1, CellReference.convertNumToColString(colIndx - 1), rowIndx + 1, CellReference.convertNumToColString(1), rowIndx + 1));
                            sheet.getRow(rowIndx).getCell(colIndx + 2).setCellStyle(numberCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(colIndx + 2), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                            
                            sheet.getRow(startRowIndx - 1).createCell(colIndx).setCellValue("AFYP");
                            sheet.getRow(startRowIndx - 1).getCell(colIndx).setCellStyle(headCellStyle);
                            sheet.getRow(startRowIndx - 1).createCell(colIndx + 1).setCellValue("AARP");
                            sheet.getRow(startRowIndx - 1).getCell(colIndx + 1).setCellStyle(headCellStyle);
                            colIndx = colIndx + 2;
                        }else if (isIssuedRate){
                            sheet.getRow(rowIndx).createCell(colIndx).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(rowIndx).getCell(colIndx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(startColSubmitAppsIndx + (3 * (colIndx - startColIssuedRate)) + 1), rowIndx + 1, CellReference.convertNumToColString(startColSuccessAppsIndx + (3 * (colIndx - startColIssuedRate)) + 1), rowIndx + 1, CellReference.convertNumToColString(startColSubmitAppsIndx + (3 * (colIndx - startColIssuedRate)) + 1), rowIndx + 1));
                            sheet.getRow(rowIndx).getCell(colIndx).setCellStyle(numberCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(colIndx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                            
                            sheet.getRow(startRowIndx - 1).createCell(colIndx).setCellStyle(headCellStyle);
                            sheet.getRow(startRowIndx - 2).createCell(colIndx).setCellValue(rs.getMetaData().getColumnName(i));
                            sheet.getRow(startRowIndx - 2).getCell(colIndx).setCellStyle(headCellStyle);
                            sheet.getRow(startRowIndx - 3).createCell(colIndx).setCellStyle(headCellStyle);
                            sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colIndx), startRowIndx - 1, CellReference.convertNumToColString(colIndx), startRowIndx)));
                        }else if (rs.getMetaData().getColumnName(i).equalsIgnoreCase("Total Issued Rate")){
                            sheet.getRow(rowIndx).createCell(colIndx).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(rowIndx).getCell(colIndx).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(startColSubmitAppsIndx + (3 * (colIndx - startColIssuedRate)) + 1), rowIndx + 1, CellReference.convertNumToColString(startColSuccessAppsIndx + (3 * (colIndx - startColIssuedRate)) + 1), rowIndx + 1, CellReference.convertNumToColString(startColSubmitAppsIndx + (3 * (colIndx - startColIssuedRate)) + 1), rowIndx + 1));
                            sheet.getRow(rowIndx).getCell(colIndx).setCellStyle(numberCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(colIndx), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                            
                            //sheet.getRow(startRowIndx - 3).createCell(colIndx).setCellValue("");
                            sheet.getRow(startRowIndx - 3).createCell(colIndx).setCellStyle(headCellStyle);
                            sheet.getRow(startRowIndx - 2).createCell(colIndx).setCellValue("Total");
                            sheet.getRow(startRowIndx - 2).getCell(colIndx).setCellStyle(headCellStyle);
                            //sheet.getRow(startRowIndx - 1).createCell(colIndx).setCellValue("");
                            sheet.getRow(startRowIndx - 1).createCell(colIndx).setCellStyle(headCellStyle);
                            sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(colIndx), startRowIndx - 1, CellReference.convertNumToColString(colIndx), startRowIndx)));
                            
                            sheet.getRow(startRowIndx - 3).getCell(startColIssuedRate).setCellValue("Issued Rate %");
                            sheet.addMergedRegion(CellRangeAddress.valueOf(String.format("%s%d:%s%d", CellReference.convertNumToColString(startColIssuedRate), startRowIndx - 2, CellReference.convertNumToColString(colIndx), startRowIndx - 2)));
                        }else if (rs.getMetaData().getColumnName(i).equalsIgnoreCase("TSR")){
                            sheet.getRow(rowIndx).createCell(colIndx).setCellValue(rs.getDouble(i));
                            sheet.getRow(rowIndx).getCell(colIndx).setCellStyle(numberCellStyle);
                            
                            sheet.getRow(rowIndx).createCell(colIndx + 1).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            sheet.getRow(rowIndx).getCell(colIndx + 1).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(colIndx), rowIndx + 1, CellReference.convertNumToColString(startColIssuedRate - 4), rowIndx + 1, CellReference.convertNumToColString(colIndx), rowIndx + 1));
                            sheet.getRow(rowIndx).getCell(colIndx + 1).setCellStyle(numberCellStyle);
                            CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(colIndx + 1), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                        }else if (i == 1) { //Date Cell
                            dataCell.setCellType(HSSFCell.CELL_TYPE_STRING);
                            dataCell.setCellValue(rs.getString(i));
                            CellUtil.setCellStyleProperty(dataCell, workbook, CellUtil.ALIGNMENT, CellStyle.ALIGN_CENTER);
                        }else if (i == 7) { //Accumulate Total List Used
                            dataCell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                                               
                            if (rowIndx == startRowIndx) {
                                dataCell.setCellFormula(String.format("SUM(%s%d)", CellReference.convertNumToColString(3), startRowIndx + 1));
                            }else{
                                dataCell.setCellFormula(String.format("SUM(%s%d:%s%d)", CellReference.convertNumToColString(3), startRowIndx + 1, CellReference.convertNumToColString(3), rowIndx + 1));
                            }
                        }else if (i == 9) { //Accumulate Daily Contactable
                            dataCell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            if (rowIndx == startRowIndx) {
                                dataCell.setCellFormula(String.format("SUM(%s%d)", CellReference.convertNumToColString(7), startRowIndx + 1));
                            }else{
                                dataCell.setCellFormula(String.format("SUM(%s%d:%s%d)", CellReference.convertNumToColString(7), startRowIndx + 1, CellReference.convertNumToColString(7), rowIndx + 1));
                            }
                        }else if (i == 11) {
                            dataCell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            dataCell.setCellFormula(String.format("IF(%s%d > 0, %s%d/%s%d, 0)", CellReference.convertNumToColString(6), rowIndx + 1, CellReference.convertNumToColString(8), rowIndx + 1, CellReference.convertNumToColString(6), rowIndx + 1));
                        }else if (i == 13) {
                            dataCell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            dataCell.setCellFormula(String.format("IF(%s%d > 0, %s%d/%s%d, 0)", CellReference.convertNumToColString(7), rowIndx + 1, CellReference.convertNumToColString(11), rowIndx + 1, CellReference.convertNumToColString(7), rowIndx + 1));
                        }else if (i < 13) {
                            dataCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            dataCell.setCellValue(rs.getDouble(i));
                        }
                        //System.out.println(rs.getMetaData().getColumnName(i));
                        colIndx++;
                                                
                        //Set Cell Format 
                        if (i == 2 || i == 3 || i == 4 || i == 5 || i == 7 || i == 8 || i == 9 || i == 12) {
                            CellUtil.setCellStyleProperty(dataCell, workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                        }else if (i == 6) {
                            CellUtil.setCellStyleProperty(dataCell, workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                        }else if (i == 10 || i == 11 || i == 13) {
                            CellUtil.setCellStyleProperty(dataCell, workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                        }
                    }
                    rowIndx++;
                    rowCount++;
                } while (rs.next());
                sheet.getRow(5).getCell(1).setCellValue(String.valueOf(rowCount));
                
                sheet.createRow(rowIndx);
                for (int i = 0; i <= sheet.getRow(startRowIndx).getLastCellNum() - 1; i++) {
                    if (i == 0) {
                        sheet.getRow(rowIndx).createCell(i).setCellValue("Total");
                        sheet.getRow(rowIndx).getCell(i).setCellStyle(headCellStyle);
                    }else if (i == 5) {
                        sheet.getRow(rowIndx).createCell(i).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                        sheet.getRow(rowIndx).getCell(i).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(1), rowIndx + 1, CellReference.convertNumToColString(4), rowIndx + 1, CellReference.convertNumToColString(1), rowIndx + 1));
                        sheet.getRow(rowIndx).getCell(i).setCellStyle(headCellStyle);
                        CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(i), workbook, CellUtil.ALIGNMENT, CellStyle.ALIGN_RIGHT);
                    }else if (i == 9) {
                        sheet.getRow(rowIndx).createCell(i).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                        sheet.getRow(rowIndx).getCell(i).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(3), rowIndx + 1, CellReference.convertNumToColString(7), rowIndx + 1, CellReference.convertNumToColString(3), rowIndx + 1));
                        sheet.getRow(rowIndx).getCell(i).setCellStyle(headCellStyle);
                        CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(i), workbook, CellUtil.ALIGNMENT, CellStyle.ALIGN_RIGHT);
                    }else if (i == 10) {
                        sheet.getRow(rowIndx).createCell(i).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                        sheet.getRow(rowIndx).getCell(i).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(6), rowIndx + 1, CellReference.convertNumToColString(8), rowIndx + 1, CellReference.convertNumToColString(6), rowIndx + 1));
                        sheet.getRow(rowIndx).getCell(i).setCellStyle(headCellStyle);
                        CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(i), workbook, CellUtil.ALIGNMENT, CellStyle.ALIGN_RIGHT);
                    }else if (i == 12) {
                        sheet.getRow(rowIndx).createCell(i).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                        sheet.getRow(rowIndx).getCell(i).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(7), rowIndx + 1, CellReference.convertNumToColString(11), rowIndx + 1, CellReference.convertNumToColString(7), rowIndx + 1));
                        sheet.getRow(rowIndx).getCell(i).setCellStyle(headCellStyle);
                        CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(i), workbook, CellUtil.ALIGNMENT, CellStyle.ALIGN_RIGHT);
                    }else if (sheet.getRow(startRowIndx - 1).getCell(i).getStringCellValue().equalsIgnoreCase("AARP")) {
                        sheet.getRow(rowIndx).createCell(i).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                        sheet.getRow(rowIndx).getCell(i).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(i - 2), rowIndx + 1, CellReference.convertNumToColString(i - 1), rowIndx + 1, CellReference.convertNumToColString(i - 2), rowIndx + 1));
                        sheet.getRow(rowIndx).getCell(i).setCellStyle(headCellStyle);
                        CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(i), workbook, CellUtil.ALIGNMENT, CellStyle.ALIGN_RIGHT);
                    }else if (sheet.getRow(startRowIndx - 3).getCell(i).getStringCellValue().equalsIgnoreCase("List Conversion %")) {
                        sheet.getRow(rowIndx).createCell(i).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                        sheet.getRow(rowIndx).getCell(i).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(1), rowIndx + 1, CellReference.convertNumToColString(i - 3), rowIndx + 1, CellReference.convertNumToColString(1), rowIndx + 1));
                        sheet.getRow(rowIndx).getCell(i).setCellStyle(headCellStyle);
                        CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(i), workbook, CellUtil.ALIGNMENT, CellStyle.ALIGN_RIGHT);
                    }else if (i >= startColIssuedRate && !sheet.getRow(startRowIndx - 3).getCell(i).getStringCellValue().equalsIgnoreCase("TSRs") && !sheet.getRow(startRowIndx - 3).getCell(i).getStringCellValue().equalsIgnoreCase("AVG Sales/TSR")) {
                        sheet.getRow(rowIndx).createCell(i).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                        sheet.getRow(rowIndx).getCell(i).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(startColSubmitAppsIndx + (3 * (i - startColIssuedRate)) + 1), rowIndx + 1, CellReference.convertNumToColString(startColSuccessAppsIndx + (3 * (i - startColIssuedRate)) + 1), rowIndx + 1, CellReference.convertNumToColString(startColSubmitAppsIndx + (3 * (i - startColIssuedRate)) + 1), rowIndx + 1));
                        sheet.getRow(rowIndx).getCell(i).setCellStyle(headCellStyle);
                        CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(i), workbook, CellUtil.ALIGNMENT, CellStyle.ALIGN_RIGHT);
                    }else if (sheet.getRow(startRowIndx - 3).getCell(i).getStringCellValue().equalsIgnoreCase("AVG Sales/TSR")) {
                        sheet.getRow(rowIndx).createCell(i).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                        sheet.getRow(rowIndx).getCell(i).setCellFormula(String.format("IF(%s%d > 0,%s%d/%s%d, 0)", CellReference.convertNumToColString(i - 1), rowIndx + 1, CellReference.convertNumToColString(startColIssuedRate - 4), rowIndx + 1, CellReference.convertNumToColString(i - 1), rowIndx + 1));
                        sheet.getRow(rowIndx).getCell(i).setCellStyle(headCellStyle);
                        CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(i), workbook, CellUtil.ALIGNMENT, CellStyle.ALIGN_RIGHT);
                    }else{
                        sheet.getRow(rowIndx).createCell(i).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                        sheet.getRow(rowIndx).getCell(i).setCellFormula(String.format("SUM(%s%d:%s%d)", CellReference.convertNumToColString(i), startRowIndx + 1, CellReference.convertNumToColString(i), rowIndx));
                        sheet.getRow(rowIndx).getCell(i).setCellStyle(headCellStyle);
                        CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(i), workbook, CellUtil.ALIGNMENT, CellStyle.ALIGN_RIGHT);
                    }
                    
                    if (sheet.getRow(rowIndx - 1).getCell(i).getCellStyle().getDataFormatString().equalsIgnoreCase("#,##0")) {
                        CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(i), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0"));
                    }else if (sheet.getRow(rowIndx - 1).getCell(i).getCellStyle().getDataFormatString().equalsIgnoreCase("#,##0.00")) {
                        CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(i), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                    }else if (sheet.getRow(rowIndx - 1).getCell(i).getCellStyle().getDataFormatString().equalsIgnoreCase("0.00%")){
                        CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(i), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
                    }
                    
                    //System.out.println(sheet.getRow(rowIndx-1).getCell(i).getCellStyle().getDataFormatString());
                    //System.out.println(sheet.getRow(startRowIndx-3).getCell(i).getStringCellValue());
                    //Set Percentage Cell Format 
                    //if (i == 5 || i == 9 || i == 10 || i == 12) {
                    //    CellUtil.setCellStyleProperty(sheet.getRow(rowIndx).getCell(i), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%")); 
                    //}
                }
                
                sheet.createRow(rowIndx + 1);
                for (int i = 0; i <= sheet.getRow(startRowIndx).getLastCellNum() - 1; i++) {
                    HSSFFont font2 = workbook.createFont();
                    font2.setBoldweight(Font.BOLDWEIGHT_BOLD);
        
                    HSSFCellStyle summaryStyle = workbook.createCellStyle();
                    summaryStyle.setFont(font2);
                    summaryStyle.setAlignment(CellStyle.ALIGN_RIGHT);
                    summaryStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
                    summaryStyle.setBorderTop(CellStyle.BORDER_THIN);
                    summaryStyle.setBorderRight(CellStyle.BORDER_THIN);
                    summaryStyle.setBorderBottom(CellStyle.BORDER_THIN);
                    summaryStyle.setBorderLeft(CellStyle.BORDER_THIN);
                    if (i == 0) {
                        sheet.getRow(rowIndx + 1).createCell(i).setCellValue("Avg./Day");
                        sheet.getRow(rowIndx + 1).getCell(i).setCellStyle(summaryStyle);
                        CellUtil.setCellStyleProperty(sheet.getRow(rowIndx + 1).getCell(i), workbook, CellUtil.ALIGNMENT, CellStyle.ALIGN_CENTER);
                    }else{
                        sheet.getRow(rowIndx + 1).createCell(i).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                        sheet.getRow(rowIndx + 1).getCell(i).setCellFormula(String.format("AVERAGE(%s%d:%s%d)", CellReference.convertNumToColString(i), startRowIndx + 1, CellReference.convertNumToColString(i), rowIndx));
                        sheet.getRow(rowIndx + 1).getCell(i).setCellStyle(summaryStyle);
                        CellUtil.setCellStyleProperty(sheet.getRow(rowIndx + 1).getCell(i), workbook, CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("#,##0.00"));
                    }  
                }
                
                workbook.getSheetAt(0).setColumnWidth(0, 6000);
                for (int col = 1; col <= sheet.getRow(startRowIndx).getLastCellNum(); col++) {
                    //workbook.getSheetAt(0).autoSizeColumn(col);
                    workbook.getSheetAt(0).setColumnWidth(col, 4000);
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

    public void reportListener(ActionEvent event) {
        generateXLS(this.group);
    }

}
