package com.maxelyz.core.controller.admin;

import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
import com.maxelyz.core.model.dao.DocumentDAO;
import com.maxelyz.core.model.dao.DocumentUploadTypeDAO;
import com.maxelyz.core.model.dao.UserGroupDAO;
import com.maxelyz.core.model.entity.Document;
import com.maxelyz.utils.SecurityUtil;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.event.ActionEvent;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class DocumentController implements Serializable{
    private static Logger log = Logger.getLogger(DocumentController.class);
    private static String REFRESH = "document.xhtml?faces-redirect=true";
    private static String EDIT = "documentedit.xhtml";
  
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<Document> documents;
    private Map<String, Integer> documentUploadTypeList;
    private Map<String, Integer> userGroupList;
    private Document document;
    private String title;
    private String fileName;
    private String name;
    private Integer userGroupId;
    private Integer documentUploadTypeId;
    private String status;
    private Date dateFrom;
    private Date dateTo;
    
    @ManagedProperty(value="#{documentUploadTypeDAO}")
    private DocumentUploadTypeDAO documentUploadTypeDAO;
    @ManagedProperty(value="#{documentDAO}")
    private DocumentDAO documentDAO;
    @ManagedProperty(value = "#{userGroupDAO}")
    private UserGroupDAO userGroupDAO;

    @PostConstruct
    public void initialize() {

        if (!SecurityUtil.isPermitted("admin:document:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        DocumentDAO dao = getDocumentDAO();
        documents = dao.findDocumentEntities();
        documentUploadTypeList = this.getDocumentUploadTypeDAO().getDocumentUploadTypeList();
        userGroupList = this.getUserGroupDAO().getUserGroupList();
        
    }
    
    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:document:add");
    }

    public boolean isEditPermitted() {
        return SecurityUtil.isPermitted("admin:document:edit");
    }
    
    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:document:delete");
    }

    public List<Document> getList() {
        return getDocuments();
    }

     public String editAction() {
       return EDIT;
    }
     
    public String deleteAction(){
        DocumentDAO dao = getDocumentDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    document = dao.findDocument(item.getKey());
                    document.setEnable(false);
                    dao.edit(document);
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return REFRESH;
    }
    
    
    public void searchActionListener(ActionEvent event){
        this.search();
    }
    
    private void search(){
        documents = documentDAO.findDocumentBySearch(title,fileName,dateFrom,dateTo,userGroupId,documentUploadTypeId,status);
    }
    
    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }
    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public Map<String, Integer> getDocumentUploadTypeList() {
        return documentUploadTypeList;
    }

    public void setDocumentUploadTypeList(Map<String, Integer> documentUploadTypeList) {
        this.documentUploadTypeList = documentUploadTypeList;
    }

    public DocumentUploadTypeDAO getDocumentUploadTypeDAO() {
        return documentUploadTypeDAO;
    }

    public void setDocumentUploadTypeDAO(DocumentUploadTypeDAO documentUploadTypeDAO) {
        this.documentUploadTypeDAO = documentUploadTypeDAO;
    }
    

    public DocumentDAO getDocumentDAO() {
        return documentDAO;
    }

    public void setDocumentDAO(DocumentDAO documentDAO) {
        this.documentDAO = documentDAO;
    }

    public UserGroupDAO getUserGroupDAO() {
        return userGroupDAO;
    }

    public void setUserGroupDAO(UserGroupDAO userGroupDAO) {
        this.userGroupDAO = userGroupDAO;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Integer userGroupId) {
        this.userGroupId = userGroupId;
    }
    
    public Map<String, Integer> getUserGroupList() {
        return userGroupList;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getDocumentUploadTypeId() {
        return documentUploadTypeId;
    }

    public void setDocumentUploadTypeId(Integer documentUploadTypeId) {
        this.documentUploadTypeId = documentUploadTypeId;
    }
    
}
