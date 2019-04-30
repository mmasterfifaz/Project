package com.maxelyz.core.controller.report;

import com.maxelyz.core.model.dao.*;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.utils.JSFUtil;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
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

public abstract class AbstractReportBaseController {
    private static Logger log = Logger.getLogger(AbstractReportBaseController.class);
    protected static final int PDF = 1;
    protected static final int EXCEL = 2;
    protected static final String CASE_PENDING = "pending";
    protected static final String CASE_CLOSED = "closed";
//    protected static final String CASE_PENDING = JSFUtil.getBundleValue("pendingValue");
//    protected static final String CASE_CLOSED = JSFUtil.getBundleValue("closedValue");
    protected Boolean useResultSet = true;
    protected Date fromDate;
    protected Date toDate;
    protected Integer userGroupId;
    protected String groupName = "All";
    protected Integer userId;
    protected String userName = "All";

    protected Integer campaignId;
    protected String campaignName = "All";
    protected Integer paymentMethodId;
    protected String paymentMethodName = "All";
    protected String marketingCode;
    protected Integer marketingId;
    protected String marketingName = "All";
    protected Integer productId;
    protected String productName = "All";
    protected Integer serviceTypeId;
    protected String serviceTypeName = "All";
    protected Integer locationId;
    protected String locationName = "All";
    private Users user;
    protected SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
    protected Integer businessUnitId;
    protected String businessUnitName = "All"; 
    private Map<String, Integer> serviceList;
    private Map<String, Integer> businessUnitList;
    private Map<String, Integer> locationList;
    private boolean showLocation;
    protected Integer caseTypeId;
    protected String caseTypeName = "All";
    protected String caseStatus;
    protected Integer listTypeId;
    protected String listTypeName = "All";
    protected String listTypeStatus;
    protected Integer outputFormat;
    protected Integer knowledgebaseId;
    protected String knowledgebaseName  = "All" ;
    protected String knowledgebaseVersion  = "";
    protected Date knowledgebaseUpdate_date  ;
    protected String knowledgebaseUpdate_by  ;
    protected String programName;
    protected String spotRefId;
    
    private Map<String, Integer> knowledgebaseList;
    private Map<String, Integer> kbVersionList;
    private Map<String, Integer> usersList;
    private List<PhoneDirectoryCategory> phoneCategoryList;
    protected Integer phoneCategoryId;
    protected String phoneCategoryName = "All";
    @ManagedProperty(value = "#{dataSource}")
    private DataSource dataSource;
    @ManagedProperty(value = "#{caseTypeDAO}")
    private CaseTypeDAO caseTypeDAO;
    @ManagedProperty(value = "#{userGroupDAO}")
    private UserGroupDAO userGroupDAO;
    @ManagedProperty(value = "#{usersDAO}")
    private UsersDAO usersDAO;
    @ManagedProperty(value = "#{mediaPlanDAO}")
    private MediaPlanDAO mediaPlanDAO;
    //Sale
    @ManagedProperty(value = "#{campaignDAO}")
    private CampaignDAO campaignDAO;
    @ManagedProperty(value = "#{marketingDAO}")
    private MarketingDAO marketingDAO;
    @ManagedProperty(value = "#{paymentMethodDAO}")
    private PaymentMethodDAO paymentMethodDAO;
    @ManagedProperty(value = "#{productDAO}")
    private ProductDAO productDAO;
    @ManagedProperty(value = "#{serviceTypeDAO}")
    private ServiceTypeDAO serviceTypeDAO;
    @ManagedProperty(value = "#{locationDAO}")
    private LocationDAO locationDAO;
    @ManagedProperty(value = "#{businessUnitDAO}")
    private BusinessUnitDAO businessUnitDAO;
    @ManagedProperty(value = "#{knowledgebaseDAO}")
    private KnowledgebaseDAO knowledgebaseDAO;
    @ManagedProperty(value = "#{channelDAO}")
    private ChannelDAO channelDAO;
    @ManagedProperty(value="#{phoneDirectoryCategoryDAO}")
    private PhoneDirectoryCategoryDAO phoneDirectoryCategoryDAO;
    private Map<String, String> channelTypeList;
    protected String channelType;
    protected String channel; 
    @ManagedProperty(value = "#{prospectlistSponsorDAO}")
    private ProspectlistSponsorDAO prospectlistSponsorDAO;
    private Map<String, Integer> sponsorList;
    protected Integer sponsorId;
    protected String sponsorName = "All"; 
    private Map<String, Integer> marketingListCode; 
    
    protected boolean decorateXLSStringLine = false;
    protected boolean decorateXLSEmptyWhenNull = false;
    protected int[] decorateXLSStringLineColumnIdList = {};
    
    protected void init() {
        fromDate = new Date();
        toDate = new Date();
        outputFormat = PDF;
        showLocation = true;
        serviceTypeId = 0;
        businessUnitId = 0;
        locationId = 0;
        knowledgebaseUpdate_date = new Date();
        
    }

    //abstract method
    protected abstract String getReportPath();

    protected abstract Map getParameterMap();

    protected abstract String getQuery();

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
                                String text = rs.getString(i);
                                if (decorateXLSEmptyWhenNull) text  = (text==null)?"":text;
                                if (isInsertDataStyleOnXLSColumn(i)) text = "<div style='mso-number-format:\\@;'>"+text+"</div>";
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

    private void generatePDF() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        InputStream reportStream = context.getExternalContext().getResourceAsStream(this.getReportPath());
        ServletOutputStream servletOutputStream = null;
        Connection connection = null;
        Statement statement;
        ResultSet resultSet;
        try {
            connection = dataSource.getConnection();

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "Attachment;filename=report.pdf");
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
    
      public Map<String, Integer> getBusinessUnitByServiceType(int serviceTypeId) {
        ServiceType service = serviceTypeDAO.findServiceType(serviceTypeId);
        Collection<BusinessUnit> buList = service.getBusinessUnitCollection();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for(BusinessUnit obj : buList){
            values.put(obj.getName(), obj.getId());
        }
        return values;    
    }

     public void setBusinessUnit(Integer serviceTypeId){
        if(serviceTypeId == 0) {
            this.businessUnitList = this.getBusinessUnitDAO().getBusinessUnitList();
        } else {
            this.businessUnitList = this.getBusinessUnitByServiceType(serviceTypeId);
        }
     }
        
    public Map<String, Integer> getBusinessUnitList() {
        List<BusinessUnit> businessUnits = this.getUserGroupDAO().getBusinessUnitList(JSFUtil.getUserSession().getUsers().getUserGroup().getId(),serviceTypeId);
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (BusinessUnit obj : businessUnits) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
        
    public void setUsersList(Integer userGroupId) {
        if (userGroupId == 0) {
            this.usersList = this.getUsersDAO().getUsersList();
        } else {
            this.usersList = this.getUsersDAO().getUsersListByUserGroup(userGroupId);
        }
    }

    public Map<String, Integer> getUsersList() {
        if (usersList == null) {
            setUsersList(0);
        }
        return usersList;
    }
      
    public Map<String, Integer> getPhoneCategoryList() {
        List<PhoneDirectoryCategory> phoneDirectoryCategorys = phoneDirectoryCategoryDAO.findPhoneDirectoryCategoryEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (PhoneDirectoryCategory obj : phoneDirectoryCategorys) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }

    public void setPhoneCategoryList(List<PhoneDirectoryCategory> phoneCategoryList) {
        this.phoneCategoryList = phoneCategoryList;
    }

    public Integer getPhoneCategoryId() {
        return phoneCategoryId;
    }

    public void setPhoneCategoryId(Integer phoneCategoryId) {
        this.phoneCategoryId = phoneCategoryId;
        this.phoneCategoryName = "All";
        
        PhoneDirectoryCategory p = this.getPhoneDirectoryCategoryDAO().findPhoneDirectoryCategory(phoneCategoryId);
        if(p != null) {
            phoneCategoryName = p.getName();
        }
    }
    
    //Listener
    public void reportListener(ActionEvent event) {
        if (outputFormat == EXCEL) {
            generateXLS();
        } else if (outputFormat == PDF) {
            generatePDF();
        }
        FacesContext.getCurrentInstance().responseComplete();
    }
    //Listener

    public void userGroupListener(ValueChangeEvent event) {
        userGroupId = (Integer) event.getNewValue();
        setUsersList(userGroupId);
        //FacesContext.getCurrentInstance().renderResponse();
    }
    
    // Listener
    public void businessUnitListener(ValueChangeEvent event) {
        serviceTypeId = (Integer) event.getNewValue();
        setBusinessUnit(serviceTypeId);
        setLocation(0);
        setBusinessUnitId(0);
        setLocationId(0);
    }
    
    //Listener
    public void locationListener(ValueChangeEvent event) {
        businessUnitId = (Integer) event.getNewValue();
        setLocation(businessUnitId);
        setLocationId(0);
    }    
    
    public Map<String, Integer> getLocationByBusinessId(int businessUnitId) {
        BusinessUnit business = businessUnitDAO.findBusinessUnit(businessUnitId);
        Collection<Location> locationList = business.getLocationCollection();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for(Location obj : locationList){
            values.put(obj.getName(), obj.getId());
        }
        return values;    
    }
    
    public void setLocation(Integer businessUnitId) {
        if (businessUnitId == 0) {
            showLocation = true;
        } else {
            this.locationList = this.getLocationByBusinessId(businessUnitId);
            showLocation = false;
        }
    }

     public Map<String, Integer> getLocationList() {
        List<Location> locations = this.getUserGroupDAO().getLocationList(JSFUtil.getUserSession().getUsers().getUserGroup().getId(),serviceTypeId,businessUnitId);
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Location obj : locations) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
         
    public String getExcelQuery() {
        return getQuery();
    }

    public Map<String, Integer> getUsergroupList() {
        return this.getUserGroupDAO().getUserGroupList();
    }

    public Map<String, Integer> getCaseTypeList() {
        return this.caseTypeDAO.getCaseTypeList();
    }

    public Map<String, Integer> getCampaignList() {
        return this.getCampaignDAO().getCampaignList();
    }
    
    public Map<String, Integer> getPaymentMethodList() {
        return this.getPaymentMethodDAO().getPaymentMethodList();
    }    
    
    public Map<String, Integer> getMarketingList() {
        return this.getMarketingDAO().getMarketList();
    }
    
    public Map<String, Integer> getProductList() {
        return this.getProductDAO().getProductList();
    }

    public Map<String, Integer> getServiceTypeList() {
        List<ServiceType> serviceTypes = this.getUserGroupDAO().getServiceTypeList(JSFUtil.getUserSession().getUsers().getUserGroup().getId());
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (ServiceType obj : serviceTypes) {
            values.put(obj.getName(), obj.getId());
        }
        return values;
    }
    
     public Map<String, String> getProgramNameList() {
        List<MediaPlan> mediaPlans = this.getMediaPlanDAO().findMediaPlanEntities();
        Map<String, String> values = new LinkedHashMap<String, String>();
        for (MediaPlan obj : mediaPlans) {
            if(!values.containsKey(obj.getProgramName())){
                values.put(obj.getProgramName(), obj.getProgramName());
            }
        }
        return values;
    }
     
     public Map<String, String> getSpotRefList() {
        List<MediaPlan> mediaPlans = this.getMediaPlanDAO().findMediaPlanEntities();
        Map<String, String> values = new LinkedHashMap<String, String>();
        for (MediaPlan obj : mediaPlans) {
            if(!values.containsKey(obj.getSpotRefId())){
                values.put(obj.getSpotRefId(), obj.getSpotRefId());
            }
        }
        return values;
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

    public Integer getCaseTypeId() {
        return caseTypeId;
    }

    public void setCaseTypeId(Integer caseTypeId) {
        this.caseTypeId = caseTypeId;
        this.caseTypeName = "All";
        try {
            if (caseTypeId!=null && caseTypeId!=0) {
                caseTypeName = this.getCaseTypeDAO().findCaseType(caseTypeId).getName();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
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
    
    public Integer getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Integer paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
        this.paymentMethodName = "All";
        try {
            if (paymentMethodId!=null && paymentMethodId!=0) {
                this.paymentMethodName = this.getPaymentMethodDAO().findPaymentMethod(paymentMethodId).getName();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
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
        this.marketingCode = "All";
        try {
            if (marketingId!=null && marketingId!=0) {
                this.marketingName = this.getMarketingDAO().findMarketing(marketingId).getName();
                this.marketingCode = this.getMarketingDAO().findMarketing(marketingId).getCode();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
 
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
        this.productName = "All";
        try {
            if (productId!=null && productId!=0) {
                this.productName = this.getProductDAO().findProduct(productId).getName();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
        this.locationName = "All";
        try {
            if (locationId!=null && locationId!=0) {
                this.locationName = this.getLocationDAO().findLocation(locationId).getName();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public Integer getBusinessUnitId(){
        return businessUnitId;
    }
    
    public void setBusinessUnitId(Integer businessUnitId) {
        this.businessUnitId = businessUnitId;
        this.businessUnitName = "All";
        try {
            if (businessUnitId!=null && businessUnitId!=0) {
                this.businessUnitName = this.getBusinessUnitDAO().findBusinessUnit(businessUnitId).getName();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }   
        
    public Integer getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Integer serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
        this.serviceTypeName = "All";
        try {
            if (serviceTypeId!=null && serviceTypeId!=0) {
                this.serviceTypeName = this.getServiceTypeDAO().findServiceType(serviceTypeId).getName();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public Map<String, Integer> getOutputFormatList() {
        Map<String, Integer> outputFormatList = new LinkedHashMap<String, Integer>();
        outputFormatList.put("PDF", PDF);
        outputFormatList.put("Excel", EXCEL);
        return outputFormatList;
    }

    public Map<String, String> getCaseStatusList() {
        Map<String, String> status = new LinkedHashMap<String, String>();
        status.put(JSFUtil.getBundleValue("pending"), JSFUtil.getBundleValue("pendingValue"));
        status.put(JSFUtil.getBundleValue("closed"), JSFUtil.getBundleValue("closedValue"));
        return status;
    }
    
    public Map<String, String> getListTypeList() {
        Map<String, String> type = new LinkedHashMap<String, String>();
        type.put("New","new");
        type.put("Old","old");
        return type;
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

    public String getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }
    
    public String getListTypeStatus() {
        return listTypeStatus;
    }

    public void setListTypeStatus(String listTypeStatus) {
        this.listTypeStatus = listTypeStatus;
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

    public CaseTypeDAO getCaseTypeDAO() {
        return caseTypeDAO;
    }

    public void setCaseTypeDAO(CaseTypeDAO caseTypeDAO) {
        this.caseTypeDAO = caseTypeDAO;
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

    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public LocationDAO getLocationDAO() {
        return locationDAO;
    }

    public void setLocationDAO(LocationDAO locationDAO) {
        this.locationDAO = locationDAO;
    }

    public ServiceTypeDAO getServiceTypeDAO() {
        return serviceTypeDAO;
    }

    public void setServiceTypeDAO(ServiceTypeDAO serviceTypeDAO) {
        this.serviceTypeDAO = serviceTypeDAO;
    }

    public BusinessUnitDAO getBusinessUnitDAO() {
        return businessUnitDAO;
    }

    public void setBusinessUnitDAO(BusinessUnitDAO businessUnitDAO) {
        this.businessUnitDAO = businessUnitDAO;
    }
    
    public boolean isShowLocation() {
        return showLocation;
    }

    public void setShowLocation(boolean showLocation) {
        this.showLocation = showLocation;
    }

    public PaymentMethodDAO getPaymentMethodDAO() {
        return paymentMethodDAO;
    }

    public void setPaymentMethodDAO(PaymentMethodDAO paymentMethodDAO) {
        this.paymentMethodDAO = paymentMethodDAO;
    }
    
    
    
    /// Anuwat
    
    public KnowledgebaseDAO getKnowledgebaseDAO() {
        return knowledgebaseDAO;
    }

    public void setKnowledgebaseDAO(KnowledgebaseDAO knowledgebaseDAO) {
        this.knowledgebaseDAO = knowledgebaseDAO;
    }

    public Integer getKnowledgebaseId() {
        return knowledgebaseId;
    }

    public void setKnowledgebaseId(Integer knowledgebaseId) {
        this.knowledgebaseId = knowledgebaseId;
        this.knowledgebaseName = "";
        this.knowledgebaseUpdate_by = "";
        this.knowledgebaseUpdate_date = null;

        Knowledgebase kb = this.getKnowledgebaseDAO().findKnowledgebase(knowledgebaseId);
        if (kb != null) {
            knowledgebaseName = kb.getTopic();
            knowledgebaseVersion = kb.getVersion();
            knowledgebaseUpdate_by = kb.getUpdateBy();
            knowledgebaseUpdate_date = kb.getUpdateDate();
        }
    }
    
    public Map<String, Integer> getKnowledgebaseList() {
        knowledgebaseList = new LinkedHashMap<String, Integer>();
        List<Knowledgebase> kbList = this.getKnowledgebaseDAO().findKnowledgebaseEntities();
        for (Knowledgebase kb : kbList) {
            knowledgebaseList.put(kb.getTopic(), kb.getId());
        }
        return knowledgebaseList;
    }

    public Map<String, Integer> getKbVersionList() {
        return kbVersionList;
    }

    public void kbVersionListener(ValueChangeEvent event) {
        knowledgebaseId = (Integer) event.getNewValue();
        //knowledgebaseVersion = this.getKnowledgebaseDAO().findKnowledgebase(knowledgebaseId).getVersion();
        setKbVersionList(knowledgebaseId); 
    }

    public void setKbVersionList(Integer knowledgebaseId) {
        kbVersionList = new LinkedHashMap<String, Integer>();
        List<KnowledgebaseDivision> kbvList = this.getKnowledgebaseDAO().findKnowledgebaseDivisionById(knowledgebaseId);
        for (KnowledgebaseDivision kbv : kbvList) {
            kbVersionList.put(kbv.getKnowledgebaseDivisionPK().getVersion(), kbv.getKnowledgebaseDivisionPK().getKnowledgebaseId());
        }

    }

    public String getKnowledgebaseVersion() {
        return knowledgebaseVersion;
    }

    public void setKnowledgebaseVersion(String knowledgebaseVersion) {
        this.knowledgebaseVersion = knowledgebaseVersion;
        List<KnowledgebaseDivision> kbvList = this.getKnowledgebaseDAO().findKnowledgebaseDivisionByIdVersion(knowledgebaseId,knowledgebaseVersion);
        for (KnowledgebaseDivision kbv : kbvList) {
             knowledgebaseUpdate_by = kbv.getUpdateBy();
            knowledgebaseUpdate_date = kbv.getUpdateDate();
        }
    }

    public String getKnowledgebaseName() {
        return knowledgebaseName;
    }

    public void setKnowledgebaseName(String knowledgebaseName) {
        this.knowledgebaseName = knowledgebaseName;
    }

    public String getKnowledgebaseUpdate_by() {
        return knowledgebaseUpdate_by;
    }

    public void setKnowledgebaseUpdate_by(String knowledgebaseUpdate_by) {
        this.knowledgebaseUpdate_by = knowledgebaseUpdate_by;
    }

    public Date getKnowledgebaseUpdate_date() {
        return knowledgebaseUpdate_date;
    }

    public void setKnowledgebaseUpdate_date(Date knowledgebaseUpdate_date) {
        this.knowledgebaseUpdate_date = knowledgebaseUpdate_date;
    }

    public ChannelDAO getChannelDAO() {
        return channelDAO;
    }

    public void setChannelDAO(ChannelDAO channelDAO) {
        this.channelDAO = channelDAO;
    }

    public Map<String, String> getChannelTypeList() {
        this.channelTypeList = new LinkedHashMap<String, String>();
         List<String> typeList = this.getChannelDAO().findChannelTypeList();
          for (String ctype : typeList) {
             this.channelTypeList.put(ctype,ctype);
         }
        return channelTypeList;
    }

    public void setChannelTypeList(Map<String, String> channelTypeList) {
        this.channelTypeList = channelTypeList;
    }
 

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

 

    public Integer getSponsorId() {
        return sponsorId;
    }

    public ProspectlistSponsorDAO getProspectlistSponsorDAO() {
        return prospectlistSponsorDAO;
    }

    public void setProspectlistSponsorDAO(ProspectlistSponsorDAO prospectlistSponsorDAO) {
        this.prospectlistSponsorDAO = prospectlistSponsorDAO;
    }

    public void setSponsorId(Integer sponsorId) {
        this.sponsorId = sponsorId;
        this.sponsorName = "All";
         try {
            if (sponsorId != null && sponsorId != 0) {
                this.sponsorName = this.prospectlistSponsorDAO.findProspectlistSponsor(sponsorId).getName();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public Map<String, Integer> getSponsorList() { 
        this.sponsorList = this.prospectlistSponsorDAO.getProspectlistSponsorList();
        return this.sponsorList; 
    }

    public void setSponsorList(Map<String, Integer> sponsorList) {
        this.sponsorList = sponsorList;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    public PhoneDirectoryCategoryDAO getPhoneDirectoryCategoryDAO() {
        return phoneDirectoryCategoryDAO;
    }

    public void setPhoneDirectoryCategoryDAO(PhoneDirectoryCategoryDAO phoneDirectoryCategoryDAO) {
        this.phoneDirectoryCategoryDAO = phoneDirectoryCategoryDAO;
    }

    protected boolean isInsertDataStyleOnXLSColumn(int columnId){
        if ( decorateXLSStringLine && decorateXLSStringLineColumnIdList!=null ){
            for(int i : decorateXLSStringLineColumnIdList){  if( i==columnId ) return true; }
        }
        return false;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public MediaPlanDAO getMediaPlanDAO() {
        return mediaPlanDAO;
    }

    public void setMediaPlanDAO(MediaPlanDAO mediaPlanDAO) {
        this.mediaPlanDAO = mediaPlanDAO;
    }

    public String getSpotRefId() {
        return spotRefId;
    }

    public void setSpotRefId(String spotRefId) {
        this.spotRefId = spotRefId;
    }

}
