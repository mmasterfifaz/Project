package com.maxelyz.core.controller.admin;

import com.maxelyz.core.model.dao.ProductCategoryDAO;
import java.io.Serializable;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.maxelyz.core.model.dao.ProductDAO;
import com.maxelyz.core.model.dao.ProductDocumentDAO;
import com.maxelyz.core.model.dao.ProductPlanDAO;
import com.maxelyz.core.model.dao.exceptions.NonexistentEntityException;
import com.maxelyz.core.model.entity.Product;
import com.maxelyz.core.model.entity.ProductDocument;
import com.maxelyz.core.model.entity.ProductPlan;
import com.maxelyz.core.model.entity.ProductPlanDetail;
import com.maxelyz.core.model.entity.ProductWorkflow;
import com.maxelyz.utils.JSFUtil;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.*;
import com.maxelyz.core.service.SecurityService;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.concurrent.ConcurrentHashMap;
import javax.transaction.UserTransaction;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

@ManagedBean
//@RequestScoped
@ViewScoped
public class ProductDocumentController implements Serializable{

    private static String REFRESH = "productdocument.xhtml";
    private static String EDIT = "productdocumentedit.xhtml";
    private static String BACK = "product.xhtml?faces-redirect=true";
    private static String REDIRECT_PAGE = "product.jsf";
    private static Log log = LogFactory.getLog(ProductDocumentController.class);

    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>(); //use for checkbox
    private List<ProductDocument> productdocuments;
    private int productCategoryId;
    private Product product;
    private ProductDocument productdocument;
    private int id;
    
    private FileUploadBean fileUploadBean = new FileUploadBean();
    private String rootPath = JSFUtil.getRealPath()+"upload\\product\\";//"D:\\crm\\MaxarCRM\\web\\upload\\";

    @ManagedProperty(value="#{productDAO}")
    private ProductDAO productDAO;
    @ManagedProperty(value="#{productCategoryDAO}")
    private ProductCategoryDAO productCategoryDAO;
    @ManagedProperty(value="#{productPlanDAO}")
    private ProductPlanDAO productPlanDAO;
    @ManagedProperty(value = "#{productDocumentDAO}")
    private ProductDocumentDAO productDocumentDAO;
   /* @ManagedProperty(value="#{productPlanDetailDAO}")
    private ProductPlanDAO productPlanDetailDAO;*/
    
    

    @PostConstruct
    public void initialize() {
        
        if (!SecurityUtil.isPermitted("admin:productinformation:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        
        String productID = JSFUtil.getRequestParameterMap("productID");
        
        if (productID == null || productID.isEmpty()) {
            JSFUtil.redirect(REDIRECT_PAGE);
            return;
        } else {
//            ProductDocumentDAO daoProductdocument = getProductDocumentDAO();
//            productdocument = daoProductdocument.findProductDocument(new Integer(productID));
            ProductDAO daoProduct = getProductDAO();
            product = daoProduct.findProduct(new Integer(productID));
        }
        
       ProductDocumentDAO dao = getProductDocumentDAO();
       productdocuments = dao.findProductDocumentByProductId(new Integer(productID));
//       productdocument = dao.findProductDocument(new Integer(productID));
    }

    public Product getProduct() {
        return product;
    }

    public void setProd(Product prod) {
        this.product = prod;
    }

    public ProductDocument getProductdocument() {
        return productdocument;
    }

    public void setProductdocument(ProductDocument productdocument) {
        this.productdocument = productdocument;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAddPermitted() {
        return SecurityUtil.isPermitted("admin:productinformation:add");
    }

    public boolean isDeletePermitted() {
        return SecurityUtil.isPermitted("admin:productinformation:delete");
    }

    public List<ProductDocument> getList() {
        return getProductdocuments();
    }
   
    
     public String editAction() {
       return EDIT;
    }

     public String backAction() {
        return BACK;
    }

    public String deleteAction(){
        ProductDocumentDAO dao = getProductDocumentDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    productdocument = dao.findProductDocument(item.getKey());
                    productdocument.setEnable(false);
                    dao.edit(productdocument);
                    
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        initialize();
        return REFRESH;
    }
    
    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public ProductDocumentDAO getProductDocumentDAO() {
        return productDocumentDAO;
    }

    public void setProductDocumentDAO(ProductDocumentDAO productDocumentDAO) {
        this.productDocumentDAO = productDocumentDAO;
    }

    public List<ProductDocument> getProductdocuments() {
        return productdocuments;
    }

    public void setProductdocuments(List<ProductDocument> productdocuments) {
        this.productdocuments = productdocuments;
    }

    public int getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(int productCategoryId) {
        this.productCategoryId = productCategoryId;
    }



    public ProductDAO getProductDAO() {
        return productDAO;
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public ProductCategoryDAO getProductCategoryDAO() {
        return productCategoryDAO;
    }

    public void setProductCategoryDAO(ProductCategoryDAO productCategoryDAO) {
        this.productCategoryDAO = productCategoryDAO;
    }
   
    public ProductPlanDAO getProductPlanDAO() {
        return productPlanDAO;
    }

    public void setProductPlanDAO(ProductPlanDAO productPlanDAO) {
        this.productPlanDAO = productPlanDAO;
    }
    
}
