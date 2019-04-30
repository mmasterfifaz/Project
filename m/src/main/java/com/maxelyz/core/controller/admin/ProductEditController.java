package com.maxelyz.core.controller.admin;

import com.maxelyz.converter.PaymentModeConverter;
import com.maxelyz.core.common.dto.FirstInstallmentOptions;
import com.maxelyz.core.common.dto.KeyOptionDTO;
import com.maxelyz.core.common.dto.PaymentMethodStringCastor;
import com.maxelyz.core.model.dao.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.maxelyz.core.model.entity.Product;
import javax.faces.bean.*;
import com.maxelyz.utils.FileUploadBean;

import javax.faces.model.SelectItem;
import java.util.*;
import com.maxelyz.core.model.entity.DeliveryMethod;
import com.maxelyz.core.model.entity.ProductSponsor;
import com.maxelyz.core.model.entity.ProductCategory;
import com.maxelyz.core.model.entity.FileTemplate;
import com.maxelyz.core.model.entity.RegistrationForm;
import com.maxelyz.core.model.entity.*;
import com.maxelyz.utils.JSFUtil;
import com.maxelyz.utils.SecurityUtil;
import java.text.SimpleDateFormat;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

@ManagedBean
@ViewScoped
public class ProductEditController {
    private static Log log = LogFactory.getLog(ProductEditController.class);
    private static String REDIRECT_PAGE = "product.jsf";
    private static String SUCCESS = "product.xhtml?faces-redirect=true";
    private static String FAILURE = "productedit.xhtml";


    private Product product;
    private String mode;
    private Boolean isInsuranceProduct=false;
    private String monthlyFirstPayment;
    private String quarterlyFirstPayment;
    private String message;
            

    //ProductSponsor
    private ProductSponsor productSponsor;
    private SelectItem[] productSponsorList;
    private List<ProductSponsor> productSponsors;

    //ProductCategory
    private ProductCategory productCategory;
    private SelectItem[] productCategoryList;
    private List<ProductCategory> productCategorys;

    //FormTemplate
    private FileTemplate fileTemplate;
    private SelectItem[] fileTemplateList;
    private List<FileTemplate> fileTemplates;

    //RegistrationForm
    private RegistrationForm registrationForm;
    private SelectItem[] registrationFormList;
    private List<RegistrationForm> registrationForms;

    //Payment Mode
    private SelectItem[] paymentModeList;
    private String[] paymentModeString;

    //Payment Method
    private SelectItem[] paymentMethodList;
    private String[] paymentMethodString;

    //Payment Method
    private SelectItem[] cardTypeList;
    private String[] cardTypeString;
    private String[] cardTypeDebitString;

    //Payment Method
    private List<Bank> bankList;
    private String[] bankString;
    private String[] bankDebitString;

    //Delivery Method
    private SelectItem[] deliveryMethodList;
    private String[] deliveryMethodString;
    //private List<DeliveryMethod> list;
    private DeliveryMethod deliveryMethod;
    
    //Delete Thumbnail
    private SelectItem[] deleteThumbnail;
    private String[] deleteThumbnailString;

    // Customer Field List
    private List<TagField> tagFieldList;
    private String tag="";
    
    //FileUpload
    private FileUploadBean fileUploadBean = new FileUploadBean(JSFUtil.getuploadPath(), "product");
    
    private Brand brand;
    private Model model;  
    private ModelType modelType;   
    private List<Brand> brandList;
    private List<Model> modelList = new ArrayList<Model>();
    private List<ModelType> modelTypeList = new ArrayList<ModelType>();
    
    private SelectItem[] modelYearList;
    private SelectItem[] modelFromYearList;
    private SelectItem[] modelToYearList;
    
    private List<ApprovalRule> approvalRuleList;
    private Boolean creditCard = false;
    private Boolean cash = false;
    private Boolean bankTransfer = false;
    private Boolean checkBankAll = false;
    private Boolean checkBankAllDebit = false;
    private List<SequenceNo> sequenceNoList;
    private Collection<ProductWorkflow> productWorkflowList;
    private Map<String, String> stepList;
    private Integer approvalRuleId;
    private List<KeyOptionDTO> paymentMethodOptions;
    private List<PaymentMethodStringCastor> paymentModeOptions;
    private SelectItem[] contactResultPlanList;
    private Integer contactResultPlanId ;
    
    //Declaration
    private Map<String, String>fieldList;
    private List<String> selectedField = new ArrayList<String>();
    private List<String> selectedId = new ArrayList<String>();
    private ProductDeclarationMapping productDeclarationMapping;
    private ProductDeclarationMappingPK productDeclarationMappingPK;
    
    //Family Product
    private Integer familyType;
    private String familyLogic;
    private Integer showCoverage;
    
    @ManagedProperty(value = "#{productDeclarationMappingDAO}")
    private ProductDeclarationMappingDAO productDeclarationMappingDAO;

    @ManagedProperty(value = "#{declarationFormDAO}")
    private DeclarationFormDAO declarationFormDAO;

    @ManagedProperty(value="#{productDAO}")
    private ProductDAO productDAO;

    @ManagedProperty(value="#{productSponsorDAO}")
    private ProductSponsorDAO productSponsorDAO;

    @ManagedProperty(value="#{productCategoryDAO}")
    private ProductCategoryDAO productCategoryDAO;

    @ManagedProperty(value="#{fileTemplateDAO}")
    private FileTemplateDAO fileTemplateDAO;

    @ManagedProperty(value="#{registrationFormDAO}")
    private RegistrationFormDAO registrationFormDAO;

    @ManagedProperty(value="#{brandDAO}")
    private BrandDAO brandDAO;

    @ManagedProperty(value="#{modelDAO}")
    private ModelDAO modelDAO;

    @ManagedProperty(value="#{modelTypeDAO}")
    private ModelTypeDAO modelTypeDAO;

    @ManagedProperty(value="#{approvalRuleDAO}")
    private ApprovalRuleDAO approvalRuleDAO;

    @ManagedProperty(value="#{tagFieldDAO}")
    private TagFieldDAO tagFieldDAO;

    @ManagedProperty(value = "#{deliveryMethodDAO}")
    private DeliveryMethodDAO deliveryMethodDAO;

    @ManagedProperty(value = "#{bankDAO}")
    private BankDAO bankDAO;

    @ManagedProperty(value = "#{sequenceNoDAO}")
    private SequenceNoDAO sequenceNoDAO;

    @ManagedProperty(value = "#{productWorkflowDAO}")
    private ProductWorkflowDAO productWorkflowDAO;

    @ManagedProperty(value = "#{paymentMethodDAO}")
    private PaymentMethodDAO paymentMethodDAO;

    @ManagedProperty(value = "#{contactResultPlanDAO}")
    private ContactResultPlanDAO contactResultPlanDAO;
    
    @ManagedProperty(value = "#{productPlanDAO}")
    private ProductPlanDAO productPlanDAO;
        
    @PostConstruct
    public void initialize() {
        if (!SecurityUtil.isPermitted("admin:productinformation:view")) {
            SecurityUtil.redirectUnauthorize();  
        }
        message = "";
        String selectedID = (String) JSFUtil.getRequestParameterMap("id");
        this.setProductCategoryList();
        this.setContactResultPlanList();
        this.setProductSponsorList();
        this.setFileTemplateList();
        this.setRegistrationFormList();
        this.initStepList();
        approvalRuleList = approvalRuleDAO.findApprovalRuleEntities();
        initBank();
        initSequenceNo();
        if (selectedID==null || selectedID.isEmpty()) {
            mode = "add";
            product = new Product();

            productSponsor = new ProductSponsor();
            product.setProductSponsor(productSponsor);
            productCategory = new ProductCategory();
            product.setProductCategory(productCategory);
            registrationForm = new RegistrationForm();
            product.setRegistrationForm(registrationForm);
            brand = new Brand();
            model = new Model();
            modelType = new ModelType();
            model.setBrand(brand);
            product.setModel(model);
            product.setModelType(modelType);
            //product.setApprovalRule(new ApprovalRule());
            product.setAllowCopy(Boolean.FALSE);
            this.monthlyFirstPayment = "2";
            product.setSequenceNo(new SequenceNo());
            product.setFamilyProduct(Boolean.FALSE);

            productWorkflowList = new ArrayList<ProductWorkflow>();
            ProductWorkflow productWorkflow = new ProductWorkflow();
            productWorkflow.setStepNo(1);
            productWorkflow.setProduct(product);
            productWorkflow.setCreateBy(JSFUtil.getUserSession().getUserName());
            productWorkflow.setCreateDate(new Date());
            productWorkflowList.add(productWorkflow);

            Map<String, String> values = new LinkedHashMap<String, String>();
            List<DeclarationForm> fieldLists = this.getDeclarationFormDAO().findDeclarationFormEntitiesEnable();
            int i=0;
            for (DeclarationForm o : fieldLists) {
                values.put(o.getName(),String.valueOf(i++));
            }
            fieldList = values;

            selectedField.clear();
        } else {
            mode = "edit"; 
            product = this.getProductDAO().findProduct(new Integer(selectedID));
            if (product.getApprovalRule() != null) {
                approvalRuleId = product.getApprovalRule().getId();
                //product.setApprovalRule(new ApprovalRule());
            }
            productCategory = product.getProductCategory();
            if (product == null) {
                JSFUtil.redirect(REDIRECT_PAGE);
                return;
            }
            this.findProductCategoryType(product.getProductCategory().getId());
            try {
                this.monthlyFirstPayment = product.getMonthlyFirstPayment().toString();
            } catch (Exception e) {
                this.monthlyFirstPayment = "1";
            }
            try {
                this.quarterlyFirstPayment = product.getQuarterlyFirstPayment().toString();
            } catch (Exception e) {
                this.quarterlyFirstPayment = "1";
            }
            initBrand();
            if (product != null && product.getPaymentMethod() != null && !product.getPaymentMethod().isEmpty()) {
                if (product.getPaymentMethod().indexOf("1") != -1) {
                    this.creditCard = true;
                }
                if (product.getPaymentMethod().indexOf("2") != -1) {
                    this.cash = true;
                }
                if (product.getPaymentMethod().indexOf("3") != -1) {
                    this.bankTransfer = true;
                }
            }
            if (product != null && product.getCardBank() != null && !product.getCardBank().isEmpty()) {
                String str = product.getCardBank();
                if (str == null) {
                    str = "0";
                }
                this.bankString = str.split(",");
            }

            if (bankString != null && bankList != null && bankString.length == bankList.size()) {
                checkBankAll = true;
            }

            if (product != null && product.getCardBankDebit() != null && !product.getCardBankDebit().isEmpty()) {
                String str = product.getCardBankDebit();
                if (str == null) {
                    str = "0";
                }
                this.bankDebitString = str.split(",");
            }

            if (product != null && !product.equals("")) {
                showCoverage = product.getProductPlanCoverage();
            }

            if (bankDebitString != null && bankList != null && bankDebitString.length == bankList.size()) {
                checkBankAllDebit = true;
            }
            if (product.getProductWorkflowCollection() == null || product.getProductWorkflowCollection().isEmpty()){
                productWorkflowList = new ArrayList<ProductWorkflow>();
                ProductWorkflow productWorkflow = new ProductWorkflow();
                productWorkflow.setStepNo(1);
                productWorkflow.setProduct(product);
                productWorkflow.setCreateBy(JSFUtil.getUserSession().getUserName());
                productWorkflow.setCreateDate(new Date());
                productWorkflowList.add(productWorkflow);                
            }else{
                productWorkflowList = productWorkflowDAO.findProductWorkflowByProductId(product.getId());
            }
            if(product != null && product.getContactResultPlan() != null && product.getContactResultPlan().getId() != null){
                contactResultPlanId = product.getContactResultPlan().getId();
            }
            Map<String, String> values = new LinkedHashMap<String, String>();
            List<DeclarationForm> fieldLists = this.getDeclarationFormDAO().findDeclarationFormEntitiesEnable();
            int i=0;
            for (DeclarationForm o : fieldLists) {
                values.put(o.getName(),String.valueOf(i++));
            }
            fieldList = values;
            selectedField.clear();
            ProductDeclarationMappingDAO dao = getProductDeclarationMappingDAO();
            List<ProductDeclarationMapping> productDeclarationMappingPages = dao.findProductDeclarationMappingByProductId(Integer.parseInt(selectedID));
            if(productDeclarationMappingPages.size() > 0) {                
                for(ProductDeclarationMapping o : productDeclarationMappingPages){
                    selectedField.add(fieldList.get((this.getDeclarationFormDAO().findDeclarationFormById(o.getProductDeclarationMappingPK().getDeclarationFormId())).getName()));          
                }
            }
            if(product != null) {
                if(product.getFamilyProductType() != null) {
                    familyType = product.getFamilyProductType();
                }
                if(product.getFamilyProductLogic()!= null) {
                    familyLogic = product.getFamilyProductLogic();
                }
            }
        }

        this.loadForm();
    }

    private void initStepList(){
        stepList = new LinkedHashMap<String, String>();
        stepList.put("UW", "Uw");
        //stepList.put("Supervisor", "Supervisor");
        stepList.put("Payment", "Payment");
        stepList.put("QC", "Qc");
    }
    
    public void addProductWorkflowListener(ActionEvent event){
        if(productWorkflowList != null && !productWorkflowList.isEmpty()){
            int i = productWorkflowList.size();
            ProductWorkflow productWorkflow = new ProductWorkflow();
            productWorkflow.setStepNo(i + 1);
            productWorkflow.setProduct(product);            
            productWorkflow.setCreateBy(JSFUtil.getUserSession().getUserName());
            productWorkflow.setCreateDate(new Date());
            productWorkflowList.add(productWorkflow);
        }
    }
    
    public void removeProductWorkflowListener(ActionEvent event){
        int stepNo = Integer.parseInt(JSFUtil.getRequestParameterMap("stepNo"));
        int i = 0;
        Collection<ProductWorkflow> list = new ArrayList<ProductWorkflow>();
        for(ProductWorkflow p : productWorkflowList){
            if(stepNo != p.getStepNo()){
                i++;
                p.setStepNo(i);
                list.add(p);
            }
        }
        productWorkflowList = list;
    }

    public boolean isSavePermitted() {
  	if (mode.equals("add")) {
           return SecurityUtil.isPermitted("admin:productinformation:add");
       } else {
 	   return SecurityUtil.isPermitted("admin:productinformation:edit"); 
       }
    }
    
    private void initBrand(){
        if(product.getProductCategory() != null){
            if(product.getProductCategory().getCategoryType().equals("motor")){
                brandList = brandDAO.findBrandEntities();
                modelTypeList = modelTypeDAO.findModelTypeEntities();
            }
        }
        if(product.getModel() != null){
            findModelList(product.getModel().getBrand().getId());
        }
    
    }
    
    private void initBank(){
        bankList = bankDAO.findBankEntities();
    }
    
    private void initSequenceNo(){
        sequenceNoList = sequenceNoDAO.findSequenceNoEntities();
    }
    
    public void addTag() {
        String tagType = JSFUtil.getRequestParameterMap("type");
        if(tagType.equals("description")) {
            tag = "description";
        } else if(tagType.equals("confirmation")){
            tag = "confirmation";
        }
        tagFieldList = getTagFieldDAO().findTagFieldByTagType(tag);
        FacesContext.getCurrentInstance().renderResponse();
    }
    
    public String saveAction() {
        message = "";
        if(checkCode(product)) {
            // IF EDIT FAMILY PRODUCT > RESET CONFIG AND CLEAR TABLE product_plan_relation 
            boolean clearProductRelation = false;
            if(product.getFamilyProduct()) {             
                if(product.getFamilyProductType() != null && product.getFamilyProductType().intValue() != familyType.intValue()) {
                    clearProductRelation = true;
                }
                if(product.getFamilyProductLogic() != null && !product.getFamilyProductLogic().equals((familyLogic))) {
                    clearProductRelation = true;
                }
                if(familyType == 1) {
                    familyLogic = null;
                }
                product.setFamilyProductType(familyType);
                product.setFamilyProductLogic(familyLogic);        
            } else {
                product.setFamilyProductType(null);
                product.setFamilyProductLogic(null);     
                clearProductRelation = true;
            }
            
            ProductDAO dao = getProductDAO();
            try {            
                if(!productCategory.getCategoryType().equals("motor")){
                    product.setModel(null);
                    product.setModelType(null);
                }
                product.setMonthlyFirstPayment(null);   // not use any more
                product.setQuarterlyFirstPayment(null); // not use any more
                product.setPaymentMethod(null);         // not use any more, payment mode will keep information about payment method and installment instead
                                
                if(approvalRuleId != null && approvalRuleId != 0) {
                    product.setApprovalRule(approvalRuleDAO.findApprovalRule(approvalRuleId));
                }else{
                    product.setApprovalRule(null);
                }

                if (getMode().equals("add")) {
                    product.setId(null);
                    product.setEnable(true);
                    String productPaymentMode = PaymentModeConverter.convertToPaymentModeStr( this.getActivePaymentModeOptions() ); // it's combination between payment method, payment mode, and installment number
                    product.setPaymentMode(productPaymentMode);
                    product.setCardType(this.convertCardTypeString());
                    product.setCardBank(this.convertCardBankString());
                    product.setCardTypeDebit(this.convertCardTypeDebitString());
                    product.setCardBankDebit(this.convertCardBankDebitString());
                    product.setProductPlanCoverage(showCoverage);
                    product.setDeliveryMethod(this.convertDeliveryMethodString());
                    if (this.fileUploadBean.getFiles().size() > 0) {
                        product.setProductThumbnail(this.fileUploadBean.getFiles().get(0).getName());
                    } else {
                        product.setProductThumbnail(null);
                    }
                    product.setProductWorkflowCollection(productWorkflowList);
                    dao.create(product);
                    if (this.fileUploadBean.getFiles().size() > 0) {
                        this.fileUploadBean.createDirName(product.getId().toString());
                        this.fileUploadBean.moveFile(this.fileUploadBean.getFiles().get(0),this.fileUploadBean.getFolderPath());
                    }
                    
                    if(this.selectedField.size() > 0) {   
                        for(int i=0;i<this.selectedField.size();i++) {   
                            for (Map.Entry<String, String> e : fieldList.entrySet()) {
                                String key = e.getKey();
                                String value = e.getValue();
                                if(this.selectedField.get(i).equals(value)) {   
                                    Integer declarationId = this.getDeclarationFormDAO().getDeclarationFormByName(key).getId();
                                    this.productDeclarationMappingDAO.edit1(product.getId(),declarationId);
                                }
                            }      
                        }
                    }
                } else {
                    product.setEnable(true);
                    String productPaymentMode = PaymentModeConverter.convertToPaymentModeStr( this.getActivePaymentModeOptions() ); // it's combination between payment method, payment mode, and installment number
                    product.setPaymentMode(productPaymentMode);
                    product.setCardType(this.convertCardTypeString());
                    product.setCardBank(this.convertCardBankString());
                    product.setCardTypeDebit(this.convertCardTypeDebitString());
                    product.setCardBankDebit(this.convertCardBankDebitString());
                    product.setDeliveryMethod(this.convertDeliveryMethodString());
                    product.setProductPlanCoverage(showCoverage);
                    if (this.getDeleteThumbnailString() == null) {
                        if (this.fileUploadBean.getFiles().size() > 0) {
                            this.fileUploadBean.createDirName(product.getId().toString());
                            this.fileUploadBean.moveFile(this.fileUploadBean.getFiles().get(0),this.fileUploadBean.getFolderPath());
                            product.setProductThumbnail(this.fileUploadBean.getFiles().get(0).getName());
                        }
                    } else if (this.getDeleteThumbnailString().length > 0) {
                        product.setProductThumbnail(null);
                    }
                    this.removeProductWorkflowList();
                    product.setProductWorkflowCollection(productWorkflowList);
                    dao.edit(product);
                    
                    this.productDeclarationMappingDAO.delete(product.getId());
                    if(this.selectedField.size()>0) {   
                        for(int i=0;i<this.selectedField.size();i++) {   
                            for (Map.Entry<String, String> e : fieldList.entrySet()) {
                                String key = e.getKey();
                                String value = e.getValue();
                                if(this.selectedField.get(i).equals(value)) {   
                                    Integer declarationId = this.getDeclarationFormDAO().getDeclarationFormByName(key).getId();
                                    this.productDeclarationMappingDAO.edit1(product.getId(),declarationId);
                                }
                            }
      
                        }
                    }
                }
                
                if(product.getId() != null && clearProductRelation) {
                    List<ProductPlan> productPlanList = this.getProductPlanDAO().findProductPlanEntitiesByProduct(product.getId());
//                    List<ProductPlan> productPlanList = (List) product.getProductPlanCollection();
                    for(ProductPlan plan: productPlanList) {
                        plan.setProductPlanMasterCollection(null);
                        this.getProductPlanDAO().edit(plan);
                    }
                }
                
            } catch (Exception e) {
                log.error(e);
                e.printStackTrace();
                        
                return null;
            }
            return SUCCESS;
        } else {
            message = " Code is already taken";
            return null;             
        }
    }
    
    private void removeProductWorkflowList(){
        Collection<ProductWorkflow> list = new ArrayList<ProductWorkflow>();
        
        for(ProductWorkflow old : product.getProductWorkflowCollection()){
            try{
                productWorkflowDAO.destroy(old.getId());
            }catch(Exception e){
                log.error(e);
            }
        }
        
        for(ProductWorkflow p : productWorkflowList){
            if(p.getId() != null){
                p.setId(null);
            }
            if(!p.getSentEmail()){
                p.setEmail(null);
            }
            p.setUpdateDate(new Date());
            p.setUpdateBy(JSFUtil.getUserSession().getUserName());
            list.add(p);
        }
        productWorkflowList = list;
    }
    
    public Boolean checkCode(Product product) {
        String code = product.getCode();
        Integer id=0; 
        if(product.getId() != null)
            id = product.getId();
        ProductDAO dao = getProductDAO();       
        
        Integer cnt = dao.checkProductCode(code, id);
        if(cnt == 0)
            return true;
        else
            return false;
    }
   
    public void deletePicThumbnailListener(ActionEvent event) {
        product.setProductThumbnail(null);
        FacesContext.getCurrentInstance().renderResponse();
    }
        
    public String backAction() {
        return SUCCESS;
    }

    public void findProductCategoryType(int productCategoryId) {
        //isInsuranceProduct = false;
        for (ProductCategory cat : productCategorys) {
            if (cat.getId().equals(productCategoryId)) {
               //isInsuranceProduct = true;
               if (product.getRegistrationForm()==null)
                   product.setRegistrationForm(new RegistrationForm());
               if (product.getProductSponsor()==null)
                   product.setProductSponsor(new ProductSponsor());
               return;
            }
        }
    }

    public void productCategoryChangeListener(ValueChangeEvent event) {
        Integer id = (Integer) event.getNewValue();
        findProductCategoryType(id);
        productCategory = productCategoryDAO.findProductCategory(id);
        product.setProductCategory(productCategory);
        initBrand();
        //FacesContext.getCurrentInstance().renderResponse();
    }
    
    public void brandChangeListener(ValueChangeEvent event){
        Integer brandId = (Integer) event.getNewValue();
        findModelList(brandId);
    }
    
    public void contactResultPlanChangeListener(ValueChangeEvent event) {
        if(event.getNewValue() != null && !event.getNewValue().toString().isEmpty()){
            Integer id = (Integer) event.getNewValue();
            ContactResultPlan contactResultPlan = new ContactResultPlan();
            contactResultPlan = this.getContactResultPlanDAO().findContactResultPlan(id);
            product.setContactResultPlan(contactResultPlan);
        }
    }
    
    private void findModelList(Integer brandId){
        modelList = modelDAO.findModelByBrand(brandId);
    }


    public String viewThumbnail() {
        return "viewthumbnail.xhtml?id=1&name=smile.gif";
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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
     * @return the productSponsorList
     */
    public SelectItem[] getProductSponsorList() {
        
        return productSponsorList;
    }

    /**
     * @param productSponsorList the productSponsorList to set
     */
    public void setProductSponsorList(SelectItem[] productSponsorList) {
        this.productSponsorList = productSponsorList;
    }

    /**
     * @return the productCategoryList
     */
    public SelectItem[] getProductCategoryList() {
        return productCategoryList;
    }

    
    public void setProductCategoryList() {

        ProductCategoryDAO dao = getProductCategoryDAO();
        productCategorys = dao.findProductCategoryEntities();
        productCategoryList = new SelectItem[productCategorys.size()+1];
        int pos=0;
        productCategoryList[pos++] = new SelectItem(null, "Select Product Category");
        for(ProductCategory pc : productCategorys){
            productCategoryList[pos++] = new SelectItem(pc.getId(), pc.getName()) ;
        }
    }
    
    public void setContactResultPlanList() {
        List<ContactResultPlan> list = this.getContactResultPlanDAO().findContactResultPlanExceptDefaultEntities();
        if(list == null){
            list = new ArrayList<ContactResultPlan>();
        }
        contactResultPlanList = new SelectItem[list.size()+1];
        int pos=0;
        contactResultPlanList[pos++] = new SelectItem(null, "Select Contact Result Plan");
        for(ContactResultPlan pc : list){
            contactResultPlanList[pos++] = new SelectItem(pc.getId(), pc.getName()) ;
        }
    }

    public void setProductSponsorList() {

       ProductSponsorDAO dao = getProductSponsorDAO();
        productSponsors = dao.findProductSponsorEntities();
        productSponsorList = new SelectItem[productSponsors.size()+1];
        int pos=0;
        productSponsorList[pos++] = new SelectItem(null, "Select Product Sponsor");
        for(ProductSponsor ps : productSponsors){
            productSponsorList[pos++] = new SelectItem(ps.getId(), ps.getName()) ;
        }
    }

    public void setFileTemplateList() {
        FileTemplateDAO dao = getFileTemplateDAO();
        fileTemplates = dao.findFileTemplateEntities();
        fileTemplateList = new SelectItem[fileTemplates.size()+1];
        int pos=0;
        fileTemplateList[pos++] = new SelectItem(null, "Select File Template");
        for(FileTemplate ft : fileTemplates){
            fileTemplateList[pos++] = new SelectItem(ft.getId(), ft.getName()) ;
        }
    }

    public void setRegistrationFormList() {
        RegistrationFormDAO dao = getRegistrationFormDAO();
        registrationForms = dao.findRegistrationFormEntities();
        registrationFormList = new SelectItem[registrationForms.size()+1];
        int pos=0;
        registrationFormList[pos++] = new SelectItem(null, "Select Registration Form");
        for(RegistrationForm ft : registrationForms){
            registrationFormList[pos++] = new SelectItem(ft.getId(), ft.getName()) ;
        }
    }
    /**
     * @return the formTemplateList
     */
    public SelectItem[] getFileTemplateList() {

        return fileTemplateList;
    }

    /**
     * @param formTemplateList the formTemplateList to set
     */
    public void setFileTemplateList(SelectItem[] fileTemplateList) {
        this.fileTemplateList = fileTemplateList;
    }

    /**
     * @return the registrationFormList
     */
    public SelectItem[] getRegistrationFormList() {

        return registrationFormList;
    }

    /**
     * @param registrationFormList the registrationFormList to set
     */
    public void setRegistrationFormList(SelectItem[] registrationFormList) {
        this.registrationFormList = registrationFormList;
    }

    @Deprecated
    public SelectItem[] getPaymentModeList() {
        paymentModeList = new SelectItem[4];
        paymentModeList[0] = new SelectItem("1", "Monthly");
        paymentModeList[1] = new SelectItem("2", "Quarterly");
        paymentModeList[2] = new SelectItem("3", "Half Year");
        paymentModeList[3] = new SelectItem("4", "Yearly");
        return paymentModeList;
    }

    @Deprecated
    public void setPaymentModeList(SelectItem[] paymentModeList) {
        this.paymentModeList = paymentModeList;
    }
    /**
     * @return the paymentModeString
     */
    public String[] getPaymentModeString() {
        String paymentMode = product.getPaymentMode();
        if (paymentMode==null) { paymentMode = "0"; }
        this.paymentModeString = paymentMode.split(",");
        return paymentModeString;
    }

    /**
     * @param paymentModeString the paymentModeString to set
     */
    public void setPaymentModeString(String[] paymentModeString) {
        this.paymentModeString = paymentModeString;
    }

    /**
     * @return the paymentModeList
     */
    public SelectItem[] getDeliveryMethodList() {
        List <DeliveryMethod> deliveryMethods = getDeliveryMethodDAO().findDeliveryMethodEntities();
        deliveryMethodList = new SelectItem[deliveryMethods.size()];
        int i = 0;
        for(DeliveryMethod d: deliveryMethods) {
            deliveryMethodList[i] = new SelectItem(d.getCode(),d.getName());
            i++;
        }
        return deliveryMethodList;
    }

    /**
     * @param paymentModeList the paymentModeList to set
     */
    public void setDeliveryMethodList(SelectItem[] deliveryMethodList) {
        this.deliveryMethodList = deliveryMethodList;
    }

    /**
     * @return the deliveryMethodString
     */
    public String[] getDeliveryMethodString() {        
        String deliveryMethod = product.getDeliveryMethod();
        if (deliveryMethod==null) { deliveryMethod = "0"; }
        this.deliveryMethodString = deliveryMethod.split(",");
        return deliveryMethodString;         
    }

    /**
     * @param deliveryMethodString the deliveryMethodString to set
     */
    public void setDeliveryMethodString(String[] deliveryMethodString) {
        this.deliveryMethodString = deliveryMethodString;
    }

    public String convertPaymentModeString() {
        String paymentMode = "";
        for (int i=0;i<this.paymentModeString.length;i++) {
            paymentMode += this.paymentModeString[i];
            if ((this.paymentModeString.length - 1) > i) {
                paymentMode += ",";
            }
        }
        return paymentMode;
    }
    
    @Deprecated
    public String convertPaymentMethodString() {
        /*String paymentMethod = "";
        for (int i=0;i<this.paymentMethodString.length;i++) {
            paymentMethod += this.paymentMethodString[i];
            if ((this.paymentMethodString.length - 1) > i) {
                paymentMethod += ",";
            }
        }
        return paymentMethod;
        */
        String str = "";
        if(this.creditCard){
            str += "1" + ",";
        }
        if(this.cash){
            str += "2" + ",";
        }
        if(this.bankTransfer){
            str += "3" + ",";
        } 
        if(!str.isEmpty()){
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public String convertCardTypeString() {
        String str = null;
        if ( this.cardTypeString!=null && this.cardTypeString.length>0 ){
            str = "";
            for (int i=0;i<this.cardTypeString.length;i++) {
                str += this.cardTypeString[i];
                if ((this.cardTypeString.length - 1) > i) {
                    str += ",";
                }
            }
        }
        return str;
    }

    public String convertCardTypeDebitString() {
        String str = null;
        if ( this.cardTypeDebitString!=null && this.cardTypeDebitString.length>0 ){
            str = "";
            for (int i=0;i<this.cardTypeDebitString.length;i++) {
                str += this.cardTypeDebitString[i];
                if ((this.cardTypeDebitString.length - 1) > i) {
                    str += ",";
                }
            }
        }
        return str;
    }

    public String convertCardBankString() {
        String str = null;
        if ( this.bankString!=null && this.bankString.length>0 ){
            str = "";
            for (int i=0;i<this.bankString.length;i++) {
                str += this.bankString[i];
                if ((this.bankString.length - 1) > i) {
                    str += ",";
                }
            }
        }
        return str;
    }
    

    public String convertCardBankDebitString() {
        String str = null;
        if ( this.bankDebitString!=null && this.bankDebitString.length>0 ){
            str = "";
            for (int i=0;i<this.bankDebitString.length;i++) {
                str += this.bankDebitString[i];
                if ((this.bankDebitString.length - 1) > i) {
                    str += ",";
                }
            }
        }
        return str;
    }
    public void checkBankAllListener(ValueChangeEvent event){
        String str = "";
        checkBankAll = !Boolean.parseBoolean(JSFUtil.getRequestParameterMap("checkBankAll"));
        try {
            if(checkBankAll){
                for (Bank bank : bankList) {
                    str += bank.getId() + ",";
                }
                str = str.substring(0, str.length() - 1);
            
                bankString = str.split(",");
            }else{
                bankString = null;
            }
        } catch (Exception e) {
            log.error(e);
        }
    }
    
    public void checkBankAllDebitListener(ValueChangeEvent event){
        String str = "";
        checkBankAllDebit = !Boolean.parseBoolean(JSFUtil.getRequestParameterMap("checkBankAllDebit"));
        try {
            if(checkBankAllDebit){
                for (Bank bank : bankList) {
                    str += bank.getId() + ",";
                }
                str = str.substring(0, str.length() - 1);
            
                bankDebitString = str.split(",");
            }else{
                bankDebitString = null;
            }
        } catch (Exception e) {
            log.error(e);
        }
    }
    
    public void checkBankListener(){
        if(bankString != null && bankList != null && bankString.length == bankList.size()){
            checkBankAll = true;
        }else{
            checkBankAll = false;
        }
    }
    
    public void checkBankDebitListener(){
        if(bankDebitString != null && bankList != null && bankDebitString.length == bankList.size()){
            checkBankAllDebit = true;
        }else{
            checkBankAllDebit = false;
        }
    }
    
    public String convertDeliveryMethodString() {
        String deliveryMethod = "";
        for (int i=0;i<this.deliveryMethodString.length;i++) {
            deliveryMethod += this.deliveryMethodString[i];
            if ((this.deliveryMethodString.length - 1) > i) {
                deliveryMethod += ",";
            }
        }
        return deliveryMethod;
    }

    public Boolean getIsInsuranceProduct() {
        return isInsuranceProduct;
    }

    public void setIsInsuranceProduct(Boolean isInsuranceProduct) {
        this.isInsuranceProduct = isInsuranceProduct;
    }


    /**
     * @return the productDAO
     */
    public ProductDAO getProductDAO() {
        return productDAO;
    }

    /**
     * @param productDAO the productDAO to set
     */
    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    /**
     * @return the productSponsorDAO
     */
    public ProductSponsorDAO getProductSponsorDAO() {
        return productSponsorDAO;
    }

    /**
     * @param productSponsorDAO the productSponsorDAO to set
     */
    public void setProductSponsorDAO(ProductSponsorDAO productSponsorDAO) {
        this.productSponsorDAO = productSponsorDAO;
    }

    /**
     * @return the productCategoryDAO
     */
    public ProductCategoryDAO getProductCategoryDAO() {
        return productCategoryDAO;
    }

    /**
     * @param productCategoryDAO the productCategoryDAO to set
     */
    public void setProductCategoryDAO(ProductCategoryDAO productCategoryDAO) {
        this.productCategoryDAO = productCategoryDAO;
    }

    /**
     * @return the fileTemplateDAO
     */
    public FileTemplateDAO getFileTemplateDAO() {
        return fileTemplateDAO;
    }

    /**
     * @param fileTemplateDAO the fileTemplateDAO to set
     */
    public void setFileTemplateDAO(FileTemplateDAO fileTemplateDAO) {
        this.fileTemplateDAO = fileTemplateDAO;
    }

    /**
     * @return the registrationFormDAO
     */
    public RegistrationFormDAO getRegistrationFormDAO() {
        return registrationFormDAO;
    }

    /**
     * @param registrationFormDAO the registrationFormDAO to set
     */
    public void setRegistrationFormDAO(RegistrationFormDAO registrationFormDAO) {
        this.registrationFormDAO = registrationFormDAO;
    }

    /**
     * @return the fileUploadBean
     */
    public FileUploadBean getFileUploadBean() {
        return this.fileUploadBean;
    }

    /**
     * @param fileUploadBean the fileUploadBean to set
     */
    public void setFileUploadBean(FileUploadBean fileUploadBean) {
        this.fileUploadBean = fileUploadBean;
    }

    /**
     * @return the deleteThumbnail
     */
    public SelectItem[] getDeleteThumbnail() {
        deleteThumbnail = new SelectItem[1];
        deleteThumbnail[0] = new SelectItem("D", "Delete Thumbnail");
        return deleteThumbnail;
    }

    /**
     * @param deleteThumbnail the deleteThumbnail to set
     */
    public void setDeleteThumbnail(SelectItem[] deleteThumbnail) {
        this.deleteThumbnail = deleteThumbnail;
    }

    /**
     * @return the deleteThumbnailString
     */
    public String[] getDeleteThumbnailString() {
        return deleteThumbnailString;
    }

    /**
     * @param deleteThumbnailString the deleteThumbnailString to set
     */
    public void setDeleteThumbnailString(String[] deleteThumbnailString) {
        this.deleteThumbnailString = deleteThumbnailString;
    }

    public BrandDAO getBrandDAO() {
        return brandDAO;
    }

    public void setBrandDAO(BrandDAO brandDAO) {
        this.brandDAO = brandDAO;
    }

    public List<Brand> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<Brand> brandList) {
        this.brandList = brandList;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public List<Model> getModelList() {
        return modelList;
    }

    public void setModelList(List<Model> modelList) {
        this.modelList = modelList;
    }

    public ModelDAO getModelDAO() {
        return modelDAO;
    }

    public void setModelDAO(ModelDAO modelDAO) {
        this.modelDAO = modelDAO;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public ModelType getModelType() {
        return modelType;
    }

    public void setModelType(ModelType modelType) {
        this.modelType = modelType;
    }

    public List<ModelType> getModelTypeList() {
        return modelTypeList;
    }

    public void setModelTypeList(List<ModelType> modelTypeList) {
        this.modelTypeList = modelTypeList;
    }

    public ModelTypeDAO getModelTypeDAO() {
        return modelTypeDAO;
    }

    public void setModelTypeDAO(ModelTypeDAO modelTypeDAO) {
        this.modelTypeDAO = modelTypeDAO;
    }

    public SelectItem[] getModelYearList() {
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("yyyy",Locale.US);
        int year = Integer.parseInt(simpleDateformat.format(new Date()));
        modelYearList = new SelectItem[20];
        for(int i = 0 ; i < 20 ; i++){
            Integer y = year - i;
            modelYearList[i] = new SelectItem(y, y.toString());
        }
        return modelYearList;
    }

    public void setModelYearList(SelectItem[] modelYearList) {
        this.modelYearList = modelYearList;
    }

    public List<ApprovalRule> getApprovalRuleList() {
        return approvalRuleList;
    }

    public void setApprovalRuleList(List<ApprovalRule> approvalRuleList) {
        this.approvalRuleList = approvalRuleList;
    }

    public ApprovalRuleDAO getApprovalRuleDAO() {
        return approvalRuleDAO;
    }

    public void setApprovalRuleDAO(ApprovalRuleDAO approvalRuleDAO) {
        this.approvalRuleDAO = approvalRuleDAO;
    }
    // Tag Field
    
    public TagFieldDAO getTagFieldDAO() {
        return tagFieldDAO;
    }

    public void setTagFieldDAO(TagFieldDAO tagFieldDAO) {
        this.tagFieldDAO = tagFieldDAO;
    }
    
    public List<TagField> getTagFieldList() {
        return tagFieldList;
    }
    
    public String getTag() {
        return tag;
    }
        
    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMonthlyFirstPayment() {
        return monthlyFirstPayment;
    }

    public void setMonthlyFirstPayment(String monthlyFirstPayment) {
        this.monthlyFirstPayment = monthlyFirstPayment;
    }

    public String getQuarterlyFirstPayment() {
        return quarterlyFirstPayment;
    }

    public void setQuarterlyFirstPayment(String quarterlyFirstPayment) {
        this.quarterlyFirstPayment = quarterlyFirstPayment;
    }
    
    public SelectItem[] getModelFromYearList() {
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("yyyy",Locale.US);
        int year = Integer.parseInt(simpleDateformat.format(new Date()));
        modelFromYearList = new SelectItem[20];
        for(int i = 0 ; i < 20 ; i++){
            Integer y = year - i;
            modelFromYearList[i] = new SelectItem(y, y.toString());
        }
        return modelFromYearList;
    }

    public void setModelFromYearList(SelectItem[] modelFromYearList) {
        this.modelFromYearList = modelFromYearList;
    }

    public SelectItem[] getModelToYearList() {
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("yyyy");
        int yearNow = Integer.parseInt(simpleDateformat.format(new Date()));        
        int yearFrom = this.product.getModelFromYear() != null ? this.product.getModelFromYear() : 0;
        int no = (yearNow - yearFrom) + 1;
        if(yearFrom != 0){
            modelToYearList = new SelectItem[no];
            for(int i = 0; i < no; i++){
                Integer y = new Integer(yearNow--);
                modelToYearList[i] = new SelectItem(y, y.toString());
            }
        }
        return modelToYearList;
    }

    public void setModelToYearList(SelectItem[] modelToYearList) {
        this.modelToYearList = modelToYearList;
    }
    
    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public DeliveryMethodDAO getDeliveryMethodDAO() {
        return deliveryMethodDAO;
    }

    public void setDeliveryMethodDAO(DeliveryMethodDAO deliveryMethodDAO) {
        this.deliveryMethodDAO = deliveryMethodDAO;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SelectItem[] getPaymentMethodList() {
        paymentMethodList = new SelectItem[3];
        paymentMethodList[0] = new SelectItem("1", "Credit Card");
        paymentMethodList[1] = new SelectItem("2", "Cash");
        paymentMethodList[2] = new SelectItem("3", "Bank Transfer");
        return paymentMethodList;
}

    public void setPaymentMethodList(SelectItem[] paymentMethodList) {
        this.paymentMethodList = paymentMethodList;
    }

    public String[] getPaymentMethodString() {
        String paymentMethod = product.getPaymentMethod();
        if (paymentMethod==null) { paymentMethod = "0"; }
        this.paymentMethodString = paymentMethod.split(",");
        return paymentMethodString;
    }

    public void setPaymentMethodString(String[] paymentMethodString) {
        this.paymentMethodString = paymentMethodString;
    }

    public Boolean getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(Boolean creditCard) {
        this.creditCard = creditCard;
    }

    public Boolean getCash() {
        return cash;
    }

    public void setCash(Boolean cash) {
        this.cash = cash;
    }

    public Boolean getBankTransfer() {
        return bankTransfer;
    }

    public void setBankTransfer(Boolean bankTransfer) {
        this.bankTransfer = bankTransfer;
    }

    public SelectItem[] getCardTypeList() {
        
        cardTypeList = new SelectItem[2];
        cardTypeList[0] = new SelectItem("1", "VISA");
        cardTypeList[1] = new SelectItem("2", "MASTER");
        return cardTypeList;
    }

    public void setCardTypeList(SelectItem[] cardTypeList) {
        this.cardTypeList = cardTypeList;
    }

    public String[] getCardTypeString() {
        return cardTypeString;
    }

    public void setCardTypeString(String[] cardTypeString) {
        this.cardTypeString = cardTypeString;
    }

    public List<Bank> getBankList() {
        return bankList;
    }

    public void setBankList(List<Bank> bankList) {
        this.bankList = bankList;
    }

    public String[] getBankString() {
        return bankString;
    }

    public void setBankString(String[] bankString) {
        this.bankString = bankString;
    }

    public BankDAO getBankDAO() {
        return bankDAO;
    }

    public void setBankDAO(BankDAO bankDAO) {
        this.bankDAO = bankDAO;
    }

    public Boolean getCheckBankAll() {
        return checkBankAll;
    }

    public void setCheckBankAll(Boolean checkBankAll) {
        this.checkBankAll = checkBankAll;
    }

    public SequenceNoDAO getSequenceNoDAO() {
        return sequenceNoDAO;
    }

    public void setSequenceNoDAO(SequenceNoDAO sequenceNoDAO) {
        this.sequenceNoDAO = sequenceNoDAO;
    }

    public List<SequenceNo> getSequenceNoList() {
        return sequenceNoList;
    }

    public void setSequenceNoList(List<SequenceNo> sequenceNoList) {
        this.sequenceNoList = sequenceNoList;
    }

    public Collection<ProductWorkflow> getProductWorkflowList() {
        return productWorkflowList;
    }

    public void setProductWorkflowList(Collection<ProductWorkflow> productWorkflowList) {
        this.productWorkflowList = productWorkflowList;
    }

    public Map<String, String> getStepList() {
        return stepList;
    }

    public void setStepList(Map<String, String> stepList) {
        this.stepList = stepList;
    }

    public ProductWorkflowDAO getProductWorkflowDAO() {
        return productWorkflowDAO;
    }

    public void setProductWorkflowDAO(ProductWorkflowDAO productWorkflowDAO) {
        this.productWorkflowDAO = productWorkflowDAO;
    }

    public Integer getApprovalRuleId() {
        return approvalRuleId;
    }

    public void setApprovalRuleId(Integer approvalRuleId) {
        this.approvalRuleId = approvalRuleId;
    }

    public PaymentMethodDAO getPaymentMethodDAO() {
        return paymentMethodDAO;
    }

    public void setPaymentMethodDAO(PaymentMethodDAO paymentMethodDAO) {
        this.paymentMethodDAO = paymentMethodDAO;
    }
    

    public FirstInstallmentOptions.InstallmentNumber[] getMonthlyInstallmentOptions() {
        return FirstInstallmentOptions.MONTHLY;
    }

    public FirstInstallmentOptions.InstallmentNumber[] getQuarterlyInstallmentOptions() {
        return FirstInstallmentOptions.QUARTERLY;
    }
    
    public FirstInstallmentOptions.InstallmentNumber[] getHalfYearInstallmentOptions() {
        return FirstInstallmentOptions.HALFYEAR;
    }

    public FirstInstallmentOptions.InstallmentNumber[] getYearlyInstallmentOptions() {
        return FirstInstallmentOptions.YEARLY;
    }
    public List<KeyOptionDTO> getPaymentMethodOptions() {
        return paymentMethodOptions;
    }

    public void setPaymentMethodOptions(List<KeyOptionDTO> paymentMethodOptions) {
        this.paymentMethodOptions = paymentMethodOptions;
    }

    public List<PaymentMethodStringCastor> getPaymentModeOptions() {
        return paymentModeOptions;
    }

    public void setPaymentModeOptions(List<PaymentMethodStringCastor> paymentModeOptions) {
        this.paymentModeOptions = paymentModeOptions;
    }

   
    public List<PaymentMethodStringCastor> getActivePaymentModeOptions() {
        List<PaymentMethodStringCastor> activeOptions = new  ArrayList<PaymentMethodStringCastor>();
        if ( this.paymentMethodOptions !=null ){
            for(int i=0; i< this.paymentMethodOptions.size(); i++ ){
                KeyOptionDTO o = this.paymentMethodOptions.get(i);
                if (o.isSelected()) activeOptions.add( this.paymentModeOptions.get(i) );
            }
        }
        return activeOptions;
    }

    public void doPaymentMethodSelected(AjaxBehaviorEvent event){
       String spmtId  = JSFUtil.getRequestParameterMap("PAYMENTMETHOD_ID_CHECKED");
       
       Integer pmtId = null;
       try{ pmtId = Integer.parseInt(spmtId); }catch(NumberFormatException nfe){ return; /*not do anything*/ }
       
       for(int i=0; i< this.paymentMethodOptions.size(); i++ ){
           KeyOptionDTO o = this.paymentMethodOptions.get(i);
            // in case deselect payment method -> clear the object value
           if ( o.getIkey().intValue() == pmtId && !o.isSelected() ){
               PaymentMethodStringCastor pmsc = this.paymentModeOptions.get(i);
               pmsc.clearInstallmentNo();
               if ( o.isBvalue() ){ // clear credit card value
                   this.setCardTypeString(new String[]{}); // clear card type
                   this.setBankString(new String[]{}); // clear bank 
                   this.setCheckBankAll(false); // clear check bank all
               }
               if ( o.isDvalue()){ // clear DEBIT card value
                   this.setCardTypeDebitString(new String[]{}); // clear card type
                   this.setBankDebitString(new String[]{}); // clear bank 
                   this.setCheckBankAllDebit(false); // clear check bank all
                }
            }
       }
       
    }
    //old value 1=credit card, 2=cash, 3=bankTransfer
    protected void loadForm(){
        this.paymentMethodOptions =  new ArrayList<KeyOptionDTO>();
        this.paymentModeOptions = new ArrayList<PaymentMethodStringCastor>();
        try{
            List<PaymentMethod> pmList = paymentMethodDAO.findPaymentMethod(1, 1);
            boolean onlyOneCreditCard = false;
            boolean onlyOneDebitCard = false;
            for (PaymentMethod o : pmList){
                 this.paymentMethodOptions.add(new KeyOptionDTO(o.getId(), o.getName(), null, (!onlyOneCreditCard)?o.isCreditcard():false, (!onlyOneDebitCard)?o.isDebitcard():false ) );
                 this.paymentModeOptions.add(new PaymentMethodStringCastor(o.getId(),o.getName()));
                 if (o.isCreditcard()){ onlyOneCreditCard=true;}// just the first credit card that will be present the credit card detail
                 if (o.isDebitcard()){ onlyOneDebitCard=true;}
            }
            // initial data
             if ( product != null && product.getPaymentMode() != null && !product.getPaymentMode().isEmpty()  ) {
                  // set up payment method and payment mode
                  List<PaymentMethodStringCastor> data = new ArrayList<PaymentMethodStringCastor> ();
                  try{ data=PaymentModeConverter.convertToPaymentModeList(product.getPaymentMode()); } catch(Exception e){ log.error(e); }
                  for (PaymentMethodStringCastor o : data){
                      for(int i=0;i<this.paymentMethodOptions.size();i++){
                          KeyOptionDTO pmOption = this.paymentMethodOptions.get(i);
                          if( o.getPaymentMethodId().intValue() == pmOption.getIkey().intValue() ){
                              pmOption.setSelected(true);
                              PaymentMethodStringCastor pmsc = this.paymentModeOptions.get(i);
                              pmsc.setNoPayPeriodOfHalfYearMode(o.getNoPayPeriodOfHalfYearMode());
                              pmsc.setNoPayPeriodOfMonthlyMode(o.getNoPayPeriodOfMonthlyMode());
                              pmsc.setNoPayPeriodOfQuarterlyMode(o.getNoPayPeriodOfQuarterlyMode());
                              pmsc.setNoPayPeriodOfYearlyMode(o.getNoPayPeriodOfYearlyMode());
                              break;
                          }    
                      }
                  }
                // set up card type 
                String str = product.getCardType();
                this.cardTypeString = (str==null || str.isEmpty() )?new String[0]:str.split(",");
                  
                // set up card bank list value
                str = product.getCardBank();
                this.bankString = (str==null || str.isEmpty() )?new String[0]:str.split(",");
                
                if (bankString != null && bankList != null && bankString.length == bankList.size()) {
                    checkBankAll = true;
                } 
                
                // set up card type 
                String strDebit = product.getCardTypeDebit();
                this.cardTypeDebitString = (strDebit==null || strDebit.isEmpty() )?new String[0]:strDebit.split(",");
                  
                // set up card bank list value
                strDebit = product.getCardBankDebit();
                this.bankDebitString = (strDebit==null || strDebit.isEmpty() )?new String[0]:strDebit.split(",");
                
                if (bankDebitString != null && bankList != null && bankDebitString.length == bankList.size()) {
                    checkBankAllDebit = true;
                }
             }
        }catch(Exception e){
            log.error("loadForm : "+e.getMessage());
            e.printStackTrace();
        }
        
    }

    public ContactResultPlanDAO getContactResultPlanDAO() {
        return contactResultPlanDAO;
    }

    public void setContactResultPlanDAO(ContactResultPlanDAO contactResultPlanDAO) {
        this.contactResultPlanDAO = contactResultPlanDAO;
    }

    public SelectItem[] getContactResultPlanList() {
        return contactResultPlanList;
    }

    public void setContactResultPlanList(SelectItem[] contactResultPlanList) {
        this.contactResultPlanList = contactResultPlanList;
    }

    public Integer getContactResultPlanId() {
        return contactResultPlanId;
    }

    public void setContactResultPlanId(Integer contactResultPlanId) {
        this.contactResultPlanId = contactResultPlanId;
    }
    
    public ProductDeclarationMappingDAO getProductDeclarationMappingDAO() {
        return productDeclarationMappingDAO;
    }

    public void setProductDeclarationMappingDAO(ProductDeclarationMappingDAO productDeclarationMappingDAO) {
        this.productDeclarationMappingDAO = productDeclarationMappingDAO;
    }

    public ProductDeclarationMapping getProductDeclarationMapping() {
        return productDeclarationMapping;
    }

    public void setProductDeclarationMapping(ProductDeclarationMapping ProductDeclarationMapping) {
        this.productDeclarationMapping = ProductDeclarationMapping;
    }

    public ProductDeclarationMappingPK getProductDeclarationMappingPK() {
        return productDeclarationMappingPK;
    }

    public void setProductDeclarationMappingPK(ProductDeclarationMappingPK productDeclarationMappingPK) {
        this.productDeclarationMappingPK = productDeclarationMappingPK;
    }
    
        public Map<String, String> getFieldList() {
        return fieldList;
    }

    public void setFieldList(Map<String, String> fieldList) {
        this.fieldList = fieldList;
    }

    public List<String> getSelectedField() {
        return selectedField;
    }

    public void setSelectedField(List<String> selectedField) {
        this.selectedField = selectedField;
    }

    public DeclarationFormDAO getDeclarationFormDAO() {
        return declarationFormDAO;
    }

    public void setDeclarationFormDAO(DeclarationFormDAO declarationFormDAO) {
        this.declarationFormDAO = declarationFormDAO;
    }
    
    
    public String[] getBankDebitString() {
        return bankDebitString;
    }

    public void setBankDebitString(String[] bankDebitString) {
        this.bankDebitString = bankDebitString;
    }

    public Boolean getCheckBankAllDebit() {
        return checkBankAllDebit;
    }

    public void setCheckBankAllDebit(Boolean checkBankAllDebit) {
        this.checkBankAllDebit = checkBankAllDebit;
    }

    public String[] getCardTypeDebitString() {
        return cardTypeDebitString;
    }

    public void setCardTypeDebitString(String[] cardTypeDebitString) {
        this.cardTypeDebitString = cardTypeDebitString;
    }

    public Integer getFamilyType() {
        return familyType;
    }

    public void setFamilyType(Integer familyType) {
        this.familyType = familyType;
    }

    public String getFamilyLogic() {
        return familyLogic;
    }

    public void setFamilyLogic(String familyLogic) {
        this.familyLogic = familyLogic;
    }

    public ProductPlanDAO getProductPlanDAO() {
        return productPlanDAO;
    }

    public void setProductPlanDAO(ProductPlanDAO productPlanDAO) {
        this.productPlanDAO = productPlanDAO;
    }

    public Integer getShowCoverage() {
        return showCoverage;
    }

    public void setShowCoverage(Integer showCoverage) {
        this.showCoverage = showCoverage;
    }
}
