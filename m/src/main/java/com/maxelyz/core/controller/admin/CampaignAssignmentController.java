package com.maxelyz.core.controller.admin;

//import com.maxelyz.core.constant.CrmConstant;
import com.maxelyz.core.controller.common.BaseController;
import com.maxelyz.core.model.dao.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.core.model.value.admin.UserAssignmentValue;
import com.maxelyz.core.model.value.admin.CriteriaListValue;
import com.maxelyz.utils.JSFUtil;
import java.io.Serializable;
import java.util.*;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;
import com.maxelyz.utils.SecurityUtil;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

@ManagedBean(name = "campaignAssignmentController")
@ViewScoped
public class CampaignAssignmentController extends BaseController implements Serializable {

    private static Logger log = Logger.getLogger(CampaignAssignmentController.class);
    private static String ASSIGNMENT_AVERAGE = "average";
    private static String ASSIGNMENT_CUSTOM = "custom";
    private static String ASSIGNMENT_POOLING = "pooling";
    private static String ASSIGNMENT_POOLINGCUSTOM = "poolingcustom";
    private static String ASSIGNMENT_POOLINGDAILY = "poolingdaily";
    private static String ASSIGNMENT_AUTODIALER = "autodialer";
    private static String MODE_MANAGER = "manager";
    private static String MODE_SUPERVISOR = "supervisor";
    
    private Map<String, Integer> marketingList = new LinkedHashMap<String, Integer>();
    private List<Integer> selectedProduct;
    private List<Users> selectedUser;
    private List<UserAssignmentValue> selectedAssignmentUser;
    private List<CriteriaListValue> selectedCriteria;
    private List<CriteriaListValue> selectedAdvanceCriteria;
    private Map<String, Integer> productList = new LinkedHashMap<String, Integer>();
    private Map<String, String> assignmentTypeList = new LinkedHashMap<String, String>();
    private static String REDIRECT_PAGE = "campaign.jsf";
    private static String SUCCESS = "campaignsummary.xhtml";
    private static String FAILURE = "campaignassignment.xhtml";
    private Assignment assignment;
    private Campaign campaign;
    private int marketingId;
    private String assignmentType;
    private String assignmentMode;
    private String assignmentAlgo;
    private String selectingUser;
    private int userGroupId;
    private int norecord;
    private int totalrecord = 0;
    private String message;
    private String messageSim;
    private String listType;
    private int allRecord = 0;
    private int newRecord = 0;
    private int oldRecord = 0;
    private String role = "";
    private int step;
//    private List<Object[]> customerList;
    protected Date fromDate;
    protected Date toDate;
    private boolean resetNewList = false;
    private boolean managerRole = false;
    
    //FOR CRITERIA CONTACT RESULT
    private boolean contactStatusAssigned;
    private boolean contactStatusViewed;
    private boolean contactStatusOpened;
    private boolean contactStatusFollowup;
    private Map<Integer, Boolean> selectedFollowupIds = new LinkedHashMap<Integer, Boolean>();
    private Map<Integer, Boolean> selectedOpenIds = new LinkedHashMap<Integer, Boolean>();
    private List<ContactResult> selectedFollowup;
    private List<ContactResult> selectedOpen;
    private List<Integer> followupIds;
    private List<Integer> openIds;
    private String uniqueID;
    
    @ManagedProperty(value = "#{campaignDAO}")
    private CampaignDAO campaignDAO;
    @ManagedProperty(value = "#{marketingDAO}")
    private MarketingDAO marketingDAO;
    @ManagedProperty(value = "#{productDAO}")
    private ProductDAO productDAO;
    @ManagedProperty(value = "#{userGroupDAO}")
    private UserGroupDAO userGroupDAO;
    @ManagedProperty(value = "#{usersDAO}")
    private UsersDAO usersDAO;
    @ManagedProperty(value = "#{customerDAO}")
    private CustomerDAO customerDAO;
    @ManagedProperty(value = "#{assignmentDAO}")
    private AssignmentDAO assignmentDAO;
    @ManagedProperty(value = "#{assignmentDetailDAO}")
    private AssignmentDetailDAO assignmentDetailDAO;
    @ManagedProperty(value = "#{assignmentProductDAO}")
    private AssignmentProductDAO assignmentProductDAO;
    @ManagedProperty(value = "#{contactResultDAO}")
    private ContactResultDAO contactResultDAO;
    @ManagedProperty(value = "#{ctiAdapterExportFileDAO}")
    private CtiAdapterExportFileDAO ctiAdapterExportFileDAO;
    
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:campaignassignment:add")) {
            SecurityUtil.redirectUnauthorize();
        }
        clearValue();
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");
        if (selectedID == null) {
            JSFUtil.redirect(REDIRECT_PAGE);
        } else {

            CampaignDAO dao = getCampaignDAO();
            campaign = dao.findCampaign(new Integer(selectedID));

//            assignmentTypeList.put("Assigned (Average)", this.ASSIGNMENT_AVERAGE);
//            assignmentTypeList.put("Assigned (Custom)", this.ASSIGNMENT_CUSTOM);
//            assignmentTypeList.put("Pooling", this.ASSIGNMENT_POOLING);
//            assignmentTypeList.put("Pooling (Custom)", this.ASSIGNMENT_POOLINGCUSTOM);
//            assignmentTypeList.put("Pooling (Daily)", this.ASSIGNMENT_POOLINGDAILY);
//            assignmentTypeList.put("Auto-Dialer", this.ASSIGNMENT_AUTODIALER);

            selectedCriteria = new ArrayList<CriteriaListValue>();
            selectedAdvanceCriteria = new ArrayList<CriteriaListValue>();
            selectedAssignmentUser = new ArrayList<UserAssignmentValue>();
            role = JSFUtil.getUserSession().getUserGroup().getRole();
            if (role.contains("CampaignManager")) {
                assignmentMode = "manager";
                managerRole = true;
            } else {
                assignmentMode = "supervisor";
            }
            assignmentAlgo = "sequential";
            
            this.setMarketingList();
            this.setProductList();
        }
        
        this.selectedFollowup = this.getContactResultDAO().findFollowContactResult();
        this.selectedOpen = this.getContactResultDAO().findOpenContactResult();
        uniqueID = UUID.randomUUID().toString();
    }

    public void clearValue() {
        message = "";
        step = 1;
        marketingId = 0;
        norecord = 0;
        totalrecord = 0;
        allRecord = 0;
        newRecord = 0;
        oldRecord = 0;
        listType = "";
        resetNewList = false;
        if (selectedProduct != null) {
            selectedProduct.clear();
        }
        assignmentType = null;
        selectingUser = null;
        assignment = null;
        fromDate = new Date();
        toDate = new Date();
        if (selectedUser != null) {
            selectedUser.clear();
        }
        
        //For Contact Result
        contactStatusAssigned = false;
        contactStatusViewed =  false;
        contactStatusFollowup = false;
        contactStatusOpened = false;
        if (selectedFollowupIds != null) {
            selectedFollowupIds.clear();
        }
        if (selectedOpenIds != null) {
            selectedOpenIds.clear();
        }
    }

    public boolean isManagerPermitted() {
        if (role.contains("CampaignManager")) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean isResetNewListPermitted() {
        return SecurityUtil.isPermitted("admin:campaignassignment:resetnewlist");
    }
    
    public String backAction() {
//        System.out.println("backAction");
        // clear reference_no_2 on customer
//        assignmentDAO.clearReferencForAssign(uniqueID);
        
        return SUCCESS;
    }

    public void marketingRecord() {
        if (role.contains("CampaignManager")) {
            allRecord = this.getAssignmentDAO().findAllCustomer(marketingId, "allList");
            newRecord = this.getAssignmentDAO().findAllCustomer(marketingId, "newList");
            oldRecord = this.getAssignmentDAO().findAllCustomer(marketingId, "oldList");
        } else {
            allRecord = this.getAssignmentDAO().findAllCustomerFromsup(marketingId, campaign, "allList");
            newRecord = this.getAssignmentDAO().findAllCustomerFromsup(marketingId, campaign, "newList");
            oldRecord = this.getAssignmentDAO().findAllCustomerFromsup(marketingId, campaign, "oldList");
        }
        initSelectedCriteria();
    }

    private List<Integer> getFollowupIdsFromCheckBox() {
        List<Integer> followupIds = new ArrayList<Integer>();
        if (selectedFollowup != null && !selectedFollowupIds.isEmpty() && selectedFollowupIds != null) {
            for (ContactResult dataItem : selectedFollowup) {
                if (selectedFollowupIds.get(dataItem.getId()).booleanValue()) {
                    followupIds.add(dataItem.getId());
                }
            }
        }
        return followupIds;
    }

    private List<Integer> getOpenIdsFromCheckBox() {
        List<Integer> openIds = new ArrayList<Integer>();
        if (selectedOpen != null && !selectedOpenIds.isEmpty() && selectedOpenIds != null) {
            for (ContactResult dataItem : selectedOpen) {
                if (selectedOpenIds.get(dataItem.getId()).booleanValue()) {
                    openIds.add(dataItem.getId());
                }
            }
        }
        return openIds;
    }
    
    private String listToString(List<Integer> l) {
        StringBuilder sb = new StringBuilder();
        for (int id : l) {
            sb.append(id);
            sb.append(',');
        }
        return sb.toString();
    }
    
    public void calculateNoofRecord() {
        if(!listType.equals("newList")) {
            StringBuilder contactStatus = new StringBuilder();
            followupIds = null;
            openIds = null;
            followupIds = this.getFollowupIdsFromCheckBox();
            openIds = this.getOpenIdsFromCheckBox();
            contactStatus.append(contactStatusAssigned ? "assigned," : "");
            contactStatus.append(contactStatusViewed ? "viewed," : "");
            if (contactStatusOpened || (openIds != null && !openIds.isEmpty())) {
                contactStatus.append("opened,");
                contactStatus.append(this.listToString(openIds));
            }
            if (contactStatusFollowup || (followupIds != null && !followupIds.isEmpty())) {
                contactStatus.append("follow up,");
                contactStatus.append(this.listToString(followupIds));
            }
        }
        
        if (role.contains("CampaignManager")) {
//            customerList = this.getAssignmentDAO().findUnAssignedCustomer(marketingId, listType, selectedCriteria, selectedAdvanceCriteria);
//            customerList = this.getAssignmentDAO().findUnAssignedCustomer(marketingId, listType, selectedCriteria, selectedAdvanceCriteria, 
//                           contactStatusAssigned, contactStatusViewed, contactStatusOpened, contactStatusFollowup, openIds, followupIds);
            totalrecord = this.getAssignmentDAO().countUnAssignedCustomer(marketingId, listType, selectedCriteria, selectedAdvanceCriteria, 
                           contactStatusAssigned, contactStatusViewed, contactStatusOpened, contactStatusFollowup, openIds, followupIds, uniqueID);
//            totalrecord = customerList.size();
        } else {
//            customerList = this.getAssignmentDAO().findUnAssignedCustomerFromsup(marketingId, campaign, listType, selectedCriteria, selectedAdvanceCriteria);
//            customerList = this.getAssignmentDAO().findUnAssignedCustomerFromsup(marketingId, campaign, listType, selectedCriteria, selectedAdvanceCriteria,
//                           contactStatusAssigned, contactStatusViewed, contactStatusOpened, contactStatusFollowup, openIds, followupIds);
            totalrecord = this.getAssignmentDAO().countUnAssignedCustomerFromsup(marketingId, campaign, listType, selectedCriteria, selectedAdvanceCriteria,
                           contactStatusAssigned, contactStatusViewed, contactStatusOpened, contactStatusFollowup, openIds, followupIds, uniqueID);
//            totalrecord = customerList.size();
        }
    }

    public void simulateList() {
        messageSim = "";
        List<UserAssignmentValue> assignmentUsers = this.getUserAssignmentFromCheckBox();
        if (assignmentType.equals(ASSIGNMENT_CUSTOM)) {
            norecord = 0;
            for (UserAssignmentValue u : assignmentUsers) {
                norecord += u.getRecord();
            }
        }
        if (checkAssignRecord()) {
            messageSim = "";
            selectedAssignmentUser.clear();
            if (!assignmentUsers.isEmpty()) {                
                if (assignmentType.equals(ASSIGNMENT_CUSTOM) || assignmentType.equals(ASSIGNMENT_POOLINGCUSTOM) || assignmentType.equals(ASSIGNMENT_POOLINGDAILY) || assignmentType.equals(ASSIGNMENT_AUTODIALER)) {
                    if(assignmentType.equals(ASSIGNMENT_AUTODIALER) && assignmentUsers.get(0) != null) {
                        assignmentUsers.get(0).setRecord((long) norecord);
                    }
                    for (UserAssignmentValue u : assignmentUsers) {
                        UserAssignmentValue value;
                        value = new UserAssignmentValue(true, u.getUser(), u.getRecord(), u.getTotals() == null ? 0 : u.getTotals(), u.getNew1() == null ? 0 : u.getNew1(), u.getUsed() == null ? 0 : u.getUsed(), null);
                        selectedAssignmentUser.add(value);
                    }
                } else if (assignmentType.equals(ASSIGNMENT_AVERAGE)) {
                    int noofuser = assignmentUsers.size();
                    int averageCustomerPerUser = 0;
                    int lastUser = 0;

                    averageCustomerPerUser = (int) (Math.floor(norecord * 1.0 / noofuser));
                    if ((norecord % noofuser) != 0) {
                        lastUser = norecord - (averageCustomerPerUser * noofuser);
                    }

                    for (UserAssignmentValue u : assignmentUsers) {
                        UserAssignmentValue value;
                        if (lastUser != 0) {
                            value = new UserAssignmentValue(true, u.getUser(), (long) averageCustomerPerUser + 1, u.getTotals(), u.getNew1() == null ? 0 : u.getNew1(), u.getUsed() == null ? 0 : u.getUsed(), null);
                            lastUser--;
                        } else {
                            value = new UserAssignmentValue(true, u.getUser(), (long) averageCustomerPerUser, u.getTotals(), u.getNew1() == null ? 0 : u.getNew1(), u.getUsed() == null ? 0 : u.getUsed(), null);
                        }
                        selectedAssignmentUser.add(value);
                    }
                } else if (assignmentType.equals(ASSIGNMENT_POOLING)) {
                    for (UserAssignmentValue u : assignmentUsers) {
                        UserAssignmentValue value;
                        value = new UserAssignmentValue(true, u.getUser(), (long) 0, u.getTotals(), null);
                        selectedAssignmentUser.add(value);
                    }
                }
            }
        } else {
            if (norecord > totalrecord) {
                messageSim = " Exceeded records";
            } else if (norecord <= 0) {
                messageSim = " Please assign record";
            }
            FacesContext.getCurrentInstance().renderResponse();
        }
    }
    
    public void findUserAutoDialer() {
        Users agent = usersDAO.findAgentForAutoDialer();
        if(agent == null) {
            message = " System cannot find system ‘s user to assign";
            step = 2;
        } else {
            if (selectedUser != null) {
                selectedUser.clear();
            }       
            selectedUser = new ArrayList<Users>();
            selectedUser.add(agent);
            this.initSelectedUsers();          
        }
    }
    
    public void exportAutoDialer(Assignment assignment) {
        String exportPath = JSFUtil.getuploadPath() + "autodialer/";        
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddhhmm", Locale.US);

        String filename = "";
        try {
            // 1. GET MARKETING CODE WITH MAX 10 CHARACTER
            Marketing marketing = marketingDAO.findMarketing(assignment.getMarketing().getId());
            String marketingCode = marketing.getCode();
            if(marketingCode.length() > 10) {
                marketingCode = marketingCode.substring(0,10);
            }        

            // 2. REPLACE SPECIAL CHARACTER
            marketingCode = marketingCode.replaceAll("[^a-zA-Z0-9]+","_");                
            if(assignment.getNewList() != null && !assignment.getNewList()) { // ALL AND OLD LIST
                filename += "CB_" + marketingCode + "_" + sdf.format(new Date()) + ".txt"; 
            } else { // NEW LIST
                filename += "NW_" + marketingCode + "_" + sdf.format(new Date()) + ".txt"; 
            }
        } catch (Exception ex) {
            filename = "_" + sdf.format(new Date()) + ".txt";  
            System.out.println("Convert File Name Error "+ex.getMessage());
        }
        
        String physicalName = exportPath + filename;      
        File file;
        Writer out = null;
        Collection<AssignmentDetail> assignmentDetails = assignmentDetailDAO.findAssignmentDetailByAssignmentID(assignment.getId());
        if(assignmentDetails != null) {
            try {
                file = new File(physicalName);
                out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "Windows-874"));

                if (!file.exists()) {
                    file.createNewFile();
                }

                String systemID = JSFUtil.getApplication().getSystemID();
                Users agent = null;
                for(AssignmentDetail ad: assignmentDetails) {
                    agent = ad.getUsers();
                    Customer customer = ad.getCustomerId();
                    String format = "%-10s%-10s%-2s%-2s%-20s%-20s%-20s%-20s%-50s%-50s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s%-100s";
                    String content = String.format(format, "Sale", ad.getId().toString(), "TH", systemID==null?"":systemID, customer.getMobilePhone()==null?"":customer.getMobilePhone(), 
                            customer.getHomePhone()==null?"":customer.getHomePhone(), customer.getOfficePhone()==null?"":customer.getOfficePhone(), 
                            customer.getFax()==null?"":customer.getFax(), customer.getName()==null?"":customer.getName(), customer.getSurname()==null?"":customer.getSurname(), 
                            customer.getFlexfield1()==null?"":customer.getFlexfield1(), customer.getFlexfield2()==null?"":customer.getFlexfield2(), customer.getFlexfield3()==null?"":customer.getFlexfield3(), 
                            customer.getFlexfield4()==null?"":customer.getFlexfield4(), customer.getFlexfield5()==null?"":customer.getFlexfield5(), customer.getFlexfield6()==null?"":customer.getFlexfield6(), 
                            customer.getFlexfield7()==null?"":customer.getFlexfield7(), customer.getFlexfield8()==null?"":customer.getFlexfield8(), customer.getFlexfield9()==null?"":customer.getFlexfield9(), 
                            customer.getFlexfield10()==null?"":customer.getFlexfield10(), customer.getFlexfield11()==null?"":customer.getFlexfield11(), customer.getFlexfield12()==null?"":customer.getFlexfield12(), 
                            customer.getFlexfield13()==null?"":customer.getFlexfield13(), customer.getFlexfield14()==null?"":customer.getFlexfield14(), customer.getFlexfield15()==null?"":customer.getFlexfield15(), 
                            customer.getFlexfield16()==null?"":customer.getFlexfield16(), customer.getFlexfield17()==null?"":customer.getFlexfield17(), customer.getFlexfield18()==null?"":customer.getFlexfield18(), 
                            customer.getFlexfield19()==null?"":customer.getFlexfield19(), customer.getFlexfield20()==null?"":customer.getFlexfield20(), customer.getFlexfield21()==null?"":customer.getFlexfield21(), 
                            customer.getFlexfield22()==null?"":customer.getFlexfield22(), customer.getFlexfield23()==null?"":customer.getFlexfield23(), customer.getFlexfield24()==null?"":customer.getFlexfield24(), 
                            customer.getFlexfield25()==null?"":customer.getFlexfield25(), customer.getFlexfield26()==null?"":customer.getFlexfield26(), customer.getFlexfield27()==null?"":customer.getFlexfield27(), 
                            customer.getFlexfield28()==null?"":customer.getFlexfield28(), customer.getFlexfield29()==null?"":customer.getFlexfield29(), customer.getFlexfield30()==null?"":customer.getFlexfield30(), 
                            customer.getFlexfield31()==null?"":customer.getFlexfield31(), customer.getFlexfield32()==null?"":customer.getFlexfield32(), customer.getFlexfield33()==null?"":customer.getFlexfield33(), 
                            customer.getFlexfield34()==null?"":customer.getFlexfield34(), customer.getFlexfield35()==null?"":customer.getFlexfield35(), customer.getFlexfield36()==null?"":customer.getFlexfield36(), 
                            customer.getFlexfield37()==null?"":customer.getFlexfield37(), customer.getFlexfield38()==null?"":customer.getFlexfield38(), customer.getFlexfield39()==null?"":customer.getFlexfield39(), 
                            customer.getFlexfield40()==null?"":customer.getFlexfield40(), customer.getFlexfield41()==null?"":customer.getFlexfield41(), customer.getFlexfield42()==null?"":customer.getFlexfield42(), 
                            customer.getFlexfield43()==null?"":customer.getFlexfield43(), customer.getFlexfield44()==null?"":customer.getFlexfield44(), customer.getFlexfield45()==null?"":customer.getFlexfield45(), 
                            customer.getFlexfield46()==null?"":customer.getFlexfield46(), customer.getFlexfield47()==null?"":customer.getFlexfield47(), customer.getFlexfield48()==null?"":customer.getFlexfield48(), 
                            customer.getFlexfield49()==null?"":customer.getFlexfield49(), customer.getFlexfield50()==null?"":customer.getFlexfield50());
                    out.append(content);
                    out.append("\r\n");
                }

                // CREATE LOG TO CTI ADAPTER EXPORT FILE TABLE
                if(assignmentDetails != null && assignmentDetails.size() > 0 && 
                        agent != null && agent.getUserGroup() != null && agent.getUserGroup().getCtiAdapter() != null) {
                    CtiAdapterExportFile ctiAdapterExportFile = new CtiAdapterExportFile();
                    ctiAdapterExportFile.setCtiAdapter(agent.getUserGroup().getCtiAdapter());
                    ctiAdapterExportFile.setFileName(filename);
                    ctiAdapterExportFile.setStatus("new");
                    ctiAdapterExportFileDAO.create(ctiAdapterExportFile);
                }
            } catch (Exception e) {
                log.error(e);
                e.printStackTrace();
            } finally {          
                if(out != null) {
                    try {
                        out.close();
                    } catch (Exception e) { 
                        e.printStackTrace();
                    }
                }
            }
        }
        
    }

    public synchronized void saveAction() {
        message = "";
       
        // CHECK AUTO DIALER
        if(assignmentType.equals("autodialer")) {
            selectingUser = "all";
            assignmentAlgo = "sequential";
            resetNewList = true;
            findUserAutoDialer();
        } 
        if(selectedAssignmentUser != null) {
            simulateList();
        }
        if (messageSim.equals("") && message.equals("")) {
            assignment = this.insertAssignment();
            this.getAssignmentDAO().assignProduct(assignment, this.getSelectedProductCollection());
            if(assignmentType.equals("autodialer")) {
                exportAutoDialer(assignment);
            }
        }
    }

    public Boolean checkAssignRecord() {
//        this.calculateNoofRecord(); //recalc

        if (norecord > totalrecord) {
            step = 2;
            message = " Exceeded records";
            return false;
        } else if (norecord <= 0) {
            step = 2;
            message = " Please assign record";
            return false;
        } else {
            return true;
        }
    }

    //List to UI----------------------------------------------------------------
    public Map<String, Integer> getMarketingList() {
        return marketingList;
    }

    private boolean containsProduct(List<Product> Products, Product product) {
        for(Product obj : Products) {
            if (obj.getId().equals(product.getId())) {
                return true;
            }
        }
        return false;
    }
    
    public void setMarketingList() {
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        List<Product> campaignProducts = (List<Product>) this.campaign.getProductCollection();
        List<Marketing> marketings;
        if (JSFUtil.getUserSession().getUsers().getIsAdministrator()) {
            marketings = this.getMarketingDAO().findAvailableMarketingEntities();
        }else if (role.contains("CampaignManager")) {
            marketings = this.getMarketingDAO().findAvailableMarketingEntitiesByUserGroup(JSFUtil.getUserSession().getUsers().getUserGroup());
        }else{
            marketings = this.getMarketingDAO().findAvailableMarketingFromSup(this.campaign);
        }
        
        for (Marketing obj : marketings) {
            if (obj.getIsLinkProduct() != null && obj.getIsLinkProduct().booleanValue()) {
                List<Product> marketingProducts = (List<Product>) obj.getProductCollection();
                for (Product product : marketingProducts) {
                    if (containsProduct(campaignProducts, product) && product.getEnable().booleanValue()) {
                        values.put(obj.getCode() + ": " + obj.getName(), obj.getId());
                        break;
                    }
                }
            }else{
                values.put(obj.getCode() + ": " + obj.getName(), obj.getId());
            }
        }
        marketingList = values;  
    }

    public void setProductList() {
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        List<Product> campaignProducts = (List<Product>) this.campaign.getProductCollection();
        Marketing marketing;
        if (this.marketingId > 0) {
            marketing = this.getMarketingDAO().findMarketing(this.marketingId);
            if (marketing.getIsLinkProduct() != null && marketing.getIsLinkProduct().booleanValue()) {
                List<Product> marketingProducts = (List<Product>) marketing.getProductCollection();
                for (Product product : marketingProducts) {
                    if (containsProduct(campaignProducts, product) && product.getEnable().booleanValue()) {
                        if (product.getProductCategory().getCategoryType().equals("motor")) {
                            values.put(product.getName() + "(" + product.getModelType().getName() + "," + product.getModelYear() + "," + product.getProductCategory().getName() + ")", product.getId());
                        } else {
                            values.put(product.getName(), product.getId());
                        }
                    }
                }
            } else {
                for (Product product : campaignProducts) {
                    if (product.getEnable().booleanValue()) {
                        if (product.getProductCategory().getCategoryType().equals("motor")) {
                            values.put(product.getName() + "(" + product.getModelType().getName() + "," + product.getModelYear() + "," + product.getProductCategory().getName() + ")", product.getId());
                        } else {
                            values.put(product.getName(), product.getId());
                        }
                    }
                }
            }
        }    
      
        productList = values;
    }

    public Map<String, Integer> getProductList() {
        return productList;
    }
  
    public Map<String, Integer> getUserGroupList() {
        if (assignmentMode.equals("manager")) {
            return getUserGroupDAO().getUserGroupListBySupervisor();
        } else {
            return getUserGroupDAO().getUserGroupListByAgent();
        }
    }

    public List<Users> getUsersList() {
        return selectedUser;
    }

    public Map<String, String> getAssignmentTypeList() {
        return assignmentTypeList;
    }

    public void setAssignmentTypeList(Map<String, String> assignmentTypeList) {
        this.assignmentTypeList = assignmentTypeList;
    }

    //Listener------------------------------------------------------------------
    public void step1Listener(ActionEvent event) {
        step = 1;
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void step2Listener(ActionEvent event) {
        step = 2;
        if (totalrecord == 0) {
            calculateNoofRecord();
        }
        if (selectedAssignmentUser != null) {
            selectedAssignmentUser.clear();
        }
        assignmentType = null;
        selectingUser = null;
        norecord = totalrecord;
        message = "";
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void step3Listener(ActionEvent event) {
        step = 3;
        saveAction();
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void searchCriteriaListener() {      //ActionEvent event
        calculateNoofRecord();
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void assignmentModeChangeListener() {   //ActionEvent event
        assignmentType = null;
        selectingUser = null;
        if (selectedUser != null) {
            selectedUser.clear();
            this.initSelectedUsers();
        }
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void assignementTypeListener() {  //ActionEvent event
        message = "";
        messageSim = "";
        assignmentType = this.getAssignmentType();
        selectingUser = null;
        if(assignmentType.equals("autodialer")) {        
            findUserAutoDialer();
        } else {        
            if (selectedUser != null) {
                selectedUser.clear();
                this.initSelectedUsers();
            }
            if(selectedAssignmentUser != null) {
                selectedAssignmentUser.clear();
            }
        }
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void userSelectingChangeListener() { //ActionEvent event
        if (assignmentType != null) {
            if (selectingUser.equals("all")) {
                if (assignmentMode.equals("manager")) {
                    selectedUser = this.getUsersDAO().findSupervisor(JSFUtil.getUserSession().getUsers());
                } else if (assignmentMode.equals("supervisor") && (assignmentType.equals("pooling") || assignmentType.equals("poolingcustom") || assignmentType.equals("poolingdaily"))) {
                    selectedUser = this.getUsersDAO().findPoolingAgent(JSFUtil.getUserSession().getUsers(), marketingId);
                } else {
                    selectedUser = this.getUsersDAO().findAgent(JSFUtil.getUserSession().getUsers());
                }
            } else {
                userGroupId = 0;
                selectedUser = null;
                getUserGroupList();
                userGroupChangeListener();
            }
            this.initSelectedUsers();
            FacesContext.getCurrentInstance().renderResponse();
        }
    }

    public void userGroupChangeListener() {     //ActionEvent event
        if (assignmentType != null) {
            if (userGroupId == 0) {
                selectedUser = null;
            } else if (assignmentMode.equals("supervisor") && (assignmentType.equals("pooling") || assignmentType.equals("poolingcustom") || assignmentType.equals("poolingdaily"))) {
                selectedUser = this.getUsersDAO().findPoolingAgentByUserGroup(userGroupId, marketingId);
                this.initSelectedUsers();
            } else {
                selectedUser = this.getUsersDAO().findUsersEntitiesByUserGroup(userGroupId);
                this.initSelectedUsers();
            }
        }
        FacesContext.getCurrentInstance().renderResponse();
    }

    private List<UserAssignmentValue> getUserAssignmentFromCheckBox() {
        List<UserAssignmentValue> u = new ArrayList<UserAssignmentValue>();
        if (selectedAssignmentUser != null) {
            for (UserAssignmentValue obj : selectedAssignmentUser) {
                if (obj.isSelected()) {
                    u.add(obj);
                }
            }
        }
        return u;
    }

    public Assignment insertAssignment() {
        //criteria
        String criteria = "";
        if (selectedCriteria != null) {
            for (CriteriaListValue obj : selectedCriteria) {
                if (obj.isSelected()) {
                    if (obj.getMappingField().equals("customer.name") || obj.getMappingField().equals("customer.surname")) {
                        criteria += " Name " + obj.getKeyword().trim() + ",";
                    } else if (obj.getMappingField().equals("customer.dob")) {
                        criteria += " Age " + obj.getFromValue() + " - " + obj.getToValue() + ",";
                    } else if (obj.getMappingField().equals("customer.gender")) {
                        criteria += " Gender: " + obj.getKeyword() + ",";
                    } else if (obj.getMappingField().equals("customer.weight")) {
                        criteria += " Weight " + obj.getFromValue() + " - " + obj.getToValue() + ",";
                    } else if (obj.getMappingField().equals("customer.height")) {
                        criteria += " Height " + obj.getFromValue() + " - " + obj.getToValue() + ",";
                    } else if (obj.getMappingField().equals("customer.home_phone")) {
                        criteria += " Home Phone: " + obj.getKeyword().trim() + ",";
                    } else if (obj.getMappingField().equals("customer.office_phone")) {
                        criteria += " Office Phone: " + obj.getKeyword().trim() + ",";
                    } else if (obj.getMappingField().equals("customer.mobile_phone")) {
                        criteria += " Mobile Phone: " + obj.getKeyword().trim() + ",";
                    } else if (obj.getMappingField().equals("customer.privilege")) {
                        criteria += " Privilege: " + obj.getKeyword().trim() + ",";
                    } else if (obj.getMappingField().equals("customer.current_address_line8")) {
                        criteria += " Sub-District: " + obj.getKeyword().trim() + ",";
                    } else if (obj.getMappingField().equals("customer.current_province_name")) {
                        criteria += " Province: " + obj.getKeyword().trim() + ",";
                    } else if (obj.getMappingField().equals("customer.current_district_name")) {
                        criteria += " District: " + obj.getKeyword().trim() + ",";
                    } else if (obj.getMappingField().equals("customer.car_brand")) {
                        criteria += " Car Brand: " + obj.getKeyword().trim() + ",";
                    } else if (obj.getMappingField().equals("customer.car_model")) {
                        criteria += " Car Model: " + obj.getKeyword().trim() + ",";
                    } else if (obj.getMappingField().equals("customer.car_year")) {
                        criteria += " Car Year " + obj.getFromValue() + " - " + obj.getToValue() + ",";
                    }
                }
            }
        }

        if (selectedAdvanceCriteria != null) {
            for (CriteriaListValue obj : selectedAdvanceCriteria) {
                if (obj.isSelected()) {
                    criteria += " " + obj.getMappingName();

                    if (obj.getType().equals("Date")) {
                        SimpleDateFormat outputFormat = new SimpleDateFormat(obj.getPattern());
                        if (obj.getCriteria().equals("between")) {
                            criteria += " between " + outputFormat.format(obj.getFromValueDate()) + " and " + outputFormat.format(obj.getToValueDate()) + ",";
                        } else {
                            criteria += " " + obj.getCriteria() + " " + outputFormat.format(obj.getFromValueDate()) + ",";
                        }
                    } else if (obj.getType().equals("Number")) {
                         if (obj.getCriteria().equals("between")) {
                            criteria += " between " + obj.getFromValue() + " and " + obj.getToValue()+ ",";
                        } else {
                            criteria += " " + obj.getCriteria() + " " + obj.getFromValue() + ",";
                        }                        
                    } else if (obj.getType().equals("String")) {
                        criteria += " " + obj.getKeyword() + ",";
                    }
                }
            }
        }
        
        if(!listType.equals("newList")) {
            if(contactStatusAssigned) {
                criteria += "assigned,";
            }
            if(contactStatusViewed) {
                criteria += "viewed,";
            }
            if(contactStatusOpened) {
                criteria += "opened,";
            }
            if(contactStatusFollowup) {
                criteria += "follow up,";
            }
        }

        if (criteria.length() > 0) {
            criteria = criteria.substring(0, criteria.length() - 1);
        }
        List<UserAssignmentValue> assignmentUsers = this.getUserAssignmentFromCheckBox();
        Date today = new Date();
        Assignment assignment = new Assignment();
        assignment.setCampaign(campaign);
        assignment.setMarketing(new Marketing(marketingId));
        assignment.setAssignmentType(assignmentType);
        assignment.setAssignmentMode(assignmentMode);
        assignment.setCriteria(criteria);
        if (listType.equals("newList")) {
            assignment.setNewList(Boolean.TRUE);
        } else if (listType.equals("oldList")) {
            assignment.setNewList(Boolean.FALSE);
        } else {
            assignment.setNewList(null);    //all list
        }
        assignment.setNoCustomer(norecord); //->set value in dao
        assignment.setNoUser(assignmentUsers.size());//->set value in dao
        assignment.setAssignDate(today);
        assignment.setCreateBy(this.getLoginUserName());
        assignment.setCreateByUser(JSFUtil.getUserSession().getUsers());
        assignment.setCreateDate(today);
        assignment.setResetNewList(resetNewList);
        if (assignmentMode.equals("supervisor")) {
            assignment.setPoolingFromDate(fromDate);
            assignment.setPoolingToDate(toDate);
        }
//        this.getAssignmentDAO().create(assignmentMode, assignmentType, campaign, assignment, assignmentUsers, marketingId, norecord,
//                                       listType, assignmentAlgo, customerList, resetNewList);
        this.getAssignmentDAO().create(assignmentMode, assignmentType, campaign, assignment, assignmentUsers, marketingId, norecord,
                                       listType, assignmentAlgo, resetNewList, uniqueID);
        return assignment;
    }

    //Properties----------------------------------------------------------------
    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setProductSponsor(Campaign campaign) {
        this.campaign = campaign;
    }

    public int getMarketingId() {
        return marketingId;
    }

    public void setMarketingId(int marketingId) {
        this.marketingId = marketingId;
    }

    public List<Integer> getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(List<Integer> selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public String getAssignmentType() {
        return assignmentType;
    }

    public void setAssignmentType(String assignmentType) {
        this.assignmentType = assignmentType;
    }

    public String getAssignmentMode() {
        return assignmentMode;
    }

    public void setAssignmentMode(String assignmentMode) {
        this.assignmentMode = assignmentMode;
    }

    public String getSelectingUser() {
        return selectingUser;
    }

    public void setSelectingUser(String selectingUser) {
        this.selectingUser = selectingUser;
    }

    public int getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(int userGroupId) {
        this.userGroupId = userGroupId;
    }

    public int getNorecord() {
        return norecord;
    }

    public void setNorecord(int norecord) {
        this.norecord = norecord;
    }

    public int getTotalrecord() {
        return totalrecord;
    }

    public void setTotalrecord(int totalrecord) {
        this.totalrecord = totalrecord;
    }

    public List<UserAssignmentValue> getSelectedAssignmentUser() {
        return selectedAssignmentUser;
    }

    private void initSelectedUsers() {
        if(selectedAssignmentUser != null) {
            selectedAssignmentUser.clear();
        }
        if(assignmentType != null) {
            selectedAssignmentUser = getAssignmentDetailDAO().countAssignSelectedUsers(this.selectedUser, campaign, assignmentMode);
        }         
    }

    public List<CriteriaListValue> getSelectedCriteria() {
        return selectedCriteria;
    }

    private void initSelectedCriteria() {
        //Customer Field
        selectedCriteria.clear();
        selectedCriteria = marketingDAO.findMarketinCriteriaByMarketingId(marketingId);
        
        //Customer FlexField
        selectedAdvanceCriteria.clear();
        selectedAdvanceCriteria = marketingDAO.findMarketinAdvanceCriteriaByMarketingId(marketingId);
        
        //Product List Field
        if (selectedProduct != null) {
            selectedProduct.clear();
        }
        this.setProductList();

        FacesContext.getCurrentInstance().renderResponse();
    }

    public String getAssignmentTypeLabel() {
        String out = "";
        if (assignmentType.equals("average")) {
            out = "Assigned (Average)";
        }
        return out;
    }

    public List<Product> getSelectedProductCollection() {
        List<Product> products = new ArrayList<Product>();
        for (int pid : getSelectedProduct()) {
            Product p = new Product();
            p.setId(pid);
            products.add(p);
        }
        return products;
    }

    public List<UserAssignmentValue> getUsersAssignedList() {
        if (assignment == null) {
            return null;
        } else {
            return this.getAssignmentDAO().findUserAssignmentByAssignmentId(assignment.getId(), assignmentMode, assignmentType);
        }
    }

    //Managed Properties-------------------------------------------------------
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

    public CustomerDAO getCustomerDAO() {
        return customerDAO;
    }

    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public AssignmentDAO getAssignmentDAO() {
        return assignmentDAO;
    }

    public void setAssignmentDAO(AssignmentDAO assigmentDAO) {
        this.assignmentDAO = assigmentDAO;
    }

    public AssignmentDetailDAO getAssignmentDetailDAO() {
        return assignmentDetailDAO;
    }

    public void setAssignmentDetailDAO(AssignmentDetailDAO assigmentDetailDAO) {
        this.assignmentDetailDAO = assigmentDetailDAO;
    }

    public AssignmentProductDAO getAssignmentProductDAO() {
        return assignmentProductDAO;
    }

    public void setAssignmentProductDAO(AssignmentProductDAO assigmentProductDAO) {
        this.assignmentProductDAO = assigmentProductDAO;
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
        this.toDate = toDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    public int getAllRecord() {
        return allRecord;
    }

    public void setAllRecord(int allRecord) {
        this.allRecord = allRecord;
    }

    public int getNewRecord() {
        return newRecord;
    }

    public void setNewRecord(int newRecord) {
        this.newRecord = newRecord;
    }

    public int getOldRecord() {
        return oldRecord;
    }

    public void setOldRecord(int oldRecord) {
        this.oldRecord = oldRecord;
    }

    public String getMessageSim() {
        return messageSim;
    }

    public void setMessageSim(String messageSim) {
        this.messageSim = messageSim;
    }

    public String getAssignmentAlgo() {
        return assignmentAlgo;
    }

    public void setAssignmentAlgo(String assignmentAlgo) {
        this.assignmentAlgo = assignmentAlgo;
    }

    public List<CriteriaListValue> getSelectedAdvanceCriteria() {
        return selectedAdvanceCriteria;
    }

    public void setSelectedAdvanceCriteria(List<CriteriaListValue> selectedAdvanceCriteria) {
        this.selectedAdvanceCriteria = selectedAdvanceCriteria;
    }

//    public List<Object[]> getCustomerList() {
//        return customerList;
//    }
//
//    public void setCustomerList(List<Object[]> customerList) {
//        this.customerList = customerList;
//    }

    public boolean isResetNewList() {
        return resetNewList;
    }

    public void setResetNewList(boolean resetNewList) {
        this.resetNewList = resetNewList;
    }

    public boolean isContactStatusAssigned() {
        return contactStatusAssigned;
    }

    public void setContactStatusAssigned(boolean contactStatusAssigned) {
        this.contactStatusAssigned = contactStatusAssigned;
    }

    public boolean isContactStatusViewed() {
        return contactStatusViewed;
    }

    public void setContactStatusViewed(boolean contactStatusViewed) {
        this.contactStatusViewed = contactStatusViewed;
    }

    public boolean isContactStatusOpened() {
        return contactStatusOpened;
    }

    public void setContactStatusOpened(boolean contactStatusOpened) {
        this.contactStatusOpened = contactStatusOpened;
    }

    public boolean isContactStatusFollowup() {
        return contactStatusFollowup;
    }

    public void setContactStatusFollowup(boolean contactStatusFollowup) {
        this.contactStatusFollowup = contactStatusFollowup;
    }

    public Map<Integer, Boolean> getSelectedFollowupIds() {
        return selectedFollowupIds;
    }

    public void setSelectedFollowupIds(Map<Integer, Boolean> selectedFollowupIds) {
        this.selectedFollowupIds = selectedFollowupIds;
    }

    public Map<Integer, Boolean> getSelectedOpenIds() {
        return selectedOpenIds;
    }

    public void setSelectedOpenIds(Map<Integer, Boolean> selectedOpenIds) {
        this.selectedOpenIds = selectedOpenIds;
    }

    public List<Integer> getFollowupIds() {
        return followupIds;
    }

    public void setFollowupIds(List<Integer> followupIds) {
        this.followupIds = followupIds;
    }

    public List<Integer> getOpenIds() {
        return openIds;
    }

    public void setOpenIds(List<Integer> openIds) {
        this.openIds = openIds;
    }

    public List<ContactResult> getFollowupsaleReasonList() {
        return selectedFollowup;
    }

    public List<ContactResult> getOpensaleReasonList() {
        return selectedOpen;
    }

    public ContactResultDAO getContactResultDAO() {
        return contactResultDAO;
    }

    public void setContactResultDAO(ContactResultDAO contactResultDAO) {
        this.contactResultDAO = contactResultDAO;
    }

    public boolean isManagerRole() {
        return managerRole;
    }

    public void setManagerRole(boolean managerRole) {
        this.managerRole = managerRole;
    }

    public CtiAdapterExportFileDAO getCtiAdapterExportFileDAO() {
        return ctiAdapterExportFileDAO;
    }

    public void setCtiAdapterExportFileDAO(CtiAdapterExportFileDAO ctiAdapterExportFileDAO) {
        this.ctiAdapterExportFileDAO = ctiAdapterExportFileDAO;
    }
    
}
