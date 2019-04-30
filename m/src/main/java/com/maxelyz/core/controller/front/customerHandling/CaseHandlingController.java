// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   CaseHandlingEditController.java
package com.maxelyz.core.controller.front.customerHandling;

import com.maxelyz.core.constant.CrmConstant;
import com.maxelyz.core.controller.common.BaseController;
import com.maxelyz.core.model.dao.*;
import com.maxelyz.core.model.dao.front.customerHandling.CustomerHandlingDAO;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.core.model.value.front.customerHandling.ActivityInfoValue;
import com.maxelyz.core.model.value.front.customerHandling.CustomerInfoValue;
import com.maxelyz.core.model.value.front.customerHandling.SaleHistoryInfoValue;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.bean.ViewScoped;
import org.apache.commons.lang3.StringUtils;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@ManagedBean
//@RequestScoped
@ViewScoped
public class CaseHandlingController extends BaseController {

    private static String SUCCESS = "customerDetail.jsf?faces-redirect=true";
    private static String EDIT = "/front/customerHandling/caseHandling.xhtml";
    private static String HISTORY = "/front/customerHandling/caseHandling.xhtml";
    private static String FAIL = "caseHandling.jsf";
    private static String ACTIVITY = "activityEdit.xhtml";
    private List<CaseType> caseTypeList = new ArrayList<CaseType>();
    private List<Channel> channelList = new ArrayList<Channel>();
    private List<Relationship> relationshipList = new ArrayList<Relationship>();
    private List caseTopics = new ArrayList();
    private List caseDetails = new ArrayList();
    private List caseRequests = new ArrayList();
    private List<ServiceType> serviceTypeList = new ArrayList<ServiceType>();
    private List<BusinessUnit> businessUnitList = new ArrayList<BusinessUnit>();
    private List<Location> locationList = new ArrayList<Location>();
    private Integer serviceTypeId;
    private Integer businessUnitId;
    private Integer locationId;
    private Integer caseTypeId;
    private Integer caseTopicId;
    private Integer caseDetailId;
    private Integer reasonRequestId;
    private Integer caseChannelId;
    private Integer priority;
    private Integer relationshipId;
    private String mode;
    private ContactCase contactCase;
    private CustomerInfoValue customerInfoValue;
    private List<ActivityInfoValue> activityList;
    @ManagedProperty(value = "#{caseTypeDAO}")
    private CaseTypeDAO caseTypeDAO;
    @ManagedProperty(value = "#{caseTopicDAO}")
    private CaseTopicDAO caseTopicDAO;
    @ManagedProperty(value = "#{caseDetailDAO}")
    private CaseDetailDAO caseDetailDAO;
    @ManagedProperty(value = "#{caseRequestDAO}")
    private CaseRequestDAO caseRequestDAO;
    @ManagedProperty(value = "#{channelDAO}")
    private ChannelDAO channelDAO;
    @ManagedProperty(value = "#{relationshipDAO}")
    private RelationshipDAO relationshipDAO;
    @ManagedProperty(value = "#{contactCaseDAO}")
    private ContactCaseDAO contactCaseDAO;
    @ManagedProperty(value = "#{activityDAO}")
    private ActivityDAO activityDAO;
    @ManagedProperty(value = "#{customerHandlingDAO}")
    private CustomerHandlingDAO customerHandlingDAO;
    @ManagedProperty(value = "#{caseAttachmentDAO}")
    private CaseAttachmentDAO caseAttachmentDAO;
    @ManagedProperty(value = "#{holidayDAO}")
    private HolidayDAO holidayDAO;
    @ManagedProperty(value = "#{serviceTypeDAO}")
    private ServiceTypeDAO serviceTypeDAO;
    @ManagedProperty(value = "#{businessUnitDAO}")
    private BusinessUnitDAO businessUnitDAO;
    @ManagedProperty(value = "#{locationDAO}")
    private LocationDAO locationDAO;
    @ManagedProperty(value = "#{userGroupDAO}")
    private UserGroupDAO userGroupDAO;
    @ManagedProperty(value="#{contactCaseWorkflowLogDAO}")
    private ContactCaseWorkflowLogDAO contactCaseWorkflowLogDAO;
    @ManagedProperty(value="#{purchaseOrderDAO}")
    private PurchaseOrderDAO purchaseOrderDAO;
    
    private Integer contactCaseId;
    private Collection<CaseAttachment> attachFiles = new ArrayList<CaseAttachment>();
    private List<String[]> attachFilesTemp = new ArrayList<String[]>();
    private List<String> attachIdsTemp = new ArrayList<String>();
    private String customerPath = JSFUtil.getuploadPath()+"customer/"; //JSFUtil.getRealPath()+"upload\\customer\\";
    private String tmpPath = JSFUtil.getuploadPath()+"temp/"; //JSFUtil.getRealPath()+"upload\\temp\\";

    private Integer relatedCaseId;
    private String relatedCaseName;
    
    private CaseDetail caseDetail;    
    
    private List<ContactCaseWorkflowLog> contactCaseWorkflowLogList;
    private ContactCaseWorkflowLog contactCaseWorkflowLog;
    private ContactCaseWorkflowLog nextContactCaseWorkflowLog;
    private Boolean allowClosedCase=true;
    private Boolean allowCreateActivity=true;
    private Boolean cancelWorkflow=false;
    private Boolean disableSave=false;
            
    private String from="";
    
    private boolean bCounterEdit = SecurityUtil.isPermitted("case:edit:counteredit");
    private List<UserGroupLocation> userGroupLocationList = JSFUtil.getUserSession().getUserGroupLocationList();
    private boolean bSaveButton = false;
    private List<SaleHistoryInfoValue> saleHistoryList;
    private Map<Integer, Boolean> selectedPoIds = new LinkedHashMap<Integer, Boolean>();
    
    @PostConstruct
    public void initialize() {
        try {
            
            if (!SecurityUtil.isPermitted("customerhandling:view")) {
                SecurityUtil.redirectUnauthorize();  
            }
            setCaseTypeList(getCaseTypeDAO().findCaseTypeEntities());
            setChannelList(getChannelDAO().findChannelEntities());
            setRelationshipList(getRelationshipDAO().findRelationshipEntities());

            contactCase = new ContactCase();

            if(JSFUtil.getRequestParameterMap("from") != null) {
                from = JSFUtil.getRequestParameterMap("from");
            } else {
                from = getRequest("from");
            }
            if (from != null && (from.equals("activity") || from.equals("caseHistory"))){
                editCase();
            }
            
            //serviceTypeList = serviceTypeDAO.findServiceTypeEntities();
            serviceTypeList = userGroupDAO.getServiceTypeList(JSFUtil.getUserSession().getUserGroup().getId());
            
            checkCounterEdit();
            
        } catch (Exception e) {
            System.out.println((new StringBuilder()).append("###################").append(e).toString());
        }
    }
    
    public String editActivity() {
//        JSFUtil.redirect(ACTIVITY);
        return ACTIVITY;
    }
    
    private void checkCounterEdit(){    
        try{
            if(contactCase.getStatus().equals("closed")) {
                bSaveButton = false;
            } else {
                bSaveButton = SecurityUtil.isViewLocation(serviceTypeId, businessUnitId, locationId);
            }
        }catch(Exception e){
            bSaveButton = false;
        }
    }

    public String back(){

        return "customerDetail.xhtml?faces-redirect=true";
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
        try{
            contactCase.setCaseDetailId(getCaseDetailDAO().findCaseDetail(caseDetailId));
            if (reasonRequestId != null && reasonRequestId != 0) {
                contactCase.setCaseRequestId(getCaseRequestDAO().findCaseRequest(reasonRequestId));
            } else {
                contactCase.setCaseRequestId(null);
            }
            if (relationshipId != null && relationshipId != 0) {
                contactCase.setRelationshipId(relationshipId);
            } else {
                contactCase.setRelationshipId(null);
            }
            if (caseChannelId != null && caseChannelId != 0) {
                contactCase.setChannelId(getChannelDAO().findChannel(caseChannelId));
            } else {
                contactCase.setChannelId(null);
            }
            if(relatedCaseId != null && relatedCaseId != 0) {
                contactCase.setContactCase(contactCaseDAO.findContactCase(relatedCaseId));
            }            
            contactCase.setCaseAttachmentCollection(getCaseAttachments());

            if(serviceTypeId != null && serviceTypeId != 0){
                contactCase.setServiceType(serviceTypeDAO.findServiceType(serviceTypeId));
            }
            if(serviceTypeId != null && serviceTypeId != 0){
                contactCase.setBusinessUnit(businessUnitDAO.findBusinessUnit(businessUnitId));
            }
            if(locationId != null && locationId != 0){
                Location location = locationDAO.findLocation(locationId);
                contactCase.setLocation(location);
                contactCase.setLocationName(location.getName());
            }
            if (contactCase.getCaseAttachmentCollection().size() > 0) {
                contactCase.setAttachFile(true);
            } else {
                contactCase.setAttachFile(false);
            }
            //save related yes sale
            String relatedSale = getPoIdsFromCheckBox();
            if(!relatedSale.equals("")) {
                contactCase.setRelatedSale(relatedSale);
            } else {
                contactCase.setRelatedSale(null);
            }
            
            //check cancel workflow
            if(contactCase.getWorkflow()) {
                if(cancelWorkflow) {
                    contactCase.setWorkflow(Boolean.FALSE);
                }
            }
            getContactCaseDAO().edit(contactCase);
            if(!attachIdsTemp.isEmpty()) {
                deleteFile();
            }
            moveFile();

            return SUCCESS;
        }catch (Exception e){
            System.out.println((new StringBuilder()).append("###################").append(e.getClass()).append(e).toString());
        }

        return FAIL;
    }
    
    private Collection<CaseAttachment> getCaseAttachments() {
        if (!attachFilesTemp.isEmpty()) {
            for (String[] stra : attachFilesTemp) {
                String fName = getRealFileName(stra[1]);
                File fTmp = new File(stra[1]);
                if (fTmp.exists()) {

                    CaseAttachment ca = new CaseAttachment();
                    ca.setCaseId(contactCase);
                    ca.setCreateBy(getLoginUser().getName());
                    ca.setCreateDate(new Date());
                    ca.setFilename(fName);

                    attachFiles.add(ca);
                }
            }
        }
        return attachFiles;
    }
    
    private void deleteFile() {
        try{
            String path = getCasePath();
            CaseAttachment ca = null;
            for (String id :attachIdsTemp) {
                ca = caseAttachmentDAO.findCaseAttachment(Integer.parseInt(id));
                String fName = "";
                Boolean delete = false;
                if(ca != null){
                    fName = ca.getFilename();
                    if(!fName.isEmpty()){
                        File f = new File(path + fName);
                        if(f.exists()){
                            delete = f.delete();
                        }
                    }
                    caseAttachmentDAO.destroy(ca.getId());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
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
                for (String[] stra : attachFilesTemp) {
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
    
    public boolean isCancelWorkflowPermitted() {
       return SecurityUtil.isPermitted("case:workflow:cancel");
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
    
    public String editCase() {
        //clear user session
        JSFUtil.getUserSession().setContactCaseId(null);
        JSFUtil.getUserSession().setWorkflowlogId(null);
        JSFUtil.getUserSession().setFrom(null);
                
        String selectId = "";
        if(JSFUtil.getRequestParameterMap("selectId") != null) {
            selectId = JSFUtil.getRequestParameterMap("selectId");
        } else {
            selectId = getRequest("selectId");
        }
        contactCaseId = Integer.parseInt(selectId);
        contactCase = contactCaseDAO.findContactCase(Integer.parseInt(selectId));
        JSFUtil.getUserSession().setContactCaseId(contactCaseId);
        if(contactCase.getActivityDelegate() != null) {
            disableSave = true;
        }
        //init sale history
        initSaleHistory(contactCase.getCustomerId().getId());
        if(contactCase.getRelatedSale() != null && !contactCase.getRelatedSale().isEmpty()) {
            String[] poIds = contactCase.getRelatedSale().split(",");
            for(String obj : poIds) {
                selectedPoIds.put(Integer.parseInt(obj), Boolean.TRUE);
            }
        }
        
        //find allow close case of workflow rule
        if(contactCase.getWorkflow() != null && contactCase.getWorkflow()) {
            nextContactCaseWorkflowLog = null;
            contactCaseWorkflowLogList = (List) contactCase.getContactCaseWorkflowLogCollection();
            contactCaseWorkflowLog = contactCaseWorkflowLogDAO.findContactCaseWorkflowLogBySeq(contactCase.getId(), contactCase.getWorkflowSeqNo());
            nextContactCaseWorkflowLog  = contactCaseWorkflowLogDAO.findContactCaseWorkflowLogBySeq(contactCase.getId(), contactCase.getWorkflowSeqNo()+1);
            
            if(contactCaseWorkflowLog != null) {
                if(contactCaseWorkflowLog.getReceiveUsers() != null && JSFUtil.getUserSession().getUsers().getId().equals(contactCaseWorkflowLog.getReceiveUsers().getId()))  {
                    allowClosedCase = contactCaseWorkflowLog.getAllowCloseCase();
                } else {
                    //check allow closed case for user log in
                    ContactCaseWorkflowLog ch = contactCaseWorkflowLogDAO.findContactCaseWorkflowLogAccept(contactCaseId);
                    if(ch != null)
                        allowClosedCase = ch.getAllowCloseCase();
                }
                
                if(contactCaseWorkflowLog.getSeqNo() == contactCase.getWorkflowSeqNo()) {
                    if(contactCaseWorkflowLog.getReceiveUsers() == JSFUtil.getUserSession().getUsers()) {
                        allowCreateActivity = true;
                    }
                }
            } 
            if(nextContactCaseWorkflowLog != null) {
                JSFUtil.getUserSession().setWorkflowlogId(nextContactCaseWorkflowLog.getId());
            }
        }

        //businessUnitList = contactCase.getServiceType() != null ? contactCase.getServiceType().getBusinessUnitCollection() : new ArrayList<BusinessUnit>();
        if (contactCase.getBusinessUnit() != null) { 
            contactCase.getBusinessUnit().getLocationCollection().size();
            locationList = (List) contactCase.getBusinessUnit().getLocationCollection();
        } else {
            locationList = new ArrayList<Location>();
        }
        
        caseDetail = contactCase.getCaseDetailId();
        caseDetailId = contactCase.getCaseDetailId().getId();
        caseTopicId = contactCase.getCaseDetailId().getCaseTopicId().getId();
        caseTypeId = contactCase.getCaseDetailId().getCaseTopicId().getCaseTypeId().getId();
        serviceTypeId = contactCase.getServiceType() != null ? contactCase.getServiceType().getId() : null;
        businessUnitId = contactCase.getBusinessUnit() != null ? contactCase.getBusinessUnit().getId() : null;
        locationId = contactCase.getLocation() != null ? contactCase.getLocation().getId() : null;
        
        businessUnitList = userGroupDAO.getBusinessUnitList(JSFUtil.getUserSession().getUserGroup().getId(), serviceTypeId);       
        if (contactCase.getCaseRequestId() != null){
            reasonRequestId = contactCase.getCaseRequestId().getId();
        }
        if (contactCase.getRelationshipId() != null){
            relationshipId = contactCase.getRelationshipId();
        }
        if (contactCase.getChannelId() != null){
            caseChannelId = contactCase.getChannelId().getId();
        }
        setCaseTopics(Integer.valueOf(caseTypeId));
        setCaseDetails(Integer.valueOf(caseTopicId));
        setCaseRequests(getCaseRequestList(caseDetailId));
        setActivityList(getCustomerHandlingDAO().findActivity(Integer.valueOf(selectId)));
        if (contactCase.getContactCase() != null) {
            relatedCaseId = contactCase.getContactCase().getId();
            relatedCaseName = contactCase.getContactCase().getCode();
        }

        contactCase.getCaseAttachmentCollection().size();
        attachFiles = contactCase.getCaseAttachmentCollection();

        if(JSFUtil.getUserSession().getCustomerDetail() == null){
            CustomerInfoValue c = customerHandlingDAO.findCustomerHandling(contactCase.getCustomerId().getId());
            JSFUtil.getUserSession().setCustomerDetail(c);
        }
        
        if(JSFUtil.getRequestParameterMap("from") != null) {
            from = JSFUtil.getRequestParameterMap("from");
            JSFUtil.getUserSession().setFrom(from);
        }
        
        if(from != null && from.equals("caseHistory")) 
            return HISTORY;
        else 
            return EDIT;
    }

    public String createActivity(){
//        contactCaseId = Integer.parseInt(JSFUtil.getRequestParameterMap("contactCaseId"));
        if(contactCaseId != null && contactCaseId != 0){
            return "activityEdit.xhtml";
        }else{
            return "caseHandling.jsf";
        }
    }

    public List getCaseTypes() {
        List caseTypeValueList = new ArrayList();
        caseTypeValueList.add(new SelectItem(null, CrmConstant.PLEASE_SELECT));
        Iterator i$ = getCaseTypeList().iterator();
        do {
            if (!i$.hasNext()) {
                break;
            }
            CaseType caseType = (CaseType) i$.next();
            if (caseType.getId() != null) {
                caseTypeValueList.add(new SelectItem(caseType.getId(), caseType.getName()));
            }
        } while (true);
        return caseTypeValueList;
    }

    public List getRelationships() {
        List relationshipValueList = new ArrayList();
        relationshipValueList.add(new SelectItem(null, CrmConstant.PLEASE_SELECT));
        Iterator i$ = getRelationshipList().iterator();
        do {
            if (!i$.hasNext()) {
                break;
            }
            Relationship relationship = (Relationship) i$.next();
            if (relationship.getId() != null) {
                relationshipValueList.add(new SelectItem(relationship.getId(), relationship.getName()));
            }
        } while (true);
        return relationshipValueList;
    }

    public List getChannels() {
        List caseChannelValuseList = new ArrayList();
        caseChannelValuseList.add(new SelectItem(null, CrmConstant.PLEASE_SELECT));
        Channel channel;
        for (Iterator i$ = channelList.iterator(); i$.hasNext(); caseChannelValuseList.add(new SelectItem(channel.getId(),channel.getType()+":"+ channel.getName()))) {
            channel = (Channel) i$.next();
        }

        return caseChannelValuseList;
    }

    private List getCaseTopicList(Integer caseTypeId) {
        List caseTopicList = getCaseTopicDAO().findCaseTopicByCaseType(caseTypeId);
        List values = new ArrayList();
        values.add(new SelectItem(null, CrmConstant.PLEASE_SELECT));
        CaseTopic obj;
        for (Iterator i$ = caseTopicList.iterator(); i$.hasNext(); values.add(new SelectItem(obj.getId(), obj.getName()))) {
            obj = (CaseTopic) i$.next();
        }
        if(caseTopicList != null && caseTopicList.size() == 1) {
            CaseTopic o = (CaseTopic) caseTopicList.get(0);
            caseTopicId = o.getId();
            caseDetails = getCaseDetailList(caseTopicId);
        } else {
            caseDetails = getCaseDetailList(Integer.valueOf(0));
        }
        return values;
    }

    private void setCaseTopics(Integer caseTypeId) {
        caseTopics = getCaseTopicList(caseTypeId);
    }

    public void caseTypeListener(ValueChangeEvent event) {
        if (caseTopics != null) {
            caseTopics.clear();
            setCaseTopicId(0);
        }
        if (caseDetails != null) {
            caseDetails.clear();
            setCaseDetailId(0);
        }
        if (caseRequests != null) {
            caseRequests.clear();
            setReasonRequestId(0);
        }
        Integer caseTypeIdChange = (Integer) event.getNewValue();
        setCaseTopics(caseTypeIdChange);
        FacesContext.getCurrentInstance().renderResponse();
    }
    
    public void caseTopicListener() {
        if (caseDetails != null) {
            caseDetails.clear();
            setCaseDetailId(0);
        }
        if (caseRequests != null) {
            caseRequests.clear();
            setReasonRequestId(0);
        }
        setCaseDetails(caseTopicId);
        FacesContext.getCurrentInstance().renderResponse();
    }
    
    public void caseDetailListener() {
        if (caseRequests != null) {
            caseRequests.clear();
            setReasonRequestId(0);
        }
        setCaseRequests(getCaseRequestList(caseDetailId));
        caseDetail = caseDetailDAO.findCaseDetail(caseDetailId);
        contactCase.setPriority(caseDetail.getDefaultPriority());
//        setSLA(contactCase.getPriority());
        FacesContext.getCurrentInstance().renderResponse();
    }

    private void setSLA(String priority){
        if(priority.equals("low")){
            contactCase.setSlaAccept(caseDetail.getSlaAcceptcaseLow());
            contactCase.setSlaClose(caseDetail.getSlaClosecaseLow());
        }else if(priority.equals("medium")){
            contactCase.setSlaAccept(caseDetail.getSlaAcceptcaseMedium());
            contactCase.setSlaClose(caseDetail.getSlaClosecaseMedium());
        }else if(priority.equals("high")){
            contactCase.setSlaAccept(caseDetail.getSlaAcceptcaseHigh());
            contactCase.setSlaClose(caseDetail.getSlaClosecaseHigh());
        }else if(priority.equals("immediate")){
            contactCase.setSlaAccept(caseDetail.getSlaAcceptcaseImmediate());
            contactCase.setSlaClose(caseDetail.getSlaClosecaseImmediate());
        }
        contactCase.setSlaCloseDate(holidayDAO.getSLADate(contactCase.getContactDate(), contactCase.getSlaClose()));
    }

    public void priorityListener() {
        if(contactCase.getCaseDetailId() != null) {
            caseDetail = contactCase.getCaseDetailId();
//            setSLA(contactCase.getPriority());
        }
    }

    private List getCaseDetailList(Integer caseTopicId) {
        List caseDetailList = getCaseDetailDAO().findCaseDetailByCaseTopic(caseTopicId);
        List values = new ArrayList();
        values.add(new SelectItem(null, CrmConstant.PLEASE_SELECT));
        CaseDetail obj;
        for (Iterator i$ = caseDetailList.iterator(); i$.hasNext(); values.add(new SelectItem(obj.getId(), obj.getName()))) {
            obj = (CaseDetail) i$.next();
        }
        if(caseDetailList != null && caseDetailList.size() == 1) {
            CaseDetail o = (CaseDetail) caseDetailList.get(0);
            caseDetailId = o.getId();
            caseRequests = getCaseRequestList(caseDetailId);
            caseDetail = caseDetailDAO.findCaseDetail(caseDetailId);
            if(contactCase.getPriority() == null) {
                contactCase.setPriority(caseDetail.getDefaultPriority());
            }
//            setSLA(contactCase.getPriority());
        } else {
            caseRequests = getCaseRequestList(Integer.valueOf(0));
        }
        return values;
    }

    public void setCaseDetails(Integer caseTopicId) {
        caseDetails = getCaseDetailList(caseTopicId);
    }

//    public void uploadListener(UploadEvent event) throws Exception {
//        String[] stra = new String[3];
//        UploadItem item = event.getUploadItem();
//        String str = item.getFileName();
//        str = getRealFileName(str);
//        stra[0] = str;//file name
//        str = JSFUtil.getDateFormat1(new Date()) + "_" + str;
//        stra[2] = str;
//        str = tmpPath + str;
//        stra[1] = str;//full path file name
//        File file = new File(str);
//        item.getFile().renameTo(file);
//
//        attachFilesTemp.add(stra);
////        String custDir = customerPath + contactCase.getCustomerId() + "\\case\\" +  + contactCase.getCustomerId().getId();
////        File fDir = new File(custDir);
////        if (!fDir.exists()) {
////            fDir.mkdir();
////        }
////        UploadItem item = event.getUploadItem();
////        String str = item.getFileName();
////        str = getRealFileName(str);
////        str = JSFUtil.getDateFormat1(new Date()) + "_" + str;
////        String f = str;
////        str = custDir + "\\" + str;
////        File file = new File(str);
////        item.getFile().renameTo(file);
////
////        CaseAttachment ca = new CaseAttachment();
////        ca.setCaseId(contactCase);
////        ca.setCreateBy(JSFUtil.getUserSession().getUserName());
////        ca.setCreateDate(new Date());
////        ca.setFilename(f);
////
////        ContactCase cc = contactCaseDAO.findContactCase(contactCase.getId());
////        attachFiles = cc.getCaseAttachmentCollection();
////        attachFiles.add(ca);
//
//    }
    
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
            attachFilesTemp.add(stra);
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
    
//    public void removeFile(ActionEvent event){
//        try{
//            String attachId = StringUtils.trim(getRequest("attachId"));
//            String path = getCasePath();
//            CaseAttachment ca = caseAttachmentDAO.findCaseAttachment(Integer.parseInt(attachId));
//            String fName = "";
//            Boolean delete = false;
//            if(ca != null){
//                fName = ca.getFilename();
//                if(!fName.isEmpty()){
//                    File f = new File(path + fName);
//                    if(f.exists()){
//                        delete = f.delete();
//                    }
//                }
//                caseAttachmentDAO.destroy(ca.getId());
//            }
//
//            ContactCase cc = contactCaseDAO.findContactCase(contactCase.getId());
//            attachFiles = cc.getCaseAttachmentCollection();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
    
    public void removeFile(ActionEvent event){
        try{
            String attachId = StringUtils.trim(getRequest("attachId"));
            attachIdsTemp.add(attachId);
            CaseAttachment ca = caseAttachmentDAO.findCaseAttachment(Integer.parseInt(attachId));
            attachFiles.remove(ca);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void removeFileTemp(ActionEvent event) {
        String rmFile = StringUtils.trim(getRequest("rmFile"));
        File f = new File(rmFile);
        if (f.exists()) {
            f.delete();
        }
        List<String[]> list = new ArrayList<String[]>();
        for (String[] s : attachFilesTemp) {
            if (!StringUtils.equals(rmFile, StringUtils.trim(s[1]))) {
                list.add(s);
            }
        }
        attachFilesTemp = list;
    }

    public String getRealFileName(String str){
        String str1 = str;
        if(str.lastIndexOf("/") != -1){
            str1 = StringUtils.substring(str, (str.lastIndexOf("/")) + 1, str.length());
        }else if(str.lastIndexOf("\\") != -1){
            str1 = StringUtils.substring(str, (str.lastIndexOf("\\")) + 1, str.length());
        }
        return str1;
    }

    public void selectCaseListener(ActionEvent event) {
        relatedCaseName = JSFUtil.getRequestParameterMap("relatedCaseName");
        String tmpId = JSFUtil.getRequestParameterMap("relatedCaseId");
        if(!tmpId.isEmpty()){
            relatedCaseId = Integer.parseInt(tmpId);
        }else{
            relatedCaseId = null;
        }
    }

    public void serviceTypeListener(ValueChangeEvent event){
        try {
            businessUnitId = null;
            locationId = null;
            if(businessUnitList != null || !businessUnitList.isEmpty())
                businessUnitList.clear();
            if(locationList != null || !locationList.isEmpty())
                locationList.clear();
            
            Integer id = (Integer) event.getNewValue();
            setServiceTypeId(id);
            if(id != null && id != 0){
                businessUnitList = userGroupDAO.getBusinessUnitList(JSFUtil.getUserSession().getUserGroup().getId(), id);  
                
                if(businessUnitList != null && businessUnitList.size() == 1) {
                    businessUnitId =  businessUnitList.get(0).getId();
                    locationList = userGroupDAO.getLocationList(JSFUtil.getUserSession().getUserGroup().getId(), id, businessUnitId);  
                    if(locationList != null && locationList.size() == 1) {
                        locationId = locationList.get(0).getId();
                        setLocationId(locationId);
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
        } catch(Exception e) {
            log.error(e);
        }
    }

    public void businessUnitListener(ValueChangeEvent event){
        try {
            Integer id = (Integer) event.getNewValue();
            if(id != null && id != 0) {
                locationList = userGroupDAO.getLocationList(JSFUtil.getUserSession().getUserGroup().getId(), serviceTypeId, id);  
                if(locationList != null && locationList.size() == 1) {
                    locationId = locationList.get(0).getId();
                    setLocationId(locationId);
                } else {
                    setLocationId(0);
                }
            }
        } catch(Exception e) {
            log.error(e);
        }
    }

    public void clearCaseListener(ActionEvent event) {
        relatedCaseId = null;
        relatedCaseName = null;
    }

    public CaseTypeDAO getCaseTypeDAO() {
        return caseTypeDAO;
    }

    public void setCaseTypeDAO(CaseTypeDAO caseTypeDAO) {
        this.caseTypeDAO = caseTypeDAO;
    }

    public CaseTopicDAO getCaseTopicDAO() {
        return caseTopicDAO;
    }

    public void setCaseTopicDAO(CaseTopicDAO caseTopicDAO) {
        this.caseTopicDAO = caseTopicDAO;
    }

    public CaseDetailDAO getCaseDetailDAO() {
        return caseDetailDAO;
    }

    public void setCaseDetailDAO(CaseDetailDAO caseDetailDAO) {
        this.caseDetailDAO = caseDetailDAO;
    }

    public List getCaseTopics() {
        return caseTopics;
    }

    public Integer getCaseTypeId() {
        return caseTypeId;
    }

    public void setCaseTypeId(Integer caseTypeId) {
        this.caseTypeId = caseTypeId;
    }

    public Integer getCaseTopicId() {
        return caseTopicId;
    }

    public void setCaseTopicId(Integer caseTopicId) {
        this.caseTopicId = caseTopicId;
    }

    public Integer getRelationshipId() {
        return relationshipId;
    }

    public void setRelationshipId(Integer relationshipId) {
        this.relationshipId = relationshipId;
    }

    public List getCaseTypeList() {
        return caseTypeList;
    }

    public void setCaseTypeList(List caseTypeList) {
        this.caseTypeList = caseTypeList;
    }

    public List getCaseDetails() {
        return caseDetails;
    }

    public Integer getCaseDetailId() {
        return caseDetailId;
    }

    public void setCaseDetailId(Integer caseDetailId) {
        this.caseDetailId = caseDetailId;
    }

    public Integer getReasonRequestId() {
        return reasonRequestId;
    }

    public void setReasonRequestId(Integer reasonRequestId) {
        this.reasonRequestId = reasonRequestId;
    }

    public Integer getCaseChannelId() {
        return caseChannelId;
    }

    public void setCaseChannelId(Integer caseChannelId) {
        this.caseChannelId = caseChannelId;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public ContactCase getContactCase() {
        return contactCase;
    }

    public void setContactCase(ContactCase contactCase) {
        this.contactCase = contactCase;
    }

    public ChannelDAO getChannelDAO() {
        return channelDAO;
    }

    public void setChannelDAO(ChannelDAO channelDAO) {
        this.channelDAO = channelDAO;
    }

    public RelationshipDAO getRelationshipDAO() {
        return relationshipDAO;
    }

    public void setRelationshipDAO(RelationshipDAO relationshipDAO) {
        this.relationshipDAO = relationshipDAO;
    }

    public ContactCaseDAO getContactCaseDAO() {
        return contactCaseDAO;
    }

    public void setContactCaseDAO(ContactCaseDAO contactCaseDAO) {
        this.contactCaseDAO = contactCaseDAO;
    }

    public List getChannelList() {
        return channelList;
    }

    private void setChannelList(List channelList) {
        this.channelList = channelList;
    }

    private void setRelationshipList(List relationshipList) {
        this.relationshipList = relationshipList;
    }

    public List getRelationshipList() {
        return relationshipList;
    }

    /**
     * @return the activityDAO
     */
    public ActivityDAO getActivityDAO() {
        return activityDAO;
    }

    /**
     * @param activityDAO the activityDAO to set
     */
    public void setActivityDAO(ActivityDAO activityDAO) {
        this.activityDAO = activityDAO;
    }

    /**
     * @return the activityList
     */
    public List<ActivityInfoValue> getActivityList() {
        return activityList;
    }

    /**
     * @param activityList the activityList to set
     */
    public void setActivityList(List<ActivityInfoValue> activityList) {
        this.activityList = activityList;
    }

    /**
     * @return the caseRequests
     */
    public List getCaseRequests() {
        return caseRequests;
    }

    /**
     * @param caseRequests the caseRequests to set
     */
    public void setCaseRequests(List caseRequests) {
        this.caseRequests = caseRequests;
    }

    private List getCaseRequestList(Integer caseDetailId) {
        List<CaseRequest> caseRequestList = getCaseRequestDAO().findCaseRequestByCaseDetail(caseDetailId);
        List values = new ArrayList();
        values.add(new SelectItem(null, CrmConstant.PLEASE_SELECT));
        for (CaseRequest obj : caseRequestList) {
            if (obj.getId() != null) {
                values.add(new SelectItem(obj.getId(), obj.getName()));
            }
        }
        if(caseRequestList != null && caseRequestList.size() == 1) {
            CaseRequest o = (CaseRequest) caseRequestList.get(0);
            reasonRequestId = o.getId();
        }
        return values;
    }

    /**
     * @return the caseRequestDAO
     */
    public CaseRequestDAO getCaseRequestDAO() {
        return caseRequestDAO;
    }

    /**
     * @param caseRequestDAO the caseRequestDAO to set
     */
    public void setCaseRequestDAO(CaseRequestDAO caseRequestDAO) {
        this.caseRequestDAO = caseRequestDAO;
    }

    /**
     * @return the customerInfoValue
     */
    public CustomerInfoValue getCustomerInfoValue() {
        return customerInfoValue;
    }

    /**
     * @param customerInfoValue the customerInfoValue to set
     */
    public void setCustomerInfoValue(CustomerInfoValue customerInfoValue) {
        this.customerInfoValue = customerInfoValue;
    }

    /**
     * @return the customerHandlingDAO
     */
    public CustomerHandlingDAO getCustomerHandlingDAO() {
        return customerHandlingDAO;
    }

    /**
     * @param customerHandlingDAO the customerHandlingDAO to set
     */
    public void setCustomerHandlingDAO(CustomerHandlingDAO customerHandlingDAO) {
        this.customerHandlingDAO = customerHandlingDAO;
    }

    public Integer getContactCaseId() {
        return contactCaseId;
    }

    public void setContactCaseId(Integer contactCaseId) {
        this.contactCaseId = contactCaseId;
    }

    public Collection<CaseAttachment> getAttachFiles() {
        return attachFiles;
    }

    public void setAttachFiles(Collection<CaseAttachment> attachFiles) {
        this.attachFiles = attachFiles;
    }

    public CaseAttachmentDAO getCaseAttachmentDAO() {
        return caseAttachmentDAO;
    }

    public void setCaseAttachmentDAO(CaseAttachmentDAO caseAttachmentDAO) {
        this.caseAttachmentDAO = caseAttachmentDAO;
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

    public List<String[]> getAttachFilesTemp() {
        return attachFilesTemp;
    }

    public void setAttachFilesTemp(List<String[]> attachFilesTemp) {
        this.attachFilesTemp = attachFilesTemp;
    }

    public CaseDetail getCaseDetail() {
        return caseDetail;
    }

    public void setCaseDetail(CaseDetail caseDetail) {
        this.caseDetail = caseDetail;
    }

    public HolidayDAO getHolidayDAO() {
        return holidayDAO;
    }

    public void setHolidayDAO(HolidayDAO holidayDAO) {
        this.holidayDAO = holidayDAO;
    }

    public LocationDAO getLocationDAO() {
        return locationDAO;
    }

    public void setLocationDAO(LocationDAO locationDAO) {
        this.locationDAO = locationDAO;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public List<BusinessUnit> getBusinessUnitList() {
        return businessUnitList;
    }

    public void setBusinessUnitList(List<BusinessUnit> businessUnitList) {
        this.businessUnitList = businessUnitList;
    }

    public List<Location> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<Location> locationList) {
        this.locationList = locationList;
    }

    public ServiceTypeDAO getServiceTypeDAO() {
        return serviceTypeDAO;
    }

    public void setServiceTypeDAO(ServiceTypeDAO serviceTypeDAO) {
        this.serviceTypeDAO = serviceTypeDAO;
    }

    public Integer getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Integer serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public List<ServiceType> getServiceTypeList() {
        return serviceTypeList;
    }

    public void setServiceTypeList(List<ServiceType> serviceTypeList) {
        this.serviceTypeList = serviceTypeList;
    }

    public BusinessUnitDAO getBusinessUnitDAO() {
        return businessUnitDAO;
    }

    public void setBusinessUnitDAO(BusinessUnitDAO businessUnitDAO) {
        this.businessUnitDAO = businessUnitDAO;
    }

    public Integer getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(Integer businessUnitId) {
        this.businessUnitId = businessUnitId;
    }

    public UserGroupDAO getUserGroupDAO() {
        return userGroupDAO;
    }

    public void setUserGroupDAO(UserGroupDAO userGroupDAO) {
        this.userGroupDAO = userGroupDAO;
    }

    public boolean isbCounterEdit() {
        return bCounterEdit;
    }

    public void setbCounterEdit(boolean bCounterEdit) {
        this.bCounterEdit = bCounterEdit;
    }

    public boolean isbSaveButton() {
        return bSaveButton;
    }

    public void setbSaveButton(boolean bSaveButton) {
        this.bSaveButton = bSaveButton;
    }
    
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
    
    //Contact Case Workflow Log

    public Boolean getAllowClosedCase() {
        return allowClosedCase;
    }

    public void setAllowClosedCase(Boolean allowClosedCase) {
        this.allowClosedCase = allowClosedCase;
    }

    public Boolean getAllowCreateActivity() {
        return allowCreateActivity;
    }

    public void setAllowCreateActivity(Boolean allowCreateActivity) {
        this.allowCreateActivity = allowCreateActivity;
    }

    public Boolean getCancelWorkflow() {
        return cancelWorkflow;
    }

    public void setCancelWorkflow(Boolean cancelWorkflow) {
        this.cancelWorkflow = cancelWorkflow;
    }

    public ContactCaseWorkflowLog getContactCaseWorkflowLog() {
        return contactCaseWorkflowLog;
    }

    public void setContactCaseWorkflowLog(ContactCaseWorkflowLog contactCaseWorkflowLog) {
        this.contactCaseWorkflowLog = contactCaseWorkflowLog;
    }

    public ContactCaseWorkflowLogDAO getContactCaseWorkflowLogDAO() {
        return contactCaseWorkflowLogDAO;
    }

    public void setContactCaseWorkflowLogDAO(ContactCaseWorkflowLogDAO contactCaseWorkflowLogDAO) {
        this.contactCaseWorkflowLogDAO = contactCaseWorkflowLogDAO;
    }

    public List<ContactCaseWorkflowLog> getContactCaseWorkflowLogList() {
        return contactCaseWorkflowLogList;
    }

    public void setContactCaseWorkflowLogList(List<ContactCaseWorkflowLog> contactCaseWorkflowLogList) {
        this.contactCaseWorkflowLogList = contactCaseWorkflowLogList;
    }

    public ContactCaseWorkflowLog getNextContactCaseWorkflowLog() {
        return nextContactCaseWorkflowLog;
    }

    public void setNextContactCaseWorkflowLog(ContactCaseWorkflowLog nextContactCaseWorkflowLog) {
        this.nextContactCaseWorkflowLog = nextContactCaseWorkflowLog;
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

    public PurchaseOrderDAO getPurchaseOrderDAO() {
        return purchaseOrderDAO;
    }

    public void setPurchaseOrderDAO(PurchaseOrderDAO purchaseOrderDAO) {
        this.purchaseOrderDAO = purchaseOrderDAO;
    }

    public List<String> getAttachIdsTemp() {
        return attachIdsTemp;
    }

    public void setAttachIdsTemp(List<String> attachIdsTemp) {
        this.attachIdsTemp = attachIdsTemp;
    }
    
    public Boolean getDisableSave() {
        return disableSave;
    }

    public void setDisableSave(Boolean disableSave) {
        this.disableSave = disableSave;
    }

}
