package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.utils.FileUploadBean;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

@ManagedBean
@ViewScoped
public class MarketingEditController {

    private static Logger log = Logger.getLogger(MarketingEditController.class);
    private static String REDIRECT_PAGE = "marketing.jsf";
    private static String SUCCESS = "marketing.xhtml?faces-redirect=true";
    private static String FAILURE = "marketingedit.xhtml";
    private Marketing marketing;
    private String mode;
    //private String modePopup;
    private String message;
    private String messageDate="";
    private String messageDup="";
    private Integer prospectlistSponsorId;
    private Integer fileTemplateId;
    private FileUploadBean fileUploadBean = new FileUploadBean(JSFUtil.getuploadPath(), "temp");
    private String sessionId;
    private Integer step = 1;
    private Integer countNew = 0;
    private Integer countExist = 0;
    private Integer countDup = 0;
    
    private List<MarketingTempCustomer> mtcList;
    private List<String[]> mtcListString;
    private List<MarketingTempCustomer> listFilter;
    private List<MarketingCriteria> marketingCriteriaList = new ArrayList<MarketingCriteria>();
    private MarketingCriteria marketingCriteria = new MarketingCriteria();
    private List<String> selectedGenderList = new ArrayList<String>();    
    private Map<Integer, Boolean> selectedIds = new LinkedHashMap<Integer, Boolean>();
    private Map<String, String> provinceList;
    private List<String> selectedProvinceList = new ArrayList<String>();    
    private Map<String, Integer> occupationList;
    private List<Integer> selectedOccupationList = new ArrayList<Integer>();   
    private List<String> selectedPrefixList = new ArrayList<String>();
    private List<String> prefixList = new ArrayList<String>();
    private String prefix;
    
    private List<Integer> selectedUserGroup = new ArrayList<Integer>();
    private Map<String, Integer> userGroupList = new LinkedHashMap<String, Integer>();
    
    private List<Integer> selectedProduct = new ArrayList<Integer>();

    private List<FileTemplateMapping> fileMaps = null;

    private boolean showBtnSave2 = true;
    private Integer amount;

    private Integer totalRecords = 0;
    private Integer totalDupWithin = 0;
    private Integer totalDupYes = 0;
    private Integer totalDupCustomer = 0;
    private Integer totalOpOut = 0;
    private Integer totalWrongPhoneNo = 0;
    private Integer totalLoaded = 0;
    private Integer totalBlacklist = 0;
    private Integer totalBadFormat = 0;
    private Integer totalMergeCustomer = 0;
    private Integer totalDnc = 0;
    private Integer totalMib = 0;
    
    private List<MarketingTempCustomer> dataListPopup;
    private List<String[]> dataListStringPopup;
    private String strQuery;
    
    private Integer dupPeriodMonth;
    
    private static File dataFile;
    
    private String listDetailfx1;
    private List<ListDetail> listDetailfx1List;
    private String listDetailfx3;
    private List<ListDetail> listDetailfx3List;
    private String listDetailfx4;
    private List<ListDetail> listDetailfx4List;
    private String listDetailfx5;
    private List<ListDetail> listDetailfx5List;

    @ManagedProperty(value = "#{marketingDAO}")
    private MarketingDAO marketingDAO;
    @ManagedProperty(value = "#{prospectlistSponsorDAO}")
    private ProspectlistSponsorDAO prospectlistSponsorDAO;
    @ManagedProperty(value = "#{fileTemplateDAO}")
    private FileTemplateDAO fileTemplateDAO;
    @ManagedProperty(value = "#{fileTemplateMappingDAO}")
    private FileTemplateMappingDAO fileTemplateMappingDAO;
    @ManagedProperty(value = "#{customerDAO}")
    private CustomerDAO customerDAO;
    @ManagedProperty(value = "#{marketingCustomerDAO}")
    private MarketingCustomerDAO marketingCustomerDAO;
    @ManagedProperty(value="#{usersDAO}")
    private UsersDAO usersDAO;
    @ManagedProperty(value="#{userGroupDAO}")
    private UserGroupDAO userGroupDAO;
    @ManagedProperty(value = "#{productDAO}")
    private ProductDAO productDAO;
    @ManagedProperty(value = "#{listDetailDAO}")
    private ListDetailDAO listDetailDAO;

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:marketinglistupload:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        messageDate="";
        messageDup="";
        sessionId = JSFUtil.getSession().getId();
        showBtnSave2 = true;
        
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");
        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            marketing = new Marketing();
            marketing.setDatasource("file");
            marketing.setEnable(Boolean.TRUE);
            marketing.setIsLinkProduct(Boolean.FALSE);
            marketing.setAutoDial(Boolean.FALSE);
        } else {
            mode = "edit";
            marketing = getMarketingDAO().findMarketing(new Integer(selectedID));
            
            if (marketing.getFx1() != null) {
                listDetailfx1 = marketing.getFx1();
            }

            if (marketing.getFx3() != null) {
                listDetailfx3 = marketing.getFx3();
            }

            if (marketing.getFx4() != null) {
                listDetailfx4 = marketing.getFx4();
            }

            if (marketing.getFx5() != null) {
                listDetailfx5 = marketing.getFx5();
            }
            
            if (marketing == null) {
                JSFUtil.redirect(REDIRECT_PAGE);
                return;
            }
            if (marketing.getProspectlistSponsor() != null)
                prospectlistSponsorId = marketing.getProspectlistSponsor().getId();
            if (marketing.getFileTemplate() != null)
                fileTemplateId = marketing.getFileTemplate().getId();
            //if (marketing.getDupType().equals("time"))
            //    dupPeriodMonth = marketing.getPeriodCustomer() / 30;
            for (UserGroup userGroup : marketing.getUserGroupCollection()) {
                this.selectedUserGroup.add(userGroup.getId());
            }
            for (Product product : marketing.getProductCollection()) {
                this.selectedProduct.add(product.getId());
            }
        }
        setUserGroupList(userGroupDAO.findUserGroupBy("", 0, "CampaignManager", "enable"));

        listDetailfx1List = listDetailDAO.findListDetailfx("marketing.fx1");
        listDetailfx3List = listDetailDAO.findListDetailfx("marketing.fx3");
        listDetailfx4List = listDetailDAO.findListDetailfx("marketing.fx4");
        listDetailfx5List = listDetailDAO.findListDetailfx("marketing.fx5");
    }
    
    public void initializeListener() {
        step = 1;
        prospectlistSponsorId = 0;
        fileTemplateId = 0;
        initialize();
    }

    public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:marketinglistupload:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:marketinglistupload:edit"); 
       }
    }  
        
    public String saveAction() {
        messageDate="";
        messageDup="";
        if(checkName(marketing)) {
            if(marketing.getStartDate().before(marketing.getEndDate())) {
                String username = JSFUtil.getUserSession().getUserName();
                marketing.setProspectlistSponsor(new ProspectlistSponsor(this.getProspectlistSponsorId()));
                marketing.setUserGroupCollection(this.getSelectedUserGroupCollection());
                if (!marketing.getIsLinkProduct().booleanValue()) {
                    this.selectedProduct.clear();
                }
                marketing.setProductCollection(this.getSelectedProductCollection());
                
                if (listDetailfx1 != null && listDetailfx1 != "") {
                    marketing.setFx1(listDetailfx1);
                } else {
                    marketing.setFx1(null);
                }

                if (listDetailfx3 != null && listDetailfx3 != "") {
                    marketing.setFx3(listDetailfx3);
                } else {
                    marketing.setFx3(null);
                }

                if (listDetailfx4 != null && listDetailfx4 != "") {
                    marketing.setFx4(listDetailfx4);
                } else {
                    marketing.setFx4(null);
                }

                if (listDetailfx5 != null && listDetailfx5 != "") {
                    marketing.setFx5(listDetailfx5);
                } else {
                    marketing.setFx5(null);
                }
                
                try {
                    marketing.setUpdateBy(username);
                    marketing.setUpdateDate(new Date());
                    this.getMarketingDAO().edit(marketing);
                } catch (Exception e) {
                    log.error(e);
                    message = e.getMessage();
                    return FAILURE;
                }
                return SUCCESS;
            } else {
                messageDate = " Effective Date From over Date To";
                return null;
            }
        } else {
            messageDup = " Code is already taken";
            return null; 
        }
    }  
     
    public MarketingController getMarketingController() {
        FacesContext context = FacesContext.getCurrentInstance();
        Application app = context.getApplication();
        ValueExpression vex = app.getExpressionFactory().
                createValueExpression(context.getELContext(), "#{marketingController}", MarketingController.class);
        MarketingController g = (MarketingController) vex.getValue(context.getELContext());
        return g;
    }
       
    public void nextStep1ToStep2(ActionEvent event) {
        messageDate = "";
        messageDup = "";
        message = "";
        if(checkName(marketing)) {
            if(marketing.getStartDate().before(marketing.getEndDate())) {
                step = 2;
                FileTemplate fileTemplate = this.getFileTemplateDAO().findFileTemplate(fileTemplateId);
                marketing.setProspectlistSponsor(new ProspectlistSponsor(this.getProspectlistSponsorId()));
                marketing.setFileTemplate(fileTemplate);
                fileMaps = this.getFileTemplateMappingDAO().findFileTemplateMappingById(fileTemplateId);
                if(fileTemplate != null){
                    strQuery = this.getStrQuery(fileMaps);
                }
                
                if (listDetailfx1 != null && listDetailfx1 != "") {
                    marketing.setFx1(listDetailfx1);
                } else {
                    marketing.setFx1(null);
                }

                if (listDetailfx3 != null && listDetailfx3 != "") {
                    marketing.setFx3(listDetailfx3);
                } else {
                    marketing.setFx3(null);
                }

                if (listDetailfx4 != null && listDetailfx4 != "") {
                    marketing.setFx4(listDetailfx4);
                } else {
                    marketing.setFx4(null);
                }

                if (listDetailfx5 != null && listDetailfx5 != "") {
                    marketing.setFx5(listDetailfx5);
                } else {
                    marketing.setFx5(null);
                }
                
                try {
                    if (getMode().equals("add")) {                        
                        showBtnSave2 = true;
                        marketing.setId(null);
                        marketing.setEnable(true);
                        marketing.setFilename(dataFile.getName());
                        //if(marketing.getDupType().equals("time")) {
                        //    marketing.setPeriodCustomer(dupPeriodMonth * 30);   // convert to day
                        //}
                        marketingDAO.create(sessionId, marketing, dataFile, fileMaps);
                    }else{
                        step = 2;
                        showBtnSave2 = false;
                    }

                    totalRecords = marketingDAO.countTotalRecords(mode, sessionId, marketing);
                    totalDupWithin = marketingDAO.countDuplicateWithin(mode, sessionId, marketing);
                    totalDupYes = marketingDAO.countDuplicateYesSale(mode, sessionId, marketing);
                    totalDupCustomer = marketingDAO.countDuplicateCustomer(mode, sessionId, marketing);
                    totalOpOut = marketingDAO.countOpOut(mode, sessionId, marketing);
                    totalWrongPhoneNo = marketingDAO.countWrongPhoneNo(mode, sessionId, marketing);
                    totalBlacklist = marketingDAO.countBlacklist(mode, sessionId, marketing);
                    totalBadFormat = marketingDAO.countBadFormat(mode, sessionId, marketing);
                    totalMergeCustomer = marketingDAO.countMergeCustomer(mode, sessionId, marketing);
                    totalDnc = marketingDAO.countDnc(mode, sessionId, marketing);
                    totalMib = marketingDAO.countMib(mode, sessionId, marketing);
                    totalLoaded = marketingDAO.countLoaded(mode, sessionId, marketing);
                    
                } catch (Exception e) {
                    log.error(e);
                    message = " Data could not insert";
                    clearFileUpload();
                    step = 1;
                }
            } else {
                messageDate = " Effective Date From over Date To";
                step = 1;
            }
        } else {
             messageDup = " Code is already taken";
             step = 1;
        }
        
    }
        
    public Boolean checkName(Marketing marketing) {
        String code = marketing.getCode();
        Integer id=0; 
        if(marketing.getId() != null)
            id = marketing.getId();
        MarketingDAO dao = getMarketingDAO();
        
        Integer cnt = dao.checkMarketingCode(code, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }
    
    private String getStrQuery(Collection<FileTemplateMapping> ftmList){
        String str = " ";
        for(FileTemplateMapping ftm : ftmList){
            String tmp = ftm.getCustomerField().getMappingField();
            if(tmp.equals("customer.dob")){
                tmp = "CONVERT(VARCHAR(10), DATEADD(YEAR, 543, dob), 103) as dob";
            }else if(tmp.equals("customer.remark")){
                tmp = "CONVERT(VARCHAR(1000), remark) as remark";            
            }
            
            str += tmp + ",";
        }
        if(!str.isEmpty()){
            str = str.substring(0, str.length() - 1);
            
            str = str.replace("marketing_customer.", "");
            str = str.replace("customer.", "");
            
        }
        return str;
    }
    
    private void countList(List<MarketingTempCustomer> list){
        countNew = 0;
        countExist = 0;
        for(MarketingTempCustomer obj : list){
            if(obj.getListStatus().equals("new")){
                countNew++;
            }else if(obj.getListStatus().equals("exist")){
                countExist++;
            }
        }
    }
        
    public void saveStep2Listener(ActionEvent event) {
        String username = JSFUtil.getUserSession().getUserName();
        try{
            marketing.setTotalTempRecord(totalRecords);
            marketing.setTotalRecord(totalLoaded);
            marketing.setCreateBy(username);
            marketing.setCreateDate(new Date());
            marketing.setUserGroupCollection(this.getSelectedUserGroupCollection());
            if (!marketing.getIsLinkProduct().booleanValue()) {
                this.selectedProduct.clear();
            }
            marketing.setProductCollection(this.getSelectedProductCollection());
            marketingDAO.createCustomerFromMarketingTempCustomer(sessionId, marketing);            
        } catch(Exception e) {
            log.error(e);
            message = " Data could not insert";
            deleteMarketingTempCustomer();
            clearFileUpload();
            step = 2;
        }
        showBtnSave2 = false;

        if (marketing.getAutoDial() != null && marketing.getAutoDial()) {
            marketingDAO.exportCalltable(marketing.getId());
        }
    }
        
    public void previousStep2ToStep1(ActionEvent event){
        step = 1;
        this.deleteMarketingTempCustomer();
    }
    
    public void previousStep2ToStep1Edit(ActionEvent event){
        step = 1;
    }
    
    private void deleteMarketingTempCustomer(){
        try{
            marketingDAO.deleteMarketingTempCustomerFromSessionId(sessionId);
        }catch(Exception e){
            log.error(e);
        }
    }
    
    private void copy(InputStream in, File file) {
        try {
            if(!file.exists()){
                OutputStream out = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearFileUpload() {
        if(dataFile != null) {
            dataFile.delete();
        }
    }
        
    public void uploadCompleteListener(FileUploadEvent event) {
        Date date = new Date();
        try {
            UploadedFile item = event.getUploadedFile();
            
            String fileName = item.getName();
            if (fileName.lastIndexOf("\\") != -1) {
                fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());
            }
            if(!fileName.isEmpty() && fileName.indexOf(",") != -1)
                fileName = fileName.replace(",", "");
            
            this.dataFile = new File(JSFUtil.getuploadPath() + date.getTime() + "_" + fileName);
            this.copy(item.getInputStream(), this.dataFile);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public Map<String, Integer> getProspectlistSponsors() {
        return this.getProspectlistSponsorDAO().getProspectlistSponsorMap();
    }

    public Map<String, Integer> getFileTemplates() {
        return this.getFileTemplateDAO().getFileTemplateMap();
    }

    public String backAction() {
        return SUCCESS;
    }

    public void getTotalRecordsList(ActionEvent event) {
        dataListStringPopup = marketingDAO.getTotalRecordsList(mode, sessionId, strQuery, marketing);
    }

    public void getDupWihtinList(ActionEvent event) {
        dataListStringPopup = marketingDAO.getDuplicateWithinList(mode, sessionId, strQuery, marketing);
    }

    public void getDupYesList(ActionEvent event) {
        dataListStringPopup = marketingDAO.getDuplicateYesSaleList(mode, sessionId, strQuery, marketing);
    }

    public void getDupCustomerList(ActionEvent event) {
        dataListStringPopup = marketingDAO.getDuplicateCustomerList(mode, sessionId, strQuery, marketing);
    }

    public void getOpOutList(ActionEvent event) {
        dataListStringPopup = marketingDAO.getOpOutList(mode, sessionId, strQuery, marketing);
    }

    public void getBlackList(ActionEvent event) {
        dataListStringPopup = marketingDAO.getBlackList(mode, sessionId, strQuery, marketing);
    }

    public void getWrongPhoneNoList(ActionEvent event) {
        dataListStringPopup = marketingDAO.getWrongPhoneNoList(mode, sessionId, strQuery, marketing);
    }

    public void getWrongFormatList(ActionEvent event) {
        dataListStringPopup = marketingDAO.getWrongFormatList(mode, sessionId, strQuery, marketing);
    }
        
    public void getMergeList(ActionEvent event) {
        dataListStringPopup = marketingDAO.getMergeList(mode, sessionId, strQuery, marketing);
    }
    
    public void getDncList(ActionEvent event) {
        dataListStringPopup = marketingDAO.getDncList(mode, sessionId, strQuery, marketing);
    }
    
    public void getMibList(ActionEvent event) {
        dataListStringPopup = marketingDAO.getMibList(mode, sessionId, strQuery, marketing);
    }
    
    public void getLoadedList(ActionEvent event) {
        dataListStringPopup = marketingDAO.getLoadedList1(mode, sessionId, strQuery, marketing);
    }
    
    public Marketing getMarketing() {
        return marketing;
    }

    public void setMarketing(Marketing marketing) {
        this.marketing = marketing;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getFileTemplateId() {
        return fileTemplateId;
    }

    public void setFileTemplateId(Integer fileTemplateId) {
        this.fileTemplateId = fileTemplateId;
    }

    public Integer getProspectlistSponsorId() {
        return prospectlistSponsorId;
    }

    public void setProspectlistSponsorId(Integer prospectlistSponsorId) {
        this.prospectlistSponsorId = prospectlistSponsorId;
    }

    public FileUploadBean getFileUploadBean() {
        return fileUploadBean;
    }

    public void setFileUploadBean(FileUploadBean FileUploadBean) {
        this.fileUploadBean = FileUploadBean;
    }


    public MarketingDAO getMarketingDAO() {
        return marketingDAO;
    }

    public void setMarketingDAO(MarketingDAO marketingDAO) {
        this.marketingDAO = marketingDAO;
    }

    public ProspectlistSponsorDAO getProspectlistSponsorDAO() {
        return prospectlistSponsorDAO;
    }

    public void setProspectlistSponsorDAO(ProspectlistSponsorDAO prospectlistSponsorDAO) {
        this.prospectlistSponsorDAO = prospectlistSponsorDAO;
    }

    public FileTemplateDAO getFileTemplateDAO() {
        return fileTemplateDAO;
    }

    public void setFileTemplateDAO(FileTemplateDAO fileTemplateDAO) {
        this.fileTemplateDAO = fileTemplateDAO;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public List<MarketingTempCustomer> getMtcList() {
        return mtcList;
    }

    public void setMtcList(List<MarketingTempCustomer> mtcList) {
        this.mtcList = mtcList;
    }

    public List<MarketingCriteria> getMarketingCriteriaList() {
        return marketingCriteriaList;
    }

    public void setMarketingCriteriaList(List<MarketingCriteria> marketingCriteriaList) {
        this.marketingCriteriaList = marketingCriteriaList;
    }

    public MarketingCriteria getMarketingCriteria() {
        return marketingCriteria;
    }

    public void setMarketingCriteria(MarketingCriteria marketingCriteria) {
        this.marketingCriteria = marketingCriteria;
    }

    public List<String> getSelectedGenderList() {
        return selectedGenderList;
    }

    public void setSelectedGenderList(List<String> selectedGenderList) {
        this.selectedGenderList = selectedGenderList;
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public Map<String, String> getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(Map<String, String> provinceList) {
        this.provinceList = provinceList;
    }

    public List<String> getSelectedProvinceList() {
        return selectedProvinceList;
    }

    public void setSelectedProvinceList(List<String> selectedProvinceList) {
        this.selectedProvinceList = selectedProvinceList;
    }

    public Map<String, Integer> getOccupationList() {
        return occupationList;
    }

    public void setOccupationList(Map<String, Integer> occupationList) {
        this.occupationList = occupationList;
    }

    public List<Integer> getSelectedOccupationList() {
        return selectedOccupationList;
    }

    public void setSelectedOccupationList(List<Integer> selectedOccupationList) {
        this.selectedOccupationList = selectedOccupationList;
    }

    public List<String> getSelectedPrefixList() {
        return selectedPrefixList;
    }

    public void setSelectedPrefixList(List<String> selectedPrefixList) {
        this.selectedPrefixList = selectedPrefixList;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public List<String> getPrefixList() {
        return prefixList;
    }

    public void setPrefixList(List<String> prefixList) {
        this.prefixList = prefixList;
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

    public MarketingCustomerDAO getMarketingCustomerDAO() {
        return marketingCustomerDAO;
    }

    public void setMarketingCustomerDAO(MarketingCustomerDAO marketingCustomerDAO) {
        this.marketingCustomerDAO = marketingCustomerDAO;
    }
    
    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public Integer getCountNew() {
        return countNew;
    }

    public void setCountNew(Integer countNew) {
        this.countNew = countNew;
    }

    public Integer getCountExist() {
        return countExist;
    }

    public void setCountExist(Integer countExist) {
        this.countExist = countExist;
    }

    public List<MarketingTempCustomer> getListFilter() {
        return listFilter;
    }

    public void setListFilter(List<MarketingTempCustomer> listFilter) {
        this.listFilter = listFilter;
    }

    public List<String[]> getMtcListString() {
        return mtcListString;
    }

    public void setMtcListString(List<String[]> mtcListString) {
        this.mtcListString = mtcListString;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
    }

    public Integer getCountDup() {
        return countDup;
    }

    public void setCountDup(Integer countDup) {
        this.countDup = countDup;
    }
    
    public Integer getTotalRecords() {
        return totalRecords;
}

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    public Integer getTotalDupWithin() {
        return totalDupWithin;
    }

    public void setTotalDupWithin(Integer totalDupWithin) {
        this.totalDupWithin = totalDupWithin;
    }

    public Integer getTotalDupYes() {
        return totalDupYes;
    }

    public void setTotalDupYes(Integer totalDupYes) {
        this.totalDupYes = totalDupYes;
    }

    public Integer getTotalDupCustomer() {
        return totalDupCustomer;
    }

    public void setTotalDupCustomer(Integer totalDupCustomer) {
        this.totalDupCustomer = totalDupCustomer;
    }

    public Integer getTotalOpOut() {
        return totalOpOut;
    }

    public void setTotalOpOut(Integer totalOpOut) {
        this.totalOpOut = totalOpOut;
    }

    public Integer getTotalWrongPhoneNo() {
        return totalWrongPhoneNo;
    }

    public void setTotalWrongPhoneNo(Integer totalWrongPhoneNo) {
        this.totalWrongPhoneNo = totalWrongPhoneNo;
    }
    
    public Integer getTotalDnc() {
        return totalDnc;
    }

    public void setTotalDnc(Integer totalDnc) {
        this.totalDnc = totalDnc;
    }
    
    public Integer getTotalMib() {
        return totalMib;
    }

    public void setTotalMib(Integer totalMib) {
        this.totalMib = totalMib;
    }

    public Integer getTotalLoaded() {
        return totalLoaded;
    }

    public void setTotalLoaded(Integer totalLoaded) {
        this.totalLoaded = totalLoaded;
    }

    public Integer getTotalBlacklist() {
        return totalBlacklist;
    }

    public void setTotalBlacklist(Integer totalBlacklist) {
        this.totalBlacklist = totalBlacklist;
    }

    public boolean isShowBtnSave2() {
        return showBtnSave2;
    }

    public void setShowBtnSave2(boolean showBtnSave2) {
        this.showBtnSave2 = showBtnSave2;
    }

    public List<MarketingTempCustomer> getDataListPopup() {
        return dataListPopup;
    }

    public void setDataListPopup(List<MarketingTempCustomer> dataListPopup) {
        this.dataListPopup = dataListPopup;
    }

    public String getStrQuery() {
        return strQuery;
    }

    public void setStrQuery(String strQuery) {
        this.strQuery = strQuery;
    }

    public List<String[]> getDataListStringPopup() {
        return dataListStringPopup;
    }

    public void setDataListStringPopup(List<String[]> dataListStringPopup) {
        this.dataListStringPopup = dataListStringPopup;
    }
    
    public FileTemplateMappingDAO getFileTemplateMappingDAO() {
        return fileTemplateMappingDAO;
}

    public void setFileTemplateMappingDAO(FileTemplateMappingDAO fileTemplateMappingDAO) {
        this.fileTemplateMappingDAO = fileTemplateMappingDAO;
    }

    public List<FileTemplateMapping> getFileMaps() {
        return fileMaps;
    }

    public void setFileMaps(List<FileTemplateMapping> fileMaps) {
        this.fileMaps = fileMaps;
    }

    public Integer getDupPeriodMonth() {
        return dupPeriodMonth;
    }

    public void setDupPeriodMonth(Integer dupPeriodMonth) {
        this.dupPeriodMonth = dupPeriodMonth;
    }

    public Integer getTotalBadFormat() {
        return totalBadFormat;
    }

    public void setTotalBadFormat(Integer totalBadFormat) {
        this.totalBadFormat = totalBadFormat;
    }

    public Integer getTotalMergeCustomer() {
        return totalMergeCustomer;
    }

    public void setTotalMergeCustomer(Integer totalMergeCustomer) {
        this.totalMergeCustomer = totalMergeCustomer;
    }
    
    public void setUserGroupList(List<UserGroup> userGroups) {
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (UserGroup obj : userGroups) {
            values.put(obj.getName(), obj.getId());
        }
        this.userGroupList = values;
    }
    
    public Map<String,Integer> getUserGroupList() {
        return this.userGroupList;
        /*
        List<UserGroup> userGroups = userGroupDAO.findUserGroupBy("", 0, "Manager", "enable");
        Map<String,Integer> values = new LinkedHashMap<String,Integer>();
        for (UserGroup obj : userGroups) {
            values.put(obj.getName(), obj.getId());
        }
        this.userGroupList = values;
        */
        /*
        for (Integer groupid : selectedUserGroup) {
            Boolean exist = false;
            for (Map.Entry<String, Integer> item : values.entrySet()) {
                if (groupid.intValue() == item.getValue()) {
                    exist = true;
                    break;
                }
            }
            if(!exist){
                UserGroup userGroup = userGroupDAO.findUserGroup(groupid);
                values.put(userGroup.getName(),userGroup.getId());
            }
        }
        return values;
        */
    }
    
    public List<Integer> getSelectedUserGroup() {
        return selectedUserGroup;
    }

    public void setSelectedUserGroup(List<Integer> selectedUserGroup) {
        this.selectedUserGroup = selectedUserGroup;
    }
    
    public List<UserGroup> getSelectedUserGroupCollection() {
        List<UserGroup> userGroups = new ArrayList<UserGroup>();
        for (int groupid: selectedUserGroup) {
            UserGroup obj = new UserGroup();
            obj.setId(groupid);
            userGroups.add(obj);
        }
        return userGroups;
    }
    
    public Map<String, Integer> getProductList() {
        ProductDAO dao = getProductDAO();
        List<Product> products = dao.findProductEntities();
        Map<String, Integer> values = new LinkedHashMap<String, Integer>();
        for (Product product : products) {
            if (product.getProductCategory().getCategoryType().equals("motor")) {
                values.put(product.getName() + "(" + product.getModelType().getName() + ", " + product.getModelFromYear() + "-" + product.getModelToYear() + ", " + product.getProductCategory().getName() + ")", product.getId());
            } else {
                values.put(product.getName(), product.getId());
            }          
        }

        for (Integer pid : selectedProduct) {
            Boolean exist = false;
            for (Map.Entry<String, Integer> item : values.entrySet()) {
                if (pid.intValue() == item.getValue()) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                Product p = productDAO.findProduct(pid);
                values.put(p.getName(), p.getId());
            }
        }
        return values;
    }

    public List<Integer> getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(List<Integer> selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public List<Product> getSelectedProductCollection() {
        List<Product> products = new ArrayList<Product>();
        for (int pid : selectedProduct) {
            Product p = new Product();
            p.setId(pid);
            products.add(p);
        }
        return products;
    }
    
    public void linkProductListener(ValueChangeEvent event) {
        this.marketing.setIsLinkProduct((Boolean) event.getNewValue());
    }

    public String getListDetailfx1() {
        return listDetailfx1;
    }

    public void setListDetailfx1(String listDetailfx1) {
        this.listDetailfx1 = listDetailfx1;
    }

    public List<ListDetail> getListDetailfx1List() {
        return listDetailfx1List;
    }

    public void setListDetailfx1List(List<ListDetail> listDetailfx1List) {
        this.listDetailfx1List = listDetailfx1List;
    }

    public ListDetailDAO getListDetailDAO() {
        return listDetailDAO;
    }

    public void setListDetailDAO(ListDetailDAO listDetailDAO) {
        this.listDetailDAO = listDetailDAO;
    }

    public String getListDetailfx3() {
        return listDetailfx3;
    }

    public void setListDetailfx3(String listDetailfx3) {
        this.listDetailfx3 = listDetailfx3;
    }

    public List<ListDetail> getListDetailfx3List() {
        return listDetailfx3List;
    }

    public void setListDetailfx3List(List<ListDetail> listDetailfx3List) {
        this.listDetailfx3List = listDetailfx3List;
    }

    public String getListDetailfx4() {
        return listDetailfx4;
    }

    public void setListDetailfx4(String listDetailfx4) {
        this.listDetailfx4 = listDetailfx4;
    }

    public List<ListDetail> getListDetailfx4List() {
        return listDetailfx4List;
    }

    public void setListDetailfx4List(List<ListDetail> listDetailfx4List) {
        this.listDetailfx4List = listDetailfx4List;
    }

    public String getListDetailfx5() {
        return listDetailfx5;
    }

    public void setListDetailfx5(String listDetailfx5) {
        this.listDetailfx5 = listDetailfx5;
    }

    public List<ListDetail> getListDetailfx5List() {
        return listDetailfx5List;
    }

    public void setListDetailfx5List(List<ListDetail> listDetailfx5List) {
        this.listDetailfx5List = listDetailfx5List;
    }
    
    
}
