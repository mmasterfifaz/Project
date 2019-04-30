/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.front.campaign;

import com.maxelyz.core.controller.UserSession;
import com.maxelyz.core.controller.common.BaseController;
import com.maxelyz.core.model.dao.*;
import com.maxelyz.core.model.dao.front.campaign.CampaignDAO;
import com.maxelyz.core.model.dao.front.customerHandling.CustomerHandlingDAO;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.core.model.value.admin.ContactRecordValue;
import com.maxelyz.core.model.value.admin.UserAssignmentValue;
import com.maxelyz.core.model.value.front.campaign.CampaignValue;
import com.maxelyz.core.model.value.front.campaign.NewsUserValue;
import com.maxelyz.core.model.value.front.campaign.PendingListInfoValue;
import com.maxelyz.core.model.value.front.customerHandling.CustomerInfoValue;
import com.maxelyz.core.service.CampaignService;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author Prawait
 */
@ManagedBean(name = "campaignFrontController")
@ViewScoped
public class CampaignController extends BaseController {

    private static Logger logger = Logger.getLogger(CampaignController.class);
    private static String ASSIGNMENT_PAGE = "assignmentList";
    private static String OPENED_PAGE = "openedList";
    private static String FOLLOWUP_PAGE = "followupList";
    private static String CLOSED_PAGE = "closedList";
    private static String PENDING_PAGE = "pendingList";
    private List<PendingListInfoValue> purchaseOrderList;
    private List<CampaignValue> assignmentList;
    private List<CampaignValue> openedList;
    private List<CampaignValue> toFollowUpList;
    private List<AssignmentDetail> closedList;
    private List<ContactRecordValue> contactHistoryList;
    private List<NewsUserValue> newsList;
    private News news;
    private List<CampaignValue> dataList;
    private Integer customerId;
    private String txtSearch = "";
    private Date assignedDate;
    private Date followupDate;
    private Double monthlyPremium;

    //Lop
    private Date assignedStartDate;
    private Date assignedEndDate;
    private String customerType = "";
    private String customerName = "";
    private String campaignName = "";
    private Date followupStartDate;
    private Date followupEndDate;
    private String ageStart = "";
    private String ageEnd = "";
    private Date lastestStartDate;
    private Date lastestEndDate;
    private Integer contactResultId = 0;
    private String remark = "";
    private String refNo = "";
    private Date purchaseStartDate;
    private Date purchaseEndDate;
    private String lastReason = "";
    private Date updateStartDate;
    private Date updateEndDate;
    private String contactHistory = "";
    private String approvalStatus = "";

                
    //rendering
    private boolean renderedAssignedDate = false;
    private boolean renderedCustomerName = false;
    private boolean renderedCustomerType = false;
    private boolean renderedGender = false;
    private boolean renderedAge = false;
    private boolean renderedCampaign = false;

    private boolean renderedApprovalStatus = false;
    private boolean renderedContactResult = false;
    private boolean renderedLastContact = false;
    private boolean renderedContactHistory = false;
    private boolean renderedRemark = false;

    private boolean renderedRefNo = false;
    private boolean renderedPurchaseDate = false;
    private boolean renderedUnderWriting = false;
    private boolean renderedPaymentStatus = false;
    private boolean renderedQCStatus = false;
    private boolean renderedLastReason = false;

    private boolean renderedUpdateDate = false;
    private boolean renderedSaleResult = false;

    //End Lop
    
    private String gender = "";
    private boolean renderedFollowupDate = false;
    private Date latestUpdated;
    private Integer contactableNo;
    private Integer uncontactableNo;
    private Integer listUsedNo;
    private Integer yesSaleNo;
    private Integer noSaleNo;
    private Integer saleApprovalNo;
    private RptSalePerformance1 rpt;
    private String note;
    private String saleResult = "";
    private Integer countAll=0,countReject=0,countUw=0,countPayment=0,countQc=0,countConfirm=0;
    private int page = 1;
    private Integer rows = 0;
    private String subPage = "";
    private Integer subCurrentPage = 1;
    private boolean supervisorViewListMode = false;

    private Boolean enableConfirm = false;
    private Product product;
    private String province = "";
    private List<AssignmentDetail> followUpSaleList;
    private Date blinkFollowUpSale;

    @ManagedProperty(value = "#{campaignFrontDAO}")
    private CampaignDAO campaignDAO;
    @ManagedProperty(value = "#{assignmentDetailDAO}")
    private AssignmentDetailDAO assignmentDetailDAO;
    @ManagedProperty(value = "#{contactHistoryDAO}")
    private ContactHistoryDAO contactHistoryDAO;
    @ManagedProperty(value = "#{purchaseOrderDAO}")
    private PurchaseOrderDAO purchaseOrderDAO;
    @ManagedProperty(value = "#{rptSalePerformanceDAO}")
    private RptSalePerformanceDAO rptSalePerformanceDAO;
    @ManagedProperty(value = "#{usersDAO}")
    private UsersDAO usersDAO;
    @ManagedProperty(value = "#{newsDAO}")
    private NewsDAO newsDAO;
    @ManagedProperty(value = "#{assignmentDAO}")
    private AssignmentDAO assignmentDAO;
    @ManagedProperty(value = "#{campaignService}")
    private CampaignService campaignService;
    @ManagedProperty(value = "#{customerHandlingDAO}")
    private CustomerHandlingDAO customerHandlingDAO;
    @ManagedProperty(value = "#{contactResultDAO}")
    private ContactResultDAO contactResultDAO;
    @ManagedProperty(value = "#{productDAO}")
    private ProductDAO productDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("campaign:view")) {
            SecurityUtil.redirectUnauthorize();
        }
        JSFUtil.getUserSession().setCustomerDetail(null);
        JSFUtil.getUserSession().setFirstPage("campaign");
        JSFUtil.getUserSession().setMainPage("campaign");
        JSFUtil.getUserSession().resetProducts();
        JSFUtil.getUserSession().resetPurchaseOrders();
        campaignService.createAssignmentDetailFromPooling(JSFUtil.getUserSession().getUsers());
        //followupStartDate = JSFUtil.toDateWithoutTime(new Date());
        //followupEndDate = JSFUtil.toDateWithMaxTime(new Date());
        subCurrentPage = 1;
        searchAction();
        initContactRecord();

        UserSession userSession = JSFUtil.getUserSession();
        note = userSession.getUsers().getMakenote();
        newsList = newsDAO.findNewsByUser(userSession.getUsers());

        String userRoleStrs = userSession.getUserGroup().getRole().toUpperCase();
        if (userRoleStrs.contains("CONFIRM")) {
            enableConfirm = true;
        }
        supervisorViewListMode = userRoleStrs.contains("SUPERVISOR");

        DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        followUpSaleList = assignmentDetailDAO.findFollowUpSaleDate(sdf.format(date), userSession.getUsers().getId());
    }

    public String checkBlinkDate(Date followupsaleDate){
        DateFormat df = new SimpleDateFormat("HHmm");
        Date now = new Date();
        Integer realTime = Integer.parseInt(df.format(now));
        Integer followDate = Integer.parseInt(df.format(followupsaleDate));
        Integer followDateBefore = followDate - 15;
        String check = String.valueOf(followDateBefore);
        if(check.length() == 4){
            if(Integer.parseInt(check.substring(2)) >= 60){
                followDateBefore -= 40;
            }
        }
        else if (check.length() == 3) {
            if(Integer.parseInt(check.substring(1)) >= 60){
                followDateBefore -= 40;
            }
        }
        String blink;
        if((realTime <= followDate && realTime >= followDateBefore) || realTime > followDate){
            blink = "true";
        } else {
            blink = "false";
        }
        return blink;
    }

    public void initContactRecord() {
        UserSession userSession = JSFUtil.getUserSession();
        //Integer userId = userSession.getUsers().getId();
        rpt = rptSalePerformanceDAO.findRptSalePerformance(userSession.getUsers());
        monthlyPremium = purchaseOrderDAO.findMonthlyPremium(userSession.getUsers());
        note = userSession.getUsers().getMakenote();

    }

    public void contactHistoryListener(ActionEvent event) {
        customerId = Integer.parseInt(JSFUtil.getRequestParameterMap("customerId"));
        Integer adId = Integer.parseInt(JSFUtil.getRequestParameterMap("adId"));
        if (customerId != null && adId != null) {
            contactHistoryList = contactHistoryDAO.findByAssignmentDetail(adId);
        }
    }

    public void searchActionListener(ActionEvent event) {
        page = 1;
        subPage = JSFUtil.getRequestParameterMap("subPage");
        searchAction();
    }

    public void searchListener() {
        page = 1;
        subCurrentPage = 1;
        subPage = JSFUtil.getRequestParameterMap("subPage");
        searchAction();
    }

    private void searchAction() {
        int itemPerPage = JSFUtil.getApplication().getMaxrows();
        int firstResult = ((page - 1) * itemPerPage);
        List<PendingListInfoValue> list = null;
        String page = JSFUtil.getRequest().getRequestURI();
        String mode = "AS";
        if (page.indexOf(ASSIGNMENT_PAGE) != -1) {
            mode = "AS";
            renderedCampaign = true;
            renderedCustomerName = true;
            renderedCustomerType = true;
            renderedGender = true;
            renderedAge = true;
            renderedAssignedDate = true;
            JSFUtil.getUserSession().setFromTab("assignmentList");
        } else if (page.indexOf(OPENED_PAGE) != -1) {
            mode = "OP";
            renderedCampaign = true;
            renderedCustomerName = true;
            renderedCustomerType = true;
            renderedGender = true;
            renderedAge = true;
            renderedAssignedDate = true;
            renderedContactHistory = true;
            JSFUtil.getUserSession().setFromTab("openedList");
        } else if (page.indexOf(FOLLOWUP_PAGE) != -1) {
            mode = "FL";
            renderedCampaign = true;
            renderedCustomerName = true;
            renderedFollowupDate = true;
            renderedApprovalStatus = true;
            renderedLastContact = true;
            renderedContactResult = true;
            renderedRemark = true;
            renderedContactHistory = true;
            JSFUtil.getUserSession().setFromTab("followupList");
        } else if (page.indexOf(CLOSED_PAGE) != -1) {
            mode = "CL";
            renderedCampaign = true;
            renderedCustomerName = true;
            renderedAssignedDate = true;
            renderedUpdateDate = true;
            renderedSaleResult = true;
            renderedContactHistory = true;
            JSFUtil.getUserSession().setFromTab("closedList");
        } else if (page.indexOf(PENDING_PAGE) != -1) {
            mode = "PDC";
            renderedCampaign = true;
            renderedRefNo = true;
            renderedPurchaseDate = true;
            renderedCustomerName = true;
            renderedUnderWriting = true;
            renderedPaymentStatus = true;
            renderedQCStatus = true;
            renderedLastReason = true;
            renderedRemark = true;
            JSFUtil.getUserSession().setFromTab("pendingList");
            if(subPage == null) subPage = "";
            countAll = purchaseOrderDAO.countPending("", JSFUtil.getUserSession().getUsers(),"");
            countReject = purchaseOrderDAO.countPending("reject", JSFUtil.getUserSession().getUsers(),"");
            countUw = purchaseOrderDAO.countPending("uw", JSFUtil.getUserSession().getUsers(),"");
            countPayment = purchaseOrderDAO.countPending("payment", JSFUtil.getUserSession().getUsers(),"");
            countQc = purchaseOrderDAO.countPending("qc", JSFUtil.getUserSession().getUsers(),"");
            countConfirm = purchaseOrderDAO.countPending("confirm", JSFUtil.getUserSession().getUsers(),"");
            // BEGIN : 2015/11/13 Fixed paging on pending tab at database level  , this method will invoked again when user click page , then data will be query following firstResult and max rows in page 
            subCurrentPage = (subCurrentPage==null)?1:subCurrentPage;
            firstResult = ((subCurrentPage - 1) * itemPerPage);
            // END : 2015/11/13 

        }
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.US);
        String strStartDate = "";
        String strEndDate = "";
        String strFLStartDate = "";
        String strFLEndDate = "";
        String sLastestStartDate = "";
        String sLastestEndDate = "";
        String sPurchaseStartDate = "";
        String sPurchaseEndDate = "";
        String sUpdateStartDate = "";
        String sUpdateEndDate = "";
        if (updateStartDate != null) {
            sUpdateStartDate = dateFormat.format(updateStartDate);
        }
        if (updateEndDate != null) {
            sUpdateEndDate = dateFormat.format(updateEndDate);
        }
        if (purchaseStartDate != null) {
            sPurchaseStartDate = dateFormat.format(purchaseStartDate);
        }
        if (purchaseEndDate != null) {
            sPurchaseEndDate = dateFormat.format(purchaseEndDate);
        }
        if (lastestStartDate != null) {
            sLastestStartDate = dateFormat.format(lastestStartDate);
        }
        if (lastestEndDate != null) {
            sLastestEndDate = dateFormat.format(lastestEndDate);
        }
        if (assignedStartDate != null) {
            strStartDate = dateFormat.format(assignedStartDate);
        }
        if (assignedEndDate != null) {
            strEndDate = dateFormat.format(assignedEndDate);
        }
        if (followupStartDate != null) {
            strFLStartDate = dateFormat.format(followupStartDate);
        }
        if (followupEndDate != null) {
            strFLEndDate = dateFormat.format(followupEndDate);
        }
        if (mode.equals("PDC")) {
            String other_where = "";

            if (!sPurchaseStartDate.equals("") && !sPurchaseEndDate.equals("")) {
                other_where += " and CONVERT(varchar(8), po.purchaseDate, 112) between " + sPurchaseStartDate + " and " + sPurchaseEndDate + " ";
            }
            if (!txtSearch.equals("")) {
                other_where += " and po.assignmentDetail.customerName like '%" + txtSearch + "%' ";
            }
            if (!customerName.equals("")) {
                other_where += " and po.assignmentDetail.customerName like '%" + customerName + "%' ";
            }
            if (!campaignName.equals("")) {
                other_where += " and po.assignmentDetail.campaignName like '%" + campaignName + "%' ";
            }
            if (!refNo.equals("")) {
                other_where += " and (po.refNo like '%" + refNo + "%' or po.refNo2 like '%" + refNo + "%') ";
            }
            if (!lastReason.equals("")) {
                other_where += " and po.latestReason like '%" + lastReason + "%' ";
            }
            if (!remark.equals("")) {
                other_where += " and po.remark like '%" + remark + "%' ";
            }
            if (!approvalStatus.equals("")) {
                other_where += " and po.qcStatus like '%" + approvalStatus + "%' ";
            }
//            list = purchaseOrderDAO.findPending(subPage, JSFUtil.getUserSession().getUsers(), firstResult, itemPerPage, other_where,JSFUtil.getUserSession().getFromTab());
//            rows = purchaseOrderDAO.countPending(subPage, JSFUtil.getUserSession().getUsers(), other_where);
//            purchaseOrderList = (list != null) ? new PagedPurchaseOrderList(list, rows, itemPerPage) : null;

            purchaseOrderList = purchaseOrderDAO.findPending(subPage, JSFUtil.getUserSession().getUsers(), firstResult, 1000, other_where, JSFUtil.getUserSession().getFromTab());
        } else {
            //dataList = campaignDAO.findCampaignValue(mode, getLoginUser().getId(), 0, 1000, txtSearch, strASDate, strFLDate, gender, saleResult);
            dataList = campaignDAO.findCampaignValue(mode,
                    getLoginUser().getId(),
                    0,
                    1000,
                    txtSearch,
                    strStartDate,
                    strEndDate,
                    gender,
                    saleResult,
                    strFLStartDate,
                    strFLEndDate,
                    customerType,
                    customerName,
                    campaignName,
                    ageStart,
                    ageEnd,
                    approvalStatus,
                    sLastestStartDate,
                    sLastestEndDate,
                    contactResultId,
                    remark,
                    sUpdateStartDate,
                    sUpdateEndDate);
        }
        if (mode.equals("CL")) {
//            toClosedList();
        }

    }

    private void toClosedList() {
        List<CampaignValue> list = new ArrayList<CampaignValue>();
        for (CampaignValue cv : dataList) {
            Collection<PurchaseOrder> poCollection = purchaseOrderDAO.findByAssignmentDetail(cv.getAssignmentDetailId());
            cv.setPurchaseOrderCollection(poCollection);
            list.add(cv);
        }
        dataList = list;
    }

    public void saveMakeNoteListener() {
        Users users = JSFUtil.getUserSession().getUsers();
        //users.setMakenote(note);
        //usersDAO.edit(users);
        usersDAO.updateNote(users.getId(), note);
        JSFUtil.getUserSession().getUsers().setMakenote(note);
    }

    public void newsListener(ActionEvent event) {
        Integer newsId = Integer.parseInt(JSFUtil.getRequestParameterMap("newsId"));
        news = newsDAO.findNews(newsId);
        Users user = JSFUtil.getUserSession().getUsers();
        //update read news
        NewsUserPK newsUserPK = new NewsUserPK();
        newsUserPK.setNewsId(newsId);
        newsUserPK.setUsersId(user.getId());
        try {
            newsDAO.updateViewStatus(newsUserPK);
        } catch (Exception e) {
            log.error(e);
        }
        newsList = newsDAO.findNewsByUser(user);
        FacesContext.getCurrentInstance().renderResponse();
    }

    public String toSaleApprove() {

        if (JSFUtil.getRequestParameterMap("poId") != null && !JSFUtil.getRequestParameterMap("poId").equals("")) {
            Integer poId = Integer.parseInt(JSFUtil.getRequestParameterMap("poId"));
            if (poId != null && poId != 0) {
                PurchaseOrder po = purchaseOrderDAO.findPurchaseOrder(poId);
                if (po != null) {
                    Integer customerId = po.getCustomer().getId();
                    CustomerInfoValue customerInfoValue = customerHandlingDAO.findCustomerHandling(customerId);
                    JSFUtil.getUserSession().setCustomerDetail(customerInfoValue);

                    //RESET REFERENCE YES SALE AND GET NEW YES REF
                    JSFUtil.getUserSession().setRefYes(null);
                    if (po.getSaleResult() == 'Y') {
                        JSFUtil.getUserSession().setRefYes(po.getRefNo());
                    }

                    // SET ASSIGNMENT DETAIL TO USER SESSION FOR CUSTOMER INFO LAYOUT TEMPLATE
                    if (po.getAssignmentDetail() != null) {
                        JSFUtil.getUserSession().setAssignmentDetail(po.getAssignmentDetail());
                    }
                    Integer productId = Integer.parseInt(JSFUtil.getRequestParameterMap("productId"));
                    Integer contactResultPlanId = Integer.parseInt(JSFUtil.getRequestParameterMap("contactResultPlanId"));
                    product = productDAO.findProduct(productId);
//        When click product name then add product into session
                    if (JSFUtil.getUserSession().getProducts() != null) {
                        UserSession u = JSFUtil.getUserSession();
                        //JSFUtil.getUserSession().setProducts(product);
                        u.setContactResultPlanId(contactResultPlanId);
                        u.setPurchaseOrderId(poId);
                    }
                }
            }
        }

        return "/front/customerHandling/saleapprove.xhtml";
    }

    public String toSaleHistory() {
        String custID = JSFUtil.getRequestParameterMap("selectedId");
        if (custID != null && !custID.equals("")) {
            CustomerInfoValue customerInfoValue = customerHandlingDAO.findCustomerHandling(Integer.parseInt(custID));
            JSFUtil.getUserSession().setCustomerDetail(customerInfoValue);
        }

        // SET ASSIGNMENT DETAIL TO USER SESSION FOR CUSTOMER INFO LAYOUT TEMPLATE
        if (JSFUtil.getRequestParameterMap("assignmentDetailId") != null) {
            Integer assignmentDetailId = Integer.parseInt(JSFUtil.getRequestParameterMap("assignmentDetailId"));
            if (assignmentDetailId != null && assignmentDetailId != 0) {
                AssignmentDetail assignmentDetail = assignmentDetailDAO.findAssignmentDetail(assignmentDetailId);
                JSFUtil.getUserSession().setAssignmentDetail(assignmentDetail);//oat add
            }
        }

        return "/front/customerHandling/saleHistory.xhtml";
    }
    /**
     * @return the assignmentList
     */
    public List<CampaignValue> getAssignmentList() {

//        setAssignmentList(campaignDAO.findAssignment(getLoginUser().getId(), 0, 1000));
        return assignmentList;
    }

    /**
     * @param assignmentList the assignmentList to set
     */
    public void setAssignmentList(List<CampaignValue> assignmentList) {
        this.assignmentList = assignmentList;
    }

    /**
     * @return the campaignDAO
     */
    public CampaignDAO getCampaignDAO() {
        return campaignDAO;
    }

    /**
     * @param campaignDAO the campaignDAO to set
     */
    public void setCampaignDAO(CampaignDAO campaignDAO) {
        this.campaignDAO = campaignDAO;
    }

    /**
     * @return the openedList
     */
    public List<CampaignValue> getOpenedList() {
//        setOpenedList(campaignDAO.findOpenedAssignment(getLoginUser().getId(), 0, 1000));
        return openedList;
    }

    /**
     * @param openedList the openedList to set
     */
    public void setOpenedList(List<CampaignValue> openedList) {
        this.openedList = openedList;
    }

    /**
     * @return the toFollowUpList
     */
    public List<CampaignValue> getToFollowUpList() {
//        setToFollowUpList(campaignDAO.findFollowupAssignment(getLoginUser().getId(), 0, 1000));
        return toFollowUpList;
    }

    /**
     * @param toFollowUpList the toFollowUpList to set
     */
    public void setToFollowUpList(List<CampaignValue> toFollowUpList) {
        this.toFollowUpList = toFollowUpList;
    }

    /**
     * @return the closedList
     */
    public List<AssignmentDetail> getClosedList() {
//        setClosedList(campaignDAO.findClosedAssignment(getLoginUser().getId(), 0, 1000, txtSearch));
        return closedList;
    }

    /**
     * @param closedList the closedList to set
     */
    public void setClosedList(List<AssignmentDetail> closedList) {
        this.closedList = closedList;
    }

    public List<ContactRecordValue> getContactHistoryList() {
        return contactHistoryList;
    }

    public void setContactHistoryList(List<ContactRecordValue> contactHistoryList) {
        this.contactHistoryList = contactHistoryList;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public AssignmentDetailDAO getAssignmentDetailDAO() {
        return assignmentDetailDAO;
    }

    public void setAssignmentDetailDAO(AssignmentDetailDAO assignmentDetailDAO) {
        this.assignmentDetailDAO = assignmentDetailDAO;
    }

    public ContactHistoryDAO getContactHistoryDAO() {
        return contactHistoryDAO;
    }

    public void setContactHistoryDAO(ContactHistoryDAO contactHistoryDAO) {
        this.contactHistoryDAO = contactHistoryDAO;
    }

    public String getTxtSearch() {
        return txtSearch;
    }

    public void setTxtSearch(String txtSearch) {
        this.txtSearch = txtSearch;
    }

    public List<CampaignValue> getDataList() {
        return dataList;
    }

    public void setDataList(List<CampaignValue> dataList) {
        this.dataList = dataList;
    }

    public PurchaseOrderDAO getPurchaseOrderDAO() {
        return purchaseOrderDAO;
    }

    public void setPurchaseOrderDAO(PurchaseOrderDAO purchaseOrderDAO) {
        this.purchaseOrderDAO = purchaseOrderDAO;
    }

    public Date getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(Date assignedDate) {
        this.assignedDate = assignedDate;
    }

    public ContactResultDAO getContactResultDAO() {
        return contactResultDAO;
    }

    public void setContactResultDAO(ContactResultDAO contactResultDAO) {
        this.contactResultDAO = contactResultDAO;
    }

    
    //Lop
   
    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public Date getUpdateStartDate() {
        return updateStartDate;
    }

    public void setUpdateStartDate(Date updateStartDate) {
        this.updateStartDate = updateStartDate;
    }

    public Date getUpdateEndDate() {
        return updateEndDate;
    }

    public void setUpdateEndDate(Date updateEndDate) {
        this.updateEndDate = updateEndDate;
    }

    String getContactHistory() {
        return contactHistory;
    }

    public void setcontactHistory(String contactHistory) {
        this.contactHistory = contactHistory;
    }

    public String getLastReason() {
        return lastReason;
    }

    public void setLastReason(String lastReason) {
        this.lastReason = lastReason;
    }

    public Date getPurchaseStartDate() {
        return purchaseStartDate;
    }

    public void setPurchaseStartDate(Date purchaseStartDate) {
        this.purchaseStartDate = purchaseStartDate;
    }

    public Date getPurchaseEndDate() {
        return purchaseEndDate;
    }

    public void setPurchaseEndDate(Date purchaseEndDate) {
        this.purchaseEndDate = purchaseEndDate;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Map<String, Integer> getContactResultList() {
        return this.getContactResultDAO().getFollowContactResult();
    }

    public Integer getContactResultId() {
        return contactResultId;
    }

    public void setContactResultId(Integer contactResultId) {
        this.contactResultId = contactResultId;
    }

    public Date getLastestStartDate() {
        return lastestStartDate;
    }

    public void setLastestStartDate(Date lastestStartDate) {
        this.lastestStartDate = lastestStartDate;
    }

    public Date getLastestEndDate() {
        return lastestEndDate;
    }

    public void setLastestEndDate(Date lastestEndDate) {
        this.lastestEndDate = lastestEndDate;
    }

    public Date getFollowupStartDate() {
        return followupStartDate;
    }

    public void setFollowupStartDate(Date followupStartDate) {
        this.followupStartDate = followupStartDate;
    }

    public Date getFollowupEndDate() {
        return followupEndDate;
    }

    public void setFollowupEndDate(Date followupEndDate) {
        this.followupEndDate = followupEndDate;
    }

    
    public Date getAssignedStartDate() {
        return assignedStartDate;
    }

    public void setAssignedStartDate(Date assignedStartDate) {
        this.assignedStartDate = assignedStartDate;
    }

    public Date getAssignedEndDate() {
        return assignedEndDate;
    }

    public void setAssignedEndDate(Date assignedEndDate) {
        this.assignedEndDate = assignedEndDate;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getAgeStart() {
        return ageStart;
    }

    public void setAgeStart(String ageStart) {
        this.ageStart = ageStart;
    }

    public String getAgeEnd() {
        return ageEnd;
    }

    public void setAgeEnd(String ageEnd) {
        this.ageEnd = ageEnd;
    }
    //Rendered

    public boolean isRenderedUpdateDate() {
        return renderedUpdateDate;
    }

    public void setRenderedUpdateDate(boolean renderedUpdateDate) {
        this.renderedUpdateDate = renderedUpdateDate;
    }

    public boolean isRenderedSaleResult() {
        return renderedSaleResult;
    }

    public void setRenderedSaleResult(boolean renderedSaleResult) {
        this.renderedSaleResult = renderedSaleResult;
    }

    public boolean isRenderedCustomerName() {
        return renderedCustomerName;
    }

    public void setRenderedCustomerName(boolean renderedCustomerName) {
        this.renderedCustomerName = renderedCustomerName;
    }

    public boolean isRenderedAssignedDate() {
        return renderedAssignedDate;
    }

    public void setRenderedAssignedDate(boolean renderedAssignedDate) {
        this.renderedAssignedDate = renderedAssignedDate;
    }

    public boolean isRenderedCustomerType() {
        return renderedCustomerType;
    }

    public void setRenderedCustomerType(boolean renderedCustomerType) {
        this.renderedCustomerType = renderedCustomerType;
    }

    public boolean isRenderedGender() {
        return renderedGender;
    }

    public void setRenderedGender(boolean renderedGender) {
        this.renderedGender = renderedGender;
    }

    public boolean isRenderedAge() {
        return renderedAge;
    }

    public void setRenderedAge(boolean renderedAge) {
        this.renderedAge = renderedAge;
    }

    public boolean isRenderedCampaign() {
        return renderedCampaign;
    }

    public void setRenderedCampaign(boolean renderedCampaign) {
        this.renderedCampaign = renderedCampaign;
    }

    public boolean isRenderedApprovalStatus() {
        return renderedApprovalStatus;
    }

    public void setRenderedApprovalStatus(boolean renderedApprovalStatus) {
        this.renderedApprovalStatus = renderedApprovalStatus;
    }

    public boolean isRenderedContactResult() {
        return renderedContactResult;
    }

    public void setRenderedContactResult(boolean renderedContactResult) {
        this.renderedContactResult = renderedContactResult;
    }

    public boolean isRenderedLastContact() {
        return renderedLastContact;
    }

    public void setRenderedLastContact(boolean renderedLastContact) {
        this.renderedLastContact = renderedLastContact;
    }

    public boolean isRenderedContactHistory() {
        return renderedContactHistory;
    }

    public void setRenderedContactHistory(boolean renderedContactHistory) {
        this.renderedContactHistory = renderedContactHistory;
    }

    public boolean isRenderedRemark() {
        return renderedRemark;
    }

    public void setRenderedRemark(boolean renderedRemark) {
        this.renderedRemark = renderedRemark;
    }

    public boolean isRenderedRefNo() {
        return renderedRefNo;
    }

    public void setRenderedRefNo(boolean renderedRefNo) {
        this.renderedRefNo = renderedRefNo;
    }

    public boolean isRenderedPurchaseDate() {
        return renderedPurchaseDate;
    }

    public void setRenderedPurchaseDate(boolean renderedPurchaseDate) {
        this.renderedPurchaseDate = renderedPurchaseDate;
    }

    public boolean isRenderedUnderWriting() {
        return renderedUnderWriting;
    }

    public void setRenderedUnderWriting(boolean renderedUnderWriting) {
        this.renderedUnderWriting = renderedUnderWriting;
    }

    public boolean isRenderedPaymentStatus() {
        return renderedPaymentStatus;
    }

    public void setRenderedPaymentStatus(boolean renderedPaymentStatus) {
        this.renderedPaymentStatus = renderedPaymentStatus;
    }

    public boolean isRenderedQCStatus() {
        return renderedQCStatus;
    }

    public void setRenderedQCStatus(boolean renderedQCStatus) {
        this.renderedQCStatus  = renderedQCStatus;
    }

    public boolean isRenderedLastReason() {
        return renderedLastReason;
    }

    public void setRenderedLastReason (boolean renderedLastReason) {
        this.renderedLastReason  = renderedLastReason;
    }

 
    //End Lop
    
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isRenderedFollowupDate() {
        return renderedFollowupDate;
    }

    public void setRenderedFollowupDate(boolean renderedFollowupDate) {
        this.renderedFollowupDate = renderedFollowupDate;
    }

    public Date getFollowupDate() {
        return followupDate;
    }

    public void setFollowupDate(Date followupDate) {
        this.followupDate = followupDate;
    }

    public Integer getContactableNo() {
        return contactableNo;
    }

    public void setContactableNo(Integer contactableNo) {
        this.contactableNo = contactableNo;
    }

    public Date getLatestUpdated() {
        return latestUpdated;
    }

    public void setLatestUpdated(Date latestUpdated) {
        this.latestUpdated = latestUpdated;
    }

    public Integer getListUsedNo() {
        return listUsedNo;
    }

    public void setListUsedNo(Integer listUsedNo) {
        this.listUsedNo = listUsedNo;
    }

    public Integer getNoSaleNo() {
        return noSaleNo;
    }

    public void setNoSaleNo(Integer noSaleNo) {
        this.noSaleNo = noSaleNo;
    }

    public Integer getSaleApprovalNo() {
        return saleApprovalNo;
    }

    public void setSaleApprovalNo(Integer saleApprovalNo) {
        this.saleApprovalNo = saleApprovalNo;
    }

    public Integer getUncontactableNo() {
        return uncontactableNo;
    }

    public void setUncontactableNo(Integer uncontactableNo) {
        this.uncontactableNo = uncontactableNo;
    }

    public Integer getYesSaleNo() {
        return yesSaleNo;
    }

    public void setYesSaleNo(Integer yesSaleNo) {
        this.yesSaleNo = yesSaleNo;
    }

    public RptSalePerformance1 getRpt() {
        return rpt;
    }

    public void setRpt(RptSalePerformance1 rpt) {
        this.rpt = rpt;
    }

    public RptSalePerformanceDAO getRptSalePerformanceDAO() {
        return rptSalePerformanceDAO;
    }

    public void setRptSalePerformanceDAO(RptSalePerformanceDAO rptSalePerformanceDAO) {
        this.rptSalePerformanceDAO = rptSalePerformanceDAO;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public UsersDAO getUsersDAO() {
        return usersDAO;
    }

    public void setUsersDAO(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public NewsDAO getNewsDAO() {
        return newsDAO;
    }

    public void setNewsDAO(NewsDAO newsDAO) {
        this.newsDAO = newsDAO;
    }

    public List<NewsUserValue> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<NewsUserValue> newsList) {
        this.newsList = newsList;
    }

    public String getSaleResult() {
        return saleResult;
    }

    public void setSaleResult(String saleResult) {
        this.saleResult = saleResult;
    }

    public AssignmentDAO getAssignmentDAO() {
        return assignmentDAO;
    }

    public void setAssignmentDAO(AssignmentDAO assignmentDAO) {
        this.assignmentDAO = assignmentDAO;
    }

    public CampaignService getCampaignService() {
        return campaignService;
    }

    public void setCampaignService(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public List<PendingListInfoValue> getPurchaseOrderList() {
        return purchaseOrderList;
    }

    public void setPurchaseOrderList(List<PendingListInfoValue> purchaseOrderList) {
        this.purchaseOrderList = purchaseOrderList;
    }

    public Integer getCountAll() {
        return countAll;
    }

    public void setCountAll(Integer countAll) {
        this.countAll = countAll;
    }

    public Integer getCountPayment() {
        return countPayment;
    }

    public void setCountPayment(Integer countPayment) {
        this.countPayment = countPayment;
    }

    public Integer getCountQc() {
        return countQc;
    }

    public void setCountQc(Integer countQc) {
        this.countQc = countQc;
    }

    public Integer getCountReject() {
        return countReject;
    }

    public void setCountReject(Integer countReject) {
        this.countReject = countReject;
    }

    public Integer getCountUw() {
        return countUw;
    }

    public void setCountUw(Integer countUw) {
        this.countUw = countUw;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
        this.searchAction();
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public String getSubPage() {
        return subPage;
    }

    public void setSubPage(String subPage) {
        this.subPage = subPage;
    }

    public CustomerHandlingDAO getCustomerHandlingDAO() {
        return customerHandlingDAO;
    }

    public void setCustomerHandlingDAO(CustomerHandlingDAO customerHandlingDAO) {
        this.customerHandlingDAO = customerHandlingDAO;
    }

    public Double getMonthlyPremium() {
        return monthlyPremium;
    }

    public void setMonthlyPremium(Double monthlyPremium) {
        this.monthlyPremium = monthlyPremium;
    }

    public Integer getCountConfirm() {
        return countConfirm;
    }

    public void setCountConfirm(Integer countConfirm) {
        this.countConfirm = countConfirm;
    }

    public Boolean getEnableConfirm() {
        return enableConfirm;
    }

    public void setEnableConfirm(Boolean enableConfirm) {
        this.enableConfirm = enableConfirm;
    }

    public Integer getSubCurrentPage() {
        return subCurrentPage;
    }

    public void setSubCurrentPage(Integer subCurrentPage) {
        this.subCurrentPage = subCurrentPage;
        searchAction();
    }

    public boolean isSupervisorViewListMode() {
        return supervisorViewListMode;
    }

    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<AssignmentDetail> getFollowUpSaleList() {
        return followUpSaleList;
    }

    public void setFollowUpSaleList(List<AssignmentDetail> followUpSaleList) {
        this.followUpSaleList = followUpSaleList;
    }

    public Date getBlinkFollowUpSale() {
        return blinkFollowUpSale;
    }

    public void setBlinkFollowUpSale(Date blinkFollowUpSale) {
        this.blinkFollowUpSale = blinkFollowUpSale;
    }

}
