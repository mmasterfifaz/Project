/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.core.controller.admin;


import com.maxelyz.core.model.dao.ChildRegFormDAO;
import com.maxelyz.core.model.dao.ChildRegTypeDAO;
import com.maxelyz.core.model.dao.ProductChildRegTypeDAO;
import com.maxelyz.core.model.dao.ProductDAO;
import com.maxelyz.core.model.entity.ChildRegForm;
import com.maxelyz.core.model.entity.ChildRegType;
import com.maxelyz.core.model.entity.Product;
import com.maxelyz.core.model.entity.ProductChildRegType;
import com.maxelyz.core.model.entity.ProductChildRegTypePK;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.richfaces.model.UploadedFile;

/**
 *
 * @author TBN
 */
@ManagedBean
//@RequestScoped
@ViewScoped
public class ProductChildRegEditController {
    public static int MAX_FLEX_FIELD = 10;
    private static Log log = LogFactory.getLog(ChildRegFormEditController.class);
//    private static String SUCCESS = "productchildreg.xhtml?faces-redirect=true";
    private static String SUCCESS = "productchildreg.xhtml";
    private static String FAILURE = "productchildreg.xhtml";
    private static String REFRESH = "productchildregedit.xhtml";

    private ProductChildRegType productChildRegType;
    String mode;
    private String message; 
    private String messageDup;
    @ManagedProperty(value = "#{productChildRegTypeDAO}")
    private ProductChildRegTypeDAO productChildRegTypeDAO;
    @ManagedProperty(value="#{productDAO}")
    private ProductDAO productDAO;
    @ManagedProperty(value = "#{childRegTypeDAO}")
    private ChildRegTypeDAO childRegTypeDAO;
    @ManagedProperty(value = "#{childRegFormDAO}")
    private ChildRegFormDAO childRegFormDAO;

    private Product product;
    private ChildRegType childRegType;
    private String code;
    private String name;
    private UploadedFile item;
    private String extFileName;
    private String realFileName;
    private String fileName;
//    private String uploadPath = JSFUtil.getuploadPath();  //JSFUtil.getRealPath() + "upload\\import\\";
//    private String tmpPath = JSFUtil.getuploadPath()+"document/declaration/";   //JSFUtil.getRealPath() + "upload\\document\\declaration"; 
    public int count_seqNo=0;
    public int count_seqNo2=0;
    private Map<String,Integer> childRegTypeList;
    private List<ProductChildRegType> productChildRegTypeList;
    private List<ChildRegForm> childRegFormAllList;
    private Map<Integer, Boolean> selectedIds = new ConcurrentHashMap<Integer, Boolean>();
//    private List<ArrayList<ChildRegForm>> childRegFormList;
    private ProductChildRegTypePK productChildRegTypePK;
    public String productID;
    String childRegTypeID="";
    Integer childRegTypeIdSelected;
    private String messageForm;
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:childreg:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
                
        messageDup = "";
        productID = JSFUtil.getRequestParameterMap("productID");
        mode = JSFUtil.getRequestParameterMap("mode");
        
        ChildRegTypeDAO daoChildRegType = getChildRegTypeDAO();
        childRegTypeList = daoChildRegType.getChildRegTypeList();
        
        if (mode.equals("add")) {
            
            productChildRegType = new ProductChildRegType();
            productChildRegType.setChildRegType(new ChildRegType());
            ProductDAO daoProduct = getProductDAO();
            product = daoProduct.findProduct(new Integer(productID));
           // declarationForm.setQuestionnaire(new Questionnaire());

        } else {
            childRegTypeID = JSFUtil.getRequestParameterMap("childRegTypeID");
            productChildRegTypePK = new ProductChildRegTypePK(new Integer(productID),new Integer(childRegTypeID));
            ProductChildRegTypeDAO dao = getProductChildRegTypeDAO();
//            productChildRegType = dao.findProductChildRegType(new Integer(productID));
            productChildRegTypeList = dao.findProductChildRegTypeByProductIdAndChildRegTypeID(new Integer(productID), new Integer(childRegTypeID));

            ChildRegFormDAO daoChildRegForm = getChildRegFormDAO();
            childRegFormAllList = daoChildRegForm.findChildRegFormListByChildRegTypeId(new Integer(childRegTypeID));
            ProductDAO daoProduct = getProductDAO();
            product = daoProduct.findProduct(new Integer(productID));
            childRegTypeIdSelected = new Integer(childRegTypeID);
            childRegType = daoChildRegType.findChildRegType(childRegTypeIdSelected);
            
            for(ChildRegForm childReg : childRegFormAllList)
            {
                String[] typelist = productChildRegTypeList.get(0).getChildRegFormList().split(",");
                for(String listid : typelist)
                {
                    Integer id = new Integer(listid);
                    if(childReg.getId().equals(id))
                    {
                        selectedIds.put(new Integer(listid), Boolean.TRUE);
                    }
                }
            }
        }
        
        
        
    }

    public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:childreg:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:childreg:edit"); 
       }
    }  

    public String saveAction() {
        messageDup = "";
        messageForm = "";
        if(checkName(childRegType)) {
            if(productChildRegTypeList!=null)
            {
                productChildRegType = productChildRegTypeList.get(0);
            }
            ProductChildRegTypeDAO dao = getProductChildRegTypeDAO();
            try {
                String userName = JSFUtil.getUserSession().getUserName();
                Date now = new Date();
//                declarationForm.setEnable(true);
//                productChildRegType.setChildRegFieldCollection(mergeChildRegField()); 
//                productChildRegType.setUpdateBy(userName);
//                productChildRegType.setUpdateDate(now);
                String childRegFormListStr = "";
                
                boolean check = false;
                
                for(ChildRegForm childReg : childRegFormAllList)
                {
                    if(selectedIds.get(childReg.getId()).booleanValue())
                    {
                        childRegFormListStr += childReg.getId()+",";
                        check = true;
                    }
                }
                
                if (check) {
                    childRegFormListStr = childRegFormListStr.substring(0, childRegFormListStr.length()-1);
                    productChildRegType.setChildRegFormList(childRegFormListStr);
                    productChildRegType.setChildRegType(childRegType);
                    productChildRegType.setProductChildRegTypePK(productChildRegTypePK);

                    if (getMode().equals("add")) {
                        productChildRegType.setId(null);
    //                    productChildRegType.setChildRegType(childRegType);
                        productChildRegType.setProduct(product);
    //                    productChildRegType.setProductChildRegTypePK(productChildRegTypePK);
                        productChildRegType.setEnable(Boolean.TRUE);
                        productChildRegType.setSeqNo(1);
    //                    childRegForm.setCreateBy(userName);
    //                    childRegForm.setCreateDate(now);
                        dao.create(productChildRegType);
                    } else {       
                        dao.edit(productChildRegType);
                    }
                } else {
                    messageForm = "Child Registration Form must selected at least 1 value";
                    return null;
                }
            } catch (Exception e) {
                log.error(e);
                message = e.getMessage();
                return null;
//                return FAILURE;
            }
            return SUCCESS;
         } else {
            messageDup = " Child Registration Type is already taken";
            return null;
//            return FAILURE;            
        }
    }

    public void changeChildRegType()
    {
        ChildRegTypeDAO daoChildRegType = getChildRegTypeDAO();
        childRegType = daoChildRegType.findChildRegType(childRegTypeIdSelected);
        productChildRegTypePK = new ProductChildRegTypePK(new Integer(productID),childRegTypeIdSelected);
        ChildRegFormDAO daoChildRegForm = getChildRegFormDAO();
        childRegFormAllList = daoChildRegForm.findChildRegFormListByChildRegTypeId(childRegTypeIdSelected);
        
    }
    
    public Boolean checkName(ChildRegType childRegType) {
//        String name = childRegType.getName();
        Integer id=0; 
        if(childRegType.getId() != null)
            id = childRegType.getId();
//        ChildRegTypeDAO dao = getChildRegTypeDAO();
        ProductChildRegTypeDAO dao = getProductChildRegTypeDAO();
        Integer cnt = dao.checkProductChildRegTypeName(id,new Integer(productID));
        
        if(mode.equals("add"))
        {
            if(cnt == 0)
                return true;
            else
                return false;
        }
        else
        {
            return true;
        }
    }
    
    public String backAction() {
        return FAILURE;
    }
    
    public String getMessage() {
        return message;
    }

    public String getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    //Managed Properties
    public ProductChildRegTypeDAO getProductChildRegTypeDAO() {
        return productChildRegTypeDAO;
    }

    public void setProductChildRegTypeDAO(ProductChildRegTypeDAO productChildRegTypeDAO) {
        this.productChildRegTypeDAO = productChildRegTypeDAO;
    }
    
    public String deleteAction(){
        ProductChildRegTypeDAO dao = getProductChildRegTypeDAO();
        try {
            for (Map.Entry<Integer, Boolean> item : selectedIds.entrySet()) {
                if (item.getValue()) {
                    productChildRegType = dao.findProductChildRegType(item.getKey());
                    productChildRegType.setEnable(false);
                    dao.edit(productChildRegType);
                    
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        initialize();
        return SUCCESS;
    }
    
/*
    public QuestionnaireDAO getQuestionnaireDAO() {
        return questionnaireDAO;
    }

    public void setQuestionnaireDAO(QuestionnaireDAO questionnaireDAO) {
        this.questionnaireDAO = questionnaireDAO;
    }
*/

    public String getMessageDup() {
        return messageDup;
    }

    public void setMessageDup(String messageDup) {
        this.messageDup = messageDup;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public ChildRegTypeDAO getChildRegTypeDAO() {
        return childRegTypeDAO;
    }

    public void setChildRegTypeDAO(ChildRegTypeDAO childRegTypeDAO) {
        this.childRegTypeDAO = childRegTypeDAO;
    }

    public Map<String, Integer> getChildRegTypeList() {
        return childRegTypeList;
    }

    public void setChildRegTypeList(Map<String, Integer> childRegTypeList) {
        this.childRegTypeList = childRegTypeList;
    }

    

    public Integer getChildRegTypeIdSelected() {
        return childRegTypeIdSelected;
    }

    public void setChildRegTypeIdSelected(Integer childRegTypeIdSelected) {
        this.childRegTypeIdSelected = childRegTypeIdSelected;
    }

    public ChildRegFormDAO getChildRegFormDAO() {
        return childRegFormDAO;
    }

    public void setChildRegFormDAO(ChildRegFormDAO childRegFormDAO) {
        this.childRegFormDAO = childRegFormDAO;
    }

    public List<ChildRegForm> getChildRegFormAllList() {
        return childRegFormAllList;
    }

    public void setChildRegFormAllList(List<ChildRegForm> childRegFormAllList) {
        this.childRegFormAllList = childRegFormAllList;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

//    public List<ArrayList<ChildRegForm>> getChildRegFormList() {
//        return childRegFormList;
//    }
//
//    public void setChildRegFormList(List<ArrayList<ChildRegForm>> childRegFormList) {
//        this.childRegFormList = childRegFormList;
//    }
    
    public Map<Integer, Boolean> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(Map<Integer, Boolean> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<ProductChildRegType> getProductChildRegTypeList() {
        return productChildRegTypeList;
    }

    public void setProductChildRegTypeList(List<ProductChildRegType> productChildRegTypeList) {
        this.productChildRegTypeList = productChildRegTypeList;
    }

    public ChildRegType getChildRegType() {
        return childRegType;
    }

    public void setChildRegType(ChildRegType childRegType) {
        this.childRegType = childRegType;
    }

    public String getMessageForm() {
        return messageForm;
    }

    public void setMessageForm(String messageForm) {
        this.messageForm = messageForm;
    }
    
}
