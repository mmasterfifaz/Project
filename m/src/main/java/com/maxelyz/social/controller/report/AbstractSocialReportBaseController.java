package com.maxelyz.social.controller.report;


import com.maxelyz.core.controller.report.*;
import com.maxelyz.core.model.dao.*;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.core.service.UserService;
import com.maxelyz.social.model.dao.*;
import com.maxelyz.social.model.entity.*;
import com.maxelyz.utils.JSFUtil;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperRunManager;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public abstract class AbstractSocialReportBaseController {

    
    private static Logger log = Logger.getLogger(AbstractSocialReportBaseController.class);
    protected static final int PDF = 1;
    protected static final int EXCEL = 2;
    protected static final int EXCEL_TEMPLATE = 3;
    protected static final int PDF_TEMPLATE = 4;
    protected static final String CASE_PENDING = "pendingValue";
    protected static final String CASE_CLOSED = "closedValue";
    protected Boolean useResultSet = true;
    protected Date fromDate;
    protected Date toDate;
    protected Integer userGroupId;
    protected String groupName = "All";
    protected Integer userId;
    protected String userName = "All";
    protected Integer outputFormat;
    
    private Map<String, Integer> usersList;
    @ManagedProperty(value = "#{dataSource}")
    private DataSource dataSource;
    @ManagedProperty(value = "#{userGroupDAO}")
    private UserGroupDAO userGroupDAO;
    @ManagedProperty(value = "#{usersDAO}")
    private UsersDAO usersDAO;
       
    // Social
    protected Integer userOption = 0;
    protected List<SoAccount> accountList = new ArrayList<SoAccount>();
    protected List<SoChannel> channelList = new ArrayList<SoChannel>();
    protected List<CustomerType> customerTypeList = new ArrayList<CustomerType>();
    protected List<SoPriority> soPriorityList; 
    protected List<SoMsgCasetypeMap> soMsgCasetypeMapList;
    protected String soMessageId;
    protected String sourceTypeName = "All";
    protected String soPriorityName = "All";
    protected String soServiceName = "All";
    protected String soCaseStatus;
    protected Integer accountID = 0;
    protected Integer sourceTypeID = 0;
    protected Integer soServiceId;
    protected Integer customerTypeId = 0;
    protected Integer soPriorityId;
    private SoMessage soMessage;
    private Map<String, Integer> soCaseTypeList;
    protected Integer soCaseTypeId;
    private Map<String, Integer> soSubCaseTypeList;
    protected Integer soSubCaseTypeId;
    private Map<String, String> soMessageList;
    private List<SoService> soServiceList; 
    protected List<LogoffType> logoffTypeList = new ArrayList<LogoffType>();
    protected Integer logoffID = 0;
    protected List<Users> supervisorList = new ArrayList<Users>();
    protected Integer supId = 0;
    protected List<SoIgnoreReason> ignoreReasonList = new ArrayList<SoIgnoreReason>();
    protected Integer ignoreReasonId = 0;
    protected Integer fromYear;
    
    @ManagedProperty(value="#{soPriorityDAO}")
    private SoPriorityDAO soPriorityDAO;
    @ManagedProperty(value="#{soServiceDAO}")
    private SoServiceDAO soServiceDAO;
    @ManagedProperty(value="#{soAccountDAO}")
    private SoAccountDAO soAccountDAO; 
    @ManagedProperty(value="#{soChannelDAO}")
    private SoChannelDAO soChannelDAO;   
    @ManagedProperty(value = "#{soMsgCasetypeMapDAO}")
    private SoMsgCasetypeMapDAO soMsgCasetypeMapDAO;
    @ManagedProperty(value = "#{soCaseTypeDAO}")
    private SoCaseTypeDAO soCaseTypeDAO;
    @ManagedProperty(value="#{customerTypeDAO}")
    private CustomerTypeDAO customerTypeDAO;
    @ManagedProperty(value = "#{logoffTypeDAO}")
    private LogoffTypeDAO logoffTypeDAO;
    @ManagedProperty(value = "#{soIgnoreReasonDAO}")
    private SoIgnoreReasonDAO soIgnoreReasonDAO;
    
    protected void init() {
        fromDate = new Date();
        toDate = new Date();
        outputFormat = EXCEL_TEMPLATE;        
    }

    //abstract method
    protected abstract String getReportPath();
    
    protected abstract String getXLSTemplatePath();

    protected abstract Map getParameterMap();

    protected abstract String getQuery();
    
    protected abstract void generateXLSFromTemplate();

    
    public ArrayList getResultArray(){
        
        PrintWriter out = null;
        Connection connection = null;
        Statement statement;
        ResultSet rs;
        ArrayList resultList = new ArrayList(); 
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(this.getExcelQuery());
            if (!rs.next()) {
            return resultList;
            }
            
            ResultSetMetaData rsmd = rs.getMetaData();
            int numOfCol = rsmd.getColumnCount();
           
            do {
                Object[] result = new Object[numOfCol+1];
                for (int i = 1; i <= numOfCol; i++) {
                    result[i-1] = rs.getObject(i); 
                }
                resultList.add(result);
            } while (rs.next());
            return resultList;
        } catch (Exception e) {
            log.error(e);
            e.printStackTrace(); 
        } finally {
            if (out != null) {
                out.close();
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception ex) {
                    log.error(ex);
                    ex.printStackTrace();
                }
            }
        }
        return resultList;
    }
    
    private void generateXLS() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        //response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "Attachment;filename=report.xls");
        PrintWriter out = null;
        Connection connection = null;
        Statement statement;
        ResultSet rs;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(this.getExcelQuery());
            if (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                int numOfCol = rsmd.getColumnCount();
                out = response.getWriter();

                out.println("<html><head><meta http-equiv=\"Content-Type\" content=\"application/vnd.ms-excel;charset=utf-8\"></head><body>");
                out.println("<table border='1'>");
                //column name
                out.print("<tr>");
                for (int i = 1; i <= numOfCol; i++) {
                    out.print("<td><b>");
                    out.print(rsmd.getColumnName(i));
                    out.print("</b></td>");
                }
                out.println("</tr>");
                //detail
                do {
                    out.print("<tr>");
                    for (int i = 1; i <= numOfCol; i++) {
                        out.print("<td>");
                        try {
                            if (rsmd.getColumnTypeName(i).equalsIgnoreCase("varchar")) {
                                out.print(rs.getString(i));
                            } else if (rsmd.getColumnTypeName(i).equalsIgnoreCase("integer")) {
                                out.print(rs.getInt(i));
                            } else if (rsmd.getColumnTypeName(i).equalsIgnoreCase("char")) {
                                out.print(rs.getString(i));
                            } else if (rsmd.getColumnTypeName(i).equalsIgnoreCase("float")) {
                                out.print(rs.getFloat(i));
                            } else if (rsmd.getColumnTypeName(i).equalsIgnoreCase("DATE")) {
                                out.print(rs.getDate(i).toString());
                            } else if (rsmd.getColumnTypeName(i).equalsIgnoreCase("DATETIME")) {
                                out.print(rs.getDate(i).toString());
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

    private void generatePDF() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        InputStream reportStream = context.getExternalContext().getResourceAsStream(getReportPath());
        
        ServletOutputStream servletOutputStream = null;
        Connection connection = null;
        Statement statement;
        ResultSet resultSet;
        try {
            connection = dataSource.getConnection();

            response.setContentType("application/pdf");
            servletOutputStream = response.getOutputStream();
            if (useResultSet) {

                statement = connection.createStatement();
                resultSet = statement.executeQuery(this.getQuery());
                JRResultSetDataSource resultSetDataSource = new JRResultSetDataSource(resultSet);
                JasperRunManager.runReportToPdfStream(reportStream, servletOutputStream, this.getParameterMap(), resultSetDataSource);
            } else {
                JasperRunManager.runReportToPdfStream(reportStream, servletOutputStream, this.getParameterMap(), connection);
            }

            servletOutputStream.flush();
        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
        } finally {
            if (servletOutputStream != null) {
                try {
                    servletOutputStream.close();
                } catch (Exception e) {
                }
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
    
    public void setUsersList(Integer userGroupId) {
        if (userGroupId == 0) {
            this.usersList = this.getUsersDAO().getUsersList();
        } else {
            this.usersList = this.getUsersDAO().getUsersListByUserGroup(userGroupId);
        }
    }
//    
     public void setUsersServiceList(Integer serviceId) {
        if (serviceId == 0) {
            this.usersList = this.getUsersDAO().getUsersList();
        } else {
            this.usersList = this.getSoServiceDAO().getUsersListByService(serviceId);
        }
    }

    public Map<String, Integer> getUsersList() {
        if (usersList == null) {
            setUsersList(0);
        }
        return usersList;
    }
    
    public Map<String, Integer> getSoCaseTypeList() {
        List<SoCaseType> soCaseTypes = soCaseTypeDAO.findSoCaseTypeStatus();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (SoCaseType obj : soCaseTypes) {
            values.put(obj.getName(), obj.getId());
        }
        return values;   
    }
    
    public void soCaseTypeListener(ValueChangeEvent event){
        soCaseTypeId = (Integer) event.getNewValue();
        List<SoCaseType> soSubCaseTypes = soCaseTypeDAO.findSoCaseTopicListById(soCaseTypeId);
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (SoCaseType obj : soSubCaseTypes) {
            values.put(obj.getName(), obj.getId());
        }
        setSoSubCaseTypeList(values);
    }
    
    public Map<String, Integer> getSoSubCaseTypeList() {
        return soSubCaseTypeList;
    }
    
    public void setSoSubCaseTypeList(Map<String, Integer> soSubCaseTypeList) {
       this.soSubCaseTypeList = soSubCaseTypeList;
    }

    public void reportListener(ActionEvent event) {
        if (outputFormat == EXCEL) {
            generateXLS();
        } else if (outputFormat == PDF) {
            generatePDF();
        } else if (outputFormat == EXCEL_TEMPLATE) {
            generateXLSFromTemplate();
        } else if (outputFormat == PDF_TEMPLATE) {
            generateXLSFromTemplate();
        } 
        FacesContext.getCurrentInstance().responseComplete();
    }
  
    public void userGroupListener(ValueChangeEvent event) {
        userGroupId = (Integer) event.getNewValue();
        setUsersList(userGroupId);
        soServiceId = 0;
        //FacesContext.getCurrentInstance().renderResponse();
    }
    
    public void userOptionListener(ValueChangeEvent event) {
        userOption = (Integer) event.getNewValue();
    }
    
    public void userServiceListener(ValueChangeEvent event) {
        soServiceId = (Integer) event.getNewValue();
        setUsersServiceList(soServiceId);
        userGroupId = 0;
    }
   
    public String getExcelQuery() {
        return getQuery();
    }

    public Map<String, Integer> getUsergroupList() {
        return this.getUserGroupDAO().getUserGroupList();
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

    public Integer getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Integer userGroupId) {
        this.userGroupId = userGroupId;
        this.groupName = "All";
        try {
            if (userGroupId!=null && userGroupId!=0) {
                groupName = this.getUserGroupDAO().findUserGroup(userGroupId).getName();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public Map<String, Integer> getOutputFormatList() {
        Map<String, Integer> outputFormatList = new LinkedHashMap<String, Integer>();
        //outputFormatList.put("PDF", PDF);
       // outputFormatList.put("Excel", EXCEL);
        outputFormatList.put("Excel Template", EXCEL_TEMPLATE);
        outputFormatList.put("PDF Template", PDF_TEMPLATE);
        return outputFormatList;
    }
    
    public Map<String, Integer> getUserOptionList() {
        Map<String, Integer> userOptionList = new LinkedHashMap<String, Integer>();
        //outputFormatList.put("PDF", PDF);
       // outputFormatList.put("Excel", EXCEL);
        userOptionList.put("User Group", 0);
        userOptionList.put("Service", 1);
        return userOptionList;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
        this.userName = "All";

        Users u = this.getUsersDAO().findUsers(userId);
        if (u != null) {
            userName = u.getName() + " " + u.getSurname();
        }
    }

    public Integer getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(Integer outputFormat) {
        this.outputFormat = outputFormat;
    }
    
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    //Managed Properties
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

    public SoPriorityDAO getSoPriorityDAO() {
    return soPriorityDAO;
    }

    public void setSoPriorityDAO(SoPriorityDAO soPriorityDAO) {
        this.soPriorityDAO = soPriorityDAO;
    }
    
   public Integer getSoPriorityId() {
        return soPriorityId;
    }

    public void setSoPriorityId(Integer soPriorityId) {
        this.soPriorityId = soPriorityId;
        this.soPriorityName = "All";
        try {
            if (soPriorityId!=null && soPriorityId!=0) {
                this.soPriorityName = this.getSoPriorityDAO().findSoPriority(soPriorityId).getName();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public Map<String, String> getSoPriorityList() {
        Map<String, String> status = new LinkedHashMap<String, String>();
        status.put("Low","PRIORITY_LOW" );
        status.put("Medium","PRIORITY_MED" );
        status.put("High","PRIORITY_HI" );
        status.put("Immediately","PRIORITY_IMM" );
        return status;
    }
    
    public void setSoPriorityList(List<SoPriority> soPriorityList) {
        this.soPriorityList = soPriorityList;
    }

    
    public Map<String, String> getSoCaseStatusList() {
        Map<String, String> status = new LinkedHashMap<String, String>();
        status.put("Queued","NW" );
        status.put("Assigned","AS" );
        status.put("Opened","PS" );
        status.put("Request For Approval" ,"WF" );
        status.put("Followed","FL" );
        status.put("Ignored","IG" );
        status.put("Closed","CL" );
        return status;
    }
    
    public String getSoSoruceTypeNameByID(int sourceTypeID){
        switch (sourceTypeID) {
        case 1:     
                        sourceTypeName = "FB";
 
                        break;
            case 2:     
                        sourceTypeName = "TW";
                        
                        
                        break;
            case 3:     
                        sourceTypeName = "IG";

                        break;
            case 4:     
                        sourceTypeName = "PT";
                       
                        break;            
            case 5:     
                        sourceTypeName = "EM";
           
                        break;       
            default:    sourceTypeName = "ALL" ;
                        break;
         }
        return sourceTypeName;
    }
    
    
    public void soSourceTypeListener(ValueChangeEvent event){
        sourceTypeID = (Integer) event.getNewValue();
        if(sourceTypeID == null)
            sourceTypeID = 0 ;
        
        Map<String, String> messages = new LinkedHashMap<String, String>();
         switch (sourceTypeID) {
            case 1:     sourceTypeID = 1; // FaceBook
                        messages.put("Facebook Stream"      ,"FB_STM" );
                        messages.put("Facebook Comment"     ,"FB_CMT" );
                        break;
            case 2:     sourceTypeID = 2; //Twitter
                        messages.put("Twitter Tweet"        ,"TW_TWT" );
                        messages.put("Twitter Search"       ,"TW_SRH" );
                        messages.put("Twitter List"         ,"TW_LST" );
                        
                        break;
            case 3:     sourceTypeID = 3; //Instagram
                        sourceTypeName = "IG";

                        break;
            case 4:     sourceTypeID = 4; //Panip
                        messages.put("Pantip Post"          ,"PT_PST" );
                        break;            
            case 5:     sourceTypeID = 5; //Email
                        /// High , Normal , Urgent และ Very Urgent
                        messages.put("Urgent"       ,"URGENT" );
                        messages.put("High"         ,"HIGH" );
                        messages.put("Normal"       ,"NORMAL" );
                        messages.put("Low"  ,"LOW" );
                        break;       

            
         }
        soMessageList = messages;
    }
    
    public String getSoCaseStatus() {
        return soCaseStatus;
    }

    public void setSoCaseStatus(String soCaseStatus) {
        this.soCaseStatus = soCaseStatus;
    }
    
    public SoServiceDAO getSoServiceDAO() {
        return soServiceDAO;
    }

    public void setSoServiceDAO(SoServiceDAO soServiceDAO) {
        this.soServiceDAO = soServiceDAO;
    }
    
   public Integer getSoServiceId() {
        return soServiceId;
    }

    public void setSoServiceId(Integer soServiceId) {
        this.soServiceId = soServiceId;
        this.soServiceName = "All";
        try {
            if (soServiceId!=null && soServiceId!=0) {
                this.soServiceName = this.getSoServiceDAO().findSoService(soServiceId).getName();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
    
    public Map<String, Integer> getSoServiceList() {
        List<SoService> soServices = this.getSoServiceDAO().findSoServiceEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (SoService obj : soServices) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }

    public void setSoServiceList(List<SoService> soServiceList) {
        this.soServiceList = soServiceList;
    }
    
    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    public Integer getSourceTypeID() {
        return sourceTypeID;
    }

    public void setSourceTypeID(Integer sourceTypeID) {
        this.sourceTypeID = sourceTypeID;
    }

    public SoAccountDAO getSoAccountDAO() {
        return soAccountDAO;
    }

    public void setSoAccountDAO(SoAccountDAO soAccountDAO) {
        this.soAccountDAO = soAccountDAO;
    }

    public SoChannelDAO getSoChannelDAO() {
        return soChannelDAO;
    }

    public void setSoChannelDAO(SoChannelDAO soChannelDAO) {
        this.soChannelDAO = soChannelDAO;
    }
    
        // Get/Set List
    public Map<String, Integer> getAccountList() {
        return getSoAccountDAO().getSoAccountList();
    }

    public void setAccountList(List<SoAccount> accountList) {
        this.accountList = accountList;
    }

    public Map<String, Integer> getChannelList() {
        return getSoChannelDAO().getSoChannelList();
    }

    public void setSoCaseTypeList(Map<String, Integer> soCaseTypeList) {
        this.soCaseTypeList = soCaseTypeList;
    }
    public void setChannelList(List<SoChannel> channelList) {
        this.channelList = channelList;
    }
    
    public List<SoMsgCasetypeMap> getSoMsgCasetypeMapList() {
        try{
            soMsgCasetypeMapList = new ArrayList<SoMsgCasetypeMap>();
            if(soMessage.getSoMsgCasetypeMapCollection() != null && !soMessage.getSoMsgCasetypeMapCollection().isEmpty()){
                for(SoMsgCasetypeMap c : soMessage.getSoMsgCasetypeMapCollection()){
                    soMsgCasetypeMapList.add(c);
                }
            }
        }catch(Exception e){
            log.error(e);
        }
        return soMsgCasetypeMapList;
    }

    public SoMessage getSoMessage() {
        return soMessage;
    }

    public void setSoMessage(SoMessage soMessage) {
        this.soMessage = soMessage;
    }
    
    public void setSoMsgCasetypeMapList(List<SoMsgCasetypeMap> soMsgCasetypeMapList) {
        this.soMsgCasetypeMapList = soMsgCasetypeMapList;
    }
    
    public SoMsgCasetypeMapDAO getSoMsgCasetypeMapDAO() {
        return soMsgCasetypeMapDAO;
    }

    public void setSoMsgCasetypeMapDAO(SoMsgCasetypeMapDAO soMsgCasetypeMapDAO) {
        this.soMsgCasetypeMapDAO = soMsgCasetypeMapDAO;
    }
    
    public SoCaseTypeDAO getSoCaseTypeDAO() {
        return soCaseTypeDAO;
    }

    public void setSoCaseTypeDAO(SoCaseTypeDAO soCaseTypeDAO) {
        this.soCaseTypeDAO = soCaseTypeDAO;
    }
   
     public static void removeRow(XSSFSheet sheet, int rowIndex) {
        int lastRowNum=sheet.getLastRowNum();
        if(rowIndex>=0&&rowIndex<lastRowNum){
            sheet.shiftRows(rowIndex+1,lastRowNum, -1);
        }
        if(rowIndex==lastRowNum){
            XSSFRow removingRow=sheet.getRow(rowIndex);
            if(removingRow!=null){
                sheet.removeRow(removingRow);
            }
        }
    }
     
     protected void copyRow(Sheet worksheet, int sourceRowNum, int destinationRowNum) {
        // Get the source / new row
        Row newRow = worksheet.getRow(destinationRowNum);
        Row sourceRow = worksheet.getRow(sourceRowNum);

        // If the row exist in destination, push down all rows by 1 else create a new row
        if (newRow != null) {
        worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
        } else {
        newRow = worksheet.createRow(destinationRowNum);
        }

        // Loop through source columns to add to new row
        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
        // Grab a copy of the old/new cell
        Cell oldCell = sourceRow.getCell(i);
        Cell newCell = newRow.createCell(i);

        // If the old cell is null jump to next cell
        if (oldCell == null) {
          newCell = null;
          continue;
        }

        // Use old cell style
        newCell.setCellStyle(oldCell.getCellStyle());

        // If there is a cell comment, copy
        if (newCell.getCellComment() != null) {
          newCell.setCellComment(oldCell.getCellComment());
        }

        // If there is a cell hyperlink, copy
        if (oldCell.getHyperlink() != null) {
          newCell.setHyperlink(oldCell.getHyperlink());
        }

        // Set the cell data type
        newCell.setCellType(oldCell.getCellType());

        // Set the cell data value
        switch (oldCell.getCellType()) {
        case Cell.CELL_TYPE_BLANK:
          break;
        case Cell.CELL_TYPE_BOOLEAN:
          newCell.setCellValue(oldCell.getBooleanCellValue());
          break;
        case Cell.CELL_TYPE_ERROR:
          newCell.setCellErrorValue(oldCell.getErrorCellValue());
          break;
        case Cell.CELL_TYPE_FORMULA:
          newCell.setCellFormula(oldCell.getCellFormula());
          break;
        case Cell.CELL_TYPE_NUMERIC:
          newCell.setCellValue(oldCell.getNumericCellValue());
          break;
        case Cell.CELL_TYPE_STRING:
          newCell.setCellValue(oldCell.getRichStringCellValue());
          break;
        }
        }
        }
        
        
    //Social Get Set
    public Integer getSoCaseTypeId() {
        return soCaseTypeId;
    }

    public void setSoCaseTypeId(Integer soCaseTypeId) {
        this.soCaseTypeId = soCaseTypeId;
    }

    public Integer getSoSubCaseTypeId() {
        return soSubCaseTypeId;
    }

    public void setSoSubCaseTypeId(Integer soSubCaseTypeId) {
        this.soSubCaseTypeId = soSubCaseTypeId;
    }

    public CustomerTypeDAO getCustomerTypeDAO() {
        return customerTypeDAO;
    }

    public void setCustomerTypeDAO(CustomerTypeDAO customerTypeDAO) {
        this.customerTypeDAO = customerTypeDAO;
    }
    
    public Map<String, Integer> getCustomerTypeList() {
        return getCustomerTypeDAO().getCustomeTypeList();
    }
    public void setCustomerTypeList(List<CustomerType> customerTypeList) {
        this.customerTypeList = customerTypeList;
    }

    public Integer getUserOption() {
        return userOption;
    }

    public void setUserOption(Integer userOption) {
        this.userOption = userOption;
    }

    public String getSoMessageId() {
        return soMessageId;
    }

    public void setSoMessageId(String soMessageId) {
        this.soMessageId = soMessageId;
    }

    public Map<String, String> getSoMessageList() {
        return soMessageList;
    }

    public void setSoMessageList(Map<String, String> soMessageList) {
        this.soMessageList = soMessageList;
    }

    public Integer getCustomerTypeId() {
        return customerTypeId;
    }

    public void setCustomerTypeId(Integer customerTypeId) {
        this.customerTypeId = customerTypeId;
    }

    public LogoffTypeDAO getLogoffTypeDAO() {
        return logoffTypeDAO;
    }

    public void setLogoffTypeDAO(LogoffTypeDAO logoffTypeDAO) {
        this.logoffTypeDAO = logoffTypeDAO;
    }

    public Map<String, Integer> getLogoffTypeList() {
        return getLogoffTypeDAO().getLogoffList();
    }

    public Integer getLogoffID() {
        return logoffID;
    }

    public void setLogoffID(Integer logoffID) {
        this.logoffID = logoffID;
    }

    public Map<String, Integer> getSupervisorList() {
        return getUsersDAO().getSupApprovalList();
    }

    public Integer getSupId() {
        return supId;
    }

    public void setSupId(Integer supId) {
        this.supId = supId;
    }

    public SoIgnoreReasonDAO getSoIgnoreReasonDAO() {
        return soIgnoreReasonDAO;
    }

    public void setSoIgnoreReasonDAO(SoIgnoreReasonDAO soIgnoreReasonDAO) {
        this.soIgnoreReasonDAO = soIgnoreReasonDAO;
    }
    
    public Map<String, Integer> getIgnoreReasonList() {
        return getSoIgnoreReasonDAO().getIgnoreReasonList();
    }

    public Integer getIgnoreReasonId() {
        return ignoreReasonId;
    }

    public void setIgnoreReasonId(Integer ignoreReasonId) {
        this.ignoreReasonId = ignoreReasonId;
    }

    public Integer getFromYear() {
        return fromYear;
    }

    public void setFromYear(Integer fromYear) {
        this.fromYear = fromYear;
    }
    
}

