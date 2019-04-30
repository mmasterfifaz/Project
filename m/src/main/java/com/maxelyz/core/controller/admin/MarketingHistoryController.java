package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.FileTemplateDAO;
import com.maxelyz.core.model.dao.FileTemplateMappingDAO;
import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.MarketingDAO;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.FileTemplate;
import com.maxelyz.core.model.entity.FileTemplateMapping;
import com.maxelyz.core.model.entity.Marketing;
import com.maxelyz.core.model.entity.MarketingTempCustomer;
import com.maxelyz.core.service.SecurityService;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class MarketingHistoryController implements Serializable {

    private static Logger log = Logger.getLogger(MarketingHistoryController.class);
    private static String REDIRECT_PAGE = "marketinghistory.jsf";
    private static String SUCCESS = "marketing.xhtml?faces-redirect=true";
    private static String FAILURE = "marketinghistory.xhtml";
    private static String REFRESH = "marketinghistory.xhtml?faces-redirect=true";
    private String message = "";
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<String[]> marketings;
    private String sessionId;
    private Marketing marketing;
    
    @ManagedProperty(value = "#{marketingDAO}")
    private MarketingDAO marketingDAO;
    @ManagedProperty(value = "#{fileTemplateDAO}")
    private FileTemplateDAO fileTemplateDAO;
    @ManagedProperty(value = "#{fileTemplateMappingDAO}")
    private FileTemplateMappingDAO fileTemplateMappingDAO;
    
    private List<FileTemplateMapping> fileMaps;
    private List<MarketingTempCustomer> dataListPopup;
    private List<String[]> dataListStringPopup;
    private String strQuery;
        
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:marketinglistupload:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
       sessionId = JSFUtil.getSession().getId();
       String selectedID = (String) JSFUtil.getRequestParameterMap("id");
       if (selectedID != null) {
            marketing = getMarketingDAO().findMarketing(new Integer(selectedID));
            marketings = getMarketingDAO().getLoadHositoryList(new Integer(selectedID));
       }
    }
    
    public void initialAction(ActionEvent event) {
        initialize();
    }
    
    public List<String[]> getList() {
        return getMarketings();
    }

    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }
    
    public Marketing getMarketing() {
        return marketing;
    }

    public void setMarketing(Marketing marketing) {
        this.marketing = marketing;
    }

    public List<String[]> getMarketings() {
        return marketings;
    }

    public void setMarketings(List<String[]> marketings) {
        this.marketings = marketings;
    }
    
    public String backAction() {
        return SUCCESS;
    }

    public MarketingDAO getMarketingDAO() {
        return marketingDAO;
    }

    public void setMarketingDAO(MarketingDAO marketingDAO) {
        this.marketingDAO = marketingDAO;
    }
    
    private String getStrQuery(Collection<FileTemplateMapping> ftmList){
        String str = " ";
        for(FileTemplateMapping ftm : ftmList){
            String tmp = ftm.getCustomerField().getMappingField();
            tmp = tmp.replace("marketing_customer.", "");
            tmp = tmp.replace("customer.", ""); 

            if(tmp.equals("dob")){
                tmp = "CONVERT(VARCHAR(10), DATEADD(YEAR, 543, dob), 103) as dob";
            }else if(tmp.equals("remark")){
                tmp = "CONVERT(VARCHAR(1000), remark) as remark";            
            }      
            str += tmp + ",";
        }
        if(!str.isEmpty()){
            str = str.substring(0, str.length() - 1);      
        }
        return str;
    }
    
    public void getTotalRecordsList(ActionEvent event) {
        String mode = "add";
               
        Integer marketingId = (Integer) event.getComponent().getAttributes().get("marketingid");
        String sessionId = (String) event.getComponent().getAttributes().get("sessionid");
        Marketing marketing = getMarketingDAO().findMarketing(marketingId);
        if (marketing == null) {
            JSFUtil.redirect(REDIRECT_PAGE);
            return;
        }
        
        Integer fileTemplateId;
        fileTemplateId = marketing.getFileTemplate().getId();
        fileMaps = this.getFileTemplateMappingDAO().findFileTemplateMappingById(fileTemplateId);
        strQuery = this.getStrQuery(fileMaps);
        
        dataListStringPopup = marketingDAO.getTotalRecordsList(mode, sessionId, strQuery, marketing);
    }
    
    public void getLoadedList(ActionEvent event) {
        //dataListStringPopup = marketingDAO.getLoadedList1(mode, sessionId, strQuery, marketing);
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
    
    public FileTemplateDAO getFileTemplateDAO() {
        return fileTemplateDAO;
    }

    public void setFileTemplateDAO(FileTemplateDAO fileTemplateDAO) {
        this.fileTemplateDAO = fileTemplateDAO;
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
}
