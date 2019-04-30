package com.maxelyz.core.controller.admin;

//import com.maxelyz.converter.PaymentModeConverter;
//import com.maxelyz.core.common.dto.PaymentMethodStringCastor;
import com.maxelyz.core.model.dao.DocumentDAO;
import com.maxelyz.core.model.dao.DocumentUploadTypeDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import com.maxelyz.core.model.dao.ProductPlanDAO;
//import com.maxelyz.core.model.entity.ProductPlan;
import javax.faces.bean.*;

import com.maxelyz.core.model.dao.ProductDAO;
import com.maxelyz.core.model.dao.ProductDocumentDAO;
//import com.maxelyz.core.model.dao.ProductPlanCategoryDAO;
import com.maxelyz.core.model.dao.UserGroupDAO;
import com.maxelyz.core.model.entity.Document;
import com.maxelyz.core.model.entity.Product;
import com.maxelyz.core.model.entity.ProductDocument;
//import com.maxelyz.core.model.entity.ProductPlanCategory;
//import com.maxelyz.core.model.entity.ProductPlanDetail;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
//import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
//import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
//import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
//import javax.faces.model.SelectItem;
import javax.faces.bean.ViewScoped;

@ManagedBean
//@RequestScoped
@ViewScoped
public class ProductDocumentEditController {
    private static Log log = LogFactory.getLog(ProductDocumentEditController.class);
    private static String SUCCESS = "productdocument.xhtml";
    private static String FAILURE = "productdocumentedit.xhtml";
    private static String BACK = "productdocument.xhtml";
    private static String REDIRECT_PAGE = "productdocument.jsf";

    private ProductDocument productDocument;
    private String mode;

    private Product product;

    private String productID="";
    private String productDocumentID="";
    
    private Boolean checkDupDocument=false;
    private String viewNameCheckDup="";
    private String messageStatusDup="";

    @ManagedProperty(value="#{productDAO}")
    private ProductDAO productDAO;
    @ManagedProperty(value="#{productDocumentDAO}")
    private ProductDocumentDAO productDocumentDAO;
    
    
    
//    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
    private List<Document> documents;
    private Date fromDate = new Date();
    private Map<String, Integer> documentUploadTypeList;
    private Map<String, Integer> userGroupList;
//    private Document document;
    private String title;
    private String fileName;
    private String name;
    private String documentid;
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
        if (!SecurityUtil.isPermitted("admin:productinformation:view")) {
            SecurityUtil.redirectUnauthorize();
        }

        productID = JSFUtil.getRequestParameterMap("productID");
        productDocumentID = JSFUtil.getRequestParameterMap("productDocumentId");
        
        if (productID == null || productID.isEmpty()) {
            JSFUtil.redirect(REDIRECT_PAGE);
            return;
        } else {
            ProductDAO daoProduct = getProductDAO();
            product = daoProduct.findProduct(new Integer(productID));
        }

        if (productDocumentID == null || productDocumentID.isEmpty()) {
            mode = "add";
            productDocument = new ProductDocument();
            productDocument.setEnable(Boolean.TRUE);
            productDocument.setStatus(Boolean.TRUE);
            productDocument.setProduct(product);
            productDocument.setDocument(new Document());
            
        } else {
            mode = "edit";
            ProductDocumentDAO dao = getProductDocumentDAO();
            productDocument = dao.findProductDocument(new Integer(productDocumentID));
            productDocument.setProduct(product);
            if (productDocument==null)
            {
                JSFUtil.redirect(REDIRECT_PAGE);
            }
        }
        
        DocumentDAO dao = getDocumentDAO();
        documents = dao.findDocumentEntities();
        documentUploadTypeList = this.getDocumentUploadTypeDAO().getDocumentUploadTypeList();
        userGroupList = this.getUserGroupDAO().getUserGroupList();
        
    }

    public boolean isSavePermitted() {
   	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:productinformation:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:productinformation:edit"); 
       }
    }  
        
    public String saveAction() {
        try {
            Boolean isStatusDup = false;
            ProductDocumentDAO dao = getProductDocumentDAO();
            if(productDocumentDAO.checkStatusbyDocumentIdAndProductId(new Integer(documentid),product.getId()).size()>0)
            {
                List<String> selectedStatusByDocumentList = productDocumentDAO.checkStatusbyDocumentIdAndProductId(new Integer(documentid),product.getId());
                for(int i=0;i<selectedStatusByDocumentList.size();i++)
                {
                    if(productDocument.getPurchaseOrderStatus().equals(selectedStatusByDocumentList.get(i)))
                    {
                        isStatusDup = true;
                    }
                }
            }
            
            if(isStatusDup)
            {
                messageStatusDup = "This status is already have in this document.";
                return "";
            }
            else
            {   
                messageStatusDup = "";
                if (getMode().equals("add")) {
                        productDocument.setId(null);
                        dao.create(productDocument);
                } else {
                    dao.edit(productDocument);
                }
            }
        } catch (Exception e) {
            log.error(e);
            return FAILURE;
        }
        return SUCCESS;
    } 
        
    

    public String backAction() {
        return BACK;
    }

    //Listener
    public void initListener(ActionEvent e) {
        initialize();
    }

    public ProductDocumentDAO getProductDocumentDAO() {
        return productDocumentDAO;
    }

    public void setProductDocumentDAO(ProductDocumentDAO productDocumentDAO) {
        this.productDocumentDAO = productDocumentDAO;
    }

    /**
     * @return the mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * @return the product
     */
    public Product getProduct() {
        return product;
    }

    public ProductDocument getProductDocument() {
        return productDocument;
    }

    public void setProductDocument(ProductDocument productDocument) {
        this.productDocument = productDocument;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(Product product) {
        this.product = product;
    }


    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

     public List<Document> getList() {
        return getDocuments();
    }
     
    public void searchActionListener(ActionEvent event){
        this.search();
    }
    
    private void search(){
        documents = documentDAO.findDocumentBySearch(title,fileName,dateFrom,dateTo,userGroupId,documentUploadTypeId,status);
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

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
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

    public Boolean getCheckDupDocument() {
        return checkDupDocument;
    }

    public void setCheckDupDocument(Boolean checkDupDocument) {
        this.checkDupDocument = checkDupDocument;
    }

    public String getViewNameCheckDup() {
        return viewNameCheckDup;
    }

    public void setViewNameCheckDup(String viewNameCheckDup) {
        this.viewNameCheckDup = viewNameCheckDup;
    }

    public String getMessageStatusDup() {
        return messageStatusDup;
    }

    public void setMessageStatusDup(String messageStatusDup) {
        this.messageStatusDup = messageStatusDup;
    }
    
    public void selectTitleListener() {
         documentid = (String) JSFUtil.getRequestParameterMap("documentID");
         Document document = documentDAO.findDocument(new Integer(documentid));
         productDocument.setDocument(document);
         
         if(!(productDocumentDAO.checkMergeViewbyDocumentIdAndProductId(new Integer(documentid),product.getId()).equals("")))
         {
             viewNameCheckDup = productDocumentDAO.checkMergeViewbyDocumentIdAndProductId(new Integer(documentid),product.getId());
             productDocument.setViewName(viewNameCheckDup);
             checkDupDocument = true;
         }
         else
         {
             checkDupDocument = false;
         }
    }
    
    public Map<String, String> getMergeDataViewList() {
        Map<String, String> values = new LinkedHashMap<String, String>();;
        if(checkDupDocument)
        {
            values = this.getProductDocumentDAO().getSpecificMergeDataViewList(viewNameCheckDup);
        }
        else
        {
            values = this.getProductDocumentDAO().getMergeDataViewList();
        }
        //return this.getProductDocumentDAO().getMergeDataViewList();
        return values;
    }
}
