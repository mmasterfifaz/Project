package com.maxelyz.core.controller.report;

import com.maxelyz.core.model.dao.*;
import com.maxelyz.core.model.entity.FileTemplateMapping;
import com.maxelyz.core.model.entity.Marketing;
import com.maxelyz.core.model.entity.ReportLog;
import com.maxelyz.core.model.entity.UserGroup;
import com.maxelyz.core.model.entity.Users;
import com.maxelyz.utils.JSFUtil;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.apache.log4j.Logger;
import com.maxelyz.utils.SecurityUtil;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperRunManager;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;

@ManagedBean
@SessionScoped
public class MarketingListContactResult {

    private static Logger log = Logger.getLogger(MarketingListContactResult.class);
    private List<ReportLog> reportLogList;

    private SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.US);
    private String fileName = "";
    DecimalFormat df = new DecimalFormat("#,##0.00");
    DecimalFormat df1 = new DecimalFormat("#,###");

    @ManagedProperty(value = "#{dataSource}")
    private DataSource dataSource;
    @ManagedProperty(value = "#{reportLogDAO}")
    protected ReportLogDAO reportLogDAO;
    @ManagedProperty(value = "#{fileTemplateMappingDAO}")
    protected FileTemplateMappingDAO fileTemplateMappingDAO;
    @ManagedProperty(value = "#{marketingDAO}")
    protected MarketingDAO marketingDAO;
    @ManagedProperty(value = "#{userGroupDAO}")
    private UserGroupDAO userGroupDAO;
    @ManagedProperty(value = "#{usersDAO}")
    private UsersDAO usersDAO;
    @ManagedProperty(value = "#{assignmentDAO}")
    private AssignmentDAO assignmentDAO;
    @ManagedProperty(value = "#{marketingCustomerDAO}")
    private MarketingCustomerDAO marketingCustomerDAO;



    private Integer marketingId;
    private Integer userId;
    private Integer userGroupId;
    private Integer expiryYear;
    private Integer expiryMonth;
    private Integer export;
    private Integer exclude;
    private Integer noRecord;
    private boolean decorateXLSStringLine = false;
    private boolean decorateXLSEmptyWhenNull = false;
    private int[] decorateXLSStringLineColumnIdList = {};
    private Map<String, Integer> usersList;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("report:marketinglistexport:view")) {
            SecurityUtil.redirectUnauthorize();
        }
        //super.init();
        reportLogList = reportLogDAO.findReportLogEntities();
        Calendar now = Calendar.getInstance();

        if (SecurityUtil.isPermitted("report:marketinglistexport:contactresult")) {
            export = 1;
        }else if (SecurityUtil.isPermitted("report:marketinglistexport:expirydate")) {
            export = 2;
        }else if (SecurityUtil.isPermitted("report:marketinglistexport:reusedata")) {
            export = 3;
        }else if (SecurityUtil.isPermitted("report:marketinglistexport:eocfile")) {
            export = 4;
        }else if (SecurityUtil.isPermitted("report:marketinglistexport:contactresultremark")) {
            export = 5;
        }else if (SecurityUtil.isPermitted("report:marketinglistexport:eocupdate")) {
            export = 6;
        }else if (SecurityUtil.isPermitted("report:marketinglistexport:eocleadresponse")) {
            export = 7;
        }else if (SecurityUtil.isPermitted("report:marketinglistexport:eoca")) {
            export = 8;
        }
    }

    private String genField(boolean blConfidential){
        //Field List
        String str = "";
        List<FileTemplateMapping> fileTemplateMappingList = null;
        Marketing marketing = marketingDAO.findMarketing(marketingId);
        if(marketing.getFileTemplate() != null){
            fileTemplateMappingList = fileTemplateMappingDAO.findFileTemplateMappingByIdContactResult(marketing.getFileTemplate().getId());
        }
        for(FileTemplateMapping fileTemplateMapping : fileTemplateMappingList){
            Boolean blFieldConf = fileTemplateMapping.getConfidential() == null ? false : fileTemplateMapping.getConfidential();
            if (!blConfidential || !blFieldConf) {
                if (fileTemplateMapping.getCustomerField().getMappingField().equalsIgnoreCase("customer.dob")) {
                    str += (fileTemplateMapping.getCustomerField().getId() == 0 ? "''" : "ISNULL(CONVERT(VARCHAR(10)," + fileTemplateMapping.getCustomerField().getMappingField() + ", 103), '')") + " as '" + fileTemplateMapping.getFileColumnName() + "',";
                } else if ((fileTemplateMapping.getCustomerField().getMappingField().equalsIgnoreCase("customer.weight"))
                        || (fileTemplateMapping.getCustomerField().getMappingField().equalsIgnoreCase("customer.height"))) {
                    str += (fileTemplateMapping.getCustomerField().getId() == 0 ? "''" : "ISNULL(" + fileTemplateMapping.getCustomerField().getMappingField() + ", 0)") + " as '" + fileTemplateMapping.getFileColumnName() + "',";
                } else {
                    str += (fileTemplateMapping.getCustomerField().getId() == 0 ? "''" : "ISNULL(" + fileTemplateMapping.getCustomerField().getMappingField() + ", '')") + " as '" + fileTemplateMapping.getFileColumnName() + "',";
                }
            }
        }
        if(!str.equals("")){
            str = str.substring(0, str.length() - 1);
        }
        return str;
        //Field List
    }

    //@Override
    public String getQuery() {
        //String customerFlexField33 = marketingCustomerDAO.findCustomerFlexField33ByMarketing(marketingId);
        String str = "";
        if(export != null){
            if(export == 5){
                str = genField(true);
            } else {
                str = genField(false);
            }
        }
        String select = "select " + str;
        String selectExt = "";
        if(export != null){
            if(export == 1){ //contactResult
                selectExt = " , ISNULL(convert(varchar(10), cr.status_id), '0') AS [Latest Status ID], ISNULL(cr.name, '') AS [Latest Contact Status] ";
            }else if(export == 2){ //expiry date
                selectExt = ", po.expiry_date as CAR_EXPIRY_DATE";
            }else if(export == 3){ //Reuse Data
                //selectExt = ", ISNULL(po.idno,'') AS [ID No.], ISNULL(ch_last.contact_to,'') AS [Contact No.], ISNULL(CONVERT(VARCHAR,ch_last.contact_date,103) + ' ' + CONVERT(VARCHAR,ch_last.contact_date,108),'') AS [Latest Contact], ISNULL(CAST(ch.talk_time AS VARCHAR),'') AS [Talk Time], ISNULL(CAST(cr.status_id AS VARCHAR), '') as [Latest Status ID], ISNULL(cr.name, '') as [Latest Contact Status], ad.call_attempt AS [Number of Contact], mk.name AS [Lot Name], u.[code] AS [TMR Code], u.[agent_code] AS [Agent Code]";
            }else if(export == 4){ //EOC
                //selectExt = ", u.[agent_code] AS [Agent Code], ISNULL(CONVERT(VARCHAR,ch_last.contact_date,103) + ' ' + CONVERT(VARCHAR,ch_last.contact_date,108),'') AS [Call Date], CASE WHEN ISNULL(CHARINDEX('|',status_id),0)>0 THEN SUBSTRING(status_id,1,CHARINDEX('|',status_id)-1) ELSE '' END AS [Main Outcome Code], CASE WHEN ISNULL(CHARINDEX('|',status_id),0)>0 THEN SUBSTRING(status_id,CHARINDEX('|',status_id)+1,LEN(status_id)-CHARINDEX('|',status_id)) ELSE '' END AS [Sub Outcome Code], ISNULL(po.product_code,'') AS [Product Code], ISNULL(po.product_plan_code,'') AS [Plan Code], CASE po.payment_method WHEN 1 THEN 'CC' WHEN 2 THEN 'CASH' WHEN 5 THEN 'DB' ELSE '' END AS [Payment Mode], ISNULL(CONVERT(VARCHAR,po.annual_net_premium),'') AS [Premium], ad.campaign_code AS [Campaign Code], ISNULL(CONVERT(VARCHAR,po.sum_insured),'') AS [Sum Insured], CASE po.[payment_method] WHEN 1 THEN 'CC' WHEN 2 THEN 'CASH' WHEN 5 THEN 'DB' ELSE '' END AS [Payment Channel], ISNULL(po.ref_no,'') AS [ReferenceNo], ISNULL(po.name,'') AS [FirstName], ISNULL(po.surname,'') AS [LastName], CASE po.gender WHEN 'M' THEN 'Male' WHEN 'F' THEN 'Female' ELSE '' END AS [Gender], ISNULL(po.nationality,'') AS [Nationality], ISNULL(CONVERT(VARCHAR,po.dob,103),'') AS [DOB], ISNULL(CONVERT(VARCHAR,po.age),'') AS [Age], ISNULL(po.mobile_phone,'') AS [MobilePhone], ISNULL(po.home_phone,'') AS [HomePhone], ISNULL(po.office_phone,'') AS [OfficePhone], ISNULL(po.current_address_line1,'') + ' ' + ISNULL(po.current_address_line2,'') AS [AddressLine1], ISNULL(po.current_address_line3,'') + ' ' + ISNULL(po.current_address_line4,'') AS [AddressLine2], ISNULL(po.subdistrict_name,'') AS [AddressLine3], ISNULL(po.district_name,'') AS [City], ISNULL(po.province_name,'') AS [State], ISNULL(po.postal_code,'') AS [PostCode]";
            }else if(export == 5){ //contactResultRemark
//                selectExt = " , ISNULL(convert(varchar(10), cr.status_id), '0') AS [Latest Status ID], ISNULL(cr.name, '') AS [Latest Contact Status], ad1.contact_date AS [Last Contact Date], ISNULL(ad1.contact_remark,'') AS [Contact Remark] ";
                selectExt = " , ISNULL(convert(varchar(10), cr.status_id), '0') AS [Latest Status ID], ISNULL(cr.name, '') AS [Latest Contact Status]" +
                        ", ch1.contact_date AS [Last Contact Date], ISNULL(ch1.remark,'') AS [Contact Remark], ISNULL(ch1.create_by,'') AS [TSR Name]" +
                        ", dbo.fnGetUserGroupName(u.id) [TSR Group], dbo.fnGetParentUserName(u.id) [Sup Name], mkt.name [Marketing Name] ";
            }
        }

        String from = "";
        if(export != null){
            if(export == 1){ //contactResult
                from = " from customer" +
                        " inner join marketing_customer on marketing_customer.customer_id = customer.id" +
                        " left join (select max(id) as id, customer_id from assignment_detail group by customer_id) ad on ad.customer_id = customer.id" +
                        " left join assignment_detail ad1 on ad1.id = ad.id" +
                        " left join users u on u.id = ad1.user_id" +
                        " left join assignment a on a.id = ad1.assignment_id" +
                        " left join contact_result cr on cr.id = ad1.contact_result_id";
            }else if(export == 5){ //contactResultRemark
                from = " from customer" +
                        " inner join marketing_customer on marketing_customer.customer_id = customer.id" +
                        " inner join marketing mkt on marketing_customer.marketing_id = mkt.id" +
                        " left join (select max(id) as id, customer_id from assignment_detail group by customer_id) ad on ad.customer_id = customer.id" +
                        " left join assignment_detail ad1 on ad1.id = ad.id" +
                        " left join (select max(id) as id, customer_id from dbo.contact_history group by customer_id) ch on ch.customer_id = customer.id" +
                        " left join dbo.contact_history ch1 on ch1.id = ch.id" +
                        " left join users u on u.id = ad1.user_id" +
                        " left join assignment a on a.id = ad1.assignment_id" +
                        " left join contact_result cr on cr.id = ad1.contact_result_id";
            }else if(export == 2){ //expiry date
                from =  " from customer" +
                        " inner join marketing_customer on marketing_customer.customer_id = customer.id" +
                        " inner join (" +
                        " 	select " +
                        " 	MIN(CONVERT(datetime,SUBSTRING(ltrim(rtrim(por.fx2)),0,7) + '' + convert(varchar(4),convert(int,SUBSTRING(ltrim(rtrim(por.fx2)),7,4))-543),103)) as expiry_date" +
                        " 	, po.customer_id " +
                        " 	from purchase_order po" +
                        " 	inner join purchase_order_detail pod on pod.purchase_order_id = po.id" +
                        " 	inner join purchase_order_register por on por.purchase_order_detail_id = pod.id" +
                        " 	left join nosale_reason nr on (nr.id = po.nosale_reason_id and po.sale_result = 'N' and nr.export = 1)" +
                        " 	left join followupsale_reason fr on (fr.id = po.followupsale_reason_id and po.sale_result = 'F' and fr.export = 1)" +
                        " 	where por.fx2 is not null and por.fx2 <> '' and po.sale_result <> 'Y' and (fr.name is not null or nr.name is not null) " +
                        "       AND MONTH(CONVERT(datetime,SUBSTRING(ltrim(rtrim(por.fx2)),0,7) + '' + convert(varchar(4),convert(int,SUBSTRING(ltrim(rtrim(por.fx2)),7,4))-543),103)) = " + expiryMonth +
                        " 	group by po.customer_id" +

                        " ) po on po.customer_id = customer.id";
                /*from =  " from customer c" +
                        " inner join purchase_order po on po.customer_id = c.id" +
                        " inner join (" +

                        " 	select MIN(po1.id) as po_id, po1.customer_id from customer c" +
                        " 	inner join marketing_customer mc on mc.customer_id = c.id" +
                        " 	inner join (" +

                        " 		select min(purchase_order.car_expiry_date) as expiry_date, customer_id from purchase_order" +
                        " 		where purchase_order.car_expiry_date is not null" +
                        " 		group by purchase_order.customer_id" +

                        " 	) po on po.customer_id = c.id" +
                        " 	inner join purchase_order po1 on po1.customer_id = po.customer_id and po1.car_expiry_date = po.expiry_date" +
                        " 	group by po1.customer_id" +

                        " ) po2 on po2.po_id = po.id" +
                        " inner join marketing_customer mc on mc.customer_id = c.id" +
                        " inner join users u on u.id = po.create_by_id" +
                        " ";*/
            }else if(export == 3){
                selectExt = ", v.*";
                from = " FROM customer" +
                        " INNER JOIN view_marketing_list_export_reusedata v ON v.customer_id = customer.id" +
                        " LEFT JOIN users u ON u.id = v.user_id";
                /*
                from = " FROM customer c" +
                       " INNER JOIN marketing_customer mc ON mc.customer_id = c.id" +
                       " INNER JOIN marketing mk on mk.id = mc.marketing_id" +
                       " INNER JOIN (" +
                       "    SELECT ad.id, a.marketing_id, ad.customer_id, ad.user_id, ISNULL(ad.[call_attempt],0) +  ISNULL(ad.[call_attempt2],0) AS call_attempt, ad.contact_result_id, RANK() OVER (PARTITION BY a.marketing_id, ad.customer_id ORDER BY ad.id DESC) AS rank" +
                       "    FROM assignment_detail ad INNER JOIN assignment a ON a.id= ad.assignment_id INNER JOIN marketing mk ON mk.id = a.marketing_id" +
                       " ) ad ON ad.customer_id = c.id AND ad.marketing_id = mc.marketing_id AND ad.rank = 1" +
                       " LEFT JOIN users u ON u.id = ad.user_id" +
                       " LEFT JOIN (" +
                       "    SELECT po.assignment_detail_id, po.customer_id, po.sale_result, po.approval_status, po.payment_status, po.qc_status, por.idno AS idno,RANK() OVER (PARTITION BY po.assignment_detail_id, po.customer_id ORDER BY sale_result DESC, po.id DESC) AS rank" +
                       "    FROM purchase_order po" +
                       "    INNER JOIN purchase_order_detail pod ON po.id = pod.purchase_order_id" +
                       "    INNER JOIN purchase_order_register por ON pod.id = por.purchase_order_detail_id" +
                       " ) po ON po.assignment_detail_id = ad.id AND po.customer_id = ad.customer_id AND po.rank = 1" +
                       " LEFT JOIN contact_result cr on cr.id = ad.contact_result_id" +
                       " LEFT JOIN (" +
                       "    SELECT assignment_detail_id, customer_id, ISNULL(CAST(SUM(talk_time)/60.00 AS DECIMAL(10,2)),0.00) AS talk_time" +
                       "    FROM contact_history" +
                       "    INNER JOIN contact_history_assignment" +
                       "    ON contact_history_id = contact_history.id" +
                       "    GROUP BY assignment_detail_id, customer_id" +
                       " ) ch ON ch.assignment_detail_id = ad.id AND ch.customer_id = ad.customer_id" +
                       " LEFT JOIN (" +
                       "    SELECT assignment_detail_id, customer_id, contact_date, contact_to, RANK() OVER (PARTITION BY [assignment_detail_id], [customer_id] ORDER BY [contact_date] DESC) AS rank" +
                       "    FROM contact_history INNER JOIN contact_history_assignment ON contact_history_id = contact_history.id" +
                       " ) ch_last ON ch_last.assignment_detail_id = ad.id AND ch_last.customer_id = ad.customer_id AND ch_last.rank = 1";
                */
            }else if(export == 4){
                select = "SELECT * ";
                selectExt = "";
                from = " FROM view_marketing_list_export_eocfile v";
            }
        }

        String where = " WHERE 1=1";
        String groupBy = "";
        String orderBy = "";
        if (export <= 2 || export==5) {
            if (userGroupId != null && userGroupId != 0 && (export == 1 || export == 5)) {
                where += " AND u.user_group_id = " + userGroupId + " ";
            }
            if (userId != null && userId != 0 && (export == 1 || export == 5)) {
                where += " AND u.id = " + userId + " ";
            }
            if (marketingId != null && marketingId != 0) {
                where += " AND marketing_customer.marketing_id = " + marketingId + " ";
            }
            if (export == 2){
                //where += " AND YEAR(po.expiry_date) = " + expiryYear;
                where += " AND MONTH(po.expiry_date) = " + expiryMonth;
            }
            /*
            if (productId!=0)
            where += "";
            */
            orderBy = " order by customer.id";
        }else if (export == 3) {
            if (userGroupId != null && userGroupId != 0) {
                where += " AND u.user_group_id = " + userGroupId;
            }
            if (userId != null && userId != 0) {
                where += " AND u.id = " + userId;
            }
            if (exclude == 1){
                where += " AND NOT (ISNULL(v.sale_result,'')='Y' and ISNULL(v.approval_status,'')='approved' and ISNULL(v.payment_status,'')='approved' and ISNULL(v.qc_status,'')='approved')";
            }
            where += " AND v.marketing_id = " + marketingId;
            orderBy = " order by v.customer_id";
        }else if (export == 4) {
            where += " AND v.marketing_id = " + marketingId;
            orderBy = " order by v.customer_id";
        }
        String query = select + selectExt + from + where + groupBy + orderBy;
        if(export == 6 ){
            query = "{call sp_eoc_update("+ marketingId+")}";
        }
        if(export == 7 ){
            query = "{call sp_eoc_lead_response("+ marketingId+")}";
        }
        if(export == 8 ){
            query = "{call sp_eoca_data("+ marketingId+")}";
        }
        System.out.println(query);
        return query;
    }

    public void refreshTable(ActionEvent event){
        //export = 1;
        reportLogList = reportLogDAO.findReportLogEntities();
    }

    public void refreshYearMonth(){
        if(export == 2){
            Calendar now = Calendar.getInstance();
            int year = now.get(Calendar.YEAR);
            int month = now.get(Calendar.MONTH) + 1;
            expiryYear = year;
            expiryMonth = month;
        }
    }

    public List<ReportLog> getReportLogList() {
        return reportLogList;
    }

    public void setReportLogList(List<ReportLog> reportLogList) {
        this.reportLogList = reportLogList;
    }

    private void generateXLS() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        //response.setContentType("application/vnd.ms-excel");
        //response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "Attachment;filename=report.xls");
        PrintWriter out = null;
        Connection connection = null;
        Statement statement;
        ResultSet rs;
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);
        noRecord = 0;
        try {
                connection = dataSource.getConnection();
                statement = connection.createStatement();
                rs = statement.executeQuery(getQuery());
//
            if (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                int numOfCol = rsmd.getColumnCount();
                out = response.getWriter();

                out.println("<html><head><meta http-equiv=\"Content-Type\" content=\"application/vnd.ms-excel;charset=utf-8\"><style>.text {mso-number-format:'\\@';}</style></head><body>");
                out.println("<table border='1'>");
                //column name
                out.print("<tr>");
                for (int i = 1; i <= numOfCol; i++) {
                    if (rsmd.getColumnName(i).equals("EOL")) {
                        break;
                    }
                    out.print("<td><b>");
                    out.print(rsmd.getColumnName(i));
                    out.print("</b></td>");
                }
                out.println("</tr>");
                //detail
                do {
                    noRecord++;
                    out.print("<tr>");
                    for (int i = 1; i <= numOfCol; i++) {
                        if (rsmd.getColumnName(i).equals("EOL")) {
                            break;
                        }
                        out.print("<td class='text'>");
                        try {
                            if (rsmd.getColumnTypeName(i).equalsIgnoreCase("varchar")) {
                                String text = rs.getString(i);
                                if (decorateXLSEmptyWhenNull) text  = (text==null)?"":text;
                                if (isInsertDataStyleOnXLSColumn(i)) text = "<div>"+text+"</div>";
                                out.print(text);
                            } else if (rsmd.getColumnTypeName(i).equalsIgnoreCase("integer")) {
                                out.print(rs.getInt(i));
                            } else if (rsmd.getColumnTypeName(i).equalsIgnoreCase("char")) {
                                out.print(rs.getString(i));
                            } else if (rsmd.getColumnTypeName(i).equalsIgnoreCase("float")) {
                                out.print(rs.getFloat(i));
                            } else if (rsmd.getColumnTypeName(i).equalsIgnoreCase("DATE")) {
                                out.print(rs.getDate(i).toString());
                            } else if (rsmd.getColumnTypeName(i).equalsIgnoreCase("DATETIME")) {
                                out.print(rs.getTimestamp(i) != null ? sdf1.format(new Date(rs.getTimestamp(i).getTime())) : "");
                            } else {
                                out.print(rs.getString(i)!=null?rs.getString(i):"");
                            }
                        } catch (Exception e) {
                            out.print(rs.getString(i));
                        }
                        out.print("</td>");
                    }
                    out.println("</tr>");
                } while (rs.next());
                out.println("</table></body></html>");
                out.flush();
            }
        } catch (Exception e) {
            log.error(e);
        } finally {
            if (out != null) {
                out.close();
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception ex) {
                    log.error(ex);
                }
            }
        }
    }

    private boolean isInsertDataStyleOnXLSColumn(int columnId){
        if ( decorateXLSStringLine && decorateXLSStringLineColumnIdList!=null ){
            for(int i : decorateXLSStringLineColumnIdList){  if( i==columnId ) return true; }
        }
        return false;
    }

    //Listener
    public void reportExportListener(ActionEvent event) {
        if(export!=6 && export != 7 && export != 8) {
            generateXLS();
        }
        if(export==6 || export ==7 || export ==8) {
            generateXLS2();
        }
        saveLog();
        reportLogList = reportLogDAO.findReportLogEntities();
        //initialize();
    }

    //Listener
    public void exportActionListener(ValueChangeEvent event) {
        export = (Integer) event.getNewValue();
        Calendar now = Calendar.getInstance();
        //expiryYear = now.get(Calendar.YEAR);
        //expiryMonth = now.get(Calendar.MONTH) + 1;
    }

    //Listener
    public void expYearListener(ValueChangeEvent event) {
        expiryYear = (Integer) event.getNewValue();
    }

    //Listener
    public void expMonthListener(ValueChangeEvent event) {
        expiryMonth = (Integer) event.getNewValue();
    }

    private void saveLog(){
        //System.out.println("save log");
        if(export != null){
            ReportLog reportLog = new ReportLog();
            if(export == 1){
                reportLog.setName("Export Contact Result");
            }else if(export == 2){
                reportLog.setName("Export Expiry Date");
            }else if(export == 3){
                reportLog.setName("Export Reuse Data");
            }else if(export == 4){
                reportLog.setName("Export EOC File");
            }else if(export == 5){
                reportLog.setName("Export Contact Result with Remark");
            }else if(export == 6){
                reportLog.setName("Export EOC Update");
            }else if(export == 7){
                reportLog.setName("Export Lead Response EOC");
            }else if(export == 8){
                reportLog.setName("Export DATA EOCA");
            }
            if(marketingId != null & marketingId != 0){
                reportLog.setMarketing(new Marketing(marketingId));
            }
            if(export == 1 || export == 5){
                if(userGroupId != null & userGroupId != 0){
                    reportLog.setUserGroup(new UserGroup(userGroupId));
                }
                if(userId != null & userId != 0){
                    reportLog.setUsers(new Users(userId));
                }
            }
            if(export == 2){
                reportLog.setYear(expiryYear);
                reportLog.setMonth(expiryMonth);
            }
            reportLog.setNoRecord(noRecord);
            reportLog.setCreateBy(JSFUtil.getUserSession().getUsers().getLoginName());
            reportLog.setCreateDate(new Date());
            try{
                reportLogDAO.create(reportLog);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public Map<String, Integer> getExportList() {
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        if (SecurityUtil.isPermitted("report:marketinglistexport:contactresult")) {
            values.put("Export Contact Result", new Integer(1));
        }
        if (SecurityUtil.isPermitted("report:marketinglistexport:expirydate")) {
            values.put("Export Expiry Date", new Integer(2));
        }
        if (SecurityUtil.isPermitted("report:marketinglistexport:reusedata")) {
            values.put("Export Reuse Data", new Integer(3));
        }
        if (SecurityUtil.isPermitted("report:marketinglistexport:eocfile")) {
            values.put("Export EOC File", new Integer(4));
        }
        if (SecurityUtil.isPermitted("report:marketinglistexport:contactresultremark")) {
            values.put("Export Contact Result with Remark", new Integer(5));
        }
        if (SecurityUtil.isPermitted("report:marketinglistexport:eocupdate")) {
            values.put("Export EOC Update", new Integer(6));
        }
        if (SecurityUtil.isPermitted("report:marketinglistexport:eocleadresponse")) {
            values.put("Export New Response EOC", new Integer(7));
        }
        if (SecurityUtil.isPermitted("report:marketinglistexport:eoca")) {
            values.put("Export DATA EOCA", new Integer(8));
        }

        return values;
    }

    public Map<Integer, Integer> getExpiryYearList() {
        Calendar now = Calendar.getInstance(Locale.US);
        int year = now.get(Calendar.YEAR);
        Map<Integer, Integer> values = new LinkedHashMap<Integer, Integer>();
        for(int i = 0; i < 5; i++){
            values.put(new Integer(year + i), new Integer(year + i));
        }

        return values;
    }

    private void generateXLS2() {
        FileInputStream file = null;
        //Date d = new Date();
        //String start = sdf2.format(d);
        Marketing m = marketingDAO.findMarketing(marketingId);
        String campaigName = assignmentDAO.findCampaignNameByMarketingId(marketingId);
        SimpleDateFormat dt = new SimpleDateFormat("YYYY-MM-dd", Locale.US);
        String date = dt.format(new Date());
        String reportName ="";
        if(export ==6) {
            fileName = "EOC_Update_" + m.getFx7() +"_"+campaigName+"_TRB_"+ date.replace("-", "")+".xls";
        } else if(export ==7){
            fileName = "LeadResponse_EOC_" + m.getFx7() + "_"+campaigName+"_TRB_"+ date.replace("-", "")+".xls";
        }else if(export ==8){
            fileName = "DATA_EOCA_" + m.getFx7() + "_"+campaigName+"_TRB_"+ date.replace("-", "")+".xls";
        }
        try {
            if(export ==6) {
                file = new FileInputStream(new File(JSFUtil.getuploadPath(), "report" + "//EOCReportFile/Eoc_Update.xls"));
            } else if(export ==7){
                file = new FileInputStream(new File(JSFUtil.getuploadPath(), "report" + "//EOCReportFile/Eoc_Lead_Response.xls"));
            } else if(export ==8){
                file = new FileInputStream(new File(JSFUtil.getuploadPath(), "report" + "//EOCReportFile/Eoca.xls"));
            }
            HSSFWorkbook workbook = new HSSFWorkbook(file);
            HSSFSheet sheet = workbook.getSheetAt(0);

            HSSFCellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderTop(CellStyle.BORDER_THIN);
            dataStyle.setBorderRight(CellStyle.BORDER_THIN);
            dataStyle.setBorderLeft(CellStyle.BORDER_THIN);
            dataStyle.setBorderBottom(CellStyle.BORDER_THIN);
            dataStyle.setAlignment(CellStyle.ALIGN_CENTER);
            dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

            HSSFCellStyle dataStyle1 = workbook.createCellStyle();
            dataStyle1.setBorderTop(CellStyle.BORDER_NONE);
            dataStyle1.setBorderRight(CellStyle.BORDER_NONE);
            dataStyle1.setBorderLeft(CellStyle.BORDER_NONE);
            dataStyle1.setBorderBottom(CellStyle.BORDER_NONE);
            dataStyle1.setAlignment(CellStyle.ALIGN_LEFT);
            dataStyle1.setVerticalAlignment(CellStyle.ALIGN_LEFT);
//            dataStyle1.setFillBackgroundColor(CellStyle.);

            FacesContext.getCurrentInstance().getExternalContext().setResponseContentType("application/vnd.ms-excel");
            FacesContext.getCurrentInstance().getExternalContext().setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            //Fill Header
            HSSFRow row;
            HSSFCell cell;
            //
//            row = sheet.getRow(6);
//            cell = row.createCell(1);
//            cell.setCellValue("Eoc report");
//            cell.setCellStyle(dataStyle1);

            retrieveData(workbook, sheet);
            workbook.write(FacesContext.getCurrentInstance().getExternalContext().getResponseOutputStream());
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(CallCodeByCampaign.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                file.close();
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(CallCodeByCampaign.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void retrieveData(HSSFWorkbook workbook, HSSFSheet sheet) throws SQLException {
        HSSFRow dataRow;
        HSSFCell dataCell;

        HSSFFont txtFont = (HSSFFont)workbook.createFont();
        txtFont.setFontName("Arial");
        HSSFCellStyle detailCellStyle = workbook.createCellStyle();
        detailCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        detailCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        detailCellStyle.setBorderTop(CellStyle.BORDER_NONE);
        detailCellStyle.setBorderRight(CellStyle.BORDER_NONE);
        detailCellStyle.setBorderBottom(CellStyle.BORDER_NONE);
        detailCellStyle.setBorderLeft(CellStyle.BORDER_NONE);
        detailCellStyle.setFont(txtFont);

        HSSFCellStyle detailRightCellStyle = workbook.createCellStyle();
        detailRightCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        detailRightCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        detailRightCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        detailRightCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        detailRightCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        detailRightCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        detailRightCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        detailRightCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        HSSFCellStyle detailLeftCellStyle = workbook.createCellStyle();
        detailLeftCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        detailLeftCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        detailLeftCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        detailLeftCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        detailLeftCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        detailLeftCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
//        detailLeftCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//        detailLeftCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        HSSFCellStyle detailRightCallCodeCellStyle = workbook.createCellStyle();
        detailRightCallCodeCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        detailRightCallCodeCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        detailRightCallCodeCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        detailRightCallCodeCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        detailRightCallCodeCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        detailRightCallCodeCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        detailRightCallCodeCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        detailRightCallCodeCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);


        HSSFCellStyle summaryCellStyle = workbook.createCellStyle();
        summaryCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        summaryCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        summaryCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        summaryCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        summaryCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        summaryCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        summaryCellStyle.setFillForegroundColor(HSSFColor.BLUE_GREY.index);
        summaryCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        HSSFFont fontBold = workbook.createFont();
        fontBold.setBoldweight(Font.BOLDWEIGHT_BOLD);

        HSSFCellStyle detailBoldRightCellStyle = workbook.createCellStyle();
        detailBoldRightCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        detailBoldRightCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        detailBoldRightCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        detailBoldRightCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        detailBoldRightCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        detailBoldRightCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        detailBoldRightCellStyle.setFont(fontBold);

        HSSFCellStyle detailBoldLightGreyRightCellStyle = workbook.createCellStyle();
        detailBoldLightGreyRightCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        detailBoldLightGreyRightCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        detailBoldLightGreyRightCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        detailBoldLightGreyRightCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        detailBoldLightGreyRightCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        detailBoldLightGreyRightCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        detailBoldLightGreyRightCellStyle.setFont(fontBold);
        detailBoldLightGreyRightCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        detailBoldLightGreyRightCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        HSSFCellStyle detailBoldLightGreyCenterCellStyle = workbook.createCellStyle();
        detailBoldLightGreyCenterCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        detailBoldLightGreyCenterCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        detailBoldLightGreyCenterCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        detailBoldLightGreyCenterCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        detailBoldLightGreyCenterCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        detailBoldLightGreyCenterCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        detailBoldLightGreyCenterCellStyle.setFont(fontBold);
        detailBoldLightGreyCenterCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        detailBoldLightGreyCenterCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        HSSFCellStyle detailBoldCenterCellStyle = workbook.createCellStyle();
        detailBoldCenterCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        detailBoldCenterCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        detailBoldCenterCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        detailBoldCenterCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        detailBoldCenterCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        detailBoldCenterCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        detailBoldCenterCellStyle.setFont(fontBold);


        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        noRecord = 0;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();

            rs = statement.executeQuery(getQuery());
            ResultSetMetaData rsmd = rs.getMetaData();
            int numCol = rsmd.getColumnCount();

            int numStartRowFxHeader = 1;
            int cellInx = 0;

            HSSFRow row;
            HSSFCell cell;
            sheet = workbook.getSheetAt(0);
            dataRow = sheet.createRow(numStartRowFxHeader);

            if (rs.next()) {
                do {
                    noRecord++;
                    dataRow = sheet.createRow(numStartRowFxHeader);
//                    sheet.setColumnWidth(cellInx, 10000);
                    for (int i = 1; i <= numCol; i++) {
                        dataCell = dataRow.createCell(cellInx);
                        dataCell.setCellStyle(detailCellStyle);
                        String d = (rs.getString(i) != null && !rs.getString(i).isEmpty()) ? rs.getString(i) : "" ;
                        dataCell.setCellValue(d);
                        cellInx++;
                    }
                    cellInx = 0;
                    numStartRowFxHeader++;
                } while (rs.next());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
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

    public Map<String, Integer> getUsergroupList() {
        return this.getUserGroupDAO().getUserGroupList();
    }

    public Map<String, Integer> getMarketingList() {
        return this.getMarketingDAO().getMarketList();
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ReportLogDAO getReportLogDAO() {
        return reportLogDAO;
    }

    public void setReportLogDAO(ReportLogDAO reportLogDAO) {
        this.reportLogDAO = reportLogDAO;
    }

    public FileTemplateMappingDAO getFileTemplateMappingDAO() {
        return fileTemplateMappingDAO;
    }

    public void setFileTemplateMappingDAO(FileTemplateMappingDAO fileTemplateMappingDAO) {this.fileTemplateMappingDAO = fileTemplateMappingDAO;}

    public MarketingDAO getMarketingDAO() {
        return marketingDAO;
    }

    public void setMarketingDAO(MarketingDAO marketingDAO) {
        this.marketingDAO = marketingDAO;
    }

    public Integer getMarketingId() {
        return marketingId;
    }

    public void setMarketingId(Integer marketingId) {
        this.marketingId = marketingId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Integer userGroupId) {
        this.userGroupId = userGroupId;
    }

    public Integer getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(Integer expiryYear) {
        this.expiryYear = expiryYear;
    }

    public Integer getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(Integer expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public Integer getExport() {
        return export;
    }

    public void setExport(Integer export) {
        this.export = export;
    }

    public Integer getExclude() {
        return exclude;
    }

    public void setExclude(Integer exclude) {
        this.exclude = exclude;
    }

    public Integer getNoRecord() {
        return noRecord;
    }

    public void setNoRecord(Integer noRecord) {
        this.noRecord = noRecord;
    }

    public boolean isDecorateXLSStringLine() {
        return decorateXLSStringLine;
    }

    public void setDecorateXLSStringLine(boolean decorateXLSStringLine) {
        this.decorateXLSStringLine = decorateXLSStringLine;
    }

    public boolean isDecorateXLSEmptyWhenNull() {
        return decorateXLSEmptyWhenNull;
    }

    public void setDecorateXLSEmptyWhenNull(boolean decorateXLSEmptyWhenNull) {
        this.decorateXLSEmptyWhenNull = decorateXLSEmptyWhenNull;
    }

    public int[] getDecorateXLSStringLineColumnIdList() {
        return decorateXLSStringLineColumnIdList;
    }

    public void setDecorateXLSStringLineColumnIdList(int[] decorateXLSStringLineColumnIdList) {
        this.decorateXLSStringLineColumnIdList = decorateXLSStringLineColumnIdList;
    }

    public UserGroupDAO getUserGroupDAO() {
        return userGroupDAO;
    }

    public void setUserGroupDAO(UserGroupDAO userGroupDAO) {
        this.userGroupDAO = userGroupDAO;
    }

    public UsersDAO getUsersDAO() {
        return usersDAO;
    }

    public void setUsersDAO(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public Map<String, Integer> getUsersList() {
        return usersList;
    }

    public void setUsersList(Integer userGroupId) {
        if (userGroupId == 0) {
            this.usersList = this.getUsersDAO().getUsersList();
        } else {
            this.usersList = this.getUsersDAO().getUsersListByUserGroup(userGroupId);
        }
    }

    public void userGroupListener(ValueChangeEvent event) {
        userGroupId = (Integer) event.getNewValue();
        setUsersList(userGroupId);
        //FacesContext.getCurrentInstance().renderResponse();
    }

    public AssignmentDAO getAssignmentDAO() {
        return assignmentDAO;
    }

    public void setAssignmentDAO(AssignmentDAO assignmentDAO) {
        this.assignmentDAO = assignmentDAO;
    }

    public MarketingCustomerDAO getMarketingCustomerDAO() {
        return marketingCustomerDAO;
    }

    public void setMarketingCustomerDAO(MarketingCustomerDAO marketingCustomerDAO) {
        this.marketingCustomerDAO = marketingCustomerDAO;
    }
}
