package com.maxelyz.core.controller.front.customerHandling;

import com.maxelyz.core.controller.front.search.SearchCustomerController;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import com.maxelyz.core.model.dao.*;
import com.maxelyz.core.model.dao.front.customerHandling.ContactSummaryDAO;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.core.model.value.front.customerHandling.ContactHistoryKnowledgeInfoValue;
import com.maxelyz.core.model.value.front.customerHandling.ContactHistoryProductInfoValue;
import com.maxelyz.core.model.value.front.customerHandling.ContactHistorySaleResultInfoValue;
import com.maxelyz.core.service.TelephonyService;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;
import org.richfaces.json.JSONObject;

@ManagedBean
@ViewScoped
public class ContactSummaryController {

    private static Logger log = Logger.getLogger(ContactSummaryController.class);
    private static Logger ctiLog = Logger.getLogger("mxCtiLogger");
    
    private static String DMC = "DMC";
    private static String DMCNOTOFFER = "DMCNotOffer";
    private static String CUSTOMER_HANDLING_PATH = "customerHandling";
    private static String CONTACT_SUMMARY = "contactsummary.xhmtl?faces-redirect=true";
    private static String NEXT_CALL = "contactsummarynextcall.xhmtl?faces-redirect=true";
    private static String BACK = "/front/customerHandling/customerDetail.xhtml?faces-redirect=true";
    private static String SUCCESS = "/front/todolist/incoming.xhtml?faces-redirect=true";
    private static String SUCCESS1 = "/front/todolist/incoming.jsf?faces-redirect=true";
    private static String SUCCESS_TS_ASSIGNMENTLIST = "/campaign/assignmentList.xhtml?faces-redirect=true";
    private static String SUCCESS_TS_ASSIGNMENTLIST1 = "/campaign/assignmentList.jsf?faces-redirect=true";
    private static String SUCCESS_TS_SALEAPPROACHING = "/front/customerHandling/saleApproaching.xhtml?faces-redirect=true";
    private static String SALE_HISTORY = "/front/customerHandling/saleHistory.xhtml?faces-redirect=true";
    private String message;
    private ContactHistory contactHistory;
    private ContactResult contactResult;
    private Integer dmcContactReasonId;
    private Integer contactResultId;
    private String contactResultCode;
    private List<ContactHistoryKnowledgeInfoValue> knowledgebaseInfoValues = new ArrayList<ContactHistoryKnowledgeInfoValue>();
    private List<ContactHistoryProductInfoValue> productInfoValues = new ArrayList<ContactHistoryProductInfoValue>();
    private List<ContactHistorySaleResultInfoValue> shoppingCartList;
    private List<ContactHistorySaleResultInfoValue> assignedProductList;
    private List<Channel> channelList;
    private List<ContactResult> contactResultList;
    private List<NosaleReason> nosaleReasonList;
    private List<FollowupsaleReason> followupsaleReasonList;
    private Date followupDate;
    private boolean showFollowupDate;
    private Date contactDate;
    private Integer channelId;
    private boolean contactClose;
    private String remark;
    private String closeReason;
    private boolean callReady;

    @ManagedProperty(value = "#{channelDAO}")
    private ChannelDAO channelDAO;
    @ManagedProperty(value = "#{dmccontactReasonDAO}")
    private DmccontactReasonDAO dmccontactReasonDAO;
    @ManagedProperty(value = "#{contactHistoryDAO}")
    private ContactHistoryDAO contactHistoryDAO;
    @ManagedProperty(value = "#{contactCaseDAO}")
    private ContactCaseDAO contactCaseDAO;
    @ManagedProperty(value = "#{purchaseOrderDAO}")
    private PurchaseOrderDAO purchaseOrderDAO;
    @ManagedProperty(value = "#{customerDAO}")
    private CustomerDAO customerDAO;
    @ManagedProperty(value = "#{assignmentDetailDAO}")
    private AssignmentDetailDAO assignmentDetailDAO;
    @ManagedProperty(value = "#{assignmentDAO}")
    private AssignmentDAO assignmentDAO;
    @ManagedProperty(value = "#{contactResultDAO}")
    private ContactResultDAO contactResultDAO;
    @ManagedProperty(value = "#{contactSummaryDAO}")
    private ContactSummaryDAO contactSummaryDAO;
    @ManagedProperty(value = "#{nosaleReasonDAO}")
    private NosaleReasonDAO nosaleReasonDAO;
    @ManagedProperty(value = "#{followupsaleReasonDAO}")
    private FollowupsaleReasonDAO followupsaleReasonDAO;
    @ManagedProperty(value = "#{productDAO}")
    private ProductDAO productDAO;
    
    @ManagedProperty(value="#{marketingDAO}")
    private MarketingDAO marketingDAO;
    @ManagedProperty(value="#{marketingCustomerDAO}")
    private MarketingCustomerDAO marketingCustomerDAO;
    @ManagedProperty(value="#{campaignDAO}")
    private CampaignDAO campaignDAO;
    @ManagedProperty(value = "#{telephonyService}")
    private TelephonyService telephonyService;
    
    @PostConstruct
    public void initialize() {
        if(JSFUtil.getUserSession().getContactHistory() != null && JSFUtil.getUserSession().getContactHistory().getContactDate() != null){
            contactDate = JSFUtil.getUserSession().getContactHistory().getContactDate();
        } else {
            contactDate = new Date();
        }
        contactClose = false;
        callReady = true;
        channelList = this.getChannelDAO().findChannelEntities();

        if (JSFUtil.getUserSession().isInboundCall() && JSFUtil.getUserSession().isOutboundCall()) {
            contactResultList = this.getContactResultDAO().findContactResultEntities();
        } else if (JSFUtil.getUserSession().isInboundCall()) {
            contactResultList = this.getContactResultDAO().findContactResultByService("inbound");
        } else if (JSFUtil.getUserSession().isOutboundCall()) {
            contactResultList = this.getContactResultDAO().findContactResultByService("outbound");
        } else {
            contactResultList = this.getContactResultDAO().findContactResultByService("outbound");
        }
        nosaleReasonList = this.getNosaleReasonDAO().findNosaleReasonEntities();
        followupsaleReasonList = this.getFollowupsaleReasonDAO().findFollowupsaleReasonEntities();
        if (JSFUtil.getUserSession().isInboundCall()) {
            if(JSFUtil.getUserSession().getUserGroup().getRole().contains("CSCounter")) {
                channelId = 2;
                if(contactResultList != null){
                    if (contactResultList.size()>0) {
                        contactResultId=contactResultList.get(0).getId();
                        contactResult = contactResultDAO.findContactResult(contactResultId);
                        contactResultCode = contactResult.getCode();
                    }
                }
            }
            else {
                channelId = 6;
            }
        } else {
            
            channelId = 9; //outbound:phonecall
        }
        
        if(JSFUtil.getUserSession().getContactHistory() != null){
            contactHistory = JSFUtil.getUserSession().getContactHistory();
        }
    }

    public void initContactSummary() {
        AssignmentDetail assignmentDetail = JSFUtil.getUserSession().getAssignmentDetail();
        contactClose = false;
        closeReason = "-";
     
        if(assignmentDetail != null && assignmentDetail.getStatus().equals("closed")) {
            contactClose = true;        
            closeReason = assignmentDetail.getContactResult();
        }
               
    }
    
    private void initContactHistory(){
        if(JSFUtil.getUserSession().getContactHistory() == null || JSFUtil.getUserSession().getContactHistory().getId() == null){
            contactHistory = new ContactHistory();
            contactHistory.setContactDate(new Date());
            if (JSFUtil.getUserSession().isInboundCall()) {
                if(JSFUtil.getUserSession().getUserGroup().getRole().contains("CSCounter")) {
                    contactHistory.setChannel(new Channel(2));//inbound:phonecall
                } else {
                    contactHistory.setChannel(new Channel(6));//inbound:Walk-in
                 }
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
            if(contactHistory.getContactTo() == null || contactHistory.getContactTo().equals("")){
                contactHistory.setContactTo(JSFUtil.getUserSession().getDialNo());
            }
            if(contactHistory.getTelephonyTrackId()== null || contactHistory.getTelephonyTrackId().equals("")){
                contactHistory.setTelephonyTrackId(JSFUtil.getUserSession().getTelephonyTrackId());
            }
            if(contactHistory.getTalkTime() == null){
                contactHistory.setTalkTime(JSFUtil.getUserSession().getTalkTime());
            }

            contactHistoryDAO.create(contactHistory);

            JSFUtil.getUserSession().setContactHistory(contactHistory);
        } else {
            contactHistory = JSFUtil.getUserSession().getContactHistory();
        }
    }

    public List<ContactHistorySaleResultInfoValue> getShoppingCartList() {
        if (JSFUtil.getUserSession().getHasPurchaseOrders()) {
            //ถ้ามีสินค้าใน Shopping Cart ให้เก็บลง Array
            List<PurchaseOrder> purchaseOrders = JSFUtil.getUserSession().getPurchaseOrders();
            shoppingCartList = contactSummaryDAO.findProductsByPurchaseOrders(purchaseOrders);
        }
        return shoppingCartList;
    }

    public List<ContactHistorySaleResultInfoValue> getAssignedProductList() {
        List<Product> assignedProducts = JSFUtil.getUserSession().getProducts();
        if (assignedProducts != null && shoppingCartList != null) {
            //ลบสินค้าที่มีใน ShoppingCart ออกจาก Array
            for (ContactHistorySaleResultInfoValue shoppingCart : shoppingCartList) {
                int idx = assignedProducts.indexOf(shoppingCart.getProduct());
                if (idx != -1) {
                    ////log.error("remove "+shoppingCart.getProduct().getName());
                    assignedProducts.remove(idx);
                }
            }
        }
        if (assignedProducts != null) {
            if(assignedProductList == null){
                assignedProductList = new ArrayList<ContactHistorySaleResultInfoValue>();
            }
            List<Product> productList = new ArrayList<Product>();
            for (Product p : assignedProducts) {
                boolean exist = false;
                for(ContactHistorySaleResultInfoValue c1 : assignedProductList){
                    if(p.getId() == c1.getProduct().getId()){
                        exist = true;
                        break;
                    }
                }
                if(!exist){
                    productList.add(p);
                }
            }
            for (Product p : productList) {
                PurchaseOrder po = new PurchaseOrder();
                po.setSaleResult('N');
                ContactHistorySaleResultInfoValue c = new ContactHistorySaleResultInfoValue(po, p);
                assignedProductList.add(c);
            }
        }
        return assignedProductList;
    }

    

    public String contactSummaryAction() {
        String toPage = JSFUtil.getRequestParameterMap("page");
        String fromPage = JSFUtil.getRequest().getRequestURI();
        String page;

        if (fromPage.contains(CUSTOMER_HANDLING_PATH) && !toPage.contains(CUSTOMER_HANDLING_PATH)) {
            //Out from Customer Handling Tab to Another Tab
            if (JSFUtil.getUserSession().isShowContactSummary()) {
                page = CONTACT_SUMMARY;
            } else {
                page = toPage;
            }
        } else if (fromPage.contains(CUSTOMER_HANDLING_PATH) && toPage.contains(CUSTOMER_HANDLING_PATH)) {
            //if working in Customer Handling -> goto customer handling main page
            page = toPage;
        } else {
            //Out from Another Tab (not Customer Handling)
            JSFUtil.getUserSession().removeCustomerSessionListener(null);
            page = toPage;
        }
        return page;
    }

    private void createContactHistory() {
        String contactTo = "";
        Integer talkTime = 0;
        Integer contactResultId = 0;
        String trackId = "";
        Integer customerId = JSFUtil.getUserSession().getCustomerDetail().getId();
        try{
            if(contactHistory != null){
                contactTo = contactHistory.getContactTo();
                talkTime = contactHistory.getTalkTime();
                trackId = contactHistory.getTelephonyTrackId();
            }
            updateContactCasesContactHistoryToNull();
            
            contactHistory.setRemark(remark);
            
            if(contactClose){
                contactHistory.setContactClose(true);
            }else{
                contactHistory.setContactClose(false);
            }
            
            contactHistoryDAO.updateContactHistory(contactHistory);            
            this.createContactHistoryPurchaseOrder(contactHistory.getId());            
            JSFUtil.getUserSession().setContactHistory(contactHistory);
        } catch(Exception e){
            //log.error("###" + customerId + "," + contactTo + "," + contactResultId + "," + talkTime + "," + trackId + "###" + e.toString());
        }
    }

    private void createContactHistoryKnowledge() throws Exception {
        if (JSFUtil.getUserSession().getHasKnowledgebases()) {
            Integer contactId = contactHistory.getId();
            List<ContactHistoryKnowledge> kb = new ArrayList<ContactHistoryKnowledge>();            
            for (ContactHistoryKnowledgeInfoValue item : knowledgebaseInfoValues) {
                ContactHistoryKnowledge chk = new ContactHistoryKnowledge(contactId, item.getKnowledgebase().getId());
                chk.setKnowledgebase(item.getKnowledgebase());
                chk.setUseful(item.getIsUseful());
                chk.setRemark(item.getRemark());
                kb.add(chk);
            }
            contactHistoryDAO.createContactHistoryKnowledge(kb);
            contactHistory.setContactHistoryKnowledgeCollection(kb);//use for automatic closed case
        }
    }

    private void createContactHistoryProduct() throws Exception {
        if (JSFUtil.getUserSession().getHasProducts()) {
            Integer contactId = contactHistory.getId();
            List<ContactHistoryProduct> p = new ArrayList<ContactHistoryProduct>();
            productInfoValues = getProductInfoValues();
            for (ContactHistoryProductInfoValue item : productInfoValues) {
                ContactHistoryProduct chp = new ContactHistoryProduct(contactId, item.getProduct().getId());
                chp.setProduct(item.getProduct());
                chp.setUseful(item.getIsUseful());
                chp.setRemark(item.getRemark());
                p.add(chp);
            }
            contactHistoryDAO.createContactHistoryProduct(p);
            contactHistory.setContactHistoryProductCollection(p); //use for automatic closed case
        }
    }

    private void createContactHistoryPurchaseOrder(Integer contactHistoryId) throws Exception {
        try {
            if (JSFUtil.getUserSession().getHasPurchaseOrders()) {
                for (PurchaseOrder po : JSFUtil.getUserSession().getPurchaseOrders()) {
                    contactHistoryDAO.createContactHistoryPurchaseOrder(po.getId(), contactHistoryId);
                }
                //contactHistoryDAO.edit(contactHistory);
            }
        } catch (Exception e) {
            log.error(e);
        }
    }

    private void createContactHistoryAssignmentDetail(ContactHistory ch) throws Exception {
        if (JSFUtil.getUserSession().getAssignmentDetail() != null) {
            List<AssignmentDetail> assignmentDetails = new ArrayList<AssignmentDetail>();
            assignmentDetails.add(JSFUtil.getUserSession().getAssignmentDetail());
            ch.setAssignmentDetailCollection(assignmentDetails);
        }
    }

    private void updateContactCases() {
        if (JSFUtil.getUserSession().getHasContactCases()) {
            for (ContactCase contactCase : JSFUtil.getUserSession().getContactCases()) {
                contactCase.setContactHistory(contactHistory);
                try {
                    this.contactCaseDAO.edit(contactCase);
                } catch (Exception e) {
                    log.error(e);
                }
            }
        }
    }
    
    private void updateContactCasesContactHistoryToNull() {
        if (JSFUtil.getUserSession().getHasContactCases()) {
            for (ContactCase contactCase : JSFUtil.getUserSession().getContactCases()) {
                try {
                    this.contactCaseDAO.edit(contactCase);
                } catch (Exception e) {
                    log.error(e);
                }
            }
        }
    }    

    private void updateAssignmentDetailStatus() {
        AssignmentDetail ad = JSFUtil.getUserSession().getAssignmentDetail();
        if (ad != null && ad.getId() != null) {
            try {
                if (contactClose) {
                    ad.setStatus("closed");
                    this.assignmentDetailDAO.editStatus(ad, "closed");
                }
                ad = assignmentDetailDAO.findAssignmentDetail(ad.getId());
                JSFUtil.getUserSession().setAssignmentDetail(ad);
            } catch (Exception e) {
                log.error(e);
            }
        }
    }

    private void createContactHistorySaleResult(ContactHistorySaleResultInfoValue value){
        try{
            ContactHistorySaleResult chsr = new ContactHistorySaleResult();
            chsr.setContactHistory(contactHistory);
            chsr.setProduct(value.getProduct());
            if(value.getPurchaseOrder() != null && value.getPurchaseOrder().getId() != null){
                chsr.setPurchaseOrderId(value.getPurchaseOrder());
            }else{
                chsr.setPurchaseOrderId(null);
            }
            chsr.setSaleResult(value.getPurchaseOrder().getSaleResult());
            chsr.setCreateBy(JSFUtil.getUserSession().getUserName());
            chsr.setCreateById(JSFUtil.getUserSession().getUsers());
            chsr.setCreateDate(new Date());

            if (value.getNosaleReasonId() != null) {
                chsr.setNosaleReasonId(value.getNosaleReasonId());
            } else {
                chsr.setNosaleReasonId(null);
            }
            if (value.getFollowupsaleReasonId() != null) {
                chsr.setFollowupsaleReasonId(value.getFollowupsaleReasonId());
            } else {
                chsr.setFollowupsaleReasonId(null);
            }
            if(value.getFollowupDate() != null){
                chsr.setFollowupsaleDate(value.getFollowupDate());
            }

            contactHistoryDAO.createContactHistorySaleResult(chsr);
        }catch (Exception e) {
            log.error(e);
        }
    }

    private void updatePurchaseOrder() throws Exception {
        if (contactHistory != null && contactHistory.getContactResult() != null &&
                (contactHistory.getContactResult().getContactStatus().equals(JSFUtil.getBundleValue("dmcValue")) 
                || contactHistory.getContactResult().getContactStatus().equals(JSFUtil.getBundleValue("dmcNotOfferValue")))) {
            AssignmentDetail assignmentDetail = JSFUtil.getUserSession().getAssignmentDetail();
            String status = "followup";
            ContactHistorySaleResult chsr = null;
            
            FollowupsaleReason followup = null;
            List<PurchaseOrder> poList = JSFUtil.getUserSession().getPurchaseOrders();
            List<Product> productList = JSFUtil.getUserSession().getProducts();
            List<Product> tmpList = new ArrayList<Product>();
            
            if(poList != null && !poList.isEmpty()){
                Boolean exist = false;
                for(Product p : productList){
                    exist = false;
                    for(PurchaseOrder po : poList){
                        PurchaseOrderDetail pod = null;
                        Product pTmp = null;
                        for(PurchaseOrderDetail tmp : po.getPurchaseOrderDetailCollection()){
                            pod = tmp;
                            if(pod != null) break;
                        }
                        if(pod != null){
                            pTmp = pod.getProduct();
                        }
                        if(p.getId().intValue() == pTmp.getId().intValue()){
                            exist = true;
                            break;
                        }
                    }
                    if(!exist){
                        tmpList.add(p);
                    }
                }
                for(PurchaseOrder po : poList){
                    PurchaseOrderDetail pod = null;
                    for(PurchaseOrderDetail tmp : po.getPurchaseOrderDetailCollection()){
                        pod = tmp;
                        if(pod != null) break;
                    }                    
                    
                    ContactHistorySaleResult contactHistorySaleResult = new ContactHistorySaleResult();
                    contactHistorySaleResult.setContactHistory(contactHistory);
                    contactHistorySaleResult.setProduct(pod.getProduct());
                    contactHistorySaleResult.setPurchaseOrderId(po);
                    contactHistorySaleResult.setSaleResult(po.getSaleResult());
                    contactHistorySaleResult.setCreateBy(JSFUtil.getUserSession().getUserName());
                    contactHistorySaleResult.setCreateById(JSFUtil.getUserSession().getUsers());
                    contactHistorySaleResult.setCreateDate(new Date());

                    if (po.getNosaleReason() != null) {
                        contactHistorySaleResult.setNosaleReasonId(po.getNosaleReason().getId());
                    } else {
                        contactHistorySaleResult.setNosaleReasonId(null);
                    }
                    if (po.getFollowupsaleReason() != null) {
                        contactHistorySaleResult.setFollowupsaleReasonId(po.getFollowupsaleReason().getId());
                    } else {
                        contactHistorySaleResult.setFollowupsaleReasonId(null);
                    }
                    if(po.getFollowupsaleDate() != null){
                        contactHistorySaleResult.setFollowupsaleDate(po.getFollowupsaleDate());
                    }else{
                        contactHistorySaleResult.setFollowupsaleDate(null);
                    }
                    try{
                        contactHistoryDAO.createContactHistorySaleResult(contactHistorySaleResult);
                    }catch(Exception e){
                        log.error(e);
                    }
                    
                }
            }else{
                tmpList = productList;
            }
            
            if(!tmpList.isEmpty()){
                for (Product p : tmpList) {
                    ContactHistorySaleResult contactHistorySaleResult = new ContactHistorySaleResult();
                    contactHistorySaleResult.setContactHistory(contactHistory);
                    contactHistorySaleResult.setProduct(p);
                    contactHistorySaleResult.setPurchaseOrderId(null);
                    contactHistorySaleResult.setSaleResult("N".charAt(0));
                    contactHistorySaleResult.setCreateBy(JSFUtil.getUserSession().getUserName());
                    contactHistorySaleResult.setCreateById(JSFUtil.getUserSession().getUsers());
                    contactHistorySaleResult.setCreateDate(new Date());
                    contactHistorySaleResult.setNosaleReasonId(null);
                    contactHistorySaleResult.setFollowupsaleReasonId(null);
                    contactHistorySaleResult.setFollowupsaleDate(null);
                    try{
                        contactHistoryDAO.createContactHistorySaleResult(contactHistorySaleResult);
                    }catch(Exception e){
                        log.error(e);
                    }
                }
            }
            
            if(assignmentDetail != null && assignmentDetail.getId() != null){
                this.updateAssignmentDetail(assignmentDetail, status, followup, followupDate);
            }
        } else if (contactHistory != null && contactHistory.getContactResult() != null &&
                (contactHistory.getContactResult().getContactStatus().equals(JSFUtil.getBundleValue("contactableValue")) 
                || contactHistory.getContactResult().getContactStatus().equals(JSFUtil.getBundleValue("uncontactableValue")))) {
            
            AssignmentDetail assignmentDetail = JSFUtil.getUserSession().getAssignmentDetail();
            
            if(assignmentDetail != null && assignmentDetail.getId() != null){
                this.updateAssignmentDetail(assignmentDetail, assignmentDetail.getStatus(), null, null);
        }
    }
    }

    private void updateAssignmentDetail(AssignmentDetail assignmentDetail, String status, FollowupsaleReason followup, Date followupDate) throws Exception {
        if (assignmentDetail != null && assignmentDetail.getId() != null) {
            AssignmentDetail ad = this.getAssignmentDetailDAO().findAssignmentDetail(assignmentDetail.getId());
            ad.setStatus(status);
            if(status.equals("followup")){
                if(followup != null){
                    ad.setFollowupsaleReason(followup);
                }
                if(followupDate != null){
                    ad.setFollowupsaleDate(followupDate);
                }
            }
            ad.setContactResult(contactHistory.getContactResult().getName());
            ad.setContactRemark(contactHistory.getRemark());
            ad.setContactDate(new Date());
            ad.setUpdateDate(new Date());
            assignmentDetailDAO.edit(ad);
            
            ad = assignmentDetailDAO.findAssignmentDetail(ad.getId());
            JSFUtil.getUserSession().setAssignmentDetail(ad);
        }
    }
    
    private String checkCurrentState(String telephonyName, String stationNo) {
        String state = null;
        try {
            JSONObject stateInfo = telephonyService.getCurrentstate(telephonyName, stationNo);   
            if(stateInfo != null) {
                String msg = stateInfo.getString("Message"); 
                state = stateInfo.getString("CurrentState"); 

                if(msg.equals("null") || msg.equals("")) {
                    ctiLog.info(new Date().toString()+" CHECK CURRENTSTATE FOR READY Extension: "+stationNo+", Result: "+state);
                } else {
                    state = null;
                    ctiLog.info(new Date().toString()+" Extension: "+stationNo+" Error GETCurrentState: "+msg);
                }       
            }
        } catch (Exception ex) {
            state = null;
            ctiLog.info(new Date().toString()+" Extension: "+stationNo+" Error GETCurrentState: "+ex.getMessage());
        }   
        return state;
    }
    
    public void setReady(String telephonyName, String stationNo) {    
        String state = checkCurrentState(telephonyName, stationNo);
        try {
            if(state.toUpperCase().equals("NOTREADY") || state.toUpperCase().equals("PARKED")) {
                telephonyService.ready(telephonyName, stationNo);
//                ctiLog.info(new Date().toString()+" READY Extension: "+stationNo+", Result: "+result);
            }
            JSFUtil.getUserSession().setInboundCall(false);            
        } catch (Exception ex) {
            ex.printStackTrace();            
        }
    }
    
    public void saveAndExitActionListener(ActionEvent event) {
        try {
            save();
            JSFUtil.getUserSession().resetProducts();
            JSFUtil.getUserSession().resetPurchaseOrders();
            JSFUtil.getUserSession().removeCustomerSession(); //remove customer session when exit
            JSFUtil.getUserSession().removeContactHistory();
            JSFUtil.getUserSession().clearRefYes();
            JSFUtil.getUserSession().removeDial();
            JSFUtil.getUserSession().clearInfo();
            
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
            message = e.getMessage();
        }
    }

    public void saveAndExitAction() {        
        if (SecurityUtil.isPermitted("todolist:view") && SecurityUtil.isPermitted("campaign:view")) {
            String page = "";
            if (JSFUtil.getUserSession().getFirstPage().equals("todolist")) {
                page = "/front/todolist/incoming.jsf?faces-redirect=true";
            }else if (JSFUtil.getUserSession().getFirstPage().equals("campaign")) {
                page = "/campaign/assignmentList.jsf?faces-redirect=true";
            }else if (JSFUtil.getUserSession().getFirstPage().equals("search")) {
                this.getSearchCustomerController().searchCustomer();
                page = "/front/search/searchCustomer.jsf?faces-redirect=true";
            }else{
                page = "/front/todolist/incoming.jsf?faces-redirect=true";
            }
            JSFUtil.redirect(JSFUtil.getRequestContextPath() + page);
        }else if (SecurityUtil.isPermitted("todolist:view")) {
            JSFUtil.redirect(JSFUtil.getRequestContextPath() + SUCCESS1);
        }else if (SecurityUtil.isPermitted("campaign:view")) {
            JSFUtil.redirect(JSFUtil.getRequestContextPath() + SUCCESS_TS_ASSIGNMENTLIST1);
        }else{
            JSFUtil.redirect(JSFUtil.getRequestContextPath() + SUCCESS1);
        }
        
        // CALL READY FOR ASPECT WHEN END CONTACT
        if(callReady) {
            String stationNo = JSFUtil.getUserSession().getStationNo();        
            String telephonyName;
            if(JSFUtil.getUserSession().getUsers().getTelephonySso()) {
                telephonyName = JSFUtil.getUserSession().getUsers().getLdapLogin();
            } else {
                telephonyName = JSFUtil.getUserSession().getUsers().getTelephonyLoginName();
            }    
            
            CtiAdapter ctiAdapter = JSFUtil.getUserSession().getUserGroup().getCtiAdapter();         
            if(stationNo != null && !stationNo.equals("") && ctiAdapter != null && ctiAdapter.getCtiVendorCode() != null && ctiAdapter.getCtiVendorCode().equals("aspect")) {
                setReady(telephonyName, stationNo);
            }
        }
    
    }
    
    public SearchCustomerController getSearchCustomerController() {
        FacesContext context = FacesContext.getCurrentInstance();
        Application app = context.getApplication();
        ValueExpression vex = app.getExpressionFactory().
                createValueExpression(context.getELContext(), "#{searchCustomerController}", SearchCustomerController.class);
        SearchCustomerController g = (SearchCustomerController) vex.getValue(context.getELContext());
        return g;
    }
    
    public String saveAndContinueAction() {
        try {
            save();
            JSFUtil.getUserSession().removeDial();
            JSFUtil.getUserSession().resetProducts();
            JSFUtil.getUserSession().resetPurchaseOrders();
            JSFUtil.getUserSession().removeContactHistory();
        } catch (Exception e) {
            log.error(e);
            message = e.getMessage();
            return null;
        }
        
        if(JSFUtil.getUserSession().getUserGroup().getRole().contains("Agent")){
            return SALE_HISTORY;
        } else if (SecurityUtil.isPermitted("todolist:view") && SecurityUtil.isPermitted("campaign:view")) {
            return BACK;
        }else if (SecurityUtil.isPermitted("todolist:view")) {
            return BACK;
        }else if (SecurityUtil.isPermitted("campaign:view")) {
            return SUCCESS_TS_SALEAPPROACHING;
        }
        return BACK;
    }

    public void save() throws Exception {
        initContactHistory();
        this.createContactHistory();
        
        if(!JSFUtil.getUserSession().isOutboundCall()) {
            this.createContactHistoryKnowledge();
        }

        this.updatePurchaseOrder();

        this.updateAssignmentDetailStatus();
        this.updateContactCases();
        if(contactHistory != null && !JSFUtil.getUserSession().isOutboundCall()) {
            contactCaseDAO.createAutomaticClosedCase(contactHistory); //create auto contact case ################bug 6 Mar 12 ##############
            contactHistoryDAO.updateRptContactHistory(contactHistory); //update report top 10 kb and top 10 product
        }        
        JSFUtil.getUserSession().setShowContactSummary(false);
        JSFUtil.getUserSession().setRefNo(null);
    }
    
    private void initAssignmentDetail(){
        try{
            if(JSFUtil.getUserSession().getAssignmentDetail() != null && JSFUtil.getUserSession().getAssignmentDetail().getId() == null) {
                AssignmentDetail ad = JSFUtil.getUserSession().getAssignmentDetail();
                Campaign cp = null;
                if(ad != null && ad.getId() == null){
                    assignmentDetailDAO.create(ad);
                    JSFUtil.getUserSession().setAssignmentDetail(ad);
                }
            }
        }catch(Exception e){
            log.error(e);
        }
    }
    
    public void changeContactResultListener(ValueChangeEvent event){
        if(event != null && event.getNewValue() != null){
            contactResultId = (Integer) event.getNewValue();
        }
        if(contactResultId != null && contactResultId != 0){
            contactResult = contactResultDAO.findContactResult(contactResultId);
            contactResultCode = contactResult.getCode();
        }        
    }

    public String backAction() {
        return BACK;
    }

    public List<Channel> getChannelList() {

        return channelList;

    }

    public List<DmccontactReason> getDmccontactList() {
        return this.getDmccontactReasonDAO().findDmccontactReasonEntities();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ContactHistory getContactHistory() {
        if(JSFUtil.getUserSession().getContactHistory() != null){
            contactHistory = JSFUtil.getUserSession().getContactHistory();
        }
        return contactHistory;
    }

    public void setContactHistory(ContactHistory ContactHistory) {
        this.contactHistory = ContactHistory;
    }

    public Integer getDmcContactReasonId() {
        return dmcContactReasonId;
    }

    public void setDmcContactReasonId(Integer dmcContactReasonId) {
        this.dmcContactReasonId = dmcContactReasonId;
    }

    public List<ContactHistoryKnowledgeInfoValue> getKnowledgebaseInfoValues() {
        knowledgebaseInfoValues.clear();
        if(!JSFUtil.getUserSession().isOutboundCall()) {
            List<Knowledgebase> knowledgebases = JSFUtil.getUserSession().getKnowledgebases();
            for (Knowledgebase kb : knowledgebases) {
                ContactHistoryKnowledgeInfoValue kbInfoValue = new ContactHistoryKnowledgeInfoValue(kb, Boolean.FALSE, "");
                knowledgebaseInfoValues.add(kbInfoValue);
            }
        }
        return knowledgebaseInfoValues;
    }

    public void setKnowledgebaseInfoValues(List<ContactHistoryKnowledgeInfoValue> knowledgebaseInfoValues) {
        this.knowledgebaseInfoValues = knowledgebaseInfoValues;
    }

    public List<ContactHistoryProductInfoValue> getProductInfoValues() {
        /*List<Product> products = JSFUtil.getUserSession().getProducts();
        productInfoValues.clear();
        for (Product p : products) {
            ContactHistoryProductInfoValue productInfoValue = new ContactHistoryProductInfoValue(p, Boolean.FALSE, "");
            productInfoValues.add(productInfoValue);
        }*/
        return productInfoValues;
    }

    public void setProductInfoValues(List<ContactHistoryProductInfoValue> productInfoValues) {
        this.productInfoValues = productInfoValues;
    }

    public Integer getContactResultId() {
        return contactResultId;
    }

    public void setContactResultId(Integer contactResultId) {
        this.contactResultId = contactResultId;

//        if (contactResultId!=null) {
//            contactResult = this.getContactResultDAO().findContactResult(contactResultId);
//            contactResultCode = contactResult.getCode();
//        } else {
//            contactResult = null;
//            contactResultCode = "";
//        }
    }
    
    

    public ContactResult getContactResult() {
        return contactResult;
    }

    

    public List<ContactResult> getContactResultList() {
        return contactResultList;
    }

    public List<NosaleReason> getNosaleReasonList() {
        return nosaleReasonList;
    }

    public List<FollowupsaleReason> getFollowupsaleReasonList() {
        return followupsaleReasonList;
    }

    //Managed Properies
    public ChannelDAO getChannelDAO() {
        return channelDAO;
    }

    public void setChannelDAO(ChannelDAO channelDAO) {
        this.channelDAO = channelDAO;
    }

    public DmccontactReasonDAO getDmccontactReasonDAO() {
        return dmccontactReasonDAO;
    }

    public void setDmccontactReasonDAO(DmccontactReasonDAO dmccontactReasonDAO) {
        this.dmccontactReasonDAO = dmccontactReasonDAO;
    }

    public ContactHistoryDAO getContactHistoryDAO() {
        return contactHistoryDAO;
    }

    public void setContactHistoryDAO(ContactHistoryDAO contactHistoryDAO) {
        this.contactHistoryDAO = contactHistoryDAO;
    }

    public ContactCaseDAO getContactCaseDAO() {
        return contactCaseDAO;
    }

    public void setContactCaseDAO(ContactCaseDAO contactCaseDAO) {
        this.contactCaseDAO = contactCaseDAO;
    }

    public PurchaseOrderDAO getPurchaseOrderDAO() {
        return purchaseOrderDAO;
    }

    public void setPurchaseOrderDAO(PurchaseOrderDAO purchaseOrderDAO) {
        this.purchaseOrderDAO = purchaseOrderDAO;
    }

    public CustomerDAO getCustomerDAO() {
        return customerDAO;
    }

    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public AssignmentDetailDAO getAssignmentDetailDAO() {
        return assignmentDetailDAO;
    }

    public void setAssignmentDetailDAO(AssignmentDetailDAO assignmentDetailDAO) {
        this.assignmentDetailDAO = assignmentDetailDAO;
    }

    public ContactResultDAO getContactResultDAO() {
        return contactResultDAO;
    }

    public void setContactResultDAO(ContactResultDAO contactResultDAO) {
        this.contactResultDAO = contactResultDAO;
    }

    public AssignmentDAO getAssignmentDAO() {
        return assignmentDAO;
    }

    public void setAssignmentDAO(AssignmentDAO assignmentDAO) {
        this.assignmentDAO = assignmentDAO;
    }

    public ContactSummaryDAO getContactSummaryDAO() {
        return contactSummaryDAO;
    }

    public void setContactSummaryDAO(ContactSummaryDAO contactSummaryDAO) {
        this.contactSummaryDAO = contactSummaryDAO;
    }

    public FollowupsaleReasonDAO getFollowupsaleReasonDAO() {
        return followupsaleReasonDAO;
    }

    public void setFollowupsaleReasonDAO(FollowupsaleReasonDAO followupsaleReasonDAO) {
        this.followupsaleReasonDAO = followupsaleReasonDAO;
    }

    public NosaleReasonDAO getNosaleReasonDAO() {
        return nosaleReasonDAO;
    }

    public void setNosaleReasonDAO(NosaleReasonDAO nosaleReasonDAO) {
        this.nosaleReasonDAO = nosaleReasonDAO;
    }

    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public Date getFollowupDate() {
        return followupDate;
    }

    public void setFollowupDate(Date followupDate) {
        this.followupDate = followupDate;
    }

    public boolean isShowFollowupDate() {
        showFollowupDate = false;
        try{
        if(JSFUtil.getUserSession().getContactHistory() != null){
            contactHistory = JSFUtil.getUserSession().getContactHistory();
        }
        if(contactHistory != null 
                && contactHistory.getContactResult().getContactStatus() != null 
                && (contactHistory.getContactResult().getContactStatus().equalsIgnoreCase("DMC") 
                || contactHistory.getContactResult().getContactStatus().equalsIgnoreCase("DMCNotOffer"))){
            showFollowupDate = true;
        }
        }catch(Exception e){
        }
        //if(JSFUtil.getUserSession().getHasProducts() || JSFUtil.getUserSession().getHasPurchaseOrders()){
        //    showFollowupDate = false;
        //} else if (JSFUtil.getUserSession().isInboundCall()) {
        //    if (!JSFUtil.getUserSession().isOutboundCall()) {
        //        showFollowupDate = false;
        //    }
        //}
        return showFollowupDate;
    }

    public void setShowFollowupDate(boolean showFollowupDate) {
        this.showFollowupDate = showFollowupDate;
    }

    public String getContactResultCode() {
        return contactResultCode;
    }

    public void setContactResultCode(String contactResultCode) {
        this.contactResultCode = contactResultCode;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public boolean isContactClose() {
        return contactClose;
    }

    public void setContactClose(boolean contactClose) {
        this.contactClose = contactClose;
    }

    public Date getContactDate() {
        if(contactDate == null && JSFUtil.getUserSession().getContactHistory() != null && JSFUtil.getUserSession().getContactHistory().getContactDate() != null){
            contactDate = JSFUtil.getUserSession().getContactHistory().getContactDate();
        }
        return contactDate;
    }

    public void setContactDate(Date contactDate) {
        this.contactDate = contactDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public MarketingCustomerDAO getMarketingCustomerDAO() {
        return marketingCustomerDAO;
    }

    public void setMarketingCustomerDAO(MarketingCustomerDAO marketingCustomerDAO) {
        this.marketingCustomerDAO = marketingCustomerDAO;
    }

    public MarketingDAO getMarketingDAO() {
        return marketingDAO;
    }

    public void setMarketingDAO(MarketingDAO marketingDAO) {
        this.marketingDAO = marketingDAO;
    }

    public CampaignDAO getCampaignDAO() {
        return campaignDAO;
    }

    public void setCampaignDAO(CampaignDAO campaignDAO) {
        this.campaignDAO = campaignDAO;
    }
    
    public String getCloseReason() {
        return closeReason;
    }

    public void setCloseReason(String closeReason) {
        this.closeReason = closeReason;
    }   

    public TelephonyService getTelephonyService() {
        return telephonyService;
    }

    public void setTelephonyService(TelephonyService telephonyService) {
        this.telephonyService = telephonyService;
    }
    
    public boolean isCallReady() {
        return callReady;
    }

    public void setCallReady(boolean callReady) {
        this.callReady = callReady;
    }
    
}
