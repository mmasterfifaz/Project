/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.front.customerHandling;

import com.maxelyz.core.constant.CrmConstant;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;

import com.maxelyz.core.controller.common.BaseController;
import com.maxelyz.core.model.dao.*;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.core.model.value.front.customerHandling.CustomerInfoValue;
import com.maxelyz.core.model.value.front.customerHandling.SaleHistoryInfoValue;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.MailBean;
import com.maxelyz.utils.SecurityUtil;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpSession;
import javax.faces.bean.ViewScoped;
import org.apache.commons.lang3.StringUtils;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

/**
 *
 * @author admin
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class InquiryEditController extends BaseController {

    private Map<String, Integer> caseTypeValue;
    private List<CaseType> caseTypes = new ArrayList<CaseType>();
    private List<CaseTopic> caseTopics = new ArrayList<CaseTopic>();
    private List<CaseDetail> caseDetails = new ArrayList<CaseDetail>();
    private List<CaseRequest> caseRequests = new ArrayList<CaseRequest>();
    private List<Relationship> relationships = new ArrayList<Relationship>();
    private List<Channel> channels = new ArrayList<Channel>();
    private List<ActivityType> activityTypes = new ArrayList<ActivityType>();
    private List<ServiceType> serviceTypeList = new ArrayList<ServiceType>();
    private List<BusinessUnit> businessUnitList = new ArrayList<BusinessUnit>();
    private List<Location> locationList = new ArrayList<Location>();
    private List<ContactCaseWorkflowLog> workflowLogList = new ArrayList<ContactCaseWorkflowLog>();
    private Integer serviceTypeId;
    private Integer businessUnitId;
    private Integer locationId;
    private String mode;
    private Integer caseTypeId;
    private Integer caseTopicId;
    private Integer caseDetailId;
    private Integer caseRequestId;
    private Integer relationshipId;
    private Integer channelId;
    private Integer activityChannelId;
    private Integer activityTypeId;
    private Integer userReceiverId;
    private String userReceiverName;
    private String userReceiverEmail;
    private String userReceiverPhone;
    private Boolean createActivity;
    private Boolean hideActivity;
    private ContactCase contactCase;
    private Activity activity;
    private ActivityType activityType;
    private Users user;
    private String SUCCESS = "customerDetail.jsf?faces-redirect=true";
    private String FAIL = "caseHandlingEdit.jsf";
    @ManagedProperty(value = "#{customerDAO}")
    private CustomerDAO customerDAO;
    @ManagedProperty(value = "#{caseTypeDAO}")
    private CaseTypeDAO caseTypeDAO;
    @ManagedProperty(value = "#{caseTopicDAO}")
    private CaseTopicDAO caseTopicDAO;
    @ManagedProperty(value = "#{caseDetailDAO}")
    private CaseDetailDAO caseDetailDAO;
    @ManagedProperty(value = "#{caseRequestDAO}")
    private CaseRequestDAO caseRequestDAO;
    @ManagedProperty(value = "#{contactCaseDAO}")
    private ContactCaseDAO contactCaseDAO;
    @ManagedProperty(value = "#{relationshipDAO}")
    private RelationshipDAO relationshipDAO;
    @ManagedProperty(value = "#{channelDAO}")
    private ChannelDAO channelDAO;
    @ManagedProperty(value = "#{activityTypeDAO}")
    private ActivityTypeDAO activityTypeDAO;
    @ManagedProperty(value = "#{usersDAO}")
    private UsersDAO usersDAO;
    @ManagedProperty(value = "#{holidayDAO}")
    private HolidayDAO holidayDAO;
    @ManagedProperty(value = "#{serviceTypeDAO}")
    private ServiceTypeDAO serviceTypeDAO;
    @ManagedProperty(value = "#{businessUnitDAO}")
    private BusinessUnitDAO businessUnitDAO;
    @ManagedProperty(value = "#{locationDAO}")
    private LocationDAO locationDAO;
    @ManagedProperty(value = "#{contactHistoryDAO}")
    private ContactHistoryDAO contactHistoryDAO;
    @ManagedProperty(value = "#{workflowRuleDAO}")
    private WorkflowRuleDAO workflowRuleDAO;
    @ManagedProperty(value = "#{contactCaseWorkflowLogDAO}")
    private ContactCaseWorkflowLogDAO contactCaseWorkflowLogDAO;
    @ManagedProperty(value="#{purchaseOrderDAO}")
    private PurchaseOrderDAO purchaseOrderDAO;
    
    private String fileName;
    private String fullFileName;
    private String ext;
    private List<String[]> attachFiles = new ArrayList<String[]>();
    private String customerPath = JSFUtil.getuploadPath() + "customer/"; //JSFUtil.getRealPath() + "upload\\customer\\";
    private String tmpPath = JSFUtil.getuploadPath() + "temp/"; //JSFUtil.getRealPath() + "upload\\temp\\";
    private Integer relatedCaseId;
    private String relatedCaseName;
    private CustomerInfoValue customerInfoValue;

    private Double slaAcceptCaseHour;
    private Double slaCloseCaseHour;
    private Date slaDueDate;
    private Date slaDueDateActivity;
    private CaseDetail caseDetail;
    private ContactHistory contactHistory;
    private List<SaleHistoryInfoValue> saleHistoryList;
    private Map<Integer, Boolean> selectedPoIds = new LinkedHashMap<Integer, Boolean>();
    
    @PostConstruct
    public void initialize() {
        //initContactHistory();
        caseTypeId = null;
        caseTopicId = null;
        caseDetailId = null;
        caseRequestId = null;
        relationshipId = null;
        activityTypeId = null;
        contactCase = null;
        activity = null;
        activityType = null;
        userReceiverId = null;
        userReceiverName = null;
        serviceTypeId = null;
        locationId = null; 
        createActivity = true;
        hideActivity = false;
        saleHistoryList = null;
        JSFUtil.getUserSession().setRefNo(null);
        if(JSFUtil.getUserSession().getUserGroup().getRole().contains("CSCounter")) {
            channelId = 2;
            activityChannelId = 2;  //inbound:Phonecall
        } else {
            channelId = 6;//inbound:Walk-in
            activityChannelId = 6;
        }
        try {
            String selectId = getRequest("selectId");

            if (selectId == null) {
                if (!SecurityUtil.isPermitted("case:add")) {
                    SecurityUtil.redirectUnauthorize();  
                }
                mode = CrmConstant.ADD_MODE;
                setContactCase(new ContactCase());
                setActivity(new Activity());
                contactCase.setContactDate(new Date());
                activity.setActivityDate(new Date());
                //JSFUtil.getUserSession().showInboundContactSummary();
            }else{
                if (!SecurityUtil.isPermitted("case:edit")) {
                    SecurityUtil.redirectUnauthorize();
                }
            }
            
            
            caseTypes = caseTypeDAO.findCaseTypeStatus();
            caseTypeValue = setCaseTypeValue1();
            relationships = relationshipDAO.findRelationshipEntities();
            channels = channelDAO.findChannelEntities();
            activityTypes = activityTypeDAO.findActivityType();
            //serviceTypeList = fillServiceTypeList();
            serviceTypeList = serviceTypeDAO.findServiceTypeStatus();
            customerInfoValue = JSFUtil.getUserSession().getCustomerDetail();
            initSaleHistory(customerInfoValue.getId());
        } catch (Exception e) {
            System.out.println((new StringBuilder()).append("###################").append(e).toString());
        }
    }
    
    private void initSaleHistory(Integer customerId) {
        saleHistoryList = purchaseOrderDAO.findYesPurchaseOrderHistory(customerId);
    }
    
    private String getPoIdsFromCheckBox() {
        String poIds = "";
        if(selectedPoIds != null && !selectedPoIds.isEmpty()) {
            for (Map.Entry<Integer, Boolean> item : selectedPoIds.entrySet()) {
                if (item.getValue()) {
                    poIds += item.getKey().toString() + ",";
                }
            }
        }
        if(!poIds.isEmpty()){
            poIds = poIds.substring(0, poIds.length() - 1);
        }
        return poIds;
    }
    
    private List<ServiceType> fillServiceTypeList(){
        List<ServiceType> list = serviceTypeDAO.findServiceTypeEntities();
        List<ServiceType> listTemp = new ArrayList<ServiceType>();
        StringTokenizer st = new StringTokenizer(JSFUtil.getUserSession().getUserGroup().getAuthServiceType(),",");
        while (st.hasMoreTokens()) {
            Integer id = Integer.parseInt((String) st.nextToken());
            for(ServiceType s : list){
                if(s.getId().intValue() == id.intValue()){
                    listTemp.add(s);
                    break;
                }
            }
        }
        return listTemp;
    }
    
    private List<Location> fillLocationList(){
        List<Location> list = locationDAO.findLocationEntities();
        List<Location> listTemp = new ArrayList<Location>();
        StringTokenizer st = new StringTokenizer(JSFUtil.getUserSession().getUserGroup().getAuthLocation(),",");
        while (st.hasMoreTokens()) {
            Integer id = Integer.parseInt((String) st.nextToken());
            for(Location location : list){
                if(location.getId().intValue() == id.intValue()){
                    listTemp.add(location);
                    break;
                }
            }
        }
        return listTemp;
    }

    public String saveAction() {
        try {
            initContactHistory();
            Date dateNow = new Date();
            if ("add".equals(mode)) {
                JSFUtil.getUserSession().showInboundContactSummary();
                contactCase.setCaseDetailId(getCaseDetailDAO().findCaseDetail(caseDetailId));
                if (caseRequestId != null && caseRequestId != 0) {
                    contactCase.setCaseRequestId(caseRequestDAO.findCaseRequest(caseRequestId));
                }
                if (relationshipId != null && relationshipId != 0) {
                    contactCase.setRelationshipId(relationshipId);
                }
                //save related yes sale
                String relatedSale = getPoIdsFromCheckBox();
                if(!relatedSale.equals("")) {
                    contactCase.setRelatedSale(relatedSale);
                } else {
                    contactCase.setRelatedSale(null);
                }
//                if(selectedPoIds != null && !selectedPoIds.isEmpty()) {
//                    System.out.println(selectedPoIds);
//                    contactCase.setRelatedSale(getPoIdsFromCheckBox());
//                    System.out.println(contactCase.getRelatedSale());
//                }
                contactCase.setChannelId(getChannelDAO().findChannel(channelId));
                customerInfoValue = JSFUtil.getUserSession().getCustomerDetail();
                contactCase.setCustomerId(getCustomerDAO().findCustomer(customerInfoValue.getId()));
                contactCase.setCreateBy(getLoginUser().getName());
                contactCase.setCreateDate(dateNow);
                if(relatedCaseId != null && relatedCaseId != 0) {
                    contactCase.setContactCase(contactCaseDAO.findContactCase(relatedCaseId));
                }
                contactCase.setCaseAttachmentCollection(getCaseAttachments());
                contactCase.setSlaClose(slaCloseCaseHour);
                contactCase.setSlaCloseDate(slaDueDate);
                contactCase.setSlaAccept(slaAcceptCaseHour);
                if (contactCase.getCaseAttachmentCollection().size() > 0) {
                    contactCase.setAttachFile(true);
                } else {
                    contactCase.setAttachFile(false);
                }
                
                if(serviceTypeId != null && serviceTypeId != 0){
                    contactCase.setServiceType(serviceTypeDAO.findServiceType(serviceTypeId));
                }
                if(businessUnitId != null && businessUnitId != 0){
                    contactCase.setBusinessUnit(businessUnitDAO.findBusinessUnit(businessUnitId));
                }
                if(locationId != null && locationId != 0){
                    Location location = locationDAO.findLocation(locationId);
                    contactCase.setLocation(location);
                    contactCase.setLocationName(location.getName());
                }
                contactCase.setContactHistory(contactHistory);
                contactCase.setWorkflow(Boolean.FALSE);
                // None Workflow create activity manual
                if (createActivity && !hideActivity) {
                    activity.setChannelId(getChannelDAO().findChannel(activityChannelId));
                    activity.setActivityTypeId(getActivityTypeDAO().findActivityType(activityTypeId));
                    activity.setUserSenderId(getUsersDAO().findUsers(getLoginUser().getId()));
                    if(activity.getActivityTypeId().getTaskDelegate()){
                        if (userReceiverId != null && userReceiverId != 0) {
                            Users userAssign = getUsersDAO().findUsers(userReceiverId);
                            String userAssignName = userAssign.getName();
                            if(userAssign.getSurname() != null && !userAssign.getSurname().equals(""))
                                userAssignName += " " + userAssign.getSurname();
                            activity.setUserReceiverId(userAssign);
                            activity.setAssignTo(userAssignName);
                            activity.setUserReceiverEmail(userReceiverEmail);
                            activity.setUserReceiverTelephone(userReceiverPhone);
                        } else {
                            activity.setUserReceiverId(null);
                            activity.setUserReceiverEmail(null);
                            activity.setUserReceiverTelephone(null);
                        }
                    }
                    activity.setCreateBy(getLoginUser().getName());
                    activity.setCreateDate(dateNow);
                    activity.setContactCase(contactCase);
                    contactCase.setActivityCollection(new ArrayList());
                    contactCase.getActivityCollection().add(activity);
                    contactCase.setActivity(activity);  //all activity type
                    if(activity.getActivityTypeId().getTaskDelegate()) {    //only task delegate
                        contactCase.setActivityDelegate(activity);
                    }
                    contactCase.setWorkflow(Boolean.FALSE);
                } else {
                    if(workflowLogList != null && workflowLogList.size() > 0) {      // Have workflow create automatic activity             
                        //create Auto activity
                        activity.setChannelId(contactCase.getChannelId());
                        activity.setActivityTypeId(activityTypeDAO.findActivityTypeSystemDelegate()); // find activity type task delegate
                        activity.setUserSenderId(getUsersDAO().findUsers(getLoginUser().getId()));
                        activity.setActivityDate(dateNow);
                        activity.setSlaAcceptDate(holidayDAO.getSLADate(activity.getActivityDate(), slaAcceptCaseHour));
                        activity.setDescription("Automatic Workflow");
                        activity.setCreateBy(getLoginUser().getName());
                        activity.setCreateDate(dateNow);
                        activity.setContactCase(contactCase);
                        activity.setUserReceiverId(workflowLogList.get(0).getUsers());
                        activity.setAssignTo(workflowLogList.get(0).getUserName()+" "+workflowLogList.get(0).getUserSurname());
                        contactCase.setActivityCollection(new ArrayList());
                        contactCase.getActivityCollection().add(activity);
                        contactCase.setActivity(activity);
                        contactCase.setActivityDelegate(activity);
                        workflowLogList.get(0).setDueDate(activity.getSlaAcceptDate());  //get sla Accept Date of First Recipient
                        //set contact case workflow
                        contactCase.setWorkflow(Boolean.TRUE);
                        contactCase.setContactCaseWorkflowLogCollection(workflowLogList);
                        contactCase.setWorkflowSeqNo(1);
                    }
                }
                
                //Call Service to generate code
//                if (po.getRefNo() == null || po.getRefNo().isEmpty()) {
//                    boolean isGenRefNo = FormUtil.isRegProductRefNoGen(this.product);
//                    Integer seqNo = this.product.getSequenceNo().getId();
//                    if (isGenRefNo && seqNo != null) {
//                        String newRefNo = this.nextSequenceService.genRef(seqNo);
//                        po.setRefNo(newRefNo);
//                        po.setRefNo2(newRefNo);
//                    }
//                }
                
                getContactCaseDAO().create(contactCase);
                moveFile();
                JSFUtil.getUserSession().setContactCases(contactCase);//oat

                if(createActivity && activity.getActivityTypeId().getTaskDelegate() && !hideActivity){
                    sendMail("activity");
                } else if(workflowLogList != null && workflowLogList.size() > 0 && workflowLogList.get(0).getSentEmail()){
                    sendMail("workflow");
                }
            }
            return SUCCESS;
        } catch (Exception e) {
            System.out.println((new StringBuilder()).append("########################").append(e.getCause()).append("########").append(e.getMessage()).toString());
        }
        return FAIL;
    }

    private void sendMail(String type) {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMMMM yyyy HH:mm", Locale.US);
        String to = "";
        String cc = "";
        String bcc = "";
        if(type.equals("activity")) {
            to = activity.getUserReceiverEmail();
        } else if(type.equals("workflow")) {
            to = workflowLogList.get(0).getEmailTo();
            cc = workflowLogList.get(0).getEmailCc();
            bcc = workflowLogList.get(0).getEmailBcc();
        }
        
        if(!to.equals("") || !cc.equals("") || !bcc.equals("")){
            MailBean mail = new MailBean();
            //mail.setFrom("CSR_CRC@terrabit.co.th");

            int count = 0;
            String splitMail[] = new String[2];
            String email = "";//String email = JSFUtil.getUserSession().getUsers().getEmail();
            if (email==null || StringUtils.isEmpty(email)) {
                email = JSFUtil.getApplication().getAdminEmail();
                count = StringUtils.countMatches(email, ",");
                if(count>0) {
                    splitMail = email.split(",");
                }
            }
            
            String alias = null;
            try {
                if(count>0) {
                    alias = splitMail[0];
                } else {
                    alias = JSFUtil.getUserSession().getUsers().getUserGroup().getName();
                    alias += "-" + JSFUtil.getUserSession().getUserName();
                }
                
            } catch(Exception e) {
                alias = null;
            }
           
             String priorityText = "";
                try {
                    priorityText = " : "+contactCase.getPriority() + " priority";
                } catch(Exception e) {
                    priorityText = "";
                }

            if(count>0) {
                mail.setFrom(splitMail[1]);
            } else {
                mail.setFrom(email);
            }
            mail.setAlias(alias);
            mail.setTo(to);
            mail.setCc(cc);
            mail.setBcc(bcc);
            
            //mail.setSubject("Call Center Case Delegation ID:"+activity.getContactCase().getCode()+priorityText);
            mail.setSubject(activity.getActivityTypeId().getName() + " : " + contactCase.getPriority() + "" +
                    " : " +   contactCase.getCode() + " : " +StringUtils.trim(activity.getContactCase().getCaseDetailId().getCaseTopicId().getCaseTypeId().getName()) +"" +
                    " / "+ StringUtils.trim(activity.getContactCase().getCaseDetailId().getCaseTopicId().getName()) +"" +
                    " / " + StringUtils.trim(activity.getContactCase().getCaseDetailId().getName()));

            String str = "To Whom it may concern,<br/>"
                        + "Please handle this urgent case. Below is information. Should you have further queries<br/><br/>"
                        + "<table width='700' border=\"0\" cellspacing=\"0\" cellpadding=\"3\">"
                        + "<tr>"
                        + "   <td width='150' align='right' bgcolor='#76923C'><b>Case</b> : </td>"
                        + "   <td bgcolor='#76923C'>" + StringUtils.trim(activity.getContactCase().getCaseDetailId().getCaseTopicId().getCaseTypeId().getName()) + " / "
                        + "       " + StringUtils.trim(activity.getContactCase().getCaseDetailId().getCaseTopicId().getName()) + " / "
                        + "       " + StringUtils.trim(activity.getContactCase().getCaseDetailId().getName()) + "</td>"
                        + "   <td align='right' bgcolor='#76923C'><b>Case ID</b> : </td>"
                        + "   <td bgcolor='#76923C'>" + activity.getContactCase().getCode() + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "   <td align='right'><b>Contact Date</b> : </td>"
                        + "   <td colspan='3'>" + StringUtils.trim(sdf.format(activity.getContactCase().getContactDate())) + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "   <td align='right'><b>Customer Name</b> : </td>"
                        + "   <td colspan='3'>" + StringUtils.trim(activity.getContactCase().getCustomerId().getName())
                        + " " + StringUtils.trim(activity.getContactCase().getCustomerId().getSurname()) + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "   <td align='right'><b>Contact No.</b> : </td>"
                        + "   <td colspan='3'>" + StringUtils.trim(activity.getContactCase().getContactHistory() != null ? activity.getContactCase().getContactHistory().getContactTo() : "") + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "   <td align='right'><b>Service Type</b> : </td>"
                        + "   <td colspan='3'>" + StringUtils.trim(activity.getContactCase().getServiceType().getName()) + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "   <td align='right'><b>Location</b> : </td>"
                        + "   <td colspan='3'>" + StringUtils.trim(activity.getContactCase().getLocationName() != null ? activity.getContactCase().getLocationName() : "") + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "   <td align='right'><b>Description</b> : </td>"
                        + "   <td colspan='3'>" + StringUtils.trim(activity.getContactCase().getDescription()) + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "   <td align='right'><b>Remark</b> : </td>"
                        + "   <td colspan='3'>" + StringUtils.trim(activity.getContactCase().getRemark()) + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "   <td align='right'><b>Priority</b> : </td>"
                        + "   <td colspan='3'>" + StringUtils.trim(activity.getContactCase().getPriority()) + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "   <td align='right'><b>Status</b> : </td>"
                        + "   <td colspan='3'>" + StringUtils.trim(activity.getContactCase().getStatus()) + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "   <td align='right'><b>Channel</b> : </td>"
                        + "   <td colspan='3'>" + StringUtils.trim(activity.getContactCase().getChannelId().getType()) + ":" + StringUtils.trim(activity.getContactCase().getChannelId().getName()) + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "   <td align='right'><b>Due Date</b> : </td>"
                        + "   <td colspan='3'>" + StringUtils.trim(sdf.format(activity.getContactCase().getSlaCloseDate())) + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "   <td align='right'><b>Create Case By</b> : </td>"
                        + "   <td colspan='3'>" + StringUtils.trim(activity.getContactCase().getCreateBy()) + "</td>"
                        + "</tr>"
                        + "</table>"
                        + "<br/><br/>"
                        + "<table width='700' border=\"0\" cellspacing=\"0\" cellpadding=\"3\">"
                        + "<tr>"
                        + "   <td colspan='6' bgcolor='#C4BC96'><b>Activity</b></td>"
                        + "</tr>"
                        + "<tr>"
                        + "   <td width='150' align='right'><b>Activity Date</b> : </td>"
                        + "   <td>" + StringUtils.trim(sdf.format(activity.getActivityDate())) + "</td>"
                        + "   <td align='right'><b>Channel</b> : </td>"
                        + "   <td>" + StringUtils.trim(activity.getChannelId().getName())+ "</td>"
                        + "   <td align='right'><b>Activity Type</b> : </td>"
                        + "   <td>" + StringUtils.trim(activity.getActivityTypeId().getName()) + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "   <td align='right'><b>Description</b> : </td>"
                        + "   <td colspan='5'>" + StringUtils.trim(activity.getDescription()) + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "   <td align='right'><b>Create Activity By</b> : </td>"
                        + "   <td colspan='5'>" + StringUtils.trim(activity.getCreateBy()) + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "   <td align='right'></td>"
                        + "   <td colspan='5' align='right'><img src='http://www.terrabit.co.th/maxelyz_01.jpg'/></td>"
                        + "</tr>"
                        + "</table>"
                        + "";

            String text = str;
            mail.setContent(text);
            mail.sendMail();
        }
    }

    private ArrayList<CaseAttachment> getCaseAttachments() {
        ArrayList<CaseAttachment> aList = new ArrayList<CaseAttachment>();
        if (!attachFiles.isEmpty()) {
            for (String[] stra : attachFiles) {
                String fName = getRealFileName(stra[1]);
                File fTmp = new File(stra[1]);
                if (fTmp.exists()) {

                    CaseAttachment ca = new CaseAttachment();
                    ca.setCaseId(contactCase);
                    ca.setCreateBy(getLoginUser().getName());
                    ca.setCreateDate(new Date());
                    ca.setFilename(fName);

                    aList.add(ca);
                }
            }
        }
        return aList;
    }

    private void moveFile(){
        if (!attachFiles.isEmpty()) {
            String path = getCasePath();
            Boolean create = true;
            File fDir = new File(path);
            if (!fDir.exists()) {
                create = fDir.mkdir();
            }
            if (create) {
                for (String[] stra : attachFiles) {
                    String fName = getRealFileName(stra[1]);
                    File fTmp = new File(stra[1]);
                    if (fTmp.exists()) {
                        File fCust = new File(path + fName);
                        fTmp.renameTo(fCust);
                    }
                }
            }
        }
    }

    private String getCasePath(){
        String path = "";
        File fDir = new File(customerPath + contactCase.getCustomerId().getId());
        if (!fDir.exists()) {
            fDir.mkdir();
        }
        fDir = new File(customerPath + contactCase.getCustomerId().getId() + "/case");
        if (!fDir.exists()) {
            fDir.mkdir();
        }
        fDir = new File(customerPath + contactCase.getCustomerId().getId() + "/case/" + contactCase.getId());
        if (!fDir.exists()) {
            fDir.mkdir();
        }
        path = customerPath + contactCase.getCustomerId().getId() + "/case/" + contactCase.getId() + "/";
        return path;
    }

    public void selectCaseListener(ActionEvent event) {
        relatedCaseName = JSFUtil.getRequestParameterMap("relatedCaseName");
        String tmpId = JSFUtil.getRequestParameterMap("relatedCaseId");
        if (!tmpId.isEmpty()) {
            relatedCaseId = Integer.parseInt(tmpId);
        } else {
            relatedCaseId = null;
        }
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void clearCaseListener(ActionEvent event) {
        relatedCaseId = null;
        relatedCaseName = null;
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void selectUserListener() {
        userReceiverName = JSFUtil.getRequestParameterMap("userReceiverName");
        String tmpId = JSFUtil.getRequestParameterMap("userReceiverId");
        if (!tmpId.isEmpty()) {
            userReceiverId = Integer.parseInt(tmpId);
            Users userTmp = usersDAO.findUsers(userReceiverId);
            userReceiverEmail = userTmp.getEmail();
            userReceiverPhone = userTmp.getMobile();
        } else {
            userReceiverId = null;
        }
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void clearUserListener(ActionEvent event) {
        userReceiverId = null;
        userReceiverName = null;
        userReceiverEmail = null;
        userReceiverPhone = null;
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void caseTypeListener(ValueChangeEvent event) {
        if(workflowLogList != null || workflowLogList.size() > 0)
            workflowLogList.clear();
        
        if(caseTopics != null) {
            caseTopics.clear();
            setCaseTopicId(0);
        }
        if(caseDetails != null) {
            caseDetails.clear();
            setCaseDetailId(0);
        }
        if(caseRequests != null) {
            caseRequests.clear();
            setCaseRequestId(0);
        }
        Integer id = (Integer) event.getNewValue();
        setCaseTypeId(id);
        setCaseTopicList(id);
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void caseTopicListener() {
        if(workflowLogList != null || workflowLogList.size() > 0)
            workflowLogList.clear();
        
        if(caseDetails != null) {
            caseDetails.clear();
            setCaseDetailId(0);
        }
        if(caseRequests != null) {
            caseRequests.clear();
            setCaseRequestId(0);
        }
        setCaseDetailList(caseTopicId);
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void caseDetailListener() {
        if(workflowLogList != null || workflowLogList.size() > 0)
            workflowLogList.clear();
        
        if(caseRequests != null) {
            caseRequests.clear();
            setCaseRequestId(0);
        }
        setCaseRequestList(caseDetailId);
        caseDetail = caseDetailDAO.findCaseDetail(caseDetailId);
        contactCase.setPriority(caseDetail.getDefaultPriority());
        try {
            userReceiverId = caseDetail.getToUsers().getId();
        } catch(Exception e) {
            userReceiverId = null;
        }
       
        userReceiverName = caseDetail.getToUserName();
        userReceiverEmail = caseDetail.getToUserEmail();
        userReceiverPhone = caseDetail.getToUserTelephone();

        setSLA(contactCase.getPriority());
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void priorityListener() {
        if(caseDetail != null){
            setSLA(contactCase.getPriority());
            initWorkflowRule(caseRequestId, serviceTypeId, businessUnitId, locationId, contactCase.getPriority());
        }else{
            slaAcceptCaseHour = null;
            slaCloseCaseHour = null;
            slaDueDate = null;
            slaDueDateActivity = null;
            activity.setSlaAcceptDate(slaDueDateActivity);
        }        
    }

    private void setSLA(String priority){
        if(priority.equals("low")){
            slaAcceptCaseHour = caseDetail.getSlaAcceptcaseLow();
            slaCloseCaseHour = caseDetail.getSlaClosecaseLow();
        }else if(priority.equals("medium")){
            slaAcceptCaseHour = caseDetail.getSlaAcceptcaseMedium();
            slaCloseCaseHour = caseDetail.getSlaClosecaseMedium();
        }else if(priority.equals("high")){
            slaAcceptCaseHour = caseDetail.getSlaAcceptcaseHigh();
            slaCloseCaseHour = caseDetail.getSlaClosecaseHigh();
        }else if(priority.equals("immediate")){
            slaAcceptCaseHour = caseDetail.getSlaAcceptcaseImmediate();
            slaCloseCaseHour = caseDetail.getSlaClosecaseImmediate();
        }
        if(contactCase != null && contactCase.getContactDate() != null){
            slaDueDate = holidayDAO.getSLADate(contactCase.getContactDate(), slaCloseCaseHour);
        }
        if(activity != null && activity.getActivityDate() != null){
            slaDueDateActivity = holidayDAO.getSLADate(activity.getActivityDate(), slaAcceptCaseHour);
        }
        activity.setSlaAcceptDate(slaDueDateActivity);
    }

    public Map<String, String> setPriorityList() {
        Map<String, String> values = new LinkedHashMap<String, String>();
        String[] stra1 = {"Please Select", "Low", "Medium", "High", "Immediate"};
        String[] stra2 = {"" , "low", "medium", "high", "immediate"};
        for(Integer i = 0; i < stra1.length; i++){
            values.put(stra1[i], stra2[i]);
        }
        return values;
    }

    private Map<String, Integer> setCaseTypeValue1(){
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        String str = "";
        String caseTypeAllow = JSFUtil.getUserSession().getUsers().getAllowCaseType();
        if(!caseTypes.isEmpty()){
            for(CaseType ct : caseTypes){
                if(caseTypeAllow != null && !caseTypeAllow.isEmpty()){
                    str = ct.getId().toString();
                    if(caseTypeAllow.indexOf(str) != -1){
                        values.put(ct.getName(), ct.getId());
                    }
                } else {
                    values.put(ct.getName(), ct.getId());
                }
            }
        }
        return values;
    }

    public void contactDateChangeListener(ValueChangeEvent event){
        try {
            Date date = (Date) event.getNewValue();
            if(date != null && slaCloseCaseHour != null){
                contactCase.setContactDate(date);
                slaDueDate = holidayDAO.getSLADate(contactCase.getContactDate(), slaCloseCaseHour);
            }
        } catch(Exception e) {
            log.error(e);
        }
    }
    
    public void caseRequestListener(ValueChangeEvent event) {       
        Integer id = (Integer) event.getNewValue();
        setCaseRequestId(id);
        initWorkflowRule(caseRequestId, serviceTypeId, businessUnitId, locationId, contactCase.getPriority());
    }
    
    public void serviceTypeListener(ValueChangeEvent event){
        try {
            businessUnitId = null;
            locationId = null;
            if(businessUnitList != null || !businessUnitList.isEmpty())
                businessUnitList.clear();
            if(locationList != null || !locationList.isEmpty())
                locationList.clear();
            if(workflowLogList != null || workflowLogList.size() > 0)
                workflowLogList.clear();
            serviceTypeId = (Integer) event.getNewValue();
            if(serviceTypeId != null && serviceTypeId != 0){
                businessUnitList = businessUnitDAO.findBusinessUnitByServiceTypeId(serviceTypeId);
                if(businessUnitList != null && businessUnitList.size() == 1) {
                    businessUnitId = businessUnitList.get(0).getId();
                    locationList = locationDAO.findLocationByBusinessUnitId(businessUnitId);
                    if(locationList != null && locationList.size() == 1) {
                        locationId = locationList.get(0).getId();
                        setLocationId(locationId);
                        initWorkflowRule(caseRequestId, serviceTypeId, businessUnitId, locationId, contactCase.getPriority());
                    } else {
                        setLocationId(0);
                    }
                } else {
                    setBusinessUnitId(0);
                    if(locationList != null && !locationList.isEmpty())
                        locationList.clear();
                    locationId = null;
                }
            }
//            initWorkflowRule(caseRequestId, id, businessUnitId, locationId, contactCase.getPriority());
        } catch(Exception e) {
            log.error(e);
        }
    }
        
    public void businessUnitListener(ValueChangeEvent event){
        try {
            locationId = null;
            if(locationList != null && !locationList.isEmpty())
                locationList.clear();
            if(workflowLogList != null || workflowLogList.size() > 0)
                workflowLogList.clear();
            businessUnitId = (Integer) event.getNewValue();
            if(businessUnitId != null && businessUnitId != 0){
                locationList = locationDAO.findLocationByBusinessUnitId(businessUnitId);
                if(locationList != null && locationList.size() == 1) {
                    locationId = locationList.get(0).getId();
                    setLocationId(locationId);
                    initWorkflowRule(caseRequestId, serviceTypeId, businessUnitId, locationId, contactCase.getPriority());
                } else {
                    setLocationId(0);
                }
            }   
        } catch(Exception e) {
            log.error(e);
        }
    }
    
    public void locationListener(ValueChangeEvent event) {       
        Integer id = (Integer) event.getNewValue();
        setLocationId(id);
        initWorkflowRule(caseRequestId, serviceTypeId, businessUnitId, locationId, contactCase.getPriority());
    }
    
    private void initContactHistory(){
        if(JSFUtil.getUserSession().getContactHistory() == null || JSFUtil.getUserSession().getContactHistory().getId() == null){
            contactHistory = new ContactHistory();
            contactHistory.setContactDate(new Date());
            if (JSFUtil.getUserSession().isInboundCall()) {
                contactHistory.setChannel(new Channel(2)); //inbound:phonecall
            } else {
                contactHistory.setChannel(new Channel(9)); //outbound:phonecall
            }
            contactHistory.setContactStatus("c");//close
            contactHistory.setCallSuccess(true);
            contactHistory.setDmccontact(true);
            contactHistory.setFollowupsale(false);
            contactHistory.setContactClose(false);
            contactHistory.setCreateDate(new Date());
            contactHistory.setUsers(JSFUtil.getUserSession().getUsers());
            contactHistory.setCreateBy(JSFUtil.getUserSession().getUserName());
            if (JSFUtil.getUserSession().getCustomerDetail() != null) {
                contactHistory.setCustomer(customerDAO.findCustomer(JSFUtil.getUserSession().getCustomerDetail().getId()));
            }
            contactHistory.setContactTo(JSFUtil.getUserSession().getContactTo());
            contactHistory.setTelephonyTrackId(JSFUtil.getUserSession().getTelephonyTrackId());

            contactHistoryDAO.create(contactHistory);

            JSFUtil.getUserSession().setContactHistory(contactHistory);
        }else{
            contactHistory = JSFUtil.getUserSession().getContactHistory();
        }
    }    

    private void initWorkflowRule(Integer requestId, Integer serviceTypeId, Integer businessUnitId, Integer locationId, String priority) {
        List<WorkflowRuleDetail> workflowRuleList = new ArrayList<WorkflowRuleDetail>();
        if(workflowRuleList != null || !workflowRuleList.isEmpty())
            workflowRuleList.clear();
        if(priority == null || priority.isEmpty())
            priority = contactCase.getPriority();
        
        try {
            workflowRuleList = this.getWorkflowRuleDAO().findWorkflowCaseRule(requestId, serviceTypeId, businessUnitId, locationId, priority);
        } catch (Exception e) {
            workflowRuleList = null;
        }
        
        if(workflowLogList != null || !workflowLogList.isEmpty())
            workflowLogList.clear();
        if(workflowRuleList != null) {
            for(WorkflowRuleDetail obj : workflowRuleList) {
                ContactCaseWorkflowLog cwf = new ContactCaseWorkflowLog();
                cwf.setAllowCloseCase(obj.getAllowCloseCase());
                cwf.setContactCase(contactCase);
                cwf.setEmailBcc(obj.getEmailBcc());
                cwf.setEmailCc(obj.getEmailCc());
                cwf.setEmailTo(obj.getEmailTo());
                cwf.setSentEmail(obj.getSentEmail());
                cwf.setUserName(obj.getUser().getName());
                cwf.setUserSurname(obj.getUser().getSurname());
                cwf.setUsers(obj.getUser());
                cwf.setWorkflowRule(obj.getWorkflowRule());
                cwf.setSeqNo(obj.getSeqNo());
                workflowLogList.add(cwf);
            }
        } 
        if(workflowLogList.size() > 0) {
            hideActivity = true;
        }else {
            hideActivity = false;
        }
    }
    
    private void setCaseTopicList(Integer caseTypeId) {
        caseTopics = caseTopicDAO.findCaseTopicByCaseTypeStatus(caseTypeId);
        if(caseTopics != null && caseTopics.size() == 1) {
            caseTopicId = caseTopics.get(0).getId();
            setCaseDetailList(caseTopicId);
        } else {            
            setCaseTopicId(Integer.valueOf(0));
        }
    }

    private void setCaseDetailList(Integer caseTopicId) {
        caseDetails = caseDetailDAO.findAvailableCaseDetailByCaseTopic(caseTopicId);
        if(caseDetails != null && caseDetails.size() == 1) {
            caseDetailId = caseDetails.get(0).getId();
            if(workflowLogList != null || workflowLogList.size() > 0)
                workflowLogList.clear();
            caseDetail = caseDetailDAO.findCaseDetail(caseDetailId);
            contactCase.setPriority(caseDetail.getDefaultPriority());
            setCaseRequestList(caseDetailId);
            try {
                userReceiverId = caseDetail.getToUsers().getId();
            } catch(Exception e) {
                userReceiverId = null;
            }
            userReceiverName = caseDetail.getToUserName();
            userReceiverEmail = caseDetail.getToUserEmail();
            userReceiverPhone = caseDetail.getToUserTelephone();
            setSLA(contactCase.getPriority());
        } else {
            setCaseRequestList(Integer.valueOf(0));
        }
    }

    private void setCaseRequestList(Integer caseDetailId) {
        caseRequests = caseRequestDAO.findCaseRequestByCaseDetailStatus(caseDetailId);
        if(caseRequests != null && caseRequests.size() == 1) {
            caseRequestId = caseRequests.get(0).getId();
            setCaseRequestId(caseRequestId);
            initWorkflowRule(caseRequestId, serviceTypeId, businessUnitId, locationId, contactCase.getPriority());
        }
    }

    public CaseDetailDAO getCaseDetailDAO() {
        return caseDetailDAO;
    }

    public void setCaseDetailDAO(CaseDetailDAO caseDetailDAO) {
        this.caseDetailDAO = caseDetailDAO;
    }

    public Integer getCaseDetailId() {
        return caseDetailId;
    }

    public void setCaseDetailId(Integer caseDetailId) {
        this.caseDetailId = caseDetailId;
    }

    public CaseRequestDAO getCaseRequestDAO() {
        return caseRequestDAO;
    }

    public void setCaseRequestDAO(CaseRequestDAO caseRequestDAO) {
        this.caseRequestDAO = caseRequestDAO;
    }

    public Integer getCaseRequestId() {
        return caseRequestId;
    }

    public void setCaseRequestId(Integer caseRequestId) {
        this.caseRequestId = caseRequestId;
    }

    public CaseTopicDAO getCaseTopicDAO() {
        return caseTopicDAO;
    }

    public void setCaseTopicDAO(CaseTopicDAO caseTopicDAO) {
        this.caseTopicDAO = caseTopicDAO;
    }

    public Integer getCaseTopicId() {
        return caseTopicId;
    }

    public void setCaseTopicId(Integer caseTopicId) {
        this.caseTopicId = caseTopicId;
    }

    public List<CaseTopic> getCaseTopics() {
        return caseTopics;
    }

    public void setCaseTopics(List<CaseTopic> caseTopics) {
        this.caseTopics = caseTopics;
    }

    public CaseTypeDAO getCaseTypeDAO() {
        return caseTypeDAO;
    }

    public void setCaseTypeDAO(CaseTypeDAO caseTypeDAO) {
        this.caseTypeDAO = caseTypeDAO;
    }

    public Integer getCaseTypeId() {
        return caseTypeId;
    }

    public void setCaseTypeId(Integer caseTypeId) {
        this.caseTypeId = caseTypeId;
    }

    public List<CaseType> getCaseTypes() {
        return caseTypes;
    }

    public void setCaseTypes(List<CaseType> caseTypes) {
        this.caseTypes = caseTypes;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public List<CaseDetail> getCaseDetails() {
        return caseDetails;
    }

    public void setCaseDetails(List<CaseDetail> caseDetails) {
        this.caseDetails = caseDetails;
    }

    public List<CaseRequest> getCaseRequests() {
        return caseRequests;
    }

    public void setCaseRequests(List<CaseRequest> caseRequests) {
        this.caseRequests = caseRequests;
    }

    public ContactCase getContactCase() {
        return contactCase;
    }

    public void setContactCase(ContactCase contactCase) {
        this.contactCase = contactCase;
    }

    public Boolean getCreateActivity() {
        return createActivity;
    }

    public void setCreateActivity(Boolean createActivity) {
        this.createActivity = createActivity;
    }

    public Boolean getHideActivity() {
        return hideActivity;
    }

    public void setHideActivity(Boolean hideActivity) {
        this.hideActivity = hideActivity;
    }

    public ContactCaseDAO getContactCaseDAO() {
        return contactCaseDAO;
    }

    public void setContactCaseDAO(ContactCaseDAO contactCaseDAO) {
        this.contactCaseDAO = contactCaseDAO;
    }

    public CustomerDAO getCustomerDAO() {
        return customerDAO;
    }

    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public RelationshipDAO getRelationshipDAO() {
        return relationshipDAO;
    }

    public void setRelationshipDAO(RelationshipDAO relationshipDAO) {
        this.relationshipDAO = relationshipDAO;
    }

    public Integer getRelationshipId() {
        return relationshipId;
    }

    public void setRelationshipId(Integer relationshipId) {
        this.relationshipId = relationshipId;
    }

    public List<Relationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<Relationship> relationships) {
        this.relationships = relationships;
    }

    public ChannelDAO getChannelDAO() {
        return channelDAO;
    }

    public void setChannelDAO(ChannelDAO channelDAO) {
        this.channelDAO = channelDAO;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public Integer getActivityChannelId() {
        return activityChannelId;
    }

    public void setActivityChannelId(Integer activityChannelId) {
        this.activityChannelId = activityChannelId;
    }

    public Integer getActivityTypeId() {
        return activityTypeId;
    }

    public void setActivityTypeId(Integer activityTypeId) {
        this.activityTypeId = activityTypeId;
        activityType = activityTypeDAO.findActivityType(activityTypeId);
    }

    public ActivityTypeDAO getActivityTypeDAO() {
        return activityTypeDAO;
    }

    public void setActivityTypeDAO(ActivityTypeDAO activityTypeDAO) {
        this.activityTypeDAO = activityTypeDAO;
    }

    public List<ActivityType> getActivityTypes() {
        return activityTypes;
    }

    public void setActivityTypes(List<ActivityType> activityTypes) {
        this.activityTypes = activityTypes;
    }

    public UsersDAO getUsersDAO() {
        return usersDAO;
    }

    public void setUsersDAO(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public Integer getUserReceiverId() {
        return userReceiverId;
    }

    public void setUserReceiverId(Integer userReceiverId) {
        this.userReceiverId = userReceiverId;
    }

    public String getUserReceiverName() {
        return userReceiverName;
    }

    public void setUserReceiverName(String userReceiverName) {
        this.userReceiverName = userReceiverName;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String strFileName) {
        String str = "";
        int extDot = strFileName.lastIndexOf('.');
        if (extDot > 0) {
            str = strFileName.substring(extDot + 1);
        }
        this.ext = str;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFullFileName() {
        return fullFileName;
    }

    public void setFullFileName(String fullFileName) {
        this.fullFileName = fullFileName;
    }

    public List getAttachFiles() {
        return attachFiles;
    }

    public void setAttachFiles(List attachFiles) {
        this.attachFiles = attachFiles;
    }

    public Integer getRelatedCaseId() {
        return relatedCaseId;
    }

    public void setRelatedCaseId(Integer relatedCaseId) {
        this.relatedCaseId = relatedCaseId;
    }

    public String getRelatedCaseName() {
        return relatedCaseName;
    }

    public void setRelatedCaseName(String relatedCaseName) {
        this.relatedCaseName = relatedCaseName;
    }

    public CustomerInfoValue getCustomerInfoValue() {
        return customerInfoValue;
    }

    public void setCustomerInfoValue(CustomerInfoValue customerInfoValue) {
        this.customerInfoValue = customerInfoValue;
    }
    
    public void uploadListener(FileUploadEvent event) throws Exception {
        String[] stra = new String[3];
        UploadedFile item = event.getUploadedFile();
        String str = item.getName();
        str = getRealFileName(str);
        stra[0] = str;//file name
        str = JSFUtil.getDateFormat1(new Date()) + "_" + str;
        stra[2] = str;
        str = tmpPath + str;
        stra[1] = str;//full path file name
        File file = new File(str);
        
        if(saveToFile(item.getInputStream(),file)) {  
            attachFiles.add(stra);
        }
    }
    
    private boolean saveToFile(InputStream inputStream, File file) {    
        FileOutputStream fos = null;
        BufferedInputStream bis = null;   
        boolean result = false;        
        try { 
            byte[] buffer = new byte[1024];
            fos = new FileOutputStream(file);            
            bis = new BufferedInputStream(inputStream, buffer.length);            
            int numRead = -1;            
            while ((numRead = bis.read(buffer, 0, buffer.length)) != -1) {                
                fos.write(buffer, 0, numRead);
            }          
            result = true;        
        } catch (IOException ex) {            
            log.error(ex, ex);
            log.error("Exception during download file " + file.getAbsolutePath());        
        } finally {            
            try {                
                fos.close();
            } catch (IOException ex) {                
                log.error(ex, ex);
                log.error("Exception during closing file output stream " + file.getAbsolutePath());            
            }            
            try {                
                bis.close();
            } catch (IOException ex) {                
                log.error(ex, ex);
                log.error("Exception during closing buffered input stream " + file.getAbsolutePath());            
            }   
        }    
        return result; 
    }
    
    public String getRealFileName(String str) {
        //return StringUtils.substring(str, (str.lastIndexOf("/")) + 1, str.length()); //oat 27 aug 12
        String str1 = str;
        if(str.lastIndexOf("/") != -1){
            str1 = StringUtils.substring(str, (str.lastIndexOf("/")) + 1, str.length());
        }else if(str.lastIndexOf("\\") != -1){
            str1 = StringUtils.substring(str, (str.lastIndexOf("\\")) + 1, str.length());
        }
        return str1;
    }

    public void removeFileTemp(ActionEvent event) {
        String rmFile = StringUtils.trim(getRequest("rmFile"));
        File f = new File(rmFile);
        if (f.exists()) {
            f.delete();
        }
        List<String[]> list = new ArrayList<String[]>();
        for (String[] s : attachFiles) {
            if (!StringUtils.equals(rmFile, StringUtils.trim(s[1]))) {
                list.add(s);
            }
        }
        attachFiles = list;
    }

    public static void main(String[] stra) {
        String str = "D:/test/open.png";
        str = StringUtils.substring(str, (str.lastIndexOf("/")) + 1, str.length());
    }


    public Date getSlaDueDate() {
        return slaDueDate;
    }

    public void setSlaDueDate(Date slaDueDate) {
        this.slaDueDate = slaDueDate;
    }

    public CaseDetail getCaseDetail() {
        return caseDetail;
    }

    public void setCaseDetail(CaseDetail caseDetail) {
        this.caseDetail = caseDetail;
    }

    public Double getSlaAcceptCaseHour() {
        return slaAcceptCaseHour;
    }

    public void setSlaAcceptCaseHour(Double slaAcceptCaseHour) {
        this.slaAcceptCaseHour = slaAcceptCaseHour;
    }

    public Double getSlaCloseCaseHour() {
        return slaCloseCaseHour;
    }

    public void setSlaCloseCaseHour(Double slaCloseCaseHour) {
        this.slaCloseCaseHour = slaCloseCaseHour;
    }

    public HolidayDAO getHolidayDAO() {
        return holidayDAO;
    }

    public void setHolidayDAO(HolidayDAO holidayDAO) {
        this.holidayDAO = holidayDAO;
    }

    public Date getSlaDueDateActivity() {
        return slaDueDateActivity;
    }

    public void setSlaDueDateActivity(Date slaDueDateActivity) {
        this.slaDueDateActivity = slaDueDateActivity;
    }

    public String getUserReceiverEmail() {
        return userReceiverEmail;
    }

    public void setUserReceiverEmail(String userReceiverEmail) {
        this.userReceiverEmail = userReceiverEmail;
    }

    public String getUserReceiverPhone() {
        return userReceiverPhone;
    }

    public void setUserReceiverPhone(String userReceiverPhone) {
        this.userReceiverPhone = userReceiverPhone;
    }

    public Map<String, Integer> getCaseTypeValue() {
        return caseTypeValue;
    }

    public void setCaseTypeValue(Map<String, Integer> caseTypeValue) {
        this.caseTypeValue = caseTypeValue;
    }

    public List<Location> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<Location> locationList) {
        this.locationList = locationList;
    }

    public List<ServiceType> getServiceTypeList() {
        return serviceTypeList;
    }

    public void setServiceTypeList(List<ServiceType> serviceTypeList) {
        this.serviceTypeList = serviceTypeList;
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

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Integer serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public BusinessUnitDAO getBusinessUnitDAO() {
        return businessUnitDAO;
    }

    public void setBusinessUnitDAO(BusinessUnitDAO businessUnitDAO) {
        this.businessUnitDAO = businessUnitDAO;
    }

    public List<BusinessUnit> getBusinessUnitList() {
        return businessUnitList;
    }

    public void setBusinessUnitList(List<BusinessUnit> businessUnitList) {
        this.businessUnitList = businessUnitList;
    }

    public Integer getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(Integer businessUnitId) {
        this.businessUnitId = businessUnitId;
    }

    public ContactHistory getContactHistory() {
        return contactHistory;
    }

    public void setContactHistory(ContactHistory contactHistory) {
        this.contactHistory = contactHistory;
    }

    public ContactHistoryDAO getContactHistoryDAO() {
        return contactHistoryDAO;
    }

    public void setContactHistoryDAO(ContactHistoryDAO contactHistoryDAO) {
        this.contactHistoryDAO = contactHistoryDAO;
    }

    public ContactCaseWorkflowLogDAO getContactCaseWorkflowLogDAO() {
        return contactCaseWorkflowLogDAO;
    }

    public void setContactCaseWorkflowLogDAO(ContactCaseWorkflowLogDAO contactCaseWorkflowLogDAO) {
        this.contactCaseWorkflowLogDAO = contactCaseWorkflowLogDAO;
    }

    public WorkflowRuleDAO getWorkflowRuleDAO() {
        return workflowRuleDAO;
    }

    public void setWorkflowRuleDAO(WorkflowRuleDAO workflowRuleDAO) {
        this.workflowRuleDAO = workflowRuleDAO;
    }

    public List<ContactCaseWorkflowLog> getWorkflowLogList() {
        return workflowLogList;
    }

    public void setWorkflowLogList(List<ContactCaseWorkflowLog> workflowLogList) {
        this.workflowLogList = workflowLogList;
    }

    public PurchaseOrderDAO getPurchaseOrderDAO() {
        return purchaseOrderDAO;
    }

    public void setPurchaseOrderDAO(PurchaseOrderDAO purchaseOrderDAO) {
        this.purchaseOrderDAO = purchaseOrderDAO;
    }

    public List<SaleHistoryInfoValue> getSaleHistoryList() {
        return saleHistoryList;
    }

    public void setSaleHistoryList(List<SaleHistoryInfoValue> saleHistoryList) {
        this.saleHistoryList = saleHistoryList;
    }

    public Map<Integer, Boolean> getSelectedPoIds() {
        return selectedPoIds;
    }

    public void setSelectedPoIds(Map<Integer, Boolean> selectedPoIds) {
        this.selectedPoIds = selectedPoIds;
    }

}
