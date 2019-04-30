package com.maxelyz.core.controller.admin;

import org.apache.log4j.Logger;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;
import com.maxelyz.core.model.dao.DocumentDAO;
import com.maxelyz.core.model.dao.DocumentFolderDAO;
import com.maxelyz.core.model.dao.DocumentUploadTypeDAO;
import com.maxelyz.core.model.dao.UserGroupDAO;
import com.maxelyz.core.model.entity.Document;
import com.maxelyz.core.model.entity.DocumentFolder;
import com.maxelyz.core.model.entity.DocumentUploadType;
import com.maxelyz.core.model.entity.UserGroup;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

@ManagedBean (name="documentEditController")
@ViewScoped
public class DocumentEditController implements Serializable {
    private static Logger log = Logger.getLogger(DocumentEditController.class);
    private static String REDIRECT_PAGE = "document.jsf";
    private static String SUCCESS = "document.xhtml?faces-redirect=true";
    private static String FAILURE = "documentedit.xhtml";
    private Map<String, Integer> userGroupList;
    private Map<String, Integer> documentFolderList;
    private Map<String, Integer> documentUploadTypeList;
    private Document document;
    private String mode;
    private String message;
    private String status;
    private Integer documentId;
    private Integer documentFolderId;
    private Integer documentUploadTypeId;
    private Integer userGroupId;
    private UploadedFile item;
    private String fileName;
    private String uploadPath = JSFUtil.getuploadPath();  //JSFUtil.getRealPath() + "upload\\import\\";
    private String tmpPath = JSFUtil.getuploadPath()+"temp/";   //JSFUtil.getRealPath() + "upload\\temp\\";
    private String extFileName;
    private String realFileName;
    
    @ManagedProperty(value = "#{documentDAO}")
    private DocumentDAO documentDAO;
    @ManagedProperty(value = "#{documentFolderDAO}")    
    private DocumentFolderDAO documentFolderDAO;
    @ManagedProperty(value="#{documentUploadTypeDAO}")
    private DocumentUploadTypeDAO documentUploadTypeDAO;
    @ManagedProperty(value = "#{userGroupDAO}")
    private UserGroupDAO userGroupDAO;
   

    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:document:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        documentUploadTypeList = this.getDocumentUploadTypeDAO().getDocumentUploadTypeList();
        documentFolderList = this.getDocumentFolderDAO().getDocumentFolderList();
        userGroupList = this.getUserGroupDAO().getUserGroupList();
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");

        if (selectedID == null || selectedID.isEmpty()) {
            mode = "add";
            document = new Document();
            document.setEnable(Boolean.TRUE);
            document.setStatus(Boolean.TRUE);
        } else {
            mode = "edit";
            DocumentDAO dao = getDocumentDAO();
            document = dao.findDocument(new Integer(selectedID));
            if (document == null) {
               JSFUtil.redirect(REDIRECT_PAGE);
            }else {
                try {
                    if (document.getDocumentFolder()!=null) {
                        documentFolderId = document.getDocumentFolder().getId();
                    }
                    if (document.getDocumentUploadType()!=null) {
                        documentUploadTypeId = document.getDocumentUploadType().getId();
                    }
                    if (document.getUserGroup()!=null) {
                        userGroupId = document.getUserGroup().getId();
                    }
                } catch (NullPointerException e) {
                    userGroupId = 0;
                    documentFolderId = 0;
                    documentUploadTypeId = 0;
                }
            }
        }
    }
    
      public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:document:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:document:edit"); 
       }
    }

    private String urlUploadPath(String folder) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.US);
        String dir = sdf.format(new Date());
//        return "../upload/yesfile/" + dir+"/";
//        return "/upload/yesfile/" + dir + "/";
        return "/upload" + folder;
    }

    public String saveAction() {
        message = "";
        DocumentDAO dao = getDocumentDAO();
        String username = JSFUtil.getUserSession().getUserName();
        Date now = new Date();
            try {
                DocumentFolder documentFolder = documentFolderDAO.findDocumentFolder(documentFolderId);
                document.setDocumentFolder(documentFolder);
                DocumentUploadType documentUploadType = new DocumentUploadType(documentUploadTypeId);
                document.setDocumentUploadType(documentUploadType);
                if (userGroupId!=null && userGroupId!=0) {
                    document.setUserGroup(new UserGroup(userGroupId));
                }
                else {
                    document.setUserGroup(null);
                }
                File datafile = null;
                if(fileName != null) {
                    File f = new File(tmpPath + fileName);
                    if(f.exists()) {
                        datafile = new File(uploadPath + documentFolder.getPath() + fileName);
                        f.renameTo(datafile);
                    }
                }
                if  (fileName != null && realFileName != null && extFileName != null){
                    document.setFileName(realFileName + "." + extFileName + ":" + urlUploadPath(documentFolder.getPath())+fileName);
                }
                if (getMode().equals("add")) {
                    document.setId(null);
                    document.setEnable(true);
                    document.setCreateBy(username);
                    document.setCreateDate(now);
                    dao.create(document);
                } else {
                    document.setUpdateBy(username);
                    document.setUpdateDate(now);
                    
                    dao.edit(document);
                }
            } catch (Exception e) {
                log.error(e);
                message = e.getMessage();
                return FAILURE;
            }
            return SUCCESS;
    }
    
    public void clearUploadData() {
        File file = new File(tmpPath + fileName);
        file.delete();
    }
    
    public void uploadListener(FileUploadEvent event) throws Exception {
        Date date = new Date();
        item = event.getUploadedFile();

        String str = item.getName();

        int idx = str.lastIndexOf(File.separator);
        int idx1 = str.lastIndexOf("\\");
        if (idx != -1) {
            str = str.substring(idx + 1, str.length());
        } else if (idx1 != -1) {
            str = str.substring(idx1 + 1, str.length());
        }
        String[] fileNameAll = str.split("\\.");
        realFileName = fileNameAll[0];
        extFileName = fileNameAll[1];
        if (str.lastIndexOf("\\") != -1) {
            str = str.substring(str.lastIndexOf("\\") + 1, str.length());
        }
        if(!str.isEmpty() && str.indexOf(",") != -1)
            str = str.replace(",", "");
        fileName = realFileName + "_" + date.getTime() + "." + extFileName ;

        /*
        String str = item.getName();
        String[] fileNameAll = str.split("\\.");
        realFileName = fileNameAll[0];
        extFileName = fileNameAll[1];
        if (str.lastIndexOf("\\") != -1) {
            str = str.substring(str.lastIndexOf("\\") + 1, str.length());
        }
        if(!str.isEmpty() && str.indexOf(",") != -1)
            str = str.replace(",", "");
        fileName = realFileName + "_" + date.getTime() + "." + extFileName ;
        */
        setFileName(fileName);
        str = tmpPath + fileName;
        File file = new File(str);
        saveToFile(item.getInputStream(),file);
        
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
    
    public void deleteFileNameListener(ActionEvent event) {
        document.setFileName(null);
        FacesContext.getCurrentInstance().renderResponse();
    }
    
    public UploadedFile getItem() {
        return item;
    }

    public void setItem(UploadedFile item) {
        this.item = item;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public String getTmpPath() {
        return tmpPath;
    }

    public void setTmpPath(String tmpPath) {
        this.tmpPath = tmpPath;
    }

    public Map<String, Integer> getDocumentFolderList() {
        return documentFolderList;
    }

    public void setDocumentFolderList(Map<String, Integer> documentFolderList) {
        this.documentFolderList = documentFolderList;
    }
      
    public String backAction() {
        return SUCCESS;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
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
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public Integer getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
    }

    public Integer getDocumentFolderId() {
        return documentFolderId;
    }

    public void setDocumentFolderId(Integer documentFolderId) {
        this.documentFolderId = documentFolderId;
    }

    public Integer getDocumentUploadTypeId() {
        return documentUploadTypeId;
    }

    public void setDocumentUploadTypeId(Integer documentUploadTypeId) {
        this.documentUploadTypeId = documentUploadTypeId;
    }
    
    public Map<String, Integer> getDocumentUploadTypeList() {
        return documentUploadTypeList;
    }

    public void setDocumentUploadTypeList(Map<String, Integer> documentUploadTypeList) {
        this.documentUploadTypeList = documentUploadTypeList;
    }
    
    public DocumentDAO getDocumentDAO() {
        return documentDAO;
    }

    public void setDocumentDAO(DocumentDAO documentDAO) {
        this.documentDAO = documentDAO;
    }

    public DocumentFolderDAO getDocumentFolderDAO() {
        return documentFolderDAO;
    }
    
    public void setDocumentFolderDAO(DocumentFolderDAO documentFolderDAO) {
        this.documentFolderDAO = documentFolderDAO;
    }

    public UserGroupDAO getUserGroupDAO() {
        return userGroupDAO;
    }

    public void setUserGroupDAO(UserGroupDAO userGroupDAO) {
        this.userGroupDAO = userGroupDAO;
    }

    public DocumentUploadTypeDAO getDocumentUploadTypeDAO() {
        return documentUploadTypeDAO;
    }

    public void setDocumentUploadTypeDAO(DocumentUploadTypeDAO documentUploadTypeDAO) {
        this.documentUploadTypeDAO = documentUploadTypeDAO;
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

    public String getExtFileName() {
        return extFileName;
    }

    public void setExtFileName(String extFileName) {
        this.extFileName = extFileName;
    }

    public String getRealFileName() {
        return realFileName;
    }

    public void setRealFileName(String realFileName) {
        this.realFileName = realFileName;
    }
}
